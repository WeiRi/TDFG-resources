package com.windrui.net.common;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BDJPic {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.budejie.com/").get();
			Elements mainElements=doc.select("p[class=main-content1]");
			Elements timeElements=doc.select("p[class=mon-fr]");
			Elements imgElements=doc.select("[src^=http://ww]");
			
			Iterator<Element> mainElement=mainElements.iterator();
			Iterator<Element> timeElement=timeElements.iterator();
			Iterator<Element> imgElement=imgElements.iterator();
			
			while(mainElement.hasNext()){
				Element me=(Element)mainElement.next();
				Element te=(Element)timeElement.next();
				Element im=(Element)imgElement.next();
				System.out.println(me.text()+"--发表于"+te.text()+"img:"+im.select("img").attr("src"));
			}
					
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
