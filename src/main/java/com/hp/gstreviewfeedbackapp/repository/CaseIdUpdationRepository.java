package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CaseIdUpdationRemarks;
import com.hp.gstreviewfeedbackapp.model.CaseidUpdation;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;

@Repository
public interface CaseIdUpdationRepository extends JpaRepository<CaseidUpdation, Integer> {

	@Query(value = "select * from analytics.caseid_updation cu where cu.status = 'foUpdated' and cu.gstin = ?1 and\r\n"
			+ "cu.period = ?2 and cu.case_reporting_date = ?3 order by id desc limit 1", nativeQuery = true)
	CaseidUpdation findCaseIdRequestDetails(String gstin, String period, Date caseReporingDate);

}
