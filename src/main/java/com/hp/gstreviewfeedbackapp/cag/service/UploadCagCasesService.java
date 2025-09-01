package com.hp.gstreviewfeedbackapp.cag.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.cag.model.CAGUploadForm;
import com.hp.gstreviewfeedbackapp.cag.model.MstCagCases;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

public interface UploadCagCasesService {
	String uploadCagCases(CAGUploadForm hqUploadForm, BindingResult formResult,RedirectAttributes redirectAttributes,Model model, List<List<String>> dataList) throws ParseException;
	//List<MstCagCases> getRequestForTransferList();
	//List<MstCagCases> getRandomCasesList(List<String> workingLoacationList);
	//String randomRecommendForCag(String recommendScrutinyGstin, String recommendScrutinyPeriod,String recommendScrutinyCaseReportingDate, String recommendScrutinyRemark);
}
