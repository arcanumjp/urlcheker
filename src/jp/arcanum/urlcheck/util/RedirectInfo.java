package jp.arcanum.urlcheck.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedirectInfo implements Serializable{

	/**
	 * チェック開始のURL（短縮）
	 */
	private String _shorturl;
	public void setShortUrl(String shorturl){
		_shorturl = shorturl;
	}
	public String getShortUrl(){
		return _shorturl;
	}


	/**
	 * 最後のオリジナルのURL
	 */
	private UrlInfo _originalurl;
	public void setOriginalUrl(UrlInfo originalurl){
		_originalurl = originalurl;
	}
	public UrlInfo getOriginalUrl(){
		return _originalurl;
	}

	/**
	 * 経由地情報
	 */
	private List<UrlInfo> _urllist = new ArrayList<UrlInfo>();
	public void addUrl(UrlInfo urlinfo){
		_urllist.add(urlinfo);
	}
	public List<UrlInfo> getUrlList(){
		return _urllist;
	}


	/**
	 * <title>タグの情報
	 */
	private String _title;
	public void setTitle(String title){
		_title = title;
	}
	public String getTitle(){
		return _title;
	}

	/**
	 * 内容
	 */
	private String _contentsbodyonly;
	public String getContentsBodyOnly(){
		return _contentsbodyonly;
	}
	public void setContentsBodyOnly(String contentsbodyonly){
		_contentsbodyonly = contentsbodyonly;
	}


	/**
	 * 内容(HTMLのみ)
	 */
	private String _contentswithhtml;
	public String getContentsWithHtml(){
		return _contentswithhtml;
	}
	public void setContentsWithHtml(String contentswithhtml){
		_contentswithhtml = contentswithhtml;
	}


	public RedirectInfo(){

	}



}
