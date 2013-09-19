package jp.arcanum.framework;

import java.util.StringTokenizer;

import jp.arcanum.framework.com.ArUtil;
import jp.arcanum.framework.dao.DBAccess;
import jp.arcanum.framework.page.internal.InternalPage;
import jp.arcanum.framework.page.notfound.NotFoundPage;
import jp.arcanum.framework.page.sessout.SessionOutPage;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;

public class WebApp extends WebApplication{

	@Override
	protected void init() {
		super.init();

		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");


        // niceURLの設定
        String niceurls = ArUtil.getProperty("page.list");
        StringTokenizer tokens = new StringTokenizer(niceurls, ",");
        while(tokens.hasMoreTokens()){
        	String token = tokens.nextToken().trim();

        	String clazzstr = ArUtil.getProperty("page." + token + ".class");
        	Class  clazz = ArUtil.loadClass(clazzstr);
        	if(token.equals(".")){
            	mountPage("/", clazz);
        	}
        	else{
            	mountPage("/" + token, clazz);
        	}

        }

        // ４０４とかの設定
        getApplicationSettings().setInternalErrorPage(InternalPage.class);
        getApplicationSettings().setPageExpiredErrorPage(SessionOutPage.class);


        // DBコネクションのリクエスト紐付け
        getRequestCycleListeners().add(DBAccess.getInstance());


	}

	@Override
	public RuntimeConfigurationType getConfigurationType() {


		if(ArUtil.isEmptyProperty()){
			 String proppath = getServletContext().getRealPath("WEB-INF/classes/app.properties");
			 ArUtil.readAppProperties(proppath);
		}

		String mode =ArUtil.getProperty("app.mode");
		if(mode == null || mode.equals("DEVELOPMENT")){
			return RuntimeConfigurationType.DEVELOPMENT;
		}

		return RuntimeConfigurationType.DEPLOYMENT;
	}

	@Override
	public Class<? extends Page> getHomePage() {

		String startpage = ArUtil.getProperty("page.start");
		String clazz = ArUtil.getProperty("page." + startpage + ".class");
		return ArUtil.loadClass(clazz);

	}





}
