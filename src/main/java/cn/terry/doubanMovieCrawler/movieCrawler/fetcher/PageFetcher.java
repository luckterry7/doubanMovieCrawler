package cn.terry.doubanMovieCrawler.movieCrawler.fetcher;

import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import cn.terry.doubanMovieCrawler.movieCrawler.model.CrawlerParams;
import cn.terry.doubanMovieCrawler.movieCrawler.model.FetchedPage;
import cn.terry.doubanMovieCrawler.movieCrawler.queue.UrlQueue;


public class PageFetcher {
	private static final Logger Log = Logger.getLogger(PageFetcher.class.getName());
	private HttpClient client;
	
	/**
	 * 创建HttpClient实例，并初始化连接参数
	 */
	public PageFetcher(){
		// 设置超时时间
		HttpParams params = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
	    HttpConnectionParams.setSoTimeout(params, 10 * 1000);	    
		client = new DefaultHttpClient(params);
	}
	
	/**
	 * 主动关闭HttpClient连接
	 */
	public void close(){
		client.getConnectionManager().shutdown();
	}
	
	/**
	 * 根据url爬取网页内容
	 * @param url
	 * @return
	 */
	public FetchedPage getContentFromUrl(String url){
		String content = null;
		String type = null;
		int statusCode = 500;
		
		// 创建Get请求，并设置Header
		HttpGet getHttp = new HttpGet(url);	
		getHttp.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
		HttpResponse response;
		Log.info("request url:" + url);
		try{
			// 获得信息载体
			response = client.execute(getHttp);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();	
			
			if(entity != null){
				// 转化为文本信息, 设置爬取网页的字符集，防止乱码
				content = EntityUtils.toString(entity, "UTF-8");
			}
			
			//获得报文头，得到报文类型
			Header[] headers = response.getHeaders("Content-Type");
			String ContentType = headers[0].getValue();
			int start = ContentType.lastIndexOf("/") + 1;
			int end = ContentType.lastIndexOf(";");
			type = ContentType.substring(start,end);
		}
		catch(Exception e){
			e.printStackTrace();
			
			// 因请求超时等问题产生的异常，将URL放回待抓取队列，重新爬取
			Log.info(">> Put back url: " + url);
			UrlQueue.addFirstElement(url);
		}

		FetchedPage fetchedPage = null; 
		if(type.equalsIgnoreCase("html")){
			fetchedPage= new FetchedPage(url, content, statusCode,CrawlerParams.FETCHEDPAGETYPE_HTML);
		}else if(type.equalsIgnoreCase("json")){
			fetchedPage = new FetchedPage(url, content, statusCode,CrawlerParams.FETCHEDPAGETYPE_JSON);
		}
		return fetchedPage;
	}
}
