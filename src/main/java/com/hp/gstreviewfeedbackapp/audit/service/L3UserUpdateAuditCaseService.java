package com.hp.gstreviewfeedbackapp.audit.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;

public interface L3UserUpdateAuditCaseService {

	String updateAuditCaseForADT01IssuedOrAuditPlanOrCommencementOfAuditOrDiscrepancyNotice(
			L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	String updateAuditCaseForDar(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	Integer checkIfTheActiveStausIsAuditPlanOrDar(String caseId, Integer activeActionPannelId, String statusCategory);

	String updateAuditCaseForAdt02Far(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	String updateAuditCaseForFinalAmountRecovery(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	String updateAuditCaseForShowCauseNotice(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);

	String updateAuditCaseForClosureReport(L3UserAuditCaseUpdate l3UserAuditCaseUpdate1);

	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations);
	
	public List<String> getDashboardData(List<String> allMappedLocations);


}
