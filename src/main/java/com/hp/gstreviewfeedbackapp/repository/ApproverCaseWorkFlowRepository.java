package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.ApproverCaseWorkFlow;
import com.hp.gstreviewfeedbackapp.model.VerifierCaseWorkflow;

@Repository
public interface ApproverCaseWorkFlowRepository extends JpaRepository<ApproverCaseWorkFlow, Integer> {

}
