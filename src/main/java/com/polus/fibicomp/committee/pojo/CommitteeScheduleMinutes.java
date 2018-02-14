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
@Table(name = "FIBI_COMM_SCHEDULE_MINUTES")
public class CommitteeScheduleMinutes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_SCHEDULE_MINUTES_ID")
	private Integer commScheduleMinutesid;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK2_FIBI_COMM_SCHEDULE_MINUTES"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "ENTRY_NUMBER")
	private Integer entryNumber;

	@Column(name = "MINUTE_ENTRY_TYPE_CODE")
	private Integer minuteEntryTypecode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE_MINUTES"), name = "MINUTE_ENTRY_TYPE_CODE", referencedColumnName = "MINUTE_ENTRY_TYPE_CODE", insertable = false, updatable = false)
	private MinuteEntrytype minuteEntrytype;

	@Column(name = "PROTOCOL_NUMBE")
	private String protocolNumber;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PROTOCOL_ID")
	private Integer protocolId;

	@Column(name = "SUBMISSION_ID")
	private Integer submissionId;

	@Column(name = "REVIEWER_ID")
	private Integer reviewerId;

	@Column(name = "SUBMISSION_NUMBER")
	private Integer submissionNumber;

	@Column(name = "PRIVATE_COMMENT_FLAG")
	private String privateCommentFlag;

	@Column(name = "PROTOCOL_CONTINGENCY_CODE")
	private String protocolContingencyCode;

	@Column(name = "MINUTE_ENTRY")
	private long minuteEntry;

	@Column(name = "FINAL_FLAG")
	private String finalFlag;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommScheduleMinutesid() {
		return commScheduleMinutesid;
	}

	public void setCommScheduleMinutesid(Integer commScheduleMinutesid) {
		this.commScheduleMinutesid = commScheduleMinutesid;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(Integer entryNumber) {
		this.entryNumber = entryNumber;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Integer getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}

	public Integer getSubmissionNumber() {
		return submissionNumber;
	}

	public void setSubmissionNumber(Integer submissionNumber) {
		this.submissionNumber = submissionNumber;
	}

	public String getPrivateCommentFlag() {
		return privateCommentFlag;
	}

	public void setPrivateCommentFlag(String privateCommentFlag) {
		this.privateCommentFlag = privateCommentFlag;
	}

	public String getProtocolContingencyCode() {
		return protocolContingencyCode;
	}

	public void setProtocolContingencyCode(String protocolContingencyCode) {
		this.protocolContingencyCode = protocolContingencyCode;
	}

	public long getMinuteEntry() {
		return minuteEntry;
	}

	public void setMinuteEntry(long minuteEntry) {
		this.minuteEntry = minuteEntry;
	}

	public String getFinalFlag() {
		return finalFlag;
	}

	public void setFinalFlag(String finalFlag) {
		this.finalFlag = finalFlag;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
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

	public MinuteEntrytype getMinuteEntrytype() {
		return minuteEntrytype;
	}

	public void setMinuteEntrytype(MinuteEntrytype minuteEntrytype) {
		this.minuteEntrytype = minuteEntrytype;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getMinuteEntryTypecode() {
		return minuteEntryTypecode;
	}

	public void setMinuteEntryTypecode(Integer minuteEntryTypecode) {
		this.minuteEntryTypecode = minuteEntryTypecode;
	}

}
