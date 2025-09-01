package com.hp.gstreviewfeedbackapp.constants;
public class ApplicationConstants {
	public static final String EXCEL_HEADER_COLUMNS_NOT_MATCHED = "Excel Header columns are not matched.";
	public static final String CASE_REPORTING_DATE = "Case Reporting Date";
	public static final String PERIOD = "Period";
	public static final String GSTIN = "GSTIN";
	public static final String CASE_REPORTING_DATE_ERROR_MESSAGE = "Case Reporting Date is not in the correct format (dd-MM-yyyy)";
	public static final String FORM_SUBMITTED_SUCCESSFULLY = "Form submitted successfully!";
	public static final String DATA_STORE_PROCESS_NOT_COMPLETED = "Data store process not completed.Please try it again!";
	public static final String PDF_FILE_NOT_UPLOADED = "Please upload PDF";
	public static final String EXCEL_FILE_NOT_UPLOADED = "Please upload EXCEL";
	public static final String COLUMNS_SIZE_NOT_MATCHED = "Column numbers is not matched";
	public static final String CIRCLE = "Assigned To Circle";
	public static final String JURISDICTION_DOES_NOT_EXIST = "Jurisdiction does not exist";
	public static final String DASHBOARD = "dashboard";
	// Headquarters Officer
	public static final String HQ = "hq";
	public static final String HO_UPLOAD_REVIEW_CASES = "upload_review_cases";
	public static final String HO_REVIEW_SUMMARY_LIST = "review_summary_list";
	public static final String HO_REQUEST_FOR_TRANSFER = "request_for_transfer";
	public static final String HQ_UPDATE_ENFORCEMENT_CASE = "update_enforcement_case";
	public static final String HQ_VIEW_ENFORCEMENT_CASE = "view_enforcement_cases";
	public static final String HQ_VIEW_ENFORCEMENT_CASE_HISTROEY = "view_enforcement_case_history";
	public static final String HQ_VERIFY_CASE_CASE = "verify_recovery";
	// CAG Officer
	public static final String CAG_HQ = "cag_hq";
	public static final String CAG_DASHBOARD = "dashboard";
	public static final String CAG_UPLOAD_CASES = "upload_cases";
	public static final String CAG_REQUEST_FOR_TRANSFER = "request_for_transfer";
	public static final String CAG_FO = "cag_fo";
	public static final String CAG_FO_DASHBOARD = "dashboard";
	public static final String CAG_ACKNOWLEDGE_CASES = "acknowledge_transfer_cases";
	public static final String CAG_UPDATE_SUMMARY_LIST = "update_summary_list";
	public static final String CAG_FO_VIEW = "view";
	public static final String CAG_VIEW_LIST_OF_CASE = "view_list_of_case";
	// Field Officer
	public static final String FO = "fo";
	public static final String FO_ACKNOWLEDGE_CASES = "acknowledge_cases";
	public static final String FO_UPDATE_SUMMARY_LIST = "update_summary_list";
	public static final String FO_ACKNOWLEDGE = "fo_acknowledge_cases";
	public static final String FO_TRANSFER = "fo_transfer_case";
	public static final String FO_RAISED_QUERY_UPDATE_SUMMARY_LIST = "raised_query_update_summary_list";
	public static final String FO_REJECTED_UPDATE_SUMMARY_LIST = "rejected_update_summary_list";
	public static final String FO_REVIEW_CATEGORY_CASE = "review_category_case";
	public static final String FO_VIEW_LIST_OF_CASE = "view_list_of_case";
	public static final String FO_SELF_DETECTED_CASES = "self_detected_cases";
	public static final String FO_CASE_UPLOAD_WITH_MANUAL_PERIOND_INSERTION = "case_upload_after_manual_period_insertion";
	public static final String FO_SAVE_CASES_WITH_MANUAL_PERIOND_INSERTION = "save_cases_with_manual_period_insertion";
	public static final String FO_UPDATE_RECOVERY_STATUS = "update_recovery_status";
	public static final String FO_RECOVERY_REVERTED_BY_APPROVER = "recovery_reverted_by_approver";
	public static final String FO_GET_SCRUTINY_DATA_LIST = "get_scrutiny_data_list";
	public static final String FO_GET_CASE_ASSIGN_FORM = "get_case_assign_form";
	// Admin Officer
	public static final String ADMIN = "admin";
	public static final String ADMIN_CREATE_USER = "create_user";
	public static final String ADMIN_UPDATE_USER_ROLE = "update_user_role";
	public static final String ADMIN_UPDATE_USER = "update_user";
	public static final String ADMIN_DELETE_USER = "delete_user";
	public static final String ADMIN_TRANSFER_ROLE = "transfer_role";
	public static final String ADMIN_UPDATE_TRANSFER_ROLE = "update_transfer_role";
	// Verifier / Reviewer Officer
	public static final String RU = "ru";
	public static final String RU_VERIFY_CASES_STATUS = "verify_cases_status";
	public static final String RU_RECOMMENDED_FOR_CLOSURE = "recommended_closure";
	public static final String RU_RECOMMENDED_FOR_CLOSURE_WITH_REMARKS = "recommended_closure_with_remarks";
	public static final String RU_RECOMMENDED_FOR_CLOSURE_WITH_RAISE_QUERY_REMARKS = "recommended_closure_with_raise_query_remarks";
	public static final String GET_RU_VERIFY_RAISED_QUERY_LIST = "raised_query_list";
	public static final String APPEAL_REVISION_CASE = "appeal_revision_case";
	public static final String APPEAL_REVISION_REJECTED_CASE = "appeal_revision_rejected_case";
	public static final String RU_ONCE_AGAIN_APPEAL_REVISION_REJECTED_CASE = "ru_once_again_appeal_revision_rejected_case";
	public static final String APPEAL_REVISION_RECOMMENDED_CASE = "appeal_revision_recommended_case";
	public static final String APPEAL_REVISION_RAISE_QUERY_CASE = "appeal_revision_raise_query_case";
	public static final String RU_FETCH_DATA = "RU";
	public static final String RU_REVIEW_CATEGORY_CASE = "review_category_case";
	public static final String RU_VIEW_LIST_OF_CASE = "view_list_of_case";
	public static final String RU_APPROVE_REJECT_CASES_ID = "approve_reject_caseid";
	public static final String RM = "rm";
	public static final String RM_Review_CASES_LIST = "review_cases_list";
	public static final String RM_Details_CASES_LIST = "review_details_cases_list";
	// Approver Officer
	public static final String AP_URL = "ap";
	public static final String AP = "AP";
	public static final String AP_URL_APPROVE_REJECT_CASES = "approved_rejected_cases";
	public static final String AP_URL_APPROVE_REJECT_CASES_AJAX = "approved_rejected_cases_ajax";
	public static final String AP_DEEM_APPROVED_CASES = "deem_approved_cases";
	public static final String AP_URL_APPROVED_CASES_WITH_REMARKS = "approved_cases_with_remarks";
	public static final String AP_URL_REJECTED_CASES_WITH_REMARKS = "rejected_cases_with_remarks";
	public static final String AP_URL_GET_AP_REVERT_REMARKS = "get_ap_revert_remarks";
	public static final String AP_URL_APPROVED_CASES = "approved_cases";
	public static final String AP_URL_REJECTED_CASES = "rejected_cases";
	public static final String AP_URL_APPEAL_REVISION_CASES = "appealed_revision_cases";
	public static final String AP_URL_REJECT_APPEAL_REVISION_CASES = "reject_appeal_revision_case";
	public static final String AP_URL_APPROVE_APPEAL_REVISION_CASES = "approve_appeal_revision_case";
	public static final String AP_URL_APPEALED_CASES = "ap_referred_for_appeal";
	public static final String AP_URL_REVISION_CASES = "ap_referred_for_revision";
	public static final String AP_REVIEW_CATEGORY_CASE = "review_category_case";
	public static final String AP_VIEW_LIST_OF_CASE = "view_list_of_case";
	// Generic User
	public static final String GENERIC_USER = "gu";
	public static final String GENERIC_DEFAULT_USER_DETAILS = "default_user_details";
	public static final String GENERIC_USER_DETAILS = "user_details";
	public static final String GENERIC_USER_CHANGE_PASSWORD = "change_password";
	public static final String GENERIC_USER_RESET_PASSWORD = "reset_password";
	public static final String CONVERT_UNREAD_TO_READ_NOTIFICATIONS = "convert_unread_to_read_notifications";
	// View Module
	public static final String VW = "vw";
	public static final String VW_VIEW_WISE_REPORT = "viewWiseReport";
	public static final String VW_REVIEW_SUMMARY_LIST = "review_summary_list";
	public static final String VW_VIEW_LIST_OF_CASE = "view_list_of_case";
	public static final String VW_MIS = "mis";
	public static final String VW_PERIOD_WISE_REPORT = "view_period_wise_report";
	public static final String VW_MANUALLY_UPLOADED_CASE_REPORT = "view_manually_uploaded_case_report";
	// Audit Officer
	public static final String AUDIT = "audit";
	// L1 (Headquater) Officer
	public static final String L1 = "l1";
	public static final String L1_UPLOAD_AUDIT_CASES = "upload_audit_cases";
	// L2 (Allocation) Officer
	public static final String L2 = "l2";
	public static final String L2_CASE_ASSIGNMENT = "case_assignment";
	public static final String L2_SUBMITTED_AUDIT_PLAN_CASES = "submitted_audit_plan_cases";
	public static final String L2_UPDATE_AUDIT_PLAN_CASES = "update_audit_plan_cases";
	public static final String L2_SUBMITTED_DAR_CASES = "submitted_dar_cases";
	public static final String L2_UPDATE_DAR_CASE = "update_dar_case";
	public static final String L2_SHOW_DAR_CASE_DETAILS = "show_dar_case_details";
	public static final String L2_MCM_RECOMMENDATION_ON_DAR = "mcm_recommendation_on_dars";
	public static final String L2_CASES_UDER_MCM_CONSIDERATION = "cases_under_mcm_consideration";
	public static final String L2_RECOMMENDED_FOR_OTHER_MODULE = "recommended_for_other_module";
	public static final String L2_RECOMMENDED_TO_OTHER_MODULE = "recommended_to_other_module";
	// L3 (Team Lead) Officer
	public static final String L3 = "l3";
	public static final String L3_UPDATE_AUDIT_CASE = "update_audit_case";
	public static final String L3_UPDATE_AUDIT_CASE_DETAILS = "update_audit_case_details";
	public static final String L3_SHOW_AUDIT_CASE_DETAILS = "show_audit_case_details";
	public static final String L3_RECOMMENDED_TO_OTHER_MODULE = "recommended_to_other_module";
	// MCM Officer
	public static final String MCM = "mcm";
	public static final String MCM_SUBMITTED_DAR_CASES = "submitted_dar_cases";
	public static final String MCM_UPDATE_DAR_CASE = "update_dar_case";
	public static final String MCM_PENDING_WITH_L2 = "pending_with_l2";
	public static final String MCM_APPROVED_DARS = "approved_dars";
	public static final String MCM_RECOMMENDED_TO_OTHER_MODULE = "recommended_to_other_module";
	// Scrutiny Upload User
	public static final String SCRUTINY = "scrutiny";
	public static final String SCRUTINY_HQ = "scrutiny_hq";
	public static final String SCRUTINY_HQ_UPLOAD_CASES = "upload_cases";
	public static final String SCRUTINY_HQ_REQUEST_FOR_TRANSFER = "request_for_transfer";
	public static final String SCRUTINY_HQ_RANDOM_VERIFICATION_CASES = "random_verification_cases";
	public static final String SCRUTINY_HQ_RANDOM_RECOMMEND_FOR_SCRUTINY = "random_recommend_for_scrutiny";
	public static final String SCRUTINY_HQ_UPDATE_EXCEL = "updateExcel";
	public static final String SCRUTINY_HQ_DOWNLOAD_UPDATED_EXCEL = "downloadUpdatedExcel";
	public static final String SCRUTINY_HQ_REVIEW_CASES_LIST = "review_cases_list";
	public static final String SCRUTINY_HQ_REVIEW_CASES_STATUS_WISE = "review_cases_status_wise";
	// Scrutiny Field Officer User
	public static final String SCRUTINY_FO = "scrutiny_fo";
	public static final String SCRUTINY_FO_ACKNOWLEDGE_TRANSFER_CASES = "ack_transfer_cases";
	public static final String SCRUTINY_FO_INITIATE_SCRUTINY = "initiate_scrutiny";
	public static final String SCRUTINY_FO_ACKNOWLEDGED_CASE = "acknowledged_case";
	public static final String SCRUTINY_FO_SCRUTINY_PROCEEDING_INITIATED_DROPPED = "scrutiny_initiated_dropped";
	public static final String SCRUTINY_FO_SCRUTINY_INITIATED = "scrutiny_initiated";
	public static final String SCRUTINY_FO_LOAD_SIDE_PANEL_VIEW = "load_side_panel_view";
	public static final String SCRUTINY_FO_SUBMIT_ASMT_TEN = "submit_asmt_ten";
	public static final String SCRUTINY_FO_UPDATE_STATUS = "update_status";
	public static final String SCRUTINY_FO_UPDATE_SCRUTINY_STATUS = "update_scrutiny_status";
	public static final String SCRUTINY_FO_SCRUTINY_STATUS_UPDATED = "scrutiny_status_updated";
	public static final String SCRUTINY_FO_LOAD_UPDATE_SCRUTINY_SIDE_PANEL_VIEW = "update_scrutiny_side_panel_view";
	public static final String SCRUTINY_FO_SUBMIT_CLOSURE_REPORT = "submit_closure_report";
	public static final String SCRUTINY_FO_RECOMMEND_ASSESMENT = "recommend_assesment";
	public static final String SCRUTINY_FO_RECOMMEND_AUDIT = "recommend_audit";
	public static final String SCRUTINY_FO_REQUEST_FOR_TRANSFER = "request_for_transfer";
	public static final String SCRUTINY_FO_REVIEW_CASES_LIST = "review_cases_list";
	public static final String SCRUTINY_FO_REVIEW_CASES_STATUS_WISE = "review_cases_status_wise";
	public static final String SCRUTINY_FO_SELF_DETECTED_CASES = "self_detected_cases";
	
	// Scrutiny FO User
	public static final String SCRUTINY_RU = "scrutiny_ru";
	public static final String SCRUTINY_RU_MANDATORY_CASES = "mandatory_cases";
	public static final String SCRUTINY_RU_RECOMMEND_FOR_SCRUTINY = "recommend_for_scrutiny";
	public static final String SCRUTINY_RU_RANDOM_CASES = "random_cases";
	public static final String SCRUTINY_RU_RANDOM_RECOMMEND_FOR_SCRUTINY = "random_recommend_for_scrutiny";
	public static final String SCRUTINY_RU_RECOMMEND_FROM_HQ_USER = "recommended_from_hq_user";
	public static final String SCRUTINY_RU_RECOMMENDED_FROM_HQ_FOR_SCRUTINY = "recommended_from_hq_for_scrutiny";
	public static final String SCRUTINY_RU_REVIEW_CASES_LIST = "review_cases_list";
	public static final String SCRUTINY_RU_REVIEW_CASES_STATUS_WISE = "review_cases_status_wise";
	// Scrutiny FO User End
	public static final String ENFORCEMENT = "enforcement";
	public static final String ENFORCEMENT_HQ = "enforcement_hq";
	public static final String ENFORCEMENT_HQ_UPLOAD_ENFORCEMENT_CASES = "upload_enforcement_cases";
	public static final String EN_HQ = "hq";
	public static final String ENFORCEMENT_REVIEW_CASES_LIST = "review_cases_list"; 
	public static final String ENFORCEMENT_HQ_REVIEW_CASES_STATUS_WISE = "review_cases_status_wise";
	// Enforcement field office
	public static final String ENFORCEMENT_FO = "enforcement_fo";
	public static final String ENFORCEMENT_ACKNOWLEDGEMENT_CASES = "acknowledgement";
	public static final String UPDATE_ENFORCEMENT_CASES = "update_enforcement_cases";
	public static final String INVESTIGATION_CASES = "investigation_cases";
	public static final String ENFORCEMENT_FO_UPDATE_CASE = "update_enforcement_case";
	public static final String ENFORCEMENT_FO_RECOMMENDED_TO_OTHER_MODULE = "recommended_to_other_module";
	// Enforcement Supervise Officer
	public static final String SVO = "svo";
	public static final String ENFORCEMENT_SVO = "enforcement_svo";
	public static final String PERMISSION_FOR_INVESTIGATION = "permission_for_investigation";
	// Assessment FO assistent
	public static final String FOA = "foa";
	public static final String FOA_UPDATE_SUMMARY_LIST = "update_summary_list";
	public static final String FOA_GET_SCRUTINY_DATA_LIST = "get_scrutiny_data_list";
	public static final String ANNEXURE7_REPORT = "annexure7";
}
