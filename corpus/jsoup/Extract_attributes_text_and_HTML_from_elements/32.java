package com.windrui.kaoyan;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 考研资讯 > 经验心得
 * @author wxq-pc
 *
 */
public class KaoYanXinDeList {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://yz.chsi.com.cn/kyzx/jyxd/?start=0").get();
			Elements contentList=doc.select("a[href^=/kyzx/jyxd/]").select("a[target=_blank]");
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
