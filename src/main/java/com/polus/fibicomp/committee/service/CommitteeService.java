package com.polus.fibicomp.committee.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.vo.CommitteeVo;

@Service
public interface CommitteeService {

	public String createCommittee(Integer committeeTypeCode);

	public String saveCommittee(CommitteeVo vo);

	public String fetchInitialDatas();

}
