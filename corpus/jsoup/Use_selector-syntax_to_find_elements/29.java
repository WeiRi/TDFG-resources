package com.mabrouk.jsoupexample;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GetSliderContentAsync extends AsyncTask<Void, Void, Void> {

    sliderImagesListener listener;

    String pageTitle = "";
    Elements images;


    List<String> imagesUrlList;
    List<String> imagesTitlesList;

    public GetSliderContentAsync(GetSliderContentAsync.sliderImagesListener listener) {
        this.listener = listener;

        imagesUrlList = new ArrayList<>();
        imagesTitlesList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //Connect to the website

            Document document = Jsoup.connect("http://www.natgeotv.com/ae").get();

            pageTitle = document.title();

            images = document.select("div.MasterContentDiv.WithBanner > div.MainContentDiv > div.MainPanel > div.Carousel > ul > li");

            Log.v("dsds","dsds");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Log.v("tagg", pageTitle);
        Log.v("tagg", images.size() + "");

        for (Element e : images) {
            // get image text
            Log.v("tagg", e.select("ul > li.ContentText > div.container > h2.Title").text());
            // get image link
            Log.v("tagg", e.select("ul > li.MainPanelImage > a > img").attr("src"));
            // get program link
            Log.v("tagg", e.select("ul > li.MainPanelImage > a").attr("href"));
            Log.v("tagg", "----------------------------------------------------------------------");


            imagesTitlesList.add(e.select("ul > li.ContentText > div.container > h2.Title").text());
            imagesUrlList.add(e.select("ul > li.MainPanelImage > a > img").attr("src"));
        }

        this.listener.onSliderImagesFetched(imagesUrlList, imagesTitlesList);
    }

    interface sliderImagesListener {
        void onSliderImagesFetched(List<String> imagesUrlList, List<String> imagesTitlesList);
    }
}
