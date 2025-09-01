package com.hp.gstreviewfeedbackapp.cag.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hp.gstreviewfeedbackapp.cag.model.CagFoReviewCase;

public interface CagFoReviewRepository  extends JpaRepository<CagFoReviewCase,Long>{
	
	@Query(value="select * from analytics.cag_fo_review_case cfrc\r\n"
			+ "where cfrc.gstin = ?1 and cfrc.period = ?2 and cfrc.case_reporting_date = ?3\r\n"
			+ "and cfrc.parameter = ?4 and cfrc.action = 'transferCagCases'\r\n"
			+ "order by id desc limit 1",nativeQuery=true)
	Optional<CagFoReviewCase> findTransfterGstn(String gstn, String perid, Date date, String parameter);
	
	
}
