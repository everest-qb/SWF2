package sunspring.swf.rest;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.swf.SwfGlobal;
import sunspring.swf.core.ProcessTool;
import sunspring.swf.ejb.RuleService;
import sunspring.swf.jpa.SwfEmpsAll;



@RunWith(Arquillian.class)
public class ExecuteTest {
	private Logger log=LoggerFactory.getLogger(ExecuteTest.class);
	
	@Deployment(testable=false)
	public static EnterpriseArchive  createDeployment() {
		WebArchive war= ShrinkWrap.create(WebArchive.class)
				.addPackage(Execute.class.getPackage())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)				
		.addPackage(SwfEmpsAll.class.getPackage())
		.addPackage(RuleService.class.getPackage())
		.addClass(ProcessTool.class)
		.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
		.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		 return  ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsModule(jar).addAsModule(war);
	}
	
	private WebTarget webTarget;
	private String RESOURCE_PREFIX;
	
	public static BigDecimal hdrId;
	
	@Before
	public void init(){
		RESOURCE_PREFIX = RestConf.class.getAnnotation(ApplicationPath.class).value();
		Client client = ClientBuilder.newClient();
		webTarget= client.target(base.toString()+RESOURCE_PREFIX+"/exec");
		log.debug("URL:{}",base.toString()+RESOURCE_PREFIX+"/exec");
	}

	@ArquillianResource 
	private URL base;

	@Test
	@RunAsClient
	@InSequence(5)
	public void testFindSwfDoc(){
		SwfDoc doc=findSwfDoc(2388);
		assertTrue(doc.getTxns().size()>0);
		log.debug("Now ID:{}",doc.getNowItemTxnId());
		for(SwfDocTxn txn:doc.getTxns()){
			log.debug("{} - Approver:{} {}",txn.getItemTxnId(),txn.getApproverId()
					,txn.getItemTxnId().longValue()==doc.getNowItemTxnId().longValue() ? "Y":"N");
		}
	}
	
	private SwfDoc findSwfDoc(long hdrID){
		return webTarget.path("findDoc")
				.queryParam("id", hdrID)
				.request(MediaType.APPLICATION_JSON)
				.get(SwfDoc.class);
	}
	
	@Test
	@RunAsClient
	@InSequence(1)
	public void testSubmit() {
		Calendar cal =Calendar.getInstance();
		SwfDoc doc =new SwfDoc();
		doc.setExecuterId(new BigDecimal(6799));
		doc.setPreparedBy(new BigDecimal(6799));
		doc.setRequestEnableDate(cal.getTime());
		doc.setRequestExpireType("Y");
		doc.setRequestLevel("R");
		doc.setServiceItemId(new BigDecimal(17));
		doc.setSubmittedDate(cal.getTime());
		cal.add(Calendar.HOUR, -2);
		doc.setCreationDate(cal.getTime());
		doc.setDescription("測試送單");
		SwfResponse response=webTarget.path("submit")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(doc, MediaType.APPLICATION_JSON),SwfResponse.class);
		assertTrue(response.getType()==SwfResponse.RESPONSE_SUCCESS
				||response.getType()==SwfResponse.RESPONSE_WARN);
		
		ExecuteTest.hdrId=new BigDecimal(response.getMessage());
		log.debug("ID:{}",hdrId);
	}

	@Test
	@RunAsClient
	@InSequence(2)
	public void testApprove() {
		SwfDoc doc=findSwfDoc(hdrId.longValue());
		List<SwfDocTxn> txnList=doc.getTxns();
		for(int i=1;i<9;i++){
			SwfDocTxn txn=txnList.get(i);
			txn.setHdrId(hdrId);
			txn.setApproverId(new BigDecimal(6799));
			SwfResponse response=webTarget.path("approve")
			.request(MediaType.APPLICATION_JSON)
			.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
			assertTrue(response.getType()==SwfResponse.RESPONSE_SUCCESS);
		}
		SwfDoc d=findSwfDoc(hdrId.longValue());
		assertTrue(d.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_ACCEPT)));
	}

	@Test
	@RunAsClient
	@InSequence(3)
	public void testBcak() {
		SwfDoc doc=findSwfDoc(hdrId.longValue());
		if(doc.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_ACCEPT))){
			SwfDocTxn txn =new SwfDocTxn();
			txn.setHdrId(hdrId);
			txn.setItemTxnId(doc.getNowItemTxnId());
			txn.setApproverId(new BigDecimal(6799));
			SwfResponse response=webTarget.path("goback")
			.request(MediaType.APPLICATION_JSON)
			.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
			assertTrue(response.getType()==SwfResponse.RESPONSE_SUCCESS);
		}
		SwfDoc d=findSwfDoc(hdrId.longValue());
		assertTrue(d.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE)));
	}
	
	@Test
	@RunAsClient
	@InSequence(4)
	public void testCancel() {
		SwfDoc doc=findSwfDoc(hdrId.longValue());
		if(doc.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE))){
			SwfDocTxn txn =new SwfDocTxn();
			txn.setApproveComment("TEST");
			txn.setHdrId(hdrId);
			txn.setItemTxnId(doc.getNowItemTxnId());
			txn.setApproverId(new BigDecimal(6799));
			SwfResponse response=webTarget.path("cancel")
			.request(MediaType.APPLICATION_JSON)
			.post(Entity.entity(txn, MediaType.APPLICATION_JSON),SwfResponse.class);
			assertTrue(response.getType()==SwfResponse.RESPONSE_SUCCESS);
		}
		SwfDoc d=findSwfDoc(hdrId.longValue());
		assertTrue(d.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CANCEL)));
	}

}
