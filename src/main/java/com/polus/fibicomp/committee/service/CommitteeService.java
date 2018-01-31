package com.polus.fibicomp.committee.service;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.vo.CommitteeVo;

@Service
public interface CommitteeService {

	public String createCommittee(Integer committeeTypeCode);

	public String saveCommittee(CommitteeVo vo);

	public String fetchInitialDatas();

	public String loadCommitteeById(String committeeId);
	
	/**
     * This method implementer must add new non conflicting, non-repeating schedule dates to existing CommitteeBase.CommitteeScheduleBase list.
     * @param committeeVo
     */
    public String addSchedule(CommitteeVo committeeVo) throws ParseException;

}
