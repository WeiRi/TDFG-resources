import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author pandey
 *
 */
public class JsoupTestToyGetUrl {

	private static final String TOY_STORY_URL = "http://www.thetoystore.com/uae/catalogsearch/result/?q=";
	
	private static final String TOY_WEBSITE = "http://www.thetoystore.com";
	
	/**
	 * 
	 */
	public JsoupTestToyGetUrl() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {

		// Reader
		BufferedReader br = new BufferedReader(new FileReader("config/keywords.txt"));

		// Writer
		File file = new File("output/toyurl.csv");
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		try {
			String line = br.readLine();

			while (line != null) {

				try {
					out.println(fetchAndPrintData(line));
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} finally {

			out.close();
			br.close();
		}

	}

	private static String fetchAndPrintData(String url1) throws IOException {

		StringBuilder sb = new StringBuilder();
		sb.append(url1);
		
		sb.append(",");
		
		url1 = TOY_STORY_URL.concat(url1);
		
		System.out.println(url1);
		
		Document doc = Jsoup.connect(url1).get();



		Elements newsHeadlines = doc.getElementsByClass("product-image");

		for (Element element : newsHeadlines) {

			String value = element.attr("href").toString();
			
			sb.append(TOY_WEBSITE.concat(value));
				
		}

		return sb.toString();
	}

}
