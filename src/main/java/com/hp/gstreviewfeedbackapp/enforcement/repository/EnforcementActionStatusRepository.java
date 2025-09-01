package com.hp.gstreviewfeedbackapp.enforcement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementActionStatus;

@Repository
public interface EnforcementActionStatusRepository extends JpaRepository<EnforcementActionStatus, Integer> {
	Optional<EnforcementActionStatus> findByCodeName(String string);

	@Query(value = "select * from analytics.enforcement_action_status eas where eas.used_in = ?1", nativeQuery = true)
	List<EnforcementActionStatus> findByUsedIn(String usedIn);

	Optional<EnforcementActionStatus> getByCodeName(String string);

	@Query(value = "select code_name from analytics.enforcement_action_status eas where eas.used_by_role = (?1)", nativeQuery = true)
	List<String> findByUsedByRole(String string);

	@Query(value = "select * from analytics.enforcement_action_status eas where eas.id > ?1 order by id", nativeQuery = true)
	List<EnforcementActionStatus> findNextStatus(Integer id);

	@Query(value = "SELECT * FROM analytics.enforcement_action_status", nativeQuery = true)
	List<EnforcementActionStatus> findAllWithOrder();
	
	//@Query(value = "select * from analytics.enforcement_action_status scs order by scs.\"order\" asc", nativeQuery = true)
}
