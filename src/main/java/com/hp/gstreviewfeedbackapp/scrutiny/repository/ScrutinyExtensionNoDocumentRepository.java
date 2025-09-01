package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyExtensionNoDocument;

@Repository
public interface ScrutinyExtensionNoDocumentRepository extends JpaRepository<ScrutinyExtensionNoDocument, Long> {

}
