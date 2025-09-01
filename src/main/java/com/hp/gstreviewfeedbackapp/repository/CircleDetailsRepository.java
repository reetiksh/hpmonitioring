package com.hp.gstreviewfeedbackapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hp.gstreviewfeedbackapp.model.CircleDetails;

public interface CircleDetailsRepository extends JpaRepository<CircleDetails, String> {

	Optional<CircleDetails> findByCircleName(String dateCellValue);

}
