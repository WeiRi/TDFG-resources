package com.example.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import utils.JsopUtil;
@Component
public class GetPlayerOnLineDAO {
	@Autowired
	MongoOperations mongoTemplate;
	public Map<String,String> getTableByURL_IDs(String url,String ...ids) throws IOException{
		Document document= JsopUtil.getDocumentByGetMethod(url);
		HashMap<String, String> map=new HashMap<>();
		for(String arg:ids)
			getTableByDoc_ID(document, arg, map);
		
		return map;
	}
	public void getTableByDoc_ID(Document document,String id,Map<String,String> map) throws IOException{
		
		//document.getAllElements().removeAttr("style");
		Element element =document.getElementById(id);  
		try{
			element.getElementsByTag("img").remove();
			element.removeAttr("style");
	
			Elements elements=element.getAllElements();
			elements.removeAttr("style");
			elements.removeAttr("onclick");
			elements.removeAttr("class");
			elements.removeAttr("href");
			
		}catch(Exception e){
			//e.printStackTrace();
		}
		try{
			map.put(id,element.toString());
		}catch(Exception e){
			
		}
		
		
	}
}
