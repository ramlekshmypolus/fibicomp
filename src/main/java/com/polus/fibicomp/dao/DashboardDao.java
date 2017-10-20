package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.vo.CommonVO;

/**
 * @author sasi
 *
 */
@Service
public interface DashboardDao {

	/**
	 * This method is used to get dashboard research summary data including
	 * piechart and table.
	 * 
	 * @param personId
	 *            - ID of the logged in person.
	 * @return Set of values used to figure out piechart and research summary
	 *         table.
	 * @throws Exception
	 */
	public String getDashBoardResearchSummary(String personId) throws Exception;

	/**
	 * This method is used to get list of awards.
	 * @param vo- Object of CommonVO class.
	 * @return A list of active awards.
	 */
	public DashBoardProfile getDashBoardDataForAward(CommonVO vo);

	/**
	 * This method is used to get list of proposal.
	 * @param vo - Object of CommonVO class.
	 * @return A list of proposal.
	 */
	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo);

	/**
	 * This method is used to get list of IRB protocols.
	 * @param vo - Object of CommonVO class.
	 * @return A list of IRB protocols.
	 */
	public DashBoardProfile getProtocolDashboardData(CommonVO vo);

	/**
	 * This method is used to get list of IACUC protocols.
	 * 
	 * @param vo
	 *            - Object of CommonVO class.
	 * @return A list of IACUC protocols.
	 */
	public DashBoardProfile getDashBoardDataForIacuc(CommonVO vo);

	/**
	 * This method is used to get list of COI Disclosure.
	 * @param vo - Object of CommonVO class.
	 * @return A list of disclosure.
	 */
	public DashBoardProfile getDashBoardDataForDisclosures(CommonVO vo);

	/**
	 * This method is used to retrieve list of pending action.
	 * @param personId - ID of the user.
	 * @return A list of actions.
	 */
	public List<ActionItem> getUserNotification(String personId);
}
