package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.VerifierRaiseQueryRemarks;
import com.hp.gstreviewfeedbackapp.model.VerifierRemarks;

@Repository
public interface VerifierRaiseQueryRemarksRepository extends JpaRepository<VerifierRaiseQueryRemarks, Long> {
	@Query(value = "select * from analytics.mst_remarks_verifier_raise_query mrvrq where mrvrq.status = true order by mrvrq.id asc  ", nativeQuery = true)
	List<VerifierRaiseQueryRemarks> findRemarksOrderById();
}
