package com.honda.galc.client.audio;

import java.net.URL;

/**
 * @author Subu Kathiresan
 * @date Jun 10, 2013
 */
public class SoundClip {
	
    private URL resource;
    private String soundName;
    private int repeat = 1;
    
    public SoundClip(URL resource) {
        super();
        this.resource = resource;
    }

    public SoundClip(URL resource, String soundName, int repeat) {
    	this(resource);
    	this.soundName = soundName;
        this.repeat = repeat;
    }
    
    public String getSoundName() {
        return soundName;
    }
    
    public void setSoundName(String soundName) {
    	this.soundName = soundName;
    }
    
    public URL getResource() {
		return resource;
	}

	public void setResource(URL resource) {
		this.resource = resource;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	    
    public void playSound() {
    	ClipPlayer.getInstance().enqueue(this);
	}
}
