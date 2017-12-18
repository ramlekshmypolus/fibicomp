package com.polus.fibicomp.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.AwardDao;
import com.polus.fibicomp.vo.AwardDetailsVO;
import com.polus.fibicomp.vo.AwardHierarchyVO;
import com.polus.fibicomp.vo.AwardTermsAndReportsVO;
import com.polus.fibicomp.vo.CommitmentsVO;

@Transactional
@Service(value = "awardService")
public class AwardServiceImpl implements AwardService {

	protected static Logger logger = Logger.getLogger(AwardServiceImpl.class.getName());

	@Autowired
	private AwardDao awardDao;

	@Override
	public String getAwardSummaryData(String awardId) throws Exception {
		AwardDetailsVO awardDetailsVO = awardDao.fetchAwardSummaryData(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardDetailsVO);
	}

	@Override
	public String getAwardReportsAndTerms(String awardId) throws Exception {
		AwardTermsAndReportsVO awardTermsAndReportsVO = awardDao.fetchAwardReportsAndTerms(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardTermsAndReportsVO);
	}

	@Override
	public String getAwardHierarchyData(String awardNumber, String selectedAwardNumber) throws Exception {
		AwardHierarchyVO awardHierarchyVO = awardDao.fetchAwardHierarchyData(awardNumber, selectedAwardNumber);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(awardHierarchyVO);
	}

	@Override
	public String getAwardCommitmentsData(String awardId) throws Exception {
		CommitmentsVO commitmentsVO = awardDao.fetchAwardCommitmentsData(awardId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(commitmentsVO);
	}

}
