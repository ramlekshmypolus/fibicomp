package com.polus.fibicomp.committee.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMembershipType;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.MembershipRole;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.schedule.ScheduleData;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

public class CommitteeVo {

	private Integer committeeTypeCode;

	private Committee committee;

	private List<ProtocolReviewType> reviewTypes;

	private List<Unit> homeUnits;

	private List<ResearchArea> researchAreas;

	private String updateType;

	private String currentUser;

	private String committeeId;
	
	private ScheduleData scheduleData;

	private List<PersonDetailsView> employees;

	private List<Rolodex> nonEmployees;

	private List<CommitteeMembershipType> committeeMembershipTypes;

	private List<MembershipRole> membershipRoles;

	private String personId;

	private boolean nonEmployeeFlag;

	private Integer rolodexId;

	private CommitteeSchedule committeeSchedule;

	private Boolean status;

	private String message;

	private Integer scheduleId;

	public List<ProtocolReviewType> getReviewTypes() {
		return reviewTypes;
	}

	public void setReviewTypes(List<ProtocolReviewType> reviewTypes) {
		this.reviewTypes = reviewTypes;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public Integer getCommitteeTypeCode() {
		return committeeTypeCode;
	}

	public void setCommitteeTypeCode(Integer committeeTypeCode) {
		this.committeeTypeCode = committeeTypeCode;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(String committeeId) {
		this.committeeId = committeeId;
	}

	public ScheduleData getScheduleData() {
		return scheduleData;
	}

	public void setScheduleData(ScheduleData scheduleData) {
		this.scheduleData = scheduleData;
	}

	public List<PersonDetailsView> getEmployees() {
		return employees;
	}

	public void setEmployees(List<PersonDetailsView> employees) {
		this.employees = employees;
	}

	public List<Rolodex> getNonEmployees() {
		return nonEmployees;
	}

	public void setNonEmployees(List<Rolodex> nonEmployees) {
		this.nonEmployees = nonEmployees;
	}

	public List<CommitteeMembershipType> getCommitteeMembershipTypes() {
		return committeeMembershipTypes;
	}

	public void setCommitteeMembershipTypes(List<CommitteeMembershipType> committeeMembershipTypes) {
		this.committeeMembershipTypes = committeeMembershipTypes;
	}

	public List<MembershipRole> getMembershipRoles() {
		return membershipRoles;
	}

	public void setMembershipRoles(List<MembershipRole> membershipRoles) {
		this.membershipRoles = membershipRoles;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public boolean isNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(boolean nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public Integer getRolodexId() {
		return rolodexId;
	}

	public void setRolodexId(Integer rolodexId) {
		this.rolodexId = rolodexId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

}
