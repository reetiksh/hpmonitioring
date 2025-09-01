package com.hp.gstreviewfeedbackapp.model;

import org.springframework.web.multipart.MultipartFile;

public class ExcelData {

	private MultipartFile excelFile;

	public MultipartFile getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(MultipartFile excelFile) {
		this.excelFile = excelFile;
	}

}
