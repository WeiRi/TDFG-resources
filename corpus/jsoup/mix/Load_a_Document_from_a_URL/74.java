package org.jsoup.examples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A simple example, used on the jsoup website.
 */
public class Wikipedia {
    public static void main(String[] args) throws IOException {
    	String q = "dog"; // 검색어
    	
    	Document doc = Jsoup.connect("https://www.naver.com").get();

//    	Document doc = Jsoup.connect("https://duckduckgo.com")
//    	.header("referer", "https://duckduckgo.com/")
//    	.header("accept-encoding", "gzip, deflate, br")
//    	.data("l", "wt-wt")
//    	.data("o", "json")
//    	.data("q", q) // 임의로 몇개의 파라미터와 헤더를 생략했다.
//    	.data("vqd", "3-339297084264160297075919534835643149456-187299293038257780499509864051964073907")
//    	.data("f", ",,,")
//    	.data("p", "1")
//    	.ignoreContentType(true) // HTML Document가 아니므로 Response의 컨텐트 타입을 무시한다.
//    	.get();
    }
}
