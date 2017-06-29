package sunspring.swf.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sunspring.swf.core.ProcessTool;
import sunspring.swf.ejb.ProcessService;
import sunspring.swf.jpa.SwfEmpsAll;
import sunspring.swf.jpa.SwfItemHdrAll;
import sunspring.swf.jpa.SwfItemTxnAll;

@RunWith(Arquillian.class)
public class ProcessToolTest {

	@Inject
	private ProcessTool tool;
	@Inject
	private ProcessService pService;
	@PersistenceContext
	private EntityManager em;
	@Inject
	private UserTransaction utx;
	
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
	
	@Before
	public void preparePersistenceTest() throws Exception {
		utx.begin();
        em.joinTransaction();
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
	}
	
	@Test
	public void testGenTxn(){
		SwfItemHdrAll hdr=pService.findProcessById(new BigDecimal(2346));
		List<SwfItemTxnAll> list=tool.genTxn(hdr);
		assertTrue(list.size()>0);
		
		for(SwfItemTxnAll txn:list){
			hdr.getItemTxn().remove(txn.getItemTxnId());
			em.remove(txn);
		}
	}
	
	@Test
	public void testFindTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindSelf() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindApplicantTop() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindDeptTop() {
		fail("Not yet implemented");
	}

}
