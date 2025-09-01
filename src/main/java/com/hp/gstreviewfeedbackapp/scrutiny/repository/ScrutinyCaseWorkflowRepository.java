package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;

@Repository
public interface ScrutinyCaseWorkflowRepository extends JpaRepository<ScrutinyCaseWorkflow, Long> {

	@Query(value = "select * from analytics.scrutiny_case_workflow scw  where scw.gstin=?1 and scw.case_reporting_date =?3 and scw.\"period\" =?2 and scw.\"action\" = 'requestForTransfer' order by scw.updating_date desc limit 1", nativeQuery = true)
	ScrutinyCaseWorkflow getAssignToLoationId(String gstin, String period, Date caseReportingDate);

	@Query(value = "select scw.other_remarks || ' (' || TO_CHAR(scw.updating_date , 'DD-MM-YYYY HH24:MI:SS') || ')' AS other_remarks_with_date from analytics.scrutiny_case_workflow scw where scw.gstin = ?1  and scw.case_reporting_date = ?3 and scw.\"period\" = ?2 "
			+ "and scw.assigned_from='SRU' and scw.assigned_to = 'SFO' and scw.\"action\" = 'recommendForScrutiny' order by scw.updating_date desc", nativeQuery = true)
	List<String> getScrutinyVerifierRemarks(String gstin, String period, Date caseReportingDate);

	
	@Query(value="select scw.file_path from analytics.scrutiny_case_workflow scw where scw.gstin=?1 and scw.case_reporting_date=?2 and scw.\"period\"=?3  \r\n"
			+ "and scw.assigned_from = 'SFO' and scw.assigned_to='SRU' and scw.\"action\"in('noNeedScrutiny','closureReportDropped') order by scw.updating_date desc limit 1",nativeQuery=true)
	String getFilePath(String gstin, Date caseReportingDate, String period);
	
	@Query(value="select scw.file_path  from analytics.scrutiny_case_workflow scw where scw.gstin =?1 and scw.case_reporting_date=?2\r\n"
			+ "and scw.\"period\" = ?3 and scw.\"action\" ='asmtTenIssued'\r\n"
			+ "order by scw.updating_date desc",nativeQuery=true)
	String getFilePathForRecommendedForAssesmentAndAdjudication(String gstin, Date caseReportingDate, String period);
	
	
	@Query(value="select scw.file_path  from analytics.scrutiny_case_workflow scw where scw.gstin =?1 and scw.case_reporting_date=?2\r\n"
			+ "and scw.\"period\" =?3 and scw.\"action\" ='recommendedForAssesAndAdjudication' order by scw.updating_date desc limit 1",nativeQuery=true)
	String getFilePathWhenSendForAssesmentAndAdjudication(String gstin, Date caseReportingDate, String period);
	
	@Query(value="select scw.other_remarks || ' (' || TO_CHAR(scw.updating_date , 'DD-MM-YYYY HH24:MI:SS') || ')' AS other_remarks_with_date from analytics.scrutiny_case_workflow scw where scw.gstin = ?1  and scw.case_reporting_date = ?3 and scw.\"period\"  = ?2\r\n"
			+ "and scw.assigned_from='SHQ' and scw.assigned_to = 'SRU' and scw.\"action\"  = 'hqRecommendForScrutiny' order by scw.updating_date desc",nativeQuery=true)
	List<String> getScrutinyHqRemarks(String gstin, String period, Date caseReportingDate);
	
	
	/*
	 * @Query(
	 * value="select scw.other_remarks || ' (' || TO_CHAR(scw.updating_date , 'DD-MM-YYYY HH24:MI:SS') || ')' AS other_remarks_with_date from analytics.scrutiny_case_workflow scw where scw.gstin = ?1  and scw.case_reporting_date = ?3 and scw.\\\"period\\\" = ?2 \"\r\n"
	 * +
	 * "			+ \"and scw.assigned_from='SRU' and scw.assigned_to = 'SFO' and scw.\\\"action\\\" = 'recommendForScrutiny' order by scw.updating_date desc"
	 * ,nativeQuery=true) List<String> getScrutinyHqTranferRejectRemarks(String
	 * gstin, String period, Date caseReportingDate);
	 */
	
	@Query(value = "select scw.other_remarks || ' (' || TO_CHAR(scw.updating_date , 'DD-MM-YYYY HH24:MI:SS') || ')' AS other_remarks_with_date from analytics.scrutiny_case_workflow scw where scw.gstin = ?1  and scw.case_reporting_date = ?3 and scw.\"period\" = ?2 "
			+ "and scw.assigned_from='SHQ' and scw.assigned_to = 'SFO' and scw.\"action\" = 'transferRequestRejected' order by scw.updating_date desc", nativeQuery = true)
	List<String> getScrutinyHqTranferRejectRemarks(String gstin, String period, Date caseReportingDate);
	

}
