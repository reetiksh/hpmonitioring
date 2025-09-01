package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.scrutiny.model.ScrutinyCaseUploadParameters;

@Repository
public interface ScrutinyCaseUploadParametersRepository extends JpaRepository<ScrutinyCaseUploadParameters, Integer> {
	Optional<ScrutinyCaseUploadParameters> findByName(String dateCellValue);

	@Query(value = "SELECT STRING_AGG(mpmw.param_name, ', ') AS concatenated_names FROM analytics.mst_parameters_module_wise mpmw  WHERE mpmw.id IN (?1)", nativeQuery = true)
	String returnAllConcatParametersValues(List<Integer> parametersList);
}
