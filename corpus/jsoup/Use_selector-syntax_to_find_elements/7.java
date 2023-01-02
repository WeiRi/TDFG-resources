package com.android.jsoupdemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jsoupdemo.HttpUtil.HttpUtility;
import com.android.jsoupdemo.Utility.CreateDialogUtil;
import com.android.jsoupdemo.Utility.SharedPreferencePageNum;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "liu-MainActivity";
    public static final String url = "http://www.baidu.com";
    public static String url1 = "https://free-api.heweather.com/v5/weather?city=PK1176615" + "&key=c239a3f3bdb94833b1df0f4081c2c940&lang=en";
    public static String url2 = "http://tool.chasfz.com/xiehouyu/?pageid=";
    private TextView textContent;
    private CreateDialogUtil dialogUtil;
    private Button prePage, nextPage;
    private TextView pageNumText;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogUtil = new CreateDialogUtil(MainActivity.this);
        textContent = (TextView) findViewById(R.id.text_content);
        prePage = (Button) findViewById(R.id.pre_page);
        nextPage = (Button) findViewById(R.id.next_page);
        pageNumText = (TextView) findViewById(R.id.page_num);
        prePage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        JsoupRequest(url2, pageNum);
    }

    /**
     * jsoup request http
     *
     * @param address
     */
    private void JsoupRequest(final String address, final int num) {
        dialogUtil.createDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (num > 0) {
                    Connection connect = Jsoup.connect(address + num);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putString("pageNumber", num + "");
                    editor.apply();
                    try {
                        Document document = connect.get();
                        Elements table = document.select("table"); // table is html page tag
                        Elements td = table.select("td"); // td is html page content tag
                        int len = td.size();
                        StringBuffer str = new StringBuffer();
                        for (int i = 0; i < len; i = i + 2) {
                            String text = td.get(i).text();
                            str.append(text + "--------" + td.get(i + 1).text() + "\n" + "\n");
                        }
                        showResponse(str.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * use okhttp request result encoding error
     *
     * @param address
     */
    private void RequestData(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtility.sendHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "request is fail");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Document document = Jsoup.parse(string);
                        Elements table = document.select("table");
                        Elements td = table.select("td");
                        int len = td.size();
                        StringBuffer str = new StringBuffer();
                        for (int i = 0; i < len; i++) {
                            String text = td.get(i).text();
                            str.append(text + "\n");
                        }
                        showResponse(str.toString());
                    }
                });
            }
        }).start();
    }


    private void showResponse(final String content) {
        final String pageNumber = SharedPreferencePageNum.getPreferencesPageNum(MainActivity.this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textContent.setText(content);
                if (pageNumber != null) {
                    pageNumText.setText(pageNumber);
                }
                dialogUtil.dismissDialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_page:
                String currentPage = SharedPreferencePageNum.getPreferencesPageNum(MainActivity.this);
                int anInt = Integer.parseInt(currentPage);
                if (!currentPage.equals("1")) {
                    JsoupRequest(url2,anInt - 1);
                }
                break;

            case  R.id.next_page:
                String currentPage2 = SharedPreferencePageNum.getPreferencesPageNum(MainActivity.this);
                int anInt2 = Integer.parseInt(currentPage2);
                JsoupRequest(url2,anInt2 + 1);
                break;
        }
    }
}
