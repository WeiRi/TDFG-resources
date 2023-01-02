package main;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.AbstractQueryParser;
import org.jsoup.select.Elements;
import org.jsoup.select.TestIdQueryParser;
import org.jsoup.select.PseudoQueryParser;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralQueryParser;
import org.jsoup.select.TestNotIdQueryParser;
import org.jsoup.helper.ViewTree;
public class Main {

	public static void main(String[] args) {
		// example
	       String url = "https://dhlottery.co.kr/gameResult.do?method=byWin";
	        String selector = ":has(div)";
	        Document doc = null;   
	        
	        try {
	            doc = Jsoup.connect(url).get(); // -- 1. get����� URL�� �����ؼ� ������ ���� doc�� ��´�.zz
//	            ViewTree.makeViewTreeFile(doc, "test.txt");
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	        
	        AbstractQueryParser parser = new TestNotIdQueryParser();
	        parser = new PseudoQueryParser(parser);
	        Jsoup.setQueryParser(parser);
	        Elements titles = doc.select(selector); // -- 2. doc���� selector�� ������ ������ Elemntes Ŭ������ ��´�.
	        
	        for(Element element: titles) { // -- 3. Elemntes ���̸�ŭ �ݺ��Ѵ�.
	            System.out.println(element); // -- 4. ���ϴ� ��Ұ� ��µȴ�.
	        }
	    
	}

}
