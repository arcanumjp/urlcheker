package jp.arcanum.framework.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;

public class ArUtil {


	private static final Properties APP_PROPS = new Properties();
	public static final void readAppProperties(String proppath){

		InputStream is = null;
		try{
			is = new FileInputStream(proppath);
			APP_PROPS.load(is);

		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	public static final boolean isEmptyProperty(){
		return APP_PROPS.isEmpty();
	}

	public static final String getProperty(String key){
		String ret = APP_PROPS.getProperty(key);
		if(ret==null){
			ret = "";
		}
		return ret;
	}


	public static final Class loadClass(String clazz){

		Class ret = null;
		try{
			ret = Class.forName(clazz);
		}
		catch(Exception e){
			// 握りつぶし

		}
		return ret;

	}




}
