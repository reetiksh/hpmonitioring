package com.hp.gstreviewfeedbackapp.cag.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hp.gstreviewfeedbackapp.cag.model.CagLinkedCases;

public interface CagLinkedCasesRepository  extends JpaRepository<CagLinkedCases,Integer>{
	
	@Query(value = "select * from analytics.cag_linked_cases \r\n"
			+ "where gstin = ?1 and case_reporting_date = ?2 and period = ?3 \r\n"
			+ "and parameter = ?4", nativeQuery = true)
	List<CagLinkedCases> findLinkedCaseList(String gstin, Date caseReportingDate, String period, String parameter);
	
	
	
	
}
