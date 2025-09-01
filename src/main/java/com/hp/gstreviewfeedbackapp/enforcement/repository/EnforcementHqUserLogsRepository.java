package com.hp.gstreviewfeedbackapp.enforcement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementHqUserLogs;

@Repository
public interface EnforcementHqUserLogsRepository extends JpaRepository<EnforcementHqUserLogs, Long> {
	@Query(value = "select count(ehul.*) from analytics.enforcement_hq_user_logs ehul \r\n"
			+ "where ehul.\"action\" = 'upload' and ehul.case_updating_timestamp > CURRENT_DATE - INTERVAL '3 months'", nativeQuery = true)
	Integer findTotalCountOfUploadedCasesInLast3Months();
}
