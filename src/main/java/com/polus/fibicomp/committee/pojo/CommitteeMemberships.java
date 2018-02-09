package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.polus.fibicomp.committee.schedule.DateUtils;
import com.polus.fibicomp.view.PersonDetailsView;

@Entity
@Table(name = "FIBI_COMM_MEMBERSHIPS")
public class CommitteeMemberships implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "membershipIdGererator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "membershipIdGererator")
	@Column(name = "COMM_MEMBERSHIP_ID", updatable = false, nullable = false)
	private Integer commMembershipId;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBERSHIPS"), name = "COMMITTEE_ID", referencedColumnName = "COMMITTEE_ID")
	private Committee committee;

	@Column(name = "MEMBERSHIP_ID")
	private String membershipId;

	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Column(name = "PERSON_ID")
	private String personId;

	@Column(name = "PERSON_NAME")
	private String personName;

	@Column(name = "NON_EMPLOYEE_FLAG")
	private String nonEmployeeFlag;

	@Column(name = "PAID_MEMBER_FLAG")
	private String paidMemberFlag;

	@Column(name = "TERM_START_DATE")
	private Date termStartDate;

	@Column(name = "TERM_END_DATE")
	private Date termEndDate;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_MEMBERSHIPS_2"), name = "MEMBERSHIP_TYPE_CODE", referencedColumnName = "MEMBERSHIP_TYPE_CODE", insertable = false, updatable = false)
	private CommitteeMembershipType committeeMembershipType;

	@Column(name = "MEMBERSHIP_TYPE_CODE")
	private String membershipTypeCode;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	private Integer versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	private String objectId;

	@Column(name = "CONTACT_NOTES")
	private char[] contactNotes;

	@Column(name = "TRAINING_NOTES")
	private char[] trainingNotes;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeMemberships", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeMemberRoles> committeeMemberRoles;

	@JsonManagedReference
	@OneToMany(mappedBy = "committeeMemberships", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<CommitteeMemberExpertise> committeeMemberExpertises;

	@Transient
	private PersonDetailsView personDetails;

	@Column(name = "COMMENTS")
	private String comments;

	public CommitteeMemberships() {
		setCommitteeMemberRoles(new ArrayList<CommitteeMemberRoles>());
		setCommitteeMemberExpertises(new ArrayList<CommitteeMemberExpertise>());
		setObjectId(UUID.randomUUID().toString());
	}

	public Integer getCommMembershipId() {
		return commMembershipId;
	}

	public void setCommMembershipId(Integer commMembershipId) {
		this.commMembershipId = commMembershipId;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public String getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getNonEmployeeFlag() {
		return nonEmployeeFlag;
	}

	public void setNonEmployeeFlag(String nonEmployeeFlag) {
		this.nonEmployeeFlag = nonEmployeeFlag;
	}

	public String getPaidMemberFlag() {
		return paidMemberFlag;
	}

	public void setPaidMemberFlag(String paidMemberFlag) {
		this.paidMemberFlag = paidMemberFlag;
	}

	public CommitteeMembershipType getCommitteeMembershipType() {
		return committeeMembershipType;
	}

	public void setCommitteeMembershipType(CommitteeMembershipType committeeMembershipType) {
		this.committeeMembershipType = committeeMembershipType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public char[] getContactNotes() {
		return contactNotes;
	}

	public void setContactNotes(char[] contactNotes) {
		this.contactNotes = contactNotes;
	}

	public char[] getTrainingNotes() {
		return trainingNotes;
	}

	public void setTrainingNotes(char[] trainingNotes) {
		this.trainingNotes = trainingNotes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Date getTermStartDate() {
		return termStartDate;
	}

	public void setTermStartDate(Date termStartDate) {
		this.termStartDate = termStartDate;
	}

	public Date getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(Date termEndDate) {
		this.termEndDate = termEndDate;
	}

	public List<CommitteeMemberRoles> getCommitteeMemberRoles() {
		return committeeMemberRoles;
	}

	public void setCommitteeMemberRoles(List<CommitteeMemberRoles> committeeMemberRoles) {
		this.committeeMemberRoles = committeeMemberRoles;
	}

	public List<CommitteeMemberExpertise> getCommitteeMemberExpertises() {
		return committeeMemberExpertises;
	}

	public void setCommitteeMemberExpertises(List<CommitteeMemberExpertise> committeeMemberExpertises) {
		this.committeeMemberExpertises = committeeMemberExpertises;
	}

	public PersonDetailsView getPersonDetails() {
		return personDetails;
	}

	public void setPersonDetails(PersonDetailsView personDetails) {
		this.personDetails = personDetails;
	}

	/**
     * This method determines if the current committee member is active as of the current date.
     * @return true if member is active, false otherwise
     */
    public boolean isActive() {
        Date currentDate = DateUtils.clearTimeFields(new Date(System.currentTimeMillis()));
        return isActive(currentDate);
    }

    /**
     * 
     * This method determines if the current committee member is active for the given date
     * @param date
     * @return true if member is active, false otherwise
     */
    public boolean isActive(Date date) {
        boolean isActive = false;
        for (CommitteeMemberRoles role : committeeMemberRoles) {
            if (role.getStartDate() != null && role.getEndDate() != null && !date.before(role.getStartDate()) && !date.after(role.getEndDate())) {
                if (role.getMembershipRoleCode().equals(CommitteeMemberRoles.INACTIVE_ROLE)) {
                    isActive = false;
                    break;
                } else {
                    isActive = true;
                }
            }
        }
        return isActive;
    }

    /**
     * Indicates if the committee memberships are of the same person (i.e. the personId and rolodexId are the same).
     * 
     * @param committeeMembership - the committee membership to compare against
     * @return <code>true</code> if both committee membership belong to the same person, <code>false</code> otherwise
     */
    public boolean isSamePerson(CommitteeMemberships committeeMembership) {
        boolean isEquals = false;
        if (this.getPersonId() != null && this.getPersonId().equals(committeeMembership.getPersonId())) {// || this.getRolodexId() != null && this.getRolodexId().equals(committeeMembership.getRolodexId())
            isEquals = true;
        }
        return isEquals;
    }

	public String getMembershipTypeCode() {
		return membershipTypeCode;
	}

	public void setMembershipTypeCode(String membershipTypeCode) {
		this.membershipTypeCode = membershipTypeCode;
	}

}
