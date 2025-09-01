package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.FileMaster;

@Repository
public interface FileMasterRepository extends JpaRepository<FileMaster, Integer> {

	List<FileMaster> findByYearAndCategory(String year, String category);

	List<FileMaster> findByCategoryAndLevel(String category, String level);

	List<FileMaster> findByYearAndTypeAndCategory(String year, String type, String category);

	List<FileMaster> findTop5ByOrderByUploadDateAsc();

//	@Query(value="SELECT fileName,uploadDate FROM FileMaster WHERE uploadDate IS NOT null and category ='Notifications' ORDER BY uploadDate desc LIMIT 5")
//	List<FileMaster> findTop5NotiByOrderByUploadDateAsc();

//	 @Query("SELECT fm FROM FileMaster fm WHERE fm.uploadDate IS NOT NULL AND fm.category = 'Notifications' ORDER BY fm.uploadDate DESC")
//	    List<FileMaster> findTop5NotiByOrderByUploadDateDesc();

	@Query("SELECT fm FROM FileMaster fm WHERE fm.uploadDate IS NOT NULL AND fm.category = 'Notifications' and fm.isDeleted='N' ORDER BY fm.uploadDate DESC")
	List<FileMaster> findTop5NotiByOrderByUploadDateDesc();

	@Query(value = "select distinct category from FileMaster")
	List<String> findDistinctCategoryList();

	@Query(value = "select distinct type from FileMaster where category=:category ")
	List<String> findDistinctTypeList(String category);

	@Query(value = "select distinct year from FileMaster order by year desc")
	List<String> getDistinctYears();

	@Query(value = "select distinct level from FileMaster where category ='Acts And Rules'")
	List<String> getDistinctLevels();

	@Query(value = "select distinct type from FileMaster where category ='Notifications'")
	List<String> getDistinctTypes();

	List<FileMaster> findByCategoryAndYearAndTypeAndFileNameAndIsDeleted(String category, String year, String type,
			String fileName, String isDeleted);

	List<FileMaster> findByCategoryAndYearAndIsDeleted(String category, String financialyearNoti, String isDeleted);

}
