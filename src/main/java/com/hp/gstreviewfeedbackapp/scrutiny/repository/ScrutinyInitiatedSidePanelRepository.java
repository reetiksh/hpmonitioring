package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyInitiatedSidePanel;

@Repository
public interface ScrutinyInitiatedSidePanelRepository extends JpaRepository<ScrutinyInitiatedSidePanel, Integer> {

	@Query(value = "select * from analytics.scrutiny_side_panel ssp where ssp.id != 6 and ssp.status = true order by ssp.priority;", nativeQuery = true)
	List<ScrutinyInitiatedSidePanel> returnActiveMenuList();

	@Query(value = "select * from analytics.scrutiny_side_panel ssp where ssp.id != 1 and ssp.status = true order by ssp.priority", nativeQuery = true)
	List<ScrutinyInitiatedSidePanel> returnUpdateScrutinyStatusSidePannel();

}
