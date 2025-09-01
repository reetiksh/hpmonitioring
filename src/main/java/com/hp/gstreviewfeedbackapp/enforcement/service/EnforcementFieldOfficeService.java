package com.hp.gstreviewfeedbackapp.enforcement.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hp.gstreviewfeedbackapp.enforcement.data.EnforcementCaseUpdate;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementActionStatus;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementDashboardSummaryDTO;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

public interface EnforcementFieldOfficeService {
	public ResponseEntity<Map<String, Object>> enforcementCaseApprovalRequestSubmission(CompositeKey compositeKey,
			String needApproval, String caseId, UserDetails userDetails);

	public void saveEnforcementFoUserLogs(EnforcementMaster enforcementMaster, Integer assignedFromUserId,
			Integer assignedToUserId);

	public String saveEnforcementCaseToScrutinyModule(EnforcementMaster enforcementMaster);

	public void saveEnforcementCaseCaseWorkflow(EnforcementMaster enforcementMaster);

	public String acknowledgeEnforcementCase(EnforcementMaster enforcementMaster);

	public String updateEnforcementCaseForPanchnamaAndPriliminaryReportAndFinalReport(
			EnforcementCaseUpdate enforcementCaseUpdateDetails);

	public List<EnforcementActionStatus> getAllStatusFromEnforcementDateDocumentDetails(CompositeKey compositeKey);

	public String updateEnforcementCaseForReferToAdjudictionAndIssueShowCause(
			EnforcementCaseUpdate enforcementCaseUpdateDetails);
	
	
	public List<EnforcementDashboardSummaryDTO> getDashboardSummaryByParamAndPeriod(String parameterId, String period,List<String> allMappedLocations);
}
