package com.jsoup.api.spider;

import com.jsoup.api.spider.enums.Source;
import com.jsoup.api.spider.model.PageSnapshot;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 爬虫
 *
 * @author kunge
 */
public interface Crawler {

    String name();

    Spider newSpider();

    Analyzer analyzer();

    // 爬虫接口
    interface Spider {

        String nextLink();
    }

    // 来源接口
    interface Analyzer {

        

        /**
         * 将图片替换到内容中
         *
         * @param document      文档对象
         * @param replaceImgSrc 需替换图片对象
         * @return 内容
         */
        default String content(Document document, Function<String, String> replaceImgSrc) {
            if (replaceImgSrc == null) {
                return content(document);
            }
            document.select("img[src]")
                    .stream()
                    .filter(element -> !element.attr("src").startsWith("data"))
                    .forEach(element -> element.attr("src", replaceImgSrc.apply(element.attr("abs:src"))));
            return content(document);
        }

        
 
    }
}
