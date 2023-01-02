package com.windrui.tangshisongci;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 宋词三百首详细
 * @author wxq-pc
 *
 */
public class SongCiSBSDetail {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		try {
			Document doc = Jsoup.connect("http://www.xigutang.com/songci300/pzz_yzlsxs_ys_zs_fy_sx_jj_6185.html").get();
			Elements titleElements=doc.select("div[class=title]");
			Elements contentElements=doc.select("div[class=content]");
			
			Iterator<Element> titleElement=titleElements.iterator();
			Iterator<Element> contentElement=contentElements.iterator();
			
			
			int num=0;
			
			while(titleElement.hasNext()){
				Element me=(Element)titleElement.next();
				//Element te=(Element)timeElement.next();
				if(num!=0){
					System.out.println(me.text().replace("|注释|翻译|赏析|讲解", ""));
				}				
				num++;
			}
			
			int flag=0;
			
			while(contentElement.hasNext()){
				Element me=(Element)contentElement.next();
				Elements contents=me.select("p");
				Iterator<Element> content=contents.iterator();
				while(content.hasNext()){
					flag++;
					Element con=(Element)content.next();
					//if(flag!=3 && flag!=5){
						System.out.println(con.text());
					//}
					
				}
			}
						
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
