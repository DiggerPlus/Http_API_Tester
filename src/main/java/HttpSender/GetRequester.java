package HttpSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GetRequester {
	private static Logger log = Logger.getLogger(GetRequester.class);	
	private static final String urlSeperator = "?";
	private static final String parameterSeperator = "&";
	
	@SuppressWarnings("static-access")
	public static void setDefaultHeaders(Unirest request,String headersFilePath){
		HashMap<String,String> headers = getHeader(headersFilePath);
		if(headers != null && !headers.isEmpty()){
			for(Entry<String,String> entry: headers.entrySet()){			
				request.setDefaultHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 用于拼装一个带参数的URL，参数全部保存在一个文本文件中
	 * @param hostName
	 * @param parametersFilePath
	 * @return
	 */
	public static String buildUrl(String hostName,String parametersFilePath){
		String url = "";
		ArrayList<String> parameters = getParameters(parametersFilePath);
		if(!parameters.isEmpty()){
			StringBuffer sb = new StringBuffer();
			int counter = 1;
			for(String param:parameters){
				if(counter < parameters.size()){
					sb.append(param+parameterSeperator);
				}else if(counter == parameters.size()){
					sb.append(param);
				}
				counter++;
			}
			url = hostName+urlSeperator+sb.toString();
		}else{
			log.debug("您的Parameter文件中没有任何参数信息，请检查！");
			log.error("您的Parameter文件中没有任何参数信息，请检查！");
		}
		return url;
	}
	
	/**
	 * 同步方式，请求服务端返回格式为json格式执行GET请求
	 * @param request
	 * @param url
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static HttpResponse<JsonNode> getInvokeAsJsonFormat(Unirest request,String url){
		HttpResponse<JsonNode> response= null;
		try {
			response = request.get(url).asJson();
		} catch (UnirestException e) {
			log.debug("执行GET请求时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			log.error("执行GET请求时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
		}finally{
			try {
				request.shutdown();
				request.clearDefaultHeaders();
			} catch (IOException e) {
				log.debug("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
				log.error("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			}
		}
		return response;
	}
	
	/**
	 * 以InputStream的形式返回响应
	 * @param request
	 * @param url
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static HttpResponse<InputStream> getInvokeInputStream(Unirest request,String url){
		HttpResponse<InputStream> response = null;
		try {
			response = request.get(url).asBinary();
		} catch (UnirestException e) {
			log.debug("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			log.error("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
		}finally{
			try {
				request.shutdown();
				request.clearDefaultHeaders();
			} catch (IOException e) {
				log.debug("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
				log.error("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			}
		}
		return response;
	}
	
	/**
	 * 以String方式返回服务端响应
	 * @param request
	 * @param url
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static HttpResponse<String> getInvokeString(Unirest request,String url){
		HttpResponse<String> response = null;
		try {
			response = request.get(url).asString();
		} catch (UnirestException e) {
			log.debug("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			log.error("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
		}finally{
			try {
				request.shutdown();
				request.clearDefaultHeaders();
			} catch (IOException e) {
				log.debug("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
				log.error("GET请求在关闭时遇到问题，URL=["+url+"],err: "+e.getLocalizedMessage());
			}
		}
		return response;
	}
	
	/**
	 * 从一个文本文件中逐行读取内容，全部做trim处理
	 * @param path
	 * @return
	 */
	 private static ArrayList<String> readDataFromFile(String path) {
	    	ArrayList<String> datas = new ArrayList<String>();
	    	File file = new File(path);
	    	if(file != null){
	    		try {
	    			String line = "";
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					if(br != null){
						while((line = br.readLine()) != null){
							datas.add(line.trim());
						}
					}
					if(br != null){
						br.close();
					}
				} catch (FileNotFoundException e) {
					log.debug("Read data err: "+e.getLocalizedMessage());
					log.error("Read data err: "+e.getLocalizedMessage());
				} catch (IOException e) {
					log.debug("Read data err: "+e.getLocalizedMessage());
					log.error("Read data err: "+e.getLocalizedMessage());
				}
	    	}      					
	    	return datas;
	  }
	 
	 /**
	  * 读取参数信息
	  * @param parameterFilePath
	  * @return
	  */
	 private static ArrayList<String> getParameters(String parameterFilePath){
			ArrayList<String> datas = new ArrayList<String>();
			if(parameterFilePath != null){
				datas = readDataFromFile(parameterFilePath);
			}else{
				log.debug("请确认您存放parameter的文件的路径是否正确或者是否存在该parameter文件！");
				log.error("请确认您存放parameter的文件的路径是否正确或者是否存在该parameter文件！");
			}		
			return datas;
	}
	 
	 /**
	  * 读取HEADER信息
	  * @param headerFilePath
	  * @return
	  */
	 private static HashMap<String,String> getHeader(String headerFilePath){
	    	ArrayList<String> datas = new ArrayList<String>();
			HashMap<String,String> returnMap = new HashMap<String,String>();
			if(headerFilePath != null){
				datas = readDataFromFile(headerFilePath);
				if(datas!=null && !datas.isEmpty()){
					for(String lineStr:datas){
						String[] ele = lineStr.trim().split(":");
						returnMap.put(ele[0].toString(), ele[1].toString());
					}
				}
			}else{
				log.debug("请确认您存放HEADER的文件的路径是否正确或者是否存在该HEADER文件！");
				log.error("请确认您存放HEADER的文件的路径是否正确或者是否存在该HEADER文件！");
			}		
			return returnMap;
	    }
}
