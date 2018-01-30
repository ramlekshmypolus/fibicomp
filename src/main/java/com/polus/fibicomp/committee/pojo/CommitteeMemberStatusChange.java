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
@Table(name = "FIBI_COMM_MEMBER_STATUS_CHANGE")
public class CommitteeMemberStatusChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_MEMBER_STATUS_CHANGE_ID")
	private Integer commMemberStatusChangeId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MBR_STATUS_CHNG_3"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID")
	private CommitteeMemberships committeeMemberships;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MBR_STATUS_CHNG_2"), name = "MEMBERSHIP_STATUS_CODE", referencedColumnName = "MEMBERSHIP_STATUS_CODE")
	private CommitteeMembershipStatus committeeMembershipStatus;

	@Column(name = "START_DATE")
	private Timestamp startDate;

	@Column(name = "END_DATE")
	private Timestamp endDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer verNbr;

	@Column(name = "OBJ_ID")
	private String objId;

	public Integer getCommMemberStatusChangeId() {
		return commMemberStatusChangeId;
	}

	public void setCommMemberStatusChangeId(Integer commMemberStatusChangeId) {
		this.commMemberStatusChangeId = commMemberStatusChangeId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public CommitteeMembershipStatus getCommitteeMembershipStatus() {
		return committeeMembershipStatus;
	}

	public void setCommitteeMembershipStatus(CommitteeMembershipStatus committeeMembershipStatus) {
		this.committeeMembershipStatus = committeeMembershipStatus;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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
}
