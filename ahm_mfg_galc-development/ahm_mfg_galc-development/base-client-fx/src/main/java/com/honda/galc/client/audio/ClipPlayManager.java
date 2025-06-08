package com.honda.galc.client.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

public class ClipPlayManager {
	
	private static String SOUND_OK = "SOUND_OK";
	private static String SOUND_NG = "SOUND_NG";
	
	private static String DEFAULT_SOUNDS ="Default_Sounds";
	
	private static Map<String,SoundClip> soundClips = new HashMap<String,SoundClip>();
	
	public static void playSound(String urlString) throws Exception{
		
		SoundClip player = getPlayer(urlString);
		player.playSound();
		
	}
	
	public static void playOkSound() throws Exception {
		
		playSound(getSoundFilePath(SOUND_OK));
		
	}
	
	public static void playNgSound() throws Exception {
		
		playSound(getSoundFilePath(SOUND_NG));
		
	}
	
	private static String  getSoundFilePath(String soundType) {
		String hostName = ClientMainFx.getInstance().getApplicationContext().getHostName();
		String filePath = PropertyService.getProperty(hostName, soundType);
		if(StringUtils.isEmpty(filePath))
			filePath = PropertyService.getProperty(DEFAULT_SOUNDS, soundType);
		return filePath;
	}
	
	private static SoundClip getPlayer(String soundName) throws Exception {
		
		SoundClip clip = soundClips.get(soundName);
		if(clip == null) {
			clip = createClip(soundName);
			soundClips.put(soundName,clip);
		}
		return clip;
	}
	
	private static SoundClip createClip(String soundName) throws Exception {
		Logger.getLogger().info("start to load sound:" + soundName);
		URL soundUrl = Thread.currentThread().getContextClassLoader().getResource(soundName);
		SoundClip clip = new SoundClip(soundUrl);
		return clip;
	}
}
