package com.hp.gstreviewfeedbackapp.model;

import org.springframework.web.multipart.MultipartFile;

public class PdfData {

	private MultipartFile pdfFile;

	public MultipartFile getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(MultipartFile pdfFile) {
		this.pdfFile = pdfFile;
	}

}
