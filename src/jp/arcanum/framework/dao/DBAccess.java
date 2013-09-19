package jp.arcanum.framework.dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import jp.arcanum.framework.com.ArUtil;

import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

public class DBAccess extends AbstractRequestCycleListener{


	private DBAccess(){

	}
	public static final DBAccess getInstance(){
		return new DBAccess();
	}

	private static final ThreadLocal<Connection> CON = new ThreadLocal<Connection>();

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		CON.set(getConnection());
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {


		Connection con = null;
		try {
			con = CON.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			try {
				if(con!=null){
					con.close();
					CON.remove();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}


	}

	public static final Connection getConnection(){

		Connection ret = null;
		try {

			String dbname = ArUtil.getProperty("app.dbname");
			if(!dbname.equals("")){
				InitialContext initCon = new InitialContext(); //(1)
				DataSource ds = (DataSource)initCon.lookup("java:comp/env/jdbc/" + dbname); //(2)
				ret = ds.getConnection(); //(3)JNDIリソースへのコネクト
				ret.setAutoCommit(true);	// 自動コミットON!
			}
		}
		catch (Exception e) {
			throw new RuntimeException("コネクション取得に失敗", e);
		}

		return ret;
	}


}
