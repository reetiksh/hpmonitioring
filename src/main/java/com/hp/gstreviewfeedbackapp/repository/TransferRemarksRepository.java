package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.TransferRemarks;

@Repository
public interface TransferRemarksRepository extends JpaRepository<TransferRemarks, Integer> {

}
