package com.polus.fibicomp.proposal.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.pojo.ProposalBudget;
import com.polus.fibicomp.proposal.pojo.ProposalIrbProtocol;
import com.polus.fibicomp.proposal.pojo.ProposalKeyword;
import com.polus.fibicomp.proposal.pojo.ProposalPerson;
import com.polus.fibicomp.proposal.pojo.ProposalResearchArea;
import com.polus.fibicomp.proposal.pojo.ProposalSponsor;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.workflow.comparator.WorkflowDetailComparator;
import com.polus.fibicomp.workflow.dao.WorkflowDao;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.service.WorkflowService;

@Transactional
@Service(value = "proposalService")
public class ProposalServiceImpl implements ProposalService {

	protected static Logger logger = Logger.getLogger(ProposalServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private GrantCallDao grantCallDao;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private WorkflowDao workflowDao;

	@Override
	public String createProposal(ProposalVO proposalVO) {
		Integer grantCallId = proposalVO.getGrantCallId();
		Proposal proposal = proposalVO.getProposal();
		if (grantCallId != null) {
			proposal.setGrantCall(grantCallDao.fetchGrantCallById(grantCallId));
			proposal.setGrantCallId(grantCallId);
		}
		proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
		proposalVO.setGrantCallTypes(grantCallDao.fetchAllGrantCallTypes());
		proposalVO.setProposalCategories(proposalDao.fetchAllCategories());
		proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
		proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
		proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
		proposalVO.setProtocols(proposalDao.fetchAllProtocols());
		proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
		proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
		proposalVO.setProposalBudgetCategories(proposalDao.fetchAllBudgetCategories());
		proposalVO.setProposalInstituteCentreLabs(proposalDao.fetchAllInstituteCentrelabs());
		proposalVO.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
		proposalVO.setSponsorTypes(grantCallDao.fetchAllSponsorTypes());
		proposalVO.setProposalTypes(proposalDao.fetchAllProposalTypes());

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String addProposalAttachment(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			ProposalAttachment newAttachment = proposalVO.getNewAttachment();
			List<ProposalAttachment> proposalAttachments = new ArrayList<ProposalAttachment>();
			for (int i = 0; i < files.length; i++) {
				ProposalAttachment proposalAttachment = new ProposalAttachment();
				proposalAttachment.setAttachmentType(newAttachment.getAttachmentType());
				proposalAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
				proposalAttachment.setDescription(newAttachment.getDescription());
				proposalAttachment.setUpdateTimeStamp(newAttachment.getUpdateTimeStamp());
				proposalAttachment.setUpdateUser(newAttachment.getUpdateUser());
				proposalAttachment.setAttachment(files[i].getBytes());
				proposalAttachment.setFileName(files[i].getOriginalFilename());
				proposalAttachment.setMimeType(files[i].getContentType());
				proposalAttachments.add(proposalAttachment);
			}
			proposal.getProposalAttachments().addAll(proposalAttachments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String saveOrUpdateProposal(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		vo.setStatus(true);
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			vo.setMessage("Proposal saved successfully");
		} else {
			vo.setMessage("Proposal updated successfully");
		}
		vo.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String loadProposalById(Integer proposalId, String personId) {
		ProposalVO proposalVO = new ProposalVO();
		proposalVO.setPersonId(personId);
		Proposal proposal = proposalDao.fetchProposalById(proposalId);
		proposalVO.setProposal(proposal);
		int statusCode = proposal.getStatusCode();
		if (statusCode == 1 || statusCode == 9) {
			proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
			proposalVO.setGrantCallTypes(grantCallDao.fetchAllGrantCallTypes());
			proposalVO.setProposalCategories(proposalDao.fetchAllCategories());
			proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
			proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
			proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
			proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
			proposalVO.setProtocols(proposalDao.fetchAllProtocols());
			proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
			proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
			proposalVO.setProposalBudgetCategories(proposalDao.fetchAllBudgetCategories());
			proposalVO.setProposalInstituteCentreLabs(proposalDao.fetchAllInstituteCentrelabs());
			proposalVO.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
			proposalVO.setSponsorTypes(grantCallDao.fetchAllSponsorTypes());
			proposalVO.setProposalTypes(proposalDao.fetchAllProposalTypes());
		}

		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVED)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)
				|| proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED)) {
			canTakeRoutingAction(proposalVO);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			//WorkflowDetail finalWorkflowDetail = workflowDao.fetchFinalApprover(workflow.getWorkflowId());
			/*if (finalWorkflowDetail.getApproverPersonId().equals(personId) && finalWorkflowDetail.getApprovalStopNumber().equals(4)) {
				proposalVO.setFinalApprover(true);
			} else {
				proposalVO.setFinalApprover(false);
			}*/
			proposalVO.setWorkflow(workflow);
		}

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String fetchCostElementByBudgetCategory(ProposalVO vo) {
		vo.setProposalCostElements(proposalDao.fetchCostElementByBudgetCategory(vo.getBudgetCategoryCode()));
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String fetchAllAreaOfExcellence(ProposalVO vo) {
		vo.setProposalExcellenceAreas(proposalDao.fetchAllAreaOfExcellence());
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String deleteProposalKeyword(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalKeyword> list = proposal.getProposalKeywords();
			List<ProposalKeyword> updatedlist = new ArrayList<ProposalKeyword>(list);
			Collections.copy(updatedlist, list);
			for (ProposalKeyword proposalKeyword : list) {
				if (proposalKeyword.getKeywordId().equals(vo.getKeywordId())) {
					updatedlist.remove(proposalKeyword);
				}
			}
			proposal.getProposalKeywords().clear();
			proposal.getProposalKeywords().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal keyword deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal keyword");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalResearchArea(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalResearchArea> list = proposal.getProposalResearchAreas();
			List<ProposalResearchArea> updatedlist = new ArrayList<ProposalResearchArea>(list);
			Collections.copy(updatedlist, list);
			for (ProposalResearchArea proposalResearchArea : list) {
				if (proposalResearchArea.getResearchAreaId().equals(vo.getResearchAreaId())) {
					updatedlist.remove(proposalResearchArea);
				}
			}
			proposal.getProposalResearchAreas().clear();
			proposal.getProposalResearchAreas().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal research area deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal research area");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalPerson(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalPerson> list = proposal.getProposalPersons();
			List<ProposalPerson> updatedlist = new ArrayList<ProposalPerson>(list);
			Collections.copy(updatedlist, list);
			for (ProposalPerson proposalPerson : list) {
				if (proposalPerson.getPersonId().equals(vo.getProposalPersonId())) {
					updatedlist.remove(proposalPerson);
				}
			}
			proposal.getProposalPersons().clear();
			proposal.getProposalPersons().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal person deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal person");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalSponsor(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalSponsor> list = proposal.getProposalSponsors();
			List<ProposalSponsor> updatedlist = new ArrayList<ProposalSponsor>(list);
			Collections.copy(updatedlist, list);
			for (ProposalSponsor proposalSponsor : list) {
				if (proposalSponsor.getSponsorId().equals(vo.getSponsorId())) {
					updatedlist.remove(proposalSponsor);
				}
			}
			proposal.getProposalSponsors().clear();
			proposal.getProposalSponsors().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal sponsor deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal proposal");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteIrbProtocol(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalIrbProtocol> list = proposal.getProposalIrbProtocols();
			List<ProposalIrbProtocol> updatedlist = new ArrayList<ProposalIrbProtocol>(list);
			Collections.copy(updatedlist, list);
			for (ProposalIrbProtocol proposalIrbProtocol : list) {
				if (proposalIrbProtocol.getIrbProtocolId().equals(vo.getIrbProtocolId())) {
					updatedlist.remove(proposalIrbProtocol);
				}
			}
			proposal.getProposalIrbProtocols().clear();
			proposal.getProposalIrbProtocols().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal protocol deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal protocol");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalBudget(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalBudget> list = proposal.getProposalBudgets();
			List<ProposalBudget> updatedlist = new ArrayList<ProposalBudget>(list);
			Collections.copy(updatedlist, list);
			for (ProposalBudget proposalBudget : list) {
				if (proposalBudget.getBudgetId().equals(vo.getBudgetId())) {
					updatedlist.remove(proposalBudget);
				}
			}
			proposal.getProposalBudgets().clear();
			proposal.getProposalBudgets().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal budget deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal budget");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public String deleteProposalAttachment(ProposalVO vo) {
		try {
			Proposal proposal = proposalDao.fetchProposalById(vo.getProposalId());
			List<ProposalAttachment> list = proposal.getProposalAttachments();
			List<ProposalAttachment> updatedlist = new ArrayList<ProposalAttachment>(list);
			Collections.copy(updatedlist, list);
			for (ProposalAttachment proposalAttachment : list) {
				if (proposalAttachment.getAttachmentId().equals(vo.getAttachmentId())) {
					updatedlist.remove(proposalAttachment);
				}
			}
			proposal.getProposalAttachments().clear();
			proposal.getProposalAttachments().addAll(updatedlist);
			proposalDao.saveOrUpdateProposal(proposal);
			vo.setProposal(proposal);
			vo.setStatus(true);
			vo.setMessage("Proposal attachment deleted successfully");
		} catch (Exception e) {
			vo.setStatus(true);
			vo.setMessage("Problem occurred in deleting proposal attachment");
			e.printStackTrace();
		}
		return committeeDao.convertObjectToJSON(vo);
	}

	@Override
	public ResponseEntity<byte[]> downloadProposalAttachment(Integer attachmentId) {
		ProposalAttachment attachment = proposalDao.fetchAttachmentById(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			byte[] data = attachment.getAttachment();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
			String filename = attachment.getFileName();
			headers.setContentDispositionFormData(filename, filename);
			headers.setContentLength(data.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentData;
	}

	@Override
	public String submitProposal(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		Workflow workflow = workflowService.createWorkflow(proposal.getProposalId(), proposalVO.getUserName(), proposalVO.getProposalStatusCode());
		canTakeRoutingAction(proposalVO);
		proposalVO.setWorkflow(workflow);
		proposalVO.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	public void canTakeRoutingAction(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		if (proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS) || proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			boolean currentPerson = true;
			List<WorkflowDetail> workflowDetails = workflow.getWorkflowDetails();
			Collections.sort(workflowDetails, new WorkflowDetailComparator());
			for (WorkflowDetail workflowDetail : workflowDetails) {
				if (currentPerson == true) {
					if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId()) && (workflowDetail.getApprovalStatusCode().equals("W") || workflowDetail.getApprovalStatusCode().equals("WR"))) {
						if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId()) && !workflowDetail.getApprovalStatusCode().equals("WR")) {
							proposalVO.setIsApproved(false);
							proposalVO.setIsApprover(true);
						}
						proposalVO.setApproverStopNumber(workflowDetail.getApprovalStopNumber());
						currentPerson = false;
						if (workflowDetail.getRoleTypeCode() == 3) {
							proposalVO.setIsReviewer(true);
							proposalVO.setIsReviewed(false);
						}
						if (workflowDetail.getRoleTypeCode() == 2) {
							proposalVO.setIsGrantAdmin(true);
						}
					} else if (workflowDetail.getApprovalStatusCode().equals("W") && !proposal.getStatusCode().equals(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS)) {
						currentPerson = false;
					}
				}
				if (workflowDetail.getApprovalStatusCode().equals("RC")) {
					proposalVO.setIsReviewed(true);
				}
			}
		}
	}

	@Override
	public String approveOrRejectProposal(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			String actionType = proposalVO.getActionType();
			String approverComment = proposalVO.getApproveComment();
			Integer approvalStopNumber = proposalVO.getApproverStopNumber();

			logger.info("actionType : " + actionType);
			logger.info("personId : " + proposalVO.getPersonId());
			logger.info("approverComment : " + approverComment);
			logger.info("approvalStopNumber : " + approvalStopNumber);

			WorkflowDetail workflowDetail = workflowService.approveOrRejectWorkflowDetail(actionType, proposal.getProposalId(), proposalVO.getPersonId(), approverComment, files, approvalStopNumber);
			/*boolean isFirstApprover = workflowService.isFirstApprover(proposal.getProposalId(), proposalVO.getPersonId());
			if (isFirstApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			}*/
			boolean isFinalApprover = workflowService.isFinalApprover(proposal.getProposalId(), proposalVO.getPersonId(), approvalStopNumber);
			if (isFinalApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVED);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVED));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			} else if (!isFinalApprover && actionType.equals("A")) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			} else if (actionType.equals("R")) {
				if(workflowDetail.getRoleTypeCode() == 2) {
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED));
					proposal = proposalDao.saveOrUpdateProposal(proposal);
				} else {
					proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
					proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
					proposal = proposalDao.saveOrUpdateProposal(proposal);
				}				
			}
			proposalVO.setIsApproved(true);
			proposalVO.setIsApprover(true);
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			proposalVO.setWorkflow(workflow);
			proposalVO.setProposal(proposal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String assignReviewer(ProposalVO proposalVO) {
		Proposal proposal = proposalVO.getProposal();
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS));
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		Workflow workflow = workflowService.assignWorkflowReviewers(proposalVO.getProposalId(), proposalVO.getUserName());
		proposalVO.setWorkflow(workflow);
		proposalVO.setProposal(proposal);
		//proposalVO.setIsReviewer(true);
		proposalVO.setIsGrantAdmin(true);
		proposalVO.setIsReviewed(false);
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String reviewCompleted(MultipartFile[] files, String formDataJSON) {
		boolean reviewComplete = true;
		ProposalVO proposalVO = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);		
			Proposal proposal = proposalVO.getProposal();
			Workflow workflow = workflowDao.fetchActiveWorkflowByModuleItemId(proposal.getProposalId());
			List<WorkflowDetail> workflowDetails = workflowDao.fetchWorkflowDetailListByApprovalStopNumber(workflow.getWorkflowId(), 3, Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW);
			for(WorkflowDetail workflowDetail : workflowDetails) {
				if (workflowDetail.getApproverPersonId().equals(proposalVO.getPersonId())) {
					workflowDetail.setApprovalStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED);
					workflowDetail.setWorkflowStatus(workflowDao.fetchWorkflowStatusByStatusCode(Constants.WORKFLOW_STATUS_CODE_REVIEW_COMPLETED));
					workflowDetail.setApprovalComment(proposalVO.getApproveComment());
					workflowDetail.setApprovalDate(new Date(committeeDao.getCurrentDate().getTime()));
					if (files != null) {
						List<WorkflowAttachment> workflowAttachments = new ArrayList<WorkflowAttachment>();
						for (int i = 0; i < files.length; i++) {
							WorkflowAttachment workflowAttachment = new WorkflowAttachment();
							workflowAttachment.setDescription(proposalVO.getApproveComment());
							workflowAttachment.setUpdateTimeStamp(committeeDao.getCurrentTimestamp());
							workflowAttachment.setUpdateUser(proposalVO.getPersonId());			
							workflowAttachment.setAttachment(files[i].getBytes());
							workflowAttachment.setFileName(files[i].getOriginalFilename());
							workflowAttachment.setMimeType(files[i].getContentType());
							workflowAttachment.setWorkflowDetail(workflowDetail);
							workflowAttachments.add(workflowAttachment);
						}
						workflowDetail.getWorkflowAttachments().addAll(workflowAttachments);
					}
					workflowDao.saveWorkflowDetail(workflowDetail);
				}
				if (workflowDetail.getApprovalStatusCode().equals(Constants.WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW)) {
					reviewComplete = false;
				}
			}
			if (reviewComplete) {
				proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
				proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS));
				proposal = proposalDao.saveOrUpdateProposal(proposal);
			}
			proposalVO.setIsReviewed(true);
			proposalVO.setIsReviewer(true);
			proposalVO.setWorkflow(workflow);
			proposalVO.setProposal(proposal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}
}
