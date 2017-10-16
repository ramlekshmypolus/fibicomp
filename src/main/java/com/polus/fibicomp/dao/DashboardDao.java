package com.polus.fibicomp.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.vo.CommonVO;

@Service
public interface DashboardDao {

	public String getDashBoardResearchSummary(String userName) throws Exception;

	public DashBoardProfile getDashBoardDataForAward(CommonVO vo);

	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo);

	public DashBoardProfile getProtocolDashboardData(CommonVO vo);

	public DashBoardProfile getDashBoardDataForIacuc(CommonVO vo);

	public DashBoardProfile getDashBoardDataForDisclosures(CommonVO vo);

	public Integer getDashBoardCount(String tabIndex, Integer pageNumber);

	public List<ActionItem> getUserNotification(String userName);
}
