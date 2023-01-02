package com.jsoup.zhilianjob.resume.service.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.jsoup.abs.impl.BaseJsoupServiceImpl;
import com.jsoup.zhilianjob.login.vo.ZhilianJobLoginVO;
import com.jsoup.zhilianjob.resume.domain.ZhilianJobResume;
import com.jsoup.zhilianjob.resume.service.ZhilianJobResumeService;

@Service
public class ZhilianJobResumeServiceImpl extends BaseJsoupServiceImpl implements ZhilianJobResumeService{

	/**
	* @Title: getResume
	* @Description: 获取简历信息
	* @author zhuzq
	* @date  2017-10-26 下午4:01:49
	* @param loginVO
	* @return
	*/
	@Override
	public ZhilianJobResume getResume(ZhilianJobLoginVO loginVO) {
		String url = loginVO.getUrl() ;
		String cookie = loginVO.getRespCookies();
		try {
			if(StringUtils.isNotBlank(cookie)){
				Map<String,Object> data  = httpGet(url, null, cookie);
				Document doc  = (Document) data.get("doc");
				return resumeHandle(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		////证明cookise值已经过期
		return null;
	}
	
	
	
	public ZhilianJobResume resumeHandle(Document doc){
		//base部分
		ZhilianJobResume resume = baseInfoHandle(doc);
		
		return resume;
	}
	
	/**
	* @Title: baseInfoHandle
	* @Description: 简历基本信息部分处理
	* @author zhuzq
	* @date  2017-10-26 下午4:16:28
	* @param doc
	* @return
	*/
	public ZhilianJobResume baseInfoHandle(Document doc){
		System.out.println(doc.html());
		ZhilianJobResume resume = new ZhilianJobResume();
		//姓名
		Elements names = doc.select("div>h3");
		if(null != names && names.size()>0){
			Element element = names.get(0);
			if(null != element){
				resume.setBaseName(element.text());
			}
		}
		return resume;
	}
	
	
	
	

	
}
