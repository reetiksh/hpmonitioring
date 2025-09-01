/**
 * 
 */
package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.AppMenu;
import com.hp.gstreviewfeedbackapp.model.CaseStage;

/**
 * 
 */
@Repository
public interface ActionStatusRepository extends JpaRepository<ActionStatus, Integer> {

	List<ActionStatus> findAll();

	Optional<ActionStatus> findByName(String name);
}
