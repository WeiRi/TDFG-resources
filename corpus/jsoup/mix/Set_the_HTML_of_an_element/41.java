package com.example.jsoup01;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FetchPage {
	
	public static void main(String[] args) {
		
		try {
			String html = Jsoup.connect("http://greg.brie.com/cgi-bin/photo/index.cgi").get().html();
			System.out.println(html);
			Document doc = Jsoup.parse(html, "UTF-8");
			//String a = "img[src~=(?i)\\.(png|jpe?g)]";
			Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]"); // a with href
			
			for (Iterator iterator = links.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				System.out.println(element);
				String imageLocation = element.attr("src");
				//System.out.println(element.attr("src"));
				Response resultImageResponse = Jsoup.connect(imageLocation).ignoreContentType(true).execute();
				
				
				
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
