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
package com.helger.lama.maven.apache;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.resolution.ModelResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.lama.maven.artifact.MavenArtifact;

public final class GetAllDependencies
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (GetAllDependencies.class);

  private GetAllDependencies ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  private static ICommonsList <MavenArtifactWithType> _findUpdatesOfPOM (@Nonnull final Model aModel)
  {
    final ICommonsList <MavenArtifactWithType> ret = new CommonsArrayList<> ();
    if (aModel.getParent () != null)
      ret.add (new MavenArtifactWithType ("ParentPOM",
                                          new MavenArtifact (aModel.getParent ().getGroupId (),
                                                             aModel.getParent ().getArtifactId (),
                                                             aModel.getParent ().getVersion ())));
    if (aModel.getDependencyManagement () != null)
      for (final Dependency aDep : aModel.getDependencyManagement ().getDependencies ())
        ret.add (new MavenArtifactWithType ("DependencyManagement", new MavenArtifact (aDep.getGroupId (),
                                                                                       aDep.getArtifactId (),
                                                                                       aDep.getVersion (),
                                                                                       aDep.getClassifier ())));
    for (final Dependency aDep : aModel.getDependencies ())
      ret.add (new MavenArtifactWithType ("Dependency",
                                          new MavenArtifact (aDep.getGroupId (),
                                                             aDep.getArtifactId (),
                                                             aDep.getVersion (),
                                                             aDep.getClassifier ())));
    if (aModel.getBuild () != null)
    {
      for (final Extension aExtension : aModel.getBuild ().getExtensions ())
        ret.add (new MavenArtifactWithType ("Extension",
                                            new MavenArtifact (aExtension.getGroupId (),
                                                               aExtension.getArtifactId (),
                                                               aExtension.getVersion ())));
      if (aModel.getBuild ().getPluginManagement () != null)
        for (final Plugin aPlugin : aModel.getBuild ().getPluginManagement ().getPlugins ())
        {
          ret.add (new MavenArtifactWithType ("PluginManagement",
                                              new MavenArtifact (aPlugin.getGroupId (),
                                                                 aPlugin.getArtifactId (),
                                                                 aPlugin.getVersion ())));
          for (final Dependency aDep : aPlugin.getDependencies ())
            ret.add (new MavenArtifactWithType ("PluginManagement-Dependency", new MavenArtifact (aDep.getGroupId (),
                                                                                                  aDep.getArtifactId (),
                                                                                                  aDep.getVersion (),
                                                                                                  aDep.getClassifier ())));
        }
      for (final Plugin aPlugin : aModel.getBuild ().getPlugins ())
      {
        ret.add (new MavenArtifactWithType ("Plugin",
                                            new MavenArtifact (aPlugin.getGroupId (),
                                                               aPlugin.getArtifactId (),
                                                               aPlugin.getVersion ())));
        for (final Dependency aDep : aPlugin.getDependencies ())
          ret.add (new MavenArtifactWithType ("Plugin-Dependency",
                                              new MavenArtifact (aDep.getGroupId (),
                                                                 aDep.getArtifactId (),
                                                                 aDep.getVersion (),
                                                                 aDep.getClassifier ())));
      }
    }
    for (final Profile aProfile : aModel.getProfiles ())
    {
      final String sProfileID = aProfile.getId ();
      if (aProfile.getDependencyManagement () != null)
        for (final Dependency aDep : aProfile.getDependencyManagement ().getDependencies ())
          ret.add (new MavenArtifactWithType ("Profile " +
                                              sProfileID +
                                              " DependencyManagement",
                                              new MavenArtifact (aDep.getGroupId (),
                                                                 aDep.getArtifactId (),
                                                                 aDep.getVersion (),
                                                                 aDep.getClassifier ())));
      for (final Dependency aDep : aProfile.getDependencies ())
        ret.add (new MavenArtifactWithType ("Profile " +
                                            sProfileID +
                                            " Dependency",
                                            new MavenArtifact (aDep.getGroupId (),
                                                               aDep.getArtifactId (),
                                                               aDep.getVersion (),
                                                               aDep.getClassifier ())));
      if (aProfile.getBuild () != null)
      {
        if (aProfile.getBuild ().getPluginManagement () != null)
          for (final Plugin aPlugin : aProfile.getBuild ().getPluginManagement ().getPlugins ())
          {
            ret.add (new MavenArtifactWithType ("Profile " +
                                                sProfileID +
                                                " PluginManagement",
                                                new MavenArtifact (aPlugin.getGroupId (),
                                                                   aPlugin.getArtifactId (),
                                                                   aPlugin.getVersion ())));
            for (final Dependency aDep : aPlugin.getDependencies ())
              ret.add (new MavenArtifactWithType ("Profile " +
                                                  sProfileID +
                                                  " PluginManagement-Dependency",
                                                  new MavenArtifact (aDep.getGroupId (),
                                                                     aDep.getArtifactId (),
                                                                     aDep.getVersion (),
                                                                     aDep.getClassifier ())));
          }
        for (final Plugin aPlugin : aProfile.getBuild ().getPlugins ())
        {
          ret.add (new MavenArtifactWithType ("Profile " +
                                              sProfileID +
                                              " Plugin",
                                              new MavenArtifact (aPlugin.getGroupId (),
                                                                 aPlugin.getArtifactId (),
                                                                 aPlugin.getVersion ())));
          for (final Dependency aDep : aPlugin.getDependencies ())
            ret.add (new MavenArtifactWithType ("Profile " +
                                                sProfileID +
                                                " Plugin-Dependency",
                                                new MavenArtifact (aDep.getGroupId (),
                                                                   aDep.getArtifactId (),
                                                                   aDep.getVersion (),
                                                                   aDep.getClassifier ())));
        }
      }
    }

    return ret;
  }

  @Nullable
  public static ICommonsList <MavenArtifactWithType> getAllDependencies (@Nonnull final File aFile,
                                                                         @Nonnull final ModelResolver aModelResolver)
  {
    final ModelBuilder builder = new DefaultModelBuilderFactory ().newInstance ();
    final ModelBuildingRequest aMBRequest = new DefaultModelBuildingRequest ();
    aMBRequest.setProcessPlugins (false);
    aMBRequest.setPomFile (aFile);
    aMBRequest.setModelResolver (aModelResolver);
    aMBRequest.setValidationLevel (ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
    aMBRequest.setSystemProperties (System.getProperties ());

    try
    {
      final Model aModel = builder.build (aMBRequest).getEffectiveModel ();
      return _findUpdatesOfPOM (aModel);
    }
    catch (final ModelBuildingException e)
    {
      s_aLogger.error ("Failed to resolve POM for " + e.getModelId (), e);
    }
    catch (final Exception e)
    {
      s_aLogger.error ("Failed to resolve POM for " + aFile.getAbsolutePath (), e);
    }
    return null;
  }
}
