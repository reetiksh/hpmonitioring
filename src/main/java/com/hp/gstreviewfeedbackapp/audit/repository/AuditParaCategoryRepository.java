package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditParaCategory;

@Repository
public interface AuditParaCategoryRepository extends JpaRepository<AuditParaCategory, Integer> {

	@Query(value = "select * from analytics.audit_para_category apc where active = ?1 order by id", nativeQuery = true)
	List<AuditParaCategory> findAllByActiveStatus(boolean activeStatus);

}
