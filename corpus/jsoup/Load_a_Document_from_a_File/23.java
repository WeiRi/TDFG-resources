package sdu.out.jsoup.input;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoadDocFromFile {

	public static void main(String[] args) throws IOException {
		
		/*//local
		File input = new File("F:/workspace/JsoupTest/index.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		System.out.println(doc.head());*/
		
		//url
		File input2 = new File("temp/input.html");
		Document doc2 = Jsoup.parse(input2, "UTF-8", "");
		System.out.println(doc2.title());
	}

}
