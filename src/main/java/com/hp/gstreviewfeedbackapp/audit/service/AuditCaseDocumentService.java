package com.hp.gstreviewfeedbackapp.audit.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFinalAmountRecoveryDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

public interface AuditCaseDocumentService {

	void saveAuditCaseDateDocumentDetailsLogs(AuditCaseDateDocumentDetails object, UserDetails userDetails);

	void saveAuditCaseDarDetailsLogs(List<AuditCaseDarDetails> auditCaseDarDetailsList, UserDetails userDetails,
			String action);

	void saveAuditCaseFarDetailsLogs(List<AuditCaseFarDetails> auditCaseFarDetailsList, UserDetails userDetails,
			String action);

	void saveAuditCaseFinalAmountRecoveryDetailsLogs(
			List<AuditCaseFinalAmountRecoveryDetails> auditCaseClosureReportDetailsList, UserDetails userDetails,
			String action);

	void saveAuditCaseDateDocumentDetailsLogs(AuditCaseDateDocumentDetails auditCaseDateDocumentDetails,
			UserDetails userDetails, String caseId);

}
