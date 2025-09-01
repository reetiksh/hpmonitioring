package com.hp.gstreviewfeedbackapp.service;

import java.util.List;
import java.util.Map;

import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;

public interface AdminUpdateUserDetails {
	public List<UserDetails> getAlluserDetails();

	public String encodeString(String str);

	public String decodeString(String str);

	public List<UserRole> getAllUserRole();

	public Map<String, String> getAllZoneMap();

	public Map<String, String> getAllLocationMap();

	public Map<String, String> getAllCircleMap();

	public Map<UserDetails, Map<String, String>> getUserLocationDetails(List<UserDetails> userDetailsList);

	public List<String> getAllMappedLocationsFromUserRoleMappingList(List<UserRoleMapping> userRoleMapList);

	public List<String> getAllHigherMappedLocationsFromUserRoleMapping(UserRoleMapping urm);

	public Map<String, String> getAllCenterMap();
}
