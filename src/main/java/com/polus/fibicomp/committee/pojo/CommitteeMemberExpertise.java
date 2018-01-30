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

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer verNbr;

	@Column(name = "OBJ_ID")
	private String objId;

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

	public Integer getVerNbr() {
		return verNbr;
	}

	public void setVerNbr(Integer verNbr) {
		this.verNbr = verNbr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
