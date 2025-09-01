package com.hp.gstreviewfeedbackapp.scrutiny.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseUploadParametersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.UploadScrutinyCasesService;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;
import com.hp.gstreviewfeedbackapp.scrutiny.util.ExcelValidatorForUploadScrutinyCases;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Service
public class UploadScrutinyCasesServiceImpl implements UploadScrutinyCasesService {
	private static final org.jboss.logging.Logger logger = LoggerFactory.logger(UploadScrutinyCasesServiceImpl.class);
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private ExcelValidator excelValidator;
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private ScrutinyExtensionNoDocumentRepository scrutinyExtensionNoDocumentRepository;
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	@Autowired
	private ScrutinyCaseUploadParametersRepository scrutinyCaseUploadParametersRepository;
	@Autowired
	private ExcelValidatorForUploadScrutinyCases excelValidatorForUploadScrutinyCases;
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;
	
	@Autowired
	private CommonUtility commonUtility;
	
	@Value("${upload.directory}")
	private String uploadDirectory;

	@Override
	@Transactional
	public String uploadScrutinyCases(HQUploadForm hqUploadForm, BindingResult formResult,
			RedirectAttributes redirectAttributes, Model model, List<List<String>> dataList) throws ParseException {
		logger.info("UploadScrutinyCasesServiceImpl: uploadScrutinyCases(): initiated");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userDetails == null) {
			return "redirect:/logout";
		}
		List<String> excelErrors = new ArrayList<>();
		MultipartFile excelFile = null;
		MultipartFile pdfFile = null;
		PdfData pdfData = hqUploadForm.getPdfData();
		ExcelData excelData = hqUploadForm.getExcelData();
		Map<String, List<List<String>>> excelDataValidationMap = null;
		/* List<List<String>> dataList = excelDataValidationMap.get("uploadData"); */
		/*****************
		 * Insert Information in mst_scrutiny_cases start
		 ***************/
		pdfFile = pdfData.getPdfFile();
		excelFile = excelData.getExcelFile();
		try {
			List<ScrutinyCaseWorkflow> scrutinyCaseWorkflowList = new ArrayList<>();
			List<MstScrutinyCases> mstScrutinyCasesList = new ArrayList<>();
			// Save PDF
			LocalDateTime currentTimestamp = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
			String formattedTimestamp = currentTimestamp.format(formatter);
			String pdfFileName = "pdf_" + hqUploadForm.getExtensionNo().replace('\\', '.').replace('/', '.') + "_"
					+ formattedTimestamp + ".pdf";
			pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
			ScrutinyExtensionNoDocument scrutinyExtensionNoDocument = new ScrutinyExtensionNoDocument();
			List<ScrutinyCasesPertainToUsers> scrutinyCasesPertainToUsersList = new ArrayList<>();
			scrutinyExtensionNoDocument.setExtensionFileName(pdfFileName);
			scrutinyExtensionNoDocument.setExtensionNo(hqUploadForm.getExtensionNo());
			// save Data List
			for (List<String> row : dataList) {
				MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
				ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = new ScrutinyCasesPertainToUsers();
				CompositeKey compositeKey = new CompositeKey();
				ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setLenient(false);
				Date parsedDate = dateFormat.parse(row.get(2));
				compositeKey.setGSTIN(row.get(0));
				compositeKey.setCaseReportingDate(parsedDate);
				compositeKey.setPeriod(row.get(4));
				Optional<MstScrutinyCases> alreadyExistedScrutinyCases = mstScrutinyCasesRepository
						.findById(compositeKey);
				if (alreadyExistedScrutinyCases.isPresent()) {
					MstScrutinyCases object = alreadyExistedScrutinyCases.get();
					logger.error("Duplicated value found on save uploded cases : " + compositeKey);
					return "Record already exists!";
				}
				scrutinyCasesPertainToUsers.setId(compositeKey);
				scrutinyCasesPertainToUsers.setHqUserId(userDetails.getUserId());
				scrutinyCasesPertainToUsers.setFoUserId(0);
				scrutinyCasesPertainToUsers.setRuUserId(0);
				mstScrutinyCases.setId(compositeKey);
				mstScrutinyCases.setCategory(categoryListRepository.findByName(hqUploadForm.getCategory()));
				mstScrutinyCases.setExtensionNo(hqUploadForm.getExtensionNo());
				mstScrutinyCases.setTaxpayerName(row.get(1));
				Double indicativeTaxValue = Double.parseDouble(row.get(3));
				mstScrutinyCases.setIndicativeTaxValue(indicativeTaxValue.longValue());
				mstScrutinyCases.setLocationDetails(locationDetailsRepository.findByLocationName(row.get(5)).get());
				mstScrutinyCases.setScrutinyExtensionNoDocument(scrutinyExtensionNoDocument);
				mstScrutinyCases.setAssignedTo("SFO");
				mstScrutinyCases.setAssignedFrom("SHQ");
				mstScrutinyCases.setAction("uploadScrutinyCases");
				mstScrutinyCases.setCaseUpdateDate(new Date());
				StringBuilder parameter = new StringBuilder();
				for (int i = 6; (i < row.size() && row.get(i).trim().length() > 0); i++) {
					Optional<MstParametersModuleWise> objectP = mstParametersModuleWiseRepository
							.findByParamNameAndStatusScrutiny(row.get(i), true);
					parameter.append(objectP.get().getId());
					if (objectP.isPresent() && (i < row.size() - 1)) {
						parameter.append(",");
					}
				}
				mstScrutinyCases.setParameters(parameter.toString());
				scrutinyExtensionNoDocument.getScrutinyCasesList().add(mstScrutinyCases);
				scrutinyCasesPertainToUsersList.add(scrutinyCasesPertainToUsers);
				scrutinyCaseWorkflow.setGstin(row.get(0));
				scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
				scrutinyCaseWorkflow.setPeriod(row.get(4));
				scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
				scrutinyCaseWorkflow.setAssignedFromUserId(userDetails.getUserId());
				scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
				scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
				scrutinyCaseWorkflow.setUpdatingDate(new Date());
				scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
				scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
				scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
				mstScrutinyCasesList.add(mstScrutinyCases);
				scrutinyCaseWorkflowList.add(scrutinyCaseWorkflow);
			}
			scrutinyExtensionNoDocumentRepository.save(scrutinyExtensionNoDocument);
			scrutinyCasesPertainToUsersRepository.saveAll(scrutinyCasesPertainToUsersList);
			mstScrutinyCasesRepository.saveAll(mstScrutinyCasesList);
			scrutinyCaseWorkflowRepository.saveAll(scrutinyCaseWorkflowList);
			return "Form submitted successfully!";
		} catch (IOException e) {
			logger.error("Uploded Case : Unable to save upload cases : " + e.getMessage());
			return "Unable to Submit the Form!";
		}
		/**************** Insert Information in mst_scrutiny_cases end ***************/
	}

	@Override
	public List<MstScrutinyCases> getRequestForTransferList() {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getRequestForTransferList();
		try {
			for (MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
				ScrutinyCaseWorkflow scrutinyCaseWorkflow = scrutinyCaseWorkflowRepository.getAssignToLoationId(
						mstScrutinyCasesSolo.getId().getGSTIN(), mstScrutinyCasesSolo.getId().getPeriod(),
						mstScrutinyCasesSolo.getId().getCaseReportingDate());
//				System.out.println("GSTIn : " + mstScrutinyCasesSolo.getId().getGSTIN() 
//						+ " | Period : " + mstScrutinyCasesSolo.getId().getPeriod() 
//						+ " | CaseReportingDate : " + mstScrutinyCasesSolo.getId().getCaseReportingDate());
				if (scrutinyCaseWorkflow != null) {
					mstScrutinyCasesSolo
							.setSuggestedJurisdictionName((scrutinyCaseWorkflow.getAssignedToLocationId() != null
									&& !scrutinyCaseWorkflow.getAssignedToLocationId().trim().isEmpty())
											? locationDetailsRepository
													.findByLocationId(scrutinyCaseWorkflow.getAssignedToLocationId())
													.get().getLocationName()
											: null);
					mstScrutinyCasesSolo
							.setSuggestedJurisdictionId((scrutinyCaseWorkflow.getAssignedToLocationId() != null
									&& !scrutinyCaseWorkflow.getAssignedToLocationId().trim().isEmpty())
											? locationDetailsRepository
													.findByLocationId(scrutinyCaseWorkflow.getAssignedToLocationId())
													.get().getLocationId()
											: null);
					mstScrutinyCasesSolo.setFilePath(scrutinyCaseWorkflow.getFilePath() != null
							? URLEncoder.encode(scrutinyCaseWorkflow.getFilePath())
							: "");
					if (scrutinyCaseWorkflow.getRemarks().getId() == 1) {
						mstScrutinyCasesSolo.setRemark(scrutinyCaseWorkflow.getOtherRemarks());
					} else {
						mstScrutinyCasesSolo.setRemark(scrutinyCaseWorkflow.getRemarks().getName());
					}
					logger.info("UploadScrutinyCasesServiceImpl : getRequestForTransferList()wrwerwer");
					logger.info("asdfsdf : " + mstScrutinyCasesSolo.getSuggestedJurisdictionName());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("UploadScrutinyCasesServiceImpl : getRequestForTransferList()" + ex.getMessage());
		}
		return mstScrutinyCasesList;
	}

	@Override
	public List<MstScrutinyCases> getRandomCasesList(List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository
				.getRandomCasesList(workingLoacationList);
		for (MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),
					mstScrutinyCasesSolo.getId().getCaseReportingDate(), mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
		}
		return mstScrutinyCasesList;
	}

	@Override
	public List<MstScrutinyCases> getRandomCasesListExceptClosureReport(List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository
				.getRandomCasesListExceptClosureReport(workingLoacationList);
		for (MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),
					mstScrutinyCasesSolo.getId().getCaseReportingDate(), mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
			switch (mstScrutinyCasesSolo.getAction()) {
			case "noNeedScrutiny":
				mstScrutinyCasesSolo.setActionDescription("Dropped before issuance of ASMT-10");
				break;
			case "closureReportDropped":
				mstScrutinyCasesSolo.setActionDescription("Dropped after issuance of ASMT-10");
				break;
			default:
				mstScrutinyCasesSolo.setActionDescription("");
			}
		}
		return mstScrutinyCasesList;
	}

	@Override
	public String randomRecommendForScrutiny(String recommendScrutinyGstin, String recommendScrutinyPeriod,
			String recommendScrutinyCaseReportingDate, String recommendScrutinyRemark) {
		String message = "";
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get()
					.getUserId();
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(recommendScrutinyCaseReportingDate);
			compositKey.setGSTIN(recommendScrutinyGstin);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(recommendScrutinyPeriod);
			mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("hqRecommendForScrutiny");
			mstScrutinyCases.setAssignedFrom("SHQ");
			mstScrutinyCases.setAssignedTo("SRU");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository
					.findById(compositKey).get();
			scrutinyCasesPertainToUsers.setHqUserId(userId);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn() != null
					? String.join(",", mstScrutinyCases.getRecoveryStageArn())
					: "");
			scrutinyCaseWorkflow.setOtherRemarks(recommendScrutinyRemark);
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Case recommended to verifier for mandatory scrutiny";
		} catch (Exception ex) {
			message = "There is network bandwidth issue....";
			logger.error("error :  " + ex.getMessage());
		}
		return message;
	}

	@Override
	public List<MstScrutinyCases> getReviewCasesListStatusWise(Integer actionStatus,
			List<String> workingLoacationList) {
		List<MstScrutinyCases> casesList = new ArrayList<>();
		switch (actionStatus) {
		case 1:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesList(workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 2:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("noNeedScrutiny",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 3:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("asmtTenIssued",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 4:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("closureReportDropped",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 5:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAssesAndAdjudication",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 6:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("acknowledgeScrutinyCase",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 7:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAudit",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		default:
			casesList = new ArrayList<>();
		}
		return casesList;
	}
}
