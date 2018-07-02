package com.polus.fibicomp.dao;

import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.vo.AwardDetailsVO;
import com.polus.fibicomp.vo.AwardHierarchyVO;
import com.polus.fibicomp.vo.AwardTermsAndReportsVO;
import com.polus.fibicomp.vo.CommitmentsVO;
import com.polus.fibicomp.vo.CommonVO;

public interface AwardDao {

	/**
	 * This method is to retrieve details regarding a corresponding award
	 * 
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public AwardDetailsVO fetchAwardSummaryData(String awardId);

	/**
	 * This method is to retrieve award reports and terms details
	 * 
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award reports and terms view
	 * @throws Exception
	 */
	public AwardTermsAndReportsVO fetchAwardReportsAndTerms(String awardId);

	/**
	 * This method is to retrieve award hierarchy details
	 * 
	 * @param awardNumber - number of award
	 * @param selectedAwardNumber - selected award number from the hierarchy
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public AwardHierarchyVO fetchAwardHierarchyData(String awardNumber, String selectedAwardNumber);

	/**
	 * This method is to retrieve award commitments details
	 * 
	 * @param awardId - unique identifier of an award
	 * @return set of values to display award view
	 * @throws Exception
	 */
	public CommitmentsVO fetchAwardCommitmentsData(String awardId);

	public CommonVO getDropDownDatas(CommonVO vo);

	public List<HashMap<String, Object>> viewTemplate(Integer categoryCode,Integer serviceTypeCode);

	public Integer generateServiceRequestId();

}
