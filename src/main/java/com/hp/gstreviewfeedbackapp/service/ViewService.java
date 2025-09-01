package com.hp.gstreviewfeedbackapp.service;

import java.util.List;

public interface ViewService {

	Long getDashBoardTotalRecovery(List<String> workingLocation);

	String getDashBoardTotalCases(List<String> workingLocation);

	String getDashBoardTotalAcknowledgedCases(List<String> workingLocation);

	String getDashBoardTotalInitiatedCases(List<String> workingLocation);

	String getDashBoardTotalCasesClosedByFo(List<String> workingLocation);

	Long getDashBoardTotalSuspectedIndicativeAmount(List<String> workingLocation);

	Long getDashBoardTotalAmount(List<String> workingLocation);

	Long getDashBoardTotalDemand(List<String> workingLocation);

}
