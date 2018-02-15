package com.polus.fibicomp.committee.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.vo.CommitteeVo;

@Service
public interface CommitteeMemberService {

	public String addCommitteeMembership(CommitteeVo committeeVo);

	public String saveCommitteeMembers(CommitteeVo committeeVo);

	public String deleteCommitteeMembers(CommitteeVo committeeVo);

	public String saveCommitteeMembersRole(CommitteeVo committeeVo);

	public String deleteMemberRoles(CommitteeVo committeeVo);

	public String updateMemberRoles(CommitteeVo committeeVo);

	public String deleteExpertise(CommitteeVo committeeVo);

	public String saveCommitteeMembersExpertise(CommitteeVo committeeVo);

}
