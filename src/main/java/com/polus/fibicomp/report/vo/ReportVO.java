package com.polus.fibicomp.report.vo;

import java.util.List;
import java.util.Map;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.proposal.pojo.Proposal;

public class ReportVO {

	private Integer grantCallId;

	private Integer proposalCount;

	private String reportName;

	private List<GrantCall> grantIds;

	private List<Proposal> proposals;

	private Map<String, Long> applicationsByGrantCallType;

	private Map<String, Long> protocolsByType;

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
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

	public List<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(List<Proposal> proposals) {
		this.proposals = proposals;
	}

	public Integer getProposalCount() {
		return proposalCount;
	}

	public void setProposalCount(Integer proposalCount) {
		this.proposalCount = proposalCount;
	}

	public Map<String, Long> getApplicationsByGrantCallType() {
		return applicationsByGrantCallType;
	}

	public void setApplicationsByGrantCallType(Map<String, Long> applicationsByGrantCallType) {
		this.applicationsByGrantCallType = applicationsByGrantCallType;
	}

	public Map<String, Long> getProtocolsByType() {
		return protocolsByType;
	}

	public void setProtocolsByType(Map<String, Long> protocolsByType) {
		this.protocolsByType = protocolsByType;
	}

}
