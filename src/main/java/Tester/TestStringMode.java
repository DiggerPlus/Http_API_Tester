package Tester;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import HttpResponse.HttpJsonParser;
import HttpResponse.HttpResponseHandler;
import HttpSender.GetRequester;

public class TestStringMode {

	public static void main(String[] args) {
		String hostName = "http://express2.paipai.com/cgi-bin/accbudget_set";
		Unirest request = new Unirest();
		String parametersFilePath = System.getProperty("user.dir")+"/Config/parameters.txt";
		String headersFilePath = System.getProperty("user.dir")+"/Config/headers.txt";
//拼装URL		
		String url = GetRequester.buildUrl(hostName, parametersFilePath);
//设置HTTP请求的默认HEADER		
		GetRequester.setDefaultHeaders(request, headersFilePath);
		HttpResponse<String> stringResponse = GetRequester.getInvokeString(request, url);		
//测试获取服务器返回码
		int statusCode = HttpResponseHandler.getServerReturnCodeString(stringResponse);
		System.out.println(statusCode);
//测试在控制台上打印出服务端返回的内容		
//		HttpResponseHandler.printResponseString(stringResponse);
//因为返回的部分包括了一个回调函数的函数体+Exception，需要处理一下来拿到真正的JSON返回部分		
		String responseJson = HttpResponseHandler.specialHandle(stringResponse, "setLimitCallback");
		System.out.println(responseJson);
//打印出所有的JSON键值对，如果是对应于JSON Object或者Json Array的键，其值不可打印。
		try {
			Map<String,String> jsonKeyValuePair = HttpJsonParser.getAllKeyValuePair(responseJson);
			if(!jsonKeyValuePair.isEmpty()){
				for(Entry<String,String> entry:jsonKeyValuePair.entrySet()){
					System.out.println("KEY: "+entry.getKey()+"     VALUE: "+entry.getValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
