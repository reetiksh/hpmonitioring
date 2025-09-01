package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.UserDetailsService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;

	@Override
	public Map<String, String> getUserLocationDetailsByRole(UserDetails userDetails, UserRole userRole) {
		Map<String, String> data = new HashMap<>();
		try {
			List<UserRoleMapping> locations = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRole);

			if (locations != null && locations.size() > 0) {
				for (UserRoleMapping row : locations) {
					if (!row.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
						data.put(row.getCircleDetails().getCircleId(), row.getCircleDetails().getCircleName());
						continue;
					}
					if (!row.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
						data.put(row.getZoneDetails().getZoneId(), row.getZoneDetails().getZoneName());
						continue;
					}
					if (!row.getStateDetails().getStateId().equalsIgnoreCase("NA")) {
						data.put(row.getStateDetails().getStateId(), row.getStateDetails().getStateName());
						continue;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public String getLocationNameFromUserRoleMapping(UserRoleMapping userRoleMapping) {
		if (!userRoleMapping.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
			return userRoleMapping.getCircleDetails().getCircleName();
		} else if (!userRoleMapping.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
			return userRoleMapping.getZoneDetails().getZoneName();
		} else if (!userRoleMapping.getStateDetails().getStateId().equalsIgnoreCase("NA")) {
			return userRoleMapping.getStateDetails().getStateName();
		}
		return null;
	}

	@Override
	public List<CustomUserDetails> getAllAssignedUserForGivenLocation(String locationId, Integer userRoleId) {
		List<CustomUserDetails> userList = new ArrayList<>();

		LocationDetails locationDetails = locationDetailsRepository.findById(locationId).get();
		List<UserRoleMapping> urmList = new ArrayList<>();
		if (locationDetails.getLocationType().equalsIgnoreCase("CIRCLE")) {
			urmList = userRoleMappingRepository.findbyCircleIdAndUserRoleId(locationId, userRoleId);
		} else if (locationDetails.getLocationType().equalsIgnoreCase("ZONE")) {
			urmList = userRoleMappingRepository.findbyZoneIdAndUserRoleId(locationId, userRoleId);
		} else if (locationDetails.getLocationType().equalsIgnoreCase("STATE")) {
			urmList = userRoleMappingRepository.findbyStateIdAndUserRoleId(locationId, userRoleId);
		}

		if (urmList != null && urmList.size() > 0) {
			for (UserRoleMapping urm : urmList) {
				CustomUserDetails customUserDetails = new CustomUserDetails(urm.getUserDetails().getUserId(),
						urm.getUserDetails().getFirstName()
								+ (urm.getUserDetails().getMiddleName().length() > 0
										? (" " + urm.getUserDetails().getMiddleName())
										: "")
								+ (urm.getUserDetails().getLastName().length() > 0
										? (" " + urm.getUserDetails().getLastName())
										: ""),
						urm.getUserDetails().getDesignation().getDesignationAcronym(),
						urm.getUserDetails().getLoginName());
				userList.add(customUserDetails);
			}
		}

		return userList;
	}

}
