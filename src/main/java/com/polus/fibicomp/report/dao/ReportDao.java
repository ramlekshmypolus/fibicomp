package com.polus.fibicomp.report.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.report.vo.ReportVO;

@Service
public interface ReportDao {

	public ReportVO fetchApplicationByGrantCallId(ReportVO reportVO);

	public List<GrantCall> fetchOpenGrantIds();

	public List<ProtocolType> fetchAllProtocolTypes();

	public Long fetchApplicationsCountByGrantCallType(Integer grantCallTypeCode);

	public Long fetchProtocolsCountByProtocolType(String protocolTypeCode);

}
