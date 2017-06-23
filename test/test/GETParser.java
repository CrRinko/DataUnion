package test;

import java.util.HashMap;
import java.util.Map.Entry;

public class GETParser {
	private HashMap<String, String> dataMap;
	
	public GETParser(){
		dataMap=new HashMap<String,String>();
	}
	
	public void addParam(String key, String value){
		if(!dataMap.containsKey(key)){
			dataMap.put(key, value);
		}else{
			dataMap.replace(key, value);
		}
	}

	@Override
	public String toString() {
		String output="";
		int count=0;
		for(Entry<String, String> entry:dataMap.entrySet()){
			if(count>0)output+="&";
			output+=entry.getKey()+"="+entry.getValue();
			count++;
		}
		return output;
	}
}
