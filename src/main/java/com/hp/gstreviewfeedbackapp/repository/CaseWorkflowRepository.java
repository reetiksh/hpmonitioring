package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;

@Repository
public interface CaseWorkflowRepository extends JpaRepository<CaseWorkflow, Long> {

	@Query(value = "select * from analytics.case_workflow where gstin = ?1 and action = 'upload' \r\n"
			+ "and period = ?3 and case_reporting_date = ?2 order by id desc limit 1", nativeQuery = true)
	CaseWorkflow findHQLocationDetails(String gstin, Date reportDate, String period);

	@Query(value = "select cw.assigned_to_user_id  from analytics.case_workflow cw where cw.gstin =?1 and cw.\"action\" ='acknowledge' order by cw.id desc limit 1", nativeQuery = true)
	Integer findFoIdFromCaseWorkFlow(String gstinNo);

	@Query(value = "select * from analytics.case_workflow cw where cw.gstin=?1 and cw.\"period\"=?2 and cw.case_reporting_date=?3 \r\n"
			+ "and cw.\"action\"=?4 order by cw.id desc limit 1", nativeQuery = true)
	List<CaseWorkflow> findAppealRevisionCaseFile(String gstin, String period, Date caseReportingDate,
			String caseAction);

	@Query(value = "select * from analytics.case_workflow cw where cw.gstin=?1 and cw.period=?2 and cw.case_reporting_date=?3\r\n"
			+ "			and cw.assigned_from='AP' and cw.assigned_to='RU' and cw.action=?4 order by cw.id desc limit 1", nativeQuery = true)
	List<CaseWorkflow> findAppealRevisionApproverRemarks(String gstin, String period, Date caseReportingDate,
			String caseAction);

	@Query(value = "select * from analytics.case_workflow cw where cw.gstin=?1 and cw.case_reporting_date=?3 \r\n"
			+ "and cw.\"period\"=?2 and cw.\"action\" in('verifyerRaiseQuery','ruAppeal','ruRevision') \r\n"
			+ "order by cw.updating_date desc", nativeQuery = true)
	List<CaseWorkflow> getVerifierRemarksWithAppealRevision(String gstin, String period, Date caseReportingDate);

	@Query(value = "select * from analytics.case_workflow cw where cw.gstin=?1 and cw.case_reporting_date=?3 \r\n"
			+ "and cw.\"period\"=?2 and cw.\"action\" in('apApproveAppeal','apApproveRevision','approverRaiseQuery','apRejectAppealRevision') \r\n"
			+ "order by cw.updating_date desc", nativeQuery = true)
	List<CaseWorkflow> getApproverRemarksWithAppealRevision(String gstin, String period, Date caseReportingDate);

	@Query(value = "select * from analytics.case_workflow cw where cw.gstin=?1 and cw.case_reporting_date=?3 and cw.period=?2", nativeQuery = true)
	List<CaseWorkflow> findAllByGstinPeriodCaseReportingDate(String GSTIN, String period, Date caseReportingDate);

	@Query(value = "select cw.reverted_case_file_path  from analytics.case_workflow cw where cw.gstin=?1 and cw.period=?2 and cw.case_reporting_date=?3 \r\n"
			+ "and cw.action='approverRaiseQuery' order by cw.id desc limit 1", nativeQuery = true)
	String returnFilePathForRevertedCaseByApprover(String GSTIN, String period, Date caseReportingDate);

	@Query(value = "select * from analytics.case_workflow cw where gstin = ?1 and period = ?2 and case_reporting_date = ?3 \r\n"
			+ "and action = 'hqTransfer' order by id desc limit 1", nativeQuery = true)
	CaseWorkflow getTransferRemarks(String gstin, String period, Date caseReportingDate);

}
