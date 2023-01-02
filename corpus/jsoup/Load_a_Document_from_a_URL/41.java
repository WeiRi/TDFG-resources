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
public class EnglishDetail {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.adreep.cn/gz/54230.html").get();
			Elements contentList=doc.select("div[class=contentmain]");		
			Iterator<Element> contentElement=contentList.iterator();
			
			while(contentElement.hasNext()){
				
				Element content=(Element)contentElement.next();
//				Elements lists=content.select("a[href^=/gz/]");
//				Iterator<Element> list=lists.iterator();
//				while(list.hasNext()){
//					Element con=list.next();
//						System.out.println(con.text()+con.attr("href"));
//				}
				System.out.println(content.text());
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
