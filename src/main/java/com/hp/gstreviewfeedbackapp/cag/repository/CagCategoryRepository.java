package com.hp.gstreviewfeedbackapp.cag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hp.gstreviewfeedbackapp.cag.model.CagCategory;

public interface CagCategoryRepository  extends JpaRepository<CagCategory,Integer>{
	
	
}
