package com.hp.gstreviewfeedbackapp.enforcement.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementDashboardSummaryDTO;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementHqUserLogsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterAllocatingUserRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementFieldOfficeService;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.ENFORCEMENT_SVO)
public class EnforcementSupervisoryOfficer {
	private static final Logger logger = LoggerFactory.getLogger(EnforcementSupervisoryOfficer.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private CustomUtility customUtility;
	@Autowired
	private EnforcementMasterRepository enforcementMasterRepository;
	@Autowired
	private EnforcementHqUserLogsRepository enforcementHqUserLogsRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private EnforcementMasterAllocatingUserRepository enfMasterAllocatingUserRepository;
	@Value("${upload.enforcement.directory}")
	private String HqPdfFileUploadLocation;
	@Autowired
	private EnforcementFieldOfficeService enforcementFieldOfficeService;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(@RequestParam(name = "category", required = false, defaultValue = "-1") String parameterId,
	        @RequestParam(name = "financialyear", required = false, defaultValue = "-1") String period,
	        Model model) {
		setEnfSvoMenu(model, ApplicationConstants.DASHBOARD);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(
				objectUserDetails.get(), userRoleRepository.findByroleCode("Enforcement_SVO").get());
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		model.addAttribute("totalEnforcementCases", enforcementMasterRepository.findTotalCountOfCasesAsLocations(allMappedLocations));
		model.addAttribute("totalSumOfIndicativeTaxValue",
				enforcementMasterRepository.findTotalSumOfIndicativeTaxValueAsLocations(allMappedLocations));
		model.addAttribute("totalEnforcementCasesInLast3Months",
				enforcementHqUserLogsRepository.findTotalCountOfUploadedCasesInLast3Months());
		model.addAttribute("countOfActiontakenCases", enforcementMasterRepository.findTotalCountOfActiontakenCases());
		model.addAttribute("countOfPendingCases",
				enforcementMasterRepository.findTotalCountOfCasesPendingWithEnfSvoByUserIdListAndActionList(
						new ArrayList<>(Arrays.asList(0, objectUserDetails.get().getUserId())),
						new ArrayList<>(Arrays.asList("investigationPermissionRequired")), allMappedLocations));
		model.addAttribute("countOfCasesActionByUser", enfMasterAllocatingUserRepository
				.totalCountOfCasesActionTakenByEnfSvoUserId(objectUserDetails.get().getUserId()));
		model.addAttribute("categories", enforcementMasterRepository.findcategoriesenforcement());
		model.addAttribute("financialyearlist", enforcementMasterRepository.findfinancialyearlistenforcement());

		
		List<EnforcementDashboardSummaryDTO> summaryList =
		        enforcementFieldOfficeService.getDashboardSummaryByParamAndPeriod(parameterId, period,allMappedLocations);

		    model.addAttribute("dashboardSummary", summaryList);
		    model.addAttribute("category", parameterId);
		    model.addAttribute("year", period);
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : Dashboard : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.SVO + "/" + ApplicationConstants.DASHBOARD;
	}
	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?parameterId=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() ;
	}
	@GetMapping("/" + ApplicationConstants.PERMISSION_FOR_INVESTIGATION)
	public String updateEnforcementCases(Model model, @RequestParam(required = false) Integer categoryId) {
		try {
			setEnfSvoMenu(model, ApplicationConstants.PERMISSION_FOR_INVESTIGATION);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			List<EnforcementMaster> enforcementCases = null;
			// Getting all user role mapping for this user and getting all mapped location
			List<UserRoleMapping> allURMId = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRoleRepository.findByroleCode("Enforcement_SVO").get());
			List<String> allMappedLocations = adminUpdateUserDetails.getAllMappedLocationsFromUserRoleMappingList(allURMId);
			if (allMappedLocations != null && allMappedLocations.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementSvoCasesByWorkingLocationListAndActionListAndUserIdList(
									allMappedLocations,
									new ArrayList<>(Arrays.asList("investigationPermissionRequired")),
									new ArrayList<>(Arrays.asList(0, userDetails.getUserId())));
				} else {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
									allMappedLocations, categoryId,
									new ArrayList<>(Arrays.asList("investigationPermissionRequired")),
									new ArrayList<>(Arrays.asList(0, userDetails.getUserId())));
					model.addAttribute("categoryId", categoryId);
				}
				if (enforcementCases != null) {
					Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
							.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
					enforcementCases.forEach(enforcementCase -> {
						List<String> parameterNames = Arrays.stream(enforcementCase.getParameter().split(","))
								.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
								.collect(Collectors.toList());
						enforcementCase.setParametersNameList(
								parameterNames.toString().substring(1, parameterNames.toString().length() - 1));
					});
					model.addAttribute("enforcementCases", enforcementCases);
				}
			}
			// Set Category
			List<Category> categoryList = categoryListRepository
					.findAllCategoryOfEnfSvoCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
							allMappedLocations, new ArrayList<>(Arrays.asList(0, userDetails.getUserId())),
							new ArrayList<>(Arrays.asList("investigationPermissionRequired")), "Enforcement_SVO");
			if (categoryList != null && categoryList.size() > 0) {
				model.addAttribute("categories", categoryList);
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : updateEnforcementCases : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.SVO + "/"
				+ ApplicationConstants.PERMISSION_FOR_INVESTIGATION;
	}

	@GetMapping("/downloadHqPdfFile")
	public ResponseEntity<Resource> downloadHqPdfFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFile().initiate" + fileName);
			String filesDirectory = HqPdfFileUploadLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("L3 Controller.downloadHqPdfFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("L3 Controller.downloadHqPdfFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private void setEnfSvoMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,
					ApplicationConstants.ENFORCEMENT_SVO));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "Enforcement_SVO");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository
						.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();
					List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
							.findByUserDetailsOrderByUserRole(userDetails);
					/***************************
					 * get user locations role wise start
					 **************************/
					List<String> userRoleMapWithLocations = customUtility.roleMapWithLocations(userRoleMappings,
							userDetails);
					model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
					/***************************
					 * get user locations role wise end
					 **************************/
					// All Roles of the user
					Map<String, UserRole> userRoles = new HashMap<>();
					for (UserRoleMapping objectUrm : userRoleMappings) {
						if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
							userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
						}
					}
					model.addAttribute("UserLoginName", userDetails.getLoginName());
					model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
					model.addAttribute("commonUserDetails", "/enforcement_svo/user_details");
					model.addAttribute("changeUserPassword", "/gu/change_password");
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
					for (UserRoleMapping userRoleSolo : userRoleMappings) {
						if ((userRoleSolo.getUserRole().getRoleName() != null)
								&& (!distinctUserRoleIds.contains(userRoleSolo.getUserRole().getId()))) {
							distinctUserRoleIds.add(userRoleSolo.getUserRole().getId());
							uniqueRolesList.add(userRoleSolo);
						}
					}
					List<String> roleNameList = uniqueRolesList.stream()
							.map(mapping -> mapping.getUserRole().getRoleName()).collect(Collectors.toList());
					String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));
					String LocationsName = returnLocationsName(userRoleMappings);
					userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
					userProfileDetails.setAssignedWorkingLocations(LocationsName);
					model.addAttribute("userProfileDetails", userProfileDetails);
					/*************************
					 * to display user generic details end
					 ****************************/
				}
			}
		} catch (Exception e) {
			logger.error("Error :: setAdminMenu" + e.getMessage());
		}
	}

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
	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("EnforcementSupervisoryOfficer.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setEnfSvoMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
}
