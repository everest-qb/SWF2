package sunspring.swf.rest;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 將JSON的Date型態轉為Long,因為JSON沒有規範Date的表達形式,所以會不同
 * @author QB
 * 
 * @see Date#getTime
 */
public class DateXmlAdapter extends XmlAdapter<Long, Date> {
	
	@Override
	public Date unmarshal(Long v) throws Exception {
		
		return new Date(v);
	}

	@Override
	public Long marshal(Date v) throws Exception {
		
		return v.getTime();
	}

}
