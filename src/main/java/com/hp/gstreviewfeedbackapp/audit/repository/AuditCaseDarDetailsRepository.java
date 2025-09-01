package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;

@Repository
public interface AuditCaseDarDetailsRepository extends JpaRepository<AuditCaseDarDetails, Long> {

	List<AuditCaseDarDetails> findByAuditCaseDateDocumentDetailsIdAndLastUpdatedTimeStamp(
			AuditCaseDateDocumentDetails auditCaseDateDocumentDetails, Date lastUpdatedTimeStamp);

}
