package jp.arcanum.urlcheck.page;

import jp.arcanum.framework.page.AbstractMenu;
import jp.arcanum.framework.page.AbstractPage;

public class AbstractBizPage extends AbstractPage{

	protected AbstractMenu _searchurl = new AbstractMenu() {
		@Override
		public String getMenuName() {
			return "URLチェック";
		}

		public void onClick(){
			setResponsePage("ck");
		}

	};

	protected AbstractMenu _devblog = new AbstractMenu() {
		@Override
		public String getMenuName() {
			return "開発者ブログ";
		}

		public void onClick(){
			setResponsePage("dev");
		}

	};

	protected AbstractMenu _searchgoogle = new AbstractMenu() {

		@Override
		public String getMenuName() {
			return "Google検索";
		}

		public void onClick(){
			setResponsePage("searchgoogle");
		}

	};


	public AbstractBizPage(){

		// デフォルトのメニュー
		addMenu(_searchurl);
		addMenu(_devblog);
		//addMenu(_searchgoogle);
		setActiveMenu(_searchurl);

	}

}
