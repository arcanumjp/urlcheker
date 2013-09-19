package jp.arcanum.urlcheck.page.devpage;

import org.apache.wicket.markup.html.pages.RedirectPage;

import jp.arcanum.framework.page.AbstractPage;

public class DevPage extends AbstractPage{



	public DevPage(){

		setResponsePage(new RedirectPage("http://d.hatena.ne.jp/arcanum_jp/searchdiary?word=urlcheck"));

	}

}
