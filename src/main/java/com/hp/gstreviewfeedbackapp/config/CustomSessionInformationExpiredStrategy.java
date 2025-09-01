package com.hp.gstreviewfeedbackapp.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

	private static final Logger logger = LoggerFactory.getLogger(CustomSessionInformationExpiredStrategy.class);

	private String expiredUrl = "";

	public CustomSessionInformationExpiredStrategy(String expiredUrl) {
		super();
		this.expiredUrl = expiredUrl;
	}

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

		logger.info("Redirecting to session expired page");
		HttpServletRequest request = event.getRequest();
		HttpServletResponse response = event.getResponse();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		request.getSession();// creates a new session
		response.sendRedirect(request.getContextPath() + expiredUrl);

	}
}