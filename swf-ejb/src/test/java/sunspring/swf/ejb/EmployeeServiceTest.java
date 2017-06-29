package sunspring.swf.ejb;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sunspring.swf.jpa.SwfEmpsAll;

@RunWith(Arquillian.class)
public class EmployeeServiceTest {

	
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)				
				.addPackage(SwfEmpsAll.class.getPackage())
				.addClass(EmployeeService.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	EmployeeService service;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindByNu() {
		assertNotNull(service.findByNu("V575"));
	}

	public void testFindByUpdate() throws Throwable{
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
		Date d=f.parse("20170401");
		assertNotNull(service.findByUpdate(d));
	}
	
}
