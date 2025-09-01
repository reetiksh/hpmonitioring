/**
 * 
 */
package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CaseStage;

/**
 * 
 */
@Repository
public interface CaseStageRepository extends JpaRepository<CaseStage, Integer> {

	List<CaseStage> findAll();

	List<CaseStage> findByActionId(Integer actionId);

	Optional<CaseStage> findByName(String name);

}
