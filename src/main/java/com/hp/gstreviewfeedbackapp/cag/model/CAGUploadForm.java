package com.hp.gstreviewfeedbackapp.cag.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.PdfData;

public class CAGUploadForm {

	@NotEmpty(message = "Extension No. cannot be empty")
	private String extensionNo;

	private String category;

	private PdfData pdfData; 
	
	private ExcelData excelData;
	
	private List<String> excelErrors ;

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public PdfData getPdfData() {
		return pdfData;
	}

	public void setPdfData(PdfData pdfData) {
		this.pdfData = pdfData;
	}

	public ExcelData getExcelData() {
		return excelData;
	}

	public void setExcelData(ExcelData excelData) {
		this.excelData = excelData;
	}

	public List<String> getExcelErrors() {
		return excelErrors;
	}

	public void setExcelErrors(List<String> excelErrors) {
		this.excelErrors = excelErrors;
	}
	
	
}
