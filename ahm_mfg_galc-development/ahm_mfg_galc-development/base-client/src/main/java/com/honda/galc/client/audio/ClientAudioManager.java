package com.honda.galc.client.audio;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.SoundClipType;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.common.logging.Logger;

/**
 * <h3>Class description</h3>
 * <h4>
 * ClientAudioManager 
 *
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
 * <TD>Apr. 15, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>added for NG alarm replay</TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author P. Chou
 */
public class ClientAudioManager {

	private AudioPropertyBean propertyBean;
	private ConcurrentHashMap<String, SoundClip> clips = new ConcurrentHashMap<String, SoundClip>();

	public ClientAudioManager() {
		
	}
	
	public ClientAudioManager(AudioPropertyBean propertybean) {
		this.propertyBean = propertybean;
	}

	private SoundClip createClip(SoundClipType soundClipType, String file) {
		return createClip(soundClipType, file, 1);
	}
	
	private SoundClip createClip(SoundClipType soundClipType, String file, int repeat) {
		SoundClip SoundClip = null;
		try {
			Logger.getLogger().info("start to load sound: " + file);
			URL soundUrl = Thread.currentThread().getContextClassLoader().getResource(file);
			SoundClip = new SoundClip(soundUrl, soundClipType.getName(), repeat);
		}catch (Exception e) {
			Logger.getLogger().error("An Exception Occured while Loading Audio File : " + e);
		}
		return SoundClip;
	}
	
	public void playOKSound() {
		playSound(getOkClip());
	}
	
	public void playScrapSound() {
		if(propertyBean.getSoundScrapEnabled()) {
			playSound(getScrapClip());
		} else {
			playSound(getNgClip());
		}
	}

	public void playNGSound() {
		playSound(getNgClip());
    }

	public void playOKProductIdSound() {
		playSound(getOkProductIdClip());
	}

	public void playWarnSound() {
		playSound(getWarnClip());
    }	
	
	public void playNoActionSound() {
		playSound(getNoActionClip());
    }
	
	public void playModelChangedSound() {
		playSound(getChangedClip());
	}

	public void playDestinationChangedSound() {
		playSound(getDestinationChangedClip());
	}

	public void playConnectedSound() {
		playSound(getConnectedClip());
	}
	
	public void playDisconnectedSound() {
		playSound(getDisConnectedClip());
	}
	
	public void playNoGoodSound() {
		// If repeat property is set then play repeated sound 
		// otherwise play the normal one-time NG sound
		if (propertyBean.getNgSoundRepeatCount() != 0 ) 
			playRepeatedNgSound();
		else 
			playNGSound();
	}
	
	public void playRepeatedNgSound() {
		if(!ClipPlayer.getInstance().isPlaying(getRepeatedNgClip())) {
			Logger.getLogger().check("ClientAudioManager attempting to play " + getRepeatedNgClip().getSoundName() + " sound");
			getRepeatedNgClip().playSound();
		}
	}
	
	public void playRepeatedModelChangedSound() {
		if(!ClipPlayer.getInstance().isPlaying(getRepeatedModelChangedClip())) {
			Logger.getLogger().check("ClientAudioManager attempting to play " + getRepeatedModelChangedClip().getSoundName() + " sound");
			getRepeatedModelChangedClip().playSound();
		}
	}
	
	public void playRepeatedDestinationSound() {
		if(!ClipPlayer.getInstance().isPlaying(getRepeatedDestinationClip())) {
			Logger.getLogger().check("ClientAudioManager attempting to play " + getRepeatedDestinationClip().getSoundName() + " sound");
			getRepeatedDestinationClip().playSound();
		}
	}

	private void playSound(SoundClip clip) {
		if(clip != null) {
			Logger.getLogger().check("ClientAudioManager attempting to play " + clip.getSoundName() + " sound");
			stopRepeatNgSound();
			clip.playSound();
		}
	}

	public void playGoodSound() {
		playSound(getOkClip());
	}
	
	public void stopRepeatNgSound() {
		try {
			Runnable audioReset = new Runnable() {
				public void run() {
					if (ClipPlayer.getInstance().isPlaying(getRepeatedNgClip()))
						ClipPlayer.getInstance().requestStop();
				}
			};
			new Thread(audioReset).start();
		} catch (Exception e) {
			Logger.getLogger().error("An Exception occured while spawning a thread to stop repeat ng sound clip: " + e);
		}
	}
	
	private SoundClip getOkClip() {
		String name = SoundClipType.OK.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.OK, propertyBean.getSoundOk()));
		}
		return getClip(name);
	}
	
	private SoundClip getOkProductIdClip() {
		String name = SoundClipType.OK_PRODUCT_ID.getName();
		if (getClip(name) == null) {
			final String soundOkProductId = propertyBean.getSoundOkProductId();
			if (StringUtils.isNotBlank(soundOkProductId)) {
				addClip(name, createClip(SoundClipType.OK_PRODUCT_ID, soundOkProductId));
			} else {
				addClip(name, createClip(SoundClipType.OK_PRODUCT_ID, propertyBean.getSoundOk()));
			}
		}
		return getClip(name);
	}
	
	private SoundClip getWarnClip() {
		String name = SoundClipType.WARN.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.WARN, propertyBean.getSoundWarn()));
		}
		return getClip(name);
	}	
	
	private SoundClip getRepeatedNgClip() {
		String name = SoundClipType.NG_REPEAT.getName();	
		if (getClip(name) == null && propertyBean != null) {
			addClip(name, createClip(SoundClipType.NG_REPEAT, propertyBean.getSoundNg(), getRepeatCount()));
		}
		return getClip(name);
	}
	
	/**
	* This method returns the number of times the repeat should play and also maintains the default of -1
	* for no set configurations for NG Sound Repeat.
	*/
	private int getRepeatCount() {
		int count = propertyBean.getNgSoundRepeatCount();
		if (count == 0) count = -1;
		return count;
	}
	
	private SoundClip getRepeatedModelChangedClip() {
		String name = SoundClipType.CHANGED.getName();
		if (getClip(name) == null && propertyBean != null) {
			addClip(name, createClip(SoundClipType.CHANGED, propertyBean.getSoundChanged(), propertyBean.getChangedSoundRepeatCount()));
		}
		return getClip(name);
	}
	
	private SoundClip getRepeatedDestinationClip() {
		String name = SoundClipType.DESTINATION.getName();
		if (getClip(name) == null && propertyBean != null) {
			addClip(name, createClip(SoundClipType.DESTINATION, propertyBean.getSoundDestination(), propertyBean.getDestinationSoundRepeatCount()));
		}
		return getClip(name);
	}
	
	private SoundClip getScrapClip() {
		String name = SoundClipType.SCRAP.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.SCRAP, propertyBean.getSoundScrap()));
		}
		return getClip(name);
	}
	
	private SoundClip getNgClip() {
		String name = SoundClipType.NG.getName();
		if (getClip(name) == null) 
			addClip(name, createClip(SoundClipType.NG, propertyBean.getSoundNg()));
		return getClip(name);
	}
	
	private SoundClip getChangedClip() {
		String name = SoundClipType.CHANGED.getName();
		if (getClip(name) == null)
			addClip(name, createClip(SoundClipType.CHANGED, propertyBean.getSoundChanged()));
		return getClip(name);
	}
	
	private SoundClip getDestinationChangedClip() {
		String name = SoundClipType.DESTINATION.getName();
		if (getClip(name) == null)
				addClip(name, createClip(SoundClipType.DESTINATION, propertyBean.getSoundDestination()));
		return getClip(name);
	}
	
	private SoundClip getConnectedClip() {
		String name = SoundClipType.CONNECTED.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.CONNECTED, propertyBean.getSoundConnected()));
		}
		return getClip(name);
	}
	
	private SoundClip getDisConnectedClip() {
		String name = SoundClipType.DISCONNECTED.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.DISCONNECTED, propertyBean.getSoundDisConnected()));
		}
		return getClip(name);
	}
	
	private SoundClip getNoActionClip() {
		String name = SoundClipType.NO_ACTION.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.NO_ACTION, propertyBean.getSoundNoAction()));
		}
		return getClip(name);
	}
	
	private SoundClip getClip(String clipName) {
		return clips.get(clipName);
	}
	
	private SoundClip addClip(String clipName, SoundClip soundClip) {
		return clips.put(clipName, soundClip);
	}
	
	public void playSound(String sound){
		SoundClip soundClip = getSoundClip(sound);
		if(soundClip == null) return;
		
		playSound(soundClip);
		
	}
	
	public void playAlarmSound() {
		// If repeat property is set then play repeated sound 
		// otherwise play the normal one-time NG sound
		if (propertyBean.getAlarmSoundRepeatCount() != 0 ) 
			playRepeatedAlarmSound();
		else 
			playSound(getAlarmClip());
    }

	private SoundClip getAlarmClip() {
		String name = SoundClipType.ALARM.getName();
		if (getClip(name) == null) 
			addClip(name, createClip(SoundClipType.ALARM, propertyBean.getSoundAlarm()));
		return getClip(name);
	}
		private SoundClip getRepeatedAlarmClip() {
		String name = SoundClipType.ALARM.getName();
		if (getClip(name) == null && propertyBean != null) {
			addClip(name, createClip(SoundClipType.ALARM, propertyBean.getSoundAlarm(), propertyBean.getAlarmSoundRepeatCount()));
		}
		return getClip(name);
	}
	
	public void playRepeatedAlarmSound() {
		if(!ClipPlayer.getInstance().isPlaying(getRepeatedAlarmClip())) {
			Logger.getLogger().check("ClientAudioManager attempting to play " + getRepeatedAlarmClip().getSoundName() + " sound");
			getRepeatedAlarmClip().playSound();
		}
	}
	
	public void stopRepeatAlarmSound() {
		try {
			Runnable audioReset = new Runnable() {
				public void run() {
					if (ClipPlayer.getInstance().isPlaying(getRepeatedAlarmClip()))
						ClipPlayer.getInstance().requestStop();
				}
			};
			new Thread(audioReset).start();
		} catch (Exception e) {
			Logger.getLogger().error("An Exception occured while spawning a thread to stop repeat ng sound clip: " + e);
		}
	}

	private SoundClip getSoundClip(String sound) {
		if(clips.get(sound) != null) return clips.get(sound);
		else{
			try {
				Logger.getLogger().info("start to load sound: " + sound);
				URL soundUrl = Thread.currentThread().getContextClassLoader().getResource(sound);
				SoundClip newClip =  new SoundClip(soundUrl, sound, 1);
				clips.put(sound, newClip);
				return newClip;
			}catch (Exception e) {
				Logger.getLogger().error("An Exception Occured while Loading Audio File : " + sound + " \n" +e);
				return null;
			}
		}
	}
	
	
}

