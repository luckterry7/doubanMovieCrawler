package cn.terry.doubanMovieCrawler.movieCrawler.worker;

import java.util.logging.Logger;

import cn.terry.doubanMovieCrawler.movieCrawler.fetcher.PageFetcher;
import cn.terry.doubanMovieCrawler.movieCrawler.hadler.ContentHandler;
import cn.terry.doubanMovieCrawler.movieCrawler.model.FetchedPage;
import cn.terry.doubanMovieCrawler.movieCrawler.model.CrawlerParams;
import cn.terry.doubanMovieCrawler.movieCrawler.parser.ContentParser;
import cn.terry.doubanMovieCrawler.movieCrawler.queue.UrlQueue;
import cn.terry.doubanMovieCrawler.movieCrawler.storage.DataStorage;


public class CrawlerWorker implements Runnable{
		private static final Logger Log = Logger.getLogger(CrawlerWorker.class.getName());
		
		private PageFetcher fetcher;
		private ContentHandler handler;
		private ContentParser parser;
		private DataStorage store;
		private int threadIndex;
		
		public CrawlerWorker(int threadIndex){
			this.threadIndex = threadIndex;
			this.fetcher = new PageFetcher();
			this.handler = new ContentHandler();
			this.parser = new ContentParser();
			this.store = new DataStorage();
		}
		
		public void run() {
			// 1,登录
			
			
			// 当待抓取URL队列不为空时，执行爬取任务
			// 注： 当队列内容为空时，也不爬取任务已经结束了
			//     因为有可能是UrlQueue暂时空，其他worker线程还没有将新的URL放入队列
			//	        所以，这里可以做个等待时间，再进行抓取（二次机会）
			while(!UrlQueue.isEmpty()){
				// 2,从待抓取队列中拿URL
				String url = UrlQueue.outElement();
				
				// 3,抓取URL指定的页面，并返回状态码和页面内容构成的FetchedPage对象
				FetchedPage fetchedPage = fetcher.getContentFromUrl(url);
				
				// 4,检查爬取页面的合法性，爬虫是否被禁止
				if(!handler.check(fetchedPage)){
					// 切换IP等操作
					// TODO
					
					Log.info("Crawler-" + threadIndex + ": switch IP to ");
					continue;
				}
				
				
				if(fetchedPage.getType() ==CrawlerParams.FETCHEDPAGETYPE_JSON){
					//如果是响应的数据类型是json，则解析json中的url
					parser.parseJson(fetchedPage);
				}else if(fetchedPage.getType() ==CrawlerParams.FETCHEDPAGETYPE_HTML){
					// 解析页面，获取目标数据
					Object targetData = parser.parse(fetchedPage);
					// 存储目标数据到数据存储（如DB）、存储已爬取的Url到VisitedUrlQueue
					store.store(targetData);
				}
				
				// delay
				try {
					Thread.sleep(CrawlerParams.DEYLAY_TIME);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			fetcher.close();
			Log.info("Spider-" + threadIndex + ": stop...");
		}

}
