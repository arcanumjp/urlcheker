package jp.arcanum.urlcheck.page.index.check;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class CheckUrlPanel extends Panel{


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


	public CheckUrlPanel(String id){
		super(id);

		add(_checkurl);
		add(_acturlcheck);
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
