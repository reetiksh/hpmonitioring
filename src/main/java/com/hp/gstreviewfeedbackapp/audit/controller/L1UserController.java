package com.hp.gstreviewfeedbackapp.audit.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.service.L1UserUploadAuditCasesService;
import com.hp.gstreviewfeedbackapp.audit.service.util.ExcelValidatorForUploadAuditCases;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.L1)
public class L1UserController {
	private static final Logger logger = LoggerFactory.getLogger(L1UserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private ExcelValidatorForUploadAuditCases excelValidatorForUploadAuditCases;
	@Autowired
	private L1UserUploadAuditCasesService l1UserUploadAuditCasesService;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private MstParametersModuleWiseRepository parametersModuleWiseRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Value("${audit.case.upload}")
	private String auditCaseUploadCategory;
	@Value("${upload.audit.directory}")
	private String hqPdfFileUploadLocation;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String index(Model model) {
		setL1Menu(model, ApplicationConstants.DASHBOARD);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L1").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
        List<AnnexureReportRow> report = l1UserUploadAuditCasesService.getAnnexureReport(allMappedLocations);
        List<String> data = l1UserUploadAuditCasesService.getDashboardData(allMappedLocations);
        model.addAttribute("allotted_cases", data.get(0));
        model.addAttribute("assigned_cases", data.get(1));
        model.addAttribute("audit_cases_completed", data.get(2));
        model.addAttribute("pending_audit_plan", data.get(3));
        model.addAttribute("pending_DAR", data.get(4));
        model.addAttribute("pending_FAR", data.get(5));
        model.addAttribute("reportData", report);
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L1 + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.L1_UPLOAD_AUDIT_CASES)
	public String uploadAuditCases(Model model) {
		setL1Menu(model, ApplicationConstants.L1_UPLOAD_AUDIT_CASES);
		List<Category> auditCaseCategoryList = categoryListRepository.findAllByActiveStatusAndModule(true, "audit");
		model.addAttribute("categories", auditCaseCategoryList);
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L1 + "/"
				+ ApplicationConstants.L1_UPLOAD_AUDIT_CASES;
	}

	@PostMapping("/" + ApplicationConstants.L1_UPLOAD_AUDIT_CASES)
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
					excelDataValidationMap = excelValidatorForUploadAuditCases
							.validateExcelAndExtractDataForUploadAuditCases(excelFile);
					if (excelDataValidationMap.get("uploadData") != null) {
						redirectAttributes.addFlashAttribute("uploadData", excelDataValidationMap.get("uploadData"));
					}
					if (excelDataValidationMap.get("errorList") != null) {
						redirectAttributes.addFlashAttribute("errorList",
								excelDataValidationMap.get("errorList").get(0));
						return "redirect:/" + ApplicationConstants.L1 + "/"
								+ ApplicationConstants.L1_UPLOAD_AUDIT_CASES;
					}
				}
			}
			pdfFile = pdfData.getPdfFile();
			excelFile = excelData.getExcelFile();
			String uploadDataFlag = "";
			if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty()
					&& excelDataValidationMap.get("uploadData") != null
					&& excelDataValidationMap.get("errorList") == null) {
				uploadDataFlag = l1UserUploadAuditCasesService.saveL1UploadAuditDataList(HqUploadForm.getExtensionNo(),
						HqUploadForm.getCategory(), pdfFile, userDetails.getUserId(),
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
			logger.error("L1UserController : uploadExcelFile : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.L1 + "/" + ApplicationConstants.L1_UPLOAD_AUDIT_CASES;
	}

	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setL1Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L1").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		model.addAttribute("categories", categoryListRepository.findAllCategoryForAuditCases(locationList));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = parametersModuleWiseRepository
						.findAllAssessmentParameterByAuditCategory(category.get().getId(), locationList);
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& parametersModuleWiseRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
					model.addAttribute("categoryTotals",
							auditMasterRepository.getAllCaseCountByCategoryAnd1stParameterId(category.get().getId(),
									parameterId.toString(), locationList));
				} else {
					model.addAttribute("categoryTotals",
							auditMasterRepository.getAllCaseCountByCategory(category.get().getId(), locationList));
				}
			}
		}
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L1 + "/review_summary_list_new";
	}

	@GetMapping("/review_cases_list")
	public String reviewCasesList(Model model, @RequestParam(required = false) Long categoryId, String parameterId) {
		List<AuditMaster> caseList = null;
		setL1Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L1").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		Optional<Category> categoryObject = categoryListRepository.findById(categoryId);
		if (categoryObject.isPresent()) {
			model.addAttribute("categoryId", categoryObject.get().getId());
		}
		if (categoryId != null && categoryId > 0 && parameterId != null && parameterId.trim().length() > 0
				&& parametersModuleWiseRepository.findById(Integer.parseInt(parameterId.trim())).isPresent()) {
			caseList = auditMasterRepository.findAllCasesByCategoryAnd1stParameterId(categoryObject.get().getId(),
					parameterId, locationList);
		} else if (categoryId != null && categoryId > 0) {
			caseList = auditMasterRepository.findAllCasesByCategory(categoryObject.get().getId(), locationList);
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L1 + "/review_cases_list_new";
	}

	@GetMapping("/downloadHqPdfFile")
	public ResponseEntity<Resource> downloadHqPdfFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFile().initiate" + fileName);
			String filesDirectory = hqPdfFileUploadLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("L2 Controller.downloadHqPdfFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("L2 Controller.downloadHqPdfFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private void setL1Menu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.L1));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "ADMIN");
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
					List<String> userRoleMapWithLocations = cutomUtility.roleMapWithLocations(userRoleMappings,
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
					model.addAttribute("commonUserDetails", "/l1/user_details");
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
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setL1Menu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
}
