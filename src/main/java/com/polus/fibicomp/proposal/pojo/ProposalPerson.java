package com.polus.fibicomp.proposal.pojo;

import java.io.Serializable;
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

@Entity
@Table(name = "FIBI_SMU_PROPOSAL_PERSONS")
public class ProposalPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "proposalPersonIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "proposalPersonIdGenerator")
	@Column(name = "PROPOSAL_PERSON_ID")
	private Integer proposalPersonId;

	@Column(name = "PROPOSAL_ID")
	private Integer proposalId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_FIBI_SMU_PROPOSAL_PERSONS"), name = "PROPOSAL_ID", referencedColumnName = "PROPOSAL_ID", insertable = false, updatable = false)
	private Proposal proposal;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "ROLODEX_ID")
	private Integer rolodexId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "CONTACT_ROLE_CODE")
	private String contactRoleCode;

	@Column(name = "KEY_PERSON_PROJECT_ROLE")
	private String keyPersonProjectRole;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getProposalPersonId() {
		return proposalPersonId;
	}

	public void setProposalPersonId(Integer proposalPersonId) {
		this.proposalPersonId = proposalPersonId;
	}

	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getContactRoleCode() {
		return contactRoleCode;
	}

	public void setContactRoleCode(String contactRoleCode) {
		this.contactRoleCode = contactRoleCode;
	}

	public String getKeyPersonProjectRole() {
		return keyPersonProjectRole;
	}

	public void setKeyPersonProjectRole(String keyPersonProjectRole) {
		this.keyPersonProjectRole = keyPersonProjectRole;
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
