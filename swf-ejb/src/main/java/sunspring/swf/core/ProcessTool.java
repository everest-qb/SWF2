package sunspring.swf.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.annotation.LogTrace;
import sunspring.swf.SwfGlobal;
import sunspring.swf.ejb.DepartmentService;
import sunspring.swf.ejb.EmployeeService;
import sunspring.swf.ejb.RuleResultVaue;
import sunspring.swf.ejb.RuleService;
import sunspring.swf.jpa.SwfDeptAll;
import sunspring.swf.jpa.SwfEmpsAll;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemTxnAll;

@LogTrace
@Named
@Transactional
public class ProcessTool {

	private Logger log=LoggerFactory.getLogger(ProcessTool.class);
	
	@Inject
	private DepartmentService deptService;
	@Inject
	private EmployeeService emplService;
	@Inject
	private RuleService ruleService;
	
	@PersistenceContext(unitName = "SWFunit")
	private EntityManager em;
	

	/**
	 * 產生流程節點關卡
	 * @param SwfItemHdrAll 表頭
	 * @return 流程關卡各個結點
	 */
	public List<SwfItemTxnAll> genTxn(SwfItemHdrAll hdr){
		List<SwfItemTxnAll> returnValue=new ArrayList<SwfItemTxnAll>();
		List<RuleResultVaue> rrvs=ruleService.findRuleResult(hdr);
		Calendar cal=Calendar.getInstance();		
		OneTxn one=new OneTxn();
		one.setApplayDone(false);
		one.setFirstTaskDone(false);
		one.setHdr(hdr);
		for(RuleResultVaue r:rrvs){	
			log.debug("Rule:{}-> Station ID:{} DEP:{} EMP:{} TOP:{}",r.getRuleType(),r.getStation().getStationId()
					,r.getDeptId(),r.getEmpId(),r.getTopLevel());
			one.setRule(r);
			if(r.getRuleType()==SwfGlobal.RULE_NONE ){
				genNoneTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}
			}
			if(r.getRuleType()==SwfGlobal.RULE_PASS){
				genPassTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}	
			}
			if(r.getRuleType()==SwfGlobal.RULE_SELF){
				genSelfTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}	
			}
			if(r.getRuleType()==SwfGlobal.RULE_SOME_ONE || r.getRuleType()==SwfGlobal.RULE2_SOME_ONE){
				genSomeOneTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}
			}
			if(r.getRuleType()==SwfGlobal.RULE_APPLICANT_TOP || r.getRuleType()==SwfGlobal.RULE2_APPLICANT_TOP){
				genApplicantTopTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}
			}
			if(r.getRuleType()==SwfGlobal.RULE_ASSIGN_DEPT_TOP || r.getRuleType()==SwfGlobal.RULE2_ASSIGN_DEPT_TOP){
				genAssignDepTopTxn(one);
				if(one.getTxns().size()>0){
					returnValue.addAll(one.getTxns());
					one.getTxns().clear();;
				}
			}
		}
		//必須儲存後有ID才能關聯各節點
		int index=1;
		Map<BigDecimal,BigDecimal> relation=new HashMap<BigDecimal,BigDecimal>();
		for(SwfItemTxnAll txn:returnValue){
			txn.setCreationDate(cal.getTime());
			txn.setLastUpdateDate(cal.getTime());
			if(index<returnValue.size()){
				txn.setNextTxnId(new BigDecimal(index+1));
			}
			if(index>1){
				txn.setLastTxnId(new BigDecimal(index-1));
			}
			em.persist(txn);
			relation.put(new BigDecimal(index), txn.getItemTxnId());
			log.debug("TXN Index:{} ID:{}",index,txn.getItemTxnId());
			index++;			
		}
		index=1;
		for(SwfItemTxnAll txn:returnValue){
			if(index<returnValue.size())
				txn.setNextTxnId(relation.get(txn.getNextTxnId()));			
			if(index>1)
				txn.setLastTxnId(relation.get(txn.getLastTxnId()));				
			em.merge(txn);
			index++;
			hdr.getItemTxn().put(txn.getItemTxnId(), txn);
		}
		return returnValue;
	}
	
	/**
	 * 利用關聯物件找尋關卡,而不是由DB撈取
	 * @param SwfItemTxnAll 關卡節點
	 * @param BigDecimal NULL表示找下一關 ,有值表示根據ID找到指定的
	 * @return 尋找的關卡節點
	 */
	public SwfItemTxnAll findTask(SwfItemTxnAll txn,BigDecimal txnId){
		Collection<SwfItemTxnAll> list=txn.getItemHdr().getItemTxn().values();
		if(txnId==null){
			txnId=txn.getNextTxnId();
		}
		for(SwfItemTxnAll returnValue:list){
			if(returnValue.getItemTxnId().longValue()==txnId.longValue())
				return returnValue;
		}
		return null;
	}
	
	/**
	 * 取得申請人ID(或者填單人)
	 * @param SwfItemHdrAll 表頭
	 * @return 申請人ID
	 */
	public BigDecimal findSelf(SwfItemHdrAll hdr){
		return hdr.getPreparedBy();
	}
	
	/**
	 * 根據申請人,取得簽核人部門的組織簽核人,如果是核准關卡只取最高部門
	 * @param SwfItemHdrAll 表頭
	 * @param RuleResultVaue 規則
	 * @return 簽核人ID 由低jbolevel先
	 */
	public List<BigDecimal> findApplicantTop(SwfItemHdrAll hdr,RuleResultVaue rrv){
		List<BigDecimal> returnValue=new ArrayList<BigDecimal>();
		if(rrv.getRuleType()==SwfGlobal.RULE2_APPLICANT_TOP || rrv.getRuleType()==SwfGlobal.RULE_APPLICANT_TOP){
			SwfEmpsAll applicant=emplService.find(hdr.getPreparedBy());
			SwfDeptAll userDept=applicant.getSwfDepartments().get(0);
			List<SwfDeptAll> deps=deptService.findHierarchy(userDept.getDeptCode());
			for(SwfDeptAll d:deps){				
				int depLevel=Integer.parseInt(d.getDeptLevelCode());
				if(depLevel>=rrv.getTopLevel().intValue()){
					if(rrv.getStation().getServiceActivityType().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_APPROVE))){
						if(depLevel>rrv.getTopLevel().intValue())
							continue;
					}
					
					List<SwfEmpsAll> emps=deptService.findManagers(d.getDeptId());
					if(userDept.getDeptId().longValue()==d.getDeptId().longValue()){ 
						for(SwfEmpsAll e:emps){
							if(e.getDuty().getJobLevel().intValue() > applicant.getDuty().getJobLevel().intValue()){
								returnValue.add(e.getEmpId());
								log.debug("Station:{} Add approval id:{} at same dept:{}."
										,rrv.getStation().getStationId(),e.getEmpId(),d.getDeptId());
							}else{
								log.debug("XX Approval job level:{} < applicant job level:{}",e.getDuty().getJobLevel().intValue(),applicant.getDuty().getJobLevel().intValue());
							}
						}
					}else{
						for(SwfEmpsAll e:emps){
							returnValue.add(e.getEmpId());
							log.debug("Station:{} Add approval id:{} dept:{}."
									,rrv.getStation().getStationId(),e.getEmpId(),d.getDeptId());
						}
					}
				}else{
					log.debug("XX  dep level:{} < top level:{}",depLevel,rrv.getTopLevel());
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 取得指定部門的組織簽核人,如果是核准關卡只取最高部門
	 * @param RuleResultVaue 規則
	 * @return 簽核人ID 由低jbolevel先
	 */
	public List<BigDecimal> findDeptTop(RuleResultVaue rrv){
		List<BigDecimal> returnValue=new ArrayList<BigDecimal>();
		if(rrv.getRuleType()==SwfGlobal.RULE2_ASSIGN_DEPT_TOP || rrv.getRuleType()==SwfGlobal.RULE_ASSIGN_DEPT_TOP){
			
			List<SwfDeptAll> deps=deptService.findHierarchy(deptService.find(rrv.getDeptId()).getDeptCode());
			for(SwfDeptAll d:deps){
				int depLevel=Integer.parseInt(d.getDeptLevelCode());
				if(depLevel>=rrv.getTopLevel().intValue()){
					if(rrv.getStation().getServiceActivityType().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_APPROVE))){
						if(depLevel>rrv.getTopLevel().intValue())
							continue;
					}
					List<SwfEmpsAll> emps=deptService.findManagers(d.getDeptId());
					for(SwfEmpsAll e:emps){
							returnValue.add(e.getEmpId());
							log.debug("Station:{} Add approval id:{} dept:{}."
									,rrv.getStation().getStationId(),e.getEmpId(),d.getDeptId());
					}
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 內含產生的新關卡節點和產生此節點時的狀態,下個節點會用狀態作判斷依據
	 * @author QB
	 */
	public class OneTxn{
		
		public OneTxn(){
			txns=new ArrayList<SwfItemTxnAll>();
		}
		
		/**
		 * 表示立案節點是否完成
		 */
		private boolean applayDone;
		/**
		 * 表示第一個節點(立案節點外的,未處理結點)是否完成
		 */
		private boolean firstTaskDone;
		/**
		 * 新的產生結點,用完後記得清空
		 */
		private List<SwfItemTxnAll> txns;
		private RuleResultVaue rule;
		private SwfItemHdrAll hdr;
		
		public boolean isApplayDone() {
			return applayDone;
		}
		public void setApplayDone(boolean applayDone) {
			this.applayDone = applayDone;
		}
		public boolean isFirstTaskDone() {
			return firstTaskDone;
		}
		public void setFirstTaskDone(boolean firstTaskDone) {
			this.firstTaskDone = firstTaskDone;
		}
		
		public RuleResultVaue getRule() {
			return rule;
		}
		public void setRule(RuleResultVaue rule) {
			this.rule = rule;
		}
		public SwfItemHdrAll getHdr() {
			return hdr;
		}
		public void setHdr(SwfItemHdrAll hdr) {
			this.hdr = hdr;
		}
		public List<SwfItemTxnAll> getTxns() {
			return txns;
		}
	}
	
	private OneTxn genNoneTxn(OneTxn one){
		SwfItemTxnAll txn=new SwfItemTxnAll();
		txn.setItemHdr(one.getHdr());
		txn.setStation(one.getRule().getStation());
		if(one.isApplayDone()){
			if(one.isFirstTaskDone()){
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
			}else{
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
				one.setFirstTaskDone(true);
			}
			txn.setApproverId(new BigDecimal(-1));
			log.debug("Approval empl ID:{}",-1);
			one.getTxns().add(txn);
		}
		return one;
	}
	
	private OneTxn genPassTxn(OneTxn one){
		SwfItemTxnAll txn=new SwfItemTxnAll();
		txn.setItemHdr(one.getHdr());
		txn.setStation(one.getRule().getStation());
		if(one.isApplayDone()){
			if(one.isFirstTaskDone()){
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
			}else{
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
				one.setFirstTaskDone(true);
			}
			txn.setApproverId(new BigDecimal(-2));
			log.debug("Approver empl ID:{}",-2);
			one.getTxns().add(txn);
		}
		return one;
	}
	
	private OneTxn genSelfTxn(OneTxn one){
		SwfItemTxnAll txn=new SwfItemTxnAll();
		txn.setItemHdr(one.getHdr());
		txn.setStation(one.getRule().getStation());
		if(one.isApplayDone()){
			if(one.isFirstTaskDone()){
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
			}else{
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
				one.setFirstTaskDone(true);
			}					
		}else{
			txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_APPLY));
			txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_DONE));
			Calendar cal=Calendar.getInstance();	
			txn.setApprovedDate(cal.getTime());
			txn.setLastTxnId(new BigDecimal(-1));
			one.setApplayDone(true);
		}
		txn.setApproverId(findSelf(one.getHdr()));
		log.debug("Approver empl ID:{}",findSelf(one.getHdr()));
		one.getTxns().add(txn);
		return one;
	}
	
	private OneTxn genSomeOneTxn(OneTxn one){
		SwfItemTxnAll txn=new SwfItemTxnAll();
		txn.setItemHdr(one.getHdr());
		txn.setStation(one.getRule().getStation());
		if(one.isApplayDone()){
			if(one.isFirstTaskDone()){
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
			}else{
				txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
				txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
				one.setFirstTaskDone(true);
			}
			txn.setApproverId(one.getRule().getEmpId());
			log.debug("Approver empl ID:{}",one.getRule().getEmpId());
			one.getTxns().add(txn);
		}
		return one;
	}
	
	private OneTxn genApplicantTopTxn(OneTxn one){
		List<BigDecimal> users= findApplicantTop(one.getHdr(),one.getRule());
		for(BigDecimal id:users){
			SwfItemTxnAll txn=new SwfItemTxnAll();
			txn.setItemHdr(one.getHdr());
			txn.setStation(one.getRule().getStation());
			if(one.isApplayDone()){
				if(one.isFirstTaskDone()){
					txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
					txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
				}else{
					txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
					txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
					one.setFirstTaskDone(true);
				}
				txn.setApproverId(id);
				one.getTxns().add(txn);
			}
		}
		return one;
	}
	
    private OneTxn genAssignDepTopTxn(OneTxn one){
		List<BigDecimal> users= findDeptTop(one.getRule());
		for(BigDecimal id:users){
			SwfItemTxnAll txn=new SwfItemTxnAll();
			txn.setItemHdr(one.getHdr());
			txn.setStation(one.getRule().getStation());
			if(one.isApplayDone()){
				if(one.isFirstTaskDone()){
					txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
					txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_UNDO));
				}else{
					txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
					txn.setProcessFlag(new BigDecimal(SwfGlobal.PROCESS_WAIT));
					one.setFirstTaskDone(true);
				}
				txn.setApproverId(id);
				one.getTxns().add(txn);
			}
		}
		return one;
	}
	
}
