package jp.arcanum.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSqlSelectable extends AbstractSqlExecutable {

	/**
	 * レコード
	 */
	private Map _record = new HashMap();
	public void put(final String key, final String value){
		_record.put(key, value);
	}
	public String get(final String key){
		String ret = (String)_record.get(key);
		if(ret != null){
			ret = ret.trim();
		}
		return ret;
	}

	private void checkField(String fld){
		if(!_record.containsKey(fld)){
			throw new RuntimeException("このレコードにそのキーのデータはありません。 " + fld);
		}
	}

	public String getString(final String key){
		checkField(key);
		String ret = (String)_record.get(key);
		return ret;
	}

	public int getInt(final String key){
		checkField(key);
		String ret = (String)_record.get(key);
		return Integer.parseInt(ret);
	}

	public long getLong(final String key){
		checkField(key);
		String ret = (String)_record.get(key);
		return Long.parseLong(ret);
	}

	public double getDouble(final String key){
		checkField(key);
		String ret = (String)_record.get(key);
		return Double.parseDouble(ret);
	}

	public List<AbstractSqlSelectable> execute(Connection con){

		List<AbstractSqlSelectable> ret = new ArrayList<AbstractSqlSelectable>();


		ResultSet result = null;
		PreparedStatement pst = null;

		try{
			clearParams();
			String sql = getSql();

			pst = con.prepareStatement(sql);
			if(IS_SQL_OUT){
				// ログ出力
				Logger logger = LoggerFactory.getLogger(getClass());
				logger.debug("SQL:" + sql);

				// TODO 引数も出力　後で書く

			}

			for(int i = 0 ; i < getParams().size(); i++){
				if(IS_SQL_OUT){
					Logger logger = LoggerFactory.getLogger(getClass());
					logger.debug("    " + getParams().get(i));
				}
				pst.setObject(i+1, getParams().get(i));
			}

        	result = pst.executeQuery();

        	while(result.next()){
        		AbstractSqlSelectable record = getInstance();
        		ResultSetMetaData meta = result.getMetaData();
        		for(int i = 1 ; i < meta.getColumnCount()+1; i++){
        			String colname = meta.getColumnName(i);
        			String value   = result.getString(i);
        			record.put(colname, value);

        		}
        		ret.add(record);
        	}

		}
		catch(Exception e){
			throw new RuntimeException("SELECT系のSQLに失敗", e);
		}
		finally{
			try {
				if(result!=null){
					result.close();
				}
				if(pst!=null){
					pst.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("更新系のSQLに失敗(クローズに失敗)", e);
			}
		}





		return ret;


	}


	private AbstractSqlSelectable getInstance(){
		AbstractSqlSelectable ret = null;

		try{

			Class clazz = getClass();
			ret = (AbstractSqlSelectable)clazz.newInstance();
		}
		catch(Exception e){
			throw new RuntimeException("Ｓｅｌｅｃｔ系クラスにデフォルトコンストラクタを作成しなかった可能性とか・・・", e);
		}

		return ret;
	}

}
