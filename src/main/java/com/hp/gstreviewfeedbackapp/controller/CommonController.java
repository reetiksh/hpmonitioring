package com.hp.gstreviewfeedbackapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.ChangePasswordData;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;

@Controller
@RequestMapping("/" + ApplicationConstants.GENERIC_USER)
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CasePertainUserNotification casePertainUserNotification;

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (!object.isPresent()) {
			return "redirect:/logout";
		}

		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setGenericUserDetailsList(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_DEFAULT_USER_DETAILS;
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_CHANGE_PASSWORD)
	public String getChangePasswordPage(Model model, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (!object.isPresent()) {
			return "redirect:/logout";
		}

		logger.info("CommonController.getChangePasswordPage() : ApplicationConstants.GENERIC_USER_CHANGE_PASSWORD");
		setGenericUserDetailsList(model, ApplicationConstants.GENERIC_USER_CHANGE_PASSWORD);
		model.addAttribute("returnToUrl", request.getHeader("referer"));
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_CHANGE_PASSWORD;
	}

	@PostMapping("/" + ApplicationConstants.GENERIC_USER_RESET_PASSWORD)
	public ResponseEntity<String> resetUserPassword(@RequestBody ChangePasswordData changePasswordData, Model model) {

		logger.info("CommonController.resetUserPassword() : ApplicationConstants.GENERIC_USER_RESET_PASSWORD");

		JSONObject object = new JSONObject();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object1 = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (object1.isPresent()) {

			UserDetails userDetails = null;
			if (object1.isPresent()) {
				userDetails = object1.get();
			}

			if (passwordEncoder.matches(changePasswordData.getOldPassword(), userDetails.getPassword())) {
				logger.info("password matches");
				userDetails.setPassword(passwordEncoder.encode(changePasswordData.getNewPassword()));
				userDetailsRepository.save(userDetails);
				object.put("error1", "pass_matched");
			} else {
				object.put("error1", "Please enter correct old password");
				return ResponseEntity.ok(object.toString());
			}
		}
		return ResponseEntity.ok(object.toString());

	}

	/************* convert unread to read Notification Start **************/
	@PostMapping("/" + ApplicationConstants.CONVERT_UNREAD_TO_READ_NOTIFICATIONS)
	// @ResponseBody
	public void convertUnreadToReadNotifications(Model model, HttpServletRequest httpServletRequest) {
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "RU");
			}

		}

	}

	/************* convert unread to read Notification End **************/

	private void setGenericUserDetailsList(Model model, String activeMenu) {

		UserDetails userDetails = new UserDetails();
		List<UserRoleMapping> userRoleMappings = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();

				userRoleMappings = userRoleMappingRepository.findByUserDetailsOrderByUserRole(userDetails);

				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
				model.addAttribute("designationAcronym", userDetails.getDesignation().getDesignationAcronym());
				model.addAttribute("userIdToChangePassword", userDetails.getUserId());

				/*************************
				 * to display user generic details start
				 ****************************/
				List<Integer> distinctUserRoleIds = new ArrayList<>();
				List<UserRoleMapping> uniqueRolesList = new ArrayList<>();
				UserProfileDetails userProfileDetails = new UserProfileDetails();
				userProfileDetails.setUserName(userDetails.getLoginName());
				userProfileDetails.setMob(userDetails.getMobileNumber());
				userProfileDetails.setEmailId(userDetails.getEmailId());
				userProfileDetails.setDob(userDetails.getDateOfBirth());
				userProfileDetails.setFirstName(userDetails.getFirstName());
				userProfileDetails.setLastName(userDetails.getLastName());
				userProfileDetails.setDesignation(userDetails.getDesignation().getDesignationName());
				model.addAttribute("activeMenu", activeMenu);
				for (UserRoleMapping userRoleSolo : userRoleMappings) {
					if ((userRoleSolo.getUserRole().getRoleName() != null)
							&& (!distinctUserRoleIds.contains(userRoleSolo.getUserRole().getId()))) {
						distinctUserRoleIds.add(userRoleSolo.getUserRole().getId());
						uniqueRolesList.add(userRoleSolo);
					}

				}
				List<String> roleNameList = uniqueRolesList.stream().map(mapping -> mapping.getUserRole().getRoleName())
						.collect(Collectors.toList());

				String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));

				String LocationsName = returnLocationsName(userRoleMappings);
				userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
				userProfileDetails.setAssignedWorkingLocations(LocationsName);
				model.addAttribute("userProfileDetails", userProfileDetails);
				model.addAttribute("changeUserPassword", "/gu/change_password");
				model.addAttribute("homePageLink", "/gu/welcome");

				/*************************
				 * to display user generic details end
				 ****************************/
			}
		}
	}

	/**************** return locations name start ******************/

	private String returnLocationsName(List<UserRoleMapping> userRoleMapList) {
		List<String> returnResultentSet = new ArrayList<>();
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());

		List<UserRoleMapping> zoneIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());

		List<UserRoleMapping> circleIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())).collect(Collectors.toList());

		if (!stateId.isEmpty()) {
			returnResultentSet.add(stateId.get(0).getStateDetails().getStateName());
		}

		if (!zoneIds.isEmpty()) {
			for (UserRoleMapping zoneIdsIdsSolo : zoneIds) {
				returnResultentSet.add(zoneIdsIdsSolo.getZoneDetails().getZoneName());
			}
		}

		if (!circleIds.isEmpty()) {
			for (UserRoleMapping circleIdsSolo : circleIds) {
				returnResultentSet.add(circleIdsSolo.getCircleDetails().getCircleName());
			}
		}

		String commaSeperatedLocationsNameList = returnResultentSet.stream().collect(Collectors.joining(", "));

		return commaSeperatedLocationsNameList;
	}

	/**************** return locations name end ******************/

}
