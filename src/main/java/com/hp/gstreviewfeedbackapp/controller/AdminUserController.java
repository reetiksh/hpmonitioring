package com.hp.gstreviewfeedbackapp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hp.gstreviewfeedbackapp.constants.ApplicationConstants;
import com.hp.gstreviewfeedbackapp.model.AdminUserDetailsLogs;
import com.hp.gstreviewfeedbackapp.model.FileMaster;
import com.hp.gstreviewfeedbackapp.model.Form1;
import com.hp.gstreviewfeedbackapp.model.SMSTemplate;
import com.hp.gstreviewfeedbackapp.model.SentSmsDetails;
import com.hp.gstreviewfeedbackapp.model.TransferRole;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserProfileDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.model.UserStatus;
import com.hp.gstreviewfeedbackapp.repository.AdminUpdateUserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.AdminUserDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.AppMenuRepository;
import com.hp.gstreviewfeedbackapp.repository.CenterDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.CircleDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.DesignationRepository;
import com.hp.gstreviewfeedbackapp.repository.FileMasterRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.SMSTemplateRepository;
import com.hp.gstreviewfeedbackapp.repository.SendSmsDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.StateDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.TransferRoleRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.repository.UserStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.ZoneDetailsRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUserService;
import com.hp.gstreviewfeedbackapp.service.SMSService;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUpdateUserDetailsImpl;
import com.hp.gstreviewfeedbackapp.service.impl.AdminUserServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.SMSServiceImpl;
import com.hp.gstreviewfeedbackapp.service.impl.UserDetailsServiceImpl;
import com.hp.gstreviewfeedbackapp.service.util.CustomUtility;
import com.hp.gstreviewfeedbackapp.service.util.DeemedApprovedCases;

@Controller
@RequestMapping("/" + ApplicationConstants.ADMIN)
public class AdminUserController {
	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
	@Autowired
	private DeemedApprovedCases deemedApprovedCases;
	@Autowired
	private AppMenuRepository appMenuRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminUserDetailsLogsRepository adminUserDetailsLogsRepository;
	@Autowired
	private AdminUpdateUserDetailsImpl adminUpdateUserDetailsImpl;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AdminUpdateUserDetailsRepository adminUpdateUserDetailsRepository;
	@Autowired
	private CircleDetailsRepository circleDetailsRepository;
	@Autowired
	private ZoneDetailsRepository zoneDetailsRepository;
	@Autowired
	private StateDetailsRepository stateDetailsRepository;
	@Autowired
	private CenterDetailsRepository centreDetailsRepository;
	@Autowired
	private DesignationRepository designationRepository;
	@Autowired
	private UserStatusRepository userStatusRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRoleRepository useRepository;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private TransferRoleRepository transferRoleRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private AdminUserServiceImpl adminUserServiceImpl;
	@Autowired
	private SMSTemplateRepository smsTemplateRepository;
	@Autowired
	private SendSmsDetailsRepository sendSmsDetailsRepository;
	@Autowired
	private CustomUtility cutomUtility;
	@Autowired
	private FileMasterRepository fileMasterRepo;
	@Autowired
	private SMSService smsService;
	@Autowired
	private SMSServiceImpl smsServiceImpl;
	@Value("${login.NewUser}")
	private String newUserStatus;
	@Value("${login.smsServiceActiveOrNot}")
	private Boolean smsServiceActiveOrNot;
	@Value("${application.url}")
	private String applicationUrl;
	@Value("${file.upload.location}")
	private String fileUploadLocation;
	@Value("${doc.upload.pdf.location}")
	private String fileUploadDir;

	@GetMapping("/" + ApplicationConstants.DASHBOARD)
	public String index(Model model) {
		setAdminMenu(model, ApplicationConstants.DASHBOARD);
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.DASHBOARD;
	}

	// Create New User Page
	@GetMapping("/" + ApplicationConstants.ADMIN_CREATE_USER)
	public String createUser(Model model) {
		setAdminMenu(model, ApplicationConstants.ADMIN_CREATE_USER);
		model.addAttribute("designationList", designationRepository.findAll());
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
	}

	// Create New User Post Mapping
	@PostMapping("/" + ApplicationConstants.ADMIN_CREATE_USER)
	public String createNewUser(@ModelAttribute("userDetails") UserDetails userDetails,
			@RequestParam(required = true) String dob, @RequestParam(required = true) int designation, Model model)
			throws ParseException {
		setAdminMenu(model, ApplicationConstants.ADMIN_CREATE_USER); // Set menu list
		model.addAttribute("designationList", designationRepository.findAll());
		AdminUserDetailsLogs adminUserDetailsLogsSolo = new AdminUserDetailsLogs();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (loginUserDetails == null) {
			return "redirect:/logout";
		}
		// checking for unique login name
		if (checkNewLoginId(userDetails.getLoginName()).equalsIgnoreCase("true")) {
			model.addAttribute("errorMessage", "Duplicate user Login Id !");
			logger.warn("Duplicate user Login Id : " + userDetails.getLoginName());
			return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
		}
		// checking for unique mobile
		if (checkNewMobile(userDetails.getMobileNumber(), null).equalsIgnoreCase("true")) {
			model.addAttribute("errorMessage", "Duplicate user mobile number !");
			logger.warn("Duplicate user mobile number : " + userDetails.getMobileNumber());
			return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
		}
		// checking for unique email id
		if (checkNewEmailId(userDetails.getEmailId(), null).equalsIgnoreCase("true")) {
			model.addAttribute("errorMessage", "Duplicate email Id !");
			logger.warn("Duplicate email Id : " + userDetails.getEmailId());
			return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
		}
		userDetails.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
		userDetails.setDesignation(designationRepository.findById(designation).get());
		// setting default password
		String uuid = UUID.randomUUID().toString();
		Integer indexOfHyphen = uuid.indexOf("-");
		String randomPassword = uuid.substring(0, indexOfHyphen);
		System.out.println("randomPassword : " + randomPassword);
		userDetails.setPassword(passwordEncoder.encode(randomPassword));
		userDetails.setUserStatus(newUserStatus);
		userDetails.setWrongPasswordAttempt(0);
		userDetails = userDetailsRepository.save(userDetails); // Save user
		if (userDetails != null) {
			logger.info("Create User with Login Id : " + userDetails.getLoginName());
			/********* Save User Log Start ********/
			adminUserDetailsLogsSolo.setLoginId(userDetails.getLoginName());
			adminUserDetailsLogsSolo.setMobileNumber(userDetails.getMobileNumber());
			adminUserDetailsLogsSolo.setFirstName(userDetails.getFirstName());
			adminUserDetailsLogsSolo.setMiddleName(userDetails.getMiddleName());
			adminUserDetailsLogsSolo.setLastName(userDetails.getLastName());
			adminUserDetailsLogsSolo.setEmailId(userDetails.getEmailId());
			adminUserDetailsLogsSolo.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
			adminUserDetailsLogsSolo.setDesignation(userDetails.getDesignation());
			adminUserDetailsLogsSolo.setUserStatus(userDetails.getUserStatus());
			adminUserDetailsLogsSolo.setUserStatus(userDetails.getUserStatus());
			adminUserDetailsLogsSolo.setUpdatingTime(new Date());
			adminUserDetailsLogsSolo.setUserDetails(userDetails);
			adminUserDetailsLogsSolo.setAdminUserDetails(loginUserDetails);
			adminUserDetailsLogsSolo.setAction("New user created");
			adminUserDetailsLogsRepository.save(adminUserDetailsLogsSolo);
			/********* Save User Log End ********/
			/******* Send Sms Start ********/
			if (smsServiceActiveOrNot) {
				SentSmsDetails sentSmsDetails = new SentSmsDetails();
				String searchString = "MsgID =";
				SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330753608218");
				String smsMsgBody = smsTemplate.getTemplate();
				smsMsgBody = smsMsgBody.replace("{#name#}", userDetails.getFirstName());
				smsMsgBody = smsMsgBody.replace("{#loginid#}", userDetails.getLoginName());
				smsMsgBody = smsMsgBody.replace("{#password#}", randomPassword);
				smsMsgBody = smsMsgBody.replace("{#url#}", applicationUrl);
				String Response = smsService.sendSMS("1107170330753608218", smsMsgBody, userDetails.getMobileNumber());
				String resultentMsgId = smsServiceImpl.extractStringAfter(Response, searchString);
				if (resultentMsgId != null) {
					sentSmsDetails.setMsgId(resultentMsgId);
				} else {
					sentSmsDetails.setMsgId("Msg Id Not Present !");
				}
				sentSmsDetails.setMsgBody(smsMsgBody);
				sentSmsDetails.setResponse(Response);
				sentSmsDetails.setSentTime(new Date());
				sentSmsDetails.setSentTo(userDetails.getMobileNumber());
				sentSmsDetails.setTemplateId("1107170330753608218");
				sendSmsDetailsRepository.save(sentSmsDetails);
			}
			/******* Send Sms End ********/
			return "redirect:/admin/user_creation_successful";
		} else {
			logger.warn("Unable to create User");
		}
		model.addAttribute("errorMessage", "Unable to create user !");
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
	}

	@GetMapping("/upload_documents")
	public String uploadDocs(Model model, @RequestParam(required = false) String category) {
		setAdminMenu(model, "upload_documents");
		model.addAttribute("categoryList", fileMasterRepo.findDistinctCategoryList());
		List<String> yearList = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017");
		model.addAttribute("yearList", yearList);
		if (category != null) {
			model.addAttribute("typeList", fileMasterRepo.findDistinctTypeList(category));
		}
		return ApplicationConstants.ADMIN + "/upload_documents";
	}

	@PostMapping("/upload_documents")
	public String uploadDocs(@ModelAttribute("uploadDocDetails") FileMaster fileMaster,
			@RequestParam(required = false) String category, @RequestParam(required = false) String year,
			@RequestParam(required = false) String type, Model model, @RequestParam("file") MultipartFile file) {
		// Set admin menu and add attributes to the model
		setAdminMenu(model, "upload_documents");
		model.addAttribute("categoryList", fileMasterRepo.findDistinctCategoryList());
		if (category != null) {
			model.addAttribute("typeList", fileMasterRepo.findDistinctTypeList(category));
		}
		List<String> yearList = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017");
		model.addAttribute("yearList", yearList);
		// Ensure user is authenticated
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName())
				.orElse(null);
		if (loginUserDetails == null) {
			return "redirect:/logout";
		}
		if (category != null && file != null && year != null && type != null && category.length() > 0 && !file.isEmpty()
				&& year.length() > 0 && type.length() > 0) {
			// Construct upload directory path based on category and year
			String uploadDir = Paths.get(fileUploadDir, "GST " + category + "/" + year).toString();
			// Create directory if it does not exist
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				boolean created = directory.mkdirs(); // Creates all necessary directories
				if (!created) {
					model.addAttribute("errorMessage", "Failed to create upload directory.");
					return "redirect:/uploadError";
				}
			}
			// Save file to the upload directory
			try {
				List<FileMaster> existingFileMasterList = fileMasterRepo
						.findByCategoryAndYearAndTypeAndFileNameAndIsDeleted(category, year, type,
								file.getOriginalFilename(), "N");
//				for (FileMaster existingFileMaster : existingFileMasterList) {
				// Delete the old file from the directory
//					String oldFilePath = Paths
//							.get(fileUploadDir, "GST " + category + "/" + year, existingFileMaster.getFileName())
//							.toString();
//					File oldFile = new File(oldFilePath);
//					if (oldFile.exists()) {
//						oldFile.delete();
//					}
				// Update the existing record with new file details
//					existingFileMaster.setFileName(file.getOriginalFilename());
//					existingFileMaster.setUploadDate(new Date()); // Set current date
//
//					byte[] bytes = file.getBytes();
//					Path newPath = Paths.get(uploadDir, file.getOriginalFilename());
//					Files.write(newPath, bytes);
//					fileMasterRepo.save(existingFileMaster);
//					model.addAttribute("successMessage", "File uploaded successfully.");
//				}
				if (existingFileMasterList.isEmpty()) {
					byte[] bytes = file.getBytes();
					Path path = Paths.get(uploadDir, file.getOriginalFilename());
					Files.write(path, bytes);
					fileMaster.setCategory(category);
					fileMaster.setFileName(file.getOriginalFilename());
					fileMaster.setType(type);
					fileMaster.setLevel("GST");
					fileMaster.setYear(year);
					fileMaster.setIsDeleted("N");
					fileMaster.setUploadDate(new Date()); // Set current date
					fileMasterRepo.save(fileMaster);
					model.addAttribute("successMessage", "File uploaded successfully.");
				} else {
					model.addAttribute("errorMessage",
							"Same File Record Already Exist! Change The File Name & Try Again");
				}
			} catch (IOException e) {
				e.printStackTrace();
				// Handle file upload error
				model.addAttribute("errorMessage", "Failed to upload file.");
				return "redirect:/uploadError";
			}
		}
		model.addAttribute("category", category);
		model.addAttribute("year", year);
		model.addAttribute("type", type);
		return ApplicationConstants.ADMIN + "/upload_documents";
	}

	@GetMapping("/manage_documents")
	public String manageDocs(Model model, @RequestParam(required = false) String category,
			@RequestParam(required = false) String year) {
		setAdminMenu(model, "manage_documents");
		model.addAttribute("categoryList", fileMasterRepo.findDistinctCategoryList());
		List<String> yearList = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017");
		model.addAttribute("yearList", yearList);
		model.addAttribute("category", category);
		model.addAttribute("year", year);
		return ApplicationConstants.ADMIN + "/manage_documents";
	}

	@PostMapping("/manage_documents")
	public String manageDocsPost(@RequestParam(name = "type", required = false) String category,
			@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "financialyearNoti", required = false) String financialyearNoti, Model model) {
		setAdminMenu(model, "manage_documents");
		model.addAttribute("categoryList", fileMasterRepo.findDistinctCategoryList());
		List<String> yearList = Arrays.asList("2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017");
		model.addAttribute("yearList", yearList);
		// Fetch files from directories based on category1 and financialyearNoti
		if (financialyearNoti != null && category != null) {
			List<FileMaster> fileNamesFromDB = fileMasterRepo.findByCategoryAndYearAndIsDeleted(category,
					financialyearNoti, "N");
			model.addAttribute("fileList", fileNamesFromDB);
		}
		if (id != null) {
			// Split the hiddenValues by commas
			String[] parts = id.split(",");
			if (parts.length == 3) {
				String idPart = parts[0].trim();
				int id2 = Integer.parseInt(idPart);
				String category1 = parts[1].trim();
				String year1 = parts[2].trim();
				if (id2 != 0 && category1 != null && year1 != null) {
					Optional<FileMaster> existingFileMaster = fileMasterRepo.findById(id2);
					if (existingFileMaster.isPresent()) {
						FileMaster fileToDelete = existingFileMaster.get();
						fileToDelete.setIsDeleted("Y");
						fileMasterRepo.save(fileToDelete);
						model.addAttribute("successMessage", "File deleted successfully.");
					} else {
						model.addAttribute("errorMessage", "File not found.");
					}
				}
			}
		}
		model.addAttribute("type", category);
		model.addAttribute("financialyearNoti", financialyearNoti);
		return ApplicationConstants.ADMIN + "/manage_documents";
	}

	// User Create successfully message
	@GetMapping("/user_creation_successful")
	public String userCreationSuccessful(Model model) {
		setAdminMenu(model, ApplicationConstants.ADMIN_CREATE_USER); // Set menu list
		model.addAttribute("designationList", designationRepository.findAll());
		model.addAttribute("successMessage", "User has been created successfully !");
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
	}

	// Update and Delete user role
	@GetMapping("/" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE)
	public String updateUserRole(@RequestParam(required = false) String loginName,
			@RequestParam(required = false) String category, @RequestParam(required = false) String inputText,
			Model model) {
		setAdminMenu(model, ApplicationConstants.ADMIN_UPDATE_USER_ROLE);
		if (loginName != null && loginName.trim().length() > 0) {
			Optional<UserDetails> userObject = userDetailsRepository.findByloginNameIgnoreCase(loginName);
			if (userObject.isPresent()) {
				model.addAttribute("userDetails", userObject.get());
				// Get All Role
				List<UserRole> allUserRole = adminUpdateUserDetailsImpl.getAllUserRole();
				Optional<UserRole> userRoleAdmin = useRepository.findByroleCode("ADMIN");
				int index = 0;
				index = allUserRole.indexOf(userRoleAdmin.get());
				if (index > 0) {
					allUserRole.remove(index);
				}
				model.addAttribute("allUserRole", allUserRole);
				// Get All Locations
				Map<String, String> locationMap = adminUpdateUserDetailsImpl.getAllLocationMap();
				Map<String, String> stateMap = adminUpdateUserDetailsImpl.getStateMap();
				model.addAttribute("stateMap", stateMap);
				Map<String, String> zoneMap = adminUpdateUserDetailsImpl.getAllZoneMap();
				model.addAttribute("zoneMap", zoneMap);
				Map<String, String> circleMap = adminUpdateUserDetailsImpl.getAllCircleMap();
				model.addAttribute("circleMap", circleMap);
				model.addAttribute("locationMapping", locationMap);
				// User Existing Role
				if (userObject.get().getUserRoleMappings() != null) {
					Map<String, Map<Long, String>> userExistingRole = new HashedMap<>();
					for (UserRoleMapping object : userObject.get().getUserRoleMappings()) {
						if (userExistingRole.get(object.getUserRole().getRoleName()) != null) {
							Map<Long, String> map = userExistingRole.get(object.getUserRole().getRoleName());
							map.put(object.getUserRoleMappingId(), adminUpdateUserDetailsImpl.getRoleLocation(object));
						} else {
							Map<Long, String> map = new HashMap<>();
							map.put(object.getUserRoleMappingId(), adminUpdateUserDetailsImpl.getRoleLocation(object));
							userExistingRole.put(object.getUserRole().getRoleName(), map);
						}
					}
					model.addAttribute("userExistingRole", userExistingRole);
				}
				if (category != null && category.length() > 0) {
					model.addAttribute("category", category);
				}
				if (inputText != null && inputText.length() > 0) {
					model.addAttribute("inputText", inputText);
				}
				return ApplicationConstants.ADMIN + "/update_user_role_details";
			}
		}
		List<String> categories = new ArrayList<>();
		categories.add("First Name");
		categories.add("Designation");
		categories.add("Mobile Number");
		categories.add("Email Id");
		categories.add("Login Id");
		model.addAttribute("categories", categories);
//		List<UserDetails> alluserDetails = adminUpdateUserDetailsImpl.getAlluserDetails();
//		model.addAttribute("userList", alluserDetails);
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE;
	}

	// Update user role
	@PostMapping("/" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE)
	public String updateUserRoles(Model model, @RequestParam(required = true) String userRoles,
			@RequestParam(required = true) String locationIds, @RequestParam(required = true) Integer userId,
			@RequestParam(required = false) String category, @RequestParam(required = false) String inputText,
			RedirectAttributes redirectAttrs) {
		List<String> errors = new ArrayList<>();
		List<String> errors1 = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (userId.equals(loginUserDetails.getUserId())) {
			errors.add("User can not modify his/her Role itself");
			redirectAttrs.addFlashAttribute("errorMessage", errors);
			redirectAttrs.addFlashAttribute("category", category);
			redirectAttrs.addFlashAttribute("inputText", inputText);
			return "redirect:" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE + "?loginName="
					+ loginUserDetails.getLoginName();
		}
		if (userRoles == null || userRoles.trim().length() == 0) {
			logger.error("ADMIN_UPDATE_USER_ROLE : Unable to get user roles from server");
			model.addAttribute("error_message", "Please Select User Role");
		}
		if (locationIds == null || locationIds.trim().length() == 0) {
			logger.error("ADMIN_UPDATE_USER_ROLE : Unable to get user id from server");
			model.addAttribute("error_message", "Please Select Role Locations");
		}
		if (userId <= 0) {
			logger.error("ADMIN_UPDATE_USER_ROLE : Unable to get user id from server");
			model.addAttribute("error_message", "Invalid user Id");
		}
		Optional<UserDetails> object = userDetailsRepository.findById(userId);
		if (object.isPresent()) {
			UserDetails userDetails = object.get();
			String str[] = userRoles.split(",");
			for (String role : str) {
				Optional<UserRole> object1 = userRoleRepository.findById(Integer.parseInt(role));
				if (object1.isPresent()) {
					UserRole userRole = object1.get();
					String str1[] = locationIds.split(",");
					for (String location : str1) { // Head Office, Review Meeting, L1 officer and MCM and CAG Head
													// Office can only assign on State level
						if (userRole.getRoleCode().equalsIgnoreCase("HQ")
								|| userRole.getRoleCode().equalsIgnoreCase("RM")
								|| userRole.getRoleCode().equalsIgnoreCase("L1")
								|| userRole.getRoleCode().equalsIgnoreCase("MCM")
								|| userRole.getRoleCode().equalsIgnoreCase("CAG_HQ")
								|| userRole.getRoleCode().equalsIgnoreCase("Enforcement_HQ")) {
							if (!(location.charAt(0) == 'H')) {
								errors1.add(userRole.getRoleName() + " role can only be assigned to State level");
								continue;
							}
						} else if (userRole.getRoleCode().equalsIgnoreCase("RU")
								|| userRole.getRoleCode().equalsIgnoreCase("ScrutinyRU")
								|| userRole.getRoleCode().equalsIgnoreCase("Enforcement_SVO")) { // Verifier to be
																									// assigned
							// at
							// Zones & State level
							// Checking if Role location already exists for this user
							if (adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndCircleAndRole(
									userDetails.getUserId(), location, userRole.getId())
									|| adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndZoneAndRole(
											userDetails.getUserId(), location, userRole.getId())
									|| adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndStateAndRole(
											userDetails.getUserId(), location, userRole.getId())) {
								if (errors.size() == 0) {
									errors.add(
											"You are trying to allocate the same role and location again. Kindly check and please try with a new role and location");
									errors.add(userRole.getRoleName() + " : "
											+ locationDetailsRepository.findById(location).get().getLocationName());
								} else {
									errors.add(userRole.getRoleName() + " : "
											+ locationDetailsRepository.findById(location).get().getLocationName());
								}
								continue;
							}
							if (location.charAt(0) == 'C' && !userRole.getRoleCode().equalsIgnoreCase("RU")) {
								errors1.add(userRole.getRoleName() + " role can not be assigned to Circle level");
								continue;
							} else {
								List<UserRoleMapping> urmList = new ArrayList<>();
								List<String> allLocations = null;
								UserRoleMapping userRoleMapping = new UserRoleMapping(null, null,
										(location.charAt(0) == 'Z' ? zoneDetailsRepository.findById(location).get()
												: zoneDetailsRepository.findById("NA").get()),
										(location.charAt(0) == 'C' ? circleDetailsRepository.findById(location).get()
												: circleDetailsRepository.findById("NA").get()),
										(location.charAt(0) == 'H' ? stateDetailsRepository.findById(location).get()
												: stateDetailsRepository.findById("NA").get()));
								urmList.add(userRoleMapping);
								if (!urmList.isEmpty()) { // get all the lower mapped locations from given location
									allLocations = adminUpdateUserDetailsImpl
											.getAllMappedLocationsFromUserRoleMappingList(urmList);
									if (allLocations != null && allLocations.size() > 0) {
//										allLocations.add("NA");
										List<UserRoleMapping> availableUrmList = userRoleMappingRepository
												.getAllUrmByUserIdRoleIdLocationList(userDetails.getUserId(),
														userRole.getId(), allLocations);
										if (!availableUrmList.isEmpty()) { // checking if user already assign to lower
																			// location from given location and role
											errors1.add("User already assiged to lower location for "
													+ userRole.getRoleName() + " Role");
											continue;
										}
									}
								}
								allLocations = null;
								if (!urmList.isEmpty()) { // get all the higher mapped locations from given location
									allLocations = adminUpdateUserDetailsImpl
											.getAllHigherMappedLocationsFromUserRoleMapping(urmList.get(0));
									if (allLocations != null && allLocations.size() > 0) {
//										allLocations.add("NA");
										List<UserRoleMapping> availableUrmList = userRoleMappingRepository
												.getAllUrmByUserIdRoleIdLocationList(userDetails.getUserId(),
														userRole.getId(), allLocations);
										if (!availableUrmList.isEmpty()) { // checking if user already assign to higher
																			// location from given location and role
											errors1.add("User already assiged to higher location for "
													+ userRole.getRoleName() + " Role");
											continue;
										}
									}
								}
							}
						} else if (userRole.getRoleCode().equalsIgnoreCase("AP")) {
							if (location.charAt(0) == 'C') {
								errors1.add(userRole.getRoleName() + " role can not be assigned to Circle level");
								continue;
							} else if (location.charAt(0) == 'Z'
									&& adminUpdateUserDetailsImpl.checkIfUserHaveStateRole(userDetails, userRole)) {
								errors1.add("User already assiged to higher location for " + userRole.getRoleName()
										+ " Role");
								continue;
							} else if (location.charAt(0) == 'H' && adminUpdateUserDetailsImpl
									.checkIfUserHaveZoneRole(userDetails, userRole, "ZONE")) {
								errors1.add("Since " + userRole.getRoleName()
										+ " role has already been assigned or under transfer process for Zone level, it can not be assigned at State level");
								continue;
							}
						} else if (userRole.getRoleCode().equalsIgnoreCase("ADMIN")) {
							errors1.add(userRole.getRoleName() + " role can't be assigned manually");
							continue;
						} else if (userRole.getRoleCode().equalsIgnoreCase("L3") && location.charAt(0) != 'C') { // Only
																													// at
																													// circle
																													// -
																													// can
																													// be
																													// at
																													// multiple
																													// jurisdictions
							errors1.add(userRole.getRoleName() + " Role can only be assigned at circle level.");
							continue;
						} else if (userRole.getRoleCode().equalsIgnoreCase("VW")
								|| userRole.getRoleCode().equalsIgnoreCase("L2")) {
							List<UserRoleMapping> urmList = new ArrayList<>();
							List<String> allLocations = null;
							if (userRole.getRoleCode().equalsIgnoreCase("L2")
									&& (location.charAt(0) == 'H' || location.charAt(0) == 'C')) { // At administrative
																									// zone -
																									// Can be at
																									// multiple
																									// jurisdictions
								errors1.add(
										userRole.getRoleName() + " Role can only be assigned at administrative zone.");
								continue;
							}
							UserRoleMapping userRoleMapping = new UserRoleMapping(null, null,
									(location.charAt(0) == 'Z' ? zoneDetailsRepository.findById(location).get()
											: zoneDetailsRepository.findById("NA").get()),
									(location.charAt(0) == 'C' ? circleDetailsRepository.findById(location).get()
											: circleDetailsRepository.findById("NA").get()),
									(location.charAt(0) == 'H' ? stateDetailsRepository.findById(location).get()
											: stateDetailsRepository.findById("NA").get()));
							urmList.add(userRoleMapping);
							if (!urmList.isEmpty()) { // get all the lower mapped locations from given location
								allLocations = adminUpdateUserDetailsImpl
										.getAllMappedLocationsFromUserRoleMappingList(urmList);
								if (allLocations != null && allLocations.size() > 0) {
//									allLocations.add("NA");
									List<UserRoleMapping> availableUrmList = userRoleMappingRepository
											.getAllUrmByUserIdRoleIdLocationList(userDetails.getUserId(),
													userRole.getId(), allLocations);
									if (!availableUrmList.isEmpty()) { // checking if user already assign to lower
																		// location from given location and role
										errors1.add("User already assiged to lower location for "
												+ userRole.getRoleName() + " Role");
										continue;
									}
								}
							}
							allLocations = null;
							if (!urmList.isEmpty()) { // get all the higher mapped locations from given location
								allLocations = adminUpdateUserDetailsImpl
										.getAllHigherMappedLocationsFromUserRoleMapping(urmList.get(0));
								if (allLocations != null && allLocations.size() > 0) {
//									allLocations.add("NA");
									List<UserRoleMapping> availableUrmList = userRoleMappingRepository
											.getAllUrmByUserIdRoleIdLocationList(userDetails.getUserId(),
													userRole.getId(), allLocations);
									if (!availableUrmList.isEmpty()) { // checking if user already assign to higher
																		// location from given location and role
										errors1.add("User already assiged to higher location for "
												+ userRole.getRoleName() + " Role");
										continue;
									}
								}
							}
						}
						if (location.charAt(0) == 'N') {
							if (!adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndStateAndRole(
									userDetails.getUserId(), location, userRole.getId())) {
								UserRoleMapping userRoleMapping = new UserRoleMapping();
								userRoleMapping.setUserDetails(userDetails);
								userRoleMapping.setUserRole(userRole);
								userRoleMapping.setCircleDetails(circleDetailsRepository.findById("NA").get());
								userRoleMapping.setZoneDetails(zoneDetailsRepository.findById("NA").get());
								userRoleMapping.setStateDetails(centreDetailsRepository.findById(location).get());
								userRoleMappingRepository.save(userRoleMapping);
								adminUserService.saveUserRoleMappinglogs(loginUserDetails, userRoleMapping,
										"Added user Role");
								logger.info("ADMIN_UPDATE_USER_ROLE: User Role and location saved : "
										+ userRoleMapping.getUserRole().getRoleName() + " : "
										+ userRoleMapping.getCircleDetails().getCircleName());
							} else {
								if (errors.size() == 0) {
									errors.add(
											"You are trying to allocate the same role and location again. Kindly check and please try with a new role and location");
									errors.add(userRole.getRoleName() + " : "
											+ stateDetailsRepository.findById(location).get().getStateName());
								} else {
									errors.add(userRole.getRoleName() + " : "
											+ stateDetailsRepository.findById(location).get().getStateName());
								}
							}
						}
						if (location.charAt(0) == 'C') {
							if (!adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndCircleAndRole(
									userDetails.getUserId(), location, userRole.getId())) {
								UserRoleMapping userRoleMapping = new UserRoleMapping();
								userRoleMapping.setUserDetails(userDetails);
								userRoleMapping.setUserRole(userRole);
								userRoleMapping.setCircleDetails(circleDetailsRepository.findById(location).get());
								userRoleMapping.setZoneDetails(zoneDetailsRepository.findById("NA").get());
								userRoleMapping.setStateDetails(stateDetailsRepository.findById("NA").get());
								userRoleMappingRepository.save(userRoleMapping);
								adminUserService.saveUserRoleMappinglogs(loginUserDetails, userRoleMapping,
										"Added user Role");
								logger.info("ADMIN_UPDATE_USER_ROLE: User Role and location saved : "
										+ userRoleMapping.getUserRole().getRoleName() + " : "
										+ userRoleMapping.getCircleDetails().getCircleName());
							} else {
								if (errors.size() == 0) {
									errors.add(
											"You are trying to allocate the same role and location again. Kindly check and please try with a new role and location");
									errors.add(userRole.getRoleName() + " : "
											+ circleDetailsRepository.findById(location).get().getCircleName());
								} else {
									errors.add(userRole.getRoleName() + " : "
											+ circleDetailsRepository.findById(location).get().getCircleName());
								}
							}
						}
						if (location.charAt(0) == 'Z') {
							if (!adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndZoneAndRole(
									userDetails.getUserId(), location, userRole.getId())) {
								UserRoleMapping userRoleMapping = new UserRoleMapping();
								userRoleMapping.setUserDetails(userDetails);
								userRoleMapping.setUserRole(userRole);
								userRoleMapping.setCircleDetails(circleDetailsRepository.findById("NA").get());
								userRoleMapping.setZoneDetails(zoneDetailsRepository.findById(location).get());
								userRoleMapping.setStateDetails(stateDetailsRepository.findById("NA").get());
								userRoleMappingRepository.save(userRoleMapping);
								adminUserService.saveUserRoleMappinglogs(loginUserDetails, userRoleMapping,
										"Added user Role");
								logger.info("ADMIN_UPDATE_USER_ROLE: User Role and location saved : "
										+ userRoleMapping.getUserRole().getRoleName() + " : "
										+ userRoleMapping.getCircleDetails().getCircleName());
							} else {
								if (errors.size() == 0) {
									errors.add(
											"You are trying to allocate the same role and location again. Kindly check and please try with a new role and location");
									errors.add(userRole.getRoleName() + " : "
											+ zoneDetailsRepository.findById(location).get().getZoneName());
								} else {
									errors.add(userRole.getRoleName() + " : "
											+ zoneDetailsRepository.findById(location).get().getZoneName());
								}
							}
						}
						if (location.charAt(0) == 'H') {
							if (!adminUpdateUserDetailsRepository.checkHaveRelationBetweenUserAndStateAndRole(
									userDetails.getUserId(), location, userRole.getId())) {
								UserRoleMapping userRoleMapping = new UserRoleMapping();
								userRoleMapping.setUserDetails(userDetails);
								userRoleMapping.setUserRole(userRole);
								userRoleMapping.setCircleDetails(circleDetailsRepository.findById("NA").get());
								userRoleMapping.setZoneDetails(zoneDetailsRepository.findById("NA").get());
								userRoleMapping.setStateDetails(stateDetailsRepository.findById(location).get());
								userRoleMappingRepository.save(userRoleMapping);
								adminUserService.saveUserRoleMappinglogs(loginUserDetails, userRoleMapping,
										"Added user Role");
								logger.info("ADMIN_UPDATE_USER_ROLE: User Role and location saved : "
										+ userRoleMapping.getUserRole().getRoleName() + " : "
										+ userRoleMapping.getCircleDetails().getCircleName());
							} else {
								if (errors.size() == 0) {
									errors.add(
											"You are trying to allocate the same role and location again. Kindly check and please try with a new role and location");
									errors.add(userRole.getRoleName() + " : "
											+ stateDetailsRepository.findById(location).get().getStateName());
								} else {
									errors.add(userRole.getRoleName() + " : "
											+ stateDetailsRepository.findById(location).get().getStateName());
								}
							}
						}
					}
				}
			}
		}
		if (errors1 != null & errors1.size() > 0) {
			errors.addAll(errors1);
		}
		if (errors != null && errors.size() > 0) {
			redirectAttrs.addFlashAttribute("errorMessage", errors);
		}
		redirectAttrs.addFlashAttribute("category", category);
		redirectAttrs.addFlashAttribute("inputText", inputText);
		return "redirect:" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE + "?loginName=" + object.get().getLoginName();
	}

	// Delete user Role
	@PostMapping("/detele_user_role")
	public String deleteUserRole(Model model, @RequestParam(required = true) int userId,
			@RequestParam(required = true) String roleMappingId, @RequestParam(required = true) String roleName,
			@RequestParam(required = false) String category, @RequestParam(required = false) String inputText,
			RedirectAttributes redirectAttrs) {
		UserDetails userDetails = null;
		UserRole userRole = null;
		List<String> errorList = new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		Optional<UserDetails> object = userDetailsRepository.findById(userId);
		if (object.isPresent()) {
			userDetails = object.get();
			if (userDetails.getUserId().equals(loginUserDetails.getUserId())) {
				List<String> list = new ArrayList<>();
				list.add("User can not modify his/her Role itself");
				redirectAttrs.addFlashAttribute("errorMessage", list);
				return "redirect:" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE + "?loginName="
						+ object.get().getLoginName();
			}
		}
		Optional<UserRole> object1 = userRoleRepository.findByroleName(roleName);
		if (object1.isPresent()) {
			userRole = object1.get();
		}
		if (!roleMappingId.equalsIgnoreCase("NA")) {
			UserRoleMapping userRoleMapping = userRoleMappingRepository.findById(Long.parseLong(roleMappingId)).get();
			if (!userRoleMapping.getUserRole().getRoleCode().equalsIgnoreCase("ADMIN")) {
				// checking if user do have any active cases for this particular location
				int caseCount = adminUpdateUserDetailsImpl.findEnfCasesFromUserRoleMapping(userRoleMapping);
				if (caseCount == 0) {
					Optional<TransferRole> objectT = transferRoleRepository
							.findById(userRoleMapping.getUserRoleMappingId());
					if (objectT.isPresent()) {
						adminUserServiceImpl.saveTransferRoleLogs(objectT.get(),
								"Deleting Entity of User Role Mapping");
						transferRoleRepository.delete(objectT.get());
					}
					adminUserService.saveUserRoleMappinglogs(loginUserDetails, userRoleMapping, "Deleted user Role");
					userRoleMappingRepository.delete(userRoleMapping);
				} else {
					errorList.add(userRoleMapping.getUserRole().getRoleName() + " : "
							+ userDetailsServiceImpl.getLocationNameFromUserRoleMapping(userRoleMapping) + " : "
							+ "Officer have " + caseCount
							+ " active cases. Please transfer the case(s) or role to other user.");
//					redirectAttrs.addFlashAttribute("RoleDeletionAlert", "Officer have " + caseCount + " active cases. Please transfer the case(s) or role to other user.");
				}
			} else {
				errorList.add("User Can not delete " + userRoleMapping.getUserRole().getRoleName() + " role");
			}
			logger.info("ADMIN_UPDATE_USER_ROLE: User Role and location Deleted : id : "
					+ userRoleMapping.getUserRoleMappingId());
		} else {
			if (userRole != null && !userRole.getRoleCode().equalsIgnoreCase("ADMIN")) {
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findAllByUserDetailsAndUserRole(userDetails, userRole);
				if (userRoleMappings != null && userRoleMappings.size() > 0) {
					for (UserRoleMapping urm : userRoleMappings) {
						int caseCount = adminUpdateUserDetailsImpl.findEnfCasesFromUserRoleMapping(urm);
						if (caseCount == 0) {
							Optional<TransferRole> objectT = transferRoleRepository
									.findById(urm.getUserRoleMappingId());
							if (objectT.isPresent()) {
								adminUserServiceImpl.saveTransferRoleLogs(objectT.get(),
										"Deleting Entity of User Role Mapping");
								transferRoleRepository.delete(objectT.get());
							}
//							transferRoleRepository.deleteById(urm.getUserRoleMappingId());
							userRoleMappingRepository.delete(urm);
							logger.info("ADMIN_UPDATE_USER_ROLE: User Role Deleted : " + urm.getUserRoleMappingId());
						} else {
							errorList.add(urm.getUserRole().getRoleName() + " : "
									+ userDetailsServiceImpl.getLocationNameFromUserRoleMapping(urm) + " : "
									+ "Officer have " + caseCount
									+ " active cases. Please transfer the case(s) or role to other user.");
						}
					}
				}
//				userRoleMappingRepository.deleteAll(userRoleMappings);
//				logger.info("ADMIN_UPDATE_USER_ROLE: User Role Deleted : " + userRole.getRoleName());
			} else {
				if (userRole.getRoleCode().equalsIgnoreCase("ADMIN")) {
					errorList.add("User Can not delete " + userRole.getRoleName() + " role");
				}
			}
		}
		if (errorList != null && errorList.size() > 0) {
			redirectAttrs.addFlashAttribute("errorMessage", errorList);
		}
		redirectAttrs.addFlashAttribute("category", category);
		redirectAttrs.addFlashAttribute("inputText", inputText);
		return "redirect:" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE + "?loginName=" + object.get().getLoginName();
	}

	// User Search
	@GetMapping("/" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE + "/search_user_deatils")
	public String searchUser(Model model, @RequestParam(required = true) String category,
			@RequestParam(required = true) String inputText, RedirectAttributes redirectAttrs) {
		List<UserDetails> allUserDetails = new ArrayList<>();
		if (category != null && category.trim().length() > 0 && inputText != null && inputText.trim().length() > 0) {
			if (category.equalsIgnoreCase("Mobile Number")) {
				allUserDetails = userDetailsRepository.findByMobileNumberLike(inputText.trim());
			} else if (category.equalsIgnoreCase("First Name")) {
				allUserDetails = userDetailsRepository.findByfirstNameLike(inputText.trim().toUpperCase());
			} else if (category.equalsIgnoreCase("Designation")) {
				allUserDetails = userDetailsRepository.findBydesignationLike(inputText.trim().toUpperCase());
			} else if (category.equalsIgnoreCase("Email Id")) {
				allUserDetails = userDetailsRepository.findByemailIdLike(inputText.trim().toUpperCase());
			} else if (category.equalsIgnoreCase("Login Id")) {
				allUserDetails = userDetailsRepository.findByloginNameLike(inputText.trim().toUpperCase());
			} else {
				model.addAttribute("error_message", "No user found with " + category + " as " + inputText);
			}
		}
		if (allUserDetails != null && allUserDetails.size() > 0) {
			Map<UserDetails, Map<String, String>> userLocationMap = adminUpdateUserDetailsImpl
					.getUserLocationDetails(allUserDetails);
			redirectAttrs.addFlashAttribute("userList", userLocationMap);
			redirectAttrs.addFlashAttribute("category", category);
			redirectAttrs.addFlashAttribute("inputText", inputText);
		} else {
			redirectAttrs.addFlashAttribute("error_message", "No user found with " + category + " as " + inputText);
		}
		return "redirect:/admin/" + ApplicationConstants.ADMIN_UPDATE_USER_ROLE;
	}

	// Update or Delete user Details
	@GetMapping("/" + ApplicationConstants.ADMIN_UPDATE_USER)
	public String updateUser(@RequestParam(required = false) boolean deleteStaus, Model model) {
		setAdminMenu(model, ApplicationConstants.ADMIN_UPDATE_USER);
		List<UserDetails> alluserDetails = adminUpdateUserDetailsImpl.getAlluserDetails();
		model.addAttribute("userList", alluserDetails);
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_USER;
	}

	// Delete User Post Mapping
	@PostMapping("/" + ApplicationConstants.ADMIN_DELETE_USER)
	public String deleteUser(@RequestParam(required = true) int userId, Model model) {
		userDetailsRepository.deleteById(userId);
		logger.info("Admin : User Deleted : user id : " + userId);
		return "redirect:/" + ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_USER
				+ "?deleteStaus=true";
	}

	// update user Details
	@PostMapping("/" + ApplicationConstants.ADMIN_UPDATE_USER)
	public String updateUser(@RequestParam(required = true) int userId, Model model) {
		setAdminMenu(model, ApplicationConstants.ADMIN_UPDATE_USER);
		Optional<UserDetails> userDetails = userDetailsRepository.findById(userId);
		if (userDetails.isPresent()) {
			model.addAttribute("userDetails", userDetails.get());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("dob", dateFormat.format(userDetails.get().getDateOfBirth()));
			model.addAttribute("designationList", designationRepository.findAll());
			List<UserStatus> userStatusList = userStatusRepository.findAll();
			model.addAttribute("userStatusList", userStatusList);
			return ApplicationConstants.ADMIN + "/update_user_details";
		}
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_USER;
	}

	// updating existing User Post Mapping
	@PostMapping("/update_existing_user_details")
	public String updateExistingUserDetails(@ModelAttribute("userDetails") UserDetails userDetails,
			@RequestParam(required = true) String dob, @RequestParam(required = true) int designation,
			@RequestParam String adminRemarks, Model model) throws ParseException {
		AdminUserDetailsLogs adminUserDetailsLogsSolo = new AdminUserDetailsLogs();
		String randomPassword = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (loginUserDetails == null) {
			return "redirect:/logout";
		}
		List<UserStatus> userStatusList = userStatusRepository.findAll();
		model.addAttribute("userStatusList", userStatusList);
		setAdminMenu(model, ApplicationConstants.ADMIN_UPDATE_USER); // Set menu list
		// checking for unique mobile
		if (checkNewMobile(userDetails.getMobileNumber(), userDetails.getLoginName()).equalsIgnoreCase("true")) {
			model.addAttribute("errorMessage", "Duplicate user mobile number !");
			logger.warn("ADMIN_UPDATE_EXISTING_USER : Duplicate user mobile number : " + userDetails.getMobileNumber());
			return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
		}
		// checking for unique email id
		if (checkNewEmailId(userDetails.getEmailId(), userDetails.getLoginName()).equalsIgnoreCase("true")) {
			model.addAttribute("errorMessage", "Duplicate email Id !");
			logger.warn("ADMIN_UPDATE_EXISTING_USER : Duplicate user email Id : " + userDetails.getEmailId());
			return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_CREATE_USER;
		}
//		userDetails.setPassword(Base64.getEncoder().encodeToString(userDetails.getPassword().getBytes())); // Encoding password
		Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(userDetails.getLoginName());
		if (object.isPresent()) {
			UserDetails userRepoObject = object.get();
			userRepoObject.setFirstName(userDetails.getFirstName());
			userRepoObject.setLastName(userDetails.getLastName());
			userRepoObject.setMiddleName(userDetails.getMiddleName());
			userRepoObject.setMobileNumber(userDetails.getMobileNumber());
			userRepoObject.setEmailId(userDetails.getEmailId());
			userRepoObject.setDesignation(userDetails.getDesignation());
			userRepoObject.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
			userRepoObject.setDesignation(designationRepository.findById(designation).get());
			if (userDetails.getUserStatus().equalsIgnoreCase("active")
					&& !userRepoObject.getUserStatus().equalsIgnoreCase("active")) {
				// setting default password
				String uuid = UUID.randomUUID().toString();
				Integer indexOfHyphen = uuid.indexOf("-");
				randomPassword = uuid.substring(0, indexOfHyphen);
				System.out.println("User activated : RandomPassword : " + randomPassword);
				userRepoObject.setUserStatus(userDetails.getUserStatus());
				userRepoObject.setPassword(passwordEncoder.encode(randomPassword));
				userRepoObject.setWrongPasswordAttempt(0);
				userRepoObject.setLastLoginDate(null);
				userRepoObject.setTotalOtpCount(0);
				userRepoObject.setPasswordUpdationDate(new Date());
			} else {
				if (!userDetails.getUserStatus().equalsIgnoreCase("active")
						&& userRepoObject.getUserStatus().equalsIgnoreCase("active")) {
					if (loginUserDetails.getLoginName().equalsIgnoreCase(userRepoObject.getLoginName())) {
						model.addAttribute("errorMessage", "User can not upadate his/her user status.");
						return ApplicationConstants.ADMIN + "/update_user_details";
					}
					List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository
							.findAllByUserDetails(userDetailsRepository.findById(userRepoObject.getUserId()).get());
					int caseCount = 0;
					if (userRoleMappingList != null && userRoleMappingList.size() > 0) {
						for (UserRoleMapping urm : userRoleMappingList) {
							caseCount = caseCount + adminUpdateUserDetailsImpl.findEnfCasesFromUserRoleMapping(urm);
						}
					}
					if (caseCount > 0) {
						model.addAttribute("errorMessage",
								"User have " + caseCount
										+ " active cases. Please transfer the role/cases to make the user "
										+ userDetails.getUserStatus() + ".");
						return ApplicationConstants.ADMIN + "/update_user_details";
					} else {
						userRepoObject.setUserStatus(userDetails.getUserStatus());
					}
				} else {
					userRepoObject.setUserStatus(userDetails.getUserStatus());
				}
			}
			userDetails = userDetailsRepository.save(userRepoObject); // Save user
			logger.info("ADMIN_UPDATE_EXISTING_USER : User details updated : Login Id : " + userDetails.getLoginName());
		}
		if (userDetails != null) {
			/******* Send Sms Start ********/
			if (smsServiceActiveOrNot && randomPassword.length() > 0) {
				logger.info("User activated successfully : Login Id : " + userDetails.getLoginName());
				SentSmsDetails sentSmsDetails = new SentSmsDetails();
				String searchString = "MsgID =";
				SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330753608218");
				String smsMsgBody = smsTemplate.getTemplate();
				smsMsgBody = smsMsgBody.replace("{#name#}", userDetails.getFirstName());
				smsMsgBody = smsMsgBody.replace("{#loginid#}", userDetails.getLoginName());
				smsMsgBody = smsMsgBody.replace("{#password#}", randomPassword);
				smsMsgBody = smsMsgBody.replace("{#url#}", applicationUrl);
				String Response = smsService.sendSMS("1107170330753608218", smsMsgBody, userDetails.getMobileNumber());
				String resultentMsgId = smsServiceImpl.extractStringAfter(Response, searchString);
				if (resultentMsgId != null) {
					sentSmsDetails.setMsgId(resultentMsgId);
				} else {
					sentSmsDetails.setMsgId("Msg Id Not Present !");
				}
				sentSmsDetails.setMsgBody(smsMsgBody);
				sentSmsDetails.setResponse(Response);
				sentSmsDetails.setSentTime(new Date());
				sentSmsDetails.setSentTo(userDetails.getMobileNumber());
				sentSmsDetails.setTemplateId("1107170330753608218");
				sendSmsDetailsRepository.save(sentSmsDetails);
			}
			/******* Send Sms End ********/
			/********* Save User Log Start ********/
			adminUserDetailsLogsSolo.setLoginId(userDetails.getLoginName());
			adminUserDetailsLogsSolo.setMobileNumber(userDetails.getMobileNumber());
			adminUserDetailsLogsSolo.setFirstName(userDetails.getFirstName());
			adminUserDetailsLogsSolo.setMiddleName(userDetails.getMiddleName());
			adminUserDetailsLogsSolo.setLastName(userDetails.getLastName());
			adminUserDetailsLogsSolo.setEmailId(userDetails.getEmailId());
			adminUserDetailsLogsSolo.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
			adminUserDetailsLogsSolo.setDesignation(userDetails.getDesignation());
			adminUserDetailsLogsSolo.setUserStatus(userDetails.getUserStatus());
			adminUserDetailsLogsSolo.setUserStatus(userDetails.getUserStatus());
			adminUserDetailsLogsSolo.setReasonForUpdate(adminRemarks);
			adminUserDetailsLogsSolo.setUpdatingTime(new Date());
			adminUserDetailsLogsSolo.setUserDetails(userDetails);
			adminUserDetailsLogsSolo.setAdminUserDetails(loginUserDetails);
			adminUserDetailsLogsSolo.setAction("update_user");
			adminUserDetailsLogsRepository.save(adminUserDetailsLogsSolo);
			/********* Save User Log End ********/
			model.addAttribute("userDetails", userDetails);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("dob", dateFormat.format(userDetails.getDateOfBirth()));
			model.addAttribute("designationList", designationRepository.findAll());
			model.addAttribute("successMessage", "User details updated successfully !");
			return ApplicationConstants.ADMIN + "/update_user_details";
		}
		model.addAttribute("userDetails", object.get());
		logger.error("ADMIN_UPDATE_EXISTING_USER : User details update failed : user id : " + userDetails.getUserId());
		model.addAttribute("errorMessage", "Unable to update user details !");
		return ApplicationConstants.ADMIN + "/update_user_details";
	}

	@GetMapping("/" + ApplicationConstants.ADMIN_TRANSFER_ROLE)
	public String transferRole(Model model, @RequestParam(required = false) String locationIds,
			@RequestParam(required = false) String userRoles, @RequestParam(required = false) Integer userId) {
		setAdminMenu(model, ApplicationConstants.ADMIN_TRANSFER_ROLE);
		// Get All Active user
		List<UserDetails> userList = userDetailsRepository.findAllActiveAndNewUserLoginName();
		if (userList != null && userList.size() > 0) {
			model.addAttribute("userList", userList);
		}
		// Get All Role
		List<UserRole> allUserRole = adminUpdateUserDetailsImpl.getAllUserRole();
		Optional<UserRole> userRoleAdmin = useRepository.findByroleCode("ADMIN");
		int index = 0;
		index = allUserRole.indexOf(userRoleAdmin.get());
		if (index > 0) {
			allUserRole.remove(index);
		}
		model.addAttribute("allUserRole", allUserRole);
		// Get All Locations
		Map<String, String> stateMap = adminUpdateUserDetailsImpl.getStateMap();
		model.addAttribute("stateMap", stateMap);
		Map<String, String> zoneMap = adminUpdateUserDetailsImpl.getAllZoneMap();
		model.addAttribute("zoneMap", zoneMap);
		Map<String, String> circleMap = adminUpdateUserDetailsImpl.getAllCircleMap();
		model.addAttribute("circleMap", circleMap);
		// find Role Details
		if (userId != null && userId > 0) {
			Optional<UserDetails> objectUD = userDetailsRepository.findById(userId);
			if (objectUD.isPresent()) {
				UserDetails userDetails = objectUD.get();
				model.addAttribute("userName", userDetails.getFirstName()
						+ (userDetails.getMiddleName().trim().length() > 0 ? (" " + userDetails.getMiddleName()) : "")
						+ (userDetails.getLastName().trim().length() > 0 ? (" " + userDetails.getLastName()) : "")
						+ " [" + userDetails.getLoginName() + "]");
//				List<String> roleList = (userRoles.length()>0 ? Arrays.asList(userRoles.split(",")) : new ArrayList<>());
				String[] roleStrings = (userRoles.length() > 0 ? userRoles.split(",") : null);
				List<Integer> roleList = new ArrayList<>();
				if (roleStrings != null) {
					for (String roleString : roleStrings) {
						int role = Integer.parseInt(roleString.trim());
						roleList.add(role);
					}
				}
				List<String> locationList = (locationIds.length() > 0 ? Arrays.asList(locationIds.split(","))
						: new ArrayList<>());
				List<UserRoleMapping> userRoleMappingList = null;
				if (roleList.size() > 0 && locationList.size() > 0) {
					userRoleMappingList = userRoleMappingRepository
							.findUserByUserIdAndRolesAndLocations(userDetails.getUserId(), roleList, locationList);
				} else if (roleList.size() == 0 && locationList.size() > 0) {
					userRoleMappingList = userRoleMappingRepository
							.findUserByUserIdAndLocations(userDetails.getUserId(), locationList);
				} else if (roleList.size() > 0 && locationList.size() == 0) {
					userRoleMappingList = userRoleMappingRepository.findUserByUserIdAndRoles(userDetails.getUserId(),
							roleList);
				} else if (roleList.size() == 0 && locationList.size() == 0) {
					userRoleMappingList = userRoleMappingRepository.findAllByUserDetails(userDetails);
				}
				if (userRoleMappingList != null && userRoleMappingList.size() > 0) {
					model.addAttribute("userRoleMappingList", userRoleMappingList);
					Map<String, Map<Long, String>> userExistingRole = new HashedMap<>();
					for (UserRoleMapping object : userRoleMappingList) {
						if (object.getUserRole().getRoleCode().equalsIgnoreCase("ADMIN")) {
							continue;
						}
						if (userExistingRole.get(object.getUserRole().getRoleName()) != null) {
							Map<Long, String> map = userExistingRole.get(object.getUserRole().getRoleName());
							map.put(object.getUserRoleMappingId(), adminUpdateUserDetailsImpl.getRoleLocation(object));
						} else {
							Map<Long, String> map = new HashMap<>();
							map.put(object.getUserRoleMappingId(), adminUpdateUserDetailsImpl.getRoleLocation(object));
							userExistingRole.put(object.getUserRole().getRoleName(), map);
						}
					}
					model.addAttribute("userExistingRole", userExistingRole);
					model.addAttribute("userId", userId);
				} else {
					model.addAttribute("error Message", "No record found !");
				}
			}
		}
//		deemedApprovedCases.TransferRoleAction();
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_TRANSFER_ROLE;
	}

	@GetMapping("/transfer_role_action")
	public String transferRole(Model model, @RequestParam(required = false) String checkedLocations,
			@RequestParam(required = true) String effectedDate, @RequestParam(required = false) String checkedRoles,
			@RequestParam(required = true) Integer userId, @RequestParam(required = true) Integer transferToUserId,
			RedirectAttributes redirectAttributes) {
		try {
			setAdminMenu(model, ApplicationConstants.ADMIN_TRANSFER_ROLE);
			logger.info("checkedLocations: " + checkedLocations + "\ncheckedRoles:" + checkedRoles + "\nuserId:"
					+ userId + "\ntransferToUserId:" + transferToUserId);
			List<UserRoleMapping> userRoleMappingsList = new ArrayList<>();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
			Date effectedDate1 = dateFormatter.parse(effectedDate);
			List<String> output = new ArrayList<>();
			List<String> output1 = new ArrayList<>();
			// Checking if user only trying to update his/her own role
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName())
					.get();
			if (loginUserDetails == null) {
				return "redirect:/logout";
			}
			if (userId.equals(loginUserDetails.getUserId()) || transferToUserId.equals(loginUserDetails.getUserId())) {
				redirectAttributes.addFlashAttribute("uploadDataError", "User can not modify his/her Role itself");
				return "redirect:/" + ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_TRANSFER_ROLE;
			}
			Optional<UserDetails> objectUDF = userDetailsRepository.findById(transferToUserId);
			Optional<UserDetails> objectUD = userDetailsRepository.findById(userId);
			if (objectUDF.isPresent() && objectUD.isPresent()) {
				UserDetails transferFromUserDetails = objectUD.get();
				UserDetails transferToUserDetails = objectUDF.get();
				// getting all the User Role object for CheckedRoles
				List<UserRole> roleList = new ArrayList<>();
				if (checkedRoles.length() > 0) {
					List<String> roleStrList = Arrays.asList(checkedRoles.split(","));
					roleList = userRoleRepository.findAllByRoleName(roleStrList);
					for (UserRole ur : roleList) {
						userRoleMappingsList.addAll(
								userRoleMappingRepository.findAllByUserDetailsAndUserRole(transferFromUserDetails, ur));
					}
				}
				// getting all the object for CheckedLocations
				if (checkedLocations.length() > 0) {
					String[] locationIdsStr = (checkedLocations.length() > 0 ? checkedLocations.split(",") : null);
					List<Long> locationIdList = new ArrayList<>();
					for (String str : locationIdsStr) {
						Long id = Long.parseLong(str.trim());
						locationIdList.add(id);
					}
					userRoleMappingsList.addAll(userRoleMappingRepository.findAllById(locationIdList));
				}
				if (userRoleMappingsList.size() > 0) {
					// getting all role mapping list for transfer
					List<UserRoleMapping> userRoleMappingsListUniqueEntity = new ArrayList<>();
					Map<Long, UserRoleMapping> userRoleMappingsListUniqueEntityMap = new HashMap<>();
					for (UserRoleMapping urm : userRoleMappingsList) {
						Optional<UserRoleMapping> object1 = userRoleMappingRepository
								.findById(urm.getUserRoleMappingId());
						if (object1.isPresent()) {
							userRoleMappingsListUniqueEntity.add(urm);
							userRoleMappingsListUniqueEntityMap.put(urm.getUserRoleMappingId(), urm);
						}
					}
					// checking if user role mapping already push for transfer
					for (Map.Entry<Long, UserRoleMapping> entry : userRoleMappingsListUniqueEntityMap.entrySet()) {
						Optional<TransferRole> objectTR = transferRoleRepository.findById(entry.getKey());
						if (!objectTR.isPresent()) {
							// checking if user push transfer role for AP and RU and location for Zone or
							// enf zone
							UserRoleMapping roleMapping = entry.getValue();
							if (roleMapping.getUserRole().getRoleCode().equalsIgnoreCase("AP")
									|| roleMapping.getUserRole().getRoleCode().equalsIgnoreCase("RU")) {
								if (adminUpdateUserDetailsImpl.checkIfUserHaveZoneRole(transferToUserDetails,
										roleMapping.getUserRole(), "ZONE")) {
									output1.add(transferToUserDetails.getLoginName()
											+ " already assiged/will be transferred to administrative zone for "
											+ roleMapping.getUserRole().getRoleName() + " Role");
									continue;
								}
							}
							TransferRole transferRole = new TransferRole(entry.getKey(), entry.getValue(),
									transferToUserDetails, effectedDate1, loginUserDetails);
							transferRoleRepository.save(transferRole);
							// Adding comment which role will be transfer
							if (output != null && output.size() == 0) {
								output.add("Below role(s) will be transferred from "
										+ transferFromUserDetails.getLoginName() + " to "
										+ transferToUserDetails.getLoginName() + " on "
										+ dateFormatter1.format(transferRole.getActionDate()));
								output.add(adminUpdateUserDetailsImpl.getRoleLocationNames(entry.getValue()));
							} else {
								output.add(adminUpdateUserDetailsImpl.getRoleLocationNames(entry.getValue()));
							}
						} else {
							TransferRole tr1 = objectTR.get();
							output1.add(adminUpdateUserDetailsImpl.getRoleLocationNames(entry.getValue())
									+ " already push for transfer from "
									+ tr1.getUserRoleMapping().getUserDetails().getLoginName() + " to "
									+ tr1.getTransferToUser().getLoginName() + " on "
									+ dateFormatter1.format(tr1.getActionDate()));
						}
					}
				}
			}
			// if the effected date is today then transfer all the role and cases
			Date today = dateFormatter.parse(dateFormatter.format(new Date()));
			;
			if (today.equals(effectedDate1)) {
				deemedApprovedCases.TransferRoleAction();
				if (output != null && output.size() > 0) {
					output.set(0, "Below role(s) has been transferred from " + objectUD.get().getLoginName() + " to "
							+ objectUDF.get().getLoginName() + " on " + dateFormatter1.format(new Date()));
				}
			}
			if (output1 != null && output1.size() > 0) {
				redirectAttributes.addFlashAttribute("uploadDataError", output1);
			}
			if (output != null && output.size() > 0) {
				redirectAttributes.addFlashAttribute("uploadDataInfo", output);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ADMIN : Transfer Role : " + e.getMessage());
		}
		return "redirect:/" + ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_TRANSFER_ROLE;
	}

	@PostMapping("/delete_transfer_role")
	public String deleteTransferRole(RedirectAttributes redirectAttributes, @RequestParam Long trnasferId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get();
		if (loginUserDetails == null) {
			return "redirect:/logout";
		}
		Optional<TransferRole> objectT = transferRoleRepository.findById(trnasferId);
		if (objectT.isPresent()) {
			TransferRole transferRole = objectT.get();
			transferRole.setRequestedByUserId(loginUserDetails);
			adminUserService.saveTransferRoleLogs(transferRole, "Delete transfer role");
			transferRoleRepository.deleteById(trnasferId);
			redirectAttributes.addFlashAttribute("uploadDataInfo",
					"The transfer request has been deleted successfully");
		}
		return "redirect:/" + ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE;
	}

	@GetMapping("/" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE)
	public String updateTransferRole(Model model, @RequestParam(required = false) String locationIds,
			@RequestParam(required = false) String userRoles, @RequestParam(required = false) Integer userId) {
		setAdminMenu(model, ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE);
		// Get All Active user
		List<UserDetails> userList = userDetailsRepository.findAllActiveAndNewUserLoginName();
		if (userList != null && userList.size() > 0) {
			model.addAttribute("userList", userList);
		}
		// Get All Role
		List<UserRole> allUserRole = adminUpdateUserDetailsImpl.getAllUserRole();
		Optional<UserRole> userRoleAdmin = useRepository.findByroleCode("ADMIN");
		int index = 0;
		index = allUserRole.indexOf(userRoleAdmin.get());
		if (index > 0) {
			allUserRole.remove(index);
		}
		model.addAttribute("allUserRole", allUserRole);
		// Get All Locations
		Map<String, String> stateMap = adminUpdateUserDetailsImpl.getStateMap();
		model.addAttribute("stateMap", stateMap);
		Map<String, String> zoneMap = adminUpdateUserDetailsImpl.getAllZoneMap();
		model.addAttribute("zoneMap", zoneMap);
		Map<String, String> circleMap = adminUpdateUserDetailsImpl.getAllCircleMap();
		model.addAttribute("circleMap", circleMap);
		List<TransferRole> allTransferRolesList = transferRoleRepository.findAll();
		model.addAttribute("allTransferRolesList", allTransferRolesList);
		return ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE;
	}

	@GetMapping("/" + "view_" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE)
	public String updateTransferRole(Model model, @RequestParam(name = "id") Long transferId) {
		Optional<TransferRole> object = transferRoleRepository.findById(transferId);
		if (object.isPresent()) {
			model.addAttribute("transferRole", object.get());
			// Get All Active user
			List<UserDetails> userList = userDetailsRepository.findAllActiveAndNewUserLoginName();
			if (userList != null && userList.size() > 0) {
				model.addAttribute("userList", userList);
			}
		}
		return ApplicationConstants.ADMIN + "/view_" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE;
	}

	@PostMapping("update_transfer_role_action")
	@Transactional
	public ResponseEntity<String> updateTransferRoleAction(RedirectAttributes redirectAttributes, Model model,
			@RequestParam(name = "effectiveDate") String effectedDate, @RequestParam Long transferId,
			@RequestParam Integer transferToUserId) {
		try {
			JSONObject objectJSON = new JSONObject();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
			Date effectedDate1 = dateFormatter.parse(effectedDate);
			List<String> output = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails loginUserDetails = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName())
					.get();
			// Get All Active user
			List<UserDetails> userList = userDetailsRepository.findAllActiveAndNewUserLoginName();
			if (userList != null && userList.size() > 0) {
				model.addAttribute("userList", userList);
			}
			Optional<TransferRole> object = transferRoleRepository.findById(transferId);
			UserDetails transferToUserDetails = userDetailsRepository.findById(transferToUserId).get();
			if (object.isPresent()) {
				model.addAttribute("transferRole", object.get());
				UserRoleMapping roleMapping = object.get().getUserRoleMapping();
				// Checking if user only trying to update his/her own role
				if (roleMapping.getUserDetails().getUserId().equals(loginUserDetails.getUserId())
						|| transferToUserId.equals(loginUserDetails.getUserId())) {
					objectJSON.put("dataInfo", "error");
					objectJSON.put("uploadDataError", "User can not modify his/her Role itself");
					return ResponseEntity.ok(objectJSON.toString());
				}
				// checking if user push transfer role for AP and RU and location for Zone or
				// enf zone
				if (roleMapping.getUserRole().getRoleCode().equalsIgnoreCase("AP")
						|| roleMapping.getUserRole().getRoleCode().equalsIgnoreCase("RU")) {
					if (adminUpdateUserDetailsImpl.checkIfUserHaveZoneRole(transferToUserDetails,
							roleMapping.getUserRole(), "ZONE")) {
						objectJSON.put("dataInfo", "error");
						objectJSON.put("uploadDataError",
								transferToUserDetails.getLoginName()
										+ " already assiged/will be transferred to administrative zone for "
										+ roleMapping.getUserRole().getRoleName() + " Role");
						return ResponseEntity.ok(objectJSON.toString());
					}
				}
				TransferRole transferRole = object.get();
				transferRole.setTransferToUser(transferToUserDetails);
				transferRole.setActionDate(effectedDate1);
				transferRole.setRequestedByUserId(loginUserDetails);
				transferRoleRepository.save(transferRole);
				adminUserService.saveTransferRoleLogs(transferRole, "Update Transfer Role");
				UserDetails transferFromUserDetails = roleMapping.getUserDetails();
				output.add(adminUpdateUserDetailsImpl.getRoleLocationNames(roleMapping));
				output.add(" - role(s) will be transferred from " + transferFromUserDetails.getLoginName() + " to "
						+ transferToUserDetails.getLoginName() + " on "
						+ dateFormatter1.format(transferRole.getActionDate()));
			}
			// if the effected date is today then transfer all the role and cases
			Date today = dateFormatter.parse(dateFormatter.format(new Date()));
			;
			if (today.equals(effectedDate1)) {
				Optional<TransferRole> object1 = transferRoleRepository.findById(transferId);
				if (output != null && output.size() > 0) {
					output.set(1,
							" - Role(s) has been transfer from "
									+ object1.get().getUserRoleMapping().getUserDetails().getLoginName() + " to "
									+ object1.get().getTransferToUser().getLoginName() + " on "
									+ dateFormatter1.format(new Date()));
				}
				deemedApprovedCases.TransferRoleAction();
			}
			objectJSON.put("dataInfo", "info");
			objectJSON.put("uploadDataInfo", output.get(0) + output.get(1));
			objectJSON.put("link",
					"/" + ApplicationConstants.ADMIN + "/" + ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE);
			return ResponseEntity.ok(objectJSON.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ApplicationConstants.ADMIN_UPDATE_TRANSFER_ROLE + "_action : " + e.getMessage());
		}
		return ResponseEntity.ok("");
	}

	// Checking the Login Id is unique or not
	@GetMapping("/checkNewLoginId")
	@ResponseBody
	public String checkNewLoginId(@RequestParam(required = true) String loginName) {
		Optional<UserDetails> userDetails = userDetailsRepository.findByloginNameIgnoreCase(loginName);
		if (userDetails.isPresent()) {
			return "true";
		}
		return "false";
	}

	// Checking the Mobile Number is unique or not
	@GetMapping("/checkNewMobile")
	@ResponseBody
	public String checkNewMobile(@RequestParam(required = true) String mobileNumber,
			@RequestParam(required = false) String loginName) {
		Optional<UserDetails> userDetails = userDetailsRepository.findBymobileNumber(mobileNumber);
		if (userDetails.isPresent()) {
			if (loginName != null && loginName.trim().length() > 0
					&& userDetails.get().getLoginName().equals(loginName)) {
				return "false";
			}
			return "true";
		}
		return "false";
	}

	// Checking the Email id is unique or not
	@GetMapping("/checkNewEmailId")
	@ResponseBody
	public String checkNewEmailId(@RequestParam(required = true) String emailId,
			@RequestParam(required = false) String loginName) {
		Optional<UserDetails> userDetails = userDetailsRepository.findByemailIdIgnoreCase(emailId);
		if (userDetails.isPresent()) {
			if (loginName != null && loginName.trim().length() > 0
					&& userDetails.get().getLoginName().equals(loginName)) {
				return "false";
			}
			return "true";
		}
		return "false";
	}

	// Get the menu list and active menu
	private void setAdminMenu(Model model, String activeMenu) {
		try {
			model.addAttribute("MenuList",
					appMenuRepository.findByIsParentAndUserTypeOrderByPriorityAsc(true, ApplicationConstants.ADMIN));
			model.addAttribute("activeMenu", activeMenu);
			model.addAttribute("activeRole", "ADMIN");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository
						.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();
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
					model.addAttribute("commonUserDetails", "/admin/user_details");
					model.addAttribute("changeUserPassword", "/gu/change_password");
					model.addAttribute("homePageLink", "/admin/dashboard");
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
					List<String> roleNameList = uniqueRolesList.stream()
							.map(mapping -> mapping.getUserRole().getRoleName()).collect(Collectors.toList());
					String commaSeperatedRoleList = roleNameList.stream().collect(Collectors.joining(", "));
					String LocationsName = returnLocationsName(userRoleMappings);
					userProfileDetails.setAssignedRoles(commaSeperatedRoleList);
					userProfileDetails.setAssignedWorkingLocations(LocationsName);
					model.addAttribute("userProfileDetails", userProfileDetails);
					/*************************
					 * to display user generic details end
					 ****************************/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error :: setAdminMenu" + e.getMessage());
		}
	}
	// ##############################################################################################################################################
//	@GetMapping("/" + ApplicationConstants.ADMIN_CREATE_USER)
//	public ModelAndView createUser(Model model) {
//		
//		setFOMenu(model, ApplicationConstants.ADMIN_CREATE_USER);
//		
//		ModelAndView mv = new ModelAndView();
//        mv.setViewName("admin/create_user");
//        return mv;
//	}

//	My Code
	@GetMapping("/my_code")
	public String getAllIcon(Model model, @RequestParam(required = false) Map<String, String> allParam) {
//		List<String> nameFromTableAppRole = categoryRepository.getNameFromTableAppRole();
//		model.addAttribute("nameFromTableAppRole", nameFromTableAppRole);
		Form1 form1 = new Form1();
		form1.setFirstName("Sutanu");
		form1.setLastName("Paul");
		model.addAttribute("form1", form1);
		return "site/index";
	}

	@PostMapping("/my_code")
	public String getAllIcon(@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName, @ModelAttribute Form1 form1, Model model) {
		model.addAttribute("form1", form1);
		return "site/index";
	}

	@PostMapping("/activeInactiveUser")
	@ResponseBody
	public ResponseEntity<String> activeInactiveUser(@RequestParam Integer userId, @RequestParam String userStatus) {
		JSONObject jsonObject = new JSONObject();
		try {
			Optional<UserDetails> userDetails = userDetailsRepository.findById(userId);
			userDetails.get().setUserStatus(userStatus);
			jsonObject.put("status", "SUCCESS");
			// userDetailsRepository.save(userDetails.get());
			return ResponseEntity.ok(jsonObject.toString());
		} catch (Exception ex) {
			jsonObject.put("status", "Fail");
			jsonObject.put("message", "Something went wrong !");
			ex.printStackTrace();
			logger.error("ADMIN_ACTIVE_INACTIVE_USER : " + ex.getMessage());
			return ResponseEntity.ok(jsonObject.toString());
		}
	}

	@GetMapping("/" + ApplicationConstants.GENERIC_USER_DETAILS)
	public String getUserDetails(Model model) {
		logger.info("CommonController.getUserDetails() : ApplicationConstants.GENERIC_USER_DETAILS");
		setAdminMenu(model, ApplicationConstants.GENERIC_USER_DETAILS);
		return ApplicationConstants.GENERIC_USER + "/" + ApplicationConstants.GENERIC_USER_DETAILS;
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
	/**************** return locations name end ******************/
}
