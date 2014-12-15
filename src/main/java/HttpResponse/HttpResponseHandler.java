package HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class HttpResponseHandler {
	private static Logger log = Logger.getLogger(HttpResponseHandler.class);
	
	/**
	 * 获取一般Inputstream的返回码
	 * @param normalResponse
	 * @return
	 */
	public static int getServerReturnCodeInputStream(HttpResponse<InputStream> inputStreamResponse){
		int returnCode = 0;
		if(inputStreamResponse != null){
			returnCode = inputStreamResponse.getCode();
		}else{
			log.debug("服务端响应异常，无法获取服务端的响应码！");
			log.error("服务端响应异常，无法获取服务端的响应码！");
		}
		return returnCode;
	}
	
	/**
	 * 获取通过JSON格式转换后的返回码
	 * @param jsonResponse
	 * @return
	 */
	public static int getServerReturnCodeJson(HttpResponse<JsonNode> jsonResponse){
		int returnCode = 0;
		if(jsonResponse != null){
			returnCode = jsonResponse.getCode();
		}else{
			log.debug("服务端响应异常，无法获取服务端的响应码！");
			log.error("服务端响应异常，无法获取服务端的响应码！");
		}
		return returnCode;
	}
	
	/**
	 * 获取String转换格式后的返回码
	 * @param stringResponse
	 * @return
	 */
	public static int getServerReturnCodeString(HttpResponse<String> stringResponse){
		int returnCode = 0;
		if(stringResponse != null){
			returnCode = stringResponse.getCode();
		}else{
			log.debug("服务端响应异常，无法获取服务端的响应码！");
			log.error("服务端响应异常，无法获取服务端的响应码！");
		}
		return returnCode;
	}
	
	/**
	 * 打印InputStream格式的返回消息
	 * @param inputStreamResponse
	 */
	public static void printResponseInputStream(HttpResponse<InputStream> inputStreamResponse){
		InputStreamReader inPutReader = new InputStreamReader(inputStreamResponse.getRawBody());
		BufferedReader reader = new BufferedReader(inPutReader);		
		try {
			System.out.println(new String(reader.readLine()));
		} catch (IOException e) {
			log.debug("打印服务端响应内容时遇到了问题，err: "+e.getLocalizedMessage());
			log.error("打印服务端响应内容时遇到了问题，err: "+e.getLocalizedMessage());
		}
	}
	
	/**
	 * 打印String格式的返回消息
	 * @param stringResponse
	 */
	public static void printResponseString(HttpResponse<String> stringResponse){		
		try {
			System.out.println(stringResponse.getBody());
		} catch (Exception e) {
			log.debug("打印服务端响应内容时遇到了问题，err: "+e.getLocalizedMessage());
			log.error("打印服务端响应内容时遇到了问题，err: "+e.getLocalizedMessage());
		}
	}
	
	/**
	 * 因为JD这面的服务端返回会带上一个回调函数体，而函数体内部才是一个标准的JSON流格式
	 * 所以在此需要做一次特殊处理因为JD这面的返回会带上一个回调函数体，而函数体内部才是一个标准的JSON流格式
	 * @param stringResponse
	 * @param callBackMethodName
	 * @return
	 */
	public static String specialHandle(HttpResponse<String> stringResponse,String callBackMethodName){
		String finalResponseStr = "";
		if(stringResponse != null){
			String rawResponseStr = stringResponse.getBody();
			if(rawResponseStr.startsWith(callBackMethodName)){
//截掉回调函数部分
				finalResponseStr = rawResponseStr.substring(callBackMethodName.length()+2, rawResponseStr.length()-2);
			}else if(rawResponseStr.contains(callBackMethodName)){
				String[] ele = rawResponseStr.split(callBackMethodName);
				String[] ele1 = ele[1].split(";");
				finalResponseStr = ele1[0].substring(3,ele1[0].length()-1);
			}else{
				log.debug("您指定的回调函数名可能有问题，请检查拼写及大小写！");
				log.error("您指定的回调函数名可能有问题，请检查拼写及大小写！");
			}
		}
		return finalResponseStr;
	}
}
