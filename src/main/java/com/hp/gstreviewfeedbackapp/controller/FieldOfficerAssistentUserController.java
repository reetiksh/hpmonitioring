package com.hp.gstreviewfeedbackapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseIdUpdationRemarks;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksCategoryWise;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksDetails;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.FieldOfficeAssistantUserLogs;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseIdRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FOUserCaseReviewRepository;
import com.hp.gstreviewfeedbackapp.repository.FieldOfficeAssistantUserLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.FOA)
public class FieldOfficerAssistentUserController {
	private static final Logger logger = LoggerFactory.getLogger(FieldOfficerAssistentUserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Autowired
	private DashboardRepository dashboardRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private HQUserUploadDataRepository hqUserUploadDataRepository;
	@Autowired
	private ReviewMeetingDetailsRepository reviewMeetingDetailsRepository;
	@Autowired
	private ReviewMeetingRepository reviewMeetingRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private CaseIdRemarksRepository caseIdUpdationRemarksRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	@Autowired
	private FOUserCaseReviewRepository foUserCaseReviewRepository;
	@Autowired
	private FieldOfficeAssistantUserLogsRepository fieldOfficeAssistantUserLogsRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private MstNotificationsRepository mstNotificationsRepository;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Value("${action.acknowledge}")
	private String actionAcknowledge;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	@Value("${action.foClose}")
	private String actionClose;

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
		setFoAssistantMenu(model, ApplicationConstants.DASHBOARD);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			// Get locations for FO roles
			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
					userRoleRepository.findByroleCode("FOA").get());
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
					.findLocationByRolebased(userDetails.getUserId(), 22);
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
			model.addAttribute("commonUserDetails", "/foa/user_details");
			model.addAttribute("changeUserPassword", "/gu/change_password");
//			List<MstNotifications> notificationsList = casePertainUserNotification
//					.getNotificationPertainToUser(userDetails, "FOA");
//			logger.info("notfication fo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************** dashboard consolidate case stage list end ******************/
		return ApplicationConstants.FOA + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.FOA_UPDATE_SUMMARY_LIST)
	public String getUpdateSummaryList(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		setFoAssistantMenu(model, ApplicationConstants.FOA_UPDATE_SUMMARY_LIST);
//		List<Category> categories = categoryListRepository.findAllByActiveStatus(true);
		List<String> actionList = new ArrayList<>();
		actionList.add(actionAcknowledge);
		actionList.add("recommendedForAssesAndAdjudication");
		List<Category> categories = categoryListRepository
				.findAllActiveCasesCategoriesByActionStatusAndFoaUserId(actionList, object.get().getUserId());
		model.addAttribute("categories", categories);
		return ApplicationConstants.FOA + "/" + ApplicationConstants.FOA_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/update_summary_data_list")
	public String getUpdateSummaryDataList(Model model, @RequestParam Long id,
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
							.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFoaUserId(
									objCategory.get().getName(), actionAcknowledge, userDetails.getUserId());
					model.addAttribute("parameters", parameters);
				}
			}
			String categoryName = category.getName();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
			List<EnforcementReviewCase> list = null;
			if (categoryName != null && categoryName.length() > 0 && parameterId != null && parameterId > 0) {
				model.addAttribute("parameterId", parameterId);
				Optional<MstParametersModuleWise> objParamter = mstParametersRepository.findById(parameterId);
				list = hqUserUploadDataRepository.findByCategoryAndActionAndFoaUserIdListAnd1stParameter(categoryName,
						actionAcknowledge, userIdList, parameterId.toString());
			} else {
				list = hqUserUploadDataRepository.findByCategoryAndActionAndFoaUserIdList(categoryName,
						actionAcknowledge, userIdList);
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
		return ApplicationConstants.FOA + "/update_summary_list_data";
	}

	@GetMapping("/" + ApplicationConstants.FOA_GET_SCRUTINY_DATA_LIST)
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
							.findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFoaUserId(
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
				list = hqUserUploadDataRepository.findByCategoryAndActionAndFoaUserIdListAnd1stParameter(categoryName,
						"recommendedForAssesAndAdjudication", userIdList, parameterId.toString());
			} else {
				list = hqUserUploadDataRepository.findByCategoryAndActionAndFoaUserIdList(categoryName,
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
		return ApplicationConstants.FOA + "/scrutiny_cases_list";
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
		return ApplicationConstants.FOA + "/view_case";
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
			FieldOfficeAssistantUserLogs foaUserLogs = new FieldOfficeAssistantUserLogs();
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
			foaUserLogs.setGSTIN(compositKey.getGSTIN());
			foaUserLogs.setCaseReportingDate(compositKey.getCaseReportingDate());
			foaUserLogs.setPeriod(compositKey.getPeriod());
			foaUserLogs.setAction(action);
			foaUserLogs.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foaUserLogs.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foaUserLogs.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foaUserLogs.setAssignedTo(assignedTo);
			foaUserLogs.setCategory(category);
			foaUserLogs.setIndicativeTaxValue(indicativeTaxValue);
			foaUserLogs.setDemand(demand);
			foaUserLogs.setRecoveryByDRC3(recoveryByDRC3);
			foaUserLogs.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foaUserLogs.setFoFilepath(fileName);
			foaUserLogs.setTaxpayerName(taxPayerName);
			foaUserLogs.setJurisdiction(circle);
			foaUserLogs.setCaseUpdatingDate(new Date());
			if (caseIdStr == null) {
				foaUserLogs.setCaseId(caseId);
			} else {
				foaUserLogs.setCaseId(updatedReviewCase.getCaseId());
			}
			foaUserLogs.setCaseStageArn(caseStageArn);
			foaUserLogs.setRecoveryStageArn(recoveryStageArn);
			foaUserLogs.setUserId(userId);
			foaUserLogs.setAssignedToUserId(userId);
			foaUserLogs.setExtensionFilename((reviewCase.getExtensionNoDocument() != null
					&& reviewCase.getExtensionNoDocument().getExtensionFileName() != null)
							? reviewCase.getExtensionNoDocument().getExtensionFileName()
							: null);
			foaUserLogs.setAssignedFromUserId(reviewCase.getAssignedFromUserId());
			foaUserLogs.setAssignedFrom(updatedReviewCase.getLocationDetails().getLocationName());
			foaUserLogs.setCaseJurisdiction(updatedReviewCase.getLocationDetails().getLocationName());
			fieldOfficeAssistantUserLogsRepository.save(foaUserLogs);
			logger.info("Case Updated Successfully by Field Officer Assistent");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:" + ApplicationConstants.FOA_UPDATE_SUMMARY_LIST;
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

	// Get the menu list and active menu
	private void setFoAssistantMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.FOA));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "FOA");
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
					model.addAttribute("commonUserDetails", "/foa/user_details");
					model.addAttribute("changeUserPassword", "/gu/change_password");
					model.addAttribute("homePageLink", "/foa/dashboard");
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
			e.printStackTrace();
			logger.error("Error :: setAdminMenu" + e.getMessage());
		}
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
			foTransferReviewCase.setAssignedFrom("FOA");
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
			caseWorkflowRepository.save(foTransferReviewCase);
			FieldOfficeAssistantUserLogs foaUserLogs = new FieldOfficeAssistantUserLogs();
			foaUserLogs.setGSTIN(compositKey.getGSTIN());
			foaUserLogs.setCaseReportingDate(compositKey.getCaseReportingDate());
			foaUserLogs.setPeriod(compositKey.getPeriod());
			foaUserLogs.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foaUserLogs.setJurisdiction(updatedReviewCase.getLocationDetails().getLocationName());
			foaUserLogs.setCategory(updatedReviewCase.getCategory());
			foaUserLogs.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foaUserLogs.setAction(updatedReviewCase.getAction());
			foaUserLogs.setCaseUpdatingDate(new Date());
			foaUserLogs.setDemand(updatedReviewCase.getDemand());
			foaUserLogs.setRecoveryAgainstDemand(updatedReviewCase.getRecoveryAgainstDemand());
			foaUserLogs.setRecoveryByDRC3(updatedReviewCase.getRecoveryByDRC3());
			foaUserLogs.setRecoveryStage(
					updatedReviewCase.getRecoveryStage() != null ? updatedReviewCase.getRecoveryStage().getId() : 0);
			foaUserLogs.setCaseStage(
					updatedReviewCase.getCaseStage() != null ? updatedReviewCase.getCaseStage().getId() : 0);
			foaUserLogs.setActionStatus(
					updatedReviewCase.getActionStatus() != null ? updatedReviewCase.getActionStatus().getId() : 0);
			foaUserLogs.setAssignedTo("RU");
			foaUserLogs.setUserId(userId);
			foaUserLogs.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foaUserLogs.setExtensionFilename(updatedReviewCase.getExtensionNoDocument().getExtensionFileName());
			foaUserLogs.setCaseId(updatedReviewCase.getCaseId());
			foaUserLogs.setCaseStageArn(updatedReviewCase.getCaseStageArn());
			foaUserLogs.setRecoveryStageArn(updatedReviewCase.getRecoveryStageArn());
			foaUserLogs.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foaUserLogs.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
			foaUserLogs.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
			fieldOfficeAssistantUserLogsRepository.save(foaUserLogs);
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

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setFoAssistantMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
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

	private List<String> returnWorkingLocation(Integer currentUserId) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId,
				userRoleRepository.findByroleCode("FOA").get().getId());
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
}
