package com.polus.fibicomp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.vo.CommonVO;

/**
 * Dashboard Service class to get dashboard details.
 *
 */
@Service
public interface DashboardService {

	/**
	 * This method is used to get dashboard research summary data for piechart and table. 
	 * @param personId - ID of the logged in person.
	 * @return Set of values used to figure out piechart and research summary table.
	 */
	public String getDashBoardResearchSummary(String personId) throws Exception;

	/**
	 * This method is used to retrieve dashboard data based on tabIndex.
	 * @param vo - Object of CommonVO
	 * @return A list of dashboard data based on tabIndex.
	 * @throws Exception
	 */
	public String getDashBoardData(CommonVO vo) throws Exception;

	/**
	 * This method is used to retrieve award data in piechart based on sponsor type.
	 * @param personId - Logged User ID
	 * @param vo - Object of CommonVO
	 * @return A list of selected pie chart items based on type.
	 * @throws Exception
	 */
	public String getPieChartDataByType(CommonVO vo) throws Exception;

	/**
	 * This method is used to get list of pending actions.
	 * @param personId - Logged User ID
	 * @return a list of pending actions.
	 * @throws Exception
	 */

	public List<ActionItem> getUserNotification(String personId);

	/**
	 * This method is used to get list of selected type in ResearchSummaryTable.
	 * @param personId - Logged User ID
	 * @param researchSummaryIndex - Selected row in Summary table
	 * @return a String of details of selected item
	 * @throws Exception
	 */
	public String getDetailedSummaryData(String personId, String researchSummaryIndex) throws Exception;

}
