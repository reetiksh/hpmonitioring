package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.EnforcementCasesRemarksCategoryWise;
import com.hp.gstreviewfeedbackapp.model.ReviewMeetingModel;

@Repository
public interface ReviewMeetingRepository extends JpaRepository<EnforcementCasesRemarksCategoryWise, Long> {

	EnforcementCasesRemarksCategoryWise findTopByOrderByIdDesc();

	EnforcementCasesRemarksCategoryWise findTopByCategoryIdOrderByIdDesc(Long categoryId);

}
