package com.hayden.novel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static String USER_AGENT="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    public Document getDocument(String url){
        Document document=null;
        try {
            document=Jsoup.connect(url)
                    .header("User-Agent",USER_AGENT)
                    .ignoreContentType(true)
                    .timeout(5000).get();
//            try {
//                Thread.currentThread().sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     *
     * @param fileName 要保存的小说的名称
     * @param url 要保存的小说的url
     */
    public void getChapter(String fileName,String url){
        Document document=getDocument(url);
        Elements chapterUrls=document.select("#list>dl>dd a");
        List<String> urls=new ArrayList<String>();
        for(Element chapterUrl:chapterUrls){
           urls.add(chapterUrl.attr("abs:href"));
        }
        for(String chapterUrl:urls){
            parseChapter(fileName,chapterUrl);
        }
    }

    /**
     *解析每一章的小说
     * @param fileName 写入到的文件名
     * @param url 章节的地址
     */
    public void parseChapter(String fileName,String url){
        Document document=getDocument(url);
        String chapterName,chapterContent;
        chapterName=document.select("div.bookname>h1").text().trim();
        System.out.println("正在下载......"+chapterName);
        chapterContent=document.select("div#content").text().replace(" ","\n");
        chapterContent=chapterName+"\n"+chapterContent+"\n";
        writeChapterToFile(fileName,chapterContent);
    }

    private void writeChapterToFile(String fileName, String chapterContent) {
        //指定写入的位置
        String dirStr="E:\\test\\novel";
        File dir=new File(dirStr);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file=new File(dirStr+"\\"+fileName+".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("文件创建失败！！！");
                e.printStackTrace();
            }
        }
        try {
            FileWriter resultFile=new FileWriter(file,true);
            PrintWriter novelFile=new PrintWriter(resultFile);
            novelFile.println(chapterContent);
            novelFile.close();
            resultFile.close();
        } catch (IOException e) {
            System.err.println("文件写入失败！！！");
            e.printStackTrace();
        }
    }

}
