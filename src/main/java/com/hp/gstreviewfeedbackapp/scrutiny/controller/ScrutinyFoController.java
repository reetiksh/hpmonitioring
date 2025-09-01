package com.hp.gstreviewfeedbackapp.scrutiny.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.data.SelfDetectedCase;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.WorkFlowModel;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyAsmtTenModel;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyInitiatedSidePanel;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyProceedingModel;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyRecoveryStage;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyTransferRemarks;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseUploadParametersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesStatusRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyExtensionNoDocumentRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyInitiatedSidePanelRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyRecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyTransferRemarksRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.UploadScrutinyCasesService;
import com.hp.gstreviewfeedbackapp.scrutiny.service.impl.ScrutinyFoServiceImpl;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;
import com.hp.gstreviewfeedbackapp.service.util.ExcelValidator;

@Controller
@RequestMapping("/" + ApplicationConstants.SCRUTINY_FO)
public class ScrutinyFoController {
	
private static final Logger logger = LoggerFactory.getLogger(ScrutinyFoController.class);
	
	@Autowired
	private AppMenuRepository appMenuRepository;
	
	@Autowired
	private ScrutinyFoServiceImpl scrutinyFoServiceImpl;
	
	@Autowired
	private CommonUtility commonUtility;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private ScrutinyRecoveryStageRepository scrutinyRecoveryStageRepository;
	
	@Autowired
	private ScrutinyInitiatedSidePanelRepository scrutinyInitiatedSidePanelRepository;
	
	@Autowired
	private ScrutinyCaseUploadParametersRepository scrutinyCaseUploadParametersRepository;
	
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	
	@Autowired
	private ScrutinyTransferRemarksRepository scrutinyTransferRemarksRepository;
	
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	
	@Autowired
	private CustomUtility cutomUtility;
	
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	
	@Autowired
	private ScrutinyCasesStatusRepository scrutinyCasesStatusRepository;
	
	@Autowired
	private ScrutinyExtensionNoDocumentRepository scrutinyExtensionNoDocumentRepository;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;
	
	@Autowired
	private ExcelValidator excelValidator;
	
	@Autowired
	private UploadScrutinyCasesService uploadScrutinyCasesService;
	
	
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;
	
	@GetMapping("/"+ ApplicationConstants.DASHBOARD)
	public String dashboard(Model model) {
		logger.info("ScrutinyFoController.dashboard() : ApplicationConstants.DASHBOARD");
		setScrutinyFoMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.DASHBOARD;
	}
	
	@GetMapping("/"+ ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("ScrutinyFoController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setScrutinyFoMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGE_TRANSFER_CASES)
	public String getAcknowledgeCases(Model model) {
		logger.info("ScrutinyFoController.dashboard() : ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGE_CASES");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Boolean remarksIsEmpty = true; 
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyFO");
		List<String> actionStatusList = Arrays.asList("uploadScrutinyCases","acknowledgeScrutinyCase","transferRequestApproved","transferRequestRejected");
		List<MstScrutinyCases> mstScrutinyCasesList =  scrutinyFoServiceImpl.getScrutinyCases(actionStatusList,workingLoacationList);
		
		for(MstScrutinyCases mstScrutinyCasesSolo :mstScrutinyCasesList ) {
			List<Integer> integerList = Arrays.stream(mstScrutinyCasesSolo.getParameters().split(","))
					 					.map(String::trim)          // Remove any surrounding whitespace from each string
		 								.map(Integer::parseInt)     // Convert each string to an integer
 										.collect(Collectors.toList());
			String concatValueOfParameters =  scrutinyCaseUploadParametersRepository.returnAllConcatParametersValues(integerList);
			mstScrutinyCasesSolo.setAllConcatParametersValue(concatValueOfParameters);
			List<String> scrutinyCaseWorkflowsList = scrutinyCaseWorkflowRepository.getScrutinyHqTranferRejectRemarks(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getPeriod(),mstScrutinyCasesSolo.getId().getCaseReportingDate());
			mstScrutinyCasesSolo.setHqRemarks(scrutinyCaseWorkflowsList)  ;
			
			if(remarksIsEmpty) {
				remarksIsEmpty = mstScrutinyCasesSolo.getHqRemarks().isEmpty() ? true : false;
			}
			
		}
		
		
		
		List<LocationDetails> circls = new ArrayList<>();
		List<LocationDetails> locationDetailsList = locationDetailsRepository.findAllByOrderByLocationNameAsc();
		for(LocationDetails ld : locationDetailsList) {
			if(!ld.getLocationId().equalsIgnoreCase("Z04") && !ld.getLocationId().equalsIgnoreCase("C81") && !ld.getLocationId().equalsIgnoreCase("HPT") 
						&& !ld.getLocationId().equalsIgnoreCase("DT14") && !ld.getLocationId().equalsIgnoreCase("EZ04")) {
				circls.add(ld);
			}
		}
		
		List<ScrutinyTransferRemarks> scrutinyTransferRemarksList = scrutinyTransferRemarksRepository.findAllByOrderByIdDesc();
		model.addAttribute("transferRemarks",scrutinyTransferRemarksList);
		model.addAttribute("circls", circls);
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGE_TRANSFER_CASES);
		model.addAttribute("remarksIsEmpty",remarksIsEmpty);   
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGE_TRANSFER_CASES;
	}
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY)
	public String updateTheCaseStatus(Model model,@RequestParam(required = false) String message) throws ParseException {
		logger.info("ScrutinyFoController.updateTheCaseStatus() : ApplicationConstants.SCRUTINY_FO_UPDATE_STATUS");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Boolean remarksIsEmpty = true;
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyFO");
		List<MstScrutinyCases> mstScrutinyCasesList = scrutinyFoServiceImpl.getInitiatedScrutinyCases(workingLoacationList);
		
		for(MstScrutinyCases mstScrutinyCasesSolo :mstScrutinyCasesList ) {
			List<Integer> integerList = Arrays.stream(mstScrutinyCasesSolo.getParameters().split(","))
					 					.map(String::trim)          // Remove any surrounding whitespace from each string
		 								.map(Integer::parseInt)     // Convert each string to an integer
 										.collect(Collectors.toList());
			String concatValueOfParameters =  scrutinyCaseUploadParametersRepository.returnAllConcatParametersValues(integerList);
			mstScrutinyCasesSolo.setAllConcatParametersValue(concatValueOfParameters);
			
			List<String> scrutinyCaseWorkflowsList = scrutinyCaseWorkflowRepository.getScrutinyVerifierRemarks(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getPeriod(),mstScrutinyCasesSolo.getId().getCaseReportingDate());
			mstScrutinyCasesSolo.setVerifierRemarks(scrutinyCaseWorkflowsList);
			
			if(remarksIsEmpty) {
				remarksIsEmpty = mstScrutinyCasesSolo.getVerifierRemarks().isEmpty() ? true : false;
			}
			
		}
		
		model.addAttribute("remarksIsEmpty",remarksIsEmpty);   
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		model.addAttribute("message",message);
		List<ScrutinyRecoveryStage> listRecovery = scrutinyRecoveryStageRepository.findAll();
		model.addAttribute("listRecovery",listRecovery);
		
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY;
	}
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGED_CASE)
	@ResponseBody
	public String acknowledgedCase(Model model, @ModelAttribute("acknowledgeForm") WorkFlowModel acknowledgeModel) {
		logger.info("ScrutinyFoController.acknowledgedCase() : ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGED_CASE");
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_ACKNOWLEDGED_CASE);
		String message =  scrutinyFoServiceImpl.acknowledgedCase(acknowledgeModel);
		return message;
	}
	
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_SCRUTINY_PROCEEDING_INITIATED_DROPPED)
	public String scrutinyProceedingDropped(Model model, @ModelAttribute("scrutinyDroppedData") ScrutinyProceedingModel scrutinyProceedingModel) {
		logger.info("ScrutinyFoController.scrutinyProceedingDropped() : ApplicationConstants.SCRUTINY_FO_SCRUTINY_PROCEEDING_INITIATED_DROPPED");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.dropScrutinyInitiatedProceeding(scrutinyProceedingModel);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY + "?message=" +message;
		return url;
	}
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_SCRUTINY_INITIATED)
	public String scrutinyInitiated(Model model, @RequestParam("gstin") String gstin,@RequestParam("reportingDate") String reportingDate,@RequestParam("period") String period ) {
		logger.info("ScrutinyFoController.scrutinyInitiated() : ApplicationConstants.SCRUTINY_FO_SCRUTINY_INITIATED");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		MstScrutinyCases mstScrutinyCases =  scrutinyFoServiceImpl.scrutinyProceedingInitiated(gstin,reportingDate,period);
		List<ScrutinyInitiatedSidePanel> ScrutinyInitiatedSidePanelList = scrutinyInitiatedSidePanelRepository.returnActiveMenuList();
		
		
		List<Integer> integerList = Arrays.stream(mstScrutinyCases.getParameters().split(","))
					.map(String::trim)          // Remove any surrounding whitespace from each string
					.map(Integer::parseInt)     // Convert each string to an integer
					.collect(Collectors.toList());
		String concatValueOfParameters =  scrutinyCaseUploadParametersRepository.returnAllConcatParametersValues(integerList);
		mstScrutinyCases.setAllConcatParametersValue(concatValueOfParameters);
		
		
		model.addAttribute("mstScrutinyCases",mstScrutinyCases);
		model.addAttribute("ScrutinyInitiatedSidePanelList",ScrutinyInitiatedSidePanelList);
		
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_SCRUTINY_INITIATED;
	}
	
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_LOAD_SIDE_PANEL_VIEW)
	public String activePannel(Model model, @RequestParam Integer caseId) {
		Optional<ScrutinyInitiatedSidePanel> scrutinyInitiatedSidePanel = scrutinyInitiatedSidePanelRepository.findById(caseId);
		if(scrutinyInitiatedSidePanel.isPresent()) {
			model.addAttribute("caseId", caseId);
			
			List<ScrutinyRecoveryStage> recoveryStageList = scrutinyRecoveryStageRepository.findAll();
			model.addAttribute("recoveryStageList",recoveryStageList);
			//model.addAttribute("activeStatusId", activeActionPannelId);
			return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + scrutinyInitiatedSidePanel.get().getJspPage();
		}
		
		return "redirect:/" + ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY;
	}
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_SUBMIT_ASMT_TEN)
	public String submitAsmtTen(Model model, @ModelAttribute("asmtTenForm") ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		logger.info("ScrutinyFoController.submitAsmtTen() : ApplicationConstants.SCRUTINY_FO_SUBMIT_ASMT_TEN");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.submitAsmtTen(scrutinyAsmtTenModel);
		model.addAttribute("scrutinyAsmtTenModel",scrutinyAsmtTenModel);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY + "?message=" +message;
		return url;
	}
	
	
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS)
	public String updateScrutinyStatus(Model model,@RequestParam(required = false) String message) {
		logger.info("ScrutinyFoController.updateScrutinyStatus() : ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyFO");
		List<MstScrutinyCases> mstScrutinyCasesList = scrutinyFoServiceImpl.getScrutinyUpdateStatusCases(workingLoacationList);
		
		for(MstScrutinyCases mstScrutinyCasesSolo :mstScrutinyCasesList ) {
			
			 List<Integer> integerList = Arrays.stream(mstScrutinyCasesSolo.getParameters().split(","))
					 					.map(String::trim)          // Remove any surrounding whitespace from each string
		 								.map(Integer::parseInt)     // Convert each string to an integer
										.collect(Collectors.toList());
			String concatValueOfParameters =  scrutinyCaseUploadParametersRepository.returnAllConcatParametersValues(integerList);
			mstScrutinyCasesSolo.setAllConcatParametersValue(concatValueOfParameters);
		}
		
		
		
		model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
		model.addAttribute("message",message);
		List<ScrutinyRecoveryStage> listRecovery = scrutinyRecoveryStageRepository.findAll();
		model.addAttribute("listRecovery",listRecovery);
		
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS;
	}
	
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_SCRUTINY_STATUS_UPDATED)
	public String scrutinyStatusUpdated(Model model, @RequestParam("gstin") String gstin,@RequestParam("reportingDate") String reportingDate,@RequestParam("period") String period ) {
		logger.info("ScrutinyFoController.scrutinyInitiated() : ApplicationConstants.SCRUTINY_FO_SCRUTINY_INITIATED");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		MstScrutinyCases mstScrutinyCases =  scrutinyFoServiceImpl.scrutinyProceedingInitiated(gstin,reportingDate,period);
		List<Integer> integerList = Arrays.stream(mstScrutinyCases.getParameters().split(","))
				.map(String::trim)          // Remove any surrounding whitespace from each string
				.map(Integer::parseInt)     // Convert each string to an integer
				.collect(Collectors.toList());
		String concatValueOfParameters =  scrutinyCaseUploadParametersRepository.returnAllConcatParametersValues(integerList);
		mstScrutinyCases.setAllConcatParametersValue(concatValueOfParameters);
		
		List<ScrutinyInitiatedSidePanel> ScrutinyInitiatedSidePanelList = scrutinyInitiatedSidePanelRepository.returnUpdateScrutinyStatusSidePannel();
		
		model.addAttribute("mstScrutinyCases",mstScrutinyCases);
		model.addAttribute("ScrutinyInitiatedSidePanelList",ScrutinyInitiatedSidePanelList);
		
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_SCRUTINY_STATUS_UPDATED;
	}
	
	
	@GetMapping("/"+ ApplicationConstants.SCRUTINY_FO_LOAD_UPDATE_SCRUTINY_SIDE_PANEL_VIEW)
	public String updateScrutinySidePannelView(Model model, @RequestParam("pageId") Integer pageId, @RequestParam String gstinStr,@RequestParam String reportingDateStr,@RequestParam String periodStr ) {
		logger.info("ScrutinyFoController.updateScrutinySidePannelView() : ApplicationConstants.SCRUTINY_FO_LOAD_UPDATE_SCRUTINY_SIDE_PANEL_VIEW");
		Optional<ScrutinyInitiatedSidePanel> scrutinyInitiatedSidePanel = scrutinyInitiatedSidePanelRepository.findById(pageId);
		if(scrutinyInitiatedSidePanel.isPresent()) {
			model.addAttribute("caseId", pageId);

			Instant instant = Instant.parse(reportingDateStr);
			// Convert Instant to LocalDateTime
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
			// Adjust to the desired time zone (e.g., IST - Indian Standard Time)
			ZoneId desiredZone = ZoneId.of("Asia/Kolkata"); // IST - Indian Standard Time
			LocalDateTime desiredDateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(desiredZone).toLocalDateTime();
			// Format LocalDateTime to desired format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			reportingDateStr = desiredDateTime.format(formatter);

			MstScrutinyCases mstScrutinyCases =  scrutinyFoServiceImpl.scrutinyProceedingInitiated(gstinStr,reportingDateStr,periodStr);
			model.addAttribute("mstScrutinyCases",mstScrutinyCases);
			model.addAttribute("amstTenFilePath",mstScrutinyCases.getFilePath());
			String[] stringArray = mstScrutinyCases.getRecoveryStageArn().split(",");
			// Convert array to list
			List<String> listOfArn = Arrays.asList(stringArray);
			model.addAttribute("listOfArn",listOfArn);
			List<ScrutinyRecoveryStage> recoveryStageList = scrutinyRecoveryStageRepository.findAll();
			model.addAttribute("recoveryStageList",recoveryStageList);
			//model.addAttribute("activeStatusId", activeActionPannelId);
			return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + scrutinyInitiatedSidePanel.get().getJspPage();
		}

		return "redirect:/" + ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_INITIATE_SCRUTINY;
	}
	
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_SUBMIT_CLOSURE_REPORT)
	public String submitClosureReport(Model model, @ModelAttribute("closureReportForm") ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		logger.info("ScrutinyFoController.submitClosureReport() : ApplicationConstants.SCRUTINY_FO_SUBMIT_CLOSURE_REPORT");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.submitClosureReport(scrutinyAsmtTenModel);
		model.addAttribute("scrutinyAsmtTenModel",scrutinyAsmtTenModel);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS + "?message=" +message;
		return url;
	}
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_RECOMMEND_ASSESMENT)
	public String recommendedForAssesmentAndAdjudication(Model model, @ModelAttribute("recommendedAssesmentForm") ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		logger.info("ScrutinyFoController.submitClosureReport() : ApplicationConstants.SCRUTINY_FO_SUBMIT_CLOSURE_REPORT");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.submitRecommendedForAssesment(scrutinyAsmtTenModel);
		model.addAttribute("scrutinyAsmtTenModel",scrutinyAsmtTenModel);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS + "?message=" +message;
		return url;
	}
	
	@PostMapping("/"+ ApplicationConstants.SCRUTINY_FO_RECOMMEND_AUDIT)
	public String recommendedToAudit(Model model, @ModelAttribute("recommendedAssesmentForm") ScrutinyAsmtTenModel scrutinyAsmtTenModel) {
		logger.info("ScrutinyFoController.recommendedToAudit() : ApplicationConstants.SCRUTINY_FO_RECOMMEND_AUDIT");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.submitAudit(scrutinyAsmtTenModel);
		model.addAttribute("scrutinyAsmtTenModel",scrutinyAsmtTenModel);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS);
		String url = "redirect:" + ApplicationConstants.SCRUTINY_FO_UPDATE_SCRUTINY_STATUS + "?message=" +message;
		return url;
	}
	
	@PostMapping("/" + ApplicationConstants.SCRUTINY_FO_REQUEST_FOR_TRANSFER)
	@ResponseBody
	public String requestForTransfer(Model model,@ModelAttribute WorkFlowModel transferModel) {
		logger.info("ScrutinyFoController.requestForTransfer() : ApplicationConstants.SCRUTINY_FO_REQUEST_FOR_TRANSFER");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		String message =  scrutinyFoServiceImpl.requestForTransfer(transferModel);
		
		return message;
	}
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST)
	public String getScrutinyReviewCasesList(Model model) {
		logger.info("ScrutinyFoController.getScrutinyReviewCasesList() : ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}

		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST);

		 model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder()); 

		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST;
	}
	
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST +"/{id}")
	public String getScrutinyReviewCasesStatusWiseList(Model model,  @PathVariable Integer id) {
		logger.info("ScrutinyFoController.getScrutinyReviewCasesStatusWiseList() : ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST/{id}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyFO");
		List<MstScrutinyCases> reviewCasesList = scrutinyFoServiceImpl.getReviewCasesListStatusWise(id,workingLoacationList);
		setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_LIST);
		
		model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder());
		model.addAttribute("reviewCasesList", reviewCasesList);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_REVIEW_CASES_STATUS_WISE;
	}
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES)
	public String scrutinySelfDetectedCasesForm(Model model) {
		try {
			setScrutinyFoMenu(model, ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			List<String> periods = new ArrayList<>();
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			LocalDate currentDate = LocalDate.now();
			// Check if the date is before April 1st
			if (currentDate.isBefore(LocalDate.of(currentDate.getYear(), 4, 1))) {
				year = year - 1;
			}
			periods.add("2017-18");
			for (int i = 2018; i <= year; i++) {
				periods.add(String.valueOf(i) + "-" + String.valueOf((i % 100) + 1));
			}
			model.addAttribute("periods", periods);
			UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
			List<String> jurisdictions = new ArrayList<String>();
			List<UserRoleMapping> roleMapping = userRoleMappingRepository.findAllRolesMapWithFOUsers(userDetails.getUserId());
			for (UserRoleMapping urm : roleMapping) {
				jurisdictions.add(userDetailsServiceImpl.getLocationNameFromUserRoleMapping(urm));
			}
			model.addAttribute("jurisdictions", jurisdictions);
			model.addAttribute("categories", "Self Detected Cases");
			model.addAttribute("uploadSelfDetectedCaseForm", new SelfDetectedCase());
			List<MstParametersModuleWise> remarks = mstParametersRepository.findAllByScrutinySelfDetectedCases(true);
			if (remarks != null && remarks.size() > 0) {
				model.addAttribute("remarks", remarks);
			}
		} catch (Exception e) {
			logger.error("ScrutinyFoController : scrutinySelfDetectedCasesForm : " + e.getMessage());
		}
		
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES;
	}
	
	@PostMapping("/" + ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES)
	public String uploadSelfDetectedCases(@ModelAttribute("selfDetectedCase") SelfDetectedCase selfDetectedCase,
			Model model, RedirectAttributes redirectAttributes) {
		Map<String, List<List<String>>> formDataValidationMap = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		formDataValidationMap = excelValidator.validateScrutinySelfDetectedCasesInputs(selfDetectedCase);
		if (formDataValidationMap.get("uploadData") != null) {
			redirectAttributes.addFlashAttribute("uploadData", formDataValidationMap.get("uploadData"));
		}
		if (formDataValidationMap.get("errorList") != null) {
			redirectAttributes.addFlashAttribute("errorList", formDataValidationMap.get("errorList").get(0));
			return "redirect:/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES;
		}
		String uploadDataFlag = "";
		if (!(formDataValidationMap.get("errorList") != null)) {
			uploadDataFlag = scrutinyFoServiceImpl.saveScrutinyHqUserUploadDataList(userDetails.getUserId(),
					formDataValidationMap.get("uploadData"), selfDetectedCase);
		}
		if (uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
			redirectAttributes.addFlashAttribute("successMessage", ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
		} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
			redirectAttributes.addFlashAttribute("successMessage", "Record already exists!");
		}
		return "redirect:/" + ApplicationConstants.SCRUTINY_FO + "/" + ApplicationConstants.SCRUTINY_FO_SELF_DETECTED_CASES;
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
	
	
	private void setScrutinyFoMenu(Model model, String activeMenu) { 
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,ApplicationConstants.SCRUTINY_FO));
			model.addAttribute("activeMenu",activeMenu);
			model.addAttribute("activeRole", ApplicationConstants.SCRUTINY_FO);
			
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();

					/*********** Get Notifications Start  *************/
					List<MstNotifications> loginedUserNotificationList = casePertainUserNotification.getNotificationPertainToUser(userDetails,"ScrutinyFO");
					List<MstNotifications> unReadNotificationList = casePertainUserNotification.getUnReadNotificationPertainToUser(userDetails,"ScrutinyFO");
					model.addAttribute("unReadNotificationListCount",unReadNotificationList.size());
					model.addAttribute("unReadNotificationList",unReadNotificationList);
					model.addAttribute("loginedUserNotificationList",loginedUserNotificationList); 
					model.addAttribute("convertUnreadToReadNotifications","/scrutiny_fo/convert_unread_to_read_notifications");
					/*********** Get Notifications End  *************/
					List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
							.findByUserDetailsOrderByUserRole(userDetails);
					
					/*************************** get user locations role wise start **************************/
						List<String> userRoleMapWithLocations = cutomUtility.roleMapWithLocations(userRoleMappings,userDetails);
						model.addAttribute("userRoleMapWithLocations", userRoleMapWithLocations);
					/*************************** get user locations role wise end **************************/

					// All Roles of the user
					Map<String, UserRole> userRoles = new HashMap<>();
					for (UserRoleMapping objectUrm : userRoleMappings) {
						if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
							userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
						}
					}
					model.addAttribute("UserLoginName", userDetails.getLoginName());
					model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
					model.addAttribute("commonUserDetails","/scrutiny_fo/user_details");
					model.addAttribute("changeUserPassword","/gu/change_password");
					model.addAttribute("homePageLink","/scrutiny_fo/dashboard");
					
					/************************* to display user generic details start ****************************/
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
						if ((userRoleSolo.getUserRole().getRoleName() != null) && (!distinctUserRoleIds.contains(userRoleSolo.getUserRole().getId()))) {
							distinctUserRoleIds.add(userRoleSolo.getUserRole().getId());
							uniqueRolesList.add(userRoleSolo);
						}
					}
					 List<String> roleNameList = uniqueRolesList.stream()
				                .map(mapping -> mapping.getUserRole().getRoleName())
				                .collect(Collectors.toList());
					  
					  String commaSeperatedRoleList = roleNameList.stream()
				                .collect(Collectors.joining(", "));
					
					  
					String LocationsName = returnLocationsName(userRoleMappings);
					userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
					userProfileDetails.setAssignedWorkingLocations(LocationsName);
					model.addAttribute("userProfileDetails", userProfileDetails);
					
					/************************* to display user generic details end ****************************/
				}
			}
			
			
			
		}catch(Exception ex) {
			logger.error("ScrutinyUploadController : setScrutinyUploadMenu() : "+ex.getMessage());
		}
	}
	
	
/**************** return locations name start ******************/
	
	private String returnLocationsName(List<UserRoleMapping> userRoleMapList) {
		List<String> returnResultentSet = new ArrayList<>();
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
				.collect(Collectors.toList());
		
		
		List<UserRoleMapping> zoneIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
				.collect(Collectors.toList());
		
		List<UserRoleMapping> circleIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))
				.collect(Collectors.toList());
		
		if(!stateId.isEmpty()){
			returnResultentSet.add(stateId.get(0).getStateDetails().getStateName());  
		}
		
		if(!zoneIds.isEmpty()){
			for(UserRoleMapping zoneIdsIdsSolo : zoneIds) {
				returnResultentSet.add(zoneIdsIdsSolo.getZoneDetails().getZoneName());  
			}
		}
		
		if(!circleIds.isEmpty()){
			for(UserRoleMapping circleIdsSolo : circleIds) {
				returnResultentSet.add(circleIdsSolo.getCircleDetails().getCircleName()); 
			}
		}
		
		 String commaSeperatedLocationsNameList = returnResultentSet.stream()
	                .collect(Collectors.joining(", "));
		
		return commaSeperatedLocationsNameList;
	}
	
	/**************** return locations name end ******************/
	

}
