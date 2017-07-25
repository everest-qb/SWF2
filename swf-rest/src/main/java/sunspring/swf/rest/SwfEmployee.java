package sunspring.swf.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * SwfeEmplsAll的REST對應
 * @author QB
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfEmployee {

	private BigDecimal empId;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date disableDate;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date enableDate;
	private String emailAddress;
	private String empCname;
	private String empEname;
	private String empNum;
	private SwfDuty duty;
	private List<SwfDepartment> depts;
	
	public BigDecimal getEmpId() {
		return empId;
	}
	public void setEmpId(BigDecimal empId) {
		this.empId = empId;
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
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmpCname() {
		return empCname;
	}
	public void setEmpCname(String empCname) {
		this.empCname = empCname;
	}
	public String getEmpEname() {
		return empEname;
	}
	public void setEmpEname(String empEname) {
		this.empEname = empEname;
	}
	public String getEmpNum() {
		return empNum;
	}
	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}
	public SwfDuty getDuty() {
		return duty;
	}
	public void setDuty(SwfDuty duty) {
		this.duty = duty;
	}
	public List<SwfDepartment> getDepts() {
		return depts;
	}
	public void setDepts(List<SwfDepartment> depts) {
		this.depts = depts;
	}
}
