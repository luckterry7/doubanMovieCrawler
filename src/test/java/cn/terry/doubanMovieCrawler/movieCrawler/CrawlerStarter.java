package cn.terry.doubanMovieCrawler.movieCrawler;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.terry.doubanMovieCrawler.movieCrawler.model.CrawlerParams;
import cn.terry.doubanMovieCrawler.movieCrawler.queue.UrlQueue;
import cn.terry.doubanMovieCrawler.movieCrawler.worker.CrawlerWorker;



public class CrawlerStarter {

	public static void main(String[] args){
		// 初始化配置参数
		initializeParams();

		// 初始化爬取队列
		initializeQueue();
		
		// 创建worker线程并启动
		for(int i = 1; i <= CrawlerParams.WORKER_NUM; i++){
			new Thread(new CrawlerWorker(i)).start();
		}
	}
	
	/**
	 * 初始化配置文件参数
	 */
	private static void initializeParams(){
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream("conf/crawler.properties"));
			Properties properties = new Properties();
			properties.load(in);
			
			// 从配置文件中读取参数
			CrawlerParams.WORKER_NUM = Integer.parseInt(properties.getProperty("crawler.threadNum"));
			CrawlerParams.DEYLAY_TIME = Integer.parseInt(properties.getProperty("crawler.fetchDelay"));

			in.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 准备初始的爬取链接
	 */
	private static void initializeQueue(){
		for(int i =0;i<2;i++){
			UrlQueue.addElement("http://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=20&page_start=" + i*20 + 1);
		}
	}
}
