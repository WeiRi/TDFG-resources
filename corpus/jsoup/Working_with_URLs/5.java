package com.example.a28062.jsoupts;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Handler handler;
    private List<MyBean> mdata ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdata = new ArrayList<>();
       listView = (ListView) findViewById(R.id.lv_mynews);
        myThread();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    NewAdapter newAdapter = new NewAdapter(MainActivity.this,mdata);
                    listView.setAdapter(newAdapter);
                }
            }
        };



    }

    public void myThread(){
        new Thread(){
            @Override
            public void run() {
              try{

                      Document doc = Jsoup.connect("http://www.qkankan.com/asia/korea/medium/").get();

                      Elements els = doc.select("div.slist");

                      Log.d("TAG", els.toString());

                      for (int j = 0 ;j<els.size();j++) {
                          String title = els.select("img").attr("alt");
                          String detail = els.select("p").text();
                          String detailUrl = els.select("a").attr("href");
                          String imageUrl = els.select("img").attr("src");
                          Log.e("TAG", "详情链接：" + els.get(j).select("a").attr("href"));
                          Log.e("TAG", "标题：" + els.get(j).select("img").attr("alt"));
                          Log.e("TAG", "地址：" + els.get(j).select("img").attr("src"));
                          Log.e("TAG", "内容：" + els.get(j).select("p").text());
                          MyBean news = new MyBean(detailUrl, imageUrl, title, detail);
                          mdata.add(news);

                      }
                  Message msg = new Message();
                  msg.what = 1;
                  handler.sendMessage(msg);
              }catch (Exception e){
                  Log.d("TAG",e.toString());
              }
            }
        }.start();
    }

//    class myThread2 implements Runnable{
//    @Override
//    public void run() {
//        try {
////                //从一个URL加载一个Document对象。
////                Document doc = Jsoup.connect("http://home.meishichina.com/show-top-type-recipe.html").get();
////                //选择“美食天下”所在节点
////                Elements titleAndPic = doc.select("div.pic");
////                //打印 <a>标签里面的title
////                Log.i("mytag","title:"+titleAndPic.get(0).select("a").attr("title")+"pic:"+titleAndPic.get(1).select("a").select("img").attr("data-src"));
////
////                Elements url = doc.select("div.detail").select("a");
////                Log.i("mytag","url:" + url.get(0).attr("href"));
////
////                Elements burden = doc.select("p.subcontent");
////                Log.i("mytag","burden"+burden.get(1).text());
//            }catch(Exception e) {
////                Log.i("mytag", e.toString());
//            }
//    }
//}
}
