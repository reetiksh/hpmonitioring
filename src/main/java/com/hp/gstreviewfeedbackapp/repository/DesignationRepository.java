package com.hp.gstreviewfeedbackapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

	Optional<Designation> findByDesignationName(String designationName);

}
