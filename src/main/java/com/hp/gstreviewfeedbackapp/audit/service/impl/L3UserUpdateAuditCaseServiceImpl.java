package com.hp.gstreviewfeedbackapp.audit.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hp.gstreviewfeedbackapp.audit.data.L3UserAuditCaseUpdate;
import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseDateDocumentDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFarDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseFinalAmountRecoveryDetails;
import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDarDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseDateDocumentDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseFarDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseFinalAmountRecoveryDetailsRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditCaseStatusRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterCasesAllocatingUsersRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditParaCategoryRepository;
import com.hp.gstreviewfeedbackapp.audit.service.AuditCaseDocumentService;
import com.hp.gstreviewfeedbackapp.audit.service.L2UserCaseAssignmentService;
import com.hp.gstreviewfeedbackapp.audit.service.L3UserUpdateAuditCaseService;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.UserDetailsRepository;

@Service
public class L3UserUpdateAuditCaseServiceImpl implements L3UserUpdateAuditCaseService {
	private static final Logger logger = LoggerFactory.getLogger(L3UserUpdateAuditCaseServiceImpl.class);
	@Autowired
	private AuditMasterRepository auditMasterRepository;
	@Autowired
	private AuditCaseStatusRepository auditCaseStatusRepository;
	@Autowired
	private AuditParaCategoryRepository auditParaCategoryRepository;
	@Autowired
	private AuditCaseDateDocumentDetailsRepository auditCaseDateDocumentDetailsRepository;
	@Autowired
	private AuditCaseDocumentService auditCaseDocumentService;
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private L2UserCaseAssignmentService l2UserCaseAssignmentService;
	@Autowired
	private AuditMasterCasesAllocatingUsersRepository auditMasterCasesAllocatingUsersRepository;
	@Autowired
	private AuditCaseDarDetailsRepository auditCaseDarDetailsRepository;
	@Autowired
	private AuditCaseFarDetailsRepository auditCaseFarDetailsRepository;
	@Autowired
	private AuditCaseFinalAmountRecoveryDetailsRepository auditCaseClosureReportDetailsRepository;
	@Value("${upload.audit.teamlead.directory}")
	private String uploadDirectory;

	@Override
	@Transactional
	public String updateAuditCaseForADT01IssuedOrAuditPlanOrCommencementOfAuditOrDiscrepancyNotice(
			L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				AuditCaseDateDocumentDetails caseDateDocumentDetails = null;
				if (auditCaseStatus.getCategory().equals("Audit Plan") && auditCaseDateDocumentDetailsRepository
						.findTheLatestStatusDataByCaseIdAndAuditStatusIdList(l3UserAuditCaseUpdate.getCaseId(),
								auditCaseStatusRepository.findAllIdByCategory(auditCaseStatus.getCategory()))
						.isPresent()) {
					caseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
							.findTheLatestStatusDataByCaseIdAndAuditStatusIdList(l3UserAuditCaseUpdate.getCaseId(),
									auditCaseStatusRepository.findAllIdByCategory(auditCaseStatus.getCategory()))
							.get();
				} else if (auditCaseDateDocumentDetailsRepository.findTheLatestStatusDataByCaseIdAndAuditStatusId(
						l3UserAuditCaseUpdate.getCaseId(), auditCaseStatus.getId()).isPresent()) {
					caseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
							.findTheLatestStatusDataByCaseIdAndAuditStatusId(l3UserAuditCaseUpdate.getCaseId(),
									auditCaseStatus.getId())
							.get();
				} else {
					caseDateDocumentDetails = new AuditCaseDateDocumentDetails();
				}
				if (!auditCaseStatus.getUsedByRole().equalsIgnoreCase("l3")) {
					auditCaseStatus = auditCaseStatusRepository.findById(auditCaseStatus.getId() + 1).get();
				}
				caseDateDocumentDetails.setAction(auditCaseStatus);
				l3UserAuditCaseUpdate.setUpdateStatusId(auditCaseStatus.getId());
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				if (auditCaseStatus.getStatus().equals("auditplansubmitted")
						|| auditCaseStatus.getStatus().equals("auditplanresubmitted")) {
					auditMaster.setAssignedFrom("L3");
					auditMaster.setAssignTo("L2");
				}
				auditMasterRepository.save(auditMaster);
				if (auditCaseStatus.getStatus().equals("auditplansubmitted")
						|| auditCaseStatus.getStatus().equals("auditplanresubmitted")) {
					l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster,
							objectUserDetails.get().getUserId(), auditMasterCasesAllocatingUsersRepository
									.findById(auditMaster.getCaseId()).get().getL2User());
				}
//				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(caseDateDocumentDetails, objectUserDetails.get());
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(
						auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(
								caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction()).get(),
						objectUserDetails.get());
				return auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get()
						.getSuccessMesage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForADT01Issued : " + e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public String updateAuditCaseForDar(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			List<AuditCaseDarDetails> darDetailsListToDelete = new ArrayList<>();
			List<Long> updatedDarDetailsIdList = new ArrayList<>();
			String assignedToOfficer = "";
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				AuditCaseDateDocumentDetails caseDateDocumentDetails = (auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndActionStausIdList(auditMaster.getCaseId(), auditCaseStatusRepository
								.findAllIdByCategory(auditCaseStatus.getCategory()))
						.isPresent()
								? auditCaseDateDocumentDetailsRepository
										.findByCaseIdAndActionStausIdList(auditMaster.getCaseId(),
												auditCaseStatusRepository
														.findAllIdByCategory(auditCaseStatus.getCategory()))
										.get()
								: new AuditCaseDateDocumentDetails());
				if (!auditCaseStatus.getUsedByRole().equalsIgnoreCase("l3")) {
					auditCaseStatus = auditCaseStatusRepository.findById(auditCaseStatus.getId() + 1).get();
				}
				caseDateDocumentDetails.setAction(auditCaseStatus);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				caseDateDocumentDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
				List<AuditCaseDarDetails> auditCaseDarDetailsList = ((caseDateDocumentDetails
						.getAuditCaseDarDetailsList() != null
						&& caseDateDocumentDetails.getAuditCaseDarDetailsList().size() > 0)
								? caseDateDocumentDetails.getAuditCaseDarDetailsList()
								: new ArrayList<>());
				// Existing Para(s) will be updated here
				if (auditCaseDarDetailsList != null && auditCaseDarDetailsList.size() > 0) {
					// If user change the NilDar option from NO to YES then delete all existing
					// Para(s)
					if (l3UserAuditCaseUpdate.getNilDar().equalsIgnoreCase("YES")) {
						darDetailsListToDelete.addAll(auditCaseDarDetailsList);
					} else if (l3UserAuditCaseUpdate.getNilDar().equalsIgnoreCase("NO")) {
						List<List<String>> newPara = new ArrayList<>(); // New para will add in this List
						for (List<String> para : l3UserAuditCaseUpdate.getPara()) {
							// If para list 5 element available then it's a old para
							if (para.size() > 5 && para.get(5).length() > 0) {
								AuditCaseDarDetails auditCaseDarDetails = auditCaseDarDetailsList.stream().filter(d -> {
									return d.getId().equals(Long.parseLong(para.get(5)));
								}).findFirst().orElse(null);
								if (auditCaseDarDetails != null) {
									auditCaseDarDetails.setAuditParaCategory(
											auditParaCategoryRepository.findById(Integer.parseInt(para.get(0))).get());
									auditCaseDarDetails.setAmountInvolved(Long.parseLong(para.get(1)));
									auditCaseDarDetails.setAmountRecovered(Long.parseLong(para.get(2)));
									auditCaseDarDetails.setAmountDropped(Long.parseLong(para.get(3)));
									auditCaseDarDetails.setAmountToBeRecovered(Long.parseLong(para.get(4)));
									auditCaseDarDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
									auditCaseDarDetails.setActionDate(caseDateDocumentDetails.getActionDate());
									auditCaseDarDetails
											.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
									auditCaseDarDetails.setAuditCaseDateDocumentDetailsId(caseDateDocumentDetails);
									updatedDarDetailsIdList.add(auditCaseDarDetails.getId());
								} else {
									newPara.add(para);
								}
							} else {
								newPara.add(para);
							}
						}
						l3UserAuditCaseUpdate.setPara(newPara);
					}
					if (auditCaseDarDetailsList.size() > updatedDarDetailsIdList.size()) {
						darDetailsListToDelete.addAll(auditCaseDarDetailsList.stream()
								.filter(d -> !updatedDarDetailsIdList.contains(d.getId()))
								.collect(Collectors.toList()));
					}
				}
				// New Para(s) will be uploaded here
				if (l3UserAuditCaseUpdate.getNilDar().equalsIgnoreCase("NO")) {
					for (List<String> para : l3UserAuditCaseUpdate.getPara()) {
						AuditCaseDarDetails auditCaseDarDetails = new AuditCaseDarDetails();
						auditCaseDarDetails.setActionDate(caseDateDocumentDetails.getActionDate());
						if (para.get(0) != null && para.get(0).length() > 0) {
							auditCaseDarDetails.setAuditParaCategory(
									auditParaCategoryRepository.findById(Integer.parseInt(para.get(0))).get());
						}
						if (para.get(1) != null && para.get(1).length() > 0) {
							auditCaseDarDetails.setAmountInvolved(Long.parseLong(para.get(1)));
						}
						if (para.get(2) != null && para.get(2).length() > 0) {
							auditCaseDarDetails.setAmountRecovered(Long.parseLong(para.get(2)));
						}
						if (para.get(3) != null && para.get(3).length() > 0) {
							auditCaseDarDetails.setAmountDropped(Long.parseLong(para.get(3)));
						}
						if (para.get(4) != null && para.get(4).length() > 0) {
							auditCaseDarDetails.setAmountToBeRecovered(Long.parseLong(para.get(4)));
						}
						auditCaseDarDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
						auditCaseDarDetails.setActionDate(caseDateDocumentDetails.getActionDate());
						auditCaseDarDetails.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
						auditCaseDarDetails.setAuditCaseDateDocumentDetailsId(caseDateDocumentDetails);
						auditCaseDarDetailsList.add(auditCaseDarDetails);
					}
					if (auditCaseDarDetailsList != null && auditCaseDarDetailsList.size() > 0) {
						caseDateDocumentDetails.setAuditCaseDarDetailsList(auditCaseDarDetailsList);
					}
				}
				// Save PDF
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				// Saving Reply PDF if any
				if (l3UserAuditCaseUpdate.getPdfDataReply().getPdfFile().getSize() > 0l) {
					MultipartFile replyPdfFile = l3UserAuditCaseUpdate.getPdfDataReply().getPdfFile();
					String replyPdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
							+ formattedTimestamp + "_taxpayerReplyDocument.pdf";
					replyPdfFile.transferTo(new File(uploadDirectory, replyPdfFileName));
					caseDateDocumentDetails.setOtherFilePath(replyPdfFileName);
				} else {
					// If the previous file name present here then the file name will remove
					caseDateDocumentDetails.setOtherFilePath(null);
				}
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				auditMaster.setTotalInvolvedAmount(l3UserAuditCaseUpdate.getTotalInvolvedAmount());
				if (auditCaseStatus.getStatus().equals("darsubmitted")
						|| auditCaseStatus.getStatus().equals("darresubmitted")) {
					if (auditMaster.getTotalInvolvedAmount() != null
							&& auditMaster.getTotalInvolvedAmount().compareTo(1000000l) > 0) {
						auditMaster.setAssignedFrom("L3");
						auditMaster.setAssignTo("MCM");
						assignedToOfficer = " MCM ";
					} else {
						auditMaster.setAssignedFrom("L3");
						auditMaster.setAssignTo("L2");
						assignedToOfficer = " Allocating Officer ";
					}
				}
				auditMasterRepository.save(auditMaster);
				l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster,
						objectUserDetails.get().getUserId(),
						auditMasterCasesAllocatingUsersRepository.findById(auditMaster.getCaseId()).get().getL2User());
				AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction())
						.get();
				// Save AuditCaseDateDocumentDetailsLogs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails,
						objectUserDetails.get());
				// Save AuditCaseDarDetailsLogs
				auditCaseDarDetailsList = auditCaseDarDetailsRepository
						.findByAuditCaseDateDocumentDetailsIdAndLastUpdatedTimeStamp(auditCaseDateDocumentDetails,
								caseDateDocumentDetails.getLastUpdatedTimeStamp());
				if (auditCaseDarDetailsList != null && auditCaseDarDetailsList.size() > 0) {
					auditCaseDocumentService.saveAuditCaseDarDetailsLogs(auditCaseDarDetailsList,
							objectUserDetails.get(), "updated/uploaded by L3 officer");
				}
				auditCaseDarDetailsList = null;
				auditCaseDateDocumentDetails = null;
				auditMaster = null;
				caseDateDocumentDetails = null;
				objectAM = null;
				if (darDetailsListToDelete != null && !darDetailsListToDelete.isEmpty()) {
					auditCaseDocumentService.saveAuditCaseDarDetailsLogs(darDetailsListToDelete,
							objectUserDetails.get(), "deleted by L3 officer");
					darDetailsListToDelete.stream().forEach(details -> details.setAuditCaseDateDocumentDetailsId(null));
					auditCaseDarDetailsRepository.deleteAll(darDetailsListToDelete);
				}
				return auditCaseStatus.getSuccessMesage() + assignedToOfficer + "successfully.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForDar : " + e.getMessage());
		}
		return "failed";
	}

	@Override
	public Integer checkIfTheActiveStausIsAuditPlanOrDar(String caseId, Integer activeActionPannelId,
			String statusCategory) {
		Optional<AuditMaster> amObject = auditMasterRepository.findById(caseId);
		if (amObject.isPresent()) {
			AuditMaster auditMaster = amObject.get();
			if (auditMaster.getAction().getCategory().equals(statusCategory) && auditCaseStatusRepository
					.findById(activeActionPannelId).get().getCategory().equals(statusCategory)) {
				return auditMaster.getAction().getId();
			}
		}
		return activeActionPannelId;
	}

	@Override
	@Transactional
	public String updateAuditCaseForAdt02Far(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				boolean fullyRecoveredFlag = true;
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				AuditCaseDateDocumentDetails caseDateDocumentDetails = (auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(auditMaster,
								auditCaseStatusRepository.findByStatus("farsubmitted").get())
						.isPresent()
								? auditCaseDateDocumentDetailsRepository.findByCaseIdAndAction(auditMaster,
										auditCaseStatusRepository.findByStatus("farsubmitted").get()).get()
								: new AuditCaseDateDocumentDetails());
				caseDateDocumentDetails.setAction(auditCaseStatus);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				caseDateDocumentDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
				List<AuditCaseFarDetails> auditCaseFarDetailsList = new ArrayList<>();
				// New Para(s) will be uploaded here
				if (l3UserAuditCaseUpdate.getNilDar().equalsIgnoreCase("NO")) {
					for (List<String> para : l3UserAuditCaseUpdate.getPara()) {
						AuditCaseFarDetails auditCaseFarDetails = new AuditCaseFarDetails();
						auditCaseFarDetails.setActionDate(caseDateDocumentDetails.getActionDate());
						if (para.get(0) != null && para.get(0).length() > 0) {
							auditCaseFarDetails.setAuditParaCategory(
									auditParaCategoryRepository.findById(Integer.parseInt(para.get(0))).get());
						}
						if (para.get(1) != null && para.get(1).length() > 0) {
							auditCaseFarDetails.setAmountInvolved(Long.parseLong(para.get(1)));
						}
						if (para.get(2) != null && para.get(2).length() > 0) {
							auditCaseFarDetails.setAmountRecovered(Long.parseLong(para.get(2)));
						}
						if (para.get(3) != null && para.get(3).length() > 0) {
							auditCaseFarDetails.setAmountDropped(Long.parseLong(para.get(3)));
						}
						if (para.get(4) != null && para.get(4).length() > 0) {
							auditCaseFarDetails.setAmountToBeRecovered(Long.parseLong(para.get(4)));
							if (fullyRecoveredFlag == true
									&& auditCaseFarDetails.getAmountToBeRecovered().compareTo(0l) > 0) {
								// Amount To Be Recovered is still not zero
								fullyRecoveredFlag = false;
							}
						}
						auditCaseFarDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
						auditCaseFarDetails.setActionDate(caseDateDocumentDetails.getActionDate());
						auditCaseFarDetails.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
						auditCaseFarDetails.setAuditCaseDateDocumentDetailsId(caseDateDocumentDetails);
						auditCaseFarDetailsList.add(auditCaseFarDetails);
					}
					if (auditCaseFarDetailsList != null && auditCaseFarDetailsList.size() > 0) {
						caseDateDocumentDetails.setAuditCaseFarDetailsList(auditCaseFarDetailsList);
					}
				}
				if (fullyRecoveredFlag == false) {
					auditMaster.setFullyRecovered("false");
				} else {
					auditMaster.setFullyRecovered("true");
				}
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				auditMasterRepository.save(auditMaster);
				AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction())
						.get();
				// Save AuditCaseDateDocumentDetailsLogs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails,
						objectUserDetails.get());
				// Save AuditCaseFarDetailsLogs
				auditCaseFarDetailsList = auditCaseFarDetailsRepository
						.findByAuditCaseDateDocumentDetailsIdAndLastUpdatedTimeStamp(auditCaseDateDocumentDetails,
								caseDateDocumentDetails.getLastUpdatedTimeStamp());
				if (auditCaseFarDetailsList != null && auditCaseFarDetailsList.size() > 0) {
					auditCaseDocumentService.saveAuditCaseFarDetailsLogs(auditCaseFarDetailsList,
							objectUserDetails.get(), "updated/uploaded by L3 officer");
				}
				return auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get().getName()
						+ " details updated successfully";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForDar : " + e.getMessage());
		}
		return "failed";
	}

	@Override
	@Transactional
	public String updateAuditCaseForFinalAmountRecovery(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				AuditCaseDateDocumentDetails caseDateDocumentDetails = new AuditCaseDateDocumentDetails();
				boolean fullyRecoveredFlag = true;
				long toalAmountToBeRecovered = 0l;
				caseDateDocumentDetails.setAction(auditCaseStatus);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				caseDateDocumentDetails.setNilDar(l3UserAuditCaseUpdate.getNilDar());
				List<AuditCaseFinalAmountRecoveryDetails> auditCaseFar2DetailsList = new ArrayList<>();
				// New Para(s) will be uploaded here
				if (l3UserAuditCaseUpdate.getNilDar().equalsIgnoreCase("NO")) {
					for (List<String> para : l3UserAuditCaseUpdate.getPara()) {
						AuditCaseFinalAmountRecoveryDetails auditCaseFar2Details = new AuditCaseFinalAmountRecoveryDetails();
						auditCaseFar2Details.setActionDate(caseDateDocumentDetails.getActionDate());
						if (para.get(0) != null && para.get(0).length() > 0) {
							auditCaseFar2Details.setAuditParaCategory(
									auditParaCategoryRepository.findById(Integer.parseInt(para.get(0))).get());
						}
						if (para.get(1) != null && para.get(1).length() > 0) {
							auditCaseFar2Details.setAmountInvolved(Long.parseLong(para.get(1)));
						}
						if (para.get(2) != null && para.get(2).length() > 0) {
							auditCaseFar2Details.setAmountRecovered(Long.parseLong(para.get(2)));
						}
						if (para.get(3) != null && para.get(3).length() > 0) {
							auditCaseFar2Details.setAmountDropped(Long.parseLong(para.get(3)));
						}
						if (para.get(4) != null && para.get(4).length() > 0) {
							auditCaseFar2Details.setAmountToBeRecovered(Long.parseLong(para.get(4)));
							toalAmountToBeRecovered = toalAmountToBeRecovered
									+ auditCaseFar2Details.getAmountToBeRecovered();
							if (fullyRecoveredFlag == true
									&& auditCaseFar2Details.getAmountToBeRecovered().compareTo(0l) > 0) {
								// Amount To Be Recovered is still not zero
								fullyRecoveredFlag = false;
							}
						}
						auditCaseFar2Details.setNilDar(l3UserAuditCaseUpdate.getNilDar());
						auditCaseFar2Details.setActionDate(caseDateDocumentDetails.getActionDate());
						auditCaseFar2Details.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
						auditCaseFar2Details.setAuditCaseDateDocumentDetailsId(caseDateDocumentDetails);
						auditCaseFar2DetailsList.add(auditCaseFar2Details);
					}
					if (auditCaseFar2DetailsList != null && auditCaseFar2DetailsList.size() > 0) {
						caseDateDocumentDetails.setAuditCaseFinalAmountRecoveryDetailsList(auditCaseFar2DetailsList);
					}
				}
				if (fullyRecoveredFlag == false) {
					auditMaster.setFullyRecovered("false");
				} else {
					auditMaster.setFullyRecovered("true");
				}
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setToalAmountToBeRecovered(toalAmountToBeRecovered);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				auditMasterRepository.save(auditMaster);
				AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction())
						.get();
				// Save AuditCaseDateDocumentDetailsLogs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails,
						objectUserDetails.get());
				// Save AuditCaseColsureReportDetailsLogs
				auditCaseFar2DetailsList = auditCaseClosureReportDetailsRepository
						.findByAuditCaseDateDocumentDetailsIdAndLastUpdatedTimeStamp(auditCaseDateDocumentDetails,
								caseDateDocumentDetails.getLastUpdatedTimeStamp());
				if (auditCaseFar2DetailsList != null && auditCaseFar2DetailsList.size() > 0) {
					auditCaseDocumentService.saveAuditCaseFinalAmountRecoveryDetailsLogs(auditCaseFar2DetailsList,
							objectUserDetails.get(), "updated/uploaded by L3 officer");
				}
				return auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get().getName()
						+ " details updated successfully";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForDar : " + e.getMessage());
		}
		return "failed";
	}

	@Override
	@Transactional
	public String updateAuditCaseForShowCauseNotice(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				AuditCaseDateDocumentDetails caseDateDocumentDetails = new AuditCaseDateDocumentDetails();
				caseDateDocumentDetails.setAction(auditCaseStatus);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				auditMaster.setFoUserDetailsForShowCauseNotice(
						userDetailsRepository.findById(l3UserAuditCaseUpdate.getFoUserId()).get());
				auditMaster.setRecommendedModule("ASSESSMENT");
				auditMaster.setArnNumber(l3UserAuditCaseUpdate.getArn());
				auditMaster.setFullyRecovered("false");
				auditMasterRepository.save(auditMaster);
				AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction())
						.get();
				// Save AuditCaseDateDocumentDetailsLogs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails,
						objectUserDetails.get());
				L3UserAuditCaseUpdate l3UserAuditCaseUpdate1 = new L3UserAuditCaseUpdate();
				l3UserAuditCaseUpdate1.setCaseId(l3UserAuditCaseUpdate.getCaseId());
				l3UserAuditCaseUpdate1.setWorkingDate(l3UserAuditCaseUpdate.getWorkingDateClosureReport());
				l3UserAuditCaseUpdate1.setPdfData(l3UserAuditCaseUpdate.getPdfDataClosureReport());
				l3UserAuditCaseUpdate1
						.setUpdateStatusId(auditCaseStatusRepository.findByStatus("closurereportissued").get().getId());
				String closureReportMessage = updateAuditCaseForClosureReport(l3UserAuditCaseUpdate1);
				if (!closureReportMessage.equalsIgnoreCase("failed")) {
					return auditCaseStatus.getName() + " and " + auditCaseStatusRepository
							.findById(l3UserAuditCaseUpdate1.getUpdateStatusId()).get().getName()
							+ " details updated successfully";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForShowCauseNotice : " + e.getMessage());
		}
		return "failed";
	}

	@Override
	@Transactional
	public String updateAuditCaseForClosureReport(L3UserAuditCaseUpdate l3UserAuditCaseUpdate) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDetails> objectUserDetails = userDetailsRepository
					.findByloginNameIgnoreCase(authentication.getName());
			if (!objectUserDetails.isPresent()) {
				return "redirect:/logout";
			}
			Optional<AuditMaster> objectAM = auditMasterRepository.findById(l3UserAuditCaseUpdate.getCaseId());
			if (objectAM.isPresent()) {
				AuditMaster auditMaster = objectAM.get();
				AuditCaseStatus auditCaseStatus = auditCaseStatusRepository
						.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get();
				if (auditMaster.getFullyRecovered() != null && auditMaster.getFullyRecovered().equals("true")) {
					auditCaseStatus = auditCaseStatusRepository.findByStatus("auditcompleted").get();
				}
				AuditCaseDateDocumentDetails caseDateDocumentDetails = new AuditCaseDateDocumentDetails();
				caseDateDocumentDetails.setAction(auditCaseStatus);
				caseDateDocumentDetails.setCaseId(auditMaster);
				caseDateDocumentDetails.setLastUpdatedTimeStamp(new Date());
				caseDateDocumentDetails.setActionDate(l3UserAuditCaseUpdate.getWorkingDate());
				MultipartFile pdfFile = l3UserAuditCaseUpdate.getPdfData().getPdfFile();
				// Save PDF
				LocalDateTime currentTimestamp = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
				String formattedTimestamp = currentTimestamp.format(formatter);
				String pdfFileName = auditMaster.getCaseId().replace('\\', '.').replace('/', '.') + "_pdf_"
						+ formattedTimestamp + "_" + auditCaseStatus.getStatus() + ".pdf";
				pdfFile.transferTo(new File(uploadDirectory, pdfFileName));
				caseDateDocumentDetails.setActionFilePath(pdfFileName);
				List<AuditCaseDateDocumentDetails> list = new ArrayList<>();
				list.add(caseDateDocumentDetails);
				auditMaster.setAction(auditCaseStatus);
				auditMaster.setLastUpdatedTimeStamp(caseDateDocumentDetails.getLastUpdatedTimeStamp());
				auditMaster.setAuditCaseDateDocumentDetails(list);
				if (auditMaster.getFullyRecovered() != null && auditMaster.getFullyRecovered().equals("false")) {
					auditMaster.setAssignedFrom("L3");
					auditMaster.setAssignTo("L2");
					l2UserCaseAssignmentService.saveAuditMasterCaseWorkflowDetails(auditMaster,
							objectUserDetails.get().getUserId(), auditMasterCasesAllocatingUsersRepository
									.findById(auditMaster.getCaseId()).get().getL2User());
				}
				auditMasterRepository.save(auditMaster);
				AuditCaseDateDocumentDetails auditCaseDateDocumentDetails = auditCaseDateDocumentDetailsRepository
						.findByCaseIdAndAction(caseDateDocumentDetails.getCaseId(), caseDateDocumentDetails.getAction())
						.get();
				// Save AuditCaseDateDocumentDetailsLogs
				auditCaseDocumentService.saveAuditCaseDateDocumentDetailsLogs(auditCaseDateDocumentDetails,
						objectUserDetails.get());
				return auditCaseStatusRepository.findById(l3UserAuditCaseUpdate.getUpdateStatusId()).get()
						.getSuccessMesage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("L3UserUpdateAuditCaseServiceImpl : updateAuditCaseForShowCauseNotice : " + e.getMessage());
		}
		return "failed";
	}
	
	@Override
	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations) {

        List<Object[]> rawData = auditMasterRepository.getAnnexureReportRaw(allMappedLocations);
        List<AnnexureReportRow> result = new ArrayList<>();

        for (Object[] row : rawData) {
            AnnexureReportRow dto = new AnnexureReportRow();
            dto.setZoneName((String) row[0]);
            dto.setCircleName((String) row[1]);
            dto.setAllottedCases(((Number) row[2]).intValue());
            dto.setAuditCasesCompleted(((Number) row[3]).intValue());
            dto.setPendingDeskReview(((Number) row[4]).intValue());
            dto.setPendingApprovalAuditPlan(((Number) row[5]).intValue());
            dto.setPendingExaminationBooks(((Number) row[6]).intValue());
            dto.setPendingDAR(((Number) row[7]).intValue());
            dto.setPendingFAR(((Number) row[8]).intValue());
            result.add(dto);
        }
        return result;
    
	}
	@Override
	public	List<String> getDashboardData(List<String> allMappedLocations){

        List<Object[]> resultList = auditMasterRepository.getDashboardMetrics(allMappedLocations);

        if (resultList != null && !resultList.isEmpty()) {
            Object[] result = resultList.get(0);
            List<String> dashboardData = new ArrayList<>();

            for (Object obj : result) {
                dashboardData.add(obj != null ? obj.toString() : "0");
            }
            return dashboardData;
        }

        return Arrays.asList("0", "0", "0", "0", "0", "0");
	}
}
