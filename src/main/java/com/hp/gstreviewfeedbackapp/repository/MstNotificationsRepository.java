package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.MstNotifications;

@Repository
public interface MstNotificationsRepository extends JpaRepository<MstNotifications, Long> {

	@Query(value = "select * from analytics.mst_notifications mn where mn.assigned_to=?2 and mn.working_location in(?3)  and (mn.notification_pertain_to = 0 or mn.notification_pertain_to=?1) and mn.updating_date >= CURRENT_TIMESTAMP - interval '15 days' and mn.updating_date <= CURRENT_TIMESTAMP order by mn.updating_date desc", nativeQuery = true)
	List<MstNotifications> findNotificationsPertainToUser(Integer userId, String roleCode,
			List<String> workingLocationsList);

	@Query(value = "select * from analytics.mst_notifications mn where mn.\"action\" in(?4) and mn.notification_pertain_to=0 and mn.assigned_to =?5   \r\n"
			+ "and mn.gstin =?1  and mn.case_reporting_date=?3 and mn.\"period\" =?2 and mn.working_location in(?6)", nativeQuery = true)
	List<MstNotifications> getNotificationsToUpdateNotificationPertainTo(String gstin, String period,
			Date caseReportingDate, List<String> action, String assignedToRole, List<String> workingLocation);

	@Query(value = "select * from analytics.mst_notifications mn where mn.assigned_to=?2 and mn.working_location in(?3)  and (mn.notification_pertain_to = 0 or mn.notification_pertain_to=?1) and mn.updating_date >= CURRENT_TIMESTAMP - interval '15 days' and mn.updating_date <= CURRENT_TIMESTAMP and mn.is_displayed=false order by mn.updating_date desc", nativeQuery = true)
	List<MstNotifications> findUnreadNotificationsPertainToUser(Integer userId, String roleCode,
			List<String> workingLocationsList);

}
