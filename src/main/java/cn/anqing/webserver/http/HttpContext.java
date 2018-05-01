package cn.anqing.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	private static Map<String, String> MIME_MAPPING = new HashMap<String, String>();
	private static Map<Integer, String> STATUS_CODE_REASON_MAPPING = new HashMap<Integer, String>();
	
	static{
		initMimeMapping();
		initStatusCodeReasonMapping();
	}
	private static void initStatusCodeReasonMapping(){
		STATUS_CODE_REASON_MAPPING.put(200, "Ok");
		STATUS_CODE_REASON_MAPPING.put(302, "Moved Temporarily");
		STATUS_CODE_REASON_MAPPING.put(404, "Not Found");
		STATUS_CODE_REASON_MAPPING.put(500, "Internal Server Error");
	}
	private static void initMimeMapping(){
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> empEle = root.elements("mime-mapping");
			for(Element minEle : empEle){
				String key = minEle.elementText("extension");
				String value = minEle.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static String getStatusReason(int code){
		return STATUS_CODE_REASON_MAPPING.get(code);
		
	}
	public static String getMimeMapping(String name){
		return MIME_MAPPING.get(name);
		
	}
}
