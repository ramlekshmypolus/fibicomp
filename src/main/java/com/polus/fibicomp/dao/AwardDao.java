package com.polus.fibicomp.dao;

public interface AwardDao {

	/**
	 * This method is to retrieve details regarding a corresponding award
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception 
	 */
	public String fetchAwardSummaryData(String awardId) throws Exception;

}
