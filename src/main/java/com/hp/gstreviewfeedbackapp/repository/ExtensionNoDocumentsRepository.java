package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;

@Repository
public interface ExtensionNoDocumentsRepository extends JpaRepository<ExtensionNoDocument, Long> {

}
