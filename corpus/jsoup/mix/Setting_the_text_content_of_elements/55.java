import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author pandey
 *
 */
public class JsoupTestToy {

	/**
	 * 
	 */
	public JsoupTestToy() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {

		// Reader
		BufferedReader br = new BufferedReader(new FileReader("config/url.txt"));

		// Writer
		File file = new File("output/values.csv");
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		try {
			String line = br.readLine();
			
			int i = 0;
			while (line != null) {

				i++;
				try {
					out.println(fetchAndPrintData(line));
				
					if (i % 50 == 0) {
						out.flush();
					}
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					line = br.readLine();
				}

			}
		} finally {
			out.flush();
			out.close();
			br.close();
		}

	}

	private static String fetchAndPrintData(String url1) throws IOException {
		Document doc = Jsoup.connect(url1).get();

//		System.out.println(doc);
		StringBuilder sb = new StringBuilder();

		sb.append(url1);
		sb.append(",");

		
		Elements shortDescirption1 = doc.getElementsByClass("product_view__description_bullets");

		Element elementRef =null;
		for (Element eachElement : shortDescirption1) {
			elementRef = eachElement;
		}
		
		Element elementRef1 = null;
		if (elementRef != null) {
			for (Element elements : elementRef.getAllElements()) {
				elementRef1 = elements;
			}
			
			sb.append(elementRef1.text());
			sb.append(",");
		}
		
		Elements newsHeadlines = doc.select("#product-tabs tbody tr");

		for (Element element : newsHeadlines) {

			String elementDescription = element.getElementsByClass("label").text();
			
			if (elementDescription.equals("Weight")  || elementDescription.equals("Gender") || elementDescription.equals("Brand") || elementDescription.equals("Recommended age") || elementDescription.equals("Dimensions (L x W x H)")) {
				addDataValue(sb, element);
			}
		}

		Elements newsHeadlines1 = doc.select("#product-tabs div");
		
		for (Element element : newsHeadlines1) {
			
			Elements descriptionElement = element.getElementsByClass("std");
			if (descriptionElement != null && descriptionElement.first() != null) {
				sb.append(descriptionElement.first().text());
			}
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	private static void addDataValue(StringBuilder sb, Element element) {
		System.out.println(element.getElementsByClass("value").text());
		sb.append(element.getElementsByClass("data").text());
		sb.append(",");
	}

}
