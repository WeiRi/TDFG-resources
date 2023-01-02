package org.jsoup.imgcrawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class NaverCrawlerTest {
	private String reference = "<img src=\"\\&quot;data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7\\&quot;\" class=\"\\&quot;_img\\&quot;\" alt=\"\\&quot;창원강아지분양\" 광명강아지분양 도그마루 좋아요!! | 블로그\\\" onerror=\"\\&quot;var\" we=\"$Element(this);\" we.addClass('bg_nimg'); we.attr('alt','이미지준비중'); we.attr('src','data:image gif;base64,R0lGODlhAQABAIAAAAAAAP yH5BAEAAAAALAAAAAABAAEAAAIBRAA7');\\\" data-source=\"\\&quot;https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAxNjEwMjdfMjE1%2FMDAxNDc3NTMxOTAyNzg0.3q5qfvzikazXia81CeI4u5LpFIi0A7KzaBBHc0sqgo4g.YtQDOJDqvdZv7wkzgtTJiWvz-FcsACu-QRH-7EXtOVsg.JPEG.txzty2%2F%25C3%25A2%25BF%25F8%25B0%25AD%25BE%25C6%25C1%25F6%25BA%25D0%25BE%25E7%25B1%25A4%25B8%25ED%25B0%25AD%25BE%25C6%25C1%25F6%25BA%25D0%25BE%25E76.jpg&amp;type=b400\\&quot;\" data-width=\"\\&quot;700\\&quot;\" data-height=\"\\&quot;922\\&quot;\">";
	
	@Test
	public void testDecodeUrl() {
		CrawlerFactory crawlerFactory = new CrawlerFactory();
		Crawler crawler = crawlerFactory.createCrawler("naver");
		
		Document doc = Jsoup.parse(reference);
		Elements imgs = doc.select("img");
		for(Element img: imgs) {
			String expected = "http://blogfiles.naver.net/MjAxNjEwMjdfMjE1/MDAxNDc3NTMxOTAyNzg0.3q5qfvzikazXia81CeI4u5LpFIi0A7KzaBBHc0sqgo4g.YtQDOJDqvdZv7wkzgtTJiWvz-FcsACu-QRH-7EXtOVsg.JPEG.txzty2/%C3%A2%BF%F8%B0%AD%BE%C6%C1%F6%BA%D0%BE%E7%B1%A4%B8%ED%B0%AD%BE%C6%C1%F6%BA%D0%BE%E76.jpg&type=b400\\";
			String actual = crawler.decodeUrl(img);
			assertEquals(expected, actual);
		}
	}
}
