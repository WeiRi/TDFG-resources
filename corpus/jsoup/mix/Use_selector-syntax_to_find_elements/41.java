package com.windrui.net.common;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NHPic {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.neihanshequ.com/pic/").get();
			Elements mainElements=doc.select("div[class=pin-context]");
			Elements timeElements=doc.select("span[class=time]");
			Elements imgElements=doc.select("[data-error^=http://]");
			
			Iterator<Element> mainElement=mainElements.iterator();
			Iterator<Element> timeElement=timeElements.iterator();
			Iterator<Element> imgElement=imgElements.iterator();
			
			while(mainElement.hasNext()){
				Element me=(Element)mainElement.next();
				Element te=(Element)timeElement.next();
				Element im=(Element)imgElement.next();
				System.out.println(me.select("p").text()+"--发表于"+te.text()+im.select("img").attr("data-original"));
			}
					
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
