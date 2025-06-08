package com.honda.galc.client.audio;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

import com.honda.galc.common.logging.Logger;


/**
 * @author Andrew Fannin
 *
 * This is a Runnable class to enable audio sounds to be played on a separete thread from its caller.
 * It accepts a URL or a String representation of a URL in its constructor.
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AudioPlayer implements Runnable {
	URL audioFile = null;
	AudioClip clip = null;
	
	public AudioPlayer(){
		super();	
	}
	/**
	 * Constructor to create a Runnable class to play audio clips.
	 * @param URL
	 * @throws Exception
	 * */
	public AudioPlayer(URL soundURL) throws Exception{
		super();
		this.audioFile = soundURL;
		try {
			createAudioClip();
		} catch (Exception e) {
			Logger.getLogger().warn(this.getClass().getName()+"::AudioPlayer(URL soundURL):ClipCreateException:"+e);
			throw new Exception(this.getClass().getName()+"::AudioPlayer(URL soundURL):ClipCreateException:"+e.toString());
		}
	}
	
	/**
	 * Constructor to create a Runnable AudioPlayer class to store in local memory and play audio clips.
	 * The parameter is a String representation of a URL.
	 * @param String
	 * @throws Exception
	 * */
	public AudioPlayer(String urlString) throws Exception{
		
		try {
			this.audioFile = new URL(urlString);
		} catch (MalformedURLException e) {
			Logger.getLogger().warn(this.getClass().getName()+"::AudioPlayer(String urlString):MalformedURLException:"+e);
			throw new Exception(this.getClass().getName()+"::AudioPlayer(String urlString):MalformedURLException:"+e);
		}
		
		try {
			createAudioClip();
		} catch (Exception e) {
			Logger.getLogger().warn(this.getClass().getName()+"::AudioPlayer(String urlString):ClipCreateException:"+e);
			throw e;
		}	
	}
	
	/**
	 * Creates a new java.applet.AudioClip using the member URL.
	 * @throws RuntimeException, Exception
	 * */
	private void createAudioClip() throws RuntimeException,Exception{
		
		try {
			clip = java.applet.Applet.newAudioClip(audioFile);
		} catch (RuntimeException e) {
			Logger.getLogger().warn(this.getClass().getName()+"::createAudioClip():RuntimeException:"+e);
			throw e;
		} catch (Exception e){
			Logger.getLogger().warn(this.getClass().getName()+"::createAudioClip():Exception:"+e);
			throw e;
		}	
	}
	
	
	/**
	 * Plays the audio clip stored as member variable.
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		try{
			//Thread.currentThread().sleep(2000);
			getClip().play();
		}catch(InterruptedException e){
			Logger.getLogger().warn(this.getClass().getName()+"::run():Exception:"+e);
			Thread.currentThread().interrupt();
			return;
		}catch(Exception e){
			Logger.getLogger().warn(this.getClass().getName()+"::run():Exception:"+e);
			return;
		}
		
	}
	
	/**
	 * Plays the audio clip stored repeatedly.
	 *
	 */
	public void loop()
	{
		try{
			//Thread.currentThread().sleep(2000);
			getClip().loop();
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
			Logger.getLogger().warn(e, this.getClass().getName()+"::run():InterruptedException:"+e);
			return;
		}catch(Exception e){
			Logger.getLogger().warn(e, this.getClass().getName()+"::run():Exception:"+e);
			Thread.currentThread().interrupt();
			return;
		}
	}



	/**
	 * Returns the clip.
	 * @return AudioClip
	 */
	public AudioClip getClip() throws Exception{
		if(clip == null){
			throw new Exception("Clip = null");
		}
		return clip;
	}
	
	/**
	 * Sets the member URL and member AudioClip to null
	 */	
	public void finalizer(){
		clip = null;
		audioFile = null;
	}

}
