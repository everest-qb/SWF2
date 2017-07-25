package sunspring.swf.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * SwfOfficeDutyAll的REST對應
 * @author QB
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfDuty {

	private BigDecimal dutyId;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date enableDate;
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date disableDate;
	private BigDecimal jobLevel;
	private String positionCode;
	private String grades;
	
	public BigDecimal getDutyId() {
		return dutyId;
	}
	public void setDutyId(BigDecimal dutyId) {
		this.dutyId = dutyId;
	}
	public Date getEnableDate() {
		return enableDate;
	}
	public void setEnableDate(Date enableDate) {
		this.enableDate = enableDate;
	}
	public Date getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}
	public BigDecimal getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(BigDecimal jobLevel) {
		this.jobLevel = jobLevel;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getGrades() {
		return grades;
	}
	public void setGrades(String grades) {
		this.grades = grades;
	}
}
