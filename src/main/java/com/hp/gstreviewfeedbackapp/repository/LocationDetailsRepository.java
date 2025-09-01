package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CircleDetails;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.ZoneDetails;

@Repository
public interface LocationDetailsRepository extends JpaRepository<LocationDetails, String> {

	Optional<LocationDetails> findByLocationName(String cellValue);

	List<LocationDetails> findAllByOrderByLocationNameAsc();

	Optional<LocationDetails> findByLocationId(String id);

	@Query(value = "select circle_id as circleId from analytics.mst_location_mapping mlm where zone_id = ?1", nativeQuery = true)
	List<String> getAllCircleByZoneId(String zoneId);

	@Query(value = "select circle_id as circleId from analytics.mst_location_mapping mlm where state_id = ?1", nativeQuery = true)
	List<String> getAllCircleByStateId(String stateId);

	@Query(value = "select distinct zone_id as zoneId from analytics.mst_location_mapping mlm where state_id = ?1", nativeQuery = true)
	List<String> getAllZoneByStateId(String stateId);

	@Query(value = "select location_id from analytics.mst_dd_location_details mdld where location_type = ?1", nativeQuery = true)
	List<String> findAllLocationIdByLocationType(String locationType);

	@Query(value = "select distinct zone_id, state_id from analytics.mst_location_mapping mlm where circle_id = ?1", nativeQuery = true)
	List<String> getHigherMappedLocationForCircle(String circleId);

	@Query(value = "select distinct state_id from analytics.mst_location_mapping mlm where zone_id = ?1", nativeQuery = true)
	List<String> getHigherMappedLocationForZone(String zoneId);

	@Query(value = "select distinct state_id from analytics.mst_location_mapping mlm where enforcement_zone_id = ?1", nativeQuery = true)
	List<String> getHigherMappedLocationForEnforcementZone(String enforcementZoneId);

	@Query(value = "select * from analytics.mst_dd_location_details mdld where mdld.location_id = '?1'", nativeQuery = true)
	LocationDetails getLocatinDetailsById(String workingLocationId);

	List<LocationDetails> findAllByOrderByLocationIdAsc();

	@Query(value = "select zone_name from analytics.mst_location_mapping mlm where circle_id = ?1", nativeQuery = true)
	String findZoneNameByCircleId(String circleId);
    
}
