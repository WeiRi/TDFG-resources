package com.windrui.tangshisongci;
/**
 * 唐诗三百首列表
 */
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TangShiSBSList {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.xigutang.com/tangshi300/tangshi300.html").get();
			Elements mainElements=doc.select("a[href^=http://www.xigutang.com/tangshi300/]");
			//Elements timeElements=doc.select("p[class=mon-fr]");
			
			Iterator<Element> mainElement=mainElements.iterator();
			//Iterator<Element> timeElement=timeElements.iterator();
			
			int num=0;
			
			while(mainElement.hasNext()){
				Element me=(Element)mainElement.next();
				//Element te=(Element)timeElement.next();
				if(num!=0 && num!=1 && num!=315){
					System.out.println(me.text()+me.attr("href"));
				}				
				num++;
			}
			
			//System.out.println(num);
						
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
