package background;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.print.Doc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import po.BasicPlayer;
import po.Player;
import po.SeasonPre;
import po.SeasonSum;
import utils.FormatUils;
import utils.HttpUtil;
import utils.JsopUtil;

public class GetNBABasicPlayer {
	static String u;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ApplicationContext ac=new ClassPathXmlApplicationContext("spring/spring-config.xml");
		final MongoTemplate mongoTemplate= (MongoTemplate) ac.getBean("mongoTemplate");
		final long start=System.currentTimeMillis();
		ExecutorService threadPool=Executors.newFixedThreadPool(100);
		

		
		FileReader fileReader=new FileReader(new File("E:\\workspace\\MyEclipse for Spring 2014\\NBA\\src\\main\\java\\background\\statu.txt"));
		BufferedReader br=new BufferedReader(fileReader);
		String url=null;
		while((url=br.readLine())!=null){
			final String idnum=url.replaceAll("./player/", "").replaceAll(".html", "");
			 u=url.substring(1);
			 try{
			//	 Thread.sleep(100);
				 getPlayInfo("http://www.stat-nba.com"+u,mongoTemplate,idnum);
			//	 System.out.print(HttpUtil.sendGet("http://www.stat-nba.com/player/4312.html"));
				 return;
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		/*	threadPool.submit(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						getPlayInfo("http://www.stat-nba.com"+u,mongoTemplate,idnum);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			);
			*/
		}
		
	}
	public static void getPlayInfo(String url,MongoTemplate mongoTemplate,String idnum) throws IOException{
		BasicPlayer player=new BasicPlayer();
		Document document=JsopUtil.getDocumentByGetMethod(url);
		player.setIdnum(idnum);
		player.setName(getPlayerName(document));
		
		player.setImg("http://www.stat-nba.com"+getPlayerImg(document));
		player.setWeizhi(getPlayerCommon(document, "位　　置:"));
		player.setHeight(getPlayerCommon(document, "身　　高:"));
		player.setWeight(getPlayerCommon(document, "体　　重:"));
		player.setBirthday(getPlayerCommon(document, "出生日期:"));
		player.setBirthplace(getPlayerCommon(document, "出生城市:"));
		player.setBiye(getPlayerCommon(document, "高　　中:"));
		player.setNum(getPlayerCommon(document, "球衣号码:"));
		player.setPick(getPlayerCommon(document, "选秀情况:"));
		System.out.print(player);
//		player.setName(getPlayerName(chilrds));
//		player.setAge(getPlayerAge(chilrds));
//		player.setBirthday(getPlayerBirthday(chilrds));
//		player.setBirthplace(getPlayerBirthplace(chilrds));
//		player.setWeight(getPlayerWeight(chilrds));
//		player.setHeight(getPlayerHeight(chilrds));
//		player.setEnternba(getPlayerEnternba(chilrds));
//		player.setPick(getPlayerPick(chilrds));
//		player.setSeasonPres(getPlaySeasonPres(chilrds));
//		player.setSeasonSums(getPlaySeasonSums(chilrds));
		
		mongoTemplate.save(player);
		
	}
	public static String  getPlayerName(Document document) {
		try{
			String name=document.getElementsByClass("name").get(0).text();
			name=name.replaceAll(" 百度百科资料>> wiki英文资料>>", "");
			System.out.println(name);
			return name;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerImg(Document document) {
		try{
			String src=document.getElementsByClass("image").get(0).getElementsByTag("img").get(0).attr("src");
			System.out.println(src);
			return src;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String getPlayerCommon(Document document,String col){
		try{
			Element detail=document.getElementsByClass("detail").get(0);
			Elements rows=detail.getElementsByClass("row");
			for(int i=0;i<rows.size();++i){
				String str=rows.get(i).text();
				if(str.contains(col))
					return str.replaceAll(col,"" ).replaceAll(" 详情", "");
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	public static String  getPlayerAge(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String age= tbody.getElementsByTag("tr").get(0).getElementsByTag("td").get(4).text();
			System.out.println(age);
			return age;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerBirthday(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String birthday= tbody.getElementsByTag("tr").get(0).getElementsByTag("td").get(2).text();
			System.out.println(birthday);
			return birthday;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerBirthplace(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String birthplace= tbody.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();
			System.out.println(birthplace);
			return birthplace;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerWeight(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String weight= tbody.getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text();
			System.out.println(weight);
			return weight;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerHeight(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String height= tbody.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();
			System.out.println(height);
			return height;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerEnternba(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String enternba= tbody.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();
			System.out.println(enternba);
			return enternba;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerPick(Elements chilrds) {
		try{
			Element tbody=chilrds.get(1).getElementsByTag("tbody").get(0);
			String pick= tbody.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text();
			System.out.println(pick);
			return pick;
		}catch(Exception e){
			return null;
		}
		
	}
	public static ArrayList<SeasonPre> getPlaySeasonPres(Elements chilrds) {
		try{
			ArrayList<SeasonPre> list=new ArrayList<>();
			Element tbody=chilrds.get(4).getElementsByTag("tbody").get(0);
			Elements trs=tbody.getElementsByTag("tr");
			
			for(int i=1;i<trs.size();++i){
				Element line=trs.get(i);
				SeasonPre seasonPre=new SeasonPre();
				seasonPre.setSeason(line.child(0).text());
				System.out.println(seasonPre.getSeason());
				
				seasonPre.setTeam(line.child(1).text());
				System.out.println(seasonPre.getTeam());
				
				seasonPre.setChuchang(Integer.parseInt(line.child(2).text()));
				System.out.println(seasonPre.getChuchang());
				
				seasonPre.setShoufa(Integer.parseInt(line.child(3).text()));
				System.out.println(seasonPre.getShoufa());
				
				seasonPre.setShijian(Float.parseFloat(line.child(4).text()));
				System.out.println(seasonPre.getShijian());
								
				seasonPre.setToulan(FormatUils.FloatDelBaifen(line.child(5).text()));
				System.out.println(seasonPre.getToulan());
								
				seasonPre.setSanfen(FormatUils.FloatDelBaifen(line.child(6).text()));
				System.out.println(seasonPre.getSanfen());
				
				seasonPre.setFaqiu(FormatUils.FloatDelBaifen(line.child(7).text()));
				System.out.println(seasonPre.getFaqiu());
				
				seasonPre.setQianlanban(FormatUils.FloatDelBaifen(line.child(8).text()));
				System.out.println(seasonPre.getQianlanban());
				
				seasonPre.setHoulanban(FormatUils.FloatDelBaifen(line.child(9).text()));
				System.out.println(seasonPre.getHoulanban());
				
				seasonPre.setZonglanban(FormatUils.FloatDelBaifen(line.child(10).text()));
				System.out.println(seasonPre.getZonglanban());
				
				seasonPre.setCugong(FormatUils.FloatDelBaifen(line.child(11).text()));
				System.out.println(seasonPre.getCugong());
				
				seasonPre.setQiangduan(FormatUils.FloatDelBaifen(line.child(12).text()));
				System.out.println(seasonPre.getQiangduan());
				
				seasonPre.setGaimao(FormatUils.FloatDelBaifen(line.child(13).text()));
				System.out.println(seasonPre.getGaimao());
				
				seasonPre.setShiwu(FormatUils.FloatDelBaifen(line.child(14).text()));
				System.out.println(seasonPre.getShiwu());
				
				seasonPre.setFangui(FormatUils.FloatDelBaifen(line.child(15).text()));
				System.out.println(seasonPre.getFangui());
				
				seasonPre.setDefen(FormatUils.FloatDelBaifen(line.child(16).text()));
				System.out.println(seasonPre.getDefen());
				
				list.add(seasonPre);
			}
			return list;
		}catch(Exception e){
			return null;
		}
	}
	public static ArrayList<SeasonSum> getPlaySeasonSums(Elements chilrds) {
		try{
			ArrayList<SeasonSum> list=new ArrayList<>();
			Element tbody=chilrds.get(4).getElementById("S_Cont_02").getElementsByTag("tbody").get(0);
			Elements trs=tbody.getElementsByTag("tr");
			
			for(int i=1;i<trs.size();++i){
				Element line=trs.get(i);
				SeasonSum seasonPre=new SeasonSum();
				seasonPre.setSeason(line.child(0).text());
				System.out.println(seasonPre.getSeason());
				
				seasonPre.setTeam(line.child(1).text());
				System.out.println(seasonPre.getTeam());
				
				seasonPre.setChuchang(Integer.parseInt(line.child(2).text()));
				System.out.println(seasonPre.getChuchang());
				
				seasonPre.setShoufa(Integer.parseInt(line.child(3).text()));
				System.out.println(seasonPre.getShoufa());
				
				seasonPre.setShijian(Integer.parseInt(line.child(4).text()));
				System.out.println(seasonPre.getShijian());
					
				String[] a=line.child(5).text().split("-");
				seasonPre.setToulanmingzhong(Integer.parseInt(a[0]));
				seasonPre.setToulancishu(Integer.parseInt(a[1]));
				
				a=line.child(6).text().split("-");
				seasonPre.setSanfenmingzhong(Integer.parseInt(a[0]));
				seasonPre.setSanfencishu(Integer.parseInt(a[1]));
				
				a=line.child(7).text().split("-");
				seasonPre.setFaqiumingzhong(Integer.parseInt(a[0]));
				seasonPre.setFaqiucishu(Integer.parseInt(a[1]));
				
				seasonPre.setQianlanban(Integer.parseInt(line.child(8).text()));
				System.out.println(seasonPre.getQianlanban());
				
				seasonPre.setHoulanban(Integer.parseInt(line.child(9).text()));
				System.out.println(seasonPre.getHoulanban());
				
				seasonPre.setZonglanban(Integer.parseInt(line.child(10).text()));
				System.out.println(seasonPre.getZonglanban());
				
				seasonPre.setCugong(Integer.parseInt(line.child(11).text()));
				System.out.println(seasonPre.getCugong());
				
				seasonPre.setQiangduan(Integer.parseInt(line.child(12).text()));
				System.out.println(seasonPre.getQiangduan());
				
				seasonPre.setGaimao(Integer.parseInt(line.child(13).text()));
				System.out.println(seasonPre.getGaimao());
				
				seasonPre.setShiwu(Integer.parseInt(line.child(14).text()));
				System.out.println(seasonPre.getShiwu());
				
				seasonPre.setFangui(Integer.parseInt(line.child(15).text()));
				System.out.println(seasonPre.getFangui());
				
				seasonPre.setDefen(Integer.parseInt(line.child(16).text()));
				System.out.println(seasonPre.getDefen());
				
				list.add(seasonPre);
			}
			return list;
		}catch(Exception e){
			return null;
		}
	}
}
