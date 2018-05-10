package com.polus.fibicomp.workflow.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.workflow.pojo.Workflow;
import com.polus.fibicomp.workflow.pojo.WorkflowAttachment;
import com.polus.fibicomp.workflow.pojo.WorkflowDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowMapDetail;
import com.polus.fibicomp.workflow.pojo.WorkflowReviewerDetail;
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
	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId) {
		Workflow workflow = null;
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Workflow.class);
		criteria.add(Restrictions.eq("moduleItemId", moduleItemId));
		criteria.add(Restrictions.eq("isWorkflowActive", true));
		workflow = (Workflow) criteria.uniqueResult();
		return workflow;
	}

	@Override
	public WorkflowDetail findUniqueWorkflowDetailByCriteria(Integer workflowId, String personId, Integer approverStopNumber) {
		WorkflowDetail workflowDetail;
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		criteria.add(Restrictions.eq("approverPersonId", personId));
		if (approverStopNumber != null) {
			criteria.add(Restrictions.eq("approvalStopNumber", approverStopNumber));
		}
		workflowDetail = (WorkflowDetail) criteria.uniqueResult();
		return workflowDetail;
	}

	@Override
	public WorkflowDetail saveWorkflowDetail(WorkflowDetail workflowDetail) {
		hibernateTemplate.saveOrUpdate(workflowDetail);
		return workflowDetail;
	}

	@Override
	public WorkflowDetail fetchFinalApprover(Integer workflowId) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		//criteria.add(Restrictions.eq("approvalStatusCode", "W"));
		criteria.add(Restrictions.eq("roleTypeCode", 2));
		//WorkflowDetail workflowDetail = (WorkflowDetail) criteria.addOrder(Order.desc("approvalStopNumber")).setMaxResults(1).uniqueResult();
		WorkflowDetail workflowDetail = (WorkflowDetail) criteria.list().get(0);
		return workflowDetail;
	}

	@Override
	public WorkflowAttachment fetchWorkflowAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(WorkflowAttachment.class, attachmentId);
	}

	@Override
	public WorkflowDetail fetchFirstApprover(Integer workflowId) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		//criteria.add(Restrictions.eq("approvalStatusCode", "W"));
		WorkflowDetail workflowDetail = (WorkflowDetail) criteria.addOrder(Order.asc("approvalStopNumber")).setMaxResults(1).uniqueResult();
		return workflowDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkflowDetail> fetchWorkflowDetailListByApprovalStopNumber(Integer workflowId, Integer approvalStopNumber, String approvalStatusCode) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		if (approvalStopNumber != null) {
			criteria.add(Restrictions.eq("approvalStopNumber", approvalStopNumber));
		}
		criteria.add(Restrictions.eq("approvalStatusCode", approvalStatusCode));
		List<WorkflowDetail> workflowDetailList = criteria.list();
		return workflowDetailList;
	}

	@Override
	public Integer getMaxStopNumber(Integer workflowId) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.setProjection(Projections.max("approvalStopNumber"));
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		Integer maxApprovalStopNumber = (Integer)criteria.uniqueResult();
		return maxApprovalStopNumber;
	}

	@Override
	public List<WorkflowMapDetail> fetchWorkflowMapDetailReviewers() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(WorkflowMapDetail.class);
		criteria.add(Restrictions.eq("mapId", 1));
		criteria.add(Restrictions.eq("roleTypeCode", 3));
		@SuppressWarnings("unchecked")
		List<WorkflowMapDetail> workflowMapDetails = criteria.list();
		return workflowMapDetails;
	}

	@Override
	public WorkflowDetail fetchWorkflowByParams(Integer workflowId, String personId, Integer stopNumber) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		criteria.add(Restrictions.eq("approvalStopNumber", stopNumber));
		criteria.add(Restrictions.eq("approverPersonId", personId));
		criteria.add(Restrictions.eq("approvalStatusCode", "A"));
		WorkflowDetail workflowDetail = (WorkflowDetail) criteria.list().get(0);
		return workflowDetail;
	}

	@Override
	public WorkflowDetail fetchWorkflowDetailById(Integer workflowId) {
		return hibernateTemplate.get(WorkflowDetail.class, workflowId);
	}

	@Override
	public List<WorkflowReviewerDetail> fetchPersonIdByCriteria(Integer workflowDetailId, String approvalStatusCode) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowReviewerDetail.class);
		criteria.add(Restrictions.eq("workflowDetail.workflowDetailId", workflowDetailId));
		criteria.add(Restrictions.eq("approvalStatusCode", approvalStatusCode));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("reviewerPersonId"), "reviewerPersonId");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(WorkflowReviewerDetail.class));
		@SuppressWarnings("unchecked")
		List<WorkflowReviewerDetail> reviewerDetails = criteria.list();
		return reviewerDetails;
	}

	@Override
	public List<WorkflowMapDetail> fetchWorkflowMapDetailByPersonId(List<String> personIds) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowMapDetail.class);
		if (!personIds.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("approverPersonId", personIds)));
		}
		criteria.add(Restrictions.eq("roleTypeCode", Constants.REVIEWER_ROLE_TYPE_CODE));
		@SuppressWarnings("unchecked")
		List<WorkflowMapDetail> workflowMapDetails = criteria.list();
		return workflowMapDetails;
	}

	@Override
	public Long activeWorkflowCountByModuleItemId(Integer moduleItemId) {
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", moduleItemId));
		Long workflowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return workflowCount;
	}

	@Override
	public WorkflowReviewerDetail saveWorkflowReviewDetail(WorkflowReviewerDetail workflowReviewerDetail) {
		hibernateTemplate.saveOrUpdate(workflowReviewerDetail);
		return workflowReviewerDetail;
	}

}
