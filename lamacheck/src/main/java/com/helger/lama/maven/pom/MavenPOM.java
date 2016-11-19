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

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.xml.XMLConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.CommonsLinkedHashSet;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.collection.ext.ICommonsOrderedSet;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.text.IHasDescription;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.xml.CXML;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroDocument;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

/**
 * This class helps to create a well formed Maven2 pom.xml file.
 *
 * @author Philip Helger
 */
public final class MavenPOM implements IHasDescription
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenPOM.class);
  private static final MavenPOM SUPER_POM = new MavenPOMFile (new ClassPathResource ("maven2/super-pom-3.0.4.xml"),
                                                              true).getPOM ();

  private final IMavenArtifact m_aArtifact;
  private IMavenArtifact m_aEffectiveArtifact;
  private IMavenArtifact m_aParentArtifact;
  private String m_sParentRelativePath;
  private MavenPOM m_aParentPOM;
  private EMavenPackaging m_ePackaging;
  private String m_sName;
  private String m_sDescription;

  private final POMPrerequisites m_aPrerequisites = new POMPrerequisites ();
  private final POMSCM m_aSCM = new POMSCM ();
  private final POMLicenses m_aLicenses = new POMLicenses ();
  private final POMProperties m_aProperties;
  private final POMRepositories m_aRepos = new POMRepositories (false);
  private final POMRepositories m_aPluginRepos = new POMRepositories (true);

  private final POMDependencies m_aDependencyMgmt = new POMDependencies (true);
  private POMDependencies m_aResolvedDependencyMgmt;
  private final POMDependencies m_aDependencies = new POMDependencies (false);
  private POMDependencies m_aResolvedDependencies;

  private final POMExtensions m_aBuildExtensions = new POMExtensions ();
  private final POMResources m_aBuildResources = new POMResources ();
  private final POMPlugins m_aBuildPluginMgmt = new POMPlugins ();
  private POMPlugins m_aResolvedBuildPluginMgmt;
  private final POMPlugins m_aBuildPlugins = new POMPlugins ();
  private POMPlugins m_aResolvedBuildPlugins;

  private final ICommonsOrderedSet <String> m_aModules = new CommonsLinkedHashSet<> ();

  public MavenPOM (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    m_aArtifact = aArtifact;
    m_aProperties = new POMProperties (aArtifact);
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  @Nonnull
  public IMavenArtifact getEffectiveArtifact ()
  {
    return m_aEffectiveArtifact != null ? m_aEffectiveArtifact : m_aArtifact;
  }

  public void setParentArtifact (@Nonnull final IMavenArtifact aParent)
  {
    setParentArtifact (aParent, null);
  }

  public void setParentArtifact (@Nonnull final IMavenArtifact aParentArtifact,
                                 @Nullable final String sParentRelativePath)
  {
    String sRealParentRelativePath = sParentRelativePath;
    if (sRealParentRelativePath != null && !sRealParentRelativePath.endsWith (CMaven.POM_XML))
      sRealParentRelativePath = FilenameHelper.getCleanConcatenatedUrlPath (sRealParentRelativePath, CMaven.POM_XML);

    m_aParentArtifact = aParentArtifact;
    m_sParentRelativePath = StringHelper.trim (sParentRelativePath);

    // Is there some identifier part to merge?
    m_aEffectiveArtifact = new MavenArtifact (StringHelper.getNotNull (m_aArtifact.getGroupID (),
                                                                       aParentArtifact.getGroupID ()),
                                              m_aArtifact.getArtifactID (),
                                              StringHelper.getNotNull (m_aArtifact.getVersion (),
                                                                       aParentArtifact.getVersion ()),
                                              StringHelper.getNotNull (m_aArtifact.getClassifier (),
                                                                       aParentArtifact.getClassifier ()));
  }

  @Nullable
  public IMavenArtifact getParentArtifact ()
  {
    return m_aParentArtifact;
  }

  public void setParentPOM (@Nullable final MavenPOM aParentPOM)
  {
    if (aParentPOM != null)
    {
      if (m_aParentArtifact == null)
        throw new IllegalStateException ("Provide the parent POM artifact before setting the parent POM!");
      final IMavenArtifact aParentArtifact = aParentPOM.getArtifact ();
      if (!m_aParentArtifact.equals (aParentArtifact))
        throw new IllegalStateException ("The provided parent artifact and the artifact from the parent POM don't match!");
      if (aParentPOM.getPackagingOrDefault () != EMavenPackaging.POM)
        throw new IllegalArgumentException ("Passed parent type does not have packaging POM but " +
                                            aParentPOM.getPackagingOrDefault ());
      m_aParentPOM = aParentPOM;
    }
    else
      m_aParentPOM = null;
  }

  public boolean isSuperPOM ()
  {
    return this == SUPER_POM;
  }

  @Nullable
  public MavenPOM getParentPOM ()
  {
    return m_aParentPOM;
  }

  @Nullable
  public MavenPOM getEffectiveParentPOM ()
  {
    if (isSuperPOM ())
      return null;
    return m_aParentPOM != null ? m_aParentPOM : SUPER_POM;
  }

  public void setPackaging (@Nullable final EMavenPackaging ePackaging)
  {
    m_ePackaging = ePackaging;
  }

  @Nonnull
  public EMavenPackaging getPackagingOrDefault ()
  {
    return m_ePackaging != null ? m_ePackaging : EMavenPackaging.DEFAULT_PACKAGING;
  }

  public void setName (@Nullable final String sName)
  {
    m_sName = StringHelper.trim (sName);
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  public void setDescription (@Nullable final String sDescription)
  {
    m_sDescription = StringHelper.trim (sDescription);
  }

  @Nullable
  public String getDescription ()
  {
    return m_sDescription;
  }

  @Nonnull
  public POMProperties getProperties ()
  {
    return m_aProperties;
  }

  @Nonnull
  @ReturnsMutableCopy
  public POMProperties getEffectiveProperties ()
  {
    final POMProperties ret = new POMProperties (getEffectiveArtifact ());

    // all system properties up-front as we cannot rely on a parent POM present
    ret.addAllSystemProperties (m_aProperties);

    // parent first, so that properties in this POM take precedence!
    final MavenPOM aParentPOM = getEffectiveParentPOM ();
    if (aParentPOM != null)
      ret.addAllProperties (aParentPOM.getEffectiveProperties ());

    // all defined properties
    ret.addAllProperties (m_aProperties);

    return ret;
  }

  public void addDependencyManagement (@Nonnull final POMDependency aDep)
  {
    if (m_aResolvedDependencyMgmt != null)
      throw new IllegalStateException ("Dependencies already resolved!");
    if (!m_aDependencyMgmt.addDependency (aDep))
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Dependency management dependency already contained: " + aDep);
  }

  @Nonnull
  public POMDependencies getDependencyManagement ()
  {
    return m_aResolvedDependencyMgmt != null ? m_aResolvedDependencyMgmt : m_aDependencyMgmt;
  }

  private static void _collectDependencyMgmt (final POMDependencies ret, final MavenPOM aPOM)
  {
    // this first
    for (final POMDependency aDep : aPOM.getDependencyManagement ().getAll ())
      ret.addDependency (aDep);

    // parent last
    if (aPOM.getEffectiveParentPOM () != null)
      _collectDependencyMgmt (ret, aPOM.getEffectiveParentPOM ());
  }

  @Nonnull
  public POMDependencies getEffectiveDependencyManagement ()
  {
    final POMDependencies ret = new POMDependencies (true);
    _collectDependencyMgmt (ret, this);
    return ret;
  }

  public void addDependency (@Nonnull final POMDependency aDep)
  {
    if (m_aResolvedDependencies != null)
      throw new IllegalStateException ("Dependencies already resolved!");
    if (!m_aDependencies.addDependency (aDep))
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Dependency already contained: " + aDep);
  }

  @Nonnull
  public POMDependencies getDependencies ()
  {
    return m_aResolvedDependencies != null ? m_aResolvedDependencies : m_aDependencies;
  }

  private static void _collectDependencies (final POMDependencies ret, final MavenPOM aPOM)
  {
    // this first
    for (final POMDependency aDep : aPOM.getDependencies ().getAll ())
      ret.addDependency (aDep);

    // parent last
    if (aPOM.getEffectiveParentPOM () != null)
      _collectDependencies (ret, aPOM.getEffectiveParentPOM ());
  }

  @Nonnull
  public POMDependencies getEffectiveDependencies ()
  {
    final POMDependencies ret = new POMDependencies (false);
    _collectDependencies (ret, this);
    return ret;
  }

  @Nonnull
  public POMExtensions getBuildExtensions ()
  {
    return m_aBuildExtensions;
  }

  @Nonnull
  public POMResources getBuildResources ()
  {
    return m_aBuildResources;
  }

  public void addBuildPluginManagement (@Nonnull final POMPlugin aPlugin)
  {
    if (m_aResolvedBuildPluginMgmt != null)
      throw new IllegalStateException ("Dependencies already resolved!");
    m_aBuildPluginMgmt.addPlugin (aPlugin);
  }

  @Nonnull
  public POMPlugins getBuildPluginManagement ()
  {
    return m_aResolvedBuildPluginMgmt != null ? m_aResolvedBuildPluginMgmt : m_aBuildPluginMgmt;
  }

  private static void _collectBuildPluginManagement (final POMPlugins ret, final MavenPOM aPOM)
  {
    // this first
    for (final POMPlugin aPlugin : aPOM.getBuildPluginManagement ().getAll ())
      ret.addPlugin (aPlugin);

    // parent last
    if (aPOM.getEffectiveParentPOM () != null)
      _collectBuildPluginManagement (ret, aPOM.getEffectiveParentPOM ());
  }

  @Nonnull
  public POMPlugins getEffectiveBuildPluginManagement ()
  {
    final POMPlugins ret = new POMPlugins ();
    _collectBuildPluginManagement (ret, this);
    return ret;
  }

  public void addBuildPlugin (@Nonnull final POMPlugin aPlugin)
  {
    if (m_aResolvedBuildPlugins != null)
      throw new IllegalStateException ("Plugins already resolved!");
    m_aBuildPlugins.addPlugin (aPlugin);
  }

  @Nonnull
  public POMPlugins getBuildPlugins ()
  {
    return m_aResolvedBuildPlugins != null ? m_aResolvedBuildPlugins : m_aBuildPlugins;
  }

  private static void _collectBuildPlugins (final POMPlugins ret, final MavenPOM aPOM)
  {
    // this first
    for (final POMPlugin aPlugin : aPOM.getBuildPlugins ().getAll ())
      ret.addPlugin (aPlugin);

    // parent last
    if (aPOM.getEffectiveParentPOM () != null)
      _collectBuildPlugins (ret, aPOM.getEffectiveParentPOM ());
  }

  @Nonnull
  public POMPlugins getEffectiveBuildPlugins ()
  {
    final POMPlugins ret = new POMPlugins ();
    _collectBuildPlugins (ret, this);
    return ret;
  }

  @Nonnull
  public EChange addModule (final String sModuleName)
  {
    if (StringHelper.hasNoText (sModuleName))
      throw new IllegalArgumentException ("Passed module name is empty");
    return EChange.valueOf (m_aModules.add (sModuleName));
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllModules ()
  {
    return CollectionHelper.newList (m_aModules);
  }

  @Nonnull
  public POMPrerequisites getPrerequisites ()
  {
    return m_aPrerequisites;
  }

  @Nonnull
  public POMSCM getSCM ()
  {
    return m_aSCM;
  }

  @Nonnull
  public POMLicenses getLicenses ()
  {
    return m_aLicenses;
  }

  public void addRepository (@Nullable final MavenRepository aRepo)
  {
    if (aRepo != null)
      m_aRepos.addRepository (new POMRepository (false, aRepo));
  }

  @Nonnull
  public POMRepositories getRepositories ()
  {
    return m_aRepos;
  }

  public void addPluginRepository (@Nullable final MavenRepository aRepo)
  {
    if (aRepo != null)
      m_aPluginRepos.addRepository (new POMRepository (true, aRepo));
  }

  @Nonnull
  public POMRepositories getPluginRepositories ()
  {
    return m_aPluginRepos;
  }

  private static String _replaceProperties (final String sText, final Map <String, String> aSearchReplaceMap)
  {
    String ret = sText;
    if (StringHelper.hasText (ret))
      for (final Map.Entry <String, String> aReplacement : aSearchReplaceMap.entrySet ())
        ret = StringHelper.replaceAll (ret, aReplacement.getKey (), StringHelper.getNotNull (aReplacement.getValue ()));
    return ret;
  }

  @Nonnull
  private static IMavenArtifact _replaceProperties (@Nonnull final IMavenArtifact aArtifact,
                                                    @Nonnull final Map <String, String> aSearchReplaceMap)
  {
    if (aSearchReplaceMap.isEmpty ())
      return aArtifact;

    final String sGroupID = _replaceProperties (aArtifact.getGroupID (), aSearchReplaceMap);
    final String sArtifactID = _replaceProperties (aArtifact.getArtifactID (), aSearchReplaceMap);
    final String sVersion = _replaceProperties (aArtifact.getVersion (), aSearchReplaceMap);
    final String sClassifier = _replaceProperties (aArtifact.getClassifier (), aSearchReplaceMap);
    return new MavenArtifact (sGroupID, sArtifactID, sVersion, sClassifier);
  }

  @Nonnull
  public MavenPOM resolveProperties ()
  {
    if (m_aResolvedDependencies != null)
    {
      // parents are automatically resolved!
      if (getPackagingOrDefault () == EMavenPackaging.POM)
        return this;
      throw new IllegalStateException ("Already resolved dependencies!");
    }

    // Ensure parent properties are resolved
    if (m_aParentPOM != null)
      m_aParentPOM.resolveProperties ();

    // Build search and replace map
    final ICommonsMap <String, String> aSearchReplaceMap = new CommonsHashMap<> ();
    for (final Map.Entry <String, String> aEntry : getEffectiveProperties ().getAll ().entrySet ())
      aSearchReplaceMap.put ("${" + aEntry.getKey () + "}", aEntry.getValue ());

    // Replace in dependencyManagement and pluginManagement
    if (!aSearchReplaceMap.isEmpty ())
    {
      m_aResolvedDependencyMgmt = new POMDependencies (true);
      for (final POMDependency aDep : m_aDependencyMgmt.getAll ())
      {
        final IMavenArtifact aArtifact = _replaceProperties (aDep.getArtifact (), aSearchReplaceMap);
        m_aResolvedDependencyMgmt.addDependency (new POMDependency (aArtifact, aDep));
      }

      m_aResolvedBuildPluginMgmt = new POMPlugins ();
      for (final POMPlugin aPlugin : m_aBuildPluginMgmt.getAll ())
      {
        final IMavenArtifact aArtifact = _replaceProperties (aPlugin.getArtifact (), aSearchReplaceMap);
        final POMPlugin aResolvedPlugin = new POMPlugin (aArtifact, aPlugin);
        for (final POMDependency aPluginDep : aPlugin.getDependencies ().getAll ())
        {
          final IMavenArtifact aPluginDepArtifact = _replaceProperties (aPluginDep.getArtifact (), aSearchReplaceMap);
          aResolvedPlugin.getDependencies ().addDependency (new POMDependency (aPluginDepArtifact, aPluginDep));
        }
        m_aResolvedBuildPluginMgmt.addPlugin (aResolvedPlugin);
      }
    }

    // Replace in dependencies
    final POMDependencies aEffectiveDependencyMgmt = getEffectiveDependencyManagement ();
    m_aResolvedDependencies = new POMDependencies (false);
    for (final POMDependency aDep : m_aDependencies.getAll ())
    {
      IMavenArtifact aArtifact = _replaceProperties (aDep.getArtifact (), aSearchReplaceMap);
      if (aArtifact.getVersion () == null)
      {
        final IMavenArtifact aManagedArtifact = aEffectiveDependencyMgmt.findDependency (aArtifact);
        if (aManagedArtifact != null)
          aArtifact = aManagedArtifact;
        else
          if (s_aLogger.isDebugEnabled ())
            s_aLogger.debug ("Failed to resolve " + aArtifact + " in " + aEffectiveDependencyMgmt);
      }
      m_aResolvedDependencies.addDependency (new POMDependency (aArtifact, aDep));
    }

    // Replace in plugins
    final POMPlugins aEffectivePluginMgmt = getEffectiveBuildPluginManagement ();
    m_aResolvedBuildPlugins = new POMPlugins ();
    for (final POMPlugin aPlugin : m_aBuildPlugins.getAll ())
    {
      IMavenArtifact aArtifact = _replaceProperties (aPlugin.getArtifact (), aSearchReplaceMap);
      if (aArtifact.getVersion () == null)
      {
        final IMavenArtifact aManagedArtifact = aEffectivePluginMgmt.findPlugin (aArtifact);
        if (aManagedArtifact != null)
          aArtifact = aManagedArtifact;
        else
          if (s_aLogger.isDebugEnabled ())
            s_aLogger.debug ("Failed to resolve " + aArtifact + " in " + aEffectivePluginMgmt);
      }

      final POMPlugin aResolvedPlugin = new POMPlugin (aArtifact, aPlugin);
      for (final POMDependency aPluginDep : aPlugin.getDependencies ().getAll ())
      {
        final IMavenArtifact aPluginDepArtifact = _replaceProperties (aPluginDep.getArtifact (), aSearchReplaceMap);
        aResolvedPlugin.getDependencies ().addDependency (new POMDependency (aPluginDepArtifact, aPluginDep));
      }
      m_aResolvedBuildPlugins.addPlugin (aResolvedPlugin);
    }

    return this;
  }

  @Nonnull
  public IMicroDocument getAsDOMTree ()
  {
    // start project
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eProject = aDoc.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PROJECT)
                                       .setAttribute (XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                                                      CXML.XML_NS_PREFIX_XSI,
                                                      CXML.XML_NS_XSI)
                                       .setAttribute (CXML.XML_NS_XSI,
                                                      CXML.XML_ATTR_XSI_SCHEMALOCATION,
                                                      CMaven.POM_XMLNS +
                                                                                        " " +
                                                                                        CMaven.POM_XMLNS_URL);
    // MicroUtils.setXMLNS (eProject, CMaven2.POM_XMLNS);
    eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_MODEL_VERSION).appendText (CMavenXML.MODEL_VERSION);

    // / parent POM present?
    if (m_aParentArtifact != null)
    {
      final IMicroElement eParent = eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PARENT);
      MavenPOMHelper.serializeArtifact (m_aParentArtifact, eParent);
      if (m_sParentRelativePath != null)
        eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_RELATIVE_PATH).appendText (m_sParentRelativePath);
    }

    // This artifact (NOT the effective one!)
    MavenPOMHelper.serializeArtifact (m_aArtifact, eProject);

    if (m_ePackaging != null)
      eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PACKAGING).appendText (m_ePackaging.getID ());

    // Any name?
    if (StringHelper.hasText (m_sName))
      eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_NAME).appendText (m_sName);
    if (StringHelper.hasText (m_sDescription))
      eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DESCRIPTION).appendText (m_sDescription);

    // Prerequisites
    m_aPrerequisites.appendToElement (eProject);

    // SCM
    m_aSCM.appendToElement (eProject);

    // licenses
    m_aLicenses.appendToElement (eProject);

    // TODO distributionManagement

    // properties
    m_aProperties.appendToElement (eProject);

    // TODO organization

    // TODO developers

    // TODO profiles

    // Repositories
    m_aRepos.appendToElement (eProject);

    // Plugin repositories
    m_aPluginRepos.appendToElement (eProject);

    // dependencyManagement
    m_aDependencyMgmt.appendToElement (eProject);

    // dependencies
    m_aDependencies.appendToElement (eProject);

    if (!m_aBuildExtensions.isEmpty () ||
        !m_aBuildResources.isEmpty () ||
        !m_aBuildPluginMgmt.isEmpty () ||
        !m_aBuildPlugins.isEmpty ())
    {
      final IMicroElement eBuild = eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_BUILD);

      // build extensions
      m_aBuildExtensions.appendToElement (eBuild);

      // build resources
      m_aBuildResources.appendToElement (eBuild);

      // Plugin management
      if (!m_aBuildPluginMgmt.isEmpty ())
      {
        final IMicroElement ePluginMgmt = eBuild.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PLUGIN_MANAGEMENT);
        m_aBuildPluginMgmt.appendToElement (ePluginMgmt);
      }

      // build plugins
      m_aBuildPlugins.appendToElement (eBuild);
    }

    // modules
    if (!m_aModules.isEmpty ())
    {
      final IMicroElement eModules = eProject.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_MODULES);
      for (final String sModule : m_aModules)
        eModules.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_MODULE).appendText (sModule);
    }

    return aDoc;
  }

  public void writeToStream (@Nonnull @WillClose final OutputStream aOS)
  {
    try
    {
      final IMicroDocument aDoc = getAsDOMTree ();
      MicroWriter.writeToStream (aDoc, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
    }
    finally
    {
      StreamHelper.close (aOS);
    }
  }
}
