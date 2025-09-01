package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.UploadSelfDetectedCaseRemark;

@Repository
public interface UploadSelfDetectedCaseRemarkRepository extends JpaRepository<UploadSelfDetectedCaseRemark, Integer> {

	List<UploadSelfDetectedCaseRemark> findAllByActiveStatus(boolean activeStatus);

}
