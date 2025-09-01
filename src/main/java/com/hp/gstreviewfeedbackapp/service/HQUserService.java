package com.hp.gstreviewfeedbackapp.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.data.CaseLogHistory;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;

public interface HQUserService {

	List<CaseLogHistory> importDataFromCaseWorkflowToCaseLogHistory(List<CaseWorkflow> caseWorkflowList,
			CompositeKey compositeKey);

}
