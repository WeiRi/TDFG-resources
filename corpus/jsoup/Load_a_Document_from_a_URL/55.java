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
public class JsoupTest {

	/**
	 * 
	 */
	public JsoupTest() {
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
		Document doc = Jsoup.connect(url1).get();

		StringBuilder sb = new StringBuilder();

		sb.append(url1.substring(url1.indexOf("keywords=") + 9));
		sb.append(",");

		Elements newsHeadlines = doc.select("#prodDetails table tbody tr");

		int i = 0;
		for (Element element : newsHeadlines) {
			if (i == 3) {
				break;
			}

			sb.append(element.getElementsByClass("value").text());
			sb.append(",");
			i++;
		}

		Elements newsHeadlines1 = doc.select("#productDescription p");
		if( newsHeadlines1 != null && newsHeadlines1.first() != null){
		sb.append(newsHeadlines1.first().text());
}
		System.out.println(sb.toString());
		return sb.toString();
	}

}
