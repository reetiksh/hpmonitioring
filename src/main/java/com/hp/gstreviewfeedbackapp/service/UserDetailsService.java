package com.hp.gstreviewfeedbackapp.service;

import java.util.List;
import java.util.Map;

import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;

public interface UserDetailsService {
	public Map<String, String> getUserLocationDetailsByRole(UserDetails userDetails, UserRole userRole);

	public String getLocationNameFromUserRoleMapping(UserRoleMapping userRoleMapping);

	public List<CustomUserDetails> getAllAssignedUserForGivenLocation(String locationId, Integer userRoleId);
}
