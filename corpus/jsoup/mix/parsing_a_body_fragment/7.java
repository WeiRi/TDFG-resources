package sdu.out.jsoup.input;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ParseString {

	public static void main(String[] args) {
		String html = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);

		String html2 = "<div><p>Lorem ipsum.</p>";
		Document doc2 = Jsoup.parseBodyFragment(html2);
		Element body = doc2.body();
	}

}
