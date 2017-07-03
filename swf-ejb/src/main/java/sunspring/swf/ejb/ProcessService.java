package sunspring.swf.ejb;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.swf.SwfGlobal;
import sunspring.swf.core.ProcessTool;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemLineAll;
import sunspring.swf.jpa.SwfItemTxnAll;
import sunspring.swf.jpa.SwfStationAll;
import sunspring.annotation.LogTrace;

/**
 * @author QB
 */
@LogTrace
@Stateless
@LocalBean
@Transactional
public class ProcessService {
	
	private Logger log=LoggerFactory.getLogger(ProcessService.class);

	@PersistenceContext(unitName = "SWFunit")
	private EntityManager em;

	
	@Inject
	private ProcessTool pTool;
	
	/**
	 * 取得單號,格式為SWF20170629000278
	 * @return SWF單號
	 */
	public String genSWF_NO(){
		String returnValue;
		SimpleDateFormat swfHoFormat = new SimpleDateFormat("YYYYMMdd");
		Calendar date = Calendar.getInstance();
		Object no=em.createNativeQuery("Select lpad(SWF.SWF_ITEM_NO_S1.nextval,6,0) from dual")
				.getSingleResult();
		returnValue="SWF" + swfHoFormat.format(date.getTime())+no;
		return returnValue;
	}
	
	public SwfItemHdrAll findProcessById(BigDecimal hdrId){
		SwfItemHdrAll p= em.find(SwfItemHdrAll.class, hdrId);		
		return p;
	}
	
	public Map<BigDecimal,SwfItemTxnAll> findAllTaskByPid(BigDecimal hdrId){
		SwfItemHdrAll p= em.find(SwfItemHdrAll.class, hdrId);
		return p.getItemTxn();
	}
	
	public List<SwfItemLineAll> findAllVariavlesByPid(BigDecimal hdrId){
		SwfItemHdrAll p= em.find(SwfItemHdrAll.class, hdrId);
		return p.getItemLine();
	}
	
	/**
	 * @param itemTxnId 關卡節點的主鍵
	 * @return 由DB返回關卡節點
	 */
	public SwfItemTxnAll findTask(BigDecimal itemTxnId){
		return em.find(SwfItemTxnAll.class, itemTxnId);
	}
	
	public void delProcess(BigDecimal hdrId){
		SwfItemHdrAll p= em.find(SwfItemHdrAll.class, hdrId);
		em.remove(p);
	}
	
	
	/**
	 * 退回後如果是遇到辦理確認,再退回一次,目的是假設會退到辦理關卡
	 * @param txn 關卡節點
	 * @param emplId 執行人ID
	 */
	public void specialCaseGoBack(SwfItemTxnAll txn,BigDecimal emplId){
		goBack(txn, emplId);
		SwfItemHdrAll hdr=txn.getItemHdr();
		if(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE_CONFIRM))){
			for(SwfItemTxnAll t:hdr.getItemTxn().values()){
				if(t.getProcessFlag().intValue()==1){
					goBack(t, emplId);
					break;
				}
			}
		}
	}
	
	/**
	 * 驗收和辦理確認可以退回
	 * @param txn 關卡節點
	 * @param emplId 執行人ID
	 */
	public void goBack(SwfItemTxnAll txn,BigDecimal emplId){
		if(txn.getStation().getServiceActivityType().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_ACCEPT))||
				txn.getStation().getServiceActivityType().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE_CONFIRM))){
			Calendar cal=Calendar.getInstance();
			SwfItemHdrAll hdr=txn.getItemHdr();
			txn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WAITTING));
			txn.setProcessFlag(new BigDecimal(2));
			txn.setLastUpdateDate(cal.getTime());
			txn.setLastUpdatedBy(emplId);
			em.merge(txn);
			SwfItemTxnAll lastTxn=pTool.findTask(txn, txn.getLastTxnId());
			lastTxn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
			lastTxn.setProcessFlag(new BigDecimal(1));
			lastTxn.setLastUpdateDate(cal.getTime());
			lastTxn.setLastUpdatedBy(emplId);
			em.merge(lastTxn);
			hdr.setServiceActivityCode(lastTxn.getStation().getServiceActivityType());
			hdr.setLastUpdateDate(cal.getTime());
			hdr.setLastUpdatedBy(emplId);
			em.merge(hdr);
			log.debug("GoBack ID:{}-{}",hdr.getHdrId(),txn.getItemTxnId());
		}else{
			log.debug("GoBack not exec ID:{}",txn.getItemTxnId());
		}
	}
	
	/**
	 * 根據指定關卡結點執行簽核,如果之後連續關卡有自動,則無條件簽核
	 * @param txn  關卡節點
	 * @param emplId 執行人ID
	 * @param comment NULL為無條件同意,有值代表有條件同意
	 */
	public void approveWithAuto(SwfItemTxnAll txn,BigDecimal emplId,String comment){
		SwfItemTxnAll next=approve(txn,emplId,comment);
		while(next!=null){
			log.debug("NEXT AUTO PASS ID:{}",next.getItemTxnId());
			next=approve(next,new BigDecimal(-2),null);
		}
	}
	
	/**
	 * 根據指定關卡結點執行一次簽核
	 * @param txn 關卡節點
	 * @param emplId 執行人ID
	 * @param comment NULL為無條件同意,有值代表有條件同意
	 * @return 如果下一關為自動(含核決人已審核過),則返回下一關卡節點
	 */
	private SwfItemTxnAll approve(SwfItemTxnAll txn,BigDecimal emplId,String comment){
		if(txn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
			Calendar cal=Calendar.getInstance();				
			String type = comment == null ? String.valueOf(SwfGlobal.APPROVE_TYPE_AGREE)
					: String.valueOf(SwfGlobal.APPROVE_TYPE_CONDITION_AGREE);
			txn.setApproveType(type);
			txn.setProcessFlag(new BigDecimal(0));
			txn.setAttribute1(emplId.toString());
			if(comment!=null)
				txn.setApproveComment(comment);
			txn.setAttribute1(emplId.toString());
			txn.setApprovedDate(cal.getTime());
			txn.setLastUpdateDate(cal.getTime());			
			txn.setLastUpdatedBy(emplId);
			em.merge(txn);
			if(txn.getNextTxnId()==null){
				SwfItemHdrAll hdr=txn.getItemHdr();
				hdr.setServiceActivityCode(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CLOSE));
				hdr.setLastUpdateDate(cal.getTime());
				hdr.setLastUpdatedBy(emplId);
				hdr.setClosedDate(cal.getTime());
				hdr.setClosedBy(emplId);
				hdr.setApprovedBy(emplId);
				hdr.setApprovedDate(cal.getTime());
				log.debug("ID:{}-{} Approve Closed!",hdr.getHdrId(),txn.getItemTxnId());
				em.merge(hdr);
			}else{
				SwfItemHdrAll hdr=txn.getItemHdr();
				SwfItemTxnAll nextTxn=pTool.findTask(txn,txn.getNextTxnId());
				boolean isDeciderAuto=false;
				if(nextTxn.getStation().getServiceActivityType()
						.equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_APPROVE))){
					for(SwfItemTxnAll c:hdr.getItemTxn().values()){
						if(c.getProcessFlag().intValue()==0)
							if(c.getApproverId().longValue()==nextTxn.getApproverId().longValue())
								isDeciderAuto=true;
					}
				}
				
				nextTxn.setProcessFlag(new BigDecimal(1));
				nextTxn.setLastUpdateDate(cal.getTime());
				nextTxn.setApproveType(String.valueOf(SwfGlobal.APPROVE_TYPE_WORKING));
				em.merge(nextTxn);
				hdr.setServiceActivityCode(nextTxn.getStation().getServiceActivityType());
				hdr.setLastUpdateDate(cal.getTime());
				hdr.setLastUpdatedBy(emplId);
				em.merge(hdr);
				log.debug("ID:{}-{} Approved !",hdr.getHdrId(),txn.getItemTxnId());
				if(nextTxn.getApproverId().longValue()==-2 || isDeciderAuto){
					return nextTxn;
				}
				
			}
			
		}
		return null;
	}
	
	/**
	 * 根據關卡作廢流程
	 * @param txn 關卡節點
	 * @param emplId 執行人ID
	 */
	public void canecl(SwfItemTxnAll txn,BigDecimal emplId){
		if(txn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
			SwfStationAll station =txn.getStation();
			int type=Integer.parseInt(station.getServiceActivityType());
			if(SwfGlobal.AVTIVITY_TYPE_AUDIT==type || SwfGlobal.AVTIVITY_TYPE_APPROVE==type 
					|| SwfGlobal.AVTIVITY_TYPE_HANDLE==type ||SwfGlobal.AVTIVITY_TYPE_HANDLE_CONFIRM==type){				
				Calendar cal=Calendar.getInstance();
				txn.setApproveType(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CANCEL));
				txn.setProcessFlag(new BigDecimal(0));
				txn.setAttribute1(emplId.toString());
				txn.setLastUpdateDate(cal.getTime());
				txn.setLastUpdatedBy(emplId);
				SwfItemHdrAll hdr=txn.getItemHdr();
				for(SwfItemTxnAll other:hdr.getItemTxn().values()){
					if(other.getProcessFlag().intValue()==SwfGlobal.PROCESS_UNDO){
						other.setProcessFlag(new BigDecimal(0));
					}
				}
				hdr.setServiceActivityCode(String.valueOf(SwfGlobal.APPROVE_TYPE_CANECL));
				hdr.setLastUpdateDate(cal.getTime());
				hdr.setLastUpdatedBy(emplId);
				hdr.setClosedDate(cal.getTime());
				hdr.setClosedBy(emplId);
				log.debug("ID:{} Canceled!",hdr.getHdrId());
				em.merge(hdr);
			}			
		}else{
			log.debug("Cancle not exec ID:{}",txn.getItemTxnId());
		}
	}
	
	/**
	 * 送單
	 * @param rougnhHdr 未儲存的表頭
	 * @return 已受控管單據(表頭+流程關卡資料)
	 */
	public SwfItemHdrAll submit(SwfItemHdrAll rougnhHdr){
		em.persist(rougnhHdr);
		pTool.genTxn(rougnhHdr);
		return rougnhHdr;
	}
	

	

}
