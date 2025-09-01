package com.hp.gstreviewfeedbackapp.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;

@Component
public class ApplicationInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationInterceptor.class);

	UrlPathHelper urlPathHelper = new UrlPathHelper();

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			logger.info("{Request created by logged in user with email id :: " + authentication.getName()
					+ ", IP address is " + request.getHeader("X-Forwarded-For") + " or " + request.getRemoteAddr()
					+ ",  Requested URI :: " + request.getRequestURI() + ", Request type :: " + request.getMethod()
					+ "}");
		} else {
			logger.info("{Request created from :: " + request.getHeader("X-Forwarded-For") + " or "
					+ request.getRemoteAddr() + ",  Requested URI :: " + request.getRequestURI() + ", Request type :: "
					+ request.getMethod() + "}");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

		response.setHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains");
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		response.setHeader("X-XSS-Protection", "1; mode=block");
		//
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		//
		response.setHeader("Content-Security-Policy",
				"script-src 'self' 'unsafe-inline' http://hpgstegov.hptax.gov.in https://www.gstatic.com https://www.gstatic.cn; form-action 'self' http://hpgstegov.hptax.gov.in; style-src 'self' 'unsafe-inline' http://hpgstegov.hptax.gov.in https://www.gstatic.com https://www.gstatic.cn; connect-src 'self' http://hpgstegov.hptax.gov.in;");
		// "script-src 'self' 'unsafe-inline'; form-action 'self'; style-src 'self'
		// 'unsafe-inline'; connect-src 'self';");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<UserDetails> object = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName());
			if (object.isPresent()) {
				UserDetails userDetails = object.get();
				if (userDetails.getUserStatus().equalsIgnoreCase("new")) {
					String encodedRedirectURL = response.encodeRedirectURL(request.getContextPath() + "/resetPassword");
					response.setStatus(HttpStatus.SC_TEMPORARY_REDIRECT);
					response.setHeader("Location", encodedRedirectURL);
				}
			}
		}
	}

}
