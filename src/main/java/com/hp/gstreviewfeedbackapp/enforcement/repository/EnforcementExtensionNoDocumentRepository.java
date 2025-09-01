package com.hp.gstreviewfeedbackapp.enforcement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementExtensionNoDocument;

@Repository
public interface EnforcementExtensionNoDocumentRepository extends JpaRepository<EnforcementExtensionNoDocument, Long> {
}
