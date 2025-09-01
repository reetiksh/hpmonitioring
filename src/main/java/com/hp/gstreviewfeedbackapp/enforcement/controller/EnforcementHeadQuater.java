package com.hp.gstreviewfeedbackapp.enforcement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementDashboardSummaryDTO;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementActionStatusRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementHqUserLogsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementFieldOfficeService;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementHQUserUploadEnforcementCasesService;
import com.hp.gstreviewfeedbackapp.enforcement.service.impl.EnforcementHQUserUploadEnforcementCasesServiceImpl;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesStatus;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Controller
@RequestMapping("/" + ApplicationConstants.ENFORCEMENT_HQ)
public class EnforcementHeadQuater {
	private static final Logger logger = LoggerFactory.getLogger(EnforcementHeadQuater.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private CustomUtility customUtility;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private EnforcementHQUserUploadEnforcementCasesService enforcementHQUserUploadEnforcementCasesService;
	@Autowired
	private EnforcementMasterRepository enforcementMasterRepository;
	@Autowired
	private EnforcementHqUserLogsRepository enforcementHqUserLogsRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private EnforcementFieldOfficeService enforcementFieldOfficeService;
	@Autowired
	private EnforcementActionStatusRepository enforcementActionStatusRepository;
	@Autowired
	private CommonUtility commonUtility;
	@Autowired
	private EnforcementHQUserUploadEnforcementCasesServiceImpl enforcementHQUserUploadEnforcementCasesServiceImpl;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(@RequestParam(name = "category", required = false, defaultValue = "-1") String parameterId,
	        @RequestParam(name = "financialyear", required = false, defaultValue = "-1") String period,
	        Model model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(
				objectUserDetails.get(), userRoleRepository.findByroleCode("Enforcement_HQ").get());
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		setHqMenu(model, ApplicationConstants.DASHBOARD);
		model.addAttribute("totalEnforcementCases",enforcementMasterRepository.findTotalCountOfCasesAsLocations(allMappedLocations));
		model.addAttribute("totalSumOfIndicativeTaxValue",
				enforcementMasterRepository.findTotalSumOfIndicativeTaxValueAsLocations(allMappedLocations));
		model.addAttribute("totalEnforcementCasesInLast3Months",
				enforcementHqUserLogsRepository.findTotalCountOfUploadedCasesInLast3Months());
		model.addAttribute("countOfActiontakenCases", enforcementMasterRepository.findTotalCountOfActiontakenCases());
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
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.HQ + "/" + ApplicationConstants.DASHBOARD;
	}
	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?parameterId=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() ;
	}

	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES)
	public String uploadAuditCases(Model model) {
		setHqMenu(model, ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES);
		List<Category> auditCaseCategoryList = categoryListRepository.findAllByActiveStatusAndModule(true,
				"enforcement");
		model.addAttribute("categories", auditCaseCategoryList);
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.HQ + "/"
				+ ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES;
	}

	@PostMapping("/" + ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES)
	public String uploadExcelFile(@ModelAttribute("HqUploadForm") @Valid HQUploadForm HqUploadForm,
			BindingResult formResult, Model model, RedirectAttributes redirectAttributes) {
		MultipartFile excelFile = null;
		MultipartFile pdfFile = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			if (userDetails == null) {
				return "redirect:/logout";
			}
			PdfData pdfData = HqUploadForm.getPdfData();
			ExcelData excelData = HqUploadForm.getExcelData();
			redirectAttributes.addFlashAttribute("selectedCategory",
					categoryListRepository.findById(Long.parseLong(HqUploadForm.getCategory())).get().getName());
			Map<String, List<List<String>>> excelDataValidationMap = null;
			if (excelData != null) {
				excelFile = excelData.getExcelFile();
				if (excelFile != null && !excelFile.isEmpty()) {
					// full validation
					excelDataValidationMap = excelValidator.validateExcelAndExtractDataForEnformentCases(excelFile,
							userDetails.getUserId());
					if (excelDataValidationMap.get("uploadData") != null) {
						redirectAttributes.addFlashAttribute("uploadData", excelDataValidationMap.get("uploadData"));
					}
					if (excelDataValidationMap.get("errorList") != null) {
						redirectAttributes.addFlashAttribute("errorList",
								excelDataValidationMap.get("errorList").get(0));
						return "redirect:/" + ApplicationConstants.ENFORCEMENT_HQ + "/"
								+ ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES;
					}
				}
			}
			pdfFile = pdfData.getPdfFile();
			excelFile = excelData.getExcelFile();
			String uploadDataFlag = "";
			if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty()
					&& excelDataValidationMap.get("uploadData") != null
					&& excelDataValidationMap.get("errorList") == null) {
				uploadDataFlag = enforcementHQUserUploadEnforcementCasesService.saveHqUploadEnforcementDataList(
						HqUploadForm.getExtensionNo(), HqUploadForm.getCategory(), pdfFile, userDetails.getUserId(),
						excelDataValidationMap.get("uploadData"));
			}
			if (uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
				redirectAttributes.addFlashAttribute("successMessage",
						ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
			} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
				List<String> errorList = new ArrayList<>();
				errorList.add(uploadDataFlag);
				redirectAttributes.addFlashAttribute("errorList", errorList);
			} else {
				List<String> errorList = new ArrayList<>();
				errorList.add(uploadDataFlag);
				redirectAttributes.addFlashAttribute("errorList", errorList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L1UserController : uploadExcelFile : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.ENFORCEMENT_HQ + "/"
				+ ApplicationConstants.ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES;
	}

	private void setHqMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,
					ApplicationConstants.ENFORCEMENT_HQ));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "Enforcement_HQ");
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
					model.addAttribute("commonUserDetails", "/enforcement_hq/user_details");
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
		logger.info("EnforcementHeadQuater.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setHqMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
	//
	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST)
	public String getEnforcementReviewCasesList(Model model) {
		logger.info("EnforcementHeadQuater.getEnforcementReviewCasesList() : ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		setHqMenu(model, ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST);
		model.addAttribute("caseStatusList", enforcementActionStatusRepository.findAllWithOrder()); 
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.EN_HQ + "/en_review_cases_list" ;
	}
	
	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST +"/{id}")
	public String getEnforcementReviewCasesStatusWiseList(Model model,  @PathVariable Integer id) {
		logger.info("EnforcementHeadQuater.getEnforcementReviewCasesStatusWiseList() : ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST/{id}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"Enforcement_HQ");
		List<EnforcementMaster> reviewCasesList = enforcementHQUserUploadEnforcementCasesServiceImpl.getReviewCasesListStatusWise(id,workingLoacationList);
		setHqMenu(model, ApplicationConstants.ENFORCEMENT_REVIEW_CASES_LIST);
		
		model.addAttribute("caseStatusList", enforcementActionStatusRepository.findAllWithOrder());
		model.addAttribute("reviewCasesList", reviewCasesList);
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.EN_HQ + "/" + ApplicationConstants.ENFORCEMENT_HQ_REVIEW_CASES_STATUS_WISE;
	}
}
