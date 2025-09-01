package com.hp.gstreviewfeedbackapp.cag.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.cag.model.CAGUploadForm;
import com.hp.gstreviewfeedbackapp.cag.model.CagCompositeKey;
import com.hp.gstreviewfeedbackapp.cag.model.CagFoReviewCase;
import com.hp.gstreviewfeedbackapp.cag.model.CagHqReviewCase;
import com.hp.gstreviewfeedbackapp.cag.model.MstCagCases;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCaseRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagFoReviewRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagHqReviewRepository;
import com.hp.gstreviewfeedbackapp.cag.service.impl.UploadCagCasesServiceImpl;
import com.hp.gstreviewfeedbackapp.cag.util.ExcelValidatorForUploadCAGCases;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.CAG_HQ)
public class CagHQUserController {
	private static final Logger logger = LoggerFactory.getLogger(CagHQUserController.class);
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private ExcelValidatorForUploadCAGCases excelValidatorForUploadCAGCases;
	@Autowired
	private UploadCagCasesServiceImpl uploadCagCasesServiceImpl;
	@Autowired
	private CagCaseRepository cagCaseRepository;
	@Autowired
	private CagFoReviewRepository cagFoReviewRepository;
	@Autowired
	private CagHqReviewRepository cagHqReviewRepository;
	@Value("${cag.upload}")
	private String actionUpload;
	@Value("${cag.transfer}")
	private String actionTransfer;
	@Value("${cag.acknowledge}")
	private String actionAcknowledge;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${cag.hqtransfer}")
	private String hqtransfer;
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	String uploadDataFlag = null;

	@GetMapping("/" + ApplicationConstants.CAG_DASHBOARD)
	public String dashboard(Model model) {
		setCAGHQMenu(model, ApplicationConstants.CAG_DASHBOARD);
		return "cag/hq/" + ApplicationConstants.CAG_DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("ScrutinyUploadController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setCAGHQMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}

	@GetMapping("/" + ApplicationConstants.CAG_UPLOAD_CASES)
	public String uploadReviewCases(Model model, @ModelAttribute("HqUploadForm") @Valid HQUploadForm HqUploadForm,
			BindingResult formResult, RedirectAttributes redirectAttributes) throws ParseException {
		logger.info("ScrutinyUploadController.uploadReviewCases() : ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES");
		setCAGHQMenu(model, ApplicationConstants.CAG_UPLOAD_CASES);
		return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
	}

	@PostMapping("/" + ApplicationConstants.CAG_UPLOAD_CASES)
	public String submitReviewCases(Model model, @ModelAttribute("HqUploadForm") @Valid CAGUploadForm HqUploadForm,
			BindingResult formResult, RedirectAttributes redirectAttributes) throws ParseException, IOException {
		List<String> excelErrors = new ArrayList<>();
		setCAGHQMenu(model, ApplicationConstants.CAG_UPLOAD_CASES);
		redirectAttributes.addFlashAttribute("formResult", formResult);
		if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists and from has error");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF does not exists and from has error");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as Excel file does not exists and from has error");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (formResult.hasErrors() && !HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as uploaded from has error");
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
				&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as PDF file does not exists");
			excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
				&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
			logger.error("Headquater : Unable to upload data as Excel file does not exists");
			excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
			HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
			return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
		}
		MultipartFile excelFile = null;
		MultipartFile pdfFile = null;
		PdfData pdfData = HqUploadForm.getPdfData();
		ExcelData excelData = HqUploadForm.getExcelData();
		Map<String, List<List<String>>> excelDataValidationMap = null;
		if (excelData != null) {
			excelFile = excelData.getExcelFile();
			if (excelFile != null && !excelFile.isEmpty()) {
				// full validation
				excelDataValidationMap = excelValidatorForUploadCAGCases
						.validateExcelAndExtractDataForUploadScrutinyCases(excelFile);
				if (excelDataValidationMap.get("uploadData") != null) {
					model.addAttribute("uploadData", excelDataValidationMap.get("uploadData"));
				}
				if (excelDataValidationMap.get("errorList") != null) {
					// redirectAttributes.addFlashAttribute("errorList",
					// excelDataValidationMap.get("errorList").get(0));
					model.addAttribute("errorList", excelDataValidationMap.get("errorList").get(0));
//						return "/hq/upload_review_cases";
					return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
				}
				if (!excelErrors.isEmpty() && excelErrors != null) {
					HqUploadForm.setExcelErrors(excelErrors);
//						return "/hq/upload_review_cases";
					return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
				}
			}
		}
		pdfFile = pdfData.getPdfFile();
		excelFile = excelData.getExcelFile();
		if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty()
				&& excelDataValidationMap.get("uploadData") != null
				&& excelDataValidationMap.get("errorList") == null) {
			uploadDataFlag = uploadCagCasesServiceImpl.uploadCagCases(HqUploadForm, formResult, redirectAttributes,
					model, excelDataValidationMap.get("uploadData"));
		}
		if (uploadDataFlag != null && uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
			// redirectAttributes.addFlashAttribute("successMessage",
			// ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
			model.addAttribute("successMessage", ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
		} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
			// redirectAttributes.addFlashAttribute("successMessage", "Record already
			// exists!");
			model.addAttribute("successMessage", "Record already exists!");
		} else {
			excelErrors.add(ApplicationConstants.DATA_STORE_PROCESS_NOT_COMPLETED);
			HqUploadForm.setExcelErrors(excelErrors);
			model.addAttribute("errorList", uploadDataFlag);
		}
		return "cag/hq/" + ApplicationConstants.CAG_UPLOAD_CASES;
	}

	@GetMapping("/" + ApplicationConstants.CAG_REQUEST_FOR_TRANSFER)
	public String requestForTransfer(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userDetails == null) {
			return "redirect:/logout";
		}
		int userId = userDetails.getUserId();
		setCAGHQMenu(model, ApplicationConstants.CAG_REQUEST_FOR_TRANSFER);
		List<EnforcementReviewCaseModel> listcase = new ArrayList<EnforcementReviewCaseModel>();
		List<MstCagCases> list = cagCaseRepository.findTransferList(actionTransfer, userId);
		for (MstCagCases mst : list) {
			EnforcementReviewCaseModel reviewCaseModel = new EnforcementReviewCaseModel();
			Optional<CagFoReviewCase> cagOptional = cagFoReviewRepository.findTransfterGstn(mst.getId().getGSTIN(),
					mst.getId().getPeriod(), mst.getId().getCaseReportingDate(), mst.getId().getParameter());
			if (cagOptional.isPresent()) {
				CagFoReviewCase gstn = cagOptional.get();
				reviewCaseModel.setGSTIN_ID(gstn.getGSTIN());
				reviewCaseModel.setPeriod_ID(gstn.getPeriod());
				reviewCaseModel.setDate(gstn.getCaseReportingDate());
				reviewCaseModel.setParameter(gstn.getParameter());
				reviewCaseModel.setTaxpayerName(gstn.getTaxpayerName());
				reviewCaseModel.setCategory(gstn.getCategory());
				reviewCaseModel.setIndicativeTaxValue(gstn.getIndicativeTaxValue());
				reviewCaseModel.setCaseUpdatedDate(gstn.getCaseUpdatingDate());
				reviewCaseModel.setCaseId(gstn.getWorkingLocation().getLocationId());
				reviewCaseModel.setCaseStageName(gstn.getWorkingLocation().getLocationName());
				reviewCaseModel.setCaseStageArn(locationDetailsRepository
						.findByLocationId(gstn.getSuggestedJurisdiction()).get().getLocationId());
				reviewCaseModel.setActionStatusName(locationDetailsRepository
						.findByLocationId(gstn.getSuggestedJurisdiction()).get().getLocationName());
				if (gstn.getSuggestedJurisdiction().equalsIgnoreCase("NC")) {
					reviewCaseModel.setUploadedFileName(gstn.getTransferFilePath());
				}
				listcase.add(reviewCaseModel);
			}
		}
		List<LocationDetails> circls = new ArrayList<>();
		List<LocationDetails> locationDetailsList = locationDetailsRepository.findAllByOrderByLocationNameAsc();
		for (LocationDetails ld : locationDetailsList) {
			if (!ld.getLocationId().equalsIgnoreCase("Z04") && !ld.getLocationId().equalsIgnoreCase("C81")
					&& !ld.getLocationId().equalsIgnoreCase("HPT") && !ld.getLocationId().equalsIgnoreCase("DT14")
					&& !ld.getLocationId().equalsIgnoreCase("EZ01") && !ld.getLocationId().equalsIgnoreCase("EZ02")
					&& !ld.getLocationId().equalsIgnoreCase("EZ03") && !ld.getLocationId().equalsIgnoreCase("EZ04")) {
				circls.add(ld);
			}
		}
		model.addAttribute("hqTransferList", listcase);
		model.addAttribute("locatoinMap", circls);
		return "cag/hq/" + ApplicationConstants.CAG_REQUEST_FOR_TRANSFER;
	}

	@PostMapping("/reject_transfer_cases")
	public String rejectTrasferCase(Model model, @ModelAttribute WorkFlowModel transferModel,
			RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(transferModel.getDate());
			CagCompositeKey compositKey = new CagCompositeKey();
			compositKey.setGSTIN(transferModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(transferModel.getPeriod());
			compositKey.setParameter(transferModel.getParameter());
			MstCagCases enforcementReviewCase = cagCaseRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(hqtransfer);
			enforcementReviewCase.setAssignedTo("cag_fo");
			enforcementReviewCase.setAssignedFrom("cag_hq");
			enforcementReviewCase.setAssignedToUserId(0);
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			cagCaseRepository.save(enforcementReviewCase);
			MstCagCases updatedReviewCase = cagCaseRepository.findById(compositKey).get();
			CagHqReviewCase hqReviewCase = new CagHqReviewCase();
			hqReviewCase.setGSTIN(compositKey.getGSTIN());
			hqReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			hqReviewCase.setPeriod(compositKey.getPeriod());
			hqReviewCase.setParameter(compositKey.getParameter());
			hqReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			hqReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			hqReviewCase.setCategory(updatedReviewCase.getCategory());
			hqReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			hqReviewCase.setAction("hqTransferCagCasesRejected");
			hqReviewCase.setCaseUpdatingDate(new Date());
			hqReviewCase.setAssignedTo("cag_fo");
			hqReviewCase.setUserId(userId);
			hqReviewCase.setAssignedToUserId(0);
			hqReviewCase.setExtensionNoDocument(updatedReviewCase.getCagExtensionNoDocument());
			hqReviewCase.setWorkingLocation(updatedReviewCase.getLocationDetails());
			hqReviewCase.setAssignedFromUserId(userId);
			hqReviewCase.setTransferRemarks(transferModel.getOtherRemarks());
			cagHqReviewRepository.save(hqReviewCase);
			logger.info("Case transfer rejected successfully");
			redirectAttrs.addFlashAttribute("successMessage", "Case transfer rejected successfully");
			/****** Check case already assign to specific user or not start *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"CAG_HQ", enforcementReviewCase.getLocationDetails(), "transfer", 0);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:/cag_hq/request_for_transfer";
	}

	@PostMapping("/approve_transfer_cases")
	public String approvetrasferCase(Model model, @ModelAttribute WorkFlowModel transferModel,
			RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(transferModel.getDate());
			CagCompositeKey compositKey = new CagCompositeKey();
			compositKey.setGSTIN(transferModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(transferModel.getPeriod());
			compositKey.setParameter(transferModel.getParameter());
			MstCagCases enforcementReviewCase = cagCaseRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(hqtransfer);
			enforcementReviewCase.setAssignedTo("cag_fo");
			enforcementReviewCase.setAssignedFrom("cag_hq");
			enforcementReviewCase.setAssignedToUserId(0);
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			enforcementReviewCase.setLocationDetails(
					locationDetailsRepository.findByLocationId(transferModel.getCaseAssignedTo()).get());
			cagCaseRepository.save(enforcementReviewCase);
			MstCagCases updatedReviewCase = cagCaseRepository.findById(compositKey).get();
			CagHqReviewCase hqReviewCase = new CagHqReviewCase();
			hqReviewCase.setGSTIN(compositKey.getGSTIN());
			hqReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			hqReviewCase.setPeriod(compositKey.getPeriod());
			hqReviewCase.setParameter(compositKey.getParameter());
			hqReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			hqReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			hqReviewCase.setCategory(updatedReviewCase.getCategory());
			hqReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			hqReviewCase.setAction("hqTransferCagCasesApprove");
			hqReviewCase.setCaseUpdatingDate(new Date());
			hqReviewCase.setAssignedTo("cag_fo");
			hqReviewCase.setUserId(userId);
			hqReviewCase.setAssignedToUserId(0);
			hqReviewCase.setExtensionNoDocument(updatedReviewCase.getCagExtensionNoDocument());
			hqReviewCase.setWorkingLocation(updatedReviewCase.getLocationDetails());
			hqReviewCase.setAssignedFromUserId(userId);
			cagHqReviewRepository.save(hqReviewCase);
			logger.info("Case transfer approved successfully");
			redirectAttrs.addFlashAttribute("successMessage", "Case transfer approved successfully");
			/****** Check case already assign to specific user or not start *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"CAG_HQ", enforcementReviewCase.getLocationDetails(), "transfer", 0);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:/cag_hq/request_for_transfer";
	}
	/*
	 * @PostMapping("/"+ApplicationConstants.CAG_REQUEST_FOR_TRANSFER)
	 * 
	 * @Transactional public String setTranferRequest(Model
	 * model, @RequestParam(required = false) String gstIn, @RequestParam(required =
	 * false) String caseReportingDate, @RequestParam(required = false) String
	 * period,
	 * 
	 * @RequestParam(required = false) String assignedTo, @RequestParam(required =
	 * false) String rejectRemark, RedirectAttributes redirectAttributes) throws
	 * ParseException {
	 * 
	 * SimpleDateFormat formatter = new
	 * SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); String
	 * requestForTransferApproveOrReject = new String(); Authentication
	 * authentication = SecurityContextHolder.getContext().getAuthentication();
	 * UserDetails userDetails =
	 * userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get
	 * (); if(userDetails==null) { return "redirect:/logout"; }
	 * 
	 * CompositeKey compositeKey = new CompositeKey(); compositeKey.setGSTIN(gstIn);
	 * compositeKey.setCaseReportingDate(formatter.parse(caseReportingDate));
	 * compositeKey.setPeriod(period);
	 * 
	 * Optional<EnforcementReviewCase> object =
	 * enforcementReviewCaseRepository.findById(compositeKey);
	 * if(object.isPresent()) { Optional<EnforcementReviewCaseAssignedUsers> object1
	 * = enforcementReviewCaseAssignedUsersRepository.findById(compositeKey);
	 * EnforcementReviewCaseAssignedUsers assignedUsers = object1.get();
	 * 
	 * EnforcementReviewCase enCase = object.get();
	 * 
	 * String assignedLocationId = enCase.getLocationDetails().getLocationId();
	 * 
	 * enCase.setAssignedTo("FO");
	 * enCase.setAssignedFromUserId(userDetails.getUserId());
	 * enCase.setAssignedToUserId(0); enCase.setAssignedFrom("HQ");
	 * enCase.setLocationDetails(locationDetailsRepository.findById(assignedTo).get(
	 * )); enCase.setAction(hqTransfer);
	 * 
	 * EnforcementReviewCase isSaveObject =
	 * enforcementReviewCaseRepository.save(enCase);
	 * logger.info("Headquater : Transfer request save"); if(isSaveObject!=null) {
	 * CaseWorkflow caseWorkflow = new CaseWorkflow();
	 * 
	 * caseWorkflow.setGSTIN(gstIn);
	 * caseWorkflow.setCaseReportingDate(formatter.parse(caseReportingDate));
	 * caseWorkflow.setPeriod(period); caseWorkflow.setAssignedFrom("HQ");
	 * caseWorkflow.setAssignedTo("FO"); caseWorkflow.setUpdatingDate(new Date());
	 * caseWorkflow.setAction(hqTransfer);
	 * caseWorkflow.setAssignedFromUserId(userDetails.getUserId());
	 * caseWorkflow.setAssigntoUserId(0);
	 * caseWorkflow.setAssignedFromLocationId("HP");
	 * caseWorkflow.setAssignedToLocationId(assignedTo);
	 * caseWorkflow.setOtherRemarks(rejectRemark);
	 * if(assignedLocationId.equals(assignedTo)) {
	 * caseWorkflow.setStatus("Rejected"); requestForTransferApproveOrReject =
	 * "hqTransferRejected"; redirectAttributes.addFlashAttribute("successMessage",
	 * "The transfer request has been rejected"); } else {
	 * caseWorkflow.setStatus("Approved"); requestForTransferApproveOrReject =
	 * "hqTransferApproved"; redirectAttributes.addFlashAttribute("successMessage",
	 * "The transfer request has been approved");
	 * 
	 * assignedUsers.setFoUserId(0); assignedUsers.setApUserId(0);
	 * assignedUsers.setRuUserId(0);
	 * 
	 * enforcementReviewCaseAssignedUsersRepository.save(assignedUsers); }
	 * 
	 * caseWorkflowRepository.save(caseWorkflow);
	 * hqUserUploadServiceImpl.transferCaseActionHeadquarterLogs(enCase,
	 * rejectRemark);
	 * 
	 * List<String> assigneZeroUserIdToSpecificUser = Arrays.asList("transfer");
	 * Integer hqUserId =
	 * enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
	 * enCase.getId().getGSTIN(),enCase.getId().getPeriod(),enCase.getId().
	 * getCaseReportingDate()).getHqUserId(); Integer foUserId =
	 * enforcementReviewCaseAssignedUsersRepository.findByGstinPeriodRepotingDate(
	 * enCase.getId().getGSTIN(),enCase.getId().getPeriod(),enCase.getId().
	 * getCaseReportingDate()).getFoUserId(); List<MstNotifications>
	 * notificationsList =
	 * mstNotificationsRepository.getNotificationsToUpdateNotificationPertainTo(
	 * enCase.getId().getGSTIN(),enCase.getId().getPeriod(),enCase.getId().
	 * getCaseReportingDate(),assigneZeroUserIdToSpecificUser,"HQ",
	 * returnWorkingLocation(userDetails.getUserId()));
	 * if(!notificationsList.isEmpty()) { for(MstNotifications notificationSolo :
	 * notificationsList) { notificationSolo.setNotificationPertainTo(hqUserId); }
	 * mstNotificationsRepository.saveAll(notificationsList); }
	 * casePertainUserNotification.insertReAssignNotification(enCase.getId().
	 * getGSTIN(),enCase.getId().getCaseReportingDate(),enCase.getId().getPeriod(),
	 * "FO",enCase.getLocationDetails(),requestForTransferApproveOrReject,foUserId,
	 * "you","Head Office");
	 * 
	 * } } else { logger.error("Headquater : Transfer request not updated"); return
	 * "redirect:/hq/"+ApplicationConstants.HO_REQUEST_FOR_TRANSFER +
	 * "?error_message = not_updated"; }
	 * 
	 * 
	 * return "redirect:/cag_hq/"+ApplicationConstants.CAG_REQUEST_FOR_TRANSFER ; }
	 */

	private void setCAGHQMenu(Model model, String activeMenu) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "CAG_HQ");
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "CAG_HQ");
				model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
				model.addAttribute("unReadNotificationList", unReadNotificationList);
				// model.addAttribute("loginedUserNotificationListCount",loginedUserNotificationList.size());
				model.addAttribute("loginedUserNotificationList", loginedUserNotificationList);
				model.addAttribute("convertUnreadToReadNotifications", "/fo/convert_unread_to_read_notifications");
				/*********** Get Notifications End *************/
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				/***************************
				 * get user locations role wise start
				 **************************/
				List<String> userRoleMapWithLocations = cutomUtility.roleMapWithLocations(userRoleMappings,
						userDetails);
				model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
				/***************************
				 * get user locations role wise end
				 **************************/
				// All Roles of the user
				Map<String, UserRole> userRoles = new HashMap<>();
				for (UserRoleMapping objectUrm : userRoleMappings) {
					if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
						userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
					}
				}
				model.addAttribute("UserLoginName", userDetails.getLoginName());
				model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
				model.addAttribute("activeRole", "CAG_HQ");
				model.addAttribute("homePageLink", "/cag_hq/dashboard");
				model.addAttribute("changeUserPassword", "/gu/change_password");
				/*************************
				 * to display user generic details start
				 ****************************/
				List<Integer> distinctUserRoleIds = new ArrayList<>();
				List<UserRoleMapping> uniqueRolesList = new ArrayList<>();
				UserProfileDetails userProfileDetails = new UserProfileDetails();
				userProfileDetails.setUserName(userDetails.getLoginName());
				userProfileDetails.setMob(userDetails.getMobileNumber());
				userProfileDetails.setEmailId(userDetails.getEmailId());
				userProfileDetails.setDob(userDetails.getDateOfBirth());
				userProfileDetails.setFirstName(userDetails.getFirstName());
				userProfileDetails.setLastName(userDetails.getLastName());
				userProfileDetails.setDesignation(userDetails.getDesignation().getDesignationName());
				for (UserRoleMapping userRoleSolo : userRoleMappings) {
					if ((userRoleSolo.getUserRole().getRoleName() != null)
							&& (!distinctUserRoleIds.contains(userRoleSolo.getUserRole().getId()))) {
						distinctUserRoleIds.add(userRoleSolo.getUserRole().getId());
						uniqueRolesList.add(userRoleSolo);
					}
				}
				List<String> roleNameList = uniqueRolesList.stream().map(mapping -> mapping.getUserRole().getRoleName())
						.collect(Collectors.toList());
				String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));
				String LocationsName = returnLocationsName(userRoleMappings);
				userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
				userProfileDetails.setAssignedWorkingLocations(LocationsName);
				model.addAttribute("userProfileDetails", userProfileDetails);
				model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
				/*************************
				 * to display user generic details end
				 ****************************/
			}
		}
		model.addAttribute("MenuList",
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.CAG_HQ));
		model.addAttribute("activeMenu", activeMenu);
	}

	/**************** return locations name start ******************/
	private String returnLocationsName(List<UserRoleMapping> userRoleMapList) {
		List<String> returnResultentSet = new ArrayList<>();
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());
		List<UserRoleMapping> zoneIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());
		List<UserRoleMapping> circleIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())).collect(Collectors.toList());
		if (!stateId.isEmpty()) {
			returnResultentSet.add(stateId.get(0).getStateDetails().getStateName());
		}
		if (!zoneIds.isEmpty()) {
			for (UserRoleMapping zoneIdsIdsSolo : zoneIds) {
				returnResultentSet.add(zoneIdsIdsSolo.getZoneDetails().getZoneName());
			}
		}
		if (!circleIds.isEmpty()) {
			for (UserRoleMapping circleIdsSolo : circleIds) {
				returnResultentSet.add(circleIdsSolo.getCircleDetails().getCircleName());
			}
		}
		String commaSeperatedLocationsNameList = returnResultentSet.stream().collect(Collectors.joining(", "));
		return commaSeperatedLocationsNameList;
	}

	@GetMapping("/downloadFOFile")
	public ResponseEntity<Resource> downloadFOFile(@RequestParam("fileName") String fileName) throws IOException {
		try {
			logger.info("HeadQuater Controller.downloadFOFile().initiate" + fileName);
			String filesDirectory = fileUploadLocation;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			logger.info("CAG HeadQuater Controller.downloadFOFile().discontinue" + fileName);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			logger.error("CAG HeadQuater Controller.downloadFOFile().catch() : " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
