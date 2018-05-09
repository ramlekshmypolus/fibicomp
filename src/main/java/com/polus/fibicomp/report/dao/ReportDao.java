package com.polus.fibicomp.report.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.grantcall.pojo.GrantCall;

@Service
public interface ReportDao {

	public Long fetchApplicationCountByGrantCallId(Integer grantCallId);

	public List<GrantCall> fetchOpenGrantIds();
}
