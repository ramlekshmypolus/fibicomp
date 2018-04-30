package com.polus.fibicomp.workflow.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

@Service
public interface WorkflowDao {

	public Workflow saveWorkflow(Workflow workflow);

	public List<WorkflowMapDetail> fetchWorkflowMapDetail();

	public WorkflowStatus fetchWorkflowStatusByStatusCode(String approveStatusCode);

	public Integer canTakeRoutingAction(Integer moduleItemId, String personId);
}
