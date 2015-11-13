package cn.terry.doubanMovieCrawler.movieCrawler.parser;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.terry.doubanMovieCrawler.movieCrawler.model.FetchedPage;
import cn.terry.doubanMovieCrawler.movieCrawler.queue.UrlQueue;
import cn.terry.doubanMovieCrawler.movieCrawler.queue.VisitedUrlQueue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class ContentParser {
	public Object parse(FetchedPage fetchedPage){
		Object targetObject = null;
		String name = null;
		String time = null;
		String countroy = null;
		String rate = null;
		String summary = null;
		String photo = null;
		
		Document doc = Jsoup.parse(fetchedPage.getContent());
		if(doc.getElementsByAttributeValue("property","v:itemreviewed") != null){
			name = doc.getElementsByAttributeValue("property","v:itemreviewed").html();
System.out.println("name:" + name);
		}
		
		if(doc.getElementsByAttributeValue("property","v:initialReleaseDate") != null){
			time = doc.getElementsByAttributeValue("property","v:initialReleaseDate").html();
System.out.println("time:" + time);
		}
		
		rate = doc.getElementsByAttributeValue("property","v:average").html();
System.out.println("rate:" + rate);
		
		summary = doc.getElementsByAttributeValue("property","v:summary").html();
System.out.println("summary:" + summary);

		photo = doc.getElementsByAttributeValue("rel","v:image").attr("src");
System.out.println("photo:" + photo);
		// 将URL放入已爬取队列
		VisitedUrlQueue.addElement(fetchedPage.getUrl());
		
		// 根据当前页面和URL获取下一步爬取的URLs
		// TODO
		
		return targetObject; 
	}
	
	private boolean containsTargetData(String url, Document contentDoc){
		// 通过URL判断
		// TODO
		
		// 通过content判断，比如需要抓取class为grid_view中的内容
		if(contentDoc.getElementsByClass("grid_view") != null){
			System.out.println(contentDoc.getElementsByClass("grid_view").toString());
			return true;
		}
		
		return false;
	}
	
	/**
	 * 使用阿里的json工具类，解析json对象
	 * @param fetchedPage
	 */
	public void parseJson(FetchedPage fetchedPage){
		JSONObject obj = JSON.parseObject(fetchedPage.getContent());
		Object subjects = obj.get("subjects");
		List<Object> list = JSON.parseArray(subjects.toString());
		for(Object object : list){
			Map movieMap = JSON.parseObject(object.toString(),Map.class);
			//把从json数据中解析出来的url放入队列
			UrlQueue.addElement(movieMap.get("url").toString());
		}
		
		// 将URL放入已爬取队列
		VisitedUrlQueue.addElement(fetchedPage.getUrl());
	}
}
