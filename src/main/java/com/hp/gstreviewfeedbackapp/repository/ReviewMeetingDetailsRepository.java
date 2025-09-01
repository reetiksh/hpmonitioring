package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksDetails;

@Repository
public interface ReviewMeetingDetailsRepository extends JpaRepository<EnforcementCasesRemarksDetails, Long> {

	EnforcementCasesRemarksDetails findTopByGSTINAndCaseReportingDateAndPeriodOrderByIdDesc(String GSTIN,
			Date caseReportingDate, String Period);

	Boolean existsByGSTINAndCaseReportingDateAndPeriod(String GSTIN, Date caseReportingDate, String Period);

}
