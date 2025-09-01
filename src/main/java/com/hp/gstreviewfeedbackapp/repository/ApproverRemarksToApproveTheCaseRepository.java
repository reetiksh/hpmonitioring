package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.ApproverRemarksToApproveTheCase;

@Repository
public interface ApproverRemarksToApproveTheCaseRepository
		extends JpaRepository<ApproverRemarksToApproveTheCase, Long> {

}
