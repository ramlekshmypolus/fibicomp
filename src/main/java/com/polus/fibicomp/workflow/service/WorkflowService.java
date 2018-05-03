package com.polus.fibicomp.workflow.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.workflow.pojo.Workflow;

@Service
public interface WorkflowService {

	/**
	 * @param proposalVO
	 * @return
	 */
	public Workflow createWorkflow(Integer moduleItemId, String userName);

	public void approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId);

	public boolean isFinalApprover(Integer moduleItemId, String personId);

}
