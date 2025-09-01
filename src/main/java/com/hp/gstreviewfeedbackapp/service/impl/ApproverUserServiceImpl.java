package com.hp.gstreviewfeedbackapp.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.controller.FieldUserController;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.repository.ApproverRemarksToRejectTheCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.service.ApproverUserService;

@Service
public class ApproverUserServiceImpl implements ApproverUserService {
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkFlowRepository;
	@Autowired
	private FieldUserController fieldUserController;
	@Autowired
	private ApproverRemarksToRejectTheCaseRepository approverRemarksToRejectTheCaseRepository;
	@Value("${maximunIndicativeTaxValueForZoneLevelAp}")
	private Long maximunIndicativeTaxValueForZoneLevelAp;

	@Override
	public Page<EnforcementReviewCase> findAllDataListByDefault(Pageable pageRequest, String actionStatus,
			Boolean hasStateAccess, List<String> allMappedLocations) {
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			Page<EnforcementReviewCase> enforcementApproverList = null;
			if (hasStateAccess) {
				enforcementApproverList = enforcementReviewCaseRepository
						.verifierRecommendedCasesListForStateLevelByAction(pageRequest, actionStatus,
								maximunIndicativeTaxValueForZoneLevelAp);
			} else {
				enforcementApproverList = enforcementReviewCaseRepository
						.verifierRecommendedCasesListForZoneLevelByAction(pageRequest, actionStatus,
								maximunIndicativeTaxValueForZoneLevelAp, allMappedLocations);
			}
			for (EnforcementReviewCase enforceSoloToAttachFile : enforcementApproverList) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(URLEncoder
						.encode(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate)));
				List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
						.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
						.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<String> remarkList = new ArrayList<>();
				List<String> apRemarkList = new ArrayList<>();
				for (CaseWorkflow str : ruRemarksWithAppealRevision) {
					if (str.getOtherRemarks() != null) {
						remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
					} else {
						remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
								+ ruDateFormat.format(str.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
				for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
					if (apRemarks.getOtherRemarks() != null) {
						apRemarkList.add(apRemarks.getOtherRemarks() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					} else {
						apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile
						.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			return enforcementApproverList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<EnforcementReviewCase> findAllDataListByDefault(Pageable pageRequest) {
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			Page<EnforcementReviewCase> enforcementApproverList = enforcementReviewCaseRepository
					.verifierRecommendedCasesList(pageRequest);
			for (EnforcementReviewCase enforceSoloToAttachFile : enforcementApproverList) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(URLEncoder
						.encode(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate)));
				List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
						.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
						.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<String> remarkList = new ArrayList<>();
				List<String> apRemarkList = new ArrayList<>();
				for (CaseWorkflow str : ruRemarksWithAppealRevision) {
					if (str.getOtherRemarks() != null) {
						remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
					} else {
						remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
								+ ruDateFormat.format(str.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
				for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
					if (apRemarks.getOtherRemarks() != null) {
						apRemarkList.add(apRemarks.getOtherRemarks() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					} else {
						apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile
						.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
			return enforcementApproverList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<EnforcementReviewCase> findAllDataListBySearchValue(String searchValue, Pageable pageRequest) {
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Page<EnforcementReviewCase> enforcementApproverList = null;
		try {
			enforcementApproverList = enforcementReviewCaseRepository
					.verifierRecommendedCasesList(searchValue.trim().toLowerCase(), pageRequest); // display cases to
																									// approver which is
																									// at state level
			for (EnforcementReviewCase enforceSoloToAttachFile : enforcementApproverList) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(URLEncoder
						.encode(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate)));
				List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
						.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
						.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<String> remarkList = new ArrayList<>();
				List<String> apRemarkList = new ArrayList<>();
				for (CaseWorkflow str : ruRemarksWithAppealRevision) {
					if (str.getOtherRemarks() != null) {
						remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
					} else {
						remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
								+ ruDateFormat.format(str.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
				for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
					if (apRemarks.getOtherRemarks() != null) {
						apRemarkList.add(apRemarks.getOtherRemarks() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					} else {
						apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile
						.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		return enforcementApproverList;
	}

	@Override
	public Page<EnforcementReviewCase> findAllDataListBySearchValue(String searchValue, Pageable pageRequest,
			String actionStatus, Boolean hasStateAccess, List<String> allMappedLocations) {
		SimpleDateFormat ruDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Page<EnforcementReviewCase> enforcementApproverList = null;
		try {
			if (hasStateAccess) {
				enforcementApproverList = enforcementReviewCaseRepository
						.verifierRecommendedCasesListForStateLevelByAction(searchValue.trim().toLowerCase(),
								pageRequest, actionStatus, maximunIndicativeTaxValueForZoneLevelAp);
			} else {
				enforcementApproverList = enforcementReviewCaseRepository
						.verifierRecommendedCasesListForZoneLevelByAction(searchValue.trim().toLowerCase(), pageRequest,
								actionStatus, maximunIndicativeTaxValueForZoneLevelAp, allMappedLocations);
			}
			// display
			// cases
			// to
			// approver which is
			// at state level
			for (EnforcementReviewCase enforceSoloToAttachFile : enforcementApproverList) {
				String gstin = enforceSoloToAttachFile.getId().getGSTIN();
				String period = enforceSoloToAttachFile.getId().getPeriod();
				Date caseReportingDate = enforceSoloToAttachFile.getId().getCaseReportingDate();
				enforceSoloToAttachFile.setFileName(URLEncoder
						.encode(enforcementReviewCaseRepository.findFileNameByGstin(gstin, period, caseReportingDate)));
				List<CaseWorkflow> ruRemarksWithAppealRevision = caseWorkFlowRepository
						.getVerifierRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<CaseWorkflow> apRemarksWithAppealRevision = caseWorkFlowRepository
						.getApproverRemarksWithAppealRevision(gstin, period, caseReportingDate);
				List<String> remarkList = new ArrayList<>();
				List<String> apRemarkList = new ArrayList<>();
				for (CaseWorkflow str : ruRemarksWithAppealRevision) {
					if (str.getOtherRemarks() != null) {
						remarkList.add(str.getOtherRemarks() + " (" + ruDateFormat.format(str.getUpdatingDate()) + ")");
					} else {
						remarkList.add(str.getVerifierRaiseQueryRemarks().getName() + " ("
								+ ruDateFormat.format(str.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile.setRemark((remarkList != null && remarkList.size() > 0) ? remarkList : null);
				for (CaseWorkflow apRemarks : apRemarksWithAppealRevision) {
					if (apRemarks.getOtherRemarks() != null) {
						apRemarkList.add(apRemarks.getOtherRemarks() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					} else {
						apRemarkList.add(apRemarks.getApproverRemarksToRejectTheCase().getName() + " ("
								+ ruDateFormat.format(apRemarks.getUpdatingDate()) + ")");
					}
				}
				enforceSoloToAttachFile
						.setApRemarks((apRemarkList != null && apRemarkList.size() > 0) ? apRemarkList : null);
				if (enforceSoloToAttachFile.getParameter() != null) {
					enforceSoloToAttachFile
							.setParameter(fieldUserController.getParameterName(enforceSoloToAttachFile.getParameter()));
				} else {
					enforceSoloToAttachFile.setParameter("");
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		return enforcementApproverList;
	}
}
