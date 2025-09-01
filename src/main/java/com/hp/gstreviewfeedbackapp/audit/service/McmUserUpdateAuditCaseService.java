package com.hp.gstreviewfeedbackapp.audit.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;

public interface McmUserUpdateAuditCaseService {

	String saveDarDetails(L3UserAuditCaseUpdate l3UserAuditCaseUpdate);
	
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations);
	
	public List<String> getDashboardData(List<String> allMappedLocations);

}
