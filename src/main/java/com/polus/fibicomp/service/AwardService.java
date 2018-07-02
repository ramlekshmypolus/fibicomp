package com.polus.fibicomp.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.vo.CommonVO;

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
	
	/**
	 * This method is used to retrieve award reports and terms.
	 * 
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about an award
	 * @throws Exception
	 */
	public String getAwardReportsAndTerms(String awardId) throws Exception;

	/**
	 * This method is used to retrieve award hierarchy data.
	 * 
	 * @param awardNumber 
	 * @return set of values to figure out hierarchy details about an award
	 * @throws Exception
	 */
	public String getAwardHierarchyData(String awardNumber, String selectedAwardNumber) throws Exception;

	/**
	 * This method is used to retrieve award commitments data.
	 * 
	 * @param awardId - unique identifier for award
	 * @return set of values to figure out details about award commitments
	 * @throws Exception
	 */
	public String getAwardCommitmentsData(String awardId) throws Exception;

	public String createProjectVariationRequest(CommonVO vo) throws Exception;

	public String viewTemplate(CommonVO vo) throws Exception;

	public String saveOSTDetails(CommonVO vo) throws Exception;
}
