package com.polus.fibicomp.report.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.report.vo.ReportVO;

@Service
public interface ReportService {

	public String fetchOpenGrantIds(ReportVO reportVO);

	public String applicationReport(ReportVO reportVO);

}
