package com.hp.gstreviewfeedbackapp.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hp.gstreviewfeedbackapp.data.RestPasswordData;
import com.hp.gstreviewfeedbackapp.model.FileMaster;
import com.hp.gstreviewfeedbackapp.model.SMSTemplate;
import com.hp.gstreviewfeedbackapp.model.SentSmsDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FileMasterRepository;
import com.hp.gstreviewfeedbackapp.repository.SMSTemplateRepository;
import com.hp.gstreviewfeedbackapp.repository.SendSmsDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.AdminUpdateUserDetails;
import com.hp.gstreviewfeedbackapp.service.LoginService;
import com.hp.gstreviewfeedbackapp.service.SMSService;
import com.hp.gstreviewfeedbackapp.service.util.CaptchaUtil;

import cn.apiclub.captcha.Captcha;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminUpdateUserDetails adminUpdateUserDetails;
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	@Autowired
	private SMSTemplateRepository smsTemplateRepository;
	@Autowired
	private LoginService loginService;
	@Autowired
	private SMSService smsService;
	@Autowired
	private SendSmsDetailsRepository sendSmsDetailsRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private FileMasterRepository fileMasterRepository;
	@Value("${login.NewUser}")
	private String newUserStatus;
	@Value("${login.UserActive}")
	private String userActiveStatus;
	@Value("${login.UserInactive}")
	private String userInactiveStatus;
	@Value("${login.passwordExpiryDate}")
	private String paswordExpiryDays;
	@Value("${login.accountLockedTimingInMinutes}")
	private String lockedTiming;
	@Value("${login.otpExpiryTimingInMinutes}")
	private String otpTiming;
	@Value("${login.totalOtpSedingCountLimit}")
	private String totalOtpSedingCountLimit;
	@Value("${login.totalOtpLimitExpiryUnlockTimeInMinutes}")
	private String totalOtpLimitExpiryUnlockTimeInMinutes;
	@Value("${login.maxIncorrectOtpAttempts}")
	private Integer maxIncorrectOtpAttempts;
	@Value("${login.smsServiceActiveOrNot}")
	private Boolean smsServiceActiveOrNot;
	@Value("${doc.upload.pdf.location}")
	private String fileUploadDir;
	private String error_meaasge = null;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		getCapture(model);
		if (error_meaasge != null && error_meaasge.length() > 0) {
			model.addAttribute("messageDiv", error_meaasge);
			error_meaasge = null; // Rest Error Message
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/welcome";
		} else {
			return "site/login"; // Return the name of the login form template (e.g., login.html).
		}
	}

	@GetMapping("/exploreMore")
	public String exploreLogin(Model model,
			@RequestParam(name = "financialyear", required = false) String financialyear,
			@RequestParam(name = "level", required = false) String level,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "financialyearNoti", required = false) String financialyearNoti,
			@RequestParam(name = "selectedTab", required = false) String selectedTab) {
		getCapture(model);
		if (error_meaasge != null && error_meaasge.length() > 0) {
			model.addAttribute("messageDiv", error_meaasge);
			error_meaasge = null; // Rest Error Message
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/login";
		} else {
			List<String> levellist = fileMasterRepository.getDistinctLevels();
			model.addAttribute("levellist", levellist);
			List<String> yearlist = fileMasterRepository.getDistinctYears();
			model.addAttribute("yearlist", yearlist);
			List<String> typelist = fileMasterRepository.getDistinctTypes();
			model.addAttribute("typelist", typelist);
			List<FileMaster> allNotifications = fileMasterRepository.findTop5NotiByOrderByUploadDateDesc();
			List<FileMaster> topNotilist = new ArrayList<>();
			int count = 0;
			for (FileMaster fileMaster : allNotifications) {
				if (count < 5) {
					topNotilist.add(fileMaster);
					count++;
				} else {
					break;
				}
			}
			// List<FileMaster> topNotilist =
			// fileMasterRepository.findTop5NotiByOrderByUploadDateDesc();
			model.addAttribute("topNotilist", topNotilist);
			if (("tab2").equals(selectedTab)) {
				if (level != null) {
					List<FileMaster> actsList = fileMasterRepository.findByCategoryAndLevel("Acts And Rules", level);
					int i = 1;
					for (FileMaster fileMaster : actsList) {
						fileMaster.setId(i++);
					}
					model.addAttribute("actsList", actsList);
				}
				model.addAttribute("level", level);
			}
			if (("tab3").equals(selectedTab)) {
				if (type != null) {
					List<FileMaster> notiList = fileMasterRepository.findByYearAndTypeAndCategory(financialyearNoti,
							type, "Notifications");
					int i = 1;
					for (FileMaster fileMaster : notiList) {
						fileMaster.setId(i++);
					}
					model.addAttribute("notiList", notiList);
					model.addAttribute("financialyearNoti", financialyearNoti);
					model.addAttribute("type", type);
				}
			}
			if (financialyear != null && financialyear.length() > 0 && ("tab5").equals(selectedTab)) {
				List<FileMaster> circularList = fileMasterRepository.findByYearAndCategory(financialyear, "Circulars");
				int i = 1;
				for (FileMaster fileMaster : circularList) {
					fileMaster.setId(i++);
				}
				model.addAttribute("circularList", circularList);
				model.addAttribute("financialyear", financialyear);
				model.addAttribute("level", level);
			}
			model.addAttribute("selectedTab", selectedTab);
			return "site/exploreMore"; // Return the name of the login form template (e.g., login.html).
		}
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName,
			@RequestParam("category") String category, @RequestParam("year") String year) throws IOException {
		try {
			String filesDirectory = fileUploadDir + "GST " + category + "/" + year;
			Path filePath = Paths.get(filesDirectory, fileName);
			Resource resource = new org.springframework.core.io.PathResource(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
			headers.setContentType(MediaType.APPLICATION_PDF);
			return ResponseEntity.ok().headers(headers).body(resource);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/")
	public String redirectDefaultToLogin(Model model) {
		return "redirect:/login"; // Return the name of the login form template (e.g., login.html).
	}

	@GetMapping("/sessionExpiredOrConcurrentSessionlogout")
	public String logout(Model model) {
		return "site/sessionExpiredOrConcurrentSessionlogout";
	}

	@GetMapping("/welcome")
	public String welcome(Model model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				Optional<UserDetails> object = userDetailsRepository
						.findByloginNameIgnoreCase(authentication.getName());
				if (object.isPresent()) {
					UserDetails userDetails = object.get();
					List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
							.findByUserDetailsOrderByUserRole(userDetails);
					// All Roles of the user
					Map<String, UserRole> userRoles = new HashMap<>();
					for (UserRoleMapping objectUrm : userRoleMappings) {
						if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
							userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
						}
					}
					Map<String, Map<String, UserRole>> useRoleCategoryMap = new HashMap<>();
					for (UserRoleMapping objectUrm : userRoleMappings) {
						if (useRoleCategoryMap.get(objectUrm.getUserRole().getCategory()) == null) {
							Map<String, UserRole> userRolesMap = new HashMap<>();
							userRolesMap.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
							useRoleCategoryMap.put(objectUrm.getUserRole().getCategory(), userRolesMap);
						} else {
							Map<String, UserRole> userRolesMap = useRoleCategoryMap
									.get(objectUrm.getUserRole().getCategory());
							userRolesMap.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
							useRoleCategoryMap.put(objectUrm.getUserRole().getCategory(), userRolesMap);
						}
					}
					if (useRoleCategoryMap.size() > 0) {
						model.addAttribute("LoggedInUserRolesV2", new TreeMap<>(useRoleCategoryMap));
					}
					model.addAttribute("UserLoginName", userDetails.getLoginName());
					model.addAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
					model.addAttribute("commonUserDetails", "/gu/user_details");
					model.addAttribute("changeUserPassword", "/gu/change_password");
					model.addAttribute("currentUrl", "/gu/welcome");
				}
				return "site/index";
			} else {
				return "redirect:/login";
			}
		} catch (Exception e) {
			logger.error("LoginController : welcome : " + e.getMessage());
		}
		return "redirect:/login";
	}

	@GetMapping("/checkLoginStatus")
	@ResponseBody
	public String checkLoginStatus(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			return "true";
		} else {
			return "false";
		}
	}

	@GetMapping(value = "/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
	/*
	 * @GetMapping("/captcha")
	 * 
	 * @ResponseBody public String getCapture(Model model) { GeneratedCaptcha
	 * generatedCaptcha; BufferedImage captchaImage; String captchaCode; Captcha
	 * captcha = new Captcha(); JSONObject jsonObject = new JSONObject();
	 * 
	 * // captcha.getConfig().setFonts(new String[]{ //
	 * "src/main/resources/fonts/Roboto-Regular.ttf", //
	 * "src/main/resources/fonts/OpenSans-Regular.ttf", //
	 * "src/main/resources/fonts/Lato-Regular.ttf" });
	 * 
	 * 
	 * captcha.getConfig().setFontStyles(new int[]{Font.BOLD});
	 * 
	 * captcha.getConfig().setDarkBackgroundColor(new Color(52, 58, 64));
	 * captcha.getConfig().setNoise(0); captcha.getConfig().setDark(true);
	 * 
	 * try { generatedCaptcha = captcha.generate(); captchaImage =
	 * generatedCaptcha.getImage(); captchaCode = generatedCaptcha.getCode();
	 * 
	 * System.out.println(captchaCode);
	 * 
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * ImageIO.write(captchaImage, "png", baos);
	 * 
	 * String imageData = DatatypeConverter.printBase64Binary(baos.toByteArray());
	 * String encodedCaptchaCode =
	 * Base64.getEncoder().encodeToString(captchaCode.getBytes());
	 * 
	 * jsonObject.put("captchaCode", encodedCaptchaCode);
	 * jsonObject.put("captchaImage", imageData);
	 * 
	 * model.addAttribute("captchaCode", encodedCaptchaCode);
	 * model.addAttribute("captchaImage", imageData);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return jsonObject.toString();
	 * }
	 */

	@GetMapping("/captcha")
	@ResponseBody
	public String getCapture(Model model) {
		JSONObject jsonObject = new JSONObject();
		try {
			Captcha captcha = CaptchaUtil.createCaptcha(150, 35);
			String encodedCaptchaCode = Base64.getEncoder().encodeToString(captcha.getAnswer().getBytes());
			jsonObject.put("captchaCode", encodedCaptchaCode);
			jsonObject.put("captchaImage", CaptchaUtil.encodeBase64(captcha));
			model.addAttribute("captchaCode", encodedCaptchaCode);
			model.addAttribute("captchaImage", CaptchaUtil.encodeBase64(captcha));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	@GetMapping("/checkCaptcha")
	@ResponseBody
	public String checkCaptcha(@RequestParam(required = true) String captchaCode,
			@RequestParam(required = true) String captcha) throws ParseException {
		String captchaAnswer = new String(Base64.getDecoder().decode(captchaCode));
		if (!captcha.trim().equals(captchaAnswer.trim())) {
			return "false";
		} else if (captcha.trim().equals(captchaAnswer.trim())) {
			return "true";
		} else {
			return "false";
		}
	}
	// @PostMapping("/authenticate")
	/*
	 * public String authenticateUser(@RequestParam(required = true) String userId,
	 * 
	 * @RequestParam(required = true) String password, @RequestParam(required =
	 * true) String captchaCode,
	 * 
	 * @RequestParam(required = true) String captcha, Model model) throws
	 * ParseException {
	 * 
	 * // Checking captcha if (!captcha.equals(captchaCode)) { error_meaasge =
	 * "Incorrect Captcha !";
	 * 
	 * return "redirect:/login"; // Return to Login Page }
	 * 
	 * if (userId != null && password != null) { Optional<UserDetails> object =
	 * userDetailsRepository.findByloginNameIgnoreCase(userId); if
	 * (object.isPresent()) { UserDetails userDetails = object.get();
	 * if(!userDetails.getUserStatus().equalsIgnoreCase("new") &&
	 * !userDetails.getUserStatus().equalsIgnoreCase("active")) { error_meaasge =
	 * userId + " is " + userDetails.getUserStatus() +
	 * ". Please contact with ADMIN further!"; return "redirect:/login"; } if
	 * (userDetails.getPassword().equals(adminUpdateuserDetails.encodeString(
	 * password))) { if (userDetails.getWrongPasswordAttempt() == null) {
	 * userDetails.setWrongPasswordAttempt(0); } // check user locked timing if
	 * (userDetails.getWrongPasswordAttempt() == 5 &&
	 * userDetails.getUserLockedTime() != null &&
	 * !loginService.checkTimeExpiry(userDetails.getUserLockedTime(),
	 * Integer.parseInt(lockedTiming))) { Date systemDateTime = new Date(); long
	 * difference_In_Time = systemDateTime.getTime() -
	 * userDetails.getUserLockedTime().getTime(); // in // millisecond
	 * difference_In_Time = (Integer.parseInt(lockedTiming) * 60 * 1000) -
	 * difference_In_Time; long difference_In_Seconds = (difference_In_Time / 1000)
	 * % 60; long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
	 * error_meaasge = "Your Account " + userDetails.getLoginName() +
	 * " has been locked for more " + difference_In_Minutes + " minutes and " +
	 * difference_In_Seconds + " seconds !"; return "redirect:/login"; }
	 * 
	 * // Checking the user is new if
	 * (userDetails.getUserStatus().equalsIgnoreCase(newUserStatus)) { // checking
	 * user have any role if (userDetails.getUserRoleMappings() != null &&
	 * userDetails.getUserRoleMappings().size() > 0) {
	 * model.addAttribute("information",
	 * "Welcome to Performance Monitoring Application, HP. Please change your default password !"
	 * );
	 * 
	 * if (session != null) { session.invalidate(); }
	 * 
	 * session.setAttribute("restPassworduserDetails", userDetails);
	 * 
	 * return "redirect:/resetPassword"; } else { error_meaasge = userId +
	 * " don't have any role. Please contact with ADMIN further!"; return
	 * "redirect:/login"; } }
	 * 
	 * // Checking the user trying to login with in 90days from last login date if
	 * (!loginService.checkDateExpiry(userDetails.getLastLoginDate(), 90) &&
	 * !userDetails.getUserStatus().equalsIgnoreCase(userInactiveStatus)) {
	 * userDetails.setUserStatus(userInactiveStatus);
	 * userDetailsRepository.save(userDetails); error_meaasge = userId +
	 * " is inactive for more than 3 months. Please contact with ADMIN further!";
	 * return "redirect:/login";
	 * 
	 * }
	 * 
	 * // Checking if user is inactive if
	 * (userDetails.getUserStatus().equalsIgnoreCase(userInactiveStatus)) {
	 * error_meaasge = userId + " is inactive. Please contact with ADMIN further!";
	 * return "redirect:/login"; }
	 * 
	 * // Checking if password is expired or not if
	 * (!loginService.checkDateExpiry(userDetails.getPasswordUpdationDate(),
	 * Integer.parseInt(paswordExpiryDays))) { // send the user to password rest
	 * page model.addAttribute("information", "You Password has been Expried !");
	 * session.setAttribute("restPassworduserDetails", userDetails); return
	 * "redirect:/resetPassword?info=passwordExpired"; }
	 * 
	 * // User have role or not if (userDetails.getUserRoleMappings() != null &&
	 * userDetails.getUserRoleMappings().size() > 0) {
	 * session.setAttribute("LoggedInUserDetails", userDetails);
	 * session.setMaxInactiveInterval(300); List<UserRoleMapping> userRoleMappings =
	 * userRoleMappingRepository .findByUserDetailsOrderByUserRole(userDetails);
	 * 
	 * // All Roles of the user Map<String, UserRole> userRoles = new HashMap<>();
	 * for (UserRoleMapping objectUrm : userRoleMappings) { if
	 * (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
	 * userRoles.put(objectUrm.getUserRole().getRoleName(),
	 * objectUrm.getUserRole()); } } session.setAttribute("LoggedInUserRoles", new
	 * TreeMap<>(userRoles));
	 * 
	 * // String clientIP = request.getRemoteAddr();
	 * 
	 * if (userDetails.getWrongPasswordAttempt() != 0) {
	 * userDetails.setWrongPasswordAttempt(0);
	 * 
	 * if (userDetails.getUserLockedTime() != null) {
	 * userDetails.setUserLockedTime(null); } } userDetails.setLastLoginDate(new
	 * Date()); userDetailsRepository.save(userDetails);
	 * 
	 * return "redirect:/" + userRoleMappings.get(0).getUserRole().getUrl(); } else
	 * { if (userDetails.getWrongPasswordAttempt() != 0) {
	 * userDetails.setWrongPasswordAttempt(0);
	 * userDetailsRepository.save(userDetails); } error_meaasge = "Login ID " +
	 * userId +
	 * " do not have any roles assigned. Please contact system administrator for role assignment"
	 * ; }
	 * 
	 * } else { if (userDetails.getWrongPasswordAttempt() < 10) {
	 * userDetails.setWrongPasswordAttempt(userDetails.getWrongPasswordAttempt() +
	 * 1); userDetailsRepository.save(userDetails); }
	 * 
	 * // After 5 login Attempt with wrong password the user will be locked and
	 * after // unlock more 5 login attempt the user will be inactivated if
	 * (userDetails.getWrongPasswordAttempt() == 5) {
	 * userDetails.setUserLockedTime(new Date());
	 * userDetailsRepository.save(userDetails);
	 * 
	 * } else if (userDetails.getWrongPasswordAttempt() >= 10) {
	 * userDetails.setUserStatus(userInactiveStatus);
	 * userDetailsRepository.save(userDetails); }
	 * 
	 * // Error messages for incorrect password if
	 * (userDetails.getWrongPasswordAttempt() > 2 &&
	 * userDetails.getWrongPasswordAttempt() < 5) { error_meaasge =
	 * "Incorrect Password ! Attempt Remaining " + (5 -
	 * userDetails.getWrongPasswordAttempt()); } else if
	 * (userDetails.getWrongPasswordAttempt() == 5) { error_meaasge =
	 * "Your Account " + userDetails.getLoginName() + " has been locked for " +
	 * lockedTiming + " minutes !"; } else if (userDetails.getWrongPasswordAttempt()
	 * > 5 && userDetails.getWrongPasswordAttempt() < 10) { error_meaasge =
	 * "Incorrect Password ! You account will be parmanently locked after " + (10 -
	 * userDetails.getWrongPasswordAttempt()) + " wrong attmepts"; } else if
	 * (userDetails.getWrongPasswordAttempt() >= 10) { error_meaasge =
	 * "Your Account " + userDetails.getLoginName() +
	 * " has been inactivated. Please contact with ADMIN !"; } else { error_meaasge
	 * = "Incorrect Password !"; } }
	 * 
	 * } else { error_meaasge = "Invalid user !"; } }
	 * 
	 * return "redirect:/login"; // Return to Login Page }
	 */

	@GetMapping("/resetPassword")
	public String resetPassword(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object1 = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (object1.isPresent()) {
			UserDetails userDetails = object1.get();
			model.addAttribute("userName", userDetails.getFirstName() + " " + userDetails.getLastName());
			model.addAttribute("designationAcronym", userDetails.getDesignation().getDesignationAcronym());
			model.addAttribute("loginName", userDetails.getLoginName());
		}
		return "/site/resetPassword";
	}

	@PostMapping("/resetPasswordCheck")
	public ResponseEntity<String> sendOtpToResetPassword(@RequestBody RestPasswordData restPasswordData, Model model) {
		JSONObject object = new JSONObject();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object1 = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		if (object1.isPresent()) {
			UserDetails userDetails = null;
			if (object1.isPresent()) {
				userDetails = object1.get();
			} else {
				showLoginForm(model);
			}
			if (passwordEncoder.matches(restPasswordData.getOldPassword(), userDetails.getPassword())) {
				if (loginService.checkIfTotalOtpCountExpired(userDetails, Integer.parseInt(totalOtpSedingCountLimit),
						Integer.parseInt(totalOtpLimitExpiryUnlockTimeInMinutes))) {
					String otp = loginService.generateOTP();
					System.out.println("OTP: " + otp);
					object.put("otpEncd", "");
					object.put("otpTime", "");
//					object.put("OTP", otp);
					userDetails.setOtpEncd(adminUpdateUserDetails.encodeString(otp));
					userDetails.setOtpSendingTime(new Date());
					userDetails.setTotalOtpCount(userDetails.getTotalOtpCount() + 1);
					userDetailsRepository.save(userDetails);
					/******* Send Sms Start ********/
					if (smsServiceActiveOrNot) {
						SentSmsDetails sentSmsDetails = new SentSmsDetails();
						String searchString = "MsgID =";
						SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330761897010");
						String smsMsgBody = smsTemplate.getTemplate().replace("{#name#}", userDetails.getFirstName());
						smsMsgBody = smsMsgBody.replace("{#otp#}", otp);
						String Response = smsService.sendSMS("1107170330761897010", smsMsgBody,
								userDetails.getMobileNumber());
						String resultentMsgId = extractStringAfter(Response, searchString);
						if (resultentMsgId != null) {
							sentSmsDetails.setMsgId(resultentMsgId);
						} else {
							sentSmsDetails.setMsgId("Msg Id Not Present !");
						}
						sentSmsDetails.setMsgBody(smsMsgBody);
						sentSmsDetails.setResponse(Response);
						sentSmsDetails.setSentTime(new Date());
						sentSmsDetails.setSentTo(userDetails.getMobileNumber());
						sentSmsDetails.setTemplateId("1107170330761897010");
						sendSmsDetailsRepository.save(sentSmsDetails);
					}
					/******* Send Sms End ********/
					return ResponseEntity.ok(object.toString());
				} else {
					Date systemDateTime = new Date();
					long difference_In_Time = systemDateTime.getTime() - userDetails.getOtpSendingTime().getTime(); // in
																													// millisecond
					difference_In_Time = (Integer.parseInt(totalOtpLimitExpiryUnlockTimeInMinutes) * 60 * 1000)
							- difference_In_Time;
					long difference_In_Seconds = (difference_In_Time / 1000) % 60;
					long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
					object.put("error2",
							"Your account has been locked due to 5 wrong attempt(s). Please try to reset password after "
									+ difference_In_Minutes + " minutes and " + difference_In_Seconds + " seconds !");
					return ResponseEntity.ok(object.toString());
				}
			}
			object.put("error1", "Please enter correct old password");
			return ResponseEntity.ok(object.toString());
		} else {
			object.put("error1", "No user found");
			return ResponseEntity.ok(object.toString());
		}
	}

	@PostMapping("/otpSubmissionAndResetPassword")
	public ResponseEntity<String> validateOtpToResetPassword(@RequestBody RestPasswordData restPasswordData,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDetails> object1 = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
		UserDetails sessionUserDetails = object1.get();
		if (sessionUserDetails == null || !sessionUserDetails.getLoginName().equals(restPasswordData.getLoginName())) {
			showLoginForm(model);
		}
		logger.info("In OTP submission and resetPassword save if OTP is correct");
		JSONObject object = new JSONObject();
		try {
			Optional<UserDetails> objectUD = userDetailsRepository
					.findByloginNameIgnoreCase(restPasswordData.getLoginName());
			UserDetails userDetails = null;
			if (objectUD.isPresent()) {
				userDetails = objectUD.get();
			} else {
				showLoginForm(model);
			}
			if (passwordEncoder.matches(restPasswordData.getOldPassword(), userDetails.getPassword())) {
				if (userDetails.getOtpEncd().equals(adminUpdateUserDetails.encodeString(restPasswordData.getOtp()))
						&& !loginService.checkTimeExpiry(userDetails.getOtpSendingTime(),
								Integer.parseInt(otpTiming))) {
					userDetails.setPasswordUpdationDate(new Date());
					userDetails.setPassword(passwordEncoder.encode(restPasswordData.getNewPassword()));
					userDetails.setUserStatus(userActiveStatus);
					userDetails.setLastLoginDate(new Date());
					userDetails = userDetailsRepository.save(userDetails);
					// Save user logs
					loginService.saveUserLogs(userDetails, "Password change : restPassword");
					logger.info("Save new password !");
					if (userDetails != null) {
						object.put("message", "You password has been changed successfully !");
						object.put("link", "/logout");
						return ResponseEntity.ok(object.toString());
					} else {
						object.put("message", "We are facing some internal error, please login again !");
						object.put("link", "/logout");
						return ResponseEntity.ok(object.toString());
					}
				} else if (!userDetails.getOtpEncd()
						.equals(adminUpdateUserDetails.encodeString(restPasswordData.getOtp()))) {
					object.put("error", "Please enter correct OTP !");
					return ResponseEntity.ok(object.toString());
				} else if (loginService.checkTimeExpiry(userDetails.getOtpSendingTime(), Integer.parseInt(otpTiming))) {
					object.put("error", "OTP has been expired. Please use resend OTP !");
					return ResponseEntity.ok(object.toString());
				}
			} else {
				object.put("message", "We are facing some internal error, please login again !");
				object.put("link", "/login");
				return ResponseEntity.ok(object.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(object.toString());
	}

	@GetMapping("/forgot")
	public String forgot() {
		return "site/forgot";
	}

	@GetMapping("/regenerateOtp")
	@ResponseBody
	public ResponseEntity<String> regenerateOtp(@RequestParam String username, @RequestParam String userdob,
			@RequestParam String usernameSelected, @RequestParam String passwordSelected) throws ParseException {
		JSONObject object = new JSONObject();
		List<UserDetails> authenticateUserList = new ArrayList<UserDetails>();
		UserDetails saveOtpInUserDetails = new UserDetails();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = inputFormat.parse(userdob);
		if ((usernameSelected.equals("false")) && (passwordSelected.equals("true"))) {
			authenticateUserList = userDetailsRepository.findByLoginNameAndDateOfBirth(username, parsedDate);
		} else {
			authenticateUserList = userDetailsRepository.findByEmailIdAndDateOfBirth(username, parsedDate);
		}
		String otp = loginService.generateOTP();
		System.out.println("OTP : " + otp);
		object.put("message", "SUCCESS");
//		object.put("otp", otp);
		saveOtpInUserDetails = authenticateUserList.get(0);
		saveOtpInUserDetails.setOtpEncd(adminUpdateUserDetails.encodeString(otp));
		saveOtpInUserDetails.setOtpSendingTime(new Date());
		saveOtpInUserDetails.setTotalOtpCount(authenticateUserList.get(0).getTotalOtpCount() + 1);
		saveOtpInUserDetails = userDetailsRepository.save(saveOtpInUserDetails);
		/******* Send Sms Start ********/
		if (smsServiceActiveOrNot) {
			SentSmsDetails sentSmsDetails = new SentSmsDetails();
			String searchString = "MsgID =";
			SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330761897010");
			String smsMsgBody = smsTemplate.getTemplate().replace("{#name#}", saveOtpInUserDetails.getFirstName());
			smsMsgBody = smsMsgBody.replace("{#otp#}", otp);
			String Response = smsService.sendSMS("1107170330761897010", smsMsgBody,
					saveOtpInUserDetails.getMobileNumber());
			String resultentMsgId = extractStringAfter(Response, searchString);
			if (resultentMsgId != null) {
				sentSmsDetails.setMsgId(resultentMsgId);
			} else {
				sentSmsDetails.setMsgId("Msg Id Not Present !");
			}
			sentSmsDetails.setMsgBody(smsMsgBody);
			sentSmsDetails.setResponse(Response);
			sentSmsDetails.setSentTime(new Date());
			sentSmsDetails.setSentTo(saveOtpInUserDetails.getMobileNumber());
			sentSmsDetails.setTemplateId("1107170330761897010");
			sendSmsDetailsRepository.save(sentSmsDetails);
		}
		/******* Send Sms End ********/
		return ResponseEntity.ok(object.toString());
	}

	@SuppressWarnings("null")
	@GetMapping("/validateUsernameAndDob")
	@ResponseBody
	public ResponseEntity<String> forgot(@RequestParam String usernameSelected, @RequestParam String passwordSelected,
			@RequestParam String username, @RequestParam String userdob) throws ParseException {
		JSONObject object = new JSONObject();
		JSONObject saveSmsSendResponse = new JSONObject();
		long lockSystemTimingInMinutesAfterIncorrectOtpAttempts = 0L;
		long countTotalSeconds = 0L;
		long minutes = 0L;
		long seconds = 0L;
		long days = 0L;
		long hours = 0L;
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = inputFormat.parse(userdob);
		UserDetails saveOtpInUserDetails = new UserDetails();
		List<UserDetails> authenticateUserList = userDetailsRepository.findByLoginNameAndDateOfBirth(username,
				parsedDate);
		Integer userListSize = authenticateUserList.size();
		if (authenticateUserList.isEmpty()) {
			object.put("message", "Fail");
			object.put("noUserPresent",
					"No user found with these details. Please enter correct login id and date of birth");
			return ResponseEntity.ok(object.toString());
		}
		if (authenticateUserList.get(0).getIncorrectOtpAttempts() == 5) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			Instant instant = authenticateUserList.get(0).getUserLockedTime().toInstant();
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			Duration duration = Duration.between(localDateTime, currentDateTime);
			days = duration.toDays();
			hours = duration.toHours() % 24;
			minutes = duration.toMinutes() % 60;
			seconds = duration.getSeconds() % 60;
			countTotalSeconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds;
			lockSystemTimingInMinutesAfterIncorrectOtpAttempts = (15 * 60);
			if (lockSystemTimingInMinutesAfterIncorrectOtpAttempts > countTotalSeconds) {
				object.put("message", "Locked");
				object.put("systemLockedForDuration",
						"Your account has been locked due to 5 wrong attempt(s). Please try to retrieve your login id after "
								+ (14 - minutes) + " minutes and " + (60 - seconds) + " seconds !");
				return ResponseEntity.ok(object.toString());
			}
		}
		if (userListSize == 1) {
			String otp = loginService.generateOTP();
			System.out.println("OTP : " + otp);
			object.put("message", "SUCCESS");
//			object.put("otp", otp);
			saveOtpInUserDetails = authenticateUserList.get(0);
			saveOtpInUserDetails.setOtpEncd(adminUpdateUserDetails.encodeString(otp));
			saveOtpInUserDetails.setOtpSendingTime(new Date());
			saveOtpInUserDetails.setTotalOtpCount(authenticateUserList.get(0).getTotalOtpCount() + 1);
			userDetailsRepository.save(saveOtpInUserDetails);
			/******* Send Sms Start ********/
			if (smsServiceActiveOrNot) {
				SentSmsDetails sentSmsDetails = new SentSmsDetails();
				String searchString = "MsgID =";
				SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330761897010");
				String smsMsgBody = smsTemplate.getTemplate().replace("{#name#}",
						authenticateUserList.get(0).getFirstName());
				smsMsgBody = smsMsgBody.replace("{#otp#}", otp);
				String Response = smsService.sendSMS("1107170330761897010", smsMsgBody,
						authenticateUserList.get(0).getMobileNumber());
				String resultentMsgId = extractStringAfter(Response, searchString);
				if (resultentMsgId != null) {
					sentSmsDetails.setMsgId(resultentMsgId);
				} else {
					sentSmsDetails.setMsgId("Msg Id Not Present !");
				}
				sentSmsDetails.setMsgBody(smsMsgBody);
				sentSmsDetails.setResponse(Response);
				sentSmsDetails.setSentTime(new Date());
				sentSmsDetails.setSentTo(authenticateUserList.get(0).getMobileNumber());
				sentSmsDetails.setTemplateId("1107170330761897010");
				sendSmsDetailsRepository.save(sentSmsDetails);
			}
			/******* Send Sms End ********/
			return ResponseEntity.ok(object.toString());
		}
		if (authenticateUserList.get(0).getUserStatus().equalsIgnoreCase("inactive")) {
			object.put("message", "InActiveUser");
			object.put("inActiveUserMessage", "User Is Not An Active Person !");
			return ResponseEntity.ok(object.toString());
		} else {
			object.put("message", "Fail");
			return ResponseEntity.ok(object.toString());
		}
	}

	/********* Validtate email id and dob start ********/
	@GetMapping("/validateEmailAndDob")
	@ResponseBody
	public ResponseEntity<String> validateEmailAndDob(@RequestParam String usernameSelected,
			@RequestParam String passwordSelected, @RequestParam String userEmailId, @RequestParam String userDob)
			throws ParseException {
		JSONObject object = new JSONObject();
		JSONObject saveSmsSendResponse = new JSONObject();
		long lockSystemTimingInMinutesAfterIncorrectOtpAttempts = 0L;
		long countTotalSeconds = 0L;
		long minutes = 0L;
		long seconds = 0L;
		long days = 0L;
		long hours = 0L;
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = inputFormat.parse(userDob);
		UserDetails saveOtpInUserDetails = new UserDetails();
		List<UserDetails> authenticateUserList = userDetailsRepository.findByEmailIdAndDateOfBirth(userEmailId,
				parsedDate);
		Integer userListSize = authenticateUserList.size();
		if (authenticateUserList.isEmpty()) {
			object.put("message", "Fail");
			object.put("noUserPresent",
					"No user found with these details. Please enter correct email id and date of birth");
			return ResponseEntity.ok(object.toString());
		}
		if (authenticateUserList.get(0).getIncorrectOtpAttempts() == 5) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			Instant instant = authenticateUserList.get(0).getUserLockedTime().toInstant();
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			Duration duration = Duration.between(localDateTime, currentDateTime);
			days = duration.toDays();
			hours = duration.toHours() % 24;
			minutes = duration.toMinutes() % 60;
			seconds = duration.getSeconds() % 60;
			countTotalSeconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds;
			lockSystemTimingInMinutesAfterIncorrectOtpAttempts = (15 * 60);
			if (lockSystemTimingInMinutesAfterIncorrectOtpAttempts > countTotalSeconds) {
				object.put("message", "Locked");
				object.put("systemLockedForDuration",
						"Your account has been locked due to 5 wrong attempt(s). Please try to retrieve your login id after "
								+ (14 - minutes) + " minutes and " + (60 - seconds) + " seconds !");
				return ResponseEntity.ok(object.toString());
			}
		}
		if (userListSize == 1) {
			String otp = loginService.generateOTP();
			System.out.println("OTP : " + otp);
			object.put("message", "SUCCESS");
			object.put("otp", otp);
			saveOtpInUserDetails = authenticateUserList.get(0);
			saveOtpInUserDetails.setOtpEncd(adminUpdateUserDetails.encodeString(otp));
			saveOtpInUserDetails.setOtpSendingTime(new Date());
			saveOtpInUserDetails.setTotalOtpCount(authenticateUserList.get(0).getTotalOtpCount() + 1);
			userDetailsRepository.save(saveOtpInUserDetails);
			/******* Send Sms Start ********/
			if (smsServiceActiveOrNot) {
				SentSmsDetails sentSmsDetails = new SentSmsDetails();
				String searchString = "MsgID =";
				SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330761897010");
				String smsMsgBody = smsTemplate.getTemplate().replace("{#name#}",
						authenticateUserList.get(0).getFirstName());
				smsMsgBody = smsMsgBody.replace("{#otp#}", otp);
				String Response = smsService.sendSMS("1107170330761897010", smsMsgBody,
						authenticateUserList.get(0).getMobileNumber());
				String resultentMsgId = extractStringAfter(Response, searchString);
				if (resultentMsgId != null) {
					sentSmsDetails.setMsgId(resultentMsgId);
				} else {
					sentSmsDetails.setMsgId("Msg Id Not Present !");
				}
				sentSmsDetails.setMsgBody(smsMsgBody);
				sentSmsDetails.setResponse(Response);
				sentSmsDetails.setSentTime(new Date());
				sentSmsDetails.setSentTo(authenticateUserList.get(0).getMobileNumber());
				sentSmsDetails.setTemplateId("1107170330761897010");
				sendSmsDetailsRepository.save(sentSmsDetails);
			}
			/******* Send Sms End ********/
			return ResponseEntity.ok(object.toString());
		}
		if (authenticateUserList.get(0).getUserStatus().equalsIgnoreCase("inactive")) {
			object.put("message", "InActiveUser");
			object.put("inActiveUserMessage", "User Is Not An Active Person !");
			return ResponseEntity.ok(object.toString());
		} else {
			object.put("message", "Fail");
			return ResponseEntity.ok(object.toString());
		}
	}

	/********* Validtate email id and dob end ********/
	@GetMapping("/validateOtp")
	@ResponseBody
	public ResponseEntity<String> validateOtp(@RequestParam String usernameSelected,
			@RequestParam String passwordSelected, @RequestParam String input, @RequestParam String otp,
			@RequestParam String forgotDOBOtp) throws ParseException {
		JSONObject object = new JSONObject();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = inputFormat.parse(forgotDOBOtp);
		Boolean checkPoint = true;
		long countTotalSeconds = 0L;
		long lockSystemTimingInMinutesAfterIncorrectOtpAttempts = 0L;
		long minutes = 0L;
		long seconds = 0L;
		try {
			List<UserDetails> authenticateUserList = userDetailsRepository.findByLoginNameAndDateOfBirth(input,
					parsedDate);
			if (authenticateUserList.get(0).getOtpSendingTime() != null) {
				long otpExpireTime = 600L;
				LocalDateTime currentDateTimeToCheckOtpExpire = LocalDateTime.now();
				Instant instant = authenticateUserList.get(0).getOtpSendingTime().toInstant();
				LocalDateTime otpSentTimeToUser = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				Duration duration = Duration.between(otpSentTimeToUser, currentDateTimeToCheckOtpExpire);
				long otpExpireMinutes = duration.toMinutes() % 60;
				long otpExpireSeconds = duration.getSeconds() % 60;
				long totalSecondsOtpExpire = (otpExpireMinutes * 60) + otpExpireSeconds;
				if (totalSecondsOtpExpire > otpExpireTime) {
					object.put("message", "otpExpire");
					object.put("otpExpire", "OTP expired ! Please regenerate new otp .");
					return ResponseEntity.ok(object.toString());
				}
			}
			String ordinaryOtpToEncodedOtp = adminUpdateUserDetails.encodeString(otp);
			if (authenticateUserList.get(0).getUserLockedTime() != null) {
				LocalDateTime currentDateTime = LocalDateTime.now();
				Instant instant = authenticateUserList.get(0).getUserLockedTime().toInstant();
				LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				Duration duration = Duration.between(localDateTime, currentDateTime);
				long days = duration.toDays();
				long hours = duration.toHours() % 24;
				minutes = duration.toMinutes() % 60;
				seconds = duration.getSeconds() % 60;
				countTotalSeconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds;
				lockSystemTimingInMinutesAfterIncorrectOtpAttempts = (15 * 60);
				checkPoint = (lockSystemTimingInMinutesAfterIncorrectOtpAttempts < countTotalSeconds);
			}
			if (ordinaryOtpToEncodedOtp.equalsIgnoreCase(authenticateUserList.get(0).getOtpEncd()) && checkPoint) {
				object.put("message", "SUCCESS");
			} else {
				Integer wrongOtpAttemptsCounter = authenticateUserList.get(0).getIncorrectOtpAttempts() + 1;
				if (wrongOtpAttemptsCounter < maxIncorrectOtpAttempts) {
					Integer remainingOtpAttempts = maxIncorrectOtpAttempts - wrongOtpAttemptsCounter;
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					authenticateUserList.get(0).setOtpSendingTime(new Date());
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "Fail");
					object.put("remainingOtpAttempts", remainingOtpAttempts);
				} else if (wrongOtpAttemptsCounter == 5) {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					// authenticateUserList.get(0).setOtpSendingTime(new Date());
					authenticateUserList.get(0).setUserLockedTime(new Date());
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("remainingOtpAttempts", 0);
					object.put("message",
							"Your account has been locked due to 5 wrong attempt(s). Please try after 15 minutes");
				} else if ((lockSystemTimingInMinutesAfterIncorrectOtpAttempts > countTotalSeconds)) {
					object.put("message", "Locked");
					object.put("systemLockedForDuration", "Your account will be unlocked after " + (14 - minutes)
							+ "minutes and " + (60 - seconds) + "seconds !");
					return ResponseEntity.ok(object.toString());
				} else if (wrongOtpAttemptsCounter > maxIncorrectOtpAttempts && wrongOtpAttemptsCounter < 10) {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "UnLocked");
					object.put("userInActiveAfterLocked", "Your account will be inactive after "
							+ (10 - wrongOtpAttemptsCounter) + " wrong attempt(s) !");
				} else {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					authenticateUserList.get(0).setUserStatus("inactive");
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "InActive");
					object.put("userInActiveAfterLocked",
							"Your account has been made inactive due to wrong OTP entry multiple times. Please contact system administrator");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("AdminUserController : validateOtp : " + ex.getMessage());
		}
		return ResponseEntity.ok(object.toString());
	}

	/********** Validate UserName Otp Start ***********/
	@GetMapping("/validateUserNameOtp")
	@ResponseBody
	public ResponseEntity<String> validateUserNameOtp(@RequestParam String usernameSelected,
			@RequestParam String passwordSelected, @RequestParam String userEmailId, @RequestParam String otp,
			@RequestParam String forgotDOBOtp) throws ParseException {
		JSONObject object = new JSONObject();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = inputFormat.parse(forgotDOBOtp);
		Boolean checkPoint = true;
		long countTotalSeconds = 0L;
		long lockSystemTimingInMinutesAfterIncorrectOtpAttempts = 0L;
		long minutes = 0L;
		long seconds = 0L;
		try {
			List<UserDetails> authenticateUserList = userDetailsRepository.findByEmailIdAndDateOfBirth(userEmailId,
					parsedDate);
			if (authenticateUserList.get(0).getOtpSendingTime() != null) {
				long otpExpireTime = 600L;
				LocalDateTime currentDateTimeToCheckOtpExpire = LocalDateTime.now();
				Instant instant = authenticateUserList.get(0).getOtpSendingTime().toInstant();
				LocalDateTime otpSentTimeToUser = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				Duration duration = Duration.between(otpSentTimeToUser, currentDateTimeToCheckOtpExpire);
				long otpExpireMinutes = duration.toMinutes() % 60;
				long otpExpireSeconds = duration.getSeconds() % 60;
				long totalSecondsOtpExpire = (otpExpireMinutes * 60) + otpExpireSeconds;
				System.out.println("totalSecondsOtpExpire : " + totalSecondsOtpExpire);
				if (totalSecondsOtpExpire > otpExpireTime) {
					object.put("message", "otpExpire");
					object.put("otpExpire", "OTP expired ! Please regenerate new otp .");
					return ResponseEntity.ok(object.toString());
				}
			}
			String ordinaryOtpToEncodedOtp = adminUpdateUserDetails.encodeString(otp);
			if (authenticateUserList.get(0).getUserLockedTime() != null) {
				LocalDateTime currentDateTime = LocalDateTime.now();
				Instant instant = authenticateUserList.get(0).getUserLockedTime().toInstant();
				LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				Duration duration = Duration.between(localDateTime, currentDateTime);
				long days = duration.toDays();
				long hours = duration.toHours() % 24;
				minutes = duration.toMinutes() % 60;
				seconds = duration.getSeconds() % 60;
				countTotalSeconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds;
				lockSystemTimingInMinutesAfterIncorrectOtpAttempts = (15 * 60);
				System.out.println("Difference: " + days + " days, " + hours + " hours, " + minutes + " minutes, "
						+ seconds + " seconds");
				checkPoint = (lockSystemTimingInMinutesAfterIncorrectOtpAttempts < countTotalSeconds);
			}
			if (ordinaryOtpToEncodedOtp.equalsIgnoreCase(authenticateUserList.get(0).getOtpEncd()) && checkPoint) {
				/******* Send Sms Start ********/
				if (smsServiceActiveOrNot) {
					String officerFullName = authenticateUserList.get(0).getLoginName() + " "
							+ authenticateUserList.get(0).getMiddleName() + " "
							+ authenticateUserList.get(0).getLastName();
					SentSmsDetails sentSmsDetails = new SentSmsDetails();
					String searchString = "MsgID =";
					SMSTemplate smsTemplate = smsTemplateRepository.findTemplateByTemplateId("1107170330768005019");
					String smsMsgBody = smsTemplate.getTemplate().replace("{#name#}",
							authenticateUserList.get(0).getFirstName());
					smsMsgBody = smsMsgBody.replace("{#loginid#}", officerFullName);
					smsMsgBody = smsMsgBody.replace("{#password#}",
							new String(Base64.getDecoder().decode(authenticateUserList.get(0).getPassword())));
//					smsMsgBody = smsMsgBody.replace("{#otp#}", otp);
					String Response = smsService.sendSMS("1107170330768005019", smsMsgBody,
							authenticateUserList.get(0).getMobileNumber());
					String resultentMsgId = extractStringAfter(Response, searchString);
					if (resultentMsgId != null) {
						sentSmsDetails.setMsgId(resultentMsgId);
					} else {
						sentSmsDetails.setMsgId("Msg Id Not Present !");
					}
					sentSmsDetails.setMsgBody(smsMsgBody);
					sentSmsDetails.setResponse(Response);
					sentSmsDetails.setSentTime(new Date());
					sentSmsDetails.setSentTo(authenticateUserList.get(0).getMobileNumber());
					sentSmsDetails.setTemplateId("1107170330768005019");
					sendSmsDetailsRepository.save(sentSmsDetails);
				}
				/******* Send Sms End ********/
				String popUpString = "Dear \"" + authenticateUserList.get(0).getFirstName() + " "
						+ authenticateUserList.get(0).getMiddleName() + " " + authenticateUserList.get(0).getLastName()
						+ "\" your login id & password has been sent to your mob. no. ( "
						+ authenticateUserList.get(0).getMobileNumber() + " ) !";
				object.put("message", "SUCCESS");
				object.put("popUpInfo", popUpString);
			} else {
				Integer wrongOtpAttemptsCounter = authenticateUserList.get(0).getIncorrectOtpAttempts() + 1;
				if (wrongOtpAttemptsCounter < maxIncorrectOtpAttempts) {
					Integer remainingOtpAttempts = maxIncorrectOtpAttempts - wrongOtpAttemptsCounter;
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					authenticateUserList.get(0).setOtpSendingTime(new Date());
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "Fail");
					object.put("remainingOtpAttempts", remainingOtpAttempts);
				} else if (wrongOtpAttemptsCounter == 5) {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					// authenticateUserList.get(0).setOtpSendingTime(new Date());
					authenticateUserList.get(0).setUserLockedTime(new Date());
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("remainingOtpAttempts", 0);
					object.put("message",
							"Your account has been locked due to 5 wrong attempt(s). Please try to reset password after 15 minutes");
				} else if ((lockSystemTimingInMinutesAfterIncorrectOtpAttempts > countTotalSeconds)) {
					object.put("message", "Locked");
					object.put("systemLockedForDuration", "Your account will be unlocked after " + (14 - minutes)
							+ " minutes and " + (60 - seconds) + "	seconds");
					return ResponseEntity.ok(object.toString());
				} else if (wrongOtpAttemptsCounter > maxIncorrectOtpAttempts && wrongOtpAttemptsCounter < 10) {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "UnLocked");
					object.put("userInActiveAfterLocked", "Please enter correct OTP. Your account will be locked after "
							+ (10 - wrongOtpAttemptsCounter) + " wrong otp attempt(s) !");
				} else {
					authenticateUserList.get(0).setIncorrectOtpAttempts(wrongOtpAttemptsCounter);
					authenticateUserList.get(0).setUserStatus("inactive");
					userDetailsRepository.save(authenticateUserList.get(0));
					object.put("message", "InActive");
					object.put("userInActiveAfterLocked",
							"Your account has been made inactive due to wrong OTP entry multiple times. Please contact system administrator");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("AdminUserController : validateUserNameOtp : " + ex.getMessage());
		}
		return ResponseEntity.ok(object.toString());
	}

	/********** Validate UserName Otp End ***********/
	@PostMapping("/saveResetPassword")
	@ResponseBody
	public ResponseEntity<String> saveResetPassword(@RequestParam String username, @RequestParam String userdob,
			@RequestParam String passwordOnSubmit) throws ParseException {
		JSONObject object = new JSONObject();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parsedDate = inputFormat.parse(userdob);
			List<UserDetails> authenticateUserList = userDetailsRepository.findByLoginNameAndDateOfBirth(username,
					parsedDate);
			authenticateUserList.get(0).setPassword(passwordEncoder.encode(passwordOnSubmit));
			authenticateUserList.get(0).setUserStatus("active");
			authenticateUserList.get(0).setPasswordUpdationDate(new Date());
			UserDetails userDetails = userDetailsRepository.save(authenticateUserList.get(0));
			// Save user logs
			loginService.saveUserLogs(userDetails, "Password change : Forget Password");
			object.put("message", "SUCCESS");
			System.out.println("reached");
			return ResponseEntity.ok(object.toString());
		} catch (Exception ex) {
			object.put("message", "Fail");
			return ResponseEntity.ok(object.toString());
		}
	}

	@PostMapping("/saveUserNameResetPassword")
	@ResponseBody
	public ResponseEntity<String> saveUserNameResetPassword(@RequestParam String userEmailId,
			@RequestParam String userdob, @RequestParam String passwordOnSubmit) throws ParseException {
		JSONObject object = new JSONObject();
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parsedDate = inputFormat.parse(userdob);
			List<UserDetails> authenticateUserList = userDetailsRepository.findByEmailIdAndDateOfBirth(userEmailId,
					parsedDate);
			authenticateUserList.get(0).setPassword(passwordEncoder.encode(passwordOnSubmit));
			authenticateUserList.get(0).setUserStatus("active");
			authenticateUserList.get(0).setPasswordUpdationDate(new Date());
			UserDetails userDetails = userDetailsRepository.save(authenticateUserList.get(0));
			// Save user logs
			loginService.saveUserLogs(userDetails, "Password change : Forget Password");
			object.put("message", "SUCCESS");
			System.out.println("reached");
			return ResponseEntity.ok(object.toString());
		} catch (Exception ex) {
			object.put("message", "Fail");
			return ResponseEntity.ok(object.toString());
		}
	}
	/*
	 * @GetMapping("/logout") public String logout() { return "redirect:/login"; }
	 */
	/*
	 * @PostMapping("/sendSmsToUser")
	 * 
	 * @ResponseBody public ResponseEntity<String> sendSmsToUser(@RequestParam
	 * String username,@RequestParam String userdob) throws ParseException {
	 * JSONObject jsonObject = new JSONObject(); SMSTemplate smsTemplate =
	 * smsTemplateRepository.findTemplateByTemplateId("1107170330773594973");
	 * System.out.println("running !"); String modifiedString =
	 * smsTemplate.getTemplate().replace("{#name#}", "Gourav"); modifiedString =
	 * modifiedString.replace("{#otp#}", "1234");
	 * 
	 * 
	 * String Response =
	 * smsService.sendSMS("1107170330773594973",modifiedString,"9024884692");
	 * jsonObject.put("response", Response); return
	 * ResponseEntity.ok(Response.toString());
	 * 
	 * 
	 * }
	 */

	private static String extractStringAfter(String original, String searchString) {
		int index = original.indexOf(searchString);
		if (index != -1) {
			// Add the length of the searchString to get the substring after it
			return original.substring(index + searchString.length()).trim();
		} else {
			return null; // Substring not found
		}
	}
	// You can also handle the form submission and authentication here.
	// You may use Spring Security for a more advanced authentication process.
}
