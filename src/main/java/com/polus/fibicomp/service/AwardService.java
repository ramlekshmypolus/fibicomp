package com.polus.fibicomp.service;

import org.springframework.stereotype.Service;

@Service
public interface AwardService {

	/**
	 * This method is used to retrieve award data for award view.
	 * 
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about an award
	 * @throws Exception
	 */
	public String getAwardSummaryData(String awardId) throws Exception;

}
