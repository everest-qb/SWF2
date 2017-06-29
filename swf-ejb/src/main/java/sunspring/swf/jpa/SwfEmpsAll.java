package sunspring.swf.jpa;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;


/**
 * The persistent class for the SWF_EMPS_ALL database table.
 * 
 */
@Entity
@Table(name="SWF_EMPS_ALL",schema="SWF")
@NamedQuery(name="SwfEmpsAll.findAll", query="SELECT s FROM SwfEmpsAll s")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfEmpsAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private BigDecimal createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DISABLE_DATE")
	private Date disableDate;

	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "DUTY_ID",table="SWF_EMPS_ALL")
	private SwfOfficeDutyAll duty;

	@Column(name="EMAIL_ADDRESS")
	@Basic
	private String emailAddress;

	@Column(name="EMP_CNAME")
	private String empCname;

	@Column(name="EMP_ENAME")
	private String empEname;

	@Id
	@SequenceGenerator(name="SWF_EMPS_ALL_EMPID_GENERATOR", sequenceName="SWF_EMPS_ALL_S1",schema="SWF",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWF_EMPS_ALL_EMPID_GENERATOR")
	@Column(name="EMP_ID")
	private BigDecimal empId;

	@Column(name="EMP_NUM")
	private String empNum;

	@Column(name="EMP_SNAME")
	private String empSname;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENABLE_DATE")
	private Date enableDate;

	private BigDecimal extension;

	@Column(name="JOB_LEVEL_CODE")
	private String jobLevelCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private BigDecimal lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private BigDecimal lastUpdatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LEAVE_OFFICE_DATE")
	private Date leaveOfficeDate;

	@Column(name="MANAGER_FLAG")
	private String managerFlag;

	@Column(name="MANAGER_SEQ")
	private BigDecimal managerSeq;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OFFICIAL_DATE")
	private Date officialDate;

	//@Column(name="ORG_DEPT_ID")
	//private BigDecimal orgDeptId;
	
	@ManyToOne
	@JoinColumn(name = "ORG_DEPT_ID", table = "SWF_EMPS_ALL")
	private SwfDeptAll department;
	
	@ManyToMany(fetch = EAGER, cascade = { MERGE, PERSIST })
	@JoinTable(joinColumns = @JoinColumn(referencedColumnName = "EMP_ID", name = "EMP_ID"), inverseJoinColumns = @JoinColumn(referencedColumnName = "DEPT_ID", name = "DEPT_ID"), schema = "SWF", name = "SWF_DEPT_MEMBER")
	private List<SwfDeptAll> swfDepartments;

	@Column(name="ORG_EMP_ID")
	private BigDecimal orgEmpId;

	@Column(name="ORG_ID")
	private BigDecimal orgId;

	@Column(name="ORG_REV")
	private String orgRev;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TAKE_OFFICE_DATE")
	private Date takeOfficeDate;

	@Column(name="WORK_TYPE_NAME")
	private String workTypeName;

	public SwfEmpsAll() {
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

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmpCname() {
		return this.empCname;
	}

	public void setEmpCname(String empCname) {
		this.empCname = empCname;
	}

	public String getEmpEname() {
		return this.empEname;
	}

	public void setEmpEname(String empEname) {
		this.empEname = empEname;
	}

	public BigDecimal getEmpId() {
		return this.empId;
	}

	public void setEmpId(BigDecimal empId) {
		this.empId = empId;
	}

	public String getEmpNum() {
		return this.empNum;
	}

	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}

	public String getEmpSname() {
		return this.empSname;
	}

	public void setEmpSname(String empSname) {
		this.empSname = empSname;
	}

	public Date getEnableDate() {
		return this.enableDate;
	}

	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}

	public BigDecimal getExtension() {
		return this.extension;
	}

	public void setExtension(BigDecimal extension) {
		this.extension = extension;
	}

	public String getJobLevelCode() {
		return this.jobLevelCode;
	}

	public void setJobLevelCode(String jobLevelCode) {
		this.jobLevelCode = jobLevelCode;
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

	public Date getLeaveOfficeDate() {
		return this.leaveOfficeDate;
	}

	public void setLeaveOfficeDate(Date leaveOfficeDate) {
		this.leaveOfficeDate = leaveOfficeDate;
	}

	public String getManagerFlag() {
		return this.managerFlag;
	}

	public void setManagerFlag(String managerFlag) {
		this.managerFlag = managerFlag;
	}

	public BigDecimal getManagerSeq() {
		return this.managerSeq;
	}

	public void setManagerSeq(BigDecimal managerSeq) {
		this.managerSeq = managerSeq;
	}

	public Date getOfficialDate() {
		return this.officialDate;
	}

	public void setOfficialDate(Date officialDate) {
		this.officialDate = officialDate;
	}

	/*public BigDecimal getOrgDeptId() {
		return this.orgDeptId;
	}

	public void setOrgDeptId(BigDecimal orgDeptId) {
		this.orgDeptId = orgDeptId;
	}*/

	public BigDecimal getOrgEmpId() {
		return this.orgEmpId;
	}

	public void setOrgEmpId(BigDecimal orgEmpId) {
		this.orgEmpId = orgEmpId;
	}

	public BigDecimal getOrgId() {
		return this.orgId;
	}

	public void setOrgId(BigDecimal orgId) {
		this.orgId = orgId;
	}

	public String getOrgRev() {
		return this.orgRev;
	}

	public void setOrgRev(String orgRev) {
		this.orgRev = orgRev;
	}

	public Date getTakeOfficeDate() {
		return this.takeOfficeDate;
	}

	public void setTakeOfficeDate(Date takeOfficeDate) {
		this.takeOfficeDate = takeOfficeDate;
	}

	public String getWorkTypeName() {
		return this.workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}

	public SwfOfficeDutyAll getDuty() {
		return duty;
	}

	public void setDuty(SwfOfficeDutyAll duty) {
		this.duty = duty;
	}

	public SwfDeptAll getDepartment() {
		return department;
	}

	public void setDepartment(SwfDeptAll department) {
		this.department = department;
	}

	public List<SwfDeptAll> getSwfDepartments() {
		return swfDepartments;
	}

	public void setSwfDepartments(List<SwfDeptAll> swfDepartments) {
		this.swfDepartments = swfDepartments;
	}

}