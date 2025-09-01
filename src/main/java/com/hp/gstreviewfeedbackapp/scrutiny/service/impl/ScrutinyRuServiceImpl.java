package com.hp.gstreviewfeedbackapp.scrutiny.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseWorkflow;
import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCasesPertainToUsers;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.MstScrutinyCasesRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.repository.ScrutinyCasesPertainToUsersRepository;
import com.hp.gstreviewfeedbackapp.scrutiny.service.ScrutinyRuService;
import com.hp.gstreviewfeedbackapp.scrutiny.util.CommonUtility;

@Service
public class ScrutinyRuServiceImpl implements ScrutinyRuService{

	private static final org.jboss.logging.Logger logger = LoggerFactory.logger(ScrutinyRuServiceImpl.class);
	

	@Autowired
	private MstScrutinyCasesRepository mstScrutinyCasesRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private ScrutinyCaseWorkflowRepository scrutinyCaseWorkflowRepository;
	
	@Autowired
	private ScrutinyCasesPertainToUsersRepository scrutinyCasesPertainToUsersRepository;
	
	@Autowired
	private CommonUtility commonUtility;
	
	public List<MstScrutinyCases> getMandatoryCasesList(List<String> workingLocationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getMandatoryCasesList(workingLocationList);
		for(MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getCaseReportingDate(),mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
		}
		return mstScrutinyCasesList;
	}

	public String recommendForScrutiny(String gstin,String period,String caseReportingDate,String remarks) {
		String message = "";
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();	
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
			
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(caseReportingDate);
			
			compositKey.setGSTIN(gstin);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			
			mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			
			mstScrutinyCases.setAction("recommendForScrutiny");
			mstScrutinyCases.setAssignedFrom("SRU");
			mstScrutinyCases.setAssignedTo("SFO");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository.findById(compositKey).get();
			scrutinyCasesPertainToUsers.setRuUserId(userId);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn() != null ? String.join(",", mstScrutinyCases.getRecoveryStageArn()): "");
			scrutinyCaseWorkflow.setOtherRemarks(remarks);
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Case recommended for scrutiny successfully";
		}catch(Exception ex) {
			message = "There is network bandwidth issue....";
			 logger.error("error :  " + ex.getMessage());
		}
		return message;
	}
	
	public List<MstScrutinyCases> getRandomCasesList(List<String> workingLocationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getRandomCasesList(workingLocationList);
		for(MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getCaseReportingDate(),mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
			
			switch(mstScrutinyCasesSolo.getAction()) {
			case "noNeedScrutiny":
				mstScrutinyCasesSolo.setActionDescription("Dropped before issuance of ASMT-10");
			break;
			case "closureReportDropped":
				mstScrutinyCasesSolo.setActionDescription("Dropped after issuance of ASMT-10");
			break;
			default:
				mstScrutinyCasesSolo.setActionDescription("");
			}
		}
		return mstScrutinyCasesList;
	}
	
	
	public String randomRecommendForScrutiny(String gstin,String period,String caseReportingDate,String remarks) {
		String message = "";
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();	
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
			
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(caseReportingDate);
			
			compositKey.setGSTIN(gstin);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			
			mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("recommendForScrutiny");
			mstScrutinyCases.setAssignedFrom("SRU");
			mstScrutinyCases.setAssignedTo("SFO");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository.findById(compositKey).get();
			scrutinyCasesPertainToUsers.setRuUserId(userId);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn() != null ? String.join(",", mstScrutinyCases.getRecoveryStageArn()): "");
			scrutinyCaseWorkflow.setOtherRemarks(remarks);
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Case recommended for scrutiny successfully";
		}catch(Exception ex) {
			message = "There is network bandwidth issue....";
			 logger.error("error :  " + ex.getMessage());
		}
		return message;
	}
	
	public List<MstScrutinyCases> getRecommendedFromHqUserCasesList(List<String> workingLocationList) {
		List<MstScrutinyCases> mstScrutinyCasesList = mstScrutinyCasesRepository.getRecommendedFromHqUserCasesList(workingLocationList);
		for(MstScrutinyCases mstScrutinyCasesSolo : mstScrutinyCasesList) {
			String filePath = scrutinyCaseWorkflowRepository.getFilePath(mstScrutinyCasesSolo.getId().getGSTIN(),mstScrutinyCasesSolo.getId().getCaseReportingDate(),mstScrutinyCasesSolo.getId().getPeriod());
			mstScrutinyCasesSolo.setFilePath(filePath);
		}
		return mstScrutinyCasesList;
	}
	
	
	public String recommendFromHqForScrutiny(String gstin,String period,String caseReportingDate,String remarks) {
		String message = "";
		MstScrutinyCases mstScrutinyCases = new MstScrutinyCases();
		ScrutinyCaseWorkflow scrutinyCaseWorkflow = new ScrutinyCaseWorkflow();	
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return "redirect:/logout";
			}
			Integer userId = userDetailsRepository.findByloginNameIgnoreCase(authentication.getName()).get().getUserId();
			
			CompositeKey compositKey = new CompositeKey();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			Date parsedDate = dateFormat.parse(caseReportingDate);
			
			compositKey.setGSTIN(gstin);
			compositKey.setCaseReportingDate(parsedDate);
			compositKey.setPeriod(period);
			
			mstScrutinyCases = mstScrutinyCasesRepository.findById(compositKey).get();
			mstScrutinyCases.setAction("recommendForScrutiny");
			mstScrutinyCases.setAssignedFrom("SRU");
			mstScrutinyCases.setAssignedTo("SFO");
			mstScrutinyCases.setCaseUpdateDate(new Date());
			mstScrutinyCasesRepository.save(mstScrutinyCases);
			
			ScrutinyCasesPertainToUsers scrutinyCasesPertainToUsers = scrutinyCasesPertainToUsersRepository.findById(compositKey).get();
			scrutinyCasesPertainToUsers.setRuUserId(userId);
			scrutinyCasesPertainToUsersRepository.save(scrutinyCasesPertainToUsers);
			
			scrutinyCaseWorkflow.setGstin(mstScrutinyCases.getId().getGSTIN());
			scrutinyCaseWorkflow.setCaseReportingDate(parsedDate);
			scrutinyCaseWorkflow.setPeriod(mstScrutinyCases.getId().getPeriod());
			scrutinyCaseWorkflow.setAssignedFrom(mstScrutinyCases.getAssignedFrom());
			scrutinyCaseWorkflow.setAssignedFromUserId(userId);
			scrutinyCaseWorkflow.setAssignedTo(mstScrutinyCases.getAssignedTo());
			scrutinyCaseWorkflow.setWorkingLocation(mstScrutinyCases.getLocationDetails().getLocationId());
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflow.setAction(mstScrutinyCases.getAction());
			scrutinyCaseWorkflow.setIndicativeTaxValue(mstScrutinyCases.getIndicativeTaxValue());
			scrutinyCaseWorkflow.setTaxpayerName(mstScrutinyCases.getTaxpayerName());
			scrutinyCaseWorkflow.setRecoveryStage(mstScrutinyCases.getRecoveryStage().getId());
			scrutinyCaseWorkflow.setAmountRecovered(mstScrutinyCases.getAmountRecovered());
			scrutinyCaseWorkflow.setRecoveryByDRC3(mstScrutinyCases.getRecoveryByDRC03());
			scrutinyCaseWorkflow.setRecoveryStageArn(mstScrutinyCases.getRecoveryStageArn() != null ? String.join(",", mstScrutinyCases.getRecoveryStageArn()): "");
			scrutinyCaseWorkflow.setOtherRemarks(remarks);
			scrutinyCaseWorkflow.setUpdatingDate(new Date());
			scrutinyCaseWorkflowRepository.save(scrutinyCaseWorkflow);
			message = "Case recommended for scrutiny successfully";
		}catch(Exception ex) {
			message = "There is network bandwidth issue....";
			 logger.error("error :  " + ex.getMessage());
		}
		return message;
	}
	
	@Override
	public List<MstScrutinyCases> getReviewCasesListStatusWise(Integer actionStatus, List<String> workingLoacationList){
		
		List<MstScrutinyCases> casesList = new ArrayList<>();
		switch (actionStatus) {
		case 1:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesList(workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 2:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("noNeedScrutiny",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 3:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("asmtTenIssued",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 4:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("closureReportDropped",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 5:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAssesAndAdjudication",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);	
			break;
		case 6:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("acknowledgeScrutinyCase",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		case 7:
			casesList = mstScrutinyCasesRepository.getAllScrutinyCasesListByAction("recommendedForAudit",workingLoacationList);
			commonUtility.getAssignedToRoleList(casesList);
			break;
		default:
			casesList = new ArrayList<>();
		}
		return casesList;
	}
}
