package sunspring.swf.rest;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ItemLin的REST對應
 * @author QB
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfDocLine {
	
	private BigDecimal lineId;
	private BigDecimal hdrId;
	private BigDecimal itemCompId;
	private String attribute1;
	public BigDecimal getLineId() {
		return lineId;
	}
	public void setLineId(BigDecimal lineId) {
		this.lineId = lineId;
	}
	public BigDecimal getHdrId() {
		return hdrId;
	}
	public void setHdrId(BigDecimal hdrId) {
		this.hdrId = hdrId;
	}
	public BigDecimal getItemCompId() {
		return itemCompId;
	}
	public void setItemCompId(BigDecimal itemCompId) {
		this.itemCompId = itemCompId;
	}
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
}
