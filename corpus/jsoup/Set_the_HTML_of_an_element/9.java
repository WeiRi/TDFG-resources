package com.example.jsoup.jsoupdemo.ui.home;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.ArrayMap;
import android.util.Log;

import com.example.jsoup.jsoupdemo.R;
import com.example.jsoup.jsoupdemo.base.activity.BaseMainActivity;
import com.example.jsoup.jsoupdemo.net.ApiStores;
import com.example.jsoup.jsoupdemo.net.AppClient;
import com.example.jsoup.jsoupdemo.net.RetrofitCallback;
import com.example.jsoup.jsoupdemo.ui.fragment.VpSimpleFragment;
import com.example.jsoup.jsoupdemo.view.indicator.ViewPagerIndicator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class MainActivity extends BaseMainActivity {

    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private ArrayList<Map<String, String>> maps = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    @BindView(R.id.my_pager_indicator)
    ViewPagerIndicator mViewPagerIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initfindViewById() {

//        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.my_pager_indicator);
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        List<String> list = new ArrayList<>();
        list.add("火锅");
        list.add("清时明月");
        list.add("清时明月汉时关");
        list.add("清时明月汉时关万里长征");
        list.add("清时明月汉时关万里长征人未还");
        list.add("天天");
        list.add("刚刚好1");
        list.add("刚刚好2");
        list.add("刚刚好3");
        list.add("刚刚好4");
        list.add("刚刚好5");
        list.add("刚刚好6");
        list.add("刚刚好7");
        mViewPagerIndicator.setmTitles(list);
        initDatas(list);

        mViewPager.setAdapter(mAdapter);
        mViewPagerIndicator.setViewPager(mViewPager, 0);
    }

    private void initDatas(List<String> mDatas) {

        for (String data : mDatas) {
            VpSimpleFragment fragment = VpSimpleFragment.newInstance(data);
            mTabContents.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
    }

    @Override
    public void loadData() {

        ApiStores apiStores = AppClient.mRetrofit.create(ApiStores.class);

        Call<ResponseBody> call = apiStores.loadMainData();
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String doc = new String(model.bytes(), "UTF-8");
                    Log.e("TAG", doc);

                    Document document = Jsoup.parse(doc);
                    Log.e("document", document.html());
                    Elements elements = document.getElementsByClass("list-item bg1 b1 boxshadow");
                    for (Element e : elements) {
                        Map<String, String> map = new ArrayMap<String, String>();
                        /**
                         * 图片
                         */
                        Elements images = e.getElementsByClass("content-img clearfix pt10 relative");
                        for (Element image : images) {
                            Elements imgElement = image.getElementsByTag("img");
                            for (Element img : imgElement) {
//                                imgs.add(img.attr("src"));
                                map.put("image", img.attr("src"));
                            }
                        }
                        Elements txtElement = e.getElementsByClass("dp-b");
                        for (Element txt : txtElement) {
                            Elements url = txt.getElementsByTag("a");
                            for (Element a : url) {
                                map.put("url", a.attr("href"));
                                map.put("text", a.text());

                            }
                        }
                        maps.add(map);
                    }

                    Log.e("TAG", maps.size() + "----");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onThrowable(Throwable t) {

            }

            @Override
            public void onFinish() {

            }
        });

    }
}
