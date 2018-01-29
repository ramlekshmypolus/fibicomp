package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIBI_COMM_CORRESP_BATCH")
public class CommitteeCorrespBatch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CORRESP_BATCH_ID")
	private String correspBatchId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_COMM_CORRESP_BATCH"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_COMM_CORRESP_BATCH2"), name = "BATCH_CORRESPONDENCE_TYPE_CODE", referencedColumnName = "BATCH_CORRESPONDENCE_TYPE_CODE")
	private BatchCorrespondence batchCorrespondence;

	@Column(name = "BATCH_RUN_DATE")
	private Timestamp batchRunDate;

	@Column(name = "TIME_WINDOW_START")
	private Timestamp timeWindowStart;

	@Column(name = "TIME_WINDOW_END")
	private Timestamp timeWindowEnd;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer verNbr;

	@Column(name = "OBJ_ID")
	private String objId;

	public String getCorrespBatchId() {
		return correspBatchId;
	}

	public void setCorrespBatchId(String correspBatchId) {
		this.correspBatchId = correspBatchId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public Timestamp getBatchRunDate() {
		return batchRunDate;
	}

	public void setBatchRunDate(Timestamp batchRunDate) {
		this.batchRunDate = batchRunDate;
	}

	public Timestamp getTimeWindowStart() {
		return timeWindowStart;
	}

	public void setTimeWindowStart(Timestamp timeWindowStart) {
		this.timeWindowStart = timeWindowStart;
	}

	public Timestamp getTimeWindowEnd() {
		return timeWindowEnd;
	}

	public void setTimeWindowEnd(Timestamp timeWindowEnd) {
		this.timeWindowEnd = timeWindowEnd;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getVerNbr() {
		return verNbr;
	}

	public void setVerNbr(Integer verNbr) {
		this.verNbr = verNbr;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BatchCorrespondence getBatchCorrespondence() {
		return batchCorrespondence;
	}

	public void setBatchCorrespondence(BatchCorrespondence batchCorrespondence) {
		this.batchCorrespondence = batchCorrespondence;
	}
}
