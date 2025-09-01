package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.MstCaseNotificationCategory;

@Repository
public interface MstCaseNotificationCategoryRepository extends JpaRepository<MstCaseNotificationCategory, Long> {

	@Query(value = "select * from analytics.mst_case_notification_category mcnc where mcnc.id=?1 ", nativeQuery = true)
	MstCaseNotificationCategory getNotificationCategoryById(Long notificationId);

}
