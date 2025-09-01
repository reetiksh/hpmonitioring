package com.hp.gstreviewfeedbackapp.audit.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;

public interface L1UserUploadAuditCasesService {

	String saveL1UploadAuditDataList(String extensionNo, String category, MultipartFile pdfFile, Integer userId,
			List<List<String>> list);
	
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations);
	
	public List<String> getDashboardData(List<String> allMappedLocations);


}
