package jp.arcanum.urlcheck.page.srchurl;

import java.util.List;

import jp.arcanum.framework.com.ArUtil;
import jp.arcanum.urlcheck.page.AbstractBizPage;
import jp.arcanum.urlcheck.util.RedirectInfo;
import jp.arcanum.urlcheck.util.UrlInfo;
import jp.arcanum.urlcheck.util.Util;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SearchUrlPage extends AbstractBizPage{

	/**
	 * チェックURL
	 */
	private TextField _checkurl = new TextField("checkurl", new Model(""));

	/**
	 * URLチェックボタン
	 */
	private AjaxButton _acturlcheck = new AjaxButton("acturlcheck") {
		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			onClickUrlCheck(target, form);
		}

	};

	/**
	 * 説明文
	 */
	private WebMarkupContainer _whatsthissite = new WebMarkupContainer("whatsthissite");

	/**
	 * チェック結果
	 */
	private WebMarkupContainer _result = new WebMarkupContainer("result");

	/**
	 * 危険メッセージ
	 */
	private WebMarkupContainer _criticalmessage = new WebMarkupContainer("criticalmessage");

	/**
	 * 危険メッセージ
	 */
	private Label _criticalmessagelabel = new Label("criticalmessagelabel", new Model(""));

	/**
	 * コンテンツのURL
	 */
	private WebMarkupContainer _contentsurl      = new WebMarkupContainer("contentsurl");
	private Label _contentsurllabel = new Label("contentsurllabel", new Model(""));
	private Label _contentsurlvirus = new Label("contentsurlvirus", new Model(""));

	/**
	 * 入力URL
	 */
	private Label _inputurl = new Label("inputurl", new Model(""));

	/**
	 * 入力URL
	 */
	private Label _outputurl = new Label("outputurl", new Model(""));

	/**
	 * タイトル
	 */
	private Label _outputtitle = new Label("outputtitle", new Model(""));

	/**
	 * 最終リダイレクト先の内容
	 */
	private Label _contents_bodyonly = new Label("contents_bodyonly", new Model(""));

	/**
	 * 最終リダイレクト先の内容
	 */
	private Label _contents_html = new Label("contents_html", new Model(""));

	private WebMarkupContainer _bodyonly = new WebMarkupContainer("bodyonly");
	private WebMarkupContainer _html     = new WebMarkupContainer("html");

	private AjaxLink _actbodyonly = new AjaxLink("actbodyonly") {

		@Override
		public void onClick(AjaxRequestTarget target) {

			_bodyonly.add(new AttributeModifier("class", "active"));
			_html.add(new AttributeModifier("class", ""));

			_contents_bodyonly.add(new AttributeModifier("style", ""));
			_contents_html.add(new AttributeModifier("style", "display:none;"));
			//_contents_bodyonly.setVisible(true);
			//_contents_html.setVisible(false);

			target.add(_bodyonly);
			target.add(_html);
			target.add(_contents_bodyonly);
			target.add(_contents_html);


		}

	};

	private AjaxLink _acthtml = new AjaxLink("acthtml") {

		@Override
		public void onClick(AjaxRequestTarget target) {

			_bodyonly.add(new AttributeModifier("class", ""));
			_html.add(new AttributeModifier("class", "active"));

			_contents_bodyonly.add(new AttributeModifier("style", "display:none;"));
			_contents_html.add(new AttributeModifier("style", ""));
			//_contents_bodyonly.setVisible(false);
			//_contents_html.setVisible(true);

			target.add(_bodyonly);
			target.add(_html);
			target.add(_contents_bodyonly);
			target.add(_contents_html);

		}

	};

	/**
	 * 経由地情報
	 */
	private ListView<UrlInfo> _urlinfolist = new ListView<UrlInfo>("urlinfolist") {

		@Override
		protected void populateItem(ListItem<UrlInfo> listitem) {

			final UrlInfo record = listitem.getModelObject();

			Label index = new Label("index", new Model(listitem.getIndex() + 1));
			listitem.add(index);


			Label urlinfo = new Label("urlinfo", new Model(record.getUrl()));
			listitem.add(urlinfo);

			Label safety = new Label("safety", new Model(record.getMessage()));
			listitem.add(safety);
		}


	};

	private boolean _firstshow;

	/**
	 * コンストラクタ
	 */
	public SearchUrlPage(){
		this(null);
	}

	/**
	 * コンストラクタ
	 */
	public SearchUrlPage(PageParameters param){

		_firstshow = true;

		// アクティブメニューの設定
		setActiveMenu(_searchurl);

		addForm(_checkurl);
		addForm(_acturlcheck);


		_whatsthissite.setOutputMarkupId(true);
		_whatsthissite.setOutputMarkupPlaceholderTag(true);
		addForm(_whatsthissite);

		// 結果関連
		_result.setOutputMarkupId(true);
		_result.setOutputMarkupPlaceholderTag(true);
		_result.add(_displaynone);
		_result.add(_criticalmessage);
		addForm(_result);
		_result.add(_inputurl);
		_result.add(_outputurl);
		_result.add(_outputtitle);
		_result.add(_contentsurl);
		_contentsurl.add(_contentsurllabel);
		_result.add(_contentsurlvirus);

		_result.add(_contents_bodyonly);
		_result.add(_contents_html);
		_result.add(_bodyonly);
		_result.add(_html);
		_bodyonly.add(_actbodyonly);
		_html.add(_acthtml);

		_contents_bodyonly.setOutputMarkupId(true);
		_contents_bodyonly.setOutputMarkupPlaceholderTag(true);
		_contents_html.setOutputMarkupId(true);
		_contents_html.setOutputMarkupPlaceholderTag(true);
		_contents_html.setEscapeModelStrings(false);
		_bodyonly.setOutputMarkupId(true);
		_bodyonly.setOutputMarkupPlaceholderTag(true);
		_html.setOutputMarkupId(true);
		_html.setOutputMarkupPlaceholderTag(true);


		//_criticalmessage.add(new AttributeModifier("style", "display:none;"));
		_criticalmessage.add(_criticalmessagelabel);

		// 経由地情報
		_result.add(_urlinfolist);




		//HttpGet get = new HttpGet("http://bit.ly/MpV4Pg");
		//HttpGet get = new HttpGet("http://goo.gl/Pmkfm");
		//HttpGet get = new HttpGet("http://goo.gl/UOg6T");
		if(!Util.getProperty("app.mode").equals("DEPLOYMENT")){
			//_checkurl.setDefaultModelObject("http://t.co/dhshaVjX");
		}

		if(param  != null){
		}

	}



/*
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		if(_firstshow){
			_result.setVisible(false);
			_firstshow = false;
		}


	}
*/

	private AttributeModifier _displaynone = new AttributeModifier("style", "display:none;");

	private void onClickUrlCheck(AjaxRequestTarget target, Form<?> form){

		// チェックするURL取得
		String url = (String)_checkurl.getModelObject();
		if(url == null || url.equals("")){
			error("チェックするURLを入力してください。", target, form);
			return;
		}

		if(url.length()<8){
			error("URLが短すぎます。", target, form);
			return;
		};

		RedirectInfo redirect  = Util.checkURL(url, true);
		String originalurl = "";
		if(redirect.getOriginalUrl()!=null){
			originalurl = redirect.getOriginalUrl().getUrl();
		}
		List<UrlInfo> urlinfolist = redirect.getUrlList();

		_inputurl.setDefaultModelObject(redirect.getShortUrl());
		_outputurl.setDefaultModelObject(originalurl);
		_outputtitle.setDefaultModelObject(redirect.getTitle());
		_urlinfolist.setList(redirect.getUrlList());

		_whatsthissite.setVisible(false);
		_result.add(new AttributeModifier("style", ""));

		// 危険な場合は危険と表示
		UrlInfo lasturl = urlinfolist.get(urlinfolist.size()-1);
		if(lasturl.isMalware() || lasturl.isPhishing()){

			_criticalmessagelabel.setDefaultModelObject(lasturl.getMessage());
			_criticalmessage.add(new AttributeModifier("style", "color: red;"));

			_contentsurl.add(new AttributeModifier("style", "display: none;"));
			_contentsurlvirus.add(new AttributeModifier("style", ""));
			_contentsurlvirus.setDefaultModelObject(lasturl.getUrl());

		}
		else if(!lasturl.isConnect() || lasturl.isInternal()){

			_criticalmessagelabel.setDefaultModelObject(lasturl.getMessage());
			_criticalmessage.add(new AttributeModifier("style", "color: black;"));

			_contentsurl.add(new AttributeModifier("style", "display: none;"));
			_contentsurlvirus.add(new AttributeModifier("style", "display: none;"));


		}
		else{

			_criticalmessagelabel.setDefaultModelObject("");

			_contentsurl.add(new AttributeModifier("style", ""));
			_contentsurl.add(new AttributeModifier("href", lasturl.getUrl()));
			_contentsurllabel.setDefaultModelObject(lasturl.getUrl());
			_contentsurlvirus.add(new AttributeModifier("style", "display:none;"));

		}
		String descs = redirect.getContentsBodyOnly();
		if(descs!=null && !descs.equals("")){
			_contents_bodyonly.setDefaultModelObject(descs);
			_contents_html.setDefaultModelObject(redirect.getContentsWithHtml());
		}
		else{
			_contents_bodyonly.setDefaultModelObject("コンテンツなし。");
			_contents_html.setDefaultModelObject("コンテンツなし。");
		}




		if(target!=null){
			target.add(_whatsthissite);
			target.add(_result);
		}


		error("URLをチェックしました。ご確認ください。", target, form);

	}


}
