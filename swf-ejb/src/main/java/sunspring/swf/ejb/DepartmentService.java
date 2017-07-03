package sunspring.swf.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.annotation.LogTrace;
import sunspring.swf.SwfGlobal;
import sunspring.swf.jpa.SwfDeptAll;
import sunspring.swf.jpa.SwfDeptMember;
import sunspring.swf.jpa.SwfEmpsAll;


@LogTrace
@Stateless
@LocalBean
@Transactional
public class DepartmentService {
	private Logger log=LoggerFactory.getLogger(DepartmentService.class);

	@PersistenceContext(unitName = "SWFunit")
	private EntityManager em;

	public SwfDeptAll find(BigDecimal deptId){
		return em.find(SwfDeptAll.class, deptId);
	}
	
	public List<SwfEmpsAll> findManagers(BigDecimal pk){
		List<SwfDeptMember> list=em.createQuery("SELECT dm FROM SwfDeptMember dm WHERE dm.id.deptId=:DEPTID AND dm.type<>'N' ORDER BY dm.jobLevel", SwfDeptMember.class)
				.setParameter("DEPTID", pk)
				.getResultList();
		List<SwfEmpsAll> returnValue=new ArrayList<SwfEmpsAll>();

		for(SwfDeptMember dm:list){
			SwfEmpsAll emp=em.find(SwfEmpsAll.class, new BigDecimal(dm.getId().getEmpId()));
			/*if(dm.getType().equals(SwfGlobal.DMEMBER_TYPE_MASTER)){
				emp.setManagerFlag(SwfGlobal.DMEMBER_TYPE_MASTER);
			}else if(dm.getType().equals(SwfGlobal.DMEMBER_TYPE_SLAVE)){
				emp.setManagerFlag(SwfGlobal.DMEMBER_TYPE_SLAVE);
			}else if(dm.getType().equals(SwfGlobal.DMEMBER_TYPE_PLURALISM)){
				emp.setManagerFlag(SwfGlobal.DMEMBER_TYPE_PLURALISM);
			}
			emp.setManagerSeq(new BigDecimal(dm.getJobLevel()));*/
			returnValue.add(emp);
			log.debug("Managers ID:{} Type:{} ",emp.getEmpId(),dm.getType(),dm.getJobLevel());
		}
		return returnValue;		
	}
	
	public SwfEmpsAll findMajarManager(BigDecimal pk){
		SwfDeptMember dm= em.createQuery("SELECT dm FROM SwfDeptMember dm WHERE dm.id.deptId=:DEPTID AND dm.type=:TYPE", SwfDeptMember.class)
		.setParameter("DEPTID", pk)
		.setParameter("TYPE", SwfGlobal.DMEMBER_TYPE_MASTER)
		.getSingleResult();
		SwfEmpsAll emp=em.find(SwfEmpsAll.class, new BigDecimal(dm.getId().getEmpId()));
		//emp.setManagerFlag(SwfGlobal.DMEMBER_TYPE_MASTER);
		return emp;
	}
	
	public SwfDeptAll findByCode(String code){
		return em.createQuery("SELECT d FROM SwfDeptAll d WHERE d.deptCode=:DEP_CODE", SwfDeptAll.class)
		.setParameter("DEP_CODE", code)
		.getSingleResult();
	}
	
	public List<SwfEmpsAll> findEmplOfDeptCode(String code){
		return findByCode(code).getEmployees();
	}
	
	public List<SwfEmpsAll> findSWFEmplOfDeptCode(String code){
		return findByCode(code).getSwfEmployees();
	}
	
	public List<SwfDeptAll> findHierarchy(String code){
		List<SwfDeptAll> list=findHierarchy(findByCode(code).getDeptId());
		List<SwfDeptAll> returnValue=new ArrayList<SwfDeptAll>();
		for(int i=list.size()-1;i>=0;i--){
			returnValue.add(list.get(i));
		}
		return returnValue;
	}
	
	private List<SwfDeptAll> findHierarchy(BigDecimal depId){
		if(depId==null)
			return new ArrayList<SwfDeptAll>();
		SwfDeptAll dep=em.find(SwfDeptAll.class, depId);
		if(dep==null){
			return new ArrayList<SwfDeptAll>();
		}else{
			List<SwfDeptAll> r=findHierarchy(dep.getParentDeptId());
			r.add(dep);
			return r;
		}	
	}
	
    public void clearCache(){
    	Cache cache=em.getEntityManagerFactory().getCache();
    	cache.evict(SwfEmpsAll.class);
    }
}
