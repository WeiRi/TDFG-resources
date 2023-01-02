package org.jsoup.examples;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class DaumTest {

	public static void main(String[] args) throws Exception {
		
		String daumUrl = "https://search.daum.net/qsearch?mk=Xec8pzUqFQ-umVR6aDXZ6wAAAIU&uk=XYT7jcuFopkAAA4Xzs0AAACy&q=%EA%B0%95%EC%95%84%EC%A7%80&w=spread&qsearch_ver=v2&viewtype=json&exp=IIM&lpp=80&m=tab_img&req=qtab&page=2";
		
		Connection con = Jsoup.connect(daumUrl)
				.header("origin","https://search.daum.net/")
				.header("referer","https://search.daum.net/")
				.header("authority", "search.daum.net")
				.header("accept-encoding","br")
				.data("q","°­¾ÆÁö")
				.data("w","spread")
				.data("mk","Xec8pzUqFQ-umVR6aDXZ6wAAAIU")
				.data("uk","XYT7jcuFopkAAA4Xzs0AAACy")
				.data("qsearch_ver","v2")
				.data("viewtype","v2")
				.data("exp","IIM")
				.data("Ipp","80")
				.data("m","tab_img")
				.data("req","qtab")
				.data("page","2")
				.ignoreContentType(true);
		
		
		Document doc = con.get();
		
		System.out.println(doc);
		
	}

}
