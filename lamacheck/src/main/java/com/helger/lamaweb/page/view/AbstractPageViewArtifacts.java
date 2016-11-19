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
package com.helger.lamaweb.page.view;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.compare.ESortOrder;
import com.helger.commons.url.ISimpleURL;
import com.helger.commons.url.SimpleURL;
import com.helger.html.hc.html.HC_Target;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.html.textlevel.HCEM;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenArtifactRepositoryInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lamaweb.app.menu.view.CLamaMenuView;
import com.helger.lamaweb.page.AbstractLamaFormPage;
import com.helger.photon.bootstrap3.form.BootstrapForm;
import com.helger.photon.bootstrap3.form.BootstrapFormGroup;
import com.helger.photon.bootstrap3.form.BootstrapViewForm;
import com.helger.photon.bootstrap3.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.bootstrap3.uictrls.datatables.plugins.BootstrapDataTablesPluginScroller;
import com.helger.photon.core.form.FormErrorList;
import com.helger.photon.uicore.css.CPageParam;
import com.helger.photon.uicore.page.EWebPageFormAction;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uictrls.datatables.DataTables;
import com.helger.photon.uictrls.datatables.column.DTCol;
import com.helger.photon.uictrls.datatables.column.EDTColType;

public abstract class AbstractPageViewArtifacts extends AbstractLamaFormPage <MavenArtifactInfo>
{
  public AbstractPageViewArtifacts (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Override
  protected boolean isActionAllowed (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final EWebPageFormAction eFormAction,
                                     @Nullable final MavenArtifactInfo aSelectedObject)
  {
    if (eFormAction.isEdit ())
      return false;
    return true;
  }

  @Override
  @Nullable
  protected MavenArtifactInfo getSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final String sID)
  {
    return LamaMetaManager.getArtifactInfoMgr ().getArtifactOfID (sID);
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final MavenArtifactInfo aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    final BootstrapViewForm aForm = aNodeList.addAndReturnChild (new BootstrapViewForm ());
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("GroupID").setCtrl (aSelectedObject.getGroupID ()));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("ArtifactID").setCtrl (aSelectedObject.getArtifactID ()));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Packaging")
                                                 .setCtrl (aSelectedObject.getPackaging () == null ? ""
                                                                                                   : aSelectedObject.getPackaging ()
                                                                                                                    .getID ()));
    final boolean bIsPlugin = aSelectedObject.isPluginArtifact ();
    final boolean bIsStandardConforming = aSelectedObject.isStandardPluginArtifactID ();
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Is plugin?")
                                                 .setCtrl (bIsPlugin ? bIsStandardConforming ? "Yes"
                                                                                             : "Yes, but with a non-standard conforming name"
                                                                     : "No"));

    final MavenVersion aLatestReleaseVersion = aSelectedObject.getLatestReleaseVersion ();
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Latest release version")
                                                 .setCtrl (aLatestReleaseVersion == null ? ""
                                                                                         : aLatestReleaseVersion.getOriginalVersion ()));

    final MavenVersion aLatestBetaVersion = aSelectedObject.getLatestBetaVersion ();
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Latest non-release version")
                                                 .setCtrl (aLatestBetaVersion == null ||
                                                           aLatestBetaVersion.isReleaseVersion () ? ""
                                                                                                  : aLatestBetaVersion.getOriginalVersion ()));

    final HCNodeList aExcludedVersions = new HCNodeList ();
    for (final MavenVersion aExcludedVersion : CollectionHelper.getSorted (aSelectedObject.getAllExcludedVersions ()))
      aExcludedVersions.addChild (new HCDiv ().addChild (aExcludedVersion.getOriginalVersion ()));
    if (aExcludedVersions.hasChildren ())
      aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Excluded versions").setCtrl (aExcludedVersions));

    final HCNodeList aRepos = new HCNodeList ();
    for (final MavenArtifactRepositoryInfo aArtifactRepoInfo : CollectionHelper.getSorted (aSelectedObject.getAllArtifactRepositoryInfos (),
                                                                                           Comparator.comparing (a -> a.getRepoInfo ()
                                                                                                                       .getURL ())))
    {
      final MavenRepositoryInfo aRepoInfo = aArtifactRepoInfo.getRepoInfo ();
      final HCDiv aDiv = new HCDiv ().addChild (new HCA (aWPEC.getLinkToMenuItem (CLamaMenuView.MENU_REPOSITORIES_ALL)
                                                              .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_VIEW)
                                                              .add (CPageParam.PARAM_OBJECT,
                                                                    aRepoInfo.getID ())).addChild (aRepoInfo.getURL ()));

      if (aRepoInfo.isValid ())
      {
        aDiv.addChild (" - ")
            .addChild (new HCA (new SimpleURL (aRepoInfo.getURL () +
                                               aSelectedObject.getArtifactPath ())).setTarget (HC_Target.BLANK)
                                                                                   .addChild ("view folder"));
      }
      if (aArtifactRepoInfo.getLatestReleaseVersion () != null)
        aDiv.addChild (" - ").addChild (aArtifactRepoInfo.getLatestReleaseVersion ().getOriginalVersion ());

      aRepos.addChild (aDiv);
    }
    if (!aRepos.hasChildren ())
      aRepos.addChild (new HCEM ().addChild ("none"));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Repositories").setCtrl (aRepos));
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final MavenArtifactInfo aSelectedObject,
                                                 @Nonnull final FormErrorList aFormErrors,
                                                 @Nonnull final EWebPageFormAction eFormAction)
  {}

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final MavenArtifactInfo aSelectedObject,
                                @Nonnull final BootstrapForm aForm,
                                @Nonnull final EWebPageFormAction eFormAction,
                                @Nonnull final FormErrorList aFormErrors)
  {}

  @Nonnull
  protected abstract Collection <MavenArtifactInfo> getRelevantArtifacts ();

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final HCTable aTable = new HCTable (new DTCol ("Group ID").setDataSort (0, 1)
                                                              .setInitialSorting (ESortOrder.ASCENDING),
                                        new DTCol ("Artifact ID"),
                                        new DTCol ("Release Version"),
                                        new DTCol ("Latest version"),
                                        new DTCol ("Packaging"),
                                        new DTCol ("Repo#").setDisplayType (EDTColType.INT,
                                                                            aDisplayLocale)).setID (getID ());
    for (final MavenArtifactInfo aArtifactInfo : getRelevantArtifacts ())
    {
      final ISimpleURL aViewURL = createViewURL (aWPEC, aArtifactInfo);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aArtifactInfo.getGroupID ());
      aRow.addCell (new HCA (aViewURL).addChild (aArtifactInfo.getArtifactID ()));
      final MavenVersion aLatestVersion = aArtifactInfo.getLatestReleaseVersion ();
      aRow.addCell (aLatestVersion == null ? "" : aLatestVersion.getOriginalVersion ());
      final MavenVersion aLatestBetaVersion = aArtifactInfo.getLatestBetaVersion ();
      aRow.addCell (aLatestBetaVersion == null ? "" : aLatestBetaVersion.getOriginalVersion ());
      final EMavenPackaging ePackaging = aArtifactInfo.getPackaging ();
      aRow.addCell (ePackaging == null ? "" : ePackaging.getID ());
      aRow.addCell (Integer.toString (aArtifactInfo.getDesiredRepoCount ()));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = BootstrapDataTables.createDefaultDataTables (aWPEC, aTable);
    aDataTables.addPlugin (new BootstrapDataTablesPluginScroller ());
    aNodeList.addChild (aDataTables);
  }
}
