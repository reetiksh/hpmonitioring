package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;

@Repository
public interface ScrutinyCasesPertainToUsersRepository extends JpaRepository<ScrutinyCasesPertainToUsers,CompositeKey>{

}
