package com.hp.gstreviewfeedbackapp.cag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hp.gstreviewfeedbackapp.cag.model.CagCategory;
import com.hp.gstreviewfeedbackapp.cag.model.CagReason;

public interface CagReasonRepository  extends JpaRepository<CagReason,Integer>{
	
	
}
