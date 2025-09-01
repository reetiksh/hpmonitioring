package com.hp.gstreviewfeedbackapp.enforcement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementActionStatus;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;

@Repository
public interface EnforcementCaseDateDocumentDetailsRepository
		extends JpaRepository<EnforcementCaseDateDocumentDetails, Long> {
	Optional<EnforcementCaseDateDocumentDetails> findByEnforcementMasterAndAction(EnforcementMaster enforcementMaster,
			EnforcementActionStatus enforcementActionStatus);

	List<EnforcementCaseDateDocumentDetails> findByEnforcementMaster(EnforcementMaster em);
}
