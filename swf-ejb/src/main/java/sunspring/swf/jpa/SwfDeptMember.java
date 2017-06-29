package sunspring.swf.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SWF_DEPT_MEMBER database table.
 * 
 */
@Entity
@Table(name="SWF_DEPT_MEMBER",schema="SWF")
@NamedQuery(name="SwfDeptMember.findAll", query="SELECT s FROM SwfDeptMember s")
public class SwfDeptMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SwfDeptMemberPK id;

	@Column(name="MEMBER_TYPE")
	private String type;
	
	@Column(name="JOB_LEVEL")
	private long jobLevel; 

	public SwfDeptMember() {
	}

	public SwfDeptMemberPK getId() {
		return this.id;
	}

	public void setId(SwfDeptMemberPK id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(long jobLevel) {
		this.jobLevel = jobLevel;
	}

}