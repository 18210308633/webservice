package http.cliet;

import java.util.Map;

import org.apache.axis2.jaxws.util.SoapUtils;

import com.fsm.config.ConfigInfo;
import com.fsm.db.DBService;


public class HelloWorld extends WebServiceClientUtils{
	public static void sendAppealInfo(String name) {
		ConfigInfo configinfo = new ConfigInfo();
		DBService dbService = new DBService();
		// ���ʵ�Զ��URl
		String url = "http://192.168.5.248:7070/AXIS2/services/axisDemo?wsdl";
		String strXML = "";
		try {
			strXML = getSoapRequest(name);
			System.out.println(dbService.getDateTime()+"Ͷ����Ҫ�طõ�Ͷ�߹���������" + strXML);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// �����ռ�
		String targetNamespace = "myname";
		// ������
		String method = "http://192.168.5.248:7070/AXIS2/sayHello/";
		String returnXML = "";
		try {
			returnXML = Send(url, strXML, targetNamespace, method);
			System.out.println(dbService.getDateTime()+"��Ҫ�طõĹ�����Ϣ�������ķ��صı���" + strXML);
			// �����صı��ļ�¼����־��
			System.out.println(returnXML);
		} catch (Exception e) {
			System.out.println("���ʺ������Ľӿ�ʧ��");
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
		System.out.println("������--==--"+sb.toString());
         return sb.toString();
	}
	public static void main(String[] args) {
		String name="����";
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
