package com.hp.gstreviewfeedbackapp.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.SelfDetectedCase;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.CircleRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;

@Component
public class ExcelValidator {
	private static final Logger logger = LoggerFactory.getLogger(ExcelValidator.class);
	@Autowired
//	private static CircleRepository circleRepository;
	private CircleRepository circleRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private HQUserUploadDataRepository hQUserUploadDataRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private EnforcementMasterRepository enforcementMasterRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	@Value("${testUserId}")
	private Integer testUserId;
	@Value("${testing}")
	private boolean testing;
	// @Value("${expectedColumns}")
	private static String[] expectedColumnNames = { "GSTIN", "Taxpayer Name", "Case Reporting Date",
			"Suspected Indicative Tax Value (₹)", "Period", "Assigned To Circle", "Parameter_1", "Parameter_2",
			"Parameter_3", "Parameter_4", "Parameter_5" };

	// Check the date String should be in (dd/MM/yyyy)
	public boolean checkDateFormat(String dataString) {
		boolean check = false;
		try {
			String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(dataString);
			check = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public List<String> validateExcel(MultipartFile excelFile) throws IOException {
		ArrayList<String> errorList = new ArrayList<>();
		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			int headerColumnCount = headerRow.getLastCellNum();
			if (headerColumnCount != expectedColumnNames.length) {
				errorList.add(ApplicationConstants.COLUMNS_SIZE_NOT_MATCHED);
				return errorList;
			}
			for (int i = 0; i < expectedColumnNames.length; i++) {
				Cell cell = headerRow.getCell(i);
				String actualColumnName = cell.getStringCellValue().trim();
				if (!expectedColumnNames[i].equals(actualColumnName)) {
					errorList.add(ApplicationConstants.EXCEL_HEADER_COLUMNS_NOT_MATCHED + " at " + actualColumnName);
					return errorList;
				}
			}
			// GSTIN number validation
			String headerColumnNameForCellOne = headerRow.getCell(0).toString().trim().toLowerCase();
			if (headerColumnNameForCellOne.equalsIgnoreCase(ApplicationConstants.GSTIN.trim().toLowerCase())) {
				if (sheet.getLastRowNum() <= 0) {
					errorList.add("The Excel has been blanked !");
				}
				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					String dateCellValue = row.getCell(0).toString().trim();
					if (dateCellValue.length() > 0) {
						if (dateCellValue.length() != 15) {
							errorList.add("The GSTIN no is not correct in the row " + rowIndex + " !");
						}
					} else {
						errorList.add("GSTIN is blank in the row " + rowIndex + " or else you enter a blank row !");
					}
				}
			}
			if (errorList.size() > 0) {
				return errorList;
			}
			// CASE REPORTING DATE validation
			String headerColumnNameForCellThree = headerRow.getCell(2).toString().trim().toLowerCase();
			if (headerColumnNameForCellThree
					.equalsIgnoreCase(ApplicationConstants.CASE_REPORTING_DATE.trim().toLowerCase())) {
				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					String dataString;
					String dateCellValue = row.getCell(2).toString().trim();
					if (dateCellValue.length() == 10 || dateCellValue.length() == 11) {
						Date date = (dateCellValue.length() == 10
								? new SimpleDateFormat("dd-MM-yyyy").parse(dateCellValue)
								: new SimpleDateFormat("dd-MMM-yyyy").parse(dateCellValue));
						dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
						if (!checkDateFormat(dataString)) {
							errorList.add(ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE + " " + rowIndex);
							return errorList;
						} else if (new Date().before(date)) {
							errorList.add(
									"We are not allowing the future date. Please check the date in row no " + rowIndex);
							return errorList;
						}
					} else {
						errorList.add(ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE + rowIndex
								+ " or else you enter a blank row");
						return errorList;
					}
				}
			} else {
				errorList.add(
						ApplicationConstants.EXCEL_HEADER_COLUMNS_NOT_MATCHED + "on " + headerColumnNameForCellThree);
			}
			// Period Validation
			String headerColumnNameForCellFive = headerRow.getCell(4).toString().trim().toLowerCase();
			if (headerColumnNameForCellFive.equalsIgnoreCase(ApplicationConstants.PERIOD.trim().toLowerCase())) {
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
//		        int smallTwoDigitYear = year % 100;
				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					String dateCellValue = row.getCell(4).toString().trim();
					if (dateCellValue.length() > 0) {
						String[] tokens = dateCellValue.split("-");
						if ((Integer.parseInt(tokens[0]) > year)
								|| (Integer.parseInt(tokens[1]) != ((Integer.parseInt(tokens[0]) % 100) + 1))
								|| tokens.length != 2) {
//							System.out.println(Integer.parseInt(tokens[0]));
//							System.out.println(Integer.parseInt(tokens[1]));
//							System.out.println((Integer.parseInt(tokens[0]) % 100) + 1);
							errorList.add("Please provide correct period at row " + rowIndex);
							return errorList;
						}
					}
				}
			}
			// Circle Name validation
			String headerColumnNameForCellSix = headerRow.getCell(5).toString().trim().toLowerCase();
			if (headerColumnNameForCellSix.equalsIgnoreCase(ApplicationConstants.CIRCLE.trim().toLowerCase())) {
				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					String dateCellValue = row.getCell(5).toString().trim();
					String result = circleRepository.getCircleIdByCircleName(dateCellValue);
					if (result == null) {
						errorList.add(ApplicationConstants.JURISDICTION_DOES_NOT_EXIST + "at row " + rowIndex);
					}
				}
				// return errorList;
			} else {
				errorList.add(
						ApplicationConstants.EXCEL_HEADER_COLUMNS_NOT_MATCHED + "on " + headerColumnNameForCellSix);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			inputStream.close();
		}
		return errorList;
	}

	public Map<String, List<List<String>>> validateExcelAndExtractData(MultipartFile excelFile, Integer userId)
			throws IOException {
		InputStream inputStream = null;
		Workbook workbook = null;
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();
		try {
			Map<String, CompositeKey> compositeKeyMap = new HashMap<>();
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			int headerColumnCount = headerRow.getLastCellNum();
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
				String GSTIN = "";
				String taxPayerName = "";
				Date caseRortingDate = null;
				String caseRortingDateStr = "";
				String suspectedIndicativeTaxValue = "";
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
				/********** Validate GSTIN number **********/
				GSTIN = dateCellValue;
				if (dateCellValue.length() > 0) {
					if (dateCellValue.length() != 15 || !dateCellValue.matches("[A-Z0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": The GSTIN no is not correct : " + dateCellValue);
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": GSTIN is blank.");
				}
				/********** Validate Taxpayer Name **********/
				dateCellValue = (row.getCell(1) != null ? row.getCell(1).toString().trim() : "");
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
				dateCellValue = (row.getCell(2) != null ? row.getCell(2).toString().trim() : "");
				if (dateCellValue.matches("[-0-9]+") && dateCellValue.length() == 10 || dateCellValue.length() == 11) {
					Date date = (dateCellValue.length() == 10 ? new SimpleDateFormat("dd-MM-yyyy").parse(dateCellValue)
							: new SimpleDateFormat("dd-MMM-yyyy").parse(dateCellValue));
					caseRortingDate = date;
					dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
					caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
					if (!checkDateFormat(dataString)) {
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
				dateCellValue = (row.getCell(3) != null ? row.getCell(3).toString().trim() : "");
				if (dateCellValue.length() > 0) {
					BigDecimal bigDecimalValue = new BigDecimal(dateCellValue);
					// Convert BigDecimal to a string with full precision
					dateCellValue = bigDecimalValue.toPlainString();
				}
				suspectedIndicativeTaxValue = Long.toString((long) Double.parseDouble(dateCellValue));
				if (dateCellValue.length() <= 0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": The Suspected Indicative Tax Value is blank.");
				} else if (dateCellValue.length() > 12
						|| !Long.toString((long) Double.parseDouble(dateCellValue)).matches("[0-9]+")) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": The Suspected Indicative Tax Value is not correct : "
							+ dateCellValue);
				}
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
					Optional<LocationDetails> objectLD = locationDetailsRepository.findByLocationName(dateCellValue);
					if (testing && userId.equals(testUserId) && objectLD.isPresent()) {
						if (!objectLD.get().getLocationName().contains("Test")) {
							dataValidated = false;
							errorList.add(
									"Test User can only upload the data for test Circle/Zone/Enforcement Zone/State");
						}
					} else if (!objectLD.isPresent() || objectLD.get().getLocationId().equalsIgnoreCase("Z04")
							|| objectLD.get().getLocationId().equalsIgnoreCase("C81")
							|| objectLD.get().getLocationId().equalsIgnoreCase("HPT")
							|| objectLD.get().getLocationId().equalsIgnoreCase("DT14")
							|| objectLD.get().getLocationId().equalsIgnoreCase("EZ04")) {
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
								.findByParamNameAndStatusAssessment(dateCellValue, true);
						if (!objectAP.isPresent()) {
							dataValidated = false;
							errorList.add(
									"Row " + rowIndex + ": Parameter_" + (i - 5) + " is not valid : " + dateCellValue);
						} else {
							if (parameters.contains(dateCellValue)) {
								dataValidated = false;
								errorList.add("Row " + rowIndex + ": Parameter_" + (i - 5) + " is repeated : "
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
					CompositeKey compositeKey = new CompositeKey();
					compositeKey.setGSTIN(GSTIN);
					compositeKey.setCaseReportingDate(caseRortingDate);
					compositeKey.setPeriod(period);
					// Checking if excel sheet have duplicate value itself
					if (compositeKeyMap.get(GSTIN) != null
							&& compositeKeyMap.get(GSTIN).getCaseReportingDate().equals(caseRortingDate)
							&& compositeKeyMap.get(GSTIN).getPeriod().equals(period)) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Excel have a repeated entity with - " + compositeKey);
					} else {
						compositeKeyMap.put(GSTIN, compositeKey);
					}
					// Checking if excel sheet have duplicate value in DB already
					if (hQUserUploadDataRepository.findById(compositeKey).isPresent()) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Duplicate value found with - " + compositeKey);
					}
				}
				/********** Inserting Correct Row **********/
				if (dataValidated == true) {
					List<String> dataList = new ArrayList<>();
//					for(int i=0; i<expectedColumnNames.length ; i++) {
//						dateCellValue = (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
//						dataList.add(dateCellValue);
//					}
					dataList.add(GSTIN);
					dataList.add(taxPayerName);
					dataList.add(caseRortingDateStr);
					dataList.add(suspectedIndicativeTaxValue);
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
			logger.error("ExcelValidator : " + e.getMessage());
		} finally {
			workbook.close();
			inputStream.close();
		}
		return outputData;
	}

	public Map<String, List<List<String>>> validateExcelAndExtractDataForEnformentCases(MultipartFile excelFile,
			Integer userId) throws IOException {
		InputStream inputStream = null;
		Workbook workbook = null;
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();
		try {
			Map<String, CompositeKey> compositeKeyMap = new HashMap<>();
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			int headerColumnCount = headerRow.getLastCellNum();
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
				errorList.add("The Excel has no enforcement case data!");
				errors.add(errorList);
				outputData.put("errorList", errors);
				return outputData;
			}
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				String GSTIN = "";
				String taxPayerName = "";
				Date caseRortingDate = null;
				String caseRortingDateStr = "";
				String suspectedIndicativeTaxValue = "";
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
				/********** Validate GSTIN number **********/
				GSTIN = dateCellValue;
				if (dateCellValue.length() > 0) {
					if (dateCellValue.length() != 15 || !dateCellValue.matches("[A-Z0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": The GSTIN no is not correct : " + dateCellValue);
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": GSTIN is blank.");
				}
				/********** Validate Taxpayer Name **********/
				dateCellValue = (row.getCell(1) != null ? row.getCell(1).toString().trim() : "");
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
				dateCellValue = (row.getCell(2) != null ? row.getCell(2).toString().trim() : "");
				if (dateCellValue.matches("[-0-9]+") && dateCellValue.length() == 10 || dateCellValue.length() == 11) {
					Date date = (dateCellValue.length() == 10 ? new SimpleDateFormat("dd-MM-yyyy").parse(dateCellValue)
							: new SimpleDateFormat("dd-MMM-yyyy").parse(dateCellValue));
					caseRortingDate = date;
					dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
					caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
					if (!checkDateFormat(dataString)) {
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
				dateCellValue = (row.getCell(3) != null ? row.getCell(3).toString().trim() : "");
				if (dateCellValue.length() > 0) {
					BigDecimal bigDecimalValue = new BigDecimal(dateCellValue);
					// Convert BigDecimal to a string with full precision
					dateCellValue = bigDecimalValue.toPlainString();
				}
				suspectedIndicativeTaxValue = Long.toString((long) Double.parseDouble(dateCellValue));
				if (dateCellValue.length() <= 0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": The Suspected Indicative Tax Value is blank.");
				} else if (dateCellValue.length() > 12
						|| !Long.toString((long) Double.parseDouble(dateCellValue)).matches("[0-9]+")) {
					dataValidated = false;
					errorList.add("Row " + rowIndex
							+ ": The Suspected Indicative Tax Value is not correct/has more than 12 digit : "
							+ dateCellValue);
				}
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
					Optional<LocationDetails> objectLD = locationDetailsRepository.findByLocationName(dateCellValue);
					if (testing && userId.equals(testUserId) && objectLD.isPresent()) {
						if (!objectLD.get().getLocationName().contains("Test")) {
							dataValidated = false;
							errorList.add(
									"Test User can only upload the data for test Circle/Zone/Enforcement Zone/State");
						}
					} else if (!objectLD.isPresent() || objectLD.get().getLocationId().equalsIgnoreCase("Z04")
							|| objectLD.get().getLocationId().equalsIgnoreCase("C81")
							|| objectLD.get().getLocationId().equalsIgnoreCase("HPT")
							|| objectLD.get().getLocationId().equalsIgnoreCase("DT14")
							|| objectLD.get().getLocationId().equalsIgnoreCase("EZ04")) {
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
								.findByParamNameAndStatusEnforcement(dateCellValue, true);
						if (!objectAP.isPresent()) {
							dataValidated = false;
							errorList.add(
									"Row " + rowIndex + ": Parameter_" + (i - 5) + " is not valid : " + dateCellValue);
						} else {
							if (parameters.contains(dateCellValue)) {
								dataValidated = false;
								errorList.add("Row " + rowIndex + ": Parameter_" + (i - 5) + " is repeated : "
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
					CompositeKey compositeKey = new CompositeKey();
					compositeKey.setGSTIN(GSTIN);
					compositeKey.setCaseReportingDate(caseRortingDate);
					compositeKey.setPeriod(period);
					// Checking if excel sheet have duplicate value itself
					if (compositeKeyMap.get(GSTIN) != null
							&& compositeKeyMap.get(GSTIN).getCaseReportingDate().equals(caseRortingDate)
							&& compositeKeyMap.get(GSTIN).getPeriod().equals(period)) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Excel have a repeated entity with - " + compositeKey);
					} else {
						compositeKeyMap.put(GSTIN, compositeKey);
					}
					// Checking if excel sheet have duplicate value in DB already
					if (enforcementMasterRepository.findById(compositeKey).isPresent()) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Duplicate value found with - " + compositeKey);
					}
				}
				/********** Inserting Correct Row **********/
				if (dataValidated == true) {
					List<String> dataList = new ArrayList<>();
//					for(int i=0; i<expectedColumnNames.length ; i++) {
//						dateCellValue = (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
//						dataList.add(dateCellValue);
//					}
					dataList.add(GSTIN);
					dataList.add(taxPayerName);
					dataList.add(caseRortingDateStr);
					dataList.add(suspectedIndicativeTaxValue);
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
			logger.error("ExcelValidator : " + e.getMessage());
		} finally {
			workbook.close();
			inputStream.close();
		}
		return outputData;
	}

	public static void main(String[] str) {
		new ExcelValidator().checkDateFormat("01/01/2023");
		new ExcelValidator().checkDateFormat("31/11/2023");
	}

	public Map<String, List<List<String>>> validateExcelAndExtractData(SelfDetectedCase selfDetectedCase) {
		InputStream inputStream = null;
		Workbook workbook = null;
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();
		try {
			Map<String, CompositeKey> compositeKeyMap = new HashMap<>();
			String GSTIN = "";
			String taxPayerName = "";
			Date caseRortingDate = null;
			String caseRortingDateStr = "";
			String suspectedIndicativeTaxValue = "";
			String period = "";
			String assignToJurisdiction = "";
			String remark = "";
			String otherRemark = "";
			String caseId = "";
			boolean dataValidated = true;
			/********** Validate GSTIN number **********/
			GSTIN = selfDetectedCase.getGstIn();
			String gstIn = selfDetectedCase.getGstIn();
			if (gstIn.length() > 0) {
				if (gstIn.length() != 15 || !gstIn.matches("[A-Z0-9]+")) {
					dataValidated = false;
					errorList.add("The GSTIN no is not correct : " + gstIn);
				}
			} else {
				dataValidated = false;
				errorList.add("GSTIN is blank.");
			}
			/********** Validate Taxpayer Name **********/
			taxPayerName = selfDetectedCase.getTaxpayerName();
			String taxpayerName1 = selfDetectedCase.getTaxpayerName();
			if (taxpayerName1.length() <= 0) {
				dataValidated = false;
				errorList.add("Taxpayer Name is blank.");
			} else if (taxpayerName1.length() > 200) {
				dataValidated = false;
				errorList.add("Taxpayer Name is more than 200 letter : " + taxpayerName1);
			}
			/********** CASE REPORTING DATE validation **********/
			String dataString;
			Date date = selfDetectedCase.getCaseReportingDate();
			caseRortingDate = selfDetectedCase.getCaseReportingDate();
			dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
			caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
			if (new Date().before(date)) {
				dataValidated = false;
				errorList.add("Future date is not allowed. Please check the date.");
			}
			/********** Suspected Indicative Tax Value validation **********/
			suspectedIndicativeTaxValue = selfDetectedCase.getSuspectedIndicativeTaxValue();
			String suspectedIndicativeTaxValue1 = selfDetectedCase.getSuspectedIndicativeTaxValue();
			if (suspectedIndicativeTaxValue1.length() <= 0) {
				dataValidated = false;
				errorList.add("The Suspected Indicative Tax Value is blank.");
			} else if (suspectedIndicativeTaxValue1.length() > 12 || !suspectedIndicativeTaxValue1.matches("[0-9]+")) {
				dataValidated = false;
				errorList.add("The Suspected Indicative Tax Value is not correct : " + suspectedIndicativeTaxValue1);
			}
			/********** CASE ID validation **********/
			caseId = selfDetectedCase.getCaseId();
			String caseId1 = selfDetectedCase.getCaseId();
			if (caseId1.length() > 0) {
				Integer matchesCaseId = enforcementReviewCaseRepository.findTotalCountOfCaseIdMatches(caseId1.trim());
				if (caseId1.length() != 15 || !caseId1.matches("[A-Z0-9]+")) {
					dataValidated = false;
					errorList.add("The Case Id is not correct : " + caseId1);
				} else if (matchesCaseId > 0) {
					dataValidated = false;
					errorList.add("The Case Id is already exists : " + caseId1);
				}
			} else {
				dataValidated = false;
				errorList.add("Case Id is blank.");
			}
			/********** Period Validation **********/
			Date date1 = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int year = calendar.get(Calendar.YEAR);
			period = selfDetectedCase.getPeriod();
			String period1 = selfDetectedCase.getPeriod();
			if (period1.length() > 0) {
				String[] tokens = period1.split("-");
				if (!period1.matches("[-0-9]+")) {
					dataValidated = false;
					errorList.add("Period is not correct : " + period1);
				} else if ((Integer.parseInt(tokens[0]) > year)
						|| (Integer.parseInt(tokens[1]) != ((Integer.parseInt(tokens[0]) % 100) + 1))
						|| tokens.length != 2) {
					dataValidated = false;
					errorList.add("Period is not correct : " + period1);
					// return errorList;
				}
			} else {
				dataValidated = false;
				errorList.add("Period is blank.");
			}
			/********** Jurisdiction Name validation **********/
			assignToJurisdiction = selfDetectedCase.getAssignedToCircle();
			String assignToJurisdiction1 = selfDetectedCase.getAssignedToCircle();
			if (assignToJurisdiction1.length() > 0) {
				Optional<LocationDetails> objectLD = locationDetailsRepository
						.findByLocationName(assignToJurisdiction1);
				if (!objectLD.isPresent()) {
					dataValidated = false;
					errorList.add(ApplicationConstants.JURISDICTION_DOES_NOT_EXIST + " : " + assignToJurisdiction1);
				}
			} else {
				dataValidated = false;
				errorList.add("Jurisdiction Name is blank.");
			}
			/********** Remark validation **********/
			remark = selfDetectedCase.getRemark();
			otherRemark = (selfDetectedCase.getOtherRemarks() != null
					&& !selfDetectedCase.getOtherRemarks().trim().isEmpty()) ? selfDetectedCase.getOtherRemarks() : "";
			if (remark.length() == 0) {
				dataValidated = false;
				errorList.add("Please provide reason.");
			}
			/********** Check duplicate value **********/
			if (dataValidated == true) {
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(GSTIN);
				compositeKey.setCaseReportingDate(caseRortingDate);
				compositeKey.setPeriod(period);
				// Checking if excel sheet have duplicate value itself
				if (compositeKeyMap.get(GSTIN) != null
						&& compositeKeyMap.get(GSTIN).getCaseReportingDate().equals(caseRortingDate)
						&& compositeKeyMap.get(GSTIN).getPeriod().equals(period)) {
					dataValidated = false;
					errorList.add("Excel have a repeated entity with - " + compositeKey);
				} else {
					compositeKeyMap.put(GSTIN, compositeKey);
				}
				// Checking if excel sheet have duplicate value in DB already
				if (hQUserUploadDataRepository.findById(compositeKey).isPresent()) {
					dataValidated = false;
					errorList.add("Duplicate value found with - " + compositeKey);
				}
			}
			/********** Inserting Correct Row **********/
			if (dataValidated == true) {
				List<String> dataList = new ArrayList<>();
//					for(int i=0; i<expectedColumnNames.length ; i++) {
//						dateCellValue = (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
//						dataList.add(dateCellValue);
//					}
				dataList.add(GSTIN);
				dataList.add(taxPayerName);
				dataList.add(caseRortingDateStr);
				dataList.add(suspectedIndicativeTaxValue);
				dataList.add(period);
				dataList.add(assignToJurisdiction);
				dataList.add(remark);
				dataList.add(otherRemark);
				dataList.add(caseId);
				if (dataList.size() > 0) {
					uploadData.add(dataList);
				}
			}
//			}
			if (uploadData.size() > 0) {
				outputData.put("uploadData", uploadData);
			}
			if (errorList.size() > 0) {
				errors.add(errorList);
				outputData.put("errorList", errors);
			}
		} catch (Exception e) {
			logger.error("ExcelValidator : " + e.getMessage());
		}
		return outputData;
	}

	public Map<String, List<List<String>>> validateScrutinySelfDetectedCasesInputs(SelfDetectedCase selfDetectedCase) {
		InputStream inputStream = null;
		Workbook workbook = null;
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();
		try {
			Map<String, CompositeKey> compositeKeyMap = new HashMap<>();
			String GSTIN = "";
			String taxPayerName = "";
			Date caseRortingDate = null;
			String caseRortingDateStr = "";
			String suspectedIndicativeTaxValue = "";
			String period = "";
			String assignToJurisdiction = "";
			String remark = "";
			String otherRemark = "";
			String caseId = "";
			String modifiedString = "";
			boolean dataValidated = true;
			/********** Validate GSTIN number **********/
			GSTIN = selfDetectedCase.getGstIn();
			String gstIn = selfDetectedCase.getGstIn();
			if (gstIn.length() > 0) {
				if (gstIn.length() != 15 || !gstIn.matches("[A-Z0-9]+")) {
					dataValidated = false;
					errorList.add("The GSTIN no is not correct : " + gstIn);
				}
			} else {
				dataValidated = false;
				errorList.add("GSTIN is blank.");
			}
			/********** Validate Taxpayer Name **********/
			taxPayerName = selfDetectedCase.getTaxpayerName();
			String taxpayerName1 = selfDetectedCase.getTaxpayerName();
			if (taxpayerName1.length() <= 0) {
				dataValidated = false;
				errorList.add("Taxpayer Name is blank.");
			} else if (taxpayerName1.length() > 200) {
				dataValidated = false;
				errorList.add("Taxpayer Name is more than 200 letter : " + taxpayerName1);
			}
			/********** CASE REPORTING DATE validation **********/
			String dataString;
			Date date = selfDetectedCase.getCaseReportingDate();
			caseRortingDate = selfDetectedCase.getCaseReportingDate();
			dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
			caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
			if (new Date().before(date)) {
				dataValidated = false;
				errorList.add("Future date is not allowed. Please check the date.");
			}
			/********** Suspected Indicative Tax Value validation **********/
			suspectedIndicativeTaxValue = selfDetectedCase.getSuspectedIndicativeTaxValue();
			String suspectedIndicativeTaxValue1 = selfDetectedCase.getSuspectedIndicativeTaxValue();
			if (suspectedIndicativeTaxValue1.length() <= 0) {
				dataValidated = false;
				errorList.add("The Suspected Indicative Tax Value is blank.");
			} else if (suspectedIndicativeTaxValue1.length() > 12 || !suspectedIndicativeTaxValue1.matches("[0-9]+")) {
				dataValidated = false;
				errorList.add("The Suspected Indicative Tax Value is not correct : " + suspectedIndicativeTaxValue1);
			}
			/********** CASE ID validation **********/
			caseId = selfDetectedCase.getCaseId();
			String caseId1 = selfDetectedCase.getCaseId();
			if (caseId1.length() > 0) {
				Integer matchesCaseId = mstScrutinyCasesRepository.findTotalCountOfCaseIdMatches(caseId1.trim());
				if (caseId1.length() != 15 || !caseId1.matches("[A-Z0-9]+")) {
					dataValidated = false;
					errorList.add("The Case Id is not correct : " + caseId1);
				} else if (matchesCaseId > 0) {
					dataValidated = false;
					errorList.add("The Case Id is already exists : " + caseId1);
				}
			} else {
				dataValidated = false;
				errorList.add("Case Id is blank.");
			}
			/********** Period Validation **********/
			Date date1 = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			int year = calendar.get(Calendar.YEAR);
			period = selfDetectedCase.getPeriod();
			String period1 = selfDetectedCase.getPeriod();
			if (period1.length() > 0) {
				String[] tokens = period1.split("-");
				if (!period1.matches("[-0-9]+")) {
					dataValidated = false;
					errorList.add("Period is not correct : " + period1);
				} else if ((Integer.parseInt(tokens[0]) > year)
						|| (Integer.parseInt(tokens[1]) != ((Integer.parseInt(tokens[0]) % 100) + 1))
						|| tokens.length != 2) {
					dataValidated = false;
					errorList.add("Period is not correct : " + period1);
					// return errorList;
				}
			} else {
				dataValidated = false;
				errorList.add("Period is blank.");
			}
			/********** Jurisdiction Name validation **********/
			assignToJurisdiction = selfDetectedCase.getAssignedToCircle();
			String assignToJurisdiction1 = selfDetectedCase.getAssignedToCircle();
			if (assignToJurisdiction1.length() > 0) {
				Optional<LocationDetails> objectLD = locationDetailsRepository
						.findByLocationName(assignToJurisdiction1);
				if (!objectLD.isPresent()) {
					dataValidated = false;
					errorList.add(ApplicationConstants.JURISDICTION_DOES_NOT_EXIST + " : " + assignToJurisdiction1);
				}
			} else {
				dataValidated = false;
				errorList.add("Jurisdiction Name is blank.");
			}
			/********** Remark validation **********/
			remark = selfDetectedCase.getRemark();
			otherRemark = (selfDetectedCase.getOtherRemarks() != null
					&& !selfDetectedCase.getOtherRemarks().trim().isEmpty()) ? selfDetectedCase.getOtherRemarks() : "";
			if (!otherRemark.isEmpty())
				modifiedString = remark.trim().replace("Others", "Others");
			if (remark.length() == 0) {
				dataValidated = false;
				errorList.add("Please provide reason.");
			}
			/********** Check duplicate value **********/
			if (dataValidated == true) {
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(GSTIN);
				compositeKey.setCaseReportingDate(caseRortingDate);
				compositeKey.setPeriod(period);
				// Checking if excel sheet have duplicate value itself
				if (compositeKeyMap.get(GSTIN) != null
						&& compositeKeyMap.get(GSTIN).getCaseReportingDate().equals(caseRortingDate)
						&& compositeKeyMap.get(GSTIN).getPeriod().equals(period)) {
					dataValidated = false;
					errorList.add("Excel have a repeated entity with - " + compositeKey);
				} else {
					compositeKeyMap.put(GSTIN, compositeKey);
				}
				// Checking if excel sheet have duplicate value in DB already
				if (mstScrutinyCasesRepository.findById(compositeKey).isPresent()) {
					dataValidated = false;
					errorList.add("Duplicate value found with - " + compositeKey);
				}
			}
			/********** Inserting Correct Row **********/
			if (dataValidated == true) {
				List<String> dataList = new ArrayList<>();
//					for(int i=0; i<expectedColumnNames.length ; i++) {
//						dateCellValue = (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
//						dataList.add(dateCellValue);
//					}
				dataList.add(GSTIN);
				dataList.add(taxPayerName);
				dataList.add(caseRortingDateStr);
				dataList.add(suspectedIndicativeTaxValue);
				dataList.add(period);
				dataList.add(assignToJurisdiction);
				dataList.add(modifiedString);
//				if(!otherRemark.isEmpty())
//				dataList.add(otherRemark);
				dataList.add(caseId);
				if (dataList.size() > 0) {
					uploadData.add(dataList);
				}
			}
//			}
			if (uploadData.size() > 0) {
				outputData.put("uploadData", uploadData);
			}
			if (errorList.size() > 0) {
				errors.add(errorList);
				outputData.put("errorList", errors);
			}
		} catch (Exception e) {
			logger.error("ExcelValidator : " + e.getMessage());
		}
		return outputData;
	}
}
