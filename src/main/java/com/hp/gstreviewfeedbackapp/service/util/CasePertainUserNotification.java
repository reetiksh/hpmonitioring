package com.hp.gstreviewfeedbackapp.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.MstCaseNotificationCategoryRepository;
import com.hp.gstreviewfeedbackapp.repository.MstNotificationsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.sun.istack.logging.Logger;

@Component
public class CasePertainUserNotification {

	private static final Logger logger = Logger.getLogger(CasePertainUserNotification.class);

	@Autowired
	private MstNotificationsRepository mstNotificationsRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private MstCaseNotificationCategoryRepository mstCaseNotificationCategoryRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/*******************************
	 * Insert Notification When HQ Upload The Cases Start
	 * 
	 * @throws ParseException
	 ***************************/
	public void caseUploadNotification(ExtensionNoDocument extensionNoDocument) throws ParseException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		/*
		 * if(userDetails==null) { return "redirect:/logout"; }
		 */
		List<MstNotifications> mstNotificationsList = new ArrayList<>();
		List<EnforcementReviewCase> uploadCases = extensionNoDocument.getEnforcementReviewCases();

		// MstCaseNotificationCategory mstCaseNotificationUploadCategory =
		// mstCaseNotificationCategoryRepository.getNotificationCategoryById(2l).getTemplate();
		String hqUploadtemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(2l)
				.getTemplate();
		String foUploadtemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(3l)
				.getTemplate();
		String ruUploadtemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(6l)
				.getTemplate();

		for (EnforcementReviewCase uploadSoloCase : uploadCases) {
			MstNotifications mstNotifications = new MstNotifications();
			String tempHqNotificationString = hqUploadtemplateString;
			mstNotifications.setAction("upload");
			mstNotifications.setNotificationPertainTo(userDetails.getUserId());
			mstNotifications.setActiveStatus(true);
			mstNotifications.setAssignedTo("HQ");
			mstNotifications.setCaseReportingDate(uploadSoloCase.getId().getCaseReportingDate());
			/*****************************
			 * Replacement of Data Start
			 *****************************/
			tempHqNotificationString = tempHqNotificationString.replace("{#gstin#}", uploadSoloCase.getId().getGSTIN());
			tempHqNotificationString = tempHqNotificationString.replace("{#caseReportingDate#}", outputDateFormat
					.format(dateFormat.parse(uploadSoloCase.getId().getCaseReportingDate().toString())));
			tempHqNotificationString = tempHqNotificationString.replace("{#period#}",
					uploadSoloCase.getId().getPeriod());
			/*****************************
			 * Replacement of Data End
			 *****************************/
			mstNotifications.setDescription(tempHqNotificationString);
			mstNotifications.setGstin(uploadSoloCase.getId().getGSTIN());
			mstNotifications.setPeriod(uploadSoloCase.getId().getPeriod());
			mstNotifications.setUpdatingDate(new Date());
			mstNotifications
					.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(2l));
			mstNotifications.setLocationDetails(uploadSoloCase.getLocationDetails());
			mstNotificationsList.add(mstNotifications);
		}

		for (EnforcementReviewCase uploadSoloCase : uploadCases) {
			MstNotifications mstNotifications = new MstNotifications();
			String tempFoNotificationString = foUploadtemplateString;
			mstNotifications.setAction("upload");
			mstNotifications.setNotificationPertainTo(0);
			mstNotifications.setActiveStatus(true);
			mstNotifications.setAssignedTo("FO");
			mstNotifications.setCaseReportingDate(uploadSoloCase.getId().getCaseReportingDate());
			/*****************************
			 * Replacement of Data Start
			 *****************************/
			tempFoNotificationString = tempFoNotificationString.replace("{#gstin#}", uploadSoloCase.getId().getGSTIN());
			tempFoNotificationString = tempFoNotificationString.replace("{#caseReportingDate#}", outputDateFormat
					.format(dateFormat.parse(uploadSoloCase.getId().getCaseReportingDate().toString())));
			tempFoNotificationString = tempFoNotificationString.replace("{#period#}",
					uploadSoloCase.getId().getPeriod());
			/*****************************
			 * Replacement of Data End
			 *****************************/
			mstNotifications.setDescription(tempFoNotificationString);
			mstNotifications.setGstin(uploadSoloCase.getId().getGSTIN());
			mstNotifications.setPeriod(uploadSoloCase.getId().getPeriod());
			mstNotifications.setUpdatingDate(new Date());
			mstNotifications
					.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(3l));
			mstNotifications.setLocationDetails(uploadSoloCase.getLocationDetails());
			mstNotificationsList.add(mstNotifications);
		}

		for (EnforcementReviewCase uploadSoloCase : uploadCases) {
			MstNotifications mstNotifications = new MstNotifications();
			String tempFoNotificationString = ruUploadtemplateString;
			mstNotifications.setAction("upload");
			mstNotifications.setNotificationPertainTo(0);
			mstNotifications.setActiveStatus(true);
			mstNotifications.setAssignedTo("RU");
			mstNotifications.setCaseReportingDate(uploadSoloCase.getId().getCaseReportingDate());
			/*****************************
			 * Replacement of Data Start
			 *****************************/
			tempFoNotificationString = tempFoNotificationString.replace("{#gstin#}", uploadSoloCase.getId().getGSTIN());
			tempFoNotificationString = tempFoNotificationString.replace("{#caseReportingDate#}", outputDateFormat
					.format(dateFormat.parse(uploadSoloCase.getId().getCaseReportingDate().toString())));
			tempFoNotificationString = tempFoNotificationString.replace("{#period#}",
					uploadSoloCase.getId().getPeriod());
			tempFoNotificationString = tempFoNotificationString.replace("{#caseCatgory#}",
					uploadSoloCase.getCategory());
			/*****************************
			 * Replacement of Data End
			 *****************************/
			mstNotifications.setDescription(tempFoNotificationString);
			mstNotifications.setGstin(uploadSoloCase.getId().getGSTIN());
			mstNotifications.setPeriod(uploadSoloCase.getId().getPeriod());
			mstNotifications.setUpdatingDate(new Date());
			mstNotifications
					.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(3l));
			mstNotifications.setLocationDetails(uploadSoloCase.getLocationDetails());
			mstNotificationsList.add(mstNotifications);
		}

		logger.info("Notification save");

		mstNotificationsRepository.saveAll(mstNotificationsList);
	}

	/*******************************
	 * Insert Notification When HQ Upload The Cases End
	 ***************************/

	/************************
	 * Insert Nofification Entry when assign from Low to High Level Start
	 * 
	 * @throws ParseException
	 ***********************/
	public void insertAssignNotification(String gstin, Date caseReportingDate, String period, String assignTo,
			LocationDetails locationDetails, String action, Integer notificationPertainTo) throws ParseException {
		String assigneTemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(3l)
				.getTemplate();
		MstNotifications mstNotifications = new MstNotifications();
		String tempNotificationString = assigneTemplateString;
		mstNotifications.setAction(action);
		mstNotifications.setNotificationPertainTo(notificationPertainTo);
		mstNotifications.setActiveStatus(true);
		mstNotifications.setAssignedTo(assignTo);
		mstNotifications.setCaseReportingDate(caseReportingDate);
		/*****************************
		 * Replacement of Data Start
		 *****************************/
		tempNotificationString = tempNotificationString.replace("{#gstin#}", gstin);
		tempNotificationString = tempNotificationString.replace("{#caseReportingDate#}",
				outputDateFormat.format(caseReportingDate));
		tempNotificationString = tempNotificationString.replace("{#period#}", period);
		/*****************************
		 * Replacement of Data End
		 *****************************/
		mstNotifications.setDescription(tempNotificationString);
		mstNotifications.setGstin(gstin);
		mstNotifications.setPeriod(period);
		mstNotifications.setUpdatingDate(new Date());
		mstNotifications
				.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(3l));
		mstNotifications.setLocationDetails(locationDetails);
		mstNotificationsRepository.save(mstNotifications);
	}

	/************************
	 * Insert Nofification Entry when assign from Low to High Level End
	 ***********************/

	/************************
	 * Insert Nofification Entry when assign from High to Low Level Start
	 * 
	 * @throws ParseException
	 ***********************/
	public void insertReAssignNotification(String gstin, Date caseReportingDate, String period, String assignTo,
			LocationDetails locationDetails, String action, Integer notificationPertainTo, String assignedTo,
			String assignedFrom) throws ParseException {
		String assigneTemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(4l)
				.getTemplate();
		MstNotifications mstNotifications = new MstNotifications();
		String tempNotificationString = assigneTemplateString;
		mstNotifications.setAction(action);
		mstNotifications.setNotificationPertainTo(notificationPertainTo);
		mstNotifications.setActiveStatus(true);
		mstNotifications.setAssignedTo(assignTo);
		mstNotifications.setCaseReportingDate(caseReportingDate);
		/*****************************
		 * Replacement of Data Start
		 *****************************/
		tempNotificationString = tempNotificationString.replace("{#gstin#}", gstin);
		tempNotificationString = tempNotificationString.replace("{#caseReportingDate#}",
				outputDateFormat.format(caseReportingDate));
		tempNotificationString = tempNotificationString.replace("{#period#}", period);
		tempNotificationString = tempNotificationString.replace("{#reAssignedFrom#}", assignedFrom);
		tempNotificationString = tempNotificationString.replace("{#reAssignedTo#}", assignedTo);

		/*****************************
		 * Replacement of Data End
		 *****************************/
		mstNotifications.setDescription(tempNotificationString);
		mstNotifications.setGstin(gstin);
		mstNotifications.setPeriod(period);
		mstNotifications.setUpdatingDate(new Date());
		mstNotifications
				.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(4l));
		mstNotifications.setLocationDetails(locationDetails);
		mstNotificationsRepository.save(mstNotifications);
	}

	/************************
	 * Insert Nofification Entry when assign from High to Low Level End
	 ***********************/

	/************************
	 * Insert Nofification Entry when assign from Low to High Level Start
	 * 
	 * @throws ParseException
	 ***********************/
	public void insertClosedNotificationByApprover(String gstin, Date caseReportingDate, String period, String assignTo,
			LocationDetails locationDetails, String action, Integer notificationPertainTo) throws ParseException {
		String assigneTemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(5l)
				.getTemplate();
		MstNotifications mstNotifications = new MstNotifications();
		String tempFoNotificationString = assigneTemplateString;
		mstNotifications.setAction(action);
		mstNotifications.setNotificationPertainTo(notificationPertainTo);
		mstNotifications.setActiveStatus(true);
		mstNotifications.setAssignedTo(assignTo);
		mstNotifications.setCaseReportingDate(caseReportingDate);
		/*****************************
		 * Replacement of Data Start
		 *****************************/
		tempFoNotificationString = tempFoNotificationString.replace("{#gstin#}", gstin);
		tempFoNotificationString = tempFoNotificationString.replace("{#caseReportingDate#}",
				outputDateFormat.format(caseReportingDate));
		tempFoNotificationString = tempFoNotificationString.replace("{#period#}", period);
		/*****************************
		 * Replacement of Data End
		 *****************************/
		mstNotifications.setDescription(tempFoNotificationString);
		mstNotifications.setGstin(gstin);
		mstNotifications.setPeriod(period);
		mstNotifications.setUpdatingDate(new Date());
		mstNotifications
				.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(5l));
		mstNotifications.setLocationDetails(locationDetails);
		mstNotificationsRepository.save(mstNotifications);
	}

	/************************
	 * Insert Nofification Entry when assign from Low to High Level End
	 ***********************/

	/************************
	 * Insert Nofification Entry when casId is approved/Rejected by Verifier Start
	 * 
	 * @throws ParseException
	 ***********************/
	public void insertReAssignNotificationForApprovedOrRejectedCaseIdUpdationRequest(String gstin,
			Date caseReportingDate, String period, String assignTo, LocationDetails locationDetails, String action,
			Integer notificationPertainTo, String caseStatus) throws ParseException {
		String assigneTemplateString = mstCaseNotificationCategoryRepository.getNotificationCategoryById(7l)
				.getTemplate();
		MstNotifications mstNotifications = new MstNotifications();
		String tempNotificationString = assigneTemplateString;
		mstNotifications.setAction(action);
		mstNotifications.setNotificationPertainTo(notificationPertainTo);
		mstNotifications.setActiveStatus(true);
		mstNotifications.setAssignedTo(assignTo);
		mstNotifications.setCaseReportingDate(caseReportingDate);
		/*****************************
		 * Replacement of Data Start
		 *****************************/
		tempNotificationString = tempNotificationString.replace("{#gstin#}", gstin);
		tempNotificationString = tempNotificationString.replace("{#caseReportingDate#}",
				outputDateFormat.format(caseReportingDate));
		tempNotificationString = tempNotificationString.replace("{#period#}", period);
		tempNotificationString = tempNotificationString.replace("{#caseIdStatus#}", caseStatus);

		/*****************************
		 * Replacement of Data End
		 *****************************/
		mstNotifications.setDescription(tempNotificationString);
		mstNotifications.setGstin(gstin);
		mstNotifications.setPeriod(period);
		mstNotifications.setUpdatingDate(new Date());
		mstNotifications
				.setCaseNotificationCategory(mstCaseNotificationCategoryRepository.getNotificationCategoryById(7l));
		mstNotifications.setLocationDetails(locationDetails);
		mstNotificationsRepository.save(mstNotifications);
	}

	/************************
	 * Insert Nofification Entry when casId is approved/Rejected by Verifier End
	 ***********************/

	/****************************
	 * Display User Notification Start
	 *********************************************/
	public List<MstNotifications> getNotificationPertainToUser(UserDetails userDetails, String userRole) {
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(
				userDetails.getUserId(), userRoleRepository.findByroleCode(userRole).get().getId());
		List<String> workingLocationsList = getAllMappedLocationsFromUserRoleMapping(userRoleMapList);
		List<MstNotifications> notificationList = mstNotificationsRepository
				.findNotificationsPertainToUser(userDetails.getUserId(), userRole, workingLocationsList);
		return notificationList;
	}

	/****************************
	 * Display User Notification End
	 *********************************************/

	/****************************
	 * Return Notifications which has not been read by user Start
	 *********************************************/
	public List<MstNotifications> getUnReadNotificationPertainToUser(UserDetails userDetails, String userRole) {
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(
				userDetails.getUserId(), userRoleRepository.findByroleCode(userRole).get().getId());
		List<String> workingLocationsList = getAllMappedLocationsFromUserRoleMapping(userRoleMapList);
		List<MstNotifications> unreadNotificationList = mstNotificationsRepository
				.findUnreadNotificationsPertainToUser(userDetails.getUserId(), userRole, workingLocationsList);
		return unreadNotificationList;
	}

	/****************************
	 * Return Notifications which has not been read by user End
	 *********************************************/

	/*************** return working location start *************************/
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
						.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())) // commented as per
																									// requirement
																									// verifier can not
																									// be assigned to at
																									// circle level.
						.collect(Collectors.toList());

				if (!circleIds.isEmpty()) {
					for (UserRoleMapping circleIdsSolo : circleIds) {
						workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
					}
				}

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
		} catch (Exception e) {
			logger.info("CasePertainUserNotification : getAllMappedLocationsFromUserRoleMapping : " + e.getMessage());
		}
		return workingLocationsIds;
	}
	/*************** return working location end *************************/

}
