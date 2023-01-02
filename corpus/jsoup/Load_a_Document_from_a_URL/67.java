import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
public class Main {
        public static void main (String[] args){
                try {
                        Document document =Jsoup
                                .connect("https://pogoda.onet.pl/prognoza-pogody/wroclaw-362450").get();
                        System.out.println("Temperatura we Wrocławiu wynosi: "+document.getElementsByClass("temp").get(0).text());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
