package com.hp.gstreviewfeedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hp.gstreviewfeedbackapp.model.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

}
