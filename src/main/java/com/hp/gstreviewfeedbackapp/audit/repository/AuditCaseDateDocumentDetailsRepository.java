package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;

@Repository
public interface AuditCaseDateDocumentDetailsRepository extends JpaRepository<AuditCaseDateDocumentDetails, Long> {

	@Query(value = "select distinct on (case_id) * from analytics.audit_case_date_document_details acddd where case_id = ?1 and action = ?2 "
			+ "order by case_id, action, last_updated_timestamp;", nativeQuery = true)
	Optional<AuditCaseDateDocumentDetails> findTheLatestStatusDataByCaseIdAndAuditStatusId(String caseId,
			Integer activeActionPannelId);

	Optional<AuditCaseDateDocumentDetails> findByCaseIdAndAction(AuditMaster auditMaster, AuditCaseStatus action);

	@Query(value = "select distinct on (case_id) * from analytics.audit_case_date_document_details acddd where case_id = ?1 and action in (?2) "
			+ "order by case_id, action, last_updated_timestamp;", nativeQuery = true)
	Optional<AuditCaseDateDocumentDetails> findTheLatestStatusDataByCaseIdAndAuditStatusIdList(String caseId,
			List<Integer> findAllIdByCategory);

	@Query(value = "select acddd.* from analytics.audit_case_date_document_details acddd where acddd.case_id = ?1 and acddd.action in (?2)", nativeQuery = true)
	Optional<AuditCaseDateDocumentDetails> findByCaseIdAndActionStausIdList(String caseId, List<Integer> statusIdList);

	@Query(value = "select * from analytics.audit_case_date_document_details acddd where case_id in (?1) and action in (?2)", nativeQuery = true)
	List<AuditCaseDateDocumentDetails> findAllByCaseIdListAndAction(List<String> collect, List<Integer> collect2);

	List<AuditCaseDateDocumentDetails> findAllByCaseId(AuditMaster auditMaster);

	@Query(value = "select * from analytics.audit_case_date_document_details acddd where case_id in (?1) and action in (?2)", nativeQuery = true)
	Optional<AuditCaseDateDocumentDetails> findByCaseIdAndActionList(String caseId, List<Integer> statusIdList);

	@Query(value = "select acddd.* from analytics.audit_case_date_document_details acddd, analytics.audit_case_status acs "
			+ "where acddd.case_id = (?1) and acddd.action = acs.id order by acs.sequence desc limit 1;", nativeQuery = true)
	Optional<AuditCaseDateDocumentDetails> getLastStatusorderBySequence(String caseId);

	@Query(value = "select acddd.* from analytics.audit_case_date_document_details acddd, analytics.audit_case_status acs "
			+ "where acddd.case_id = (?1) and acddd.action = acs.id order by acs.sequence desc limit 2;", nativeQuery = true)
	List<AuditCaseDateDocumentDetails> getLastTwoStatusorderBySequence(String caseId);

	@Query(value = "select MAX(acddd.date) AS latest_date from analytics.audit_case_date_document_details acddd where acddd.case_id = ?1", nativeQuery = true)
	Date getHighestDateByCaseId(String caseId);

}
