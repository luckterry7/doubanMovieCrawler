package cn.terry.doubanMovieCrawler.movieCrawler.model;

public class FetchedPage {
	private String url;
	private String content;
	private int statusCode;
	private int type = 0;//记录该对象的类型，0表示html，1表示json
	

	public FetchedPage(){
		
	}
	
	public FetchedPage(String url, String content, int statusCode){
		this.url = url;
		this.content = content;
		this.statusCode = statusCode;
		
	}
	
	public FetchedPage(String url, String content, int statusCode,int type){
		this.url = url;
		this.content = content;
		this.statusCode = statusCode;
		this.type = type;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
