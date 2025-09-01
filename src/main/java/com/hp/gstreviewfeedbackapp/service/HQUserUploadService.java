package com.hp.gstreviewfeedbackapp.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface HQUserUploadService {

	public String saveHqUserUploadData(String extensionNo, String category, MultipartFile pdfFile,
			MultipartFile excelFile, int userId) throws IOException, ParseException;

	public String saveHqUserUploadDataList(String extensionNo, String category, MultipartFile pdfFile, Integer userId,
			List<List<String>> dataList) throws IOException, ParseException;

	public String saveHqUserUploadDataList(Integer userId, List<List<String>> list, String category);

}
