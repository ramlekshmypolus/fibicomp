package com.polus.fibicomp.report.vo;

import java.util.List;

import com.polus.fibicomp.grantcall.pojo.GrantCall;

public class ReportVO {

	private Integer grantCallId;

	private Integer proposalCount;

	private String reportName;

	private List<GrantCall> grantIds;

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Integer getProposalCount() {
		return proposalCount;
	}

	public void setProposalCount(Integer proposalCount) {
		this.proposalCount = proposalCount;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<GrantCall> getGrantIds() {
		return grantIds;
	}

	public void setGrantIds(List<GrantCall> grantIds) {
		this.grantIds = grantIds;
	}
}
