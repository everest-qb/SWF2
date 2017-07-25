package sunspring.swf.rest.client;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import sunspring.swf.rest.SwfEmployee;

/**
 * SWF employee and department REST client
 * @author QB
 *
 */
public class SwfInfoClient {

	/**
	 * REST base URL
	 * exp: http://localhost:8181/swf/rest
	 */
	private WebTarget webTarget;
	
	/**
	 * 建構子
	 * @param url REST base URL
	 * exp: http://localhost:8181/swf/rest
	 */
	public SwfInfoClient(String url) {
		super();
		webTarget= ClientBuilder.newClient().target(url+"/info");
	}
	
	
	/**
	 * 根據主建找到員工資料
	 * @param empId 員工主鍵
	 * @return 
	 * {@link SwfEmployee} 
	 */
	public SwfEmployee findEmpl(BigDecimal empId){
		SwfEmployee r=webTarget.path("emplID")
		.queryParam("id", empId.longValue())		
		.request(MediaType.APPLICATION_JSON)
		.get(SwfEmployee.class);
		return r;
	}
	
	/**
	 * 根據工號找到員工資料
	 * @param empNum 工號
	 * @return {@link SwfEmployee}
	 */
	public SwfEmployee findEmplByNumber(String empNum){
		SwfEmployee r=webTarget.path("emplNumber")
		.queryParam("id", empNum)		
		.request(MediaType.APPLICATION_JSON)
		.get(SwfEmployee.class);
		return r;
	}
	
	/**
	 * 列出審核人員資料,由小至大排列
	 * @param empId 員工主鍵
	 * @param topLevel 部門Level
	 * @return List {@link SwfEmployee} 
	 */
	public List<SwfEmployee> findAudit(BigDecimal empId,BigDecimal topLevel){
		List<SwfEmployee> r=webTarget.path("audit")
				.queryParam("id", empId.longValue())
				.queryParam("level", topLevel.longValue())
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<SwfEmployee>>(){});
		return r;
	}
	
	/**
	 * 列出核準人員資料
	 * @param empId 員工主鍵
	 * @param topLevel 部門Level
	 * @return  {@link SwfEmployee}
	 */
	public List<SwfEmployee> findApprover(BigDecimal empId,BigDecimal topLevel){
		List<SwfEmployee> r=webTarget.path("approver")
				.queryParam("id", empId.longValue())
				.queryParam("level", topLevel.longValue())
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<SwfEmployee>>(){});
		return r;
	
	}
}
