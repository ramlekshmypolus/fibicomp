package com.polus.fibicomp.workflow.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Workflow fetchActiveWorkflowByModuleItemId(Integer moduleItemId) {
		Workflow workflow = null;
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Workflow.class);
		criteria.add(Restrictions.eq("moduleItemId", moduleItemId));
		criteria.add(Restrictions.eq("isWorkflowActive", true));
		workflow = (Workflow) criteria.uniqueResult();
		return workflow;
	}

	@Override
	public WorkflowDetail findUniqueWorkflowDetailByCriteria(Integer workflowId, String personId) {
		WorkflowDetail workflowDetail;
		Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(WorkflowDetail.class);
		criteria.add(Restrictions.eq("workflow.workflowId", workflowId));
		criteria.add(Restrictions.eq("approverPersonId", personId));
		criteria.add(Restrictions.eq("approvalStatusCode", "W"));
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
		WorkflowDetail workflowDetail = (WorkflowDetail) criteria.addOrder(Order.desc("approvalStopNumber")).setMaxResults(1).uniqueResult();
		return workflowDetail;
	}

}
