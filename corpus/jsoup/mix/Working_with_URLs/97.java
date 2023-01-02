package jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.connect("https://es.wikipedia.org/wiki/Sistema_solar").get();

		Elements tablas = doc.select("table.wikitable");
		Element tablePlaneta = tablas.get(0);
		Elements filas = tablePlaneta.getElementsByTag("tr");

		int cont = 1;
		for (Element fila : filas) {
			
			if (cont > 1) {
				
				Elements celdas = fila.getElementsByTag("td");
				System.out.println(celdas.get(0).text());
				Element img = celdas.get(11).select("img").first();
				System.out.println(img.attr("src"));
			
			}
			
			cont++;
		
		}

		/*
		 * Elements links = doc.getElementsByTag("a"); for (Element link :
		 * links) { String linkHref = link.attr("href");
		 * System.out.println(linkHref);
		 * 
		 * // IR A LA SIGUIENTE PAGINA Document doc2 =
		 * Jsoup.connect(linkHref).get(); Elements heads = doc2.select("h1");
		 * System.out.println(heads.get(0).text());
		 * 
		 * Document doc3 = Jsoup.connect(linkHref).get(); Elements tablas =
		 * doc.select("table .wikitable");
		 * 
		 * }
		 */
	}
}
