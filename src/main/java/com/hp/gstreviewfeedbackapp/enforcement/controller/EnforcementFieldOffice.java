package com.hp.gstreviewfeedbackapp.enforcement.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.enforcement.data.EnforcementCaseUpdate;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementActionStatus;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementDashboardSummaryDTO;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementActionStatusRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementHqUserLogsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterAllocatingUserRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementFieldOfficeService;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.UserDetailsService;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.ENFORCEMENT_FO)
public class EnforcementFieldOffice {
	private static final Logger logger = LoggerFactory.getLogger(EnforcementFieldOffice.class);
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
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private EnforcementFieldOfficeService enforcementFieldOfficeService;
	@Autowired
	private EnforcementMasterAllocatingUserRepository enfMasterAllocatingUserRepository;
	@Autowired
	private EnforcementActionStatusRepository enforcementActionStatusRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private EnforcementCaseDateDocumentDetailsRepository dateDocumentDetailsRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Value("${upload.enforcement.directory}")
	private String HqPdfFileUploadLocation;
	@Value("${upload.directory}")
	private String assessmentHqFileLocation;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(@RequestParam(name = "category", required = false, defaultValue = "-1") String parameterId,
	        @RequestParam(name = "financialyear", required = false, defaultValue = "-1") String period,
	        Model model) {
		try {
			setEnfFOMenu(model, ApplicationConstants.DASHBOARD);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			// Getting all user role mapping for this user and getting all mapped location
			List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(
					objectUserDetails.get(), userRoleRepository.findByroleCode("Enforcement_FO").get());
			List<String> allMappedLocations = adminUpdateUserDetails
					.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
			model.addAttribute("totalEnforcementCases", enforcementMasterRepository.findTotalCountOfCasesAsLocations(allMappedLocations));
			model.addAttribute("totalSumOfIndicativeTaxValue",
					enforcementMasterRepository.findTotalSumOfIndicativeTaxValueAsLocations(allMappedLocations));
			model.addAttribute("totalEnforcementCasesInLast3Months",
					enforcementHqUserLogsRepository.findTotalCountOfUploadedCasesInLast3Months());
			model.addAttribute("countOfActiontakenCases",
					enforcementMasterRepository.findTotalCountOfActiontakenCases());
			model.addAttribute("countOfPendingCases",
					enforcementMasterRepository.findTotalCountOfCasesPendingWithEnfFoByUserIdListAndActionList(
							new ArrayList<>(Arrays.asList(0, objectUserDetails.get().getUserId())),
							new ArrayList<>(Arrays.asList("upload", "investigationStarted")), allMappedLocations));
			model.addAttribute("countOfCasesActionByUser", enfMasterAllocatingUserRepository
					.totalCountOfCasesActionTakenByEnfFoUserId(objectUserDetails.get().getUserId()));
			
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
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?parameterId=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() ;
	}
	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_ACKNOWLEDGEMENT_CASES)
	public String acknowledgementCases(Model model, @RequestParam(required = false) Integer categoryId) {
		try {
			setEnfFOMenu(model, ApplicationConstants.ENFORCEMENT_ACKNOWLEDGEMENT_CASES);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			List<EnforcementMaster> enforcementCases = null;
			// Getting all user role mapping for this user and getting all mapped location
			List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRoleRepository.findByroleCode("Enforcement_FO").get());
			List<String> allMappedLocations = adminUpdateUserDetails
					.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
			if (allMappedLocations != null && allMappedLocations.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndActionListAndUserIdList(
									allMappedLocations, new ArrayList<>(Arrays.asList("upload")),
									new ArrayList<>(Arrays.asList(0, userDetails.getUserId())));
				} else {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
									allMappedLocations, categoryId, new ArrayList<>(Arrays.asList("upload")),
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
					.findAllCategoryOfEnfFOCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
							allMappedLocations, new ArrayList<>(Arrays.asList(0, userDetails.getUserId())),
							new ArrayList<>(Arrays.asList("upload")), "Enforcement_FO");
			if (categoryList != null && categoryList.size() > 0) {
				model.addAttribute("categories", categoryList);
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : acknowledgementCases : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
				+ ApplicationConstants.ENFORCEMENT_ACKNOWLEDGEMENT_CASES;
	}

	@GetMapping("/acknowledge_case")
	public String acknowledgeCase(Model model, CompositeKey compositeKey) {
		model.addAttribute("caseDetails", compositeKey);
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/" + "acknowledgeCase";
	}

	@PostMapping("/" + ApplicationConstants.ENFORCEMENT_ACKNOWLEDGEMENT_CASES)
	public String saveAcknowledgeCase(RedirectAttributes redirectAttributes, CompositeKey compositeKey,
			String caseType) {
		try {
			System.out.println("compositeKey : " + compositeKey + " caseType: " + caseType);
			Optional<EnforcementMaster> enforOptional = enforcementMasterRepository.findById(compositeKey);
			String message = null;
			if (caseType.equalsIgnoreCase("enforcement") && enforOptional.isPresent()) {
				message = enforcementFieldOfficeService.acknowledgeEnforcementCase(enforOptional.get());
			} else if (caseType.equalsIgnoreCase("scrutiny") && enforOptional.isPresent()) {
				message = enforcementFieldOfficeService.saveEnforcementCaseToScrutinyModule(enforOptional.get());
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Some Internal Issue Occure. Please contact with Administrator");
			}
			if (message != null && message.trim().length() > 0) {
				redirectAttributes.addFlashAttribute("successMessage", message);
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : acknowledgeCase : " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.ENFORCEMENT_FO + "/"
				+ ApplicationConstants.ENFORCEMENT_ACKNOWLEDGEMENT_CASES;
	}

	@GetMapping("/" + ApplicationConstants.UPDATE_ENFORCEMENT_CASES)
	public String updateEnforcementCases(Model model, @RequestParam(required = false) Integer categoryId) {
		try {
			setEnfFOMenu(model, ApplicationConstants.UPDATE_ENFORCEMENT_CASES);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			List<EnforcementMaster> enforcementCases = null;
			// Getting all user role mapping for this user and getting all mapped location
			List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRoleRepository.findByroleCode("Enforcement_FO").get());
			List<String> allMappedLocations = adminUpdateUserDetails
					.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
			if (allMappedLocations != null && allMappedLocations.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndActionListAndUserIdList(
									allMappedLocations,
									enforcementActionStatusRepository.findByUsedByRole("EnforcementOfficer"),
									new ArrayList<>(Arrays.asList(0, userDetails.getUserId())));
				} else {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
									allMappedLocations, categoryId,
									enforcementActionStatusRepository.findByUsedByRole("EnforcementOfficer"),
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
					.findAllCategoryOfEnfFOCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
							allMappedLocations, new ArrayList<>(Arrays.asList(0, userDetails.getUserId())),
							new ArrayList<>(Arrays.asList("acknowledged")), "Enforcement_FO");
			if (categoryList != null && categoryList.size() > 0) {
				model.addAttribute("categories", categoryList);
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : updateEnforcementCases : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
				+ ApplicationConstants.UPDATE_ENFORCEMENT_CASES;
	}

	@PostMapping("/" + ApplicationConstants.ENFORCEMENT_FO_UPDATE_CASE)
	public String showCaseDetails(RedirectAttributes redirectAttributes, CompositeKey compositeKey,
			@RequestParam(required = false) Boolean inspectionRequired) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			EnforcementMaster enforcementCase = enforcementMasterRepository.findById(compositeKey).get();
			if (!(enforcementCase.getAction().getCodeName().equals("referToAdjudiction")
					|| enforcementCase.getAction().getCodeName().equals("issueShowCause")
					|| enforcementCase.getAction().getCodeName().equals("enforcementCompleted"))) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				String parameterNames = Arrays.stream(enforcementCase.getParameter().split(","))
						.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
						.collect(Collectors.joining(","));
				enforcementCase.setParametersNameList(parameterNames);
				if (enforcementCase.getInspectionRequired() != null
						&& enforcementCase.getInspectionRequired().trim().length() > 0) {
					inspectionRequired = Boolean.parseBoolean(enforcementCase.getInspectionRequired());
					redirectAttributes.addFlashAttribute("checkboxLock", "lock");
				}
				if ((inspectionRequired == null || inspectionRequired == false)
						&& enforcementCase.getAction().getCodeName().equalsIgnoreCase("acknowledged")) {
					// Update the action id due to skip the "panchnama" status
					enforcementCase.getAction()
							.setId(enforcementActionStatusRepository.getByCodeName("panchnama").get().getId());
				} // else {
//				redirectAttributes.addFlashAttribute("checkboxValue", "on");
//			}
				redirectAttributes.addFlashAttribute("inspectionRequired",
						inspectionRequired != null ? inspectionRequired : "false");
				redirectAttributes.addFlashAttribute("enforcementCaseDetails", enforcementCase);
				redirectAttributes.addFlashAttribute("activeActionPannelId", enforcementCase.getAction().getId() + 1);
				List<EnforcementActionStatus> trueStatus = enforcementFieldOfficeService
						.getAllStatusFromEnforcementDateDocumentDetails(compositeKey);
				redirectAttributes.addFlashAttribute("enforcementCaseStatusList",
						(inspectionRequired != null && inspectionRequired == true) ? trueStatus
								: trueStatus.stream().filter(s -> !s.getCodeName().equalsIgnoreCase("panchnama"))
										.collect(Collectors.toList()));
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : showCaseDetails : " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.ENFORCEMENT_FO + "/"
				+ ApplicationConstants.ENFORCEMENT_FO_UPDATE_CASE;
	}

	@GetMapping("/update_enforcement_case/activePannel")
	public String activePannel(Model model, CompositeKey compositeKey, Integer activeActionPannelId) {
		String pageName = null;
		try {
			System.out.println(compositeKey);
			Optional<EnforcementMaster> emOptional = enforcementMasterRepository.findById(compositeKey);
			if (emOptional.isPresent()) {
				Optional<EnforcementActionStatus> actionOptional = enforcementActionStatusRepository
						.findById(activeActionPannelId);
				System.out.println(actionOptional.get());
				if (actionOptional.get().getCodeName().equals("referToAdjudiction")
						|| actionOptional.get().getCodeName().equals("issueShowCause")) {
					List<LocationDetails> locationDetailsList = locationDetailsRepository
							.findAllByOrderByLocationIdAsc();
					locationDetailsList = locationDetailsList.stream()
							.filter(location -> !(location.getLocationId().equalsIgnoreCase("Z04")
									|| location.getLocationId().equalsIgnoreCase("C81")
									|| location.getLocationId().equalsIgnoreCase("HPT")))
							.collect(Collectors.toList());
					model.addAttribute("locationDetailsList", locationDetailsList);
					List<CustomUserDetails> userList = userDetailsService
							.getAllAssignedUserForGivenLocation(emOptional.get().getCaseLocation().getLocationId(), 2);
					model.addAttribute("userList", userList);
				}
				Optional<EnforcementCaseDateDocumentDetails> caseDateDocumentDetailsObject = dateDocumentDetailsRepository
						.findByEnforcementMasterAndAction(emOptional.get(), actionOptional.get());
				if (caseDateDocumentDetailsObject.isPresent()) {
					model.addAttribute("enforcementCaseDateDocumentDetails", caseDateDocumentDetailsObject.get());
				}
				List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
				pageName = actionOptional.get().getCodeName();
				model.addAttribute("enforcementCaseDetails", emOptional.get());
				model.addAttribute("activeActionPannelId", activeActionPannelId);
				model.addAttribute("listRecovery",listRecovery);
				
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : activePannel : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/" + pageName;
	}

	@GetMapping("/get_all_assigned_users")
	@ResponseBody
	public List<CustomUserDetails> getAllAssignedUserForALocation(@RequestParam String location) {
		List<CustomUserDetails> userList = userDetailsService.getAllAssignedUserForGivenLocation(location, 2);
		return userList;
	}

	@PostMapping("/update_enforcement_case_details")
	public String updateEnforcementCaseDetails(RedirectAttributes redirectAttributes,
			EnforcementCaseUpdate enforcementCaseUpdateDetails) {
		try {
			String output = null;
			CompositeKey compositeKey = new CompositeKey(enforcementCaseUpdateDetails.getGSTIN(),
					enforcementCaseUpdateDetails.getCaseReportingDate(), enforcementCaseUpdateDetails.getPeriod());
			Optional<EnforcementMaster> emObject = enforcementMasterRepository.findById(compositeKey);
			Optional<EnforcementActionStatus> actionObject = enforcementActionStatusRepository
					.findById(enforcementCaseUpdateDetails.getUpdateStatusId());
			if (emObject.isPresent()) {
				if (actionObject.isPresent()) {
					if (actionObject.get().getCodeName().equalsIgnoreCase("panchnama")
							|| actionObject.get().getCodeName().equalsIgnoreCase("priliminaryReport")
							|| actionObject.get().getCodeName().equalsIgnoreCase("finalReport")) {
						output = enforcementFieldOfficeService
								.updateEnforcementCaseForPanchnamaAndPriliminaryReportAndFinalReport(
										enforcementCaseUpdateDetails);
					} else if (actionObject.get().getCodeName().equalsIgnoreCase("referToAdjudiction")
							|| actionObject.get().getCodeName().equalsIgnoreCase("issueShowCause")) { // Initiated &
																										// DRC01 Issued
						output = enforcementFieldOfficeService
								.updateEnforcementCaseForReferToAdjudictionAndIssueShowCause(
										enforcementCaseUpdateDetails);
//						output = "Debug Success!";
					}
					if (output != null && output.length() > 0) {
						redirectAttributes.addFlashAttribute("successMessage", output);
						return showCaseDetails(redirectAttributes, compositeKey, null);
					}
				} else {
					System.err.println("Unable to get the enforcement object!");
				}
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : updateEnforcementCaseDetails : " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.ENFORCEMENT_FO + "/"
				+ ApplicationConstants.ENFORCEMENT_FO_UPDATE_CASE;
	}

	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_FO_RECOMMENDED_TO_OTHER_MODULE)
	public String RecommandedToOtherModules(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setEnfFOMenu(model, ApplicationConstants.ENFORCEMENT_FO_RECOMMENDED_TO_OTHER_MODULE);
		UserDetails userDetails = objectUserDetails.get();
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("Enforcement_FO").get());
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		// Find All Enforcement Cases for this user id
		List<EnforcementReviewCase> enforcementReviewCaseLis = enforcementReviewCaseRepository
				.findEnforcementReviewCaseListByEnforcementMasterUserIdAndLocationList(userDetails.getUserId(),
						allMappedLocations);
//		if (allMappedLocations != null && allMappedLocations.size() > 0) {
//			if (categoryId == null || categoryId.equals(0)) {
//				auditCases = auditMasterRepository
//						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndL2UserId(allMappedLocations,
//								userDetails.getUserId());
//			} else {
//				auditCases = auditMasterRepository
//						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndL2UserId(
//								allMappedLocations, categoryId, userDetails.getUserId());
//				model.addAttribute("categoryId", categoryId);
//			}
//			if (auditCases != null) {
//				auditCaseIds = auditCases.stream().map(acs -> acs.getCaseId()).toList();
//			}
//		}
		// Set Category
//		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
//		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
//		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
//		if (categoryList != null && categoryList.size() > 0) {
//			model.addAttribute("categories", categoryList);
//		}
		Map<Integer, String> allParameterMap = mstParametersModuleWiseRepository.findAll().stream()
				.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
		for (EnforcementReviewCase e : enforcementReviewCaseLis) {
			e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
			e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
					e.getId().getPeriod(), e.getId().getCaseReportingDate()));
			if (e.getParameter() != null && e.getParameter().trim().length() > 0) {
				e.setParameter(String.join(", ", Arrays.stream(e.getParameter().split(",")).map(String::trim)
						.map(Integer::parseInt).map(allParameterMap::get).collect(Collectors.toList())));
			}
		}
		model.addAttribute("caseList", enforcementReviewCaseLis);
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
				+ ApplicationConstants.ENFORCEMENT_FO_RECOMMENDED_TO_OTHER_MODULE;
	}

	@GetMapping("/" + ApplicationConstants.ENFORCEMENT_FO_UPDATE_CASE)
	public String showCaseDetailsPage(Model model) {
		setEnfFOMenu(model, ApplicationConstants.UPDATE_ENFORCEMENT_CASES);
		if (model.containsAttribute("enforcementCaseDetails")) {
			return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
					+ ApplicationConstants.ENFORCEMENT_FO_UPDATE_CASE;
		} else {
			return "redirect:/" + ApplicationConstants.ENFORCEMENT_FO + "/"
					+ ApplicationConstants.UPDATE_ENFORCEMENT_CASES;
		}
	}
//	@PostMapping("/caseForApproval")
//	public String caseForApproval(RedirectAttributes redirectAttributes, CompositeKey compositeKey,
//			String needApproval) {
//		return "redirect:/" + ApplicationConstants.ENFORCEMENT_FO + "/" + ApplicationConstants.UPDATE_ENFORCEMENT_CASES;
//	}

	@GetMapping("/caseIdSubmissionAndApprovalRequest")
	public String caseIdSubmissionAndApprovalRequest(Model model, CompositeKey compositeKey) {
		model.addAttribute("caseDetails", compositeKey);
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
				+ "caseIdSubmissionAndApprovalRequestForm";
	}

	@PostMapping("/caseForApproval")
	public ResponseEntity<Map<String, Object>> caseForApproval(RedirectAttributes redirectAttributes,
			CompositeKey compositeKey, String needApproval, String caseId) {
		Map<String, Object> response = new HashMap<>();
		UserDetails userDetails = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			return enforcementFieldOfficeService.enforcementCaseApprovalRequestSubmission(compositeKey, needApproval,
					caseId, userDetails);
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : caseForApproval : " + e.getMessage());
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/" + ApplicationConstants.INVESTIGATION_CASES)
	public String investigationCases(Model model, @RequestParam(required = false) Integer categoryId) {
		try {
			setEnfFOMenu(model, ApplicationConstants.INVESTIGATION_CASES);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			List<EnforcementMaster> enforcementCases = null;
			// Getting all user role mapping for this user and getting all mapped location
			List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRoleRepository.findByroleCode("Enforcement_FO").get());
			List<String> allMappedLocations = adminUpdateUserDetails
					.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
			if (allMappedLocations != null && allMappedLocations.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndActionListAndUserIdList(
									allMappedLocations, new ArrayList<>(Arrays.asList("investigationStarted")),
									new ArrayList<>(Arrays.asList(userDetails.getUserId())));
				} else {
					enforcementCases = enforcementMasterRepository
							.findAllEnforcementFoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
									allMappedLocations, categoryId,
									new ArrayList<>(Arrays.asList("investigationStarted")),
									new ArrayList<>(Arrays.asList(userDetails.getUserId())));
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
					.findAllCategoryOfEnfFOCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
							allMappedLocations, new ArrayList<>(Arrays.asList(0, userDetails.getUserId())),
							new ArrayList<>(Arrays.asList("investigationStarted")), "Enforcement_FO");
			if (categoryList != null && categoryList.size() > 0) {
				model.addAttribute("categories", categoryList);
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : investigationCases : " + e.getMessage());
			e.printStackTrace();
		}
		return ApplicationConstants.ENFORCEMENT + "/" + ApplicationConstants.FO + "/"
				+ ApplicationConstants.INVESTIGATION_CASES;
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

	@GetMapping("/downloadAssessmentHqFile")
	public ResponseEntity<Resource> downloadAssessmentHqFile(@RequestParam("fileName") String fileName)
			throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFile().initiate" + fileName);
			String filesDirectory = assessmentHqFileLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("HeadQuater Controller.downloadFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("HeadQuater Controller.downloadFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private void setEnfFOMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,
					ApplicationConstants.ENFORCEMENT_FO));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "Enforcement_FO");
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
					model.addAttribute("commonUserDetails", "/enforcement_fo/user_details");
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
		logger.info("EnforcementFieldOffice.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setEnfFOMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
}
