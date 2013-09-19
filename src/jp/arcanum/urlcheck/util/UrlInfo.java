package jp.arcanum.urlcheck.util;

import java.io.Serializable;

public class UrlInfo  implements Serializable{

	/**
	 * 危険かどうか
	 */
	private boolean _ismalware;
	public boolean isMalware(){
		return _ismalware;
	}
	public void setIsMalware(boolean ismalware){
		_ismalware = ismalware;
	}
	private boolean _isphishing;
	public boolean isPhishing(){
		return _isphishing;
	}
	public void setIsPhishing(boolean isphishing){
		_isphishing = isphishing;
	}

	private boolean _isinternal;
	public boolean isInternal(){
		return _isinternal;
	}
	public void setIsInternal(boolean isinternal){
		_isinternal = isinternal;
	}

	public boolean _isconnect;
	public boolean isConnect(){
		return _isconnect;
	}
	public void setIsConnect(boolean isconnect){
		_isconnect = isconnect;
	}

	public String getMessage(){

		String ret = "";

		if(_isinternal){
			ret = "サイトに接続できませんでした。(503)";
		}
		else if(!_isconnect){
			ret = "サイトに接続できませんでした。";
		}
		else{
			if(!_isphishing && !_ismalware){
				// do nothing
			}
			else{
				ret = "このURLは";
				if(_isphishing){
					ret = ret + "、フィッシングサイト";
				}
				if(_ismalware){
					ret = ret + "、マルウエア配布サイト";
				}
				ret = ret + "のようです。";
			}
		}

		return ret;

	}

	/**
	 * URL
	 */
	private String _url;
	public String getUrl(){
		return _url;
	}
	public void setUrl(String url){
		_url = url;
	}

}
