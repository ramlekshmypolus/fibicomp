package com.polus.fibicomp.service;

import org.springframework.stereotype.Service;
import com.polus.fibicomp.pojo.PersonDTO;

@Service
public interface DashboardService {

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception;

	public String getDashBoardData(PersonDTO personDTO, String requestType, Integer pageNumber, String sortBy,
			String reverse) throws Exception;

	public String getSearchData(PersonDTO personDTO, String tabIndex, String inputData) throws Exception;

}
