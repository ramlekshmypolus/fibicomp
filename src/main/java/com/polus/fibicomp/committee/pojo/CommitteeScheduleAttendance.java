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
@Table(name = "FIBI_COMM_SCHEDULE_ATTENDANCE")
public class CommitteeScheduleAttendance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_SCHEDULE_ATTENDANCE_ID")
	private Integer committeeScheduleAttendanceId;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_ATTENDANCE"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "GUEST_FLAG")
	private String guestFlag;

	@Column(name = "ALTERNATE_FLAG")
	private String alternateFlag;

	@Column(name = "ALTERNATE_FOR")
	private String alternateFor;

	@Column(name = "NON_EMPLOYEE_FLAG")
	private String nonEmployeeFlag;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public Integer getCommitteeScheduleAttendanceId() {
		return committeeScheduleAttendanceId;
	}

	public void setCommitteeScheduleAttendanceId(Integer committeeScheduleAttendanceId) {
		this.committeeScheduleAttendanceId = committeeScheduleAttendanceId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
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

	public String getGuestFlag() {
		return guestFlag;
	}

	public void setGuestFlag(String guestFlag) {
		this.guestFlag = guestFlag;
	}

	public String getAlternateFlag() {
		return alternateFlag;
	}

	public void setAlternateFlag(String alternateFlag) {
		this.alternateFlag = alternateFlag;
	}

	public String getAlternateFor() {
		return alternateFor;
	}

	public void setAlternateFor(String alternateFor) {
		this.alternateFor = alternateFor;
	}

	public String getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(String nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

}
