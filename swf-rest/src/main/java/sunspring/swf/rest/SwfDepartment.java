package sunspring.swf.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * SwfDeptAll的REST對應
 * @author QB
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfDepartment {

	private BigDecimal deptId;
	private String deptCode;
	private String deptLevelCode;
	private String deptName;
	private String deptShortCname;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date disableDate;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date enableDate;
	private BigDecimal parentDeptId;
	
	public BigDecimal getDeptId() {
		return deptId;
	}
	public void setDeptId(BigDecimal deptId) {
		this.deptId = deptId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptLevelCode() {
		return deptLevelCode;
	}
	public void setDeptLevelCode(String deptLevelCode) {
		this.deptLevelCode = deptLevelCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptShortCname() {
		return deptShortCname;
	}
	public void setDeptShortCname(String deptShortCname) {
		this.deptShortCname = deptShortCname;
	}
	public Date getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}
	public Date getEnableDate() {
		return enableDate;
	}
	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}
	public BigDecimal getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(BigDecimal parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

}
