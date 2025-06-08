package com.honda.galc.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>Message</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Message is a general class for supporting multiple languages in GALC <br/>
 * The Message implements singleton pattern and one instance per JVM. It will load resource bundle
 * based Locale which can be defined in the GALC property. If it's not defined in property then 
 * the default system Locale 
 * will be used.
 * </p>
 * 
 * <p>Usage:<br/> 
 * <li> Message.get(msgId)</li>
 * example message: greeting=hello. Message.get(MessageKey.greeting) return hello.
 *  
 * <li> Message.get(msgId, String...args)</li>
 * example message: IvalidProduduct=Invalid product: {1}. message.get(MessageKey.InvalidProduct, "R18A17600000"), return Invalid product: R18A17600000
 * 
 * <br/>
 * <br/>
 * Exception handling:
 * When get un-defined message, a error message will be logged and return the message key.
 * </P>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Dec 9, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Dec 9, 2011
 */
 /**
 * 
 * @author Gangadhararao Gadde
 * @date nov 25, 2013
 *  ver 0.2
 */
 

public class Message {
	private static Message instance;
	private static String MESSAGE_PATH="resource/com.honda.galc.message.MessageBundle";
	
	private ResourceBundle messages;
	private Locale locale;

	private Message() {
		super();
		init();
	}
	
	public static Message getInstance(){
		if(instance == null)
			instance = createInstance();
		
		return instance;
			
	}
	
	private static Message createInstance() {
		instance = new Message();
		return instance;
	}

	private void init() {
		SystemPropertyBean bean = PropertyService.getPropertyBean(SystemPropertyBean.class);
		locale = Locale.getDefault();
		if(!StringUtils.isEmpty(bean.getLanguage()) && !StringUtils.isEmpty(bean.getCountry()))
			locale = new Locale(bean.getLanguage(), bean.getCountry());
		
		messages = ResourceBundle.getBundle(MESSAGE_PATH, locale);
		
	}
	
	private String getMessage(String msgId){
		try {
			return messages.getString(msgId);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Invalid message key.");
			return msgId;
		}
	}
	
	private String getMessage(String msgId, String ...msgs){
		try {
			String msg = messages.getString(msgId);
			for (int i = 0; i < msgs.length; i++) {
				msg = msg.replace("{" + i + "}", msgs[i]);
			}
			return msg;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Invalid message key.");
			return msgId;
		}
	}

	public static String get(String msgId){
		return Message.getInstance().getMessage(msgId);
	}
	
	public static Object get(String msgId, String ... msgs){
		return Message.getInstance().getMessage(msgId, msgs);
	}
	
	public static String[] getStrArrayMultipleLineMessage(String msgId, String ... msgs) {
		String labels=null;
		if(msgs==null)
			labels=Message.getInstance().getMessage(msgId);
		else
			labels = Message.getInstance().getMessage(msgId, msgs);
		StringTokenizer tokens = new StringTokenizer(labels,"|");
		List linesList = new ArrayList();
		while (tokens.hasMoreTokens()) {
			String line = tokens.nextToken();
			linesList.add(line);
		}    
		return (String[]) linesList.toArray(new String[0]);
	}
	
}
