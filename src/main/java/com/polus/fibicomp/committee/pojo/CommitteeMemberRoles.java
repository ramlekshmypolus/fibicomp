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
@Table(name = "FIBI_COMM_MEMBER_ROLES")
public class CommitteeMemberRoles implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_MEMBER_ROLES_ID")
	private Integer commMemberRolesId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBER_ROLES"), name = "COMM_MEMBERSHIP_ID", referencedColumnName = "COMM_MEMBERSHIP_ID")
	private CommitteeMemberships committeeMemberships;

	@Column(name = "MEMBERSHIP_ROLE_CODE")
	private Integer membershipRoleCode;

	@Column(name = "START_DATE")
	private Timestamp startDate;

	@Column(name = "END_DATE")
	private Timestamp endDate;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	private Integer versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	private String objectId;

	public Integer getCommMemberRolesId() {
		return commMemberRolesId;
	}

	public void setCommMemberRolesId(Integer commMemberRolesId) {
		this.commMemberRolesId = commMemberRolesId;
	}

	public CommitteeMemberships getCommitteeMemberships() {
		return committeeMemberships;
	}

	public void setCommitteeMemberships(CommitteeMemberships committeeMemberships) {
		this.committeeMemberships = committeeMemberships;
	}

	public Integer getMembershipRoleCode() {
		return membershipRoleCode;
	}

	public void setMembershipRoleCode(Integer membershipRoleCode) {
		this.membershipRoleCode = membershipRoleCode;
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

}
