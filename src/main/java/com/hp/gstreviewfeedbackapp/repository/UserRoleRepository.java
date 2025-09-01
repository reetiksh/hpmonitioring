package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	Optional<UserRole> findByroleName(String roleName);

	Optional<UserRole> findByroleCode(String assignedTo);

	@Query(value = "select * from analytics.mst_user_role mur where \"name\" in(?1)", nativeQuery = true)
	List<UserRole> findAllByRoleName(List<String> roleStrList);

}
