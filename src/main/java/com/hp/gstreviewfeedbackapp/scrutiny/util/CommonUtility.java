package com.hp.gstreviewfeedbackapp.scrutiny.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

@Component
public class CommonUtility {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtility.class);
	
	@Autowired 
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	
	@Autowired 
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired 
	private UserRoleRepository userRoleRepository;
	
 	
	public List<String> returnWorkingLocation(Integer currentUserId,String roleCode) {
		List<String> workingLocationsIds = new ArrayList<>();
		List<UserRoleMapping> userRoleMapList = userRoleMappingRepository.findAllRegionsMapWithUsers(currentUserId, userRoleRepository.findByroleCode(roleCode).get().getId());
		
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId()))
				.collect(Collectors.toList());
		
		if(stateId!=null && stateId.size()>0) {
			workingLocationsIds.addAll(enforcementReviewCaseRepository.finAllLocationsByStateid());
			return workingLocationsIds;
		} else {
			List<UserRoleMapping> zoneIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId()))
					.collect(Collectors.toList());
			
			
			List<UserRoleMapping> circleIds = userRoleMapList.stream()
					.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId()))   // commented as per requirement verifier can not be assigned to at circle level.
					.collect(Collectors.toList());
			
			if(!zoneIds.isEmpty()) {
				List<String> onlyZoneIdsList = new ArrayList<String>();
				for(UserRoleMapping zoneIdsSolo : zoneIds) {
					workingLocationsIds.add(zoneIdsSolo.getZoneDetails().getZoneId()); // add zones
					onlyZoneIdsList.add(zoneIdsSolo.getZoneDetails().getZoneId()); // temp store zone ids to collect circle later
				}
	
				List<String> allCirclesListUnderZones = enforcementReviewCaseRepository.findAllCirclesByZoneIds(onlyZoneIdsList);
				for(String circleSolo : allCirclesListUnderZones) {
					workingLocationsIds.add(circleSolo);	// add Circles
				}
			}
			
			if(!circleIds.isEmpty()) { 
				  for(UserRoleMapping circleIdsSolo : circleIds) { 
					  workingLocationsIds.add(circleIdsSolo.getCircleDetails().getCircleId());
				  }
			  }
		}
		return workingLocationsIds;
	}
	
	public List<MstScrutinyCases> getAssignedToRoleList(List<MstScrutinyCases> mstScrutinyCasesList){
		for(MstScrutinyCases mstScrutinyCasesSolo:  mstScrutinyCasesList)
		{
			switch(mstScrutinyCasesSolo.getAssignedTo()) {
			 case "FO":
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("Field Office (Assessment)");
				 break;
			 case "L2":
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("ALLOCATING OFFICER");
				 break;
			 case "SFO":
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("Field Office (Scrutiny)");
				 break;
			 case "SRU":
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("VERIFIER");
				 break;
			 case "SHQ":
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("Head Office");
				 break;
			 default:
				 mstScrutinyCasesSolo.setCurrentlyAssignedTo("-");	 
			}
		}
		return mstScrutinyCasesList;
		
	}

	public List<EnforcementMaster> getAssignedToRoleList1(List<EnforcementMaster> EnforcementcasesList){
		
		
		for(EnforcementMaster enforcementCasesSolo:  EnforcementcasesList)
		{
			switch(enforcementCasesSolo.getAssignedTo()) {
			 case "ASSESSMENT_FO":
				// enforcementCasesSolo.setCurrentlyAssignedTo("Field Office (Assessment)");
				 enforcementCasesSolo.setAssignedTo("ASSESSMENT_FO");
				 //enforcementCasesSolo.setAssignedToUserId();
				 break;
			 case "Enforcement_FO":
				// enforcementCasesSolo.setCurrentlyAssignedTo("ALLOCATING OFFICER");
				 enforcementCasesSolo.setAssignedTo("Enforcement_FO");
				 break;
			 case "Enforcement_SVO":
				// enforcementCasesSolo.setCurrentlyAssignedTo("Field Office (Scrutiny)");
				 enforcementCasesSolo.setAssignedTo("Enforcement_SVO");
				 break;
			 case "ScrutinyFO":
				// enforcementCasesSolo.setCurrentlyAssignedTo("VERIFIER");
				 enforcementCasesSolo.setAssignedTo("ScrutinyFO");
				 break;
			 default:
				 enforcementCasesSolo.setAssignedTo("-");
			}
		}
		return EnforcementcasesList;
		
	}
	
}
