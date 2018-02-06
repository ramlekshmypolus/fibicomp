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

	public String addSchedule(CommitteeVo committeeVo) throws ParseException;

	public String saveAreaOfResearch(CommitteeVo committeeVo);

	public void deleteAreaOfResearch(Integer researchAreaId);

	public void deleteSchedule(Integer scheduleId);

	/*public String loadAllResearchAreas();

	public String loadAllUnitsAndReviewTypes();

	public String getAllEmployees();

	public String getAllNonEmployees();*/

	public String addCommitteeMembership(CommitteeVo committeeVo);

	public String saveCommitteeMembers(CommitteeVo committeeVo);

}
