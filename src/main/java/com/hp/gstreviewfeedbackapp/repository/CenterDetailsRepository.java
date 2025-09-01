package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.StateDetails;

public interface CenterDetailsRepository extends JpaRepository<StateDetails, String> {

	@Query(value = "select distinct lm.state_id , lm.state_name from analytics.mst_dd_state_details lm", nativeQuery = true)
	Map<String, String> getCenterMap();

}
