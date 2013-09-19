package jp.arcanum.framework.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlExecutable {

	// SQL 引数
	private List<Object> _params = new ArrayList<Object>();
	protected List<Object> getParams(){
		return _params;
	}

	protected String quote(Object o){

		_params.add(o);
		return "?";
	}

	protected void clearParams(){
		_params.clear();
	}


	/**
	 * SQLをログ出力するかどうか
	 * （アプリ起動時に設定される）
	 */
	public static boolean IS_SQL_OUT;

	/**
	 *
	 * @return
	 */
	public abstract String getSql();


	protected PreparedStatement getPreparedStatement(String sql){

		return null;

	}
}
