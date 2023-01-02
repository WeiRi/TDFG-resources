package com.yogie.spider;

import com.yogie.domain.Essay;
import com.yogie.utils.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @program: JsoupDemo
 * @Date: 2019/4/11 9:38
 * @Author: Chenyogie
 * @Description: 爬虫控制器，根据url获取内容
 */
public class EssaySpider {

    /**
     * 获取url地址对应的页面的文档对象
     * @param url 属性
     * @return 文档对象
     */
    public Document getDoc(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        //获取到整页的html元素
        //String title = doc.html();
        return doc;
    }

    /**
     * 定义一个方法，根据题目的url地址去获取每一章的内容
     * @param url 每一章节对应的url地址
     * @return
     * @throws IOException
     */
    public String getText(String url) throws IOException {
        String str = "";
        //获取到url的文档对象
        Document doc = getDoc(url);
        //获取当前页面的html描述
        String html = doc.html();
        //获取到id为htmlContent的标签的集合（这里只有一个htmlContent）
        Elements els = doc.select("#htmlContent");
        //遍历标签集合
        for (Element e:els){
            //获取章节text内容
            //str += e.text();
            str = e.text();
            //System.out.println(e.text());
        }
        return str;
    }

    /**
     * 根据文档对象获取当前文章实体并写入本地
     * @param doc 文档对象
     * @return 文章对象列表
     * @throws IOException
     */
    public void writeEssay(Document doc) throws IOException {
        //获取到章节目录对应的a标签列表，并用Elements集合中
        Elements els = doc.select("li a");
        for (Element e:els) {
            //实例化一个文章对象
            Essay essay = new Essay();
            //获取文章的标题
            String tile = e.text();
            essay.setTitle(tile);
            //获取文章的链接
            essay.setHref(e.attr("href"));
            //根据文章的链接去获取文章的内容
            String text = getText(essay.getHref());
            //将文章的内容添加到essay对象中
            essay.setText(text.substring(14));
            //爬取一个文章就写到本地
            new FileUtils().writeToFile(essay);
        }
    }
}
