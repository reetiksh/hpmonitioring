package com.hp.gstreviewfeedbackapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.ApproverCaseWorkFlow;
import com.hp.gstreviewfeedbackapp.model.ApproverRemarksToApproveTheCase;
import com.hp.gstreviewfeedbackapp.model.ApproverRemarksToRejectTheCase;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.TransferRemarks;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.ApproverCaseWorkFlowRepository;
import com.hp.gstreviewfeedbackapp.repository.ApproverRemarksToApproveTheCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.ApproverRemarksToRejectTheCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.FOReviewSummeryList;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.impl.ApproverUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.AP_URL)
public class ApproverUserController {
	private static final Logger logger = LoggerFactory.getLogger(ReviewUserController.class);
	private static final Integer NULL = null;
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	ApproverRemarksToApproveTheCaseRepository approverRemarksToApproveTheCaseRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkFlowRepository;
	@Autowired
	private TransferRemarksRepository transferRemarksRepository;
	@Autowired
	ApproverCaseWorkFlowRepository approverCaseWorkFlowRepository;
	@Autowired
	ApproverRemarksToRejectTheCaseRepository approverRemarksToRejectTheCaseRepository;
	@Autowired
	UserDetailsRepository userDetailsRepository;
	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private DashboardRepository dashboardRepository;
	@Autowired
	private FieldUserController fieldUserController;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	MstNotificationsRepository mstNotificationsRepository;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private FOReviewSummeryList FOReviewSummeryList;
	@Autowired
	private FieldUserController FieldUserController;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Autowired
	private ApproverUserServiceImpl approverUserServiceImpl;
	@Autowired
	private MstParametersModuleWiseRepository parametersModuleWiseRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(Model model, @RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "view", required = false) String view,
			@RequestParam(required = false) Integer parameter) {
		logger.info("ApproverUserController.dashboard() : ApplicationConstants.DASHBOARD");
		setAPMenu(model, ApplicationConstants.DASHBOARD);
		DecimalFormat formatter = new DecimalFormat("##,##,##,##,000",
				new DecimalFormatSymbols(new Locale("en", "IN")));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			// Get locations for FO roles
			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
					userRoleRepository.findByroleCode("AP").get());
//			JSONArray totalIndicativeTaxValueCategoryWise = fieldUserServiceImpl.getTotalIndicativeTaxValueCategoryWise(locationMap);
			List<List<String>> totalIndicativeTaxValueCategoryWise = fieldUserServiceImpl
					.getTotalIndicativeTaxValueCategoryWiseList(foDashBoardWorkingLocations);
			if (totalIndicativeTaxValueCategoryWise != null && totalIndicativeTaxValueCategoryWise.size() > 0) {
				model.addAttribute("indicativeTaxValue", totalIndicativeTaxValueCategoryWise);
			}
//			JSONArray totalDemandCategoryWise = fieldUserServiceImpl.getTotalDemandCategoryWise(locationMap);
			List<List<String>> totalDemandCategoryWise = fieldUserServiceImpl
					.getTotalDemandCategoryWiseList(foDashBoardWorkingLocations);
			if (totalDemandCategoryWise != null && totalDemandCategoryWise.size() > 0) {
				model.addAttribute("demandValue", totalDemandCategoryWise);
			}
//			JSONArray totalRecoveryCategoryWise = fieldUserServiceImpl.getTotalRecoveryCategoryWise(locationMap);
			List<List<String>> totalRecoveryCategoryWise = fieldUserServiceImpl
					.getTotalRecoveryCategoryWiseList(foDashBoardWorkingLocations);
			if (totalRecoveryCategoryWise != null && totalRecoveryCategoryWise.size() > 0) {
				model.addAttribute("recoveryValue", totalRecoveryCategoryWise);
			}
			/************** dashboard consolidate case stage list start ******************/
			List<FoConsolidateCategoryWiseCaseData> consolidateCategoryWiseDataList = fieldUserServiceImpl
					.getTotalCategoryWiseCasesList(returnWorkingLocation(userDetails.getUserId()));
			model.addAttribute("consolidateCategoryWiseDataList", consolidateCategoryWiseDataList);
			/************** dashboard consolidate case stage list end ******************/
			String dashBoardTotalCases = fieldUserServiceImpl.getDashBoardTotalCases(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalCases", ((dashBoardTotalCases == null) ? "0" : dashBoardTotalCases));
			String dashBoardTotalAcknowledgedCases = fieldUserServiceImpl
					.getDashBoardTotalAcknowledgedCases(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalAcknowledgedCases",
					((dashBoardTotalAcknowledgedCases == null) ? "0" : dashBoardTotalAcknowledgedCases));
			String dashBoardTotalInitiatedCases = fieldUserServiceImpl
					.getDashBoardTotalInitiatedCases(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalInitiatedCases",
					((dashBoardTotalInitiatedCases == null) ? "0" : dashBoardTotalInitiatedCases));
			String dashBoardTotalCasesClosedByFo = fieldUserServiceImpl
					.getDashBoardTotalCasesClosedByFo(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalCasesClosedByFo", dashBoardTotalCasesClosedByFo);
			Long dashBoardTotalSuspectedIndicativeAmount = fieldUserServiceImpl
					.getDashBoardTotalSuspectedIndicativeAmount(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalSuspectedIndicativeAmount",
					((dashBoardTotalSuspectedIndicativeAmount == null) ? "0"
							: formatter.format(dashBoardTotalSuspectedIndicativeAmount).toString()));
			Long dashBoardTotalAmount = fieldUserServiceImpl.getDashBoardTotalAmount(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalAmount",
					((dashBoardTotalAmount == null) ? "0" : formatter.format(dashBoardTotalAmount).toString()));
			Long dashBoardTotalDemand = fieldUserServiceImpl.getDashBoardTotalDemand(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalDemand",
					((dashBoardTotalDemand == null) ? "0" : formatter.format(dashBoardTotalDemand).toString()));
			Long dashBoardTotalRecovery = fieldUserServiceImpl.getDashBoardTotalRecovery(foDashBoardWorkingLocations);
			model.addAttribute("dashBoardTotalRecovery",
					((dashBoardTotalRecovery == null) ? "0" : formatter.format(dashBoardTotalRecovery).toString()));
			int categoryInt = 0;
			List<String> circlelocation = null;
			List<String> districtlocation = null;
			List<DashboardDistrictCircle> circleList = null;
			List<DashboardDistrictCircle> zoneList = null;
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 5);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			Boolean hasRoleDistrict = fieldUserController.isRoleDistrict(locationId);
			List<MstParametersModuleWise> parameterList = mstParametersRepository.findAllCasesParameter();
			model.addAttribute("parameterList", parameterList);
			Optional<MstParametersModuleWise> parameterObject = mstParametersRepository
					.findById((parameter != null && parameter > 0) ? parameter : 0);
			if (parameterObject.isPresent()) {
				model.addAttribute("parameterId", parameter);
			}
			Optional<String> verificationTotalCases = enforcementReviewCaseRepository
					.getVerificationTotalCases(locationId);
			model.addAttribute("verificationTotalCases",
					verificationTotalCases.isPresent() ? verificationTotalCases.get() : "0");
			Optional<String> pendingVerificationTotalCases = enforcementReviewCaseRepository
					.getPendingVerificationTotalCases(locationId);
			model.addAttribute("pendingVerificationTotalCases",
					pendingVerificationTotalCases.isPresent() ? pendingVerificationTotalCases.get() : "0");
			Optional<String> approvalTotalCases = enforcementReviewCaseRepository.getApprovalTotalCases(locationId);
			model.addAttribute("approvalTotalCases", approvalTotalCases.isPresent() ? approvalTotalCases.get() : "0");
			Optional<String> pendingApprovalTotalCases = enforcementReviewCaseRepository
					.getPendingApprovalTotalCases(locationId);
			model.addAttribute("pendingApprovalTotalCases",
					pendingApprovalTotalCases.isPresent() ? pendingApprovalTotalCases.get() : "0");
			if (locationId.contains("HP")) {
				if (category == null && financialyear == null && view == null) {
					circleList = dashboardRepository.getStateCircle();
					zoneList = dashboardRepository.getStateZone();
				} else if (category != null && view != null && parameterObject.isEmpty()) {
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getStateCircleByCategory(categoryInt);
						} else if (financialyear != null) {
							circleList = dashboardRepository.getStateCircleByCategoryFinancialyear(categoryInt,
									financialyear);
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategory(categoryInt, locationId);
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyear(categoryInt,
									financialyear, locationId);
						}
					}
				} else if (category != null && view != null && parameterObject.isPresent()) { // When Category, View and
					// Parameter is present
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getStateCircleByCategoryAndParameterName(categoryInt,
									parameterObject.get().getParamName());
						} else if (financialyear != null) {
							circleList = dashboardRepository.getStateCircleByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, parameterObject.get().getParamName());
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName());
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, locationId, parameterObject.get().getParamName());
						}
					}
				}
			} else {
				if (category == null && financialyear == null && view == null) {
					circleList = dashboardRepository.getLocationCircle(locationId);
					zoneList = dashboardRepository.getLocationZone(locationId);
				} else if (category != null && view != null && parameterObject.isEmpty()) {
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getLocationCircleByCategory(categoryInt, locationId);
						} else if (financialyear != null) {
							circleList = dashboardRepository.getLocationCircleByCategoryFinancialyear(categoryInt,
									financialyear, locationId);
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategory(categoryInt, locationId);
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyear(categoryInt,
									financialyear, locationId);
						}
					}
				} else if (category != null && view != null && parameterObject.isPresent()) { // When category, View and
					// Parameter is present
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getLocationCircleByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName());
						} else if (financialyear != null) {
							circleList = dashboardRepository.getLocationCircleByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, locationId, parameterObject.get().getParamName());
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName());
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, locationId, parameterObject.get().getParamName());
						}
					}
				}
			}
			model.addAttribute("financialyear", yearlist);
			model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
			model.addAttribute("circleList", circleList);
			model.addAttribute("zoneList", zoneList);
			model.addAttribute("category", category);
			model.addAttribute("year", financialyear);
			model.addAttribute("viewtype", view);
			model.addAttribute("hasRoleDistrict", hasRoleDistrict);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?category=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() + "&view=" + dashboard.getView();
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_APPROVE_REJECT_CASES)
	public String getVerifyCasesList(Model model) {
		logger.info("ApproverUserController.getVerifyCasesList()" + ApplicationConstants.AP_URL_APPROVE_REJECT_CASES);
		setAPMenu(model, ApplicationConstants.AP_URL_APPROVE_REJECT_CASES);
		model.addAttribute("actionStatus", "verifyerRecommended");
		// setAPApproveRejectedCases(model,
		// ApplicationConstants.AP_URL_APPROVE_REJECT_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_APPROVE_REJECT_CASES;
	}

	@GetMapping("/" + ApplicationConstants.AP_DEEM_APPROVED_CASES)
	public String getDeemApprovedCasesList(Model model) {
		logger.info("ApproverUserController.getDeemApprovedCasesList()" + ApplicationConstants.AP_DEEM_APPROVED_CASES);
		setAPMenu(model, ApplicationConstants.AP_DEEM_APPROVED_CASES);
		model.addAttribute("actionStatus", "deemApproved");
		// setAPApproveRejectedCases(model,
		// ApplicationConstants.AP_URL_APPROVE_REJECT_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_DEEM_APPROVED_CASES;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_APPROVE_REJECT_CASES_AJAX)
	@ResponseBody
	public ResponseEntity<?> getApproverCasesList(@RequestParam(defaultValue = "0") int draw,
			@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int length,
			@RequestParam(required = false) String searchValue, @RequestParam(required = false) String actionStatus) {
		int currentPage = start / length;
		Pageable pageable = PageRequest.of(currentPage, length);
		Page<EnforcementReviewCase> pagesList = null;
		Map<String, Object> response = new HashMap<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		List<UserRoleMapping> allURMIdForAp = userRoleMappingRepository.findAllByUserDetailsAndUserRole(
				objectUserDetails.get(), userRoleRepository.findByroleCode("AP").get());
		List<String> allMappedLocations = adminUpdateUserDetails
				.getAllMappedLocationsFromUserRoleMappingList(allURMIdForAp);
		Boolean hasStateAccess = allMappedLocations.contains("HP");
		try {
			if (searchValue == null || searchValue.trim().length() == 0) {
				pagesList = approverUserServiceImpl.findAllDataListByDefault(pageable, actionStatus, hasStateAccess,
						allMappedLocations);
			} else {
				pagesList = approverUserServiceImpl.findAllDataListBySearchValue(searchValue, pageable, actionStatus,
						hasStateAccess, allMappedLocations);
			}
			response.put("draw", draw);
			response.put("recordsTotal", pagesList.getTotalElements());
			response.put("recordsFiltered", pagesList.getTotalElements());
			response.put("data", pagesList.getContent());
		} catch (Exception e) {
			logger.error("ApproverController: getApproverCasesList()" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get_all_ap_revert_remarks")
	@ResponseBody
	public List<ApproverRemarksToRejectTheCase> getAllAssignedUserForALocation() {
		List<ApproverRemarksToRejectTheCase> approverRemarksToRejectTheCaseList = approverRemarksToRejectTheCaseRepository
				.findAllByOrderByIdDesc();
		return approverRemarksToRejectTheCaseList;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_APPEAL_REVISION_CASES)
	public String getAppealRevisionCaesList(Model model) {
		logger.info(
				"ApproverUserController.getAppealRevisionCaesList() : ApplicationConstants.AP_URL_APPEAL_REVISION_CASES");
		setAPMenu(model, ApplicationConstants.AP_URL_APPEAL_REVISION_CASES);
		setAPAppealRevisionCases(model, ApplicationConstants.AP_URL_APPEAL_REVISION_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_APPEAL_REVISION_CASES;
	}

	@PostMapping("/" + ApplicationConstants.AP_URL_REJECT_APPEAL_REVISION_CASES)
	public String rejectAppealRevisionCase(@RequestParam("appealRevisionRejectGstin") String appealRevisionRejectGstin,
			@RequestParam("appealRevisionRejectCaseReportingDate") String appealRevisionRejectCaseReportingDate,
			@RequestParam("appealRevisionRejectPeriod") String appealRevisionRejectPeriod,
			@RequestParam("appealRevisionRejectRemarksValue") String appealRevisionRejectRemarks,
			String appealRevisionRejectFilePath, Model model) throws ParseException {
		logger.info(
				"ApproverUserController.rejectAppealRevisionCase() : ApplicationConstants.AP_URL_REJECT_APPEAL_REVISION_CASES");
		setAPAppealRevisionRejecedCase(model, ApplicationConstants.AP_URL_APPEAL_REVISION_CASES,
				appealRevisionRejectGstin, appealRevisionRejectCaseReportingDate, appealRevisionRejectPeriod,
				appealRevisionRejectRemarks, appealRevisionRejectFilePath);
		String url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/"
				+ ApplicationConstants.AP_URL_APPEAL_REVISION_CASES;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.AP_URL_APPROVE_APPEAL_REVISION_CASES)
	public String approveAppealRevisionCase(@RequestParam("appRevApproveGstiNo") String appRevApproveGstiNo,
			@RequestParam("appRevApproveReportingdate") String appRevApproveReportingdate,
			@RequestParam("appRevApprovePeriod") String appRevApprovePeriod,
			@RequestParam("appealRevisionApproveRemarks") String appealRevisionApproveRemarks,
			String appealRevisionApproveFilePath, String appealRevisionApproved, Model model) throws ParseException {
		logger.info(
				"ApproverUserController.approveAppealRevisionCase() : ApplicationConstants.AP_URL_APPROVE_APPEAL_REVISION_CASES");
		setAPAppealRevisionApproveCase(model, ApplicationConstants.AP_URL_APPEAL_REVISION_CASES, appRevApproveGstiNo,
				appRevApproveReportingdate, appRevApprovePeriod, appealRevisionApproveRemarks,
				appealRevisionApproveFilePath, appealRevisionApproved);
		String url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/"
				+ ApplicationConstants.AP_URL_APPEAL_REVISION_CASES;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.AP_URL_APPROVED_CASES_WITH_REMARKS)
	public String getRecomendedForUserList(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("apApproveRemarks") String apApproveRemarks, String actionStatus, Model model)
			throws ParseException {
		logger.info(
				"ApproverUserController.getRecomendedForUserList() : ApplicationConstants.AP_URL_APPROVED_CASES_WITH_REMARKS");
		setAPMenuRemarksApproved(model, ApplicationConstants.AP_URL_APPROVED_CASES_WITH_REMARKS, gstinno, reportingdate,
				period, apApproveRemarks);
		String url;
		if (actionStatus.equals("verifyerRecommended")) {
			url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/"
					+ ApplicationConstants.AP_URL_APPROVE_REJECT_CASES;
		} else {
			url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_DEEM_APPROVED_CASES;
		}
		return url;
	}

	@PostMapping("/" + ApplicationConstants.AP_URL_REJECTED_CASES_WITH_REMARKS)
	public String getRecomendedWithRaiseQueryForUserList(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("apRejectedRemarks") String apRejectedRemarks,
			@RequestParam("apRejectedTextValueRemarks") String apRejectedTextValueRemarks,
			@RequestParam("fileAttachedWhileRevert") MultipartFile fileAttachedWhileRevert, String actionStatus,
			Model model) throws ParseException, IOException {
		logger.info(
				"ApproverUserController.getRecomendedWithRaiseQueryForUserList() : ApplicationConstants.AP_URL_REJECTED_CASES_WITH_REMARKS");
		setAPMenuRemarksRejected(model, ApplicationConstants.AP_URL_APPROVE_REJECT_CASES, gstinno, reportingdate,
				period, apRejectedRemarks, apRejectedTextValueRemarks, fileAttachedWhileRevert);
		String url;
		if (actionStatus.equals("verifyerRecommended")) {
			url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/"
					+ ApplicationConstants.AP_URL_APPROVE_REJECT_CASES;
		} else {
			url = "redirect:" + "/" + ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_DEEM_APPROVED_CASES;
		}
		return url;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_APPROVED_CASES)
	public String getApprovedCasesList(Model model) {
		logger.info("ApproverUserController.getApprovedCasesList() : ApplicationConstants.AP_URL_APPROVED_CASES");
		setAPMenu(model, ApplicationConstants.AP_URL_APPROVED_CASES);
		setAPForApprovedCases(model, ApplicationConstants.AP_URL_APPROVED_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_APPROVED_CASES;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_APPEALED_CASES)
	public String getAppealedCasesList(Model model) {
		logger.info("ApproverUserController.getAppealedCasesList() : ApplicationConstants.AP_URL_APPEALED_CASES");
		setAPMenu(model, ApplicationConstants.AP_URL_APPEALED_CASES);
		setAPForAppealedCases(model, ApplicationConstants.AP_URL_APPEALED_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_APPEALED_CASES;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_REVISION_CASES)
	public String getRevisionedCasesList(Model model) {
		logger.info("ApproverUserController.getRevisionedCasesList() : ApplicationConstants.AP_URL_REVISION_CASES");
		setAPMenu(model, ApplicationConstants.AP_URL_REVISION_CASES);
		setAPForRevisionedCases(model, ApplicationConstants.AP_URL_REVISION_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_REVISION_CASES;
	}

	@GetMapping("/" + ApplicationConstants.AP_URL_REJECTED_CASES)
	public String getRejectedCasesList(Model model) {
		logger.info("ApproverUserController.getRejectedCasesList() : ApplicationConstants.AP_URL_REJECTED_CASES");
		setAPMenu(model, ApplicationConstants.AP_URL_REJECTED_CASES);
		setAPForRejectedCases(model, ApplicationConstants.AP_URL_REJECTED_CASES);
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_URL_REJECTED_CASES;
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setAPMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}

	/**********************
	 * Convert Unread To Read Notification Start
	 ***********************/
	@PostMapping("/" + ApplicationConstants.CONVERT_UNREAD_TO_READ_NOTIFICATIONS)
	public String convertUnreadToReadNotifications(Model model, @RequestBody List<MstNotifications> mstNotifications) {
		logger.info(
				"ReviewController.convertUnreadToReadNotifications() : ApplicationConstants.CONVERT_UNREAD_TO_READ_NOTIFICATIONS");
		for (MstNotifications notification : mstNotifications) {
			notification.setIsDisplayed(true);
		}
		mstNotificationsRepository.saveAll(mstNotifications);
		return ApplicationConstants.RU + "/" + ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST;
	}

	/**********************
	 * Convert Unread To Read Notification End
	 ***********************/
	/*
	 * private void setAPApproveRejectedCases(Model model, String
	 * apUrlApproveRejectCases) {
	 * 
	 * SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	 * 
	 * List<EnforcementReviewCase> enforcementApproverList =
	 * enforcementReviewCaseRepository.verifierRecommendedCasesList(); // display
	 * cases to approver which is at state level for (EnforcementReviewCase
	 * enforceSoloToAttachFile : enforcementApproverList) { String gstin =
	 * enforceSoloToAttachFile.getId().getGSTIN(); String period =
	 * enforceSoloToAttachFile.getId().getPeriod(); Date caseReportingDate =
	 * enforceSoloToAttachFile.getId().getCaseReportingDate();
	 * enforceSoloToAttachFile.setFileName(URLEncoder.encode(
	 * enforcementReviewCaseRepository.findFileNameByGstin(gstin, period,
	 * caseReportingDate)));
	 * 
	 * List<CaseWorkflow> ruRemarksWithAppealRevision =
	 * caseWorkFlowRepository.getVerifierRemarksWithAppealRevision(gstin, period,
	 * caseReportingDate); List<CaseWorkflow> apRemarksWithAppealRevision =
	 * caseWorkFlowRepository.getApproverRemarksWithAppealRevision(gstin, period,
	 * caseReportingDate); List<String> remarkList = new ArrayList<>(); List<String>
	 * apRemarkList = new ArrayList<>(); for (CaseWorkflow str :
	 * ruRemarksWithAppealRevision) { if (str.getOtherRemarks() != null) {
	 * remarkList.add(str.getOtherRemarks() + " (" +
	 * ruDateFormat.format(str.getUpdatingDate()) + ")"); } else {
	 * remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("+
	 * ruDateFormat.format(str.getUpdatingDate()) + ")"); } }
	 * enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() >
	 * 0) ? remarkList : null);
	 * 
	 * for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) { if
	 * (apRemarks.getOtherRemarks() != null) {
	 * apRemarkList.add(apRemarks.getOtherRemarks() + " ("+
	 * ruDateFormat.format(apRemarks.getUpdatingDate()) + ")"); } else {
	 * apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() +
	 * " (" + ruDateFormat.format(apRemarks.getUpdatingDate()) + ")"); } }
	 * enforceSoloToAttachFile.setApRemarks((apRemarkList != null &&
	 * apRemarkList.size() > 0) ? apRemarkList : null);
	 * if(enforceSoloToAttachFile.getParameter() != null) {
	 * enforceSoloToAttachFile.setParameter(fieldUserController.getParameterName
	 * (enforceSoloToAttachFile.getParameter())); } else {
	 * enforceSoloToAttachFile.setParameter(""); }
	 * 
	 * } model.addAttribute("approverCaseList", enforcementApproverList);
	 * List<ApproverRemarksToRejectTheCase> approverRemarksToRejectTheCaseList =
	 * approverRemarksToRejectTheCaseRepository.findAllByOrderByIdDesc();
	 * model.addAttribute("approverRemarksToRejectTheCaseList",
	 * approverRemarksToRejectTheCaseList); }
	 */
	private void setAPMenuRemarksRejected(Model model, String activeMenu, String gstino, String reportingdate,
			String period, String apRejectedRemarks, String apRejectTextRemarks, MultipartFile fileAttachedWhileRevert)
			throws ParseException, IOException {
		logger.info("ApproverUserController.setAPMenuRemarksRejected().initiate");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstino);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info("apRejectedRemarks : " + apRejectedRemarks);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		/*******************************
		 * Appeal/Revision upload file start
		 **********************************************/
		String revertedCaseFileName = fileAttachedWhileRevert.getOriginalFilename();
		String timeStamp = fileDateFormat.format(new java.util.Date());
		String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
		byte[] appealRevisionBytes = fileAttachedWhileRevert.getBytes();
		File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
		OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
		revertedCaseOutputStream.write(appealRevisionBytes);
		revertedCaseOutputStream.close();
		/*******************************
		 * Appeal/Revision upload file end
		 **********************************************/
		Integer currentUser = userDetails.getUserId();
		Integer foUserId = caseWorkFlowRepository.findFoIdFromCaseWorkFlow(gstino);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fetchCase = dateFormat.parse(reportingdate);
		// Optional<TransferRemarks> transferRemarks =
		// transferRemarksRepository.findById(Integer.parseInt(apRejectedRemarks));
		Optional<ApproverRemarksToRejectTheCase> approverRemarksToRejectTheCase = approverRemarksToRejectTheCaseRepository
				.findById(Long.parseLong(apRejectedRemarks));
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, fetchCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction("approverRaiseQuery");
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("AP");
			cashWorkFlow.setAssignedTo("FO");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setPeriod(period);
			// cashWorkFlow.setRemarks(transferRemarks.get());
			cashWorkFlow.setSuggestedJurisdiction(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssignedToLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			// Date updateDate = java.sql.Date.valueOf(LocalDate.now());
//			cashWorkFlow.setRemarksDescription(approverRemarksToRejectTheCase.get().getName());
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssigntoUserId((enforcementReviewList.get(0).getAssignedToUserId() == NULL) ? 0
					: enforcementReviewList.get(0).getAssignedToUserId());
//			cashWorkFlow.setApproverRemarksToRejectTheCase(approverRemarksToRejectTheCase.get());
			cashWorkFlow.setApRevertedCaseFile(revertedCaseFileNameWithTimeStamp);
			if (apRejectedRemarks.equals("1")) {
				cashWorkFlow.setApproverRemarksToRejectTheCase(approverRemarksToRejectTheCase.get());
				cashWorkFlow.setOtherRemarks(apRejectTextRemarks);
			} else {
				cashWorkFlow.setApproverRemarksToRejectTheCase(approverRemarksToRejectTheCase.get());
			}
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow Start
			// Insertion in Verifier CaseFlow Start
			ApproverCaseWorkFlow approverCaseWorkFlow = new ApproverCaseWorkFlow();
			approverCaseWorkFlow.setGstin(gstino);
			approverCaseWorkFlow.setCaseReportingDate(parsedReportingDate);
			approverCaseWorkFlow.setPeriod(period);
			approverCaseWorkFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			approverCaseWorkFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			approverCaseWorkFlow.setCategory(enforcementReviewList.get(0).getCategory());
			approverCaseWorkFlow.setDemand(enforcementReviewList.get(0).getDemand());
			approverCaseWorkFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			approverCaseWorkFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			approverCaseWorkFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			approverCaseWorkFlow.setReviewMeetingComments("");
			approverCaseWorkFlow.setUpdatingDate(new Date());
			approverCaseWorkFlow.setAction("approverRaiseQuery");
			approverCaseWorkFlow.setAssignedTo("FO");
			approverCaseWorkFlow.setRecoveryStageName(enforcementReviewList.get(0).getRecoveryStage().getName());
			approverCaseWorkFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			approverCaseWorkFlow.setAmountRecovered(enforcementReviewList.get(0).getAmountRecovered());
			approverCaseWorkFlow.setAssignedFrom(enforcementReviewList.get(0).getAssignedFrom());
			approverCaseWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			approverCaseWorkFlow.setAssignedToUserId(enforcementReviewList.get(0).getAssignedToUserId());
//			approverCaseWorkFlow.setCaseId(enforcementReviewList.get(0).getCaseId());   fo is working on it,will be add later
//			approverCaseWorkFlow.setCaseStageArn(enforcementReviewList.get(0).getCaseStageArn());
			approverCaseWorkFlow.setExtensionNo(enforcementReviewList.get(0).getExtensionNo());
//			approverCaseWorkFlow.setRecoveryStageArn(enforcementReviewList.get(0).getRecoveryStageArn());
			approverCaseWorkFlow
					.setExtensionFileName(enforcementReviewList.get(0).getExtensionNoDocument().getExtensionFileName());
			approverCaseWorkFlow
					.setWorkingLocationName(enforcementReviewList.get(0).getLocationDetails().getLocationName());
			approverCaseWorkFlowRepository.save(approverCaseWorkFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ApproverUserController.setAPMenuRemarksRejected().catch()", e.getMessage());
		}
		enforcementReviewList.get(0).setAssignedFromUserId(currentUser);
		enforcementReviewList.get(0).setAssignedToUserId(foUserId);
		enforcementReviewList.get(0).setAssignedFrom("AP");
		enforcementReviewList.get(0).setAssignedTo("FO");
		enforcementReviewList.get(0).setAction("approverRaiseQuery");
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, fetchCase);
		enforcementReviewCaseAssignedUsers.setApUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("verifyerRecommended");
		Integer apUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getApUserId();
		Integer foUserrId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getFoUserId();
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		List<MstNotifications> notificationsList = mstNotificationsRepository
				.getNotificationsToUpdateNotificationPertainTo(enforcementReviewList.get(0).getId().getGSTIN(),
						enforcementReviewList.get(0).getId().getPeriod(),
						enforcementReviewList.get(0).getId().getCaseReportingDate(), assigneZeroUserIdToSpecificUser,
						"AP", returnWorkingLocation(userDetails.getUserId()));
		if (!notificationsList.isEmpty()) {
			for (MstNotifications notificationSolo : notificationsList) {
				notificationSolo.setNotificationPertainTo(apUserId);
			}
			mstNotificationsRepository.saveAll(notificationsList);
		}
		casePertainUserNotification.insertReAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "RU",
				enforcementReviewList.get(0).getLocationDetails(), "approverRaiseQuery", ruUserId, "field officer",
				"Approver");
		casePertainUserNotification.insertReAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "FO",
				enforcementReviewList.get(0).getLocationDetails(), "approverRaiseQuery", foUserrId, "you", "Approver");
		/****** Check case already assign to specific user or not End *****/
		logger.info("ApproverUserController.setAPMenuRemarksRejected().discontinue");
	}

	private void setAPAppealRevisionApproveCase(Model model, String activeMenu, String appealRevisionApproveGstin,
			String appealRevisionApproveCaseReportingDate, String appealRevisionApprovePeriod,
			String appealRevisionApproveRemarksValue, String appealRevisionApproveFilePath,
			String appealRevisionApproved) throws ParseException {
		logger.info("ApproverUserController.setAPAppealRevisionApproveCase().initiate");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + appealRevisionApproveGstin);
		logger.info("reportingdate : " + appealRevisionApproveCaseReportingDate);
		logger.info("period : " + appealRevisionApprovePeriod);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fetchSoloCase = dateFormat.parse(appealRevisionApproveCaseReportingDate);
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(appealRevisionApproveGstin, appealRevisionApprovePeriod, fetchSoloCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(appealRevisionApproveCaseReportingDate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(appealRevisionApproveGstin);
			cashWorkFlow.setAction(appealRevisionApproved);
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("AP");
			cashWorkFlow.setAssignedTo("RU");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setOtherRemarks(appealRevisionApproveRemarksValue);
			cashWorkFlow.setPeriod(appealRevisionApprovePeriod);
			cashWorkFlow.setSuggestedJurisdiction("");
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId("test");
			cashWorkFlow.setAssigntoUserId(0);
			cashWorkFlow.setRuAppealRevisionFile(appealRevisionApproveFilePath);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow Start
			// Insertion in Verifier CaseFlow Start
			ApproverCaseWorkFlow approverCaseWorkFlow = new ApproverCaseWorkFlow();
			approverCaseWorkFlow.setGstin(appealRevisionApproveGstin);
			approverCaseWorkFlow.setCaseReportingDate(parsedReportingDate);
			approverCaseWorkFlow.setPeriod(appealRevisionApprovePeriod);
			approverCaseWorkFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			approverCaseWorkFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			approverCaseWorkFlow.setCategory(enforcementReviewList.get(0).getCategory());
			approverCaseWorkFlow.setDemand(enforcementReviewList.get(0).getDemand());
			approverCaseWorkFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			approverCaseWorkFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			approverCaseWorkFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			approverCaseWorkFlow.setReviewMeetingComments("");
			approverCaseWorkFlow.setUpdatingDate(new Date());
			approverCaseWorkFlow.setAction(appealRevisionApproved);
			approverCaseWorkFlow.setAssignedTo("HQ");
			// verifierCaseFlow.setRecoveryStage(enforcementReviewList.get(0).getRecoveryStage());
			approverCaseWorkFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			approverCaseWorkFlowRepository.save(approverCaseWorkFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ApproverUserController.setAPMenuRemarksApproved().catch()", e.getMessage());
		}
		enforcementReviewList.get(0).setAssignedFrom("AP");
		enforcementReviewList.get(0).setAssignedTo("HQ");
		enforcementReviewList.get(0).setAction(appealRevisionApproved);
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(appealRevisionApproveGstin, appealRevisionApprovePeriod, fetchSoloCase);
		enforcementReviewCaseAssignedUsers.setApUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		Integer apUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getApUserId();
		Integer foUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getFoUserId();
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		casePertainUserNotification.insertClosedNotificationByApprover(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "RU",
				enforcementReviewList.get(0).getLocationDetails(), appealRevisionApproved, ruUserId);
		casePertainUserNotification.insertClosedNotificationByApprover(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "FO",
				enforcementReviewList.get(0).getLocationDetails(), appealRevisionApproved, foUserId);
		/****** Check case already assign to specific user or not End *****/
		logger.info("ApproverUserController.setAPAppealRevisionApproveCase().discontinue");
	}

	private void setAPAppealRevisionRejecedCase(Model model, String activeMenu, String appealRevisionRejectGstin,
			String appealRevisionRejectCaseReportingDate, String appealRevisionRejectPeriod,
			String appealRevisionRejectRemarksValue, String appealRevisionRejectFilePath) throws ParseException {
		logger.info("ApproverUserController.setAPAppealRevisionCase().initiate");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + appealRevisionRejectGstin);
		logger.info("reportingdate : " + appealRevisionRejectCaseReportingDate);
		logger.info("period : " + appealRevisionRejectPeriod);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fetchSoloCase = dateFormat.parse(appealRevisionRejectCaseReportingDate);
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(appealRevisionRejectGstin, appealRevisionRejectPeriod, fetchSoloCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(appealRevisionRejectCaseReportingDate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(appealRevisionRejectGstin);
			cashWorkFlow.setAction("apRejectAppealRevision");
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("AP");
			cashWorkFlow.setAssignedTo("RU");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setOtherRemarks(appealRevisionRejectRemarksValue);
			cashWorkFlow.setPeriod(appealRevisionRejectPeriod);
			cashWorkFlow.setSuggestedJurisdiction("");
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId("test");
			cashWorkFlow.setAssigntoUserId(0);
			cashWorkFlow.setRuAppealRevisionFile(appealRevisionRejectFilePath);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow Start
			// Insertion in Verifier CaseFlow Start
			ApproverCaseWorkFlow approverCaseWorkFlow = new ApproverCaseWorkFlow();
			approverCaseWorkFlow.setGstin(appealRevisionRejectGstin);
			approverCaseWorkFlow.setCaseReportingDate(parsedReportingDate);
			approverCaseWorkFlow.setPeriod(appealRevisionRejectPeriod);
			approverCaseWorkFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			approverCaseWorkFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			approverCaseWorkFlow.setCategory(enforcementReviewList.get(0).getCategory());
			approverCaseWorkFlow.setDemand(enforcementReviewList.get(0).getDemand());
			approverCaseWorkFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			approverCaseWorkFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			approverCaseWorkFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			approverCaseWorkFlow.setReviewMeetingComments("");
			approverCaseWorkFlow.setUpdatingDate(new Date());
			approverCaseWorkFlow.setAction("apRejectAppealRevision");
			approverCaseWorkFlow.setAssignedTo("RU");
			// verifierCaseFlow.setRecoveryStage(enforcementReviewList.get(0).getRecoveryStage());
			approverCaseWorkFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			approverCaseWorkFlowRepository.save(approverCaseWorkFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ApproverUserController.setAPMenuRemarksApproved().catch()", e.getMessage());
		}
		List<String> tempPreviousStatus = Arrays.asList(enforcementReviewList.get(0).getAction());
		enforcementReviewList.get(0).setAssignedFrom("AP");
		enforcementReviewList.get(0).setAssignedTo("RU");
		enforcementReviewList.get(0).setAction("apRejectAppealRevision");
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(appealRevisionRejectGstin, appealRevisionRejectPeriod, fetchSoloCase);
		enforcementReviewCaseAssignedUsers.setApUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		Integer apUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getApUserId();
		List<MstNotifications> notificationsList = mstNotificationsRepository
				.getNotificationsToUpdateNotificationPertainTo(enforcementReviewList.get(0).getId().getGSTIN(),
						enforcementReviewList.get(0).getId().getPeriod(),
						enforcementReviewList.get(0).getId().getCaseReportingDate(), tempPreviousStatus, "AP",
						returnWorkingLocation(userDetails.getUserId()));
		if (!notificationsList.isEmpty()) {
			for (MstNotifications notificationSolo : notificationsList) {
				notificationSolo.setNotificationPertainTo(apUserId);
			}
			mstNotificationsRepository.saveAll(notificationsList);
		}
		casePertainUserNotification.insertReAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "RU",
				enforcementReviewList.get(0).getLocationDetails(), "apRejectAppealRevision", ruUserId, "you",
				"Approver");
		/****** Check case already assign to specific user or not End *****/
		logger.info("ApproverUserController.setAPAppealRevisionCase().discontinue");
	}

	private void setAPMenuRemarksApproved(Model model, String activeMenu, String gstino, String reportingdate,
			String period, String apApproveRemarks) throws ParseException {
		logger.info("ApproverUserController.setAPMenuRemarksApproved().initiate");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstino);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		apApproveRemarks = "1";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fetchSoloCase = dateFormat.parse(reportingdate);
		Optional<TransferRemarks> transferRemarks = transferRemarksRepository
				.findById(Integer.parseInt(apApproveRemarks));
		Optional<ApproverRemarksToApproveTheCase> approverRemarksToApproveTheCasea = approverRemarksToApproveTheCaseRepository
				.findById(Long.parseLong(apApproveRemarks));
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, fetchSoloCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction("closed");
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("AP");
			cashWorkFlow.setAssignedTo("HQ");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setOtherRemarks("");
			cashWorkFlow.setPeriod(period);
			cashWorkFlow.setRemarks(transferRemarks.get());
			cashWorkFlow.setSuggestedJurisdiction("");
			// Date updateDate = java.sql.Date.valueOf(LocalDate.now());
//			cashWorkFlow.setRemarksDescription(approverRemarksToApproveTheCasea.get().getName());
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssigntoUserId(0);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow Start
			// Insertion in Verifier CaseFlow Start
			ApproverCaseWorkFlow approverCaseWorkFlow = new ApproverCaseWorkFlow();
			approverCaseWorkFlow.setGstin(gstino);
			approverCaseWorkFlow.setCaseReportingDate(parsedReportingDate);
			approverCaseWorkFlow.setPeriod(period);
			approverCaseWorkFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			approverCaseWorkFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			approverCaseWorkFlow.setCategory(enforcementReviewList.get(0).getCategory());
			approverCaseWorkFlow.setDemand(enforcementReviewList.get(0).getDemand());
			approverCaseWorkFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			approverCaseWorkFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			approverCaseWorkFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			approverCaseWorkFlow.setReviewMeetingComments("");
			approverCaseWorkFlow.setUpdatingDate(new Date());
			approverCaseWorkFlow.setAction("closed");
			approverCaseWorkFlow.setAssignedTo("HQ");
			// verifierCaseFlow.setRecoveryStage(enforcementReviewList.get(0).getRecoveryStage());
			approverCaseWorkFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			approverCaseWorkFlowRepository.save(approverCaseWorkFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ApproverUserController.setAPMenuRemarksApproved().catch()", e.getMessage());
		}
		enforcementReviewList.get(0).setAssignedFrom("AP");
		enforcementReviewList.get(0).setAssignedTo("HQ");
		enforcementReviewList.get(0).setAction("closed");
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, fetchSoloCase);
		enforcementReviewCaseAssignedUsers.setApUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of approver userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("verifyerRecommended");
		Integer apUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getApUserId();
		Integer foUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getFoUserId();
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		Integer hqUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getHqUserId();
		List<MstNotifications> notificationsList = mstNotificationsRepository
				.getNotificationsToUpdateNotificationPertainTo(enforcementReviewList.get(0).getId().getGSTIN(),
						enforcementReviewList.get(0).getId().getPeriod(),
						enforcementReviewList.get(0).getId().getCaseReportingDate(), assigneZeroUserIdToSpecificUser,
						"AP", returnWorkingLocation(userDetails.getUserId()));
		if (!notificationsList.isEmpty()) {
			for (MstNotifications notificationSolo : notificationsList) {
				notificationSolo.setNotificationPertainTo(apUserId);
			}
			mstNotificationsRepository.saveAll(notificationsList);
		}
		casePertainUserNotification.insertClosedNotificationByApprover(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "RU",
				enforcementReviewList.get(0).getLocationDetails(), "closed", ruUserId);
		casePertainUserNotification.insertClosedNotificationByApprover(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "FO",
				enforcementReviewList.get(0).getLocationDetails(), "closed", foUserId);
		casePertainUserNotification.insertClosedNotificationByApprover(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "HQ",
				enforcementReviewList.get(0).getLocationDetails(), "closed", hqUserId);
		/****** Check case already assign to specific user or not End *****/
		logger.info("ApproverUserController.setAPMenuRemarksApproved().discontinue");
	}

	private void setAPAppealRevisionCases(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPAppealRevisionCases().initiate");
		UserDetails userDetails = new UserDetails();
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "AP");
				model.addAttribute("loginedUserNotificationListCount", loginedUserNotificationList.size());
				model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
				/*********** Get Notifications End *************/
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		Integer userId = returnCurrentUserId();
		// List<String> workingLocations = returnWorkingLocation(userId);
		List<EnforcementReviewCase> enforcementAppealRevisionCasesList = enforcementReviewCaseRepository
				.findAppealRevisionCasesList(userId);
		for (EnforcementReviewCase enforceSoloToAttachFile : enforcementAppealRevisionCasesList) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			String caseAction = enforceSoloToAttachFile.getAction();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			List<CaseWorkflow> appealRevisionInfo = caseWorkFlowRepository.findAppealRevisionCaseFile(gstin, period,
					caseReportingDate, caseAction);
			enforceSoloToAttachFile.setAppealRevisionFilePath(appealRevisionInfo.get(0).getRuAppealRevisionFile());
			/*
			 * enforceSoloToAttachFile.setAppealRevisionRemarkByRu(appealRevisionInfo.get(0)
			 * .getOtherRemarks());
			 */
			List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
					.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
			List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
					.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
			List<String> remarkList = new ArrayList<>();
			List<String> apRemarkList = new ArrayList<>();
			for (CaseWorkflow str : ruRemarksWithAppealRevision) {
				if (str.getOtherRemarks() != null) {
					remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
				} else {
					remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
							+ ruDateFormat.format(str.getUpdatingDate()) + ")");
				}
			}
			enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
			for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
				if (apRemarks.getOtherRemarks() != null) {
					apRemarkList.add(apRemarks.getOtherRemarks() + " ("
							+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
				} else {
					apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
							+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
				}
			}
			enforceSoloToAttachFile
					.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
			if (enforceSoloToAttachFile.getParameter() != null) {
				enforceSoloToAttachFile
						.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
			} else {
				enforceSoloToAttachFile.setParameter("");
			}
		}
		model.addAttribute("enforcementAppealRevisionCasesList", enforcementAppealRevisionCasesList);
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		logger.info("ApproverUserController.setAPAppealRevisionCases().discontinue");
	}

	private void setAPMenu(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPMenu().initiate");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			/*********** Get Notifications Start *************/
			List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
					.getNotificationPertainToUser(object.get(), "AP");
			List<MstNotifications> unReadNotificationList = casePertainUserNotification
					.getUnReadNotificationPertainToUser(object.get(), "AP");
			model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
			model.addAttribute("unReadNotificationList", unReadNotificationList);
			model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
			model.addAttribute("convertUnreadToReadNotifications", "/ap/convert_unread_to_read_notifications");
			/*********** Get Notifications End *************/
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				/****************************
				 * get user locations role wise start
				 **************************/
				List<String> userRoleMapWithLocations = cutomUtility.roleMapWithLocations(userRoleMappings,
						userDetails);
				model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
				/****************************
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
				model.addAttribute("commonUserDetails", "/ap/user_details");
				model.addAttribute("changeUserPassword", "/gu/change_password");
				model.addAttribute("homePageLink", "/ap/dashboard");
				/**************************
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
				List<String> roleNameList = uniqueRolesList.stream().map(mapping -> mapping.getUserRole().getRoleName())
						.collect(Collectors.toList());
				String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));
				String LocationsName = returnLocationsName(userRoleMappings);
				userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
				userProfileDetails.setAssignedWorkingLocations(LocationsName);
				model.addAttribute("userProfileDetails", userProfileDetails);
				/**************************
				 * to display user generic details end
				 ****************************/
			}
		}
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.AP_URL));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "AP");
		logger.info("ApproverUserController.setAPMenu().discontinue");
	}

	/****************************
	 * Revisioned cases Reffered by approver start
	 *************************************/
	private void setAPForRevisionedCases(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPForRevisionedCases().initiate");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		Integer userId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(userId);
//		List<EnforcementReviewCase> approvedCaseListByApprover = enforcementReviewCaseRepository.approvedCasesListByApprover(workingLocations,userId);
		List<EnforcementReviewCase> revisionedCaseListByApprover = enforcementReviewCaseRepository
				.revisionedCasesListByApprover();
		Map<Integer, String> allParameterMap = parametersModuleWiseRepository.findAll().stream()
				.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
		for (EnforcementReviewCase enforceSoloToAttachFile : revisionedCaseListByApprover) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			String caseAction = enforceSoloToAttachFile.getAction();
			List<CaseWorkflow> appealRevisionInfo = caseWorkFlowRepository.findAppealRevisionCaseFile(gstin, period,
					caseReportingDate, caseAction);
			enforceSoloToAttachFile.setAppealRevisionFilePath(appealRevisionInfo.get(0).getRuAppealRevisionFile());
			if (enforceSoloToAttachFile.getParameter() != null
					&& enforceSoloToAttachFile.getParameter().trim().length() > 0) {
				enforceSoloToAttachFile.setParameter(String.join(", ",
						Arrays.stream(enforceSoloToAttachFile.getParameter().split(",")).map(String::trim)
								.map(Integer::parseInt).map(allParameterMap::get).collect(Collectors.toList())));
			}
		}
		model.addAttribute("revisionedCaseListByApprover", revisionedCaseListByApprover);
		logger.info("ApproverUserController.setAPForRevisionedCases().discontinue");
	}

	/****************************
	 * Revisioned cases Reffered by approver end
	 *************************************/
	/****************************
	 * Appealed cases Reffered by approver start
	 *************************************/
	private void setAPForAppealedCases(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPForAppealedCases().initiate");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		Integer userId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(userId);
//		List<EnforcementReviewCase> approvedCaseListByApprover = enforcementReviewCaseRepository.approvedCasesListByApprover(workingLocations,userId);
		List<EnforcementReviewCase> appealedCaseListByApprover = enforcementReviewCaseRepository
				.appealedCasesListByApprover();
		Map<Integer, String> allParameterMap = parametersModuleWiseRepository.findAll().stream()
				.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
		for (EnforcementReviewCase enforceSoloToAttachFile : appealedCaseListByApprover) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			String caseAction = enforceSoloToAttachFile.getAction();
			List<CaseWorkflow> appealRevisionInfo = caseWorkFlowRepository.findAppealRevisionCaseFile(gstin, period,
					caseReportingDate, caseAction);
			enforceSoloToAttachFile.setAppealRevisionFilePath(appealRevisionInfo.get(0).getRuAppealRevisionFile());
			if (enforceSoloToAttachFile.getParameter() != null
					&& enforceSoloToAttachFile.getParameter().trim().length() > 0) {
				enforceSoloToAttachFile.setParameter(String.join(", ",
						Arrays.stream(enforceSoloToAttachFile.getParameter().split(",")).map(String::trim)
								.map(Integer::parseInt).map(allParameterMap::get).collect(Collectors.toList())));
			}
		}
		model.addAttribute("appealedCaseListByApprover", appealedCaseListByApprover);
		logger.info("ApproverUserController.setAPForAppealedCases().discontinue");
	}

	/****************************
	 * Appealed cases Reffered by approver end
	 *************************************/
	private void setAPForApprovedCases(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPForApprovedCases().initiate");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		Integer userId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(userId);
//		List<EnforcementReviewCase> approvedCaseListByApprover = enforcementReviewCaseRepository.approvedCasesListByApprover(workingLocations,userId);
		List<EnforcementReviewCase> approvedCaseListByApprover = enforcementReviewCaseRepository
				.approvedCasesListByApprover();
		for (EnforcementReviewCase enforceSoloToAttachFile : approvedCaseListByApprover) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			enforceSoloToAttachFile
					.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
			if (enforceSoloToAttachFile.getParameter() != null) {
				enforceSoloToAttachFile
						.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
			} else {
				enforceSoloToAttachFile.setParameter("");
			}
		}
		model.addAttribute("approvedCaseListByApprover", approvedCaseListByApprover);
		logger.info("ApproverUserController.setAPForApprovedCases().discontinue");
	}

	private void setAPForRejectedCases(Model model, String activeMenu) {
		logger.info("ApproverUserController.setAPForRejectedCases().initiate");
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
			}
		}
		Integer userId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(userId);
//		List<EnforcementReviewCase> rejectedCaseListByApprover = enforcementReviewCaseRepository.rejectedCasesListByApprover(workingLocations,userId);
		List<EnforcementReviewCase> rejectedCaseListByApprover = enforcementReviewCaseRepository
				.rejectedCasesListByApprover();
		for (EnforcementReviewCase enforceSoloToAttachFile : rejectedCaseListByApprover) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			enforceSoloToAttachFile
					.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
			if (enforceSoloToAttachFile.getParameter() != null) {
				enforceSoloToAttachFile
						.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
			} else {
				enforceSoloToAttachFile.setParameter("");
			}
		}
		model.addAttribute("rejectedCaseListByApprover", rejectedCaseListByApprover);
		logger.info("ApproverUserController.setAPForRejectedCases().discontinue");
	}

	private Integer returnCurrentUserId() {
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
			}
		}
		return userDetails.getUserId();
	}

	private List<String> returnWorkingLocation(Integer currentUserId) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId,
				userRoleRepository.findByroleCode("AP").get().getId());
//		List<UserRoleMapping> circleIds = userRoleMapList.stream()
//				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))   // commented as per requirement verifier can not be assigned to at circle level.
//				.collect(Collectors.toList());
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
		if (stateId != null && stateId.size() > 0) {
			workingLocationsIds.addAll(enforcementReviewCaseRepository.finAllLocationsByStateid());
			return workingLocationsIds;
		} else {
			List<UserRoleMapping> zoneIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
			/*
			 * if(!circleIds.isEmpty()) { for(UserRoleMapping circleIdsSolo : circleIds) {
			 * workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId()); //
			 * commented as per requirement verifier can not be assigned to at circle level.
			 * } }
			 */
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
		return workingLocationsIds;
	}

	/* download file strat */
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("ReviewController.downloadFile().initiate" + fileName);
			//////////////////
			// String filesDirectory = pathToSupportingFileRepository.getSourcePathById(1L);
			String filesDirectory = fileUploadLocation;
			///////////////////
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("ReviewController.downloadFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("ReviewController.downloadFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	/* download file end */

	/**************** return locations name start ******************/
	@GetMapping("/download_approve_reject_excel")
	public ResponseEntity<byte[]> downloadExcel() throws IOException {
		try {
			// Generate the Excel file
			byte[] excelFile = cutomUtility
					.createApproveRejectExcelFile(enforcementReviewCaseRepository.verifierRecommendedCasesList());
			// Return the file as a downloadable response
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelFile);
		} catch (IOException e) {
			// Log error and return a proper error response
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to generate the Excel file".getBytes());
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
	@GetMapping("/" + ApplicationConstants.AP_REVIEW_CATEGORY_CASE)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setAPMenu(model, ApplicationConstants.AP_REVIEW_CATEGORY_CASE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("AP").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("ABCD");
		model.addAttribute("categories", categoryListRepository
				.findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(locationList, exceptActionList));
//		model.addAttribute("categories",
//				categoryListRepository.findAllCategoryForAssessmentCasesByAPUserId(userDetails.getUserId()));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = mstParametersRepository
						.findAllAssessmentParameterByAssessmentCategoryAndLocationListAndExceptActionList(
								category.get().getName(), locationList, exceptActionList);
//				List<MstParametersModuleWise> parameters = mstParametersRepository
//						.findAllAssessmentParameterByAssessmentCategoryAndAPUserId(category.get().getName(),
//								userDetails.getUserId());
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& mstParametersRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
					model.addAttribute("categoryTotals", enforcementReviewCaseRepository
							.getAllCaseCountByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(
									category.get().getName(), parameterId.toString(), locationList, exceptActionList));
//					model.addAttribute("categoryTotals",
//							enforcementReviewCaseRepository.getAllCaseCountByCategoryAnd1stParameterIdAndApUserId(
//									category.get().getName(), parameterId.toString(), userDetails.getUserId()));
				} else {
					model.addAttribute("categoryTotals",
							enforcementReviewCaseRepository.getAllCaseCountByCategoryAndLocationListAndExceptActionList(
									category.get().getName(), locationList, exceptActionList));
//					model.addAttribute("categoryTotals", enforcementReviewCaseRepository
//							.getAllCaseCountByCategoryAndApUserId(category.get().getName(), userDetails.getUserId()));
				}
			}
		}
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_REVIEW_CATEGORY_CASE + "_new";
	}

	@GetMapping("/" + ApplicationConstants.AP_VIEW_LIST_OF_CASE)
	public String reviewCasesList(Model model, @RequestParam(required = false) String category, String parameterId) {
		List<String[]> caseList = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("AP").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("ABCD");
		model.addAttribute("categories", categoryListRepository
				.findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(locationList, exceptActionList));
		setAPMenu(model, ApplicationConstants.AP_REVIEW_CATEGORY_CASE);
		Category categoryObject = categoryListRepository.findByName(category);
		if (categoryObject != null) {
			model.addAttribute("categoryId", categoryObject.getId());
		}
		if (category != null && category.length() > 0 && parameterId != null && parameterId.length() > 0) {
			caseList = enforcementReviewCaseRepository
					.findByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(category, parameterId,
							locationList, exceptActionList);
		} else if (category != null && category.length() > 0) {
			caseList = enforcementReviewCaseRepository.findReviewCasesListByCategoryAndLocationListAndExceptActionList(
					category, locationList, exceptActionList);
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_VIEW_LIST_OF_CASE + "_new";
	}
//	@GetMapping("/review_category_case")
//	public String reviewSummaryList(Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//			return "redirect:/logout";
//		}
//		setAPMenu(model, ApplicationConstants.AP_REVIEW_CATEGORY_CASE);
//		try {
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//			List<String> locationId = FieldUserController
//					.getAllMappedLocationsFromUserRoleMapping(userDetails.getUserRoleMappings());
//			logger.info("location details : " + locationId.toString());
//			List<CategoryTotal> categoryList = new ArrayList<CategoryTotal>();
//			if (locationId.contains("HP")) {
//				categoryList = FOReviewSummeryList.getStateCategoryDetails();
//			} else {
//				categoryList = FOReviewSummeryList.getCategoryDetails(locationId);
//			}
//			model.addAttribute("categoryTotals", categoryList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_REVIEW_CATEGORY_CASE;
//	}
//
//	@GetMapping("/view_list_of_case")
//	public String viewlistOfCase(Model model, @RequestParam(required = false) String category) {
//		try {
//			setAPMenu(model, ApplicationConstants.FO_REVIEW_CATEGORY_CASE);
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			List<EnforcementReviewCase> enforcementReviewList = new ArrayList<EnforcementReviewCase>();
//			List<EnforcementReviewCase> enforcementReviewCase = new ArrayList<EnforcementReviewCase>();
//			;
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//			List<String> locationId = FieldUserController
//					.getAllMappedLocationsFromUserRoleMapping(userDetails.getUserRoleMappings());
//			if (locationId.contains("HP")) {
//				enforcementReviewCase = enforcementReviewCaseRepository.enforcementReviewList(category);
//			} else {
//				enforcementReviewCase = enforcementReviewCaseRepository.getListBasedLocationAndCategory(locationId,
//						category);
//			}
//			for (EnforcementReviewCase e : enforcementReviewCase) {
//				e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
//				e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
//						e.getId().getPeriod(), e.getId().getCaseReportingDate()));
//				enforcementReviewList.add(e);
//			}
//			model.addAttribute("caseList", enforcementReviewList);
//			model.addAttribute("category", category);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ApplicationConstants.AP_URL + "/" + ApplicationConstants.AP_VIEW_LIST_OF_CASE;
//	}
}
