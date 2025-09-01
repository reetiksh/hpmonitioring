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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CaseidUpdation;
import com.hp.gstreviewfeedbackapp.model.CaseidUpdationModel;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.TransferRemarks;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.VerifierCaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.VerifierRaiseQueryRemarks;
import com.hp.gstreviewfeedbackapp.model.VerifierRemarks;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseIdRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseIdUpdationRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.repository.VerifierCaseWorkFlowRepository;
import com.hp.gstreviewfeedbackapp.repository.VerifierRaiseQueryRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.VerifierRemarksRepository;
import com.hp.gstreviewfeedbackapp.service.FOReviewSummeryList;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.RU)
public class ReviewUserController {
	private static final Logger logger = LoggerFactory.getLogger(ReviewUserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private VerifierRemarksRepository verifierRemarksRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkFlowRepository;
	@Autowired
	private TransferRemarksRepository transferRemarksRepository;
	@Autowired
	private VerifierCaseWorkFlowRepository verifierCaseFlowRepository;
	@Autowired
	VerifierRaiseQueryRemarksRepository verifierRaiseQueryRemarksRepository;
	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	UserDetailsRepository userDetailsRepository;
	@Autowired
	EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private CaseIdRemarksRepository caseIdRemarksRepository;
	@Autowired
	private CaseIdUpdationRepository caseIdUpdationRepository;
	@Autowired
	private HQUserUploadDataRepository hqUserUploadDataRepository;
	@Autowired
	private MstNotificationsRepository mstNotificationsRepository;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private FieldUserController fieldUserController;
	@Autowired
	private DashboardRepository dashboardRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Value("${action.verifyerRecommended}")
	private String greetingMessage;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${action.caseid.update.verifyerApprove}")
	private String ruApprove;
	@Value("${action.caseid.update.verifyerReject}")
	private String ruRejected;
	@Value("${maximunIndicativeTaxValueForDeemApproval}")
	private Long maximunIndicativeTaxValueForDeemApproval;
	@Autowired
	private FOReviewSummeryList FOReviewSummeryList;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(Model model, @RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "view", required = false) String view,
			@RequestParam(required = false) Integer parameter) {
		logger.info("ReviewController.dashboard() : ApplicationConstants.DASHBOARD");
		setRUMenu(model, ApplicationConstants.DASHBOARD);
		DecimalFormat formatter = new DecimalFormat("##,##,##,##,000",
				new DecimalFormatSymbols(new Locale("en", "IN")));
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			System.out.println("foDashBoardWorkingLocations " + foDashBoardWorkingLocations.toString());
			// Get locations for FO roles
//			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
//					userRoleRepository.findByroleCode("RU").get());
//		JSONArray totalIndicativeTaxValueCategoryWise = fieldUserServiceImpl.getTotalIndicativeTaxValueCategoryWise(locationMap);
			List<List<String>> totalIndicativeTaxValueCategoryWise = fieldUserServiceImpl
					.getTotalIndicativeTaxValueCategoryWiseList(foDashBoardWorkingLocations);
			if (totalIndicativeTaxValueCategoryWise != null && totalIndicativeTaxValueCategoryWise.size() > 0) {
				model.addAttribute("indicativeTaxValue", totalIndicativeTaxValueCategoryWise);
			}
//		JSONArray totalDemandCategoryWise = fieldUserServiceImpl.getTotalDemandCategoryWise(locationMap);
			List<List<String>> totalDemandCategoryWise = fieldUserServiceImpl
					.getTotalDemandCategoryWiseList(foDashBoardWorkingLocations);
			if (totalDemandCategoryWise != null && totalDemandCategoryWise.size() > 0) {
				model.addAttribute("demandValue", totalDemandCategoryWise);
			}
//		JSONArray totalRecoveryCategoryWise = fieldUserServiceImpl.getTotalRecoveryCategoryWise(locationMap);
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
			List<DashboardDistrictCircle> circleList = null;
			List<DashboardDistrictCircle> zoneList = null;
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 3);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			// Boolean hasRoleDistrict = fieldUserController.isRoleDistrict(locationId);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.RU + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?category=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() + "&view=" + dashboard.getView();
	}

	@GetMapping("/" + ApplicationConstants.RU_VERIFY_CASES_STATUS)
	public String getVerifyCasesList(Model model, @RequestParam(required = false) String categoryName) {
		logger.info("ReviewController.getVerifyCasesList() : ApplicationConstants.RU_VERIFY_CASES_STATUS");
		setRUMenu(model, ApplicationConstants.RU_VERIFY_CASES_STATUS);
		setVerifyCasesStatusList(model, ApplicationConstants.RU_VERIFY_CASES_STATUS, categoryName);
		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_VERIFY_CASES_STATUS;
	}

	@GetMapping("/" + ApplicationConstants.APPEAL_REVISION_REJECTED_CASE)
	public String getAppealedRevisionRejected(Model model) {
		logger.info(
				"ReviewController.getAppealedRevisionRejected() : ApplicationConstants.APPEAL_REVISION_REJECTED_CASE");
		setRUMenu(model, ApplicationConstants.APPEAL_REVISION_REJECTED_CASE);
		setAppealRevisionRejectedCasesList(model, ApplicationConstants.APPEAL_REVISION_REJECTED_CASE);
		return ApplicationConstants.RU + "/" + ApplicationConstants.APPEAL_REVISION_REJECTED_CASE;
	}

	@GetMapping("/" + ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE)
	public String getRecomendedForUserList(Model model) {
		logger.info("ReviewController.getRecomendedForUserList() : ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE");
		setRUMenu(model, ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE);
		setRURecommendedForClosureMenu(model, ApplicationConstants.RU_VERIFY_CASES_STATUS);
		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE;
	}

	@PostMapping("/" + ApplicationConstants.APPEAL_REVISION_CASE)
	public String getAppealRevisionCase(@RequestParam("appealRevisionFile") MultipartFile appealRevisionFile,
			@RequestParam("appRegGstiNo") String gstinno, @RequestParam("appRegReportingdate") String reportingdate,
			@RequestParam("appRegPeriod") String period,
			@RequestParam("appealRevisionRemarks") String appealRevisionRemarks,
			@RequestParam("appealRevision") String appealRevision, Model model) throws ParseException, IOException {
		logger.info("ReviewController.getAppealRevisionCase() : ApplicationConstants.APPEAL_REVISION_CASE");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstinno);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUAppealRevisionCase(model, ApplicationConstants.APPEAL_REVISION_CASE, gstinno, reportingdate, period,
				appealRevisionRemarks, appealRevision, appealRevisionFile);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/" + ApplicationConstants.RU_VERIFY_CASES_STATUS;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.RU_ONCE_AGAIN_APPEAL_REVISION_REJECTED_CASE)
	public String ruAgainAppealRevisionRejectedCase(
			@RequestParam("appealRevisionRejectedFile") MultipartFile appealRevisionRejectedFile,
			@RequestParam("appRevRejGstiNo") String appRevRejGstiNo,
			@RequestParam("appRevRejReportingdate") String appRevRejReportingdate,
			@RequestParam("appRevRejPeriod") String appRevRejPeriod,
			@RequestParam("appealRevisionRejectedRemarks") String appealRevisionRejectedRemarks,
			@RequestParam("appealRevisionRejected") String appealRevisionRejected, Model model)
			throws ParseException, IOException {
		logger.info(
				"ReviewController.ruAgainAppealRevisionRejectedCase() : ApplicationConstants.RU_ONCE_AGAIN_APPEAL_REVISION_REJECTED_CASE");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + appRevRejGstiNo);
		logger.info("reportingdate : " + appRevRejReportingdate);
		logger.info("period : " + appRevRejPeriod);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUAppealRevisionOnceAgainCase(model, ApplicationConstants.APPEAL_REVISION_REJECTED_CASE, appRevRejGstiNo,
				appRevRejReportingdate, appRevRejPeriod, appealRevisionRejectedRemarks, appealRevisionRejected,
				appealRevisionRejectedFile);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/"
				+ ApplicationConstants.APPEAL_REVISION_REJECTED_CASE;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE_WITH_REMARKS)
	public String getRecomendedForUserList(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("remarks") String remarks, Model model) throws ParseException {
		logger.info(
				"ReviewController.getRecomendedForUserList() : ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE_WITH_REMARKS");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstinno);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUMenuRemarks(model, ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE, gstinno, reportingdate, period,
				remarks);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/" + ApplicationConstants.RU_VERIFY_CASES_STATUS;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.APPEAL_REVISION_RECOMMENDED_CASE)
	public String getAppealRevisionRecommendedCase(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("remarks") String remarks, Model model) throws ParseException {
		logger.info(
				"ReviewController.getRecomendedForUserList() : ApplicationConstants.APPEAL_REVISION_RECOMMENDED_CASE");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstinno);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUMenuRemarks(model, ApplicationConstants.APPEAL_REVISION_RECOMMENDED_CASE, gstinno, reportingdate, period,
				remarks);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/"
				+ ApplicationConstants.APPEAL_REVISION_REJECTED_CASE;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE_WITH_RAISE_QUERY_REMARKS)
	public String getRecomendedWithRaiseQueryForUserList(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("remarksRaiseQuery") String remarksRaiseQuery,
			@RequestParam("otherRemarksTextValue") String otherRemarksTextValue, Model model) throws ParseException {
		logger.info(
				"ReviewController.getRecomendedWithRaiseQueryForUserList() : ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE_WITH_RAISE_QUERY_REMARKS");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstinno);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info("remarksRaiseQuery : " + remarksRaiseQuery);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUMenuRaisqeQueryRemarks(model, ApplicationConstants.RU_RECOMMENDED_FOR_CLOSURE, gstinno, reportingdate,
				period, remarksRaiseQuery, otherRemarksTextValue);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/" + ApplicationConstants.RU_VERIFY_CASES_STATUS;
		return url;
	}

	@PostMapping("/" + ApplicationConstants.APPEAL_REVISION_RAISE_QUERY_CASE)
	public String getAppealRevisionRaiseQueryCase(@RequestParam("gstinno") String gstinno,
			@RequestParam("reportingdate") String reportingdate, @RequestParam("period") String period,
			@RequestParam("remarksRaiseQuery") String remarksRaiseQuery,
			@RequestParam("otherRemarksTextValue") String otherRemarksTextValue, Model model) throws ParseException {
		logger.info(
				"ReviewController.getAppealRevisionRaiseQueryCase() : ApplicationConstants.APPEAL_REVISION_RAISE_QUERY_CASE");
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		logger.info("gstinno : " + gstinno);
		logger.info("reportingdate : " + reportingdate);
		logger.info("period : " + period);
		logger.info("remarksRaiseQuery : " + remarksRaiseQuery);
		logger.info(
				"-------------------------------------------------------------------------------------------------------");
		setRUMenuRaisqeQueryRemarks(model, ApplicationConstants.APPEAL_REVISION_RAISE_QUERY_CASE, gstinno,
				reportingdate, period, remarksRaiseQuery, otherRemarksTextValue);
		String url = "redirect:" + "/" + ApplicationConstants.RU + "/"
				+ ApplicationConstants.APPEAL_REVISION_REJECTED_CASE;
		return url;
	}

	@GetMapping("/" + ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST)
	public String getRaisedQueryList(Model model) {
		logger.info("ReviewController.getRaisedQueryList() : ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST");
		setRUMenu(model, ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST);
		setQueryRaisedListByVerifier(model, ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST);
		return ApplicationConstants.RU + "/" + ApplicationConstants.GET_RU_VERIFY_RAISED_QUERY_LIST;
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

	/***********************
	 * Appeal Revision Case Start
	 ********************************/
	/*********************
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

	/*********************
	 * Convert Unread To Read Notification End
	 ***********************/
	private void setRUAppealRevisionCase(Model model, String activeMenu, String gstino, String reportingdate,
			String period, String appealRejectionRemarks, String appealRevision, MultipartFile appealOrRevisionFile)
			throws ParseException, IOException {
		logger.info("ReviewController.setRUAppealRevisionCase().initiate");
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
		String appealRevisionFileName = appealOrRevisionFile.getOriginalFilename();
		String timeStamp = fileDateFormat.format(new java.util.Date());
		String appealRevisionFileNameWithTimeStamp = timeStamp + "_" + appealRevisionFileName;
		byte[] appealRevisionBytes = appealRevisionFileName.getBytes();
		File appealRevisionFileByteStream = new File(fileUploadLocation + appealRevisionFileNameWithTimeStamp);
		OutputStream appealRevisionOutputStream = new FileOutputStream(appealRevisionFileByteStream);
		appealRevisionOutputStream.write(appealRevisionBytes);
		/*******************************
		 * Appeal/Revision upload file end
		 **********************************************/
		Integer currentUser = userDetails.getUserId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date pickTheCase = dateFormat.parse(reportingdate);
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction(appealRevision);
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("RU");
			cashWorkFlow.setAssignedTo("AP");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setAssignedToLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setPeriod(period);
			cashWorkFlow.setSuggestedJurisdiction("");
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssigntoUserId(enforcementReviewList.get(0).getAssignedToUserId());
			cashWorkFlow.setOtherRemarks(appealRejectionRemarks);
			cashWorkFlow.setRuAppealRevisionFile(appealRevisionFileNameWithTimeStamp);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow End
			// Insertion in Verifier CaseFlow Start
			VerifierCaseWorkflow verifierCaseFlow = new VerifierCaseWorkflow();
			verifierCaseFlow.setGstin(gstino);
			verifierCaseFlow.setCaseReportingDate(parsedReportingDate);
			verifierCaseFlow.setPeriod(period);
			verifierCaseFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			verifierCaseFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			verifierCaseFlow.setCategory(enforcementReviewList.get(0).getCategory());
			verifierCaseFlow.setDemand(enforcementReviewList.get(0).getDemand());
			verifierCaseFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			verifierCaseFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			verifierCaseFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			verifierCaseFlow.setReviewMeetingComments("");
			verifierCaseFlow.setUpdatingDate(new Date());
			verifierCaseFlow.setAction(appealRevision);
			verifierCaseFlow.setAssignedTo("AP");
			verifierCaseFlow.setRecoveryStageName(enforcementReviewList.get(0).getRecoveryStage().getName());
			verifierCaseFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			verifierCaseFlow.setAmountRecovered(enforcementReviewList.get(0).getAmountRecovered());
			verifierCaseFlow.setAssignedFrom("RU");
			verifierCaseFlow.setAssignedFromUserId(userDetails.getUserId());
			verifierCaseFlow.setAssignedToUserId(enforcementReviewList.get(0).getAssignedToUserId());
			verifierCaseFlow.setExtensionNo(enforcementReviewList.get(0).getExtensionNo());
			verifierCaseFlow
					.setExtensionFileName(enforcementReviewList.get(0).getExtensionNoDocument().getExtensionFileName());
			verifierCaseFlow
					.setWorkingLocationName(enforcementReviewList.get(0).getLocationDetails().getLocationName());
			verifierCaseFlowRepository.save(verifierCaseFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ReviewController.setRUAppealRevisionCase().catch()", e.getMessage());
		}
		Integer previousLogin = enforcementReviewList.get(0).getAssignedFromUserId();
		enforcementReviewList.get(0).setAssignedToUserId(previousLogin);
		enforcementReviewList.get(0).setAssignedFromUserId(currentUser);
		enforcementReviewList.get(0).setAssignedFrom("RU");
		enforcementReviewList.get(0).setAssignedTo("AP");
		enforcementReviewList.get(0).setAction(appealRevision);
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		enforcementReviewCaseAssignedUsers.setRuUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not Start *****/
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		casePertainUserNotification.insertAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "AP",
				enforcementReviewList.get(0).getLocationDetails(), appealRevision, 0);
		/****** Check case already assign to specific user or not End *****/
		logger.info("ReviewController.setRUAppealRevisionCase().discontinue");
	}

	/***********************
	 * Appeal Revision Case End
	 ********************************/
	/***********************
	 * Appeal Revision Once Again Case Start
	 ********************************/
	private void setRUAppealRevisionOnceAgainCase(Model model, String activeMenu, String gstino, String reportingdate,
			String period, String appealRejectionRemarks, String appealRevision, MultipartFile appealOrRevisionFile)
			throws ParseException, IOException {
		logger.info("ReviewController.setRUAppealRevisionOnceAgainCase().initiate");
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
		String appealRevisionFileName = appealOrRevisionFile.getOriginalFilename();
		String timeStamp = fileDateFormat.format(new java.util.Date());
		String appealRevisionFileNameWithTimeStamp = timeStamp + "_" + appealRevisionFileName;
		byte[] appealRevisionBytes = appealRevisionFileName.getBytes();
		File appealRevisionFileByteStream = new File(fileUploadLocation + appealRevisionFileNameWithTimeStamp);
		OutputStream appealRevisionOutputStream = new FileOutputStream(appealRevisionFileByteStream);
		appealRevisionOutputStream.write(appealRevisionBytes);
		/*******************************
		 * Appeal/Revision upload file end
		 **********************************************/
		Integer currentUser = userDetails.getUserId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date pickTheCase = dateFormat.parse(reportingdate);
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction(appealRevision);
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("RU");
			cashWorkFlow.setAssignedTo("AP");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setAssignedToLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setPeriod(period);
			cashWorkFlow.setSuggestedJurisdiction("");
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssigntoUserId(enforcementReviewList.get(0).getAssignedToUserId());
			cashWorkFlow.setOtherRemarks(appealRejectionRemarks);
			cashWorkFlow.setRuAppealRevisionFile(appealRevisionFileNameWithTimeStamp);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow End
			// Insertion in Verifier CaseFlow Start
			VerifierCaseWorkflow verifierCaseFlow = new VerifierCaseWorkflow();
			verifierCaseFlow.setGstin(gstino);
			verifierCaseFlow.setCaseReportingDate(parsedReportingDate);
			verifierCaseFlow.setPeriod(period);
			verifierCaseFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			verifierCaseFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			verifierCaseFlow.setCategory(enforcementReviewList.get(0).getCategory());
			verifierCaseFlow.setDemand(enforcementReviewList.get(0).getDemand());
			verifierCaseFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			verifierCaseFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			verifierCaseFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			verifierCaseFlow.setReviewMeetingComments("");
			verifierCaseFlow.setUpdatingDate(new Date());
			verifierCaseFlow.setAction(appealRevision);
			verifierCaseFlow.setAssignedTo("AP");
			verifierCaseFlow.setRecoveryStageName(enforcementReviewList.get(0).getRecoveryStage().getName());
			verifierCaseFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			verifierCaseFlow.setAmountRecovered(enforcementReviewList.get(0).getAmountRecovered());
			verifierCaseFlow.setAssignedFrom("RU");
			verifierCaseFlow.setAssignedFromUserId(userDetails.getUserId());
			verifierCaseFlow.setAssignedToUserId(enforcementReviewList.get(0).getAssignedToUserId());
			verifierCaseFlow.setExtensionNo(enforcementReviewList.get(0).getExtensionNo());
			verifierCaseFlow
					.setExtensionFileName(enforcementReviewList.get(0).getExtensionNoDocument().getExtensionFileName());
			verifierCaseFlow
					.setWorkingLocationName(enforcementReviewList.get(0).getLocationDetails().getLocationName());
			verifierCaseFlowRepository.save(verifierCaseFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ReviewController.setRUAppealRevisionCase().catch()", e.getMessage());
		}
		Integer previousLogin = enforcementReviewList.get(0).getAssignedFromUserId();
		enforcementReviewList.get(0).setAssignedToUserId(previousLogin);
		enforcementReviewList.get(0).setAssignedFromUserId(currentUser);
		enforcementReviewList.get(0).setAssignedFrom("RU");
		enforcementReviewList.get(0).setAssignedTo("AP");
		enforcementReviewList.get(0).setAction(appealRevision);
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		enforcementReviewCaseAssignedUsers.setRuUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not Start *****/
		Integer apUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getApUserId();
		casePertainUserNotification.insertAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "AP",
				enforcementReviewList.get(0).getLocationDetails(), appealRevision, apUserId);
		/****** Check case already assign to specific user or not End *****/
		logger.info("ReviewController.setRUAppealRevisionOnceAgainCase().discontinue");
	}

	/***********************
	 * Appeal Revision Once Again Case End
	 ********************************/
	private void setRUMenuRaisqeQueryRemarks(Model model, String activeMenu, String gstino, String reportingdate,
			String period, String remarksRaiseQuery, String otherRemarksTextValue) throws ParseException {
		logger.info("ReviewController.setRUMenuRaisqeQueryRemarks().initiate");
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
		Integer currentUser = userDetails.getUserId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date pickTheCase = dateFormat.parse(reportingdate);
		// Optional<TransferRemarks> transferRemarks =
		// transferRemarksRepository.findById(Integer.parseInt(remarksRaiseQuery));
		Optional<VerifierRaiseQueryRemarks> verifierRaiseQueryRemarks = verifierRaiseQueryRemarksRepository
				.findById(Long.parseLong(remarksRaiseQuery));
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction("verifyerRaiseQuery");
			cashWorkFlow.setAssignedFromLocationId("");
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssignedFrom("RU");
			cashWorkFlow.setAssignedTo("FO");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setAssignedToLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setPeriod(period);
			// cashWorkFlow.setRemarks(transferRemarks.get());
			cashWorkFlow.setSuggestedJurisdiction("");
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssigntoUserId(enforcementReviewList.get(0).getAssignedToUserId());
			if (remarksRaiseQuery.equals("1")) {
				cashWorkFlow.setVerifierRaiseQueryRemarks(verifierRaiseQueryRemarks.get());
				cashWorkFlow.setOtherRemarks(otherRemarksTextValue);
			} else {
				cashWorkFlow.setVerifierRaiseQueryRemarks(verifierRaiseQueryRemarks.get());
			}
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow End
			// Insertion in Verifier CaseFlow Start
			VerifierCaseWorkflow verifierCaseFlow = new VerifierCaseWorkflow();
			verifierCaseFlow.setGstin(gstino);
			verifierCaseFlow.setCaseReportingDate(parsedReportingDate);
			verifierCaseFlow.setPeriod(period);
			verifierCaseFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			verifierCaseFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			verifierCaseFlow.setCategory(enforcementReviewList.get(0).getCategory());
			verifierCaseFlow.setDemand(enforcementReviewList.get(0).getDemand());
			verifierCaseFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			verifierCaseFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			verifierCaseFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			verifierCaseFlow.setReviewMeetingComments("");
			verifierCaseFlow.setUpdatingDate(new Date());
			verifierCaseFlow.setAction("verifyerRaiseQuery");
			verifierCaseFlow.setAssignedTo("FO");
			verifierCaseFlow.setRecoveryStageName(enforcementReviewList.get(0).getRecoveryStage().getName());
			verifierCaseFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			verifierCaseFlow.setAmountRecovered(enforcementReviewList.get(0).getAmountRecovered());
			verifierCaseFlow.setAssignedFrom("RU");
			verifierCaseFlow.setAssignedFromUserId(userDetails.getUserId());
			verifierCaseFlow.setAssignedToUserId(enforcementReviewList.get(0).getAssignedToUserId());
			// verifierCaseFlow.setCaseId(enforcementReviewList.get(0).getCaseId()); fo is
			// working on it,will be add later
			// verifierCaseFlow.setCaseStageArn(enforcementReviewList.get(0).getCaseStageArn());
			verifierCaseFlow.setExtensionNo(enforcementReviewList.get(0).getExtensionNo());
			// verifierCaseFlow.setRecoveryStageArn(enforcementReviewList.get(0).getRecoveryStageArn());
			verifierCaseFlow
					.setExtensionFileName(enforcementReviewList.get(0).getExtensionNoDocument().getExtensionFileName());
			verifierCaseFlow
					.setWorkingLocationName(enforcementReviewList.get(0).getLocationDetails().getLocationName());
			verifierCaseFlowRepository.save(verifierCaseFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ReviewController.setRUMenuRaisqeQueryRemarks().catch()", e.getMessage());
		}
		Integer previousLogin = enforcementReviewList.get(0).getAssignedFromUserId();
		enforcementReviewList.get(0).setAssignedToUserId(previousLogin);
		enforcementReviewList.get(0).setAssignedFromUserId(currentUser);
		enforcementReviewList.get(0).setAssignedFrom("RU");
		enforcementReviewList.get(0).setAssignedTo("FO");
		enforcementReviewList.get(0).setAction("verifyerRaiseQuery");
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		enforcementReviewCaseAssignedUsers.setRuUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("close");
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		Integer foUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getFoUserId();
		List<MstNotifications> notificationsList = mstNotificationsRepository
				.getNotificationsToUpdateNotificationPertainTo(enforcementReviewList.get(0).getId().getGSTIN(),
						enforcementReviewList.get(0).getId().getPeriod(),
						enforcementReviewList.get(0).getId().getCaseReportingDate(), assigneZeroUserIdToSpecificUser,
						"RU", returnWorkingLocation(userDetails.getUserId()));
		if (!notificationsList.isEmpty()) {
			for (MstNotifications notificationSolo : notificationsList) {
				notificationSolo.setNotificationPertainTo(ruUserId);
			}
			mstNotificationsRepository.saveAll(notificationsList);
		}
		casePertainUserNotification.insertReAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "FO",
				enforcementReviewList.get(0).getLocationDetails(), "verifyerRaiseQuery", foUserId, "you", "Verifier");
		/****** Check case already assign to specific user or not End *****/
		logger.info("ReviewController.setRUMenuRaisqeQueryRemarks().discontinue");
	}

	private void setRUMenuRemarks(Model model, String activeMenu, String gstino, String reportingdate, String period,
			String remarks) throws ParseException {
		logger.info("ReviewController.setRUMenuRemarks().initiate");
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
		Integer currentUser = userDetails.getUserId();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date pickTheCase = dateFormat.parse(reportingdate);
		Optional<TransferRemarks> transferRemarks = transferRemarksRepository.findById(Integer.parseInt(remarks));
		Optional<VerifierRemarks> verifierRemarks = verifierRemarksRepository.findById(Long.parseLong(remarks));
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		String action = "verifyerRecommended";
		MstParametersModuleWise oldCase = (mstParametersRepository.findById(16).isPresent()
				? mstParametersRepository.findById(16).get()
				: null);
		if (enforcementReviewList.get(0).getIndicativeTaxValue() <= maximunIndicativeTaxValueForDeemApproval
				|| (oldCase != null && enforcementReviewList.get(0).getCategory().equals(oldCase.getParamName()))) {
			action = "deemApproved";
		}
		Date parsedReportingDate;
		try {
			// Insertion in CaseFlow Start
			parsedReportingDate = dateFormat.parse(reportingdate);
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(gstino);
			cashWorkFlow.setAction(action);
			cashWorkFlow.setAssignedToLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssignedFrom("RU");
			cashWorkFlow.setAssignedTo("AP");
			cashWorkFlow.setCaseReportingDate(parsedReportingDate);
			cashWorkFlow.setOtherRemarks("");
			cashWorkFlow.setPeriod(period);
			cashWorkFlow.setRemarks(transferRemarks.get());
			cashWorkFlow.setSuggestedJurisdiction("");
			// Date updateDate = java.sql.Date.valueOf(LocalDate.now());
			cashWorkFlow.setUpdatingDate(new Date());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewList.get(0).getLocationDetails().getLocationId());
			cashWorkFlow.setAssignedFromUserId(userDetails.getUserId());
			cashWorkFlow.setAssigntoUserId(0);
			caseWorkFlowRepository.save(cashWorkFlow);
			// Insertion in CaseFlow Start
			// Insertion in Verifier CaseFlow Start
			VerifierCaseWorkflow verifierCaseFlow = new VerifierCaseWorkflow();
			verifierCaseFlow.setGstin(gstino);
			verifierCaseFlow.setCaseReportingDate(parsedReportingDate);
			verifierCaseFlow.setPeriod(period);
			verifierCaseFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			verifierCaseFlow.setCaseStageName(enforcementReviewList.get(0).getCaseStage().getName());
			verifierCaseFlow.setCategory(enforcementReviewList.get(0).getCategory());
			verifierCaseFlow.setDemand(enforcementReviewList.get(0).getDemand());
			verifierCaseFlow.setRecoveryStageName(enforcementReviewList.get(0).getRecoveryStage().getName());
			verifierCaseFlow.setActionStatusName(enforcementReviewList.get(0).getActionStatus().getName());
			verifierCaseFlow.setIndicativeTaxValue(enforcementReviewList.get(0).getIndicativeTaxValue());
			verifierCaseFlow.setRecoveryAgainstDemand(enforcementReviewList.get(0).getRecoveryAgainstDemand());
			verifierCaseFlow.setRecoveryByDrc3(enforcementReviewList.get(0).getRecoveryByDRC3());
			verifierCaseFlow.setAmountRecovered(enforcementReviewList.get(0).getAmountRecovered());
			verifierCaseFlow.setAssignedFrom("RU");
			verifierCaseFlow.setReviewMeetingComments("");
			verifierCaseFlow.setUpdatingDate(new Date());
			verifierCaseFlow.setAction(action);
			verifierCaseFlow.setAssignedTo("AP");
			// verifierCaseFlow.setRecoveryStage(enforcementReviewList.get(0).getRecoveryStage());
			verifierCaseFlow.setTaxpayerName(enforcementReviewList.get(0).getTaxpayerName());
			verifierCaseFlowRepository.save(verifierCaseFlow);
			// Insertion in Verifier CaseFlow End
		} catch (Exception e) {
			logger.error("ReviewController.setRUMenuRemarks().catch()", e.getMessage());
		}
		System.out.println("greetingMessage : " + greetingMessage);
		enforcementReviewList.get(0).setAssignedFrom("RU");
		enforcementReviewList.get(0).setAssignedTo("AP");
		enforcementReviewList.get(0).setAction(action);
		enforcementReviewList.get(0).setAssignedFromUserId(currentUser);
		enforcementReviewCaseRepository.save(enforcementReviewList.get(0));
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * start
		 ***********/
		EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
				.findByGstinPeriodRepotingDate(gstino, period, pickTheCase);
		enforcementReviewCaseAssignedUsers.setRuUserId(returnCurrentUserId());
		enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
		/**********
		 * Insertion of verifier userId in enforcement_review_case_assigned_users table
		 * end
		 ***********/
		/****** Check case already assign to specific user or not start *****/
		Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
				enforcementReviewList.get(0).getId().getGSTIN(), enforcementReviewList.get(0).getId().getPeriod(),
				enforcementReviewList.get(0).getId().getCaseReportingDate()).getRuUserId();
		List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("upload", "close");
		List<MstNotifications> notificationsList = mstNotificationsRepository
				.getNotificationsToUpdateNotificationPertainTo(enforcementReviewList.get(0).getId().getGSTIN(),
						enforcementReviewList.get(0).getId().getPeriod(),
						enforcementReviewList.get(0).getId().getCaseReportingDate(), assigneZeroUserIdToSpecificUser,
						"RU", returnWorkingLocation(userDetails.getUserId()));
		if (!notificationsList.isEmpty()) {
			for (MstNotifications notificationSolo : notificationsList) {
				notificationSolo.setNotificationPertainTo(ruUserId);
			}
			mstNotificationsRepository.saveAll(notificationsList);
		}
		casePertainUserNotification.insertAssignNotification(enforcementReviewList.get(0).getId().getGSTIN(),
				enforcementReviewList.get(0).getId().getCaseReportingDate(),
				enforcementReviewList.get(0).getId().getPeriod(), "AP",
				enforcementReviewList.get(0).getLocationDetails(), action, 0);
		/****** Check case already assign to specific user or not End *****/
		logger.info("ReviewController.setRUMenuRemarks().discontinue");
	}

	private void setVerifyCasesStatusList(Model model, String activeMenu, String categoryName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = new UserDetails();
		List<EnforcementReviewCase> verifierCaseList = new ArrayList<>();
		List<String> zoneCodeIds = new ArrayList<>();
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			userDetails = object.get();
		}
		Integer loginUserId = userDetails.getUserId();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(loginUserId,
				userRoleRepository.findByroleCode("RU").get().getId());
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
		if (!stateId.isEmpty()) {
			verifierCaseList = enforcementReviewCaseRepository.verifierStateLevelCasesList();
		} else {
			List<UserRoleMapping> zoneIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
			List<UserRoleMapping> circleIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))
					.collect(Collectors.toList());
			if (!circleIds.isEmpty()) {
				for (UserRoleMapping circleIdsSolo : circleIds) {
					zoneCodeIds.add(circleIdsSolo.getCircleDetails().getCircleId()); // commented as per requirement
																						// verifier can not be assigned
																						// to at circle level.
				}
			}
			/************ Add Zone Hierarchy Start ******************/
			if (!zoneIds.isEmpty()) {
				List<String> onlyZoneIdsList = new ArrayList<String>();
				List<String> onlyDistrictIdsList = new ArrayList<String>();
				for (UserRoleMapping zoneIdsSolo : zoneIds) {
					zoneCodeIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
					onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect //
																					// circle later
				}
				List<String> allCirclesListUnderDistricts = enforcementReviewCaseRepository
						.findAllCirclesByZoneIds(onlyZoneIdsList);
				for (String circleSolo : allCirclesListUnderDistricts) {
					zoneCodeIds.add(circleSolo); // add Circles
				}
			}
			/************ Add Zone Hierarchy End ******************/
			if (categoryName == null || categoryName.equals("NA")) {
				verifierCaseList = enforcementReviewCaseRepository.findAllByWorkingLocation(zoneCodeIds,
						userDetails.getUserId());
			} else {
				model.addAttribute("categoryName", categoryName);
				verifierCaseList = enforcementReviewCaseRepository.findAllByWorkingLocationAndCategory(zoneCodeIds,
						userDetails.getUserId(), categoryName);
			}
			model.addAttribute("categoryNameList",
					enforcementReviewCaseRepository.findAllDistinctCategoryForVerifierByWorkingLocationsAndUserId(
							zoneCodeIds, userDetails.getUserId()));
		}
		for (EnforcementReviewCase enforceSoloToAttachFile : verifierCaseList) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			enforceSoloToAttachFile
					.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
			// Set all Verifier previous remarks
//			List<String[]> remarks = enforcementReviewCaseRepository.getVerifierRemarks(gstin,period,caseReportingDate);
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
		model.addAttribute("verifierCaseList", verifierCaseList);
		List<VerifierRaiseQueryRemarks> raiseQueryRemarks = verifierRaiseQueryRemarksRepository.findRemarksOrderById();
		raiseQueryRemarks = raiseQueryRemarks.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
				.collect(Collectors.toList());
		model.addAttribute("raiseQueryRemarks", raiseQueryRemarks);
	}

	private void setAppealRevisionRejectedCasesList(Model model, String activeMenu) {
		logger.info("ReviewController.setAppealRevisionRejectedCasesList().initiate");
		List<String> zoneCodeIds = new ArrayList<>();
		UserDetails userDetails = new UserDetails();
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		List<EnforcementReviewCase> verifierCaseList = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			/*********** Get Notifications Start *************/
			List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
					.getNotificationPertainToUser(object.get(), "RU");
			model.addAttribute("loginedUserNotificationListCount", loginedUserNotificationList.size());
			model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
			/*********** Get Notifications End *************/
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
		Integer loginUserId = userDetails.getUserId();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(loginUserId,
				userRoleRepository.findByroleCode("RU").get().getId());
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
		if (!stateId.isEmpty()) {
			verifierCaseList = enforcementReviewCaseRepository.verifierStateLevelAppealRejectionCasesList();
		} else {
			List<UserRoleMapping> zoneIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
			/************ Add Zone Hierarchy Start ******************/
			if (!zoneIds.isEmpty()) {
				List<String> onlyZoneIdsList = new ArrayList<String>();
				List<String> onlyDistrictIdsList = new ArrayList<String>();
				for (UserRoleMapping zoneIdsSolo : zoneIds) {
					zoneCodeIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
					onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect
																					// circle later
				}
				List<String> allCirclesListUnderDistricts = enforcementReviewCaseRepository
						.findAllCirclesByZoneIds(onlyZoneIdsList);
				for (String circleSolo : allCirclesListUnderDistricts) {
					zoneCodeIds.add(circleSolo); // add Circles
				}
			}
			/************ Add Zone Hierarchy End ******************/
			verifierCaseList = enforcementReviewCaseRepository.findAllAppealRevisionRejectedCasesList(zoneCodeIds,
					userDetails.getUserId());
		}
		for (EnforcementReviewCase enforceSoloToAttachFile : verifierCaseList) {
			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
			String period = enforceSoloToAttachFile.getId().getPeriod();
			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
			String caseAction = enforceSoloToAttachFile.getAction();
			List<CaseWorkflow> appealRevisionInfo = caseWorkFlowRepository.findAppealRevisionApproverRemarks(gstin,
					period, caseReportingDate, caseAction);
			enforceSoloToAttachFile.setAppealRevisionFilePath(appealRevisionInfo.get(0).getRuAppealRevisionFile());
			enforceSoloToAttachFile.setAppealRevisionRemarkByAp(appealRevisionInfo.get(0).getOtherRemarks());
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
		}
		model.addAttribute("verifierCaseList", verifierCaseList);
		List<VerifierRemarks> verifierRemakrs = verifierRemarksRepository.findAll();
		model.addAttribute("verifierRemarksModelList", verifierRemakrs);
		List<VerifierRaiseQueryRemarks> raiseQueryRemarks = verifierRaiseQueryRemarksRepository.findRemarksOrderById();
		raiseQueryRemarks = raiseQueryRemarks.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
				.collect(Collectors.toList());
		model.addAttribute("raiseQueryRemarks", raiseQueryRemarks);
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.RU));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "RU");
		logger.info("ReviewController.setAppealRevisionRejectedCasesList().discontinue");
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setRUMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}

	private void setRUMenu(Model model, String activeMenu) {
		logger.info("ReviewController.setRUMenu().initiate");
		// List<String> zoneCodeIds = new ArrayList<>();
		UserDetails userDetails = new UserDetails();
		// SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		// List<EnforcementReviewCase> verifierCaseList = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "RU");
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "RU");
				model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
				model.addAttribute("unReadNotificationList", unReadNotificationList);
				model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
				model.addAttribute("convertUnreadToReadNotifications", "/ru/convert_unread_to_read_notifications");
				/*********** Get Notifications End *************/
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
				model.addAttribute("homePageLink", "/ru/dashboard");
				/**************************
				 * to display user generic details start
				 ***************************/
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
				 ***************************/
			}
		}
		// Integer loginUserId = userDetails.getUserId();
		// List<UserRoleMapping> userRoleMapList =
		// userRoleMappingRepository.findAllRegionsMapWithUsers(loginUserId,userRoleRepository.findByroleCode("RU").get().getId());
//		List<UserRoleMapping> circleIds = userRoleMapList.stream()
//				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))   // commented as per requirement verifier can not be assigned to at circle level.
//				.collect(Collectors.toList());
		// List<UserRoleMapping> stateId = userRoleMapList.stream().filter(mapping ->
		// !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
//		if (!stateId.isEmpty()) {
//			verifierCaseList = enforcementReviewCaseRepository.verifierStateLevelCasesList();
//		} else {
//			List<UserRoleMapping> zoneIds = userRoleMapList.stream()
//					.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
//
////			if(!circleIds.isEmpty()) {
////				for(UserRoleMapping circleIdsSolo : circleIds) {
////					zoneCodeIds.add(circleIdsSolo.getCircleDetails().getCircleId());	// commented as per requirement verifier can not be assigned to at circle level.
////				}
////			}
//
//			/************ Add Zone Hierarchy Start ******************/
//			if (!zoneIds.isEmpty()) {
//				List<String> onlyZoneIdsList = new ArrayList<String>();
//				List<String> onlyDistrictIdsList = new ArrayList<String>();
//				for (UserRoleMapping zoneIdsSolo : zoneIds) {
//					zoneCodeIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
//					onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect
//																					// circle later
//				}
//
//				List<String> allCirclesListUnderDistricts = enforcementReviewCaseRepository
//						.findAllCirclesByZoneIds(onlyZoneIdsList);
//				for (String circleSolo : allCirclesListUnderDistricts) {
//					zoneCodeIds.add(circleSolo); // add Circles
//				}
//			}
//			/************ Add Zone Hierarchy End ******************/
//
//			verifierCaseList = enforcementReviewCaseRepository.findAllByWorkingLocation(zoneCodeIds,userDetails.getUserId());
//		}
//		for (EnforcementReviewCase enforceSoloToAttachFile : verifierCaseList) {
//			String gstin = enforceSoloToAttachFile.getId().getGSTIN();
//			String period = enforceSoloToAttachFile.getId().getPeriod();
//			Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
//			enforceSoloToAttachFile
//					.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
//			// Set all Verifier previous remarks
////			List<String[]> remarks = enforcementReviewCaseRepository.getVerifierRemarks(gstin,period,caseReportingDate);
//			List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
//					.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
//			List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
//					.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
//			List<String> remarkList = new ArrayList<>();
//			List<String> apRemarkList = new ArrayList<>();
//			for (CaseWorkflow str : ruRemarksWithAppealRevision) {
//				if (str.getOtherRemarks() != null) {
//					remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
//				} else {
//					remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
//							+ ruDateFormat.format(str.getUpdatingDate()) + ")");
//				}
//			}
//			enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
//
//			for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
//				if (apRemarks.getOtherRemarks() != null) {
//					apRemarkList.add(apRemarks.getOtherRemarks() + " ("
//							+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
//				} else {
//					apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
//							+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
//				}
//			}
//			enforceSoloToAttachFile
//					.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
//			
//			if(enforceSoloToAttachFile.getParameter() != null) {
//				enforceSoloToAttachFile.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
//			}
//			else {
//				enforceSoloToAttachFile.setParameter("");
//			}
//		}
		// model.addAttribute("verifierCaseList", verifierCaseList);
//		List<VerifierRemarks> verifierRemakrs = verifierRemarksRepository.findAll();
//		model.addAttribute("verifierRemarksModelList", verifierRemakrs);
//		List<VerifierRaiseQueryRemarks> raiseQueryRemarks = verifierRaiseQueryRemarksRepository.findRemarksOrderById();
//		raiseQueryRemarks = raiseQueryRemarks.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
//				.collect(Collectors.toList());
//		model.addAttribute("raiseQueryRemarks", raiseQueryRemarks);
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.RU));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "RU");
		model.addAttribute("commonUserDetails", "/ru/user_details");
		model.addAttribute("changeUserPassword", "/gu/change_password");
		logger.info("ReviewController.setRUMenu().discontinue");
	}

	private void setRURecommendedForClosureMenu(Model model, String activeMenu) {
		logger.info("ReviewController.setRURecommendedForClosureMenu().initiate");
		List<EnforcementReviewCase> recommendedeByVerifierForClosure = new ArrayList<>();
		Integer currentUserId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(currentUserId);
		if (workingLocations.get(0).equals("HP")) {
			recommendedeByVerifierForClosure = enforcementReviewCaseRepository.verifierRecommendedCasesList();
			for (EnforcementReviewCase enforceSoloToAttachFile : recommendedeByVerifierForClosure) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(
						enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			model.addAttribute("recommendedeByVerifierForClosure", recommendedeByVerifierForClosure);
		} else {
			recommendedeByVerifierForClosure = enforcementReviewCaseRepository
					.recommendeForClosureByVerifier(workingLocations, currentUserId);
			for (EnforcementReviewCase enforceSoloToAttachFile : recommendedeByVerifierForClosure) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(
						enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			model.addAttribute("recommendedeByVerifierForClosure", recommendedeByVerifierForClosure);
		}
		logger.info("ReviewController.setRURecommendedForClosureMenu().discontinue");
	}

	private void setQueryRaisedListByVerifier(Model model, String activeMenu) {
		logger.info("ReviewController.setQueryRaisedListByVerifier().initiate");
		List<EnforcementReviewCase> raisedQueryListByVerifier = new ArrayList<>();
		Integer currentUserId = returnCurrentUserId();
		List<String> workingLocations = returnWorkingLocation(currentUserId);
		if (workingLocations.get(0).equals("HP")) {
			raisedQueryListByVerifier = enforcementReviewCaseRepository.verifierRaiseQueryCasesList();
			for (EnforcementReviewCase enforceSoloToAttachFile : raisedQueryListByVerifier) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(
						enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			model.addAttribute("raisedQueryListByVerifier", raisedQueryListByVerifier);
		} else {
			raisedQueryListByVerifier = enforcementReviewCaseRepository.raisedQueryByVerifier(workingLocations,
					currentUserId);
			for (EnforcementReviewCase enforceSoloToAttachFile : raisedQueryListByVerifier) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(
						enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate));
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			model.addAttribute("raisedQueryListByVerifier", raisedQueryListByVerifier);
			logger.info("ReviewController.setQueryRaisedListByVerifier().discontinue");
		}
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
				userRoleRepository.findByroleCode("RU").get().getId());
		workingLocationsIds = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(userRoleMapList);
		return workingLocationsIds;
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
	@GetMapping("/approve_reject_caseid")
	public String approveRejectCaseId(Model model) {
		List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
		try {
			setRUMenu(model, ApplicationConstants.RU_APPROVE_REJECT_CASES_ID);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> locationList = returnWorkingLocation(userDetails.getUserId());
			System.out.println("locationList : " + locationList);
			List<EnforcementReviewCase> list = enforcementReviewCaseRepository.getAllCaseIdList(locationList);
			System.out.println(list.toString());
			for (EnforcementReviewCase obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String fileName = null;
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				Date date = obj.getId().getCaseReportingDate();
				String actionStatusName = obj.getActionStatus().getName();
				String caseId = obj.getCaseId();
				String caseStageName = obj.getCaseStage().getName();
				String caseStageArn = obj.getCaseStageArn();
				Long amount = obj.getDemand();
				String recoveryStageName = obj.getRecoveryStage().getName();
				String recoveryStageArn = obj.getRecoveryStageArn();
				Long recoveryByDrc3 = obj.getRecoveryByDRC3();
				Long recoveryAgainstDemand = obj.getRecoveryAgainstDemand();
				try {
					CaseidUpdation caseidUpdation = caseIdUpdationRepository.findCaseIdRequestDetails(
							obj.getId().getGSTIN(), obj.getId().getPeriod(), obj.getId().getCaseReportingDate());
					fileName = caseidUpdation.getFilePath();
				} catch (Exception e) {
					e.printStackTrace();
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setDate(date);
				object.setActionStatusName(actionStatusName);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageArn);
				object.setCaseStageName(caseStageName);
				object.setDemand(amount);
				object.setRecoveryStageName(recoveryStageName);
				object.setRecoveryStageArnStr(recoveryStageArn);
				object.setRecoveryByDRC3(recoveryByDrc3);
				object.setRecoveryAgainstDemand(recoveryAgainstDemand);
				object.setUploadedFileName(fileName);
				object.setParameter(fieldUserController.getParameterName(obj.getParameter()));
				if (obj.getParameter() != null) {
					object.setParameter(fieldUserController.getParameterName(obj.getParameter()));
				} else {
					object.setParameter(fieldUserController.getParameterName(""));
				}
				listofCases.add(object);
			}
		} catch (Exception e) {
		}
		model.addAttribute("listofCases", listofCases);
		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_APPROVE_REJECT_CASES_ID;
	}

	@GetMapping("/view_case_id/id")
	public String viewCaseId(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String remarks = null;
			System.out.println("gst " + gst);
			System.out.println("date " + date);
			System.out.println("period " + period);
			Date caseReportingDate = dateFormat.parse(date);
			CaseidUpdation caseidUpdation = caseIdUpdationRepository.findCaseIdRequestDetails(gst, period,
					caseReportingDate);
			String oldCaseId = caseidUpdation.getOldCaseid();
			String suggestedCaseId = caseidUpdation.getCaseid();
			Integer remarkId = caseidUpdation.getRemarks();
			if (remarkId == 1) {
				remarks = caseidUpdation.getOtherRemarks();
			} else {
				remarks = caseIdRemarksRepository.findById(remarkId).get().getName();
			}
			model.addAttribute("oldCaseId", oldCaseId);
			model.addAttribute("suggestedCaseId", suggestedCaseId);
			model.addAttribute("remarks", remarks);
			model.addAttribute("gst", gst);
			model.addAttribute("date", date);
			model.addAttribute("period", period);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.RU + "/view_case_id";
	}

	@PostMapping("/updateCaseId")
	public String approveRejectCaseIdUpdation(Model model,
			@ModelAttribute("caseUpdateForm") CaseidUpdationModel caseidUpdationModel,
			RedirectAttributes redirectAttrs) {
		try {
			// String tempStatusForNotifications = new String();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			String tempNotificationAction = new String();
			String tempCaseStatus = new String();
			String message = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			System.out.println(caseidUpdationModel.getStatus());
			String status = caseidUpdationModel.getStatus();
			String oldCaseId = caseidUpdationModel.getCaseid();
			String suggestedCaseId = caseidUpdationModel.getOtherRemarks();
			String remarks = caseidUpdationModel.getApprovalRemarks();
			System.out.println(caseidUpdationModel.getGstnocaseid());
			System.out.println(caseidUpdationModel.getDatecaseid());
			System.out.println(caseidUpdationModel.getPeriodcaseid());
			String gstn = caseidUpdationModel.getGstnocaseid();
			String period = caseidUpdationModel.getPeriodcaseid();
			String date = caseidUpdationModel.getDatecaseid();
			Date caseReportingDate = dateFormat.parse(date);
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gstn);
			compositeKey.setCaseReportingDate(caseReportingDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			if (status.equals("approve")) {
				enforcementReviewCase.setCaseidUpdationStatus(ruApprove);
				enforcementReviewCase.setCaseId(suggestedCaseId);
				enforcementReviewCaseRepository.save(enforcementReviewCase);
				CaseidUpdation caseidUpdation = new CaseidUpdation();
				caseidUpdation.setAssignedTo("FO");
				caseidUpdation.setAssignedFrom("RU");
				caseidUpdation.setGSTIN(gstn);
				caseidUpdation.setCaseReportingDate(caseReportingDate);
				caseidUpdation.setPeriod(period);
				caseidUpdation.setAssignedFromUserId(userId);
				caseidUpdation.setAssigntoUserId(0);
				caseidUpdation.setJurisdiction(enforcementReviewCase.getLocationDetails().getLocationId());
				caseidUpdation.setUpdatingDate(new Date());
				caseidUpdation.setOldCaseid(oldCaseId);
				caseidUpdation.setCaseid(suggestedCaseId);
				caseidUpdation.setStatus(ruApprove);
				caseidUpdation.setApprovalRemarks(remarks);
				caseIdUpdationRepository.save(caseidUpdation);
				message = "Case ID updation request approved successfully";
				tempNotificationAction = "ruApproveCaseIdUpdation";
				tempCaseStatus = "Approved";
			} else if (status.equals("reject")) {
				enforcementReviewCase.setCaseidUpdationStatus(ruRejected);
				enforcementReviewCaseRepository.save(enforcementReviewCase);
				CaseidUpdation caseidUpdation = new CaseidUpdation();
				caseidUpdation.setAssignedTo("FO");
				caseidUpdation.setAssignedFrom("RU");
				caseidUpdation.setGSTIN(gstn);
				caseidUpdation.setCaseReportingDate(caseReportingDate);
				caseidUpdation.setPeriod(period);
				caseidUpdation.setAssignedFromUserId(userId);
				caseidUpdation.setAssigntoUserId(0);
				caseidUpdation.setJurisdiction(enforcementReviewCase.getLocationDetails().getLocationId());
				caseidUpdation.setUpdatingDate(new Date());
				caseidUpdation.setOldCaseid(oldCaseId);
				caseidUpdation.setCaseid(suggestedCaseId);
				caseidUpdation.setStatus(ruRejected);
				caseidUpdation.setApprovalRemarks(remarks);
				caseIdUpdationRepository.save(caseidUpdation);
				message = "Case ID updation request rejected successfully";
				tempNotificationAction = "ruRejectCaseIdUpdation";
				tempCaseStatus = "Rejected";
			}
			redirectAttrs.addFlashAttribute("message", message);
			/************
			 * Update RU UserId in EnforcementReviewCaseAssignedUsers table start
			 **************/
			EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
					.findByGstinPeriodRepotingDate(enforcementReviewCase.getId().getGSTIN(),
							enforcementReviewCase.getId().getPeriod(),
							enforcementReviewCase.getId().getCaseReportingDate());
			enforcementReviewCaseAssignedUsers.setRuUserId(returnCurrentUserId());
			enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
			/************
			 * Update RU UserId in EnforcementReviewCaseAssignedUsers table end
			 **************/
			/****** Check case already assign to specific user or not start *****/
			List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("caseIdUpdateRequestByFo");
			Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getPeriod(),
					enforcementReviewCase.getId().getCaseReportingDate()).getRuUserId();
			Integer foUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getPeriod(),
					enforcementReviewCase.getId().getCaseReportingDate()).getFoUserId();
			List<MstNotifications> notificationsList = mstNotificationsRepository
					.getNotificationsToUpdateNotificationPertainTo(enforcementReviewCase.getId().getGSTIN(),
							enforcementReviewCase.getId().getPeriod(),
							enforcementReviewCase.getId().getCaseReportingDate(), assigneZeroUserIdToSpecificUser, "RU",
							returnWorkingLocation(userDetails.getUserId()));
			if (!notificationsList.isEmpty()) {
				for (MstNotifications notificationSolo : notificationsList) {
					notificationSolo.setNotificationPertainTo(ruUserId);
				}
				mstNotificationsRepository.saveAll(notificationsList);
			}
			casePertainUserNotification.insertReAssignNotificationForApprovedOrRejectedCaseIdUpdationRequest(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod(), "FO", enforcementReviewCase.getLocationDetails(),
					tempNotificationAction, foUserId, tempCaseStatus);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + "/ru/approve_reject_caseid";
	}

	@GetMapping("/" + ApplicationConstants.RU_REVIEW_CATEGORY_CASE)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setRUMenu(model, ApplicationConstants.RU_REVIEW_CATEGORY_CASE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("RU").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("ABCD");
		model.addAttribute("categories", categoryListRepository
				.findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(locationList, exceptActionList));
//		model.addAttribute("categories",
//				categoryListRepository.findAllCategoryForAssessmentCasesByRuUserId(userDetails.getUserId()));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
//				List<MstParametersModuleWise> parameters = mstParametersRepository
//						.findAllAssessmentParameterByAssessmentCategoryAndRuUserId(category.get().getName(),
//								userDetails.getUserId());
				List<MstParametersModuleWise> parameters = mstParametersRepository
						.findAllAssessmentParameterByAssessmentCategoryAndLocationListAndExceptActionList(
								category.get().getName(), locationList, exceptActionList);
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& mstParametersRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
//					model.addAttribute("categoryTotals",
//							enforcementReviewCaseRepository.getAllCaseCountByCategoryAnd1stParameterIdAndRuUserId(
//									category.get().getName(), parameterId.toString(), userDetails.getUserId()));
					model.addAttribute("categoryTotals", enforcementReviewCaseRepository
							.getAllCaseCountByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(
									category.get().getName(), parameterId.toString(), locationList, exceptActionList));
				} else {
//					model.addAttribute("categoryTotals", enforcementReviewCaseRepository
//							.getAllCaseCountByCategoryAndRuUserId(category.get().getName(), userDetails.getUserId()));
					model.addAttribute("categoryTotals",
							enforcementReviewCaseRepository.getAllCaseCountByCategoryAndLocationListAndExceptActionList(
									category.get().getName(), locationList, exceptActionList));
				}
			}
		}
		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_REVIEW_CATEGORY_CASE + "_new";
	}

	@GetMapping("/" + ApplicationConstants.RU_VIEW_LIST_OF_CASE)
	public String reviewCasesList(Model model, @RequestParam(required = false) String category, String parameterId) {
		List<String[]> caseList = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		setRUMenu(model, ApplicationConstants.RU_REVIEW_CATEGORY_CASE);
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("RU").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("upload", "hqTransfer", "acknowledge", "transfer");
		Category categoryObject = categoryListRepository.findByName(category);
		if (categoryObject != null) {
			model.addAttribute("categoryId", categoryObject.getId());
		}
		if (category != null && category.length() > 0 && parameterId != null && parameterId.length() > 0) {
			caseList = enforcementReviewCaseRepository
					.findByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(category, parameterId,
							locationList, exceptActionList);
//			caseList = enforcementReviewCaseRepository.findByCategoryAnd1stParameterIdAndRuUserId(category, parameterId,
//					userDetails.getUserId());
		} else if (category != null && category.length() > 0) {
			caseList = enforcementReviewCaseRepository.findReviewCasesListByCategoryAndLocationListAndExceptActionList(
					category, locationList, exceptActionList);
//			caseList = enforcementReviewCaseRepository.findReviewCasesListByCategoryAndApUserId(category,
//					userDetails.getUserId());
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_VIEW_LIST_OF_CASE + "_new";
	}
//	@GetMapping("/review_category_case")
//	public String reviewSummaryList(Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//			return "redirect:/logout";
//		}
//		setRUMenu(model, ApplicationConstants.RU_REVIEW_CATEGORY_CASE);
//		try {
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//			List<String> locationId = fieldUserController
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
//		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_REVIEW_CATEGORY_CASE;
//	}
//
//	@GetMapping("/view_list_of_case")
//	public String viewlistOfCase(Model model, @RequestParam(required = false) String category) {
//		try {
//			setRUMenu(model, ApplicationConstants.RU_REVIEW_CATEGORY_CASE);
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			List<EnforcementReviewCase> enforcementReviewList = new ArrayList<EnforcementReviewCase>();
//			List<EnforcementReviewCase> enforcementReviewCase = new ArrayList<EnforcementReviewCase>();
//			;
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//			List<String> locationId = fieldUserController
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
//		return ApplicationConstants.RU + "/" + ApplicationConstants.RU_VIEW_LIST_OF_CASE;
//	}
}
