package com.example.dpl.jsoupdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

/*
1.使用Jsoup解析html新闻列表
2.使用Jsoup解析Epub
 */
public class MainActivity extends AppCompatActivity {
    private String url="http://mobile.csdn.net/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseHtml();//1.使用Jsoup解析html新闻列表
                //parseEpub();//2.使用Jsoup解析Epub(电子书)
            }
        }).start();
    }

    private void parseEpub() {
        try {
            InputStream is=getAssets().open("fb.ncx");//获取assets文件的输入流
            int size=is.available();//定义缓冲的大小
            byte[] buffer=new byte[size];//定义缓冲区
            is.read(buffer);//将所有内容读入缓冲区
            is.close();//输入流关闭
            String ePubText=new String(buffer,"UTF-8"); //用缓冲区中的内容创建字符串
            Document document1=Jsoup.parse(ePubText);//Document 对象代表整个 XML 文档
            String docTitle=document1.getElementsByTag("docTitle").first().text();//获取节点docTitle第一条text内容
            Log.i("info",docTitle);
            Elements elements=document1.getElementsByTag("navPoint");//获取节点navPoint
            for (Element ele1:elements){//遍历
                String title=ele1.text();//获取节点navPoint中的text内容
                String href=ele1.getElementsByTag("content").first().attr("src");//获取节点content第一条链接
                Log.i("info",title+":"+href);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseHtml() {
        try {
            Document document= Jsoup.connect(url).get();//get方式获取数据
            Elements elements=document.select("div.unit");//获取节点unit
            for (Element ele:elements){
                String title=ele.getElementsByTag("h1").first().text();//获取节点unit内第一条h1标题
                String href=ele.getElementsByTag("h1").first()
                        .getElementsByTag("a").first().attr("href");//获取节点unit内第一条h1超级链接
                Log.i("info",title+":"+href);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
