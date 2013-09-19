package jp.arcanum.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSqlUpdatable extends AbstractSqlExecutable {

	public int execute(Connection con){

		int ret = -1;

		ResultSet result = null;
		PreparedStatement pst = null;

		try{
			String sql = getSql();

			if(IS_SQL_OUT){
				// ログ出力
				Logger logger = LoggerFactory.getLogger(getClass());
				logger.debug("SQL:" + sql);

				// TODO 引数も出力　後で書く

			}

			pst = getPreparedStatement(sql);
			ret = pst.executeUpdate();


		}
		catch(Exception e){
			throw new RuntimeException("更新系のSQLに失敗", e);
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



}
