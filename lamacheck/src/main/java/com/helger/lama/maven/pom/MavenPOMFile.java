/**
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.helger.lama.maven.pom;

import java.io.File;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.StringParser;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.system.SystemProperties;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.EMavenScope;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.lama.maven.repo.EMavenRepositoryLayout;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.xml.microdom.EMicroNodeType;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroContainer;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.microdom.util.MicroHelper;

/**
 * Represents a Maven 2 POM file including the location on the disk.
 *
 * @author Philip Helger
 */
public final class MavenPOMFile
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenPOMFile.class);

  private final IReadableResource m_aRes;
  private final MavenPOM m_aPOM;

  @Nonnull
  private static IMavenArtifact _readArtifact (@Nonnull final IMicroElement e)
  {
    final String sGroupID = MicroHelper.getChildTextContent (e, CMavenXML.ELEMENT_GROUPID);
    final String sArtifactID = MicroHelper.getChildTextContent (e, CMavenXML.ELEMENT_ARTIFACTID);
    final String sVersion = MicroHelper.getChildTextContent (e, CMavenXML.ELEMENT_VERSION);
    final String sClassifier = MicroHelper.getChildTextContent (e, CMavenXML.ELEMENT_CLASSIFIER);
    return new MavenArtifact (sGroupID, sArtifactID, sVersion, sClassifier);
  }

  @Nonnull
  private static POMDependency _readDependency (@Nonnull final IMicroElement eDep)
  {
    final POMDependency aDep = new POMDependency (_readArtifact (eDep));
    final String sScope = MicroHelper.getChildTextContent (eDep, CMavenXML.ELEMENT_SCOPE);
    if (sScope != null)
    {
      final EMavenScope eScope = EMavenScope.getFromIDOrThrow (sScope);
      if (eScope != null)
        aDep.setScope (eScope);
      else
        s_aLogger.error ("Illegal Maven POM scope '" + sScope + "' provided!");
    }

    final IMicroElement eOptional = eDep.getFirstChildElement (CMavenXML.ELEMENT_OPTIONAL);
    if (eOptional != null)
      aDep.setOptional (StringParser.parseBool (eOptional.getTextContentTrimmed ()));

    final IMicroElement eExclusions = eDep.getFirstChildElement (CMavenXML.ELEMENT_EXCLUSIONS);
    if (eExclusions != null)
      for (final IMicroElement eExclusion : eExclusions.getAllChildElements (CMavenXML.ELEMENT_EXCLUSION))
        aDep.addExclusion (_readArtifact (eExclusion));

    return aDep;
  }

  private static void _fillConfiguration (@Nonnull final IMicroElement eConfiguration, @Nonnull final POMConfiguration aConfiguration)
  {
    for (final IMicroElement eChild : eConfiguration.getAllChildElements ())
      if (eChild.getChildCount () == 1 && eChild.getFirstChild ().getType ().equals (EMicroNodeType.TEXT))
      {
        // Use the text only
        aConfiguration.addItem (eChild.getTagName (), eChild.getTextContent ());
      }
      else
      {
        // Copy all child elements
        aConfiguration.addItem (eChild.getTagName (), MicroContainer.createWithClones (eChild.getAllChildElements ()));
      }
  }

  @Nonnull
  private static POMPlugin _readPlugin (@Nonnull final IMicroElement ePlugin)
  {
    final POMPlugin aPlugin = new POMPlugin (_readArtifact (ePlugin));

    // Extensions
    final String sExtensions = MicroHelper.getChildTextContentTrimmed (ePlugin, CMavenXML.ELEMENT_EXTENSIONS);
    if (sExtensions != null)
      aPlugin.setExtensions (StringParser.parseBool (sExtensions));

    // Executions
    final IMicroElement eExecutions = ePlugin.getFirstChildElement (CMavenXML.ELEMENT_EXECUTIONS);
    if (eExecutions != null)
    {
      for (final IMicroElement eExecution : eExecutions.getAllChildElements (CMavenXML.ELEMENT_EXECUTION))
      {
        final POMExecution aExecution = new POMExecution ();
        aExecution.setID (MicroHelper.getChildTextContentTrimmed (eExecution, CMavenXML.ELEMENT_ID));
        aExecution.setPhase (MicroHelper.getChildTextContentTrimmed (eExecution, CMavenXML.ELEMENT_PHASE));

        final IMicroElement eGoals = eExecution.getFirstChildElement (CMavenXML.ELEMENT_GOALS);
        if (eGoals != null)
          for (final IMicroElement eGoal : eGoals.getAllChildElements (CMavenXML.ELEMENT_GOAL))
            aExecution.addGoal (eGoal.getTextContentTrimmed ());

        final IMicroElement eConfiguration = eExecution.getFirstChildElement (CMavenXML.ELEMENT_CONFIGURATION);
        if (eConfiguration != null)
          _fillConfiguration (eConfiguration, aExecution.getConfiguration ());

        aPlugin.getExecutions ().addExecution (aExecution);
      }
    }

    // Configuration
    final IMicroElement eConfiguration = ePlugin.getFirstChildElement (CMavenXML.ELEMENT_CONFIGURATION);
    if (eConfiguration != null)
      _fillConfiguration (eConfiguration, aPlugin.getConfiguration ());

    // read plugin dependencies
    final IMicroElement eDependencies = ePlugin.getFirstChildElement (CMavenXML.ELEMENT_DEPENDENCIES);
    if (eDependencies != null)
      for (final IMicroElement eDep : eDependencies.getAllChildElements (CMavenXML.ELEMENT_DEPENDENCY))
      {
        final POMDependency aDep = _readDependency (eDep);
        aPlugin.getDependencies ().addDependency (aDep);
      }

    return aPlugin;
  }

  private void _readRepository (@Nonnull final IMicroElement eRepository, final boolean bPluginRepo)
  {
    final String sID = MicroHelper.getChildTextContentTrimmed (eRepository, CMavenXML.ELEMENT_ID);
    final String sURL = MicroHelper.getChildTextContentTrimmed (eRepository, CMavenXML.ELEMENT_URL);
    final String sLayout = MicroHelper.getChildTextContentTrimmed (eRepository, CMavenXML.ELEMENT_LAYOUT);
    final EMavenRepositoryLayout eLayout = EMavenRepositoryLayout.getFromIDOrDefault (sLayout, EMavenRepositoryLayout.DEFAULT);
    final String sUserName = null;
    final String sPassword = null;

    boolean bReleases = MavenRepository.DEFAULT_RELEASES;
    final IMicroElement eReleases = eRepository.getFirstChildElement (CMavenXML.ELEMENT_RELEASES);
    if (eReleases != null)
    {
      final IMicroElement eEnabled = eReleases.getFirstChildElement (CMavenXML.ELEMENT_ENABLED);
      if (eEnabled != null)
        bReleases = StringParser.parseBool (eEnabled.getTextContentTrimmed ());
    }

    boolean bSnapshots = MavenRepository.DEFAULT_SNAPSHOTS;
    final IMicroElement eSnapshots = eRepository.getFirstChildElement (CMavenXML.ELEMENT_SNAPSHOTS);
    if (eSnapshots != null)
    {
      final IMicroElement eEnabled = eSnapshots.getFirstChildElement (CMavenXML.ELEMENT_ENABLED);
      if (eEnabled != null)
        bSnapshots = StringParser.parseBool (eEnabled.getTextContentTrimmed ());
    }

    final MavenRepository aRepo = new MavenRepository (sID, sURL, eLayout, sUserName, sPassword, bReleases, bSnapshots);
    if (bPluginRepo)
      m_aPOM.addPluginRepository (aRepo);
    else
      m_aPOM.addRepository (aRepo);
  }

  public MavenPOMFile (@Nonnull final IReadableResource aRes)
  {
    this (aRes, false);
  }

  MavenPOMFile (@Nonnull final IReadableResource aRes, final boolean bIsSuperPOM)
  {
    this (MicroReader.readMicroXML (aRes), aRes, bIsSuperPOM);
  }

  public MavenPOMFile (@Nonnull final IMicroDocument aDoc, @Nonnull final IReadableResource aRes, final boolean bIsSuperPOM)
  {
    if (aDoc == null)
      throw new NullPointerException ("Doc");
    m_aRes = aRes;

    final IMicroElement eProject = aDoc.getDocumentElement ();
    if (!CMavenXML.ELEMENT_PROJECT.equals (eProject.getTagName ()))
      throw new IllegalArgumentException ("This is not a POM file: " + eProject.getTagName ());
    m_aPOM = new MavenPOM (bIsSuperPOM ? new MavenArtifact ("INTERNAL", "SUPER-POM") : _readArtifact (eProject));

    // set basedir properties
    final POMProperties aProperties = m_aPOM.getProperties ();
    if (aRes instanceof FileSystemResource)
    {
      // Source: http://maven.apache.org/pom.html#Properties
      final File aFile = aRes.getAsFile ().getAbsoluteFile ();
      final String sBaseDir = aFile.getParentFile ().getAbsolutePath ();
      aProperties.addSystemProperty ("project.basedir", sBaseDir);
    }

    // add all env variables (name in uppercase!)
    for (final Map.Entry <String, String> aEnv : System.getenv ().entrySet ())
      aProperties.addSystemProperty ("env." + aEnv.getKey ().toUpperCase (), aEnv.getValue ());

    // all system properties
    for (final Map.Entry <String, String> aSysProp : SystemProperties.getAllProperties ().entrySet ())
      aProperties.addSystemProperty (aSysProp.getKey (), aSysProp.getValue ());

    // read packaging
    final IMicroElement ePackaging = eProject.getFirstChildElement (CMavenXML.ELEMENT_PACKAGING);
    if (ePackaging != null)
    {
      final String sPackaging = ePackaging.getTextContentTrimmed ();
      if (sPackaging != null)
      {
        final EMavenPackaging eMavenPackaging = EMavenPackaging.getFromIDOrNull (sPackaging);
        if (eMavenPackaging == null)
          s_aLogger.error ("Failed to resolve POM packaging '" + sPackaging + "'");
        m_aPOM.setPackaging (eMavenPackaging);
      }
    }

    // read parent
    final IMicroElement eParent = eProject.getFirstChildElement (CMavenXML.ELEMENT_PARENT);
    if (eParent != null)
    {
      final String sRelativePath = MicroHelper.getChildTextContent (eParent, CMavenXML.ELEMENT_RELATIVE_PATH);
      m_aPOM.setParentArtifact (_readArtifact (eParent), sRelativePath);
    }

    // read name
    final IMicroElement eName = eProject.getFirstChildElement (CMavenXML.ELEMENT_NAME);
    if (eName != null)
      m_aPOM.setName (eName.getTextContentTrimmed ());

    // read description
    final IMicroElement eDescription = eProject.getFirstChildElement (CMavenXML.ELEMENT_DESCRIPTION);
    if (eDescription != null)
      m_aPOM.setDescription (eDescription.getTextContentTrimmed ());

    // read prerequisites
    final IMicroElement ePrerequisites = eProject.getFirstChildElement (CMavenXML.ELEMENT_PREREQUISITES);
    if (ePrerequisites != null)
      for (final IMicroElement ePrerequisite : ePrerequisites.getAllChildElements ())
        m_aPOM.getPrerequisites ().addItem (ePrerequisite.getTagName (), ePrerequisite.getTextContentTrimmed ());

    // Read SCM
    final IMicroElement eSCM = eProject.getFirstChildElement (CMavenXML.ELEMENT_SCM);
    if (eSCM != null)
    {
      m_aPOM.getSCM ().setUrl (MicroHelper.getChildTextContentTrimmed (eSCM, CMavenXML.ELEMENT_URL));
      m_aPOM.getSCM ().setConnection (MicroHelper.getChildTextContentTrimmed (eSCM, CMavenXML.ELEMENT_CONNECTION));
      m_aPOM.getSCM ().setDeveloperConnection (MicroHelper.getChildTextContentTrimmed (eSCM, CMavenXML.ELEMENT_DEVELOPER_CONNECTION));
    }

    // read licenses
    final IMicroElement eLicenses = eProject.getFirstChildElement (CMavenXML.ELEMENT_LICENSES);
    if (eLicenses != null)
      for (final IMicroElement eLicense : eLicenses.getAllChildElements (CMavenXML.ELEMENT_LICENSE))
      {
        final POMLicense aLicense = new POMLicense ();
        aLicense.setName (MicroHelper.getChildTextContent (eLicense, CMavenXML.ELEMENT_NAME));
        aLicense.setUrl (MicroHelper.getChildTextContent (eLicense, CMavenXML.ELEMENT_URL));
        aLicense.setDistribution (MicroHelper.getChildTextContent (eLicense, CMavenXML.ELEMENT_DISTRIBUTION));
        m_aPOM.getLicenses ().addLicense (aLicense);
      }

    // read properties
    final IMicroElement eProperties = eProject.getFirstChildElement (CMavenXML.ELEMENT_PROPERTIES);
    if (eProperties != null)
      for (final IMicroElement eProperty : eProperties.getAllChildElements ())
      {
        // Text content may be null - in the case the property value is an empty
        // String!
        // Found e.g. in com.akiban:akiban-persistit:3.2.7
        final String sTextContent = eProperty.getTextContent ();
        aProperties.addProperty (eProperty.getTagName (), StringHelper.getNotNull (sTextContent));
      }

    // read dependencyManagement
    final IMicroElement eDepMgmt = eProject.getFirstChildElement (CMavenXML.ELEMENT_DEPENDENCY_MANAGEMENT);
    if (eDepMgmt != null)
    {
      // read dependencyManagement dependencies
      final IMicroElement eDependencies = eDepMgmt.getFirstChildElement (CMavenXML.ELEMENT_DEPENDENCIES);
      if (eDependencies != null)
        for (final IMicroElement eDep : eDependencies.getAllChildElements (CMavenXML.ELEMENT_DEPENDENCY))
        {
          final POMDependency aDep = _readDependency (eDep);
          m_aPOM.addDependencyManagement (aDep);
        }
    }

    // read dependencies
    final IMicroElement eDeps = eProject.getFirstChildElement (CMavenXML.ELEMENT_DEPENDENCIES);
    if (eDeps != null)
      for (final IMicroElement eDep : eDeps.getAllChildElements (CMavenXML.ELEMENT_DEPENDENCY))
      {
        final POMDependency aDep = _readDependency (eDep);
        m_aPOM.addDependency (aDep);
      }

    // read build
    final IMicroElement eBuild = eProject.getFirstChildElement (CMavenXML.ELEMENT_BUILD);
    if (eBuild != null)
    {
      // read extensions
      final IMicroElement eExtensions = eBuild.getFirstChildElement (CMavenXML.ELEMENT_EXTENSIONS);
      if (eExtensions != null)
      {
        for (final IMicroElement eExtension : eExtensions.getAllChildElements (CMavenXML.ELEMENT_EXTENSION))
        {
          final POMExtension aExtension = new POMExtension (_readArtifact (eExtension));
          m_aPOM.getBuildExtensions ().addExtension (aExtension);
        }
      }

      // read resources
      final IMicroElement eResources = eBuild.getFirstChildElement (CMavenXML.ELEMENT_RESOURCES);
      if (eResources != null)
      {
        for (final IMicroElement eResource : eResources.getAllChildElements (CMavenXML.ELEMENT_RESOURCE))
        {
          final String sDirectory = MicroHelper.getChildTextContentTrimmed (eResource, CMavenXML.ELEMENT_DIRECTORY);
          final String sTargetPath = MicroHelper.getChildTextContentTrimmed (eResource, CMavenXML.ELEMENT_TARGETPATH);
          final POMResource aResource = new POMResource (sDirectory);
          aResource.setTargetPath (sTargetPath);
          m_aPOM.getBuildResources ().addResource (aResource);
        }
      }

      // read pluginManagement
      final IMicroElement ePluginManagement = eBuild.getFirstChildElement (CMavenXML.ELEMENT_PLUGIN_MANAGEMENT);
      if (ePluginManagement != null)
      {
        // read plugins
        final IMicroElement ePlugins = ePluginManagement.getFirstChildElement (CMavenXML.ELEMENT_PLUGINS);
        if (ePlugins != null)
        {
          for (final IMicroElement ePlugin : ePlugins.getAllChildElements (CMavenXML.ELEMENT_PLUGIN))
          {
            final POMPlugin aPlugin = _readPlugin (ePlugin);
            m_aPOM.addBuildPluginManagement (aPlugin);
          }
        }
      }

      // read plugins
      final IMicroElement ePlugins = eBuild.getFirstChildElement (CMavenXML.ELEMENT_PLUGINS);
      if (ePlugins != null)
      {
        for (final IMicroElement ePlugin : ePlugins.getAllChildElements (CMavenXML.ELEMENT_PLUGIN))
        {
          final POMPlugin aPlugin = _readPlugin (ePlugin);
          m_aPOM.addBuildPlugin (aPlugin);
        }
      }
    }

    // read modules
    final IMicroElement eModules = eProject.getFirstChildElement (CMavenXML.ELEMENT_MODULES);
    if (eModules != null)
      for (final IMicroElement eModule : eModules.getAllChildElements (CMavenXML.ELEMENT_MODULE))
        m_aPOM.addModule (eModule.getTextContentTrimmed ());

    // Read repositories
    final IMicroElement eRepositories = eProject.getFirstChildElement (CMavenXML.ELEMENT_REPOSITORIES);
    if (eRepositories != null)
      for (final IMicroElement eRepository : eRepositories.getAllChildElements (CMavenXML.ELEMENT_REPOSITORY))
        _readRepository (eRepository, false);

    // Read plugin repositories
    final IMicroElement ePluginRepositories = eProject.getFirstChildElement (CMavenXML.ELEMENT_PLUGINREPOSITORIES);
    if (ePluginRepositories != null)
      for (final IMicroElement ePluginRepository : ePluginRepositories.getAllChildElements (CMavenXML.ELEMENT_PLUGINREPOSITORY))
        _readRepository (ePluginRepository, true);
  }

  @Nonnull
  public MavenPOM getPOM ()
  {
    return m_aPOM;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MavenPOMFile))
      return false;
    final MavenPOMFile rhs = (MavenPOMFile) o;
    return m_aRes.equals (rhs.m_aRes) && m_aPOM.equals (rhs.m_aPOM);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aRes).append (m_aPOM).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("res", m_aRes).append ("POM", m_aPOM).toString ();
  }
}
