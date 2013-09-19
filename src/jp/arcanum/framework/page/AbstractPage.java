package jp.arcanum.framework.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.arcanum.framework.com.ArUtil;
import jp.arcanum.framework.component.MyFeedbackPanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class AbstractPage extends WebPage{


	protected static final AbstractMenu _MENU_SEP = new AbstractMenu() {

		@Override
		public String getMenuName() {
			return "  ";
		}

		@Override
		public boolean isSeparator() {
			return true;
		}


	};

	private Form _form = new Form("form");

	protected void addForm(Component comp){
		_form.add(comp);
	}

	/**
	 * メニューリスト
	 */
	private List<AbstractMenu> _menuitemlist = new ArrayList<AbstractMenu>();
	public void addMenu(AbstractMenu menuitem){
		_menuitemlist.add(menuitem);
	}
	private AbstractMenu _activemenu = null;
	protected void setActiveMenu(AbstractMenu activemenu){
		_activemenu = activemenu;
	}

	private ListView<AbstractMenu> _menulist = new ListView<AbstractMenu>("menulist", _menuitemlist) {
		@Override
		protected void populateItem(ListItem<AbstractMenu> listitem) {

			final AbstractMenu item = listitem.getModelObject();

			final WebMarkupContainer liitem = new WebMarkupContainer("liitem");
			if(item == _activemenu){
				liitem.add(new AttributeModifier("class", "active"));
			}

			listitem.add(liitem);
			listitem.setRenderBodyOnly(true);

			Link menuitem = new Link("menuitem") {
				@Override
				public void onClick() {
					_activemenu = item;
					item.onClick();
				}

			};
			liitem.add(menuitem);
			Label menuitemlabel = new Label("menuitemlabel", new Model(item.getMenuName()));
			if(item.isSeparator()){
				menuitem.add(new AttributeModifier("disabled", "true"));
			}
			menuitem.add(menuitemlabel);

		}
	};

	/**
	 * メッセージ
	 */
	//FeedbackPanel _feedback = new FeedbackPanel("feedback");
	MyFeedbackPanel _feedback = new MyFeedbackPanel("feedback");

	public AbstractPage(){

		// アプリケーション名の設定
		String appname = ArUtil.getProperty("app.name");
		Label prjname = new Label("projectname", new Model(appname));
		add(prjname);
		Label title = new Label("title", new Model(appname));
		title.setRenderBodyOnly(true);
		add(title);


		// メニューリスト
		add(_menulist);


		// メッセージ
		_feedback.setOutputMarkupId(true);
		_form.add(_feedback);

		// 唯一のフォームを追加
		add(_form);



	}


	public void error(Serializable message, AjaxRequestTarget target, Form<?> form){
		//warn(message);
		info(message);

		if(target!=null)target.add(_feedback);
	}

	public void setResponsePage(String url, PageParameters params){

		String clazzstr = ArUtil.getProperty("page." + url + ".class");
		Class clazz = ArUtil.loadClass(clazzstr);
		if(clazz == null){
			throw new RuntimeException(url + " に対応するクラスがありません");
		}

		if(params!=null){
			setResponsePage(clazz, params);
		}
		else{
			setResponsePage(clazz);
		}

	}

	public void setResponsePage(String url){
		setResponsePage(url, null);
	}

}
