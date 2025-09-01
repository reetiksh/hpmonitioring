package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.exceptions.UserAccountLockedException;
import com.hp.gstreviewfeedbackapp.exceptions.UserNoRoleAssignedException;
import com.hp.gstreviewfeedbackapp.exceptions.UserPasswordExpireException;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.service.LoginService;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginServiceImpl loginService;

	@Value("${login.accountLockedTimingInMinutes}")
	private String lockedTiming;

	@Value("${login.NewUser}")
	private String newUserStatus;

	@Value("${login.UserActive}")
	private String userActiveStatus;

	@Value("${login.UserInactive}")
	private String userInactiveStatus;

	@Value("${login.passwordExpiryDate}")
	private String paswordExpiryDays;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (name != null && password != null) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				// Check user is active or new
				if (!userDetails.getUserStatus().equalsIgnoreCase(newUserStatus)
						&& !userDetails.getUserStatus().equalsIgnoreCase(userActiveStatus)) {
					throw new UserAccountLockedException(
							name + " is " + userDetails.getUserStatus() + ". Please contact with ADMIN further!");
				}
				List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
						.findByUserDetailsOrderByUserRole(userDetails);
				if (passwordEncoder.matches(password, userDetails.getPassword())) {
					if (userDetails.getWrongPasswordAttempt() == null) {
						userDetails.setWrongPasswordAttempt(0);
					}
					// check user locked timing
					if (userDetails.getWrongPasswordAttempt() == 5 && userDetails.getUserLockedTime() != null
							&& !loginService.checkTimeExpiry(userDetails.getUserLockedTime(),
									Integer.parseInt(lockedTiming))) {
						Date systemDateTime = new Date();
						long difference_In_Time = systemDateTime.getTime() - userDetails.getUserLockedTime().getTime(); // in
																														// millisecond
						difference_In_Time = (Integer.parseInt(lockedTiming) * 60 * 1000) - difference_In_Time;
						long difference_In_Seconds = (difference_In_Time / 1000) % 60;
						long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
						throw new UserAccountLockedException("Your Account " + userDetails.getLoginName()
								+ " has been locked for more " + difference_In_Minutes + " minutes and "
								+ difference_In_Seconds + " seconds !");
					}

					// Checking the user is new
					if (userDetails.getUserStatus().equalsIgnoreCase(newUserStatus)) {

						// checking user have any role
						if (userRoleMappings != null && userRoleMappings.size() > 0) {

							Set<GrantedAuthority> authorities = userRoleMappings.stream().map(
									(role) -> new SimpleGrantedAuthority("ROLE_" + role.getUserRole().getRoleCode()))
									.collect(Collectors.toSet());
							return new UsernamePasswordAuthenticationToken(name, password, authorities);
						} else {
							throw new UserNoRoleAssignedException(name
									+ " login id does not have any role assigned. Please contact system administrator for role assignment.");
						}
					}

					// Checking the user trying to login with in 90days from last login date
					if (userDetails.getLastLoginDate() != null
							&& !loginService.checkDateExpiry(userDetails.getLastLoginDate(), 90)
							&& !userDetails.getUserStatus().equalsIgnoreCase(userInactiveStatus)) {
						userDetails.setUserStatus(userInactiveStatus);
						userDetailsRepository.save(userDetails);
						throw new UserAccountLockedException(
								name + " is inactive for more than 3 months. Please contact with ADMIN further!");
					}

					// Checking if user is inactive
//					if (userDetails.getUserStatus().equalsIgnoreCase(userInactiveStatus)) {
//						throw new UserAccountLockedException(name + " is inactive. Please contact with ADMIN further!");
//					}

					// Checking if password is expired or not
					if (!loginService.checkDateExpiry(userDetails.getPasswordUpdationDate(),
							Integer.parseInt(paswordExpiryDays))) {
						throw new UserPasswordExpireException("You password has been expired, please reset.");
					}

					// User have role or not
					if (userRoleMappings != null && userRoleMappings.size() > 0) {

						// All Roles of the user
						Map<String, UserRole> userRoles = new HashMap<>();
						for (UserRoleMapping objectUrm : userRoleMappings) {
							if (userRoles.get(objectUrm.getUserRole().getRoleName()) == null) {
								userRoles.put(objectUrm.getUserRole().getRoleName(), objectUrm.getUserRole());
							}
						}
						// session.setAttribute("LoggedInUserRoles", new TreeMap<>(userRoles));
						if (userDetails.getWrongPasswordAttempt() != 0) {
							userDetails.setWrongPasswordAttempt(0);

							if (userDetails.getUserLockedTime() != null) {
								userDetails.setUserLockedTime(null);
							}
						}
						userDetails.setLastLoginDate(new Date());
						userDetailsRepository.save(userDetails);

						Set<GrantedAuthority> authorities = userRoleMappings.stream()
								.map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getUserRole().getRoleCode()))
								.collect(Collectors.toSet());
						return new UsernamePasswordAuthenticationToken(name, password, authorities);

					} else {
						if (userDetails.getWrongPasswordAttempt() != 0) {
							userDetails.setWrongPasswordAttempt(0);
							userDetailsRepository.save(userDetails);
						}
						throw new UserNoRoleAssignedException(name
								+ " login id does not have any role assigned. Please contact system administrator for role assignment.");
					}

				} else {
					if (userDetails.getWrongPasswordAttempt() < 10) {
						userDetails.setWrongPasswordAttempt(userDetails.getWrongPasswordAttempt() + 1);
						userDetailsRepository.save(userDetails);
					}

					// After 5 login Attempt with wrong password the user will be locked and after
					// unlock more 5 login attempt the user will be inactivated
					if (userDetails.getWrongPasswordAttempt() == 5) {
						userDetails.setUserLockedTime(new Date());
						userDetailsRepository.save(userDetails);
					} else if (userDetails.getWrongPasswordAttempt() >= 10) {
						userDetails.setUserStatus(userInactiveStatus);
						userDetailsRepository.save(userDetails);
					}
					String error_meaasge = null;
					// Error messages for incorrect password
					if (userDetails.getWrongPasswordAttempt() > 2 && userDetails.getWrongPasswordAttempt() < 5) {
						error_meaasge = "Please enter correct password. ! Attempt Remaining "
								+ (5 - userDetails.getWrongPasswordAttempt());

					} else if (userDetails.getWrongPasswordAttempt() == 5) {
						error_meaasge = "Your Account " + userDetails.getLoginName() + " has been locked for "
								+ lockedTiming + " minutes !";
					} else if (userDetails.getWrongPasswordAttempt() > 5
							&& userDetails.getWrongPasswordAttempt() < 10) {
						error_meaasge = "Please enter correct password. ! You account will be parmanently locked after "
								+ (10 - userDetails.getWrongPasswordAttempt()) + " wrong attmepts";
					} else if (userDetails.getWrongPasswordAttempt() >= 10) {
						error_meaasge = "Your Account " + userDetails.getLoginName()
								+ " has been inactivated. Please contact with ADMIN !";
					} else {
						error_meaasge = "Please enter correct password. !";
					}
					throw new UserPasswordExpireException(error_meaasge);
				}

			} else {
				throw new UsernameNotFoundException("User does not exist. Please enter a valid login id.");
			}
		}
		throw new UsernameNotFoundException("Username or password empty.");
	}
	//////////////////////////

	/*
	 * if (userEx.isPresent()) { Set<GrantedAuthority> authorities =
	 * userEx.get().getUserRoleMappings().stream() .map((role) -> new
	 * SimpleGrantedAuthority("ROLE_" + role.getUserRole().getRoleCode()))
	 * .collect(Collectors.toSet()); return new
	 * UsernamePasswordAuthenticationToken(name, password, authorities); } else {
	 * throw new
	 * UsernameNotFoundException("User you are looking for does not exist."); }
	 */

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
