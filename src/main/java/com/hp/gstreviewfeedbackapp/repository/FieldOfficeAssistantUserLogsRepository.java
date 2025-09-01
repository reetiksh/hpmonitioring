package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.FieldOfficeAssistantUserLogs;

@Repository
public interface FieldOfficeAssistantUserLogsRepository extends JpaRepository<FieldOfficeAssistantUserLogs, Long> {
}
