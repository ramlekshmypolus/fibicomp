package com.polus.fibicomp.committee.pojo;

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

@Entity
@Table(name = "FIBI_COMM_SCHEDULE")
public class CommitteeSchedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "scheduleIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "scheduleIdGererator")
	@Column(name = "SCHEDULE_ID", updatable = false, nullable = false)
	private Integer scheduleId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE_2"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@Column(name = "SCHEDULED_DATE")
	private Date scheduledDate;

	@Column(name = "PLACE")
	private String place;

	@Column(name = "TIME")
	private Timestamp time;

	@Column(name = "PROTOCOL_SUB_DEADLINE")
	private Date protocolSubDeadline;

	@Column(name = "SCHEDULE_STATUS_CODE")
	private Integer scheduleStatusCode;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCHEDULE"), name = "SCHEDULE_STATUS_CODE", referencedColumnName = "SCHEDULE_STATUS_CODE", insertable = false, updatable = false)
	private ScheduleStatus scheduleStatus;

	@Column(name = "MEETING_DATE")
	private Date meetingDate;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "AGENDA_PROD_REV_DATE")
	private Date agendaProdRevDate;

	@Column(name = "MAX_PROTOCOLS")
	private Integer maxProtocols;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer versionNumber;

	@Column(name = "OBJ_ID")
	private String objectId;

	@Column(name = "AVAILABLE_TO_REVIEWERS")
	private String availableToReviewers;

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public ScheduleStatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Integer getMaxProtocols() {
		return maxProtocols;
	}

	public void setMaxProtocols(Integer maxProtocols) {
		this.maxProtocols = maxProtocols;
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

	public String getAvailableToReviewers() {
		return availableToReviewers;
	}

	public void setAvailableToReviewers(String availableToReviewers) {
		this.availableToReviewers = availableToReviewers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getProtocolSubDeadline() {
		return protocolSubDeadline;
	}

	public void setProtocolSubDeadline(Date protocolSubDeadline) {
		this.protocolSubDeadline = protocolSubDeadline;
	}

	public Integer getScheduleStatusCode() {
		return scheduleStatusCode;
	}

	public void setScheduleStatusCode(Integer scheduleStatusCode) {
		this.scheduleStatusCode = scheduleStatusCode;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getAgendaProdRevDate() {
		return agendaProdRevDate;
	}

	public void setAgendaProdRevDate(Date agendaProdRevDate) {
		this.agendaProdRevDate = agendaProdRevDate;
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

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
