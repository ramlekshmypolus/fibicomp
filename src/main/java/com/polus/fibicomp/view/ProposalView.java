package com.polus.fibicomp.view;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "PROPOSALS_MV")
public class ProposalView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Integer versionNumber;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "LEAD_UNIT_NUMBER")
	private String leadUnitNumber;

	@Column(name = "LEAD_UNIT")
	private String leadUnit;

	@Column(name = "SPONSOR_CODE")
	private String sponsorCode;

	@Column(name = "SPONSOR")
	private String sponsor;

	@Column(name = "DEADLINE_DATE")
	private Date deadLinedate;

	@Column(name = "STATUS_CODE")
	private Integer statusCode;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeadUnitNumber() {
		return leadUnitNumber;
	}

	public void setLeadUnitNumber(String leadUnitNumber) {
		this.leadUnitNumber = leadUnitNumber;
	}

	public String getLeadUnit() {
		return leadUnit;
	}

	public void setLeadUnit(String leadUnit) {
		this.leadUnit = leadUnit;
	}

	public String getSponsorCode() {
		return sponsorCode;
	}

	public void setSponsorCode(String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public Date getDeadLinedate() {
		return deadLinedate;
	}

	public void setDeadLinedate(Date deadLinedate) {
		this.deadLinedate = deadLinedate;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
