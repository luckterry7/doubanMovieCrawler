package cn.terry.doubanMovieCrawler.movieCrawler.model;

public class Movie {
	private String name = null;//电影名称	
	private String time = null;//上映时间
	private String countroy = null;	//语言国家
	private String rate = null;	//评分
	private String summary = null;	//简介
	private String photo = null;//图片
	
	public Movie() {
	}
	
	public Movie(String name, String rate) {
		super();
		this.name = name;
		this.rate = rate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCountroy() {
		return countroy;
	}
	public void setCountroy(String countroy) {
		this.countroy = countroy;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		return "Movie [name=" + name + ", time=" + time + ", countroy="
				+ countroy + ", rate=" + rate + ", summary=" + summary
				+ ", photo=" + photo + "]";
	}
}
