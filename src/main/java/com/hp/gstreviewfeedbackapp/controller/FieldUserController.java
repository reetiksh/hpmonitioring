package com.hp.gstreviewfeedbackapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import org.springframework.core.io.FileSystemResource;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.data.SelfDetectedCase;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementFieldOfficeService;
import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseIdUpdationRemarks;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CaseidUpdation;
import com.hp.gstreviewfeedbackapp.model.CaseidUpdationModel;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.DataObject;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksCategoryWise;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksDetails;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.FoReviewCase;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.OldCasesUploadManually;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;
import com.hp.gstreviewfeedbackapp.model.TransferRemarks;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseIdRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseIdUpdationRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FOUserCaseReviewRepository;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.OldCasesUploadManuallyRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.RemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.UploadSelfDetectedCaseRemarkRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.service.FOReviewSummeryList;
import com.hp.gstreviewfeedbackapp.service.HQUserUploadService;
import com.hp.gstreviewfeedbackapp.service.UserDetailsService;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Controller
@RequestMapping("/" + ApplicationConstants.FO)
public class FieldUserController {
	private static final Logger logger = LoggerFactory.getLogger(FieldUserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private HQUserUploadDataRepository hqUserUploadDataRepository;
	@Autowired
	private RemarksRepository reviewRepository;
	@Autowired
	private CaseWorkflowRepository foTransferRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private FOUserCaseReviewRepository foUserCaseReviewRepository;
	@Autowired
	private TransferRemarksRepository transferRemarksRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private ReviewMeetingDetailsRepository reviewMeetingDetailsRepository;
	@Autowired
	private ReviewMeetingRepository reviewMeetingRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private FOReviewSummeryList FOReviewSummeryList;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private OldCasesUploadManuallyRepository oldCasesUploadManuallyRepository;
	@Autowired
	private HQUserUploadService hqUserUploadService;
	@Autowired
	private UploadSelfDetectedCaseRemarkRepository uploadSelfDetectedCaseRemarkRepository;
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Autowired
	private EnforcementFieldOfficeService enforcementFieldOfficeService;
	@Autowired
	private UserDetailsService userDetailsService;
	@Value("${action.upload}")
	private String actionUpload;
	@Value("${action.transfer}")
	private String actionTransfer;
	@Value("${action.acknowledge}")
	private String actionAcknowledge;
	@Value("${action.foClose}")
	private String actionClose;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${action.verifyerRaiseQuery}")
	private String verifyerRaiseQuery;
	@Value("${action.approverRaiseQuery}")
	private String approverRaiseQuery;
	@Value("${action.hqTransfer}")
	private String hqtransfer;
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	@Value("${action.caseid.update.foUpdated}")
	private String foUpdated;
	@Value("${action.caseid.update.verifyerApprove}")
	private String verifyerApprove;
	@Value("${action.caseid.update.verifyerReject}")
	private String verifyerReject;
	@Autowired
	private DashboardRepository dashboardRepository;
	@Autowired
	private CaseIdRemarksRepository caseIdUpdationRemarksRepository;
	@Autowired
	private CaseIdUpdationRepository caseIdUpdationRepository;
	@Autowired
	private MstNotificationsRepository mstNotificationsRepository;
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	/*
	 * ...............................dashboard code...............................
	 */

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(Model model, @RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "view", required = false) String view,
			@RequestParam(required = false) Integer parameter) {
		DecimalFormat formatter = new DecimalFormat("##,##,##,##,000",
				new DecimalFormatSymbols(new Locale("en", "IN")));
		/*
		 * NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en",
		 * "IN"));
		 */
		setFOMenu(model, ApplicationConstants.DASHBOARD);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			// Get locations for FO roles
			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
					userRoleRepository.findByroleCode("FO").get());
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
			List<String> circlelocation = null;
			int categoryInt = 0;
			List<DashboardDistrictCircle> circleList = null;
			List<DashboardDistrictCircle> zoneList = null;
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 2);
			List<String> locationId = getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			List<MstParametersModuleWise> parameterList = mstParametersRepository.findAllCasesParameter();
			model.addAttribute("parameterList", parameterList);
			Optional<MstParametersModuleWise> parameterObject = mstParametersRepository
					.findById((parameter != null && parameter > 0) ? parameter : 0);
			if (parameterObject.isPresent()) {
				model.addAttribute("parameterId", parameter);
			}
			if (locationId.contains("HP")) {
				if (category == null && financialyear == null && view == null && parameterObject.isEmpty()) {
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
			model.addAttribute("categories", categoryListRepository.findAllCategoryForAssessmentCases());
			model.addAttribute("circleList", circleList);
			model.addAttribute("category", category);
			model.addAttribute("zoneList", zoneList);
			model.addAttribute("year", financialyear);
			model.addAttribute("viewtype", view);
//			model.addAttribute("hasRoleDistrict", hasRoleDistrict);
			model.addAttribute("commonUserDetails", "/fo/user_details");
			model.addAttribute("changeUserPassword", "/gu/change_password");
			List<MstNotifications> notificationsList = casePertainUserNotification
					.getNotificationPertainToUser(userDetails, "FO");
			logger.info("notfication fo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************** dashboard consolidate case stage list end ******************/
		return ApplicationConstants.FO + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?category=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() + "&view=" + dashboard.getView();
	}
	/*
	 * ...............................dashboard code...............................
	 */

	@GetMapping("/" + ApplicationConstants.FO_ACKNOWLEDGE_CASES)
	public String getAcknowledgeCases(Model model, @RequestParam(required = false) Integer selectedCaseType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> locationId = new ArrayList<String>();
			List<UserRoleMapping> roleMapping = userRoleMappingRepository
					.findAllRolesMapWithFOUsers(userDetails.getUserId());
			for (UserRoleMapping userRoleMapping : roleMapping) {
				String location = null;
				List<String> list = new ArrayList<String>();
				list.add(userRoleMapping.getStateDetails().getStateId());
				list.add(userRoleMapping.getZoneDetails().getZoneId());
				list.add(userRoleMapping.getCircleDetails().getCircleId());
				location = getLocation(list);
				if (location != null) {
					locationId.add(location);
				}
			}
			setFOMenu(model, ApplicationConstants.FO_ACKNOWLEDGE_CASES);
			List<TransferRemarks> transferRemarks = reviewRepository.findAllByOrderByIdDesc();
			List<LocationDetails> circls = new ArrayList<>();
			List<LocationDetails> locationDetailsList = locationDetailsRepository.findAllByOrderByLocationNameAsc();
			for (LocationDetails ld : locationDetailsList) {
				if (!ld.getLocationId().equalsIgnoreCase("Z04") && !ld.getLocationId().equalsIgnoreCase("C81")
						&& !ld.getLocationId().equalsIgnoreCase("HPT") && !ld.getLocationId().equalsIgnoreCase("DT14")
						&& !ld.getLocationId().equalsIgnoreCase("EZ04")) {
					circls.add(ld);
				}
			}
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<String> actionList = new ArrayList<String>();
//			actionList.add(actionUpload);
//			actionList.add(hqtransfer);
//			actionList.add(actionAcknowledge);
			if (selectedCaseType == null || selectedCaseType == 0) {
				selectedCaseType = 0;
				actionList.add(actionUpload);
				actionList.add(hqtransfer);
			} else if (selectedCaseType == 1) {
				actionList.add(actionAcknowledge);
			} else if (selectedCaseType == 2) {
				actionList.add(actionUpload);
				actionList.add(hqtransfer);
				actionList.add(actionAcknowledge);
			}
			model.addAttribute("selectedCaseType", selectedCaseType);
			List<EnforcementReviewCase> locationList = hqUserUploadDataRepository.findByLocationDetail(locationId,
					actionList);
			locationList = locationList.stream().filter(removeManualOldCases -> !removeManualOldCases.getCategory()
					.equals("Detailed Enforcement Old Cases")).collect(Collectors.toList());
			for (EnforcementReviewCase obj : locationList) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = "";
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				Date date = obj.getId().getCaseReportingDate();
				String circleId = obj.getLocationDetails().getLocationId();
				remarks = getRejectRemarks(obj.getId().getGSTIN(), obj.getId().getPeriod(),
						obj.getId().getCaseReportingDate());
				if (obj.getActionStatus() == null
						|| (obj.getCategory().contains("Audit") && obj.getAction().equalsIgnoreCase("upload"))) {
					String pdfFileName = null;
					try {
						pdfFileName = (obj.getExtensionNoDocument() != null
								&& obj.getExtensionNoDocument().getExtensionFileName() != null)
										? obj.getExtensionNoDocument().getExtensionFileName()
										: null;
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
					object.setActionStatus(obj.getAction().equals(actionAcknowledge) ? 1 : 0);
					object.setRemarks(remarks);
					object.setCaseId(circleId);
					object.setUploadedFileName(pdfFileName);
					object.setParameter(getParameterName(obj.getParameter()));
					if (obj.getCategory().contains("Audit")) {
						object.setAuditCase("true");
					}
					listofCases.add(object);
				}
			}
			Map<Integer, String> caseTypeList = new HashMap<>();
			caseTypeList.put(0, "Pending for Acknowledgement");
			caseTypeList.put(1, "Acknowledged");
			caseTypeList.put(2, "Select All");
			model.addAttribute("caseTypeList", caseTypeList);
			model.addAttribute("transferRemarks", transferRemarks);
			model.addAttribute("circls", circls);
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_ACKNOWLEDGE_CASES;
	}

	@GetMapping("/" + ApplicationConstants.FO_UPDATE_SUMMARY_LIST)
	public String getUpdateSummaryList(Model model, @RequestParam(required = false) Integer selectedCaseType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		setFOMenu(model, ApplicationConstants.FO_UPDATE_SUMMARY_LIST);
//		List<Category> categories = categoryListRepository.findAllByActiveStatus(true);
		List<String> actionList = new ArrayList<>();
		actionList.add(actionAcknowledge);
		actionList.add("recommendedForAssesAndAdjudication");
		List<Category> categories = null;
		if (selectedCaseType != null && selectedCaseType > 0) {
			model.addAttribute("selectedCaseType", selectedCaseType);
			categories = selectedCaseType == 1
					? categoryListRepository.findAllActiveCasesCategoriesByActionStatusAndFOUserIdAndAssisnedToSomeFOA(
							actionList, object.get().getUserId())
					: categoryListRepository.findAllActiveCasesCategoriesByActionStatusAndFOUserId(actionList,
							object.get().getUserId());
		} else {
			categories = categoryListRepository
					.findAllActiveCasesCategoriesByActionStatusAndFOUserIdAndNotAssisnedToSomeFOA(actionList,
							object.get().getUserId());
			model.addAttribute("selectedCaseType", "0");
		}
		if (categories != null) {
			model.addAttribute("categories", categories);
		}
//		List<String> caseTypeList = Arrays.asList("Assisted to Me","Assigned to Me and my assistant");
		Map<Integer, String> caseTypeList = new HashMap<>();
		caseTypeList.put(0, "Assigned to Me");
		caseTypeList.put(1, "Assigned to Me and my assistant");
		caseTypeList.put(2, "All Selected");
		model.addAttribute("caseTypeList", caseTypeList);
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/query_raised_by_verifier")
	public String queryRaisedByVerifier(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		setFOMenu(model, "query_raised_by_verifier");
//		model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
		List<String> actionList = new ArrayList<>();
		actionList.add(verifyerRaiseQuery);
		List<Category> categories = categoryListRepository
				.findAllActiveCasesCategoriesByActionStatusAndFOUserId(actionList, objectUserDetails.get().getUserId());
		model.addAttribute("categories", categories);
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_RAISED_QUERY_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/rejected_by_approver")
	public String rejectedByApprover(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		setFOMenu(model, "rejected_by_approver");
//		model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
		List<String> actionList = new ArrayList<>();
		actionList.add(approverRaiseQuery);
		List<Category> categories = categoryListRepository
				.findAllActiveCasesCategoriesByActionStatusAndFOUserId(actionList, objectUserDetails.get().getUserId());
		model.addAttribute("categories", categories);
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_REJECTED_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/update_summary_data_list")
	public String getUpdateSummaryDataList(Model model, @RequestParam Long id,
			@RequestParam(required = false) Integer parameterId,
			@RequestParam(required = false) Integer selectedCaseType) {
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			Category category = null;
			Optional<Category> objCategory = null;
			if (id != null) {
				objCategory = categoryListRepository.findById(id);
				if (objCategory.isPresent()) {
					category = objCategory.get();
					model.addAttribute("categoryId", id);// sending the category id
					List<MstParametersModuleWise> parameters = null;
					if (selectedCaseType != null && selectedCaseType > 0) {
						parameters = (selectedCaseType == 1 ? mstParametersRepository
								.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndAssignedToAnyFOA(
										objCategory.get().getName(), actionAcknowledge, userDetails.getUserId())
								: mstParametersRepository
										.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserId(
												objCategory.get().getName(), actionAcknowledge,
												userDetails.getUserId()));
					} else {
						parameters = mstParametersRepository
								.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndNotAssignedToAnyFOA(
										objCategory.get().getName(), actionAcknowledge, userDetails.getUserId());
					}
					model.addAttribute("parameters", parameters);
				}
			}
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
			List<EnforcementReviewCase> list = null;
			if (selectedCaseType != null && selectedCaseType > 0) {
				if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
					model.addAttribute("parameterId", parameterId);
					Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
					list = selectedCaseType == 1
							? hqUserUploadDataRepository
									.findByCategoryAndActionAndUserIdListAnd1stParameterAndAssignedToAnyFOA(
											categoryName, actionAcknowledge, userIdList, parameterId.toString())
							: hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAnd1stParameter(
									categoryName, actionAcknowledge, userIdList, parameterId.toString());
				} else {
					list = selectedCaseType == 1
							? hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAndAssignedToAnyFOA(
									categoryName, actionAcknowledge, userIdList)
							: hqUserUploadDataRepository.findByCategoryAndActionAndUserIdList(categoryName,
									actionAcknowledge, userIdList);
				}
				model.addAttribute("selectedCaseType", selectedCaseType);
			} else {
				if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
					model.addAttribute("parameterId", parameterId);
					Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
					list = hqUserUploadDataRepository
							.findByCategoryAndActionAndUserIdListAnd1stParameterAndNotAssignedToAnyFOA(categoryName,
									actionAcknowledge, userIdList, parameterId.toString());
				} else {
					list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAndNotAssignedToAnyFOA(
							categoryName, actionAcknowledge, userIdList);
				}
			}
			for (EnforcementReviewCase obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = "";
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				String caseidUpdateStatus = obj.getCaseidUpdationStatus();
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(obj.getId().getGSTIN());
				compositeKey.setCaseReportingDate(obj.getId().getCaseReportingDate());
				compositeKey.setPeriod(obj.getId().getPeriod());
				String uploadedFileName = null;
				try {
					ExtensionNoDocument extensionNoDocument = obj.getExtensionNoDocument();
					if (extensionNoDocument != null) {
						uploadedFileName = extensionNoDocument.getExtensionFileName();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (id > 0 && id <= 6) {
					EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
							.findTopByCategoryIdOrderByIdDesc(id);
					if (enforcementCasesRemarksCategoryWise != null) {
						Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date CaseReportingDate = dateFormat.parse(caseReportingDate);
						if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
							remarks = enforcementCasesRemarksCategoryWise.getRemarks();
						} else {
							remarks = "";
						}
					}
				} else {
					remarks = getGSTNRemarks(obj.getId().getGSTIN(), obj.getId().getCaseReportingDate(),
							obj.getId().getPeriod());
				}
				if (actionStatus > 0 & caseStage > 0 & recoveryStage > 0) {
					actionName = actionStatusRepository.findById(actionStatus).get().getName();
					caseStageName = caseStageRepository.findById(caseStage).get().getName();
					recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				} else {
					actionName = "";
					caseStageName = "";
					recoveryStageName = "";
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				if (caseId != null && caseId.trim().length() > 0) {
					object.setCaseId(caseId);
				}
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setRemarks(remarks);
				object.setUploadedFileName(uploadedFileName);
				object.setParameter(getParameterName(obj.getParameter()));
				if (caseId != null && caseId.trim().length() > 0) {
					if (caseidUpdateStatus == null) {
						object.setCaseIdUpdate("yes");
					} else {
						if (caseidUpdateStatus.equals("foUpdated")) {
							object.setCaseIdUpdate("");
						} else if (caseidUpdateStatus.equals("verifyerApprove")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("verifyerReject")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("")) {
							object.setCaseIdUpdate("yes");
						}
					}
				} else {
					object.setCaseIdUpdate("");
				}
				listofCases.add(object);
			}
			List<CaseIdUpdationRemarks> caseIdUpdationRemarks = caseIdUpdationRemarksRepository
					.findAllByOrderByIdDesc();
			model.addAttribute("listofCases", listofCases);
			model.addAttribute("listRemarks", caseIdUpdationRemarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/update_summary_list_data";
	}

	@GetMapping("/raised_query_update_summary_data_list")
	public String getRaisedQueryUpdateSummaryDataList(Model model, @RequestParam Long id,
			@RequestParam(required = false) Integer parameterId) {
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			Category category = null;
			Optional<Category> objCategory = null;
			if (id != null) {
				objCategory = categoryListRepository.findById(id);
				if (objCategory.isPresent()) {
					category = objCategory.get();
					model.addAttribute("categoryId", id);// sending the category id
					List<MstParametersModuleWise> parameters = mstParametersRepository
							.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserId(
									objCategory.get().getName(), verifyerRaiseQuery, userDetails.getUserId());
					model.addAttribute("parameters", parameters);
				}
			}
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
//		    List<EnforcementReviewCase> list = hqUserUploadDataRepository.findByCategory(categoryName);
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
//			List<EnforcementReviewCase> list = hqUserUploadDataRepository
//					.findByCategoryAndActionAndUserIdList(categoryName, verifyerRaiseQuery, userIdList);
			List<EnforcementReviewCase> list = null;
			if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
				model.addAttribute("parameterId", parameterId);
				Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAnd1stParameter(categoryName,
						verifyerRaiseQuery, userIdList, parameterId.toString());
			} else {
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdList(categoryName, verifyerRaiseQuery,
						userIdList);
			}
			for (EnforcementReviewCase obj : list) {
//				if (obj.getAction().equals(verifyerRaiseQuery) && obj.getAssignedToUserId() == userId) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = foUserCaseReviewRepository.findVerifierRemarks(obj.getId().getGSTIN(),
						obj.getId().getCaseReportingDate(), obj.getId().getPeriod());
				String uploadedFileName = null;
				try {
					ExtensionNoDocument extensionNoDocument = obj.getExtensionNoDocument();
					uploadedFileName = extensionNoDocument.getExtensionFileName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				String caseidUpdateStatus = obj.getCaseidUpdationStatus();
				if (actionStatus > 0 & caseStage > 0 & recoveryStage > 0) {
					actionName = actionStatusRepository.findById(actionStatus).get().getName();
					caseStageName = caseStageRepository.findById(caseStage).get().getName();
					recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				} else {
					actionName = "";
					caseStageName = "";
					recoveryStageName = "";
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				object.setRemarks(remarks);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setUploadedFileName(uploadedFileName);
				object.setParameter(getParameterName(obj.getParameter()));
				if (caseId != null) {
					if (caseidUpdateStatus == null) {
						object.setCaseIdUpdate("yes");
					} else {
						if (caseidUpdateStatus.equals("foUpdated")) {
							object.setCaseIdUpdate("");
						} else if (caseidUpdateStatus.equals("verifyerApprove")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("verifyerReject")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("")) {
							object.setCaseIdUpdate("yes");
						}
					}
				} else {
					object.setCaseIdUpdate("");
				}
				listofCases.add(object);
			}
			List<CaseIdUpdationRemarks> caseIdUpdationRemarks = caseIdUpdationRemarksRepository
					.findAllByOrderByIdDesc();
			model.addAttribute("listRemarks", caseIdUpdationRemarks);
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/raised_query_update_summary_list_data";
	}

	@GetMapping("/rejected_update_summary_data_list")
	public String getRejectedUpdateSummaryDataList(Model model, @RequestParam Long id,
			@RequestParam(required = false) Integer parameterId) {
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
//			Category category = categoryListRepository.findById(id).get();
			Category category = null;
			Optional<Category> objCategory = null;
			if (id != null) {
				objCategory = categoryListRepository.findById(id);
				if (objCategory.isPresent()) {
					category = objCategory.get();
					model.addAttribute("categoryId", id);// sending the category id
					List<MstParametersModuleWise> parameters = mstParametersRepository
							.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserId(
									objCategory.get().getName(), approverRaiseQuery, userDetails.getUserId());
					model.addAttribute("parameters", parameters);
				}
			}
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
//		List<EnforcementReviewCase> list = hqUserUploadDataRepository.findByCategory(categoryName);
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
//			List<EnforcementReviewCase> list = hqUserUploadDataRepository
//					.findByCategoryAndActionAndUserIdList(categoryName, approverRaiseQuery, userIdList);
			List<EnforcementReviewCase> list = null;
			if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
				model.addAttribute("parameterId", parameterId);
				Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAnd1stParameter(categoryName,
						approverRaiseQuery, userIdList, parameterId.toString());
			} else {
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdList(categoryName, approverRaiseQuery,
						userIdList);
			}
			for (EnforcementReviewCase obj : list) {
//				if (obj.getAction().equals(approverRaiseQuery) && obj.getAssignedToUserId() == userId) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = foUserCaseReviewRepository.findApproverRemarks(obj.getId().getGSTIN(),
						obj.getId().getCaseReportingDate(), obj.getId().getPeriod());
				String uploadedFileName = null;
				try {
					ExtensionNoDocument extensionNoDocument = obj.getExtensionNoDocument();
					uploadedFileName = extensionNoDocument.getExtensionFileName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				String caseidUpdateStatus = obj.getCaseidUpdationStatus();
				if (actionStatus > 0 & caseStage > 0 & recoveryStage > 0) {
					actionName = actionStatusRepository.findById(actionStatus).get().getName();
					caseStageName = caseStageRepository.findById(caseStage).get().getName();
					recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				} else {
					actionName = "";
					caseStageName = "";
					recoveryStageName = "";
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				object.setRemarks(remarks);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setParameter(getParameterName(obj.getParameter()));
				/* object.setUploadedFileName(uploadedFileName); */
				object.setUploadedFileName(URLEncoder.encode(caseWorkflowRepository
						.returnFilePathForRevertedCaseByApprover(gst, period, obj.getId().getCaseReportingDate())));
				if (caseId != null) {
					if (caseidUpdateStatus == null) {
						object.setCaseIdUpdate("yes");
					} else {
						if (caseidUpdateStatus.equals("foUpdated")) {
							object.setCaseIdUpdate("");
						} else if (caseidUpdateStatus.equals("verifyerApprove")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("verifyerReject")) {
							object.setCaseIdUpdate("yes");
						} else if (caseidUpdateStatus.equals("")) {
							object.setCaseIdUpdate("yes");
						}
					}
				} else {
					object.setCaseIdUpdate("");
				}
				listofCases.add(object);
			}
			List<CaseIdUpdationRemarks> caseIdUpdationRemarks = caseIdUpdationRemarksRepository
					.findAllByOrderByIdDesc();
			model.addAttribute("listRemarks", caseIdUpdationRemarks);
			model.addAttribute("listofCases", listofCases);
		} catch (NullPointerException ex) {
			logger.error("NullPointerException " + ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/rejected_update_summary_list_data";
	}

	@GetMapping("/view_case/id")
	public String getCaseData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<ActionStatus> obj1 = new ArrayList<ActionStatus>();
		List<CaseStage> obj2 = new ArrayList<CaseStage>();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			String remarks = "";
			Long id = 0L;
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<ActionStatus> listActionStatus = actionStatusRepository.findAll();
			List<CaseStage> listCaseStage = caseStageRepository.findAll();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			id = categoryListRepository.findByName(catecategory).getId();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			if (id > 0 && id <= 6) {
				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
						.findTopByCategoryIdOrderByIdDesc(id);
				if (enforcementCasesRemarksCategoryWise != null) {
					Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
					Date CaseReportingDate = caseReportingDate;
					if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
						remarks = enforcementCasesRemarksCategoryWise.getRemarks();
					} else {
						remarks = "";
					}
				}
			} else {
				remarks = getGSTNRemarks(enforcementReviewCase.getId().getGSTIN(),
						enforcementReviewCase.getId().getCaseReportingDate(),
						enforcementReviewCase.getId().getPeriod());
			}
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setDate(reportingDate);
			object.setRemarks(remarks);
			if (enforcementReviewCase.getCategory().contains("Audit") && enforcementReviewCase.getCaseStageArn() != null
					&& enforcementReviewCase.getCaseStageArn().trim().length() > 0
					&& (enforcementReviewCase.getCaseStage() == null || (enforcementReviewCase.getCaseStage() != null
							&& enforcementReviewCase.getCaseStage().getName().equalsIgnoreCase("DRC01 issued")))) {
				object.setAuditCase("true");
			}
			String caseId = enforcementReviewCase.getCaseId();
			if (caseId == null || caseId.trim().length() == 0) {
				object.setAuditCaseId(enforcementReviewCase.getAuditCaseId());
				object.setCaseIdUpdated("no");
			} else {
				object.setCaseIdUpdated("yes");
			}
			if (caseId != null && caseId.trim().length() > 0) {
				object.setCaseId(caseId);
			}
			for (ActionStatus actionStatus : listActionStatus) {
				obj1.add(actionStatus);
			}
			for (CaseStage caseStage : listCaseStage) {
				obj2.add(caseStage);
			}
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			if (enforcementReviewCase.getActionStatus() == null) {
				submittedData.setActionStatusName("");
				submittedData.setActionStatus(0);
			} else {
				submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
				submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			}
			if (enforcementReviewCase.getCaseStage() == null) {
				submittedData.setCaseStageName("");
				submittedData.setCaseStage(0);
			} else {
				submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
				submittedData.setCaseStage(enforcementReviewCase.getCaseStage().getId());
			}
			if (enforcementReviewCase.getCaseStageArn() == null) {
				submittedData.setCaseStageArn("");
			} else {
				submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			}
			if (enforcementReviewCase.getRecoveryStage() == null) {
				submittedData.setRecoveryStageName("");
				submittedData.setRecoveryStage(0);
			} else {
				submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
				submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			}
			if (enforcementReviewCase.getRecoveryStageArn() == null) {
				submittedData.setRecoveryStageArnStr("");
			} else {
				submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			}
			String fileName = foUserCaseReviewRepository.getFileNameUploadedByFO(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedData.setSum("filenotexist");
			} else {
				submittedData.setSum("fileexist");
			}
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listActionStatus", obj1);
			model.addAttribute("listCaseStage", obj2);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("submittedData", submittedData);
			model.addAttribute("viewItem", object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/view_case";
	}

	@GetMapping("/raised_query_view_case/id")
	public String getRaisedQueryCaseData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<ActionStatus> obj1 = new ArrayList<ActionStatus>();
		List<CaseStage> obj2 = new ArrayList<CaseStage>();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			String remarks = "";
			Long id = 0L;
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<ActionStatus> listActionStatus = actionStatusRepository.findAll();
			List<CaseStage> listCaseStage = caseStageRepository.findAll();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			id = categoryListRepository.findByName(catecategory).getId();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			if (id > 0 && id <= 6) {
				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
						.findTopByCategoryIdOrderByIdDesc(id);
				if (enforcementCasesRemarksCategoryWise != null) {
					Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
					Date CaseReportingDate = caseReportingDate;
					if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
						remarks = enforcementCasesRemarksCategoryWise.getRemarks();
					} else {
						remarks = "";
					}
				}
			} else {
				remarks = getGSTNRemarks(enforcementReviewCase.getId().getGSTIN(),
						enforcementReviewCase.getId().getCaseReportingDate(),
						enforcementReviewCase.getId().getPeriod());
			}
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			String caseId = enforcementReviewCase.getCaseId();
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setCaseId(caseId);
			object.setDate(reportingDate);
			object.setRemarks(remarks);
			for (ActionStatus actionStatus : listActionStatus) {
				obj1.add(actionStatus);
			}
			for (CaseStage caseStage : listCaseStage) {
				obj2.add(caseStage);
			}
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			if (enforcementReviewCase.getActionStatus() == null) {
				submittedData.setActionStatusName("");
				submittedData.setActionStatus(0);
			} else {
				submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
				submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			}
			if (enforcementReviewCase.getCaseStage() == null) {
				submittedData.setCaseStageName("");
				submittedData.setCaseStage(0);
			} else {
				submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
				submittedData.setCaseStage(enforcementReviewCase.getCaseStage().getId());
			}
			if (enforcementReviewCase.getCaseStageArn() == null) {
				submittedData.setCaseStageArn("");
			} else {
				submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			}
			if (enforcementReviewCase.getRecoveryStage() == null) {
				submittedData.setRecoveryStageName("");
				submittedData.setRecoveryStage(0);
			} else {
				submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
				submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			}
			if (enforcementReviewCase.getRecoveryStageArn() == null) {
				submittedData.setRecoveryStageArnStr("");
			} else {
				submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			}
			String fileName = foUserCaseReviewRepository.getFileNameRejectedByVerifier(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedData.setSum("filenotexist");
			} else {
				submittedData.setSum("fileexist");
			}
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listActionStatus", obj1);
			model.addAttribute("listCaseStage", obj2);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("viewItem", object);
			model.addAttribute("submittedData", submittedData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/raised_query_view_case";
	}

	@GetMapping("/rejected_view_case/id")
	public String getRejectedCaseData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedobject = new EnforcementReviewCaseModel();
		List<ActionStatus> obj1 = new ArrayList<ActionStatus>();
		List<CaseStage> obj2 = new ArrayList<CaseStage>();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<ActionStatus> listActionStatus = actionStatusRepository.findAll();
			List<CaseStage> listCaseStage = caseStageRepository.findAll();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String remarks = "";
			Long id = 0L;
			String catecategory = enforcementReviewCase.getCategory();
			id = categoryListRepository.findByName(catecategory).getId();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			if (id > 0 && id <= 6) {
				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
						.findTopByCategoryIdOrderByIdDesc(id);
				if (enforcementCasesRemarksCategoryWise != null) {
					Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
					Date CaseReportingDate = caseReportingDate;
					if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
						remarks = enforcementCasesRemarksCategoryWise.getRemarks();
					} else {
						remarks = "";
					}
				}
			} else {
				remarks = getGSTNRemarks(enforcementReviewCase.getId().getGSTIN(),
						enforcementReviewCase.getId().getCaseReportingDate(),
						enforcementReviewCase.getId().getPeriod());
			}
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			String caseId = enforcementReviewCase.getCaseId();
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setCaseId(caseId);
			object.setDate(reportingDate);
			object.setRemarks(remarks);
			for (ActionStatus actionStatus : listActionStatus) {
				obj1.add(actionStatus);
			}
			for (CaseStage caseStage : listCaseStage) {
				obj2.add(caseStage);
			}
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			if (enforcementReviewCase.getActionStatus() == null) {
				submittedobject.setActionStatusName("");
				submittedobject.setActionStatus(0);
			} else {
				submittedobject.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
				submittedobject.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			}
			if (enforcementReviewCase.getCaseStage() == null) {
				submittedobject.setCaseStageName("");
				submittedobject.setCaseStage(0);
			} else {
				submittedobject.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
				submittedobject.setCaseStage(enforcementReviewCase.getCaseStage().getId());
			}
			if (enforcementReviewCase.getRecoveryStage() == null) {
				submittedobject.setRecoveryStageName("");
				submittedobject.setRecoveryStage(0);
			} else {
				submittedobject.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
				submittedobject.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			}
			String fileName = foUserCaseReviewRepository.getFileNameRejectedByApprover(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedobject.setSum("filenotexist");
			} else {
				submittedobject.setSum("fileexist");
			}
			submittedobject.setDemand(enforcementReviewCase.getDemand());
			submittedobject.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			submittedobject.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedobject.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedobject.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedobject.setRemarks(fileName);
			model.addAttribute("listActionStatus", obj1);
			model.addAttribute("listCaseStage", obj2);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("viewItem", object);
			model.addAttribute("submittedData", submittedobject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/rejected_view_case";
	}

	@PostMapping("/fo_transfer_cases")
	@ResponseBody
	public String trasferCase(Model model, @ModelAttribute WorkFlowModel transferModel,
			RedirectAttributes redirectAttrs) {
		String message = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			String fileName = null;
			String gstn = transferModel.getGstno();
			String caseAssignTo = transferModel.getCaseAssignedTo();
			String period = transferModel.getPeriod();
			String reportingdate = transferModel.getDate();
			int reamrks = Integer.parseInt(transferModel.getRemarkOptions());
			SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(transferModel.getDate());
			String timestamp = timestampFormat.format(new Date());
			if (caseAssignTo.equals("NC")) {
				String orginalFileName = transferModel.getUploadedFile().getOriginalFilename();
				fileName = timestamp + "_" + orginalFileName;
				byte[] byteAry = transferModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(byteAry);
			}
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(transferModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(transferModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			int assignedFromUserId = enforcementReviewCase.getAssignedFromUserId();
			enforcementReviewCase.setAction(actionTransfer);
			enforcementReviewCase.setAssignedTo("HQ");
			enforcementReviewCase.setAssignedFrom("FO");
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			enforcementReviewCase.setAssignedToUserId(
					enforcementReviewCaseAssignedUsersRepository.findById(compositKey).get().getHqUserId());
			hqUserUploadDataRepository.save(enforcementReviewCase);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			CaseWorkflow locationDewtails = foTransferRepository.findHQLocationDetails(compositKey.getGSTIN(),
					compositKey.getCaseReportingDate(), compositKey.getPeriod());
			CaseWorkflow foTransferReviewCase = new CaseWorkflow();
			foTransferReviewCase.setGSTIN(compositKey.getGSTIN());
			foTransferReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foTransferReviewCase.setPeriod(compositKey.getPeriod());
			foTransferReviewCase.setAssignedFrom("FO");
			foTransferReviewCase.setAssignedTo("HQ");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			foTransferReviewCase.setUpdatingDate(new Date());
			foTransferReviewCase.setAction(actionTransfer);
			foTransferReviewCase.setSuggestedJurisdiction(transferModel.getCaseAssignedTo());
			foTransferReviewCase.setRemarks(
					transferRemarksRepository.findById(Integer.parseInt(transferModel.getRemarkOptions())).get());
			foTransferReviewCase.setOtherRemarks(transferModel.getOtherRemarks());
			foTransferReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foTransferReviewCase.setAssigntoUserId(updatedReviewCase.getAssignedToUserId());
			foTransferReviewCase.setAssignedFromLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedToLocationId(transferModel.getCaseAssignedTo());
			foTransferReviewCase.setTransferFilePath(fileName);
			foTransferReviewCase.setAssignedFromLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferReviewCase.setAssignedToLocationName(
					locationDetailsRepository.findById(transferModel.getCaseAssignedTo()).get().getLocationName());
			foTransferReviewCase.setSuggestedJurisdictionName(
					locationDetailsRepository.findById(transferModel.getCaseAssignedTo()).get().getLocationName());
			foTransferRepository.save(foTransferReviewCase);
			FoReviewCase foReviewCase = new FoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setAssignedTo("HQ");
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foReviewCase.setSuggestedJurisdiction(transferModel.getCaseAssignedTo());
			foReviewCase.setSuggestedJurisdictionName(
					locationDetailsRepository.findById(transferModel.getCaseAssignedTo()).get().getLocationName());
			foReviewCase.setTransferRemarks(transferRemarksRepository
					.findById(Integer.parseInt(transferModel.getRemarkOptions())).get().getName());
			foReviewCase.setOtherRemarks(transferModel.getOtherRemarks());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setTransferFilePath(fileName);
			foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("Transfer request completed successfully");
			message = "Transfer request completed successfully";
			/****** Check case already assign to specific user or not start *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"HQ", enforcementReviewCase.getLocationDetails(), "transfer", 0);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
			message = "There is some error....";
		}
		return message;
	}

	@PostMapping("/fo_acknowledge_cases")
	@ResponseBody
	public String acknowledgeCase(Model model, @ModelAttribute("acknowledgeForm") WorkFlowModel acknowledgeModel,
			RedirectAttributes redirectAttrs) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(acknowledgeModel.getDate());
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(acknowledgeModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(acknowledgeModel.getPeriod());
			EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
					.findById(compositKey).get();
			enforcementReviewCaseAssignedUsers.setFoUserId(userDetails.getUserId());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionAcknowledge);
			enforcementReviewCase.setAssignedToUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(enforcementReviewCase);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			CaseWorkflow foTransferReviewCase = new CaseWorkflow();
			foTransferReviewCase.setGSTIN(compositKey.getGSTIN());
			foTransferReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foTransferReviewCase.setPeriod(compositKey.getPeriod());
			foTransferReviewCase.setAssignedFrom("FO");
			foTransferReviewCase.setAssignedTo("FO");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			foTransferReviewCase.setUpdatingDate(new Date());
			foTransferReviewCase.setAction(actionAcknowledge);
			foTransferReviewCase.setAssignedFromUserId(userId);
			foTransferReviewCase.setAssigntoUserId(userId);
			foTransferReviewCase.setAssignedFromLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedToLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedFromLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferReviewCase.setAssignedToLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferRepository.save(foTransferReviewCase);
			FoReviewCase foReviewCase = new FoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setAssignedTo("FO");
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("Acknowledgement completed successfully");
			message = "Acknowledgement completed successfully";
		} catch (Exception e) {
			message = "There is some error....";
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return message;
	}

	@PostMapping("/fo_close_cases")
	public String closeCase(Model model, @ModelAttribute("caseCloseForm") WorkFlowModel closeModel,
			RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(closeModel.getDate());
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(closeModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(closeModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionClose);
			enforcementReviewCase.setAssignedTo("RU");
			enforcementReviewCase.setAssignedFrom("FO");
			enforcementReviewCase.setAssignedToUserId(
					enforcementReviewCaseAssignedUsersRepository.findById(compositKey).get().getRuUserId());
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(enforcementReviewCase);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			CaseWorkflow foTransferReviewCase = new CaseWorkflow();
			foTransferReviewCase.setGSTIN(compositKey.getGSTIN());
			foTransferReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foTransferReviewCase.setPeriod(compositKey.getPeriod());
			foTransferReviewCase.setAssignedFrom("FO");
			foTransferReviewCase.setAssignedTo("RU");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			foTransferReviewCase.setUpdatingDate(parsedCurrentDate);
			foTransferReviewCase.setAction(actionClose);
			foTransferReviewCase.setAssignedFromLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedToLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedFromUserId(userId);
			foTransferReviewCase.setAssigntoUserId(0);
			foTransferReviewCase.setAssignedFromLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferReviewCase.setAssignedToLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferRepository.save(foTransferReviewCase);
			FoReviewCase foReviewCase = new FoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setDemand(updatedReviewCase.getDemand());
			foReviewCase.setRecoveryAgainstDemand(updatedReviewCase.getRecoveryAgainstDemand());
			foReviewCase.setRecoveryByDRC3(updatedReviewCase.getRecoveryByDRC3());
			foReviewCase.setRecoveryStage(
					updatedReviewCase.getRecoveryStage() != null ? updatedReviewCase.getRecoveryStage().getId() : 0);
			foReviewCase.setCaseStage(
					updatedReviewCase.getCaseStage() != null ? updatedReviewCase.getCaseStage().getId() : 0);
			foReviewCase.setActionStatus(
					updatedReviewCase.getActionStatus() != null ? updatedReviewCase.getActionStatus().getId() : 0);
			foReviewCase.setAssignedTo("RU");
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foReviewCase.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			foReviewCase.setCaseStageArn(updatedReviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(updatedReviewCase.getRecoveryStageArn());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			/****** Check case already assign to specific user or not start *****/
			List<String> tempPreviousStatus = Arrays.asList("upload");
			Integer foUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
					compositKey.getGSTIN(), compositKey.getPeriod(), compositKey.getCaseReportingDate()).getFoUserId();
			List<MstNotifications> notificationsList = mstNotificationsRepository
					.getNotificationsToUpdateNotificationPertainTo(compositKey.getGSTIN(), compositKey.getPeriod(),
							compositKey.getCaseReportingDate(), tempPreviousStatus, "FO",
							returnWorkingLocation(userDetails.getUserId()));
			if (!notificationsList.isEmpty()) {
				for (MstNotifications notificationSolo : notificationsList) {
					notificationSolo.setNotificationPertainTo(foUserId);
				}
				mstNotificationsRepository.saveAll(notificationsList);
			}
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"RU", enforcementReviewCase.getLocationDetails(), "close", 0);
			/****** Check case already assign to specific user or not End *****/
			redirectAttrs.addFlashAttribute("closeclasemessage", "Case closed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FO_UPDATE_SUMMARY_LIST;
	}

	@PostMapping("/fo_close_raised_query_cases")
	public String closeRaisedQueryCase(Model model, @ModelAttribute("caseCloseForm") WorkFlowModel closeModel,
			RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(closeModel.getDate());
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(closeModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(closeModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionClose);
			enforcementReviewCase.setAssignedTo("RU");
			enforcementReviewCase.setAssignedFrom("FO");
			enforcementReviewCase.setAssignedToUserId(
					enforcementReviewCaseAssignedUsersRepository.findById(compositKey).get().getRuUserId());
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(enforcementReviewCase);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			CaseWorkflow foTransferReviewCase = new CaseWorkflow();
			foTransferReviewCase.setGSTIN(compositKey.getGSTIN());
			foTransferReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foTransferReviewCase.setPeriod(compositKey.getPeriod());
			foTransferReviewCase.setAssignedFrom("FO");
			foTransferReviewCase.setAssignedTo("RU");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			foTransferReviewCase.setUpdatingDate(parsedCurrentDate);
			foTransferReviewCase.setAction(actionClose);
			foTransferReviewCase.setAssignedFromLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedToLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedFromUserId(userId);
			foTransferReviewCase.setAssigntoUserId(0);
			foTransferReviewCase.setAssignedFromLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferReviewCase.setAssignedToLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferRepository.save(foTransferReviewCase);
			FoReviewCase foReviewCase = new FoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setDemand(updatedReviewCase.getDemand());
			foReviewCase.setRecoveryAgainstDemand(updatedReviewCase.getRecoveryAgainstDemand());
			foReviewCase.setRecoveryByDRC3(updatedReviewCase.getRecoveryByDRC3());
			foReviewCase.setRecoveryStage(
					updatedReviewCase.getRecoveryStage() != null ? updatedReviewCase.getRecoveryStage().getId() : 0);
			foReviewCase.setCaseStage(
					updatedReviewCase.getCaseStage() != null ? updatedReviewCase.getCaseStage().getId() : 0);
			foReviewCase.setActionStatus(
					updatedReviewCase.getActionStatus() != null ? updatedReviewCase.getActionStatus().getId() : 0);
			foReviewCase.setAssignedTo("RU");
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foReviewCase.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			foReviewCase.setCaseStageArn(updatedReviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(updatedReviewCase.getRecoveryStageArn());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			redirectAttrs.addFlashAttribute("closeclasemessage", "Case closed successfully");
			/****** Check case already assign to specific user or not start *****/
			Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getPeriod(),
					enforcementReviewCase.getId().getCaseReportingDate()).getRuUserId();
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"RU", enforcementReviewCase.getLocationDetails(), "close", ruUserId);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + "/fo/query_raised_by_verifier";
	}

	@PostMapping("/fo_close_rejected_cases")
	public String closeRejectedCase(Model model, @ModelAttribute("caseCloseForm") WorkFlowModel closeModel,
			RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(closeModel.getDate());
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(closeModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(closeModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionClose);
			enforcementReviewCase.setAssignedTo("RU");
			enforcementReviewCase.setAssignedFrom("FO");
			enforcementReviewCase.setAssignedToUserId(
					enforcementReviewCaseAssignedUsersRepository.findById(compositKey).get().getRuUserId());
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(enforcementReviewCase);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			CaseWorkflow foTransferReviewCase = new CaseWorkflow();
			foTransferReviewCase.setGSTIN(compositKey.getGSTIN());
			foTransferReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foTransferReviewCase.setPeriod(compositKey.getPeriod());
			foTransferReviewCase.setAssignedFrom("FO");
			foTransferReviewCase.setAssignedTo("RU");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			foTransferReviewCase.setUpdatingDate(parsedCurrentDate);
			foTransferReviewCase.setAction(actionClose);
			foTransferReviewCase.setAssignedFromLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedToLocationId(updatedReviewCase.getLocationDetails().getLocationId());
			foTransferReviewCase.setAssignedFromUserId(userId);
			foTransferReviewCase.setAssigntoUserId(0);
			foTransferReviewCase.setAssignedFromLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferReviewCase.setAssignedToLocationName(updatedReviewCase.getLocationDetails().getLocationName());
			foTransferRepository.save(foTransferReviewCase);
			FoReviewCase foReviewCase = new FoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setDemand(updatedReviewCase.getDemand());
			foReviewCase.setRecoveryAgainstDemand(updatedReviewCase.getRecoveryAgainstDemand());
			foReviewCase.setRecoveryByDRC3(updatedReviewCase.getRecoveryByDRC3());
			foReviewCase.setRecoveryStage(
					updatedReviewCase.getRecoveryStage() != null ? updatedReviewCase.getRecoveryStage().getId() : 0);
			foReviewCase.setCaseStage(
					updatedReviewCase.getCaseStage() != null ? updatedReviewCase.getCaseStage().getId() : 0);
			foReviewCase.setActionStatus(
					updatedReviewCase.getActionStatus() != null ? updatedReviewCase.getActionStatus().getId() : 0);
			foReviewCase.setAssignedTo("RU");
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foReviewCase.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			foReviewCase.setCaseStageArn(updatedReviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(updatedReviewCase.getRecoveryStageArn());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			/****** Check case already assign to specific user or not start *****/
			Integer ruUserId = enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getPeriod(),
					enforcementReviewCase.getId().getCaseReportingDate()).getRuUserId();
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"RU", enforcementReviewCase.getLocationDetails(), "close", ruUserId);
			/****** Check case already assign to specific user or not End *****/
			redirectAttrs.addFlashAttribute("closeclasemessage", "Case closed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + "/fo/rejected_by_approver";
	}

	@PostMapping("/fo_update_cases")
	@Transactional
	public String uploadCase(Model model, @ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel,
			BindingResult br, RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getFieldError());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseId = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			int actionStatusId = caseUpdateModel.getActionStatus();
			if (actionStatusId == 1) {
				caseUpdateModel.setRecoveryStage(4);
				caseUpdateModel.setCaseStage(7);
				caseUpdateModel.setRecoveryAgainstDemand(0L);
				caseUpdateModel.setRecoveryByDRC3(0L);
				caseUpdateModel.setDemand(0L);
				fileName = "";
				caseStageArn = "";
				recoveryStageArnArray = "";
				recoveryStageArn = "";
			} else {
				orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
				timeStamp = dateFormat.format(new java.util.Date());
				fileName = timeStamp + "_" + orgFileName;
				byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(bytes);
				caseStageArn = caseUpdateModel.getCaseStageArn();
				if (caseUpdateModel.getRecoveryStageArn() != null) {
					recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
					recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
				} else {
					recoveryStageArn = "";
				}
			}
			int userId = userDetails.getUserId();
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			caseId = caseUpdateModel.getCaseId();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			String caseIdStr = updatedReviewCase.getCaseId();
			if (actionStatusId != 1) {
				if (caseIdStr == null) {
					updatedReviewCase.setCaseId(caseId);
				} else {
					updatedReviewCase.setCaseId(updatedReviewCase.getCaseId());
				}
			}
			updatedReviewCase.setActionStatus(actionStatusRepository.findById(caseUpdateModel.getActionStatus()).get());
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setCaseStage(caseStageRepository.findById(caseUpdateModel.getCaseStage()).get());
			updatedReviewCase.setDemand(caseUpdateModel.getDemand());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryByDRC3(caseUpdateModel.getRecoveryByDRC3());
			updatedReviewCase.setCaseStageArn(caseStageArn);
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			updatedReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			String action = "status_updated";
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String assignedTo = reviewCase.getAssignedTo();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo(assignedTo);
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			if (caseIdStr == null) {
				foReviewCase.setCaseId(caseId);
			} else {
				foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			}
			foReviewCase.setCaseStageArn(caseStageArn);
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionFilename((reviewCase.getExtensionNoDocument() != null
					&& reviewCase.getExtensionNoDocument().getExtensionFileName() != null)
							? reviewCase.getExtensionNoDocument().getExtensionFileName()
							: null);
			foReviewCase.setAssignedFromUserId(reviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(updatedReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case updated successfully");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FO_UPDATE_SUMMARY_LIST;
	}

	@PostMapping("/fo_update_raised_query_cases")
	public String uploadRaisedQueryCase(Model model,
			@ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error :  " + br.getErrorCount());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			int actionStatusId = caseUpdateModel.getActionStatus();
			if (actionStatusId == 1) {
				caseUpdateModel.setRecoveryStage(4);
				caseUpdateModel.setCaseStage(7);
				caseUpdateModel.setRecoveryAgainstDemand(0L);
				caseUpdateModel.setRecoveryByDRC3(0L);
				caseUpdateModel.setDemand(0L);
				fileName = "";
				caseStageArn = "";
				recoveryStageArnArray = "";
				recoveryStageArn = "";
			} else {
				orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
				timeStamp = dateFormat.format(new java.util.Date());
				fileName = timeStamp + "_" + orgFileName;
				byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(bytes);
				caseStageArn = caseUpdateModel.getCaseStageArn();
				if (caseUpdateModel.getRecoveryStageArn() != null) {
					recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
					recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
				} else {
					recoveryStageArn = "";
				}
			}
			int userId = userDetails.getUserId();
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			updatedReviewCase.setActionStatus(actionStatusRepository.findById(caseUpdateModel.getActionStatus()).get());
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setCaseStage(caseStageRepository.findById(caseUpdateModel.getCaseStage()).get());
			updatedReviewCase.setDemand(caseUpdateModel.getDemand());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryByDRC3(caseUpdateModel.getRecoveryByDRC3());
			updatedReviewCase.setCaseStageArn(caseStageArn);
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			updatedReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			// String action = reviewCase.getAction();
			String action = "status_updated_v";
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String assignedTo = reviewCase.getAssignedTo();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo(assignedTo);
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseStageArn(caseStageArn);
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(reviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(updatedReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case updated successfully");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + "query_raised_by_verifier";
	}

	@PostMapping("/fo_update_reject_cases")
	public String uploadRejectCase(Model model,
			@ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getErrorCount());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			int actionStatusId = caseUpdateModel.getActionStatus();
			if (actionStatusId == 1) {
				caseUpdateModel.setRecoveryStage(4);
				caseUpdateModel.setCaseStage(7);
				caseUpdateModel.setRecoveryAgainstDemand(0L);
				caseUpdateModel.setRecoveryByDRC3(0L);
				caseUpdateModel.setDemand(0L);
				fileName = "";
				caseStageArn = "";
				recoveryStageArnArray = "";
				recoveryStageArn = "";
			} else {
				orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
				timeStamp = dateFormat.format(new java.util.Date());
				fileName = timeStamp + "_" + orgFileName;
				byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(bytes);
				caseStageArn = caseUpdateModel.getCaseStageArn();
				if (caseUpdateModel.getRecoveryStageArn() != null) {
					recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
					recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
				} else {
					recoveryStageArn = "";
				}
			}
			int userId = userDetails.getUserId();
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			updatedReviewCase.setActionStatus(actionStatusRepository.findById(caseUpdateModel.getActionStatus()).get());
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setCaseStage(caseStageRepository.findById(caseUpdateModel.getCaseStage()).get());
			updatedReviewCase.setDemand(caseUpdateModel.getDemand());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryByDRC3(caseUpdateModel.getRecoveryByDRC3());
			updatedReviewCase.setCaseStageArn(caseStageArn);
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			updatedReviewCase.setCaseUpdateDate(new Date());
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			String action = "status_updated_a";
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String assignedTo = reviewCase.getAssignedTo();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo(assignedTo);
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			foReviewCase.setCaseStageArn(caseStageArn);
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(reviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(updatedReviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case updated successfully");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + "rejected_by_approver";
	}

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
	public List<String> getAllMappedLocationsFromUserRoleMapping(List<UserRoleMapping> userRoleMapList) {
		List<String> workingLocationsIds = new ArrayList<>();
		try {
			List<UserRoleMapping> stateId = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
					.collect(Collectors.toList());
			if (stateId != null && stateId.size() > 0) {
				workingLocationsIds.addAll(enforcementReviewCaseRepository.finAllLocationsByStateid());
				return workingLocationsIds;
			} else {
				List<UserRoleMapping> zoneIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
						.collect(Collectors.toList());
				List<UserRoleMapping> circleIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))
						.collect(Collectors.toList());
				if (!circleIds.isEmpty()) {
					for (UserRoleMapping circleIdsSolo : circleIds) {
						workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
					}
				}
				if (!zoneIds.isEmpty()) {
					List<String> onlyZoneIdsList = new ArrayList<String>();
					for (UserRoleMapping zoneIdsSolo : zoneIds) {
						workingLocationsIds.add(zoneIdsSolo.getZoneDetails().getZoneId());
						onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId());
					}
					List<String> allCirclesListUnderZones = enforcementReviewCaseRepository
							.findAllCirclesByZoneIds(onlyZoneIdsList);
					for (String circleSolo : allCirclesListUnderZones) {
						workingLocationsIds.add(circleSolo); // add Circles
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : getAllMappedLocationsFromUserRoleMapping : " + e.getMessage());
		}
		return workingLocationsIds;
	}
//	@GetMapping("/review_category_case")
//	public String reviewSummaryList(Model model) {
//
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//			return "redirect:/logout";
//		}
//
//		setFOMenu(model, ApplicationConstants.FO_REVIEW_CATEGORY_CASE);
//
//		try {
//
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//
//			List<String> locationId = getAllMappedLocationsFromUserRoleMapping(userDetails.getUserRoleMappings());
//
//			logger.info("location details : " + locationId.toString());
//
//			List<CategoryTotal> categoryList = new ArrayList<CategoryTotal>();
//
//			if (locationId.contains("HP")) {
//
//				categoryList = FOReviewSummeryList.getStateCategoryDetails();
//
//			} else {
//
//				categoryList = FOReviewSummeryList.getCategoryDetails(locationId);
//			}
//
//			model.addAttribute("categoryTotals", categoryList);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_REVIEW_CATEGORY_CASE;
//
//	}

	@GetMapping("/" + ApplicationConstants.FO_REVIEW_CATEGORY_CASE)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setFOMenu(model, ApplicationConstants.FO_REVIEW_CATEGORY_CASE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("FO").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("ABCD");
		model.addAttribute("categories", categoryListRepository
				.findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(locationList, exceptActionList));
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = mstParametersRepository
						.findAllAssessmentParameterByAssessmentCategoryAndLocationListAndExceptActionList(
								category.get().getName(), locationList, exceptActionList);
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& mstParametersRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
					model.addAttribute("categoryTotals", enforcementReviewCaseRepository
							.getAllCaseCountByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(
									category.get().getName(), parameterId.toString(), locationList, exceptActionList));
				} else {
					model.addAttribute("categoryTotals",
							enforcementReviewCaseRepository.getAllCaseCountByCategoryAndLocationListAndExceptActionList(
									category.get().getName(), locationList, exceptActionList));
				}
			}
		}
//		return "/" + ApplicationConstants.HQ + "/review_summary_list_new";
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_REVIEW_CATEGORY_CASE + "_new";
	}
//	@GetMapping("/view_list_of_case")
//	public String viewlistOfCase(Model model, @RequestParam(required = false) String category) {
//
//		try {
//
//			setFOMenu(model, ApplicationConstants.FO_REVIEW_CATEGORY_CASE);
//
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//			List<EnforcementReviewCase> enforcementReviewList = new ArrayList<EnforcementReviewCase>();
//
//			List<EnforcementReviewCase> enforcementReviewCase = new ArrayList<EnforcementReviewCase>();
//			;
//
//			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
//
//			List<String> locationId = getAllMappedLocationsFromUserRoleMapping(userDetails.getUserRoleMappings());
//
//			if (locationId.contains("HP")) {
//
//				enforcementReviewCase = enforcementReviewCaseRepository.enforcementReviewList(category);
//
//			} else {
//
//				enforcementReviewCase = enforcementReviewCaseRepository.getListBasedLocationAndCategory(locationId,
//						category);
//			}
//
//			for (EnforcementReviewCase e : enforcementReviewCase) {
//
//				e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
//
//				e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
//						e.getId().getPeriod(), e.getId().getCaseReportingDate()));
//
//				enforcementReviewList.add(e);
//			}
//
//			model.addAttribute("caseList", enforcementReviewList);
//
//			model.addAttribute("category", category);
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_VIEW_LIST_OF_CASE;
//
//	}

	@GetMapping("/view_list_of_case")
	public String reviewCasesList(Model model, @RequestParam(required = false) String category, String parameterId) {
		List<String[]> caseList = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		setFOMenu(model, ApplicationConstants.FO_REVIEW_CATEGORY_CASE);
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("FO").get());
		List<String> locationList = adminUpdateUserDetailsImpl.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("upload");
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
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_VIEW_LIST_OF_CASE + "_new";
	}

	@GetMapping("/get_case_stage_by_actionid")
	@ResponseBody
	public ResponseEntity<Map<Integer, String>> getCaseStageListByActionId(Model model, @RequestParam int actionId,
			@RequestParam(required = false) String auditCase) {
		Map<Integer, String> mapObj = new HashMap<Integer, String>();
		try {
			List<CaseStage> caseStge = caseStageRepository.findByActionId(actionId);
			for (CaseStage obj : caseStge) {
				if (auditCase != null && auditCase.equalsIgnoreCase("true") && actionId == 2) {
					if (obj.getName().equalsIgnoreCase("DRC01 issued")) {
						mapObj.put(obj.getId(), obj.getName());
					}
				} else {
					mapObj.put(obj.getId(), obj.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ResponseEntity.ok(mapObj);
	}

	@GetMapping("/downloadUploadedFile")
	public ResponseEntity<Resource> downloadUploadedFile(@RequestParam("fileName") String fileName) {
		try {
			String fileDirectoryPath = fileUploadLocation;
			Path Path = Paths.get(fileDirectoryPath, fileName);
			Resource resource = new FileSystemResource(Path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/downloadUploadedPdfFile")
	public ResponseEntity<Resource> downloadUploadedPdfFile(@RequestParam("fileName") String fileName) {
		try {
			String fileDirectoryPath = pdfFilePath;
			Path Path = Paths.get(fileDirectoryPath, fileName);
			Resource resource = new FileSystemResource(Path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	public String getGSTNRemarks(String gstin, Date caseReportingDate, String period) {
		String remarks = "";
		EnforcementCasesRemarksDetails enforcementCasesRemarksDetails = null;
		try {
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gstin);
			compositeKey.setCaseReportingDate(caseReportingDate);
			compositeKey.setPeriod(period);
			Boolean isPresent = reviewMeetingDetailsRepository.existsByGSTINAndCaseReportingDateAndPeriod(gstin,
					caseReportingDate, period);
			if (isPresent) {
				enforcementCasesRemarksDetails = reviewMeetingDetailsRepository
						.findTopByGSTINAndCaseReportingDateAndPeriodOrderByIdDesc(gstin, caseReportingDate, period);
				remarks = enforcementCasesRemarksDetails.getRemarks();
			} else {
				remarks = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return remarks;
	}

	public String getLocation(List<String> list) {
		String locationId = null;
		for (String str : list) {
			if (!str.equals("NA")) {
				locationId = str;
			}
		}
		return locationId;
	}

	private List<LocationDetails> getLocationDetails(List<String> list) {
		List<LocationDetails> locationId = new ArrayList<LocationDetails>();
		for (String str : list) {
			LocationDetails locationDetasils = locationDetailsRepository.findByLocationName(str).get();
			locationId.add(locationDetasils);
		}
		return locationId;
	}

	@PostMapping("/" + ApplicationConstants.FO_SAVE_CASES_WITH_MANUAL_PERIOND_INSERTION)
	public String saveOldCasesManual(Model model, @RequestParam(name = "gstinOldCases") String gstinOldCases,
			@RequestParam(name = "taxPayerNameOldCases") String taxPayerNameOldCases,
			@RequestParam(name = "workinglocationid") String workinglocationid,
			@RequestParam(name = "periodIndicativeJson") String periodIndicativeJson,
			@RequestParam(name = "reportingDateOldCases") String reportingDateOldCases,
			@RequestParam(name = "oldCaseId") String oldCaseId,
			@RequestParam(name = "jurisdictionOldCases") String jurisdictionOldCases) throws ParseException {
		// Create an ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		List<EnforcementReviewCase> oldCasesUploadList = new ArrayList<>();
		List<FoReviewCase> foReviewCasesList = new ArrayList<>();
		List<CaseWorkflow> caseWorkflowsList = new ArrayList<>();
		List<EnforcementReviewCaseAssignedUsers> enforcementReviewCaseAssignedUsersList = new ArrayList<>();
		UserDetails userDetails = new UserDetails();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (object.isPresent()) {
			userDetails = object.get();
		}
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = inputFormat.parse(reportingDateOldCases);
		String convertedDate = outputFormat.format(date);
		Date parsedReportingDate = outputFormat.parse(convertedDate);
		Optional<LocationDetails> locationDetails = locationDetailsRepository.findById(workinglocationid);
		Optional<OldCasesUploadManually> oldCasesUploadManuallySolo = oldCasesUploadManuallyRepository
				.findById(Long.parseLong(oldCaseId));
		oldCasesUploadManuallySolo.get().setUploaded(true);
		List<OldCasesUploadManually> objectList = oldCasesUploadManuallySolo.map(Collections::singletonList)
				.orElse(Collections.emptyList());
		oldCasesUploadManuallyRepository.saveAll(objectList);
		try {
			Map<Integer, DataObject> map = objectMapper.readValue(periodIndicativeJson,
					new TypeReference<Map<Integer, DataObject>>() {
					});
			for (Map.Entry<Integer, DataObject> entry : map.entrySet()) {
				CompositeKey compositeKey = new CompositeKey();
				EnforcementReviewCase enforcementReviewCaseSolo = new EnforcementReviewCase();
				FoReviewCase foReviewCaseSolo = new FoReviewCase();
				CaseWorkflow caseWorkflowSolo = new CaseWorkflow();
				EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsersSolo = new EnforcementReviewCaseAssignedUsers();
				// parsedReportingDate = dateFormat.parse(reportingDateOldCases);
				compositeKey.setGSTIN(gstinOldCases);
				compositeKey.setCaseReportingDate(parsedReportingDate);
				compositeKey.setPeriod(entry.getValue().getPeriod());
				/******************
				 * make list for enforcement_review_case_assigned_users table to save start
				 *****************/
				enforcementReviewCaseAssignedUsersSolo.setId(compositeKey);
				enforcementReviewCaseAssignedUsersSolo.setFoUserId(userDetails.getUserId());
				enforcementReviewCaseAssignedUsersList.add(enforcementReviewCaseAssignedUsersSolo);
				/******************
				 * make list for enforcement_review_case_assigned_users table to save end
				 *****************/
				/******************
				 * make list for enforcement_review_case table to save start
				 *****************/
				enforcementReviewCaseSolo.setId(compositeKey);
				enforcementReviewCaseSolo.setAction("acknowledge");
				enforcementReviewCaseSolo.setAssignedFrom("HQ");
				enforcementReviewCaseSolo.setAssignedFromUserId(userDetails.getUserId());
				enforcementReviewCaseSolo.setAssignedTo("FO");
				enforcementReviewCaseSolo.setCategory("Detailed Enforcement Old Cases");
				enforcementReviewCaseSolo.setIndicativeTaxValue(Long.parseLong(entry.getValue().getIndicativeTaxAmt()));
				enforcementReviewCaseSolo.setTaxpayerName(taxPayerNameOldCases);
				enforcementReviewCaseSolo.setLocationDetails(locationDetails.get());
				enforcementReviewCaseSolo.setCaseUpdateDate(new Date());
				oldCasesUploadList.add(enforcementReviewCaseSolo);
				/******************
				 * make list for enforcement_review_case table to save end
				 *****************/
				/************ make list for fo_review_case table to save start ***************/
				foReviewCaseSolo.setGSTIN(gstinOldCases);
				foReviewCaseSolo.setCaseReportingDate(parsedReportingDate);
				foReviewCaseSolo.setPeriod(entry.getValue().getPeriod());
				foReviewCaseSolo.setActionStatus(0);
				foReviewCaseSolo.setAssignedTo("FO");
				foReviewCaseSolo.setAssignedToUserId(userDetails.getUserId());
				foReviewCaseSolo.setCaseStage(0);
				foReviewCaseSolo.setCaseUpdatingDate(new Date());
				foReviewCaseSolo.setCategory("Detailed Enforcement Old Cases");
				foReviewCaseSolo.setCircle(jurisdictionOldCases);
				foReviewCaseSolo.setIndicativeTaxValue(Long.parseLong(entry.getValue().getIndicativeTaxAmt()));
				foReviewCaseSolo.setTaxpayerName(taxPayerNameOldCases);
				foReviewCaseSolo.setUserId(userDetails.getUserId());
				foReviewCaseSolo.setSuggestedJurisdiction(workinglocationid);
				foReviewCaseSolo.setAssignedFrom(enforcementReviewCaseSolo.getLocationDetails().getLocationName());
				foReviewCaseSolo.setCaseJurisdiction(enforcementReviewCaseSolo.getLocationDetails().getLocationName());
				if (workinglocationid != null && workinglocationid.length() > 0) {
					foReviewCaseSolo.setSuggestedJurisdictionName(
							locationDetailsRepository.findById(workinglocationid).get().getLocationName());
				}
				foReviewCasesList.add(foReviewCaseSolo);
				/************ make list for fo_review_case table to save end ***************/
				/************ make list for case_workflow table to save start ***************/
				caseWorkflowSolo.setGSTIN(gstinOldCases);
				caseWorkflowSolo.setCaseReportingDate(parsedReportingDate);
				caseWorkflowSolo.setPeriod(entry.getValue().getPeriod());
				caseWorkflowSolo.setAction("acknowledge");
				caseWorkflowSolo.setAssignedFrom("HQ");
				caseWorkflowSolo.setAssignedTo("FO");
				caseWorkflowSolo.setAssigntoUserId(userDetails.getUserId());
				caseWorkflowSolo.setUpdatingDate(new Date());
				caseWorkflowsList.add(caseWorkflowSolo);
				/************ make list for case_workflow table to save end ***************/
			}
			enforcementReviewCaseRepository.saveAll(oldCasesUploadList);
			enforcementReviewCaseAssignedUsersRepository.saveAll(enforcementReviewCaseAssignedUsersList);
			foUserCaseReviewRepository.saveAll(foReviewCasesList);
			caseWorkflowRepository.saveAll(caseWorkflowsList);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "redirect:" + "/" + ApplicationConstants.FO + "/"
				+ ApplicationConstants.FO_CASE_UPLOAD_WITH_MANUAL_PERIOND_INSERTION;
		return url;
	}

	@GetMapping("/" + ApplicationConstants.FO_CASE_UPLOAD_WITH_MANUAL_PERIOND_INSERTION)
	public String caseUploadWithManualPeriodInsertion(Model model) {
		UserDetails userDetails = new UserDetails();
		List<String> attachedWorkingLocations = new ArrayList<>();
		List<UserRoleMapping> roleMappedWithFo = new ArrayList<>();
		List<String> periods = new ArrayList<>();
		setFOMenu(model, ApplicationConstants.FO_CASE_UPLOAD_WITH_MANUAL_PERIOND_INSERTION);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				userDetails = object.get();
				roleMappedWithFo = userDetails.getUserRoleMappings().stream()
						.filter(userRoleMap -> userRoleMap.getUserRole().getId() == 2).collect(Collectors.toList());
			}
		}
		List<UserRoleMapping> circleIdsForFo = roleMappedWithFo.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())) // commented as per
																							// requirement verifier can
																							// not be assigned to at
																							// circle level.
				.collect(Collectors.toList());
		List<UserRoleMapping> zoneIdsForFo = roleMappedWithFo.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
		List<UserRoleMapping> stateIdForFo = roleMappedWithFo.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
		if (!circleIdsForFo.isEmpty()) {
			for (UserRoleMapping circleIdsForFoSolo : circleIdsForFo) {
				attachedWorkingLocations.add(circleIdsForFoSolo.getCircleDetails().getCircleId());
			}
		}
		if (!zoneIdsForFo.isEmpty()) {
			for (UserRoleMapping zoneIdsForFoSolo : zoneIdsForFo) {
				attachedWorkingLocations.add(zoneIdsForFoSolo.getZoneDetails().getZoneId());
			}
		}
		if (!stateIdForFo.isEmpty()) {
			for (UserRoleMapping stateIdForFoSolo : stateIdForFo) {
				attachedWorkingLocations.add(stateIdForFoSolo.getStateDetails().getStateId());
			}
		}
		/*
		 * List<String> location_ids = new ArrayList<String>(); location_ids.add("C08");
		 */
		List<OldCasesUploadManually> oldCasesUploadManuallyList = oldCasesUploadManuallyRepository
				.getOldCasesByLocationAndStatusUploadedOrNot(attachedWorkingLocations);
		// List<OldCasesUploadManually> oldCasesUploadManuallyList = new
		// ArrayList<OldCasesUploadManually>();
		model.addAttribute("oldCasesUploadManuallyList", oldCasesUploadManuallyList);
		periods.add("2017-18");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer year = calendar.get(Calendar.YEAR);
		LocalDate currentDate = LocalDate.now();
		// Check if the date is before April 1st
		if (currentDate.isBefore(LocalDate.of(currentDate.getYear(), 4, 1))) {
			year = year - 1;
		}
		for (int i = 2018; i <= year; i++) {
			periods.add(String.valueOf(i) + "-" + String.valueOf((i % 100) + 1));
		}
		model.addAttribute("periods", periods);
		return "/" + ApplicationConstants.FO + "/" + ApplicationConstants.FO_CASE_UPLOAD_WITH_MANUAL_PERIOND_INSERTION;
	}

	@GetMapping("/" + ApplicationConstants.FO_SELF_DETECTED_CASES)
	public String uploadSelfDetectedCasesForm(Model model) {
		try {
			setFOMenu(model, ApplicationConstants.FO_SELF_DETECTED_CASES);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			List<String> periods = new ArrayList<>();
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			LocalDate currentDate = LocalDate.now();
			// Check if the date is before April 1st
			if (currentDate.isBefore(LocalDate.of(currentDate.getYear(), 4, 1))) {
				year = year - 1;
			}
			periods.add("2017-18");
			for (int i = 2018; i <= year; i++) {
				periods.add(String.valueOf(i) + "-" + String.valueOf((i % 100) + 1));
			}
			model.addAttribute("periods", periods);
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> jurisdictions = new ArrayList<String>();
			List<UserRoleMapping> roleMapping = userRoleMappingRepository
					.findAllRolesMapWithFOUsers(userDetails.getUserId());
			for (UserRoleMapping urm : roleMapping) {
				jurisdictions.add(userDetailsServiceImpl.getLocationNameFromUserRoleMapping(urm));
			}
			model.addAttribute("jurisdictions", jurisdictions);
			model.addAttribute("categories", "Self Detected Cases");
			model.addAttribute("uploadSelfDetectedCaseForm", new SelfDetectedCase());
//			List<UploadSelfDetectedCaseRemark> remarks = uploadSelfDetectedCaseRemarkRepository
//					.findAllByActiveStatus(true);
			List<MstParametersModuleWise> remarks = mstParametersRepository.findAllByStatusSelfDetectedCases(true);
			if (remarks != null && remarks.size() > 0) {
				model.addAttribute("remarks", remarks);
			}
		} catch (Exception e) {
			logger.error("FieldUserController : uploadSelfDetectedCasesForm : " + e.getMessage());
		}
		return "/" + ApplicationConstants.FO + "/" + ApplicationConstants.FO_SELF_DETECTED_CASES;
	}

	@PostMapping("/" + ApplicationConstants.FO_SELF_DETECTED_CASES)
	public String uploadSelfDetectedCases(@ModelAttribute("selfDetectedCase") SelfDetectedCase selfDetectedCase,
			Model model, RedirectAttributes redirectAttributes) {
		Map<String, List<List<String>>> formDataValidationMap = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		formDataValidationMap = excelValidator.validateExcelAndExtractData(selfDetectedCase);
		if (formDataValidationMap.get("uploadData") != null) {
			redirectAttributes.addFlashAttribute("uploadData", formDataValidationMap.get("uploadData"));
		}
		if (formDataValidationMap.get("errorList") != null) {
			redirectAttributes.addFlashAttribute("errorList", formDataValidationMap.get("errorList").get(0));
			return "redirect:/" + ApplicationConstants.FO + "/" + ApplicationConstants.FO_SELF_DETECTED_CASES;
		}
		String uploadDataFlag = "";
		if (!(formDataValidationMap.get("errorList") != null)) {
			uploadDataFlag = hqUserUploadService.saveHqUserUploadDataList(userDetails.getUserId(),
					formDataValidationMap.get("uploadData"), selfDetectedCase.getCategory());
		}
		if (uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
			redirectAttributes.addFlashAttribute("successMessage", ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
		} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
			redirectAttributes.addFlashAttribute("successMessage", "Record already exists!");
		}
		return "redirect:/" + ApplicationConstants.FO + "/" + ApplicationConstants.FO_SELF_DETECTED_CASES;
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setFOMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}

	@PostMapping("/update_caseid")
	public String updateCaseId(Model model, CaseidUpdationModel caseidUpdationModel, RedirectAttributes redirectAttrs)
			throws ParseException, IOException {
		try {
			setFOMenu(model, ApplicationConstants.FO_UPDATE_SUMMARY_LIST);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			String fileName = null;
			String gstn = caseidUpdationModel.getGstnocaseid();
			String date = caseidUpdationModel.getDatecaseid();
			Date parsedDate = dateFormat.parse(date);
			String period = caseidUpdationModel.getPeriodcaseid();
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gstn);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			String oldCaseId = enforcementReviewCase.getCaseId();
			String caseId = caseidUpdationModel.getCaseid();
			Integer remarks = caseidUpdationModel.getRemarks();
			String otherRemarks = caseidUpdationModel.getOtherRemarks();
			String orginalFileName = caseidUpdationModel.getFilePath().getOriginalFilename();
			MultipartFile multipartFile = caseidUpdationModel.getFilePath();
			byte[] byteary = multipartFile.getBytes();
			String timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orginalFileName;
			File file = new File(fileUploadLocation + fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(byteary);
			CaseidUpdation caseidUpdation = new CaseidUpdation();
			caseidUpdation.setAssignedTo("RU");
			caseidUpdation.setAssignedFrom("FO");
			caseidUpdation.setFilePath(fileName);
			caseidUpdation.setGSTIN(gstn);
			caseidUpdation.setCaseReportingDate(parsedDate);
			caseidUpdation.setPeriod(period);
			caseidUpdation.setAssignedFromUserId(userId);
			caseidUpdation.setAssigntoUserId(0);
			caseidUpdation.setJurisdiction(enforcementReviewCase.getLocationDetails().getLocationId());
			caseidUpdation.setUpdatingDate(new Date());
			caseidUpdation.setRemarks(remarks);
			caseidUpdation.setOtherRemarks(otherRemarks);
			caseidUpdation.setOldCaseid(oldCaseId);
			caseidUpdation.setCaseid(caseId);
			caseidUpdation.setStatus(foUpdated);
			caseIdUpdationRepository.save(caseidUpdation);
			enforcementReviewCase.setCaseidUpdationStatus(foUpdated);
			enforcementReviewCaseRepository.save(enforcementReviewCase);
			redirectAttrs.addFlashAttribute("message", "Request for case id updation sent successfully");
			/******
			 * insert notifications when fo send request to ru for case id updation start
			 *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"RU", enforcementReviewCase.getLocationDetails(), "caseIdUpdateRequestByFo", 0);
			/******
			 * insert notifications when fo send request to ru for case id updation start
			 *****/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.FO + "/update_summary_list";
	}

	@PostMapping("/update_raisedquery_caseid")
	public String updateRaisedQueryCaseId(Model model, CaseidUpdationModel caseidUpdationModel,
			RedirectAttributes redirectAttrs) throws ParseException, IOException {
		try {
			setFOMenu(model, ApplicationConstants.FO_UPDATE_SUMMARY_LIST);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			String fileName = null;
			String gstn = caseidUpdationModel.getGstnocaseid();
			String date = caseidUpdationModel.getDatecaseid();
			Date parsedDate = dateFormat.parse(date);
			String period = caseidUpdationModel.getPeriodcaseid();
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gstn);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			String oldCaseId = enforcementReviewCase.getCaseId();
			String caseId = caseidUpdationModel.getCaseid();
			Integer remarks = caseidUpdationModel.getRemarks();
			String otherRemarks = caseidUpdationModel.getOtherRemarks();
			String orginalFileName = caseidUpdationModel.getFilePath().getOriginalFilename();
			MultipartFile multipartFile = caseidUpdationModel.getFilePath();
			byte[] byteary = multipartFile.getBytes();
			String timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orginalFileName;
			File file = new File(fileUploadLocation + fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(byteary);
			CaseidUpdation caseidUpdation = new CaseidUpdation();
			caseidUpdation.setAssignedTo("RU");
			caseidUpdation.setAssignedFrom("FO");
			caseidUpdation.setFilePath(fileName);
			caseidUpdation.setGSTIN(gstn);
			caseidUpdation.setCaseReportingDate(parsedDate);
			caseidUpdation.setPeriod(period);
			caseidUpdation.setAssignedFromUserId(userId);
			caseidUpdation.setAssigntoUserId(0);
			caseidUpdation.setJurisdiction(enforcementReviewCase.getLocationDetails().getLocationId());
			caseidUpdation.setUpdatingDate(new Date());
			caseidUpdation.setRemarks(remarks);
			caseidUpdation.setOtherRemarks(otherRemarks);
			caseidUpdation.setOldCaseid(oldCaseId);
			caseidUpdation.setCaseid(caseId);
			caseidUpdation.setStatus(foUpdated);
			caseIdUpdationRepository.save(caseidUpdation);
			enforcementReviewCase.setCaseidUpdationStatus(foUpdated);
			enforcementReviewCaseRepository.save(enforcementReviewCase);
			redirectAttrs.addFlashAttribute("message", "Request for case id updation sent successfully");
			/******
			 * insert notifications when fo send request to ru for case id updation start
			 *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"RU", enforcementReviewCase.getLocationDetails(), "caseIdUpdateRequestByFo", 0);
			/******
			 * insert notifications when fo send request to ru for case id updation start
			 *****/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.FO + "/query_raised_by_verifier";
	}

	@PostMapping("/update_rejected_caseid")
	public String updateRejectedCaseId(Model model, CaseidUpdationModel caseidUpdationModel,
			RedirectAttributes redirectAttrs) throws ParseException, IOException {
		try {
			setFOMenu(model, ApplicationConstants.FO_UPDATE_SUMMARY_LIST);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			String fileName = null;
			String gstn = caseidUpdationModel.getGstnocaseid();
			String date = caseidUpdationModel.getDatecaseid();
			Date parsedDate = dateFormat.parse(date);
			String period = caseidUpdationModel.getPeriodcaseid();
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gstn);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			String oldCaseId = enforcementReviewCase.getCaseId();
			String caseId = caseidUpdationModel.getCaseid();
			Integer remarks = caseidUpdationModel.getRemarks();
			String otherRemarks = caseidUpdationModel.getOtherRemarks();
			String orginalFileName = caseidUpdationModel.getFilePath().getOriginalFilename();
			MultipartFile multipartFile = caseidUpdationModel.getFilePath();
			byte[] byteary = multipartFile.getBytes();
			String timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orginalFileName;
			File file = new File(fileUploadLocation + fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(byteary);
			CaseidUpdation caseidUpdation = new CaseidUpdation();
			caseidUpdation.setAssignedTo("RU");
			caseidUpdation.setAssignedFrom("FO");
			caseidUpdation.setFilePath(fileName);
			caseidUpdation.setGSTIN(gstn);
			caseidUpdation.setCaseReportingDate(parsedDate);
			caseidUpdation.setPeriod(period);
			caseidUpdation.setAssignedFromUserId(userId);
			caseidUpdation.setAssigntoUserId(0);
			caseidUpdation.setJurisdiction(enforcementReviewCase.getLocationDetails().getLocationId());
			caseidUpdation.setUpdatingDate(new Date());
			caseidUpdation.setRemarks(remarks);
			caseidUpdation.setOtherRemarks(otherRemarks);
			caseidUpdation.setOldCaseid(oldCaseId);
			caseidUpdation.setCaseid(caseId);
			caseidUpdation.setStatus(foUpdated);
			caseIdUpdationRepository.save(caseidUpdation);
			enforcementReviewCase.setCaseidUpdationStatus(foUpdated);
			enforcementReviewCaseRepository.save(enforcementReviewCase);
			redirectAttrs.addFlashAttribute("message", "Request for case id updation sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/" + ApplicationConstants.FO + "/rejected_by_approver";
	}

	@GetMapping("/" + ApplicationConstants.FO_UPDATE_RECOVERY_STATUS)
	public String updateRecoveryStrage(Model model) {
		setFOMenu(model, ApplicationConstants.FO_UPDATE_RECOVERY_STATUS);
		model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_UPDATE_RECOVERY_STATUS;
	}

	@GetMapping("/update_summary_recovery_data_list/{id}")
	public String getUpdateSummaryRecoveryDataList(Model model, @PathVariable Long id) {
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			List<String> locationId = getLocationDetails(userId);
			Category category = categoryListRepository.findById(id).get();
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<EnforcementReviewCase> list = hqUserUploadDataRepository.findByCategorywiseRecoveryList(locationId,
					categoryName);
			for (EnforcementReviewCase obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(obj.getId().getGSTIN());
				compositeKey.setCaseReportingDate(obj.getId().getCaseReportingDate());
				compositeKey.setPeriod(obj.getId().getPeriod());
				String uploadedFileName = null;
				try {
					ExtensionNoDocument extensionNoDocument = obj.getExtensionNoDocument();
					uploadedFileName = extensionNoDocument.getExtensionFileName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				actionName = actionStatusRepository.findById(actionStatus).get().getName();
				caseStageName = caseStageRepository.findById(caseStage).get().getName();
				recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setUploadedFileName(uploadedFileName);
				object.setParameter(getParameterName(obj.getParameter()));
				listofCases.add(object);
			}
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/update_summary_recovery_list_data";
	}

	@GetMapping("/view_recovery_case/id")
	public String getCaseRecoveryData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setDate(reportingDate);
			String caseId = enforcementReviewCase.getCaseId();
			object.setCaseId(caseId);
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
			submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
			submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
			submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			String fileName = foUserCaseReviewRepository.getFileNameByFO(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedData.setSum("filenotexist");
			} else {
				submittedData.setSum("fileexist");
			}
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("submittedData", submittedData);
			model.addAttribute("viewItem", object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/view_recovery_case";
	}

	@PostMapping("/fo_update_recovery_cases")
	@Transactional
	public String uploadRecoveryCase(Model model,
			@ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getFieldError());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseId = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			int userId = userDetails.getUserId();
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			String action = "foRecoveryUpdated";
			String closeAction = "foRecoveryClose";
			int recoveryStageId = caseUpdateModel.getRecoveryStage();
			orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
			timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orgFileName;
			byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
			File file = new File(fileUploadLocation + fileName);
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
			if (caseUpdateModel.getRecoveryStageArn() != null) {
				recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
				recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
			}
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			if (recoveryStageId == 3) {
				updatedReviewCase.setRecoveryStatus(closeAction);
				updatedReviewCase.setFullRecoveryDate(new Date());
			} else {
				updatedReviewCase.setRecoveryStatus(action);
			}
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(reviewCase.getId().getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			if (recoveryStageId == 3) {
				foReviewCase.setAction(closeAction);
			} else {
				foReviewCase.setAction(action);
			}
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo("FO");
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseId(reviewCase.getCaseId());
			foReviewCase.setCaseStageArn(reviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(userId);
			foReviewCase.setAssignedFrom(reviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(reviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case updated successfully");
			if (recoveryStageId == 3) {
				redirectAttrs.addFlashAttribute("message", "Case closed successfully");
			} else {
				redirectAttrs.addFlashAttribute("message", "Case updated successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FO_UPDATE_RECOVERY_STATUS;
	}

	@GetMapping("/" + ApplicationConstants.FO_RECOVERY_REVERTED_BY_APPROVER)
	public String recoveryRevertedByApprover(Model model) {
		setFOMenu(model, ApplicationConstants.FO_RECOVERY_REVERTED_BY_APPROVER);
		model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
		return ApplicationConstants.FO + "/" + ApplicationConstants.FO_RECOVERY_REVERTED_BY_APPROVER;
	}

	@GetMapping("/update_reverted_approver_recovery_data_list/{id}")
	public String approverRevertedCaseDataList(Model model, @PathVariable Long id) {
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			List<String> locationId = getLocationDetails(userId);
			Category category = categoryListRepository.findById(id).get();
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<EnforcementReviewCase> list = hqUserUploadDataRepository.findRevertedByApproverRecoveryList(locationId,
					categoryName);
			for (EnforcementReviewCase obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String remarks = foUserCaseReviewRepository.getRaiseQueryRemarks(gst, date, period);
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(obj.getId().getGSTIN());
				compositeKey.setCaseReportingDate(obj.getId().getCaseReportingDate());
				compositeKey.setPeriod(obj.getId().getPeriod());
				actionName = actionStatusRepository.findById(actionStatus).get().getName();
				caseStageName = caseStageRepository.findById(caseStage).get().getName();
				recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setRemarks(remarks);
				object.setParameter(getParameterName(obj.getParameter()));
				listofCases.add(object);
			}
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/update_reverted_approver_recovery_list_data";
	}

	@GetMapping("/view_raise_query_recovery_case/id")
	public String getCaseRaiseQueryRecoveryData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setDate(reportingDate);
			String caseId = enforcementReviewCase.getCaseId();
			object.setCaseId(caseId);
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
			submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
			submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
			submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			String fileName = "";
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("submittedData", submittedData);
			model.addAttribute("viewItem", object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/view_raise_query_recovery_case";
	}

	@PostMapping("/fo_update_raised_query_recovery_cases")
	@Transactional
	public String uploadRaisedQueryRecoveryCase(Model model,
			@ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getFieldError());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseId = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			int userId = userDetails.getUserId();
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			String closeAction = "foRecoveryClose";
			orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
			timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orgFileName;
			byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
			File file = new File(fileUploadLocation + fileName);
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
			if (caseUpdateModel.getRecoveryStageArn() != null) {
				recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
				recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
			}
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			updatedReviewCase.setRecoveryStatus(closeAction);
			updatedReviewCase.setFullRecoveryDate(new Date());
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(reviewCase.getId().getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(closeAction);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo("AP");
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseId(reviewCase.getCaseId());
			foReviewCase.setCaseStageArn(reviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(0);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(userId);
			foReviewCase.setAssignedFrom(reviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(reviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case closed successfully");
			redirectAttrs.addFlashAttribute("message", "Case closed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FO_RECOVERY_REVERTED_BY_APPROVER;
	}

	public List<String> getLocationDetails(int userId) {
		List<String> locationId = new ArrayList<String>();
		List<UserRoleMapping> roleMapping = userRoleMappingRepository.findAllRolesMapWithFOUsers(userId);
		for (UserRoleMapping userRoleMapping : roleMapping) {
			String location = null;
			List<String> list = new ArrayList<String>();
			list.add(userRoleMapping.getStateDetails().getStateId());
			list.add(userRoleMapping.getZoneDetails().getZoneId());
			list.add(userRoleMapping.getCircleDetails().getCircleId());
			location = getLocation(list);
			locationId.add(location);
		}
		return locationId;
	}

	public String getRejectRemarks(String gstn, String period, Date caseReporingDate) {
		String remarks = null;
		CaseWorkflow caseWorkflow = new CaseWorkflow();
		caseWorkflow = caseWorkflowRepository.getTransferRemarks(gstn, period, caseReporingDate);
		if (caseWorkflow != null) {
			remarks = caseWorkflow.getOtherRemarks();
		} else {
			remarks = "";
		}
		return remarks;
	}

	private void setFOMenu(Model model, String activeMenu) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "FO");
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "FO");
				model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
				model.addAttribute("unReadNotificationList", unReadNotificationList);
				// model.addAttribute("loginedUserNotificationListCount",loginedUserNotificationList.size());
				model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
				model.addAttribute("convertUnreadToReadNotifications", "/fo/convert_unread_to_read_notifications");
				/*********** Get Notifications End *************/
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
				model.addAttribute("activeRole", "FO");
				model.addAttribute("homePageLink", "/fo/dashboard");
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
				List<String> roleNameList = uniqueRolesList.stream().map(mapping -> mapping.getUserRole().getRoleName())
						.collect(Collectors.toList());
				String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));
				String LocationsName = returnLocationsName(userRoleMappings);
				userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
				userProfileDetails.setAssignedWorkingLocations(LocationsName);
				model.addAttribute("userProfileDetails", userProfileDetails);
				model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
				/*************************
				 * to display user generic details end
				 ****************************/
			}
		}
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.FO));
		System.out
				.println(appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.FO));
		model.addAttribute("activeMenu", activeMenu);
	}

	/*************** return working location start *************************/
	private List<String> returnWorkingLocation(Integer currentUserId) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId,
				userRoleRepository.findByroleCode("FO").get().getId());
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
			List<UserRoleMapping> circleIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))
					.collect(Collectors.toList());
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
			if (!circleIds.isEmpty()) {
				for (UserRoleMapping circleIdsSolo : circleIds) {
					workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
				}
			}
		}
		return workingLocationsIds;
	}

	/*************** return working location end *************************/
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
	public String[] getArnArray(String[] ary) {
		String[] strArray = null;
		ArrayList<String> arrayListStr = new ArrayList<String>();
		for (String str : ary) {
			if (str != null) {
				arrayListStr.add(str);
			}
		}
		strArray = (String[]) arrayListStr.toArray();
		return strArray;
	}

	public static String getStringArn(List<String> list) {
		String arn = null;
		ArrayList<String> arrayListStr = new ArrayList<String>();
		for (String str : list) {
			if (str != null) {
				arrayListStr.add(str);
			}
		}
		arn = arrayListStr.toString();
		return arn;
	}

	public Boolean isRoleDistrict(List<String> location) {
		Boolean hasDistrictRole = false;
		for (String loc : location) {
			if (loc.startsWith("D") || loc.startsWith("Z") || loc.startsWith("E")) {
				hasDistrictRole = true;
				break;
			}
		}
		return hasDistrictRole;
	}

	@PostMapping("/fo_update_scrutiny_cases")
	@Transactional
	public String updateScrutinyCase(Model model,
			@ModelAttribute("caseUpdateForm") EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getFieldError());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		String caseId = null;
		String caseStageArn = null;
		String recoveryStageArnArray = null;
		String recoveryStageArn = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			String currentDateStr = dateFormat.format(date);
			Date parsedCurrentDate = dateFormat.parse(currentDateStr);
			int actionStatusId = caseUpdateModel.getActionStatus();
			if (actionStatusId == 1) {
				caseUpdateModel.setRecoveryStage(4);
				caseUpdateModel.setCaseStage(7);
				caseUpdateModel.setRecoveryAgainstDemand(0L);
				caseUpdateModel.setRecoveryByDRC3(0L);
				caseUpdateModel.setDemand(0L);
				fileName = "";
				caseStageArn = "";
				recoveryStageArnArray = "";
				recoveryStageArn = "";
			} else {
				orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
				timeStamp = dateFormat.format(new java.util.Date());
				fileName = timeStamp + "_" + orgFileName;
				byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(bytes);
				caseStageArn = caseUpdateModel.getCaseStageArn();
				if (caseUpdateModel.getRecoveryStageArn() != null) {
					recoveryStageArnArray = getStringArn(caseUpdateModel.getRecoveryStageArn());
					recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
				} else {
					recoveryStageArn = "";
				}
			}
			int userId = userDetails.getUserId();
			CompositeKey compositKey = new CompositeKey();
			String gstn = caseUpdateModel.getGSTIN_ID();
			String period = caseUpdateModel.getPeriod_ID();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			caseId = caseUpdateModel.getCaseId();
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			compositKey.setGSTIN(gstn);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
					.findById(compositKey).get();
			enforcementReviewCaseAssignedUsers.setFoUserId(userId);
			enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
			EnforcementReviewCase updatedReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			String caseIdStr = updatedReviewCase.getCaseId();
			if (actionStatusId != 1) {
				if (caseIdStr == null) {
					updatedReviewCase.setCaseId(caseId);
				} else {
					updatedReviewCase.setCaseId(updatedReviewCase.getCaseId());
				}
			}
			updatedReviewCase.setActionStatus(actionStatusRepository.findById(caseUpdateModel.getActionStatus()).get());
			if (caseUpdateModel.getRecoveryStage() == 0) {
				caseUpdateModel.setRecoveryStage(4);
			}
			updatedReviewCase
					.setRecoveryStage(recoveryStageRepository.findById(caseUpdateModel.getRecoveryStage()).get());
			updatedReviewCase.setCaseStage(caseStageRepository.findById(caseUpdateModel.getCaseStage()).get());
			updatedReviewCase.setDemand(caseUpdateModel.getDemand());
			updatedReviewCase.setRecoveryAgainstDemand(caseUpdateModel.getRecoveryAgainstDemand());
			updatedReviewCase.setRecoveryByDRC3(caseUpdateModel.getRecoveryByDRC3());
			updatedReviewCase.setCaseStageArn(caseStageArn);
			updatedReviewCase.setRecoveryStageArn(recoveryStageArn);
			updatedReviewCase.setCaseidUpdationStatus("scrutinyCaseIdUpdated");
			hqUserUploadDataRepository.save(updatedReviewCase);
			EnforcementReviewCase reviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			FoReviewCase foReviewCase = new FoReviewCase();
			String action = "status_updated";
			ActionStatus actionStatus = reviewCase.getActionStatus();
			RecoveryStage recoveryStage = reviewCase.getRecoveryStage();
			CaseStage caseStage = reviewCase.getCaseStage();
			String assignedTo = reviewCase.getAssignedTo();
			String category = reviewCase.getCategory();
			Long indicativeTaxValue = reviewCase.getIndicativeTaxValue();
			Long demand = reviewCase.getDemand();
			Long recoveryByDRC3 = reviewCase.getRecoveryByDRC3();
			Long recoveryAgainstDemand = reviewCase.getRecoveryAgainstDemand();
			String taxPayerName = reviewCase.getTaxpayerName();
			String circle = reviewCase.getLocationDetails().getLocationName();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo(assignedTo);
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setFoFilepath(fileName);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			if (caseIdStr == null) {
				foReviewCase.setCaseId(caseId);
			} else {
				foReviewCase.setCaseId(updatedReviewCase.getCaseId());
			}
			foReviewCase.setCaseStageArn(caseStageArn);
			foReviewCase.setRecoveryStageArn(recoveryStageArn);
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			// foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionNo());
			// // scrutiny case does not contain extension file in assesment module when
			// refer from scrutiny to assesment
			foReviewCase.setAssignedFromUserId(reviewCase.getAssignedFromUserId());
			foReviewCase.setAssignedFrom(reviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCaseJurisdiction(reviewCase.getLocationDetails().getLocationName());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case updated successfully");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FO_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/" + ApplicationConstants.FO_GET_SCRUTINY_DATA_LIST)
	public String getScrutinyDataList(Model model, @RequestParam Long id,
			@RequestParam(required = false) Integer parameterId) {
		String actionStatusName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			Category category = null;
			Optional<Category> objCategory = null;
			if (id != null) {
				objCategory = categoryListRepository.findById(id);
				if (objCategory.isPresent()) {
					category = objCategory.get();
					model.addAttribute("categoryId", id);// sending the category id
					List<MstParametersModuleWise> parameters = mstParametersRepository
							.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndDefaultFOUserId(
									objCategory.get().getName(), "recommendedForAssesAndAdjudication",
									userDetails.getUserId());
					model.addAttribute("parameters", parameters);
				}
			}
			model.addAttribute("parameterId", parameterId);
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
			/*
			 * List<EnforcementReviewCase> list = hqUserUploadDataRepository
			 * .findByCategoryAndActionAndUserIdList(categoryName, actionAcknowledge,
			 * userIdList);
			 */
			List<String> workingLocation = returnWorkingLocation(userId);
			// List<EnforcementReviewCase> list =
			// hqUserUploadDataRepository.getScrutinyCasesList(workingLocation);
			List<EnforcementReviewCase> list = null;
			if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
				model.addAttribute("parameterId", parameterId);
				Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAndDefaultUserIdAnd1stParameter(
						categoryName, "recommendedForAssesAndAdjudication", userIdList, parameterId.toString());
			} else {
				list = hqUserUploadDataRepository.findByCategoryAndActionAndUserIdListAndDefaultUserId(categoryName,
						"recommendedForAssesAndAdjudication", userIdList);
			}
			for (EnforcementReviewCase obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = "";
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				actionStatus = (obj.getActionStatus() != null ? obj.getActionStatus().getId() : 0);
				recoveryStage = (obj.getRecoveryStage() != null ? obj.getRecoveryStage().getId() : 0);
				caseStage = (obj.getCaseStage() != null ? obj.getCaseStage().getId() : 0);
				Date date = obj.getId().getCaseReportingDate();
				String caseId = obj.getCaseId();
				String caseStageARN = obj.getCaseStageArn();
				String recoveryStageARN = obj.getRecoveryStageArn();
				String caseidUpdateStatus = obj.getCaseidUpdationStatus();
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(obj.getId().getGSTIN());
				compositeKey.setCaseReportingDate(obj.getId().getCaseReportingDate());
				compositeKey.setPeriod(obj.getId().getPeriod());
				String uploadedFileName = null;
				try {
					/*
					 * ExtensionNoDocument extensionNoDocument = obj.getExtensionNoDocument();
					 * 
					 * uploadedFileName = extensionNoDocument.getPdfFileName();
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*
				 * if(id > 0 && id <= 6) {
				 * 
				 * EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise =
				 * reviewMeetingRepository.findTopByCategoryIdOrderByIdDesc(id);
				 * 
				 * if(enforcementCasesRemarksCategoryWise != null) {
				 * 
				 * Date reviewMeetingDate =
				 * enforcementCasesRemarksCategoryWise.getRecordCreationDate();
				 * 
				 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				 * 
				 * Date CaseReportingDate = dateFormat.parse(caseReportingDate);
				 * 
				 * if(reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
				 * 
				 * remarks = enforcementCasesRemarksCategoryWise.getRemarks();
				 * 
				 * }else {
				 * 
				 * remarks = ""; }
				 * 
				 * }
				 * 
				 * }else {
				 * 
				 * remarks = getGSTNRemarks(obj.getId().getGSTIN(),
				 * obj.getId().getCaseReportingDate(), obj.getId().getPeriod()); }
				 */
				/*
				 * if (actionStatus > 0 & caseStage > 0 & recoveryStage > 0) {
				 * 
				 * actionName = actionStatusRepository.findById(actionStatus).get().getName();
				 * 
				 * caseStageName = caseStageRepository.findById(caseStage).get().getName();
				 * 
				 * recoveryStageName =
				 * recoveryStageRepository.findById(recoveryStage).get().getName(); } else {
				 * 
				 * actionName = ""; caseStageName = ""; recoveryStageName = ""; }
				 */
				recoveryStageName = recoveryStageRepository.findById(recoveryStage).get().getName();
				actionStatusName = actionStatus == 0 ? ""
						: actionStatusRepository.findById(obj.getActionStatus().getId()).get().getName();
				caseStageName = caseStage == 0 ? ""
						: caseStageRepository.findById(obj.getCaseStage().getId()).get().getName();
				uploadedFileName = scrutinyCaseWorkflowRepository.getFilePathWhenSendForAssesmentAndAdjudication(
						compositeKey.getGSTIN(), compositeKey.getCaseReportingDate(), compositeKey.getPeriod());
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setActionStatus(actionStatus);
				object.setCaseStage(caseStage);
				object.setRecoveryStage(recoveryStage);
				object.setRecoveryAgainstDemand(obj.getRecoveryAgainstDemand());
				object.setRecoveryByDRC3(obj.getRecoveryByDRC3());
				object.setDemand(obj.getDemand());
				object.setActionStatusName(actionStatusName);
				object.setRecoveryStageName(recoveryStageName);
				object.setCaseStageName(caseStageName);
				object.setCaseId(caseId);
				object.setCaseStageArn(caseStageARN);
				object.setRecoveryStageArnStr(recoveryStageARN);
				object.setDate(date);
				object.setRemarks(remarks);
				object.setUploadedFileName(uploadedFileName);
				object.setParameter(getParameterName(obj.getParameter()));
				if (caseidUpdateStatus == null) {
					object.setCaseIdUpdate("no");
				} else {
					object.setCaseIdUpdate("yes");
				}
				/*
				 * if(caseId != null) {
				 * 
				 * if (caseidUpdateStatus == null) {
				 * 
				 * object.setCaseIdUpdate("yes");
				 * 
				 * }else {
				 * 
				 * if(caseidUpdateStatus.equals("foUpdated")) {
				 * 
				 * object.setCaseIdUpdate("");
				 * 
				 * }else if(caseidUpdateStatus.equals("verifyerApprove")) {
				 * 
				 * object.setCaseIdUpdate("yes");
				 * 
				 * }else if(caseidUpdateStatus.equals("verifyerReject")) {
				 * 
				 * object.setCaseIdUpdate("yes");
				 * 
				 * }else if(caseidUpdateStatus.equals("")) {
				 * 
				 * object.setCaseIdUpdate("yes"); } }
				 * 
				 * }else {
				 * 
				 * object.setCaseIdUpdate(""); }
				 */
				listofCases.add(object);
			}
			List<CaseIdUpdationRemarks> caseIdUpdationRemarks = caseIdUpdationRemarksRepository
					.findAllByOrderByIdDesc();
			model.addAttribute("listofCases", listofCases);
			model.addAttribute("listRemarks", caseIdUpdationRemarks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/scrutiny_cases_list";
	}

	@GetMapping("/update_pop_up_view/id")
	public String getUpdatePopUpData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<ActionStatus> obj1 = new ArrayList<ActionStatus>();
		List<CaseStage> obj2 = new ArrayList<CaseStage>();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			String remarks = "";
			Long id = 0L;
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<ActionStatus> listActionStatus = actionStatusRepository.findAll();
			List<CaseStage> listCaseStage = caseStageRepository.findAll();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			id = categoryListRepository.findByName(catecategory).getId();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			if (id > 0 && id <= 6) {
				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
						.findTopByCategoryIdOrderByIdDesc(id);
				if (enforcementCasesRemarksCategoryWise != null) {
					Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
					Date CaseReportingDate = caseReportingDate;
					if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
						remarks = enforcementCasesRemarksCategoryWise.getRemarks();
					} else {
						remarks = "";
					}
				}
			} else {
				remarks = getGSTNRemarks(enforcementReviewCase.getId().getGSTIN(),
						enforcementReviewCase.getId().getCaseReportingDate(),
						enforcementReviewCase.getId().getPeriod());
			}
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setDate(reportingDate);
			object.setRemarks(remarks);
			String caseId = enforcementReviewCase.getCaseId();
			String caseIdUpdationStatus = enforcementReviewCase.getCaseidUpdationStatus();
			if (caseId == null) {
				object.setCaseIdUpdated("no");
			} else {
				if (caseIdUpdationStatus != null) {
					object.setCaseIdUpdated("yes");
				} else {
					object.setCaseIdUpdated("no");
				}
			}
			object.setCaseId(caseId);
			for (ActionStatus actionStatus : listActionStatus) {
				obj1.add(actionStatus);
			}
			for (CaseStage caseStage : listCaseStage) {
				obj2.add(caseStage);
			}
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			if (enforcementReviewCase.getActionStatus() == null) {
				submittedData.setActionStatusName("");
				submittedData.setActionStatus(0);
			} else {
				submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
				submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			}
			if (enforcementReviewCase.getCaseStage() == null) {
				submittedData.setCaseStageName("");
				submittedData.setCaseStage(0);
			} else {
				submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
				submittedData.setCaseStage(enforcementReviewCase.getCaseStage().getId());
			}
			if (enforcementReviewCase.getCaseStageArn() == null) {
				submittedData.setCaseStageArn("");
			} else {
				submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			}
			if (enforcementReviewCase.getRecoveryStage() == null) {
				submittedData.setRecoveryStageName("");
				submittedData.setRecoveryStage(0);
			} else {
				submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
				submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			}
			if (enforcementReviewCase.getRecoveryStageArn() == null) {
				submittedData.setRecoveryStageArnStr("");
			} else {
				submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			}
			String fileName = foUserCaseReviewRepository.getFileNameUploadedByFO(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedData.setSum("filenotexist");
			} else {
				submittedData.setSum("fileexist");
			}
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listActionStatus", obj1);
			model.addAttribute("listCaseStage", obj2);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("submittedData", submittedData);
			model.addAttribute("viewItem", object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/update_pop_up";
	}

	public String getParameterName(String parameterId) {
		if (parameterId == null || parameterId.length() == 0) {
			return "Not Available";
		}
		String parameter = parameterId.replaceAll("^,|,$", "");
		String[] parameterList = parameter.split(",");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < parameterList.length; i++) {
			String parameterName = mstParametersRepository.findById(Integer.parseInt(parameterList[i])).get()
					.getParamName();
			stringBuilder.append(parameterName);
			if (i != parameterList.length - 1) {
				stringBuilder.append(",");
			}
		}
		return stringBuilder.toString();
	}

	@GetMapping("/scrutiny_update_pop_up_view/id")
	public String getScrutinyUpdatePopUpData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period) {
		EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
		EnforcementReviewCaseModel submittedData = new EnforcementReviewCaseModel();
		List<ActionStatus> obj1 = new ArrayList<ActionStatus>();
		List<CaseStage> obj2 = new ArrayList<CaseStage>();
		List<RecoveryStage> obj3 = new ArrayList<RecoveryStage>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			String remarks = "";
			Long id = 0L;
			CompositeKey compositeKey = new CompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositeKey).get();
			List<ActionStatus> listActionStatus = actionStatusRepository.findAll();
			List<CaseStage> listCaseStage = caseStageRepository.findAll();
			List<RecoveryStage> listRecovery = recoveryStageRepository.findAll();
			String catecategory = enforcementReviewCase.getCategory();
			id = categoryListRepository.findByName(catecategory).getId();
			Date caseReportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			if (id > 0 && id <= 6) {
				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = reviewMeetingRepository
						.findTopByCategoryIdOrderByIdDesc(id);
				if (enforcementCasesRemarksCategoryWise != null) {
					Date reviewMeetingDate = enforcementCasesRemarksCategoryWise.getRecordCreationDate();
					Date CaseReportingDate = caseReportingDate;
					if (reviewMeetingDate.compareTo(CaseReportingDate) > 0) {
						remarks = enforcementCasesRemarksCategoryWise.getRemarks();
					} else {
						remarks = "";
					}
				}
			} else {
				remarks = getGSTNRemarks(enforcementReviewCase.getId().getGSTIN(),
						enforcementReviewCase.getId().getCaseReportingDate(),
						enforcementReviewCase.getId().getPeriod());
			}
			Date dateFmt = enforcementReviewCase.getId().getCaseReportingDate();
			Date reportingDate = enforcementReviewCase.getId().getCaseReportingDate();
			String strDate = dateFormat.format(dateFmt);
			object.setGSTIN_ID(enforcementReviewCase.getId().getGSTIN());
			object.setCaseReportingDate_ID(strDate);
			object.setCategory(enforcementReviewCase.getCategory());
			object.setPeriod_ID(enforcementReviewCase.getId().getPeriod());
			object.setCircle(enforcementReviewCase.getLocationDetails().getLocationName());
			object.setExtensionNo(enforcementReviewCase.getExtensionNo());
			object.setTaxpayerName(enforcementReviewCase.getTaxpayerName());
			object.setIndicativeTaxValue(enforcementReviewCase.getIndicativeTaxValue());
			object.setScrutinyCaseId(enforcementReviewCase.getScrutinyCaseId());
			object.setDate(reportingDate);
			object.setRemarks(remarks);
			String caseId = enforcementReviewCase.getCaseId();
			String caseIdUpdationStatus = enforcementReviewCase.getCaseidUpdationStatus();
			if (caseId == null) {
				object.setCaseIdUpdated("no");
			} else {
				if (caseIdUpdationStatus != null) {
					object.setCaseIdUpdated("yes");
				} else {
					object.setCaseIdUpdated("no");
				}
			}
			object.setCaseId(caseId);
			for (ActionStatus actionStatus : listActionStatus) {
				obj1.add(actionStatus);
			}
			for (CaseStage caseStage : listCaseStage) {
				obj2.add(caseStage);
			}
			for (RecoveryStage recovery : listRecovery) {
				obj3.add(recovery);
			}
			if (enforcementReviewCase.getActionStatus() == null) {
				submittedData.setActionStatusName("");
				submittedData.setActionStatus(0);
			} else {
				submittedData.setActionStatusName(enforcementReviewCase.getActionStatus().getName());
				submittedData.setActionStatus(enforcementReviewCase.getActionStatus().getId());
			}
			if (enforcementReviewCase.getCaseStage() == null) {
				submittedData.setCaseStageName("");
				submittedData.setCaseStage(0);
			} else {
				submittedData.setCaseStageName(enforcementReviewCase.getCaseStage().getName());
				submittedData.setCaseStage(enforcementReviewCase.getCaseStage().getId());
			}
			if (enforcementReviewCase.getCaseStageArn() == null) {
				submittedData.setCaseStageArn("");
			} else {
				submittedData.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
			}
			if (enforcementReviewCase.getRecoveryStage() == null) {
				submittedData.setRecoveryStageName("");
				submittedData.setRecoveryStage(0);
			} else {
				submittedData.setRecoveryStageName(enforcementReviewCase.getRecoveryStage().getName());
				submittedData.setRecoveryStage(enforcementReviewCase.getRecoveryStage().getId());
			}
			if (enforcementReviewCase.getRecoveryStageArn() == null) {
				submittedData.setRecoveryStageArnStr("");
			} else {
				submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			}
			String fileName = foUserCaseReviewRepository.getFileNameUploadedByFO(
					enforcementReviewCase.getId().getGSTIN(), enforcementReviewCase.getId().getCaseReportingDate(),
					enforcementReviewCase.getId().getPeriod());
			if (fileName == null || fileName.equals("")) {
				submittedData.setSum("filenotexist");
			} else {
				submittedData.setSum("fileexist");
			}
			submittedData.setDemand(enforcementReviewCase.getDemand());
			submittedData.setRecoveryByDRC3(enforcementReviewCase.getRecoveryByDRC3());
			submittedData.setRecoveryAgainstDemand(enforcementReviewCase.getRecoveryAgainstDemand());
			submittedData.setRecoveryStageArnStr(enforcementReviewCase.getRecoveryStageArn());
			submittedData.setRemarks(fileName);
			model.addAttribute("listActionStatus", obj1);
			model.addAttribute("listCaseStage", obj2);
			model.addAttribute("listRecovery", obj3);
			model.addAttribute("submittedData", submittedData);
			model.addAttribute("viewItem", object);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return ApplicationConstants.FO + "/scrutiny_update_pop_up";
	}

	@GetMapping("/" + ApplicationConstants.FO_GET_CASE_ASSIGN_FORM)
	public String caseAssignForm(Model model, CompositeKey compositeKey) {
		Optional<EnforcementReviewCase> enfOptional = enforcementReviewCaseRepository.findById(compositeKey);
		if (enfOptional.isPresent()) {
			List<CustomUserDetails> userList = userDetailsService.getAllAssignedUserForGivenLocation(
					enfOptional.get().getLocationDetails().getLocationId(),
					userRoleRepository.findByroleCode(ApplicationConstants.FOA.toUpperCase()).get().getId());
			model.addAttribute("caseDetails", compositeKey);
			model.addAttribute("userList", userList);
			Optional<EnforcementReviewCaseAssignedUsers> assignedUsers = enforcementReviewCaseAssignedUsersRepository
					.findById(compositeKey);
			if (assignedUsers.isPresent() && assignedUsers.get().getFoaUserId() != null
					&& assignedUsers.get().getFoaUserId() > 0) {
				model.addAttribute("assignedOfficer", assignedUsers.get().getFoaUserId());
			}
		}
		return ApplicationConstants.FO + "/" + "caseIdSubmissionAndApprovalRequestForm";
	}

	@PostMapping("/caseForApproval")
	public ResponseEntity<Map<String, Object>> caseForApproval(RedirectAttributes redirectAttributes,
			CompositeKey compositeKey, String needApproval, String assigneToUserId) {
		Map<String, Object> response = new HashMap<>();
		UserDetails userDetails = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			return fieldUserServiceImpl.assesmentCaseApprovalRequestSubmission(compositeKey, needApproval,
					assigneToUserId, userDetails);
		} catch (Exception e) {
			logger.error("EnforcementFieldOffice : caseForApproval : " + e.getMessage());
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
