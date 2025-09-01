package com.hp.gstreviewfeedbackapp.audit.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFinalAmountRecoveryDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditParaCategory;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditParaCategoryRepository;
import com.hp.gstreviewfeedbackapp.audit.service.L3UserUpdateAuditCaseService;
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
@RequestMapping("/" + ApplicationConstants.L3)
public class L3UserController {
	private static final Logger logger = LoggerFactory.getLogger(L3UserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private L3UserUpdateAuditCaseService l3UserUpdateAuditCaseService;
	@Autowired
	private AuditParaCategoryRepository auditParaCategoryRepository;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Value("${upload.audit.teamlead.directory}")
	private String fileUploadLocation;
	@Value("${audit.case.assignment}")
	private String auditCaseAssignmentCategory;
	@Value("${upload.audit.directory}")
	private String HqPdfFileUploadLocation;
	@Value("${upload.directory}")
	private String assessmentHqFileLocation;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String index(Model model) {
		setL3Menu(model, ApplicationConstants.DASHBOARD);
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
				userRoleRepository.findByroleCode("L3").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
        List<AnnexureReportRow> report = l3UserUpdateAuditCaseService.getAnnexureReport(allMappedLocations);
        List<String> data = l3UserUpdateAuditCaseService.getDashboardData(allMappedLocations);
        model.addAttribute("allotted_cases", data.get(0));
        model.addAttribute("assigned_cases", data.get(1));
        model.addAttribute("audit_cases_completed", data.get(2));
        model.addAttribute("pending_audit_plan", data.get(3));
        model.addAttribute("pending_DAR", data.get(4));
        model.addAttribute("pending_FAR", data.get(5));
        model.addAttribute("reportData", report);
      
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE)
	public String updateAuditCase(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL3Menu(model, ApplicationConstants.L3_UPDATE_AUDIT_CASE);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL2 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L3").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL2);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository.findAllAuditCasesByWorkingLocationListAndL3UserId(allMappedLocations,
						userDetails.getUserId());
			} else {
				auditCases = auditMasterRepository.findAllAuditCasesByWorkingLocationListAndCatgoryIdAndL3UserId(
						allMappedLocations, categoryId, userDetails.getUserId());
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
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/"
				+ ApplicationConstants.L3_UPDATE_AUDIT_CASE;
	}

	@PostMapping("/" + ApplicationConstants.L3_SHOW_AUDIT_CASE_DETAILS)
	public String showCaseDetails(RedirectAttributes redirectAttributes, @RequestParam String caseId) {
		// set Audit case object
		Optional<AuditMaster> objectAM = auditMasterRepository.findById(caseId);
		if (objectAM.isPresent()) {
			redirectAttributes.addFlashAttribute("auditCaseDetails", objectAM.get());
			// Set active menu
			AuditMaster auditMaster = objectAM.get();
			Integer activeActionPannelId = (auditCaseStatusRepository
					.findBySequence(auditMaster.getAction().getSequence() + 1).isPresent()
							? auditCaseStatusRepository.findBySequence(auditMaster.getAction().getSequence() + 1).get()
									.getId()
							: auditMaster.getAction().getId());
			if (objectAM.get().getAction().getStatus().equalsIgnoreCase("finalAmountRecovery")
					&& objectAM.get().getFullyRecovered() != null
					&& objectAM.get().getFullyRecovered().equalsIgnoreCase("true")) {
				activeActionPannelId = auditCaseStatusRepository.findByStatus("closurereportissued").get().getId();
			} else if (objectAM.get().getAction().getStatus().equalsIgnoreCase("finalAmountRecovery")
					&& objectAM.get().getFullyRecovered() != null
					&& objectAM.get().getFullyRecovered().equalsIgnoreCase("false")) {
				activeActionPannelId = auditCaseStatusRepository.findByStatus("showCauseNotice").get().getId();
			}
			redirectAttributes.addFlashAttribute("activeActionPannelId", activeActionPannelId);
			String[] parameterArray = auditMaster.getParameter().split(",");
			List<MstParametersModuleWise> parameterList = new ArrayList<>();
			for (String value : parameterArray) {
				if (value.trim().length() > 0) {
					parameterList.add(mstParametersModuleWiseRepository.findById(Integer.parseInt(value.trim())).get());
				}
			}
			if (parameterList != null && parameterList.size() > 0) {
				redirectAttributes.addFlashAttribute("parameterList", parameterList);
			}
			List<AuditCaseDateDocumentDetails> dateDocumentDetailsList = auditCaseDateDocumentDetailsRepository
					.findAllByCaseId(objectAM.get());
			List<AuditCaseStatus> auditCaseStatusList = dateDocumentDetailsList.stream()
					.map(AuditCaseDateDocumentDetails::getAction).collect(Collectors.toList());
			if (!objectAM.get().getAction().getCategory().equalsIgnoreCase("Closure Report")) {
				auditCaseStatusList.addAll(auditCaseStatusRepository.findAllByUsedByRoleOrderById("l3").stream()
						.filter(acs -> acs.getSequence() > objectAM.get().getAction().getSequence())
						.filter(acs -> !objectAM.get().getAction().getCategory().equals(acs.getCategory()))
						.collect(Collectors.collectingAndThen(
								Collectors.toMap(AuditCaseStatus::getCategory, acs -> acs, (e1, e2) -> e1),
								map -> new ArrayList<>(map.values()))));
				if (auditCaseStatusRepository.findByStatus("discrepancy_notice").get().getId() < objectAM.get()
						.getAction().getId() || !objectAM.get().getAssignTo().equalsIgnoreCase("L3")
						|| objectAM.get().getAction().getStatus().equalsIgnoreCase("auditplanrejected")) {
					auditCaseStatusList.removeIf(acs -> (acs.getStatus().equalsIgnoreCase("recommended_for_enforcement")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_assessment_adjudication")));
				}
				if (objectAM.get().getAction().getStatus().equalsIgnoreCase("farsubmitted")
						&& objectAM.get().getFullyRecovered() != null
						&& objectAM.get().getFullyRecovered().equalsIgnoreCase("true")) {
					auditCaseStatusList.removeIf(acs -> (acs.getStatus().equalsIgnoreCase("showCauseNotice")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_enforcement")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_assessment_adjudication")));
				} else if (objectAM.get().getAction().getStatus().equalsIgnoreCase("finalAmountRecovery")
						&& objectAM.get().getFullyRecovered() != null
						&& objectAM.get().getFullyRecovered().equalsIgnoreCase("true")) {
					auditCaseStatusList.removeIf(acs -> (acs.getStatus().equalsIgnoreCase("showCauseNotice")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_enforcement")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_assessment_adjudication")));
				} else if (objectAM.get().getAction().getStatus().equalsIgnoreCase("finalAmountRecovery")
						&& objectAM.get().getFullyRecovered() != null
						&& objectAM.get().getFullyRecovered().equalsIgnoreCase("false")) {
					auditCaseStatusList.removeIf(acs -> (acs.getStatus().equalsIgnoreCase("closurereportissued")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_enforcement")
							|| acs.getStatus().equalsIgnoreCase("recommended_for_assessment_adjudication")));
				}
			}
			List<AuditCaseStatus> sortedList = auditCaseStatusList.stream()
					.sorted(Comparator.comparing(AuditCaseStatus::getSequence)).collect(Collectors.toList());
			if (sortedList != null && sortedList.size() > 0) {
				redirectAttributes.addFlashAttribute("auditCaseStatusList", sortedList);
			}
		}
//		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS;
		return "redirect:/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS;
	}

	@GetMapping("/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS)
	public String showCaseDetailsPage(Model model) {
		setL3Menu(model, ApplicationConstants.L3_UPDATE_AUDIT_CASE);
		if (model.containsAttribute("auditCaseDetails")) {
			return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/"
					+ ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS;
		} else {
			return "redirect:/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE;
		}
	}

	@GetMapping("/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS + "/activePannel")
	public String activePannel(Model model, @RequestParam String caseId, @RequestParam Integer activeActionPannelId) {
		activeActionPannelId = l3UserUpdateAuditCaseService.checkIfTheActiveStausIsAuditPlanOrDar(caseId,
				activeActionPannelId, auditCaseStatusRepository.findById(activeActionPannelId).get().getCategory());
		AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
		Optional<AuditCaseStatus> objectStatus = auditCaseStatusRepository.findById(activeActionPannelId);
		if (auditMaster.getAction().getStatus().equalsIgnoreCase("auditcompleted")
				&& objectStatus.get().getStatus().equalsIgnoreCase("auditcompleted")) {
			activeActionPannelId = auditMaster.getAction().getId();
			objectStatus = auditCaseStatusRepository.findById(auditMaster.getAction().getId());
		}
		if (objectStatus.isPresent()) {
			Optional<AuditCaseDateDocumentDetails> object = null;
			if (objectStatus.get().getCategory().equals("Audit Plan")) {
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusIdList(
						caseId, auditCaseStatusRepository.findAllIdByCategory(objectStatus.get().getCategory()));
			} else if (objectStatus.get().getCategory().equals("DAR")) {
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusIdList(
						caseId, auditCaseStatusRepository.findAllIdByCategory(objectStatus.get().getCategory()));
				if (object.isPresent() && !object.get().getAction().getStatus().equalsIgnoreCase("darrejected")) {
					model.addAttribute("nonEditable", "true");
					object.get().getAction().setJspPage("dar_rejected");
				}
				List<String> nilDARdddValues = new ArrayList<>();
				nilDARdddValues.add("NO");
				nilDARdddValues.add("YES");
				model.addAttribute("nilDARdddValues", nilDARdddValues);
			} else if (objectStatus.get().getStatus().equalsIgnoreCase("farsubmitted")) {
				AuditCaseDateDocumentDetails dateDocumentDetailsForDar = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndActionStausIdList(caseId, auditCaseStatusRepository.findAllIdByCategory("DAR"))
						.get();
				model.addAttribute("nilDar", dateDocumentDetailsForDar.getNilDar());
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusId(caseId,
						activeActionPannelId);
				if (object.isPresent()) {
					model.addAttribute("nonEditable", "true");
				}
			} else if (objectStatus.get().getStatus().equalsIgnoreCase("finalAmountRecovery")) {
				AuditCaseDateDocumentDetails dateDocumentDetailsForDar = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndActionStausIdList(caseId, auditCaseStatusRepository.findAllIdByCategory("DAR"))
						.get();
				model.addAttribute("nilDar", dateDocumentDetailsForDar.getNilDar());
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusId(caseId,
						activeActionPannelId);
				if (object.isPresent()) {
					model.addAttribute("nonEditable", "true");
				}
			} else if (objectStatus.get().getStatus().equalsIgnoreCase("showCauseNotice")
					|| objectStatus.get().getStatus().equalsIgnoreCase("recommended_for_assessment_adjudication")) {
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusId(caseId,
						activeActionPannelId);
				List<CustomUserDetails> foUserList = userDetailsService
						.getAllAssignedUserForGivenLocation(auditMaster.getLocationDetails().getLocationId(), 2);
				model.addAttribute("foUserList", foUserList);
				if (object.isPresent()) {
					model.addAttribute("nonEditable", "true");
				}
			} else {
				object = auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusId(caseId,
						activeActionPannelId);
			}
			if (object.isPresent()) {
				model.addAttribute("amdDocument", object.get());
			}
			model.addAttribute("caseId", caseId);
			model.addAttribute("activeStatusId", activeActionPannelId);
			model.addAttribute("previousStatusDate",
					auditMaster.getAction().getStatus().equalsIgnoreCase("caseAssigned")
							? auditMaster.getAssignedDateFromL2ToL3()
							: auditCaseDateDocumentDetailsRepository.getHighestDateByCaseId(auditMaster.getCaseId()));
//			if(activeCaseState.getCategory().equalsIgnoreCase("DAR")) {
//				Optional<AuditCaseDateDocumentDetails> dateDocumentDetails = auditCaseDateDocumentDetailsRepository.findByCaseIdAndActionStausIdList(caseId, auditCaseStatusRepository.findAllIdByCategory("DAR"));
//				
//				if(dateDocumentDetails.isPresent() && !dateDocumentDetails.get().getAction().getStatus().equalsIgnoreCase("darrejected")) {
//					model.addAttribute("nonEditable", "true");
//				}
//				
//				List<String> nilDARdddValues = new ArrayList<>();
//				nilDARdddValues.add("NO");
//				nilDARdddValues.add("YES");
//				model.addAttribute("nilDARdddValues", nilDARdddValues);
//			}
			return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/" + objectStatus.get().getJspPage();
		}
		return "redirect:/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE;
	}

	@GetMapping("/nil_dar_option")
	public String darInputDiv(Model model, @RequestParam String nilDar, @RequestParam(required = false) String caseId) {
		try {
			if (caseId == null || caseId.trim().length() == 0) {
				if (nilDar != null && nilDar.equals("NO")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarNO";
				} else if (nilDar != null && nilDar.equals("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarYES";
				}
			} else {
				AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
				AuditCaseDateDocumentDetails dateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndActionStausIdList(auditMaster.getCaseId(),
								auditCaseStatusRepository.findAllIdByCategory("DAR"))
						.get();
				model.addAttribute("auditMaster", auditMaster);
				model.addAttribute("dateDocumentDetails", dateDocumentDetails);
				if (!auditMaster.getAction().getStatus().equalsIgnoreCase("darrejected")) {
					model.addAttribute("nonEditable", "true");
				}
				if (auditMaster.getAction().getStatus().equalsIgnoreCase("darrejected")) {
					model.addAttribute("darRejected", "true");
				}
				if (nilDar != null && nilDar.equals("NO") && dateDocumentDetails.getNilDar().equalsIgnoreCase("NO")) {
					model.addAttribute("paraCount", (dateDocumentDetails.getAuditCaseDarDetailsList().size() - 1));
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarNO_rejected";
				} else if (nilDar != null && nilDar.equals("NO")
						&& dateDocumentDetails.getNilDar().equalsIgnoreCase("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarNO";
				} else if (nilDar != null && nilDar.equals("YES")) {
//					model.addAttribute("dateDocumentDetails", dateDocumentDetails);
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarYES";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserController : darInputDiv : " + e.getMessage());
		}
		return null;
	}

	@GetMapping("/loadNilDarNOInputFields")
	public String loadInputFieldsForDar(@RequestParam("counter") int counter,
			@RequestParam(required = false) String caseId, Model model) {
		model.addAttribute("counter", counter);
		List<AuditParaCategory> paraCategoryList = auditParaCategoryRepository.findAllByActiveStatus(true);
		if (paraCategoryList != null && paraCategoryList.size() > 0) {
			model.addAttribute("paraCategoryList", paraCategoryList);
		}
		if (caseId != null && caseId.length() > 0) {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			AuditCaseDateDocumentDetails dateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndActionStausIdList(auditMaster.getCaseId(),
							auditCaseStatusRepository.findAllIdByCategory("DAR"))
					.get();
			List<AuditCaseDarDetails> caseDarDetailsList = dateDocumentDetails.getAuditCaseDarDetailsList();
			if (caseDarDetailsList.size() > counter) {
				model.addAttribute("paraCategory", caseDarDetailsList.get(counter).getAuditParaCategory().getId());
				model.addAttribute("amount_involved", caseDarDetailsList.get(counter).getAmountInvolved());
				model.addAttribute("amount_recovered", caseDarDetailsList.get(counter).getAmountRecovered());
				model.addAttribute("dropped_amount", caseDarDetailsList.get(counter).getAmountDropped());
				model.addAttribute("amountToBe_recovered", caseDarDetailsList.get(counter).getAmountToBeRecovered());
				model.addAttribute("dar_Details_Id", caseDarDetailsList.get(counter).getId());
				model.addAttribute("commentFromL2", caseDarDetailsList.get(counter).getCommentL2());
				model.addAttribute("raisedQuery", caseDarDetailsList.get(counter).getRaiseQuery());
				model.addAttribute("commentFromMcm", caseDarDetailsList.get(counter).getCommentMcm());
				model.addAttribute("old_para", "true");
			}
			if (!auditMaster.getAction().getStatus().equalsIgnoreCase("darrejected")) {
				model.addAttribute("nonEditable", "true");
			}
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilDarNOInputFileds";
	}

	@GetMapping("/nil_far_option")
	public String farInputDiv(Model model, @RequestParam String caseId) {
		try {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			model.addAttribute("auditMaster", auditMaster);
			AuditCaseDateDocumentDetails auditDDDarObject = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndActionStausIdList(caseId, auditCaseStatusRepository.findAllIdByCategory("DAR"))
					.get();
			Optional<AuditCaseDateDocumentDetails> auditDDFarObject = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMasterRepository.findById(caseId).get(),
							auditCaseStatusRepository.findByStatus("farsubmitted").get());
			if (!auditDDFarObject.isPresent()) {
				if (auditDDDarObject.getNilDar().equalsIgnoreCase("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFarYES";
				} else {
					model.addAttribute("paraCount", (auditDDDarObject.getAuditCaseDarDetailsList().size() - 1));
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFarNO";
				}
			} else {
				AuditCaseDateDocumentDetails dateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(auditMaster,
								auditCaseStatusRepository.findByStatus("farsubmitted").get())
						.get();
				model.addAttribute("dateDocumentDetails", dateDocumentDetails);
				model.addAttribute("nonEditable", "true");
				model.addAttribute("paraCount", (auditDDFarObject.get().getAuditCaseFarDetailsList().size() - 1));
				if (auditDDDarObject.getNilDar().equalsIgnoreCase("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFarYES";
				} else {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFarNO";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserController : darInputDiv : " + e.getMessage());
		}
		return null;
	}

	@GetMapping("/loadNilFarNOInputFields")
	public String loadInputFieldsForFar(@RequestParam("counter") int counter,
			@RequestParam(required = false) String caseId, Model model) {
		model.addAttribute("counter", counter);
		List<AuditParaCategory> paraCategoryList = auditParaCategoryRepository.findAllByActiveStatus(true);
		if (paraCategoryList != null && paraCategoryList.size() > 0) {
			model.addAttribute("paraCategoryList", paraCategoryList);
		}
		if (caseId != null && caseId.length() > 0) {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			AuditCaseDateDocumentDetails dateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditCaseStatusRepository.findByStatus("farsubmitted").get())
					.isPresent()
							? auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(auditMaster,
									auditCaseStatusRepository.findByStatus("farsubmitted").get()).get()
							: auditCaseDateDocumentDetailsRepository
									.findByCaseIdAndActionStausIdList(auditMaster.getCaseId(),
											auditCaseStatusRepository.findAllIdByCategory("DAR"))
									.get();
			if (dateDocumentDetails.getAuditCaseFarDetailsList() != null
					&& dateDocumentDetails.getAuditCaseFarDetailsList().size() > 0) {
				List<AuditCaseFarDetails> caseFarDetailsList = dateDocumentDetails.getAuditCaseFarDetailsList();
				if (caseFarDetailsList.size() > counter) {
					model.addAttribute("paraCategory", caseFarDetailsList.get(counter).getAuditParaCategory().getId());
					model.addAttribute("amount_involved", caseFarDetailsList.get(counter).getAmountInvolved());
					model.addAttribute("amount_recovered", caseFarDetailsList.get(counter).getAmountRecovered());
					model.addAttribute("dropped_amount", caseFarDetailsList.get(counter).getAmountDropped());
					model.addAttribute("amountToBe_recovered",
							caseFarDetailsList.get(counter).getAmountToBeRecovered());
				}
				model.addAttribute("nonEditable", "true");
			} else {
				List<AuditCaseDarDetails> caseDarDetailsList = dateDocumentDetails.getAuditCaseDarDetailsList();
				if (caseDarDetailsList.size() > counter) {
					model.addAttribute("paraCategory", caseDarDetailsList.get(counter).getAuditParaCategory().getId());
					model.addAttribute("amount_involved", caseDarDetailsList.get(counter).getAmountInvolved());
					model.addAttribute("amount_recovered", caseDarDetailsList.get(counter).getAmountRecovered());
					model.addAttribute("dropped_amount", caseDarDetailsList.get(counter).getAmountDropped());
					model.addAttribute("amountToBe_recovered",
							caseDarDetailsList.get(counter).getAmountToBeRecovered());
				}
			}
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFarNOInputFileds";
	}

	@GetMapping("/nilFinalAmountRecoveryOption")
	public String finalAmountRecoveryInputDiv(Model model, @RequestParam String caseId) {
		try {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			model.addAttribute("auditMaster", auditMaster);
			AuditCaseDateDocumentDetails auditDDFarObject = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditCaseStatusRepository.findByStatus("farsubmitted").get())
					.get();
			Optional<AuditCaseDateDocumentDetails> auditDDFar2Object = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster,
							auditCaseStatusRepository.findByStatus("finalAmountRecovery").get());
			if (!auditDDFar2Object.isPresent()) {
				if (auditDDFarObject.getNilDar().equalsIgnoreCase("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFinalAmountRecoverytYES";
				} else {
					model.addAttribute("paraCount", (auditDDFarObject.getAuditCaseFarDetailsList().size() - 1));
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFinalAmountRecoveryNO";
				}
			} else {
				model.addAttribute("dateDocumentDetails", auditDDFar2Object.get());
				model.addAttribute("nonEditable", "true");
				model.addAttribute("paraCount",
						(auditDDFar2Object.get().getAuditCaseFinalAmountRecoveryDetailsList().size() - 1));
				if (auditDDFar2Object.get().getNilDar().equalsIgnoreCase("YES")) {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFinalAmountRecoverytYES";
				} else {
					return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFinalAmountRecoveryNO";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserController : darInputDiv : " + e.getMessage());
		}
		return null;
	}

	@GetMapping("/loadNilFinalAmountRecoveryNOInputFields")
	public String loadInputFieldsForFinalAmountRecovery(@RequestParam("counter") int counter,
			@RequestParam(required = false) String caseId, Model model) {
		model.addAttribute("counter", counter);
		List<AuditParaCategory> paraCategoryList = auditParaCategoryRepository.findAllByActiveStatus(true);
		if (paraCategoryList != null && paraCategoryList.size() > 0) {
			model.addAttribute("paraCategoryList", paraCategoryList);
		}
		if (caseId != null && caseId.length() > 0) {
			AuditMaster auditMaster = auditMasterRepository.findById(caseId).get();
			AuditCaseDateDocumentDetails dateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster,
							auditCaseStatusRepository.findByStatus("finalAmountRecovery").get())
					.isPresent()
							? auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(auditMaster,
									auditCaseStatusRepository.findByStatus("finalAmountRecovery").get()).get()
							: auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(auditMaster,
									auditCaseStatusRepository.findByStatus("farsubmitted").get()).get();
			if (dateDocumentDetails.getAuditCaseFinalAmountRecoveryDetailsList() != null
					&& dateDocumentDetails.getAuditCaseFinalAmountRecoveryDetailsList().size() > 0) {
				List<AuditCaseFinalAmountRecoveryDetails> caseFar2DetailsList = dateDocumentDetails
						.getAuditCaseFinalAmountRecoveryDetailsList();
				if (caseFar2DetailsList.size() > counter) {
					model.addAttribute("paraCategory", caseFar2DetailsList.get(counter).getAuditParaCategory().getId());
					model.addAttribute("amount_involved", caseFar2DetailsList.get(counter).getAmountInvolved());
					model.addAttribute("amount_recovered", caseFar2DetailsList.get(counter).getAmountRecovered());
					model.addAttribute("dropped_amount", caseFar2DetailsList.get(counter).getAmountDropped());
					model.addAttribute("amountToBe_recovered",
							caseFar2DetailsList.get(counter).getAmountToBeRecovered());
				}
				model.addAttribute("nonEditable", "true");
			} else {
				List<AuditCaseFarDetails> caseFarDetailsList = dateDocumentDetails.getAuditCaseFarDetailsList();
				if (caseFarDetailsList.size() > counter) {
					model.addAttribute("paraCategory", caseFarDetailsList.get(counter).getAuditParaCategory().getId());
					model.addAttribute("amount_involved", caseFarDetailsList.get(counter).getAmountInvolved());
					model.addAttribute("amount_recovered", caseFarDetailsList.get(counter).getAmountRecovered());
					model.addAttribute("dropped_amount", caseFarDetailsList.get(counter).getAmountDropped());
					model.addAttribute("amountToBe_recovered",
							caseFarDetailsList.get(counter).getAmountToBeRecovered());
				}
			}
		}
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/nilFinalAmountRecoveryNOInputFileds";
	}

	@PostMapping("/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE_DETAILS)
	public String caseUpdate(Model model,
			@ModelAttribute("caseUpdationActionForm") L3UserAuditCaseUpdate l3UserAuditCaseUpdate,
			RedirectAttributes redirectAttributes) {
		try {
			String output = null;
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			Optional<AuditCaseStatus> objectStatus = auditCaseStatusRepository
					.findById(l3UserAuditCaseUpdate.getUpdateStatusId());
			if (objectAM.isPresent() && objectStatus.isPresent()) {
				AuditCaseStatus auditCaseUpdateStatus = objectStatus.get();
				if (auditCaseUpdateStatus.getStatus().equals("adt_01_issued")
						|| auditCaseUpdateStatus.getCategory().equals("Audit Plan")
						|| auditCaseUpdateStatus.getStatus().equals("commencement_of_audit")
						|| auditCaseUpdateStatus.getStatus().equals("discrepancy_notice")) {
					output = l3UserUpdateAuditCaseService
							.updateAuditCaseForADT01IssuedOrAuditPlanOrCommencementOfAuditOrDiscrepancyNotice(
									l3UserAuditCaseUpdate);
				} else if (auditCaseUpdateStatus.getCategory().equals("DAR")) {
					output = l3UserUpdateAuditCaseService.updateAuditCaseForDar(l3UserAuditCaseUpdate);
				} else if (auditCaseUpdateStatus.getStatus().equals("farsubmitted")) {
					output = l3UserUpdateAuditCaseService.updateAuditCaseForAdt02Far(l3UserAuditCaseUpdate);
				} else if (auditCaseUpdateStatus.getStatus().equals("finalAmountRecovery")) {
					output = l3UserUpdateAuditCaseService.updateAuditCaseForFinalAmountRecovery(l3UserAuditCaseUpdate);
				} else if (auditCaseUpdateStatus.getStatus().equals("showCauseNotice")
						|| auditCaseUpdateStatus.getStatus().equals("recommended_for_assessment_adjudication")) {
					output = l3UserUpdateAuditCaseService.updateAuditCaseForShowCauseNotice(l3UserAuditCaseUpdate);
				} else if (auditCaseUpdateStatus.getStatus().equals("closurereportissued")) {
					output = l3UserUpdateAuditCaseService.updateAuditCaseForClosureReport(l3UserAuditCaseUpdate);
				}
				if (output != null && output.length() > 0) {
					redirectAttributes.addFlashAttribute("successMessage", output);
//					return "redirect:/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_SHOW_AUDIT_CASE_DETAILS + 
//							"?caseId=" + l3UserAuditCaseUpdate.getCaseId();
					return showCaseDetails(redirectAttributes, l3UserAuditCaseUpdate.getCaseId());
				}
			}
			if (objectStatus.isPresent()) {
				model.addAttribute("activeStatusId", l3UserAuditCaseUpdate.getUpdateStatusId());
				return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/"
						+ objectStatus.get().getJspPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserController : caseUpdate : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.L3 + "/" + ApplicationConstants.L3_UPDATE_AUDIT_CASE;
	}

	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setL3Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L3").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		model.addAttribute("categories",
				categoryListRepository.findAllCategoryForAuditCasesByL3UserId(userDetails.getUserId(), locationList));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = mstParametersModuleWiseRepository
						.findAllAssessmentParameterByAuditCategoryAndL3UserId(category.get().getId(),
								userDetails.getUserId(), locationList);
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& mstParametersModuleWiseRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
					model.addAttribute("categoryTotals",
							auditMasterRepository.getAllCaseCountByCategoryAnd1stParameterIdAndL3UserId(
									category.get().getId(), parameterId.toString(), locationList,
									userDetails.getUserId()));
				} else {
					model.addAttribute("categoryTotals", auditMasterRepository.getAllCaseCountByCategoryAndL3UserId(
							category.get().getId(), locationList, userDetails.getUserId()));
				}
			}
		}
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/review_summary_list_new";
	}

	@GetMapping("/review_cases_list")
	public String reviewCasesList(Model model, @RequestParam(required = false) Long categoryId, String parameterId) {
		List<AuditMaster> caseList = null;
		setL3Menu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L3").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		Optional<Category> categoryObject = categoryListRepository.findById(categoryId);
		if (categoryObject.isPresent()) {
			model.addAttribute("categoryId", categoryObject.get().getId());
		}
		if (categoryId != null && categoryId > 0 && parameterId != null && parameterId.trim().length() > 0
				&& mstParametersModuleWiseRepository.findById(Integer.parseInt(parameterId.trim())).isPresent()) {
			caseList = auditMasterRepository.findAllCasesByCategoryAnd1stParameterIdAndL3UserId(
					categoryObject.get().getId(), parameterId, locationList, userDetails.getUserId());
		} else if (categoryId != null && categoryId > 0) {
			caseList = auditMasterRepository.findAllCasesByCategory(categoryObject.get().getId(), locationList);
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return "/" + ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/review_cases_list_new";
	}

	@GetMapping("/" + ApplicationConstants.L3_RECOMMENDED_TO_OTHER_MODULE)
	public String RecommandedToOtherModules(Model model, @RequestParam(required = false) Integer categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		setL3Menu(model, ApplicationConstants.L3_RECOMMENDED_TO_OTHER_MODULE);
		UserDetails userDetails = objectUserDetails.get();
		List<AuditMaster> auditCases = null;
		List<String> auditCaseIds = null;
		// Getting all user role mapping for this user and getting all mapped location
		List<UserRoleMapping> allURMIdForL3 = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("L3").get());
		// Set Audit Cases
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForL3);
		if (allMappedLocations != null && allMappedLocations.size() > 0) {
			if (categoryId == null || categoryId.equals(0)) {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndL3UserId(allMappedLocations,
								userDetails.getUserId());
			} else {
				auditCases = auditMasterRepository
						.findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndL3UserId(
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
		return ApplicationConstants.AUDIT + "/" + ApplicationConstants.L3 + "/"
				+ ApplicationConstants.L3_RECOMMENDED_TO_OTHER_MODULE;
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
			logger.info("L3 Controller.downloadFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("L3 Controller.downloadFile().catch() : " + ex.getMessage());
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

	private void setL3Menu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.L3));
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
					model.addAttribute("commonUserDetails", "/l3/user_details");
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
		setL3Menu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
  

}
