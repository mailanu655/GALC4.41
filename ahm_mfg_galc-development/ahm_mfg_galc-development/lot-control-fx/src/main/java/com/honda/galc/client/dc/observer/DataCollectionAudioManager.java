package com.honda.galc.client.dc.observer;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.fsm.IActionType;
import com.honda.galc.fsm.IState;
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
 * @author Jeffray Huang
 * @since Feb 19, 2014 
 */ 
public class DataCollectionAudioManager extends AbstractManager implements IAudioObserver {
	protected ClientAudioManager audio;

	public DataCollectionAudioManager(DataCollectionController controller) {
		super(controller);
		init();
	}

	protected void init() {
		AudioPropertyBean property = PropertyService.getPropertyBean(AudioPropertyBean.class,
				ApplicationContext.getInstance().getProcessPointId());
		audio = new ClientAudioManager(property);
	}
	
	@Override
	public void publish(IState<?> state, Class<? extends IActionType> actionType ){
		super.invoke(state, actionType);
	}
	
	public void playOkSound(Object arg){
		audio.playOKSound();
	}

	public void playNgSound(Object arg) {
		audio.playNGSound();
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
	}
	
	public void playNGSound() {
		audio.playNGSound();
	}
	
	public void playRepeatedNGSound(AbstractDataCollectionState state) {
		audio.playRepeatedNgSound();
	}
	
	public void playModelChangedSound(){
		audio.playModelChangedSound();
	}
	
	public void playConnectedSound(){
		audio.playConnectedSound();
	}
	
	public void playDisconnectedSound(){
		audio.playDisconnectedSound();
	}

	public void playRepeatedNGSound(Object arg) {
		audio.playRepeatedNgSound();
	}

	public void stopRepeatedSound() {
		audio.stopRepeatNgSound();
	}
	
	public void playCustomrisedSound(AbstractDataCollectionState state){
	}

	public void message(IState<?> state) {
		// TODO Auto-generated method stub
	}

	public void playRepeatedNGSound(IState<?> state) {
		// TODO Auto-generated method stub
	}
	public void playUserNotTrained(){
		audio.playUserNotTrained();
	}
}
