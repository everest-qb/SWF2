package sunspring.swf.ejb;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sunspring.swf.SwfGlobal;
import sunspring.swf.core.ProcessTool;
import sunspring.swf.jpa.SwfEmpsAll;
import sunspring.swf.jpa.SwfItemApplAll;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemLineAll;
import sunspring.swf.jpa.SwfItemTxnAll;


@RunWith(Arquillian.class)
public class ProcessServiceTest {
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)				
				.addPackage(SwfEmpsAll.class.getPackage())
				.addPackage(ProcessService.class.getPackage()) 
				.addClass(ProcessTool.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("simplelogger.properties")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	private ProcessService service;
	
	@Test
	public void testSpecialCaseGoBack(){
		SwfItemHdrAll hdr=genProcess(new BigDecimal(17));
		int index=0;
		while(index <15 && !(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_ACCEPT)))){
			runApprove(hdr);
			index++;	
		}
		for(SwfItemTxnAll txn:hdr.getItemTxn().values()){
			if(txn.getProcessFlag().intValue()==1){
				service.specialCaseGoBack(txn, new BigDecimal(6799));
				break;
			}
		}
		assertTrue(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE)));	
	}
	
	@Test
	public void testGoBack(){
		SwfItemHdrAll hdr=genProcess(new BigDecimal(17));
		int index=0;
		while(index <15 && !(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_ACCEPT)))){
			runApprove(hdr);
			index++;	
		}
		for(SwfItemTxnAll txn:hdr.getItemTxn().values()){
			if(txn.getProcessFlag().intValue()==1){
				service.goBack(txn, new BigDecimal(6799));
				break;
			}
		}
		assertTrue(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_HANDLE_CONFIRM)));	
	}
	
	@Test
	public void testApproveWithAuto(){
		SwfItemHdrAll hdr=genProcess(new BigDecimal(46));
		int index=0;
		while(index <10 && !(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CLOSE)))){
			runApprove(hdr);
			index++;	
		}
		assertTrue(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CLOSE)));
	}
	
	private void runApprove(SwfItemHdrAll hdr){
		for(SwfItemTxnAll txn:hdr.getItemTxn().values()){
			if(txn.getProcessFlag().intValue()==1){
				service.approveWithAuto(txn, new BigDecimal(6799), System.currentTimeMillis()%2==0? null:"test comment");
				assertTrue(txn.getProcessFlag().intValue()==0);
				assertTrue(txn.getApproveType().equals(String.valueOf(SwfGlobal.APPROVE_TYPE_AGREE))
						||txn.getApproveType().equals(String.valueOf(SwfGlobal.APPROVE_TYPE_CONDITION_AGREE)));
				return  ;
			}
		}
	}
	
	@Test
	public void testCanecl(){
		SwfItemHdrAll hdr=genProcess(new BigDecimal(46));
		for(SwfItemTxnAll txn:hdr.getItemTxn().values()){
			if(txn.getProcessFlag().intValue()==1){
				service.canecl(txn, new BigDecimal(6799));
				assertTrue(hdr.getServiceActivityCode().equals(String.valueOf(SwfGlobal.AVTIVITY_TYPE_CANCEL)));
				return;
			}
		}
	}
	
	@Test
	public void testSubmit(){
		genProcess(new BigDecimal(46));
	}
	
	
	@Test
	public void testFindProcessById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllTaskByPid() {
		Map<BigDecimal, SwfItemTxnAll>  map= service.findAllTaskByPid(new BigDecimal(2346));
		assertTrue(!map.isEmpty());
		for(Entry<BigDecimal, SwfItemTxnAll> e:map.entrySet()){
			assertTrue(e.getKey().longValue()==e.getValue().getItemTxnId().longValue());
		}		
	}

	@Test
	public void testFindAllVariavlesByPid() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelProcess() {
		fail("Not yet implemented");
	}
	
	private SwfItemHdrAll genProcess(BigDecimal serviceId){

		Calendar cal =Calendar.getInstance();
		SwfItemHdrAll hdr=new SwfItemHdrAll();
		hdr.setServiceItemId(serviceId);
		hdr.setHdrNo(service.genSWF_NO());
		hdr.setRequestLevel(SwfGlobal.REQUEST_LEVEL_NORMAL);
		hdr.setServiceActivityCode(String.valueOf(SwfGlobal.AVTIVITY_TYPE_AUDIT));
		hdr.setProcessStatus("10");
		hdr.setAppliedDate(cal.getTime());
		hdr.setRequestEnableDate(cal.getTime());
		hdr.setRequestExpireDate(cal.getTime());
		hdr.setRequestExpireType("N");
		hdr.setPreparedBy(new BigDecimal(6799));
		hdr.setSubmittedDate(cal.getTime());
		hdr.setLastUpdateDate(cal.getTime());
		hdr.setCreationDate(cal.getTime());
		SwfItemApplAll app=new SwfItemApplAll();
		app.setSeq(new BigDecimal(1));
		app.setEmpId(new BigDecimal(6799));
		app.setLastUpdateDate(cal.getTime());
		app.setCreationDate(cal.getTime());
		app.setItemHdr(hdr);
		List<SwfItemApplAll> apps=new ArrayList<SwfItemApplAll>();
		apps.add(app);
		hdr.setItemAppl(apps);
		List<SwfItemLineAll> lines=new ArrayList<SwfItemLineAll>();
		if(serviceId.intValue() == 46) {//訪客單人申請
			SwfItemLineAll lin = new SwfItemLineAll();
			lin.setAttribute1("一般廠商");
			lin.setItemCompId(new BigDecimal(530));
			lin.setLastUpdateDate(cal.getTime());
			lin.setCreationDate(cal.getTime());
			lin.setItemHdr(hdr);
			lines.add(lin);
		}
		
		hdr.setItemLine(lines);
		return service.submit(hdr);
	
	}
	
}
