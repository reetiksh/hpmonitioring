package com.hp.gstreviewfeedbackapp.scrutiny.service;

import java.util.List;

import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

public interface ScrutinyRuService {

	List<MstScrutinyCases> getMandatoryCasesList(List<String> workingLocationList);

	String recommendForScrutiny(String gstin, String period, String caseReportingDate, String remarks);

	List<MstScrutinyCases> getRandomCasesList(List<String> workingLocationList);

	String randomRecommendForScrutiny(String gstin, String period, String caseReportingDate, String remarks);

	List<MstScrutinyCases> getRecommendedFromHqUserCasesList(List<String> workingLocationList);

	String recommendFromHqForScrutiny(String gstin, String period, String caseReportingDate, String remarks);
	
	List<MstScrutinyCases> getReviewCasesListStatusWise(Integer id, List<String> workingLoacationList);

}
