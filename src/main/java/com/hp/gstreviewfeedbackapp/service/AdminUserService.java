package com.hp.gstreviewfeedbackapp.service;

import com.hp.gstreviewfeedbackapp.model.TransferRole;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;

public interface AdminUserService {
	public void saveTransferRoleLogs(TransferRole transferRole, String action);

	public void saveUserRoleMappinglogs(UserDetails loginUserDetails, UserRoleMapping userRoleMapping, String string);
}
