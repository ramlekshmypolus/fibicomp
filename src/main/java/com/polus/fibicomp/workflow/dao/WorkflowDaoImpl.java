package com.polus.fibicomp.workflow.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.vo.ProposalVO;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowStatus;

@Transactional
@Service(value = "workflowDao")
public class WorkflowDaoImpl implements WorkflowDao {

	protected static Logger logger = Logger.getLogger(WorkflowDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public Workflow saveWorkflow(Workflow workflow) {
		hibernateTemplate.saveOrUpdate(workflow);
		return workflow;
	}

	@Override
	public List<WorkflowMapDetail> fetchWorkflowMapDetail() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(WorkflowMapDetail.class);
		criteria.add(Restrictions.eq("mapId", 1));
		@SuppressWarnings("unchecked")
		List<WorkflowMapDetail> workflowMapDetails = criteria.list();
		return workflowMapDetails;
	}

	@Override
	public WorkflowStatus fetchWorkflowStatusByStatusCode(String approveStatusCode) {
		return hibernateTemplate.get(WorkflowStatus.class, approveStatusCode);
	}

	@Override
	public Integer canTakeRoutingAction(Integer moduleItemId, String personId) {
		Integer count = 0;
		Integer approveFlag = 0;
		Integer currentStopNumber = 0;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria countCriteria = session.createCriteria(Proposal.class);
		countCriteria.add(Restrictions.eq("proposalId", moduleItemId));
		countCriteria.add(Restrictions.eq("statusCode", Constants.PROPOSAL_STATUS_CODE_SUBMITTED));
		count = (Integer) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
		logger.info("Count : " + count);
		if (count == 0) {
			return count;
		}
		Criteria workflowCount = session.createCriteria(Workflow.class);
		Criteria workflowDetail = session.createCriteria(WorkflowDetail.class);
		workflowCount.add(Restrictions.eq("moduleCode", moduleItemId));
		workflowCount.add(Restrictions.eq("isWorkflowActive", true));
		workflowDetail.add(Restrictions.eq("workflow.workflowId", ""));
		workflowDetail.add(Restrictions.eq("approverPersonId", personId));
		workflowDetail.add(Restrictions.eq("approvalStatusCode", Constants.WORKFLOW_STATUS_CODE_WAITING));
		List<WorkflowDetail> list = workflowDetail.list();
		workflowCount.add(Restrictions.eq("workflowDetails", list));
		count = (Integer) workflowCount.setProjection(Projections.rowCount()).uniqueResult();
		logger.info("Workflow Count : " + count);
		if (count == 2) {
			return count;
		} else {
			if (count == 1) {
				approveFlag = 1;
			} else {
				return 0;
			}			
		}
		Criteria criteria = session.createCriteria(WorkflowDetail.class).setProjection(Projections.max("approvalStopNumber"));
		criteria.add(Restrictions.eq("moduleCode", moduleItemId));
			Integer maxAge = (Integer)criteria.uniqueResult();
		return count;
	}

	@Override
	public Proposal approveProposal(ProposalVO proposalVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Proposal rejectProposal(ProposalVO proposalVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId) {
		Workflow workflow = null;
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Workflow.class);
		criteria.add(Restrictions.eq("moduleItemId", moduleItemId));
		criteria.add(Restrictions.eq("isWorkflowActive", true));
		workflow = (Workflow) criteria.uniqueResult();
		return workflow;
	}

}
