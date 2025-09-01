package com.hp.gstreviewfeedbackapp.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditMasterCasesAllocatingUsers;

@Repository
public interface AuditMasterCasesAllocatingUsersRepository
		extends JpaRepository<AuditMasterCasesAllocatingUsers, String> {

}
