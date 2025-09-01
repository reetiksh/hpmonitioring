package com.hp.gstreviewfeedbackapp.audit.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCaseWorkflow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;
import com.hp.gstreviewfeedbackapp.audit.model.L1UserWorkLogs;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.L1UserWorkLogsRepository;
import com.hp.gstreviewfeedbackapp.audit.service.L1UserUploadAuditCasesService;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;

@Service
public class L1UserUploadAuditCasesServiceImpl implements L1UserUploadAuditCasesService {
	private static final Logger logger = LoggerFactory.getLogger(L1UserUploadAuditCasesServiceImpl.class);
	@Value("${upload.audit.directory}")
	private String uploadDirectory;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private AuditExtensionNoDocumentRepository auditExtensionNoDocumentRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private AuditMasterCaseWorkflowRepository auditMasterCaseWorkflowRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private L1UserWorkLogsRepository l1UserWorkLogsRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Value("${audit.case.upload}")
	private String auditCaseUploadCategory;

	@Override
	@Transactional
	public String saveL1UploadAuditDataList(String extensionNo, String category, MultipartFile pdfFile, Integer userId,
			List<List<String>> dataList) {
		try {
			List<AuditMasterCaseWorkflow> auditMasterCaseWorkflowList = new ArrayList<>();
			// Save PDF
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
			String pdfFileName = "pdf_" + extensionNo.replace('\\', '.').replace('/', '.') + "_" + formattedTimestamp
					+ ".pdf";
			pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
			AuditExtensionNoDocument extensionNoDocument = new AuditExtensionNoDocument();
			List<AuditMasterCasesAllocatingUsers> allocatingUserList = new ArrayList<>();
			extensionNoDocument.setPdfFileName(pdfFileName);
			extensionNoDocument.setExtensionNo(extensionNo);
			// save Data List
			for (List<String> row : dataList) {
				AuditMaster auditMaster = new AuditMaster();
				AuditMasterCaseWorkflow auditMasterCaseWorkflow = new AuditMasterCaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(3));
				Optional<AuditMaster> objectAM = auditMasterRepository.findById(row.get(0));
				if (objectAM.isPresent()) {
					logger.error(
							"Duplicated value found on save uploded cases : Case Id : " + objectAM.get().getCaseId());
					return "Record already exists!";
				}
				auditMaster.setCaseId(row.get(0));
				auditMaster.setGSTIN(row.get(1));
				auditMaster.setPeriod(row.get(4));
				auditMaster.setCaseReportingDate(parsedDate);
				auditMaster.setCategory(categoryListRepository.findById(Long.parseLong(category)).get());
				auditMaster.setExtensionNo(extensionNo);
				auditMaster.setTaxpayerName(row.get(2));
//				Double indicativeTaxValue = Double.parseDouble(row.get(4));
//				auditMaster.setIndicativeTaxValue(indicativeTaxValue.longValue());
				String parameter = "";
				for (int i = 6; (i < row.size() && row.get(i).trim().length() > 0); i++) {
					Optional<MstParametersModuleWise> objectP = mstParametersModuleWiseRepository
							.findByParamName(row.get(i));
					if (objectP.isPresent()) {
						parameter = parameter.concat(objectP.get().getId() + ",");
					}
				}
				auditMaster.setParameter(parameter);
				auditMaster.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				auditMaster.setAuditExtensionNoDocument(extensionNoDocument);
				auditMaster.setAssignTo("L2");
				auditMaster.setAssignedFrom("L1");
				auditMaster.setAction(auditCaseStatusRepository.findByStatus(auditCaseUploadCategory).get());
				auditMaster.setLastUpdatedTimeStamp(new Date());
				logger.info("Uploded Case : " + auditMaster);
				extensionNoDocument.getAuditMasterList().add(auditMaster);
				auditMasterCaseWorkflow.setCaseId(row.get(0));
				auditMasterCaseWorkflow.setGSTIN(row.get(1));
				auditMasterCaseWorkflow.setCaseReportingDate(parsedDate);
				auditMasterCaseWorkflow.setPeriod(row.get(4));
				auditMasterCaseWorkflow.setAssignedFrom("L1");
				auditMasterCaseWorkflow.setAssignedFromUserId(userId);
				auditMasterCaseWorkflow.setAssignTo("L2");
				auditMasterCaseWorkflow.setAssignToUserId(0);
				auditMasterCaseWorkflow.setAssignedFromLocationId(locationDetailsRepository.findById("HP").get());
				auditMasterCaseWorkflow.setUpdatingTimestamp(auditMaster.getLastUpdatedTimeStamp());
				auditMasterCaseWorkflow
						.setAction(auditCaseStatusRepository.findByStatus(auditCaseUploadCategory).get());
				auditMasterCaseWorkflow
						.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				auditMasterCaseWorkflow.setCategory(categoryListRepository.findById(Long.parseLong(category)).get());
				auditMasterCaseWorkflow.setTaxpayerName(auditMaster.getTaxpayerName());
				auditMasterCaseWorkflow.setIndicativeTaxValue(auditMaster.getIndicativeTaxValue());
				auditMasterCaseWorkflow.setParameter(auditMaster.getParameter());
				auditMasterCaseWorkflowList.add(auditMasterCaseWorkflow);
				AuditMasterCasesAllocatingUsers allocatingUsers = new AuditMasterCasesAllocatingUsers(row.get(0),
						userId, 0, 0, 0);
				allocatingUserList.add(allocatingUsers);
			}
			auditExtensionNoDocumentRepository.save(extensionNoDocument);
			logger.info("L1 : Upload cases save");
			auditMasterCaseWorkflowRepository.saveAll(auditMasterCaseWorkflowList);
			auditMasterCasesAllocatingUsersRepository.saveAll(allocatingUserList);
			uploadDataSaveL1UserWorkLogs(extensionNoDocument, userId);
			return "Form submitted successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L1 Uploded Case : Unable to save upload cases : " + e.getMessage());
		}
		return "Unable to Submit the Form! Please contact with administrator.";
	}

	private void uploadDataSaveL1UserWorkLogs(AuditExtensionNoDocument extensionNoDocument, Integer userId) {
		List<L1UserWorkLogs> l1UserWorkLogList = new ArrayList<>();
		try {
			for (AuditMaster auditCase : extensionNoDocument.getAuditMasterList()) {
				L1UserWorkLogs l1UserWorkLogs = new L1UserWorkLogs();
				l1UserWorkLogs.setCaseId(auditCase.getCaseId());
				l1UserWorkLogs.setGSTIN(auditCase.getGSTIN());
				l1UserWorkLogs.setCaseReportingDate(auditCase.getCaseReportingDate());
				l1UserWorkLogs.setPeriod(auditCase.getPeriod());
				l1UserWorkLogs.setTaxpayerName(auditCase.getTaxpayerName());
				l1UserWorkLogs.setIndicativeTaxValue(auditCase.getIndicativeTaxValue());
				l1UserWorkLogs.setParameter(auditCase.getParameter());
				l1UserWorkLogs.setLocationDetails(auditCase.getLocationDetails().getLocationName());
				l1UserWorkLogs.setCategory(auditCase.getCategory().getName());
				l1UserWorkLogs
						.setAuditExtensionNoDocument(auditCase.getAuditExtensionNoDocument().getExtensionFileName());
				l1UserWorkLogs.setExtensionNo(auditCase.getExtensionNo());
				l1UserWorkLogs.setAssignedFrom(auditCase.getAssignedFrom());
				l1UserWorkLogs.setAssignTo(auditCase.getAssignTo());
				l1UserWorkLogs.setAction(auditCase.getAction().getStatus());
				l1UserWorkLogs.setLastUpdatedTimeStamp(auditCase.getLastUpdatedTimeStamp());
				l1UserWorkLogs.setAssignedFromLocation("HP");
				l1UserWorkLogs.setAssignToLocation(auditCase.getLocationDetails().getLocationName());
				l1UserWorkLogs.setAssignedFromUserId(userId);
				l1UserWorkLogs.setAssignedToUserId(0);
				l1UserWorkLogList.add(l1UserWorkLogs);
			}
			l1UserWorkLogsRepository.saveAll(l1UserWorkLogList);
		} catch (Exception e) {
			logger.error("L1UserUploadAuditCasesServiceImpl : uploadDataSaveL1UserWorkLogs : " + e.getStackTrace());
		}
	}
	@Override
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations) {

        List<Object[]> rawData = auditMasterRepository.getAnnexureReportRaw(allMappedLocations);
        List<AnnexureReportRow> result = new ArrayList<>();

        for (Object[] row : rawData) {
            AnnexureReportRow dto = new AnnexureReportRow();
            dto.setZoneName((String) row[0]);
            dto.setCircleName((String) row[1]);
            dto.setAllottedCases(((Number) row[2]).intValue());
            dto.setAuditCasesCompleted(((Number) row[3]).intValue());
            dto.setPendingDeskReview(((Number) row[4]).intValue());
            dto.setPendingApprovalAuditPlan(((Number) row[5]).intValue());
            dto.setPendingExaminationBooks(((Number) row[6]).intValue());
            dto.setPendingDAR(((Number) row[7]).intValue());
            dto.setPendingFAR(((Number) row[8]).intValue());
            result.add(dto);
        }
        return result;
    
	}
	
	@Override
	public	List<String> getDashboardData(List<String> allMappedLocations){

        List<Object[]> resultList = auditMasterRepository.getDashboardMetrics(allMappedLocations);

        if (resultList != null && !resultList.isEmpty()) {
            Object[] result = resultList.get(0);
            List<String> dashboardData = new ArrayList<>();

            for (Object obj : result) {
                dashboardData.add(obj != null ? obj.toString() : "0");
            }
            return dashboardData;
        }

        return Arrays.asList("0", "0", "0", "0", "0", "0");
	}
}
