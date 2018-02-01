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
@Table(name = "FIBI_COMM_MEMBERSHIPS")
public class CommitteeMemberships implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_MEMBERSHIP_ID")
	private Integer commMembershipId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBERSHIPS"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@Column(name = "MEMBERSHIP_ID")
	private String membershipId;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "NON_EMPLOYEE_FLAG")
	private String nonEmployeeFlag;

	@Column(name = "PAID_MEMBER_FLAG")
	private String paidMemberFlag;

	@Column(name = "TERM_START_DATE")
	private Timestamp termStartDate;

	@Column(name = "TERM_END_DATE")
	private Timestamp termEndDate;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBERSHIPS_2"), name = "MEMBERSHIP_TYPE_CODE", referencedColumnName = "MEMBERSHIP_TYPE_CODE")
	private CommitteeMembershipType committeeMembershipType;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	private Integer versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	private String objectId;

	@Column(name = "CONTACT_NOTES")
	private char[] contactNotes;

	@Column(name = "TRAINING_NOTES")
	private char[] trainingNotes;

	public Integer getCommMembershipId() {
		return commMembershipId;
	}

	public void setCommMembershipId(Integer commMembershipId) {
		this.commMembershipId = commMembershipId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(String nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public String getPaidMemberFlag() {
		return paidMemberFlag;
	}

	public void setPaidMemberFlag(String paidMemberFlag) {
		this.paidMemberFlag = paidMemberFlag;
	}

	public Timestamp getTermStartDate() {
		return termStartDate;
	}

	public void setTermStartDate(Timestamp termStartDate) {
		this.termStartDate = termStartDate;
	}

	public Timestamp getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(Timestamp termEndDate) {
		this.termEndDate = termEndDate;
	}

	public CommitteeMembershipType getCommitteeMembershipType() {
		return committeeMembershipType;
	}

	public void setCommitteeMembershipType(CommitteeMembershipType committeeMembershipType) {
		this.committeeMembershipType = committeeMembershipType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public char[] getContactNotes() {
		return contactNotes;
	}

	public void setContactNotes(char[] contactNotes) {
		this.contactNotes = contactNotes;
	}

	public char[] getTrainingNotes() {
		return trainingNotes;
	}

	public void setTrainingNotes(char[] trainingNotes) {
		this.trainingNotes = trainingNotes;
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
