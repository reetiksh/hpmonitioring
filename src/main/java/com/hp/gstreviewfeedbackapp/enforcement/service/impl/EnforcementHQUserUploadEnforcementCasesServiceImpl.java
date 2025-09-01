package com.hp.gstreviewfeedbackapp.enforcement.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementCaseCaseWorkflow;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementHqUserLogs;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMasterAllocatingUser;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementActionStatusRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementCaseCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementHqUserLogsRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterAllocatingUserRepository;
import com.hp.gstreviewfeedbackapp.enforcement.repository.EnforcementMasterRepository;
import com.hp.gstreviewfeedbackapp.enforcement.service.EnforcementHQUserUploadEnforcementCasesService;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;

@Service
public class EnforcementHQUserUploadEnforcementCasesServiceImpl
		implements EnforcementHQUserUploadEnforcementCasesService {
	private static final Logger logger = LoggerFactory
			.getLogger(EnforcementHQUserUploadEnforcementCasesServiceImpl.class);
	@Autowired
	private EnforcementExtensionNoDocumentRepository extensionNoDocumentRepository;
	@Autowired
	private EnforcementCaseCaseWorkflowRepository caseCaseWorkflowRepository;
	@Autowired
	private EnforcementMasterRepository enforcementMasterRepository;
	@Autowired
	private CategoryListRepository categoryRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private EnforcementActionStatusRepository enforcementActionStatusRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private EnforcementMasterAllocatingUserRepository allocatingUserRepository;
	@Autowired
	private EnforcementHqUserLogsRepository enforcementHqUserLogsRepository;
	@Value("${upload.enforcement.directory}")
	private String uploadDirectory;
	
	@Autowired
	private CommonUtility commonUtility;
	
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;

	@Override
	public String saveHqUploadEnforcementDataList(String extensionNo, String category, MultipartFile pdfFile,
			Integer userId, List<List<String>> dataList) {
		try {
			List<EnforcementCaseCaseWorkflow> caseWorkflowList = new ArrayList<>();
			// Save PDF
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
			String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_" + formattedTimestamp
					+ ".pdf";
			pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
			EnforcementExtensionNoDocument extensionNoDocument = new EnforcementExtensionNoDocument();
			List<EnforcementMasterAllocatingUser> enforcementcasAllocatingUserList = new ArrayList<>();
			extensionNoDocument.setExtensionFileName(pdfFileName);
			extensionNoDocument.setExtensionNo(extensionNo);
			extensionNoDocument.setFileDirectory(uploadDirectory);
			// save Data List
			for (List<String> row : dataList) {
				EnforcementMaster enforcementMaster = new EnforcementMaster();
				EnforcementMasterAllocatingUser enfAllocatingUser = new EnforcementMasterAllocatingUser();
				CompositeKey compositeKey = new CompositeKey();
				EnforcementCaseCaseWorkflow caseWorkflow = new EnforcementCaseCaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(2));
				compositeKey.setGSTIN(row.get(0));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(row.get(4));
				Optional<EnforcementMaster> enforcementCaseExist = enforcementMasterRepository.findById(compositeKey);
				if (enforcementCaseExist.isPresent()) {
					logger.error("Duplicated value found on save uploded cases : " + compositeKey);
					return "Record already exists!";
				}
				// Save allocating user details
				enfAllocatingUser.setId(compositeKey);
				enfAllocatingUser.setHqUserId(userId);
				// Save case details
				enforcementMaster.setId(compositeKey);
				enforcementMaster.setTaxpayerName(row.get(1));
				enforcementMaster.setExtensionNo(extensionNo);
				enforcementMaster.setCategory(categoryRepository.findById(Long.parseLong(category.trim())).get());
				Double indicativeTaxValue = Double.parseDouble(row.get(3));
				enforcementMaster.setIndicativeTaxValue(indicativeTaxValue.longValue());
				enforcementMaster.setAction(enforcementActionStatusRepository.findByCodeName("upload").get());
				enforcementMaster.setAssignedTo("Enforcement_FO");
				enforcementMaster.setAssignedFrom("Enforcement_HQ");
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
				enforcementMaster.setParameter(parameter.toString());
				enforcementMaster.setCaseLocation(locationDetailsRepository.findByLocationName(row.get(5)).get());
				enforcementMaster.setExtensionNoDocumentId(extensionNoDocument);
				enforcementMaster.setCaseUpdatingTimestamp(new Date());
				enforcementMaster.setCaseUpdatedByUser(userDetailsRepository.findById(userId).get());
				// Adding case to extension document
				extensionNoDocument.getEnforcementCases().add(enforcementMaster);
				enforcementcasAllocatingUserList.add(enfAllocatingUser);
				// Adding data in case workflow table
				caseWorkflow.setGstin(row.get(0));
				caseWorkflow.setCaseReportingDate(parsedDate);
				caseWorkflow.setPeriod(row.get(4));
				caseWorkflow.setAssignedFrom("Enforcement_HQ");
				caseWorkflow.setAssignedFromUserId(userId);
				caseWorkflow.setAssignedTo("Enforcement_FO");
				caseWorkflow.setAssignedFromLocation(locationDetailsRepository.findByLocationId("HP").get());
				caseWorkflow
						.setAssignedToLocation(locationDetailsRepository.findByLocationName(row.get(5).trim()).get());
				caseWorkflow.setAssigntoUserId(0);
				caseWorkflow.setCaseUpdatingTimestamp(new Date());
				caseWorkflow.setSupportingFile(pdfFileName);
				caseWorkflow.setSupportingFileDirectory(uploadDirectory);
				caseWorkflow.setAssignedFromLocationName(
						locationDetailsRepository.findByLocationId("HP").get().getLocationName());
				caseWorkflow.setAssignedToLocationName(
						locationDetailsRepository.findByLocationName(row.get(5).trim()).get().getLocationName());
				caseWorkflow.setParameterName(parameterName.toString());
				caseWorkflow.setAction(enforcementMaster.getAction());
				caseWorkflowList.add(caseWorkflow);
			}
			extensionNoDocumentRepository.save(extensionNoDocument);
			logger.info("Enforcement Headquater : Upload cases save : Headquater User : "
					+ userDetailsRepository.findById(userId).get().getLoginName() + " : Total Case uploaded : "
					+ extensionNoDocument.getEnforcementCases().size());
			allocatingUserRepository.saveAll(enforcementcasAllocatingUserList);
			caseCaseWorkflowRepository.saveAll(caseWorkflowList);
			// Saving logs
			uploadDataSaveEnforcementCaseHeadquarterLogs(extensionNoDocument, userId);
			/********** Upload Notification Start ***********/
//			casePertainUserNotification.caseUploadNotification(extensionNoDocument);
			/********** Upload Notification End ***********/
			return "Form submitted successfully!";
		} catch (Exception e) {
			logger.error("Enforcement Upload Case : Unable to save upload cases : " + e.getMessage()
					+ " : Headquater User : " + userDetailsRepository.findById(userId).get().getLoginName());
			e.printStackTrace();
			return "Unable to Submit the Form!";
		}
	}

	@Override
	public String uploadDataSaveEnforcementCaseHeadquarterLogs(EnforcementExtensionNoDocument extensionNoDocument,
			Integer assignFromUserId) {
		try {
			List<EnforcementHqUserLogs> hqUserLogsList = new ArrayList<>();
			for (EnforcementMaster master : extensionNoDocument.getEnforcementCases()) {
				EnforcementHqUserLogs logs = new EnforcementHqUserLogs();
				logs.setGstin(master.getId().getGSTIN());
				logs.setCaseReportingDate(master.getId().getCaseReportingDate());
				logs.setPeriod(master.getId().getPeriod());
				logs.setAssignedFrom(master.getAssignedFrom());
				logs.setAssignedTo(master.getAssignedTo());
				logs.setAssignedFromUserId(assignFromUserId);
				logs.setAssigntoUserId(0);
				logs.setCaseUpdatingTimestamp(new Date());
				logs.setSupportingFile(master.getExtensionNoDocumentId().getExtensionFileName());
				logs.setSupportingFileDirectory(master.getExtensionNoDocumentId().getFileDirectory());
				logs.setAssignedFromLocationName(
						locationDetailsRepository.findByLocationId("HP").get().getLocationName());
				logs.setAssignedToLocationName(master.getCaseLocation().getLocationName());
				logs.setCaseUpdatedByUser(assignFromUserId);
				logs.setAction(master.getAction().getCodeName());
				hqUserLogsList.add(logs);
			}
			enforcementHqUserLogsRepository.saveAll(hqUserLogsList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"EnforcementHQUserUploadEnforcementCasesServiceImpl : uploadDataSaveEnforcementCaseHeadquarterLogs : "
							+ e.getMessage());
		}
		return null;
	}

//	public List<EnforcementMaster> getReviewCasesListStatusWise(Integer id, List<String> workingLoacationList) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<EnforcementMaster> getReviewCasesListStatusWise(Integer actionStatus,
			List<String> workingLoacationList) {
		List<EnforcementMaster> casesList = new ArrayList<>();
		
		switch (actionStatus) {
		
		case 1:
			casesList = enforcementMasterRepository.getAllEnforcementCasesList(workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 2:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 3:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 4:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 5:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 6:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 7:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 8:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 9:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		case 10:
			casesList = enforcementMasterRepository.getAllEnforcementCasesListByAction(actionStatus,workingLoacationList);
			commonUtility.getAssignedToRoleList1(casesList);
			break;
		default:
			casesList = new ArrayList<>();
		}
		return casesList;
	}
	
}
