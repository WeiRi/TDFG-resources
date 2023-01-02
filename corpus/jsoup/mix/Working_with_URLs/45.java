import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author pandey
 *
 */
public class JsoupTestToyImage {

	private static final String UPC_URL = "http://www.upcitemdb.com/upc/";

	private static final String IMAGE_FOLDER = "output/images/";

	/**
	 * 
	 */
	public JsoupTestToyImage() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {

		// Reader
		BufferedReader br = new BufferedReader(new FileReader("config/keywords4img.txt"));

		try {
			String line = br.readLine();

			while (line != null) {

				try {
					fetchImageUrlAndCreateImage(line);
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					line = br.readLine();
				}

			}
		} finally {
			br.close();
		}

	}

	private static void fetchImageUrlAndCreateImage(String keyword) throws IOException {

		String url1 = UPC_URL.concat(keyword);

		// System.out.println(url1);

		// get the HTML document
		Document doc = Jsoup.connect(url1).get();

		Elements newsHeadlines = doc.getElementsByClass("imglist");

		if (newsHeadlines != null && newsHeadlines.get(0) != null) {
			Element element = newsHeadlines.get(0);

			int i = 0;
			for (Element img : element.getAllElements()) {
				String imageSrc = img.attr("src");
				if (imageSrc != null && !"".equals(imageSrc)) {
					i++;
					// System.out.println(imageSrc);

					try {
						// Open a URL Stream
						getImages(imageSrc, keyword, i);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		}

	}

	private static void getImages(String src, String keyword, int count) throws IOException {

		String name = keyword + "_" + count+".jpg";
		// System.out.println(name);

		// Open a URL Stream
		URL url = new URL(src);

		InputStream in = url.openStream();

		OutputStream out = new BufferedOutputStream(new FileOutputStream(IMAGE_FOLDER + name));
		for (int b; (b = in.read()) != -1;) {
			out.write(b);
		}
		out.close();
		in.close();

	}

}
