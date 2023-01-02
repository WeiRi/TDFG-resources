package org.jsoup.test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.AbstractQueryParser;
import org.jsoup.select.Elements;
import org.jsoup.select.PseudoQueryParser;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralQueryParser;
import org.jsoup.select.TestClassQueryParser;
import org.jsoup.select.TestIdQueryParser;
import org.jsoup.select.TestNotIdQueryParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestQueryParser {
    String url = "https://dhlottery.co.kr/gameResult.do?method=byWin";
    Document doc = null; 
   
	@BeforeEach
	void setUp() throws Exception {
	    try {
	        doc = Jsoup.connect(url).get(); // -- 1. get방식의 URL에 연결해서 가져온 값을 doc에 담는다.zz
//	        ViewTree.makeViewTreeFile(doc, "test.txt");
	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }
	}

	@AfterEach
	void tearDown() throws Exception {
        doc = Jsoup.connect(url).get(); // -- 1. get방식의 URL에 연결해서 가져온 값을 doc에 담는다.zz

	}
	@Test
	void defualtIdQuerytest() {
		/*set query*/
		String selector = "#footer";
		/*set parser*/
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void defualtClassQuerytest() {
		/*set query*/
		String selector = ".footer";
		/*set parser*/
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void defualtattrQuerytest() {
		/*set query*/
		String selector = "[src]";
		/*set parser*/
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void defualtattrAllQuerytest() {
		/*set query*/
		String selector = "*";
		/*set parser*/
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void pseudoHasQuerytest() {
		/*set query*/
		String selector = ":has(div)";
		/*set parser*/
	    AbstractQueryParser parser = new TestNotIdQueryParser();
	    parser = new PseudoQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void pseudoNotQuerytest() {
		/*set query*/
		String selector = ":not(h2)";
		/*set parser*/
	    AbstractQueryParser parser = new TestNotIdQueryParser();
	    parser = new PseudoQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void testNotidQuerytest() {
		/*set query*/
		String selector = "#footer";
		/*set parser*/
	    AbstractQueryParser parser = new TestNotIdQueryParser();
	    parser = new TestIdQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void testNotclassQuerytest() {
		/*set query*/
		String selector = ".footer";
		/*set parser*/
	    AbstractQueryParser parser = new TestNotIdQueryParser();
	    parser = new TestClassQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	@Test
	void testStructualQuerytest() {
		/*set query*/
		String selector = ":first-child";
		/*set parser*/
	    AbstractQueryParser parser = new QueryParser();
	    parser = new StructuralQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	void testNotClassIdQuerytest() {
		/*set query*/
		String selector = ":first-child";
		/*set parser*/
	    AbstractQueryParser parser = new QueryParser();
	    parser = new StructuralQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);

	}
	void testNotClassIdQuerytestwerew22() {
		/*set query*/
		String selector = ":first-child";
		/*set parser*/
	    AbstractQueryParser parser = new QueryParser();
	    parser = new StructuralQueryParser(parser);
	    Jsoup.setQueryParser(parser);
	    /*running*/
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);
	}
	@Test
	void idQueryParser() {
	    String selector = ".footer";
	    Elements titles = doc.select(selector); // -- 2. doc에서 selector의 내용을 가져와 Elemntes 클래스에 담는다.
	    assertNotNull(titles);
	}
}
