package com.hp.gstreviewfeedbackapp.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.data.CaseLogHistory;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.FoReviewCase;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

public interface FieldUserService {
	public JSONArray getTotalIndicativeTaxValueCategoryWise(Map<String, String> locationMap);

	public JSONArray getTotalDemandCategoryWise(Map<String, String> locationMap);

	public JSONArray getTotalRecoveryCategoryWise(Map<String, String> locationMap);

	public List<FoConsolidateCategoryWiseCaseData> getTotalCategoryWiseCasesList(List<String> workingLoacationIds);

	public void uploadDataSaveFoLogs(List<EnforcementReviewCase> enCases);

	public List<CaseLogHistory> importDataFromCaseWorkflowToCaseLogHistory(List<FoReviewCase> foReviewCaseLsit);

	public List<EnforcementReviewCase> getPeriodWiseCasesList(List<String> workingLocationIds, String category,
			int index);

	public ResponseEntity<Map<String, Object>> assesmentCaseApprovalRequestSubmission(CompositeKey compositeKey,
			String needApproval, String caseId, UserDetails userDetails);
}
