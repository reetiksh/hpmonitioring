package com.hp.gstreviewfeedbackapp.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.L2UserWorkLogs;

@Repository
public interface L2UserWorkLogsRepository extends JpaRepository<L2UserWorkLogs, Long> {

}
