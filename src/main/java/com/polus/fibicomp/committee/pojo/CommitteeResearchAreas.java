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
@Table(name = "FIBI_COMM_RESEARCH_AREAS")
public class CommitteeResearchAreas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_RESEARCH_AREAS_ID")
	private Integer commResearchAreasId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_RESEARCH_AREAS"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@Column(name = "RESEARCH_AREA_CODE")
	private String researchAreaCode;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp UPDATE_TIMESTAMP;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "VER_NBR")
	private Integer verNbr;

	@Column(name = "OBJ_ID")
	private String objId;

	public Integer getCommResearchAreasId() {
		return commResearchAreasId;
	}

	public void setCommResearchAreasId(Integer commResearchAreasId) {
		this.commResearchAreasId = commResearchAreasId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getResearchAreaCode() {
		return researchAreaCode;
	}

	public void setResearchAreaCode(String researchAreaCode) {
		this.researchAreaCode = researchAreaCode;
	}

	public Timestamp getUPDATE_TIMESTAMP() {
		return UPDATE_TIMESTAMP;
	}

	public void setUPDATE_TIMESTAMP(Timestamp uPDATE_TIMESTAMP) {
		UPDATE_TIMESTAMP = uPDATE_TIMESTAMP;
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
}
