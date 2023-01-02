package com.hsc.jsoupdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "result---->";
    private SwipeRefreshLayout mSrlJianShu;
    private RecyclerView mRlvJianShu;
    
    private Document mDocument;
    private List<JianshuBeans> mbeans;
    private JianShuAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);

        mSrlJianShu = (SwipeRefreshLayout) findViewById(R.id.srl_jianshu);
        mRlvJianShu = (RecyclerView) findViewById(R.id.rv_jianshu);

        mbeans = new ArrayList<>();
        mAdapter = new JianShuAdapter(MainActivity.this);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mRlvJianShu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRlvJianShu.setAdapter(mAdapter);
        mRlvJianShu.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_title:
                    case R.id.tv_content:
                    case R.id.iv_primary:
                        Intent title = new Intent(MainActivity.this, ShowActivity.class);
                        title.putExtra("link", ((JianshuBeans) adapter.getItem(position)).getTitleLink());
                        startActivity(title);
                        break;
                    case R.id.iv_avatar:
                    case R.id.tv_author:
                        Intent author = new Intent(MainActivity.this, ShowActivity.class);
                        author.putExtra("link", ((JianshuBeans) adapter.getItem(position)).getAuthorLink());
                        startActivity(author);
                        break;
                    case R.id.tv_collectTag:
                        Intent collect = new Intent(MainActivity.this, ShowActivity.class);
                        collect.putExtra("link", ((JianshuBeans) adapter.getItem(position)).getCollectionTagLink());
                        startActivity(collect);
                        break;
                    default:
                        break;
                }
            }
        });
        
        jsoupData();
        mSrlJianShu.setColorSchemeColors(Color.RED, Color.YELLOW);
        mSrlJianShu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsoupData();
            }
        });
    }
    
    private void jsoupData(){
        mSrlJianShu.setRefreshing(true);
        mbeans.clear();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //找到文章列表模块，发现 <ul class="note-list></ul> 是我们需要信息的跟标签。
                    // 通过 select 方法查询节点所有信息。
                    //1.获取 Document 对象
                    mDocument = Jsoup.connect("http://www.jianshu.com/")
                            .timeout(10000)
                            .get();

                    //2.获取跟标签的 Elements 对象
                    Elements noteList = mDocument.select("ul.note-list");
                    Elements li = noteList.select("li");
                    for (Element element : li) {
                        JianshuBeans bean = new JianshuBeans();
                        bean.setAuthorName(element.select("div.name").text());//作者名字
                        bean.setAuthorLink(element.select("a.blue-link").attr("abs:href"));//作者首页链接
                        bean.setTime(timeChange(element.select("span.time").attr("data-shared-at")));//发表时间
                        bean.setPrimaryImg(element.select("img").attr("src"));//主图
                        //这个就是先找到头像 节点，然后图片节点，最后通过 attr 获取图片 url
                        bean.setAvatarImg(element.select("a.avatar").select("img").attr("src"));//头像

                        //通过 select 查询节点信息，然后.text 获取里面文本内容
                        bean.setTitle(element.select("a.title").text());//标题
                        bean.setTitleLink(element.select("a.title").attr("abs:href")); //标题链接
                        
                        bean.setContent(element.select("p.abstract").text());//内容
                        bean.setCollectionTagLink(element.select("a.collection-tag").attr("abs:href"));//专题链接
                        
                        //将获取到的数据添加到集合中
                        String[] text = element.select("div.meta").text().split(" ");//把text这个字符串根据空格分割
                        if (text[0].matches("[0-9]+")) {
                            bean.setLikeNum(text[0]);
                            bean.setTalkNum(text[1]);
                            bean.setTalkNum(text[2]);
                        } else {
                            bean.setCollectionTag(text[0]);
                            bean.setReadNum(text[1]);
                            bean.setTalkNum(text[2]);
                            bean.setTalkNum(text[3]);
                        }
                        mbeans.add(bean);
                        
                    }
                    
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setNewData(mbeans);
                            mSrlJianShu.setRefreshing(false);
                        }
                    });

                    Log.i(TAG, "run: " + mbeans.get(0).toString());


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "run: " + e.getMessage());
                }
            }
        }).start();
    }

    private String timeChange(String time){
        String[] ts = time.split("T");
        String[] split = ts[1].split("\\+");
        return ts[0] + "    " +  split[0];
    }
}
