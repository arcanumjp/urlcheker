package jp.arcanum.framework.page;

import java.io.Serializable;

public abstract class AbstractMenu implements Serializable{

	public abstract String getMenuName();

	public void onClick(){

	}

	public boolean isSeparator(){
		return false;
	}

}
