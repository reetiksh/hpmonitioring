package com.hp.gstreviewfeedbackapp.controller;

import java.io.IOException;
import java.net.URLEncoder;
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

import javax.validation.Valid;

import org.json.JSONObject;
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
import org.springframework.validation.BindingResult;
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
import com.hp.gstreviewfeedbackapp.data.CaseLogHistory;
import com.hp.gstreviewfeedbackapp.data.CustomUserDetails;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.data.HqTransfer;
import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.EditEnforcementReviewCaseRemark;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.FoReviewCase;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.HeadquarterLogs;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EditEnforcementReviewCaseRemarkRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FOUserCaseReviewRepository;
import com.hp.gstreviewfeedbackapp.repository.HQTransferRequest;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.HeadquarterLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.FieldUserService;
import com.hp.gstreviewfeedbackapp.service.HQReviewSummaryList;
import com.hp.gstreviewfeedbackapp.service.HQUserService;
import com.hp.gstreviewfeedbackapp.service.HQUserUploadService;
import com.hp.gstreviewfeedbackapp.service.UserDetailsService;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.HQUserUploadServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Controller
@RequestMapping("/" + ApplicationConstants.HQ)
public class HQUserController {
	private static final Logger logger = LoggerFactory.getLogger(HQUserController.class);
	@Autowired
	private HQUserUploadService HQUserUploadService;
	@Autowired
	private HQReviewSummaryList HQReviewSummeryList;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private HQTransferRequest hqTransferRequest;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private HQUserUploadServiceImpl hqUserUploadServiceImpl;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private EditEnforcementReviewCaseRemarkRepository editEnforcementReviewCaseRemarkRepository;
	@Autowired
	private HeadquarterLogsRepository headquarterLogsRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private HQUserService hqUserService;
	@Autowired
	private FOUserCaseReviewRepository foReviewCaseRepository;
	@Autowired
	private FieldUserService fieldUserService;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	MstNotificationsRepository mstNotificationsRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private HQUserUploadDataRepository hqUserUploadDataRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private FieldUserController fieldUserController;
	@Autowired
	private MstParametersModuleWiseRepository parametersModuleWiseRepository;
	@Value("${action.hqTransfer}")
	private String hqTransfer;
//	@Value("${file.upload.location}")
	@Value("${upload.directory}")
	private String fileUploadLocation;
	@Value("${file.upload.location}")
	private String foFileUploadLocation;
	@Autowired
	private FOUserCaseReviewRepository foUserCaseReviewRepository;
	@Autowired
	private DashboardRepository dashboardRepository;
	String uploadDataFlag = null;

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
		setHQMenu(model, ApplicationConstants.DASHBOARD);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			// Get locations for FO roles
			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
					userRoleRepository.findByroleCode("HQ").get());
			// JSONArray totalIndicativeTaxValueCategoryWise =
			// fieldUserServiceImpl.getTotalIndicativeTaxValueCategoryWise(locationMap);
			List<List<String>> totalIndicativeTaxValueCategoryWise = fieldUserServiceImpl
					.getTotalIndicativeTaxValueCategoryWiseList(foDashBoardWorkingLocations);
			if (totalIndicativeTaxValueCategoryWise != null && totalIndicativeTaxValueCategoryWise.size() > 0) {
				model.addAttribute("indicativeTaxValue", totalIndicativeTaxValueCategoryWise);
			}
			// JSONArray totalDemandCategoryWise =
			// fieldUserServiceImpl.getTotalDemandCategoryWise(locationMap);
			List<List<String>> totalDemandCategoryWise = fieldUserServiceImpl
					.getTotalDemandCategoryWiseList(foDashBoardWorkingLocations);
			if (totalDemandCategoryWise != null && totalDemandCategoryWise.size() > 0) {
				model.addAttribute("demandValue", totalDemandCategoryWise);
			}
			// JSONArray totalRecoveryCategoryWise =
			// fieldUserServiceImpl.getTotalRecoveryCategoryWise(locationMap);
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
					.findLocationByRolebased(userDetails.getUserId(), 1);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			Boolean hasRoleDistrict = isRoleDistrict(locationId);
			List<MstParametersModuleWise> parameterList = parametersModuleWiseRepository.findAllCasesParameter();
			model.addAttribute("parameterList", parameterList);
			Optional<MstParametersModuleWise> parameterObject = parametersModuleWiseRepository
					.findById((parameter != null && parameter > 0) ? parameter : 0);
			if (parameterObject.isPresent()) {
				model.addAttribute("parameterId", parameter);
			}
			if (locationId.contains("HP")) {
				if (category == null && financialyear == null && view == null) {
					circleList = dashboardRepository.getStateCircle(); // ok
					zoneList = dashboardRepository.getStateZone(); // ok
				} else if (category != null && view != null && parameterObject.isEmpty()) {
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getStateCircleByCategory(categoryInt); // ok
						} else if (financialyear != null) {
							circleList = dashboardRepository.getStateCircleByCategoryFinancialyear(categoryInt,
									financialyear); // ok
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategory(categoryInt, locationId); // ok
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyear(categoryInt,
									financialyear, locationId); // ok
						}
					}
				} else if (category != null && view != null && parameterObject.isPresent()) { // When Category, View and
					// Parameter is present
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getStateCircleByCategoryAndParameterName(categoryInt,
									parameterObject.get().getParamName()); // ok
						} else if (financialyear != null) {
							circleList = dashboardRepository.getStateCircleByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, parameterObject.get().getParamName()); // ok
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName()); // ok
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, locationId, parameterObject.get().getParamName()); // ok
						}
					}
				}
			} else {
				if (category == null && financialyear == null && view == null) {
					circleList = dashboardRepository.getLocationCircle(locationId); // ok
					zoneList = dashboardRepository.getLocationZone(locationId); // ok
				} else if (category != null && view != null && parameterObject.isEmpty()) {
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.trim().length() == 0) {
							circleList = dashboardRepository.getLocationCircleByCategory(categoryInt, locationId); // ok
						} else if (financialyear != null) {
							circleList = dashboardRepository.getLocationCircleByCategoryFinancialyear(categoryInt,
									financialyear, locationId); // ok
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategory(categoryInt, locationId); // ok
						} else if (financialyear != null) {
							zoneList = dashboardRepository.getLocationZoneByCategoryFinancialyear(categoryInt,
									financialyear, locationId); // ok
						}
					}
				} else if (category != null && view != null && parameterObject.isPresent()) { // When category, View and
					// Parameter is present
					categoryInt = Integer.parseInt(category);
					if (view.equals("circleWise")) {
						if (financialyear == null || financialyear.equals("")) {
							circleList = dashboardRepository.getLocationCircleByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName()); // ok
						} else if (financialyear != null) {
							circleList = dashboardRepository.getLocationCircleByCategoryFinancialyearAndParameterName(
									categoryInt, financialyear, locationId, parameterObject.get().getParamName()); // ok
						}
					} else if (view.equals("zoneWise")) {
						if (financialyear == null || financialyear.equals("")) {
							zoneList = dashboardRepository.getLocationZoneByCategoryAndParameterName(categoryInt,
									locationId, parameterObject.get().getParamName()); // ok
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
			model.addAttribute("zoneList", zoneList);
			model.addAttribute("category", category);
			model.addAttribute("year", financialyear);
			model.addAttribute("viewtype", view);
			model.addAttribute("hasRoleDistrict", hasRoleDistrict);
			model.addAttribute("commonUserDetails", "/hq/user_details");
			model.addAttribute("changeUserPassword", "/gu/change_password");
			List<MstNotifications> notificationsList = casePertainUserNotification
					.getNotificationPertainToUser(userDetails, "FO");
			logger.info("notfication fo");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HQUserController : dashboard : " + e.getMessage());
		}
		/************** dashboard consolidate case stage list end ******************/
		return ApplicationConstants.HQ + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?category=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() + "&view=" + dashboard.getView();
	}

	@GetMapping("/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES)
	public String uploadReviewCases(Model model) {
		setHQMenu(model, ApplicationConstants.HO_UPLOAD_REVIEW_CASES);
		// model.addAttribute("categories", categoryRepository.findAllCategoryNames());
		model.addAttribute("categories", categoryListRepository.findAllCategoryNamesForHQUploadCase());
		model.addAttribute("HQUploadForm", new HQUploadForm());
		return "/hq/upload_review_cases";
	}
//	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
//	public String reviewSummaryList(Model model) {
//
//		setHQMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
//		return "/hq/review_summary_list";
//
//	}

	@GetMapping("/" + ApplicationConstants.HO_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setHQMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		model.addAttribute("categories", categoryListRepository.findAllCategoryForAssessmentCases());
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
			Optional<Category> category = categoryListRepository.findById(categoryId);
			if (category.isPresent()) {
				List<MstParametersModuleWise> parameters = parametersModuleWiseRepository
						.findAllAssessmentParameterByAssessmentCategory(category.get().getName());
				if (parameters != null && parameters.size() > 0) {
					model.addAttribute("parameters", parameters);
				}
				if (parameterId != null && parameterId > 0
						&& parametersModuleWiseRepository.findById(parameterId).isPresent()) {
					model.addAttribute("parameterId", parameterId);
					model.addAttribute("categoryTotals",
							enforcementReviewCaseRepository.getAllCaseCountByCategoryAnd1stParameterId(
									category.get().getName(), parameterId.toString()));
				} else {
					model.addAttribute("categoryTotals",
							enforcementReviewCaseRepository.getAllCaseCountByCategory(category.get().getName()));
				}
			}
		}
		return "/" + ApplicationConstants.HQ + "/review_summary_list_new";
	}

	@GetMapping("/review_cases_list")
	public String reviewCasesList(Model model, @RequestParam(required = false) String category, String parameterId) {
		List<String[]> caseList = null;
		setHQMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
		Category categoryObject = categoryListRepository.findByName(category);
		if (categoryObject != null) {
			model.addAttribute("categoryId", categoryObject.getId());
		}
		if (category != null && category.length() > 0 && parameterId != null && parameterId.trim().length() > 0
				&& parametersModuleWiseRepository.findById(Integer.parseInt(parameterId.trim())).isPresent()) {
			caseList = enforcementReviewCaseRepository.findByCategoryAnd1stParameterId(category, parameterId);
		} else if (category != null && category.trim().length() > 0) {
			caseList = enforcementReviewCaseRepository.findReviewCasesListByCategory(category);
		}
		if (caseList != null && caseList.size() > 0) {
			model.addAttribute("caseList", caseList);
		}
		return "/hq/review_cases_list_new";
	}

	@GetMapping("/" + ApplicationConstants.HO_REQUEST_FOR_TRANSFER)
	public String requestForTransfer(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userDetails == null) {
			return "redirect:/logout";
		}
		setHQMenu(model, ApplicationConstants.HO_REQUEST_FOR_TRANSFER);
		List<HqTransfer> hqTransferList = hqTransferRequest.getHqTransferList(userDetails.getUserId());
		if (hqTransferList != null && hqTransferList.size() > 0) {
			model.addAttribute("hqTransferList", hqTransferList);
		}
//		Map<String, String> locatoinMap = adminUpdateUserDetailsImpl.getAllCircleMap();
		Map<String, String> locatoinMap = adminUpdateUserDetailsImpl.getAllLocationMap();
		if (locatoinMap != null && locatoinMap.size() > 0) {
			JSONObject object = new JSONObject();
			for (Map.Entry<String, String> entry : locatoinMap.entrySet()) {
				if (!entry.getKey().equalsIgnoreCase("Z04") && !entry.getKey().equalsIgnoreCase("C81")
						&& !entry.getKey().equalsIgnoreCase("HPT") && !entry.getKey().equalsIgnoreCase("DT14")
						&& !entry.getKey().equalsIgnoreCase("EZ04")) {
					object.put(entry.getKey(), entry.getValue());
				}
			}
			model.addAttribute("locatoinMap", object);
		}
//		List<EnforcementReviewCase> caseList = enforcementReviewCaseRepository.`	("HQ","transfer",76);
		return "/hq/request_for_transfer";
	}

	@PostMapping("/" + ApplicationConstants.HO_REQUEST_FOR_TRANSFER)
	@Transactional
	public String setTransferRequest(Model model, @RequestParam(required = false) String gstIn,
			@RequestParam(required = false) String caseReportingDate, @RequestParam(required = false) String period,
			@RequestParam(required = false) String assignedTo, @RequestParam(required = false) String rejectRemark,
			RedirectAttributes redirectAttributes) throws ParseException {
//		setHQMenu(model, ApplicationConstants.HO_REQUEST_FOR_TRANSFER);
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		String requestForTransferApproveOrReject = new String();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userDetails == null) {
			return "redirect:/logout";
		}
		CompositeKey compositeKey = new CompositeKey();
		compositeKey.setGSTIN(gstIn);
		compositeKey.setCaseReportingDate(formatter.parse(caseReportingDate));
		compositeKey.setPeriod(period);
		Optional<EnforcementReviewCase> object = enforcementReviewCaseRepository.findById(compositeKey);
		if (object.isPresent()) {
			Optional<EnforcementReviewCaseAssignedUsers> object1 = enforcementReviewCaseAssignedUsersRepository
					.findById(compositeKey);
			EnforcementReviewCaseAssignedUsers assignedUsers = object1.get();
			EnforcementReviewCase enCase = object.get();
			String assignedLocationId = enCase.getLocationDetails().getLocationId();
			enCase.setAssignedTo("FO");
			enCase.setAssignedFromUserId(userDetails.getUserId());
			enCase.setAssignedToUserId(0);
			enCase.setAssignedFrom("HQ");
			enCase.setLocationDetails(locationDetailsRepository.findById(assignedTo).get());
			enCase.setAction(hqTransfer);
			EnforcementReviewCase isSaveObject = enforcementReviewCaseRepository.save(enCase);
			logger.info("Headquater : Transfer request save");
			if (isSaveObject != null) {
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				caseWorkflow.setGSTIN(gstIn);
				caseWorkflow.setCaseReportingDate(formatter.parse(caseReportingDate));
				caseWorkflow.setPeriod(period);
				caseWorkflow.setAssignedFrom("HQ");
				caseWorkflow.setAssignedTo("FO");
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAction(hqTransfer);
				caseWorkflow.setAssignedFromUserId(userDetails.getUserId());
				caseWorkflow.setAssigntoUserId(0);
				caseWorkflow.setAssignedFromLocationId("HP");
				caseWorkflow.setAssignedFromLocationName(
						locationDetailsRepository.findByLocationId("HP").get().getLocationName());
				caseWorkflow.setAssignedToLocationId(assignedTo);
				caseWorkflow.setAssignedToLocationName(
						locationDetailsRepository.findById(assignedTo).get().getLocationName());
				caseWorkflow.setOtherRemarks(rejectRemark);
				if (assignedLocationId.equals(assignedTo)) {
					caseWorkflow.setStatus("Rejected");
					requestForTransferApproveOrReject = "hqTransferRejected";
					redirectAttributes.addFlashAttribute("successMessage", "The transfer request has been rejected");
				} else {
					caseWorkflow.setStatus("Approved");
					requestForTransferApproveOrReject = "hqTransferApproved";
					redirectAttributes.addFlashAttribute("successMessage", "The transfer request has been approved");
					assignedUsers.setFoUserId(0);
					assignedUsers.setApUserId(0);
					assignedUsers.setRuUserId(0);
					enforcementReviewCaseAssignedUsersRepository.save(assignedUsers);
				}
				caseWorkflowRepository.save(caseWorkflow);
				hqUserUploadServiceImpl.transferCaseActionHeadquarterLogs(enCase, rejectRemark);
				/****** Check case already assign to specific user or not start *****/
				List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("transfer");
				Integer hqUserId = enforcementReviewCaseAssignedUsersRepository
						.findByGstinPeriodRepotingDate(enCase.getId().getGSTIN(), enCase.getId().getPeriod(),
								enCase.getId().getCaseReportingDate())
						.getHqUserId();
				Integer foUserId = enforcementReviewCaseAssignedUsersRepository
						.findByGstinPeriodRepotingDate(enCase.getId().getGSTIN(), enCase.getId().getPeriod(),
								enCase.getId().getCaseReportingDate())
						.getFoUserId();
				List<MstNotifications> notificationsList = mstNotificationsRepository
						.getNotificationsToUpdateNotificationPertainTo(enCase.getId().getGSTIN(),
								enCase.getId().getPeriod(), enCase.getId().getCaseReportingDate(),
								assigneZeroUserIdToSpecificUser, "HQ", returnWorkingLocation(userDetails.getUserId()));
				if (!notificationsList.isEmpty()) {
					for (MstNotifications notificationSolo : notificationsList) {
						notificationSolo.setNotificationPertainTo(hqUserId);
					}
					mstNotificationsRepository.saveAll(notificationsList);
				}
				casePertainUserNotification.insertReAssignNotification(enCase.getId().getGSTIN(),
						enCase.getId().getCaseReportingDate(), enCase.getId().getPeriod(), "FO",
						enCase.getLocationDetails(), requestForTransferApproveOrReject, foUserId, "you", "Head Office");
				/****** Check case already assign to specific user or not End *****/
			}
		} else {
			logger.error("Headquater : Transfer request not updated");
			return "redirect:/hq/" + ApplicationConstants.HO_REQUEST_FOR_TRANSFER + "?error_message = not_updated";
		}
		return "redirect:/hq/" + ApplicationConstants.HO_REQUEST_FOR_TRANSFER;
	}

	@PostMapping("/upload_data")
	public String uploadExcelFile(@ModelAttribute("HqUploadForm") @Valid HQUploadForm HqUploadForm,
			BindingResult formResult, Model model, RedirectAttributes redirectAttributes)
			throws IOException, ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userDetails == null) {
			return "redirect:/logout";
		}
//		List<String> categories = categoryRepository.findAllCategoryNames();
//		redirectAttributes.addFlashAttribute("categories", categories);
		List<String> excelErrors = new ArrayList<>();
		redirectAttributes.addFlashAttribute("formResult", formResult);
//		setHQMenu(model, ApplicationConstants.HO_UPLOAD_REVIEW_CASES);//Set Menu list and active menu
		if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists and from has error");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF does not exists and from has error");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as Excel file does not exists and from has error");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (formResult.hasErrors() && !HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as uploaded from has error");
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF file does not exists");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as Excel file does not exists");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//			return "/hq/upload_review_cases";
			return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
		}
		MultipartFile excelFile = null;
		MultipartFile pdfFile = null;
		PdfData pdfData = HqUploadForm.getPdfData();
		ExcelData excelData = HqUploadForm.getExcelData();
		Map<String, List<List<String>>> excelDataValidationMap = null;
		if (excelData != null) {
			excelFile = excelData.getExcelFile();
			if (excelFile != null && !excelFile.isEmpty()) {
//				excelErrors = excelValidator.validateExcel(excelFile);
				// full validation
				excelDataValidationMap = excelValidator.validateExcelAndExtractData(excelFile, userDetails.getUserId());
				if (excelDataValidationMap.get("uploadData") != null) {
					redirectAttributes.addFlashAttribute("uploadData", excelDataValidationMap.get("uploadData"));
				}
				if (excelDataValidationMap.get("errorList") != null) {
					redirectAttributes.addFlashAttribute("errorList", excelDataValidationMap.get("errorList").get(0));
//					return "/hq/upload_review_cases";
					return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
				}
				if (!excelErrors.isEmpty() && excelErrors != null) {
					HqUploadForm.setExcelErrors(excelErrors);
//					return "/hq/upload_review_cases";
					return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
				}
			}
		}
		pdfFile = pdfData.getPdfFile();
		excelFile = excelData.getExcelFile();
		uploadDataFlag = "";
		if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty()
				&& excelDataValidationMap.get("uploadData") != null
				&& excelDataValidationMap.get("errorList") == null) {
			uploadDataFlag = HQUserUploadService.saveHqUserUploadDataList(HqUploadForm.getExtensionNo(),
					HqUploadForm.getCategory(), pdfFile, userDetails.getUserId(),
					excelDataValidationMap.get("uploadData"));
		}
//		if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty()) {
//			uploadDataFlag = HQUserUploadService.saveHqUserUploadData(HqUploadForm.getExtensionNo(),HqUploadForm.getCategory(), pdfFile, excelFile, userDetails.getUserId());
//		}
		if (uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
			redirectAttributes.addFlashAttribute("successMessage", ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
		} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
			redirectAttributes.addFlashAttribute("successMessage", "Record already exists!");
		} else {
			excelErrors.add(ApplicationConstants.DATA_STORE_PROCESS_NOT_COMPLETED);
			HqUploadForm.setExcelErrors(excelErrors);
		}
//		return "/hq/upload_review_cases";
		return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HO_UPLOAD_REVIEW_CASES;
	}
//	@GetMapping("/review_cases_list")
//	public String reviewCasesList(Model model, @RequestParam(required = false) String category) {
//		setHQMenu(model, ApplicationConstants.HO_REVIEW_SUMMARY_LIST);
//		model.addAttribute("category", category);
//
//		return "/hq/review_cases_list";
//	}
//	@GetMapping("/review_cases_list_ajax")
//	@ResponseBody
//	public ResponseEntity<Map<String, Object>> reviewCasesList(@RequestParam(required = false) String category,
//			@RequestParam("start") int start, @RequestParam("length") int length, @RequestParam("draw") int draw,
//			@RequestParam("search[value]") String searchValue) {
//
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Optional<UserDetails> userDetailsOpt = userDetailsRepository
//				.findByloginNameIgnoreCase(authentication.getName());
//		if (!userDetailsOpt.isPresent()) {
//			Map<String, Object> response = new HashMap<>();
//			response.put("redirect", "/logout");
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//		}
//
//		int page = start / length;
//		Pageable pageable = PageRequest.of(page, length);
//		Page<EnforcementReviewCase> enforcementReviewCasePage;
//
//		if (category != null && !category.isEmpty() && searchValue.trim().isEmpty()) {
//			enforcementReviewCasePage = enforcementReviewCaseRepository.findByCategory(category, pageable);
//		} else if (category != null && !category.isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
//			enforcementReviewCasePage = enforcementReviewCaseRepository
//					.findByCategoryWithSearchValue(searchValue.trim().toLowerCase(), category, pageable);
//		} else if (category.isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
//			enforcementReviewCasePage = enforcementReviewCaseRepository
//					.findBySearchValue(searchValue.trim().toLowerCase(), pageable);
//		} else {
//			enforcementReviewCasePage = enforcementReviewCaseRepository.findAll(pageable);
//		}
//
//		List<Map<String, Object>> cases = enforcementReviewCasePage.stream().map(e -> {
//			Map<String, Object> caseData = new HashMap<>();
//			caseData.put("GSTIN", e.getId().getGSTIN());
//			caseData.put("taxpayerName", e.getTaxpayerName());
//
//			String districtName = "";
//			if (e.getLocationDetails() != null && e.getLocationDetails().getLocationId().charAt(0) == 'C') {
//				districtName = " (Zone Name: "
//						+ locationDetailsRepository.findZoneNameByCircleId(e.getLocationDetails().getLocationId())
//						+ ")";
//			}
//			caseData.put("locationName",
//					e.getLocationDetails() != null ? e.getLocationDetails().getLocationName() + districtName : null);
//			caseData.put("period", e.getId().getPeriod());
//			caseData.put("extensionNo", e.getExtensionNo());
//			caseData.put("caseReportingDate", e.getId().getCaseReportingDate());
//			caseData.put("indicativeTaxValue", e.getIndicativeTaxValue());
//
//			String assignedofficerName = "";
//			if (e.getAssignedTo() != null && e.getAssignedTo().trim().length() > 0) {
//				if (e.getAssignedTo().equals("SFO")) {
//					e.setAssignedTo("ScrutinyFO");
//				}
//
//				int userId = 0;
//				Optional<EnforcementReviewCaseAssignedUsers> eee = null;
//				if (e.getAssignedTo().equals("HQ")) {
//					eee = enforcementReviewCaseAssignedUsersRepository.findById(e.getId());
//					if (eee.isPresent()) {
//						userId = eee.get().getHqUserId();
//					}
//				} else if (e.getAssignedTo().equals("FO")) {
//					eee = enforcementReviewCaseAssignedUsersRepository.findById(e.getId());
//					if (eee.isPresent()) {
//						userId = eee.get().getFoUserId();
//					}
//				} else if (e.getAssignedTo().equals("AP")) {
//					eee = enforcementReviewCaseAssignedUsersRepository.findById(e.getId());
//					if (eee.isPresent()) {
//						userId = eee.get().getApUserId();
//					}
//				} else if (e.getAssignedTo().equals("RU")) {
//					eee = enforcementReviewCaseAssignedUsersRepository.findById(e.getId());
//					if (eee.isPresent()) {
//						userId = eee.get().getRuUserId();
//					}
//				}
//
//				if (userId != 0) {
//					UserDetails userDetails = userDetailsRepository.findById(userId).get();
//					assignedofficerName = " (" + userDetails.getFirstName()
//							+ ((userDetails.getMiddleName() != null && userDetails.getMiddleName().trim().length() > 0)
//									? (" " + userDetails.getMiddleName())
//									: "")
//							+ ((userDetails.getLastName() != null && userDetails.getLastName().trim().length() > 0)
//									? (" " + userDetails.getLastName())
//									: "")
//							+ ")";
//				}
//			}
//			caseData.put("assignedTo",
//					userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName() + assignedofficerName);
//			caseData.put("actionStatus", e.getActionStatus() != null ? e.getActionStatus().getName() : null);
//			caseData.put("caseId", e.getCaseId());
//			caseData.put("caseStage", e.getCaseStage() != null ? e.getCaseStage().getName() : null);
//			caseData.put("caseStageArn", e.getCaseStageArn());
//			caseData.put("demand", e.getDemand());
//			caseData.put("recoveryStage", e.getRecoveryStage() != null ? e.getRecoveryStage().getName() : null);
//			caseData.put("recoveryStageArn", e.getRecoveryStageArn());
//			caseData.put("recoveryByDRC3", e.getRecoveryByDRC3());
//			caseData.put("recoveryAgainstDemand", e.getRecoveryAgainstDemand());
//			caseData.put("fileName", enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
//					e.getId().getPeriod(), e.getId().getCaseReportingDate()));
//			return caseData;
//		}).collect(Collectors.toList());
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("draw", draw);
//		response.put("recordsTotal", enforcementReviewCaseRepository.count());
//		response.put("recordsFiltered", enforcementReviewCasePage.getTotalElements());
//		response.put("data", cases);
//
//		return ResponseEntity.ok(response);
//	}

	@GetMapping("/" + ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE)
	public String updateEnforcementCase(Model model) {
		setHQMenu(model, ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE);
		List<String> periods = enforcementReviewCaseRepository.findAllDistinctPeriod();
		model.addAttribute("periods", periods);
		return "/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE;
	}

	@PostMapping("/" + ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE)
	@Transactional
	public String updateEnforcementCase(@ModelAttribute CompositeKey compositeKey, @RequestParam String location,
			@RequestParam Integer assignToEmployee, @RequestParam String remark, @RequestParam String otherRemarks,
			RedirectAttributes redirectAttributes) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			if (userDetails == null) {
				return "redirect:/logout";
			}
			Optional<EnforcementReviewCase> objectErc = enforcementReviewCaseRepository.findById(compositeKey);
			if (objectErc.isPresent()) {
				EnforcementReviewCase case1 = objectErc.get();
				case1.setLocationDetails(locationDetailsRepository.findById(location).get());
				case1.setAssignedTo("FO");
				case1.setAssignedToUserId(assignToEmployee);
				case1.setFindingCategory(remark + (otherRemarks.length() > 0 ? (" Reason : " + otherRemarks) : ""));
				EnforcementReviewCase enfCase = enforcementReviewCaseRepository.save(case1);
				if (enfCase != null) {
					logger.info(
							"HQUserController : updateEnforcementCase : Case's jurisdiction is updated successfully");
					redirectAttributes.addFlashAttribute("successMessage", "Enforcement case is updated successfully");
					List<EnforcementReviewCase> caseList = new ArrayList<>();
					caseList.addAll(enforcementReviewCaseRepository.findByGstinPeriodRepotingDate(
							compositeKey.getGSTIN(), compositeKey.getPeriod(), compositeKey.getCaseReportingDate()));
					if (!caseList.isEmpty()) {
						redirectAttributes.addFlashAttribute("caseList", caseList);
					}
					CaseWorkflow caseWorkflow = new CaseWorkflow();
					caseWorkflow.setGSTIN(case1.getId().getGSTIN());
					caseWorkflow.setCaseReportingDate(case1.getId().getCaseReportingDate());
					caseWorkflow.setPeriod(case1.getId().getPeriod());
					caseWorkflow.setAssignedFrom(case1.getAssignedFrom());
					caseWorkflow.setAssignedFromUserId(case1.getAssignedFromUserId());
					caseWorkflow.setAssignedTo(case1.getAssignedTo());
					caseWorkflow.setAssigntoUserId(case1.getAssignedToUserId());
					caseWorkflow.setAssignedFromLocationId("HP");
					caseWorkflow.setUpdatingDate(new Date());
					caseWorkflow.setAction("change jurisdiction");
					caseWorkflow.setAssignedToLocationId(
							locationDetailsRepository.findByLocationName(location).get().getLocationId());
					caseWorkflowRepository.save(caseWorkflow);
					HeadquarterLogs object = new HeadquarterLogs();
					object.setGSTIN(case1.getId().getGSTIN());
					object.setPeriod(case1.getId().getPeriod());
					object.setCaseReportingDate(case1.getId().getCaseReportingDate());
					object.setTaxpayerName(case1.getTaxpayerName());
					object.setExtensionNo(case1.getExtensionNo());
					object.setCategory(case1.getCategory());
					object.setIndicativeTaxValue(Long.valueOf(case1.getIndicativeTaxValue()));
					object.setAction(case1.getAction());
					object.setAssignedTo(case1.getAssignedTo());
					object.setAssignedFromUserId(userDetails.getUserId());
					object.setAssignedToUserId(case1.getAssignedToUserId());
					object.setRemark("change jurisdiction : " + remark
							+ (otherRemarks.length() > 0 ? (" Reason : " + otherRemarks) : ""));
					object.setWorkingLocation(case1.getLocationDetails().getLocationName());
					object.setCaseUpdatingTime(new Date());
					object.setAssignedFrom("HQ");
					headquarterLogsRepository.save(object);
					Optional<EnforcementReviewCaseAssignedUsers> objectERCAssignedUser = enforcementReviewCaseAssignedUsersRepository
							.findById(compositeKey);
					if (objectERCAssignedUser.isPresent()) {
						EnforcementReviewCaseAssignedUsers eRCAssignedUser = objectERCAssignedUser.get();
						eRCAssignedUser.setFoUserId(assignToEmployee);
						eRCAssignedUser.setRuUserId(0);
						eRCAssignedUser.setApUserId(0);
						enforcementReviewCaseAssignedUsersRepository.save(eRCAssignedUser);
					}
				} else {
					logger.error("HQUserController : updateEnforcementCase : Case's jurisdiction is not updated");
					redirectAttributes.addFlashAttribute("errorMessage", "Enforcement case is not updated");
				}
			}
		} catch (Exception e) {
			logger.error("HQUserController : updateEnforcementCase : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE;
	}

	@GetMapping("/search_enforcement_cases")
	public String searchEnforcementCases(@ModelAttribute CompositeKey compositeKey,
			RedirectAttributes redirectAttributes) {
		List<EnforcementReviewCase> caseList = new ArrayList<>();
		if (compositeKey.getGSTIN() != null && compositeKey.getPeriod() != null && compositeKey.getPeriod().length() > 0
				&& compositeKey.getCaseReportingDate() == null) {
			caseList = enforcementReviewCaseRepository.findAllByGstinPeriod(compositeKey.getGSTIN(),
					compositeKey.getPeriod());
		} else if (compositeKey.getGSTIN() != null
				&& (compositeKey.getPeriod() == null || compositeKey.getPeriod().length() == 0)
				&& compositeKey.getCaseReportingDate() != null) {
			caseList = enforcementReviewCaseRepository.findAllByGstinCaseReportingDate(compositeKey.getGSTIN(),
					compositeKey.getCaseReportingDate());
		} else if (compositeKey.getGSTIN() != null
				&& (compositeKey.getPeriod() == null || compositeKey.getPeriod().length() == 0)
				&& compositeKey.getCaseReportingDate() == null) {
			caseList = enforcementReviewCaseRepository.findAllByGstin(compositeKey.getGSTIN());
		} else {
			List<EnforcementReviewCase> object = enforcementReviewCaseRepository.findByGstinPeriodRepotingDate(
					compositeKey.getGSTIN(), compositeKey.getPeriod(), compositeKey.getCaseReportingDate());
			if (!object.isEmpty()) {
				caseList.addAll(object);
			}
		}
		if (caseList != null && caseList.size() > 0) {
			Map<Integer, String> allParameterMap = parametersModuleWiseRepository.findAll().stream()
					.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
			caseList.forEach(caseDetails -> {
				if (caseDetails.getParameter() != null && !caseDetails.getParameter().trim().isEmpty()) {
					List<String> parameterName = Arrays.stream(caseDetails.getParameter().split(","))
							.map(paramId -> Integer.parseInt(paramId)).map(allParameterMap::get)
							.collect(Collectors.toList());
					caseDetails.setParameter(String.join(", ", parameterName));
				} else {
					caseDetails.setParameter(null);
				}
			});
			redirectAttributes.addFlashAttribute("caseList", caseList);
		}
		return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HQ_UPDATE_ENFORCEMENT_CASE;
	}

	@GetMapping("/view_enforcement_case_to_edit")
	public String viewEnforcementCaseToEdit(@RequestParam String GSTIN, @RequestParam String period,
			@RequestParam String caseReportingDateStr, Model model) {
		Date caseReportingDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			caseReportingDate = sdf.parse(caseReportingDateStr);
			CompositeKey compositeKey = new CompositeKey(GSTIN, caseReportingDate, period);
			Optional<EnforcementReviewCase> object = enforcementReviewCaseRepository.findById(compositeKey);
			if (object.isPresent()) {
				model.addAttribute("enforcementReviewCase", object.get());
			}
			List<LocationDetails> locationList = new ArrayList<>();
			List<LocationDetails> locationDetailsList = locationDetailsRepository.findAllByOrderByLocationIdAsc();
			for (LocationDetails ld : locationDetailsList) {
				if (!ld.getLocationId().equalsIgnoreCase("Z04") && !ld.getLocationId().equalsIgnoreCase("C81")
						&& !ld.getLocationId().equalsIgnoreCase("HPT") && !ld.getLocationId().equalsIgnoreCase("DT14")
						&& !ld.getLocationId().equalsIgnoreCase("EZ04")) {
					locationList.add(ld);
				}
			}
			if (locationList != null && locationList.size() > 0) {
				model.addAttribute("locationList", locationList);
			}
			List<EditEnforcementReviewCaseRemark> remarks = editEnforcementReviewCaseRemarkRepository.findAll();
			if (remarks != null && remarks.size() > 0) {
				model.addAttribute("remarks", remarks);
			}
		} catch (Exception e) {
			logger.error("HQUserController : viewEnforcementCaseToEdit : " + e.getMessage());
		}
		return ApplicationConstants.HQ + "/view_enforcement_case_to_edit";
	}

	@GetMapping("/get_all_assigned_users")
	@ResponseBody
	public List<CustomUserDetails> getAllAssignedUserForALocation(@RequestParam String location) {
		List<CustomUserDetails> userList = userDetailsService.getAllAssignedUserForGivenLocation(location, 2);
		return userList;
	}

	@GetMapping("/" + ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE)
	public String viewEnforcementCases(Model model) {
		setHQMenu(model, ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE);
		return "/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE;
	}

	@GetMapping("/" + ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE + "/search_enforcement_cases")
	public String viewEnforcementCasesSearchEnforcementCases(@RequestParam String GSTIN,
			RedirectAttributes redirectAttributes) {
		List<EnforcementReviewCase> caseList = enforcementReviewCaseRepository.findAllByGstin(GSTIN);
		if (caseList != null && caseList.size() > 0) {
			Map<Integer, String> allParameterMap = parametersModuleWiseRepository.findAll().stream()
					.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
			caseList.forEach(caseDetails -> {
				String parameter = caseDetails.getParameter();
				if (parameter != null && !parameter.trim().isEmpty()) {
					List<String> parameterName = Arrays.stream(parameter.split(",")).map(String::trim)
							.map(Integer::parseInt).map(allParameterMap::get).collect(Collectors.toList());
					caseDetails.setParameter(String.join(", ", parameterName));
				} else {
					caseDetails.setParameter(null);
				}
			});
			redirectAttributes.addFlashAttribute("caseList", caseList);
		}
		return "redirect:/" + ApplicationConstants.HQ + "/" + ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE;
	}

	@GetMapping("/" + ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE_HISTROEY)
	public String view_enforcement_case_history(Model model, @ModelAttribute CompositeKey compositeKey) {
		model.addAttribute("GST_IN", compositeKey.getGSTIN());
		List<CaseWorkflow> caseWorkflowList = caseWorkflowRepository.findAllByGstinPeriodCaseReportingDate(
				compositeKey.getGSTIN(), compositeKey.getPeriod(), compositeKey.getCaseReportingDate());
		List<CaseLogHistory> caseLogHistoriyList1 = hqUserService
				.importDataFromCaseWorkflowToCaseLogHistory(caseWorkflowList, compositeKey);
		List<FoReviewCase> foReviewCaseLsit = foReviewCaseRepository.findAllByGstinPeriodCaseReportingDate(
				compositeKey.getGSTIN(), compositeKey.getPeriod(), compositeKey.getCaseReportingDate());
		List<CaseLogHistory> caseLogHistoriyList2 = fieldUserService
				.importDataFromCaseWorkflowToCaseLogHistory(foReviewCaseLsit);
		List<CaseLogHistory> caseLogHistoriyList = new ArrayList<>();
		caseLogHistoriyList.addAll(caseLogHistoriyList1);
		caseLogHistoriyList.addAll(caseLogHistoriyList2);
		String parameters = enforcementReviewCaseRepository.findParameterNamesById(compositeKey.getGSTIN(),
				compositeKey.getPeriod(), compositeKey.getCaseReportingDate());
		caseLogHistoriyList.forEach(caseDetails -> caseDetails.setParameter(parameters));
		model.addAttribute("caseLogHistoriyList", caseLogHistoriyList);
		setHQMenu(model, ApplicationConstants.HQ_VIEW_ENFORCEMENT_CASE);
		return "/" + ApplicationConstants.HQ + "/history_enforcement_cases";
	}

	@GetMapping("/update_case")
	public String updateCase(Model model) {
		return "/hq/update_case";
	}

	/* download file strat */
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
			logger.info("HeadQuater Controller.downloadFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("HeadQuater Controller.downloadFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/downloadFOFile")
	public ResponseEntity<Resource> downloadFOFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFOFile().initiate" + fileName);
			String filesDirectory = foFileUploadLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("HeadQuater Controller.downloadFOFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("HeadQuater Controller.downloadFOFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	/* download file end */

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setHQMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
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
	private void setHQMenu(Model model, String activeMenu) {
//		appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.HQ);
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.HQ));
		model.addAttribute("activeMenu", activeMenu);
		model.addAttribute("activeRole", "HQ");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "HQ");
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "HQ");
				model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
				model.addAttribute("unReadNotificationList", unReadNotificationList);
				model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
				model.addAttribute("convertUnreadToReadNotifications", "/hq/convert_unread_to_read_notifications");
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
				model.addAttribute("commonUserDetails", "/hq/user_details");
				model.addAttribute("changeUserPassword", "/gu/change_password");
				model.addAttribute("homePageLink", "/hq/dashboard");
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
				/*************************
				 * to display user generic details end
				 ****************************/
			}
		}
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
	private List<String> returnWorkingLocation(Integer currentUserId) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId,
				userRoleRepository.findByroleCode("HQ").get().getId());
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

	@GetMapping("/verify_recovery")
	public String getRecoveryList(Model model) {
		setHQMenu(model, ApplicationConstants.HQ_VERIFY_CASE_CASE);
		String actionName = null;
		String caseStageName = null;
		String recoveryStageName = null;
		Integer actionStatus = 0;
		Integer recoveryStage = 0;
		Integer caseStage = 0;
		List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
		List<EnforcementReviewCase> list = hqUserUploadDataRepository.findFullRecoveryList();
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
			String uploadedFileName = URLEncoder.encode(
					enforcementReviewCaseRepository.findFoRecoveryCloseFileNameByGstinAndPeriodAndCaseZReportingDate(
							obj.getId().getGSTIN(), obj.getId().getPeriod(), obj.getId().getCaseReportingDate()));
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
			object.setUploadedFileName(
					(uploadedFileName != null && uploadedFileName.trim().length() > 0) ? uploadedFileName.trim()
							: null);
			listofCases.add(object);
		}
		model.addAttribute("listofCases", listofCases);
		return ApplicationConstants.HQ + "/" + "verify_recovery";
	}

	@PostMapping("/approve_recovery_cases")
	public String approveRecoveryCases(Model model, @ModelAttribute WorkFlowModel workFlowModel,
			RedirectAttributes redirectAttribute) throws ParseException {
		setHQMenu(model, ApplicationConstants.HQ_VERIFY_CASE_CASE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			logger.info("approve recovery cases" + workFlowModel.getGstno() + "," + workFlowModel.getDate() + ","
					+ workFlowModel.getPeriod());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(workFlowModel.getDate());
			String action = "foRecoveryApprove";
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(workFlowModel.getGstno());
			compositKey.setCaseReportingDate(date);
			compositKey.setPeriod(workFlowModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setRecoveryStatus(action);
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
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo("AP");
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseId(reviewCase.getCaseId());
			foReviewCase.setCaseStageArn(reviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(reviewCase.getRecoveryStageArn());
			foReviewCase.setUserId(userDetails.getUserId());
			foReviewCase.setAssignedToUserId(0);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(userDetails.getUserId());
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case approve successfully");
			enforcementReviewCaseRepository.save(enforcementReviewCase);
			redirectAttribute.addFlashAttribute("acknowlegdemessage", "Approval completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/hq/verify_recovery";
	}

	@PostMapping("/raise_query_recovery_cases")
	public String raiseQueryRecoveryCases(Model model, @ModelAttribute WorkFlowModel workFlowModel,
			RedirectAttributes redirectAttribute) throws ParseException {
		setHQMenu(model, ApplicationConstants.HQ_VERIFY_CASE_CASE);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			logger.info("raise query recovery cases" + workFlowModel.getGstno() + "," + workFlowModel.getDate() + ","
					+ workFlowModel.getPeriod() + "," + workFlowModel.getOtherRemarks());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(workFlowModel.getDate());
			String action = "foRecoveryRaiseQuery";
			String otherRemarks = workFlowModel.getOtherRemarks();
			CompositeKey compositKey = new CompositeKey();
			compositKey.setGSTIN(workFlowModel.getGstno());
			compositKey.setCaseReportingDate(date);
			compositKey.setPeriod(workFlowModel.getPeriod());
			EnforcementReviewCase enforcementReviewCase = hqUserUploadDataRepository.findById(compositKey).get();
			enforcementReviewCase.setRecoveryStatus(action);
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
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setAction(action);
			foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
			foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
			foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
			foReviewCase.setAssignedTo("FO");
			foReviewCase.setCategory(category);
			foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
			foReviewCase.setDemand(demand);
			foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
			foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
			foReviewCase.setTaxpayerName(taxPayerName);
			foReviewCase.setCircle(circle);
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setCaseId(reviewCase.getCaseId());
			foReviewCase.setCaseStageArn(reviewCase.getCaseStageArn());
			foReviewCase.setRecoveryStageArn(reviewCase.getRecoveryStageArn());
			foReviewCase.setUserId(userDetails.getUserId());
			foReviewCase.setAssignedToUserId(0);
			foReviewCase.setExtensionFilename(reviewCase.getExtensionNoDocument().getExtensionFileName());
			foReviewCase.setAssignedFromUserId(userDetails.getUserId());
			foReviewCase.setOtherRemarksRecovery(otherRemarks);
			foUserCaseReviewRepository.save(foReviewCase);
			logger.info("case raise query successfully");
			enforcementReviewCaseRepository.save(enforcementReviewCase);
			redirectAttribute.addFlashAttribute("message", "Raise Query completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/hq/verify_recovery";
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
}
