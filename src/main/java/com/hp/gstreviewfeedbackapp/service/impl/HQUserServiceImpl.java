package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.data.CaseLogHistory;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.HQUserService;

@Service
public class HQUserServiceImpl implements HQUserService {
	private static final Logger logger = LoggerFactory.getLogger(HQUserServiceImpl.class);
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Override
	public List<CaseLogHistory> importDataFromCaseWorkflowToCaseLogHistory(List<CaseWorkflow> caseWorkflowList,
			CompositeKey compositeKey) {
		List<CaseLogHistory> caseLogHistoryList = new ArrayList<>();
		try {
			Optional<EnforcementReviewCase> ercObject = enforcementReviewCaseRepository.findById(compositeKey);
			EnforcementReviewCase erc = (ercObject.isPresent() ? ercObject.get() : null);
			for (CaseWorkflow caseWorkflow : caseWorkflowList) {
				CaseLogHistory caseLogHistory = new CaseLogHistory();
				caseLogHistory.setGSTIN(caseWorkflow.getGSTIN());
				caseLogHistory.setPeriod(caseWorkflow.getPeriod());
				caseLogHistory.setCaseReportingDate(caseWorkflow.getCaseReportingDate());
				caseLogHistory.setAssignedFrom(
						(caseWorkflow.getAssignedFrom() != null && caseWorkflow.getAssignedFrom().trim().length() > 0)
								? (userRoleRepository.findByroleCode(caseWorkflow.getAssignedFrom().trim()).isPresent()
										? userRoleRepository.findByroleCode(caseWorkflow.getAssignedFrom().trim()).get()
												.getRoleName()
										: caseWorkflow.getAssignedFrom().trim())
								: "");
				caseLogHistory.setAssignedTo(
						(caseWorkflow.getAssignedTo() != null && caseWorkflow.getAssignedTo().trim().length() > 0)
								? (userRoleRepository.findByroleCode(caseWorkflow.getAssignedTo().trim()).isPresent()
										? userRoleRepository.findByroleCode(caseWorkflow.getAssignedTo().trim()).get()
												.getRoleName()
										: caseWorkflow.getAssignedTo().trim())
								: "");
				caseLogHistory.setUpdatingDate(caseWorkflow.getUpdatingDate());
				caseLogHistory.setAction(caseWorkflow.getAction());
				caseLogHistory.setSuggestedJurisdiction((caseWorkflow.getSuggestedJurisdiction() != null
						&& caseWorkflow.getSuggestedJurisdiction().trim().length() > 0)
								? (locationDetailsRepository.findById(caseWorkflow.getSuggestedJurisdiction().trim())
										.isPresent()
												? locationDetailsRepository
														.findById(caseWorkflow.getSuggestedJurisdiction().trim()).get()
														.getLocationName()
												: caseWorkflow.getSuggestedJurisdiction().trim())
								: "");
				caseLogHistory.setTransferRemarks(
						caseWorkflow.getRemarks() != null ? caseWorkflow.getRemarks().getName() : "");
				caseLogHistory.setOtherRemarks(caseWorkflow.getOtherRemarks());
				caseLogHistory.setAssignedFromUser(caseWorkflow.getAssignedFromUserId() > 0
						? userDetailsRepository.getFullNameById(caseWorkflow.getAssignedFromUserId()).get()
						: "");
				caseLogHistory.setAssigntoUser(caseWorkflow.getAssigntoUserId() > 0
						? userDetailsRepository.getFullNameById(caseWorkflow.getAssigntoUserId()).get()
						: "");
				caseLogHistory.setAssignedFromLocation((caseWorkflow.getAssignedFromLocationId() != null
						&& caseWorkflow.getAssignedFromLocationId().trim().length() > 0)
								? locationDetailsRepository.findById(caseWorkflow.getAssignedFromLocationId()).get()
										.getLocationName()
								: "");
				caseLogHistory.setAssignedToLocation(
						caseWorkflow.getAssignedToLocationId() != null
								? locationDetailsRepository.findById(caseWorkflow.getAssignedToLocationId()).get()
										.getLocationName()
								: "");
				caseLogHistory.setVerifierRaiseQueryRemarks(caseWorkflow.getVerifierRaiseQueryRemarks() != null
						? caseWorkflow.getVerifierRaiseQueryRemarks().getName()
						: "");
				caseLogHistory
						.setApproverRemarksToRejectTheCase(caseWorkflow.getApproverRemarksToRejectTheCase() != null
								? caseWorkflow.getApproverRemarksToRejectTheCase().getName()
								: "");
				if (erc != null
						&& !(caseWorkflow.getAction().equals("upload") || caseWorkflow.getAction().equals("acknowledge")
								|| caseWorkflow.getAction().equals("transfer"))) {
					caseLogHistory.setTaxpayerName(erc.getTaxpayerName());
					caseLogHistory.setIndicativeTaxValue(erc.getIndicativeTaxValue());
					caseLogHistory.setCaseId(erc.getCaseId());
					caseLogHistory.setCaseStage(erc.getCaseStage() != null ? erc.getCaseStage().getName() : null);
					caseLogHistory.setCaseStageArn(erc.getCaseStageArn());
					caseLogHistory.setDemand(erc.getDemand());
					caseLogHistory
							.setRecoveryStage(erc.getRecoveryStage() != null ? erc.getRecoveryStage().getName() : null);
					caseLogHistory.setRecoveryStageArn(erc.getRecoveryStageArn());
					caseLogHistory.setRecoveryByDRC3(erc.getRecoveryByDRC3());
					caseLogHistory.setRecoveryAgainstDemand(erc.getRecoveryAgainstDemand());
				} else if (erc != null) {
					caseLogHistory.setTaxpayerName(erc.getTaxpayerName());
					caseLogHistory.setIndicativeTaxValue(erc.getIndicativeTaxValue());
					caseLogHistory.setCategory(erc.getCategory());
				}
				caseLogHistoryList.add(caseLogHistory);
			}
		} catch (Exception e) {
			logger.error(".HQUserServiceImpl : importDataFromCaseWorkflowToCaseLogHistory : " + e.getMessage());
			e.printStackTrace();
		}
		return caseLogHistoryList;
	}
}
