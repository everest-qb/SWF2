package sunspring.swf.rest.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.annotation.LogTrace;
import sunspring.swf.SwfGlobal;
import sunspring.swf.ejb.ProcessService;
import sunspring.swf.jpa.SwfItemApplAll;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemLineAll;
import sunspring.swf.jpa.SwfItemTxnAll;
import sunspring.swf.rest.SwfDoc;
import sunspring.swf.rest.SwfDocLine;
import sunspring.swf.rest.SwfDocTxn;
import sunspring.swf.rest.SwfResponse;

/**
 * SWF SERVER REST
 * @author QB
 *
 */
@LogTrace
@Path("exec")
public class Execute {
	private Logger log=LoggerFactory.getLogger(Execute.class);
	@Inject
	private ProcessService processService;
	
	@GET
	@Path("findDoc")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SwfDoc findSwfDoc(@NotNull @QueryParam("id") long hdrId){
		SwfDoc doc=new SwfDoc();
		doc.setTxns(new ArrayList<SwfDocTxn>());
		SwfItemHdrAll hdr=processService.findProcessById(new BigDecimal(hdrId));
		doc.setCreationDate(hdr.getCreationDate());
		doc.setDescription(hdr.getDescription());
		doc.setExecuterId(new BigDecimal(0));
		doc.setHdrId(hdr.getHdrId());
		doc.setHdrNo(hdr.getHdrNo());
		doc.setPreparedBy(hdr.getPreparedBy());
		doc.setRequestEnableDate(hdr.getRequestEnableDate());
		doc.setRequestExpireDate(hdr.getRequestExpireDate());
		doc.setRequestExpireType(doc.getRequestExpireType());
		doc.setRequestLevel(hdr.getRequestLevel());
		doc.setServiceActivityCode(hdr.getServiceActivityCode());
		doc.setServiceItemId(hdr.getServiceItemId());
		doc.setSubmittedDate(hdr.getSubmittedDate());
		doc.getTxns().addAll(converToSwfDocTxn(doc,hdr.getItemTxn()));
		return doc;
	}
	
	private List<SwfDocTxn> converToSwfDocTxn(SwfDoc doc,Map<BigDecimal, SwfItemTxnAll> map){
		List<SwfDocTxn> returnValue=new ArrayList<SwfDocTxn>();
		SwfItemTxnAll itemTxn=null;
		for(SwfItemTxnAll txnTmp:map.values()){//find first
			if(txnTmp.getLastTxnId().longValue()==-1){
				itemTxn=txnTmp;
				SwfDocTxn docTxn=new SwfDocTxn();
				docTxn.setApproveComment(itemTxn.getApproveComment());
				docTxn.setApproverId(itemTxn.getApproverId());
				docTxn.setHdrId(itemTxn.getItemHdr().getHdrId());
				docTxn.setItemTxnId(itemTxn.getItemTxnId());
				docTxn.setApprovedDate(itemTxn.getApprovedDate());
				returnValue.add(docTxn);
				break;
			}
		}
		while(itemTxn!=null && itemTxn.getNextTxnId()!=null){//loop next until null
			itemTxn=map.get(itemTxn.getNextTxnId());
			if(itemTxn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
				doc.setNowItemTxnId(itemTxn.getItemTxnId());
			}
			SwfDocTxn docTxn=new SwfDocTxn();
			docTxn.setApproveComment(itemTxn.getApproveComment());
			docTxn.setApproverId(itemTxn.getApproverId());
			docTxn.setHdrId(itemTxn.getItemHdr().getHdrId());
			docTxn.setItemTxnId(itemTxn.getItemTxnId());
			returnValue.add(docTxn);
		}
		return returnValue;
	}
	
	@POST
	@Path("submit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SwfResponse restSubmit(@NotNull SwfDoc doc){
		SwfResponse returnValue=new SwfResponse();
		try{
			SwfItemHdrAll hdr=submit(doc);
			if(hdr.getItemTxn().isEmpty()){
				returnValue.setType(SwfResponse.RESPONSE_WARN);
			}else{
				returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
			}
			returnValue.setMessage(""+hdr.getHdrId().longValue());
		}catch (Exception e) {
			returnValue.setType(SwfResponse.RESPONSE_FAIL);
			returnValue.setMessage(e.getMessage());
			log.error("Rest Submit Error!", e.fillInStackTrace());
		}
		return returnValue;
	}
	
	@Transactional(value=TxType.REQUIRED)
	private SwfItemHdrAll  submit(SwfDoc doc){
		Calendar cal =Calendar.getInstance();
		SwfItemHdrAll hdr=new SwfItemHdrAll();
		hdr.setServiceItemId(doc.getServiceItemId());
		hdr.setHdrNo(processService.genSWF_NO());
		hdr.setRequestLevel(doc.getRequestLevel());
		hdr.setServiceActivityCode(String.valueOf(SwfGlobal.AVTIVITY_TYPE_AUDIT));
		hdr.setProcessStatus("10");
		hdr.setRequestEnableDate(doc.getRequestEnableDate());
		hdr.setRequestExpireDate(doc.getRequestExpireDate());
		hdr.setRequestExpireType(doc.getRequestExpireType());	
		hdr.setPreparedBy(doc.getPreparedBy());
		hdr.setSubmittedDate(cal.getTime());
		hdr.setLastUpdateDate(cal.getTime());
		hdr.setCreationDate(doc.getCreationDate());
		hdr.setDescription(doc.getDescription());
		SwfItemApplAll app=new SwfItemApplAll();
		app.setSeq(new BigDecimal(1));
		app.setEmpId(doc.getPreparedBy());
		app.setLastUpdateDate(cal.getTime());
		app.setCreationDate(doc.getCreationDate());
		app.setItemHdr(hdr);
		List<SwfItemApplAll> apps=new ArrayList<SwfItemApplAll>();
		apps.add(app);
		hdr.setItemAppl(apps);
		List<SwfItemLineAll> lines=new ArrayList<SwfItemLineAll>();
		if(doc.getLines()!=null){
			for(SwfDocLine docLin:doc.getLines()){
				SwfItemLineAll lin = new SwfItemLineAll();
				lin.setAttribute1(docLin.getAttribute1());
				lin.setItemCompId(docLin.getItemCompId());
				lin.setLastUpdateDate(cal.getTime());
				lin.setCreationDate(cal.getTime());
				lin.setItemHdr(hdr);
				lines.add(lin);
			}
		}
		hdr.setItemLine(lines);
		return processService.submit(hdr);
	}
	
	@POST
	@Path("approve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SwfResponse resAapprove(@NotNull SwfDocTxn txn){
		SwfResponse returnValue=new SwfResponse();
		try{
			approve(txn);
			returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		}catch(RestWarnException e){
			returnValue.setType(SwfResponse.RESPONSE_WARN);
			returnValue.setMessage(e.getMessage());
			log.warn("Rest Approve Error!:{}", txn.getItemTxnId(),e.fillInStackTrace());
		}catch (Exception e) {
			returnValue.setType(SwfResponse.RESPONSE_FAIL);
			returnValue.setMessage(e.getMessage());
			log.error("Rest Approve Error!:{}", txn.getItemTxnId(), e.fillInStackTrace());
		}
		
		returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		return returnValue;
	}
	
	@Transactional(value=TxType.REQUIRED)
	private void approve(SwfDocTxn txn) throws RestWarnException{
		SwfItemTxnAll itemTxn=processService.findTask(txn.getItemTxnId());
		if(itemTxn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
			processService.approveWithAuto(itemTxn, txn.getApproverId(), txn.getApproveComment());
		}else{
			throw new RestWarnException();
		}
	}
	
	@POST
	@Path("cancel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SwfResponse restCancel(@NotNull SwfDocTxn txn){
		SwfResponse returnValue=new SwfResponse();
		try{
			cancel(txn);
			returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		}catch(RestWarnException e){
			returnValue.setType(SwfResponse.RESPONSE_WARN);
			returnValue.setMessage(e.getMessage());
			log.warn("Rest Cancel Error!:{}", txn.getItemTxnId() ,e.fillInStackTrace());
		}catch (Exception e) {
			returnValue.setType(SwfResponse.RESPONSE_FAIL);
			returnValue.setMessage(e.getMessage());
			log.error("Rest Cancel Error!:{}", txn.getItemTxnId(), e.fillInStackTrace());
		}
		
		returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		return returnValue;
	}
	
	@Transactional(value=TxType.REQUIRED)
	private void cancel(SwfDocTxn txn) throws RestWarnException{
		SwfItemTxnAll itemTxn=processService.findTask(txn.getItemTxnId());
		if(itemTxn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
			processService.canecl(itemTxn, txn.getApproverId());
		}else{
			throw new RestWarnException();
		}
	}
	
	@POST
	@Path("goback")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SwfResponse restBcak(@NotNull SwfDocTxn txn){
		SwfResponse returnValue=new SwfResponse();
		try{
			goBack(txn);
			returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		}catch(RestWarnException e){
			returnValue.setType(SwfResponse.RESPONSE_WARN);
			returnValue.setMessage(e.getMessage());
			log.warn("Rest Back Error!:{}", txn.getItemTxnId(), e.fillInStackTrace());
		}catch (Exception e) {
			returnValue.setType(SwfResponse.RESPONSE_FAIL);
			returnValue.setMessage(e.getMessage());
			log.error("Rest Back Error!:{}", txn.getItemTxnId(), e.fillInStackTrace());
		}
		
		returnValue.setType(SwfResponse.RESPONSE_SUCCESS);
		return returnValue;
	}
	
	private void goBack(SwfDocTxn txn) throws RestWarnException{
		SwfItemTxnAll itemTxn=processService.findTask(txn.getItemTxnId());
		if(itemTxn.getProcessFlag().intValue()==SwfGlobal.PROCESS_WAIT){
			processService.specialCaseGoBack(itemTxn, txn.getApproverId());
		}else{
			throw new RestWarnException();
		}
	}
}
