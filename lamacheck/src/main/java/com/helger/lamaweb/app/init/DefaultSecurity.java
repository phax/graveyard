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
package com.helger.lamaweb.app.init;

import javax.annotation.concurrent.Immutable;

import com.helger.lamaweb.app.CLamaSecurity;
import com.helger.photon.security.CSecurity;
import com.helger.photon.security.mgr.PhotonSecurityManager;
import com.helger.photon.security.role.RoleManager;
import com.helger.photon.security.user.UserManager;
import com.helger.photon.security.usergroup.UserGroupManager;

@Immutable
public final class DefaultSecurity
{
  private DefaultSecurity ()
  {}

  public static void init ()
  {
    final UserManager aUserMgr = PhotonSecurityManager.getUserMgr ();
    final UserGroupManager aUserGroupMgr = PhotonSecurityManager.getUserGroupMgr ();
    final RoleManager aRoleMgr = PhotonSecurityManager.getRoleMgr ();

    // Standard users
    if (!aUserMgr.containsWithID (CSecurity.USER_ADMINISTRATOR_ID))
      aUserMgr.createPredefinedUser (CSecurity.USER_ADMINISTRATOR_ID,
                                     CSecurity.USER_ADMINISTRATOR_EMAIL,
                                     CSecurity.USER_ADMINISTRATOR_EMAIL,
                                     CSecurity.USER_ADMINISTRATOR_PASSWORD,
                                     CSecurity.USER_ADMINISTRATOR_NAME,
                                     null,
                                     null,
                                     null,
                                     null,
                                     false);

    // Create all roles
    if (!aRoleMgr.containsWithID (CLamaSecurity.ROLEID_CONFIG))
      aRoleMgr.createPredefinedRole (CLamaSecurity.ROLEID_CONFIG, "Config user", null, null);
    if (!aRoleMgr.containsWithID (CLamaSecurity.ROLEID_VIEW))
      aRoleMgr.createPredefinedRole (CLamaSecurity.ROLEID_VIEW, "View user", null, null);

    // User group Administrators
    if (!aUserGroupMgr.containsWithID (CLamaSecurity.USERGROUPID_SUPERUSER))
    {
      aUserGroupMgr.createPredefinedUserGroup (CLamaSecurity.USERGROUPID_SUPERUSER, "Administrators", null, null);
      // Assign administrator user to UG administrators
      aUserGroupMgr.assignUserToUserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID, CSecurity.USER_ADMINISTRATOR_ID);
    }
    aUserGroupMgr.assignRoleToUserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID, CLamaSecurity.ROLEID_CONFIG);
    aUserGroupMgr.assignRoleToUserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID, CLamaSecurity.ROLEID_VIEW);

    // User group Config users
    if (!aUserGroupMgr.containsWithID (CLamaSecurity.USERGROUPID_CONFIG))
      aUserGroupMgr.createPredefinedUserGroup (CLamaSecurity.USERGROUPID_CONFIG, "Config user", null, null);
    aUserGroupMgr.assignRoleToUserGroup (CLamaSecurity.USERGROUPID_CONFIG, CLamaSecurity.ROLEID_CONFIG);

    // User group Shop users
    if (!aUserGroupMgr.containsWithID (CLamaSecurity.USERGROUPID_VIEW))
      aUserGroupMgr.createPredefinedUserGroup (CLamaSecurity.USERGROUPID_VIEW, "View user", null, null);
    aUserGroupMgr.assignRoleToUserGroup (CLamaSecurity.USERGROUPID_VIEW, CLamaSecurity.ROLEID_VIEW);
  }
}
