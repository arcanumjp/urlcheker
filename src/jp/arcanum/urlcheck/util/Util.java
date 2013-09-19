package jp.arcanum.urlcheck.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElements;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceFormatter;
import net.htmlparser.jericho.nodoc.SequentialListSegment;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import sun.security.action.GetBooleanAction;


import jp.arcanum.framework.com.ArUtil;

public class Util extends ArUtil{

	private static final boolean[] isSafeUrl(String url) {

		boolean[] ret = new boolean[2];
		ret[0] = false;

		try {
			// Google web safe API
			//https://sb-ssl.google.com/safebrowsing/api/lookup?client=api&apikey=ABQIAAAApjlRS3lm4-ekdPk0eXq3hhQNrCd6KKR1PjFmOFNgVDQHqDSRbg&appver=1.0&pver=3.0&url=http%3A%2F%2Fianfette.org%2F

			String baseurl    = "https://sb-ssl.google.com/safebrowsing/api/lookup";
			String webclient  = "?client=urlchecker.arcanum.jp";
			String apikey     = "&apikey=" + Util.getProperty("google.apikey");
			String appver     = "&appver=1.0";
			String pver       = "&pver=3.0";
			String checkurl   = "&url=" + url;

			String requesturl = baseurl + webclient + apikey + appver + pver + checkurl;

			Object[] retres = getHttpResponse(requesturl, false);
			HttpClient client     = (HttpClient)retres[0];
			HttpResponse response = (HttpResponse)retres[1];

			//■200: The queried URL is either phishing, malware or both, see the response body for the specific type. (リクエストURLはフィッシングサイト、マルウェア配布サイトまたは両方です。詳しくはコンテンツを確認してください。)
			//■204: The requested URL is legitimate, no response body returned. (リクエストURLは問題ありません。コンテンツは返しません。)
			//■400: Bad Request — The HTTP request was not correctly formed. (HTTPリクエストのフォーマットが間違っています。)
			//■401: Not Authorized — The apikey is not authorized (APIキーが間違っています。)
			//■503: Service Unavailable — The server cannot handle the request. Besides the normal server failures, it could also indicate that the client has been “throttled” by sending too many requests (サーバがリクエストを処理できません。リクエストを大量に送っている可能性もあります。)

			int status = response.getStatusLine().getStatusCode();
			if(status == 200){

				String resbody = EntityUtils.toString(response.getEntity());
				//  malware / phishing / phishing,malware
				if(resbody.indexOf("phishing")!=-1){
					ret[0] = true;
				}
				if(resbody.indexOf("malware")!=-1){
					ret[1] = true;
				}
			}
			else if(status == 204){
				// 問題ないURL
			}
			else if(status == 400){
				throw new RuntimeException("Google Safe Browsing APIのフォーマットが間違っている");
			}
			else if(status == 401){
				// なにもしない
			}
			else if(status == 503){
				return null;
			}
			else{
				throw new RuntimeException("想定外のエラー");
			}
			client.getConnectionManager().shutdown();



		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return ret;

	}


	private static final Object[] getHttpResponse(String url, boolean redirect){

		Object[] ret = new Object[2];

		HttpResponse response = null;
		HttpClient client = null;

		try {

			//HttpParams params = new BasicHttpParams();
			//params.setParameter("http.protocol.handle-redirects",false);

			client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			//get.setParams(params);


			// TODO タイムアウト設定
			// 　　　・コネクションのタイムアウト
			// 　　　・ソケットのタイムアウト

			if(redirect){
				HttpParams params = get.getParams();
				params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
				//get.setParams(params);
			}

			HttpContext context = new BasicHttpContext();
			response = client.execute(get, context);


		} catch (Exception e) {
			e.printStackTrace();
		}

		ret[0] = client;
		ret[1] = response;
		return ret;

	}

	private static final void getUriInfo(
			String url, List<UrlInfo>list,
			boolean redirect
	){

			//危険かどうかチェック
			boolean isinternal = false;
			boolean ismalware = false;
			boolean isphishing = false;
			boolean[] critical = isSafeUrl(url);
			if(critical != null){


				isinternal =false;
				isphishing = critical[0];
				ismalware = critical[1];

				// 危険なURLなのでこれ以上検査しない
				if(isphishing || ismalware){

					UrlInfo urlinfo = new UrlInfo();
					urlinfo.setIsInternal(isinternal);
					urlinfo.setIsPhishing(isphishing);
					urlinfo.setIsMalware(ismalware);
					urlinfo.setIsConnect(true);
					urlinfo.setUrl(url);

					list.add(urlinfo);
					return;
				}


			}
			else{
				isinternal = true;
				isphishing = false;
				ismalware = false;

			}

			// URLに接続してレスポンスを取得(接続不可の場合はnull)
			Object[] retres = getHttpResponse(url, redirect);
			HttpClient client     = (HttpClient)retres[0];
			HttpResponse response = (HttpResponse)retres[1];

			UrlInfo urlinfo = new UrlInfo();
			urlinfo.setIsInternal(isinternal);
			urlinfo.setIsPhishing(isphishing);
			urlinfo.setIsConnect(true);
			urlinfo.setIsMalware(ismalware);
			urlinfo.setUrl(url);

			list.add(urlinfo);

			if(response != null){


				//System.out.println("----------------------------------------------");
				//System.out.println(get.getURI());
				//System.out.println("----------------------------------------------");
				//for(int i=0; i<response.getAllHeaders().length; i++){
				//	Header head = response.getAllHeaders()[i];
				//	System.out.println(head.getName() + "  : " + head.getValue());
				//}

				Header[] headers = response.getHeaders("Location");
				if(headers != null && headers.length > 0){
					String nexturl = headers[0].getValue();
					getUriInfo(nexturl, list, redirect);

				}

				//System.out.println("");
				//if(response.getEntity()!=null){
				//	System.out.println(urlinfo.getDescription());
				//}

			}
			else{
				urlinfo.setIsConnect(false);
			}

			client.getConnectionManager().shutdown();


	}

	public static final RedirectInfo checkURL(String url, boolean redirect){

		RedirectInfo ret = new RedirectInfo();

		if(!url.substring(0,7).equalsIgnoreCase("http://") &&
		   !url.substring(0,8).equalsIgnoreCase("https://")){
			url = "http://" + url;
		}

		List<UrlInfo> responselist = new ArrayList<UrlInfo>();
		getUriInfo(url, responselist, redirect);

		UrlInfo originalurl = null;
		String title = "(タイトルなし)";
		String contentsbodyonly = "";
		String contentswithhtml = "";
		if(!responselist.isEmpty()){

			/*
			// Locationが無い最終ページでも以下のようにhttp-equiv="refresh"でリダイレクト
			// する場合もある。これをやんなきゃ意味ないよ
			<head>
				<meta
					content="0;URL=http://www.town.tomiya.miyagi.jp/event-topics/svEveDtl.aspx?servno=2789"
					http-equiv="refresh">
			</head>
			 */


			try {

				String lasturl = null;
				while(true){

					UrlInfo urlinfo2 = responselist.get(responselist.size()-1);
					String url2 = urlinfo2.getUrl();
					if(url2.equals(lasturl)){
						break;
					}

					if(urlinfo2.isMalware() || urlinfo2.isPhishing() || urlinfo2.isInternal() ){
						break;
					}

					Source source = new Source(new URL(url2));
					List<Element> elemlist = source.getAllElements("meta");

					String content = null;
					String httpequiv = null;
					for(int i=0; i<elemlist.size(); i++){
						Element elem = elemlist.get(i);
						content   = elem.getAttributeValue("content");
						httpequiv = elem.getAttributeValue("http-equiv");
						if(content != null && httpequiv != null){
							break;
						}
					}
					if(content==null){
						break;
					}

					int urlstart = content.indexOf("URL=");
					if(urlstart!=-1){
						String realurl = content.substring(urlstart + 4);
						getUriInfo(realurl, responselist, true);

					}
					lasturl = responselist.get(responselist.size()-1).getUrl();

				}
			} catch (Exception e) {
				// 握りつぶし
				e.printStackTrace();
			}

			UrlInfo lasturl = responselist.get(responselist.size()-1);
			try{

				if(lasturl.isMalware() || lasturl.isPhishing() || lasturl.isInternal() ){
					// 明示的;
				}
				else{

					Source source = new Source(new URL(lasturl.getUrl()));
					source.fullSequentialParse();

					List<Element> titletaglist = source.getAllElements("title");
					if(titletaglist != null && !titletaglist.isEmpty()){
						Element tltletag = titletaglist.get(0);
						title = tltletag.getContent().toString();

					}

					// HTMLなしコンテンツのみ
					contentsbodyonly = source.getTextExtractor().toString();


					// 属性を全て削除
					OutputDocument od = new OutputDocument(source);
					List elemlist = source.getAllElements();
					for(int i=0; i<elemlist.size(); i++){
						Element elem = (Element)elemlist.get(i);
						if(elem.getAttributes()==null){
							continue;
						}

						Attributes allattrs = elem.getAttributes();
						Map<String, String> updatedmap = new HashMap<String, String>();
						for(int j=0; j<allattrs.size(); j++){
							//Element attr = (Element)allattrs.get(j);
							Attribute attr = allattrs.get(j);
							//od.remove(attr);
							updatedmap.put(attr.getKey(), "...");

						}
						od.replace(allattrs, updatedmap);

					}
					source = new Source(od.toString());
					SourceFormatter sf = source.getSourceFormatter();
					sf.setIndentString("  ");
					contentswithhtml = sf.toString();
					//contentswithhtml.replaceAll("<", "&gt;");
					contentswithhtml = contentswithhtml.replaceAll("<", "&lt;");
					contentswithhtml = contentswithhtml.replaceAll(">", "&gt;");
					contentswithhtml = contentswithhtml.replaceAll(" ", "&nbsp;");

					//System.out.println(contentswithhtml);



					originalurl = responselist.get(responselist.size()-1);

				}


			}
			catch(Exception e){
				// 握りつぶし
				e.printStackTrace();
			}


		}

		for(int i=0; i<responselist.size(); i++){
			ret.addUrl(responselist.get(i));
		}

		// 終了処理
		ret.setShortUrl(url);
		ret.setOriginalUrl(originalurl);
		ret.setTitle(title);
		ret.setContentsBodyOnly(contentsbodyonly);
		ret.setContentsWithHtml(contentswithhtml);
		return ret;

	}

}
