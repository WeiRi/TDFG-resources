package com.goisan;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class JsoupDemo {
    public static void main(String[] args) throws IOException {
        try {
            String url = "https://read.qidian.com/chapter/St3c_MxCBK2oUTngny7bXQ2/Qn_8dzrXB6xp4rPq4Fd4KQ2";
            FileOutputStream fileOutputStream1 = new FileOutputStream("d://a.txt");
            BufferedOutputStream fileOutputStream = new BufferedOutputStream(fileOutputStream1);
            while (true) {
                String titleText = "";
                //获取dom树
                Document document = Jsoup.connect(url).get();
                Elements title = document.getElementsByClass("content-wrap");
                titleText += title.text();
                Elements body = document.select("div[class=read-content j_readContent]>p");
                for (Element element : body) {
                    String bodyText = element.text();
                    titleText += bodyText;
                }
                System.out.println(title.text()+"爬取成功");
                //获取下一章url
                String html = document.getElementById("j_chapterNext").attr("href");
                url = "https:" + html;
                fileOutputStream.write(titleText.getBytes());
                fileOutputStream1.flush();
                fileOutputStream.flush();
            }
        } catch (NullPointerException e) {
            System.out.println("全部爬取完成");
        }


    }
}
