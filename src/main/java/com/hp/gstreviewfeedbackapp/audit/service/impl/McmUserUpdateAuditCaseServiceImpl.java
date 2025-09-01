package com.hp.gstreviewfeedbackapp.audit.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.service.AuditCaseDocumentService;
import com.hp.gstreviewfeedbackapp.audit.service.McmUserUpdateAuditCaseService;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;

@Service
public class McmUserUpdateAuditCaseServiceImpl implements McmUserUpdateAuditCaseService {
	private static final Logger logger = LoggerFactory.getLogger(McmUserUpdateAuditCaseServiceImpl.class);
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private AuditCaseDocumentService auditCaseDocumentService;
	@Autowired
	private L2UserCaseAssignmentServiceImpl l2UserCaseAssignmentServiceImpl;

	@Override
	public String saveDarDetails(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Date lastUpdatedTimestamp = new Date();
			AuditMaster auditMaster = auditMasterRepository.findByCaseIdAndAction(l3UserAuditCaseUpdate.getCaseId(),
					auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get()).get();
			boolean rejectDarFlag = false;
			Optional<AuditCaseDateDocumentDetails> auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
					.findByCaseIdAndAction(auditMaster, auditMaster.getAction());
			if (auditCaseDateDocumentDetails.isPresent()) {
				List<AuditCaseDarDetails> auditCaseDarDetailsList = null;
				if (l3UserAuditCaseUpdate.getNilDar().trim().equals("NO")) {
					auditCaseDarDetailsList = auditCaseDateDocumentDetails.get().getAuditCaseDarDetailsList();
					for (List<String> strList : l3UserAuditCaseUpdate.getPara()) {
						AuditCaseDarDetails object = auditCaseDarDetailsList.stream()
								.filter(AuditCaseDarDetails -> AuditCaseDarDetails.getId()
										.equals(Long.parseLong(strList.get(0))))
								.findFirst().orElse(null);
						int size = strList.size();
//						if (rejectDarFlag == false && size > 2) {
//							rejectDarFlag = true;
//						}
						object.setCommentMcm(size > 1 ? strList.get(1) : null);
						object.setRaiseQuery(size > 2 ? strList.get(2) : null);
						object.setLastUpdatedTimeStamp(
								size > 1 ? lastUpdatedTimestamp : object.getLastUpdatedTimeStamp());
					}
				}
				if (l3UserAuditCaseUpdate.getDarApproval() != null && l3UserAuditCaseUpdate.getDarApproval()
						.equalsIgnoreCase("recommendation for raising query")) {
					rejectDarFlag = true;
				}
//				auditCaseDateDocumentDetails.get().setCommentFromMcmOfficer(
//						l3UserAuditCaseUpdate.getComment() != null ? l3UserAuditCaseUpdate.getComment()
//								: auditCaseDateDocumentDetails.get().getCommentFromL2Officer());
				if (rejectDarFlag == true) {
					auditCaseDateDocumentDetails.get()
							.setAction(auditCaseStatusRepository.findByStatus("recommendedForRaiseQuery").get());
				} else {
					auditCaseDateDocumentDetails.get()
							.setAction(auditCaseStatusRepository.findByStatus("recommendedForApproval").get());
				}
				auditCaseDateDocumentDetails.get().setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				auditCaseDateDocumentDetails.get().setLastUpdatedTimeStamp(lastUpdatedTimestamp);
				auditCaseDateDocumentDetails.get().setCommentFromMcmOfficer(l3UserAuditCaseUpdate.getComment());
				auditCaseDateDocumentDetailsRepository.save(auditCaseDateDocumentDetails.get());
				auditMaster = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId()).get();
				auditMaster.setAction(auditCaseDateDocumentDetails.get().getAction());
				auditMaster.setAssignedFrom("MCM");
				auditMaster.setAssignTo("L2");
				auditMaster.setLastUpdatedTimeStamp(lastUpdatedTimestamp);
//				
				auditMasterRepository.save(auditMaster);
				// Save MCM user details to Audit master allocating user
				AuditMasterCasesAllocatingUsers allocatingUsers = auditMasterCasesAllocatingUsersRepository
						.findById(auditMaster.getCaseId()).get();
				allocatingUsers.setMcmUser(objectUserDetails.get().getUserId());
				auditMasterCasesAllocatingUsersRepository.save(allocatingUsers);
				// save Case workflow logs
				l2UserCaseAssignmentServiceImpl.saveAuditMasterCaseWorkflowDetails(auditMaster,
						objectUserDetails.get().getUserId(),
						auditMasterCasesAllocatingUsersRepository.findById(auditMaster.getCaseId()).get().getL2User());
				// save Document logs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails.get(),
						objectUserDetails.get());
				// save DAR details
				if (auditCaseDarDetailsList != null && auditCaseDarDetailsList.size() > 0) {
					auditCaseDocumentService.saveAuditCaseDarDetailsLogs(auditCaseDarDetailsList,
							objectUserDetails.get(), "updated by MCM");
				}
				return auditMaster.getAction().getSuccessMesage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("McmUserUpdateAuditCaseServiceImpl : saveDarDetails :" + e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations) {

        List<Object[]> rawData = auditMasterRepository.getAnnexureReportRaw(allMappedLocations);
        List<AnnexureReportRow> result = new ArrayList<>();

        for (Object[] row : rawData) {
            AnnexureReportRow dto = new AnnexureReportRow();
            dto.setZoneName((String) row[0]);
            dto.setCircleName((String) row[1]);
            dto.setAllottedCases(((Number) row[2]).intValue());
            dto.setAuditCasesCompleted(((Number) row[3]).intValue());
            dto.setPendingDeskReview(((Number) row[4]).intValue());
            dto.setPendingApprovalAuditPlan(((Number) row[5]).intValue());
            dto.setPendingExaminationBooks(((Number) row[6]).intValue());
            dto.setPendingDAR(((Number) row[7]).intValue());
            dto.setPendingFAR(((Number) row[8]).intValue());
            result.add(dto);
        }
        return result;
    
	}
	@Override
	public	List<String> getDashboardData(List<String> allMappedLocations){

        List<Object[]> resultList = auditMasterRepository.getDashboardMetrics(allMappedLocations);

        if (resultList != null && !resultList.isEmpty()) {
            Object[] result = resultList.get(0);
            List<String> dashboardData = new ArrayList<>();

            for (Object obj : result) {
                dashboardData.add(obj != null ? obj.toString() : "0");
            }
            return dashboardData;
        }

        return Arrays.asList("0", "0", "0", "0", "0", "0");
	}
}
