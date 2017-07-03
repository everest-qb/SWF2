package sunspring.swf.jpa;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;


/**
 * The persistent class for the SWF_ITEM_HDR_ALL database table.
 * 
 */
@Entity
@Table(name="SWF_ITEM_HDR_ALL",schema="SWF")
@NamedQuery(name="SwfItemHdrAll.findAll", query="SELECT s FROM SwfItemHdrAll s")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfItemHdrAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPLIED_DATE")
	private Date appliedDate;

	@Column(name="APPROVED_BY")
	private BigDecimal approvedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPROVED_DATE")
	private Date approvedDate;

	@Column(name="ATTRIBUTE_CATEGORY")
	private String attributeCategory;

	private String attribute1;

	private String attribute10;

	private String attribute11;

	private String attribute12;

	private String attribute13;

	private String attribute14;

	private String attribute15;

	private String attribute2;

	private String attribute3;

	private String attribute4;

	private String attribute5;

	private String attribute6;

	private String attribute7;

	private String attribute8;

	private String attribute9;

	@Column(name="CLOSED_BY")
	private BigDecimal closedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLOSED_DATE")
	private Date closedDate;

	@Column(name="CREATED_BY")
	private BigDecimal createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	private String description;

	@Id
	@SequenceGenerator(name="SWF_ITEM_HDR_ALL_HDRID_GENERATOR", sequenceName="SWF_ITEM_HDR_ALL_S1",schema="SWF",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWF_ITEM_HDR_ALL_HDRID_GENERATOR")
	@Column(name="HDR_ID")
	private BigDecimal hdrId;

	@Column(name="HDR_NO")
	private String hdrNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private BigDecimal lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private BigDecimal lastUpdatedBy;

	@Column(name="PREPARED_BY")
	private BigDecimal preparedBy;

	@Column(name="PROCESS_STATUS")
	private String processStatus;

	/**
	 * 需求起始日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQUEST_ENABLE_DATE")
	private Date requestEnableDate;

	/**
	 * 需求結束日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQUEST_EXPIRE_DATE")
	private Date requestExpireDate;

	/**
	 * Y/N 是否到職務異動終止
	 */
	@Column(name="REQUEST_EXPIRE_TYPE")
	private String requestExpireType;

	@Column(name="REQUEST_LEVEL")
	private String requestLevel;

	@Column(name="SERVICE_ACTIVITY_CODE")
	private String serviceActivityCode;

	@Column(name="SERVICE_ITEM_ID")
	private BigDecimal serviceItemId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SUBMITTED_DATE")
	private Date submittedDate;
	
	@OneToMany(mappedBy="itemHdr", cascade = ALL, orphanRemoval = true)
	@MapKey(name="itemTxnId")
	private Map<BigDecimal,SwfItemTxnAll> itemTxn;
	
	@OneToMany(mappedBy="itemHdr", cascade = ALL, orphanRemoval = true)
	private List<SwfItemApplAll> itemAppl;
	
	@OneToMany(mappedBy="itemHdr", cascade = ALL, orphanRemoval = true)
	private List<SwfItemLineAll> itemLine;
	
	public SwfItemHdrAll() {
	}

	public Date getAppliedDate() {
		return this.appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}

	public BigDecimal getApprovedBy() {
		return this.approvedBy;
	}

	public void setApprovedBy(BigDecimal approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getAttributeCategory() {
		return this.attributeCategory;
	}

	public void setAttributeCategory(String attributeCategory) {
		this.attributeCategory = attributeCategory;
	}

	public String getAttribute1() {
		return this.attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute10() {
		return this.attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

	public String getAttribute11() {
		return this.attribute11;
	}

	public void setAttribute11(String attribute11) {
		this.attribute11 = attribute11;
	}

	public String getAttribute12() {
		return this.attribute12;
	}

	public void setAttribute12(String attribute12) {
		this.attribute12 = attribute12;
	}

	public String getAttribute13() {
		return this.attribute13;
	}

	public void setAttribute13(String attribute13) {
		this.attribute13 = attribute13;
	}

	public String getAttribute14() {
		return this.attribute14;
	}

	public void setAttribute14(String attribute14) {
		this.attribute14 = attribute14;
	}

	public String getAttribute15() {
		return this.attribute15;
	}

	public void setAttribute15(String attribute15) {
		this.attribute15 = attribute15;
	}

	public String getAttribute2() {
		return this.attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return this.attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return this.attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return this.attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public String getAttribute6() {
		return this.attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	public String getAttribute7() {
		return this.attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	public String getAttribute8() {
		return this.attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

	public String getAttribute9() {
		return this.attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}

	public BigDecimal getClosedBy() {
		return this.closedBy;
	}

	public void setClosedBy(BigDecimal closedBy) {
		this.closedBy = closedBy;
	}

	public Date getClosedDate() {
		return this.closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getHdrId() {
		return this.hdrId;
	}

	public void setHdrId(BigDecimal hdrId) {
		this.hdrId = hdrId;
	}

	public String getHdrNo() {
		return this.hdrNo;
	}

	public void setHdrNo(String hdrNo) {
		this.hdrNo = hdrNo;
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

	public BigDecimal getPreparedBy() {
		return this.preparedBy;
	}

	public void setPreparedBy(BigDecimal preparedBy) {
		this.preparedBy = preparedBy;
	}

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Date getRequestEnableDate() {
		return this.requestEnableDate;
	}

	public void setRequestEnableDate(Date requestEnableDate) {
		this.requestEnableDate = requestEnableDate;
	}

	public Date getRequestExpireDate() {
		return this.requestExpireDate;
	}

	public void setRequestExpireDate(Date requestExpireDate) {
		this.requestExpireDate = requestExpireDate;
	}

	public String getRequestExpireType() {
		return this.requestExpireType;
	}

	public void setRequestExpireType(String requestExpireType) {
		this.requestExpireType = requestExpireType;
	}

	public String getRequestLevel() {
		return this.requestLevel;
	}

	public void setRequestLevel(String requestLevel) {
		this.requestLevel = requestLevel;
	}

	public String getServiceActivityCode() {
		return this.serviceActivityCode;
	}

	public void setServiceActivityCode(String serviceActivityCode) {
		this.serviceActivityCode = serviceActivityCode;
	}

	public BigDecimal getServiceItemId() {
		return this.serviceItemId;
	}

	public void setServiceItemId(BigDecimal serviceItemId) {
		this.serviceItemId = serviceItemId;
	}

	public Date getSubmittedDate() {
		return this.submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}


	public List<SwfItemApplAll> getItemAppl() {
		return itemAppl;
	}

	public void setItemAppl(List<SwfItemApplAll> itemAppl) {
		this.itemAppl = itemAppl;
	}

	public List<SwfItemLineAll> getItemLine() {
		return itemLine;
	}

	public void setItemLine(List<SwfItemLineAll> itemLine) {
		this.itemLine = itemLine;
	}

	public Map<BigDecimal, SwfItemTxnAll> getItemTxn() {
		return itemTxn;
	}

	public void setItemTxn(Map<BigDecimal, SwfItemTxnAll> itemTxn) {
		this.itemTxn = itemTxn;
	}

}