package com.polus.fibicomp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.vo.CommonVO;

@Service
public interface DashboardService {

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception;

	public String getDashBoardData(PersonDTO personDTO, String requestType, Integer pageNumber, String sortBy,
			String reverse) throws Exception;

	public String getSearchData(PersonDTO personDTO, String tabIndex, String inputData) throws Exception;

	public String getSearchDataByProperty(PersonDTO personDTO, CommonVO vo) throws Exception;
	
	public List<ActionItem> getUserNotification(String userName) ;
}
