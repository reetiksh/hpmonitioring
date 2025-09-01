package com.hp.gstreviewfeedbackapp.audit.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.CircleDetails;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.CircleDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Component
public class ExcelValidatorForUploadAuditCases {
	private static final Logger logger = LoggerFactory.getLogger(ExcelValidatorForUploadAuditCases.class);
	private static String[] expectedColumnNames = { "Case Id", "GSTIN", "Taxpayer Name", "Case Reporting Date",
			"Period", "Assigned To Circle", "Parameter_1", "Parameter_2", "Parameter_3", "Parameter_4", "Parameter_5" };
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private CircleDetailsRepository circleDetailsRepository;

	public Map<String, List<List<String>>> validateExcelAndExtractDataForUploadAuditCases(MultipartFile excelFile)
			throws IOException {
		InputStream inputStream = null;
		Workbook workbook = null;
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();
		try {
			List<String> caseIdList = new ArrayList<>();
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
//			if(headerColumnCount != expectedColumnNames.length) {
//				errorList.add(ApplicationConstants.COLUMNS_SIZE_NOT_MATCHED);
//				return errorList;
//			}
			if (sheet.getLastRowNum() <= 0) {
				errorList.add("The Excel is blank!");
				errors.add(errorList);
				outputData.put("errorList", errors);
				return outputData;
			}
			for (int i = 0; i < expectedColumnNames.length; i++) {
				Cell cell = headerRow.getCell(i);
				String actualColumnName = cell.getStringCellValue().trim();
				if (!expectedColumnNames[i].equals(actualColumnName)) {
					errorList.add("Column " + actualColumnName + ": "
							+ ApplicationConstants.EXCEL_HEADER_COLUMNS_NOT_MATCHED);
				}
				if (errorList.size() > 0) {
					errorList.add("Please check the sample format for the reference");
					errors.add(errorList);
					outputData.put("errorList", errors);
					return outputData;
				}
			}
			if (sheet.getLastRowNum() < 1) {
				errorList.add("The Excel has no enforcement review case data!");
				errors.add(errorList);
				outputData.put("errorList", errors);
				return outputData;
			}
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				String caseId = "";
				String GSTIN = "";
				String taxPayerName = "";
//				Date caseRortingDate = null;
				String caseRortingDateStr = "";
				String suspectedIndicativeTaxValue = "0";
				String period = "";
				String assignToJurisdiction = "";
				List<String> parameters = new ArrayList<>();
				boolean dataValidated = true;
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					errorList.add("Row " + rowIndex + ": The row is blank. Please delete the row from excel.");
					continue;
				}
				String dateCellValue = (row.getCell(0) != null ? row.getCell(0).toString().trim() : "");
				/********* Check if the total row is blank *********/
				if (dateCellValue.length() == 0) {
					boolean flag = false;
					for (int i = 0; i < expectedColumnNames.length; i++) {
						String cellValue = (row.getCell(i) != null ? row.getCell(i).toString().trim() : "");
						if (cellValue.length() != 0) {
							flag = true;
							break;
						}
					}
					if (flag == false) {
						errorList.add("Row " + rowIndex + ": The row is blank. Please delete the row from excel.");
						continue;
					}
					dataValidated = false;
				}
				/********** Validate Case id number **********/
				caseId = dateCellValue;
				if (dateCellValue.length() > 0) {
					if (dateCellValue.length() != 15 || !dateCellValue.matches("[A-Z0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex
								+ ": The Case Id (15-digits alphanumeric in capital letter) no is not correct : "
								+ dateCellValue);
					}
				} else {
					dataValidated = false;
				}
				/********** Validate GSTIN number **********/
				dateCellValue = (row.getCell(1) != null ? row.getCell(1).toString().trim() : "");
				GSTIN = dateCellValue;
				if (dateCellValue.length() > 0) {
					if (dateCellValue.length() != 15 || !dateCellValue.matches("[A-Z0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex
								+ ": The GSTIN no (15-digits alphanumeric in capital letter) is not correct : "
								+ dateCellValue);
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": GSTIN is blank.");
				}
				/********** Validate Taxpayer Name **********/
				dateCellValue = (row.getCell(2) != null ? row.getCell(2).toString().trim() : "");
				taxPayerName = dateCellValue;
				if (dateCellValue.length() <= 0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Taxpayer Name is blank.");
				} else if (dateCellValue.length() > 200) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Taxpayer Name is more than 200 letter : " + dateCellValue);
				}
				/********** CASE REPORTING DATE validation **********/
				row = sheet.getRow(rowIndex);
				String dataString;
				dateCellValue = (row.getCell(3) != null ? row.getCell(3).toString().trim() : "");
				if (dateCellValue.matches("[-0-9]+") && dateCellValue.length() == 10 || dateCellValue.length() == 11) {
					Date date = (dateCellValue.length() == 10 ? new SimpleDateFormat("dd-MM-yyyy").parse(dateCellValue)
							: new SimpleDateFormat("dd-MMM-yyyy").parse(dateCellValue));
//					caseRortingDate = date;
					dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
					caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
					if (!excelValidator.checkDateFormat(dataString)) {
						dataValidated = false;
						errorList
								.add("Row " + rowIndex + ": " + ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE);
//			        	return errorList;
					} else if (new Date().before(date)) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Future date is not allowed. Please check the date.");
//			        	return errorList;
					}
				} else {
					dataValidated = false;
					if (dateCellValue.length() > 0) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": " + ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE
								+ " : " + dateCellValue);
					} else {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": The Case Reporting Date is blank.");
					}
//					return errorList;
				}
				/********** Suspected Indicative Tax Value validation **********/
//				dateCellValue = (row.getCell(4) != null ? row.getCell(4).toString().trim() : "");
//				if (dateCellValue.length() > 0) {
//					BigDecimal bigDecimalValue = new BigDecimal(dateCellValue);
//					// Convert BigDecimal to a string with full precision
//					dateCellValue = bigDecimalValue.toPlainString();
//				}
//				suspectedIndicativeTaxValue = dateCellValue;
//				if (dateCellValue.length() <= 0) {
//					dataValidated = false;
//					errorList.add("Row " + rowIndex + ": The Suspected Indicative Tax Value is blank.");
//				} else if (dateCellValue.length() > 11
//						|| !Long.toString((long) Double.parseDouble(dateCellValue)).matches("[0-9]+")) {
//					dataValidated = false;
//					errorList.add("Row " + rowIndex
//							+ ": The Suspected Indicative Tax Value is not correct or have more than 11 digits : "
//							+ dateCellValue);
//				}
				/********** Period Validation **********/
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				dateCellValue = (row.getCell(4) != null ? row.getCell(4).toString().trim() : "");
				period = dateCellValue;
				if (dateCellValue.length() > 0) {
					String[] tokens = dateCellValue.split("-");
					if (!dateCellValue.matches("[-0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Period is not correct : " + dateCellValue);
					} else if ((Integer.parseInt(tokens[0]) > year)
							|| (Integer.parseInt(tokens[1]) != ((Integer.parseInt(tokens[0]) % 100) + 1))
							|| tokens.length != 2) {
//						System.out.println(Integer.parseInt(tokens[0]));
//						System.out.println(Integer.parseInt(tokens[1]));
//						System.out.println((Integer.parseInt(tokens[0])% 100)+1);
//						System.out.println(tokens.length);
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Period is not correct : " + dateCellValue);
						// return errorList;
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Period is blank.");
				}
				/********** Jurisdiction Name validation **********/
				dateCellValue = (row.getCell(5) != null ? row.getCell(5).toString().trim() : "");
				assignToJurisdiction = dateCellValue;
				if (dateCellValue.length() > 0) {
//				    Optional<LocationDetails> objectLD = locationDetailsRepository.findByLocationName(dateCellValue);
					Optional<CircleDetails> objectCD = circleDetailsRepository.findByCircleName(dateCellValue);
					if (!objectCD.isPresent() || dateCellValue.equalsIgnoreCase("NA")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": " + ApplicationConstants.JURISDICTION_DOES_NOT_EXIST
								+ " : " + dateCellValue);
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Jurisdiction Name is blank.");
				}
				/********** Parameter(s) validation **********/
				for (int i = 6; i <= expectedColumnNames.length; i++) {
					dateCellValue = (row.getCell(i) != null ? row.getCell(i).toString().trim() : "");
					if (dateCellValue.length() > 0) {
						Optional<MstParametersModuleWise> objectAP = mstParametersModuleWiseRepository
								.findByParamNameAndStatusAudit(dateCellValue, true);
						if (!objectAP.isPresent()) {
							dataValidated = false;
							errorList.add(
									"Row " + rowIndex + ": Parameter_" + (i - 5) + " is not valid : " + dateCellValue);
						} else {
							if (parameters.contains(dateCellValue)) {
								dataValidated = false;
								errorList.add("Row " + rowIndex + ": Parameter_" + (i - 6) + " is repeted : "
										+ dateCellValue);
							} else {
								parameters.add(dateCellValue);
							}
						}
					}
				}
				if (parameters.size() == 0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Parameter is not available");
				} else {
					for (int i = parameters.size(); i < 5; i++) {
						parameters.add("");
					}
				}
				/********** Check duplicate value **********/
				if (dataValidated == true) {
					// Checking if excel sheet have duplicate value itself
					if (caseIdList.contains(caseId)) {
						dataValidated = false;
						errorList
								.add("Row " + rowIndex + ": The excel has a repetitive entry with case id : " + caseId);
					} else {
						caseIdList.add(caseId);
					}
					// Checking if excel sheet have duplicate value in DB already
					if (auditMasterRepository.findById(caseId).isPresent()) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Duplicate value found with Case Id : " + caseId);
					}
				}
				/********** Inserting Correct Row **********/
				if (dataValidated == true) {
					List<String> dataList = new ArrayList<>();
					dataList.add(caseId);
					dataList.add(GSTIN);
					dataList.add(taxPayerName);
					dataList.add(caseRortingDateStr);
//					dataList.add(suspectedIndicativeTaxValue);
					dataList.add(period);
					dataList.add(assignToJurisdiction);
					dataList.addAll(parameters);
					if (dataList.size() > 0) {
						uploadData.add(dataList);
					}
				}
			}
			if (uploadData.size() > 0) {
				outputData.put("uploadData", uploadData);
			}
			if (errorList.size() > 0) {
				errors.add(errorList);
				outputData.put("errorList", errors);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ExcelValidator : " + e.getMessage());
		} finally {
			workbook.close();
			inputStream.close();
		}
		return outputData;
	}
}
