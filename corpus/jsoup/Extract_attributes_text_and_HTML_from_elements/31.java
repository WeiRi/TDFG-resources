package com.windrui.kaoyan;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 考研资讯 > 政策导航
 * @author wxq-pc
 *
 */
public class KaoYanZhengCeDaoHangList {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://yz.chsi.com.cn/kyzx/zcdh/?start=0").get();
			Elements contentList=doc.select("a[href^=/kyzx/zcdh/]").select("a[target=_blank]");
			Elements timeList=doc.select("span[class=spanTime]");
			Iterator<Element> contentElement=contentList.iterator();
			Iterator<Element> timeElement=timeList.iterator();
			
			while(contentElement.hasNext()){
				
				Element content=(Element)contentElement.next();
				Element time=(Element)timeElement.next();
				System.out.println(content.text()+"--"+time.text()+"--http://yz.chsi.com.cn"+content.attr("href"));
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
