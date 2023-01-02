package org.twt.thread;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.twt.entity.NoteDetail;
import org.twt.entity.NoteDetailVO;
import org.twt.entity.User;
import org.twt.util.CrawlerUtils;

public class CrawlerThread02 implements Runnable {
	private Connection conn = null;

	@Override
	public void run() {
		Document doc = null;
		conn = getMysqlConnection("127.0.0.1", 3306, "crawler_education", "root", "root");
		final String sql = "select url from note_list where modual_id="+flag;
		Statement st = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("url"));
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
//		list.add("http://www.19lou.com/forum-263-thread-18881456803855883-1-1.html");
//		list.add("http://www.19lou.com/forum-263-thread-18981457262828109-1-1.html");
		int size = list.size();
		System.out.println(flag +"of size:" + size);
		try {
			for (int i = 0; i < size; i++) {
				NoteDetailVO vo = new NoteDetailVO();
				HashMap<Integer, User> userMap = new HashMap<Integer, User>();
				List<NoteDetail> listNoteDetail = new ArrayList<NoteDetail>();
				String url = list.get(i);
				try {
					Thread.sleep(666);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					doc = CrawlerUtils.getHtml(url);
					if (doc == null) {
						System.out.println("step 1 : conne't connect [" + url + "] , doc is null !");
					} else {
						vo = crawlering(doc, vo, userMap, listNoteDetail, url);
						saveInMysql(vo);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	private NoteDetailVO crawlering(Document doc, NoteDetailVO vo, HashMap<Integer, User> userMap,
			List<NoteDetail> listNoteDetail, String url) throws IOException {
		Element pageLast = doc.select(".page-last").first();
		int totalPage = 1;
		String[] urlArr = url.split("-");
		if (pageLast != null) {
			urlArr = pageLast.attr("href").split("-");
			totalPage = Integer.parseInt(urlArr[4]);
		}
		System.out.println(flag + " of totalPage : " + totalPage);
		for (int j = 1; j <= totalPage; j++) {
			StringBuffer sb = new StringBuffer("");
			sb = sb.append(urlArr[0]).append("-").append(urlArr[1]).append("-").append(urlArr[2]).append("-")
					.append(urlArr[3]).append("-").append(j + "-1.html");
			System.out.println(flag + " of ***************** 解析 url:" + sb);
			try {
				Thread.sleep(666);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			doc = CrawlerUtils.getHtml(sb.toString());
			if (doc == null)
				continue;
			// 获取姓名
			String authorname;
			// 获取发帖人发帖内容
			String content;
			// 获取发帖时间
			String time;
			// 发帖还是回帖的标识
			int flag;
			// 获取人的id
			int authorid;
			// 获取帖子id
			long noteid = Long.parseLong(url.split("-")[3]);
			Elements hds = doc.select(".post-hd");
			// System.out.println(hds.toString());
			Elements bds = doc.select(".post-bd.J_referSelectItem");
			int count = hds.size();
			for (int m = 0; m < count; m++) {
				Element hd = hds.get(m);
				if (j == 1 && m == 0) {// 表示第一页，第一项 表示楼主
					authorname = hd.select(".name span").text();
					authorid = Integer.parseInt(hd.select(".name").attr("href").split("-")[1]);
					time = hd.select(".u-add.link0.clearall span").get(2).attr("title").replace("发表于", "");
					flag = 0;
				} else {// 回帖人
					authorname = hd.select(".name span").text();
					authorid = Integer.parseInt(hd.select(".name").attr("href").split("-")[1]);
					time = hd.select(".u-add.link0.clearall span").get(2).text().replace("发表于: ", "");
					flag = 1;
				}
				User user = new User(authorid, authorname, "2");
				userMap.put(authorid, user);
				// 拿到帖子信息
				Element bd = bds.get(m);
				content = bd.child(1).html();
				if (!"".equals(content.trim()) || flag == 0) {
					NoteDetail nd = new NoteDetail(noteid, authorid, time, content, flag);
					listNoteDetail.add(nd);
				}
			}
		}
		vo.setNoteDetailList(listNoteDetail);
		vo.setUserMap(userMap);
		return vo;
	}

	private void saveInMysql(NoteDetailVO vo) {
		PreparedStatement ps = null;
		System.out.println(flag+" of "+conn);
		final String sql_user = "INSERT INTO crawler_education.user (userid,username,source) VALUES (?, ?, ?)";
		final String sql_notedetail = "INSERT INTO crawler_education.note_detail (note_id,author,note_time,content,flag) VALUES (?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql_user);
			HashMap<Integer, User> userMap = vo.getUserMap();
//			System.out.println("map size : " + userMap.size());
			for (Entry<Integer, User> entry : userMap.entrySet()) {
				User u = entry.getValue();
//				System.out.println(u.toString());
				ps.setInt(1, u.getId());
				ps.setString(2, u.getUsername());
				ps.setString(3, u.getSource());
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.clearBatch();
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			ps = null;
			ps = conn.prepareStatement(sql_notedetail);
			List<NoteDetail> noteDetailList = vo.getNoteDetailList();
			for (NoteDetail n : noteDetailList) {
//				System.out.println(n.toString());
				ps.setLong(1, n.getNote_id());
				ps.setLong(2, n.getAuthor());
				ps.setString(3, n.getNote_time());
				ps.setString(4, n.getContent());
				ps.setInt(5, n.getFlag());
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.clearBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	private int flag;
	public CrawlerThread02(int flag) {
		this.flag = flag;
	}

	public Connection getMysqlConnection(String host, int port, String dbName, String username, String password) {
		if (conn != null)
			return conn;
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?user=" + username + "&" + "password="
				+ password + "&useUnicode=true&characterEncoding=UTF8";
		// &autoReconnect=true&failOverReadOnly=false&maxReconnects=10
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("加载数据库驱动失败!" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("获取数据库连接失败!" + e);
		}
		return conn;
	}

	public static void main(String[] args) {
		CrawlerThread02 c = new CrawlerThread02(1);
		System.out.println(c.getMysqlConnection("127.0.0.1", 3306, "crawler_education", "root", "root"));
	}
}
