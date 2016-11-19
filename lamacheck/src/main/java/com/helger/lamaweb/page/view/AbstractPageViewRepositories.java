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

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.ICommonsCollection;
import com.helger.commons.compare.ESortOrder;
import com.helger.commons.url.EURLProtocol;
import com.helger.commons.url.ISimpleURL;
import com.helger.commons.url.IURLProtocol;
import com.helger.commons.url.SimpleURL;
import com.helger.commons.url.URLProtocolRegistry;
import com.helger.html.hc.html.HC_Target;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.html.textlevel.HCEM;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lamaweb.app.menu.view.CLamaMenuView;
import com.helger.lamaweb.page.AbstractLamaFormPage;
import com.helger.photon.bootstrap3.form.BootstrapForm;
import com.helger.photon.bootstrap3.form.BootstrapFormGroup;
import com.helger.photon.bootstrap3.form.BootstrapViewForm;
import com.helger.photon.bootstrap3.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.form.FormErrorList;
import com.helger.photon.uicore.css.CPageParam;
import com.helger.photon.uicore.page.EWebPageFormAction;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uictrls.datatables.DataTables;
import com.helger.photon.uictrls.datatables.column.DTCol;

public abstract class AbstractPageViewRepositories extends AbstractLamaFormPage <MavenRepositoryInfo>
{
  public AbstractPageViewRepositories (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Override
  protected boolean isActionAllowed (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final EWebPageFormAction eFormAction,
                                     @Nullable final MavenRepositoryInfo aSelectedObject)
  {
    if (eFormAction.isEdit ())
      return false;
    return true;
  }

  @Override
  @Nullable
  protected MavenRepositoryInfo getSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                                   @Nullable final String sID)
  {
    return LamaMetaManager.getRepoInfoMgr ().getRepositoryOfID (sID);
  }

  public static boolean canCreateLinkTo (@Nonnull final MavenRepositoryInfo aRepoInfo)
  {
    final IURLProtocol aProtocol = URLProtocolRegistry.getInstance ().getProtocol (aRepoInfo.getURL ());
    return EURLProtocol.HTTP.equals (aProtocol) || EURLProtocol.HTTPS.equals (aProtocol);
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final MavenRepositoryInfo aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final MavenRepository aRepo = aSelectedObject.getRepo ();

    final BootstrapViewForm aForm = aNodeList.addAndReturnChild (new BootstrapViewForm ());
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("ID").setCtrl (aRepo.getID ()));

    final boolean bCreateLink = canCreateLinkTo (aSelectedObject);
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("URL")
                                                 .setCtrl (bCreateLink ? new HCA (new SimpleURL (aRepo.getURL ())).setTarget (HC_Target.BLANK)
                                                                                                                  .addChild (aRepo.getURL ())
                                                                       : new HCTextNode (aRepo.getURL ())));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Layout").setCtrl (aRepo.getLayout ().getID ()));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Valid?")
                                                 .setCtrl (EPhotonCoreText.getYesOrNo (aSelectedObject.isValid (),
                                                                                       aDisplayLocale)));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Has username?")
                                                 .setCtrl (EPhotonCoreText.getYesOrNo (aRepo.getUserName () != null,
                                                                                       aDisplayLocale)));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Releases?")
                                                 .setCtrl (EPhotonCoreText.getYesOrNo (aRepo.isReleasesEnabled (),
                                                                                       aDisplayLocale)));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Snapshots?")
                                                 .setCtrl (EPhotonCoreText.getYesOrNo (aRepo.isSnapshotsEnabled (),
                                                                                       aDisplayLocale)));

    final List <MavenArtifactInfo> aArtifactsOfRepo = LamaMetaManager.getArtifactInfoMgr ()
                                                                     .getAllArtifactsWithDesiredRepoIDSorted (aRepo.getID ());

    final HCNodeList aUnique = new HCNodeList ();
    for (final MavenArtifactInfo aArtifactInfo : aArtifactsOfRepo)
    {
      // If the artifact is only in one repo, it is unique to this
      if (aArtifactInfo.getDesiredRepoCount () == 1)
        aUnique.addChild (new HCDiv ().addChild (new HCA (aWPEC.getLinkToMenuItem (CLamaMenuView.MENU_ARTIFACTS_ALL)
                                                               .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_VIEW)
                                                               .add (CPageParam.PARAM_OBJECT,
                                                                     aArtifactInfo.getID ())).addChild (aArtifactInfo.getID ())));
    }
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Total artifacts")
                                                 .setCtrl (Integer.toString (aArtifactsOfRepo.size ())));

    if (!aUnique.hasChildren ())
      aUnique.addChild (new HCEM ().addChild ("none"));
    aForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Unique artifacts").setCtrl (aUnique));
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final MavenRepositoryInfo aSelectedObject,
                                                 @Nonnull final FormErrorList aFormErrors,
                                                 @Nonnull final EWebPageFormAction eFormAction)
  {}

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final MavenRepositoryInfo aSelectedObject,
                                @Nonnull final BootstrapForm aForm,
                                @Nonnull final EWebPageFormAction eFormAction,
                                @Nonnull final FormErrorList aFormErrors)
  {}

  @Nonnull
  protected abstract ICommonsCollection <MavenRepositoryInfo> getRelevantRepositories ();

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final HCTable aTable = new HCTable (new DTCol ("ID").setInitialSorting (ESortOrder.ASCENDING),
                                        new DTCol ("URL"),
                                        new DTCol ("Valid?"),
                                        new DTCol ("Layout")).setID (getID ());
    for (final MavenRepositoryInfo aRepoInfo : getRelevantRepositories ())
    {
      final boolean bCreateLink = canCreateLinkTo (aRepoInfo);
      final ISimpleURL aViewURL = createViewURL (aWPEC, aRepoInfo);
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewURL).addChild (aRepoInfo.getID ()));
      aRow.addCell (bCreateLink ? new HCA (new SimpleURL (aRepoInfo.getURL ())).setTargetBlank ()
                                                                               .addChild (aRepoInfo.getURL ())
                                : new HCTextNode (aRepoInfo.getURL ()));
      aRow.addCell (EPhotonCoreText.getYesOrNo (aRepoInfo.isValid (), aDisplayLocale));
      aRow.addCell (aRepoInfo.getRepo ().getLayout ().getID ());
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = BootstrapDataTables.createDefaultDataTables (aWPEC, aTable);
    aNodeList.addChild (aDataTables);
  }
}
