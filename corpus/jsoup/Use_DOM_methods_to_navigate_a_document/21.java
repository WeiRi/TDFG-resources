package sdu.out.jsoup.extract;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DomMethods {

	public static void main(String[] args) throws IOException {
//		File input = new File("temp/input.html");
//		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		Document doc = Jsoup.connect("http://www.baidu.com/").get();

		Element content = doc.getElementById("content");
		Elements links = content.getElementsByTag("a");
		for (Element link : links) {
		  String linkHref = link.attr("href");
		  String linkText = link.text();
		  System.out.println(linkHref+"   "+linkText);
		}

	}

}
