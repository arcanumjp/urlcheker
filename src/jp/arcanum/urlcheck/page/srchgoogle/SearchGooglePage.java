package jp.arcanum.urlcheck.page.srchgoogle;

import java.util.ArrayList;
import java.util.List;

import jp.arcanum.framework.page.AbstractMenu;
import jp.arcanum.framework.page.AbstractPage;
import jp.arcanum.urlcheck.page.AbstractBizPage;
import jp.arcanum.urlcheck.page.index.check.CheckUrlPanel;
import jp.arcanum.urlcheck.page.index.search.SearchGooglePanel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class SearchGooglePage extends AbstractBizPage{

	/**
	 * チェックURL
	 */
	private TextField _checkurl = new TextField("checkurl", new Model(""));

	/**
	 * URLチェックボタン
	 */
	private Button _acturlcheck = new Button("acturlcheck") {
		public void onSubmit(){
			onClickUrlCheck();
		}
	};


	public SearchGooglePage(){

		addForm(_checkurl);
		addForm(_acturlcheck);

		setActiveMenu(_searchgoogle);


		addMenu(_MENU_SEP);
		addMenu(
				new AbstractMenu() {
					@Override
					public String getMenuName() {
						return "画面独自メニュー";
					}

				}
		);



	}



	private void onClickUrlCheck(){

		// チェックするURL取得
		String url = (String)_checkurl.getModelObject();
		if(url == null || url.equals("")){
			error("チェックするURLを入力してください。");
			return;
		}



		try {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://t.co/bw1apb6A");

			HttpContext context = new BasicHttpContext();
			HttpResponse response = client.execute(get, context);


		} catch (Exception e) {
			throw new RuntimeException("HTTP接続でエラー", e);
		}

		error("______/**************************");

	}
}
