package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.Designation;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

	Optional<UserDetails> findByloginName(String string);

	List<UserDetails> findByfirstName(String string);

	Optional<UserDetails> findBymobileNumber(String mobileNumber);

	Optional<UserDetails> findByemailId(String emailId);

//	Optional<UserDetails> findByfirstName(String inputText);

	List<UserDetails> findBydesignation(Designation designation);

	@Query(value = "select * from analytics.user_details ud where ud.login_name = ?1 and ud.date_of_birth = ?2", nativeQuery = true)
	List<UserDetails> findByLoginNameAndDateOfBirth(String username, Date userdob);

	@Query(value = "select * from analytics.user_details ud where ud.email_id = ?1 and ud.date_of_birth = ?2", nativeQuery = true)
	List<UserDetails> findByEmailIdAndDateOfBirth(String emailId, Date userdob);

	@Query(value = "select * from analytics.user_details ud where ud.mobile_number LIKE %?1%", nativeQuery = true)
	List<UserDetails> findByMobileNumberLike(String mobileNumber);

	@Query(value = "select * from analytics.user_details ud where upper(ud.first_name) like %?1%", nativeQuery = true)
	List<UserDetails> findByfirstNameLike(String firstName);

	@Query(value = "select ud.* from analytics.user_details ud, analytics.mst_dd_admin_designation d where ud.designation_id  = d.designation_id and (upper(d.designation_name) like %?1% or upper(d.designation_acronym) like %?1%)", nativeQuery = true)
	List<UserDetails> findBydesignationLike(String designation);

	@Query(value = "select * from analytics.user_details ud where upper(ud.email_id) like %?1%", nativeQuery = true)
	List<UserDetails> findByemailIdLike(String email);

	@Query(value = "select * from analytics.user_details ud where upper(ud.login_name) like %?1%", nativeQuery = true)
	List<UserDetails> findByloginNameLike(String loginId);

	@Query(value = "select * from analytics.user_details ud where user_status ='active';", nativeQuery = true)
	List<UserDetails> findAllActiveUserLoginName();

	@Query(value = "select * from analytics.user_details ud where user_status ='active' or user_status ='new';", nativeQuery = true)
	List<UserDetails> findAllActiveAndNewUserLoginName();

	@Query(value = "select * from analytics.user_details ud where LOWER(login_name) = LOWER(?1)", nativeQuery = true)
	Optional<UserDetails> findByloginNameIgnoreCase(String LoginName);

	@Query(value = "select * from analytics.user_details ud where LOWER(email_id) = LOWER(?1)", nativeQuery = true)
	Optional<UserDetails> findByemailIdIgnoreCase(String emailId);

	@Query(value = "select concat(first_name,' ',middle_name,' ',last_name) from analytics.user_details ud where user_id = ?1", nativeQuery = true)
	Optional<String> getFullNameById(Integer assignedFromUserId);
}
