package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyTransferRemarks;

@Repository
public interface ScrutinyTransferRemarksRepository extends JpaRepository<ScrutinyTransferRemarks, Integer> {

	List<ScrutinyTransferRemarks> findAllByOrderByIdDesc();

}
