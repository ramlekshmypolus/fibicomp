package com.polus.fibicomp.dao;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.PersonDTO;

@Service
public interface DashboardDao {

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception;

	public DashBoardProfile getDashBoardDataForAward(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForProposal(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getProtocolDashboardData(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForIacuc(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public DashBoardProfile getDashBoardDataForDisclosures(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse);

	public Integer getDashBoardCount(PersonDTO personDTO, String requestType, Integer pageNumber);

	public String getSearchData(PersonDTO personDTO, String tabIndex, String inputData) throws Exception;

}
