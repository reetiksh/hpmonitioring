package com.hp.gstreviewfeedbackapp.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {
	private byte[] fileContent;
	private String fileName;
	private String contentType;

	public CustomMultipartFile(byte[] fileContent, String fileName, String contentType) {
		this.fileContent = fileContent;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	@Override
	public String getName() {
		return fileName;
	}

	@Override
	public String getOriginalFilename() {
		return fileName;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return fileContent.length == 0;
	}

	@Override
	public long getSize() {
		return fileContent.length;
	}

	@Override
	public byte[] getBytes() {
		return fileContent;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(fileContent);
	}

	@Override
	public void transferTo(File dest) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(dest)) {
			fos.write(fileContent);
		}
	}
}
