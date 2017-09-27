package com.polus.fibicomp.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.DashboardDao;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.PersonDTO;

@Transactional
@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {

	protected static Logger logger = Logger.getLogger(DashboardServiceImpl.class.getName());

	@Autowired
	private DashboardDao dashboardDao;

	@Override
	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception {
		return dashboardDao.getDashBoardResearchSummary(personDTO);
	}

	@Override
	public String getDashBoardData(PersonDTO personDTO, String requestType, Integer pageNumber, String sortBy,
			String reverse) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			if (requestType.equals("AWARD")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForAward(personDTO, requestType, pageNumber, sortBy,
						reverse);
			}
			if (requestType.equals("PROPOSAL")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForProposal(personDTO, requestType, pageNumber, sortBy,
						reverse);
			}
			if (requestType.equals("IRB")) {
				dashBoardProfile = dashboardDao.getProtocolDashboardData(personDTO, requestType, pageNumber, sortBy,
						reverse);
			}
			if (requestType.equals("IACUC")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForIacuc(personDTO, requestType, pageNumber, sortBy,
						reverse);
			}
			if (requestType.equals("DISCLOSURE")) {
				dashBoardProfile = dashboardDao.getDashBoardDataForDisclosures(personDTO, requestType, pageNumber,
						sortBy, reverse);
			}
			dashBoardProfile.setPersonDTO(personDTO);
		} catch (Exception e) {
			logger.error("Error in methord getDashBoardData", e);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@Override
	public String getSearchData(PersonDTO personDTO, String tabIndex, String inputData) throws Exception {
		return dashboardDao.getSearchData(personDTO, tabIndex, inputData);
	}

}
