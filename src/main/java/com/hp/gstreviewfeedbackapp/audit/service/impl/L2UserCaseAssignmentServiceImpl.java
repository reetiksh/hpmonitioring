package com.hp.gstreviewfeedbackapp.audit.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCaseWorkflow;
import com.hp.gstreviewfeedbackapp.audit.model.L2UserWorkLogs;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.L2UserWorkLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.service.AuditCaseDocumentService;
import com.hp.gstreviewfeedbackapp.audit.service.L2UserCaseAssignmentService;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.ExtensionNoDocumentsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;

@Service
public class L2UserCaseAssignmentServiceImpl implements L2UserCaseAssignmentService {
	private static final Logger logger = LoggerFactory.getLogger(L2UserCaseAssignmentServiceImpl.class);
	@Autowired
	private AuditMasterCaseWorkflowRepository auditMasterCaseWorkflowRepository;
	@Autowired
	private L2UserWorkLogsRepository l2UserWorkLogsRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private AuditCaseDocumentService auditCaseDocumentService;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private ExtensionNoDocumentsRepository extensionNoDocumentsRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Value("${upload.audit.teamlead.directory}")
	private String teamLeadUploadDirectory;
	@Value("${upload.directory}")
	private String assessmentHqUploadDirectory;

	@Override
	public void saveAuditMasterCaseWorkflowDetails(AuditMaster auditMaster, Integer assignedFromUserId,
			Integer assignToUserId) {
		try {
			AuditMasterCaseWorkflow auditMasterCaseWorkflow = new AuditMasterCaseWorkflow();
			auditMasterCaseWorkflow.setCaseId(auditMaster.getCaseId());
			auditMasterCaseWorkflow.setGSTIN(auditMaster.getGSTIN());
			auditMasterCaseWorkflow.setCaseReportingDate(auditMaster.getCaseReportingDate());
			auditMasterCaseWorkflow.setPeriod(auditMaster.getPeriod());
			auditMasterCaseWorkflow.setAssignedFrom(auditMaster.getAssignedFrom());
			auditMasterCaseWorkflow.setAssignedFromUserId(assignedFromUserId);
			auditMasterCaseWorkflow.setAssignTo(auditMaster.getAssignTo());
			auditMasterCaseWorkflow.setAssignToUserId(assignToUserId);
			auditMasterCaseWorkflow.setAssignedFromLocationId(auditMaster.getLocationDetails());
			auditMasterCaseWorkflow.setUpdatingTimestamp(auditMaster.getLastUpdatedTimeStamp());
			auditMasterCaseWorkflow.setAction(auditMaster.getAction());
			auditMasterCaseWorkflow.setLocationDetails(auditMaster.getLocationDetails());
			auditMasterCaseWorkflow.setCategory(auditMaster.getCategory());
			auditMasterCaseWorkflow.setTaxpayerName(auditMaster.getTaxpayerName());
			auditMasterCaseWorkflow.setIndicativeTaxValue(auditMaster.getIndicativeTaxValue());
			auditMasterCaseWorkflow.setParameter(auditMaster.getParameter());
			auditMasterCaseWorkflowRepository.save(auditMasterCaseWorkflow);
		} catch (Exception e) {
			logger.error("L2UserCaseAssignmentServiceImpl : saveAuditMasterCaseWorkflowDetails : " + e.getMessage());
		}
	}

	@Override
	public void saveL2UserWorkLogs(AuditMaster auditMaster, Integer assignedFromUserId, Integer assignToUserId) {
		try {
			L2UserWorkLogs l2UserWorkLogs = new L2UserWorkLogs();
			l2UserWorkLogs.setCaseId(auditMaster.getCaseId());
			l2UserWorkLogs.setGSTIN(auditMaster.getGSTIN());
			l2UserWorkLogs.setCaseReportingDate(auditMaster.getCaseReportingDate());
			l2UserWorkLogs.setPeriod(auditMaster.getPeriod());
			l2UserWorkLogs.setTaxpayerName(auditMaster.getTaxpayerName());
			l2UserWorkLogs.setIndicativeTaxValue(auditMaster.getIndicativeTaxValue());
			l2UserWorkLogs.setParameter(auditMaster.getParameter());
			l2UserWorkLogs.setLocationDetails(auditMaster.getLocationDetails().getLocationName());
			l2UserWorkLogs.setCategory(auditMaster.getCategory().getName());
			l2UserWorkLogs
					.setAuditExtensionNoDocument(auditMaster.getAuditExtensionNoDocument().getExtensionFileName());
			l2UserWorkLogs.setExtensionNo(auditMaster.getExtensionNo());
			l2UserWorkLogs.setAssignedFrom(auditMaster.getAssignedFrom());
			l2UserWorkLogs.setAssignTo(auditMaster.getAssignTo());
			l2UserWorkLogs.setAction(auditMaster.getAction().getStatus());
			l2UserWorkLogs.setLastUpdatedTimeStamp(auditMaster.getLastUpdatedTimeStamp());
			l2UserWorkLogs.setAssignedFromLocation(auditMaster.getAssignedFrom());
			l2UserWorkLogs.setAssignToLocation(auditMaster.getLocationDetails().getLocationName());
			l2UserWorkLogs.setAssignedFromUserId(assignedFromUserId);
			l2UserWorkLogs.setAssignedToUserId(assignToUserId);
			l2UserWorkLogsRepository.save(l2UserWorkLogs);
		} catch (Exception e) {
			logger.error("L2UserCaseAssignmentServiceImpl : saveL2UserWorkLogs : " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public String saveDarDetails(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Date lastUpdatedTimestamp = new Date();
			AuditMaster auditMaster = auditMasterRepository.findByCaseIdAndAction(l3UserAuditCaseUpdate.getCaseId(),
					auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get()).get();
			boolean rejectDarFlag = false;
			Optional<AuditCaseDateDocumentDetails> auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditMaster.getAction());
			if (auditCaseDateDocumentDetails.isPresent()) {
				if (auditCaseDateDocumentDetails.get().getAction().getStatus()
						.equalsIgnoreCase("recommendedForRaiseQuery")) {
					rejectDarFlag = true;
				}
				List<AuditCaseDarDetails> auditCaseDarDetailsList = null;
				if (l3UserAuditCaseUpdate.getNilDar().trim().equals("NO")) {
					auditCaseDarDetailsList = auditCaseDateDocumentDetails.get().getAuditCaseDarDetailsList();
					for (List<String> strList : l3UserAuditCaseUpdate.getPara()) {
						AuditCaseDarDetails object = auditCaseDarDetailsList.stream()
								.filter(AuditCaseDarDetails -> AuditCaseDarDetails.getId()
										.equals(Long.parseLong(strList.get(0))))
								.findFirst().orElse(null);
						int size = strList.size();
//						if (rejectDarFlag == false && size > 2) {
//							rejectDarFlag = true;
//						}
						object.setCommentL2(size > 1 ? strList.get(1) : null);
						object.setRaiseQuery(size > 2 ? strList.get(2) : null);
						object.setLastUpdatedTimeStamp(
								size > 1 ? lastUpdatedTimestamp : object.getLastUpdatedTimeStamp());
					}
				}
				if (l3UserAuditCaseUpdate.getDarApproval() != null
						&& l3UserAuditCaseUpdate.getDarApproval().equalsIgnoreCase("rejection")) {
					rejectDarFlag = true;
				}
				auditCaseDateDocumentDetails.get().setCommentFromL2Officer(
						l3UserAuditCaseUpdate.getComment() != null ? l3UserAuditCaseUpdate.getComment()
								: auditCaseDateDocumentDetails.get().getCommentFromL2Officer());
				if (rejectDarFlag == true) {
					auditCaseDateDocumentDetails.get()
							.setAction(auditCaseStatusRepository.findByStatus("darrejected").get());
				} else {
					auditCaseDateDocumentDetails.get()
							.setAction(auditCaseStatusRepository.findByStatus("darapproved").get());
				}
				auditCaseDateDocumentDetails.get().setLastUpdatedTimeStamp(lastUpdatedTimestamp);
				auditCaseDateDocumentDetailsRepository.save(auditCaseDateDocumentDetails.get());
				auditMaster = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId()).get();
				auditMaster.setAction(auditCaseDateDocumentDetails.get().getAction());
				auditMaster.setAssignedFrom("L2");
				auditMaster.setAssignTo("L3");
				auditMaster.setLastUpdatedTimeStamp(lastUpdatedTimestamp);
//				
				auditMasterRepository.save(auditMaster);
				// save Case workflow logs
				saveAuditMasterCaseWorkflowDetails(auditMaster, objectUserDetails.get().getUserId(),
						auditMasterCasesAllocatingUsersRepository.findById(auditMaster.getCaseId()).get().getL3User());
				// save Document logs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails.get(),
						objectUserDetails.get());
				// save DAR details
				if (auditCaseDarDetailsList != null && auditCaseDarDetailsList.size() > 0) {
					auditCaseDocumentService.saveAuditCaseDarDetailsLogs(auditCaseDarDetailsList,
							objectUserDetails.get(), "updated by L2 officer");
				}
				return auditMaster.getAction().getSuccessMesage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L2UserCaseAssignmentServiceImpl : saveDarDetails :" + e.getMessage());
		}
		return "Failed to update the case";
	}

	@Override
	@Transactional
	public String saveRecomandedToMcmDetails(L3UserAuditCaseUpdate userAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Date lastUpdatedTimestamp = new Date();
			AuditMaster auditMaster = auditMasterRepository.findByCaseIdAndAction(userAuditCaseUpdate.getCaseId(),
					auditCaseStatusRepository.findById(userAuditCaseUpdate.getUpdateStatusId()).get()).get();
			Optional<AuditCaseDateDocumentDetails> auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditMaster.getAction());
			if (auditCaseDateDocumentDetails.isPresent()) {
				auditCaseDateDocumentDetails.get().setCommentFromL2Officer(
						userAuditCaseUpdate.getComment() != null ? userAuditCaseUpdate.getComment()
								: auditCaseDateDocumentDetails.get().getCommentFromL2Officer());
				auditCaseDateDocumentDetails.get()
						.setAction(auditCaseStatusRepository.findByStatus("recommendedtoMCM").get());
				auditCaseDateDocumentDetails.get().setActionDate(userAuditCaseUpdate.getWorkingDate());
				auditCaseDateDocumentDetails.get().setLastUpdatedTimeStamp(lastUpdatedTimestamp);
				auditCaseDateDocumentDetailsRepository.save(auditCaseDateDocumentDetails.get());
				auditMaster = auditMasterRepository.findById(userAuditCaseUpdate.getCaseId()).get();
				auditMaster.setAction(auditCaseDateDocumentDetails.get().getAction());
				auditMaster.setAssignedFrom("L2");
				auditMaster.setAssignTo("MCM");
				auditMaster.setLastUpdatedTimeStamp(lastUpdatedTimestamp);
//				
				auditMasterRepository.save(auditMaster);
				// save Case workflow logs
				saveAuditMasterCaseWorkflowDetails(auditMaster, objectUserDetails.get().getUserId(),
						auditMasterCasesAllocatingUsersRepository.findById(auditMaster.getCaseId()).get().getMcmUser());
				// save Document logs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails.get(),
						objectUserDetails.get());
				return auditMaster.getAction().getSuccessMesage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L2UserCaseAssignmentServiceImpl : saveDarDetails :" + e.getMessage());
		}
		return "Failed to update the case";
	}

	@Override
	@Transactional
	public String saveRecommendationToOtherModule(String caseId, Integer userId, String comment) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
			if (amObject.isPresent()) {
				AuditMaster auditMaster = amObject.get();
				List<String> statusList = new ArrayList<>();
				statusList.add("showCauseNotice");
				statusList.add("recommended_for_enforcement");
				statusList.add("recommended_for_assessment_adjudication");
				Optional<AuditCaseDateDocumentDetails> dateDocumentDetrailsObject = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndActionList(auditMaster.getCaseId(),
								auditCaseStatusRepository.findIdsByStatusList(statusList));
				CompositeKey compositeKey = new CompositeKey(auditMaster.getGSTIN(), auditMaster.getCaseReportingDate(),
						auditMaster.getPeriod());
				Optional<EnforcementReviewCase> ercObject = enforcementReviewCaseRepository.findById(compositeKey);
				if (ercObject.isPresent()) {
					auditMaster.setAction(auditCaseStatusRepository.findByStatus("auditcompleted").get());
					auditMaster.setLastUpdatedTimeStamp(new Date());
					auditMaster.setAssignedFrom("L2");
					auditMaster.setAssignTo("FO");
					auditMasterRepository.save(auditMaster);
					saveAuditMasterCaseWorkflowDetails(auditMaster, objectUserDetails.get().getUserId(), userId);
					logger.info(auditMaster.getCaseId() + " audit case already exists in assessement module.");
					Optional<AuditCaseDateDocumentDetails> closureReport = auditCaseDateDocumentDetailsRepository
							.findByCaseIdAndAction(auditMaster,
									auditCaseStatusRepository.findByStatus("closurereportissued").get());
					if (closureReport.isPresent()) {
						closureReport.get().setAction(auditCaseStatusRepository.findByStatus("auditcompleted").get());
						if (comment != null && comment.trim().length() > 0) {
							closureReport.get().setCommentFromL2Officer(comment);
						}
						closureReport.get().setLastUpdatedTimeStamp(new Date());
						auditCaseDateDocumentDetailsRepository.save(closureReport.get());
						// Save AuditCaseDateDocumentDetailsLogs
						auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(closureReport.get(),
								objectUserDetails.get());
					}
					// Save the audit case id to assessment
					EnforcementReviewCase enforcementReviewCase = ercObject.get();
					enforcementReviewCase.setAuditCaseId(caseId);
					enforcementReviewCaseRepository.save(enforcementReviewCase);
					return "Case already exisits in Assessment for the GSTIN " + compositeKey.getGSTIN() + ", Period "
							+ compositeKey.getPeriod() + ", Reporting Date "
							+ compositeKey.getCaseReportingDate().toString();
				}
				// Save PDF for enforcement from Audit showcauseNotice and create Extension file
				// for assessment
				if (dateDocumentDetrailsObject.get().getActionFilePath() != null
						&& dateDocumentDetrailsObject.get().getActionFilePath().trim().length() == 0) {
					logger.warn("L2UserCaseAssignmentServiceImpl : saveRecommendationToOtherModule : Case Id : "
							+ auditMaster.getCaseId()
							+ " : Unable to assign the Audit case to other module as to document is missing in directory.");
					return "Document missing! Please contact with team officer for the document details of show cause notice.";
				}
				File pdfFile = new File(
						teamLeadUploadDirectory + "\\" + dateDocumentDetrailsObject.get().getActionFilePath());
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String extensionNo = auditMaster.getExtensionNo();
				String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_"
						+ formattedTimestamp + ".pdf";
				Path targetPath = Path.of(assessmentHqUploadDirectory, pdfFileName);
				Files.copy(pdfFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
				ExtensionNoDocument extensionNoDocument = new ExtensionNoDocument();
				extensionNoDocument.setExtensionFileName(pdfFileName);
				extensionNoDocument.setExtensionNo(extensionNo);
				// Create Assessment case
				EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
				enforcementReviewCase.setId(compositeKey);
				enforcementReviewCase.setAction("upload");
				enforcementReviewCase.setAssignedFrom("HQ");
				enforcementReviewCase.setAssignedFromUserId(0);
				enforcementReviewCase.setAssignedTo("FO");
				enforcementReviewCase.setAssignedToUserId(userId);
				enforcementReviewCase.setAuditCaseId(caseId);
				enforcementReviewCase.setCaseUpdateDate(new Date());
				enforcementReviewCase.setCategory(auditMaster.getCategory().getName());
				enforcementReviewCase.setIndicativeTaxValue((auditMaster.getToalAmountToBeRecovered() != null
						&& auditMaster.getToalAmountToBeRecovered().compareTo(0l) > 0)
								? auditMaster.getToalAmountToBeRecovered()
								: auditMaster.getIndicativeTaxValue());
				enforcementReviewCase.setTaxpayerName(auditMaster.getTaxpayerName());
				enforcementReviewCase.setLocationDetails(auditMaster.getLocationDetails());
				enforcementReviewCase.setExtensionNoDocument(extensionNoDocument);
				enforcementReviewCase.setParameter(auditMaster.getParameter());
				enforcementReviewCase.setExtensionNo(extensionNo);
				if (dateDocumentDetrailsObject.get().getAction().getStatus().equalsIgnoreCase("showCauseNotice")) {
					enforcementReviewCase.setCaseStageArn(auditMaster.getArnNumber());
					enforcementReviewCase.setActionStatus(actionStatusRepository.findByName("Initiated").get());
					enforcementReviewCase.setCaseStage(caseStageRepository.findByName("DRC01 issued").get());
				}
				extensionNoDocument.getEnforcementReviewCases().add(enforcementReviewCase);
				// Save assessment case
				extensionNoDocumentsRepository.save(extensionNoDocument);
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				// Creating workflow for assessment
				caseWorkflow.setGSTIN(enforcementReviewCase.getId().getGSTIN());
				caseWorkflow.setCaseReportingDate(enforcementReviewCase.getId().getCaseReportingDate());
				caseWorkflow.setPeriod(enforcementReviewCase.getId().getPeriod());
				caseWorkflow.setAssignedFrom("HQ");
				caseWorkflow.setAssignedFromUserId(objectUserDetails.get().getUserId());
				caseWorkflow.setAssignedTo("FO");
				caseWorkflow.setAssigntoUserId(userId);
				caseWorkflow.setAssignedFromLocationId(auditMaster.getLocationDetails().getLocationName() + " (Audit)");
				caseWorkflow.setUpdatingDate(enforcementReviewCase.getCaseUpdateDate());
				caseWorkflow.setAction("upload");
				caseWorkflow.setAssignedToLocationId(auditMaster.getLocationDetails().getLocationName());
				caseWorkflowRepository.save(caseWorkflow);
				// Creating allocating user for assessment
				EnforcementReviewCaseAssignedUsers ercAssignedUsers = new EnforcementReviewCaseAssignedUsers();
				ercAssignedUsers.setId(compositeKey);
				ercAssignedUsers.setHqUserId(0);
				ercAssignedUsers.setFoUserId(userId);
				ercAssignedUsers.setRuUserId(0);
				ercAssignedUsers.setApUserId(0);
				ercAssignedUsers.setRmUserId(0);
				enforcementReviewCaseAssignedUsersRepository.save(ercAssignedUsers);
				auditMaster.setAction(auditCaseStatusRepository.findByStatus("auditcompleted").get());
				auditMaster.setLastUpdatedTimeStamp(new Date());
				auditMaster.setAssignedFrom("L2");
				auditMaster.setAssignTo("ASSESSMENT_FO");
				auditMasterRepository.save(auditMaster);
				saveAuditMasterCaseWorkflowDetails(auditMaster, objectUserDetails.get().getUserId(), userId);
				logger.info(auditMaster.getCaseId() + " Case save in assessement module from Audit module");
				Optional<AuditCaseDateDocumentDetails> closureReport = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(auditMaster,
								auditCaseStatusRepository.findByStatus("closurereportissued").get());
				if (closureReport.isPresent()) {
					closureReport.get().setAction(auditCaseStatusRepository.findByStatus("auditcompleted").get());
					if (comment != null && comment.trim().length() > 0) {
						closureReport.get().setCommentFromL2Officer(comment);
					}
					closureReport.get().setLastUpdatedTimeStamp(new Date());
					auditCaseDateDocumentDetailsRepository.save(closureReport.get());
					// Save AuditCaseDateDocumentDetailsLogs
					auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(closureReport.get(),
							objectUserDetails.get());
				}
				return "Case Id : " + auditMaster.getCaseId() + " has been assigned to Assessment module";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L2UserCaseAssignmentServiceImpl : saveRecommendationToOtherModule : " + e.getMessage());
		}
		return "Failed";
	}

	@Override
	@Transactional
	public String saveRecommendationRejected(String caseId, String comment) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
			if (amObject.isPresent()) {
				AuditMaster auditMaster = amObject.get();
				List<AuditCaseDateDocumentDetails> deleteList = auditCaseDateDocumentDetailsRepository
						.getLastTwoStatusorderBySequence(caseId);
				if (deleteList.size() == 2) {
					deleteList.forEach(ddd -> ddd.setCaseId(null));
					auditCaseDateDocumentDetailsRepository.deleteAll(deleteList);
					deleteList.get(0)
							.setCommentFromL2Officer("Deleting the recommendation to other module request from L3");
					deleteList.get(1)
							.setCommentFromL2Officer("Deleting the recommendation to other module request from L3");
					auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(deleteList.get(0),
							objectUserDetails.get(), caseId);
					auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(deleteList.get(1),
							objectUserDetails.get(), caseId);
				} else {
					logger.error("L2UserCaseAssignmentServiceImpl : saveRecommendationRejected : Case Id " + caseId
							+ " does not have last 3 status details in AuditCaseDateDocumentDetails.");
					return "Facing Some internal issue please contact with admin";
				}
				Optional<AuditCaseDateDocumentDetails> lastStatus = auditCaseDateDocumentDetailsRepository
						.getLastStatusorderBySequence(caseId);
				if (lastStatus.isPresent()) {
					auditMaster.setAssignedFrom("L2");
					auditMaster.setAssignTo("L3");
					auditMaster.setLastUpdatedTimeStamp(new Date());
					auditMaster.setAction(lastStatus.get().getAction());
					auditMasterRepository.save(auditMaster);
					AuditCaseDateDocumentDetails dateDocumentDetails = lastStatus.get();
					dateDocumentDetails.setCommentFromL2Officer(comment);
					dateDocumentDetails.setLastUpdatedTimeStamp(auditMaster.getLastUpdatedTimeStamp());
					auditCaseDateDocumentDetailsRepository.save(dateDocumentDetails);
					auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(dateDocumentDetails,
							objectUserDetails.get());
				} else {
					logger.error("L2UserCaseAssignmentServiceImpl : saveRecommendationRejected : Case Id " + caseId
							+ " does not have last 3 status details in AuditCaseDateDocumentDetails.");
					return "Facing Some internal issue please contact with admin";
				}
				return "Recommendation has been rejected for the case Id " + caseId
						+ " and assigned back to Team Lead officer";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L2UserCaseAssignmentServiceImpl : saveRecommendationRejected : " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations) {

        List<Object[]> rawData = auditMasterRepository.getAnnexureReportRaw(allMappedLocations);
        List<AnnexureReportRow> result = new ArrayList<>();

        for (Object[] row : rawData) {
            AnnexureReportRow dto = new AnnexureReportRow();
            dto.setZoneName((String) row[0]);
            dto.setCircleName((String) row[1]);
            dto.setAllottedCases(((Number) row[2]).intValue());
            dto.setAuditCasesCompleted(((Number) row[3]).intValue());
            dto.setPendingDeskReview(((Number) row[4]).intValue());
            dto.setPendingApprovalAuditPlan(((Number) row[5]).intValue());
            dto.setPendingExaminationBooks(((Number) row[6]).intValue());
            dto.setPendingDAR(((Number) row[7]).intValue());
            dto.setPendingFAR(((Number) row[8]).intValue());
            result.add(dto);
        }
        return result;
    
	}
	
	@Override
	public	List<String> getDashboardData(List<String> allMappedLocations){

        List<Object[]> resultList = auditMasterRepository.getDashboardMetrics(allMappedLocations);

        if (resultList != null && !resultList.isEmpty()) {
            Object[] result = resultList.get(0);
            List<String> dashboardData = new ArrayList<>();

            for (Object obj : result) {
                dashboardData.add(obj != null ? obj.toString() : "0");
            }
            return dashboardData;
        }

        return Arrays.asList("0", "0", "0", "0", "0", "0");
	}
}
