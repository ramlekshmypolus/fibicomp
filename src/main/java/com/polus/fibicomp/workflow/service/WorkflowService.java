package com.polus.fibicomp.workflow.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.workflow.pojo.Workflow;

@Service
public interface WorkflowService {

	/**
	 * @param proposalVO
	 * @return
	 */
	public Workflow createWorkflow(Integer moduleItemId, String userName);

	public void approveOrRejectWorkflowDetail(String actionType, Integer moduleItemId, String personId, String approverComment, MultipartFile[] files);

	public boolean isFinalApprover(Integer moduleItemId, String personId);

	public String addWorkflowAttachment(MultipartFile[] files, String formDataJSON);

	public ResponseEntity<byte[]> downloadWorkflowAttachment(Integer attachmentId);

}
