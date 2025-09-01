package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hp.gstreviewfeedbackapp.model.ApproverRemarksToRejectTheCase;

public interface ApproverRemarksToRejectTheCaseRepository extends JpaRepository<ApproverRemarksToRejectTheCase, Long> {

	List<ApproverRemarksToRejectTheCase> findAllByOrderByIdDesc();

}
