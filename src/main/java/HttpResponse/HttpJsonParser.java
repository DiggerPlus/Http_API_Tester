package HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.BaseJsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;

public class HttpJsonParser {
static Logger log = Logger.getLogger(HttpJsonParser.class);	
	
	private enum JsonSchemaType{
		JSON_OBJECT,
		JSON_ARRAY,
		JSON_STRING,
		JSON_INT,
		JSON_NUMBER,
		JSON_DOUBLE,
		JSONG_LONG,
		JSON_FLOAT,
		JSON_BOOLEAN,
		JSON_NULL
	}
	
	public static String getJSONResponse(InputStream ip) throws IOException{
		InputStreamReader inPutReader = new InputStreamReader(ip);
		String responseStr = null;
		BufferedReader reader = new BufferedReader(inPutReader);
		responseStr = reader.readLine();
		return responseStr;
	}
	
	public static void printJSONResponse(InputStream ip) throws IOException{		
		InputStreamReader inPutReader = new InputStreamReader(ip);
		BufferedReader reader = new BufferedReader(inPutReader);		
		System.out.println(new String(reader.readLine()));			
	}
	
	public static List<String> getValue2ListByUsingKeyName(String KeyName,Map<String,String> map){
		List<String> list = new ArrayList<String>();
		String str = null;
		str = map.get(KeyName).toString();
		if(str != null){
			if(str.contains(",")){
				String[] strs = str.split(",");
				for(String string : strs){
					if(string != null){
						if(string.isEmpty()){
							list.add("EmptyValue");
						}else{
							list.add(string);
						}
					}
				}
			}else{
				list.add(str);
			}
		}
		return list;
	}
	
	public static Map<String,String> getAllKeyValuePair(String jsonStr) throws IOException{
		Map<String,String> resultFullMap = new HashMap<String,String>();
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createJsonParser(jsonStr);
		try {			
			while(parser.nextToken() != null){
				String key = null;
				String value = null;
				if(parser.getCurrentName() == null || parser.getText() == null){
					key = "NULLVALUE";
					value = "NULLVALUE";
				}else{
					key = parser.getCurrentName();
					value = parser.getText();
					if(value.endsWith("]")){
						value = "Key='"+key+"' follows with a JSON Array,don't show it out!";
					}
					else if(value.endsWith("}")){
						value = "Key='"+key+"' follows with a JSON Object,don't show it out!";
					}
				}
				if(key != null && value != null){
					resultFullMap.put(key, value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!parser.isClosed()){
			parser.close();
		}
		return resultFullMap;
	}
	
	public static void writePrettyJSONStream2Files(String jsonStr,String filePath,String jsonFileName) throws JsonProcessingException, IOException{
		long startTime = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();
		String fileFullPath = filePath+jsonFileName;
		String newPrettyJsonStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(jsonStr);		
//Format JSON output string
		if(!newPrettyJsonStr.isEmpty()){
			newPrettyJsonStr = newPrettyJsonStr.substring(1,newPrettyJsonStr.length()-1);
//JAVA的正则中\\\\表示一个\		
			newPrettyJsonStr = newPrettyJsonStr.replaceAll("\\\\", "").trim();
		}
		FileWriter fstream = new FileWriter(fileFullPath);
		BufferedWriter writer = new BufferedWriter(fstream);
		writer.write(newPrettyJsonStr);
		writer.close();
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println("JSON file named:"+jsonFileName+" has generated successfully,under path==>"+fileFullPath+"\n Time cost:"+endTime+" ms");
		log.info("JSON file named:"+jsonFileName+" has generated successfully,under path==>"+fileFullPath+"\n Time cost:"+endTime+" ms");
	}
	
	public static String getJSONSchema(String jsonStr,String keyName) throws JsonParseException, IOException{
		String jsonSchemaName = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode nodes = mapper.readTree(jsonStr);
		JsonNode node = nodes.findPath(keyName);
		if(node != null && node.isObject()){
			jsonSchemaName = JsonSchemaType.JSON_OBJECT.toString();
		}
		else if(node != null && node.isArray()){
			jsonSchemaName = JsonSchemaType.JSON_ARRAY.toString();
		}
		else if(node != null && node.isInt()){
			jsonSchemaName = JsonSchemaType.JSON_INT.toString();
		}
		else if(node != null && node.isNull()){
			jsonSchemaName = JsonSchemaType.JSON_NULL.toString();
		}
		else if(node != null && node.isBoolean()){
			jsonSchemaName = JsonSchemaType.JSON_BOOLEAN.toString();
		}
		else if(node != null && node.isTextual()){
			jsonSchemaName = JsonSchemaType.JSON_STRING.toString();
		}
		else if(node != null && node.isNumber()){
			jsonSchemaName = JsonSchemaType.JSON_NUMBER.toString();
		}
		else if(node != null && node.isDouble()){
			jsonSchemaName = JsonSchemaType.JSON_DOUBLE.toString();
		}
		else if(node != null && node.isLong()){
			jsonSchemaName = JsonSchemaType.JSONG_LONG.toString();
		}
		else if(node != null && node.isFloatingPointNumber()){
			jsonSchemaName = JsonSchemaType.JSON_FLOAT.toString();
		}
		return jsonSchemaName;
	}
	
	public static boolean getChildJsonNode(String jsonStr,String parentKeyName,String childKeyName) throws JsonParseException, IOException{		
		boolean isChildNode = false;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootnodes = mapper.readTree(jsonStr);
		JsonSchema schema = new JsonSchema((ObjectNode) rootnodes);
		ObjectNode objNode = schema.getSchemaNode();
		BaseJsonNode bNode = (BaseJsonNode) objNode.findPath(parentKeyName);
		if(bNode.toString().contains(childKeyName)){
			isChildNode = true;
		}else{
			isChildNode = false;
		}
		return isChildNode;
	}
}
