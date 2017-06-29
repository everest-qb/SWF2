package sunspring.swf.ejb;

import java.math.BigDecimal;

import sunspring.swf.jpa.SwfStationAll;

public class RuleResultVaue {
	private int ruleType;
	private SwfStationAll station;
	private BigDecimal deptId;
	private BigDecimal topLevel;
	private BigDecimal empId;
	
	public RuleResultVaue(){
		
	}
	
	public int getRuleType() {
		return ruleType;
	}
	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}
	public BigDecimal getDeptId() {
		return deptId;
	}
	public void setDeptId(BigDecimal deptId) {
		this.deptId = deptId;
	}
	public BigDecimal getTopLevel() {
		return topLevel;
	}
	public void setTopLevel(BigDecimal topLevel) {
		this.topLevel = topLevel;
	}
	public BigDecimal getEmpId() {
		return empId;
	}
	public void setEmpId(BigDecimal empId) {
		this.empId = empId;
	}

	public SwfStationAll getStation() {
		return station;
	}

	public void setStation(SwfStationAll station) {
		this.station = station;
	}

}
