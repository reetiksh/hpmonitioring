package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetails;

@Repository
public interface AuditCaseFarDetailsRepository extends JpaRepository<AuditCaseFarDetails, Long> {

	List<AuditCaseFarDetails> findByAuditCaseDateDocumentDetailsIdAndLastUpdatedTimeStamp(
			AuditCaseDateDocumentDetails auditCaseDateDocumentDetails, Date lastUpdatedTimeStamp);

}
