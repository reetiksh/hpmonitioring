package com.hp.gstreviewfeedbackapp.enforcement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

public interface EnforcementHQUserUploadEnforcementCasesService {
	public String saveHqUploadEnforcementDataList(String extensionNo, String category, MultipartFile pdfFile,
			Integer userId, List<List<String>> list);

	String uploadDataSaveEnforcementCaseHeadquarterLogs(EnforcementExtensionNoDocument extensionNoDocument,
			Integer userId);

	List<EnforcementMaster> getReviewCasesListStatusWise(Integer actionStatus, List<String> workingLoacationList);
}
