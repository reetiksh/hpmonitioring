package com.hp.gstreviewfeedbackapp.cag.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hp.gstreviewfeedbackapp.cag.model.CagFoReviewCase;
import com.hp.gstreviewfeedbackapp.cag.model.CagHqReviewCase;

public interface CagHqReviewRepository  extends JpaRepository<CagHqReviewCase,Long>{
	
	@Query(value="select chrc.* from analytics.cag_hq_review_case chrc\r\n"
			+ "where chrc.gstin =?1 and chrc.period =?2 and chrc.case_reporting_date =?3 \r\n"
			+ "and chrc.parameter =?4 order by id desc limit 1",nativeQuery=true)
	Optional<CagHqReviewCase> findTransfterRemarks(String gstn, String perid, Date date, String parameter);
	
	
}
