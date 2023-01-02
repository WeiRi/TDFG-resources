package com.windrui.tangshisongci;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 宋词三百首
 * @author wxq-pc
 *
 */
public class SongCiSBSList {
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.xigutang.com/songci300/songci300.html").get();
			Elements mainElements=doc.select("a[href^=http://www.xigutang.com/songci300/]");
			//Elements timeElements=doc.select("p[class=mon-fr]");
			
			Iterator<Element> mainElement=mainElements.iterator();
			//Iterator<Element> timeElement=timeElements.iterator();
			
			int num=0;
			
			while(mainElement.hasNext()){
				Element me=(Element)mainElement.next();
				//Element te=(Element)timeElement.next();
				if(num!=0 && num!=338){
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
