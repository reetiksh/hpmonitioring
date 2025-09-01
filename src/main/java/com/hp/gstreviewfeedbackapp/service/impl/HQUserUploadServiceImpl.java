package com.hp.gstreviewfeedbackapp.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.controller.AdminUserController;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.HeadquarterLogs;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CircleRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.ExtensionNoDocumentsRepository;
import com.hp.gstreviewfeedbackapp.repository.HQTransferRequest;
import com.hp.gstreviewfeedbackapp.repository.HQUserUploadDataRepository;
import com.hp.gstreviewfeedbackapp.repository.HeadquarterLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.service.FieldUserService;
import com.hp.gstreviewfeedbackapp.service.HQUserUploadService;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.ExcelCellDataExtractor;

@Service
public class HQUserUploadServiceImpl implements HQUserUploadService {
	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
	@Value("${upload.directory}")
	private String uploadDirectory;
	@Autowired
	private HQUserUploadDataRepository hQUserUploadDataRepository;
	@Autowired
	private ExtensionNoDocumentsRepository extensionNoDocumentsRepository;
	@Autowired
	private CircleRepository circleRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	@Autowired
	private TransferRemarksRepository transferRemarksRepository;
	@Autowired
	private HeadquarterLogsRepository headquarterLogsRepository;
	@Autowired
	private HQTransferRequest hqTransferRequest;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private FieldUserService fieldUserService;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;

	@Override
	public String saveHqUserUploadData(String extensionNo, String category, MultipartFile pdfFile,
			MultipartFile excelFile, int userId) throws IOException, ParseException {
		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			List<CaseWorkflow> caseWorkflowList = new ArrayList<>();
			// Save PDF
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
//			System.out.println(extensionNo.replace('\\', '.').replace('/', '.'));
			String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_" + formattedTimestamp
					+ ".pdf";
			pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
			ExtensionNoDocument extensionNoDocument = new ExtensionNoDocument();
			extensionNoDocument.setExtensionFileName(pdfFileName);
			extensionNoDocument.setExtensionNo(extensionNo);
			// save Excel to DB
			inputStream = excelFile.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int totalRow = sheet.getLastRowNum();
			System.out.println(totalRow);
			for (int rowIndex = 1; rowIndex <= totalRow; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
				CompositeKey compositeKey = new CompositeKey();
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				enforcementReviewCase.setCategory(category);
				enforcementReviewCase.setExtensionNo(extensionNo);
//      		enforcementReviewCase.setGSTIN(ExcelCellDataExtractor.getCellValue(row.getCell(0)));
				enforcementReviewCase.setTaxpayerName(ExcelCellDataExtractor.getCellValue(row.getCell(1)));
//				enforcementReviewCase.setCircle(ExcelCellDataExtractor.getCellValue(row.getCell(2)));
				String dateCellValue = row.getCell(2).toString().trim();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(dateCellValue);
//				System.out.println("parsedDate "+parsedDate);
//				System.out.println("dateCellValue "+dateCellValue);
//				enforcementReviewCase.setCaseReportingDate(parsedDate);
				Double indicativeTaxValue = Double.parseDouble(ExcelCellDataExtractor.getCellValue(row.getCell(3)));
				enforcementReviewCase.setIndicativeTaxValue(indicativeTaxValue.longValue());
//				enforcementReviewCase.setPeriod(ExcelCellDataExtractor.getCellValue(row.getCell(5)));
//				enforcementReviewCase.setAssignedTo(circleRepository.getCircleIdByCircleName(ExcelCellDataExtractor.getCellValue(row.getCell(5))));
				enforcementReviewCase.setLocationDetails(locationDetailsRepository
						.findByLocationName(ExcelCellDataExtractor.getCellValue(row.getCell(5))).get());
				enforcementReviewCase.setExtensionNoDocument(extensionNoDocument);
				enforcementReviewCase.setAssignedTo("FO");
				enforcementReviewCase.setAssignedFrom("HQ");
				enforcementReviewCase.setAssignedFromUserId(userId);
				enforcementReviewCase.setAssignedToUserId(0);
				enforcementReviewCase.setAction("upload");
				compositeKey.setGSTIN(ExcelCellDataExtractor.getCellValue(row.getCell(0)));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(ExcelCellDataExtractor.getCellValue(row.getCell(4)));
				Optional<EnforcementReviewCase> enforcementReviewCaseExist = hQUserUploadDataRepository
						.findById(compositeKey);
				if (enforcementReviewCaseExist.isPresent()) {
					EnforcementReviewCase object = enforcementReviewCaseExist.get();
					System.out.println("Value exist!");
					return "Record already exists!";
				}
				enforcementReviewCase.setId(compositeKey);
				System.out.println(enforcementReviewCase);
				extensionNoDocument.getEnforcementReviewCases().add(enforcementReviewCase);
				caseWorkflow.setGSTIN(ExcelCellDataExtractor.getCellValue(row.getCell(0)));
				caseWorkflow.setCaseReportingDate(parsedDate);
				caseWorkflow.setPeriod(ExcelCellDataExtractor.getCellValue(row.getCell(4)));
				caseWorkflow.setAssignedFrom("HQ");
				caseWorkflow.setAssignedFromUserId(userId);
				caseWorkflow.setAssignedTo("FO");
				caseWorkflow.setAssigntoUserId(0);
				caseWorkflow.setAssignedFromLocationId("HP");
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAction("upload");
//				caseWorkflow.setWorkflowStatus(true);
//				caseWorkflow.setRemarks(transferRemarksRepository.findById(1).get());
				caseWorkflow.setAssignedToLocationId(locationDetailsRepository
						.findByLocationName(ExcelCellDataExtractor.getCellValue(row.getCell(5))).get().getLocationId());
				caseWorkflowList.add(caseWorkflow);
			}
			uploadDataSaveHeadquarterLogs(extensionNoDocumentsRepository.save(extensionNoDocument));
			logger.info("Headquater : Upload cases save");
			caseWorkflowRepository.saveAll(caseWorkflowList);
			// uploadDataSaveHeadquarterLogs(extensionNoDocument);
			return "Form submitted successfully!";
		} catch (IOException e) {
			e.printStackTrace();
			return "Unable to Submit the Form!";
		} finally {
			workbook.close();
			inputStream.close();
		}
	}

	private void uploadDataSaveHeadquarterLogs(ExtensionNoDocument extensionNoDocument) {
		List<HeadquarterLogs> hqLogsList = new ArrayList<>();
		List<EnforcementReviewCase> enCases = extensionNoDocument.getEnforcementReviewCases();
		Map<Integer, String> allParameterMap = mstParametersModuleWiseRepository.findAll().stream()
				.collect(Collectors.toMap(param -> param.getId(), param -> param.getParamName()));
		for (EnforcementReviewCase enCase : enCases) {
			HeadquarterLogs object = new HeadquarterLogs();
			object.setGSTIN(enCase.getId().getGSTIN());
			object.setPeriod(enCase.getId().getPeriod());
			object.setCaseReportingDate(enCase.getId().getCaseReportingDate());
			object.setTaxpayerName(enCase.getTaxpayerName());
			object.setExtensionNo(enCase.getExtensionNo());
			object.setCategory(enCase.getCategory());
			object.setIndicativeTaxValue(enCase.getIndicativeTaxValue());
			object.setAction(enCase.getAction());
			object.setAssignedTo(enCase.getAssignedTo());
			object.setAssignedFromUserId(enCase.getAssignedFromUserId());
			object.setAssignedToUserId(enCase.getAssignedToUserId());
			object.setExtensionFileName(extensionNoDocument.getExtensionFileName());
			object.setWorkingLocation(enCase.getLocationDetails().getLocationName());
			object.setCaseUpdatingTime(new Date());
			object.setAssignedFrom("HQ");
			object.setParameter(
					Arrays.stream(enCase.getParameter().split(",")).map(paramId -> Integer.parseInt(paramId))
							.map(allParameterMap::get).collect(Collectors.joining(",")));
			hqLogsList.add(object);
		}
		if (hqLogsList != null && hqLogsList.size() > 0) {
			headquarterLogsRepository.saveAll(hqLogsList);
		}
	}

	public void transferCaseActionHeadquarterLogs(EnforcementReviewCase enCase, String rejectRemark) {
		HeadquarterLogs object = new HeadquarterLogs();
		Map<String, String> map = hqTransferRequest.getSuggestedJurisdiction(enCase);
		object.setGSTIN(enCase.getId().getGSTIN());
		object.setPeriod(enCase.getId().getPeriod());
		object.setCaseReportingDate(enCase.getId().getCaseReportingDate());
		object.setTaxpayerName(enCase.getTaxpayerName());
		object.setExtensionNo(enCase.getExtensionNo());
		object.setCategory(enCase.getCategory());
		object.setIndicativeTaxValue(Long.valueOf(enCase.getIndicativeTaxValue()));
		object.setAction(enCase.getAction());
		object.setAssignedTo(enCase.getAssignedTo());
		object.setAssignedFromUserId(enCase.getAssignedFromUserId());
		object.setAssignedToUserId(enCase.getAssignedToUserId());
		object.setSuggestedJurisdiction(
				(map.get("suggested_jurisdiction") != null && map.get("suggested_jurisdiction").trim().length() > 0)
						? (locationDetailsRepository.findById(map.get("suggested_jurisdiction")).get()
								.getLocationName())
						: "Not Available");
		if (rejectRemark != null && rejectRemark.trim().length() > 0) {
			object.setRemark(map.get("remark") + " | Reject Remarks :" + rejectRemark);
		} else {
			object.setRemark(map.get("remark"));
		}
		object.setWorkingLocation(enCase.getLocationDetails().getLocationName());
		object.setCaseUpdatingTime(new Date());
		object.setAssignedFrom("HQ");
		headquarterLogsRepository.save(object);
	}

	@Override
	@Transactional
	public String saveHqUserUploadDataList(String extensionNo, String category, MultipartFile pdfFile, Integer userId,
			List<List<String>> dataList) throws IOException, ParseException {
		try {
			List<CaseWorkflow> caseWorkflowList = new ArrayList<>();
			// Save PDF
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
			String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_" + formattedTimestamp
					+ ".pdf";
			pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
			ExtensionNoDocument extensionNoDocument = new ExtensionNoDocument();
			List<EnforcementReviewCaseAssignedUsers> enforcementReviewCaseAssignedUsersList = new ArrayList<>();
			extensionNoDocument.setExtensionFileName(pdfFileName);
			extensionNoDocument.setExtensionNo(extensionNo);
			// save Data List
			for (List<String> row : dataList) {
				EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
				EnforcementReviewCaseAssignedUsers ercAssignedUsers = new EnforcementReviewCaseAssignedUsers();
				CompositeKey compositeKey = new CompositeKey();
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(2));
				compositeKey.setGSTIN(row.get(0));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(row.get(4));
				Optional<EnforcementReviewCase> enforcementReviewCaseExist = hQUserUploadDataRepository
						.findById(compositeKey);
				if (enforcementReviewCaseExist.isPresent()) {
					EnforcementReviewCase object = enforcementReviewCaseExist.get();
					logger.error("Duplicated value found on save uploded cases : " + compositeKey);
					return "Record already exists!";
				}
				ercAssignedUsers.setId(compositeKey);
				ercAssignedUsers.setHqUserId(userId);
				ercAssignedUsers.setFoUserId(0);
				ercAssignedUsers.setRuUserId(0);
				ercAssignedUsers.setApUserId(0);
				ercAssignedUsers.setRmUserId(0);
				enforcementReviewCase.setId(compositeKey);
				enforcementReviewCase.setCategory(category);
				enforcementReviewCase.setExtensionNo(extensionNo);
				enforcementReviewCase.setTaxpayerName(row.get(1));
				Double indicativeTaxValue = Double.parseDouble(row.get(3));
				enforcementReviewCase.setIndicativeTaxValue(indicativeTaxValue.longValue());
				enforcementReviewCase
						.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				enforcementReviewCase.setExtensionNoDocument(extensionNoDocument);
				enforcementReviewCase.setAssignedTo("FO");
				enforcementReviewCase.setAssignedFrom("HQ");
				enforcementReviewCase.setAssignedFromUserId(userId);
				enforcementReviewCase.setAssignedToUserId(0);
				enforcementReviewCase.setAction("upload");
				StringBuilder parameter = new StringBuilder();
				StringBuilder parameterName = new StringBuilder();
				for (int i = 6; (i < row.size() && row.get(i).trim().length() > 0); i++) {
					Optional<MstParametersModuleWise> objectP = mstParametersModuleWiseRepository
							.findByParamNameAndStatusAssessment(row.get(i), true);
					if (objectP.isPresent() && (i < row.size() - 1)) {
						parameter.append(objectP.get().getId());
						parameter.append(",");
						parameterName.append(row.get(i) + ",");
					}
				}
				enforcementReviewCase.setParameter(parameter.toString());
				logger.info("Uploded Case : " + enforcementReviewCase);
				extensionNoDocument.getEnforcementReviewCases().add(enforcementReviewCase);
				enforcementReviewCaseAssignedUsersList.add(ercAssignedUsers);
				caseWorkflow.setGSTIN(row.get(0));
				caseWorkflow.setCaseReportingDate(parsedDate);
				caseWorkflow.setPeriod(row.get(4));
				caseWorkflow.setAssignedFrom("HQ");
				caseWorkflow.setAssignedFromUserId(userId);
				caseWorkflow.setAssignedTo("FO");
				caseWorkflow.setAssigntoUserId(0);
				caseWorkflow.setAssignedFromLocationId("HP");
				caseWorkflow.setAssignedFromLocationName(
						locationDetailsRepository.findByLocationId("HP").get().getLocationName());
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAction("upload");
				caseWorkflow.setAssignedToLocationId(
						locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationId());
				caseWorkflow.setAssignedToLocationName(
						locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationName());
				caseWorkflow.setParameter(parameterName.toString());
				caseWorkflowList.add(caseWorkflow);
			}
			extensionNoDocumentsRepository.save(extensionNoDocument);
			logger.info("Headquater : Upload cases save");
			enforcementReviewCaseAssignedUsersRepository.saveAll(enforcementReviewCaseAssignedUsersList);
			caseWorkflowRepository.saveAll(caseWorkflowList);
			uploadDataSaveHeadquarterLogs(extensionNoDocument);
			/********** Upload Notification Start ***********/
			casePertainUserNotification.caseUploadNotification(extensionNoDocument);
			/********** Upload Notification End ***********/
			return "Form submitted successfully!";
		} catch (IOException e) {
			logger.error("Uploded Case : Unable to save upload cases : " + e.getMessage());
			return "Unable to Submit the Form!";
		}
	}

	@Override
	@Transactional
	public String saveHqUserUploadDataList(Integer assignedToUserId, List<List<String>> uploadDataList,
			String category) {
		try {
			List<CaseWorkflow> caseWorkflowList = new ArrayList<>();
			List<EnforcementReviewCaseAssignedUsers> enforcementReviewCaseAssignedUsersList = new ArrayList<>();
			List<EnforcementReviewCase> enforcementReviewCaseList = new ArrayList<>();
			// save Data List
			for (List<String> row : uploadDataList) {
				EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
				EnforcementReviewCaseAssignedUsers ercAssignedUsers = new EnforcementReviewCaseAssignedUsers();
				CompositeKey compositeKey = new CompositeKey();
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(2));
				compositeKey.setGSTIN(row.get(0));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(row.get(4));
				Optional<EnforcementReviewCase> enforcementReviewCaseExist = hQUserUploadDataRepository
						.findById(compositeKey);
				if (enforcementReviewCaseExist.isPresent()) {
					EnforcementReviewCase object = enforcementReviewCaseExist.get();
					logger.error("Duplicated value found on save uploded cases : " + compositeKey);
					return "Record already exists!";
				}
				ercAssignedUsers.setId(compositeKey);
				ercAssignedUsers.setHqUserId(0);
				ercAssignedUsers.setFoUserId(assignedToUserId);
				ercAssignedUsers.setRuUserId(0);
				ercAssignedUsers.setApUserId(0);
				ercAssignedUsers.setRmUserId(0);
				enforcementReviewCase.setId(compositeKey);
				enforcementReviewCase.setCategory(category);
				enforcementReviewCase.setTaxpayerName(row.get(1));
				Double indicativeTaxValue = Double.parseDouble(row.get(3));
				enforcementReviewCase.setIndicativeTaxValue(indicativeTaxValue.longValue());
				enforcementReviewCase
						.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				enforcementReviewCase.setAssignedTo("FO");
				enforcementReviewCase.setAssignedFrom("FO");
				enforcementReviewCase.setAssignedFromUserId(assignedToUserId);
				enforcementReviewCase.setAssignedToUserId(assignedToUserId);
				enforcementReviewCase.setAction("acknowledge");
				enforcementReviewCase.setCaseId(row.get(8));
				enforcementReviewCase.setActionStatus(
						actionStatusRepository.findById(1).isPresent() ? actionStatusRepository.findById(1).get()
								: null);
				enforcementReviewCase
						.setFindingCategory((row.get(7) != null && !row.get(7).trim().isEmpty()) ? row.get(7) : null);
				// Remarks
				Map<String, String> allParameterMap = mstParametersModuleWiseRepository.findAll().stream()
						.collect(Collectors.toMap(param -> param.getParamName(), param -> param.getId().toString()));
				if (row.get(6) != null && !row.get(6).trim().isEmpty()) {
					List<String> parameterIds = Arrays.stream(row.get(6).trim().split(",")).map(paramName -> paramName)
							.map(allParameterMap::get).collect(Collectors.toList());
					enforcementReviewCase.setParameter(String.join(",", parameterIds));
				}
				enforcementReviewCaseList.add(enforcementReviewCase);
				logger.info("Uploded Case : " + enforcementReviewCase);
				enforcementReviewCaseAssignedUsersList.add(ercAssignedUsers);
				caseWorkflow.setGSTIN(row.get(0));
				caseWorkflow.setCaseReportingDate(parsedDate);
				caseWorkflow.setPeriod(row.get(4));
				caseWorkflow.setAssignedFrom("FO");
				caseWorkflow.setAssignedFromUserId(assignedToUserId);
				caseWorkflow.setAssignedTo("FO");
				caseWorkflow.setAssigntoUserId(assignedToUserId);
				caseWorkflow.setAssignedFromLocationId(
						locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationId());
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAction("uploadFromFO");
				caseWorkflow.setAssignedToLocationId(
						locationDetailsRepository.findByLocationName(row.get(5)).get().getLocationId());
				caseWorkflowList.add(caseWorkflow);
			}
			if (enforcementReviewCaseList != null && enforcementReviewCaseList.size() > 0) {
				enforcementReviewCaseRepository.saveAll(enforcementReviewCaseList);
				logger.info("Field Office | Self Detected Case : Upload cases save");
				enforcementReviewCaseAssignedUsersRepository.saveAll(enforcementReviewCaseAssignedUsersList);
				caseWorkflowRepository.saveAll(caseWorkflowList);
				fieldUserService.uploadDataSaveFoLogs(enforcementReviewCaseList);
				return "Form submitted successfully!";
			}
		} catch (Exception e) {
			logger.error("Field Office | Self Detected Case | Uploded Case : Unable to save upload cases : "
					+ e.getMessage());
		}
		return "Unable to Submit the Form!";
	}

	private void uploadDataSaveHeadquarterLogs(List<EnforcementReviewCase> enCases) {
		try {
			List<HeadquarterLogs> hqLogsList = new ArrayList<>();
			for (EnforcementReviewCase enCase : enCases) {
				HeadquarterLogs object = new HeadquarterLogs();
				object.setGSTIN(enCase.getId().getGSTIN());
				object.setPeriod(enCase.getId().getPeriod());
				object.setCaseReportingDate(enCase.getId().getCaseReportingDate());
				object.setTaxpayerName(enCase.getTaxpayerName());
				object.setCategory(enCase.getCategory());
				object.setIndicativeTaxValue(enCase.getIndicativeTaxValue());
				object.setAction(enCase.getAction());
				object.setAssignedTo(enCase.getAssignedTo());
				object.setAssignedFromUserId(enCase.getAssignedFromUserId());
				object.setAssignedToUserId(enCase.getAssignedToUserId());
				object.setWorkingLocation(enCase.getLocationDetails().getLocationName());
				object.setCaseUpdatingTime(new Date());
				object.setAssignedFrom("HQ");
				hqLogsList.add(object);
			}
			if (hqLogsList != null && hqLogsList.size() > 0) {
				headquarterLogsRepository.saveAll(hqLogsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HQUserUploadServiceImpl : uploadDataSaveHeadquarterLogs : " + e.getMessage());
		}
	}
}
