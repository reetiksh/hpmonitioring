package com.hp.gstreviewfeedbackapp.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomeErrorController implements ErrorController {
	private static final Logger logger = LoggerFactory.getLogger(CustomeErrorController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				logger.error("Error thrown with status code ::" + statusCode + " where requested URI :: "
						+ request.getPathInfo());
				return "error/404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				logger.error("Error thrown with status code ::" + statusCode + " where requested URI :: "
						+ request.getPathInfo());
				return "error/500";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				logger.error("Error thrown with status code ::" + statusCode + " where requested URI :: "
						+ request.getPathInfo());
				return "error/403";
			} else {
				logger.error("Error thrown with status code ::" + statusCode.toString() + " where requested URI :: "
						+ request.getPathInfo());
				return "error/error";
			}
		}
		return "error/error";
	}
}
