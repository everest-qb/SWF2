package sunspring.swf.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfDoc {
	
	/**
	 * 執行人ID,客戶端到服務端必填,服務端返回填0
	 */
	@NotNull
	private BigDecimal executerId;
	private BigDecimal hdrId;
	private String hdrNo;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date submittedDate;
	private String description;
	@NotNull
	private String requestLevel;
	@NotNull
	private BigDecimal serviceItemId;
	private String serviceActivityCode;
	@NotNull
	private BigDecimal preparedBy;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date requestEnableDate;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date requestExpireDate;
	private String requestExpireType;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date creationDate;
	/**
	 * 目前等待處理的節點ID
	 */
	private BigDecimal nowItemTxnId;
	
	private List<SwfDocLine> lines;
	
	private List<SwfDocTxn> txns;
	
	public BigDecimal getHdrId() {
		return hdrId;
	}
	public void setHdrId(BigDecimal hdrId) {
		this.hdrId = hdrId;
	}
	public String getHdrNo() {
		return hdrNo;
	}
	public void setHdrNo(String hdrNo) {
		this.hdrNo = hdrNo;
	}
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRequestLevel() {
		return requestLevel;
	}
	public void setRequestLevel(String requestLevel) {
		this.requestLevel = requestLevel;
	}
	public BigDecimal getServiceItemId() {
		return serviceItemId;
	}
	public void setServiceItemId(BigDecimal serviceItemId) {
		this.serviceItemId = serviceItemId;
	}
	public String getServiceActivityCode() {
		return serviceActivityCode;
	}
	public void setServiceActivityCode(String serviceActivityCode) {
		this.serviceActivityCode = serviceActivityCode;
	}
	public BigDecimal getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(BigDecimal preparedBy) {
		this.preparedBy = preparedBy;
	}
	public Date getRequestEnableDate() {
		return requestEnableDate;
	}
	public void setRequestEnableDate(Date requestEnableDate) {
		this.requestEnableDate = requestEnableDate;
	}
	public String getRequestExpireType() {
		return requestExpireType;
	}
	public void setRequestExpireType(String requestExpireType) {
		this.requestExpireType = requestExpireType;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getRequestExpireDate() {
		return requestExpireDate;
	}
	public void setRequestExpireDate(Date requestExpireDate) {
		this.requestExpireDate = requestExpireDate;
	}
	public List<SwfDocLine> getLines() {
		return lines;
	}
	public void setLines(List<SwfDocLine> lines) {
		this.lines = lines;
	}
	public BigDecimal getExecuterId() {
		return executerId;
	}
	public void setExecuterId(BigDecimal executerId) {
		this.executerId = executerId;
	}
	public List<SwfDocTxn> getTxns() {
		return txns;
	}
	public void setTxns(List<SwfDocTxn> txns) {
		this.txns = txns;
	}
	public BigDecimal getNowItemTxnId() {
		return nowItemTxnId;
	}
	public void setNowItemTxnId(BigDecimal nowItemTxnId) {
		this.nowItemTxnId = nowItemTxnId;
	}
}
