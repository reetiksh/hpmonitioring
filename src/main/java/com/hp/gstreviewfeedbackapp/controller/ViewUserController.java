package com.hp.gstreviewfeedbackapp.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.DashboardRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.SubAppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.HQReviewSummaryList;
import com.hp.gstreviewfeedbackapp.service.impl.FieldUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.ViewServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.VW)
public class ViewUserController {
	private static final Logger logger = LoggerFactory.getLogger(ViewUserController.class);
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private ViewServiceImpl viewServiceImpl;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private SubAppMenuRepository subAppMenuRepository;
	@Autowired
	private FieldUserController fieldUserController;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private FieldUserServiceImpl fieldUserServiceImpl;
	@Autowired
	private DashboardRepository dashboardRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private HQReviewSummaryList HQReviewSummeryList;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Autowired
	private MstParametersModuleWiseRepository parametersModuleWiseRepository;
	@Value("${file.upload.location}")
	private String fileUploadLocation;

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
		setViewMenu(model, ApplicationConstants.DASHBOARD);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> foDashBoardWorkingLocations = returnWorkingLocation(userDetails.getUserId());
			// Get locations for FO roles
			Map<String, String> locationMap = userDetailsServiceImpl.getUserLocationDetailsByRole(userDetails,
					userRoleRepository.findByroleCode("VW").get());
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
			List<String> circlelocation = null;
			int categoryInt = 0;
			List<DashboardDistrictCircle> circleList = null;
			List<DashboardDistrictCircle> zoneList = null;
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 7);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			Boolean hasRoleDistrict = fieldUserController.isRoleDistrict(locationId);
			List<MstParametersModuleWise> parameterList = parametersModuleWiseRepository.findAllCasesParameter();
			model.addAttribute("parameterList", parameterList);
			Optional<MstParametersModuleWise> parameterObject = parametersModuleWiseRepository
					.findById((parameter != null && parameter > 0) ? parameter : 0);
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
			model.addAttribute("commonUserDetails", "/vw/user_details");
			model.addAttribute("changeUserPassword", "/gu/change_password");
			List<MstNotifications> notificationsList = casePertainUserNotification
					.getNotificationPertainToUser(userDetails, "FO");
			logger.info("notfication fo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************** dashboard consolidate case stage list end ******************/
		return ApplicationConstants.VW + "/" + ApplicationConstants.DASHBOARD;
	}

	@PostMapping("/dashboard")
	public String foDashboad(Model model, @ModelAttribute("dashboard") WorkFlowModel dashboard,
			RedirectAttributes redirectAttrs) {
		return "redirect:" + ApplicationConstants.DASHBOARD + "?category=" + dashboard.getCategory() + "&financialyear="
				+ dashboard.getFinancialyear() + "&view=" + dashboard.getView();
	}

	@GetMapping("/" + ApplicationConstants.VW_MIS)
	public String misList(Model model) {
		setViewMenu(model, ApplicationConstants.VW_MIS);
		return "/vw/mis";
	}

	@SuppressWarnings("null")
	@GetMapping("/viewWiseReport")
	public String viewWiseReport(Model model, @RequestParam(name = "view", required = false) String view,
			@RequestParam(name = "selectedReport", required = false) String selectedReport,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "category", required = false) String category) {
		setViewMenu(model, ApplicationConstants.VW_MIS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			Boolean isYearSelected = false;
			if (view == null) {
				view = "zoneWise";
				model.addAttribute("view", view);
			}
			if (selectedReport != null) {
				model.addAttribute("selectedReport", selectedReport);
			}
			List<Object[]> zoneWiseCounts = null;
			List<Object[]> foWiseCounts = null;
			Long categoryId = null;
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 7);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			List<Long> categoryIdList = categoryListRepository.findOnlyActiveId();
			List<String> list = new ArrayList<String>();
			List<Long> categoryList = new ArrayList<Long>();
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			if (financialyear == null) {
				list = yearlist;
			} else if (financialyear != null && financialyear.length() == 0) {
				list = yearlist;
			} else if (financialyear != null && financialyear.length() > 0) {
				list.clear();
				list.add(financialyear);
				isYearSelected = true;
			}
			if (category == null) {
				categoryList = categoryIdList;
			} else if (category != null) {
				categoryId = Long.parseLong(category.trim());
				if (categoryId > 0) {
					categoryList.clear();
					categoryList.add(categoryId);
				} else {
					categoryList.clear();
					categoryList = categoryIdList;
				}
			}
			// Remove duplicates by converting to a Set and back to a List
			Set<String> uniqueLocationIdSet = new HashSet<>(locationId);
			List<String> uniqueLocationId = new ArrayList<>(uniqueLocationIdSet);
			List<String> locationDistId = uniqueLocationId.stream().filter(loc -> loc.startsWith("C"))
					.collect(Collectors.toList());
			if (uniqueLocationId != null && uniqueLocationId.size() > 0 && "zoneWise".equals(view)) {
				if (financialyear == null) {
					zoneWiseCounts = userRoleMappingRepository.zoneAverageWiseFoCount(locationDistId, list,
							categoryList);
				} else if (financialyear != null && financialyear.length() == 0) {
					zoneWiseCounts = userRoleMappingRepository.zoneAverageWiseFoCount(locationDistId, list,
							categoryList);
				} else if (financialyear != null && financialyear.length() > 0) {
					zoneWiseCounts = userRoleMappingRepository.zoneWiseFoCount(locationDistId, list, categoryList);
				}
			}
			if (uniqueLocationId != null && uniqueLocationId.size() > 0 && "officerWise".equals(view)) {
				foWiseCounts = userRoleMappingRepository.FoWiseCount(locationDistId, list, categoryList);
			}
			model.addAttribute("zoneWiseCounts", zoneWiseCounts);
			model.addAttribute("foWiseCounts", foWiseCounts);
			model.addAttribute("year", financialyear);
			model.addAttribute("financialyear", yearlist);
			model.addAttribute("view", view);
			model.addAttribute("selectedReport", selectedReport);
			model.addAttribute("isYearSelected", isYearSelected);
			model.addAttribute("categories", categoryListRepository.findAllByActiveStatus(true));
			model.addAttribute("category", category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/viewWiseReport";
	}

	@SuppressWarnings("null")
	@GetMapping("/landingDrillDown")
	public String drillDown(Model model, @RequestParam(name = "zoneName", required = false) String zoneName,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "category", required = false) String category) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 7);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			List<String> list = new ArrayList<String>();
			List<Object[]> circleWiseCounts = null;
			List<Long> categoryList = new ArrayList<Long>();
			List<String> yearlist = enforcementReviewCaseRepository.findAllFinancialYear();
			List<Long> categoryIdList = categoryListRepository.findOnlyActiveId();
			Long categoryId = Long.parseLong(category);
			if (financialyear.equals("0")) {
				list = yearlist;
			} else {
				list.clear();
				list.add(financialyear);
			}
			if (categoryId == 0) {
				categoryList = categoryIdList;
			} else if (categoryId > 0) {
				categoryList.clear();
				categoryList.add(categoryId);
			}
			// Remove duplicates by converting to a Set and back to a List
			Set<String> uniqueLocationIdSet = new HashSet<>(locationId);
			List<String> uniqueLocationId = new ArrayList<>(uniqueLocationIdSet);
			List<String> locationDistId = uniqueLocationId.stream().filter(loc -> loc.startsWith("C"))
					.collect(Collectors.toList());
			if (uniqueLocationId != null && uniqueLocationId.size() > 0 && zoneName != null && zoneName.length() > 0) {
				circleWiseCounts = userRoleMappingRepository.circleWiseFoCount(locationDistId, zoneName, list,
						categoryList);
			}
			model.addAttribute("circleWiseCounts", circleWiseCounts);
			model.addAttribute("distName", zoneName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/landingDrillDown";
	}

	@GetMapping("/landingDrillDownUser")
	public String drillDownUser(Model model, @RequestParam(name = "year", required = false) String year,
			@RequestParam(name = "userId", required = false) String userId,
			@RequestParam(name = "circleId", required = false) String circleId,
			@RequestParam(name = "categoryId", required = false) String categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 7);
			List<EnforcementReviewCase> enforcementReviewList = new ArrayList<EnforcementReviewCase>();
			Integer userIdInt = Integer.parseInt(userId);
			Long category = Long.parseLong(categoryId);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			// Remove duplicates by converting to a Set and back to a List
			Set<String> uniqueLocationIdSet = new HashSet<>(locationId);
			List<String> uniqueLocationId = new ArrayList<>(uniqueLocationIdSet);
			List<String> locationDistId = uniqueLocationId.stream().filter(loc -> loc.startsWith("C"))
					.collect(Collectors.toList());
			List<EnforcementReviewCase> enforcementReviewCaseList = enforcementReviewCaseRepository
					.enforcementReviewCaseListFoWise(circleId, year, userIdInt, category);
			for (EnforcementReviewCase e : enforcementReviewCaseList) {
				e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
				e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
						e.getId().getPeriod(), e.getId().getCaseReportingDate()));
				enforcementReviewList.add(e);
			}
			model.addAttribute("caseList", enforcementReviewCaseList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/landingDrillDownUser";
	}

	@SuppressWarnings("null")
	@GetMapping("/landingDrillDownPeriodWiseList")
	public String drillPeriodWiseList(Model model, @RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "index", required = false) String index) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			List<EnforcementReviewCase> enforcementReviewList = new ArrayList<EnforcementReviewCase>();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 7);
			List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			int indexId = Integer.parseInt(index);
			List<EnforcementReviewCase> consolidateCategoryWiseDataList = fieldUserServiceImpl
					.getPeriodWiseCasesList(returnWorkingLocation(userDetails.getUserId()), category, indexId);
			for (EnforcementReviewCase e : consolidateCategoryWiseDataList) {
				e.setAssignedTo(userRoleRepository.findByroleCode(e.getAssignedTo()).get().getRoleName());
				e.setFileName(enforcementReviewCaseRepository.findFileNameByGstin(e.getId().getGSTIN(),
						e.getId().getPeriod(), e.getId().getCaseReportingDate()));
				enforcementReviewList.add(e);
			}
			model.addAttribute("caseList", enforcementReviewList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/landingDrillDownUser";
	}

	@GetMapping("/" + ApplicationConstants.DASHBOARD + "/officer_wise_pendency")
	public String officerWisePendency(Model model) {
		setViewMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.VW + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.VW_REVIEW_SUMMARY_LIST)
	public String reviewSummaryList(Model model, @RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) Integer parameterId) {
		setViewMenu(model, ApplicationConstants.VW_REVIEW_SUMMARY_LIST);
//		model.addAttribute("categoryTotals", HQReviewSummeryList.getExtensionTotals());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
				userRoleRepository.findByroleCode("VW").get());
		List<String> locationList = adminUpdateUserDetails.getAllMappedLocationsFromUserRoleMappingList(urmList);
		List<String> exceptActionList = Arrays.asList("ABCD");
		model.addAttribute("categories", categoryListRepository
				.findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(locationList, exceptActionList));
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
		return "/vw/review_summary_list_new";
	}

	@GetMapping("/view_list_of_case")
	public String viewlistOfCase(Model model, @RequestParam(required = false) String category, String parameterId) {
		try {
			setViewMenu(model, ApplicationConstants.VW_VIEW_LIST_OF_CASE);
			List<String[]> caseList = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<UserRoleMapping> urmList = userRoleMappingRepository.findAllByUserDetailsAndUserRole(userDetails,
					userRoleRepository.findByroleCode("VW").get());
			List<String> locationList = adminUpdateUserDetails.getAllMappedLocationsFromUserRoleMappingList(urmList);
			List<String> exceptActionList = Arrays.asList("");
			Category categoryObject = categoryListRepository.findByName(category);
			if (categoryObject != null) {
				model.addAttribute("categoryId", categoryObject.getId());
			}
			if (category != null && category.length() > 0 && parameterId != null && parameterId.length() > 0) {
				caseList = enforcementReviewCaseRepository
						.findByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(category, parameterId,
								locationList, exceptActionList);
			} else if (category != null && category.length() > 0) {
				caseList = enforcementReviewCaseRepository
						.findReviewCasesListByCategoryAndLocationListAndExceptActionList(category, locationList,
								exceptActionList);
			}
			if (caseList != null && caseList.size() > 0) {
				model.addAttribute("caseList", caseList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/" + ApplicationConstants.VW_VIEW_LIST_OF_CASE + "_new";
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

	@GetMapping("/" + ApplicationConstants.VW_PERIOD_WISE_REPORT)
	public String viewPeriodWiseReport(Model model) {
		setViewMenu(model, ApplicationConstants.VW_MIS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<FoConsolidateCategoryWiseCaseData> consolidateCategoryWiseDataList = fieldUserServiceImpl
				.getTotalCategoryWiseCasesList(returnWorkingLocation(userDetails.getUserId()));
		model.addAttribute("consolidateCategoryWiseDataList", consolidateCategoryWiseDataList);
		return "/vw/view_period_wise_report";
	}

	@GetMapping("/" + ApplicationConstants.VW_MANUALLY_UPLOADED_CASE_REPORT)
	public String viewManuallyUploadedCaseReport(Model model,
			@RequestParam(name = "view", required = false) String view) {
		setViewMenu(model, ApplicationConstants.VW_MIS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String location = "C%";
		List<Object[]> oldCaseList = userRoleMappingRepository.getOldCasesManuallyUploaded(location);
		model.addAttribute("oldCaseList", oldCaseList);
		model.addAttribute("location", view);
		return "/vw/view_manually_uploaded_case_report";
	}

	@PostMapping("/submit_manually_uploaded_case_report")
	public String submitManuallyUploadedCaseReport(Model model,
			@ModelAttribute("workFlowModel") WorkFlowModel workFlowModel, RedirectAttributes redirectAttrs) {
		String view = workFlowModel.getCategory();
		return "redirect:view_manually_uploaded_case_report?view=" + view;
	}

	@SuppressWarnings("null")
	@GetMapping("/landingDrillDownOldCaseList")
	public String drillDownOldCaseList(Model model,
			@RequestParam(name = "location", required = false) String location) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			List<Object[]> oldCaseList = userRoleMappingRepository.getOldCasesManuallyUploadedByLocation(location);
			model.addAttribute("oldCaseList", oldCaseList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApplicationConstants.VW + "/landingDrillDownOldCase";
	}

	private void setViewMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.VW));
			model.addAttribute("misSubMenuList", userRoleMappingRepository.findSubMenu());
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
					model.addAttribute("commonUserDetails", "/vw/user_details");
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

	private List<String> returnWorkingLocation(Integer currentUserId) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId,
				userRoleRepository.findByroleCode("VW").get().getId());
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

	/**************** return locations name end ******************/
	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setViewMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	@GetMapping("/" + ApplicationConstants.ANNEXURE7_REPORT)
    public String showReport(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> objectUserDetails = userDetailsRepository
				.findByloginNameIgnoreCase(authentication.getName());
		if (!objectUserDetails.isPresent()) {
			return "redirect:/logout";
		}
		UserDetails userDetails = objectUserDetails.get();
		
		// Getting all user role mapping for this user and getting all mapped location

		List<UserRoleMapping> roleMappingList = userRoleMappingRepository
				.findLocationByRolebased(userDetails.getUserId(), 7);
		List<String> locationId = fieldUserController.getAllMappedLocationsFromUserRoleMapping(roleMappingList);
		// Set Audit Cases

    	
		setViewMenu(model, ApplicationConstants.ANNEXURE7_REPORT);
        List<AnnexureReportRow> report = viewServiceImpl.getAnnexureReport(locationId);
        model.addAttribute("reportData", report);
      
        return ApplicationConstants.VW + "/"+ ApplicationConstants.ANNEXURE7_REPORT;
    }
}
