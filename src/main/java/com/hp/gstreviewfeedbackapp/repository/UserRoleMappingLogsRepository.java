package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.UserRoleMappingLogs;

@Repository
public interface UserRoleMappingLogsRepository extends JpaRepository<UserRoleMappingLogs, Long> {

}
