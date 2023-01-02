package com.windrui.net.common;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 热门内含段子
 * @author wxq-pc
 *
 */
public class HotNeiHanDuanZi {

	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.neihanshequ.com/joke/hot/?page=0").get();
			Elements mainElements=doc.select("div[class=pin-context]");
			Elements timeElements=doc.select("span[class=time]");
			
			Iterator<Element> mainElement=mainElements.iterator();
			Iterator<Element> timeElement=timeElements.iterator();
			
			while(mainElement.hasNext()){
				Element me=(Element)mainElement.next();
				Element te=(Element)timeElement.next();
				System.out.println(me.select("p").text()+"--发表于"+te.text());
			}
			
			
//			Element element=elements.first();
//			Elements es=element.getElementsByTag("a");
//			Elements eh=element.getElementsByTag("span");
//			Iterator<Element> it=es.iterator();
//			Iterator<Element> is=eh.iterator();
//			while(it.hasNext()){
//				Element ee=(Element) it.next();
//				Element sy=(Element)is.next();
//				System.out.println("http://www.jokeji.cn"+ee.attr("href")+":"+ee.text()+":"+sy.text());
//			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
