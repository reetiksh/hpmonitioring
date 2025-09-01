package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.SentSmsDetails;

@Repository
public interface SendSmsDetailsRepository extends JpaRepository<SentSmsDetails, Long> {

}
