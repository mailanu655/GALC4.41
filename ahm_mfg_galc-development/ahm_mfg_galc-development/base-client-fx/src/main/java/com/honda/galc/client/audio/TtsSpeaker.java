package com.honda.galc.client.audio;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.property.SpeechPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.sun.speech.freetts.FreeTTSSpeakable;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * 
 * <h3>TtsSpeaker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TtsSpeaker description </p>
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
 * <TD>Apr 18, 2012</TD>
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
 * @since Apr 18, 2012
 */
public class TtsSpeaker {
	private static final String DEFAULT_VOICE = "kevin";
	private static TtsSpeaker instance;
	private Voice voice;
	private SpeechPropertyBean bean;
	
	private TtsSpeaker() {
		super();
		init();
	}
	
	private void init() {
		ApplicationContext appContext = ApplicationContext.getInstance();
		String ppId = appContext == null ? "" : appContext.getProcessPointId();
		
		try {
			if (StringUtils.isEmpty(ppId))
				bean = PropertyService.getPropertyBean(SpeechPropertyBean.class);
			else
				bean = PropertyService.getPropertyBean(SpeechPropertyBean.class, ppId);
		} catch (Exception e) {
			System.out.println("failed to load property service:" + e.getCause());
			bean = null;
		}
		
	}


	public static TtsSpeaker getInstance(){
		if(instance == null)
			instance = createInstance();
		
		return instance;
			
	}
	
	private static TtsSpeaker createInstance() {
		instance = new TtsSpeaker();
		return instance;
	}
	
	public static void speak(final String text){

		getInstance().getVoice().speak(text);

	}
	
	public static void speak(final InputStream inputStream){

		getInstance().getVoice().speak(inputStream);

	}
	
	public static void speak(final FreeTTSSpeakable speakable){

		getInstance().getVoice().speak(speakable);

	}
	
	public static void speakAsync(final String text){
		Thread t = new Thread(){
			public void run() {
				getInstance().getVoice().speak(text);
			}
		};
		
		t.start();
		
	}
	
	public static void speakAsync(final InputStream inputStream){
		Thread t = new Thread(){
			public void run() {
				getInstance().getVoice().speak(inputStream);
			}
		};
		
		t.start();
	}
	
	public static void speakAsync(final FreeTTSSpeakable speakable){
		Thread t = new Thread(){
			public void run() {
				getInstance().getVoice().speak(speakable);
			}
		};
		
		t.start();
	}

	private Voice getVoice() {
		if(voice == null){
            voice = VoiceManager.getInstance().getVoice(getVoiceName()); 
            
            if (bean != null) {
				if (!StringUtils.isEmpty(bean.getVoiceStyle()))
					voice.setStyle(bean.getVoiceStyle());
				
				if (!StringUtils.isEmpty(bean.getPitch()))
					voice.setPitch(Float.valueOf(bean.getPitch()));
				
				if (!StringUtils.isEmpty(bean.getPitchShift()))
					voice.setPitchShift(Float.valueOf(bean.getPitchShift()));
				
				if (!StringUtils.isEmpty(bean.getPitchRange()))
					voice.setPitchRange(Float.valueOf(bean.getPitchRange()));
				
				if (!StringUtils.isEmpty(bean.getRate()))
					voice.setRate(Float.valueOf(bean.getRate()));
			}
            
            voice.allocate();
            
		}
		return voice;
	}

	private String getVoiceName() {
		return bean == null ? DEFAULT_VOICE : bean.getVoiceName();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		voice.deallocate();
	}
	
	
}
