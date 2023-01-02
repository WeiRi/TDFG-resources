package com.example.ge.jsoupdemo;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final String T = "MainAcitivity";
    private String feed;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private long myReference;
    private DownloadManager downloadManager;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (reference == myReference) {
                DownloadManager.Query query=new DownloadManager.Query();
                query.setFilterById(reference);
                Cursor myDownlod = downloadManager.query(query);
                if (myDownlod.moveToFirst()) {
                    int nameID = myDownlod.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    int uriID = myDownlod.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String name = myDownlod.getString(nameID);
                    String uri = myDownlod.getString(uriID);
                    Log.d("name:uri", name + ":" + uri);
                }
                myDownlod.close();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(T, "onDestroy()");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        arrayList.add("网站链接");
        final TextView textView_connect_status = (TextView) findViewById(R.id.textview_connect_status);
        Button button_connect = (Button) findViewById(R.id.button_connect);
        Button button_download = (Button) findViewById(R.id.button_download);
        feed = getString(R.string.feed);
        final AsyncTask<String,Integer,String> asyncTask_download=new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                /*下载链接*/
                String link=params[0];
                String result="";
                int status=0;
                //下载状态
                publishProgress(status);
                /*进行下载*/
                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                registerReceiver(broadcastReceiver, intentFilter);
                download(link);
                return result;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (values[0] == 0) {
                    textView_connect_status.setText("Downloading");
                }else if(values[0]==1){

                }
            }
        };
        final AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                String connectStatus = s;
                textView_connect_status.setText(connectStatus);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_activated_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }

            @Override
            protected String doInBackground(String... params) {
                String connectStatus = "not sure";
                Log.d(T, params[0]);
                String result = "";
                Boolean flag = refreshEarthquakes(params[0]);
                if (flag) {
                    connectStatus = "WebSite bupt connected";
                                /*处理信息流，解析HTML*/
                    /*模拟信息流*/

/*                    String html = "<html><head><title>First parse</title></head>"
                            + "<body><p>Parsed HTML into a doc.</p></body></html>";
                    Document document = Jsoup.parse(html);
                    String body = document.getElementsByTag("body").toString();
                    Log.d(T, body);
                    String head = document.getElementsByTag("head").toString();
                    Log.d(T, head);
                    */

                    String feed = getString(R.string.feed);
                    try {
                        Connection connection = Jsoup.connect(feed);
                        Document document = connection.get();
                        String html = document.html();
                        Document doc = Jsoup.parse(html, feed);
                        /*采用Jsoup得到document*/
                        Elements links = doc.getElementsByTag("a");
                        for (Element link : links) {
                            String strLink = link.attr("href");
                            if (strLink.startsWith("/")) {
                                String msg = link.attr("abs:href");
                                Log.d(T, msg);
                                arrayList.add(msg);
                            } else if (strLink.startsWith("http")) {
                                String msg = link.attr("href");
                                Log.d(T, msg);
                                arrayList.add(msg);
                            }

                            //            Log.d(T, strLinkText);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    connectStatus = "webSite bupt no response";
                }
                return connectStatus;

            }
        };
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask.execute(feed);
            }
        });
        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = getString(R.string.download_link);
                asyncTask_download.execute(link);
            }
        });
    }

    private void download(String link){
        String feed=link;
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(feed);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        /*可以设置request属性，来确定下载的位置，名称，特性等*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       /*以下注释之间的代码，去掉则默认下载位置，没有错误。*/
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path);
        Log.d("Location:", path);
        request.setDestinationUri(Uri.fromFile(file));
        /*这部分是设置路径的，尚未解决。*/
        request.setTitle("百度音乐");
        request.setDescription("百度音乐APP");
        /**********************************************/

        myReference=downloadManager.enqueue(request);
        DownloadManager.Query failedDownloadQuery=new DownloadManager.Query();
        failedDownloadQuery.setFilterByStatus(DownloadManager.STATUS_FAILED);
        Cursor failedDownload = downloadManager.query(failedDownloadQuery);
        int reasonID = failedDownload.getColumnIndex(DownloadManager.COLUMN_REASON);
        while (failedDownload.moveToNext()) {
            int reason = failedDownload.getInt(reasonID);
            Log.d("Reason:", Integer.toString(reason));
            /*错误状态1001*/
            /*错误为ERROR_FAIL_ERROR*/
            /*这部分在安卓文件学习中尝试解决*/
        }
        failedDownload.close();
        //Log.d(T, Long.toString(reference));


    }
    private boolean refreshEarthquakes(String feed) {
        URL url;
        String quakeFeed = feed;
        boolean b = false;
        try {
            url = new URL(quakeFeed);
            URLConnection connection;
            connection = url.openConnection();

            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            int responseCode = httpURLConnection.getResponseCode();
            b = responseCode == HttpURLConnection.HTTP_OK;
            Log.d(T, Boolean.toString(b));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;


    }


}
