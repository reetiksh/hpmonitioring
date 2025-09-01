package com.hp.gstreviewfeedbackapp.service;

import java.util.List;
import com.hp.gstreviewfeedbackapp.model.CategoryTotal;

public interface FOReviewSummeryList {

	public List<CategoryTotal> getCategoryDetails(List<String> locations);

	public List<CategoryTotal> getStateCategoryDetails();
}
