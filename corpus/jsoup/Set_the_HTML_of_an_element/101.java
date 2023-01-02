package com.jsoup.zhilianjob.login.service.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.jsoup.abs.impl.BaseJsoupServiceImpl;
import com.jsoup.common.constant.UrlConstant;
import com.jsoup.common.utils.GsonUtil;
import com.jsoup.zhilianjob.login.domain.ZhilianJobSuccessInfo;
import com.jsoup.zhilianjob.login.service.ZhilianJobLoginService;
import com.jsoup.zhilianjob.login.vo.ZhilianJobLoginVO;
@Service
public class ZhilianJobLoginServiceImpl extends BaseJsoupServiceImpl implements ZhilianJobLoginService{

	/**
	* @Title: login
	* @Description: 登录
	* @author zhuzq
	* @date  2017-10-25 下午5:18:51
	* @param loginVO
	* @return
	*/
	@Override
	public ZhilianJobLoginVO login(ZhilianJobLoginVO loginVO) {
		//登录请求参数处理
		Map<String,String> paramMap = reqParamHandle(loginVO);
		String url = UrlConstant.Zhilian.LOGIN_URL;
		ZhilianJobLoginVO login = loginVO;
		try {
		//登录特殊处理(获取reqCookies)
		Map<String,Object>	map = httpGet(url, paramMap, null);
		Document doc = (Document) map.get("doc");
		Connection con = (Connection) map.get("con");
		login.setRespCookies(getRespCookies(con));
		
		//检查是否登录成功
		String LoginInfo = replaceStr(doc.text());
		ZhilianJobSuccessInfo successInfo = (ZhilianJobSuccessInfo) GsonUtil.jsonToBean(LoginInfo, ZhilianJobSuccessInfo.class);
		if(null != successInfo && "您输入的密码与账号不匹配".equalsIgnoreCase(successInfo.getMessageText())){
			loginVO.setLoginMsg(successInfo.getMessageText());
			return loginVO;
		}
		
		
		//成功之后继续
		if(!cheackLogin(doc)){
			return null;
		}
		
		//智联招聘登录以后会跳转urlSecond(本次带cookies请求)
		String urlSecond = UrlConstant.Zhilian.LOGIN_SECOND_URL;
		String cookie = login.getRespCookies();
		Map<String,Object> data =httpGet(urlSecond, paramMap, cookie);
		Document docSecond =(Document) data.get("doc");
		//获取到简历界面url
		loginVO = getResposeInfo(login,docSecond);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return login;
	}

	
	/**
	* @Title: getResposeInfo
	* @Description: 获取修改简历url
	* @author zhuzq
	* @date  2017-10-27 下午12:20:36
	* @param login
	* @param doc
	* @return
	*/
	public ZhilianJobLoginVO getResposeInfo(ZhilianJobLoginVO login,Document doc) {
		//获取修改简历url
	System.out.println(doc.html());
		Elements elements = doc.select("div.myLink>a.myLinkA.linkAmend");
	
		if(null != elements){
			Element element = elements.get(0);
			if(null != element){
				String href = element.attr("href");
				login.setUrl(UrlConstant.Zhilian.DOMAIN_URL+href);
			}
		}
		return login;
	}

	
	/**
	* @Title: cheackLogin
	* @Description: 判读登录是否成功
	* @author zhuzq
	* @date  2017-10-27 上午11:47:02
	* @param doc
	* @return
	*/
	public boolean cheackLogin(Document doc){
		boolean result =  false;
		String body = doc.text();
		if(null != body){
			int beginIndex = body.indexOf(":");
			int endIndex = body.indexOf(",");
			String content = body.substring(beginIndex+1, endIndex);
			if("0".equalsIgnoreCase(content)){
				result = true;			}
		}
		return result;
	}

	/**
	* @Title: loginParamHandle
	* @Description: 登录请求参数处理
	* @author zhuzq
	* @date  2017-10-26 上午11:19:19
	* @param loginVO
	* @return
	*/
	public Map<String,String> reqParamHandle(ZhilianJobLoginVO loginVO){
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("LoginName", loginVO.getLoginName());
		paramMap.put("Password", loginVO.getPassword());
		return paramMap;
	}
	
	public String replaceStr(String str){
		if(str.indexOf("(") != -1 && str.indexOf(")") != -1){
			int start = str.indexOf("(");
			int end = str.indexOf(")");
			str = str.substring(start + 1,end);
			return str;
			
		}
		return null;
	}
	
	


	

}
