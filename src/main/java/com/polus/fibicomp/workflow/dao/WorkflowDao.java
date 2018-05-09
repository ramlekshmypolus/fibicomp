package com.polus.fibicomp.workflow.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

@Service
public interface WorkflowDao {

	public Workflow saveWorkflow(Workflow workflow);

	public List<WorkflowMapDetail> fetchWorkflowMapDetail();

	public WorkflowStatus fetchWorkflowStatusByStatusCode(String approveStatusCode);

	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId);

	public WorkflowDetail findUniqueWorkflowDetailByCriteria(Integer workflowId, String personId, Integer approverStopNumber);

	public WorkflowDetail saveWorkflowDetail(WorkflowDetail workflowDetail);

	public WorkflowDetail fetchFinalApprover(Integer workflowId);

	public WorkflowAttachment fetchWorkflowAttachmentById(Integer attachmentId);

	public WorkflowDetail fetchFirstApprover(Integer workflowId);

	public List<WorkflowDetail> fetchWorkflowDetailListByApprovalStopNumber(Integer approvalStopNumber, String approvalStatusCode);

	public Integer getMaxStopNumber(Integer workflowId);

	public List<WorkflowMapDetail> fetchWorkflowMapDetailReviewers();

}
