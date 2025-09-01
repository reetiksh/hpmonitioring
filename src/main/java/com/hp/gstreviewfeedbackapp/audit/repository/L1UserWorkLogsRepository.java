package com.hp.gstreviewfeedbackapp.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.L1UserWorkLogs;

@Repository
public interface L1UserWorkLogsRepository extends JpaRepository<L1UserWorkLogs, Long> {

}
