/**
 * 
 */
package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.AppMenu;
import com.hp.gstreviewfeedbackapp.model.SubAppMenu;
import com.hp.gstreviewfeedbackapp.model.SubAppMenuId;

/**
 * 
 */
@Repository
public interface SubAppMenuRepository extends JpaRepository<SubAppMenu, SubAppMenuId> {

}
