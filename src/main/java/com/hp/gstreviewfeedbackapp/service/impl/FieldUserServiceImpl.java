package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.data.CaseLogHistory;
import com.hp.gstreviewfeedbackapp.data.FoConsolidateCategoryWiseCaseData;
import com.hp.gstreviewfeedbackapp.model.CaseWorkflow;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseAssignedUsers;
import com.hp.gstreviewfeedbackapp.model.FoReviewCase;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.ActionStatusRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseStageRepository;
import com.hp.gstreviewfeedbackapp.repository.CaseWorkflowRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseAssignedUsersRepository;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.FOUserCaseReviewRepository;
import com.hp.gstreviewfeedbackapp.repository.LocationDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.RecoveryStageRepository;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleRepository;
import com.hp.gstreviewfeedbackapp.service.FieldUserService;

@Service
public class FieldUserServiceImpl implements FieldUserService {
	private static final Logger logger = LoggerFactory.getLogger(FieldUserServiceImpl.class);
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;
	@Autowired
	private ActionStatusRepository actionStatusRepository;
	@Autowired
	private FOUserCaseReviewRepository foUserCaseReviewRepository;
	@Autowired
	private LocationDetailsRepository locationDetailsRepository;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private CaseStageRepository caseStageRepository;
	@Autowired
	private RecoveryStageRepository recoveryStageRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private EnforcementReviewCaseAssignedUsersRepository enforcementReviewCaseAssignedUsersRepository;
	@Autowired
	private CaseWorkflowRepository caseWorkflowRepository;

	@Override
	public JSONArray getTotalIndicativeTaxValueCategoryWise(Map<String, String> locationMap) {
		JSONArray array = new JSONArray();
		try {
			if (locationMap != null && locationMap.size() > 0) {
				List<String> locationIds = locationMap.keySet().stream()
						.collect(Collectors.toCollection(ArrayList::new));
				List<Object[]> objectList = enforcementReviewCaseRepository
						.getTotalIndicativeTaxValueCategoryWise(locationIds);
				if (objectList != null && objectList.size() > 0) {
					for (Object[] objArray : objectList) {
						JSONObject object = new JSONObject();
						object.put("label", objArray[0].toString());
						object.put("value", objArray[1].toString());
						array.put(object);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	@Override
	public JSONArray getTotalDemandCategoryWise(Map<String, String> locationMap) {
		JSONArray array = new JSONArray();
		try {
			if (locationMap != null && locationMap.size() > 0) {
				List<String> locationIds = locationMap.keySet().stream()
						.collect(Collectors.toCollection(ArrayList::new));
				List<Object[]> objectList = enforcementReviewCaseRepository.getTotalDemandCategoryWise(locationIds);
				if (objectList != null && objectList.size() > 0) {
					for (Object[] objArray : objectList) {
						JSONObject object = new JSONObject();
						object.put("label", objArray[0].toString());
						object.put("value", objArray[1].toString());
						array.put(object);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	@Override
	public JSONArray getTotalRecoveryCategoryWise(Map<String, String> locationMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<List<String>> getTotalIndicativeTaxValueCategoryWiseList(List<String> locationIds) {
		List<List<String>> array = null;
		try {
			List<Object[]> objectList = enforcementReviewCaseRepository
					.getTotalIndicativeTaxValueCategoryWise(locationIds);
			if (objectList != null && objectList.size() > 0) {
				array = new ArrayList<>();
				List<String> infoType = new ArrayList<>();
				infoType.add("'Category'");
				infoType.add("'Amount'");
				array.add(infoType);
				for (Object[] objArray : objectList) {
					List<String> data = new ArrayList<>();
					data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
					data.add((objArray[1] != null ? objArray[1].toString() : "0"));
					array.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	public List<List<String>> getTotalDemandCategoryWiseList(List<String> locationIds) {
		List<List<String>> array = null;
		try {
			List<Object[]> objectList = enforcementReviewCaseRepository.getTotalDemandCategoryWise(locationIds);
			if (objectList != null && objectList.size() > 0) {
				array = new ArrayList<>();
				List<String> infoType = new ArrayList<>();
				infoType.add("'Category'");
				infoType.add("'Amount'");
				array.add(infoType);
				for (Object[] objArray : objectList) {
					List<String> data = new ArrayList<>();
					data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
					data.add((objArray[1] != null ? objArray[1].toString() : "0"));
					array.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	public String getDashBoardTotalCases(List<String> workingLocation) {
		String dashBoardTotalCases = enforcementReviewCaseRepository.getDashBoardTotalCases(workingLocation);
		return dashBoardTotalCases;
	}

	public String getDashBoardTotalAcknowledgedCases(List<String> workingLocation) {
		String dashBoardTotalAcknowledgedCases = enforcementReviewCaseRepository
				.getDashBoardTotalAcknowledgedCases(workingLocation);
		return dashBoardTotalAcknowledgedCases;
	}

	public String getDashBoardTotalInitiatedCases(List<String> workingLocation) {
		String dashBoardTotalInitiatedCases = enforcementReviewCaseRepository
				.getDashBoardTotalInitiatedCases(workingLocation);
		return dashBoardTotalInitiatedCases;
	}

	public String getDashBoardTotalCasesClosedByFo(List<String> workingLocation) {
		String dashBoardTotalCasesClosedByFo = enforcementReviewCaseRepository
				.getDashBoardTotalCasesClosedByFo(workingLocation);
		return dashBoardTotalCasesClosedByFo;
	}

	public Long getDashBoardTotalSuspectedIndicativeAmount(List<String> workingLocation) {
		Long dashBoardTotalSuspectedIndicativeAmount = enforcementReviewCaseRepository
				.getDashBoardTotalSuspectedIndicativeAmount(workingLocation);
		return dashBoardTotalSuspectedIndicativeAmount;
	}

	public Long getDashBoardTotalAmount(List<String> workingLocation) {
		Long dashBoardTotalAmount = enforcementReviewCaseRepository.getDashBoardTotalAmount(workingLocation);
		return dashBoardTotalAmount;
	}

	public Long getDashBoardTotalDemand(List<String> workingLocation) {
		Long dashBoardTotalDemand = enforcementReviewCaseRepository.getDashBoardTotalDemand(workingLocation);
		return dashBoardTotalDemand;
	}

	public Long getDashBoardTotalRecovery(List<String> workingLocation) {
		Long dashBoardTotalRecovery = enforcementReviewCaseRepository.getDashBoardTotalRecovery(workingLocation);
		return dashBoardTotalRecovery;
	}

	public List<List<String>> getTotalRecoveryCategoryWiseList(List<String> locationIds) {
		List<List<String>> array = null;
		try {
			List<Object[]> objectList = enforcementReviewCaseRepository.getTotalRecoveryCategoryWise(locationIds);
			if (objectList != null && objectList.size() > 0) {
				array = new ArrayList<>();
				List<String> infoType = new ArrayList<>();
				infoType.add("'Category'");
				infoType.add("'Amount'");
				array.add(infoType);
				for (Object[] objArray : objectList) {
					List<String> data = new ArrayList<>();
					data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
					data.add((objArray[1] != null ? objArray[1].toString() : "0"));
					array.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	@Override
	public List<FoConsolidateCategoryWiseCaseData> getTotalCategoryWiseCasesList(List<String> workingLocationIds) {
		List<FoConsolidateCategoryWiseCaseData> foConsolidateCategoryWiseCaseDataList = new ArrayList<>();
		try {
			List<String> periodInMonths = Arrays.asList("0-3", "3-6", "6-12", "12-24", "24-60", "60-24300");
			List<EnforcementReviewCase> listOfConsolidateCases = new ArrayList<>();
			String[] splitPeriod;
			Date latestDate;
			Date previousDate;
			for (String soloPeriod : periodInMonths) {
				splitPeriod = soloPeriod.split("-");
				latestDate = returnPreviousDate(Integer.parseInt(splitPeriod[0]));
				previousDate = returnPreviousDate(Integer.parseInt(splitPeriod[1]));
				switch (soloPeriod) {
				case "0-3":
					soloPeriod = "00-03 month";
					break;
				case "3-6":
					soloPeriod = "03-06 month";
					break;
				case "6-12":
					soloPeriod = "06-12 month";
					break;
				case "12-24":
					soloPeriod = "1-2 yrs";
					break;
				case "24-60":
					soloPeriod = "2-5 yrs";
					break;
				case "60-24300":
					soloPeriod = "Beyond 5 yrs";
					break;
				default:
					soloPeriod = "";
				}
				FoConsolidateCategoryWiseCaseData foConsolidateCategoryWiseCaseDataSolo = new FoConsolidateCategoryWiseCaseData();
				listOfConsolidateCases = enforcementReviewCaseRepository.getConsolidateCaseStageList(previousDate,
						latestDate);
				listOfConsolidateCases = listOfConsolidateCases.stream().filter(
						soloObject -> workingLocationIds.contains(soloObject.getLocationDetails().getLocationId()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> dRC01aIssued = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "DRC-01A issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> aSMT10Issued = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "ASMT-10 issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> dRc01Issued = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "DRC01 issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> caseDropped = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Case Dropped".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				// caseStage should be null and actionStatus should be 1 and action should be
				// 'acknowledge'
				/*
				 * List<EnforcementReviewCase> notApplicable = listOfConsolidateCases.stream()
				 * .filter(drc01solo -> drc01solo.getCaseStage() == null &&
				 * drc01solo.getActionStatus() != null && drc01solo.getActionStatus().getId() ==
				 * 1 && "acknowledge".equals(drc01solo.getAction())
				 * ).collect(Collectors.toList());
				 */
				List<EnforcementReviewCase> notApplicable = listOfConsolidateCases.stream().filter(
						drc01solo -> drc01solo.getActionStatus() == null || drc01solo.getActionStatus().getId() == 1)
						.filter(drc01solo -> drc01solo.getAction().equals("acknowledge")).collect(Collectors.toList());
				List<EnforcementReviewCase> notAcknowledge = listOfConsolidateCases.stream().filter(drc01solo -> {
					List<String> validActions = Arrays.asList("upload", "hqTransfer", "transfer");
					return validActions.contains(drc01solo.getAction());
				}).collect(Collectors.toList());
				List<EnforcementReviewCase> demandCreatedViaDrc07 = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Demand Created via DRC07".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> partialVoluntaryPaymentRemainingDemand = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Partial Voluntary Payment Remaining Demand"
										.equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				List<EnforcementReviewCase> demandCreatedHoweverDischargedViaDrc03 = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Demand Created However Discharged via DRC-03"
										.equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
				/*
				 * foConsolidateCategoryWiseCaseDataSolo.setPeriod(soloPeriod);
				 * foConsolidateCategoryWiseCaseDataSolo.setdRC01AIssued(dRC01aIssued.isEmpty()
				 * ? 0 : (long)dRC01aIssued.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setaSMT10Issued(aSMT10Issued.isEmpty()
				 * ? 0 : (long)aSMT10Issued.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setdRC01Issued(dRc01Issued.isEmpty() ?
				 * 0 : (long)dRc01Issued.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setCaseDropped(caseDropped.isEmpty() ?
				 * 0 : (long)caseDropped.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setNotApplicable(notApplicable.isEmpty(
				 * ) ? 0 : (long)notApplicable.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setDemandCreatedByDrc07(
				 * demandCreatedViaDrc07.isEmpty() ? 0 : (long)demandCreatedViaDrc07.size());
				 * foConsolidateCategoryWiseCaseDataSolo.setNotAcknowledge(notAcknowledge.
				 * isEmpty() ? 0 : (long)notAcknowledge.size());
				 * foConsolidateCategoryWiseCaseDataSolo.
				 * setPartialVoluntaryPaymentRemainingDemand(
				 * partialVoluntaryPaymentRemainingDemand.isEmpty() ? 0 :
				 * (long)partialVoluntaryPaymentRemainingDemand.size());
				 * foConsolidateCategoryWiseCaseDataSolo.
				 * setDemandCreatedHoweverDischargedViaDrc03(
				 * demandCreatedHoweverDischargedViaDrc03.isEmpty() ? 0 :
				 * (long)demandCreatedHoweverDischargedViaDrc03.size());
				 */
				foConsolidateCategoryWiseCaseDataSolo.setPeriod(soloPeriod);
				foConsolidateCategoryWiseCaseDataSolo
						.setdRC01AIssued(dRC01aIssued.size() > 0 ? (long) dRC01aIssued.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setaSMT10Issued(aSMT10Issued.size() > 0 ? (long) aSMT10Issued.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setdRC01Issued(dRc01Issued.size() > 0 ? (long) dRc01Issued.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setCaseDropped(caseDropped.size() > 0 ? (long) caseDropped.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setNotApplicable(notApplicable.size() > 0 ? (long) notApplicable.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setNotAcknowledge(notAcknowledge.size() > 0 ? (long) notAcknowledge.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo.setDemandCreatedByDrc07(
						demandCreatedViaDrc07.size() > 0 ? (long) demandCreatedViaDrc07.size() : 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setPartialVoluntaryPaymentRemainingDemand(partialVoluntaryPaymentRemainingDemand.size() > 0
								? (long) partialVoluntaryPaymentRemainingDemand.size()
								: 0);
				foConsolidateCategoryWiseCaseDataSolo
						.setDemandCreatedHoweverDischargedViaDrc03(demandCreatedHoweverDischargedViaDrc03.size() > 0
								? (long) demandCreatedHoweverDischargedViaDrc03.size()
								: 0);
				foConsolidateCategoryWiseCaseDataList.add(foConsolidateCategoryWiseCaseDataSolo);
			}
		} catch (Exception ex) {
			logger.info("FieldUserServiceImpl implements FieldUserService : getTotalCategoryWiseCasesList()"
					+ ex.getMessage());
		}
		return foConsolidateCategoryWiseCaseDataList;
	}

	public Date returnPreviousDate(Integer noOfMonthAgo) {
		if (noOfMonthAgo != 0) {
			noOfMonthAgo = noOfMonthAgo * -1;
			Date currentDate = new Date();
			// Create a Calendar instance and set it to the current date
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			// Subtract three months
			calendar.add(Calendar.MONTH, noOfMonthAgo);
			// Get the date three months ago
			Date threeMonthsAgo = calendar.getTime();
			return threeMonthsAgo;
		} else {
			return new Date();
		}
		// Get the current date
	}

	@Override
	public void uploadDataSaveFoLogs(List<EnforcementReviewCase> enCases) {
		try {
			List<FoReviewCase> foLogsList = new ArrayList<>();
			for (EnforcementReviewCase enCase : enCases) {
				FoReviewCase object = new FoReviewCase();
				object.setGSTIN(enCase.getId().getGSTIN());
				object.setPeriod(enCase.getId().getPeriod());
				object.setCaseReportingDate(enCase.getId().getCaseReportingDate());
				object.setTaxpayerName(enCase.getTaxpayerName());
				object.setCategory(enCase.getCategory());
				object.setIndicativeTaxValue(enCase.getIndicativeTaxValue());
				object.setAction(enCase.getAction());
				object.setAssignedTo(enCase.getAssignedTo());
				object.setAssignedFromUserId(enCase.getAssignedFromUserId());
				object.setAssignedToUserId(enCase.getAssignedToUserId());
				object.setCircle(enCase.getLocationDetails().getLocationName());
				object.setExtensionFilename((enCase.getExtensionNoDocument() != null
						&& enCase.getExtensionNoDocument().getExtensionFileName() != null)
								? enCase.getExtensionNoDocument().getExtensionFileName()
								: null);
				object.setCaseUpdatingDate(new Date());
				foLogsList.add(object);
			}
			if (foLogsList != null && foLogsList.size() > 0) {
				foUserCaseReviewRepository.saveAll(foLogsList);
			}
		} catch (Exception e) {
			logger.error("FieldUserServiceImpl.uploadDataSaveFoLogs(List<EnforcementReviewCase> enCases) : "
					+ e.getMessage());
		}
	}

	@Override
	public List<CaseLogHistory> importDataFromCaseWorkflowToCaseLogHistory(List<FoReviewCase> foReviewCaseLsit) {
		List<CaseLogHistory> caseLogHistoryList = new ArrayList<>();
		try {
			for (FoReviewCase foReviewCase : foReviewCaseLsit) {
				CaseLogHistory caseLogHistory = new CaseLogHistory();
				caseLogHistory.setGSTIN(foReviewCase.getGSTIN());
				caseLogHistory.setPeriod(foReviewCase.getPeriod());
				caseLogHistory.setCaseReportingDate(foReviewCase.getCaseReportingDate());
				caseLogHistory.setAssignedFrom(userRoleRepository.findByroleCode("FO").get().getRoleName());
				caseLogHistory.setAssignedTo(
						(foReviewCase.getAssignedTo() != null && foReviewCase.getAssignedTo().trim().length() > 0)
								? (userRoleRepository.findByroleCode(foReviewCase.getAssignedTo().trim()).isPresent()
										? userRoleRepository.findByroleCode(foReviewCase.getAssignedTo().trim()).get()
												.getRoleName()
										: foReviewCase.getAssignedTo().trim())
								: "");
				caseLogHistory.setUpdatingDate(foReviewCase.getCaseUpdatingDate());
				caseLogHistory.setAction(foReviewCase.getAction());
				caseLogHistory.setActionStatus(foReviewCase.getActionStatus() > 0
						? actionStatusRepository.findById(foReviewCase.getActionStatus()).get().getName()
						: "");
				caseLogHistory.setSuggestedJurisdiction(
						foReviewCase.getSuggestedJurisdiction() != null
								? locationDetailsRepository.findById(foReviewCase.getSuggestedJurisdiction()).get()
										.getLocationName()
								: null);
				caseLogHistory.setTransferRemarks(foReviewCase.getTransferRemarks());
				caseLogHistory.setOtherRemarks(foReviewCase.getOtherRemarks());
				caseLogHistory.setAssignedFromUser(
						(foReviewCase.getAssignedFromUserId() != null && foReviewCase.getAssignedFromUserId() > 0)
								? userDetailsRepository.getFullNameById(foReviewCase.getAssignedFromUserId()).get()
								: null);
				caseLogHistory.setAssigntoUser(
						(foReviewCase.getAssignedToUserId() != null && foReviewCase.getAssignedToUserId() > 0)
								? userDetailsRepository.getFullNameById(foReviewCase.getAssignedToUserId()).get()
								: null);
				caseLogHistory.setAssignedFromLocation(foReviewCase.getAssignedFrom());
				caseLogHistory.setAssignedToLocation(foReviewCase.getCaseJurisdiction());
				caseLogHistory.setSuggestedJurisdiction((foReviewCase.getSuggestedJurisdiction() != null
						&& foReviewCase.getSuggestedJurisdiction().trim().length() > 0)
								? (locationDetailsRepository.findById(foReviewCase.getSuggestedJurisdiction())
										.isPresent()
												? locationDetailsRepository
														.findById(foReviewCase.getSuggestedJurisdiction()).get()
														.getLocationName()
												: foReviewCase.getSuggestedJurisdiction())
								: "");
//				 caseLogHistory.setVerifierRaiseQueryRemarks();
				caseLogHistory.setTaxpayerName(foReviewCase.getTaxpayerName());
				caseLogHistory.setCategory(foReviewCase.getCategory());
				caseLogHistory.setIndicativeTaxValue(foReviewCase.getIndicativeTaxValue());
				caseLogHistory.setCaseId(foReviewCase.getCaseId());
				caseLogHistory.setCaseStage(foReviewCase.getCaseStage() > 0
						? caseStageRepository.findById(foReviewCase.getCaseStage()).get().getName()
						: null);
				caseLogHistory.setCaseStageArn(foReviewCase.getCaseStageArn());
				caseLogHistory.setDemand(foReviewCase.getDemand());
				caseLogHistory.setRecoveryStage(
						(foReviewCase.getRecoveryStage() != null && foReviewCase.getRecoveryStage() > 0)
								? recoveryStageRepository.findById(foReviewCase.getRecoveryStage()).get().getName()
								: "");
				caseLogHistory.setRecoveryByDRC3(foReviewCase.getRecoveryByDRC3());
				caseLogHistory.setRecoveryAgainstDemand(foReviewCase.getRecoveryAgainstDemand());
				caseLogHistory.setCaseId(foReviewCase.getCaseId());
				caseLogHistory.setRecoveryStageArn(foReviewCase.getRecoveryStageArn());
				caseLogHistory.setReviewMeetingComment(foReviewCase.getReviewMeetingComment());
				caseLogHistoryList.add(caseLogHistory);
			}
		} catch (Exception e) {
			logger.error(".FieldUserServiceImpl : importDataFromCaseWorkflowToCaseLogHistory : " + e.getMessage());
			e.printStackTrace();
		}
		return caseLogHistoryList;
	}

	@Override
	public List<EnforcementReviewCase> getPeriodWiseCasesList(List<String> workingLocationIds, String category,
			int index) {
		List<EnforcementReviewCase> listOfCases = new ArrayList<>();
		try {
			List<EnforcementReviewCase> listOfConsolidateCases = new ArrayList<>();
			List<Date> dates = getDateDifference(index);
			Date latestDate = dates.get(0);
			Date previousDate = dates.get(1);
			listOfConsolidateCases = enforcementReviewCaseRepository.getConsolidateCaseStageList(previousDate,
					latestDate);
			listOfConsolidateCases = listOfConsolidateCases.stream()
					.filter(soloObject -> workingLocationIds.contains(soloObject.getLocationDetails().getLocationId()))
					.collect(Collectors.toList());
			if (category.equals("notAcknowledge")) {
				listOfCases = listOfConsolidateCases.stream().filter(drc01solo -> {
					List<String> validActions = Arrays.asList("upload", "hqTransfer", "transfer");
					return validActions.contains(drc01solo.getAction());
				}).collect(Collectors.toList());
			} else if (category.equals("notApplicable")) {
				listOfCases = listOfConsolidateCases.stream().filter(
						drc01solo -> drc01solo.getActionStatus() == null || drc01solo.getActionStatus().getId() == 1)
						.filter(drc01solo -> drc01solo.getAction().equals("acknowledge")).collect(Collectors.toList());
			} else if (category.equals("dRC01AIssued")) {
				listOfCases = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "DRC-01A issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("dRC01Issued")) {
				listOfCases = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "DRC01 issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("aSMT10Issued")) {
				listOfCases = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "ASMT-10 issued".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("caseDropped")) {
				listOfCases = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Case Dropped".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("demandCreatedByDrc07")) {
				listOfCases = listOfConsolidateCases.stream()
						.filter(drc01solo -> drc01solo.getCaseStage() != null
								&& "Demand Created via DRC07".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("partialVoluntaryPaymentRemainingDemand")) {
				listOfCases = listOfConsolidateCases.stream().filter(drc01solo -> drc01solo.getCaseStage() != null
						&& "Partial Voluntary Payment Remaining Demand".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			} else if (category.equals("demandCreatedHoweverDischargedViaDrc03")) {
				listOfCases = listOfConsolidateCases.stream().filter(drc01solo -> drc01solo.getCaseStage() != null
						&& "Demand Created However Discharged via DRC-03".equals(drc01solo.getCaseStage().getName()))
						.collect(Collectors.toList());
			}
		} catch (Exception ex) {
			logger.info("FieldUserServiceImpl implements FieldUserService : getTotalCategoryWiseCasesList()"
					+ ex.getMessage());
		}
		return listOfCases;
	}

	public List<Date> getDateDifference(int index) {
		List<Date> list = new ArrayList<>();
		List<String> periodInMonths = Arrays.asList("0-3", "3-6", "6-12", "12-24", "24-60", "60-24300");
		String period = periodInMonths.get(index - 1);
		String[] splitPeriod = period.split("-");
		String firtIndex = splitPeriod[0];
		String secondIndex = splitPeriod[1];
		Date latestDate = returnPreviousDate(Integer.parseInt(firtIndex));
		Date previousDate = returnPreviousDate(Integer.parseInt(secondIndex));
		list.add(latestDate);
		list.add(previousDate);
		return list;
	}

	@Override
	public ResponseEntity<Map<String, Object>> assesmentCaseApprovalRequestSubmission(CompositeKey compositeKey,
			String needApproval, String assignToUserId, UserDetails userDetails) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<EnforcementReviewCase> enforcementReviewCase = enforcementReviewCaseRepository
					.findById(compositeKey);
			if (enforcementReviewCase.isPresent()) {
				/***********************
				 * Save in EnforcementReviewCase Table start
				 **************************/
				enforcementReviewCase.get().setAssignedFrom("FO");
				enforcementReviewCase.get().setAssignedTo("FOA");
				enforcementReviewCase.get().setAssignedToUserId(Integer.parseInt(assignToUserId));
				enforcementReviewCase.get().setCaseUpdateDate(new Date());
				/***********************
				 * Save in EnforcementReviewCase Table end
				 **************************/
				/***********************
				 * Save in EnforcementReviewCaseAssignedUsers Table Start
				 **********************/
				EnforcementReviewCaseAssignedUsers enforcementReviewCaseAssignedUsers = enforcementReviewCaseAssignedUsersRepository
						.findById(compositeKey).get();
				enforcementReviewCaseAssignedUsers.setFoaUserId(Integer.parseInt(assignToUserId));
				/***********************
				 * Save in EnforcementReviewCaseAssignedUsers Table End
				 **********************/
				/***********************
				 * Save in EnforcementCaseWorkFlow Table start
				 **********************/
				CaseWorkflow caseWorkflow = new CaseWorkflow();
				caseWorkflow.setGSTIN(enforcementReviewCase.get().getId().getGSTIN());
				caseWorkflow.setPeriod(enforcementReviewCase.get().getId().getPeriod());
				caseWorkflow.setCaseReportingDate(enforcementReviewCase.get().getId().getCaseReportingDate());
				caseWorkflow.setAction(enforcementReviewCase.get().getAction());
				caseWorkflow.setAssignedFrom(enforcementReviewCase.get().getAssignedFrom());
				caseWorkflow
						.setAssignedFromLocationId(enforcementReviewCase.get().getLocationDetails().getLocationId());
				caseWorkflow.setAssignedFromUserId(userDetails.getUserId());
				caseWorkflow.setAssignedTo(enforcementReviewCase.get().getAssignedTo());
				caseWorkflow.setAssignedToLocationId(enforcementReviewCase.get().getLocationDetails().getLocationId());
				caseWorkflow.setAssigntoUserId(Integer.parseInt(assignToUserId));
				caseWorkflow.setUpdatingDate(new Date());
				caseWorkflow.setAssignedFromLocationName(
						enforcementReviewCase.get().getLocationDetails().getLocationName());
				caseWorkflow
						.setAssignedToLocationName(enforcementReviewCase.get().getLocationDetails().getLocationName());
				/***********************
				 * Save in EnforcementCaseWorkFlow Table end
				 **********************/
				enforcementReviewCaseRepository.save(enforcementReviewCase.get());
				enforcementReviewCaseAssignedUsersRepository.save(enforcementReviewCaseAssignedUsers);
				caseWorkflowRepository.save(caseWorkflow);
				response.put("success", true);
				response.put("message", "Case assigned successfully!");
				return ResponseEntity.ok(response);
			}
			response.put("success", false);
			response.put("message", "Anonymous form submission!");
			return ResponseEntity.unprocessableEntity().body(response);
		} catch (Exception e) {
			logger.error("FieldUserServiceImpl : assesmentCaseApprovalRequestSubmission() : " + e.getMessage());
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
