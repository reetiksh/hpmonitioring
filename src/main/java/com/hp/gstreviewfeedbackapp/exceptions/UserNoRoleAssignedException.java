package com.hp.gstreviewfeedbackapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNoRoleAssignedException extends AuthenticationException {

	private static final long serialVersionUID = -1213316466774145041L;

	public UserNoRoleAssignedException(String msg) {
		super(msg);
	}

}
