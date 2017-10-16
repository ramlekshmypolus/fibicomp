package com.polus.fibicomp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.vo.CommonVO;

@Service
public interface DashboardService {

	public String getDashBoardResearchSummary(String userName) throws Exception;

	public String getDashBoardData(CommonVO vo) throws Exception;

	public List<ActionItem> getUserNotification(String userName);
}
