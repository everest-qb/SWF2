package sunspring.swf.rest;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import sunspring.swf.rest.client.SwfInfoClient;

public class SwfInfoClientTest {

	private SwfInfoClient client=new SwfInfoClient("http://hrdev.sunspring.com.tw:8080/swf/rest");;

	@Test
	public void testFindEmpl() {
		SwfEmployee e=client.findEmpl(new BigDecimal(968));
		assertNotNull(e);
	}

	@Test
	public void testFindEmplByNumber() {
		SwfEmployee e=client.findEmplByNumber("V410");
		assertNotNull(e);
	}

	@Test
	public void testFindAudit() {
	 	List<SwfEmployee> list=client.findAudit(new BigDecimal(968), new BigDecimal(3));
	 	assertTrue(list.size()>0);
	}

	@Test
	public void testFindApprover() {
		List<SwfEmployee> list=client.findApprover(new BigDecimal(968), new BigDecimal(2));

		assertTrue(list.size()>0);
	}

}
