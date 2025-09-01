package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;

@Repository
public interface EnforcementReviewCaseAssignedUsersRepository
		extends JpaRepository<EnforcementReviewCaseAssignedUsers, CompositeKey> {

	@Query(value = "select * from analytics.enforcement_review_case_assigned_users ercau where ercau.gstin=?1 and ercau.\"period\" = ?2\r\n"
			+ "and ercau.case_reporting_date=?3  ", nativeQuery = true)
	EnforcementReviewCaseAssignedUsers findByGstinPeriodRepotingDate(String gstin, String period,
			Date caseReportingDate);

	@Query(value = "select ercau.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.\"period\" = ercau.\"period\" and erc.working_location = ?1\r\n"
			+ "and erc.\"action\" != 'closed'", nativeQuery = true)
	List<EnforcementReviewCaseAssignedUsers> findAllActiveCasesByLocation(String location);
}
