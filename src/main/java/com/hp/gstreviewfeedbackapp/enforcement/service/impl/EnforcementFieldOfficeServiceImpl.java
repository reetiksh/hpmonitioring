package com.hp.gstreviewfeedbackapp.enforcement.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.enforcement.data.EnforcementCaseUpdate;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementActionStatus;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementCaseCaseWorkflow;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementDashboardSummaryDTO;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementFoUserLogs;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMasterAllocatingUser;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementActionStatusRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementCaseCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementFoUserLogsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterAllocatingUserRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementFieldOfficeService;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.ExtensionNoDocumentsRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyExtensionNoDocumentRepository;

@Service
@Primary
public class EnforcementFieldOfficeServiceImpl implements EnforcementFieldOfficeService {
	private static final Logger logger = LoggerFactory.getLogger(EnforcementFieldOfficeServiceImpl.class);
	@Autowired
	private EnforcementMasterRepository enforcementMasterRepository;
	@Autowired
	private EnforcementCaseCaseWorkflowRepository enfCaseWorkflowRepository;
	@Autowired
	private EnforcementMasterAllocatingUserRepository enfAllocatingUserRepository;
	@Autowired
	private EnforcementFoUserLogsRepository enforcementFoUserLogsRepository;
	@Autowired
	private ScrutinyExtensionNoDocumentRepository scrutinyExtensionNoDocumentRepository;
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	@Autowired
	private EnforcementActionStatusRepository enforcementActionStatusRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private EnforcementCaseCaseWorkflowRepository enforcementCaseCaseWorkflowRepository;
	@Autowired
	private EnforcementCaseDateDocumentDetailsRepository dateDocumentDetailsRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private ExtensionNoDocumentsRepository extensionNoDocumentsRepository;
	@Autowired
	private ActionStatusRepository assessmentFoActionStatusRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Value("${upload.enforcement.directory}")
	private String uploadDirectory;
	@Value("${upload.directory}")
	private String scrutinyModulePdfUploadDirectory;
	@Value("${upload.directory}")
	private String assessmentHqUploadDirectory;

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> enforcementCaseApprovalRequestSubmission(CompositeKey compositeKey,
			String needApproval, String caseId, UserDetails foUserDetails) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<EnforcementMaster> enforcementMasterOptional = enforcementMasterRepository.findById(compositeKey);
			if (enforcementMasterOptional.isPresent()
					&& (needApproval.trim().equalsIgnoreCase("yes") || needApproval.trim().equalsIgnoreCase("no"))) {
				Integer assignedToUserId = 0;
				EnforcementMaster enforcementMaster = enforcementMasterOptional.get();
				// Updating enforcement case attributes according to the approval
				if (needApproval.trim().equalsIgnoreCase("yes")) {
					enforcementMaster.setAssignedFrom("Enforcement_FO");
					enforcementMaster.setAssignedTo("Enforcement_SVO");
					enforcementMaster.setAction(
							enforcementActionStatusRepository.findByCodeName("investigationPermissionRequired").get());
				} else {
					enforcementMaster.setAssignedFrom("Enforcement_FO");
					enforcementMaster.setAssignedTo("Enforcement_FO");
					enforcementMaster
							.setAction(enforcementActionStatusRepository.findByCodeName("investigationStarted").get());
					assignedToUserId = foUserDetails.getUserId();
				}
				enforcementMaster.setCaseId(caseId);
				enforcementMaster.setCaseUpdatingTimestamp(new Date());
				// Save Case workflow
				EnforcementCaseCaseWorkflow caseWorkflow = new EnforcementCaseCaseWorkflow();
				caseWorkflow.setGstin(enforcementMaster.getId().getGSTIN());
				caseWorkflow.setCaseReportingDate(enforcementMaster.getId().getCaseReportingDate());
				caseWorkflow.setPeriod(enforcementMaster.getId().getPeriod());
				caseWorkflow.setAssignedFrom(enforcementMaster.getAssignedFrom());
				caseWorkflow.setAssignedFromUserId(foUserDetails.getUserId());
				caseWorkflow.setAssignedTo(enforcementMaster.getAssignedTo());
				caseWorkflow.setAssignedFromLocation(enforcementMaster.getCaseLocation());
				caseWorkflow.setAssignedToLocation(enforcementMaster.getCaseLocation());
				caseWorkflow.setAssigntoUserId(assignedToUserId);
				caseWorkflow.setCaseUpdatingTimestamp(enforcementMaster.getCaseUpdatingTimestamp());
				caseWorkflow.setAssignedFromLocationName(enforcementMaster.getCaseLocation().getLocationName());
				caseWorkflow.setAssignedToLocationName(enforcementMaster.getCaseLocation().getLocationName());
				caseWorkflow.setAction(enforcementMaster.getAction());
				// save Enforcement master allocating user details for FO
				EnforcementMasterAllocatingUser allocatingUser = enfAllocatingUserRepository.findById(compositeKey)
						.get();
				allocatingUser.setEnfFoUserId(foUserDetails.getUserId());
				enforcementMasterRepository.save(enforcementMaster);
				enfCaseWorkflowRepository.save(caseWorkflow);
				enfAllocatingUserRepository.save(allocatingUser);
				// Save FO user Logs
				saveEnforcementFoUserLogs(enforcementMaster, foUserDetails.getUserId(), assignedToUserId);
				response.put("success", true);
				response.put("message", "Case submission successful!");
				return ResponseEntity.ok(response);
			}
			response.put("success", false);
			response.put("message", "Anonymous form submission!");
			return ResponseEntity.unprocessableEntity().body(response);
		} catch (Exception e) {
			logger.error(
					"EnforcementFieldOfficeServiceImpl : enforcementCaseApprovalRequestSubmission : " + e.getMessage());
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Override
	public void saveEnforcementFoUserLogs(EnforcementMaster enforcementMaster, Integer assignedFromUserId,
			Integer assignedToUserId) {
		try {
			EnforcementFoUserLogs foLogs = new EnforcementFoUserLogs();
			foLogs.setGstin(enforcementMaster.getId().getGSTIN());
			foLogs.setPeriod(enforcementMaster.getId().getPeriod());
			foLogs.setCaseReportingDate(enforcementMaster.getId().getCaseReportingDate());
			foLogs.setAction(enforcementMaster.getAction().getCodeName());
			foLogs.setAssignedFrom(enforcementMaster.getAssignedFrom());
			foLogs.setAssignedFromLocationName(enforcementMaster.getCaseLocation().getLocationName());
			foLogs.setAssignedFromUserId(assignedFromUserId);
			foLogs.setAssignedTo(enforcementMaster.getAssignedTo());
			foLogs.setAssignedToLocationName(enforcementMaster.getCaseLocation().getLocationName());
			foLogs.setAssigntoUserId(assignedToUserId);
			foLogs.setCaseUpdatedByUser(assignedFromUserId);
			foLogs.setCaseUpdatingTimestamp(enforcementMaster.getCaseUpdatingTimestamp());
			foLogs.setCaseId(enforcementMaster.getCaseId());
			enforcementFoUserLogsRepository.save(foLogs);
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : saveEnforcementFoUserLogs : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public String saveEnforcementCaseToScrutinyModule(EnforcementMaster enforcementMaster) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			// Set Scrutiny Case Allocating User Details
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = new ScrutinyCasesPertainToUsers();
			scrutinyCasesPertainToUsers.setId(enforcementMaster.getId());
			scrutinyCasesPertainToUsers.setHqUserId(0);
			scrutinyCasesPertainToUsers.setFoUserId(0);
			scrutinyCasesPertainToUsers.setRuUserId(0);
			// Copy the Enforcement HQ Upload PDF to Scrutiny HQ Upload PDF
			File pdfFile = new File(
					uploadDirectory + "\\" + enforcementMaster.getExtensionNoDocumentId().getExtensionFileName());
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
			String extensionNo = enforcementMaster.getExtensionNo();
			String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_" + formattedTimestamp
					+ ".pdf";
			Path targetPath = Path.of(scrutinyModulePdfUploadDirectory, pdfFileName);
			Files.copy(pdfFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
			// Set Scrutiny Extension No Document Details
			ScrutinyExtensionNoDocument scrutinyExtensionNoDocument = new ScrutinyExtensionNoDocument();
			scrutinyExtensionNoDocument.setExtensionFileName(pdfFileName);
			scrutinyExtensionNoDocument.setExtensionNo(enforcementMaster.getExtensionNo());
			// Set MstScrutinyCases Details
			MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
			mstScrutinyCases.setId(enforcementMaster.getId());
			mstScrutinyCases.setCategory(enforcementMaster.getCategory());
			mstScrutinyCases.setExtensionNo(enforcementMaster.getExtensionNo());
			mstScrutinyCases.setTaxpayerName(enforcementMaster.getTaxpayerName());
			mstScrutinyCases.setIndicativeTaxValue(enforcementMaster.getIndicativeTaxValue());
			mstScrutinyCases.setLocationDetails(enforcementMaster.getCaseLocation());
			mstScrutinyCases.setScrutinyExtensionNoDocument(scrutinyExtensionNoDocument);
			mstScrutinyCases.setAssignedTo("SFO");
			mstScrutinyCases.setAssignedFrom("SHQ");
			mstScrutinyCases.setAction("uploadScrutinyCases");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCases.setParameters(enforcementMaster.getParameter());
			// Adding The Scrutiny Case Details to Scrutiny Extension No Document
			scrutinyExtensionNoDocument.getScrutinyCasesList().add(mstScrutinyCases);
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(mstScrutinyCases.getId().getCaseReportingDate());
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(0);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			// Saving the all data to DB
			scrutinyExtensionNoDocumentRepository.save(scrutinyExtensionNoDocument);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			// Save Details
			enforcementMaster.setAction(enforcementActionStatusRepository.findByCodeName("transferToScrutiny").get());
			enforcementMaster.setAssignedFrom("Enforcement_FO");
			enforcementMaster.setAssignedTo("ScrutinyFO");
			enforcementMaster.setCaseUpdatedByUser(userDetails);
			enforcementMaster.setCaseUpdatingTimestamp(new Date());
			enforcementMasterRepository.save(enforcementMaster);
			// Save the Enforcement FO User Id to Enforcement Master Allocating User table
			Optional<EnforcementMasterAllocatingUser> allocatedUserOptional = enfAllocatingUserRepository
					.findById(enforcementMaster.getId());
			allocatedUserOptional.get().setEnfFoUserId(userDetails.getUserId());
			enfAllocatingUserRepository.save(allocatedUserOptional.get());
			// Save Enforcement Case Workflow Details from Enforcement case Object
			saveEnforcementCaseCaseWorkflow(enforcementMaster);
			// Save Enforcement Officer Logs
			saveEnforcementFoUserLogs(enforcementMaster, userDetails.getUserId(), 0);
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : saveEnforcementCaseToScrutinyModule : " + e.getMessage());
			e.printStackTrace();
		}
		return "Case Assigned to Scrutiny Module Successfully";
	}

	@Override
	public void saveEnforcementCaseCaseWorkflow(EnforcementMaster enforcementMaster) {
		try {
			EnforcementCaseCaseWorkflow caseWorkflow = new EnforcementCaseCaseWorkflow();
			caseWorkflow.setGstin(enforcementMaster.getId().getGSTIN());
			caseWorkflow.setCaseReportingDate(enforcementMaster.getId().getCaseReportingDate());
			caseWorkflow.setPeriod(enforcementMaster.getId().getPeriod());
			caseWorkflow.setAssignedFrom(enforcementMaster.getAssignedFrom());
			caseWorkflow.setAssignedTo(enforcementMaster.getAssignedTo());
			caseWorkflow.setAssignedFromLocation(enforcementMaster.getCaseLocation());
			caseWorkflow.setAssignedFromLocationName(enforcementMaster.getCaseLocation().getLocationName());
			caseWorkflow.setAssignedToLocation(enforcementMaster.getCaseLocation());
			caseWorkflow.setAssignedToLocationName(enforcementMaster.getCaseLocation().getLocationName());
			caseWorkflow.setAssignedFromUserId(enforcementMaster.getCaseUpdatedByUser().getUserId());
			caseWorkflow.setAssigntoUserId(0);
			caseWorkflow.setCaseUpdatingTimestamp(enforcementMaster.getCaseUpdatingTimestamp());
			caseWorkflow.setAction(enforcementMaster.getAction());
			Map<Integer, String> parameterMap = mstParametersModuleWiseRepository.findAll().stream()
					.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
			caseWorkflow.setParameterName(Arrays.stream(enforcementMaster.getParameter().split(","))
					.map(paramId -> Integer.parseInt(paramId.trim())).map(parameterMap::get)
					.collect(Collectors.joining(",")));
			enforcementCaseCaseWorkflowRepository.save(caseWorkflow);
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : saveEnforcementCaseCaseWorkflow : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public String acknowledgeEnforcementCase(EnforcementMaster enforcementMaster) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			UserDetails userDetails = objectUserDetails.get();
			// Update Case Details as Acknowledged
			enforcementMaster.setAction(enforcementActionStatusRepository.findByCodeName("acknowledged").get());
			enforcementMaster.setCaseUpdatedByUser(userDetails);
			enforcementMaster.setCaseUpdatingTimestamp(new Date());
			enforcementMasterRepository.save(enforcementMaster);
			// Save the Enforcement FO User Id to Enforcement Master Allocating User table
			Optional<EnforcementMasterAllocatingUser> allocatedUserOptional = enfAllocatingUserRepository
					.findById(enforcementMaster.getId());
			allocatedUserOptional.get().setEnfFoUserId(userDetails.getUserId());
			enfAllocatingUserRepository.save(allocatedUserOptional.get());
			// Save Enforcement Case Workflow Details from Enforcement case Object
			saveEnforcementCaseCaseWorkflow(enforcementMaster);
			// Save Enforcement Officer Logs
			saveEnforcementFoUserLogs(enforcementMaster, userDetails.getUserId(), userDetails.getUserId());
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : acknowledgeEnforcementCase : " + e.getMessage());
			e.printStackTrace();
		}
		return "Case Has Been Acknowledged Successfully";
	}

	@Override
	public String updateEnforcementCaseForPanchnamaAndPriliminaryReportAndFinalReport(
			EnforcementCaseUpdate enforcementCaseUpdateDetails) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			String recoveryStageArnArray = null;
			String recoveryStageArn = null;
			CompositeKey compositeKey = new CompositeKey(enforcementCaseUpdateDetails.getGSTIN(),
					enforcementCaseUpdateDetails.getCaseReportingDate(), enforcementCaseUpdateDetails.getPeriod());
			Optional<EnforcementMaster> emObject = enforcementMasterRepository.findById(compositeKey);
			Optional<EnforcementActionStatus> actionObject = enforcementActionStatusRepository
					.findById(enforcementCaseUpdateDetails.getUpdateStatusId());
			EnforcementCaseDateDocumentDetails caseDateDocumentDetails = null;
			
			if (emObject.isPresent()) {
				EnforcementMaster enforcementMaster = emObject.get();
				if (dateDocumentDetailsRepository
						.findByEnforcementMasterAndAction(enforcementMaster, actionObject.get()).isEmpty()) {
					caseDateDocumentDetails = new EnforcementCaseDateDocumentDetails();
					caseDateDocumentDetails.setEnforcementMaster(enforcementMaster);
					caseDateDocumentDetails.setActionDate(enforcementCaseUpdateDetails.getWorkingDate());
					caseDateDocumentDetails.setAction(actionObject.get());
					caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
					if (enforcementCaseUpdateDetails.getRecoveryStageArn() != null) {
						recoveryStageArnArray = getStringArn(enforcementCaseUpdateDetails.getRecoveryStageArn());
						recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
					} else {
						recoveryStageArn = "";
					}
					caseDateDocumentDetails.setRecoveryStageArn(recoveryStageArn);
					caseDateDocumentDetails.setRecoveryStage(enforcementCaseUpdateDetails.getRecoveryStage());
					
					MultipartFile pdfFile = enforcementCaseUpdateDetails.getPdfData().getPdfFile();
					// Save PDF
					LocalDateTime currentTimestamp = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
					String formattedTimestamp = currentTimestamp.format(formatter);
					String pdfFileName = enforcementMaster.getId().getGSTIN().replace('\\', '.').replace('/', '.')
							+ "_pdf_" + formattedTimestamp + "_" + actionObject.get().getCodeName() + ".pdf";
					pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
					caseDateDocumentDetails.setActionFileName(pdfFileName);
					List<EnforcementCaseDateDocumentDetails> list = new ArrayList<>();
					list.add(caseDateDocumentDetails);
					if (enforcementMaster.getAction().getCodeName().equals("acknowledged")
							&& actionObject.get().getCodeName().equals("panchnama")) {
						enforcementMaster.setInspectionRequired("true");
					} else if (enforcementMaster.getAction().getCodeName().equals("acknowledged")
							&& actionObject.get().getCodeName().equals("priliminaryReport")) {
						enforcementMaster.setInspectionRequired("false");
					}
					
					
					
					
					enforcementMaster.setAction(actionObject.get());
					enforcementMaster.setCaseUpdatingTimestamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
					enforcementMaster.setCaseUpdatedByUser(objectUserDetails.get());
					enforcementMaster.setEnforcementCaseDateDocumentDetails(list);
					enforcementMasterRepository.save(enforcementMaster);
					return enforcementMaster.getId() + " updated with the status "
							+ enforcementMaster.getAction().getStatus();
				}
			}
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : updateEnforcementCaseForPanchnamaAndPriliminaryReport : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return "Failed";
	}

	@Override
	public List<EnforcementActionStatus> getAllStatusFromEnforcementDateDocumentDetails(CompositeKey compositeKey) {
		try {
			EnforcementMaster em = enforcementMasterRepository.findById(compositeKey).get();
			List<EnforcementCaseDateDocumentDetails> enforcementCaseDateDocumentDetailsList = dateDocumentDetailsRepository
					.findByEnforcementMaster(em);
			List<EnforcementActionStatus> allStatus = enforcementCaseDateDocumentDetailsList.stream()
					.map(EnforcementCaseDateDocumentDetails::getAction)
					.sorted(Comparator.comparing(EnforcementActionStatus::getId)).collect(Collectors.toList());
			allStatus.addAll(enforcementActionStatusRepository.findNextStatus(em.getAction().getId()));
//			allStatus.forEach(System.out::println);
			return allStatus;
		} catch (Exception e) {
			logger.error("EnforcementFieldOfficeServiceImpl : getAllStatusFromEnforcementDateDocumentDetails : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public String updateEnforcementCaseForReferToAdjudictionAndIssueShowCause(
			EnforcementCaseUpdate enforcementCaseUpdateDetails) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			String recoveryStageArnArray = null;
			String recoveryStageArn = null;
			CompositeKey compositeKey = new CompositeKey(enforcementCaseUpdateDetails.getGSTIN(),
					enforcementCaseUpdateDetails.getCaseReportingDate(), enforcementCaseUpdateDetails.getPeriod());
			Optional<EnforcementMaster> emObject = enforcementMasterRepository.findById(compositeKey);
			Optional<EnforcementActionStatus> actionObject = enforcementActionStatusRepository
					.findById(enforcementCaseUpdateDetails.getUpdateStatusId());
			EnforcementCaseDateDocumentDetails caseDateDocumentDetails = null;
			if (emObject.isPresent()) {
				EnforcementMaster enforcementMaster = emObject.get();
				if (dateDocumentDetailsRepository
						.findByEnforcementMasterAndAction(enforcementMaster, actionObject.get()).isEmpty()) {
					caseDateDocumentDetails = new EnforcementCaseDateDocumentDetails();
					caseDateDocumentDetails.setEnforcementMaster(enforcementMaster);
					caseDateDocumentDetails.setActionDate(enforcementCaseUpdateDetails.getWorkingDate());
					caseDateDocumentDetails.setAction(actionObject.get());
					caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
					
					if (enforcementCaseUpdateDetails.getRecoveryStageArn() != null) {
						recoveryStageArnArray = getStringArn(enforcementCaseUpdateDetails.getRecoveryStageArn());
						recoveryStageArn = recoveryStageArnArray.substring(1, recoveryStageArnArray.length() - 1);
					} else {
						recoveryStageArn = "";
					}
					caseDateDocumentDetails.setRecoveryStageArn(recoveryStageArn);
					caseDateDocumentDetails.setRecoveryStage(enforcementCaseUpdateDetails.getRecoveryStage());
					
					caseDateDocumentDetails.setCaseId(enforcementCaseUpdateDetails.getCaseId() != null
							&& enforcementCaseUpdateDetails.getCaseId().trim().length() > 0
									? enforcementCaseUpdateDetails.getCaseId()
									: null);
					caseDateDocumentDetails
							.setAssignedToAssessmentFOUserId(enforcementCaseUpdateDetails.getAssignedUser() != null
									? enforcementCaseUpdateDetails.getAssignedUser()
									: null);
					caseDateDocumentDetails.setAssignedToAssessmentLocation(
							locationDetailsRepository.findById(enforcementCaseUpdateDetails.getLocation()).isPresent()
									? locationDetailsRepository.findById(enforcementCaseUpdateDetails.getLocation())
											.get()
									: null);
					// Save PDF
					MultipartFile pdfFile = enforcementCaseUpdateDetails.getPdfData().getPdfFile();
					byte[] pdfBytes = pdfFile.getBytes();
					LocalDateTime currentTimestamp = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
					String formattedTimestamp = currentTimestamp.format(formatter);
					String pdfFileName = enforcementMaster.getId().getGSTIN().replace('\\', '.').replace('/', '.')
							+ "_pdf_" + formattedTimestamp + "_" + actionObject.get().getCodeName() + ".pdf";
//					pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
					File enforcementTarget = new File(uploadDirectory, pdfFileName);
					Files.write(enforcementTarget.toPath(), pdfBytes);
					// End PDF saving
					caseDateDocumentDetails.setActionFileName(pdfFileName);
					List<EnforcementCaseDateDocumentDetails> list = new ArrayList<>();
					list.add(caseDateDocumentDetails);
					enforcementMaster
							.setAction(enforcementActionStatusRepository.findByCodeName("enforcementCompleted").get());
					enforcementMaster.setCaseId(enforcementCaseUpdateDetails.getCaseId() != null
							&& enforcementCaseUpdateDetails.getCaseId().trim().length() > 0
									? enforcementCaseUpdateDetails.getCaseId()
									: null);
					enforcementMaster.setCaseUpdatingTimestamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
					enforcementMaster.setCaseUpdatedByUser(objectUserDetails.get());
					enforcementMaster.setEnforcementCaseDateDocumentDetails(list);
					Optional<EnforcementReviewCase> ercObject = enforcementReviewCaseRepository.findById(compositeKey);
					if (ercObject.isEmpty()) {
						String extensionNo = enforcementMaster.getExtensionNo();
						String pdfFileNameForAssessment = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.')
								+ "_" + formattedTimestamp + ".pdf";
						Path assessmentTarget = Path.of(assessmentHqUploadDirectory, pdfFileNameForAssessment);
						Files.write(assessmentTarget, pdfBytes);
						// saving details
						ExtensionNoDocument extensionNoDocument = new ExtensionNoDocument();
						extensionNoDocument.setExtensionFileName(pdfFileNameForAssessment);
						extensionNoDocument.setExtensionNo(extensionNo);
						// Create Assessment case
						EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
						enforcementReviewCase.setId(compositeKey);
						if (actionObject.get().getCodeName().equals("issueShowCause")) {
							enforcementReviewCase.setAction("acknowledge");
						} else {
							enforcementReviewCase.setAction("upload");
						}
						enforcementReviewCase.setAssignedFrom("HQ");
						enforcementReviewCase.setAssignedFromUserId(0);
						enforcementReviewCase.setAssignedTo("FO");
						enforcementReviewCase.setAssignedToUserId(enforcementCaseUpdateDetails.getAssignedUser());
						enforcementReviewCase.setEnforcementCaseId(enforcementCaseUpdateDetails.getCaseId());
						enforcementReviewCase.setCaseUpdateDate(new Date());
						enforcementReviewCase.setCategory(enforcementMaster.getCategory().getName());
						enforcementReviewCase.setIndicativeTaxValue(enforcementMaster.getIndicativeTaxValue());
						enforcementReviewCase.setTaxpayerName(enforcementMaster.getTaxpayerName());
						enforcementReviewCase
								.setLocationDetails(caseDateDocumentDetails.getAssignedToAssessmentLocation());
						enforcementReviewCase.setExtensionNoDocument(extensionNoDocument);
						enforcementReviewCase.setParameter(enforcementMaster.getParameter());
						enforcementReviewCase.setExtensionNo(extensionNo);
						if (actionObject.get().getCodeName().equals("issueShowCause")) {
							enforcementReviewCase
									.setActionStatus(assessmentFoActionStatusRepository.findByName("Initiated").get());
							enforcementReviewCase.setCaseStage(caseStageRepository.findByName("DRC01 issued").get());
							enforcementReviewCase.setCaseId(enforcementMaster.getCaseId());
						}
						extensionNoDocument.getEnforcementReviewCases().add(enforcementReviewCase);
						// Save assessment case
						extensionNoDocumentsRepository.save(extensionNoDocument);
						EnforcementReviewCaseAssignedUsers ercAssignedUsers = new EnforcementReviewCaseAssignedUsers();
						ercAssignedUsers.setId(compositeKey);
						ercAssignedUsers.setHqUserId(0);
						ercAssignedUsers.setFoUserId(enforcementCaseUpdateDetails.getAssignedUser() != null
								? enforcementCaseUpdateDetails.getAssignedUser()
								: 0);
						ercAssignedUsers.setRuUserId(0);
						ercAssignedUsers.setApUserId(0);
						ercAssignedUsers.setRmUserId(0);
						enforcementReviewCaseAssignedUsersRepository.save(ercAssignedUsers);
					}
					enforcementMaster.setAssignedFrom("Enforcement_FO");
					enforcementMaster.setAssignedTo("ASSESSMENT_FO");
					enforcementMasterRepository.save(enforcementMaster);
					// Adding data in case workflow table
					EnforcementCaseCaseWorkflow enforcementCaseWorkflow = new EnforcementCaseCaseWorkflow();
					enforcementCaseWorkflow.setGstin(enforcementMaster.getId().getGSTIN());
					enforcementCaseWorkflow.setCaseReportingDate(enforcementMaster.getId().getCaseReportingDate());
					enforcementCaseWorkflow.setPeriod(enforcementMaster.getId().getPeriod());
					enforcementCaseWorkflow.setAssignedFrom(enforcementMaster.getAssignedFrom());
					enforcementCaseWorkflow.setAssignedFromUserId(objectUserDetails.get().getUserId());
					enforcementCaseWorkflow.setAssignedTo(enforcementMaster.getAssignedTo());
					enforcementCaseWorkflow.setAssignedFromLocation(enforcementMaster.getCaseLocation());
					enforcementCaseWorkflow
							.setAssignedToLocation(caseDateDocumentDetails.getAssignedToAssessmentLocation());
					enforcementCaseWorkflow
							.setAssigntoUserId(caseDateDocumentDetails.getAssignedToAssessmentFOUserId());
					enforcementCaseWorkflow.setCaseUpdatingTimestamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
					enforcementCaseWorkflow.setSupportingFile(pdfFileName);
					enforcementCaseWorkflow.setSupportingFileDirectory(uploadDirectory);
					enforcementCaseWorkflow
							.setAssignedFromLocationName(enforcementMaster.getCaseLocation().getLocationName());
					enforcementCaseWorkflow.setAssignedToLocationName(
							caseDateDocumentDetails.getAssignedToAssessmentLocation().getLocationName());
//					enforcementCaseWorkflow.setParameterName();
					enforcementCaseWorkflow.setAction(caseDateDocumentDetails.getAction());
					enfCaseWorkflowRepository.save(enforcementCaseWorkflow);
					return enforcementMaster.getId() + " has been assigned to Assessment module";
				}
			}
		} catch (Exception e) {
			logger.error(
					"EnforcementFieldOfficeServiceImpl : updateEnforcementCaseForReferToAdjudictionAndIssueShowCause : "
							+ e.getMessage());
			e.printStackTrace();
		}
		return "Failed";
	}
	
	@Override
	public List<EnforcementDashboardSummaryDTO> getDashboardSummaryByParamAndPeriod(String parameterId, String period,List<String> allMappedLocations) {
	    List<Object[]> result = enforcementMasterRepository.getDashboardSummaryByParamAndPeriod(parameterId, period,allMappedLocations);
	    List<EnforcementDashboardSummaryDTO> summaryList = new ArrayList<>();

	    for (Object[] row : result) {
	        EnforcementDashboardSummaryDTO dto = new EnforcementDashboardSummaryDTO();
	        dto.setZoneName((String) row[0]);
	        dto.setCircleName((String) row[1]);
	        dto.setIndicativeTaxValue(row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO);
	        dto.setAllottedCases(((Number) row[3]).longValue());
	        dto.setCasesCompleted(((Number) row[4]).longValue());
	        dto.setNotAcknowledged(((Number) row[5]).longValue());
	        dto.setTransferToScrutiny(((Number) row[6]).longValue());
	        dto.setAcknowledged(((Number) row[7]).longValue());
	        dto.setPanchnama(((Number) row[8]).longValue());
	        dto.setPreliminaryReport(((Number) row[9]).longValue());
	        dto.setFinalReport(((Number) row[10]).longValue());
	        dto.setReferToAdjudication(((Number) row[11]).longValue());
	        dto.setShowCauseIssued(((Number) row[12]).longValue());
	        dto.setSortOrder((Integer) row[13]);
	        dto.setZoneSort((String) row[14]);

	        summaryList.add(dto);
	    }

	    return summaryList;
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
}
