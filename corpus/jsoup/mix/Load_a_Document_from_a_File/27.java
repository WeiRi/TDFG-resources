package cricinfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PlayerData {
	
	static Elements content_head;
	static Elements content_body;
	static JSONObject jsonleague = new JSONObject(); //Top most json
	static JSONArray jsonteamarray = new JSONArray(); //Array of teams (8)
	static JSONObject eachteam = new JSONObject(); // Each team is a json object with team name and players array
	static JSONArray playersarray = new JSONArray(); //Contains an array of all the players in a particular team
	static JSONObject playerobject = new JSONObject(); //Each object in players array
	static JSONArray playerprofile = new JSONArray(); //Array of profile objects of each player (Tests,OneDays,T20I etc)
	static JSONObject playerprofiledata = new JSONObject();
	static JSONArray eachprofilearray = new JSONArray();
	static JSONObject eachprofiledata = new JSONObject();
	static ArrayList<String> teamname = new ArrayList<String>();
	static ArrayList<String> teamurl = new ArrayList<String>();
	static int count=0;

	public static void main(String[] args) {
		
		jsonleague.put("league", "Indian Premier League");
		scrapeTopic("http://www.espncricinfo.com/indian-premier-league-2015/content/series/791129.html");

	}
	
	public static void scrapeTopic(String url){
		
		String html = getUrl(url);
		Document doc = Jsoup.parse(html);
		Elements content = doc.select("#subnav_tier1>li").get(9).select(".subnav_tire2>li>a");
		jsonteamarray = new JSONArray();
		
		System.out.println(content.size());
		for(int i=0; i<content.size(); i++){
			count=i;
			teamname.add(i, content.get(i).text());
			teamurl.add(i, "http://www.espncricinfo.com/"+content.get(i).attr("href"));
			eachteam= new JSONObject();
			eachteam.put("teamname", teamname.get(i));
			//System.outprintln(eachteam.toString());
			System.out.println(teamname.get(i));
			scrapeTeam(teamurl.get(i));
			jsonteamarray.add(eachteam);
		}
		//System.outprintln("Topic scrape success");
		jsonleague.put("info", jsonteamarray);
		try{
			File file = new File("E:/ipldata.txt");
			 
			if (!file.exists()) {
				file.createNewFile();
			}
	
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(jsonleague.toString());
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void scrapeTeam(String url){
		
		String html = getUrl(url);
		Document doc = Jsoup.parse(html);
		Elements content = doc.select("div.large-13.medium-13.small-13.columns>h3>a");
		ArrayList<String> playername = new ArrayList<String>();
		ArrayList<String> playerurl = new ArrayList<String>();
		
		playersarray = new JSONArray();
		
		System.out.println(content.size());
		for(int i=0; i<content.size(); i++){
			
			playername.add(i, content.get(i).text());
			playerurl.add(i, "http://www.espncricinfo.com/"+content.get(i).attr("href"));
			
			playerobject = new JSONObject();
			playerobject.put("name",playername.get(i));
			//System.outprintln(playername.get(i));
			scrapePlayer(playerurl.get(i));
			playersarray.add(playerobject);
			
		}
		
		eachteam.put("data",playersarray);
		
		try{
			File file = new File("E:/ipldata_"+count+".txt");
			 
			if (!file.exists()) {
				file.createNewFile();
			}
	
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(eachteam.toString());
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.outprintln("Team Scrape Success");
		
	}
	
	public static void scrapePlayer(String url){
		
		String html = getUrl(url);
		Document doc = Jsoup.parse(html);
		
		content_head = doc.select("table").select("thead").first().getElementsByTag("th");
		content_body = doc.select("table.engineTable").select("tbody").first().getElementsByTag("tr");
		
		playerprofile = new JSONArray();
		
		for(int i=0; i<content_body.size(); i++){
			playerprofiledata = new JSONObject();
			playerprofiledata.put("name", content_body.get(i).getElementsByTag("td").get(0).text());
			eachprofilearray = new JSONArray();
			eachprofiledata = new JSONObject();
			//System.outprintln(content_body.get(i).getElementsByTag("td").get(0).text());
			
			if(i != content_head.size()){
				for(int j=1; j<content_body.get(i).toString().split("nowrap>").length-1; j++){
					eachprofiledata.put(content_head.get(j).text(), content_body.get(i).getElementsByTag("td").get(j).text());
					//System.outprintln("----------------"+content_head.get(j).text());
					//System.outprintln("==================="+content_body.get(i).getElementsByTag("td").get(j).text());
				}
			}
			
			eachprofilearray.add(eachprofiledata);
			playerprofiledata.put("data", eachprofilearray);
			playerprofile.add(playerprofiledata);
		}
		
		playerobject.put("profile", playerprofile);
		//System.outprintln("Player Scrape Success");
	}
	
	public static String getUrl(String url){
		
		URL urlObj = null;
		
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			//System.outprintln("The url is malformed");
			e.printStackTrace();
			return "";
		}
		
		String outputText = "" ;
		
		try{
			//System.outprintln(urlObj.toString());

			HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
			////System.outprintln(urlCon.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line="";
			//System.outprintln(in.toString());
			
			while((line= in.readLine())!=null ){
				
				outputText += line;
			}
			
			//System.outprintln("url opened");
			in.close();
		}
		
		catch(IOException e){
			//System.outprintln("There was an error connecting to the URL");
			e.printStackTrace();
			return e.toString();
		}
		
		return outputText;
		
	}

}
