package com.hp.gstreviewfeedbackapp.scrutiny.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.gstreviewfeedbackapp.audit.model.AuditExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.service.L2UserCaseAssignmentService;
import com.hp.gstreviewfeedbackapp.data.SelfDetectedCase;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyAsmtTenModel;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyProceedingModel;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyRecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyTransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.ScrutinyFoService;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;

@Service
public class ScrutinyFoServiceImpl implements ScrutinyFoService {
	private static final org.jboss.logging.Logger logger = LoggerFactory.logger(ScrutinyFoServiceImpl.class);
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	@Autowired
	private ScrutinyRecoveryStageRepository scrutinyRecoveryStageRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private ScrutinyTransferRemarksRepository scrutinyTransferRemarksRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private AuditExtensionNoDocumentRepository auditExtensionNoDocumentRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private L2UserCaseAssignmentService l2UserCaseAssignmentService;
	@Autowired
	private AuditMasterCaseWorkflowRepository auditMasterCaseWorkflowRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private CommonUtility commonUtility;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	@Value("${upload.audit.directory}")
	private String HqPdfFileUploadLocation;

	@Override
	public List<MstScrutinyCases> getScrutinyCases(List<String> actionStatusList, List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getScrutinyCases(actionStatusList,
				workingLoacationList);
		return mstScrutinyCasesList;
	}

	@Override
	public String acknowledgedCase(WorkFlowModel workFlowModel) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(workFlowModel.getDate());
			compositKey.setGSTIN(workFlowModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(workFlowModel.getPeriod());
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository
					.findById(compositKey).get();
			scrutinyCasesPertainToUsers.setFoUserId(userId);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("acknowledgeScrutinyCase");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCases.setAcknowlegeByFoOrNot(true);
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Acknowledgement completed successfully";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public List<MstScrutinyCases> getInitiatedScrutinyCases(List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository
				.getInitiatedScrutinyCases(workingLoacationList);
		return mstScrutinyCasesList;
	}

	@Override
	public String dropScrutinyInitiatedProceeding(ScrutinyProceedingModel scrutinyProceedingModel) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = inputFormat.parse(scrutinyProceedingModel.getCasereportingdate());
			String formattedDateStr = outputFormat.format(date);
			Date outputDate = outputFormat.parse(formattedDateStr);
			compositKey.setGSTIN(scrutinyProceedingModel.getGstin());
			compositKey.setCaseReportingDate(outputDate);
			compositKey.setPeriod(scrutinyProceedingModel.getPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("noNeedScrutiny");
			mstScrutinyCases.setAssignedFrom("SFO");
			mstScrutinyCases.setAssignedTo("SRU");
			mstScrutinyCases.setRecoveryStage(
					scrutinyRecoveryStageRepository.findById(scrutinyProceedingModel.getRecoveryStage()).get());
			mstScrutinyCases.setAmountRecovered(scrutinyProceedingModel.getDemand());
			mstScrutinyCases.setRecoveryByDRC03(scrutinyProceedingModel.getRecoveryByDRC3());
			// scrutinyProceedingModel.getRecoveryStageArn().removeIf(item -> item == null);
			mstScrutinyCases.setRecoveryStageArn(scrutinyProceedingModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyProceedingModel.getRecoveryStageArn())
					: "");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			/*******************************
			 * Scrutiny Dropped upload file start
			 **********************************************/
			String revertedCaseFileName = scrutinyProceedingModel.getUploadedFile().getOriginalFilename();
			String timeStamp = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
			byte[] appealRevisionBytes = scrutinyProceedingModel.getUploadedFile().getBytes();
			File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
			OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
			revertedCaseOutputStream.write(appealRevisionBytes);
			revertedCaseOutputStream.close();
			/*******************************
			 * Scrutiny Dropped upload file end
			 **********************************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(outputDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(scrutinyProceedingModel.getRecoveryStage());
			scrutinyCaseWorkflow.setAmountRecovered(scrutinyProceedingModel.getDemand());
			scrutinyCaseWorkflow.setRecoveryByDRC3(scrutinyProceedingModel.getRecoveryByDRC3());
			scrutinyCaseWorkflow.setRecoveryStageArn(scrutinyProceedingModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyProceedingModel.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Scrutiny proceedings dropped successfully";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public MstScrutinyCases scrutinyProceedingInitiated(String gstin, String caseReportingDate, String period) {
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		try {
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(caseReportingDate);
			compositKey.setGSTIN(gstin);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases
					.setFilePath(scrutinyCaseWorkflowRepository.getFilePathForRecommendedForAssesmentAndAdjudication(
							compositKey.getGSTIN(), compositKey.getCaseReportingDate(), compositKey.getPeriod()));
		} catch (Exception ex) {
			logger.error("error :  " + ex.getMessage());
		}
		return mstScrutinyCases;
	}

	@Override
	public String submitAsmtTen(ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat
					.parse(dateFormat.format(originalFormat.parse(scrutinyAsmtTenModel.getAsmtCaseReportingDate())));
			compositKey.setGSTIN(scrutinyAsmtTenModel.getAsmtGstin());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(scrutinyAsmtTenModel.getAsmtPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("asmtTenIssued");
			mstScrutinyCases.setAsmtTenIssuedOrNot(true); // set asmt-10-issued
			mstScrutinyCases.setCaseId(scrutinyAsmtTenModel.getCaseId());
			mstScrutinyCases.setCaseStage(caseStageRepository.findById(2).get());
			mstScrutinyCases.setCaseStageArn(scrutinyAsmtTenModel.getCaseStageArn());
			mstScrutinyCases.setAmountRecovered(scrutinyAsmtTenModel.getDemand());
			mstScrutinyCases.setRecoveryStage(
					scrutinyRecoveryStageRepository.findById(scrutinyAsmtTenModel.getRecoveryStage()).get());
			mstScrutinyCases.setRecoveryByDRC03(scrutinyAsmtTenModel.getRecoveryByDRC3());
			// scrutinyAsmtTenModel.getRecoveryStageArn().removeIf(item -> item == null);
			mstScrutinyCases.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			/*******************************
			 * Scrutiny ASMT-10 Attached file start
			 **********************************************/
			String revertedCaseFileName = scrutinyAsmtTenModel.getAsmtTenUploadedFile().getOriginalFilename();
			String timeStamp = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
			byte[] appealRevisionBytes = scrutinyAsmtTenModel.getAsmtTenUploadedFile().getBytes();
			File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
			OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
			revertedCaseOutputStream.write(appealRevisionBytes);
			revertedCaseOutputStream.close();
			/*******************************
			 * Scrutiny ASMT-10 Attached file end
			 **********************************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			scrutinyCaseWorkflow.setCaseId(mstScrutinyCases.getCaseId());
			scrutinyCaseWorkflow.setCaseStage("ASMT-10 Issued");
			scrutinyCaseWorkflow.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
			/* scrutinyCaseWorkflow.setDemand(mstScrutinyCases.getDemand()); */
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn());
			scrutinyCaseWorkflow.setAsmtTenIssuedOrNot(mstScrutinyCases.getAsmtTenIssuedOrNot());
			scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
			scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "ASMT-10 issued successfully.";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public List<MstScrutinyCases> getScrutinyUpdateStatusCases(List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository
				.getScrutinyUpdateStatusCases(workingLoacationList);
		return mstScrutinyCasesList;
	}

	@Override
	public String submitClosureReport(ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat
					.parse(dateFormat.format(originalFormat.parse(scrutinyAsmtTenModel.getAsmtCaseReportingDate())));
			compositKey.setGSTIN(scrutinyAsmtTenModel.getAsmtGstin());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(scrutinyAsmtTenModel.getAsmtPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("closureReportDropped");
			mstScrutinyCases.setAssignedFrom("SFO");
			mstScrutinyCases.setAssignedTo("SRU");
			mstScrutinyCases.setAmountRecovered(scrutinyAsmtTenModel.getRecoveryByDRC3()); // as per requirment
																							// recovered amount will be
																							// equal to amount in drc03
																							// at the time of closure
																							// report
			mstScrutinyCases.setRecoveryStage(
					scrutinyRecoveryStageRepository.findById(scrutinyAsmtTenModel.getRecoveryStage()).get());
			mstScrutinyCases.setRecoveryByDRC03(scrutinyAsmtTenModel.getRecoveryByDRC3());
			mstScrutinyCases.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			/*******************************
			 * Scrutiny ASMT-10 Attached file start
			 **********************************************/
			String revertedCaseFileName = scrutinyAsmtTenModel.getClosureReportFile().getOriginalFilename();
			String timeStamp = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
			byte[] appealRevisionBytes = scrutinyAsmtTenModel.getClosureReportFile().getBytes();
			File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
			OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
			revertedCaseOutputStream.write(appealRevisionBytes);
			revertedCaseOutputStream.close();
			/*******************************
			 * Scrutiny ASMT-10 Attached file end
			 **********************************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			scrutinyCaseWorkflow.setCaseId(mstScrutinyCases.getCaseId());
			// scrutinyCaseWorkflow.setCaseStage("ASMT-10 Issued");
			scrutinyCaseWorkflow.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
			// scrutinyCaseWorkflow.setDemand(mstScrutinyCases.getDemand());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn());
			scrutinyCaseWorkflow.setAsmtTenIssuedOrNot(mstScrutinyCases.getAsmtTenIssuedOrNot());
			scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
			scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Closure report submitted successfully.";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public String submitAudit(ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		String message = "Recommended for audit successfully.";
		AuditMaster auditMaster = new AuditMaster();
		CompositeKey compositKey = new CompositeKey();
		AuditExtensionNoDocument auditExtensionNoDocument = new AuditExtensionNoDocument();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
		SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		AuditMasterCasesAllocatingUsers auditMasterCasesAllocatingUsers = new AuditMasterCasesAllocatingUsers();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat
					.parse(dateFormat.format(originalFormat.parse(scrutinyAsmtTenModel.getAsmtCaseReportingDate())));
			compositKey.setGSTIN(scrutinyAsmtTenModel.getAsmtGstin());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(scrutinyAsmtTenModel.getAsmtPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("recommendedForAudit");
			mstScrutinyCases.setAssignedFrom("SFO");
			mstScrutinyCases.setAssignedTo("L2");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			/*******************************
			 * Scrutiny Audit Attached file start
			 **********************************************/
			String revertedCaseFileName = scrutinyAsmtTenModel.getRecommendedForAuditFile().getOriginalFilename();
			String timeStamp = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
			byte[] appealRevisionBytes = scrutinyAsmtTenModel.getRecommendedForAuditFile().getBytes();
			File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
			OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
			revertedCaseOutputStream.write(appealRevisionBytes);
			revertedCaseOutputStream.close();
			/*******************************
			 * Scrutiny Audit Attached file end
			 **********************************************/
			/*******************************
			 * Scrutiny Audit Attached file paste on path
			 * it\\Documents\\upload_files\audit\\pdf\\ start
			 **********************************************/
			String revertedCaseFileNameAudit = scrutinyAsmtTenModel.getRecommendedForAuditFile().getOriginalFilename();
			String timeStampAudit = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStampAudit = timeStampAudit + "_" + revertedCaseFileNameAudit;
			byte[] appealRevisionBytesAudit = scrutinyAsmtTenModel.getRecommendedForAuditFile().getBytes();
			File revertedCaseFileByteStreamAudit = new File(
					HqPdfFileUploadLocation + revertedCaseFileNameWithTimeStampAudit);
			OutputStream revertedCaseOutputStreamAudit = new FileOutputStream(revertedCaseFileByteStreamAudit);
			revertedCaseOutputStreamAudit.write(appealRevisionBytesAudit);
			revertedCaseOutputStreamAudit.close();
			/*******************************
			 * Scrutiny Audit Attached file paste on path
			 * it\\Documents\\upload_files\audit\\pdf\\ start
			 **********************************************/
			/***************
			 * Save audit attached file in audit_extension_no_documents table start
			 **************/
			auditExtensionNoDocument.setExtensionFileName(revertedCaseFileNameWithTimeStamp);
			auditExtensionNoDocument.setExtensionNo(scrutinyAsmtTenModel.getCaseId());
			AuditExtensionNoDocument response = auditExtensionNoDocumentRepository.save(auditExtensionNoDocument);
			/***************
			 * Save audit attached file in audit_extension_no_documents table end
			 **************/
			/******************
			 * Save in scrutiny_caseworkflow table start
			 *************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			scrutinyCaseWorkflow.setCaseId(mstScrutinyCases.getCaseId());
			// scrutinyCaseWorkflow.setCaseStage("ASMT-10 Issued");
			scrutinyCaseWorkflow.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
			// scrutinyCaseWorkflow.setDemand(mstScrutinyCases.getDemand());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn());
			scrutinyCaseWorkflow.setAsmtTenIssuedOrNot(mstScrutinyCases.getAsmtTenIssuedOrNot());
			scrutinyCaseWorkflow.setCaseStage(mstScrutinyCases.getCaseStage().getName());
			scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
			scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());
			scrutinyCaseWorkflow.setParameter(mstScrutinyCases.getParameters());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			/*******************
			 * Save in scrutiny_caseworkflow table end
			 ***********************/
			/********************* Set Audit Master Value Start ***************************/
			auditMaster.setCaseId(mstScrutinyCases.getCaseId());
			auditMaster.setGSTIN(mstScrutinyCases.getId().getGSTIN());
			auditMaster.setCaseReportingDate(mstScrutinyCases.getId().getCaseReportingDate());
			auditMaster.setPeriod(mstScrutinyCases.getId().getPeriod());
			auditMaster.setAssignTo("L2");
			auditMaster.setAssignedFrom("SFO");
			auditMaster.setExtensionNo(mstScrutinyCases.getExtensionNo());
			auditMaster.setIndicativeTaxValue(mstScrutinyCases.getAmountRecovered());
			auditMaster.setLastUpdatedTimeStamp(new Date());
			auditMaster.setParameter(mstScrutinyCases.getParameters());
			auditMaster.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			auditMaster.setAction(auditCaseStatusRepository.findByStatus("caseAllocated").get());
			auditMaster.setAuditExtensionNoDocument(response);
			auditMaster.setCategory(categoryListRepository.findByName("Scrutiny"));
			auditMaster.setScrutinyCaseId(scrutinyAsmtTenModel.getCaseId());
			auditMaster.setLocationDetails(mstScrutinyCases.getLocationDetails());
			/********************* Set Audit Master Value End ***************************/
			/*********** Save in audit_case_workflow table start *************/
			l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster, userId, 0);
			/*********** Save in audit_case_workflow table end *************/
			/****************
			 * Save Data in audit_master_cases_allocating_users table start
			 *********************/
			auditMasterCasesAllocatingUsers.setCaseId(auditMaster.getCaseId());
			auditMasterCasesAllocatingUsers.setL1User(0);
			auditMasterCasesAllocatingUsers.setL2User(0);
			auditMasterCasesAllocatingUsers.setL3User(0);
			auditMasterCasesAllocatingUsers.setMcmUser(0);
			/****************
			 * Save Data in audit_master_cases_allocating_users table end
			 *********************/
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			auditMasterRepository.save(auditMaster);
			auditMasterCasesAllocatingUsersRepository.save(auditMasterCasesAllocatingUsers);
		} catch (Exception ex) {
			logger.error("ScrutinyFoServiceImpl: submitAudit()" + ex.getMessage());
			return message = "Network Issue !";
		}
		return message;
	}

	@Override
	public String submitRecommendedForAssesment(ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		String message = "";
		EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
		EnforcementReviewCaseAssignedUsers ercAssignedUsers = new EnforcementReviewCaseAssignedUsers();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
			SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat
					.parse(dateFormat.format(originalFormat.parse(scrutinyAsmtTenModel.getAsmtCaseReportingDate())));
			compositKey.setGSTIN(scrutinyAsmtTenModel.getAsmtGstin());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(scrutinyAsmtTenModel.getAsmtPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("recommendedForAssesAndAdjudication");
			mstScrutinyCases.setAssignedFrom("SFO");
			mstScrutinyCases.setAssignedTo("FO");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			/**********************
			 * insert entry in enforcement table for scrutiny case start
			 *************************/
			Optional<EnforcementReviewCase> enforcementReviewCaseList = enforcementReviewCaseRepository
					.findById(compositKey);
			if (enforcementReviewCaseList.isPresent()) {
				return "Case Already Initiated in Assesment.";
			} else {
				enforcementReviewCase.setId(compositKey);
				enforcementReviewCase.setAction("recommendedForAssesAndAdjudication");
				enforcementReviewCase.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
				enforcementReviewCase.setAssignedFrom("SFO");
				enforcementReviewCase.setAssignedFromUserId(userId);
				enforcementReviewCase.setAssignedTo("FO");
				enforcementReviewCase.setCaseId(mstScrutinyCases.getCaseId());
				enforcementReviewCase.setScrutinyCaseId(mstScrutinyCases.getCaseId());
				enforcementReviewCase.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
				enforcementReviewCase.setCaseUpdateDate(new Date());
				enforcementReviewCase.setCategory("Scrutiny");
				enforcementReviewCase.setDemand(mstScrutinyCases.getAmountRecovered());
				/* enforcementReviewCase.setExtensionNo(""); */
				enforcementReviewCase.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
				enforcementReviewCase.setRecoveryAgainstDemand(mstScrutinyCases.getRecoveryAgainstDemand());
				enforcementReviewCase.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
				enforcementReviewCase.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn());
				enforcementReviewCase.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
				enforcementReviewCase.setActionStatus(actionStatusRepository.findById(2).get());
				enforcementReviewCase.setCaseStage(mstScrutinyCases.getCaseStage());
				enforcementReviewCase.setParameter(mstScrutinyCases.getParameters());
				/* enforcementReviewCase.setExtensionNoDocument(null); */
				enforcementReviewCase.setLocationDetails(mstScrutinyCases.getLocationDetails());
				enforcementReviewCase.setRecoveryStage(
						recoveryStageRepository.findById(mstScrutinyCases.getRecoveryStage().getId()).get());
				ercAssignedUsers.setId(compositKey);
				ercAssignedUsers.setHqUserId(0);
				ercAssignedUsers.setFoUserId(0);
				ercAssignedUsers.setRuUserId(0);
				ercAssignedUsers.setApUserId(0);
				ercAssignedUsers.setRmUserId(0);
				enforcementReviewCaseAssignedUsersRepository.save(ercAssignedUsers);
				enforcementReviewCaseRepository.save(enforcementReviewCase);
			}
			/**********************
			 * insert entry in enforcement table for scrutiny case start
			 *************************/
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			/*******************************
			 * Scrutiny ASMT-10 Attached file start
			 **********************************************/
			String revertedCaseFileName = scrutinyAsmtTenModel.getRecommendedForAssessAndAdjudicationFile()
					.getOriginalFilename();
			String timeStamp = fileDateFormat.format(new java.util.Date());
			String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
			byte[] appealRevisionBytes = scrutinyAsmtTenModel.getRecommendedForAssessAndAdjudicationFile().getBytes();
			File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
			OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
			revertedCaseOutputStream.write(appealRevisionBytes);
			revertedCaseOutputStream.close();
			/*******************************
			 * Scrutiny ASMT-10 Attached file end
			 **********************************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(scrutinyAsmtTenModel.getRecoveryStageArn() != null
					? String.join(",", scrutinyAsmtTenModel.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			scrutinyCaseWorkflow.setCaseId(mstScrutinyCases.getCaseId());
			// scrutinyCaseWorkflow.setCaseStage("ASMT-10 Issued");
			scrutinyCaseWorkflow.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
			// scrutinyCaseWorkflow.setDemand(mstScrutinyCases.getDemand());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn());
			scrutinyCaseWorkflow.setAsmtTenIssuedOrNot(mstScrutinyCases.getAsmtTenIssuedOrNot());
			scrutinyCaseWorkflow.setCaseStage(mstScrutinyCases.getCaseStage().getName());
			scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
			scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());
			scrutinyCaseWorkflow.setParameter(mstScrutinyCases.getParameters());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			Date setCaseRecommendedDateForAssessmentAndAdjudication = dateFormat
					.parse(scrutinyAsmtTenModel.getRecommendedOnDate());
			scrutinyCaseWorkflow.setCaseRecommendedDateForAssessmentAndAdjudication(
					setCaseRecommendedDateForAssessmentAndAdjudication);
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Recommended for assesment and adjudication successfully.";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public String requestForTransfer(WorkFlowModel transferModel) {
		String message = "";
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(transferModel.getDate());
			compositKey.setGSTIN(transferModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(transferModel.getPeriod());
			MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("requestForTransfer");
			mstScrutinyCases.setAssignedFrom("SFO");
			mstScrutinyCases.setAssignedTo("SHQ");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			/*******************************
			 * Scrutiny Dropped upload file start
			 **********************************************/
			if (transferModel.getUploadedFile() != null) {
				String revertedCaseFileName = transferModel.getUploadedFile().getOriginalFilename();
				String timeStamp = fileDateFormat.format(new java.util.Date());
				String revertedCaseFileNameWithTimeStamp = timeStamp + "_" + revertedCaseFileName;
				byte[] appealRevisionBytes = transferModel.getUploadedFile().getBytes();
				File revertedCaseFileByteStream = new File(pdfFilePath + revertedCaseFileNameWithTimeStamp);
				OutputStream revertedCaseOutputStream = new FileOutputStream(revertedCaseFileByteStream);
				revertedCaseOutputStream.write(appealRevisionBytes);
				revertedCaseOutputStream.close();
				scrutinyCaseWorkflow.setFilePath(revertedCaseFileNameWithTimeStamp);
			}
			/*******************************
			 * Scrutiny Dropped upload file end
			 **********************************************/
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			// scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId()
			// != null ? mstScrutinyCases.getRecoveryStage().getId() :
			// Integer.parseInt("0"));
			// scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			// scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			// scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn()
			// != null ? String.join(",", mstScrutinyCases.getRecoveryStageArn()): "");
			// scrutinyCaseWorkflow.setCaseId(mstScrutinyCases.getCaseId());
			// scrutinyCaseWorkflow.setCaseStage("ASMT-10 Issued");
			// scrutinyCaseWorkflow.setCaseStageArn(mstScrutinyCases.getCaseStageArn());
			// scrutinyCaseWorkflow.setDemand(mstScrutinyCases.getDemand());
			// scrutinyCaseWorkflow.setAsmtTenIssuedOrNot(mstScrutinyCases.getAsmtTenIssuedOrNot());
			// scrutinyCaseWorkflow.setCaseStage(mstScrutinyCases.getCaseStage().getName());
			scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
			scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());
			if (transferModel.getRemarkOptions().equals("1")) {
				scrutinyCaseWorkflow.setRemarks(scrutinyTransferRemarksRepository
						.findById(Integer.parseInt(transferModel.getRemarkOptions())).get());
				scrutinyCaseWorkflow.setOtherRemarks(transferModel.getOtherRemarks());
			} else {
				scrutinyCaseWorkflow.setRemarks(scrutinyTransferRemarksRepository
						.findById(Integer.parseInt(transferModel.getRemarkOptions())).get());
			}
			scrutinyCaseWorkflow.setAssignedFromLocationId(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setAssignedToLocationId(transferModel.getCaseAssignedTo());
			/* scrutinyCaseWorkflow.setRemarks(transferModel.getRemarkOptions()); */
			/* scrutinyCaseWorkflow.setFilePath(null); */
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Transfer request completed successfully";
		} catch (Exception ex) {
			message = "There is some error....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public List<MstScrutinyCases> getReviewCasesListStatusWise(Integer actionStatus,
			List<String> workingLoacationList) {
		List<MstScrutinyCases> casesList = new ArrayList<>();
		switch (actionStatus) {
		case 1:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesList(workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 2:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("noNeedScrutiny",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 3:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("asmtTenIssued",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 4:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("closureReportDropped",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 5:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAssesAndAdjudication",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 6:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("acknowledgeScrutinyCase",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 7:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAudit",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		default:
			casesList = new ArrayList<>();
		}
		return casesList;
	}
	
	@Override
	@Transactional
	public String saveScrutinyHqUserUploadDataList(Integer assignedToUserId, List<List<String>> uploadDataList,SelfDetectedCase selfDetectedCase) {
		try {
			List<CaseWorkflow> caseWorkflowList = new ArrayList<>();
			List<MstScrutinyCases> mstScrutinyCasesList = new ArrayList<>();
			List<ScrutinyCasesPertainToUsers> scrutinyCasesPertainToUsersList = new ArrayList<>();
			String modifiedString = "";
			MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = new ScrutinyCasesPertainToUsers();
			// save Data List
			for (List<String> row : uploadDataList) {
				CompositeKey compositeKey = new CompositeKey();
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(2));
				compositeKey.setGSTIN(row.get(0));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(row.get(4));
				Optional<MstScrutinyCases> mstScrutinyCaseExist = mstScrutinyCasesRepository.findById(compositeKey);
				if (mstScrutinyCaseExist.isPresent()) {
					MstScrutinyCases object = mstScrutinyCaseExist.get();
					logger.error("Duplicated value found on save uploded cases : " + compositeKey);
					return "Record already exists!";
				}
				
				scrutinyCasesPertainToUsers.setId(compositeKey);
				scrutinyCasesPertainToUsers.setHqUserId(0);
				scrutinyCasesPertainToUsers.setFoUserId(assignedToUserId);
				scrutinyCasesPertainToUsers.setRuUserId(0);
				scrutinyCasesPertainToUsersList.add(scrutinyCasesPertainToUsers);
				
				mstScrutinyCases.setId(compositeKey);
				mstScrutinyCases.setAcknowlegeByFoOrNot(false);
				mstScrutinyCases.setAction("uploadScrutinyCases");
				mstScrutinyCases.setCategory(categoryListRepository.findByNameAndModule("Self Detected Cases","scrutiny"));
				mstScrutinyCases.setTaxpayerName(row.get(1));
				Double indicativeTaxValue = Double.parseDouble(row.get(3));
				mstScrutinyCases.setIndicativeTaxValue(indicativeTaxValue.longValue());
				mstScrutinyCases.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				mstScrutinyCases.setAssignedTo("SFO");
				mstScrutinyCases.setAssignedFrom("SHQ");
				mstScrutinyCases.setCaseUpdateDate(new Date());
				mstScrutinyCases.setCaseId(row.get(7));
				//mstScrutinyCases.setFindingCategory((row.get(7) != null && !row.get(7).trim().isEmpty()) ? row.get(7) : null);
				// Remarks
				Map<String, String> allParameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getParamName(), param -> param.getId().toString()));
				
				if (row.get(6) != null && !row.get(6).trim().isEmpty()) {
					List<String> parameterIds = Arrays.stream(row.get(6).trim().split(",")).map(paramName -> paramName)
							.map(allParameterMap::get).collect(Collectors.toList());
					parameterIds.removeIf(item -> item == null);
					mstScrutinyCases.setParameters(String.join(",", parameterIds));
				}
				mstScrutinyCasesList.add(mstScrutinyCases);
				logger.info("Uploded Case : " + mstScrutinyCases);
				
				caseWorkflow.setGSTIN(row.get(0));
				caseWorkflow.setCaseReportingDate(parsedDate);
				caseWorkflow.setPeriod(row.get(4));
				caseWorkflow.setAssignedFrom("SHQ");
				caseWorkflow.setAssignedFromUserId(assignedToUserId);
				caseWorkflow.setAssignedTo("SFO");
				caseWorkflow.setAssigntoUserId(assignedToUserId);
				caseWorkflow.setAssignedFromLocationId(locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationId());
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAction("uploadFromSFO");
				caseWorkflow.setAssignedToLocationId(locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationId());
				caseWorkflow.setAssignedToLocationName(row.get(5));
				String remark = selfDetectedCase.getRemark();
				String otherRemark = (selfDetectedCase.getOtherRemarks() != null && !selfDetectedCase.getOtherRemarks().trim().isEmpty()) ? selfDetectedCase.getOtherRemarks() : "";
				if(!otherRemark.isEmpty())
					modifiedString = remark.trim().replace("Others",otherRemark);
				caseWorkflow.setOtherRemarks(modifiedString);
				caseWorkflowList.add(caseWorkflow);
			}
			if (mstScrutinyCasesList != null && mstScrutinyCasesList.size() > 0) {
				mstScrutinyCasesRepository.save(mstScrutinyCases);
				scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
				caseWorkflowRepository.saveAll(caseWorkflowList);
				return "Form submitted successfully!";
			}
		} catch (Exception e) {
			logger.error("Field Office | Self Detected Case | Uploded Case : Unable to save upload cases : "
					+ e.getMessage());
		}
		return "Unable to Submit the Form!";
	}
}
