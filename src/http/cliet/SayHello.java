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
	 * 获取SOAP的请求头，并替换其中的标志符号为用户输入的城市
	 * 
	 * 编写者：王景辉
	 * 
	 * @param name
	 *            用户输入的人名
	 * @return 客户将要发送给服务器的SOAP请求
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
		System.out.println("请求报文--==--"+sb.toString());
         return sb.toString();
	}
	/**
	 * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
	 * 
	 * 编写者：王景辉
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 服务器端返回的输入流，供客户端读取
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String name) throws Exception {
		try {
			String soap = getSoapRequest(name);
			if (soap == null) {
				return null;
			}
			//创建url连接
			URL url = new URL("http://192.168.5.248:7070/AXIS2/services/axisDemo?wsdl");
			//　一旦你成功的创建了一个URL class的实例，你就可以对其进行操作了。但是在你能够访问这个URL上的资源和内容之前，你必须要打开到这些资源与内容上的连接。
			//你可以通过使用openConnection来完成这一操作。
			//打开url连接
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			//使用setDoOutput(boolean)方式把输出（Output）属性设置为真（true）来指定可以进行写操作的那些连接
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
	 * 对服务器端返回的XML进行解析
	 * 
	 * 编写者：王景辉
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 字符串 用,分割
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
				if(n.getFirstChild().getNodeValue().equals("查询结果为空！")) {
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
	 * 测试用
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(getWeather("王信"));

	}
}
