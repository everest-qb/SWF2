package sunspring.swf.jpa;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SWF_OFFICE_DUTY_ALL database table.
 * 
 */
@Entity
@Table(name="SWF_OFFICE_DUTY_ALL",schema="SWF")
@NamedQuery(name="SwfOfficeDutyAll.findAll", query="SELECT s FROM SwfOfficeDutyAll s")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfOfficeDutyAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private BigDecimal createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DISABLE_DATE")
	private Date disableDate;

	@Id
	@SequenceGenerator(name="SWF_OFFICE_DUTY_ALL_DUTYID_GENERATOR", sequenceName="SWF_DEPT_ALL_S1",schema="SWF",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWF_OFFICE_DUTY_ALL_DUTYID_GENERATOR")
	@Column(name="DUTY_ID")
	private BigDecimal dutyId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENABLE_DATE")
	private Date enableDate;

	private String grades;

	@Column(name="JOB_LEVEL")
	private BigDecimal jobLevel;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private BigDecimal lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private BigDecimal lastUpdatedBy;

	@Column(name="POSITION_CODE")
	private String positionCode;

	private String segment1;

	private String segment2;

	private String segment3;

	private String segment4;

	private String segment5;

	public SwfOfficeDutyAll() {
	}

	public BigDecimal getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDisableDate() {
		return this.disableDate;
	}

	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}

	public BigDecimal getDutyId() {
		return this.dutyId;
	}

	public void setDutyId(BigDecimal dutyId) {
		this.dutyId = dutyId;
	}

	public Date getEnableDate() {
		return this.enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public String getGrades() {
		return this.grades;
	}

	public void setGrades(String grades) {
		this.grades = grades;
	}

	public BigDecimal getJobLevel() {
		return this.jobLevel;
	}

	public void setJobLevel(BigDecimal jobLevel) {
		this.jobLevel = jobLevel;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public BigDecimal getLastUpdateLogin() {
		return this.lastUpdateLogin;
	}

	public void setLastUpdateLogin(BigDecimal lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public BigDecimal getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(BigDecimal lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getPositionCode() {
		return this.positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getSegment1() {
		return this.segment1;
	}

	public void setSegment1(String segment1) {
		this.segment1 = segment1;
	}

	public String getSegment2() {
		return this.segment2;
	}

	public void setSegment2(String segment2) {
		this.segment2 = segment2;
	}

	public String getSegment3() {
		return this.segment3;
	}

	public void setSegment3(String segment3) {
		this.segment3 = segment3;
	}

	public String getSegment4() {
		return this.segment4;
	}

	public void setSegment4(String segment4) {
		this.segment4 = segment4;
	}

	public String getSegment5() {
		return this.segment5;
	}

	public void setSegment5(String segment5) {
		this.segment5 = segment5;
	}

}