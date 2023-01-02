package com.windrui.kaoyan;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 考研资讯 > 考研人物
 * @author wxq-pc
 *
 */
public class KaoYanRenWuDetail {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://yz.chsi.com.cn/kyzx/kyrw/201407/20140715/1113370945.html").get();
			Elements titleList=doc.select("title");
			Elements contentList=doc.select("div[id=article_bd]");		
			Elements timeList=doc.select("div[class=sate_info]");	
			Iterator<Element> contentElement=contentList.iterator();
			Iterator<Element> titleElement=titleList.iterator();
			Iterator<Element> timeElement=timeList.iterator();
			
			while(contentElement.hasNext()){
				
				Element content=(Element)contentElement.next();
				Element title=(Element)titleElement.next();
				Element time=(Element)timeElement.next();
				Element pubTime=(Element)time.select("span").get(0);
				Element pubFrom=(Element)time.select("span").get(1);
				Elements lists=content.select("p");
				Iterator<Element> list=lists.iterator();
				System.out.println(title.text()+"--"+pubTime.text()+"--"+pubFrom.text());
				while(list.hasNext()){
					Element con=list.next();
					System.out.println(con.text());
				}				
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
