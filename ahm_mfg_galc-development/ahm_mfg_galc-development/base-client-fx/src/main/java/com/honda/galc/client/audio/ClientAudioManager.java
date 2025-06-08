package com.honda.galc.client.audio;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

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

	public void playNGSound() {
		playSound(getNgClip());
    }
	
	public void playNoActionSound() {
		playSound(getNoActionClip());
    }
	
	public void playModelChangedSound() {
		playSound(getChangedClip());
	}

	public void playConnectedSound() {
		playSound(getConnectedClip());
	}
	
	public void playDisconnectedSound() {
		playSound(getDisConnectedClip());
	}
	
	public void playUrgeSound() {
		playSound(getUrgeClip());
    }

	public void playRepeatedNgSound() {
		if(!ClipPlayer.getInstance().isPlaying(getRepeatedNgClip())) {
			Logger.getLogger().debug("ClientAudioManager attempting to play " + getRepeatedNgClip().getSoundName() + " sound");
			getRepeatedNgClip().playSound();
		}
	}

	private void playSound(SoundClip clip) {
		if(clip != null) {
			Logger.getLogger().debug("ClientAudioManager attempting to play " + clip.getSoundName() + " sound");
			stopRepeatNgSound();
			clip.playSound();
		}
	}

	public void stopRepeatNgSound() {
		try {
			Runnable audioReset = new Runnable() {
				public void run() {
					if(ClipPlayer.getInstance().isPlaying(getRepeatedNgClip())) {
						ClipPlayer.getInstance().requestStop();
					}
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
	
	private SoundClip getNgClip() {
		String name = SoundClipType.NG.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.NG, propertyBean.getSoundNg()));
		}
		return getClip(name);
	}
	
	private SoundClip getRepeatedNgClip() {
		String name = SoundClipType.NG_REPEAT.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.NG_REPEAT, propertyBean.getSoundNg(), propertyBean.getNgSoundRepeatCount()));
		}
		return getClip(name);
	}
	
	private SoundClip getChangedClip() {
		String name = SoundClipType.CHANGED.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.CHANGED, propertyBean.getSoundChanged()));
		}
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
	
	private SoundClip getUrgeClip() {
		String name = SoundClipType.URGE.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.URGE, propertyBean.getSoundUrge()));
		}
		return getClip(name);
	}

	private SoundClip getClip(String clipName) {
		return clips.get(clipName);
	}
	
	private SoundClip addClip(String clipName, SoundClip soundClip) {
		return clips.put(clipName, soundClip);
	}

	public void playUserNotTrained() {
		playSound(getUserNotTrainedClip());
		
	}
	public void playWarnSound() {
		playSound(getWarnClip());
		
	}
	
	private SoundClip getUserNotTrainedClip() {
		String name = SoundClipType.NOT_TRAINED.getName();
		if (getClip(name) == null) {
			addClip(name, createClip(SoundClipType.NOT_TRAINED, propertyBean.getSoundUserNotTrained()));
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
}

