package com.polus.fibicomp.committee.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMembershipType;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.MembershipRole;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.pojo.Rolodex;
import com.polus.fibicomp.pojo.Unit;
import com.polus.fibicomp.view.PersonDetailsView;

@Service
public interface CommitteeDao {

	public CommitteeType fetchCommitteeType(Integer committeeTypeCode);

	public List<ProtocolReviewType> fetchAllReviewType();

	public List<Unit> fetchAllHomeUnits();

	public List<ResearchArea> fetchAllResearchAreas();

	public Date getCurrentDate();

	public Timestamp getCurrentTimestamp();

	public String convertObjectToJSON(Object object);

	public Committee saveCommittee(Committee committee);

	public Committee fetchCommitteeById(String committeeId);

	public ScheduleStatus fetchScheduleStatusByStatus(String scheduleStatus);

	public List<Committee> loadAllCommittee();

	public void deleteAreaOfResearch(Integer researchAreaId);

	public void deleteSchedule(CommitteeSchedule committeeSchedule);

	public List<PersonDetailsView> getAllEmployees();

	public List<Rolodex> getAllNonEmployees();

	public List<CommitteeMembershipType> getMembershipTypes();

	public List<MembershipRole> getMembershipRoles();

	public PersonDetailsView getPersonDetailsById(String personId);

	public void deleteMemberRoles(Integer roleId);

	public void deleteExpertise(Integer expertiseId);

	public Rolodex getRolodexById(Integer rolodexId);

	public void updateCommitteSchedule(CommitteeSchedule committeeSchedule);

	public CommitteeSchedule getCommitteeScheduleById(Integer scheduleId);

}
