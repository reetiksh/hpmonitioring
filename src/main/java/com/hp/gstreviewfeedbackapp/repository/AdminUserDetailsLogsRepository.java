package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.AdminUserDetailsLogs;

@Repository
public interface AdminUserDetailsLogsRepository extends JpaRepository<AdminUserDetailsLogs, Long> {

}
