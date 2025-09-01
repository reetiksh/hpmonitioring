package com.hp.gstreviewfeedbackapp.cag.service.impl;

import java.io.File;
import java.io.IOException;
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

import com.hp.gstreviewfeedbackapp.cag.model.CAGUploadForm;
import com.hp.gstreviewfeedbackapp.cag.model.CagCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.cag.model.CagCompositeKey;
import com.hp.gstreviewfeedbackapp.cag.model.CagExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.cag.model.MstCagCases;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCasePertainToUserRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCaseRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.cag.service.UploadCagCasesService;
import com.hp.gstreviewfeedbackapp.cag.util.ExcelValidatorForUploadCAGCases;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseUploadParameters;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseUploadParametersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.UploadScrutinyCasesService;
import com.hp.gstreviewfeedbackapp.scrutiny.util.ExcelValidatorForUploadScrutinyCases;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Service
public class UploadCagCasesServiceImpl implements UploadCagCasesService {

	private static final org.jboss.logging.Logger logger = LoggerFactory.logger(UploadCagCasesServiceImpl.class);

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private ExcelValidator excelValidator;

	@Autowired
	private CagCaseRepository cagCaseRepository;

	@Autowired
	private LocationDetailsRepository locationDetailsRepository;

	@Autowired
	private CagExtensionNoDocumentRepository cagExtensionNoDocumentRepository;

	@Autowired
	private ExcelValidatorForUploadCAGCases excelValidatorForUploadCAGCases;
	
	@Autowired
	private CagCasePertainToUserRepository cagCasePertainToUserRepository;
	
	@Value("${upload.directory}")
	private String uploadDirectory;

	@Override
	@Transactional
	public String uploadCagCases(CAGUploadForm hqUploadForm, BindingResult formResult,
			RedirectAttributes redirectAttributes, Model model, List<List<String>> dataList) throws ParseException {
		
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

		/***************** Insert Information in mst_scrutiny_cases start ***************/
		pdfFile = pdfData.getPdfFile();
		excelFile = excelData.getExcelFile();
		
			try {
				 
				List<MstCagCases> mstCagCasesList = new ArrayList<>();
				
				Long uniqueId = (long) (cagCaseRepository.findAll().size() + 5);

				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = "pdf_" + hqUploadForm.getExtensionNo().replace('\\', '.').replace('/', '.') + "_"
						+ formattedTimestamp + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));

				CagExtensionNoDocument cagExtensionNoDocument = new CagExtensionNoDocument();
				List<CagCasesPertainToUsers> cagCasesPertainToUsersList = new ArrayList<>();
				cagExtensionNoDocument.setExtensionFileName(pdfFileName);
				cagExtensionNoDocument.setExtensionNo(hqUploadForm.getExtensionNo());
				
				// save Data List
				//for (List<String> row : dataList) {
				for (int i=0; i<dataList.size(); i++) {

					MstCagCases mstCagCases = new MstCagCases();
					
					CagCompositeKey compositeKey = new CagCompositeKey();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					dateFormat.setLenient(false);

					String taxpayerName = dataList.get(i).get(1);
					Date parsedDate = dateFormat.parse(dataList.get(i).get(2));
					Double indicativeTaxValue = Double.parseDouble(dataList.get(i).get(3));
					
					compositeKey.setGSTIN(dataList.get(i).get(0));
					compositeKey.setCaseReportingDate(parsedDate);
					compositeKey.setPeriod(dataList.get(i).get(4));
					compositeKey.setParameter(dataList.get(i).get(6));
					
					Optional<MstCagCases> alreadyExistedCagCases = cagCaseRepository.findById(compositeKey);
					
					if (alreadyExistedCagCases.isPresent()) {
						MstCagCases object = alreadyExistedCagCases.get();
						logger.error("Duplicated value found on save uploded cases : " + compositeKey);
						return "Record already exists!";
					}

					CagCasesPertainToUsers cagCasesPertainToUsers = new CagCasesPertainToUsers();
					cagCasesPertainToUsers.setId(compositeKey);
					cagCasesPertainToUsers.setHqUserId(userDetails.getUserId());
					cagCasesPertainToUsers.setFoUserId(0);

					mstCagCases.setId(compositeKey);
					mstCagCases.setCategory("CAG Param");
					mstCagCases.setExtensionNo(hqUploadForm.getExtensionNo());
					mstCagCases.setTaxpayerName(taxpayerName);
					mstCagCases.setIndicativeTaxValue(indicativeTaxValue.longValue());
					mstCagCases.setLocationDetails(locationDetailsRepository.findByLocationName(dataList.get(i).get(5)).get());
					mstCagCases.setCagExtensionNoDocument(cagExtensionNoDocument);
					mstCagCases.setAssignedTo("cag_fo");
					mstCagCases.setAssignedFrom("cag_hq");
					mstCagCases.setAction("uploadCagCases");
					mstCagCases.setCaseUpdateDate(new Date());
					mstCagCases.setUniqueId(uniqueId + i);
					
					cagExtensionNoDocument.getCagCasesList().add(mstCagCases);
					
					cagCasesPertainToUsersList.add(cagCasesPertainToUsers);

					mstCagCasesList.add(mstCagCases);
				}
				
				cagExtensionNoDocumentRepository.save(cagExtensionNoDocument);
				
				cagCasePertainToUserRepository.saveAll(cagCasesPertainToUsersList);
				
				cagCaseRepository.saveAll(mstCagCasesList);

				return "Form submitted successfully!";

			} catch (Exception e) {
				logger.error("Uploded Case : Unable to save upload cases : " + e.getMessage());
				return "Unable to Submit the Form!";

			}
		
		/**************** Insert Information in mst_scrutiny_cases end ***************/
	}
	

/*	
	public List<MstCagCases> getRequestForTransferList(){
		List<MstCagCases> mstCagCasesList =  cagCasesRepository.getRequestForTransferList();
		try {
			
			for(MstCagCases mstScrutinyCasesSolo:mstCagCasesList) {
				
				ScrutinyCaseWorkflow scrutinyCaseWorkflow = scrutinyCaseWorkflowRepository.getAssignToLoationId(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getPeriod(),mstScrutinyCasesSolo.getId().getCaseReportingDate());
				mstScrutinyCasesSolo.setSuggestedJurisdictionName(locationDetailsRepository.findByLocationId(scrutinyCaseWorkflow.getAssignedToLocationId()).get().getLocationName());
				mstScrutinyCasesSolo.setSuggestedJurisdictionId(locationDetailsRepository.findByLocationId(scrutinyCaseWorkflow.getAssignedToLocationId()).get().getLocationId());
				
				if(scrutinyCaseWorkflow.getRemarks().getId() == 1) {
					mstScrutinyCasesSolo.setRemark(scrutinyCaseWorkflow.getOtherRemarks());
				}
				else {
					mstScrutinyCasesSolo.setRemark(scrutinyCaseWorkflow.getRemarks().getName());
				}
				
				logger.info("UploadScrutinyCasesServiceImpl : getRequestForTransferList()wrwerwer");
			}
			
			
		}catch(Exception ex) {
			logger.error("UploadScrutinyCasesServiceImpl : getRequestForTransferList()" + ex.getMessage());
		}
		
		
		
		return mstCagCasesList;
	}

	public List<MstScrutinyCases> getRandomCasesList(List<String> workingLoacationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getRandomCasesList(workingLoacationList);
		for(MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getCaseReportingDate(),mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
		}
		return mstScrutinyCasesList;
	}

	public String randomRecommendForScrutiny(String recommendScrutinyGstin, String recommendScrutinyPeriod,	String recommendScrutinyCaseReportingDate, String recommendScrutinyRemark) {
		
		String message = "";
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();	
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
			
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
			
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository.findById(compositKey).get();
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
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn() != null ? String.join(",", mstScrutinyCases.getRecoveryStageArn()): "");
			scrutinyCaseWorkflow.setOtherRemarks(recommendScrutinyRemark);
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Case recommended to verifier for mandatory scrutiny";
		}catch(Exception ex) {
			message = "There is network bandwidth issue....";
			 logger.error("error :  " + ex.getMessage());
		}
		return message;
	}
	*/

	
	
}
