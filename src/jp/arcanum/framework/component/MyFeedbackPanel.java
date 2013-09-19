package jp.arcanum.framework.component;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;


/**
 * A panel that displays {@link org.apache.wicket.feedback.FeedbackMessage}s in a list view. The
 * maximum number of messages to show can be set with setMaxMessages().
 *
 * @see org.apache.wicket.feedback.FeedbackMessage
 * @see org.apache.wicket.feedback.FeedbackMessages
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public class MyFeedbackPanel extends FeedbackPanel implements IFeedback{

	public MyFeedbackPanel(String id){
		super(id);
	}

	/**
	 * Gets the css class for the given message.
	 *
	 * @param message
	 *            the message
	 * @return the css class; by default, this returns feedbackPanel + the message level, eg
	 *         'feedbackPanelERROR', but you can override this method to provide your own
	 */
	protected String getCSSClass(final FeedbackMessage message){

		//System.out.println("---------------------------------");
		//System.out.println(message.getLevel());
		//System.out.println(message.getLevelAsString());
		//System.out.println("---------------------------------");

		int level = message.getLevel();
		String levelstr = "";
		if(level == 100){
			// ??
		}
		if(level == 200){
			levelstr = "info";
		}
		if(level == 300){
			// warning
			levelstr = "error";
		}
		if(level == 400){
			levelstr = "error";
		}

/*
		if(levelstr.equals("")){
			return "alert";
		}
		else{
			return "alert-" + levelstr;
		}
*/
		return "";

	}


}
