package com.hp.gstreviewfeedbackapp.scrutiny.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyAsmtTenModel;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyProceedingModel;
import com.hp.gstreviewfeedbackapp.data.SelfDetectedCase;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;

public interface ScrutinyFoService {
	List<MstScrutinyCases> getScrutinyCases(List<String> actionStausList, List<String> workingLoacationList);

	String acknowledgedCase(WorkFlowModel workFlowModel);

	String dropScrutinyInitiatedProceeding(ScrutinyProceedingModel scrutinyProceedingModel);

	MstScrutinyCases scrutinyProceedingInitiated(String gstin, String caseReportingDate, String period);

	String submitAsmtTen(ScrutinyAsmtTenModel scrutinyAsmtTenModel);

	List<MstScrutinyCases> getInitiatedScrutinyCases(List<String> workingLoacationList);

	List<MstScrutinyCases> getScrutinyUpdateStatusCases(List<String> workingLoacationList);

	String submitClosureReport(ScrutinyAsmtTenModel scrutinyAsmtTenModel);

	String submitRecommendedForAssesment(ScrutinyAsmtTenModel scrutinyAsmtTenModel);
	
	String submitAudit(ScrutinyAsmtTenModel scrutinyAsmtTenModel);

	String requestForTransfer(WorkFlowModel transferModel);

	List<MstScrutinyCases> getReviewCasesListStatusWise(Integer actionStatus, List<String> workingLoacationList);
	
	public String saveScrutinyHqUserUploadDataList(Integer userId, List<List<String>> list, SelfDetectedCase selfDetectedCase);
}
