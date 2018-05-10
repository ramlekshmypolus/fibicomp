package com.polus.fibicomp.workflow.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;

@Service
public interface WorkflowService {

	/**
	 * @param proposalVO
	 * @return
	 */
	public Workflow createWorkflow(Integer moduleItemId, String userName, Integer statusCode);

	public WorkflowDetail approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files, Integer approverStopNumber) throws IOException;

	public boolean isFinalApprover(Integer moduleItemId, String personId, Integer approverStopNumber);

	public ResponseEntity<byte[]> downloadWorkflowAttachment(Integer attachmentId);

	public boolean isFirstApprover(Integer moduleItemId, String personId, Integer approverStopNumber);

	public Workflow assignWorkflowReviewers(Integer moduleItemId, String userName, List<WorkflowMapDetail> reviewers);

	public List<WorkflowMapDetail> fetchReviewers(Integer moduleItemId);

}
