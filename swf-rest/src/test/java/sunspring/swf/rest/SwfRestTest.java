package sunspring.swf.rest;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.xmlmodel.ObjectFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.swf.rest.SwfDoc;


@RunWith(Arquillian.class)
public class SwfRestTest {
	private Logger log=LoggerFactory.getLogger(SwfRestTest.class);
	
	private Map<String, Object> properties;
	private JAXBContext jc;
	@Deployment
	
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)	
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Before
	public void setUp() throws Exception {
		properties = new HashMap<>();
		properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
		jc =JAXBContextFactory.createContext(new Class[]  {
			    		SwfDoc.class,ObjectFactory.class}, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSwfDoc() throws Exception {
		Calendar cal=Calendar.getInstance();
		SwfDoc doc=new SwfDoc();
		doc.setDescription("description test");
		doc.setHdrId(new BigDecimal(100));
		doc.setHdrNo("SWF20170629000001");
		doc.setPreparedBy(new BigDecimal(6637));
		doc.setRequestLevel("R");
		doc.setServiceActivityCode("10");
		doc.setServiceItemId(new BigDecimal(17));
		doc.setSubmittedDate(cal.getTime());
		List<SwfDocLine> lineList=new ArrayList<SwfDocLine>();
		doc.setLines(lineList);
		SwfDocLine line=new SwfDocLine();
		line.setAttribute1("廠商夥伴");
		line.setItemCompId(new BigDecimal(530));
		line.setHdrId(new BigDecimal(100));
		line.setLineId(new BigDecimal(1123));
		lineList.add(line);
		
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter w=new StringWriter();
		marshaller.marshal(doc,w);
		assertTrue(w.toString().length()>0);
		log.info(w.toString());
		/*Unmarshaller unmarshaller=jc.createUnmarshaller();
		unmarshaller.setAdapter(new DateXmlAdapter());
		StringReader reader=new StringReader(w.toString());
		SwfDoc r=(SwfDoc)unmarshaller.unmarshal(reader);
		assertTrue(r.getHdrNo().equals("SWF20170629000001"));*/
	}

}
