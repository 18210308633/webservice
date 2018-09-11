package http.cliet;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SayHello {
	/**
	 * ��ȡSOAP������ͷ�����滻���еı�־����Ϊ�û�����ĳ���
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param name
	 *            �û����������
	 * @return �ͻ���Ҫ���͸���������SOAP����
	 */
	private static String getSoapRequest(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com\">"
				+ "<soapenv:Header/> "
				+ " <soapenv:Body>"
				+ "<com:sayHello>"
				+ " <com:soap>"+name+"</com:soap>"
				+ "</com:sayHello>"
				+ "</soapenv:Body></soapenv:Envelope>");
		System.out.println("������--==--"+sb.toString());
         return sb.toString();
	}
	/**
	 * �û���SOAP�����͸��������ˣ������ط������㷵�ص�������
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param city
	 *            �û�����ĳ�������
	 * @return �������˷��ص������������ͻ��˶�ȡ
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String name) throws Exception {
		try {
			String soap = getSoapRequest(name);
			if (soap == null) {
				return null;
			}
			//����url����
			URL url = new URL("http://192.168.5.248:7070/AXIS2/services/axisDemo?wsdl");
			//��һ����ɹ��Ĵ�����һ��URL class��ʵ������Ϳ��Զ�����в����ˡ����������ܹ��������URL�ϵ���Դ������֮ǰ�������Ҫ�򿪵���Щ��Դ�������ϵ����ӡ�
			//�����ͨ��ʹ��openConnection�������һ������
			//��url����
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			//ʹ��setDoOutput(boolean)��ʽ�������Output����������Ϊ�棨true����ָ�����Խ���д��������Щ����
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction","http://192.168.5.248:7070/AXIS2/services/axisDemo/sayHello/");

			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();

			InputStream is = conn.getInputStream();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �Է������˷��ص�XML���н���
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param city
	 *            �û�����ĳ�������
	 * @return �ַ��� ��,�ָ�
	 */
	public static String getWeather(String name) {
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = getSoapInputStream(name);
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");
			StringBuffer sb = new StringBuffer();
			for (int count = 0; count < nl.getLength(); count++) {
				Node n = nl.item(count);
				if(n.getFirstChild().getNodeValue().equals("��ѯ���Ϊ�գ�")) {
					sb = new StringBuffer("#") ;
					break ;
				}
				sb.append(n.getFirstChild().getNodeValue() + "#\n");
			}
			is.close();	
			System.out.println(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ������
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(getWeather("����"));

	}
}
