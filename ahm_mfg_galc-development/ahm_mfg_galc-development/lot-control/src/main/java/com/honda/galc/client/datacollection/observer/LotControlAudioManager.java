package com.honda.galc.client.datacollection.observer;

import java.util.Map;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.audio.LightAlarmManager;
import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.processor.ProductIdProcessor;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.service.property.PropertyService;
/**
 * <h3>LotcontrolDataCollectionAudioManager</h3>
 * <h4>
 * Manage Audio device based action.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlAudioManager extends DataCollectionObserverBase
implements IAudioObserver {
	protected ClientAudioManager audio;
	protected static volatile LotControlAudioManager INSTANCE;
	AudioPropertyBean property;

	private LotControlAudioManager() {
		super();
		init();
	}

	protected void init() {
		property = PropertyService.getPropertyBean(AudioPropertyBean.class,
				ApplicationContext.getInstance().getProcessPointId());
		audio = new ClientAudioManager(property);
		if (property.getLightAlarm().equalsIgnoreCase("Y"))
			LightAlarmManager.getInstance().raiseAlarm(false);
	}
	
	public static LotControlAudioManager getInstance() {
		synchronized(LotControlAudioManager.class) {
			if (INSTANCE == null)
				INSTANCE = new LotControlAudioManager();
		}
		return INSTANCE;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(((DataCollectionState)arg).getExceptionList().size() > 0){
			audio.playNGSound();
		}
		super.update(o, arg);
	}

	public void playOkSound(Object arg){
		audio.playOKSound();
		setLightAlarm(false);
	}

	public void playOkProductIdSound(Object arg){
		audio.playOKProductIdSound();
		setLightAlarm(false);
	}

	public void playConnectedSound(Object arg){
		audio.playConnectedSound();
	}
	
	public void playDisconnectedSound(Object arg){
		audio.playDisconnectedSound();
	}

	public void playNoActionSound(Object arg) {
		audio.playNoActionSound();
	}

	public void playNoActionSound() {
		audio.playNoActionSound();
	}
	
	public void playOkSound(){
		audio.playOKSound();
		setLightAlarm(false);
	}
	
	public void playOkProductIdSound(){
		audio.playOKProductIdSound();
		setLightAlarm(false);
	}
	
	public void setLightAlarm(boolean onOff) {
		if (LightAlarmManager.isExist()) 
			LightAlarmManager.getInstance().raiseAlarm(onOff);
	}
	
	public void playNgSound(Object arg) {
		if (property.getNgSoundRepeatCount() != 0 ) 
			audio.playRepeatedNgSound();
		else 
			audio.playNGSound();
		setLightAlarm(true);
	}
	
	public void playNGSound() {
		if (property.getNgSoundRepeatCount() != 0 ) 
			audio.playRepeatedNgSound();
		else 
			audio.playNGSound();
		setLightAlarm(true);
	}
	
	public void playNGSoundForImmobi() {
		audio.playNGSound();
		setLightAlarm(true);
	}
	
	public void playRepeatedNGSound(DataCollectionState state) {
		if(ProductIdProcessor.LOT_CONTROL_RULE_NOT_DEFINED.equals(state.getMessage().getId())){
			audio.playOKSound();
			return;
		}
		audio.playRepeatedNgSound();
		setLightAlarm(true);
	}
	
	public void playModelChangedSound(){
		if (property.getChangedSoundRepeatCount() != 0 ) 
			audio.playRepeatedModelChangedSound();
		else 
			audio.playModelChangedSound();
		setLightAlarm(true);
	}
	
	public void playDestinationChangedSound(){
		if (property.getDestinationSoundRepeatCount() != 0 ) 
			audio.playRepeatedDestinationSound();
		else 
			audio.playDestinationChangedSound();
		setLightAlarm(true);
	}
	
	public void playConnectedSound(){
		audio.playConnectedSound();
	}
	
	public void playDisconnectedSound(){
		audio.playDisconnectedSound();
	}

	public void playRepeatedNGSound(Object arg) {
		audio.playRepeatedNgSound();
		setLightAlarm(true);
	}

	public void stopRepeatedSound() {
		audio.stopRepeatNgSound();
		setLightAlarm(false);
	}
	
	public void playCustomrisedSound(DataCollectionState state){
	}
	
	public static boolean isExist() {
		return INSTANCE != null;
	}
	
	public static String findAudioManagerPropertyKey() {
		LotControlPropertyBean defaultLotControlBean = PropertyService.getPropertyBean(LotControlPropertyBean.class, "");
		Map<String, String> observers = defaultLotControlBean.getObservers();
		for(Map.Entry<String, String> me : observers.entrySet()){
			if(me.getValue().contains(LotControlAudioManager.class.getSimpleName())){
				return "OBSERVERS{" + me.getKey() + "}";
			}
		}
		Logger.getLogger().warn("Can't find property key for " + LotControlAudioManager.class.getSimpleName());
		return null;
	}
	
	public void message(DataCollectionState state) {
		if (state.getMessage() == null)
				return;
		
		if (state.getMessage().getType() != MessageType.INFO)
			playNGSound();
		else if(state.getMessage().getType() == MessageType.INFO){
			playInfomationMessageSound(state);
		}
	}

	protected void playInfomationMessageSound(DataCollectionState state) {
		if(state.getMessage().getId() == StatusMessage.SERVER_ON_LINE ||
		   state.getMessage().getId() == StatusMessage.DEVICE_CONNECT  )
			audio.playConnectedSound();
		else if(state.getMessage().getId() == StatusMessage.SERVER_OFF_LINE ||
				state.getMessage().getId() == StatusMessage.DEVICE_DISCONNECT)
			audio.playDisconnectedSound();
	}
	
	public void cleanUp() {
		setLightAlarm(false);
		super.cleanUp();
	}

	public void playWarnSound() {
		audio.playWarnSound();
		
	}
}
