package com.polus.fibicomp.workflow.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

@Transactional
@Service(value = "workflowService")
public class WorkflowServiceImpl implements WorkflowService {

	protected static Logger logger = Logger.getLogger(WorkflowServiceImpl.class.getName());

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private ProposalDao proposalDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public String submitProposal(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_SUBMITTED);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_SUBMITTED));
		proposal = proposalDao.saveOrUpdateProposal(proposal);		
		Workflow workflow = createWorkflow(proposal);		
		proposalVO.setWorkflow(workflow);
		proposalVO.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}
	
	public Workflow createWorkflow(Proposal proposal) {
		Workflow workflow = new Workflow();
		workflow.setIsWorkflowActive(true);
		workflow.setModuleCode(1);
		workflow.setModuleItemId(proposal.getProposalId());
		workflow.setCreateTimeStamp(proposal.getCreateTimeStamp());
		workflow.setCreateUser(proposal.getCreateUser());
		workflow.setUpdateTimeStamp(proposal.getUpdateTimeStamp());
		workflow.setUpdateUser(proposal.getUpdateUser());

		List<WorkflowMapDetail> workflowMapDetails = workflowDao.fetchWorkflowMapDetail();
		List<WorkflowDetail> workflowDetails = new ArrayList<WorkflowDetail>();
		for(WorkflowMapDetail workflowMapDetail : workflowMapDetails) {
			WorkflowDetail workflowDetail = new WorkflowDetail();
			workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING);
			workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_WAITING));
			workflowDetail.setApprovalStopNumber(workflowMapDetail.getApprovalStopNumber());
			workflowDetail.setApproverNumber(workflowMapDetail.getApproverNumber());
			workflowDetail.setApproverPersonId(workflowMapDetail.getApproverPersonId());
			workflowDetail.setMapId(workflowMapDetail.getMapId());
			workflowDetail.setPrimaryApproverFlag(workflowMapDetail.getPrimaryApproverFlag());
			workflowDetail.setUpdateTimeStamp(workflow.getUpdateTimeStamp());
			workflowDetail.setUpdateUser(workflow.getUpdateUser());
			workflowDetails.add(workflowDetail);
		}
		workflow.setWorkflowDetails(workflowDetails);
		workflow = workflowDao.saveWorkflow(workflow);
		return workflow;		
	}

	@Override
	public String approveProposal(ProposalVO proposalVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String disapproveProposal(ProposalVO proposalVO) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer canTakeRoutingAction(Integer moduleItemId, String personId) {
		return workflowDao.canTakeRoutingAction(moduleItemId, personId);
		
	}
}
