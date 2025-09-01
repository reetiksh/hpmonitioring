package com.hp.gstreviewfeedbackapp.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.FoReviewCase;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;
import com.hp.gstreviewfeedbackapp.model.TransferRole;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FOUserCaseReviewRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRoleRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.VerifierCaseWorkFlowRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUserService;
import com.hp.gstreviewfeedbackapp.service.LoginService;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;

@Component
public class DeemedApprovedCases {
	private static final Logger logger = LoggerFactory.getLogger(DeemedApprovedCases.class);
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkFlowRepository;
	@Autowired
	private TransferRoleRepository transferRoleRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private FOUserCaseReviewRepository foReviewCaseRepository;
	@Autowired
	private LoginService loginService;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private VerifierCaseWorkFlowRepository verifierCaseWorkFlowRepository;
	@Value("${login.UserInactive}")
	private String userInactiveStatus;
	@Value("${login.NewUser}")
	private String newUserStatus;
	@Value("${login.UserActive}")
	private String userActiveStatus;
	@Value("${closingIntervalMonthsForDeemedApprovedCases}")
	private Long closingIntervalMonthsForDeemedApprovedCases;
	@Value("${maximunIndicativeTaxValueForDeemApproval}")
	private Long maximunIndicativeTaxValueForDeemApproval;

	// Define a scheduled task method
	// @Scheduled(cron = "00 01 00 * * *") // Run every day at 12:01 AM
	public void myScheduledTask() {
		// Your task logic goes here
		System.out.println("Scheduled task executed at: " + System.currentTimeMillis());
		List<CaseWorkflow> caseWorkflowsList = new ArrayList<>();
		List<EnforcementReviewCase> enforcementReviewList = enforcementReviewCaseRepository
				.findAllCasesPendingToVerifierMoreThanFifteenDays();
		for (EnforcementReviewCase enforcementReviewCaseSolo : enforcementReviewList) {
			enforcementReviewCaseSolo.setAssignedFrom("RU");
			enforcementReviewCaseSolo.setAssignedTo("AP");
			enforcementReviewCaseSolo.setAction("verifyerRecommended");
			enforcementReviewCaseSolo.setCaseUpdateDate(new Date());
		}
		enforcementReviewCaseRepository.saveAll(enforcementReviewList);
		/***********
		 * save all cases logs in caseworkflow table that was pending on verifier level
		 * for 15 or more days start
		 ************/
		for (EnforcementReviewCase enforcementReviewCaseSolo : enforcementReviewList) {
			CaseWorkflow cashWorkFlow = new CaseWorkflow();
			cashWorkFlow.setGSTIN(enforcementReviewCaseSolo.getId().getGSTIN());
			cashWorkFlow.setAction("verifyerRecommended");
			cashWorkFlow.setAssignedFrom(enforcementReviewCaseSolo.getAssignedFrom());
			cashWorkFlow.setAssignedFromLocationId(enforcementReviewCaseSolo.getLocationDetails().getLocationId());
			cashWorkFlow.setAssignedFromUserId(enforcementReviewCaseSolo.getAssignedFromUserId());
			cashWorkFlow.setAssignedTo(enforcementReviewCaseSolo.getAssignedTo());
			cashWorkFlow.setAssignedToLocationId(enforcementReviewCaseSolo.getLocationDetails().getLocationId());
			cashWorkFlow.setCaseReportingDate(enforcementReviewCaseSolo.getId().getCaseReportingDate());
			cashWorkFlow.setOtherRemarks("deemed approved");
			cashWorkFlow.setPeriod(enforcementReviewCaseSolo.getId().getPeriod());
			cashWorkFlow.setSuggestedJurisdiction(enforcementReviewCaseSolo.getLocationDetails().getLocationId());
			cashWorkFlow.setUpdatingDate(new Date());
			caseWorkflowsList.add(cashWorkFlow);
		}
		caseWorkFlowRepository.saveAll(caseWorkflowsList);
		/***********
		 * save all cases logs in caseworkflow table that was pending on verifier level
		 * for 15 or more days end
		 ************/
	}

	@Scheduled(cron = "00 01 00 * * *")
	@Transactional
	public void closeDeemApprovedCases() {
		try {
			logger.info("DeemedApprovedCases : closeDeemApprovedCases : Scheduled runing for Deemed Approved Cases");
			List<EnforcementReviewCase> deemApprovedCases = enforcementReviewCaseRepository
					.findAllDeemedApprovedCasesbyLastUpdatedTimeStampMoreThanMonthsOrPeriodThresholdDates(
							closingIntervalMonthsForDeemedApprovedCases);
			if (deemApprovedCases != null && deemApprovedCases.size() > 0) {
				deemApprovedCases.forEach(e -> {
					e.setAction("closed");
					e.setAssignedFrom("AP");
					e.setAssignedTo("HQ");
					e.setCaseUpdateDate(new Date());
				});
				List<CaseWorkflow> caseWorkflowList = getCaseWorkflowListByEnforcementReviewCaseList(deemApprovedCases);
				enforcementReviewCaseRepository.saveAll(deemApprovedCases);
				caseWorkFlowRepository.saveAll(caseWorkflowList);
				logger.info("DeemedApprovedCases : closeDeemApprovedCases : Total closed cases : "
						+ deemApprovedCases.size());
			}
		} catch (Exception e) {
			logger.error("DeemedApprovedCases : closeDeemApprovedCases : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<CaseWorkflow> getCaseWorkflowListByEnforcementReviewCaseList(
			List<EnforcementReviewCase> deemApprovedCases) {
		List<CaseWorkflow> caseWorkflowList = new ArrayList<>();
		for (EnforcementReviewCase erc : deemApprovedCases) {
			CaseWorkflow caseWorkflow = new CaseWorkflow();
			EnforcementReviewCaseAssignedUsers assignedUsers = enforcementReviewCaseAssignedUsersRepository
					.findById(erc.getId()).get();
			caseWorkflow.setGSTIN(erc.getId().getGSTIN());
			caseWorkflow.setCaseReportingDate(erc.getId().getCaseReportingDate());
			caseWorkflow.setPeriod(erc.getId().getPeriod());
			caseWorkflow.setAssignedFrom(erc.getAssignedFrom());
			caseWorkflow.setAssignedFromUserId(assignedUsers.getApUserId());
			caseWorkflow.setAssignedTo(erc.getAssignedTo());
			caseWorkflow.setAssigntoUserId(assignedUsers.getHqUserId());
			caseWorkflow.setAssignedToLocationId("HP");
			caseWorkflow.setAssignedToLocationName(
					locationDetailsRepository.findByLocationId("HP").get().getLocationName());
			caseWorkflow.setUpdatingDate(erc.getCaseUpdateDate());
			caseWorkflow.setAction(erc.getAction());
			caseWorkflow.setAssignedFromLocationId(erc.getLocationDetails().getLocationId());
			caseWorkflow.setAssignedFromLocationName(erc.getLocationDetails().getLocationName());
			caseWorkflowList.add(caseWorkflow);
		}
		return caseWorkflowList;
	}

	@Scheduled(cron = "00 30 00 * * *")
	public void TransferRoleAction() {
		// Checking if any transfer case is there for today
		List<Long> deleteURMList = new ArrayList<>();
		List<TransferRole> transferRoles = transferRoleRepository.findByActionDateBeforeOrOn(new Date());
		if (transferRoles != null & transferRoles.size() > 0) {
			for (TransferRole transferRole : transferRoles) {
				// Save in logs table
				adminUserService.saveTransferRoleLogs(transferRole, "Deemed Approved");
				// Getting existing user role by user role mapping id
				Optional<UserRoleMapping> object1 = userRoleMappingRepository
						.findById(transferRole.getUserRoleMapping().getUserRoleMappingId());
				if (object1.isPresent()) {
					UserRoleMapping urm = object1.get();
					UserDetails transferFromUD = urm.getUserDetails();
					// Checking if user2 already have the role or not
					Optional<UserRoleMapping> object2 = userRoleMappingRepository
							.findByUserIdAndUserRoleIdAndCircleIdAndZoneIdAndStateId(
									transferRole.getTransferToUser().getUserId(), urm.getUserRole().getId(),
									urm.getCircleDetails().getCircleId(), urm.getZoneDetails().getZoneId(),
									urm.getStateDetails().getStateId());
					// If don't have the particular role to the transfer officer then adding new
					// role or else delete the transfer_from_user role
					if (!object2.isPresent()) {
						// Transfer role is not a verifier or approver role
						if (!urm.getUserRole().getRoleCode().equalsIgnoreCase("RU")
								&& !urm.getUserRole().getRoleCode().equalsIgnoreCase("AP")) {
							UserRoleMapping urmSave = urm;
							adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
									transferRole.getTransferToUser(), transferFromUD);
							urmSave.setUserDetails(transferRole.getTransferToUser());
							// Save UserRoleMapping Logs
							adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(), urmSave,
									"Added user Role(Transfer)");
							userRoleMappingRepository.save(urmSave);
						} else {
							/*
							 * //Transfer Role have State (verifier or approver) role but user don't have
							 * state (verifier or approver) role
							 * if(urm.getZoneDetails().getZoneId().equalsIgnoreCase("NA")) {
							 * List<UserRoleMapping> objectURM =
							 * userRoleMappingRepository.findByUserIdAndUserRoleId(transferRole.
							 * getTransferToUser().getUserId(),urm.getUserRole().getId()); //checking if
							 * user have any zone (verifier or approver) role or not if user have zone role
							 * then delete if(objectURM!=null && objectURM.size()>0) { for(UserRoleMapping
							 * obj : objectURM) { //Save UserRoleMapping Logs
							 * adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
							 * urm, "Deleted user Role(Transfer)");
							 * deleteURMList.add(obj.getUserRoleMappingId()); } } //adding state (verifier
							 * or approver) role UserRoleMapping urmSave = urm;
							 * adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
							 * transferRole.getTransferToUser(), transferFromUD);
							 * urmSave.setUserDetails(transferRole.getTransferToUser()); //Save
							 * UserRoleMapping Logs
							 * adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
							 * urmSave, "Added user Role(Transfer)");
							 * userRoleMappingRepository.save(urmSave); } //Transfer Role have the
							 * particular Zone(Z01 or Z02 or Z03) (verifier or approver) role but user don't
							 * have that particular Zone (verifier or approver) role else {
							 * List<UserRoleMapping> objectURM =
							 * userRoleMappingRepository.findByUserIdAndUserRoleIdAndStateId(transferRole.
							 * getTransferToUser().getUserId(),urm.getUserRole().getId(), "HP"); //Checking
							 * if user have the state (verifier or approver) role then add the cases and
							 * delete the transfer_from_user role if(objectURM!=null && objectURM.size()>0)
							 * { adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
							 * transferRole.getTransferToUser(), transferFromUD); //Save UserRoleMapping
							 * Logs
							 * adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
							 * urm, "Deleted user Role(Transfer)");
							 * deleteURMList.add(urm.getUserRoleMappingId()); } else { //adding the
							 * particular Zone(Z01 or Z02 or Z03) (verifier or approver) role
							 * UserRoleMapping urmSave = urm;
							 * adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
							 * transferRole.getTransferToUser(), transferFromUD);
							 * urmSave.setUserDetails(transferRole.getTransferToUser()); //Save
							 * UserRoleMapping Logs
							 * adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
							 * urmSave, "Added user Role(Transfer)");
							 * userRoleMappingRepository.save(urmSave); } }
							 */
							/****************** Latest code ******************/
							List<UserRoleMapping> urmList = new ArrayList<>();
							urmList.add(urm);
							if (!urmList.isEmpty()) {
								boolean haveURM = false;
								List<String> allLowerLocations = adminUpdateUserDetailsImpl
										.getAllMappedLocationsFromUserRoleMappingList(urmList); // get all the lower mapped
																							// locations from given
																							// location
								if (allLowerLocations != null && allLowerLocations.size() > 0) {
//									allLocations.add("NA");
									List<UserRoleMapping> availableUrmList = userRoleMappingRepository
											.getAllUrmByUserIdRoleIdLocationList(
													transferRole.getTransferToUser().getUserId(),
													urm.getUserRole().getId(), allLowerLocations);
									if (!availableUrmList.isEmpty()) { // checking if user already assign to lower
																		// location from given location and role
										haveURM = true;
										System.out.println("User already assiged to lower location");
										for (UserRoleMapping roleMapping : availableUrmList) { // delete all ToUser
																								// roles already assign
																								// to lower location
											deleteURMList.add(roleMapping.getUserRoleMappingId());
										}
										// Save toUser higher role (Transfer role) and transfer cases
										UserRoleMapping urmSave = urm;
										adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
												transferRole.getTransferToUser(), transferFromUD);
										urmSave.setUserDetails(transferRole.getTransferToUser());
										// Save UserRoleMapping Logs
										adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
												urmSave, "Added user Role(Transfer)");
										userRoleMappingRepository.save(urmSave);
										continue;
									}
								}
								List<String> allHigherLocations = adminUpdateUserDetailsImpl
										.getAllHigherMappedLocationsFromUserRoleMapping(urmList.get(0));// get all the
																										// higher mapped
																										// locations
																										// from given
																										// location
								if (allHigherLocations != null && allHigherLocations.size() > 0) {
//									allLocations.add("NA");
									List<UserRoleMapping> availableUrmList = userRoleMappingRepository
											.getAllUrmByUserIdRoleIdLocationList(
													transferRole.getTransferToUser().getUserId(),
													urm.getUserRole().getId(), allHigherLocations);
									if (!availableUrmList.isEmpty()) { // checking if user already assign to higher
																		// location from given location and role
										haveURM = true;
										System.out.println("User already assiged to higher location");
										// only delete the fromUser role and transfer the cases
										adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
												transferRole.getTransferToUser(), transferFromUD);
										// Save UserRoleMapping Logs
										adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
												urm, "Deleted user Role(Transfer)");
										deleteURMList.add(urm.getUserRoleMappingId());
										continue;
									}
								}
								// If don't found any lower and higher mapped location URM for transferToUser
								if (!haveURM) {
									UserRoleMapping urmSave = urm;
									adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
											transferRole.getTransferToUser(), transferFromUD);
									urmSave.setUserDetails(transferRole.getTransferToUser());
									// Save UserRoleMapping Logs
									adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(),
											urmSave, "Added user Role(Transfer)");
									userRoleMappingRepository.save(urmSave);
								}
							}
//							allLocations = null;
//							if(!urmList.isEmpty()) { //get all the higher mapped locations from given location
//								allLocations = adminUpdateUserDetailsImpl.getAllHigherMappedLocationsFromUserRoleMapping(urmList.get(0));
//								if(allLocations!=null && allLocations.size()>0) {
////									allLocations.add("NA");
//									List<UserRoleMapping> availableUrmList = userRoleMappingRepository.getAllUrmByUserIdRoleIdLocationList(transferRole.getTransferToUser().getUserId(), urm.getUserRole().getId(), allLocations);
//									if(!availableUrmList.isEmpty()) { // checking if user already assign to higher location from given location and role
//										System.out.println("User already assiged to higher location");
//										//only delete the fromUser role and transfer the cases
//										adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm, transferRole.getTransferToUser(), transferFromUD);
//				    					//Save UserRoleMapping Logs
//				    					adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(), urm, "Deleted user Role(Transfer)");
//				    					deleteURMList.add(urm.getUserRoleMappingId());
//										continue;
//									}
//								}
//							}
						}
					} else {
						adminUpdateUserDetailsImpl.moveEnfCasesFromUserToAnotherUser(urm,
								transferRole.getTransferToUser(), transferFromUD);
						// Save UserRoleMapping Logs
						adminUserService.saveUserRoleMappinglogs(transferRole.getRequestedByUserId(), urm,
								"Deleted user Role(Transfer)");
						deleteURMList.add(urm.getUserRoleMappingId());
					}
				}
			}
		}
		deleteTransferRoleforCurrentDate();
		if (deleteURMList != null && deleteURMList.size() > 0) {
			userRoleMappingRepository.deleteAllById(deleteURMList);
		}
	}

	public void deleteTransferRoleforCurrentDate() {
		List<TransferRole> transferRoles = transferRoleRepository.findByActionDateBeforeOrOn(new Date());
		if (transferRoles != null & transferRoles.size() > 0) {
			transferRoleRepository.deleteAll(transferRoles);
		}
	}

	@Scheduled(cron = "00 10 00 * * *")
	public void validateActiveAndNewUser() {
		// Get All Active and New user
		List<UserDetails> userList = userDetailsRepository.findAllActiveAndNewUserLoginName();
		// Checking the user trying to login with in 90days from last login date
		if (userList != null && userList.size() > 0) {
			for (UserDetails userDetails : userList) {
				if (userDetails.getLastLoginDate() != null
						&& !loginService.checkDateExpiry(userDetails.getLastLoginDate(), 90)
						&& (userDetails.getUserStatus().equalsIgnoreCase(userActiveStatus)
								|| userDetails.getUserStatus().equalsIgnoreCase(newUserStatus))) {
					userDetails.setUserStatus(userInactiveStatus);
					userDetailsRepository.save(userDetails);
				}
			}
		}
	}

	@Scheduled(cron = "00 18 00 * * *")
	public void approveRecoveryCase() {
		try {
			List<EnforcementReviewCase> listOfRecoveryCase = enforcementReviewCaseRepository
					.getlistOfRecoveryCasePendingForApproval();
			for (EnforcementReviewCase enforcementReviewCase : listOfRecoveryCase) {
				String action = "foRecoveryApprove";
				try {
					enforcementReviewCase.setRecoveryStatus(action);
					ActionStatus actionStatus = enforcementReviewCase.getActionStatus();
					RecoveryStage recoveryStage = enforcementReviewCase.getRecoveryStage();
					CaseStage caseStage = enforcementReviewCase.getCaseStage();
					String category = enforcementReviewCase.getCategory();
					Long indicativeTaxValue = enforcementReviewCase.getIndicativeTaxValue();
					Long demand = enforcementReviewCase.getDemand();
					Long recoveryByDRC3 = enforcementReviewCase.getRecoveryByDRC3();
					Long recoveryAgainstDemand = enforcementReviewCase.getRecoveryAgainstDemand();
					String taxPayerName = enforcementReviewCase.getTaxpayerName();
					String circle = enforcementReviewCase.getLocationDetails().getLocationName();
					FoReviewCase foReviewCase = new FoReviewCase();
					foReviewCase.setGSTIN(enforcementReviewCase.getId().getGSTIN());
					foReviewCase.setCaseReportingDate(enforcementReviewCase.getId().getCaseReportingDate());
					foReviewCase.setPeriod(enforcementReviewCase.getId().getPeriod());
					foReviewCase.setAction(action);
					foReviewCase.setActionStatus(actionStatus != null ? actionStatus.getId() : 0);
					foReviewCase.setRecoveryStage(recoveryStage != null ? recoveryStage.getId() : 0);
					foReviewCase.setCaseStage(caseStage != null ? caseStage.getId() : 0);
					foReviewCase.setAssignedTo("ap");
					foReviewCase.setCategory(category);
					foReviewCase.setIndicativeTaxValue(indicativeTaxValue);
					foReviewCase.setDemand(demand);
					foReviewCase.setRecoveryByDRC3(recoveryByDRC3);
					foReviewCase.setRecoveryAgainstDemand(recoveryAgainstDemand);
					foReviewCase.setTaxpayerName(taxPayerName);
					foReviewCase.setCircle(circle);
					foReviewCase.setCaseUpdatingDate(new Date());
					foReviewCase.setCaseId(enforcementReviewCase.getCaseId());
					foReviewCase.setCaseStageArn(enforcementReviewCase.getCaseStageArn());
					foReviewCase.setRecoveryStageArn(enforcementReviewCase.getRecoveryStageArn());
					foReviewCase.setUserId(0);
					foReviewCase.setAssignedToUserId(0);
					foReviewCase.setExtensionFilename(
							enforcementReviewCase.getExtensionNoDocument().getExtensionFileName());
					foReviewCase.setAssignedFromUserId(0);
					foReviewCase.setAssignedFrom(enforcementReviewCase.getLocationDetails().getLocationName());
					foReviewCase.setCaseJurisdiction(enforcementReviewCase.getLocationDetails().getLocationName());
					foReviewCaseRepository.save(foReviewCase);
					enforcementReviewCaseRepository.save(enforcementReviewCase);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
