import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author pandey
 *
 */
public class JsoupTestToyImageZoom {

	private static final String TOY_STORY_URL = "http://www.thetoystore.com";
	private static final String IMAGE_FOLDER = "output/images/";

	public JsoupTestToyImageZoom() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {

		Path imageFolderPath = Paths.get(IMAGE_FOLDER);

		boolean pathExists = Files.exists(imageFolderPath, LinkOption.NOFOLLOW_LINKS);

		if (!pathExists) {
			Files.createDirectory(imageFolderPath);
		}

		// Reader
		BufferedReader br = new BufferedReader(new FileReader("config/keywordsUrl4img.txt"));

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

	private static void fetchImageUrlAndCreateImage(String keywordUrl) throws IOException {

		String[] keywordUrlArr = keywordUrl.split(",");
		
		String url1 = keywordUrlArr[1];

		// System.out.println(url1);

		// get the HTML document
		Document doc = Jsoup.connect(url1).get();

		Elements newsHeadlines = doc.select("#zoom-btn");

		if (newsHeadlines != null && newsHeadlines.get(0) != null) {
			Element element = newsHeadlines.get(0);

//			System.out.println(element.attr("href"));
			
				String imageSrc = TOY_STORY_URL.concat(element.attr("href"));
				if (imageSrc != null && !"".equals(imageSrc)) {
					try {
						// Open a URL Stream
						getImages(imageSrc, keywordUrlArr[0], 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
//
//				}
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
