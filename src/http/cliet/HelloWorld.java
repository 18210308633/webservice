package http.cliet;

import java.util.Map;

import org.apache.axis2.jaxws.util.SoapUtils;

import com.fsm.config.ConfigInfo;
import com.fsm.db.DBService;


public class HelloWorld extends WebServiceClientUtils{
	public static void sendAppealInfo(String name) {
		ConfigInfo configinfo = new ConfigInfo();
		DBService dbService = new DBService();
		// 访问的远程URl
		String url = "http://192.168.5.248:7070/AXIS2/services/axisDemo?wsdl";
		String strXML = "";
		try {
			strXML = getSoapRequest(name);
			System.out.println(dbService.getDateTime()+"投诉需要回访的投诉工单请求报文" + strXML);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 命名空间
		String targetNamespace = "myname";
		// 方法名
		String method = "http://192.168.5.248:7070/AXIS2/sayHello/";
		String returnXML = "";
		try {
			returnXML = Send(url, strXML, targetNamespace, method);
			System.out.println(dbService.getDateTime()+"需要回访的工单信息呼叫中心返回的报文" + strXML);
			// 将返回的报文记录到日志中
			System.out.println(returnXML);
		} catch (Exception e) {
			System.out.println("访问呼叫中心接口失败");
			e.printStackTrace();
		} finally {

		}

	}
	private static String getSoapRequest(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com\">"
				+ "<soapenv:Header/> "
				+ "<soapenv:Body>"
				+ "<com:sayHello>"
				+ "<com:soap>"+name+"</com:soap>"
				+ "</com:sayHello>"
				+ "</soapenv:Body></soapenv:Envelope>");
		System.out.println("请求报文--==--"+sb.toString());
         return sb.toString();
	}
	public static void main(String[] args) {
		String name="王信";
		String url = "http://localhost:8080/AXIS2/services/axisDemo?wsdl";
		String strXML = getSoapRequest(name);
		String method = "sayHello";
		String targetNamespace = "http://com";
		try {
		String	xmlString =Send(url, strXML, targetNamespace, method);
		System.out.println(xmlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
