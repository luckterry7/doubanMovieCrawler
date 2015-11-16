package cn.terry.doubanMovieCrawler.movieCrawler.filter;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.terry.doubanMovieCrawler.movieCrawler.model.Movie;


/**
 * 过滤器，用于过滤出需要的电影
 * @author terry
 *
 */
public class MovieFilter {

	private static final Logger Log = Logger.getLogger(MovieFilter.class.getName());
	
	public static boolean isMatch(Movie movie){
		
		//设定过滤条件,评分大于7的电影筛选出来
		Movie defineMovie = new Movie("","^[7-9]\\d*\\.\\d$");
		
		
		Pattern pattern = Pattern.compile(defineMovie.getRate());
		Matcher matcher = pattern.matcher(movie.getRate());
		Boolean isMatch = matcher.matches();
		
		Log.info("MovieFilter match : " + isMatch);
		return isMatch;
	}
}
