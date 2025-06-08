/**
 * 
 */
package com.honda.galc.device.simulator.torque;

import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import com.honda.galc.device.simulator.utils.Logger;
import com.honda.galc.device.simulator.utils.Utils;

  
/**
 * @author Kathiresan Subu
 *
 */
public class OPReplies
{
	private static Hashtable<String, ArrayList<String>> _replies = new Hashtable<String, ArrayList<String>>(); 
	private static final String OP_REPLIES_FILE = "OPReplies.xml";
	
	static 
	{
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(Utils.getPath(Utils.CONTROL_FILES_DIR + OP_REPLIES_FILE)));

			Iterator iter = doc.getRootElement().getChildren().iterator();
			while(iter.hasNext())
			{
				Object obj = iter.next();
				if (obj instanceof Element)
					populateReplies((Element) obj);
			}
		}
		catch(Exception ex)
		{
			Logger.log(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param repliesForMessage
	 */
	private static void populateReplies(Element repliesForMessage)
	{
		Iterator iter = repliesForMessage.getChildren().iterator();
		String key = repliesForMessage.getName();
		
		while(iter.hasNext())
		{
			Object obj = iter.next();
			if (obj instanceof Element)
			{
				String replyMsgId = ((Element) obj).getText();
				if (getReplies().containsKey(key))
				{
					// get list and Add reply to the list
					getReplies().get(key).add(replyMsgId);
					Logger.log("Adding reply for " + key + "    OP" +  replyMsgId);
				}
				else
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add(replyMsgId);
					
					// create a new key
					getReplies().put(key, list);
					Logger.log("Adding reply for " + key + " -> OP" +  replyMsgId);
				}
			}
		}		
	}

	/**
	 * @param replies the replies to set
	 */
	public static void setReplies(Hashtable<String, ArrayList<String>> replies)
	{
		_replies = replies;
	}

	/**
	 * @return the replies
	 */
	public static Hashtable<String, ArrayList<String>> getReplies()
	{
		return _replies;
	}
	
}
