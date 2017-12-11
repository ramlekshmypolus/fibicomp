package com.polus.fibicomp.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.dao.AwardDao;
import com.polus.fibicomp.vo.AwardDetailsVO;

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

}
