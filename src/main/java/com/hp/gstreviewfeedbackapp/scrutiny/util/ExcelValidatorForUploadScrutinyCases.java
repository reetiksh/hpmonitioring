package com.hp.gstreviewfeedbackapp.scrutiny.util;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.CircleDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseUploadParameters;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseUploadParametersRepository;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Component
public class ExcelValidatorForUploadScrutinyCases {
	private static final Logger logger = LoggerFactory.getLogger(ExcelValidatorForUploadScrutinyCases.class);
	
	private static String[] expectedColumnNames = { "GSTIN", "Taxpayer Name", "Case Reporting Date",
			"Suspected Indicative Tax Value (₹)", "Period", "Assigned To Circle", "Parameter_1", "Parameter_2", "Parameter_3", "Parameter_4", "Parameter_5"};
	
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private HQUserUploadDataRepository hQUserUploadDataRepository;
	@Autowired
	private ScrutinyCaseUploadParametersRepository scrutinyCaseUploadParametersRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private CircleDetailsRepository circleDetailsRepository;
	
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	
	

	public Map<String, List<List<String>>> validateExcelAndExtractDataForUploadScrutinyCases(MultipartFile excelFile) throws IOException {
		InputStream inputStream = null;
		Workbook workbook = null;
		
		List<String> errorList = new ArrayList<>();
		List<List<String>> errors = new ArrayList<>();
		List<List<String>> uploadData = new ArrayList<>();
		Map<String, List<List<String>>> outputData = new HashMap<>();

		try {
			Map<String, CompositeKey> compositeKeyMap = new HashMap<>();
			List<String> caseIdList = new ArrayList<>();
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			
			int headerColumnCount = headerRow.getLastCellNum();
			
//			if(headerColumnCount != expectedColumnNames.length) {
//				errorList.add(ApplicationConstants.COLUMNS_SIZE_NOT_MATCHED);
//				return errorList;
//			}
			if(sheet.getLastRowNum()<=0) {
				errorList.add("The Excel is blank!");
				errors.add(errorList);
				outputData.put("errorList", errors);
				return outputData;
			}
			for (int i = 0; i<expectedColumnNames.length; i++) {
				Cell cell = headerRow.getCell(i);
				String actualColumnName = cell.getStringCellValue().trim();

				if (!expectedColumnNames[i].equals(actualColumnName)) {
					errorList.add("Column " + actualColumnName + ": " + ApplicationConstants.EXCEL_HEADER_COLUMNS_NOT_MATCHED);
					
				}
				if(errorList.size()>0) {
					errorList.add("Please check the sample format for the reference");
					
					errors.add(errorList);
					outputData.put("errorList", errors);
					return outputData;
				}
			}
			if(sheet.getLastRowNum()<1) {
				errorList.add("The Excel has no enforcement review case data!");
				errors.add(errorList);
				outputData.put("errorList", errors);
				return outputData;
			}
			Integer actualLastRowNum=-1;
			for(int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if(row != null && isRowNotEmpty(row)) {
					actualLastRowNum=i;
				}
			}
			
			
			/*for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {*/
			for(int rowIndex = 1; rowIndex <= actualLastRowNum; rowIndex++) {
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
				if(row==null) {
					errorList.add("Row " + rowIndex + ": The row is blank. Please delete the row from excel.");
					continue;
				}
				String dateCellValue = (row.getCell(0)!= null ? row.getCell(0).toString().trim() : "");
				
				/********* Check if the total row is blank *********/
				if(dateCellValue.length()==0) {
					boolean flag = false;
					for(int i=0; i<expectedColumnNames.length ; i++) {
						String cellValue = (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
						if(cellValue.length()!=0) {
							flag = true;
							break;
						}
					}
					if(flag==false) {
						errorList.add("Row " + rowIndex + ": The row is blank. Please delete the row from excel.");
						continue;
					}
					dataValidated = false;
				}
				
				/********** Validate Case id number **********/
				/*
				 * caseId = dateCellValue; if(dateCellValue.length()>0) {
				 * System.out.println(dateCellValue.length()); if(dateCellValue.length()!=15 ||
				 * !dateCellValue.matches("[A-Z0-9]+")) { dataValidated = false;
				 * errorList.add("Row " + rowIndex + ": The Case Id no is not correct : " +
				 * dateCellValue); } } else { dataValidated = false; }
				 */
				
				/********** Validate GSTIN number **********/
				dateCellValue = (row.getCell(0)!= null ? row.getCell(0).toString().trim() : "");
				GSTIN = dateCellValue;
				if(dateCellValue.length()>0) {
					System.out.println(dateCellValue.length());
					if(dateCellValue.length()!=15 || !dateCellValue.matches("[A-Z0-9]+")) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": The GSTIN no is not correct : " + dateCellValue);
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": GSTIN is blank.");
				}
				
				/********** Validate Taxpayer Name **********/
				dateCellValue = (row.getCell(1)!= null ? row.getCell(1).toString().trim() : "");
				taxPayerName = dateCellValue;
				if(dateCellValue.length()<=0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Taxpayer Name is blank.");
				} else if (dateCellValue.length() > 200) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Taxpayer Name is more than 200 letter : " + dateCellValue);
				}
				
				/********** CASE REPORTING DATE validation **********/
				row = sheet.getRow(rowIndex);
				String dataString;
				dateCellValue = (row.getCell(2)!= null ? row.getCell(2).toString().trim() : "");
				if(dateCellValue.matches("[-0-9]+") && dateCellValue.length()==10 || dateCellValue.length()==11) {
					Date date = (dateCellValue.length()==10 ? new SimpleDateFormat("dd-MM-yyyy").parse(dateCellValue) : new SimpleDateFormat("dd-MMM-yyyy").parse(dateCellValue));
					caseRortingDate = date;
					dataString = new SimpleDateFormat("dd/MM/yyyy").format(date);
					caseRortingDateStr = new SimpleDateFormat("dd-MM-yyyy").format(date);
					
					if (!excelValidator.checkDateFormat(dataString)) { 
						dataValidated = false;
			        	errorList.add("Row " + rowIndex + ": " + ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE);
//			        	return errorList;
			        } else if(new Date().before(date)) {
			        	dataValidated = false;
			        	errorList.add("Row " + rowIndex + ": Future date is not allowed. Please check the date.");
//			        	return errorList;
			        }
				} else {
					dataValidated = false;
					if(dateCellValue.length()>0) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": " + ApplicationConstants.CASE_REPORTING_DATE_ERROR_MESSAGE  + " : " + dateCellValue);
					} else {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": The Case Reporting Date is blank.");
					}
//					return errorList;
				}
				
				/********** Suspected Indicative Tax Value validation **********/
				
				dateCellValue = (row.getCell(3)!= null ? row.getCell(3).toString().trim() : "");
				if(dateCellValue.length()>0) {
					BigDecimal bigDecimalValue = new BigDecimal(dateCellValue);
			        // Convert BigDecimal to a string with full precision
			        dateCellValue = bigDecimalValue.toPlainString();
				}
				suspectedIndicativeTaxValue = dateCellValue;
				if(dateCellValue.length()<=0) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": The Suspected Indicative Tax Value is blank.");
				} else if (dateCellValue.length() > 11 || !Long.toString((long) Double.parseDouble(dateCellValue)).matches("[0-9]+")) {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": The suspected indicative tax value cannot be more than 11 digits : " + dateCellValue);
				}
				
				/********** Period Validation **********/
				Date date = new Date();
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        int year = calendar.get(Calendar.YEAR);
		        dateCellValue = (row.getCell(4)!= null ? row.getCell(4).toString().trim() : "");
				period = dateCellValue;
				if(dateCellValue.length()>0) {
					String[] tokens = dateCellValue.split("-");
					if (!dateCellValue.matches("[-0-9]+")){
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Period is not correct : " + dateCellValue);
					} else if((Integer.parseInt(tokens[0])>year) || (Integer.parseInt(tokens[1])!=((Integer.parseInt(tokens[0])% 100)+1)) || tokens.length!=2) {
//						System.out.println(Integer.parseInt(tokens[0]));
//						System.out.println(Integer.parseInt(tokens[1]));
//						System.out.println((Integer.parseInt(tokens[0])% 100)+1);
//						System.out.println(tokens.length);
						
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Period is not correct : " + dateCellValue);
	//					return errorList;
					}
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Period is blank.");
				}
				
				/********** Jurisdiction Name validation **********/
				dateCellValue =  (row.getCell(5)!= null ? row.getCell(5).toString().trim() : "");
				assignToJurisdiction = dateCellValue;
				if(dateCellValue.length()>0) {
//				    Optional<LocationDetails> objectLD = locationDetailsRepository.findByLocationName(dateCellValue);
				    Optional<CircleDetails> objectCD = circleDetailsRepository.findByCircleName(dateCellValue);
				    if(!objectCD.isPresent() || dateCellValue.equalsIgnoreCase("NA")) {
				    	dataValidated = false;
				    	errorList.add("Row " + rowIndex + ": " + ApplicationConstants.JURISDICTION_DOES_NOT_EXIST + " : " + dateCellValue);
				    }
				} else {
					dataValidated = false;
					errorList.add("Row " + rowIndex + ": Jurisdiction Name is blank.");
				}
				
				/********** Parameter(s) validation **********/
				for(int i = 6 ; i<=expectedColumnNames.length ; i++) {
					dateCellValue =  (row.getCell(i)!= null ? row.getCell(i).toString().trim() : "");
					if(dateCellValue.length()>0) {
					    
//						Optional<ScrutinyCaseUploadParameters> objectAP = scrutinyCaseUploadParametersRepository.findByName(dateCellValue);
						
						Optional<MstParametersModuleWise> objectAP = mstParametersModuleWiseRepository.findByParamNameAndStatusScrutiny(dateCellValue,true);
					    
					    
					    if(!objectAP.isPresent()) {
					    	dataValidated = false;
					    	errorList.add("Row " + rowIndex + ": Parameter_" + (i-5) + " is not valid : " + dateCellValue);
					    } else {
					    	if(parameters.contains(dateCellValue)) {
					    		dataValidated = false;
						    	errorList.add("Row " + rowIndex + ": Parameter_" + (i-5) + " is repeated : " + dateCellValue);
					    	} else {
					    		parameters.add(dateCellValue);
					    	}
					    }
					}
				}
				if(parameters.size()==0) {
					dataValidated = false;
			    	errorList.add("Row " + rowIndex + ": Parameter is not available");
				} else {
					for(int i=parameters.size(); i<5 ; i++) {
						parameters.add("");
					}
				}

				/********** Check duplicate value **********/
				if(dataValidated==true) {
					CompositeKey compositeKey = new CompositeKey();
					compositeKey.setGSTIN(GSTIN);
					compositeKey.setCaseReportingDate(caseRortingDate);
					compositeKey.setPeriod(period);
					
					//Checking if excel sheet have duplicate value itself
					if(compositeKeyMap.get(GSTIN)!=null && compositeKeyMap.get(GSTIN).getCaseReportingDate().equals(caseRortingDate) && compositeKeyMap.get(GSTIN).getPeriod().equals(period)) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Excel have a repeated entity with - " + compositeKey);
					} else {
						compositeKeyMap.put(GSTIN, compositeKey);
					}
					//Checking if excel sheet have duplicate value in DB already
					if(mstScrutinyCasesRepository.findById(compositeKey).isPresent()) {
						dataValidated = false;
						errorList.add("Row " + rowIndex + ": Duplicate value found with - " + compositeKey);
					}
				}
				
				/********** Inserting Correct Row **********/
				if(dataValidated==true) {
					List<String> dataList = new ArrayList<>();
					
					//dataList.add(caseId);
					dataList.add(GSTIN);
					dataList.add(taxPayerName);
					dataList.add(caseRortingDateStr);
					dataList.add(Long.toString((long) Double.parseDouble(suspectedIndicativeTaxValue)));
					dataList.add(period);
					dataList.add(assignToJurisdiction);
					dataList.addAll(parameters);
					
					if(dataList.size()>0) {
						uploadData.add(dataList);
					}
				}
				
			}
			
			if(uploadData.size()>0) {
				outputData.put("uploadData", uploadData);
			}
			if(errorList.size()>0) {
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

	private boolean isRowNotEmpty(Row row) {
		for(Cell cell : row) {
			if(cell.getCellType() != CellType.BLANK) {
				return true;
			}
		}
		return false;
	}
}
