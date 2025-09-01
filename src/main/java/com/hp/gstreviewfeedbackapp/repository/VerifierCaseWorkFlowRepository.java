package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.VerifierCaseWorkflow;

@Repository
public interface VerifierCaseWorkFlowRepository extends JpaRepository<VerifierCaseWorkflow, Integer> {

}
