package background;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.dao.GetPlayerOnLineDAO;

import po.Player;
import po.SeasonPre;
import po.SeasonSum;
import utils.FormatUils;
import utils.JsopUtil;

public class GetNBAStatus {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ApplicationContext ac=new ClassPathXmlApplicationContext("spring/spring-config.xml");
		final MongoTemplate mongoTemplate= (MongoTemplate) ac.getBean("mongoTemplate");
		final long start=System.currentTimeMillis();
		ExecutorService threadPool=Executors.newCachedThreadPool();
		ArrayList<String> u1s=new ArrayList<>();
		for(char ci='A';ci<='Z';ci+=1){
			for(char cj='A';cj<='Z';cj+=1)
				u1s.add("http://www.stat-nba.com/playerList.php?il="+ci+"&lil="+cj);
		}
		final String host="http://www.stat-nba.com/playerList.php?il=M&lil=B";
	/*	for(String u1:u1s){
			Document document=JsopUtil.getDocumentByGetMethod(u1);
			System.out.println(document.text());
			Elements elements=document.getElementsByClass("normalplayer");
			for(int i=0;i<elements.size();++i){
				if(elements.get(i).attr("href").contains("player"))
					System.out.println(elements.get(i).attr("href"));
			}
		}
		*/
		GetPlayerOnLineDAO getPlayerOnLineDAO=(GetPlayerOnLineDAO) ac.getBean("getPlayerOnLineDAO");
		getPlayerOnLineDAO.getTableByURL_ID("http://www.stat-nba.com/player/stat_box/1862_playoff.html","stat_box_avg" );
		/*	Elements trs=document.getElementsByTag("tr");
		ArrayList<String> playurls=new ArrayList<>();
		for(int i=0;i<trs.size();++i)
			try{
			
				Element id=trs.get(i).getElementById("playerslist");
				Elements a=id.getElementsByTag("a");
				for(int j=0;j<a.size();++j)
					playurls.add(a.get(j).attr("href"));
			}catch(Exception e){}*/
		//System.out.print(playurls.size());
		/*for(String pl:playurls){
			final String pp=pl;
			threadPool.execute( new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						getPlayInfo(host+pp,mongoTemplate);
						System.out.println("time:"+(System.currentTimeMillis()-start));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		});
		}
		
			*/
		
	}
	public static void getPlayInfo(String url,MongoTemplate mongoTemplate) throws IOException{
		Player player=new Player();
		Document play1Document=JsopUtil.getDocumentByGetMethod(url);
		Element left=play1Document.getElementById("left");
		
		Elements chilrds =left.children();
		
		player.setImg(getPlayerImg(chilrds));
		player.setName(getPlayerName(chilrds));
		player.setAge(getPlayerAge(chilrds));
		player.setBirthday(getPlayerBirthday(chilrds));
		player.setBirthplace(getPlayerBirthplace(chilrds));
		player.setWeight(getPlayerWeight(chilrds));
		player.setHeight(getPlayerHeight(chilrds));
		player.setEnternba(getPlayerEnternba(chilrds));
		player.setPick(getPlayerPick(chilrds));
		player.setSeasonPres(getPlaySeasonPres(chilrds));
		player.setSeasonSums(getPlaySeasonSums(chilrds));
		
		mongoTemplate.save(player);
		
	}
	public static String  getPlayerName(Elements chilrds) {
		try{
			String name=chilrds.get(0).getElementsByTag("strong").get(0).text();
		
			System.out.println(name);
			return name;
		}catch(Exception e){
			return null;
		}
		
	}
	public static String  getPlayerImg(Elements chilrds) {
		try{
			String src=chilrds.get(1).getElementsByTag("tbody").get(0).getElementsByTag("img").get(0).attr("src");
		
			System.out.println(src);
			return src;
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
