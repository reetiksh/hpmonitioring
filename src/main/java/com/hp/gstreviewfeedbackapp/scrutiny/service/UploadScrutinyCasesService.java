package com.hp.gstreviewfeedbackapp.scrutiny.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

public interface UploadScrutinyCasesService {
	String uploadScrutinyCases(HQUploadForm hqUploadForm, BindingResult formResult,RedirectAttributes redirectAttributes,Model model, List<List<String>> dataList) throws ParseException;
	List<MstScrutinyCases> getRequestForTransferList();
	List<MstScrutinyCases> getRandomCasesList(List<String> workingLoacationList);
	List<MstScrutinyCases> getRandomCasesListExceptClosureReport(List<String> workingLoacationList);
	String randomRecommendForScrutiny(String recommendScrutinyGstin, String recommendScrutinyPeriod,String recommendScrutinyCaseReportingDate, String recommendScrutinyRemark);
	List<MstScrutinyCases> getReviewCasesListStatusWise(Integer id, List<String> workingLoacationList);
}
