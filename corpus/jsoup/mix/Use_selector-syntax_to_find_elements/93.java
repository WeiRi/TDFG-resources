package org.jsoup.imgcrawler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.urllist.BingUrlList;
import org.jsoup.urllist.NaverUrlList;
import org.jsoup.urllist.UrlList;

public class BingCrawler extends Crawler {
	
	public BingCrawler () {
		super();
		urls = new BingUrlList(tot_imgCnt);
	}
	
	@Override
	public Elements getImages() throws IOException {
		Document doc = Jsoup.connect(urlIterator.next().toString()).get();
		return doc.select("img");
	}

	@Override
	public String decodeUrl(Element e) {
		return e.getElementsByAttribute("src").attr("src");
	}

}


