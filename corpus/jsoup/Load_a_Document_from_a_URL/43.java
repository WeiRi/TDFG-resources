package com.tatsuya;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		System.out.println("Reading content from Web......");
		Document doc = Jsoup.connect("http://dantri.com.vn").get();
		// String title = doc.title();
		// //System.out.println("Title : " + title);
		// System.out.println(doc);
		SimpleDateFormat date = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		String time = date.format(new Date());
		
		time = time.replace(':', '_');
		
		BufferedWriter out = new BufferedWriter(new FileWriter(time));
		out.write(doc.toString());
		out.close();
		System.out.println("Done!!");
	}
}
