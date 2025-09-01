package com.hp.gstreviewfeedbackapp.audit.controller;

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
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.service.L3UserUpdateAuditCaseService;
import com.hp.gstreviewfeedbackapp.audit.service.McmUserUpdateAuditCaseService;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
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
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.MCM)
public class McmUserController {
	private static final Logger logger = LoggerFactory.getLogger(L2UserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private McmUserUpdateAuditCaseService mcmUserUpdateAuditCaseService;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Value("${upload.audit.teamlead.directory}")
	private String fileUploadLocation;
	@Value("${upload.directory}")
	private String assessmentHqFileLocation;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String index(Model model) {
		setMCMMenu(model, ApplicationConstants.DASHBOARD);
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
				userRoleRepository.findByroleCode("MCM").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
        List<AnnexureReportRow> report = mcmUserUpdateAuditCaseService.getAnnexureReport(allMappedLocations);
        List<String> data = mcmUserUpdateAuditCaseService.getDashboardData(allMappedLocations);
        model.addAttribute("allotted_cases", data.get(0));
        model.addAttribute("assigned_cases", data.get(1));
        model.addAttribute("audit_cases_completed", data.get(2));
        model.addAttribute("pending_audit_plan", data.get(3));
        model.addAttribute("pending_DAR", data.get(4));
        model.addAttribute("pending_FAR", data.get(5));
        model.addAttribute("reportData", report);
	
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.MCM_SUBMITTED_DAR_CASES)
	public String submittedDarCases(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setMCMMenu(model, ApplicationConstants.MCM_SUBMITTED_DAR_CASES);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
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
		model.addAttribute("pageHeading", "MCM consideration (Submittted DAR)");
		model.addAttribute("cardHeading", "DAR (Submitted by team lead and allocating officer(s))");
		model.addAttribute("pagename", "MCM consideration");
		model.addAttribute("tabUrl", "submitted_dar_cases");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/"
				+ ApplicationConstants.MCM_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.MCM_PENDING_WITH_L2)
	public String pendingWithL2Officer(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setMCMMenu(model, ApplicationConstants.MCM_PENDING_WITH_L2);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("mcm", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList());
			caseStatusList.addAll(auditCaseStatusRepository.findByUsedByRoleAndCategory("l3", "DAR").stream()
					.map(AuditCaseStatus::getId).collect(Collectors.toList()));
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
		model.addAttribute("pageHeading", "DARs pending with Allocating officer");
		model.addAttribute("cardHeading", "DAR (Submitted by team lead and MCM)");
		model.addAttribute("pagename", "Pending with allocating officers");
		model.addAttribute("tabUrl", "pending_with_l2");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/"
				+ ApplicationConstants.MCM_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.MCM_APPROVED_DARS)
	public String approvedDars(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setMCMMenu(model, ApplicationConstants.MCM_APPROVED_DARS);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
//			List<Integer> caseStatusList = auditCaseStatusRepository.findByUsedByRoleAndCategory("mcm", "DAR")
//					.stream()
//					.map(AuditCaseStatus::getId)
//					.collect(Collectors.toList());
			List<Integer> caseStatusList = auditCaseStatusRepository.findIdByStatus("recommendedForApproval");
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
		model.addAttribute("pageHeading", "Recommended DARs pending with Allocating officer");
		model.addAttribute("cardHeading", "DAR (Submitted by MCM");
		model.addAttribute("pagename", "Approved DARs");
		model.addAttribute("tabUrl", "approved_dars");
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/"
				+ ApplicationConstants.MCM_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.MCM_UPDATE_DAR_CASE)
	public String showDarCaseDetails(Model model, String caseId, String tabUrl) {
		Optional<AuditMaster> objectAM = auditMasterRepository.findById(caseId);
		if (objectAM.isPresent()) {
			setMCMMenu(model, tabUrl);
			model.addAttribute("tabUrl", tabUrl);
			model.addAttribute("tabName", appMenuRepository.findByUserTypeAndUrl("mcm", tabUrl).get().getName());
			model.addAttribute("auditCaseDetails", objectAM.get());
			model.addAttribute("darDetails", auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(objectAM.get(), objectAM.get().getAction()).get());
			AuditMaster auditMaster = objectAM.get();
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
					auditCaseDateDocumentDetailsRepository.getHighestDateByCaseId(caseId));
		} else {
			return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/"
					+ ApplicationConstants.MCM_SUBMITTED_DAR_CASES;
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/submitted_dar_case_details";
	}

	@GetMapping("/nil_dar_option")
	public String darInputDiv(Model model, String nilDar, String caseId) {
		try {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			model.addAttribute("auditMaster", auditMaster);
			if (!auditMaster.getAssignTo().equalsIgnoreCase("mcm")) {
				model.addAttribute("nonEditable", "true");
			}
			AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditMaster.getAction()).get();
			model.addAttribute("documentDetails", auditCaseDateDocumentDetails);
			if (nilDar != null && nilDar.equals("NO")) {
				model.addAttribute("darDetailsList", auditCaseDateDocumentDetails.getAuditCaseDarDetailsList());
				return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/mcm_nilDarNO";
			} else if (nilDar != null && nilDar.equals("YES")) {
				return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/mcm_nilDarYES";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MCMUserController : darInputDiv : " + e.getMessage());
		}
		return null;
	}

	@PostMapping("/" + ApplicationConstants.L2_UPDATE_DAR_CASE)
	@Transactional
	public String updateDarCaseDetails(RedirectAttributes redirectAttributes,
			@ModelAttribute("caseUpdationActionForm") L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		String message = mcmUserUpdateAuditCaseService.saveDarDetails(l3UserAuditCaseUpdate);
		redirectAttributes.addFlashAttribute("successMessage", message);
		return "redirect:/" + ApplicationConstants.MCM + "/" + ApplicationConstants.MCM_SUBMITTED_DAR_CASES;
	}

	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setMCMMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		model.addAttribute("categories", categoryListRepository.findAllCategoryForAuditCases(locationList));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = mstParametersModuleWiseRepository
						.findAllAssessmentParameterByAuditCategory(category.get().getId(), locationList);
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& mstParametersModuleWiseRepository.findById(parameterId).isPresent()) {
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
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/review_summary_list_new";
	}

	@GetMapping("/review_cases_list")
	public String reviewCasesList(Model model, @RequestParam(required = false) Long categoryId, String parameterId) {
		List<AuditMaster> caseList = null;
		setMCMMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		Optional<Category> categoryObject = categoryListRepository.findById(categoryId);
		if (categoryObject.isPresent()) {
			model.addAttribute("categoryId", categoryObject.get().getId());
		}
		if (categoryId != null && categoryId > 0 && parameterId != null && parameterId.trim().length() > 0
				&& mstParametersModuleWiseRepository.findById(Integer.parseInt(parameterId.trim())).isPresent()) {
			caseList = auditMasterRepository.findAllCasesByCategoryAnd1stParameterId(categoryObject.get().getId(),
					parameterId, locationList);
		} else if (categoryId != null && categoryId > 0) {
			caseList = auditMasterRepository.findAllCasesByCategory(categoryObject.get().getId(), locationList);
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/review_cases_list_new";
	}

	@GetMapping("/" + ApplicationConstants.MCM_RECOMMENDED_TO_OTHER_MODULE)
	public String RecommandedToOtherModules(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setMCMMenu(model, ApplicationConstants.MCM_RECOMMENDED_TO_OTHER_MODULE);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		List<String> auditCaseIds = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForMcm = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("MCM").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForMcm);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndMcmUserId(allMappedLocations,
								userDetails.getUserId());
			} else {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndMcmUserId(
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
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.MCM + "/"
				+ ApplicationConstants.MCM_RECOMMENDED_TO_OTHER_MODULE;
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("McmUser Controller.downloadFile().initiate" + fileName);
			String filesDirectory = fileUploadLocation;
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

	private void setMCMMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.MCM));
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
					model.addAttribute("commonUserDetails", "/mcm/user_details");
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
		setMCMMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
}
