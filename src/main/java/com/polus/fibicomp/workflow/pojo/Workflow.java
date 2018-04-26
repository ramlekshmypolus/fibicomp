package com.polus.fibicomp.workflow.pojo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "FIBI_WORKFLOW")
public class Workflow implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "workflowIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "workflowIdGenerator")
	@Column(name = "WORKFLOW_ID")
	private Integer workflowId;

	@Column(name = "MODULE_CODE")
	private Integer moduleCode;

	@Column(name = "MODULE_ITEM_ID")
	private String moduleItemId;

	@Column(name = "WORKFLOW_SEQUENCE")
	private Integer workflowSequence;

	@Column(name = "IS_WORKFLOW_ACTIVE")
	private Boolean isWorkflowActive;

	@Column(name = "WORKFLOW_START_DATE")
	private Date workflowStartDate;

	@Column(name = "WORKFLOW_END_DATE")
	private Date workflowEndDate;

	@Column(name = "WORKFLOW_START_PERSON")
	private String workflowStartPerson;

	@Column(name = "WORKFLOW_END_PERSON")
	private String workflowEndPerson;

	@Column(name = "COMMENTS")
	private String comments;

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public Integer getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(Integer moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleItemId() {
		return moduleItemId;
	}

	public void setModuleItemId(String moduleItemId) {
		this.moduleItemId = moduleItemId;
	}

	public Integer getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(Integer workflowSequence) {
		this.workflowSequence = workflowSequence;
	}

	public Boolean getIsWorkflowActive() {
		return isWorkflowActive;
	}

	public void setIsWorkflowActive(Boolean isWorkflowActive) {
		this.isWorkflowActive = isWorkflowActive;
	}

	public Date getWorkflowStartDate() {
		return workflowStartDate;
	}

	public void setWorkflowStartDate(Date workflowStartDate) {
		this.workflowStartDate = workflowStartDate;
	}

	public Date getWorkflowEndDate() {
		return workflowEndDate;
	}

	public void setWorkflowEndDate(Date workflowEndDate) {
		this.workflowEndDate = workflowEndDate;
	}

	public String getWorkflowStartPerson() {
		return workflowStartPerson;
	}

	public void setWorkflowStartPerson(String workflowStartPerson) {
		this.workflowStartPerson = workflowStartPerson;
	}

	public String getWorkflowEndPerson() {
		return workflowEndPerson;
	}

	public void setWorkflowEndPerson(String workflowEndPerson) {
		this.workflowEndPerson = workflowEndPerson;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
