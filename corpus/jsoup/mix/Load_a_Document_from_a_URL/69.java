package jsoupTest;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {
	public static void main(String[] args) throws IOException {
		String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EB%82%A0%EC%94%A8";
		Document doc = Jsoup.connect(url).get();
//		System.out.println(doc);
		Elements text = doc.select(".cast_txt");
		Iterator<Element> iter = text.iterator();
		while(iter.hasNext()) {
			Element t = iter.next();
			System.out.println(t.text());
	}
}
}