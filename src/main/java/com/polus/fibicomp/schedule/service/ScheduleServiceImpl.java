package com.polus.fibicomp.schedule.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeMemberRoles;
import com.polus.fibicomp.committee.pojo.CommitteeMemberships;
import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleAttendance;
import com.polus.fibicomp.committee.pojo.ProtocolSubmission;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.schedule.dao.ScheduleDao;
import com.polus.fibicomp.schedule.vo.ScheduleVo;
import com.polus.fibicomp.view.ProtocolView;

@Transactional
@Service(value = "scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	protected static Logger logger = Logger.getLogger(ScheduleServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	public String loadScheduleById(Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleId);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommittee(committeeSchedule.getCommittee());
		List<ScheduleStatus> scheduleStatus = committeeDao.fetchAllScheduleStatus();
		scheduleVo.setScheduleStatus(scheduleStatus);
		List<ScheduleActItemType> scheduleActItemTypes = scheduleDao.fetchAllScheduleActItemType();
		scheduleVo.setScheduleActItemTypes(scheduleActItemTypes);
		List<ProtocolSubmission> protocolSubmissions = committeeSchedule.getProtocolSubmissions();
		if (protocolSubmissions != null && !protocolSubmissions.isEmpty()) {
			for (ProtocolSubmission protocolSubmission : protocolSubmissions) {
				logger.info("protocolId : " + protocolSubmission.getProtocolId());
				logger.info("piPersonId : " + protocolSubmission.getPiPersonId());
				logger.info("piPersonName : " + protocolSubmission.getPiPersonName());
				ProtocolView protocolView = scheduleDao.fetchProtocolViewByParams(protocolSubmission.getProtocolId().intValue(), protocolSubmission.getPiPersonId(), protocolSubmission.getPiPersonName());
				if (protocolView != null) {
					protocolSubmission.setDocumentNumber(protocolView.getDocumentNumber());
				}
			}
		}
		if (committeeSchedule.getCommitteeScheduleAttendances().isEmpty() && !committeeSchedule.getCommittee().getCommitteeMemberships().isEmpty()) {
			initAttendance(scheduleVo.getMemberAbsents(), committeeSchedule);
		} else {
			populateAttendanceToForm(scheduleVo, committeeSchedule.getCommittee().getCommitteeMemberships(), committeeSchedule);
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

	/*
	 * Init attendance if this meeting schedule is maintained for the first time.
	 */
	protected void initAttendance(List<CommitteeScheduleAttendance> committeeScheduleAttendances, CommitteeSchedule commSchedule) {
		List<CommitteeMemberships> committeeMemberships = commSchedule.getCommittee().getCommitteeMemberships();
		committeeMemberships.forEach(committeeMembership -> {
			if (isActiveMembership(committeeMembership, commSchedule.getScheduledDate())) {
				CommitteeScheduleAttendance committeeScheduleAttendance = new CommitteeScheduleAttendance();
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					committeeScheduleAttendance.setPersonId(committeeMembership.getRolodexId().toString());
					committeeScheduleAttendance.setNonEmployeeFlag(true);
				} else {
					committeeScheduleAttendance.setPersonId(committeeMembership.getPersonId());
					committeeScheduleAttendance.setNonEmployeeFlag(false);
				}
				committeeScheduleAttendance.setPersonName(committeeMembership.getPersonName());
				if (isAlternate(committeeMembership, commSchedule.getScheduledDate())) {
					committeeScheduleAttendance.setAlternateFlag(true);
				} else {
					committeeScheduleAttendance.setAlternateFlag(false);
				}
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				committeeScheduleAttendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				// memberAbsentBean.setAttendance(committeeScheduleAttendance);
				committeeScheduleAttendances.add(committeeScheduleAttendance);
			}
		});
	}

	protected boolean isActiveMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		return isActiveForScheduledDate(scheduledDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())
				&& hasActiveMembershipRoleForScheduledDate(committeeMembership.getCommitteeMemberRoles(), scheduledDate);
	}

	private boolean isActiveForScheduledDate(Date scheduledDate, Date startDate, Date endDate) {
		return startDate.before(scheduledDate) && endDate.after(scheduledDate);
	}

	private boolean hasActiveMembershipRoleForScheduledDate(List<CommitteeMemberRoles> committeeMembershipRoles,
			Date scheduledDate) {
		for (CommitteeMemberRoles membershipRole : committeeMembershipRoles) {
			if (!membershipRole.getMembershipRoleCode().equals(CommitteeMemberRoles.INACTIVE_ROLE)
					&& isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * get rolename, concatenated with ',' separator if multiple roles exist for
	 * this membership
	 */
	protected String getRoleNameForMembership(CommitteeMemberships committeeMembership, Date scheduledDate) {
		String roleName = "";
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (isActiveForScheduledDate(scheduledDate, membershipRole.getStartDate(), membershipRole.getEndDate())) {
				roleName = roleName + "," + membershipRole.getMembershipRoleDescription();
			}
		}
		if (StringUtils.isNotBlank(roleName)) {
			roleName = roleName.substring(1); // remove ","
		}
		return roleName;
	}

	/*
	 * check if this membership has alternate role based on schedule date.
	 */
	protected boolean isAlternate(CommitteeMemberships committeeMembership, Date scheduledDate) {
		boolean isAlternate = false;
		for (CommitteeMemberRoles membershipRole : committeeMembership.getCommitteeMemberRoles()) {
			if (membershipRole.getMembershipRoleCode().equals(CommitteeMemberRoles.ALTERNATE_ROLE)
					&& !membershipRole.getStartDate().after(scheduledDate)
					&& !membershipRole.getEndDate().before(scheduledDate)) {
				isAlternate = true;
				break;
			}
		}
		return isAlternate;
	}

	/*
	 * populate 3 attendance form beans
	 */
	protected void populateAttendanceToForm(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		populatePresentBean(scheduleVo, committeeMemberships, commSchedule);
		populateMemberAbsentBean(scheduleVo, committeeMemberships, commSchedule);
	}

	/*
	 * populate memberpresentbean &amp; otherpresentbean
	 */
	protected void populatePresentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships,
			CommitteeSchedule commSchedule) {
		scheduleVo.setOtherPresents(new ArrayList<CommitteeScheduleAttendance>());
		// commSchedule.setMemberPresents(new ArrayList<>());
		for (CommitteeScheduleAttendance committeeScheduleAttendance : commSchedule.getCommitteeScheduleAttendances()) {
			getRoleName(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate());
			if (committeeScheduleAttendance.getGuestFlag()) {
				// OtherPresentBeanBase otherPresentBean =
				// getNewOtherPresentBeanInstanceHook();
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
				committeeScheduleAttendance.setGuestMemberActive(isActiveMember(committeeScheduleAttendance, committeeMemberships, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeScheduleAttendance.getRoleName())) {
					committeeScheduleAttendance.setRoleName("Guest");
				}
				scheduleVo.getOtherPresents().add(committeeScheduleAttendance);
				// otherPresentBean.setAttendance(committeeScheduleAttendance);
			}
			/*
			 * else { MemberPresentBean memberPresentBean = new
			 * MemberPresentBean();
			 * memberPresentBean.setAttendance(committeeScheduleAttendance);
			 * meetingHelper.getMemberPresentBeans().add(memberPresentBean); }
			 */
		}
	}

	/*
	 * get a person's role name within this committee memberships based on
	 * schedule date.
	 */
	protected void getRoleName(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		String roleName = "";
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				roleName = getRoleNameForMembership(committeeMembership, scheduleDate);
				break;
			}
		}
		committeeScheduleAttendance.setRoleName(roleName);
	}

	/*
	 * Check if this member is active in this committee. Inactive scenario : -
	 * not defined in membership. - in membership, but non of the memberships
	 * period cover schedule date - an 'Inactive' role period cover schedule
	 * date.
	 */
	protected boolean isActiveMember(CommitteeScheduleAttendance committeeScheduleAttendance, List<CommitteeMemberships> committeeMemberships, Date scheduleDate) {
		boolean isActiveMember = false;
		for (CommitteeMemberships committeeMembership : committeeMemberships) {
			if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
					&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
					|| (!committeeScheduleAttendance.getNonEmployeeFlag()
							&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
				if (isActiveForScheduledDate(scheduleDate, committeeMembership.getTermStartDate(), committeeMembership.getTermEndDate())) {
					isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
				}
			}
		}
		return isActiveMember;
	}

	/*
	 * populate memberabsentbean
	 */
	protected void populateMemberAbsentBean(ScheduleVo scheduleVo, List<CommitteeMemberships> committeeMemberships, CommitteeSchedule commSchedule) {
		scheduleVo.setMemberAbsents(new ArrayList<CommitteeScheduleAttendance>());
		committeeMemberships.forEach(committeeMembership -> {
			if (!isInMemberPresent(commSchedule.getCommitteeScheduleAttendances(), committeeMembership)
					&& !isInOtherPresent(scheduleVo.getOtherPresents(), committeeMembership)) {
				// MemberAbsentBean memberAbsentBean = new MemberAbsentBean();
				CommitteeScheduleAttendance attendance = new CommitteeScheduleAttendance();
				attendance.setRoleName(getRoleNameForMembership(committeeMembership, commSchedule.getScheduledDate()));
				if (StringUtils.isBlank(committeeMembership.getPersonId())) {
					attendance.setPersonId(committeeMembership.getRolodexId().toString());
				} else {
					attendance.setPersonId(committeeMembership.getPersonId());
				}
				if (isActiveMemberAbsent(attendance, committeeMembership, commSchedule.getScheduledDate())) {
					attendance.setPersonName(committeeMembership.getPersonName());
					attendance.setAlternateFlag(false);
					attendance.setNonEmployeeFlag(StringUtils.isBlank(committeeMembership.getPersonId()));
					// memberAbsentBean.setAttendance(attendance);
					scheduleVo.getMemberAbsents().add(attendance);
				}
			}
		});
	}

	/*
	 * check if person is in member present.
	 */
	protected boolean isInMemberPresent(List<CommitteeScheduleAttendance> memberPresents,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance memberPresentBean : memberPresents) {
			if (memberPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!memberPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& memberPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	/*
	 * check if person is in other present
	 */
	protected boolean isInOtherPresent(List<CommitteeScheduleAttendance> otherPresentBeans,
			CommitteeMemberships committeeMembership) {
		boolean isPresent = false;
		for (CommitteeScheduleAttendance otherPresentBean : otherPresentBeans) {
			if (otherPresentBean.getNonEmployeeFlag() && StringUtils.isBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getRolodexId().toString())) {
				isPresent = true;
				break;
			} else if (!otherPresentBean.getNonEmployeeFlag()
					&& StringUtils.isNotBlank(committeeMembership.getPersonId())
					&& otherPresentBean.getPersonId().equals(committeeMembership.getPersonId())) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	protected boolean isActiveMemberAbsent(CommitteeScheduleAttendance committeeScheduleAttendance,
			CommitteeMemberships committeeMembership, Date scheduleDate) {
		boolean isActiveMember = false;
		if ((committeeScheduleAttendance.getNonEmployeeFlag() && committeeMembership.getRolodexId() != null
				&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getRolodexId().toString()))
				|| (!committeeScheduleAttendance.getNonEmployeeFlag()
						&& committeeScheduleAttendance.getPersonId() != null
						&& committeeScheduleAttendance.getPersonId().equals(committeeMembership.getPersonId()))) {
			if (!committeeMembership.getTermStartDate().after(scheduleDate)
					&& !committeeMembership.getTermEndDate().before(scheduleDate)) {
				isActiveMember = isActiveMembership(committeeMembership, scheduleDate);
			}
		}

		return isActiveMember;
	}

	@Override
	public String updateSchedule(ScheduleVo scheduleVo) {
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		CommitteeSchedule committeeSchedule = scheduleVo.getCommitteeSchedule();
		committeeSchedule.setCommittee(committee);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		committee.getCommitteeSchedules().add(committeeSchedule);
		committee = committeeDao.saveCommittee(committee);
		scheduleVo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	@Override
	public String addOtherActions(ScheduleVo scheduleVo) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleVo.getScheduleId());
		CommitteeScheduleActItems committeeScheduleActItems = scheduleVo.getCommitteeScheduleActItems();
		CommitteeScheduleActItems scheduleActItem = new CommitteeScheduleActItems();
		scheduleActItem.setCommitteeSchedule(committeeSchedule);
		scheduleActItem.setScheduleActItemTypecode(committeeScheduleActItems.getScheduleActItemTypecode());
		scheduleActItem.setItemDescription(committeeScheduleActItems.getItemDescription());
		scheduleActItem.setScheduleActItemTypeDescription(committeeScheduleActItems.getScheduleActItemTypeDescription());
		scheduleActItem.setActionItemNumber(getNextActionItemNumber(committeeSchedule));
		scheduleActItem = scheduleDao.addOtherActions(scheduleActItem);
		committeeSchedule.getCommitteeScheduleActItems().add(scheduleActItem);
		Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
		committeeSchedule.setCommittee(committee);
		committeeSchedule = scheduleDao.updateCommitteeSchedule(committeeSchedule);
		scheduleVo.setCommitteeSchedule(committeeSchedule);
		String response = committeeDao.convertObjectToJSON(scheduleVo);
		return response;
	}

	public Integer getNextActionItemNumber(CommitteeSchedule committeeSchedule) {
        Integer nextActionItemNumber = committeeSchedule.getCommitteeScheduleActItems().size();
        for (CommitteeScheduleActItems commScheduleActItem : committeeSchedule.getCommitteeScheduleActItems()) {
            if (commScheduleActItem.getActionItemNumber() > nextActionItemNumber) {
                nextActionItemNumber = commScheduleActItem.getActionItemNumber();
            }
        }
        return nextActionItemNumber + 1;

    }

	@Override
	public String deleteOtherActions(ScheduleVo scheduleVo) {
		try {
			Committee committee = committeeDao.fetchCommitteeById(scheduleVo.getCommitteeId());
			List<CommitteeSchedule> committeeSchedules = committee.getCommitteeSchedules();
			for (CommitteeSchedule committeeSchedule : committeeSchedules) {
				if (committeeSchedule.getScheduleId().equals(scheduleVo.getScheduleId())) {
					List<CommitteeScheduleActItems> list = committeeSchedule.getCommitteeScheduleActItems();
					List<CommitteeScheduleActItems> updatedlist = new ArrayList<CommitteeScheduleActItems>(list);
					Collections.copy(updatedlist, list);
					for (CommitteeScheduleActItems actionItem : list) {
						if (actionItem.getCommScheduleActItemsId().equals(scheduleVo.getCommScheduleActItemsId())) {
							updatedlist.remove(actionItem);
						}
					}
					committeeSchedule.getCommitteeScheduleActItems().clear();
					committeeSchedule.getCommitteeScheduleActItems().addAll(updatedlist);
				}
			}
			committeeDao.saveCommittee(committee);
			scheduleVo.setCommittee(committee);
			scheduleVo.setStatus(true);
			scheduleVo.setMessage("Schedule other action item deleted successfully");
		} catch (Exception e) {
			scheduleVo.setStatus(false);
			scheduleVo.setMessage("Problem occurred in deleting Schedule other action item");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(scheduleVo);
	}

}
