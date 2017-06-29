package sunspring.swf.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SWF_DEPT_MEMBER database table.
 * 
 */
@Embeddable
public class SwfDeptMemberPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="DEPT_ID")
	private long deptId;

	@Column(name="EMP_ID")
	private long empId;

	public SwfDeptMemberPK() {
	}
	public long getDeptId() {
		return this.deptId;
	}
	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}
	public long getEmpId() {
		return this.empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SwfDeptMemberPK)) {
			return false;
		}
		SwfDeptMemberPK castOther = (SwfDeptMemberPK)other;
		return 
			(this.deptId == castOther.deptId)
			&& (this.empId == castOther.empId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.deptId ^ (this.deptId >>> 32)));
		hash = hash * prime + ((int) (this.empId ^ (this.empId >>> 32)));
		
		return hash;
	}
}