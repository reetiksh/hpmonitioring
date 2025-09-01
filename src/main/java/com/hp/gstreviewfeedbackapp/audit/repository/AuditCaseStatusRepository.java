package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;

@Repository
public interface AuditCaseStatusRepository extends JpaRepository<AuditCaseStatus, Integer> {

	Optional<AuditCaseStatus> findByStatus(String string);

	List<AuditCaseStatus> findAllByUsedByRole(String usedByRole);

	List<AuditCaseStatus> findAllByUsedByRoleOrderById(String usedByRole);

	@Query(value = "select acs.id from analytics.audit_case_status acs where acs.category = ?1 ", nativeQuery = true)
	List<Integer> findAllIdByCategory(String category);

	@Query(value = "select * from analytics.audit_case_status acs where acs.used_by_role = ?1 and acs.category != ?2 ", nativeQuery = true)
	List<AuditCaseStatus> findAllByUsedByRoleOrderByIdWithoutMentionedCategory(String usedByRole, String category);

	List<AuditCaseStatus> findByUsedByRoleAndCategory(String usedByRole, String category);

	List<AuditCaseStatus> findAllByCategory(String category);

	Optional<AuditCaseStatus> findByStatusAndCategory(String status, String category);

	Optional<AuditCaseStatus> findBySequence(int i);

	@Query(value = "select acs.id from analytics.audit_case_status acs where acs.status in (?1)", nativeQuery = true)
	List<Integer> findIdsByStatusList(List<String> statusList);

	@Query(value = "select acs.id from analytics.audit_case_status acs where acs.status = ?1", nativeQuery = true)
	List<Integer> findIdByStatus(String status);

}
