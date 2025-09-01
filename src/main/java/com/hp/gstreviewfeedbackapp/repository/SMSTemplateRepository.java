package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.SMSTemplate;

@Repository
public interface SMSTemplateRepository extends JpaRepository<SMSTemplate, String> {

	SMSTemplate findTemplateByTemplateId(String string);

}
