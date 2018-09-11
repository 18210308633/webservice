package http.cliet;

import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

/**
 * 
 * @username: 黄勇
 * @Description:使用axis1调用webservice
 * @date 2016-5-19 上午9:38:23
 * 
 */
public class WebServiceClientUtils {

	static final Logger logger = Logger.getLogger(WebServiceClientUtils.class);
    /**
     * 
      * @Title: Send
      * @Description: TODO
      * @username : 黄勇
      * @param @param url 请求地址
      * @param @param strXML 入参
      * @param @param targetNamespace 命名空间
      * @param @param method 请求方法
      * @return String    返回类型
      * @throws
     */
	public static String Send(String url, String strXML,
			String targetNamespace, String method) {
		Service service = new Service();
		Call call = null;
		String result = "";
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(url));
			// 使用SOAP Action响应
			call.setUseSOAPAction(true);
			// 超时时间设置
			call.setProperty(Call.CONNECTION_TIMEOUT_PROPERTY, new Integer(
					60 * 60000));
			call.setTimeout(new Integer(60 * 60000));
		} catch (Exception e) {
			logger.info("调用webservice接口出现异常!");
			System.out.println("请求webservice失败");
			e.printStackTrace();
		}
		// 设置命名空间和调用方法
		call.setOperationName(new QName(targetNamespace, method));
		// 参数名
		// 参数类型:String
		// 参数模式：'IN' or 'OUT'
		call.addParameter("strXML", Constants.XSD_STRING, ParameterMode.IN);
		// 返回字符类型
		call.setReturnType(XMLType.XSD_STRING);
		// 参数是数组，也就是参数个数
		System.out.println("请求报文--==--"+strXML);
		Object[] obj = new Object[] { strXML };
		try {
			result = call.invoke(obj).toString();
			System.out.println("返回报文----"+result);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("请求webservice失败");
		}
		return result;
	}

	

}
