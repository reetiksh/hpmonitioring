package com.hp.gstreviewfeedbackapp.enforcement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMasterAllocatingUser;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;

@Repository
public interface EnforcementMasterAllocatingUserRepository
		extends JpaRepository<EnforcementMasterAllocatingUser, CompositeKey> {
	@Query(value = "select count(*) from analytics.enforcement_master_allocating_user emau where emau.enf_fo_user_id = ?1 ", nativeQuery = true)
	Integer totalCountOfCasesActionTakenByEnfFoUserId(Integer userId);

	@Query(value = "select count(*) from analytics.enforcement_master_allocating_user emau where emau.enf_svo_user_id = ?1 ", nativeQuery = true)
	Object totalCountOfCasesActionTakenByEnfSvoUserId(Integer userId);
}
