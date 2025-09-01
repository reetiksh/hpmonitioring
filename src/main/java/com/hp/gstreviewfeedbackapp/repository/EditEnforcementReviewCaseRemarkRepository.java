package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.EditEnforcementReviewCaseRemark;

@Repository
public interface EditEnforcementReviewCaseRemarkRepository
		extends JpaRepository<EditEnforcementReviewCaseRemark, Integer> {

}
