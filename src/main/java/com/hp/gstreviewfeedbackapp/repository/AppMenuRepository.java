/**
 * 
 */
package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.AppMenu;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

/**
 * 
 */
@Repository
public interface AppMenuRepository extends JpaRepository<AppMenu, Integer> {

	List<AppMenu> findByIsParentAndUserTypeOrderByPriorityAsc(boolean input, String userType);

	Optional<AppMenu> findByUserTypeAndUrl(String userType, String tabUrl);
}
