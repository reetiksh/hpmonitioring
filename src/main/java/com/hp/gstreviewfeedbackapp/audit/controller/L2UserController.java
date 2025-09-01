package com.hp.gstreviewfeedbackapp.audit.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.service.AuditCaseDocumentService;
import com.hp.gstreviewfeedbackapp.audit.service.L2UserCaseAssignmentService;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.UserDetailsService;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.L2)
public class L2UserController {
	private static final Logger logger = LoggerFactory.getLogger(L2UserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository allocatingUsersRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private L2UserCaseAssignmentService l2UserCaseAssignmentService;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private AuditCaseDocumentService auditCaseDocumentService;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private MstParametersModuleWiseRepository parametersModuleWiseRepository;
	@Value("${audit.case.upload}")
	private String auditCaseUploadCategory;
	@Value("${audit.case.assignment}")
	private String auditCaseAssignmentCategory;
	@Value("${upload.audit.teamlead.directory}")
	private String fileUploadLocation;
	@Value("${upload.audit.directory}")
	private String HqPdfFileUploadLocation;
	@Value("${upload.directory}")
	private String assessmentHqFileLocation;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String index(Model model) {
		setL2Menu(model, ApplicationConstants.DASHBOARD);
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
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);

        List<AnnexureReportRow> report = l2UserCaseAssignmentService.getAnnexureReport(allMappedLocations);
        List<String> data = l2UserCaseAssignmentService.getDashboardData(allMappedLocations);
        model.addAttribute("allotted_cases", data.get(0));
        model.addAttribute("assigned_cases", data.get(1));
        model.addAttribute("audit_cases_completed", data.get(2));
        model.addAttribute("pending_audit_plan", data.get(3));
        model.addAttribute("pending_DAR", data.get(4));
        model.addAttribute("pending_FAR", data.get(5));
        model.addAttribute("reportData", report);
      
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.L2_CASE_ASSIGNMENT)
	public String caseAssignment(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_CASE_ASSIGNMENT);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository.findAllAuditCasesByWorkingLocationListAndActionAndAssignTo(
						allMappedLocations,
						auditCaseStatusRepository.findByStatus(auditCaseUploadCategory).get().getId(), "L2");
			} else {
				auditCases = auditMasterRepository
						.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionAndAssignTo(allMappedLocations,
								categoryId,
								auditCaseStatusRepository.findByStatus(auditCaseUploadCategory).get().getId(), "L2");
				model.addAttribute("categoryId", categoryId);
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_CASE_ASSIGNMENT;
	}

	@GetMapping("/" + ApplicationConstants.L2_CASE_ASSIGNMENT + "/showCaseAssignmentDetails")
	public String showCaseAssignmentDetails(Model model, String caseId) {
		model.addAttribute("caseId", caseId);
		Optional<AuditMaster> objectAM = auditMasterRepository.findById(caseId);
		if (objectAM.isPresent()) {
//			LocationDetails locationDetails = objectAM.get().getLocationDetails();
//			UserRoleMapping userRoleMapping = new UserRoleMapping(null, null,
//					(locationDetails.getLocationId().charAt(0) == 'Z'
//							? zoneDetailsRepository.findById(locationDetails.getLocationId()).get()
//							: zoneDetailsRepository.findById("NA").get()),
//					(locationDetails.getLocationId().charAt(0) == 'C'
//							? circleDetailsRepository.findById(locationDetails.getLocationId()).get()
//							: circleDetailsRepository.findById("NA").get()),
//					(locationDetails.getLocationId().charAt(0) == 'H'
//							? stateDetailsRepository.findById(locationDetails.getLocationId()).get()
//							: stateDetailsRepository.findById("NA").get()));
//
//			List<String> allHigherLocation = adminUpdateUserDetails
//					.getAllHigherMappedLocationsFromUserRoleMapping(userRoleMapping);
			List<String> location = new ArrayList<>();
			location.add(objectAM.get().getLocationDetails().getLocationId());
			List<UserRoleMapping> userList = userRoleMappingRepository.getAllUrmByRoleIdLocationList(10, location);
			if (userList != null && userList.size() > 0) {
				model.addAttribute("userList", userList);
			}
			model.addAttribute("previousStatusDate", objectAM.get().getCaseReportingDate());
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/showCaseAssignmentDetails";
	}

	@PostMapping("/" + ApplicationConstants.L2_CASE_ASSIGNMENT)
	@Transactional
	public String caseAssignment(RedirectAttributes redirectAttributes, String caseId,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date workingDate, Integer teamLeadUser) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			AuditMasterCasesAllocatingUsers allocatingUsers;
			AuditMaster auditMaster;
			UserDetails teamLeadUserDetails;
			Optional<UserDetails> objectUD = userDetailsRepository.findById(teamLeadUser);
			if (objectUD.isPresent()) {
				Optional<AuditMasterCasesAllocatingUsers> objectAMU = allocatingUsersRepository.findById(caseId);
				Optional<AuditMaster> objectAM = auditMasterRepository.findById(caseId);
				teamLeadUserDetails = objectUD.get();
				if (objectAMU.isPresent() && objectAM.isPresent()) {
					allocatingUsers = objectAMU.get();
					auditMaster = objectAM.get();
					allocatingUsers.setL2User(userDetails.getUserId());
					allocatingUsers.setL3User(teamLeadUser);
					auditMaster.setAction(auditCaseStatusRepository.findByStatus(auditCaseAssignmentCategory).get());
					auditMaster.setLastUpdatedTimeStamp(new Date());
					auditMaster.setAssignedFrom("L2");
					auditMaster.setAssignTo("L3");
					auditMaster.setAssignedDateFromL2ToL3(workingDate);
					auditMasterRepository.save(auditMaster);
					allocatingUsersRepository.save(allocatingUsers);
					l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster, userDetails.getUserId(),
							teamLeadUser);
					l2UserCaseAssignmentService.saveL2UserWorkLogs(auditMaster, userDetails.getUserId(), teamLeadUser);
					redirectAttributes.addFlashAttribute("successMessage",
							"The case with id " + caseId + " has been assigned to team lead user with details: "
									+ teamLeadUserDetails.getFirstName()
									+ ((teamLeadUserDetails.getMiddleName().length() > 0)
											? (" " + teamLeadUserDetails.getMiddleName())
											: "")
									+ ((teamLeadUserDetails.getLastName().length() > 0)
											? (" " + teamLeadUserDetails.getLastName())
											: "")
									+ " & " + teamLeadUserDetails.getLoginName());
				} else {
					redirectAttributes.addFlashAttribute("errorMessage", "Audit case is not available in database");
					return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_CASE_ASSIGNMENT;
				}
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Team Lead User Details id not available in database");
				return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_CASE_ASSIGNMENT;
			}
		} catch (Exception e) {
			logger.error("L2UserController.: caseAssignment : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_CASE_ASSIGNMENT;
	}

	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setL2Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
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
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/review_summary_list_new";
	}

	@GetMapping("/review_cases_list")
	public String reviewCasesList(Model model, @RequestParam(required = false) Long categoryId, String parameterId) {
		List<AuditMaster> caseList = null;
		setL2Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
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
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/review_cases_list_new";
	}

	@GetMapping("/" + ApplicationConstants.L2_SUBMITTED_AUDIT_PLAN_CASES)
	public String submittedAuditPlanCases(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_SUBMITTED_AUDIT_PLAN_CASES);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("l3", "Audit Plan")
					.stream().map(AuditCaseStatus::getId).collect(Collectors.toList());
			if (caseStatusList != null && caseStatusList.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					auditCases = auditMasterRepository.findAllAuditCasesByWorkingLocationListAndActionListAndAssignTo(
							allMappedLocations, caseStatusList, "L2");
				} else {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignTo(
									allMappedLocations, categoryId, caseStatusList, "L2");
					model.addAttribute("categoryId", categoryId);
				}
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				Map<String, Date> auditCaseLastWorkingDateMap = auditCaseDateDocumentDetailsRepository
						.findAllByCaseIdListAndAction(
								auditCases.stream().map(AuditMaster::getCaseId).collect(Collectors.toList()),
								auditCaseStatusRepository.findAllByCategory("Audit Plan").stream()
										.map(AuditCaseStatus::getId).collect(Collectors.toList()))
						.stream().collect(Collectors.toMap(param -> param.getCaseId().getCaseId(),
								param -> param.getActionDate()));
				auditCases.forEach(auditCase -> auditCase
						.setCurrentStatusLastWorkingDate(auditCaseLastWorkingDateMap.get(auditCase.getCaseId())));
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_SUBMITTED_AUDIT_PLAN_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_UPDATE_AUDIT_PLAN_CASES)
	public String showAuditPlanCases(Model model, String caseId) {
		if (caseId != null && caseId.length() > 0) {
			Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
			if (amObject.isPresent()) {
				model.addAttribute("caseId", caseId);
				model.addAttribute("auditCase", amObject.get());
				Optional<AuditCaseDateDocumentDetails> documentObject = auditCaseDateDocumentDetailsRepository
						.findTheLatestStatusDataByCaseIdAndAuditStatusId(caseId, amObject.get().getAction().getId());
				if (documentObject.isPresent()) {
					model.addAttribute("documentObject", documentObject.get());
				}
				model.addAttribute("previousStatusDate",
						auditCaseDateDocumentDetailsRepository.getHighestDateByCaseId(caseId));
			}
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_UPDATE_AUDIT_PLAN_CASES;
	}

	@PostMapping("/" + ApplicationConstants.L2_UPDATE_AUDIT_PLAN_CASES)
	@Transactional
	public String updateAuditPlanCases(RedirectAttributes redirectAttributes, String caseId,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date workingDate, String buttonAction, String comment) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			AuditCaseStatus action = null;
			if (buttonAction.equals("approval")) {
				action = auditCaseStatusRepository.findByStatus("auditplanapproved").get();
			} else if (buttonAction.equals("rejection")) {
				action = auditCaseStatusRepository.findByStatus("auditplanrejected").get();
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "Facing some internal database issue.");
				return "redirect:/" + ApplicationConstants.L2 + "/"
						+ ApplicationConstants.L2_SUBMITTED_AUDIT_PLAN_CASES;
			}
			Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
			if (amObject.isPresent()) {
				// Optional<AuditCaseDateDocumentDetailsRepository> documentObject =
				// auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(caseId,
				// amObject.get().getAction());
				Date timeStamp = new Date();
				AuditCaseDateDocumentDetails object = amObject.get().getAuditCaseDateDocumentDetails().stream()
						.filter(e -> {
							return e.getAction().getId().equals(amObject.get().getAction().getId());
						}).findFirst().orElse(null);
				if (object != null) {
					object.setAction(action);
					object.setLastUpdatedTimeStamp(timeStamp);
					object.setCommentFromL2Officer(comment);
					object.setActionDate(workingDate);
					auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(object, userDetails);
				}
				amObject.get().setAction(action);
				amObject.get().setLastUpdatedTimeStamp(timeStamp);
				amObject.get().setAssignedFrom("L2");
				amObject.get().setAssignTo("L3");
			}
			AuditMaster auditMaster = auditMasterRepository.save(amObject.get());
			Optional<AuditMasterCasesAllocatingUsers> userObject = allocatingUsersRepository.findById(caseId);
			l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster, userDetails.getUserId(),
					(userObject != null ? userObject.get().getL3User() : null));
			l2UserCaseAssignmentService.saveL2UserWorkLogs(auditMaster, userDetails.getUserId(),
					(userObject != null ? userObject.get().getL3User() : null));
			redirectAttributes.addFlashAttribute("successMessage", auditMaster.getAction().getSuccessMesage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L2UserController : updateAuditPlanCases : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_SUBMITTED_AUDIT_PLAN_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_SUBMITTED_DAR_CASES)
	public String submittedDarCases(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_SUBMITTED_DAR_CASES);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("l3", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList());
			if (caseStatusList != null && caseStatusList.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndActionListAndAssignToAndAssignedFrom(
									allMappedLocations, caseStatusList, "L2", "L3");
				} else {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignToAndAssignedFrom(
									allMappedLocations, categoryId, caseStatusList, "L2", "L3");
					model.addAttribute("categoryId", categoryId);
				}
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		model.addAttribute("pageHeading", "Verification for DAR");
		model.addAttribute("cardHeading", "DAR (Submitted by team lead officer(s))");
		model.addAttribute("pagename", "Verification for DAR");
		model.addAttribute("tabUrl", "submitted_dar_cases");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_MCM_RECOMMENDATION_ON_DAR)
	public String mcmRecommendationOnDars(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_MCM_RECOMMENDATION_ON_DAR);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("mcm", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList());
			if (caseStatusList != null && caseStatusList.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndActionListAndAssignToAndAssignedFrom(
									allMappedLocations, caseStatusList, "L2", "MCM");
				} else {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignToAndAssignedFrom(
									allMappedLocations, categoryId, caseStatusList, "L2", "MCM");
					model.addAttribute("categoryId", categoryId);
				}
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		model.addAttribute("pageHeading", "MCM’s Recommendation on DARs");
		model.addAttribute("cardHeading", "DAR (Recommended by MCM)");
		model.addAttribute("pagename", "MCM’s Recommendation on DARs");
		model.addAttribute("tabUrl", "mcm_recommendation_on_dars");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_CASES_UDER_MCM_CONSIDERATION)
	public String casesUnderMcmConsideration(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_CASES_UDER_MCM_CONSIDERATION);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("l3", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList());
			caseStatusList.addAll(auditCaseStatusRepository.findByUsedByRoleAndCategory("l2", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList()));
			if (caseStatusList != null && caseStatusList.size() > 0) {
				if (categoryId == null || categoryId.equals(0)) {
					auditCases = auditMasterRepository.findAllAuditCasesByWorkingLocationListAndActionListAndAssignTo(
							allMappedLocations, caseStatusList, "MCM");
				} else {
					auditCases = auditMasterRepository
							.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignTo(
									allMappedLocations, categoryId, caseStatusList, "MCM");
					model.addAttribute("categoryId", categoryId);
				}
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		model.addAttribute("pageHeading", "Cases under MCM consideration");
		model.addAttribute("cardHeading", "Cases under MCM consideration");
		model.addAttribute("pagename", "Cases under MCM consideration");
		model.addAttribute("tabUrl", "cases_under_mcm_consideration");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_SHOW_DAR_CASE_DETAILS)
	public String showDarCaseDetails(Model model, String tabUrl, String caseId) {
		setL2Menu(model, tabUrl);
		model.addAttribute("tabUrl", tabUrl);
		model.addAttribute("tabName", appMenuRepository.findByUserTypeAndUrl("l2", tabUrl).get().getName());
		Optional<AuditMaster> objectAM = auditMasterRepository.findById(caseId);
		if (objectAM.isPresent()) {
			model.addAttribute("auditCaseDetails", objectAM.get());
			model.addAttribute("darDetails", auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(objectAM.get(), objectAM.get().getAction()).get());
			AuditMaster auditMaster = objectAM.get();
			if (auditMaster.getAction().getUsedByRole().equals("mcm")) {
				model.addAttribute("mcmAction", auditMaster.getAction().getStatus());
			}
			String[] parameterArray = auditMaster.getParameter().split(",");
			List<MstParametersModuleWise> parameterList = new ArrayList<>();
			for (String value : parameterArray) {
				if (value.trim().length() > 0) {
					parameterList.add(mstParametersModuleWiseRepository.findById(Integer.parseInt(value.trim())).get());
				}
			}
			if (parameterList != null && parameterList.size() > 0) {
				model.addAttribute("parameterList", parameterList);
			}
			model.addAttribute("previousStatusDate",
					auditCaseDateDocumentDetailsRepository.getHighestDateByCaseId(auditMaster.getCaseId()));
		} else {
			return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
					+ ApplicationConstants.L2_SUBMITTED_DAR_CASES;
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/submitted_dar_case_details";
	}

	@GetMapping("/nil_dar_option")
	public String darInputDiv(Model model, String nilDar, String caseId) {
		try {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			model.addAttribute("auditMaster", auditMaster);
			if (!auditMaster.getAssignTo().equalsIgnoreCase("l2")) {
				model.addAttribute("nonEditable", "true");
			}
			if (auditMaster.getAssignedFrom().equalsIgnoreCase("mcm")) {
				model.addAttribute("assignedFromMcm", "true");
			} else {
				model.addAttribute("assignedFromMcm", "false");
			}
			AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditMaster.getAction()).get();
			model.addAttribute("documentDetails", auditCaseDateDocumentDetails);
			if (nilDar != null && nilDar.equals("NO")) {
				model.addAttribute("darDetailsList", auditCaseDateDocumentDetails.getAuditCaseDarDetailsList());
				return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/l2_nilDarNO";
			} else if (nilDar != null && nilDar.equals("YES")) {
				if (auditCaseDateDocumentDetails.getAction().getStatus().equalsIgnoreCase("recommendedForRaiseQuery")) {
					model.addAttribute("darRejected", "true");
				}
				return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/l2_nilDarYES";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserController : darInputDiv : " + e.getMessage());
		}
		return null;
	}

	@PostMapping("/" + ApplicationConstants.L2_UPDATE_DAR_CASE)
	@Transactional
	public String updateDarCaseDetails(RedirectAttributes redirectAttributes,
			@ModelAttribute("caseUpdationActionForm") L3UserAuditCaseUpdate userAuditCaseUpdate) {
		String assignedFrom = auditMasterRepository.findById(userAuditCaseUpdate.getCaseId()).get().getAssignedFrom();
//		String message = "";
		String message = l2UserCaseAssignmentService.saveDarDetails(userAuditCaseUpdate);
		redirectAttributes.addFlashAttribute("successMessage", message);
		if (assignedFrom.equalsIgnoreCase("MCM")) {
			return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_MCM_RECOMMENDATION_ON_DAR;
		} else if (assignedFrom.equalsIgnoreCase("L3")) {
			return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_SUBMITTED_DAR_CASES;
		}
		return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/recomandedToMcm")
	public String viewrecomandedToMcmModal(String caseId, Model model) {
		model.addAttribute("caseId", caseId);
		model.addAttribute("updateStatusId", auditMasterRepository.findById(caseId).get().getAction().getId());
		model.addAttribute("previousStatusDate", auditCaseDateDocumentDetailsRepository.getHighestDateByCaseId(caseId));
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/recomandedToMcmModal";
	}

	@PostMapping("/recomandedToMcm")
	@Transactional
	public String submissionTheRecomandedToMcmRequestModal(L3UserAuditCaseUpdate userAuditCaseUpdate,
			RedirectAttributes redirectAttributes) {
		String message = l2UserCaseAssignmentService.saveRecomandedToMcmDetails(userAuditCaseUpdate);
		redirectAttributes.addFlashAttribute("successMessage", message);
		return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE)
	public String recommendedForOtherModule(Model model, @RequestParam(required = false) Integer categoryId) {
		setL2Menu(model, ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE);
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
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository
						.findAllAuditCasesByWorkingLocationListAndActionAndAssignToAndFullyRecovered(allMappedLocations,
								auditCaseStatusRepository.findByStatus("closurereportissued").get().getId(), "L2",
								"false");
			} else {
				auditCases = auditMasterRepository
						.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionAndAssignToAndFullyRecovered(
								allMappedLocations, categoryId,
								auditCaseStatusRepository.findByStatus("closurereportissued").get().getId(), "L2",
								"false");
				model.addAttribute("categoryId", categoryId);
			}
			if (auditCases != null) {
				Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
				auditCases.forEach(auditCase -> {
					List<String> parameterNames = Arrays.stream(auditCase.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
							.collect(Collectors.toList());
					auditCase.setParametersNameList(parameterNames);
				});
				model.addAttribute("auditCases", auditCases);
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE;
	}

	@GetMapping("/" + ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE
			+ "/showCaseAssignmentDetailsForRecommendaionModule")
	public String recommendedForOtherModuleModal(String caseId, Model model) {
		Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
		if (amObject.isPresent()) {
			AuditMaster auditMaster = amObject.get();
			model.addAttribute("foUserId", auditMaster.getFoUserDetailsForShowCauseNotice().getUserId());
			List<CustomUserDetails> foUserList = userDetailsService
					.getAllAssignedUserForGivenLocation(auditMaster.getLocationDetails().getLocationId(), 2);
			model.addAttribute("foUserList", foUserList);
			model.addAttribute("jurisdictionName", auditMaster.getLocationDetails().getLocationName());
			model.addAttribute("caseId", caseId);
			model.addAttribute("recommendedModule", auditMaster.getRecommendedModule());
			List<String> statusList = new ArrayList<>();
			statusList.add("showCauseNotice");
			statusList.add("recommended_for_enforcement");
			statusList.add("recommended_for_assessment_adjudication");
			Optional<AuditCaseDateDocumentDetails> recommendationDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndActionList(auditMaster.getCaseId(),
							auditCaseStatusRepository.findIdsByStatusList(statusList));
			if (recommendationDocumentDetails.isPresent()) {
				model.addAttribute("recommendationStatus", recommendationDocumentDetails.get().getAction().getName());
				model.addAttribute("recommendationStatusDocument",
						recommendationDocumentDetails.get().getActionFilePath());
			}
			recommendationDocumentDetails = auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(auditMaster,
					auditCaseStatusRepository.findByStatus("closurereportissued").get());
			if (recommendationDocumentDetails.isPresent()) {
				model.addAttribute("closureReportIssueDocument",
						recommendationDocumentDetails.get().getActionFilePath());
			}
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2
				+ "/showCaseAssignmentDetailsForRecommendaionModule";
	}

	@PostMapping("/" + ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE)
	public String redirectTheCaseToOtherModule(RedirectAttributes redirectAttributes, String caseId,
			Integer teamLeadUser, String buttonAction, @RequestParam(required = false) String comment) {
		String message = "Facing some internal issue";
		if (buttonAction.equals("approval")) {
			message = l2UserCaseAssignmentService.saveRecommendationToOtherModule(caseId, teamLeadUser, comment);
		} else if (buttonAction.equals("rejection")) {
			message = l2UserCaseAssignmentService.saveRecommendationRejected(caseId, comment);
		}
		redirectAttributes.addFlashAttribute("successMessage", message);
		return "redirect:/" + ApplicationConstants.L2 + "/" + ApplicationConstants.L2_RECOMMENDED_FOR_OTHER_MODULE;
	}

	@GetMapping("/" + ApplicationConstants.L2_RECOMMENDED_TO_OTHER_MODULE)
	public String RecommandedToOtherModules(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL2Menu(model, ApplicationConstants.L2_RECOMMENDED_TO_OTHER_MODULE);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		List<String> auditCaseIds = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L2").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndL2UserId(allMappedLocations,
								userDetails.getUserId());
			} else {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndL2UserId(
								allMappedLocations, categoryId, userDetails.getUserId());
				model.addAttribute("categoryId", categoryId);
			}
			if (auditCases != null) {
				auditCaseIds = auditCases.stream().map(acs -> acs.getCaseId()).toList();
			}
		}
		// Set Category
		List<Category> categoryList = categoryListRepository.findAllByModule("audit");
		categoryList.addAll(categoryListRepository.findAllByModule("scrutiny"));
		categoryList.addAll(categoryListRepository.findAllByModule("cag"));
		if (categoryList != null && categoryList.size() > 0) {
			model.addAttribute("categories", categoryList);
		}
		List<EnforcementReviewCase> enforcementReviewCaseListAccordingTocategory = enforcementReviewCaseRepository
				.findAllByAuditCaseId(auditCaseIds);
		Map<Integer, String> allParameterMap = mstParametersModuleWiseRepository.findAll().stream()
				.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
		for (EnforcementReviewCase e : enforcementReviewCaseListAccordingTocategory) {
			e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
			e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
					e.getId().getPeriod(), e.getId().getCaseReportingDate()));
			if (e.getParameter() != null && e.getParameter().trim().length() > 0) {
				e.setParameter(String.join(", ", Arrays.stream(e.getParameter().split(",")).map(String::trim)
						.map(Integer::parseInt).map(allParameterMap::get).collect(Collectors.toList())));
			}
		}
		model.addAttribute("caseList", enforcementReviewCaseListAccordingTocategory);
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L2 + "/"
				+ ApplicationConstants.L2_RECOMMENDED_TO_OTHER_MODULE;
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFile().initiate" + fileName);
			String filesDirectory = fileUploadLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("L2 Controller.downloadFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("L2 Controller.downloadFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
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
			logger.info("L2 Controller.downloadHqPdfFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("L2 Controller.downloadHqPdfFile().catch() : " + ex.getMessage());
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

	private void setL2Menu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.L2));
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
					model.addAttribute("commonUserDetails", "/l2/user_details");
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
		setL2Menu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	

	
}
