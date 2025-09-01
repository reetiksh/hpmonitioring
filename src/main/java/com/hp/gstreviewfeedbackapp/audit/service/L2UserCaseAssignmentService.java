package com.hp.gstreviewfeedbackapp.audit.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;

public interface L2UserCaseAssignmentService {

	public void saveAuditMasterCaseWorkflowDetails(AuditMaster auditMaster, Integer assignedFromUserId,
			Integer assignToUserId);

	public void saveL2UserWorkLogs(AuditMaster auditMaster, Integer assignedFromUserId, Integer assignToUserId);

	public String saveDarDetails(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	public String saveRecomandedToMcmDetails(L3UserAuditCaseUpdate userAuditCaseUpdate);

	public String saveRecommendationToOtherModule(String caseId, Integer userId, String comment);

	public String saveRecommendationRejected(String caseId, String comment);
	
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations);
	
	public List<String> getDashboardData(List<String> allMappedLocations);

}
