package sunspring.swf.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfDocTxn {

	private BigDecimal itemTxnId;
	private BigDecimal hdrId;
	private BigDecimal approverId;
	private String approveComment;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date approvedDate;
	public BigDecimal getItemTxnId() {
		return itemTxnId;
	}
	public void setItemTxnId(BigDecimal itemTxnId) {
		this.itemTxnId = itemTxnId;
	}
	public BigDecimal getHdrId() {
		return hdrId;
	}
	public void setHdrId(BigDecimal hdrId) {
		this.hdrId = hdrId;
	}
	public BigDecimal getApproverId() {
		return approverId;
	}
	public void setApproverId(BigDecimal approverId) {
		this.approverId = approverId;
	}
	public String getApproveComment() {
		return approveComment;
	}
	public void setApproveComment(String approveComment) {
		this.approveComment = approveComment;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
}
