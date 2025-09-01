package com.hp.gstreviewfeedbackapp.enforcement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementFoUserLogs;

@Repository
public interface EnforcementFoUserLogsRepository extends JpaRepository<EnforcementFoUserLogs, Long> {
}
