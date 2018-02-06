package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "FIBI_COMM_MEMBER_EXPERTISE")
public class CommitteeMemberExpertise implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_MEMBER_EXPERTISE_ID")
	private Integer commMemberExpertiseId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBER_EXPERTISE"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID")
	private CommitteeMemberships committeeMemberships;

	@Column(name = "RESEARCH_AREA_CODE")
	private String researchAreaCode;

	@Column(name = "RESEARCH_AREA_DESCRIPTION")
	private String researchAreaDescription;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	private Integer versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	private String objectId;

	public Integer getCommMemberExpertiseId() {
		return commMemberExpertiseId;
	}

	public void setCommMemberExpertiseId(Integer commMemberExpertiseId) {
		this.commMemberExpertiseId = commMemberExpertiseId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public String getResearchAreaCode() {
		return researchAreaCode;
	}

	public void setResearchAreaCode(String researchAreaCode) {
		this.researchAreaCode = researchAreaCode;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getResearchAreaDescription() {
		return researchAreaDescription;
	}

	public void setResearchAreaDescription(String researchAreaDescription) {
		this.researchAreaDescription = researchAreaDescription;
	}
}
