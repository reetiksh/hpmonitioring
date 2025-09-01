package com.hp.gstreviewfeedbackapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksCategoryWise;
import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksDetails;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ReviewMeetingModel;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.ReviewMeetingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.RM)
public class ReviewMeetingUserController {

	private static final Logger logger = LoggerFactory.getLogger(ReviewMeetingUserController.class);

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
	private LocationDetailsRepository locationDetailsRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private ReviewMeetingRepository reviewMeetingRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private ReviewMeetingDetailsRepository reviewMeetingDetailsRepository;

	@Autowired
	private CategoryListRepository categoryListRepository;

	@Autowired
	private CustomUtility cutomUtility;

	@Value("${action.upload}")
	private String actionUpload;

	@Value("${file.upload.location}")
	private String uploadPath;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String dashboard(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}

		setRMMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.RM + "/" + ApplicationConstants.DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.RM_Review_CASES_LIST)
	public String reviewMeetingList(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}

		try {

			int categoryListCount = 0;

			setRMMenu(model, ApplicationConstants.RM_Review_CASES_LIST);

			List<EnforcementReviewCaseModel> list = new ArrayList<EnforcementReviewCaseModel>();

			List<EnforcementReviewCaseModel> gstlist = new ArrayList<EnforcementReviewCaseModel>();

			List<Category> obj = categoryListRepository.findAllCategoryforReview();

			List<java.util.Map<String, Object>> gstObj = categoryRepository.getDetailedCasesDetails();

			categoryListCount = reviewMeetingRepository.findAll().size();

			for (Category objcategory : obj) {

				EnforcementReviewCaseModel category = new EnforcementReviewCaseModel();

				String oldremarks = "";

				Long categoryId = objcategory.getId();

				String categoryName = objcategory.getName();

				if (categoryListCount > 1) {

					EnforcementCasesRemarksCategoryWise categoryReviewMeetingList = reviewMeetingRepository
							.findTopByCategoryIdOrderByIdDesc(categoryId);

					try {

						oldremarks = categoryReviewMeetingList.getRemarks();

					} catch (Exception e) {

						e.printStackTrace();
					}

				}

				System.out.println("categoryReviewMeetingList : " + categoryId + " remarks : " + oldremarks);

				category.setCategoryId(categoryId);

				category.setCategory(categoryName);

				category.setRemarks(oldremarks);

				list.add(category);

			}

			for (java.util.Map<String, Object> cases : gstObj) {

				EnforcementReviewCaseModel record = new EnforcementReviewCaseModel();

				String actionStausName = null;

				String caseStageName = null;

				String recoveryStageName = null;

				String circleName = null;

				String gstin = (String) cases.get("gstin");

				Date caseReportingDate = (Date) cases.get("case_reporting_date");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				dateFormat.setLenient(false);
				String strDate = dateFormat.format(caseReportingDate);

				String period = (String) cases.get("period");

				String oldRemark = getDetailedGSTNRemarks(gstin, caseReportingDate, period);

				String action = (String) cases.get("action");

				String assignTo = (String) cases.get("assigned_to");

				String category = (String) cases.get("category");

				Long demand = (Long) cases.get("demand");

				Long indicativeTax = (Long) cases.get("indicative_tax_value");

				Long recoveryAgainstDemand = (Long) cases.get("recovery_against_demand");

				Long recoveryByDrc = (Long) cases.get("recovery_by_drc3");

				String circleId = (String) cases.get("working_location");

				String taxpayerName = (String) cases.get("taxpayer_name");

				Integer actionStausId = (Integer) cases.get("action_status");

				Integer caseStausId = (Integer) cases.get("case_stage");

				Integer recoveryStageId = (Integer) cases.get("recovery_stage");

				Long categoryId = categoryListRepository.findByName(category).getId();

				circleName = locationDetailsRepository.findByLocationId(circleId).get().getLocationName();

				if (actionStausId == null) {

					actionStausName = "";

				} else {

					actionStausName = actionStatusRepository.findById((int) actionStausId).get().getName();
				}

				if (caseStausId == null) {

					caseStageName = "";

				} else {

					caseStageName = caseStageRepository.findById((int) caseStausId).get().getName();

				}

				if (recoveryStageId == null) {

					recoveryStageName = "";

				} else {

					recoveryStageName = recoveryStageRepository.findById((int) recoveryStageId).get().getName();

				}

				record.setGSTIN_ID(gstin);
				record.setDemand(demand != null ? demand : null);
				record.setIndicativeTaxVal(indicativeTax);
				record.setPeriod_ID(period);
				record.setRecoveryAgainstDemand(recoveryAgainstDemand != null ? recoveryAgainstDemand : null);
				record.setRecoveryByDRC3(recoveryByDrc != null ? recoveryByDrc : null);
				record.setTaxpayerName(taxpayerName);
				record.setActionStatusName(actionStausName);
				record.setCaseStageName(caseStageName);
				record.setRecoveryStageName(recoveryStageName);
				record.setCircle(circleName);
				record.setCategory(category);
				record.setCaseReportingDate_ID(strDate);
				record.setAssignedTo(assignTo);
				record.setRemarks(oldRemark);
				record.setCategoryId(categoryId);
				record.setDate(caseReportingDate);

				gstlist.add(record);

			}

			EnforcementCasesRemarksCategoryWise oldRemarks = reviewMeetingRepository.findTopByOrderByIdDesc();

			model.addAttribute("oldRemarks", oldRemarks);
			model.addAttribute("list", list);
			model.addAttribute("gstlist", gstlist);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " + e.getMessage());
		}

		return ApplicationConstants.RM + "/" + ApplicationConstants.RM_Review_CASES_LIST;

	}

	@PostMapping("/update_category_remarks")
	public String updateCategorywiseReviewMeetingComments(Model model,
			@ModelAttribute("category_remarks") ReviewMeetingModel reviewMeetingModel, RedirectAttributes redirect,
			BindingResult bindingResult) {

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}

			setRMMenu(model, ApplicationConstants.RM_Review_CASES_LIST);

			List<Category> obj = categoryListRepository.findAllCategoryforReview();

			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

			dateFmt.setLenient(false);

			String reviewMeetingDate = reviewMeetingModel.getReviewMeetingDate();

			Date parseDate = dateFmt.parse(reviewMeetingDate);

			MultipartFile meetingDocument = reviewMeetingModel.getMeetingDocument();

			String orginalFileName = meetingDocument.getOriginalFilename();

			String timestamp = String.valueOf(new Date().getTime());

			String fileName = "review_meeting_remarks_" + timestamp + "_" + orginalFileName;

			File file = new File(uploadPath + fileName);

			byte[] bytesArray = meetingDocument.getBytes();

			OutputStream outputStream = new FileOutputStream(file);

			outputStream.write(bytesArray);

			String[] categoryremarks = reviewMeetingModel.getCategoryremarks();

			for (int i = 0; i < obj.size(); i++) {

				EnforcementCasesRemarksCategoryWise enforcementCasesRemarksCategoryWise = new EnforcementCasesRemarksCategoryWise();

				enforcementCasesRemarksCategoryWise.setReviewMeetingDate(parseDate);

				enforcementCasesRemarksCategoryWise.setRecordCreationDate(new Date());

				enforcementCasesRemarksCategoryWise.setMomDocument(fileName);

				enforcementCasesRemarksCategoryWise.setCategoryId(obj.get(i).getId());

				enforcementCasesRemarksCategoryWise.setRemarks(categoryremarks[i]);

				reviewMeetingRepository.save(enforcementCasesRemarksCategoryWise);

			}

			String[] remarks = reviewMeetingModel.getRemarks();

			String[] gstnId = reviewMeetingModel.getGstin();

			String[] caseReporingDate = reviewMeetingModel.getCaseReportingDateId();

			String[] period = reviewMeetingModel.getPeriod();

			Long[] categoryId = reviewMeetingModel.getCatogy();

			for (int i = 0; i < gstnId.length; i++) {

				Date parsedDate = null;
				String remark = null;
				String gstn = null;
				String peiodStr = null;
				Long category = null;

				EnforcementCasesRemarksDetails enforcementCasesRemarksDetails = new EnforcementCasesRemarksDetails();

				remark = remarks[i];

				gstn = gstnId[i];

				parsedDate = dateFmt.parse(caseReporingDate[i]);

				peiodStr = period[i];

				category = categoryId[i];

				enforcementCasesRemarksDetails.setMomDocument(fileName);

				enforcementCasesRemarksDetails.setRecordCreationDate(new Date());

				enforcementCasesRemarksDetails.setRemarks(remark);

				enforcementCasesRemarksDetails.setReviewMeetingDate(parseDate);

				enforcementCasesRemarksDetails.setGSTIN(gstn);

				enforcementCasesRemarksDetails.setCaseReportingDate(parsedDate);

				enforcementCasesRemarksDetails.setCategoryId(category);

				enforcementCasesRemarksDetails.setPeriod(peiodStr);

				reviewMeetingDetailsRepository.save(enforcementCasesRemarksDetails);

			}

			logger.info("Review meeting comments submitted");

			redirect.addFlashAttribute("message", "Review meeting comments submitted successfully!");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " + e.getMessage());
		}

		return "redirect:/" + ApplicationConstants.RM + "/" + ApplicationConstants.RM_Review_CASES_LIST;
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setRMMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}

	public String getDetailedGSTNRemarks(String gstin, Date caseReportingDate, String period) {

		String remarks = "";
		EnforcementCasesRemarksDetails enforcementCasesRemarksDetails = null;

		try {

			Boolean isPresent = reviewMeetingDetailsRepository.existsByGSTINAndCaseReportingDateAndPeriod(gstin,
					caseReportingDate, period);

			if (isPresent) {

				enforcementCasesRemarksDetails = reviewMeetingDetailsRepository
						.findTopByGSTINAndCaseReportingDateAndPeriodOrderByIdDesc(gstin, caseReportingDate, period);

				remarks = enforcementCasesRemarksDetails.getRemarks();

				logger.info("remarks details :" + enforcementCasesRemarksDetails.toString());

			} else {

				remarks = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :: " + e.getMessage());
		}

		return remarks;

	}

	private void setRMMenu(Model model, String activeMenu) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
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
				model.addAttribute("commonUserDetails", "/rm/user_details");
				model.addAttribute("changeUserPassword", "/gu/change_password");
				model.addAttribute("homePageLink", "/rm/dashboard");

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

		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.RM));
		model.addAttribute("activeMenu", activeMenu);
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

}
