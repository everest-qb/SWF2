package sunspring.swf.rest.client;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.swf.rest.SwfDoc;
import sunspring.swf.rest.SwfDocTxn;
import sunspring.swf.rest.SwfResponse;

public class SwfRestClient {
	private Logger log=LoggerFactory.getLogger(SwfRestClient.class);
	
	public static final String REST_SUBMIT="submit";
	public static final String REST_APPROVE="approve";
	public static final String REST_CANCEL="cancel";
	public static final String REST_GOBACK="goback";
	
	/**
	 * REST base URL
	 * exp: http://localhost:8181/swf/rest/exec
	 */
	private WebTarget webTarget;
	
	/**
	 * 作廢SWF,審核 核准 辦理 辦理確認 關卡才能作廢
	 * @param txn 關卡節點
	 * @throws RestError
	 */
	public void cancel(@NotNull SwfDocTxn txn) throws RestError,CheckError{
		if(txn.getApproverId()==null || txn.getHdrId()==null || txn.getItemTxnId()==null)
			throw new CheckError();
		SwfResponse response=webTarget.path("cancel")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
		if(response.getType()!=SwfResponse.RESPONSE_SUCCESS)
			throw new RestError();
	}
	
	/**
	 * 退回至辦理,只有驗收關卡才有作用
	 * @param txn 關卡節點
	 * @throws RestError
	 */
	public void goBack(@NotNull SwfDocTxn txn) throws RestError,CheckError{
		if(txn.getApproverId()==null || txn.getHdrId()==null || txn.getItemTxnId()==null)
			throw new CheckError();
		SwfResponse response=webTarget.path("goback")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
		if(response.getType()!=SwfResponse.RESPONSE_SUCCESS)
			throw new RestError();
	}
	
	/**
	 * 同意/有條件同意
	 * @param txn 關卡節點
	 * @throws RestError
	 */
	public void approve(@NotNull SwfDocTxn txn) throws RestError,CheckError{
		if(txn.getApproverId()==null || txn.getHdrId()==null || txn.getItemTxnId()==null)
			throw new CheckError();
		SwfResponse response=webTarget.path("approve")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
		if(response.getType()!=SwfResponse.RESPONSE_SUCCESS)
			throw new RestError();
	}
	
	/**
	 * 建立新的SWF
	 * @param doc 基本資料,可以想像為文件的基本資料
	 * @return hdrID swf的主鍵
	 * @throws RestError 
	 */
	public BigDecimal submit(@NotNull SwfDoc doc) throws RestError,CheckError{
		if(doc.getExecuterId()!=null || doc.getPreparedBy()!=null || doc.getServiceItemId()!=null)
			throw new CheckError();
		SwfResponse response=webTarget.path("submit")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(doc, MediaType.APPLICATION_JSON),SwfResponse.class);
		if(response.getType()==SwfResponse.RESPONSE_SUCCESS){
			return new BigDecimal(response.getMessage());
		}else{
			throw new RestError();
		}
	}
	
	public SwfDoc findSwfDoc(long hdrID){
		return webTarget.path("findDoc")
				.queryParam("id", hdrID)
				.request(MediaType.APPLICATION_JSON)
				.get(SwfDoc.class);
	}

	/**
	 * @param url REST base URL 
	 * exp: http://localhost:8181/swf/rest/exec
	 */
	public SwfRestClient(String url) {
		super();
		webTarget= ClientBuilder.newClient().target(url);
	}
}
