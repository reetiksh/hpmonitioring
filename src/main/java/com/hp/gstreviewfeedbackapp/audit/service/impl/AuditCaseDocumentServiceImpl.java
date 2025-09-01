package com.hp.gstreviewfeedbackapp.audit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFinalAmountRecoveryDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFinalAmountRecoveryDetailsLogs;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetailsLogs;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetailsLogs;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetailsLogs;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseFinalAmountRecoveryDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDarDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseFarDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.service.AuditCaseDocumentService;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

@Service
public class AuditCaseDocumentServiceImpl implements AuditCaseDocumentService {
	private static final Logger logger = LoggerFactory.getLogger(AuditCaseDocumentServiceImpl.class);
	@Autowired
	private AuditCaseDateDocumentDetailsLogsRepository auditCaseDateDocumentDetailsLogsRepository;
	@Autowired
	private AuditCaseDarDetailsLogsRepository auditCaseDarDetailsLogsRepository;
	@Autowired
	private AuditCaseFarDetailsLogsRepository auditCaseFarDetailsLogsRepository;
	@Autowired
	private AuditCaseFinalAmountRecoveryDetailsLogsRepository auditCaseClosureReportDetailsLogsRepository;

	@Override
	public void saveAuditCaseDateDocumentDetailsLogs(AuditCaseDateDocumentDetails object, UserDetails userDetails) {
		try {
			AuditCaseDateDocumentDetailsLogs auditCaseDateDocumentDetailsLogs = new AuditCaseDateDocumentDetailsLogs();

			auditCaseDateDocumentDetailsLogs.setAuditCaseDateDocumnetDetailsId(object.getId());
			auditCaseDateDocumentDetailsLogs.setAction(object.getAction().getStatus());
			auditCaseDateDocumentDetailsLogs.setActionDate(object.getActionDate());
			auditCaseDateDocumentDetailsLogs.setActionFilePath(object.getActionFilePath());
			auditCaseDateDocumentDetailsLogs
					.setCaseId(object.getCaseId() != null ? object.getCaseId().getCaseId() : null);
			auditCaseDateDocumentDetailsLogs.setLastUpdatedTimeStamp(object.getLastUpdatedTimeStamp());
			auditCaseDateDocumentDetailsLogs.setNilDarOrFar(object.getNilDar());
			auditCaseDateDocumentDetailsLogs.setUpdatedBy(userDetails);
			auditCaseDateDocumentDetailsLogs.setCommentFromL2Officer(object.getCommentFromL2Officer());

			auditCaseDateDocumentDetailsLogsRepository.save(auditCaseDateDocumentDetailsLogs);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AuditCaseDocumentServiceImpl : saveAuditCaseDateDocumentDetailsLogs : " + e.getMessage());
		}
	}

	@Override
	public void saveAuditCaseDarDetailsLogs(List<AuditCaseDarDetails> auditCaseDarDetailsList, UserDetails userDetails,
			String action) {
		try {
			List<AuditCaseDarDetailsLogs> auditCaseDarDetailsLogsList = new ArrayList<>();

			for (AuditCaseDarDetails auditCaseDarDetails : auditCaseDarDetailsList) {
				AuditCaseDarDetailsLogs auditCaseDarDetailsLogs = new AuditCaseDarDetailsLogs();

				auditCaseDarDetailsLogs.setActionDate(auditCaseDarDetails.getActionDate());
				auditCaseDarDetailsLogs.setAmountDropped(auditCaseDarDetails.getAmountDropped());
				auditCaseDarDetailsLogs.setAmountInvolved(auditCaseDarDetails.getAmountInvolved());
				auditCaseDarDetailsLogs.setAmountRecovered(auditCaseDarDetails.getAmountRecovered());
				auditCaseDarDetailsLogs.setAmountToBeRecovered(auditCaseDarDetails.getAmountToBeRecovered());
				auditCaseDarDetailsLogs.setAuditCaseDarDetailsId(auditCaseDarDetails.getId());
				auditCaseDarDetailsLogs.setAuditCaseDateDocumentDetailsId(
						auditCaseDarDetails.getAuditCaseDateDocumentDetailsId().getId());
				auditCaseDarDetailsLogs.setAuditParaCategory(auditCaseDarDetails.getAuditParaCategory().getName());
				auditCaseDarDetailsLogs.setLastUpdatedTimeStamp(auditCaseDarDetails.getLastUpdatedTimeStamp());
				auditCaseDarDetailsLogs.setNilDar(auditCaseDarDetails.getNilDar());
				auditCaseDarDetailsLogs.setRaiseQuery(auditCaseDarDetails.getRaiseQuery());
				auditCaseDarDetailsLogs.setUpdatedBy(userDetails);
				auditCaseDarDetailsLogs.setAction(action);

				auditCaseDarDetailsLogsList.add(auditCaseDarDetailsLogs);
			}

			auditCaseDarDetailsLogsRepository.saveAll(auditCaseDarDetailsLogsList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AuditCaseDocumentServiceImpl : saveAuditCaseDarDetailsLogs : " + e.getMessage());
		}
	}

	@Override
	public void saveAuditCaseFarDetailsLogs(List<AuditCaseFarDetails> auditCaseFarDetailsList, UserDetails userDetails,
			String action) {
		try {
			List<AuditCaseFarDetailsLogs> auditCaseFarDetailsLogsList = new ArrayList<>();

			for (AuditCaseFarDetails auditCaseFarDetails : auditCaseFarDetailsList) {
				AuditCaseFarDetailsLogs auditCaseFarDetailsLogs = new AuditCaseFarDetailsLogs();

				auditCaseFarDetailsLogs.setActionDate(auditCaseFarDetails.getActionDate());
				auditCaseFarDetailsLogs.setAmountDropped(auditCaseFarDetails.getAmountDropped());
				auditCaseFarDetailsLogs.setAmountInvolved(auditCaseFarDetails.getAmountInvolved());
				auditCaseFarDetailsLogs.setAmountRecovered(auditCaseFarDetails.getAmountRecovered());
				auditCaseFarDetailsLogs.setAmountToBeRecovered(auditCaseFarDetails.getAmountToBeRecovered());
				auditCaseFarDetailsLogs.setAuditCaseFarDetailsId(auditCaseFarDetails.getId());
				auditCaseFarDetailsLogs.setAuditCaseDateDocumentDetailsId(
						auditCaseFarDetails.getAuditCaseDateDocumentDetailsId().getId());
				auditCaseFarDetailsLogs.setAuditParaCategory(auditCaseFarDetails.getAuditParaCategory().getName());
				auditCaseFarDetailsLogs.setLastUpdatedTimeStamp(auditCaseFarDetails.getLastUpdatedTimeStamp());
				auditCaseFarDetailsLogs.setNilDar(auditCaseFarDetails.getNilDar());
				auditCaseFarDetailsLogs.setUpdatedBy(userDetails);
				auditCaseFarDetailsLogs.setAction(action);

				auditCaseFarDetailsLogsList.add(auditCaseFarDetailsLogs);
			}

			auditCaseFarDetailsLogsRepository.saveAll(auditCaseFarDetailsLogsList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AuditCaseDocumentServiceImpl : saveAuditCaseFarDetailsLogs : " + e.getMessage());
		}
	}

	@Override
	public void saveAuditCaseFinalAmountRecoveryDetailsLogs(
			List<AuditCaseFinalAmountRecoveryDetails> auditCaseClosureReportDetailsList, UserDetails userDetails,
			String action) {
		try {
			List<AuditCaseFinalAmountRecoveryDetailsLogs> auditCaseClosureReportDetailsLogsList = new ArrayList<>();

			for (AuditCaseFinalAmountRecoveryDetails auditCaseClosureReportDetails : auditCaseClosureReportDetailsList) {
				AuditCaseFinalAmountRecoveryDetailsLogs auditCaseClosureReportDetailsLogs = new AuditCaseFinalAmountRecoveryDetailsLogs();

				auditCaseClosureReportDetailsLogs.setActionDate(auditCaseClosureReportDetails.getActionDate());
				auditCaseClosureReportDetailsLogs.setAmountDropped(auditCaseClosureReportDetails.getAmountDropped());
				auditCaseClosureReportDetailsLogs.setAmountInvolved(auditCaseClosureReportDetails.getAmountInvolved());
				auditCaseClosureReportDetailsLogs
						.setAmountRecovered(auditCaseClosureReportDetails.getAmountRecovered());
				auditCaseClosureReportDetailsLogs
						.setAmountToBeRecovered(auditCaseClosureReportDetails.getAmountToBeRecovered());
				auditCaseClosureReportDetailsLogs
						.setAuditCaseClosureReportDetailsId(auditCaseClosureReportDetails.getId());
				auditCaseClosureReportDetailsLogs.setAuditCaseDateDocumentDetailsId(
						auditCaseClosureReportDetails.getAuditCaseDateDocumentDetailsId().getId());
				auditCaseClosureReportDetailsLogs
						.setAuditParaCategory(auditCaseClosureReportDetails.getAuditParaCategory().getName());
				auditCaseClosureReportDetailsLogs
						.setLastUpdatedTimeStamp(auditCaseClosureReportDetails.getLastUpdatedTimeStamp());
				auditCaseClosureReportDetailsLogs.setNilDar(auditCaseClosureReportDetails.getNilDar());
				auditCaseClosureReportDetailsLogs.setUpdatedBy(userDetails);
				auditCaseClosureReportDetailsLogs.setAction(action);

				auditCaseClosureReportDetailsLogsList.add(auditCaseClosureReportDetailsLogs);
			}

			auditCaseClosureReportDetailsLogsRepository.saveAll(auditCaseClosureReportDetailsLogsList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AuditCaseDocumentServiceImpl : saveAuditCaseClosureReportDetailsLogs : " + e.getMessage());
		}

	}

	@Override
	public void saveAuditCaseDateDocumentDetailsLogs(AuditCaseDateDocumentDetails object, UserDetails userDetails,
			String caseId) {
		try {
			AuditCaseDateDocumentDetailsLogs auditCaseDateDocumentDetailsLogs = new AuditCaseDateDocumentDetailsLogs();

			auditCaseDateDocumentDetailsLogs.setAuditCaseDateDocumnetDetailsId(object.getId());
			auditCaseDateDocumentDetailsLogs.setAction(object.getAction().getStatus());
			auditCaseDateDocumentDetailsLogs.setActionDate(object.getActionDate());
			auditCaseDateDocumentDetailsLogs.setActionFilePath(object.getActionFilePath());
			auditCaseDateDocumentDetailsLogs
					.setCaseId(object.getCaseId() != null ? object.getCaseId().getCaseId() : caseId);
			auditCaseDateDocumentDetailsLogs.setLastUpdatedTimeStamp(object.getLastUpdatedTimeStamp());
			auditCaseDateDocumentDetailsLogs.setNilDarOrFar(object.getNilDar());
			auditCaseDateDocumentDetailsLogs.setUpdatedBy(userDetails);
			auditCaseDateDocumentDetailsLogs.setCommentFromL2Officer(object.getCommentFromL2Officer());

			auditCaseDateDocumentDetailsLogsRepository.save(auditCaseDateDocumentDetailsLogs);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AuditCaseDocumentServiceImpl : saveAuditCaseDateDocumentDetailsLogs : " + e.getMessage());
		}
	}

}
