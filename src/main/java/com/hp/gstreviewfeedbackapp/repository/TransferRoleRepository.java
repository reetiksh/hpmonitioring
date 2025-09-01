package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.TransferRole;

@Repository
public interface TransferRoleRepository extends JpaRepository<TransferRole, Long> {

	List<TransferRole> findByActionDate(Date date);

	@Query(value = "select * from analytics.admin_transfer_role atr where action_date <= ?1", nativeQuery = true)
	List<TransferRole> findByActionDateBeforeOrOn(Date date);

	@Query(value = "select atr.* from analytics.admin_transfer_role atr, analytics.mst_user_role_mapping murm \r\n"
			+ "where atr.transfer_role_id = murm.user_role_mapping_id and atr.transfer_to_user_id = ?1 and murm.user_role_id = ?2 and murm.enforcement_zone_id != 'NA'", nativeQuery = true)
	List<TransferRole> findAllTransferRoleForEnfZoneByUserDetailsAndUserRole(Integer userId, int userRoleId);

	@Query(value = "select atr.* from analytics.admin_transfer_role atr, analytics.mst_user_role_mapping murm \r\n"
			+ "where atr.transfer_role_id = murm.user_role_mapping_id and atr.transfer_to_user_id = ?1 and murm.user_role_id = ?2 and murm.zone_id != 'NA'", nativeQuery = true)
	List<TransferRole> findAllTransferRoleForZoneByUserDetailsAndUserRole(Integer userId, int userRoleId);

}
