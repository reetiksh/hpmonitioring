package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.TransferRoleLogs;

@Repository
public interface TransferRoleLogsRepository extends JpaRepository<TransferRoleLogs, Long> {

}
