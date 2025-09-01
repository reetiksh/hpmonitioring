package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.FoReviewCase;

@Repository
public interface FOUserCaseReviewRepository extends JpaRepository<FoReviewCase, Long> {
	@Override
	List<FoReviewCase> findAll();

	@Query(value = "select case c.verifier_raise_query_remarks when 1 then c.other_remarks else v.name end as remarks from analytics.case_workflow c left join \r\n"
			+ "analytics.mst_remarks_verifier_raise_query v on c.verifier_raise_query_remarks = v.id where c.gstin = ?1 and c.case_reporting_date = ?2 \r\n"
			+ "and c.period = ?3 and c.action = 'verifyerRaiseQuery' order by c.id desc limit 1", nativeQuery = true)
	String findVerifierRemarks(String gstin, Date reportDate, String period);

	@Query(value = "select case c.approver_reject_case_remarks when 1 then c.other_remarks else v.name end as remarks from analytics.case_workflow c left join \r\n"
			+ "analytics.mst_dd_ap_rejected v on c.approver_reject_case_remarks = v.id where c.gstin = ?1 and c.case_reporting_date = ?2 \r\n"
			+ "and c.period = ?3 and c.action = 'approverRaiseQuery' order by c.id desc limit 1", nativeQuery = true)
	String findApproverRemarks(String gstin, Date reportDate, String period);

//	@Query(value = "select fo_filepath from analytics.fo_review_case where gstin = ?1 and case_reporting_date = ?2 \r\n"
//			+ " and period = ?3 order by id desc limit 1 ", nativeQuery = true)
	@Query(value = "SELECT fo_filepath FROM (SELECT fo_filepath, case_updating_date FROM analytics.fo_review_case WHERE gstin = ?1 AND case_reporting_date = ?2 AND period = ?3 \r\n"
			+ "UNION SELECT fo_filepath, case_updating_date FROM analytics.field_office_assistant_user_logs foaul WHERE gstin = ?1 AND case_reporting_date = ?2 AND period = ?3 \r\n"
			+ ") AS combined ORDER BY case_updating_date DESC LIMIT 1", nativeQuery = true)
	String getFileNameUploadedByFO(String gstin, Date reportDate, String period);

	@Query(value = "select fo_filepath from analytics.fo_review_case where gstin = ?1 and case_reporting_date = ?2 \r\n"
			+ " and period = ?3 and action = 'status_updated_v' order by id desc limit 1 ", nativeQuery = true)
	String getFileNameRejectedByVerifier(String gstin, Date reportDate, String period);

	@Query(value = "select fo_filepath from analytics.fo_review_case where gstin = ?1 and case_reporting_date = ?2 \r\n"
			+ " and period = ?3 and action = 'status_updated_a' order by id desc limit 1 ", nativeQuery = true)
	String getFileNameRejectedByApprover(String gstin, Date reportDate, String period);

	@Query(value = "select * from analytics.fo_review_case frc where gstin = ?1 and period = ?2 and case_reporting_date = ?3 ", nativeQuery = true)
	List<FoReviewCase> findAllByGstinPeriodCaseReportingDate(String gstin, String period, Date caseReportingDate);

	@Query(value = "select fo_filepath from analytics.fo_review_case where gstin = ?1 and case_reporting_date = ?2 \r\n"
			+ " and period = ?3 and action != 'close' order by id desc limit 1 ", nativeQuery = true)
	String getFileNameByFO(String gstin, Date reportDate, String period);

	@Query(value = "select other_remarks_recovery from analytics.fo_review_case where gstin = ?1 and case_reporting_date = ?2 \r\n"
			+ " and period = ?3 and action = 'foRecoveryRaiseQuery' order by id desc limit 1 ", nativeQuery = true)
	String getRaiseQueryRemarks(String gstin, Date reportDate, String period);
}
