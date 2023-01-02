package com.windrui.english;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 英语作文
 * @author wxq-pc
 *
 */
public class EnglishList {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.adreep.cn/gz/?page=1").get();
			Elements contentList=doc.select("div[class=content-list]");		
			Iterator<Element> contentElement=contentList.iterator();
			
			while(contentElement.hasNext()){
				
				Element content=(Element)contentElement.next();
				Elements lists=content.select("a[href^=/gz/]");
				Iterator<Element> list=lists.iterator();
				while(list.hasNext()){
					Element con=list.next();
						System.out.println(con.text()+con.attr("href"));
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
