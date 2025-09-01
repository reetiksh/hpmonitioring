package com.hp.gstreviewfeedbackapp.scrutiny.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesStatusRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.impl.ScrutinyRuServiceImpl;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.SCRUTINY_RU)
public class ScrutinyRuController {
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyRuController.class);

	@Autowired
	private AppMenuRepository appMenuRepository;
	
	@Autowired
	private ScrutinyRuServiceImpl scrutinyRuServiceImpl;
	
	@Autowired
	private CommonUtility commonUtility;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private CustomUtility cutomUtility;
	
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	
	@Autowired
	private ScrutinyCasesStatusRepository scrutinyCasesStatusRepository;
	
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	
	@GetMapping("/"+ ApplicationConstants.DASHBOARD)
	public String dashboard(Model model) {
		logger.info("ScrutinyFoController.dashboard() : ApplicationConstants.DASHBOARD");
		setScrutinyRuMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.DASHBOARD;
	}
	
	@GetMapping("/"+ ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("ScrutinyRuController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setScrutinyRuMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES)
	public String getMandatoryCasesList(Model model,@RequestParam(required = false) String message) {
		logger.info("ScrutinyFoController.getMandatoryCasesList() : ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyRU");
		List<MstScrutinyCases> mstScrutinyCasesList = scrutinyRuServiceImpl.getMandatoryCasesList(workingLoacationList);
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		model.addAttribute("message",message);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES;
	}
	
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_RU_RECOMMEND_FOR_SCRUTINY)
	public String recommendForScrutiny(Model model, @RequestParam("recommendScrutinyGstin") String recommendScrutinyGstin,@RequestParam("recommendScrutinyPeriod") String recommendScrutinyPeriod,@RequestParam("recommendScrutinyCaseReportingDate") String recommendScrutinyCaseReportingDate,@RequestParam("recommendScrutinyRemark") String recommendScrutinyRemark) {
		logger.info("ScrutinyFoController.recommendForScrutiny() : ApplicationConstants.SCRUTINY_RU_RECOMMEND_FOR_SCRUTINY");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyRuServiceImpl.recommendForScrutiny(recommendScrutinyGstin,recommendScrutinyPeriod,recommendScrutinyCaseReportingDate,recommendScrutinyRemark);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_RU_MANDATORY_CASES + "?message=" +message;
		return url;
	}
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_RU_RANDOM_CASES)
	public String getRandomCasesList(Model model,@RequestParam(required = false) String message) {
		logger.info("ScrutinyFoController.getRandomCasesList() : ApplicationConstants.SCRUTINY_RU_RANDOM_CASES");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyRU");
		List<MstScrutinyCases> mstScrutinyCasesList = scrutinyRuServiceImpl.getRandomCasesList(workingLoacationList);
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		model.addAttribute("message",message);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_RANDOM_CASES);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.SCRUTINY_RU_RANDOM_CASES;
	}
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_RU_RANDOM_RECOMMEND_FOR_SCRUTINY)
	public String randomRecommendForScrutiny(Model model, @RequestParam("recommendScrutinyGstin") String recommendScrutinyGstin,@RequestParam("recommendScrutinyPeriod") String recommendScrutinyPeriod,@RequestParam("recommendScrutinyCaseReportingDate") String recommendScrutinyCaseReportingDate,@RequestParam("recommendScrutinyRemark") String recommendScrutinyRemark) {
		logger.info("ScrutinyFoController.randomRecommendForScrutiny() : ApplicationConstants.SCRUTINY_RU_RANDOM_RECOMMEND_FOR_SCRUTINY");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyRuServiceImpl.randomRecommendForScrutiny(recommendScrutinyGstin,recommendScrutinyPeriod,recommendScrutinyCaseReportingDate,recommendScrutinyRemark);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_RANDOM_CASES);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_RU_RANDOM_CASES + "?message=" +message;
		return url;
	}
	
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER)
	public String getRecommendedFromHqUserCasesList(Model model,@RequestParam(required = false) String message) {
		logger.info("ScrutinyFoController.getRecommendedFromHqUserCasesList() : ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyRU");
		List<MstScrutinyCases> mstScrutinyCasesList = scrutinyRuServiceImpl.getRecommendedFromHqUserCasesList(workingLoacationList);
		
		for(MstScrutinyCases mstScrutinyCasesSolo :mstScrutinyCasesList ) {
			List<String> scrutinyCaseWorkflowsList = scrutinyCaseWorkflowRepository.getScrutinyHqRemarks(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getPeriod(),mstScrutinyCasesSolo.getId().getCaseReportingDate());
			mstScrutinyCasesSolo.setHqRemarks(scrutinyCaseWorkflowsList);
		}
		
		
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		model.addAttribute("message",message);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER;
	}
	
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_RU_RECOMMENDED_FROM_HQ_FOR_SCRUTINY)
	public String recommendFromHqForScrutiny(Model model, @RequestParam("recommendScrutinyGstin") String recommendScrutinyGstin,@RequestParam("recommendScrutinyPeriod") String recommendScrutinyPeriod,@RequestParam("recommendScrutinyCaseReportingDate") String recommendScrutinyCaseReportingDate,@RequestParam("recommendScrutinyRemark") String recommendScrutinyRemark) {
		logger.info("ScrutinyFoController.recommendFromHqForScrutiny() : ApplicationConstants.SCRUTINY_RU_RECOMMENDED_FROM_HQ_FOR_SCRUTINY");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyRuServiceImpl.recommendFromHqForScrutiny(recommendScrutinyGstin,recommendScrutinyPeriod,recommendScrutinyCaseReportingDate,recommendScrutinyRemark);
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_RU_RECOMMEND_FROM_HQ_USER + "?message=" +message;
		return url;
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
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST)
	public String getScrutinyReviewCasesList(Model model) {
		logger.info("ScrutinyRuController.getScrutinyReviewCasesList() : ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST);
		model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder()); 
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST;
	}
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST +"/{id}")
	public String getScrutinyReviewCasesStatusWiseList(Model model,  @PathVariable Integer id) {
		logger.info("ScrutinyRuController.getScrutinyReviewCasesStatusWiseList() : ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST/{id}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		setScrutinyRuMenu(model, ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_LIST);
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyRU");
		List<MstScrutinyCases> reviewCasesList = scrutinyRuServiceImpl.getReviewCasesListStatusWise(id,workingLoacationList);
		model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder());
		model.addAttribute("reviewCasesList", reviewCasesList);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_RU + "/" + ApplicationConstants.SCRUTINY_RU_REVIEW_CASES_STATUS_WISE;
	}
	
	
	private void setScrutinyRuMenu(Model model, String activeMenu) { 
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,ApplicationConstants.SCRUTINY_RU));
			model.addAttribute("activeMenu",activeMenu);
			model.addAttribute("activeRole", ApplicationConstants.SCRUTINY_RU);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();

					/*********** Get Notifications Start  *************/
					List<MstNotifications> loginedUserNotificationList = casePertainUserNotification.getNotificationPertainToUser(userDetails,"ScrutinyRU");
					List<MstNotifications> unReadNotificationList = casePertainUserNotification.getUnReadNotificationPertainToUser(userDetails,"ScrutinyRU");
					model.addAttribute("unReadNotificationListCount",unReadNotificationList.size());
					model.addAttribute("unReadNotificationList",unReadNotificationList);
					model.addAttribute("loginedUserNotificationList",loginedUserNotificationList); 
					model.addAttribute("convertUnreadToReadNotifications","/scrutiny_ru/convert_unread_to_read_notifications");
					/*********** Get Notifications End  *************/
					List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
							.findByUserDetailsOrderByUserRole(userDetails);
					
					/*************************** get user locations role wise start **************************/
						List<String> userRoleMapWithLocations = cutomUtility.roleMapWithLocations(userRoleMappings,userDetails);
						model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
					/*************************** get user locations role wise end **************************/

					// All Roles of the user
					Map<String, UserRole> userRoles = new HashMap<>();
					for (UserRoleMapping objectUrm : userRoleMappings) {
						if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
							userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
						}
					}
					model.addAttribute("UserLoginName", userDetails.getLoginName());
					model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
					model.addAttribute("commonUserDetails","/scrutiny_ru/user_details");
					model.addAttribute("changeUserPassword","/gu/change_password");
					model.addAttribute("homePageLink","/scrutiny_ru/dashboard");
					
					/************************* to display user generic details start ****************************/
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
						if ((userRoleSolo.getUserRole().getRoleName() != null) && (!distinctUserRoleIds.contains(userRoleSolo.getUserRole().getId()))) {
							distinctUserRoleIds.add(userRoleSolo.getUserRole().getId());
							uniqueRolesList.add(userRoleSolo);
						}
					}
					 List<String> roleNameList = uniqueRolesList.stream()
				                .map(mapping -> mapping.getUserRole().getRoleName())
				                .collect(Collectors.toList());
					  
					  String commaSeperatedRoleList = roleNameList.stream()
				                .collect(Collectors.joining(", "));
					
					  
					String LocationsName = returnLocationsName(userRoleMappings);
					userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
					userProfileDetails.setAssignedWorkingLocations(LocationsName);
					model.addAttribute("userProfileDetails", userProfileDetails);
					
					/************************* to display user generic details end ****************************/
				}
			}
			
		}catch(Exception ex) {
			logger.error("ScrutinyUploadController : setScrutinyUploadMenu() : "+ex.getMessage());
		}
	}
	
/**************** return locations name start ******************/
	
	private String returnLocationsName(List<UserRoleMapping> userRoleMapList) {
		List<String> returnResultentSet = new ArrayList<>();
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
				.collect(Collectors.toList());
		
		
		List<UserRoleMapping> zoneIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
				.collect(Collectors.toList());
		
		List<UserRoleMapping> circleIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))
				.collect(Collectors.toList());
		
		if(!stateId.isEmpty()){
			returnResultentSet.add(stateId.get(0).getStateDetails().getStateName());  
		}
		
		if(!zoneIds.isEmpty()){
			for(UserRoleMapping zoneIdsIdsSolo : zoneIds) {
				returnResultentSet.add(zoneIdsIdsSolo.getZoneDetails().getZoneName());  
			}
		}
		
		if(!circleIds.isEmpty()){
			for(UserRoleMapping circleIdsSolo : circleIds) {
				returnResultentSet.add(circleIdsSolo.getCircleDetails().getCircleName()); 
			}
		}
		
		 String commaSeperatedLocationsNameList = returnResultentSet.stream()
	                .collect(Collectors.joining(", "));
		
		return commaSeperatedLocationsNameList;
	}
	
	/**************** return locations name end ******************/
}
