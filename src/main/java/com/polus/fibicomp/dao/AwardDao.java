package com.polus.fibicomp.dao;

import com.polus.fibicomp.vo.AwardDetailsVO;

public interface AwardDao {

	/**
	 * This method is to retrieve details regarding a corresponding award
	 * 
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public AwardDetailsVO fetchAwardSummaryData(String awardId);

}
