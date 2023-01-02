package mmjpg;


import download.DownLoadUtils;
import huihui.ImageItem;
import huihui.MMImageBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.rmi.runtime.Log;
import utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavin on 2017/10/31.
 */
public class MeiZiImageApi {

    private static String RAGEX_IMAGE = ".jpg";


    private static String MZ_TYPE_HOME="";
    private static String MZ_TYPE_XING_GAN="tag/xinggan";
    private static String MZ_TYPE_MEI_TUI="tag/meitui";
    private static String MZ_TYPE_DAXIONG_NVSHEN="tag/daxiongnvshen";
    private static String MZ_TYPE_RO_SI="tag/rosi";
    private static String MZ_TYPE_MEI_XIONG="tag/meixiong";
    private static String MZ_TYPE_XIAO_QING_XIN="tag/xiaoqingxin";
    private static String MZ_TYPE_MEI_TUN="tag/meitun";
    private static String MZ_TYPE_SIFANG_XIEZHEN="tag/sifangxiezhen";

    private static String MZ_TYPE_DI_SI="tag/disi";
    private static String MZ_TYPE_TONGYAN_JURU="tag/tongyanjuru";
    private static String MZ_TYPE_MEI_YI="tag/neiyi";

    public static final String [] MZ_IMAGE_TYPE={MZ_TYPE_HOME,MZ_TYPE_XING_GAN,MZ_TYPE_MEI_TUI,MZ_TYPE_DAXIONG_NVSHEN,
            MZ_TYPE_RO_SI,MZ_TYPE_MEI_XIONG,MZ_TYPE_XIAO_QING_XIN,MZ_TYPE_MEI_TUN,MZ_TYPE_SIFANG_XIEZHEN,
            MZ_TYPE_DI_SI,MZ_TYPE_TONGYAN_JURU,MZ_TYPE_MEI_YI};


    public static final String [] MZ_IMAGE_TYPE_NAME={"首页","性感美女","丝袜美腿","大胸女神","肉丝写真","美胸图片",
    "清纯美女","美臀图片","私房写真","第四印象","童颜巨乳","内衣图片"};


    private static final String HOME_URL="http://www.mmjpg.com/";




    public static void getHome(){

        try {
            Document document = Jsoup.connect("http://www.mmjpg.com/").get();

            System.out.println("document:"+document.toString());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //http://www.mmjpg.com/tag/xinggan/2
    //http://www.mmjpg.com/home/2





    public static List<MMImageBean>  getMZImagesFromType(int page, String flag) throws IOException {

        String url="";


        if (page==1){

            url=String.format("%s%s",HOME_URL,flag);
        }else {

            if (flag.equals("")){

                url=String.format("%shome/%d",HOME_URL,page);

            }else {

                url=String.format("%s%s/%d",HOME_URL,flag,page);

            }
        }


        System.out.println("URL:"+url);


            Document document = Jsoup.connect(url).get();

        return getImages(document);

        //return null;
    }



    public static List<MMImageBean> getImages(Document document) throws IOException {
        Elements mainElements = document.getElementsByClass("pic");
        Element mainElement = mainElements.get(0);
        Element ulElement = mainElement.select("ul").get(0);
        Elements liElements = ulElement.select("li");
        List<MMImageBean> mmImageBeans=new ArrayList<>(liElements.size());
        for (Element liElement : liElements) {
            Element aElement = liElement.select("a").get(0);
            Element spanElement = liElement.select("span").get(1);
            String url=  aElement.attr("href");
            String updateTime = spanElement.text();
            Elements img = aElement.getElementsByTag("img");
            String imageUrl= img.attr("src");
            String title=img.attr("alt");
            MMImageBean mmImageBean =new MMImageBean(imageUrl,title);
            mmImageBean.setUpdateTime(updateTime);
            Document mItemDocument = Jsoup.connect(url).get();
            Element page = mItemDocument.getElementById("page");
            Elements aPageElements = page.select("a");
            Element element = aPageElements.get(aPageElements.size()-2);
            int num = Integer.valueOf(element.text());
            List<String> itemUrls=new ArrayList<>(num);
            for (int i = 1; i <= num; i++) {
                String newstr = String.format("/%d.jpg",i);
                String s=  imageUrl.replace(RAGEX_IMAGE,newstr);
                itemUrls.add(s.replaceAll("/small",""));
            }
            mmImageBean.setPage(num);
            mmImageBean.setItems(itemUrls);
            mmImageBeans.add(mmImageBean);
        }
        return mmImageBeans;
    }


    private static String  getImage(String url){

        String image="";

        try {
            Document mItemDocument = Jsoup.connect(url).get();

            // System.out.println("document = [" + mItemDocument.toString() + "]");


            Element page = mItemDocument.getElementById("page");
            Elements aPageElements = page.select("a");
            Element element = aPageElements.get(aPageElements.size()-2);
            int num = Integer.valueOf(element.text());

            Element content = mItemDocument.getElementById("content");

            Elements aElements = content.select("a");

            Elements img = aElements.get(0).getElementsByTag("img");

            String imageUrl= img.attr("src");
            String title=img.attr("alt");

            image=imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    public static void getDownLoadImages(String url){



        try {
            Document mItemDocument = Jsoup.connect(url).get();

           // System.out.println("document = [" + mItemDocument.toString() + "]");



            Element page = mItemDocument.getElementById("page");
            Elements aPageElements = page.select("a");
            Element element = aPageElements.get(aPageElements.size()-2);
            int num = Integer.valueOf(element.text());

            Element content = mItemDocument.getElementById("content");

            Elements aElements = content.select("a");

            Elements img = aElements.get(0).getElementsByTag("img");

            String imageUrl= img.attr("src");
            String title=img.attr("alt");





            List<ImageItem> imageItems=null;

            System.out.println("原始image:"+imageUrl);
            if (num<=18){

                imageItems=new ArrayList<>(num);



                for (int i = 1; i <= num; i++) {

                    String s= getImage(String.format("%s/%d",url,i));
                    ImageItem imageItem=new ImageItem(s,title);

                    imageItems.add(imageItem);
                }

            }else {


                imageItems=new ArrayList<>(18);

                for (int i = 1; i <= 18; i++) {
                    String s= getImage(String.format("%s/%d",url,i));
                    ImageItem imageItem=new ImageItem(s,title);

                    imageItems.add(imageItem);
                }

            }

            //http://img.mmjpg.com/2017/1174/1.jpg

            //http://img.mmjpg.com/2017/1174/1/1.jpg
            //http://img.mmjpg.com/2017/1174/2.jpg
            //C:\Users\Y\Desktop\今日头条


            File file=new File(String.format("C:\\Users\\Y\\Desktop\\今日头条\\%s\\%s",
                    TimeUtil.getTimeShort(),imageItems.get(0).getTitle()));


            if (!file.exists()){

                file.mkdirs();
            }


            List<String> images=new ArrayList<>(imageItems.size());

            for (ImageItem data : imageItems) {

                images.add(data.getSrc());
            }


            System.out.println("imageItems = [" + imageItems.toString() + "]");//http://www.mmjpg.com/

            DownLoadUtils.downLoadImageReferrer(images,file.getAbsolutePath(),"http://www.mmjpg.com/");






        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("错误信息 = [" + e.toString() + "]");
        }

    }
}
