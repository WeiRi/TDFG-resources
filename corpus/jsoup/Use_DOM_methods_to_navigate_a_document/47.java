package com.ims.c02getDomElement;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author: GaoXu
 * @date: 2020/5/24
 * @desc：请对类进行描述
 */
public class App {

    public static void main(String[] args) throws IOException {
        // httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // httpPost
        HttpPost httpPost = new HttpPost("https://www.cnblogs.com/");
        // 执行httpPost请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取httpPost请求返回的httpEntity
        HttpEntity httpEntity = response.getEntity();
        // 解析httpEntity
        String pageContent = EntityUtils.toString(httpEntity, "utf-8");
        // 释放资源response关闭 httpClient关闭
        response.close();
        httpClient.close();

        //jsoup解析html内容
        Document doc = Jsoup.parse(pageContent);
        /**
         * 基本查找
         */
        //1.    根据tag获取获取DOM元素
        Elements elements1 = doc.getElementsByTag("title");
        Element element1 = elements1.get(0);
        System.out.println("网页标题是：" + element1.text());
        //2.    根据id获取获取DOM元素
        Element element2 = doc.getElementById("site_nav_top");
        System.out.println("口号：" + element2.text());
        //3.    根据class获取获取DOM元素
        Elements element3 = doc.getElementsByClass("post_item");
        for (Element e : element3) {
            System.out.println(e.html());
        }
        //4.    根据属性获取获取DOM元素
        Elements elements4 = doc.getElementsByAttribute("width");
        for (Element e : elements4) {
            System.out.println(e.toString());
        }
        //5.    根据属性值获取获取DOM元素
        Elements elements5 = doc.getElementsByAttributeValue("target", "_blank");
        for (Element e : elements5) {
            System.out.println(e.toString());
        }

        /**
         * 选择查找
         */
        //通过选择器查找所有博客链接DOM
        Elements linkElements = doc.select("#post_list .post_item .post_item_body h3 a");
        for (Element e : linkElements) {
            System.out.println("博客标题：" + e.text());
        }
        // 带有href属性的a元素
        Elements hrefElements = doc.select("a[href]");
        for (Element e : hrefElements) {
            System.out.println(e.toString());
        }
        // 查找扩展名为.png的图片DOM节点
        Elements imgElements = doc.select("img[src$=.png]");
        for (Element e : imgElements) {
            System.out.println(e.toString());
        }
    }
}
