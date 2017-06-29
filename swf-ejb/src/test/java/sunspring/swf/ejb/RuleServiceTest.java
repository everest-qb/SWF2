package sunspring.swf.ejb;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import sunspring.swf.SwfGlobal;
import sunspring.swf.core.ProcessTool;
import sunspring.swf.jpa.SwfEmpsAll;
import sunspring.swf.jpa.SwfStationAll;
import sunspring.swf.jpa.SwfStationTxnAll;

@RunWith(Arquillian.class)
public class RuleServiceTest {

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)				
				.addPackage(SwfEmpsAll.class.getPackage())
				.addPackage(RuleService.class.getPackage())
				.addClass(ProcessTool.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	private RuleService service;
	@Inject
	private ProcessService process;
	
	@Test
	public void testFindAllStationByService() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindStationByPk() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindRuleByStationPK() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllByService() {
		List<SwfStationAll> rules=service.findAllByService(new BigDecimal(46));
		assertNotNull(rules);
		for(SwfStationAll r:rules){
			System.out.println("STATION ID:"+r.getStationId());
			for(SwfStationTxnAll txn:r.getSwfStationTxnAll()){
				System.out.println("RULE COMPO NAME/VALUE:"+txn.getSwfStationRule().getItemCompId()+" "+txn.getSwfStationRule().getRuleName()+
						" "+txn.getSwfStationRule().getRuleValue());
			}		
		}
	}

	@Test
	public void testfindRuleResult() {
		List<RuleResultVaue> list=service.findRuleResult(process.findProcessById(new BigDecimal(2346)));
		assertTrue(list.size()>0);
		for(RuleResultVaue r :list){
			String type="";
			switch(r.getRuleType()){
			case SwfGlobal.RULE_NONE:
				type="NONE";
				break;
			case SwfGlobal.RULE_PASS:
				type="PASS";
				break;
			case SwfGlobal.RULE_SELF:
				type="SELF";
				break;
			case SwfGlobal.RULE_APPLICANT_TOP:
				type="APPLICANT TOP";
				break;
			case SwfGlobal.RULE_SOME_ONE:
				type="SOME ONE";
				break;
			case SwfGlobal.RULE_ASSIGN_DEPT_TOP:
				type="DEPT TOP";
				break;
			case SwfGlobal.RULE2_APPLICANT_TOP:
				type="APPLICANT TOP2";
				break;
			case SwfGlobal.RULE2_SOME_ONE:
				type="SOME ONE2";
				break;
			case SwfGlobal.RULE2_ASSIGN_DEPT_TOP:
				type="DEPT TOP2";
				break;	
			}
			System.out.println(r.getStation().getStationId()+" :"+type);
			System.out.println("DEP:"+r.getDeptId()+" SOME_ONE:"+r.getEmpId()+" TOP:"+r.getTopLevel());
			System.out.println("==================");
		}
		
	}


}
