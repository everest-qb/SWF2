package sunspring.swf.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 呼叫REST後的狀態回應
 * @author QB
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwfResponse {

	/**
	 * 執行成功
	 */
	public static final int RESPONSE_SUCCESS=1;
	/**
	 * 執行失敗
	 */
	public static final int RESPONSE_FAIL=2;
	/**
	 * 執行無錯誤,但不一定正常
	 */
	public static final int RESPONSE_WARN=3;
	
	/**
	 * 狀態
	 */
	private int type;
	/**
	 * 訊息,如果是submit而且成功,回傳HDR_ID
	 */
	private String message;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
