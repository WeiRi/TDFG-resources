package com.ims.c01hello;

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
        //1. 根据tag获取获取DOM元素
        Elements elements = doc.getElementsByTag("title");
        Element element = elements.get(0);
        System.out.println("网页标题是：" + element.text());
        //2. 根据id获取获取DOM元素
        Element element2 = doc.getElementById("site_nav_top");
        System.out.println("口号：" + element2.text());
    }
}
