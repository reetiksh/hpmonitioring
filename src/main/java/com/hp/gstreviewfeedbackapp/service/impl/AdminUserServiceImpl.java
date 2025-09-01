package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.TransferRole;
import com.hp.gstreviewfeedbackapp.model.TransferRoleLogs;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.UserRoleMappingLogs;
import com.hp.gstreviewfeedbackapp.repository.TransferRoleLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUserService;

@Service
public class AdminUserServiceImpl implements AdminUserService {
	@Autowired
	private TransferRoleLogsRepository transferRoleLogsRepository;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private UserRoleMappingLogsRepository userRoleMappingLogsRepository;

	@Override
	public void saveTransferRoleLogs(TransferRole transferRole, String action) {
		TransferRoleLogs transferRoleLogs = new TransferRoleLogs(
				transferRole.getUserRoleMapping().getUserDetails().getUserId(),
				transferRole.getUserRoleMapping().getUserRole().getRoleCode(),
				userDetailsServiceImpl.getLocationNameFromUserRoleMapping(transferRole.getUserRoleMapping()),
				transferRole.getTransferToUser().getUserId(), transferRole.getActionDate(),
				transferRole.getRequestedByUserId().getUserId(), new Date(), action);

		if (transferRoleLogs != null) {
			transferRoleLogsRepository.save(transferRoleLogs);
		}
	}

	@Override
	public void saveUserRoleMappinglogs(UserDetails loginUserDetails, UserRoleMapping userRoleMapping,
			String actionTaken) {
		UserRoleMappingLogs uRoleMappingLogs = new UserRoleMappingLogs(
				userDetailsServiceImpl.getLocationNameFromUserRoleMapping(userRoleMapping), new Date(),
				loginUserDetails.getUserId(), userRoleMapping.getUserRole().getRoleCode(),
				userRoleMapping.getUserDetails().getUserId(), actionTaken, userRoleMapping.getUserRoleMappingId());

		userRoleMappingLogsRepository.save(uRoleMappingLogs);
	}
}
