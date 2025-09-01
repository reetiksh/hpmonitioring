package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CaseIdUpdationRemarks;

@Repository
public interface CaseIdRemarksRepository extends JpaRepository<CaseIdUpdationRemarks, Integer> {

	List<CaseIdUpdationRemarks> findAllByOrderByIdAsc();

	List<CaseIdUpdationRemarks> findAllByOrderByIdDesc();

}
