package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hp.gstreviewfeedbackapp.model.OldCasesUploadManually;

public interface OldCasesUploadManuallyRepository extends JpaRepository<OldCasesUploadManually, Long> {

	@Query(value = "select * from analytics.old_cases_upload_manually ocum where ocum.working_location in(?1) and ocum.is_uploaded=false", nativeQuery = true)
	List<OldCasesUploadManually> getOldCasesByLocationAndStatusUploadedOrNot(List<String> LocationIds);

	@Query(value = "select * from analytics.old_cases_upload_manually ocum where ocum.gstin = '?1' and ocum.working_location = '?2' and ocum.is_uploaded=true ", nativeQuery = true)
	OldCasesUploadManually getOldCasesToUpdateStatus(String gstinOldCases, String workinglocationid);
}
