package sunspring.swf.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sunspring.swf.SwfGlobal;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemLineAll;
import sunspring.swf.jpa.SwfStationAll;
import sunspring.swf.jpa.SwfStationRuleAll;
import sunspring.swf.jpa.SwfStationTxnAll;
import sunspring.annotation.LogTrace;

@LogTrace
@Stateless
@LocalBean
public class RuleService {
	
	@PersistenceContext(unitName = "SWFunit")
	private EntityManager em;

	public SwfStationAll findStationByPk(BigDecimal stationId){
		return em.find(SwfStationAll.class, stationId);
	}
	
	public List<SwfStationRuleAll> findRuleByStationPK(BigDecimal stationId){
		List<SwfStationRuleAll> returnValue=new ArrayList<>();
		for(SwfStationTxnAll txn:findStationByPk(stationId).getSwfStationTxnAll()){
			returnValue.add(txn.getSwfStationRule());
		}	
		return returnValue;
	}
	
	public List<SwfStationAll> findAllByService(BigDecimal serviceId){
		return em.createQuery("SELECT s FROM SwfStationAll s WHERE s.serviceItemId=:ID ORDER BY s.seq", SwfStationAll.class)
		.setParameter("ID", serviceId)
		.getResultList();
	}
	
	
	/**
	 * 取得每個站點的規則物件
	 * @param 表頭
	 * @return 每個站點的規則物件
	 */
	public List<RuleResultVaue> findRuleResult(SwfItemHdrAll hdr){
		List<RuleResultVaue> returnValue= new ArrayList<RuleResultVaue>();
		SwfItemHdrAll p= hdr;
		if(!em.contains(hdr)){
			p=em.find(SwfItemHdrAll.class, hdr.getHdrId());
		}
		List<SwfItemLineAll> lins=p.getItemLine();
		List<SwfStationAll> list=findAllByService(p.getServiceItemId());
		
		for(SwfStationAll s:list){
			if(s.getEndTime()==null){
				if(s.getServiceActivityType().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_REGISTER))){
					RuleResultVaue rrv=new RuleResultVaue();
					rrv.setStation(s);
					rrv.setRuleType(SwfGlobal.RULE_SELF);
					returnValue.add(rrv);
				}else{
					returnValue.add(RuleSelect(lins, s));	
				}	
			}
		}		
		return returnValue;
	}
	
	/**
	 * 取得某站點的規則物件
	 * @param 輸入判斷參數
	 * @param 所有站點
	 * @return 規則物件
	 */
	private RuleResultVaue RuleSelect(List<SwfItemLineAll> list,SwfStationAll station){
		RuleResultVaue rrv=new RuleResultVaue();
		rrv.setStation(station);
		if(station.getSwfStationTxnAll().size()>0){
			List<SwfStationRuleAll> rList=new ArrayList<SwfStationRuleAll>();
			for(SwfStationTxnAll txn:station.getSwfStationTxnAll()){
				if(txn.getSwfStationRule().getEndTime()==null)
					rList.add(txn.getSwfStationRule());
			}
			rrv=rule2Select(matchRule2(list,rList));
			rrv.setStation(station);
			return rrv;
		}else if(station.getAutoFlag().toUpperCase().equals("Y")){
			rrv.setRuleType(SwfGlobal.RULE_PASS);
			return rrv;
		}else if(station.getDeptId()!=null && station.getTopLevel()!=null){
			rrv.setRuleType(SwfGlobal.RULE_ASSIGN_DEPT_TOP);
			rrv.setDeptId(station.getDeptId());
			rrv.setTopLevel(station.getTopLevel());
			return rrv;
		}else if(station.getTopLevel()!=null){
			rrv.setRuleType(SwfGlobal.RULE_APPLICANT_TOP);
			rrv.setTopLevel(station.getTopLevel());
			return rrv;
		}else if(station.getEmpId()!=null){
			rrv.setRuleType(SwfGlobal.RULE_SOME_ONE);
			rrv.setEmpId(station.getEmpId());
			return rrv;
		}else if(station.getAttribute15()!=null && station.getAttribute15().toUpperCase().equals("Y")){
			rrv.setRuleType(SwfGlobal.RULE_SELF);
			return rrv;
		}
		rrv.setRuleType(SwfGlobal.RULE_NONE);
		return rrv;
	}
	
	/**
	 * 判斷並取得規則條件(第二層)
	 * @param 輸入參數
	 * @param 站點的所有規則條件
	 * @return 規則條件
	 */
	private SwfStationRuleAll matchRule2(List<SwfItemLineAll> list,List<SwfStationRuleAll> rList){
		for(SwfItemLineAll line:list){
			for(SwfStationRuleAll r:rList){
				if(r.getItemCompId().compareTo(line.getItemCompId())==0 && r.getRuleValue().equals(line.getAttribute1())){
					return r;
				}
			}
		}
		return new SwfStationRuleAll();
	}
	
	/**
	 * 根據規則條件取得規則物件(第二層)
	 * @param 站點規則條件
	 * @return 規則物件
	 */
	private RuleResultVaue rule2Select(SwfStationRuleAll r){
		RuleResultVaue rrv=new RuleResultVaue();
		if(r.getAutoFlag().toUpperCase().equals("Y")){
			rrv.setRuleType(SwfGlobal.RULE_PASS);
			return rrv;
		}else if(r.getDeptId()!=null && r.getTopLevel()!=null){
			rrv.setRuleType(SwfGlobal.RULE2_ASSIGN_DEPT_TOP);
			rrv.setDeptId(r.getDeptId());
			rrv.setTopLevel(r.getTopLevel());
			return rrv;
		}else if(r.getTopLevel()!=null){
			rrv.setRuleType(SwfGlobal.RULE2_APPLICANT_TOP);
			rrv.setTopLevel(r.getTopLevel());
			return rrv;
		}else if(r.getEmpId()!=null){
			rrv.setRuleType(SwfGlobal.RULE2_SOME_ONE);
			rrv.setEmpId(r.getEmpId());
			return rrv;
		}else if(r.getAttribute15() !=null && r.getAttribute15().toUpperCase().equals("Y")){
			rrv.setRuleType(SwfGlobal.RULE_SELF);
			return rrv;
		}
		rrv.setRuleType(SwfGlobal.RULE_NONE);
		return rrv;
	}
}
