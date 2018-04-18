package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.polus.fibicomp.pojo.Protocol;

@Entity
@Table(name = "FIBI_SMU_PROPOSAL_IRB_PROTOCOL")
public class ProposalIrbProtocol implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalIrbProtocolIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalIrbProtocolIdGenerator")
	@Column(name = "IRB_PROTOCOL_ID")
	private Integer irbProtocolId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_SMU_PROP_PROTOCOL"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID")
	private Proposal proposal;

	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_SMU_PROP_PROTOCOL"), name = "PROTOCOL_ID", referencedColumnName = "PROTOCOL_ID", insertable = false, updatable = false)
	private Protocol protocol;

	@Column(name = "APPROVAL_STATUS")
	private String aprovalStatus;

	@Column(name = "APPROVAL_DATE")
	private Date aprovalDate;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getIrbProtocolId() {
		return irbProtocolId;
	}

	public void setIrbProtocolId(Integer irbProtocolId) {
		this.irbProtocolId = irbProtocolId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public String getAprovalStatus() {
		return aprovalStatus;
	}

	public void setAprovalStatus(String aprovalStatus) {
		this.aprovalStatus = aprovalStatus;
	}

	public Date getAprovalDate() {
		return aprovalDate;
	}

	public void setAprovalDate(Date aprovalDate) {
		this.aprovalDate = aprovalDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
