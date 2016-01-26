/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.jdk5;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.charset.CCharset;
import com.helger.commons.collection.iterate.FilterIterator;
import com.helger.commons.hierarchy.visit.DefaultHierarchyVisitorCallback;
import com.helger.commons.hierarchy.visit.EHierarchyVisitorReturn;
import com.helger.commons.io.file.FileOperationManager;
import com.helger.commons.io.file.LoggingFileOperationCallback;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.filter.FileFilterFilenameMatchNoRegEx;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.microdom.IMicroDocument;
import com.helger.commons.microdom.IMicroElement;
import com.helger.commons.microdom.IMicroNode;
import com.helger.commons.microdom.serialize.MicroReader;
import com.helger.commons.microdom.serialize.MicroWriter;
import com.helger.commons.microdom.util.MicroVisitor;
import com.helger.commons.state.ETriState;
import com.helger.commons.string.StringHelper;
import com.helger.commons.system.ENewLineMode;
import com.helger.commons.xml.namespace.MapBasedNamespaceContext;
import com.helger.commons.xml.serialize.write.XMLWriterSettings;

/**
 * Main logic for creating JDK5 versions of a "ph"-project by handling certain
 * special comments in Java files.
 *
 * @author Philip Helger
 */
public final class CreateJava5Version
{
  private static final String ARTIFACT_SUFFIX_JDK15 = "-jdk5";
  private static final String DIRNAME_TARGET = "target";
  private static final String JDK15_README_CREATED_1ST = "readme-created.1st";
  private static final Logger s_aLogger = LoggerFactory.getLogger (CreateJava5Version.class);
  private static final FileOperationManager s_aFOM = new FileOperationManager (new LoggingFileOperationCallback ());

  @PresentForCodeCoverage
  private static final CreateJava5Version s_aInstance = new CreateJava5Version ();

  private CreateJava5Version ()
  {}

  @Nonnull
  @Nonempty
  private static String _getArtifactName15 (@Nonnull final String sName16)
  {
    return sName16 + ARTIFACT_SUFFIX_JDK15;
  }

  private static void _updatePOM (@Nonnull final File aSrcFile,
                                  @Nonnull final File aDstFile,
                                  @Nonnull final String sSrcComponentName,
                                  @Nonnull final String sDstComponentName)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    // Global artifact ID
    {
      final IMicroElement eArtifact = eRoot.getFirstChildElement ("artifactId");
      if (!sSrcComponentName.equals (eArtifact.getTextContent ()))
        throw new IllegalStateException ("Illegal artifactId");
      eArtifact.removeAllChildren ();
      eArtifact.appendText (sDstComponentName);
    }

    // Global artifact name
    {
      final IMicroElement eName = eRoot.getFirstChildElement ("name");
      if (!sSrcComponentName.equals (eName.getTextContent ()))
        throw new IllegalStateException ("Illegal name");
      eName.removeAllChildren ();
      eName.appendText (sDstComponentName);
    }

    // Global doc URL
    {
      final IMicroElement eUrl = eRoot.getFirstChildElement ("url");
      if (eUrl != null)
      {
        final String sOld = eUrl.getTextContent ();
        eUrl.removeAllChildren ();
        eUrl.appendText (sOld.replace (sSrcComponentName, sDstComponentName));
      }
    }

    // SCM settings
    {
      final IMicroElement eScm = eRoot.getFirstChildElement ("scm");
      if (eScm != null)
        for (final IMicroElement eChild : eScm.getAllChildElements ())
        {
          final String sOld = eChild.getTextContent ();
          if (sOld.endsWith (sSrcComponentName))
          {
            eChild.removeAllChildren ();
            eChild.appendText (sOld.substring (0, sOld.length () - sSrcComponentName.length ()) + sDstComponentName);
          }
        }
    }

    // Set compiler plugin
    {
      IMicroElement eBuild = eRoot.getFirstChildElement ("build");
      if (eBuild == null)
        eBuild = eRoot.appendElement (eRoot.getNamespaceURI (), "build");

      IMicroElement ePlugins = eBuild.getFirstChildElement ("plugins");
      if (ePlugins == null)
        ePlugins = eBuild.appendElement (eRoot.getNamespaceURI (), "plugins");

      final IMicroElement ePlugin = ePlugins.appendElement (eRoot.getNamespaceURI (), "plugin");
      ePlugin.appendElement (eRoot.getNamespaceURI (), "groupId").appendText ("org.apache.maven.plugins");
      ePlugin.appendElement (eRoot.getNamespaceURI (), "artifactId").appendText ("maven-compiler-plugin");
      final IMicroElement eConfiguration = ePlugin.appendElement (eRoot.getNamespaceURI (), "configuration");
      eConfiguration.appendElement (eRoot.getNamespaceURI (), "source").appendText ("1.5");
      eConfiguration.appendElement (eRoot.getNamespaceURI (), DIRNAME_TARGET).appendText ("1.5");
    }

    // Modify all dependencies
    {
      MicroVisitor.visit (aDoc, new DefaultHierarchyVisitorCallback <IMicroNode> ()
      {
        @Override
        public EHierarchyVisitorReturn onItemBeforeChildren (final IMicroNode aItem)
        {
          if (aItem.isElement ())
          {
            final IMicroElement eItem = (IMicroElement) aItem;
            if (eItem.getLocalName ().equals ("groupId"))
            {
              final String sGroupID = eItem.getTextContent ();
              // Handle all "com.helger" group artifacts
              if (sGroupID.equals ("com.helger"))
              {
                final IMicroElement eArtifactId = ((IMicroElement) eItem.getParent ()).getFirstChildElement ("artifactId");
                final String sArtifactId = eArtifactId.getTextContent ();
                if ("ph-jdk5".equals (sArtifactId))
                {
                  // Remove the dependency to this project
                  final IMicroElement eDep = (IMicroElement) eItem.getParent ();
                  eDep.getParent ().removeChild (eDep);
                }
                else
                  if (!"parent-pom".equals (sArtifactId))
                  {
                    // Change the artifact name
                    eArtifactId.removeAllChildren ();
                    eArtifactId.appendText (_getArtifactName15 (sArtifactId));
                  }
              }
            }
          }
          return EHierarchyVisitorReturn.CONTINUE;
        }
      });

      // Add JAXB-API dependency as it is in the runtime in 1.6 but not for 1.5
      final IMicroElement eDependencies = eRoot.getFirstChildElement ("dependencies");
      eDependencies.appendComment ("Explicitly required for Java 1.5!");
      final IMicroElement eDep = eDependencies.appendElement (eRoot.getNamespaceURI (), "dependency");
      eDep.appendElement (eRoot.getNamespaceURI (), "groupId").appendText ("javax.xml.bind");
      eDep.appendElement (eRoot.getNamespaceURI (), "artifactId").appendText ("jaxb-api");
      eDep.appendElement (eRoot.getNamespaceURI (), "version").appendText ("2.1");
    }

    // Write
    final XMLWriterSettings aXWS = new XMLWriterSettings ().setNamespaceContext (new MapBasedNamespaceContext ().addMapping ("xsi",
                                                                                                                             "http://www.w3.org/2001/XMLSchema-instance"))
                                                           .setPutNamespaceContextPrefixesInRoot (true);
    MicroWriter.writeToFile (aDoc, aDstFile, aXWS);

    s_aLogger.info ("Modified pom.xml");
  }

  private static void _updateEclipseProject (@Nonnull final File aSrcFile,
                                             @Nonnull final File aDstFile,
                                             @Nonnull final String sSrcComponentName,
                                             @Nonnull final String sDstComponentName)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    final IMicroElement eName = eRoot.getFirstChildElement ("name");
    if (!sSrcComponentName.equals (eName.getTextContent ()))
      throw new IllegalStateException ("Illegal name");
    eName.removeAllChildren ();
    eName.appendText (sDstComponentName);

    // Write
    MicroWriter.writeToFile (aDoc, aDstFile);

    s_aLogger.info ("Modified Eclipse project file");
  }

  private static void _updateEclipseClasspath (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    final IMicroElement eRoot = aDoc.getDocumentElement ();

    for (final IMicroElement eEntry : eRoot.getAllChildElements ("classpathentry"))
      if ("con".equals (eEntry.getAttributeValue ("kind")))
      {
        final String sPath = eEntry.getAttributeValue ("path");
        final int nIndex = sPath.indexOf ("JavaSE-");
        if (nIndex >= 0)
        {
          // Main modification
          eEntry.setAttribute ("path", sPath.replaceAll ("JavaSE-1\\.[0-9]", "J2SE-1.5"));
        }
      }

    // Write
    MicroWriter.writeToFile (aDoc, aDstFile);

    s_aLogger.info ("Modified Eclipse classpath file");
  }

  private static void _updateEclipsePrefs (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    String sContent = SimpleFileIO.getFileAsString (aSrcFile, CCharset.CHARSET_UTF_8_OBJ);
    sContent = sContent.replace ("=1.6", "=1.5");
    SimpleFileIO.writeFile (aDstFile, sContent, CCharset.CHARSET_UTF_8_OBJ);

    s_aLogger.info ("Modified Eclipse preferences file");
  }

  private static void _updateEclipseFacets (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aSrcFile);
    for (final IMicroElement eInstalled : aDoc.getDocumentElement ().getAllChildElements ("installed"))
      if ("java".equals (eInstalled.getAttributeValue ("facet")))
        eInstalled.setAttribute ("version", "1.5");
    MicroWriter.writeToFile (aDoc, aDstFile);

    s_aLogger.info ("Modified Eclipse facets file");
  }

  private static void _processJavaFile (@Nonnull final File aSrcFile, @Nonnull final File aDstFile)
  {
    boolean bSkipFile = false;
    boolean bModifiedFile = false;
    ETriState eJDK5Mode = ETriState.UNDEFINED;
    final List <String> aLines = SimpleFileIO.getAllFileLines (aSrcFile, CCharset.CHARSET_UTF_8_OBJ);
    for (int i = 0; i < aLines.size (); ++i)
    {
      final String sLine = aLines.get (i);

      String sTrimmedLine = sLine.trim ();
      if (sTrimmedLine.startsWith ("//"))
      {
        sTrimmedLine = sTrimmedLine.substring (2).trim ();
        if (sTrimmedLine.startsWith ("SKIPJDK5"))
        {
          // Skip the whole file - no need to continue parsing the file
          bSkipFile = true;
          break;
        }
        if (sTrimmedLine.startsWith ("IFJDK5"))
        {
          // The following lines are for JDK5
          if (eJDK5Mode.isDefined ())
            throw new IllegalStateException ("Cannot enter JDK5 mode!");
          eJDK5Mode = ETriState.TRUE;
        }
        else
          if (sTrimmedLine.startsWith ("ELSE"))
          {
            // Switch from JDK5 to default mode
            if (!eJDK5Mode.isTrue ())
              throw new IllegalStateException ("Not in JDK5 mode!");
            eJDK5Mode = ETriState.FALSE;
          }
          else
            if (sTrimmedLine.startsWith ("ENDIF"))
            {
              // We're back to regular mode which is valid for all versions
              // Note: this is different from "isTrue"!!!
              if (!eJDK5Mode.isFalse ())
                throw new IllegalStateException ("Not in default mode!");
              eJDK5Mode = ETriState.UNDEFINED;
            }
            else
              if (eJDK5Mode.isTrue ())
              {
                // Remove the comment from the line
                aLines.set (i, sLine.replace ("//", ""));
                bModifiedFile = true;
              }
      }
      else
        if (eJDK5Mode.isFalse ())
        {
          // Comment this line, as we're in JDK5 mode
          aLines.set (i, "//" + sLine);
          bModifiedFile = true;
        }
        else
        {
          // All the hacks start here
          if (sLine.equals ("import java.util.ServiceLoader;"))
            aLines.set (i, "// " + sLine);
        }
    }

    // The following should never happen!
    if (eJDK5Mode.isDefined ())
      throw new IllegalStateException ("JDK5 comments are inconsistent in file " + aSrcFile.getAbsolutePath ());

    if (bSkipFile)
      s_aLogger.info ("Skipping file " + aSrcFile.getName ());
    else
    {
      if (bModifiedFile)
        s_aLogger.info ("Modified file " + aSrcFile.getName ());

      final String sContent = StringHelper.getImploded (ENewLineMode.DEFAULT.getText (), aLines) +
                              ENewLineMode.DEFAULT.getText ();
      SimpleFileIO.writeFile (aDstFile, sContent, CCharset.CHARSET_UTF_8_OBJ);
    }
  }

  private static void _checkFor15Deletions (@Nonnull final File aBaseDir15, @Nonnull final File aBaseDir16)
  {
    final String sBasePath15 = aBaseDir15.getAbsolutePath ();
    final FileFilterFilenameMatchNoRegEx aFF = new FileFilterFilenameMatchNoRegEx (DIRNAME_TARGET);
    for (final File aFile15 : new FilterIterator <File> (new FileSystemRecursiveIterator (aBaseDir15, aFF), aFF))
    {
      final String sName15 = aFile15.getName ();
      final String sAbsPath15 = aFile15.getAbsolutePath ();
      final String sRelPath = sAbsPath15.substring (sBasePath15.length () + 1);
      final File aFile16 = new File (aBaseDir16, sRelPath);

      if (aFile15.isFile ())
      {
        // Ignore special files
        if (sName15.equals (JDK15_README_CREATED_1ST) || sName15.endsWith (ARTIFACT_SUFFIX_JDK15 + ".iml"))
          continue;

        if (!aFile16.exists ())
        {
          s_aLogger.info (" DELETE FILE " + sAbsPath15);
          s_aFOM.deleteFile (aFile15);
        }
      }
      else
        if (aFile15.isDirectory ())
        {
          if (!aFile16.exists ())
          {
            s_aLogger.info (" DELETE DIRECTORY " + sAbsPath15);
            s_aFOM.deleteDirRecursive (aFile15);
          }
        }
    }
  }

  public static void createJDK5Version (@Nonnull final File aSrcBaseDir16) throws IOException
  {
    if (aSrcBaseDir16 == null)
      throw new NullPointerException ("Null directory");
    final File aBaseDir16 = aSrcBaseDir16.getAbsoluteFile ();
    if (!aBaseDir16.isDirectory ())
      throw new IllegalArgumentException ("Illegal base directory provided: " + aBaseDir16);

    final String sComponentName16 = aBaseDir16.getName ();
    final String sComponentName15 = _getArtifactName15 (sComponentName16);

    final File aBaseDir15 = new File (aBaseDir16, "../" + sComponentName15).getCanonicalFile ();
    s_aLogger.info ("From " + aBaseDir16.toString ());
    s_aLogger.info ("To " + aBaseDir15.toString ());
    final String sBasePath16 = aBaseDir16.getAbsolutePath ();
    final FileFilterFilenameMatchNoRegEx aFF = new FileFilterFilenameMatchNoRegEx (DIRNAME_TARGET);
    for (final File aFile16 : new FilterIterator <File> (new FileSystemRecursiveIterator (aBaseDir16, aFF), aFF))
      if (aFile16.isFile ())
      {
        final String sName16 = aFile16.getName ();
        final String sAbsPath16 = aFile16.getAbsolutePath ();
        final String sRelPath = sAbsPath16.substring (sBasePath16.length () + 1);
        final File aFile15 = new File (aBaseDir15, sRelPath);

        if (sName16.equals ("pom.xml"))
          _updatePOM (aFile16, aFile15, sComponentName16, sComponentName15);
        else
          if (sName16.equals (".project"))
            _updateEclipseProject (aFile16, aFile15, sComponentName16, sComponentName15);
          else
            if (sName16.equals (".classpath"))
              _updateEclipseClasspath (aFile16, aFile15);
            else
              if (sName16.equals ("org.eclipse.jdt.core.prefs"))
                _updateEclipsePrefs (aFile16, aFile15);
              else
                if (sName16.equals ("org.eclipse.wst.common.project.facet.core.xml"))
                  _updateEclipseFacets (aFile16, aFile15);
                else
                  if (sName16.endsWith (".java"))
                    _processJavaFile (aFile16, aFile15);
                  else
                    if (sName16.equals (sComponentName16 + ".iml"))
                    {
                      // Skip the file - Idea project file
                    }
                    else
                    {
                      s_aFOM.deleteFileIfExisting (aFile15);
                      s_aFOM.copyFile (aFile16, aFile15);
                    }
      }

    // Add a simple text file as information!
    SimpleFileIO.writeFile (new File (aBaseDir15, JDK15_README_CREATED_1ST),
                            "The content of this project is 100% automatically generated.\n"
                                + "Do not alter anything in here, as it will be overwritten next time the project is generated!!!",
                            CCharset.CHARSET_UTF_8_OBJ);
    s_aLogger.info ("Done creating JDK5 version of " + sComponentName16);

    _checkFor15Deletions (aBaseDir15, aBaseDir16);
  }
}
