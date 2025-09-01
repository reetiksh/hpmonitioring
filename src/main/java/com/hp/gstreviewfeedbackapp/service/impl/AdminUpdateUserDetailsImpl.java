package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.StateDetails;
import com.hp.gstreviewfeedbackapp.model.TransferRole;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.AdminUpdateUserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.StateDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRoleRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;

@Service
public class AdminUpdateUserDetailsImpl implements AdminUpdateUserDetails {

	private static final Logger logger = LoggerFactory.getLogger(AdminUpdateUserDetailsImpl.class);

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AdminUpdateUserDetailsRepository adminUpdateuserDetailsRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;

	@Autowired
	LocationDetailsRepository locationDetailsRepository;

	@Autowired
	StateDetailsRepository stateDetailsRepository;

	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;

	@Autowired
	private TransferRoleRepository transferRoleRepository;

	@Override
	public List<UserDetails> getAlluserDetails() {

		List<UserDetails> allUser = userDetailsRepository.findAll();
		return allUser;
	}

	@Override
	public String encodeString(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}

	@Override
	public String decodeString(String str) {
		return new String(Base64.getDecoder().decode(str));
	}

	@Override
	public List<UserRole> getAllUserRole() {
		List<UserRole> allUserRole = userRoleRepository.findAll();
		return allUserRole;
	}

	@Override
	public Map<String, String> getAllZoneMap() {
		return categoryRepository.getAllZoneIdAndName();
	}

	@Override
	public Map<String, String> getAllLocationMap() {
		Map<String, String> zoneMap = getAllZoneMap();
		Map<String, String> circleMap = getAllCircleMap();
		Map<String, String> stateMap = getAllCenterMap();

		Map<String, String> allLocationMap = new HashMap<>();
		allLocationMap.putAll(zoneMap);
		allLocationMap.putAll(circleMap);
		allLocationMap.putAll(stateMap);

		return allLocationMap;
	}

	@Override
	public Map<String, String> getAllCenterMap() {

		return categoryRepository.getAllCenterIdAndName();
	}

	@Override
	public Map<String, String> getAllCircleMap() {
		return categoryRepository.getAllCircleIdAndName();
	}

	public String getRoleLocation(UserRoleMapping object) {
		if (!object.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
			return adminUpdateuserDetailsRepository
					.getLocationHierarchyForCircleByCircleId(object.getCircleDetails().getCircleId());
		}
		if (!object.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
			return adminUpdateuserDetailsRepository
					.getLocationHierarchyForZoneByZoneId(object.getZoneDetails().getZoneId());
		}
		if (!object.getStateDetails().getStateId().equalsIgnoreCase("NA")) {
			return adminUpdateuserDetailsRepository
					.getLocationHierarchyForStateByStateId(object.getStateDetails().getStateId());
		}
		return null;
	}

	@Override
	public Map<UserDetails, Map<String, String>> getUserLocationDetails(List<UserDetails> userDetailsList) {
		Map<UserDetails, Map<String, String>> userLocationDetailsMap = null;
		if (userDetailsList != null && userDetailsList.size() > 0) {
			userLocationDetailsMap = new HashMap<>();
			for (UserDetails userDetails : userDetailsList) {
				Map<String, String> map = new HashMap<>();
				List<List<String>> userLocationDetails = userRoleMappingRepository
						.findAllUserRoleAndLocationByUserId(userDetails.getUserId());
				if (userLocationDetails != null && userLocationDetails.size() > 0) {
					String role = userLocationDetails.get(0).get(0);
					String locations = "";
					for (List<String> list : userLocationDetails) {
						if (!role.equalsIgnoreCase(list.get(0))) {
							map.put(role,
									(locations.length() > 0 ? locations.substring(0, locations.length() - 2) : ""));
							role = list.get(0);
							locations = "";
						}
						for (String location : list) {
							if (!location.equalsIgnoreCase("NA") && !role.equalsIgnoreCase(location)) {
								locations = locations + location + ", ";
								break;
							}
						}
					}
					map.put(role, (locations.length() > 0 ? locations.substring(0, locations.length() - 2) : ""));
					userLocationDetailsMap.put(userDetails, map);
				} else {
					userLocationDetailsMap.put(userDetails, null);
				}
			}
		}
		return userLocationDetailsMap;
	}

	public Map<String, String> getStateMap() {
		return categoryRepository.getStateIdAndName();
	}

	@Override
	public List<String> getAllMappedLocationsFromUserRoleMappingList(List<UserRoleMapping> userRoleMapList) {
		List<String> workingLocationsIds = new ArrayList<>();
		try {
			List<UserRoleMapping> stateId = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
					.collect(Collectors.toList());

			if (stateId != null && stateId.size() > 0) {
				workingLocationsIds.addAll(enforcementReviewCaseRepository.finAllLocationsByStateid());
				return workingLocationsIds;
			} else {
				List<UserRoleMapping> zoneIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
						.collect(Collectors.toList());

				List<UserRoleMapping> circleIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())) // commented as per
																									// requirement
																									// verifier can not
																									// be assigned to at
																									// circle level.
						.collect(Collectors.toList());

				if (!circleIds.isEmpty()) {
					for (UserRoleMapping circleIdsSolo : circleIds) {
						workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
					}
				}

				if (!zoneIds.isEmpty()) {
					List<String> onlyZoneIdsList = new ArrayList<String>();
					for (UserRoleMapping zoneIdsSolo : zoneIds) {
						workingLocationsIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
						onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect
																						// circle later
					}

					List<String> allCirclesListUnderZones = enforcementReviewCaseRepository
							.findAllCirclesByZoneIds(onlyZoneIdsList);
					for (String circleSolo : allCirclesListUnderZones) {
						workingLocationsIds.add(circleSolo); // add Circles
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : getAllMappedLocationsFromUserRoleMapping : " + e.getMessage());
		}

		return workingLocationsIds;
	}

	public Map<String, String> getLocationStringFromUserRoleMappingList(UserRoleMapping userRoleMapping) {
		Map<String, String> map = null;
		try {
			if (userRoleMapping != null) {
				map = new HashMap<>();
				List<String> circleList = null;
				if (userRoleMapping.getUserRole().getRoleCode().equalsIgnoreCase("RU")
						|| userRoleMapping.getUserRole().getRoleCode().equalsIgnoreCase("AP")
						|| userRoleMapping.getUserRole().getRoleCode().equalsIgnoreCase("HQ")
						|| userRoleMapping.getUserRole().getRoleCode().equalsIgnoreCase("RM")) {
					/*
					 * if(!userRoleMapping.getStateDeatils().getStateId().equalsIgnoreCase("NA")) {
					 * map.put(userRoleMapping.getStateDeatils().getStateId(),
					 * userRoleMapping.getUserRole().getRoleCode()); //adding state id List<String>
					 * zoneList =
					 * locationDetailsRepository.getAllZoneByStateId(userRoleMapping.getStateDeatils
					 * ().getStateId()); //adding all Zone for the state for(String zd : zoneList) {
					 * map.put(zd, userRoleMapping.getUserRole().getRoleCode()); } //adding circle
					 * for the state circleList =
					 * locationDetailsRepository.getAllCircleByStateId(userRoleMapping.
					 * getStateDeatils().getStateId()); } //adding circle id if(circleList!=null &&
					 * circleList.size()>0) { for(String cd : circleList) { map.put(cd,
					 * userRoleMapping.getUserRole().getRoleCode()); } }
					 */
					List<UserRoleMapping> urmList = new ArrayList<>();
					List<String> allLocations = null;
					urmList.add(userRoleMapping);
					if (!urmList.isEmpty()) {
						allLocations = getAllMappedLocationsFromUserRoleMappingList(urmList);
					}
					if (allLocations != null && allLocations.size() > 0) {
						for (String str : allLocations) {
							map.put(str, userRoleMapping.getUserRole().getRoleCode());
						}
					}
				} else {
					if (!userRoleMapping.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
						map.put(userRoleMapping.getCircleDetails().getCircleId(),
								userRoleMapping.getUserRole().getRoleCode());
					} else if (!userRoleMapping.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
						map.put(userRoleMapping.getZoneDetails().getZoneId(),
								userRoleMapping.getUserRole().getRoleCode());
					} else if (!userRoleMapping.getStateDetails().getStateId().equalsIgnoreCase("NA")) {
						map.put(userRoleMapping.getStateDetails().getStateId(),
								userRoleMapping.getUserRole().getRoleCode());
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : " + e.getMessage());
		}

		return map;
	}

	public void moveEnfCasesFromUserToAnotherUser(UserRoleMapping urm, UserDetails transferToUser,
			UserDetails transferFromUD) {
		try {
			// Map<LocationId, Role>
			Map<String, String> roleLocation = getLocationStringFromUserRoleMappingList(urm);
			if (roleLocation != null && roleLocation.size() > 0) {
				for (Map.Entry<String, String> entry : roleLocation.entrySet()) {
					// Finding all the active cases for the location
					List<EnforcementReviewCaseAssignedUsers> eCaseAssignedUsersList = enforcementReviewCaseAssignedUsersRepository
							.findAllActiveCasesByLocation(entry.getKey());
					// checking if user have the cases with the particular role then change it to
					// new user id
					if (eCaseAssignedUsersList != null && eCaseAssignedUsersList.size() > 0) {
						for (EnforcementReviewCaseAssignedUsers caseAssignedUsers : eCaseAssignedUsersList) {
							if (entry.getValue().equalsIgnoreCase("HQ")) {
								if (caseAssignedUsers.getHqUserId().equals(transferFromUD.getUserId())) {
									Optional<EnforcementReviewCaseAssignedUsers> object = enforcementReviewCaseAssignedUsersRepository
											.findById(caseAssignedUsers.getId());
									EnforcementReviewCaseAssignedUsers saveobject = object.get();
									saveobject.setHqUserId(transferToUser.getUserId());
									enforcementReviewCaseAssignedUsersRepository.save(saveobject);
								}
							} else if (entry.getValue().equalsIgnoreCase("FO")) {
								if (caseAssignedUsers.getFoUserId().equals(transferFromUD.getUserId())) {
									Optional<EnforcementReviewCaseAssignedUsers> object = enforcementReviewCaseAssignedUsersRepository
											.findById(caseAssignedUsers.getId());
									EnforcementReviewCaseAssignedUsers saveobject = object.get();
									saveobject.setFoUserId(transferToUser.getUserId());
									enforcementReviewCaseAssignedUsersRepository.save(saveobject);
								}
							} else if (entry.getValue().equalsIgnoreCase("RU")) {
								if (caseAssignedUsers.getRuUserId().equals(transferFromUD.getUserId())) {
									Optional<EnforcementReviewCaseAssignedUsers> object = enforcementReviewCaseAssignedUsersRepository
											.findById(caseAssignedUsers.getId());
									EnforcementReviewCaseAssignedUsers saveobject = object.get();
									saveobject.setRuUserId(transferToUser.getUserId());
									enforcementReviewCaseAssignedUsersRepository.save(saveobject);
								}
							} else if (entry.getValue().equalsIgnoreCase("AP")) {
								if (caseAssignedUsers.getApUserId().equals(transferFromUD.getUserId())) {
									Optional<EnforcementReviewCaseAssignedUsers> object = enforcementReviewCaseAssignedUsersRepository
											.findById(caseAssignedUsers.getId());
									EnforcementReviewCaseAssignedUsers saveobject = object.get();
									saveobject.setApUserId(transferToUser.getUserId());
									enforcementReviewCaseAssignedUsersRepository.save(saveobject);
								}
							} else if (entry.getValue().equalsIgnoreCase("RM")) {
								if (caseAssignedUsers.getRmUserId().equals(transferFromUD.getUserId())) {
									Optional<EnforcementReviewCaseAssignedUsers> object = enforcementReviewCaseAssignedUsersRepository
											.findById(caseAssignedUsers.getId());
									EnforcementReviewCaseAssignedUsers saveobject = object.get();
									saveobject.setRmUserId(transferToUser.getUserId());
									enforcementReviewCaseAssignedUsersRepository.save(saveobject);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : moveEnfCasesFromUserToAnotherUser : " + e.getMessage());
		}
	}

	public int findEnfCasesFromUserRoleMapping(UserRoleMapping urm) {
		int caseCount = 0;
		try {
			// Map<Role, LocationId>
			Map<String, String> roleLocation = getLocationStringFromUserRoleMappingList(urm);
			if (roleLocation != null && roleLocation.size() > 0) {
				Integer userId = urm.getUserDetails().getUserId();
				for (Map.Entry<String, String> entry : roleLocation.entrySet()) {
					// Finding all the active cases for the location
					List<EnforcementReviewCaseAssignedUsers> eCaseAssignedUsersList = enforcementReviewCaseAssignedUsersRepository
							.findAllActiveCasesByLocation(entry.getKey());
					// checking if user have the cases with the particular role then count the case
					if (eCaseAssignedUsersList != null && eCaseAssignedUsersList.size() > 0) {
						for (EnforcementReviewCaseAssignedUsers caseAssignedUsers : eCaseAssignedUsersList) {
							if (entry.getValue().equalsIgnoreCase("HQ")) {
								if (caseAssignedUsers.getHqUserId().equals(userId)) {
									caseCount++;
								}
							} else if (entry.getValue().equalsIgnoreCase("FO")) {
								if (caseAssignedUsers.getFoUserId().equals(userId)) {
									caseCount++;
								}
							} else if (entry.getValue().equalsIgnoreCase("RU")) {
								if (caseAssignedUsers.getRuUserId().equals(userId)) {
									caseCount++;
								}
							} else if (entry.getValue().equalsIgnoreCase("AP")) {
								if (caseAssignedUsers.getApUserId().equals(userId)) {
									caseCount++;
								}
							} else if (entry.getValue().equalsIgnoreCase("RM")) {
								if (caseAssignedUsers.getRmUserId().equals(userId)) {
									caseCount++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : findEnfCasesFromUserRoleMapping : " + e.getMessage());
		}
		return caseCount;
	}

	public boolean checkIfUserHaveStateRole(UserDetails userDetails, UserRole userRole) {
		boolean flag = false;
		Optional<StateDetails> stateDetails = stateDetailsRepository.findById("HP");
		if (stateDetails.isPresent() && userDetails != null && userRole != null) {
			Optional<UserRoleMapping> object = userRoleMappingRepository
					.findByUserDetailsAndUserRoleAndStateDetails(userDetails, userRole, stateDetails.get());
			if (object.isPresent()) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean checkIfUserHaveZoneRole(UserDetails userDetails, UserRole userRole, String locationType) {
		boolean flag = false;
		if (userDetails != null && userRole != null) {
			List<UserRoleMapping> object = null;
			if (locationType.equalsIgnoreCase("ENFORCEMENT ZONE")) {
				object = userRoleMappingRepository.findByUserIdAndUserRoleIdAndEnfZoneList(userDetails.getUserId(),
						userRole.getId());
			} else if (locationType.equalsIgnoreCase("ZONE")) {
				object = userRoleMappingRepository.findByUserIdAndUserRoleIdAndZoneList(userDetails.getUserId(),
						userRole.getId());
			}

			if ((object != null && object.size() > 0)
					|| checkIfUserHaveZoneTransferRole(userDetails, userRole, locationType)) {
				flag = true;
			}
		}
		return flag;
	}

	private boolean checkIfUserHaveZoneTransferRole(UserDetails userDetails, UserRole userRole, String locationType) {
		boolean flag = false;
		if (locationType.equalsIgnoreCase("ENFORCEMENT ZONE")) {
			List<TransferRole> object = transferRoleRepository
					.findAllTransferRoleForEnfZoneByUserDetailsAndUserRole(userDetails.getUserId(), userRole.getId());
			if (object != null && object.size() > 0) {
				flag = true;
			}
		} else if (locationType.equalsIgnoreCase("ZONE")) {
			List<TransferRole> object = transferRoleRepository
					.findAllTransferRoleForZoneByUserDetailsAndUserRole(userDetails.getUserId(), userRole.getId());
			if (object != null && object.size() > 0) {
				flag = true;
			}
		}
		return flag;
	}

	public String getRoleLocationNames(UserRoleMapping userRoleMapping) {
		String data = null;
		try {
			if (!userRoleMapping.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
				data = userRoleMapping.getUserRole().getRoleName() + " : "
						+ userRoleMapping.getCircleDetails().getCircleName();
			} else if (!userRoleMapping.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
				data = userRoleMapping.getUserRole().getRoleName() + " : "
						+ userRoleMapping.getZoneDetails().getZoneName();
			} else if (!userRoleMapping.getStateDetails().getStateId().equalsIgnoreCase("NA")) {
				data = userRoleMapping.getUserRole().getRoleName() + " : "
						+ userRoleMapping.getStateDetails().getStateName();
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : getRoleLocationNames : " + e.getMessage());
		}
		return data;
	}

	@Override
	public List<String> getAllHigherMappedLocationsFromUserRoleMapping(UserRoleMapping urm) {
		// TODO Auto-generated method stub
		List<String> allLocations = new ArrayList<>();
		try {
			if (!urm.getCircleDetails().getCircleId().equalsIgnoreCase("NA")) {
				List<String> list = locationDetailsRepository
						.getHigherMappedLocationForCircle(urm.getCircleDetails().getCircleId());
				if (!list.isEmpty()) {
					List<String> tempList = Arrays.asList(list.get(0).split(","));
					allLocations = new ArrayList<>(tempList);
				}
				return allLocations;
			} else if (!urm.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
				List<String> list = locationDetailsRepository
						.getHigherMappedLocationForZone(urm.getZoneDetails().getZoneId());
				if (!list.isEmpty()) {
					List<String> tempList = Arrays.asList(list.get(0).split(","));
					allLocations = new ArrayList<>(tempList);
				}
				return allLocations;
			}
		} catch (Exception e) {
			logger.error(
					"AdminUpdateUserDetailsImpl : getAllHigherMappedLocationsFromUserRoleMapping : " + e.getMessage());
		}
		return allLocations;
	}
}
