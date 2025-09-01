package com.hp.gstreviewfeedbackapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

public interface ApproverUserService {
	public Page<EnforcementReviewCase> findAllDataListByDefault(Pageable pageRequest);

	public Page<EnforcementReviewCase> findAllDataListBySearchValue(String searchValue, Pageable pageRequest);

	public Page<EnforcementReviewCase> findAllDataListByDefault(Pageable pageRequest, String actionStatus,
			Boolean hasStateAccess, List<String> allMappedLocations);

	public Page<EnforcementReviewCase> findAllDataListBySearchValue(String searchValue, Pageable pageable,
			String actionStatus, Boolean hasStateAccess, List<String> allMappedLocations);
}
