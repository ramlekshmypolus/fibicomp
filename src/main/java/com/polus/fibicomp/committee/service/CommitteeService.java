package com.polus.fibicomp.committee.service;

import org.springframework.stereotype.Service;

@Service
public interface CommitteeService {

	public String createCommittee(Integer committeeTypeCode);

	public String fetchInitialDatas();

}
