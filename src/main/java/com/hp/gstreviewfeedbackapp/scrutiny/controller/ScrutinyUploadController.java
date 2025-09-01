package com.hp.gstreviewfeedbackapp.scrutiny.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.ExcelData;
import com.hp.gstreviewfeedbackapp.model.HQUploadForm;
import com.hp.gstreviewfeedbackapp.model.MstNotifications;
import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;
import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CategoryListRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesStatusRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.impl.UploadScrutinyCasesServiceImpl;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;
import com.hp.gstreviewfeedbackapp.scrutiny.util.ExcelValidatorForUploadScrutinyCases;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.util.CasePertainUserNotification;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;

@Controller
@RequestMapping("/" + ApplicationConstants.SCRUTINY_HQ)
public class ScrutinyUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyUploadController.class);
	
	@Autowired
	private AppMenuRepository appMenuRepository;
	
	@Autowired
	private UploadScrutinyCasesServiceImpl uploadScrutinyCasesServiceImpl;
	
	@Autowired
	private CategoryListRepository categoryListRepository;
	
	@Autowired
	private ExcelValidatorForUploadScrutinyCases excelValidatorForUploadScrutinyCases;
	
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	
	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	
	@Autowired
	private CommonUtility commonUtility;
	
	@Autowired
	private CasePertainUserNotification casePertainUserNotification;
	
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private CustomUtility cutomUtility;

	@Autowired
	private MstParametersModuleWiseRepository mstParametersModuleWiseRepository;
	
	@Autowired
	private ScrutinyCasesStatusRepository scrutinyCasesStatusRepository;
	
	
	
	
	@Value("${file.upload.pdf.location}")
	private String pdfFilePath;

	String uploadDataFlag = null;
	
	
	@GetMapping("/"+ ApplicationConstants.DASHBOARD)
	public String dashboard(Model model) {
		logger.info("ScrutinyUploadController.dashboard() : ApplicationConstants.DASHBOARD");
		setScrutinyUploadMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.DASHBOARD;
	}
	
	@GetMapping("/"+ ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("ScrutinyUploadController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setScrutinyUploadMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
	}
	
	 @GetMapping("/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES) 
	 public String uploadReviewCases(Model model,@ModelAttribute("HqUploadForm") @Valid HQUploadForm HqUploadForm,BindingResult formResult,RedirectAttributes redirectAttributes) throws ParseException {
		 logger.info("ScrutinyUploadController.uploadReviewCases() : ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES");
		 
		 //uploadScrutinyCasesServiceImpl.uploadScrutinyCases(HqUploadForm,formResult,redirectAttributes);
		 model.addAttribute("categories", categoryListRepository.findByName("Scrutiny").getName());
		 setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES);  
		 return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
	  }
	 
	 @PostMapping("/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES) 
	 public String submitReviewCases(Model model,@ModelAttribute("HqUploadForm") @Valid HQUploadForm HqUploadForm,BindingResult formResult,RedirectAttributes redirectAttributes) throws ParseException, IOException {
		 logger.info("ScrutinyUploadController.uploadReviewCases() : ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES");
		 List<String> excelErrors = new ArrayList<>();
		 
		 model.addAttribute("categories", categoryListRepository.findByName("Scrutiny").getName());
		 setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES);  
		 
			redirectAttributes.addFlashAttribute("formResult", formResult);
			
//			setHQMenu(model, ApplicationConstants.HO_UPLOAD_REVIEW_CASES);//Set Menu list and active menu

			if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
					&& HqUploadForm.getExcelData().getExcelFile().isEmpty()) { 
				
				logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists and from has error");

				excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
				excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
					&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
				
				logger.error("Headquater : Unable to upload data as PDF does not exists and from has error");

				excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
					&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
				
				logger.error("Headquater : Unable to upload data as Excel file does not exists and from has error");

				excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (formResult.hasErrors() && !HqUploadForm.getExcelData().getExcelFile().isEmpty()
					&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
				logger.error("Headquater : Unable to upload data as uploaded from has error");

//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
					&& HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
				logger.error("Headquater : Unable to upload data as PDF and Excel file does not exists");

				excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
				excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (!formResult.hasErrors() && HqUploadForm.getPdfData().getPdfFile().isEmpty()
					&& !HqUploadForm.getExcelData().getExcelFile().isEmpty()) {
				logger.error("Headquater : Unable to upload data as PDF file does not exists");

				excelErrors.add(ApplicationConstants.PDF_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}

			if (!formResult.hasErrors() && HqUploadForm.getExcelData().getExcelFile().isEmpty()
					&& !HqUploadForm.getPdfData().getPdfFile().isEmpty()) {
				logger.error("Headquater : Unable to upload data as Excel file does not exists");

				excelErrors.add(ApplicationConstants.EXCEL_FILE_NOT_UPLOADED);
				HqUploadForm.setExcelErrors(excelErrors);
//				return "/hq/upload_review_cases";
				return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
			}
		 
		
			MultipartFile excelFile = null;
			MultipartFile pdfFile = null;

			PdfData pdfData = HqUploadForm.getPdfData();
			ExcelData excelData = HqUploadForm.getExcelData();
			Map<String, List<List<String>>> excelDataValidationMap = null;
			if (excelData != null) {
				excelFile = excelData.getExcelFile();
				if (excelFile != null && !excelFile.isEmpty()) {
//					excelErrors = excelValidator.validateExcel(excelFile);
					  
					//full validation
					excelDataValidationMap = excelValidatorForUploadScrutinyCases.validateExcelAndExtractDataForUploadScrutinyCases(excelFile);
					if(excelDataValidationMap.get("uploadData")!=null) {
						model.addAttribute("uploadData", excelDataValidationMap.get("uploadData"));
					}
					if(excelDataValidationMap.get("errorList")!=null) {
						//redirectAttributes.addFlashAttribute("errorList", excelDataValidationMap.get("errorList").get(0));
						model.addAttribute("errorList", excelDataValidationMap.get("errorList").get(0));
//						return "/hq/upload_review_cases";
						return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
					}
					if (!excelErrors.isEmpty() && excelErrors != null) {
						HqUploadForm.setExcelErrors(excelErrors);
//						return "/hq/upload_review_cases";
						return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
					}
				}
			}

			pdfFile = pdfData.getPdfFile();
			excelFile = excelData.getExcelFile();	
			
			if (pdfFile != null && !pdfFile.isEmpty() && excelFile != null && !excelFile.isEmpty() && excelDataValidationMap.get("uploadData")!=null && excelDataValidationMap.get("errorList")==null) {
				uploadDataFlag = uploadScrutinyCasesServiceImpl.uploadScrutinyCases(HqUploadForm,formResult,redirectAttributes,model,excelDataValidationMap.get("uploadData"));
			}	
		 
		 
		 
		 
		 if (uploadDataFlag !=null && uploadDataFlag.equalsIgnoreCase("Form submitted successfully!")) {
				// redirectAttributes.addFlashAttribute("successMessage", ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);
			 model.addAttribute("successMessage",ApplicationConstants.FORM_SUBMITTED_SUCCESSFULLY);

			} else if (uploadDataFlag.equalsIgnoreCase("Record already exists!")) {
				//redirectAttributes.addFlashAttribute("successMessage", "Record already exists!");
				model.addAttribute("successMessage","Record already exists!");
				
			} else {
				excelErrors.add(ApplicationConstants.DATA_STORE_PROCESS_NOT_COMPLETED);
				HqUploadForm.setExcelErrors(excelErrors);
				model.addAttribute("errorList",uploadDataFlag);
			}
		 return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_UPLOAD_CASES;
	  }
	 
	 
	 
	 @GetMapping("/"+ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER)
	 public String getRequestForTransferList(Model model,@RequestParam(required = false) String successMessage) {
		 logger.info("ScrutinyUploadController.getRequestForTransferList() : ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER");	

		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication instanceof AnonymousAuthenticationToken) {
			 return "redirect:/logout";
		 }
		 List<MstScrutinyCases> mstScrutinyCasesList = 	uploadScrutinyCasesServiceImpl.getRequestForTransferList();
		 model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);

		 Map<String, String> locatoinMap = adminUpdateUserDetailsImpl.getAllLocationMap();
		 if(locatoinMap!=null && locatoinMap.size()>0) {
			 JSONObject object = new JSONObject();
			 for (Map.Entry<String,String> entry : locatoinMap.entrySet()) {
				 if(!entry.getKey().equalsIgnoreCase("Z04") && !entry.getKey().equalsIgnoreCase("C81") && !entry.getKey().equalsIgnoreCase("HPT") 
						 && !entry.getKey().equalsIgnoreCase("DT14") && !entry.getKey().equalsIgnoreCase("EZ04")) {
					 object.put(entry.getKey(), entry.getValue());
				 }
			 }
			 model.addAttribute("locatoinMap", object);
		 }
		 model.addAttribute("successMessage",successMessage);
		 setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER);
		 return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER;
	 }
	
	 @PostMapping("/"+ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER)
	 public String approveRejectTransferRequest(Model model, @RequestParam(required = false) String gstIn, @RequestParam(required = false) String caseReportingDate, @RequestParam(required = false) String period, 
			 @RequestParam(required = false) String assignedTo, @RequestParam(required = false) String rejectRemark, RedirectAttributes redirectAttributes) throws ParseException
	 {
		 String successMessage="";
		 logger.info("ScrutinyUploadController.approveRejectTransferRequest() : ApplicationConstants.SCRUTINY_HQ_REQUEST_FOR_TRANSFER");
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 UserDetails userDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		 if (userDetails==null) {
			 return "redirect:/logout";
		 }

		 try {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 dateFormat.setLenient(false);
			 Date parsedDate = dateFormat.parse(caseReportingDate);
			 CompositeKey compositeKey = new CompositeKey();
			 compositeKey.setGSTIN(gstIn);
			 compositeKey.setCaseReportingDate(parsedDate);
			 compositeKey.setPeriod(period);

			 Optional<MstScrutinyCases> object = mstScrutinyCasesRepository.findById(compositeKey);
			 if(object.isPresent()) {
				 ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();
				 ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository.findById(compositeKey).get();

				 MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
				 mstScrutinyCases = object.get();
				 mstScrutinyCases.setAssignedFrom("SHQ");
				 mstScrutinyCases.setAssignedTo("SFO");
				 mstScrutinyCases.setAcknowlegeByFoOrNot(false);
				 scrutinyCaseWorkflow.setAssignedFromLocationId(mstScrutinyCases.getLocationDetails().getLocationId());
				 scrutinyCaseWorkflow.setAssignedToLocationId(assignedTo);

				 if(mstScrutinyCases.getLocationDetails().getLocationId().equals(assignedTo)) {
					 mstScrutinyCases.setAction("transferRequestRejected");
					 scrutinyCaseWorkflow.setOtherRemarks(rejectRemark);
					 scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
					 successMessage = "The transfer request has been rejected";
				 }
				 else {
					 mstScrutinyCases.setLocationDetails(locationDetailsRepository.findById(assignedTo).get());
					 mstScrutinyCases.setAction("transferRequestApproved");
					 scrutinyCasesPertainToUsers.setFoUserId(0);
					 scrutinyCasesPertainToUsers.setHqUserId(0);
					 scrutinyCasesPertainToUsers.setRuUserId(0);
					 scrutinyCaseWorkflow.setWorkingLocation(assignedTo);
					 successMessage = "The transfer request has been approved";
				 }

				 scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
				 scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
				 scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
				 scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
				 scrutinyCaseWorkflow.setAssignedFromUserId(userDetails.getUserId());
				 scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
				 scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
				 scrutinyCaseWorkflow.setUpdatingDate(new Date());
				 scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
				 scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
				 scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
				 scrutinyCaseWorkflow.setCategory(mstScrutinyCases.getCategory().getName());
				 scrutinyCaseWorkflow.setExtensionNo(mstScrutinyCases.getExtensionNo());


				 mstScrutinyCasesRepository.save(mstScrutinyCases); 
				 scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
				 scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			 }

		 }catch(Exception ex) {
			 logger.error("Network Issue : "+ex.getMessage());
			 return successMessage="Network Bandwidth Issue !";
		 }
		 return "redirect:/scrutiny_hq/"+ApplicationConstants.HO_REQUEST_FOR_TRANSFER + "?successMessage=" +successMessage;
	 }
	
	
	 @GetMapping("/"+ ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES)
		public String getRandomVerificationCasesList(Model model,@RequestParam(required = false) String message) {
			logger.info("ScrutinyFoController.getRandomVerificationCasesList() : ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
			List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyUpload");
			List<MstScrutinyCases> mstScrutinyCasesList = uploadScrutinyCasesServiceImpl.getRandomCasesListExceptClosureReport(workingLoacationList);
			model.addAttribute("mstScrutinyCasesList",mstScrutinyCasesList);
			model.addAttribute("message",message);
			setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES);
			return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES;
		}
	 
		@PostMapping("/"+ ApplicationConstants.SCRUTINY_HQ_RANDOM_RECOMMEND_FOR_SCRUTINY)
		public String randomRecommendForScrutiny(Model model, @RequestParam("recommendScrutinyGstin") String recommendScrutinyGstin,@RequestParam("recommendScrutinyPeriod") String recommendScrutinyPeriod,@RequestParam("recommendScrutinyCaseReportingDate") String recommendScrutinyCaseReportingDate,@RequestParam("recommendScrutinyRemark") String recommendScrutinyRemark) {
			logger.info("ScrutinyFoController.randomRecommendForScrutiny() : ApplicationConstants.SCRUTINY_HQ_RANDOM_RECOMMEND_FOR_SCRUTINY");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			String message =  uploadScrutinyCasesServiceImpl.randomRecommendForScrutiny(recommendScrutinyGstin,recommendScrutinyPeriod,recommendScrutinyCaseReportingDate,recommendScrutinyRemark);
			setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES);
			String url = "redirect:" + ApplicationConstants.SCRUTINY_HQ_RANDOM_VERIFICATION_CASES + "?message=" +message;
			return url;
		}
	 
	 
	
	
	// Endpoint to update the Excel file
	@PostMapping("/" + ApplicationConstants.SCRUTINY_HQ_UPDATE_EXCEL)
	public ResponseEntity<Void> updateExcel() {
	    Path filePath = Paths.get("src/main/resources/static/files/upload_scrutiny_cases.xlsx");
 
	    try (FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
	         XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

	        // Get the sheet by name
	        Sheet sheet = workbook.getSheet("Active Parameter(s) List");
	        if (sheet == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        // Clear existing data except for the first row
	        int numberOfRows = sheet.getLastRowNum();
	        for (int i = numberOfRows; i > 0; i--) {
	            Row row = sheet.getRow(i);
	            if (row != null) {
	                sheet.removeRow(row);
	            }
	        }

	        // Fetch data to update the sheet
	        List<MstParametersModuleWise> mstParametersModuleWiseList = mstParametersModuleWiseRepository.findByStatusScrutinyTrue();

	        Object[][] data = convertListToObjectArray(mstParametersModuleWiseList);

	        // Insert new data starting from the second row (to keep the first row unchanged)
	        int rowNum = 1; // Start from the second row
	        for (Object[] rowData : data) {
	            Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : rowData) {
	                Cell cell = row.createCell(colNum++);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                }
	            }
	        }

	        // Save changes to the same file
	        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
	            workbook.write(fileOutputStream);
	        }

	        return ResponseEntity.ok().build();

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	// Endpoint to download the Excel file
	@GetMapping("/" + ApplicationConstants.SCRUTINY_HQ_DOWNLOAD_UPDATED_EXCEL)
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) throws IOException {

	    Path filePath = Paths.get("src/main/resources/static/files/" + fileName);

	    try {
	        InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath.toFile()));

	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(filePath.toFile().length())
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(resource);

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST)
	public String getScrutinyReviewCasesList(Model model) {
		logger.info("ScrutinyUploadController.getScrutinyReviewCasesList() : ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST);
		model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder()); 
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST;
	}
	
	@GetMapping("/" + ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST +"/{id}")
	public String getScrutinyReviewCasesStatusWiseList(Model model,  @PathVariable Integer id) {
		logger.info("ScrutinyUploadController.getScrutinyReviewCasesStatusWiseList() : ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST/{id}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "redirect:/logout";
		}
		Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
		List<String> workingLoacationList = commonUtility.returnWorkingLocation(userId,"ScrutinyUpload");
		List<MstScrutinyCases> reviewCasesList = uploadScrutinyCasesServiceImpl.getReviewCasesListStatusWise(id,workingLoacationList);
		setScrutinyUploadMenu(model, ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_LIST);
		
		model.addAttribute("caseStatusList", scrutinyCasesStatusRepository.findAllWithOrder());
		model.addAttribute("reviewCasesList", reviewCasesList);
		return ApplicationConstants.SCRUTINY + "/" + ApplicationConstants.SCRUTINY_HQ + "/" + ApplicationConstants.SCRUTINY_HQ_REVIEW_CASES_STATUS_WISE;
	}
	
	private Object[][] convertListToObjectArray(List<MstParametersModuleWise> entityList) {
	    Object[][] data = new Object[entityList.size()][2]; // Assuming 2 columns: ID and Name

	    for (int i = 0; i < entityList.size(); i++) {
	        MstParametersModuleWise entity = entityList.get(i);
	        data[i][0] = i + 1; // Assuming this is the ID or serial number
	        data[i][1] = entity.getParamName(); // Assuming this is the name
	    }
	    return data;
	}


	private void setScrutinyUploadMenu(Model model, String activeMenu) {
		
		try {
			model.addAttribute("MenuList", appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true,ApplicationConstants.SCRUTINY_HQ));
			model.addAttribute("activeMenu",activeMenu);
			model.addAttribute("activeRole", ApplicationConstants.SCRUTINY_HQ);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();

					/*********** Get Notifications Start  *************/
					List<MstNotifications> loginedUserNotificationList = casePertainUserNotification.getNotificationPertainToUser(userDetails,"ScrutinyUpload");
					List<MstNotifications> unReadNotificationList = casePertainUserNotification.getUnReadNotificationPertainToUser(userDetails,"ScrutinyUpload");
					model.addAttribute("unReadNotificationListCount",unReadNotificationList.size());
					model.addAttribute("unReadNotificationList",unReadNotificationList);
					model.addAttribute("loginedUserNotificationList",loginedUserNotificationList); 
					model.addAttribute("convertUnreadToReadNotifications","/scrutiny_hq/convert_unread_to_read_notifications");
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
					model.addAttribute("commonUserDetails","/scrutiny_hq/user_details");
					model.addAttribute("changeUserPassword","/gu/change_password");
					model.addAttribute("homePageLink","/scrutiny_hq/dashboard");
					
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
