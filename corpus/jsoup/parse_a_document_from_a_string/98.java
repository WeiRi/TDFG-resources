package org.jsoup.imgcrawler;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class BingCrawlerTest {
	private String reference = "<img class=\"mimg\" style=\"background-color:#43760a;color:#43760a\" height=\"178\" width=\"285\" src=\"https://tse1.mm.bing.net/th?id=OIP.tIo2QDvahsvcOH-CzOgUeQHaEt&amp;w=285&amp;h=178&amp;c=7&amp;o=5&amp;pid=1.7\" alt=\"dog에 대한 이미지 결과\">";
	
	@Test
	public void testDecodeUrl() {
		CrawlerFactory crawlerFactory = new CrawlerFactory();
		Crawler crawler = crawlerFactory.createCrawler("bing");
		
		Document doc = Jsoup.parse(reference);
		Elements imgs = doc.select("img");
		for(Element img: imgs) {
			String expected = "https://tse1.mm.bing.net/th?id=OIP.tIo2QDvahsvcOH-CzOgUeQHaEt&w=285&h=178&c=7&o=5&pid=1.7";
			String actual = crawler.decodeUrl(img);
			assertEquals(expected, actual);
		}
	}
}
