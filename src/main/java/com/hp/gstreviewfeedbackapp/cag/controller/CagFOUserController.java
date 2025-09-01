package com.hp.gstreviewfeedbackapp.cag.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.cag.model.CagCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.cag.model.CagCategory;
import com.hp.gstreviewfeedbackapp.cag.model.CagCompositeKey;
import com.hp.gstreviewfeedbackapp.cag.model.CagExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.cag.model.CagFoReviewCase;
import com.hp.gstreviewfeedbackapp.cag.model.CagHqReviewCase;
import com.hp.gstreviewfeedbackapp.cag.model.CagLinkedCases;
import com.hp.gstreviewfeedbackapp.cag.model.CagReason;
import com.hp.gstreviewfeedbackapp.cag.model.MstCagCases;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCasePertainToUserRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCaseRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagCategoryRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagFoReviewRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagHqReviewRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagLinkedCasesRepository;
import com.hp.gstreviewfeedbackapp.cag.repository.CagReasonRepository;
import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.controller.FieldUserController;
import com.hp.gstreviewfeedbackapp.data.CustomMultipartFile;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.TransferRemarks;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.ExtensionNoDocumentsRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.RemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.CAG_FO)
public class CagFOUserController {
	private static final Logger logger = LoggerFactory.getLogger(CagFOUserController.class);
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
	private FieldUserController fieldUserController;
	@Autowired
	private RemarksRepository reviewRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private CagCaseRepository cagCaseRepository;
	@Autowired
	private CagFoReviewRepository cagFoReviewRepository;
	@Autowired
	private CagCasePertainToUserRepository cagCasePertainToUserRepository;
	@Autowired
	private TransferRemarksRepository transferRemarksRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private CagHqReviewRepository cagHqReviewRepository;
	@Autowired
	private CagCategoryRepository cagCagCategoryRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private CagReasonRepository cagReasonRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private CagLinkedCasesRepository cagLinkedCasesRepository;
	@Autowired
	private CategoryListRepository categoryListRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	@Autowired
	private AuditExtensionNoDocumentRepository auditExtensionNoDocumentRepository;
	@Autowired
	private ExtensionNoDocumentsRepository extensionNoDocumentsRepository;
	@Autowired
	private ScrutinyExtensionNoDocumentRepository scrutinyExtensionNoDocumentRepository;
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
	@Value("${upload.directory}")
	private String uploadDirectory;
	@Value("${upload.audit.directory}")
	private String auditUploadDirectory;
	String uploadDataFlag = null;

	@GetMapping("/" + ApplicationConstants.CAG_FO_DASHBOARD)
	public String dashboard(Model model) {
		setCAGFOMenu(model, ApplicationConstants.CAG_FO_DASHBOARD);
		return "cag/fo/" + ApplicationConstants.CAG_FO_DASHBOARD;
	}

	@GetMapping("/" + ApplicationConstants.CAG_ACKNOWLEDGE_CASES)
	public String getAcknowledgeCases(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			setCAGFOMenu(model, ApplicationConstants.CAG_ACKNOWLEDGE_CASES);
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<TransferRemarks> transferRemarks = reviewRepository.findAllByOrderByIdDesc();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<String> locationId = new ArrayList<String>();
			List<String> actionList = new ArrayList<String>();
			actionList.add(actionUpload);
			actionList.add(hqtransfer);
			actionList.add(actionAcknowledge);
			List<UserRoleMapping> roleMapping = userRoleMappingRepository
					.findAllRolesMapWithCagFOUsers(userDetails.getUserId());
			for (UserRoleMapping userRoleMapping : roleMapping) {
				String location = null;
				List<String> list = new ArrayList<String>();
				list.add(userRoleMapping.getStateDetails().getStateId());
				list.add(userRoleMapping.getZoneDetails().getZoneId());
				list.add(userRoleMapping.getCircleDetails().getCircleId());
				location = fieldUserController.getLocation(list);
				locationId.add(location);
			}
			List<LocationDetails> circls = new ArrayList<>();
			List<LocationDetails> locationDetailsList = locationDetailsRepository.findAllByOrderByLocationNameAsc();
			for (LocationDetails ld : locationDetailsList) {
				if (!ld.getLocationId().equalsIgnoreCase("Z04") && !ld.getLocationId().equalsIgnoreCase("C81")
						&& !ld.getLocationId().equalsIgnoreCase("HPT") && !ld.getLocationId().equalsIgnoreCase("DT14")
						&& !ld.getLocationId().equalsIgnoreCase("EZ01") && !ld.getLocationId().equalsIgnoreCase("EZ02")
						&& !ld.getLocationId().equalsIgnoreCase("EZ03")
						&& !ld.getLocationId().equalsIgnoreCase("EZ04")) {
					circls.add(ld);
				}
			}
			List<MstCagCases> locationList = cagCaseRepository.findByLocationDetail(locationId, actionList);
			for (MstCagCases obj : locationList) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String remarks = "";
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				Date date = obj.getId().getCaseReportingDate();
				String circleId = obj.getLocationDetails().getLocationId();
				String parameter = obj.getId().getParameter();
				String pdfFileName = obj.getCagExtensionNoDocument().getExtensionFileName();
				try {
					Optional<CagHqReviewCase> hqCagCase = cagHqReviewRepository.findTransfterRemarks(
							obj.getId().getGSTIN(), obj.getId().getPeriod(), obj.getId().getCaseReportingDate(),
							obj.getId().getParameter());
					remarks = hqCagCase.isPresent() == true ? hqCagCase.get().getTransferRemarks() : "";
				} catch (Exception e) {
					e.printStackTrace();
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setDate(date);
				object.setActionStatus(obj.getAction().equals(actionAcknowledge) ? 1 : 0);
				object.setRemarks(remarks);
				object.setCaseId(circleId);
				object.setUploadedFileName(pdfFileName);
				object.setParameter(parameter);
				listofCases.add(object);
			}
			model.addAttribute("transferRemarks", transferRemarks);
			model.addAttribute("circls", circls);
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "cag/fo/" + ApplicationConstants.CAG_ACKNOWLEDGE_CASES;
	}

	@PostMapping("/acknowledge_cases")
	@ResponseBody
	public String acknowledgeCase(Model model, @ModelAttribute("acknowledgeForm") WorkFlowModel acknowledgeModel,
			RedirectAttributes redirectAttrs) {
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(acknowledgeModel.getDate());
			CagCompositeKey compositKey = new CagCompositeKey();
			compositKey.setGSTIN(acknowledgeModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(acknowledgeModel.getPeriod());
			compositKey.setParameter(acknowledgeModel.getParameter());
			MstCagCases enforcementReviewCase = cagCaseRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionAcknowledge);
			enforcementReviewCase.setAssignedFrom("cag_fo");
			enforcementReviewCase.setAssignedTo("cag_fo");
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setAssignedToUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			cagCaseRepository.save(enforcementReviewCase);
			CagCasesPertainToUsers cagCasesPertainToUsers = cagCasePertainToUserRepository.findById(compositKey).get();
			cagCasesPertainToUsers.setFoUserId(userId);
			cagCasePertainToUserRepository.save(cagCasesPertainToUsers);
			MstCagCases updatedReviewCase = cagCaseRepository.findById(compositKey).get();
			CagFoReviewCase foReviewCase = new CagFoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setParameter(compositKey.getParameter());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setAssignedTo("cag_fo");
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(userId);
			foReviewCase.setExtensionNoDocument(updatedReviewCase.getCagExtensionNoDocument());
			foReviewCase.setWorkingLocation(updatedReviewCase.getLocationDetails());
			foReviewCase.setAssignedFromUserId(userId);
			cagFoReviewRepository.save(foReviewCase);
			logger.info("Acknowledgement completed successfully");
			message = "Acknowledgement completed successfully";
		} catch (Exception e) {
			message = "There is some error....";
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return message;
	}

	@PostMapping("/transfer_cases")
	@ResponseBody
	public String trasferCase(Model model, @ModelAttribute WorkFlowModel transferModel,
			RedirectAttributes redirectAttrs) {
		String message = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			String fileName = null;
			String caseAssignTo = transferModel.getCaseAssignedTo();
			String reportingdate = transferModel.getDate();
			int reamrks = Integer.parseInt(transferModel.getRemarkOptions());
			SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(transferModel.getDate());
			String timestamp = timestampFormat.format(new Date());
			if (caseAssignTo.equals("NC")) {
				String orginalFileName = transferModel.getUploadedFile().getOriginalFilename();
				fileName = timestamp + "_" + orginalFileName;
				byte[] byteAry = transferModel.getUploadedFile().getBytes();
				File file = new File(fileUploadLocation + fileName);
				OutputStream outputStream = new FileOutputStream(file);
				outputStream.write(byteAry);
			}
			CagCompositeKey compositKey = new CagCompositeKey();
			compositKey.setGSTIN(transferModel.getGstno());
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(transferModel.getPeriod());
			compositKey.setParameter(transferModel.getParameter());
			MstCagCases enforcementReviewCase = cagCaseRepository.findById(compositKey).get();
			enforcementReviewCase.setAction(actionTransfer);
			enforcementReviewCase.setAssignedTo("cag_hq");
			enforcementReviewCase.setAssignedFrom("cag_fo");
			enforcementReviewCase.setAssignedFromUserId(userId);
			enforcementReviewCase.setCaseUpdateDate(new Date());
			enforcementReviewCase
					.setAssignedToUserId(cagCasePertainToUserRepository.findById(compositKey).get().getHqUserId());
			cagCaseRepository.save(enforcementReviewCase);
			MstCagCases updatedReviewCase = cagCaseRepository.findById(compositKey).get();
			CagFoReviewCase foReviewCase = new CagFoReviewCase();
			foReviewCase.setGSTIN(compositKey.getGSTIN());
			foReviewCase.setCaseReportingDate(compositKey.getCaseReportingDate());
			foReviewCase.setPeriod(compositKey.getPeriod());
			foReviewCase.setParameter(compositKey.getParameter());
			foReviewCase.setTaxpayerName(updatedReviewCase.getTaxpayerName());
			foReviewCase.setCircle(updatedReviewCase.getLocationDetails().getLocationName());
			foReviewCase.setCategory(updatedReviewCase.getCategory());
			foReviewCase.setIndicativeTaxValue(updatedReviewCase.getIndicativeTaxValue());
			foReviewCase.setAction(updatedReviewCase.getAction());
			foReviewCase.setAssignedTo("cag_hq");
			foReviewCase.setCaseUpdatingDate(new Date());
			foReviewCase.setUserId(userId);
			foReviewCase.setAssignedToUserId(updatedReviewCase.getAssignedToUserId());
			foReviewCase.setSuggestedJurisdiction(transferModel.getCaseAssignedTo());
			foReviewCase.setRemarks(
					transferRemarksRepository.findById(Integer.parseInt(transferModel.getRemarkOptions())).get());
			foReviewCase.setOtherRemarks(transferModel.getOtherRemarks());
			foReviewCase.setWorkingLocation(updatedReviewCase.getLocationDetails());
			foReviewCase.setExtensionNoDocument(updatedReviewCase.getCagExtensionNoDocument());
			foReviewCase.setAssignedFromUserId(updatedReviewCase.getAssignedFromUserId());
			foReviewCase.setTransferFilePath(fileName);
			cagFoReviewRepository.save(foReviewCase);
			logger.info("Transfer request completed successfully");
			message = "Transfer request completed successfully";
			/****** Check case already assign to specific user or not start *****/
			casePertainUserNotification.insertAssignNotification(enforcementReviewCase.getId().getGSTIN(),
					enforcementReviewCase.getId().getCaseReportingDate(), enforcementReviewCase.getId().getPeriod(),
					"CAG_HQ", enforcementReviewCase.getLocationDetails(), "transfer", 0);
			/****** Check case already assign to specific user or not End *****/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
			message = "There is some error....";
		}
		return message;
	}

	@GetMapping("/update_summary_list")
	public String getUpdateSummaryDataList(Model model) {
		setCAGFOMenu(model, ApplicationConstants.CAG_UPDATE_SUMMARY_LIST);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			List<EnforcementReviewCaseModel> listofCases = new ArrayList<EnforcementReviewCaseModel>();
			List<Integer> userIdList = new ArrayList<>();
			userIdList.add(userDetails.getUserId());
			List<MstCagCases> list = cagCaseRepository.findByCategoryAndActionAndUserIdList(actionAcknowledge,
					userIdList);
			for (MstCagCases obj : list) {
				EnforcementReviewCaseModel object = new EnforcementReviewCaseModel();
				String gst = obj.getId().getGSTIN();
				String caseReportingDate = obj.getId().getCaseReportingDate().toString().substring(0, 10).trim();
				String period = obj.getId().getPeriod();
				String circle = obj.getLocationDetails().getLocationName();
				String extensionNo = obj.getExtensionNo();
				String taxpayerName = obj.getTaxpayerName();
				Long indicativeTax = obj.getIndicativeTaxValue();
				String catecategory = obj.getCategory();
				String parameter = obj.getId().getParameter();
				String uploadedFileName = null;
				try {
					CagExtensionNoDocument extensionNoDocument = obj.getCagExtensionNoDocument();
					uploadedFileName = extensionNoDocument.getExtensionFileName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				object.setGSTIN_ID(gst);
				object.setCaseReportingDate_ID(caseReportingDate);
				object.setCategory(catecategory);
				object.setPeriod_ID(period);
				object.setCircle(circle);
				object.setExtensionNo(extensionNo);
				object.setTaxpayerName(taxpayerName);
				object.setIndicativeTaxValue(indicativeTax);
				object.setUploadedFileName(uploadedFileName);
				object.setParameter(parameter);
				object.setDate(obj.getId().getCaseReportingDate());
				object.setReason(obj.getActionStatus() != null ? "deactive" : "active");
				object.setCaseIdUpdated(isCaseClosed(obj));
				listofCases.add(object);
			}
			model.addAttribute("listofCases", listofCases);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "cag/fo/" + ApplicationConstants.CAG_UPDATE_SUMMARY_LIST;
	}

	@GetMapping("/view_cag_case/id")
	public String getCaseData(Model model, @RequestParam(name = "gst") String gst,
			@RequestParam(name = "date") String date, @RequestParam(name = "period") String period,
			@RequestParam(name = "parameter") String parameter) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			CagCompositeKey compositeKey = new CagCompositeKey();
			compositeKey.setGSTIN(gst);
			compositeKey.setCaseReportingDate(parsedDate);
			compositeKey.setPeriod(period);
			compositeKey.setParameter(parameter);
			MstCagCases mstCagCases = cagCaseRepository.findById(compositeKey).get();
			List<EnforcementReviewCaseModel> modulesCaselist = new ArrayList<EnforcementReviewCaseModel>();
			try {
				if (mstParametersRepository.existsByParamNameAndStatusAssessment(parameter, true)) {
					String assementParameterId = mstParametersRepository
							.findByParamNameAndStatusAssessment(parameter, true).get().getId() + "";
					List<EnforcementReviewCase> assementlist = enforcementReviewCaseRepository
							.findAssesmentCaseByGstinAndPeriodAndCategory(mstCagCases.getId().getGSTIN(),
									mstCagCases.getId().getPeriod(), assementParameterId);
					List<EnforcementReviewCaseModel> assement = assementlist.stream()
							.map(obj -> new EnforcementReviewCaseModel(obj.getId().getGSTIN(), obj.getId().getPeriod(),
									obj.getId().getCaseReportingDate(), obj.getCategory(), obj.getIndicativeTaxValue(),
									obj.getDemand(), obj.getCaseStage() != null ? obj.getCaseStage().getName() : "",
									obj.getRecoveryByDRC3(), obj.getRecoveryAgainstDemand(), obj.getCaseId(),
									"assessment",
									obj.getId().getCaseReportingDate().toString().substring(0, 10).trim()))
							.collect(Collectors.toList());
					modulesCaselist.addAll(assement);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (mstParametersRepository.existsByParamNameAndStatusScrutiny(parameter, true)) {
					String scrutinyParameterId = mstParametersRepository
							.findByParamNameAndStatusScrutiny(parameter, true).get().getId() + "";
					List<MstScrutinyCases> scrutinylist = mstScrutinyCasesRepository.findScrutinyCase(
							mstCagCases.getId().getGSTIN(), mstCagCases.getId().getPeriod(), scrutinyParameterId);
					List<EnforcementReviewCaseModel> scrutiny = scrutinylist.stream()
							.map(obj -> new EnforcementReviewCaseModel(obj.getId().getGSTIN(), obj.getId().getPeriod(),
									obj.getId().getCaseReportingDate(), obj.getParameters(),
									obj.getIndicativeTaxValue(), obj.getDemand(),
									obj.getCaseStage() != null ? obj.getCaseStage().getName() : "",
									obj.getRecoveryByDRC03(), obj.getRecoveryAgainstDemand(), obj.getCaseId(),
									"scrutiny", obj.getId().getCaseReportingDate().toString().substring(0, 10).trim()))
							.collect(Collectors.toList());
					modulesCaselist.addAll(scrutiny);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (mstParametersRepository.existsByParamNameAndStatusAudit(parameter, true)) {
					String auditParameterId = mstParametersRepository.findByParamNameAndStatusAudit(parameter, true)
							.get().getId() + "";
					List<AuditMaster> auditlist = auditMasterRepository.findAuditCase(mstCagCases.getId().getGSTIN(),
							mstCagCases.getId().getPeriod(), auditParameterId);
					List<EnforcementReviewCaseModel> audit = auditlist.stream()
							.map(obj -> new EnforcementReviewCaseModel(obj.getGSTIN(), obj.getPeriod(),
									obj.getCaseReportingDate(), obj.getParameter(), obj.getIndicativeTaxValue(), 0L, "",
									0L, 0L, obj.getCaseId(), "audit",
									obj.getCaseReportingDate().toString().substring(0, 10).trim()))
							.collect(Collectors.toList());
					modulesCaselist.addAll(audit);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("categories", cagCagCategoryRepository.findAll());
			model.addAttribute("reasons", cagReasonRepository.findAll());
			model.addAttribute("viewItem", mstCagCases);
			model.addAttribute("modulesCaselist", modulesCaselist);
			model.addAttribute("date", date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cag/fo/view_case";
	}

	@PostMapping("/update_cag_cases")
	@Transactional
	public String uploadCase(Model model, EnforcementReviewCaseModel caseUpdateModel, BindingResult br,
			RedirectAttributes redirectAttrs) {
		if (br.hasErrors()) {
			logger.error("error: " + br.getFieldError());
		}
		String orgFileName = null;
		String fileName = null;
		String timeStamp = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			int userId = userDetails.getUserId();
			String caseReporingDate = caseUpdateModel.getCaseReportingDate_ID();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			dateFmt.setLenient(false);
			Date parsedDate = dateFmt.parse(caseReporingDate);
			CagCompositeKey cagCompositKey = new CagCompositeKey();
			cagCompositKey.setGSTIN(caseUpdateModel.getRecovery());
			cagCompositKey.setCaseReportingDate(parsedDate);
			cagCompositKey.setPeriod(caseUpdateModel.getPeriod_ID());
			cagCompositKey.setParameter(caseUpdateModel.getParameter());
			MstCagCases mstCagCases = cagCaseRepository.findById(cagCompositKey).get();
			int categoryId = Integer.parseInt(caseUpdateModel.getCategoryListId());
			if (categoryId == 1) {
				List<String> list = caseUpdateModel.getCaseList();
				for (String element : list) {
					String[] ary = element.split("/");
					Date caseReportingDate = dateFmt.parse(ary[2]);
					CagLinkedCases cagLinkedCases = new CagLinkedCases();
					cagLinkedCases.setGSTIN(mstCagCases.getId().getGSTIN());
					cagLinkedCases.setCaseReportingDate(mstCagCases.getId().getCaseReportingDate());
					cagLinkedCases.setPeriod(mstCagCases.getId().getPeriod());
					cagLinkedCases.setParameter(mstCagCases.getId().getParameter());
					cagLinkedCases.setLinkedGSTIN(ary[0]);
					cagLinkedCases.setLinkedperiod(ary[1]);
					cagLinkedCases.setLinkedcaseReportingDate(caseReportingDate);
					cagLinkedCases.setLinkedCaseId(ary[3]);
					cagLinkedCases.setModule(ary[4]);
					cagLinkedCasesRepository.save(cagLinkedCases);
					mstCagCases.setActionStatus(cagCagCategoryRepository.findById(categoryId).get());
				}
			} else if (categoryId == 2) {
				String module = caseUpdateModel.getModule();
				if (module.equals("assessment")) {
					// Save PDF
					MultipartFile pdfFile = new CustomMultipartFile(caseUpdateModel.getUploadedFile().getBytes(),
							caseUpdateModel.getUploadedFile().getOriginalFilename(),
							caseUpdateModel.getUploadedFile().getContentType());
					LocalDateTime currentTimestamp = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
					String formattedTimestamp = currentTimestamp.format(formatter);
					String pdfFileName = "pdf_assessment_"
							+ mstCagCases.getExtensionNo().replace('\\', '.').replace('/', '.') + "_"
							+ formattedTimestamp + ".pdf";
					pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
					ExtensionNoDocument extensionNoDocument = new ExtensionNoDocument();
					extensionNoDocument.setExtensionFileName(pdfFileName);
					extensionNoDocument.setExtensionNo(mstCagCases.getExtensionNo());
					EnforcementReviewCase enforcementReviewCase = new EnforcementReviewCase();
					CompositeKey compositeKey = new CompositeKey();
					compositeKey.setGSTIN(mstCagCases.getId().getGSTIN());
					compositeKey.setPeriod(mstCagCases.getId().getPeriod());
					compositeKey.setCaseReportingDate(mstCagCases.getId().getCaseReportingDate());
					enforcementReviewCase.setId(compositeKey);
					enforcementReviewCase.setIndicativeTaxValue(mstCagCases.getIndicativeTaxValue());
					enforcementReviewCase.setLocationDetails(mstCagCases.getLocationDetails());
					enforcementReviewCase.setCategory(mstCagCases.getId().getParameter());
					enforcementReviewCase.setTaxpayerName(mstCagCases.getTaxpayerName());
					enforcementReviewCase.setAction("upload");
					enforcementReviewCase.setAssignedFrom("HQ");
					enforcementReviewCase.setAssignedTo("FO");
					enforcementReviewCase.setAssignedFromUserId(0);
					enforcementReviewCase.setAssignedToUserId(0);
					enforcementReviewCase.setCaseUpdateDate(new Date());
					enforcementReviewCase.setCategory(categoryListRepository
							.findByNameAndModuleAndActiveStatus("CAG Param", "cag", true).getName());
					enforcementReviewCase.setParameter(mstParametersRepository
							.findByParamNameAndStatusAssessment(mstCagCases.getId().getParameter(), true).get().getId()
							.toString());
					enforcementReviewCase.setExtensionNoDocument(extensionNoDocument);
					enforcementReviewCase.setExtensionNo(extensionNoDocument.getExtensionNo());
					extensionNoDocument.getEnforcementReviewCases().add(enforcementReviewCase);
//					enforcementReviewCaseRepository.save(enforcementReviewCase);
					extensionNoDocumentsRepository.save(extensionNoDocument);
					EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = new EnforcementReviewCaseAssignedUsers();
					enforcementReviewCaseAssignedUsers.setId(compositeKey);
					enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
					mstCagCases.setActionStatus(cagCagCategoryRepository.findById(categoryId).get());
					mstCagCases.setInitiatedModule(module);
				} else if (module.equals("scrutiny")) {
					// Save PDF
					MultipartFile pdfFile = new CustomMultipartFile(caseUpdateModel.getUploadedFile().getBytes(),
							caseUpdateModel.getUploadedFile().getOriginalFilename(),
							caseUpdateModel.getUploadedFile().getContentType());
					LocalDateTime currentTimestamp = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
					String formattedTimestamp = currentTimestamp.format(formatter);
					String pdfFileName = "pdf_scrutiny_"
							+ mstCagCases.getExtensionNo().replace('\\', '.').replace('/', '.') + "_"
							+ formattedTimestamp + ".pdf";
					pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
					// Create Scrutiny Extension Number Document
					ScrutinyExtensionNoDocument scrutinyExtensionNoDocument = new ScrutinyExtensionNoDocument();
					scrutinyExtensionNoDocument.setExtensionFileName(pdfFileName);
					scrutinyExtensionNoDocument.setExtensionNo(mstCagCases.getExtensionNo());
					MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
					CompositeKey compositeKey = new CompositeKey();
					compositeKey.setGSTIN(mstCagCases.getId().getGSTIN());
					compositeKey.setPeriod(mstCagCases.getId().getPeriod());
					compositeKey.setCaseReportingDate(mstCagCases.getId().getCaseReportingDate());
					mstScrutinyCases.setId(compositeKey);
					mstScrutinyCases.setAcknowlegeByFoOrNot(false);
					mstScrutinyCases.setAsmtTenIssuedOrNot(false);
					mstScrutinyCases.setCategory(categoryListRepository.findByName("CAG Param"));
					mstScrutinyCases.setTaxpayerName(mstCagCases.getTaxpayerName());
					mstScrutinyCases.setIndicativeTaxValue(mstCagCases.getIndicativeTaxValue());
					mstScrutinyCases.setLocationDetails(mstCagCases.getLocationDetails());
					mstScrutinyCases.setParameters(mstParametersRepository
							.findByParamNameAndStatusScrutiny(mstCagCases.getId().getParameter(), true).get().getId()
							.toString());
					mstScrutinyCases.setAction("uploadScrutinyCases");
					mstScrutinyCases.setAssignedFrom("SHQ");
					mstScrutinyCases.setAssignedTo("SFO");
					mstScrutinyCases.setCaseUpdateDate(new Date());
					mstScrutinyCases.setExtensionNo(scrutinyExtensionNoDocument.getExtensionNo());
					mstScrutinyCases.setScrutinyExtensionNoDocument(scrutinyExtensionNoDocument);
//					mstScrutinyCasesRepository.save(mstScrutinyCases);
					scrutinyExtensionNoDocument.getScrutinyCasesList().add(mstScrutinyCases);
					ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = new ScrutinyCasesPertainToUsers();
					// SaveScrutiny Extension Number Document
					scrutinyExtensionNoDocumentRepository.save(scrutinyExtensionNoDocument);
					scrutinyCasesPertainToUsers.setId(compositeKey);
					scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
					mstCagCases.setActionStatus(cagCagCategoryRepository.findById(categoryId).get());
					mstCagCases.setInitiatedModule(module);
				} else if (module.equals("audit")) {
					String caseId = caseUpdateModel.getCagCaseId();
					// Save PDF
					MultipartFile pdfFile = new CustomMultipartFile(caseUpdateModel.getUploadedFile().getBytes(),
							caseUpdateModel.getUploadedFile().getOriginalFilename(),
							caseUpdateModel.getUploadedFile().getContentType());
					LocalDateTime currentTimestamp = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
					String formattedTimestamp = currentTimestamp.format(formatter);
					String pdfFileName = "pdf_" + mstCagCases.getExtensionNo().replace('\\', '.').replace('/', '.')
							+ "_" + formattedTimestamp + ".pdf";
					pdfFile.transferTo(new File(auditUploadDirectory, pdfFileName));
//					MultipartFile pdfFile = caseUpdateModel.getUploadedFile();
//					File destination1 = new File(auditUploadDirectory + File.separator + pdfFileName);
//					pdfFile.transferTo(destination1);
					AuditExtensionNoDocument extensionNoDocument = new AuditExtensionNoDocument();
					List<AuditMasterCasesAllocatingUsers> allocatingUserList = new ArrayList<>();
					extensionNoDocument.setPdfFileName(pdfFileName);
					extensionNoDocument.setExtensionNo(mstCagCases.getExtensionNo());
					AuditMaster auditMaster = new AuditMaster();
					AuditCaseStatus auditCaseStatus = auditCaseStatusRepository.findById(1).get();
					auditMaster.setCaseId(caseId);
					auditMaster.setGSTIN(mstCagCases.getId().getGSTIN());
					auditMaster.setPeriod(mstCagCases.getId().getPeriod());
					auditMaster.setCaseReportingDate(mstCagCases.getId().getCaseReportingDate());
					auditMaster.setIndicativeTaxValue(mstCagCases.getIndicativeTaxValue());
					auditMaster.setLocationDetails(mstCagCases.getLocationDetails());
					auditMaster.setParameter(mstParametersRepository
							.findByParamNameAndStatusAudit(mstCagCases.getId().getParameter(), true).get().getId()
							.toString());
					auditMaster.setTaxpayerName(mstCagCases.getTaxpayerName());
					auditMaster.setCategory(categoryListRepository.findByName("CAG Param"));
					auditMaster.setAction(auditCaseStatus);
					auditMaster.setAssignedFrom("L1");
					auditMaster.setAssignTo("L2");
					auditMaster.setLastUpdatedTimeStamp(new Date());
					auditMaster.setExtensionNo(mstCagCases.getExtensionNo());
					auditMaster.setAuditExtensionNoDocument(extensionNoDocument);
//					auditMasterRepository.save(auditMaster);
					extensionNoDocument.getAuditMasterList().add(auditMaster);
					auditExtensionNoDocumentRepository.save(extensionNoDocument);
					AuditMasterCasesAllocatingUsers auditMasterCasesAllocatingUsers = new AuditMasterCasesAllocatingUsers();
					auditMasterCasesAllocatingUsers.setCaseId(caseId);
					auditMasterCasesAllocatingUsers.setL1User(0);
					auditMasterCasesAllocatingUsers.setL2User(0);
					auditMasterCasesAllocatingUsers.setL3User(0);
					auditMasterCasesAllocatingUsers.setMcmUser(0);
					auditMasterCasesAllocatingUsersRepository.save(auditMasterCasesAllocatingUsers);
					mstCagCases.setActionStatus(cagCagCategoryRepository.findById(categoryId).get());
					mstCagCases.setCaseId(caseId);
					mstCagCases.setInitiatedModule(module);
				}
			} else if (categoryId == 3) {
				int reason = Integer.parseInt(caseUpdateModel.getReason());
				CagReason cagReason = cagReasonRepository.findById(reason).get();
				CagCategory cagCategory = cagCagCategoryRepository.findById(categoryId).get();
				Long recoveryByDrc = caseUpdateModel.getRecoveryByDRC3();
				mstCagCases.setCagReason(cagReason);
				mstCagCases.setActionStatus(cagCategory);
				mstCagCases.setRecoveryByDRC03(recoveryByDrc);
			}
			orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
			timeStamp = dateFormat.format(new java.util.Date());
			fileName = timeStamp + "_" + orgFileName;
			byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
			File file = new File(fileUploadLocation + fileName);
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
			mstCagCases.setFilePath(fileName);
//			LocalDateTime currentTimestamp = LocalDateTime.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
//			String formattedTimestamp = currentTimestamp.format(formatter);
//			String pdfFileName = "pdf_" + mstCagCases.getExtensionNo().replace('\\', '.').replace('/', '.') + "_"
//					+ formattedTimestamp + ".pdf";
//
//			MultipartFile pdfFile = caseUpdateModel.getUploadedFile();
//			File destination2 = new File(fileUploadLocation + File.separator + pdfFileName);
//			pdfFile.transferTo(destination2);
//			mstCagCases.setFilePath(pdfFileName);
			mstCagCases.setRemark(caseUpdateModel.getRemarks());
			cagCaseRepository.save(mstCagCases);
			MstCagCases reviewCagCases = cagCaseRepository.findById(cagCompositKey).get();
			CagFoReviewCase cagFoReviewCase = new CagFoReviewCase();
			cagFoReviewCase.setGSTIN(reviewCagCases.getId().getGSTIN());
			cagFoReviewCase.setCategory(reviewCagCases.getCategory());
			cagFoReviewCase.setPeriod(reviewCagCases.getId().getPeriod());
			cagFoReviewCase.setCaseReportingDate(reviewCagCases.getId().getCaseReportingDate());
			cagFoReviewCase.setParameter(reviewCagCases.getId().getParameter());
			cagFoReviewCase.setAction(reviewCagCases.getAction());
			cagFoReviewCase.setActionStatus(reviewCagCases.getActionStatus());
			cagFoReviewCase.setCagReason(reviewCagCases.getCagReason());
			cagFoReviewCase.setCircle(reviewCagCases.getLocationDetails().getLocationName());
			cagFoReviewCase.setAssignedFromUserId(reviewCagCases.getAssignedFromUserId());
			cagFoReviewCase.setAssignedToUserId(reviewCagCases.getAssignedToUserId());
			cagFoReviewCase.setCaseUpdatingDate(reviewCagCases.getCaseUpdateDate());
			cagFoReviewCase.setExtensionNoDocument(reviewCagCases.getCagExtensionNoDocument());
			cagFoReviewCase.setIndicativeTaxValue(reviewCagCases.getIndicativeTaxValue());
			cagFoReviewCase.setRecoveryByDRC3(reviewCagCases.getRecoveryByDRC03());
			cagFoReviewCase.setRemark(reviewCagCases.getRemark());
			cagFoReviewCase.setWorkingLocation(reviewCagCases.getLocationDetails());
			cagFoReviewCase.setCaseId(reviewCagCases.getCaseId());
			cagFoReviewCase.setTaxpayerName(reviewCagCases.getTaxpayerName());
			cagFoReviewCase.setFilePath(reviewCagCases.getFilePath());
			cagFoReviewCase.setCagReason(reviewCagCases.getCagReason());
			cagFoReviewCase.setUserId(userId);
			cagFoReviewCase.setAssignedTo(reviewCagCases.getAssignedTo());
			cagFoReviewCase.setInitiatedModule(reviewCagCases.getInitiatedModule());
			cagFoReviewRepository.save(cagFoReviewCase);
			logger.info("case updated successfully");
			redirectAttrs.addFlashAttribute("message", "Case updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return "redirect:update_summary_list";
	}

	@GetMapping("/validate_parameter")
	@ResponseBody
	public String validateParameter(Model model, @RequestParam(name = "parameter") String parameter,
			@RequestParam(name = "module") String module) {
		Boolean isValid = false;
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			if (module.equals("assessment")) {
				isValid = mstParametersRepository.existsByParamNameAndStatusAssessment(parameter, true);
			} else if (module.equals("scrutiny")) {
				isValid = mstParametersRepository.existsByParamNameAndStatusScrutiny(parameter, true);
			} else if (module.equals("audit")) {
				isValid = mstParametersRepository.existsByParamNameAndStatusAudit(parameter, true);
			}
			if (!isValid) {
				message = parameter + " does not exist in " + module + " module";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return message;
	}

	@GetMapping("/validate_caseid")
	@ResponseBody
	public String validateCaseId(Model model, @RequestParam(name = "parameter") String parameter) {
		Boolean isValid = false;
		String message = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			isValid = auditMasterRepository.existsById(parameter);
			if (isValid) {
				message = parameter + " caseId already exist in audit module";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error :  " + e.getMessage());
		}
		return message;
	}

	@PostMapping("/close_cases")
	@Transactional
	public String closeCase(Model model, WorkFlowModel caseUpdateModel, RedirectAttributes redirectAttrs) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(caseUpdateModel.getDate());
			String timestamp = timestampFormat.format(new Date());
			String orgFileName = caseUpdateModel.getUploadedFile().getOriginalFilename();
			String fileName = timestamp + "_" + orgFileName;
			byte[] bytes = caseUpdateModel.getUploadedFile().getBytes();
			File file = new File(fileUploadLocation + fileName);
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
			CagCompositeKey cagCompositeKey = new CagCompositeKey();
			cagCompositeKey.setGSTIN(caseUpdateModel.getGstno());
			cagCompositeKey.setCaseReportingDate(parsedDate);
			cagCompositeKey.setPeriod(caseUpdateModel.getPeriod());
			cagCompositeKey.setParameter(caseUpdateModel.getParameter());
			MstCagCases mstCagCases = cagCaseRepository.findById(cagCompositeKey).get();
			mstCagCases.setCloseFilePath(fileName);
			mstCagCases.setCloseRemark(caseUpdateModel.getOtherRemarks());
			mstCagCases.setCaseUpdateDate(new Date());
			mstCagCases.setAction("closeCagCases");
			cagCaseRepository.save(mstCagCases);
			MstCagCases reviewCagCases = cagCaseRepository.findById(cagCompositeKey).get();
			CagFoReviewCase cagFoReviewCase = new CagFoReviewCase();
			cagFoReviewCase.setGSTIN(reviewCagCases.getId().getGSTIN());
			cagFoReviewCase.setCategory(reviewCagCases.getCategory());
			cagFoReviewCase.setPeriod(reviewCagCases.getId().getPeriod());
			cagFoReviewCase.setCaseReportingDate(reviewCagCases.getId().getCaseReportingDate());
			cagFoReviewCase.setParameter(reviewCagCases.getId().getParameter());
			cagFoReviewCase.setAction(reviewCagCases.getAction());
			cagFoReviewCase.setActionStatus(reviewCagCases.getActionStatus());
			cagFoReviewCase.setCagReason(reviewCagCases.getCagReason());
			cagFoReviewCase.setCircle(reviewCagCases.getLocationDetails().getLocationName());
			cagFoReviewCase.setAssignedFromUserId(reviewCagCases.getAssignedFromUserId());
			cagFoReviewCase.setAssignedToUserId(reviewCagCases.getAssignedToUserId());
			cagFoReviewCase.setCaseUpdatingDate(reviewCagCases.getCaseUpdateDate());
			cagFoReviewCase.setExtensionNoDocument(reviewCagCases.getCagExtensionNoDocument());
			cagFoReviewCase.setIndicativeTaxValue(reviewCagCases.getIndicativeTaxValue());
			cagFoReviewCase.setRecoveryByDRC3(reviewCagCases.getRecoveryByDRC03());
			cagFoReviewCase.setRemark(reviewCagCases.getRemark());
			cagFoReviewCase.setWorkingLocation(reviewCagCases.getLocationDetails());
			cagFoReviewCase.setCaseId(reviewCagCases.getCaseId());
			cagFoReviewCase.setTaxpayerName(reviewCagCases.getTaxpayerName());
			cagFoReviewCase.setFilePath(reviewCagCases.getFilePath());
			cagFoReviewCase.setCagReason(reviewCagCases.getCagReason());
			cagFoReviewCase.setUserId(userDetails.getUserId());
			cagFoReviewCase.setAssignedTo(reviewCagCases.getAssignedTo());
			cagFoReviewCase.setInitiatedModule(reviewCagCases.getInitiatedModule());
			cagFoReviewCase.setCloseFilePath(reviewCagCases.getCloseFilePath());
			cagFoReviewCase.setCloseRemark(reviewCagCases.getCloseRemark());
			cagFoReviewRepository.save(cagFoReviewCase);
			redirectAttrs.addFlashAttribute("message", "Case closed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:update_summary_list";
	}

	@GetMapping("/" + ApplicationConstants.CAG_FO_VIEW)
	public String view(Model model) {
		setCAGFOMenu(model, ApplicationConstants.CAG_FO_VIEW);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		List<UserRoleMapping> roleMappingList = userRoleMappingRepository
				.findLocationByRolebased(userDetails.getUserId(), 17);
		List<String> locationId = getAllMappedLocationsFromUserRoleMapping(roleMappingList);
		logger.info("location details : " + locationId.toString());
		List<Object[]> parameterList = new ArrayList<Object[]>();
		parameterList = cagCaseRepository.getCagParameterDetails(locationId);
		model.addAttribute("categoryTotals", parameterList);
		return "cag/fo/" + ApplicationConstants.CAG_FO_VIEW;
	}

	@GetMapping("/" + ApplicationConstants.CAG_VIEW_LIST_OF_CASE)
	public String viewlistOfCase(Model model, @RequestParam(required = false) String parameter) {
		try {
			setCAGFOMenu(model, ApplicationConstants.CAG_FO_VIEW);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<Object[]> cagReviewCase = new ArrayList<Object[]>();
			List<UserRoleMapping> roleMappingList = userRoleMappingRepository
					.findLocationByRolebased(userDetails.getUserId(), 17);
			List<String> locationId = getAllMappedLocationsFromUserRoleMapping(roleMappingList);
			cagReviewCase = cagCaseRepository.getCagCaseDetails(locationId, parameter);
			model.addAttribute("caseList", cagReviewCase);
			model.addAttribute("category", parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cag/fo/" + ApplicationConstants.CAG_VIEW_LIST_OF_CASE;
	}

	@GetMapping("/landingDrillDownCagCases")
	public String drillDownUser(Model model, @RequestParam(name = "gstin", required = false) String gstin,
			@RequestParam(name = "date", required = false) String date,
			@RequestParam(name = "period", required = false) String period,
			@RequestParam(name = "parameter", required = false) String parameter) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		try {
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<EnforcementReviewCaseModel> linkedCagCase = new ArrayList<EnforcementReviewCaseModel>();
			List<EnforcementReviewCaseModel> initiatedCagCase = new ArrayList<EnforcementReviewCaseModel>();
			List<EnforcementReviewCaseModel> notrequireCagCase = new ArrayList<EnforcementReviewCaseModel>();
			String action = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(date);
			CagCompositeKey cagCompositeKey = new CagCompositeKey();
			cagCompositeKey.setGSTIN(gstin);
			cagCompositeKey.setCaseReportingDate(parsedDate);
			cagCompositeKey.setPeriod(period);
			cagCompositeKey.setParameter(parameter);
			MstCagCases mstCagCases = cagCaseRepository.findById(cagCompositeKey).get();
			if (mstCagCases.getActionStatus().getId() == 3) {
				action = "notrequired";
				EnforcementReviewCaseModel enforcementReviewCaseModel = new EnforcementReviewCaseModel();
				enforcementReviewCaseModel.setGSTIN_ID(mstCagCases.getId().getGSTIN());
				enforcementReviewCaseModel.setDate(mstCagCases.getId().getCaseReportingDate());
				enforcementReviewCaseModel.setPeriod_ID(mstCagCases.getId().getPeriod());
				enforcementReviewCaseModel.setCircle(mstCagCases.getLocationDetails().getLocationName());
				enforcementReviewCaseModel.setCategory(mstCagCases.getCategory());
				enforcementReviewCaseModel.setAssignedFrom(mstCagCases.getAssignedFrom());
				enforcementReviewCaseModel.setAssignedTo(mstCagCases.getAssignedTo());
				enforcementReviewCaseModel.setIndicativeTaxVal(mstCagCases.getIndicativeTaxValue());
				enforcementReviewCaseModel.setRecoveryByDRC3(mstCagCases.getRecoveryByDRC03());
				enforcementReviewCaseModel.setTaxpayerName(mstCagCases.getTaxpayerName());
				enforcementReviewCaseModel.setUploadedFileName(mstCagCases.getFilePath());
				enforcementReviewCaseModel.setReason(mstCagCases.getCagReason().getName());
				enforcementReviewCaseModel.setRemarks(mstCagCases.getRemark());
				enforcementReviewCaseModel.setParameter(mstCagCases.getId().getParameter());
				enforcementReviewCaseModel.setExtensionNo(mstCagCases.getExtensionNo());
				notrequireCagCase.add(enforcementReviewCaseModel);
			} else if (mstCagCases.getActionStatus().getId() == 1) {
				action = "linked";
				List<CagLinkedCases> linkedCases = cagLinkedCasesRepository.findLinkedCaseList(
						mstCagCases.getId().getGSTIN(), mstCagCases.getId().getCaseReportingDate(),
						mstCagCases.getId().getPeriod(), mstCagCases.getId().getParameter());
				for (CagLinkedCases linkedCase : linkedCases) {
					List<Object[]> list = new ArrayList<Object[]>();
					EnforcementReviewCaseModel linkedmodel = new EnforcementReviewCaseModel();
					if (linkedCase.getModule().equals("assessment")) {
						list = cagCaseRepository.getAssessmentCaseDetails(linkedCase.getLinkedGSTIN(),
								linkedCase.getLinkedcaseReportingDate(), linkedCase.getLinkedperiod());
						linkedmodel.setGSTIN_ID(linkedCase.getLinkedGSTIN());
						linkedmodel.setDate(linkedCase.getLinkedcaseReportingDate());
						linkedmodel.setPeriod_ID(linkedCase.getLinkedperiod());
						linkedmodel.setCircle((String) list.get(0)[0]);
						linkedmodel.setCategory((String) list.get(0)[1]);
						linkedmodel.setCaseStageName((String) list.get(0)[2]);
						linkedmodel.setRecoveryStageName((String) list.get(0)[3]);
						linkedmodel.setAssignedFrom((String) list.get(0)[5]);
						linkedmodel.setAssignedTo((String) list.get(0)[6]);
						linkedmodel.setDemand(((BigInteger) list.get(0)[7]).longValue());
						linkedmodel.setIndicativeTaxVal(((BigInteger) list.get(0)[8]).longValue());
						linkedmodel.setRecoveryAgainstDemand(((BigInteger) list.get(0)[9]).longValue());
						linkedmodel.setRecoveryByDRC3(((BigInteger) list.get(0)[10]).longValue());
						linkedmodel.setTaxpayerName((String) list.get(0)[11]);
						linkedmodel.setCaseId((String) list.get(0)[12]);
						linkedmodel.setActionStatusName((String) list.get(0)[13]);
						linkedmodel.setModule(linkedCase.getModule());
					} else if (linkedCase.getModule().equals("scrutiny")) {
						list = cagCaseRepository.getScrutinyCaseDetails(linkedCase.getLinkedGSTIN(),
								linkedCase.getLinkedcaseReportingDate(), linkedCase.getLinkedperiod());
						linkedmodel.setGSTIN_ID(linkedCase.getLinkedGSTIN());
						linkedmodel.setDate(linkedCase.getLinkedcaseReportingDate());
						linkedmodel.setPeriod_ID(linkedCase.getLinkedperiod());
						linkedmodel.setCircle((String) list.get(0)[0]);
						linkedmodel.setCategory((String) list.get(0)[1]);
						linkedmodel.setCaseStageName((String) list.get(0)[2]);
						linkedmodel.setRecoveryStageName((String) list.get(0)[3]);
						linkedmodel.setAssignedFrom((String) list.get(0)[5]);
						linkedmodel.setAssignedTo((String) list.get(0)[6]);
						linkedmodel.setDemand(((BigInteger) list.get(0)[7]).longValue());
						linkedmodel.setIndicativeTaxVal(((BigInteger) list.get(0)[8]).longValue());
						linkedmodel.setRecoveryAgainstDemand(((BigInteger) list.get(0)[9]).longValue());
						linkedmodel.setRecoveryByDRC3(((BigInteger) list.get(0)[10]).longValue());
						linkedmodel.setTaxpayerName((String) list.get(0)[11]);
						linkedmodel.setCaseId((String) list.get(0)[12]);
						linkedmodel.setModule(linkedCase.getModule());
					} else if (linkedCase.getModule().equals("audit")) {
						list = cagCaseRepository.getAuditCaseDetails(linkedCase.getLinkedGSTIN(),
								linkedCase.getLinkedcaseReportingDate(), linkedCase.getLinkedperiod(),
								linkedCase.getLinkedCaseId());
						linkedmodel.setGSTIN_ID(linkedCase.getLinkedGSTIN());
						linkedmodel.setDate(linkedCase.getLinkedcaseReportingDate());
						linkedmodel.setPeriod_ID(linkedCase.getLinkedperiod());
						linkedmodel.setCaseId(linkedCase.getLinkedCaseId());
						linkedmodel.setCircle((String) list.get(0)[0]);
						linkedmodel.setCategory((String) list.get(0)[1]);
						linkedmodel.setAssignedFrom((String) list.get(0)[2]);
						linkedmodel.setAssignedTo((String) list.get(0)[3]);
						linkedmodel.setIndicativeTaxVal((((BigInteger) list.get(0)[4]).longValue()));
						linkedmodel.setTaxpayerName((String) list.get(0)[6]);
						linkedmodel.setCaseId((String) list.get(0)[7]);
						linkedmodel.setModule(linkedCase.getModule());
					}
					linkedCagCase.add(linkedmodel);
				}
			} else if (mstCagCases.getActionStatus().getId() == 2) {
				List<Object[]> initiatedCase = new ArrayList<Object[]>();
				EnforcementReviewCaseModel initiatedmodel = new EnforcementReviewCaseModel();
				action = "initiated";
				String module = mstCagCases.getInitiatedModule();
				if (module.equals("assessment")) {
					initiatedCase = cagCaseRepository.getAssessmentCaseDetails(gstin, parsedDate, period);
					initiatedmodel.setGSTIN_ID(gstin);
					initiatedmodel.setDate(parsedDate);
					initiatedmodel.setPeriod_ID(period);
					initiatedmodel.setCircle((String) initiatedCase.get(0)[0]);
					initiatedmodel.setCategory((String) initiatedCase.get(0)[1]);
					initiatedmodel.setCaseStageName((String) initiatedCase.get(0)[2]);
					initiatedmodel.setRecoveryStageName((String) initiatedCase.get(0)[3]);
					initiatedmodel.setAssignedFrom((String) initiatedCase.get(0)[5]);
					initiatedmodel.setAssignedTo((String) initiatedCase.get(0)[6]);
					initiatedmodel.setDemand((((BigInteger) initiatedCase.get(0)[7]).longValue()));
					initiatedmodel.setIndicativeTaxVal((((BigInteger) initiatedCase.get(0)[8]).longValue()));
					initiatedmodel.setRecoveryAgainstDemand((((BigInteger) initiatedCase.get(0)[9]).longValue()));
					initiatedmodel.setRecoveryByDRC3((((BigInteger) initiatedCase.get(0)[10]).longValue()));
					initiatedmodel.setTaxpayerName((String) initiatedCase.get(0)[11]);
					initiatedmodel.setCaseId((String) initiatedCase.get(0)[12]);
					initiatedmodel.setActionStatusName((String) initiatedCase.get(0)[13]);
					initiatedmodel.setModule(module);
				} else if (module.equals("scrutiny")) {
					initiatedCase = cagCaseRepository.getScrutinyCaseDetails(gstin, parsedDate, period);
					initiatedmodel.setGSTIN_ID(gstin);
					initiatedmodel.setDate(parsedDate);
					initiatedmodel.setPeriod_ID(period);
					initiatedmodel.setCircle((String) initiatedCase.get(0)[0]);
					initiatedmodel.setCategory((String) initiatedCase.get(0)[1]);
					initiatedmodel.setCaseStageName((String) initiatedCase.get(0)[2]);
					initiatedmodel.setRecoveryStageName((String) initiatedCase.get(0)[3]);
					initiatedmodel.setAssignedFrom((String) initiatedCase.get(0)[5]);
					initiatedmodel.setAssignedTo((String) initiatedCase.get(0)[6]);
					initiatedmodel.setDemand((((BigInteger) initiatedCase.get(0)[7]).longValue()));
					initiatedmodel.setIndicativeTaxVal((((BigInteger) initiatedCase.get(0)[8]).longValue()));
					initiatedmodel.setRecoveryAgainstDemand((((BigInteger) initiatedCase.get(0)[9]).longValue()));
					initiatedmodel.setRecoveryByDRC3((((BigInteger) initiatedCase.get(0)[10]).longValue()));
					initiatedmodel.setTaxpayerName((String) initiatedCase.get(0)[11]);
					initiatedmodel.setCaseId((String) initiatedCase.get(0)[12]);
					initiatedmodel.setActionStatusName((String) initiatedCase.get(0)[13]);
					initiatedmodel.setModule(module);
				} else if (module.equals("audit")) {
					initiatedCase = cagCaseRepository.getAuditCaseDetails(gstin, parsedDate, period,
							mstCagCases.getCaseId());
					initiatedmodel.setGSTIN_ID(gstin);
					initiatedmodel.setDate(parsedDate);
					initiatedmodel.setPeriod_ID(period);
					initiatedmodel.setCaseId(mstCagCases.getCaseId());
					initiatedmodel.setCircle((String) initiatedCase.get(0)[0]);
					initiatedmodel.setCategory((String) initiatedCase.get(0)[1]);
					initiatedmodel.setAssignedFrom((String) initiatedCase.get(0)[2]);
					initiatedmodel.setAssignedTo((String) initiatedCase.get(0)[3]);
					initiatedmodel.setIndicativeTaxVal((((BigInteger) initiatedCase.get(0)[4]).longValue()));
					initiatedmodel.setTaxpayerName((String) initiatedCase.get(0)[6]);
					initiatedmodel.setCaseId((String) initiatedCase.get(0)[7]);
					initiatedmodel.setModule(module);
				}
				initiatedCagCase.add(initiatedmodel);
			}
			model.addAttribute("notrequireCagCase", notrequireCagCase);
			model.addAttribute("linkedCagCase", linkedCagCase);
			model.addAttribute("initiatedCagCase", initiatedCagCase);
			model.addAttribute("action", action);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cag/fo/landingDrillDownCagCases";
	}

	@GetMapping("/downloadUploadedFile")
	public ResponseEntity<Resource> downloadUploadedFile(@RequestParam("fileName") String fileName) {
		try {
			String fileDirectoryPath = fileUploadLocation;
			Path Path = Paths.get(fileDirectoryPath, fileName);
			Resource resource = new FileSystemResource(Path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/downloadPdfUploadedFile")
	public ResponseEntity<Resource> downloadPdfUploadedFile(@RequestParam("fileName") String fileName) {
		try {
			String fileDirectoryPath = uploadDirectory;
			Path Path = Paths.get(fileDirectoryPath, fileName);
			Resource resource = new FileSystemResource(Path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/downloadUploadedPdfFile")
	public ResponseEntity<Resource> downloadUploadedPdfFile(@RequestParam("fileName") String fileName) {
		try {
			String fileDirectoryPath = pdfFilePath;
			Path Path = Paths.get(fileDirectoryPath, fileName);
			Resource resource = new FileSystemResource(Path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@SuppressWarnings("unused")
	private String isCaseClosed(MstCagCases obj) {
		String reason = "";
		CagCategory cagCategory = null;
		try {
			cagCategory = obj.getActionStatus();
			if (cagCategory != null) {
				int categoryId = cagCategory.getId();
				CompositeKey compositeKey = new CompositeKey();
				compositeKey.setGSTIN(obj.getId().getGSTIN());
				compositeKey.setPeriod(obj.getId().getPeriod());
				compositeKey.setCaseReportingDate(obj.getId().getCaseReportingDate());
				if (categoryId == 1) {
					List<CagLinkedCases> linkedCases = cagLinkedCasesRepository.findLinkedCaseList(
							obj.getId().getGSTIN(), obj.getId().getCaseReportingDate(), obj.getId().getPeriod(),
							obj.getId().getParameter());
					List<String> listStatus = new ArrayList<String>();
					for (CagLinkedCases cag : linkedCases) {
						String action = "";
						if (cag.getModule().equals("assessment")) {
							EnforcementReviewCase cargenforcementReviewCase = enforcementReviewCaseRepository
									.findById(compositeKey).get();
							action = valiateAssessmetCase(cargenforcementReviewCase);
						} else if (cag.getModule().equals("scrutiny")) {
							MstScrutinyCases cagmstScrutinyCases = mstScrutinyCasesRepository.findById(compositeKey)
									.get();
							action = valiateScrutinyCases(cagmstScrutinyCases);
						} else if (cag.getModule().equals("audit")) {
							AuditMaster cagauditMaster = auditMasterRepository.findById(cag.getLinkedCaseId()).get();
							action = valiateAuditCase(cagauditMaster);
						}
						listStatus.add(action);
					}
					if (listStatus.contains("deactive")) {
						reason = "deactive";
					} else {
						reason = "active";
					}
				} else if (categoryId == 2) {
					String module = obj.getInitiatedModule();
					if (module.equals("assessment")) {
						EnforcementReviewCase enforcementReviewCase = enforcementReviewCaseRepository
								.findById(compositeKey).get();
						reason = valiateAssessmetCase(enforcementReviewCase);
					} else if (module.equals("scrutiny")) {
						MstScrutinyCases mstScrutinyCases = mstScrutinyCasesRepository.findById(compositeKey).get();
						reason = valiateScrutinyCases(mstScrutinyCases);
					} else if (module.equals("audit")) {
						AuditMaster auditMaster = auditMasterRepository.findById(obj.getCaseId()).get();
						reason = valiateAuditCase(auditMaster);
					}
				} else if (categoryId == 3) {
					reason = "active";
				}
			} else {
				reason = "deactive";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reason;
	}

	private String valiateScrutinyCases(MstScrutinyCases mstScrutinyCases) {
		String reason = "";
		try {
			if (mstScrutinyCases.getAction().equals("noNeedScrutiny")) {
				reason = "active";
			} else if (mstScrutinyCases.getAction().equals("closureReportDropped")) {
				reason = "active";
			} else if (mstScrutinyCases.getAction().equals("recommendedForAssesAndAdjudication")) {
				reason = "active";
			} else {
				reason = "deactive";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reason;
	}

	private String valiateAssessmetCase(EnforcementReviewCase enforcementReviewCase) {
		String reason = "";
		try {
			if (enforcementReviewCase.getAction().equals("closed")) {
				reason = "active";
			} else {
				reason = "deactive";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reason;
	}

	private String valiateAuditCase(AuditMaster auditMaster) {
		String reason = "";
		try {
			int actionId = auditMaster.getAction().getId();
			if (actionId == 19) {
				reason = "active";
			} else {
				reason = "deactive";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reason;
	}

	private void setCAGFOMenu(Model model, String activeMenu) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				/*********** Get Notifications Start *************/
				List<MstNotifications> loginedUserNotificationList = casePertainUserNotification
						.getNotificationPertainToUser(userDetails, "CAG_FO");
				List<MstNotifications> unReadNotificationList = casePertainUserNotification
						.getUnReadNotificationPertainToUser(userDetails, "CAG_FO");
				model.addAttribute("unReadNotificationListCount", unReadNotificationList.size());
				model.addAttribute("unReadNotificationList", unReadNotificationList);
				model.addAttribute("loginedUserNotificationListCount", loginedUserNotificationList.size());
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
				model.addAttribute("activeRole", "CAG_FO");
				model.addAttribute("homePageLink", "/cag_fo/dashboard");
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
				appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.CAG_FO));
		model.addAttribute("activeMenu", activeMenu);
	}

	/**************** return locations name start ******************/
	public List<String> getAllMappedLocationsFromUserRoleMapping(List<UserRoleMapping> userRoleMapList) {
		List<String> workingLocationsIds = new ArrayList<>();
		try {
			List<UserRoleMapping> stateId = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
					.collect(Collectors.toList());
			if (stateId != null && stateId.size() > 0) {
				workingLocationsIds.addAll(enforcementReviewCaseRepository.finAllLocationsByStateid());
				return workingLocationsIds;
			} else {
				List<UserRoleMapping> zoneIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
						.collect(Collectors.toList());
				List<UserRoleMapping> circleIds = userRoleMapList.stream()
						.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())) // commented as per
																									// requirement
																									// verifier can not
																									// be assigned to at
																									// circle level.
						.collect(Collectors.toList());
				if (!circleIds.isEmpty()) {
					for (UserRoleMapping circleIdsSolo : circleIds) {
						workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
					}
				}
				if (!zoneIds.isEmpty()) {
					List<String> onlyZoneIdsList = new ArrayList<String>();
					for (UserRoleMapping zoneIdsSolo : zoneIds) {
						workingLocationsIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
						onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect
																						// district later
					}
					List<String> allCirclesListUnderZones = enforcementReviewCaseRepository
							.findAllCirclesByZoneIds(onlyZoneIdsList);
					for (String circleSolo : allCirclesListUnderZones) {
						workingLocationsIds.add(circleSolo); // add Circles
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdminUpdateUserDetailsImpl : getAllMappedLocationsFromUserRoleMapping : " + e.getMessage());
		}
		return workingLocationsIds;
	}

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
}
