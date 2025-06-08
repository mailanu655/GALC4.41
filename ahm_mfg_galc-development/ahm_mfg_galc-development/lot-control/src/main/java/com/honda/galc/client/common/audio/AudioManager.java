package com.honda.galc.client.common.audio;

import com.honda.galc.client.audio.AudioPlayer;
import com.honda.galc.client.audio.RepeatableAudioPlayer;
import com.honda.galc.common.logging.Logger;

/**
 * @author ha20794
 *
 * The AudioManager loads audio clips into memory and manages the playing of these clips. Each clip is given a 
 * reference number which maps directly to a member array holding all loaded clips. When a clip is called upon to be played,
 * the AudioManager spawns a new Thread to play the clip.  Periodic calls to resetClips() helps keep resources form being hung
 * be the spawned threads.  Each Thread generated to play an audio clip is managed by a member ThreadGroup. 
 * AudioManager can can hold up to 20 clips in memory.
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AudioManager {
	private static AudioPlayer[] audioClips	=	new AudioPlayer[21];
	private int clipCount = 0;
	
	private ThreadGroup threadGroup = null;
	
	/**
	 * resetClips looks to see if any audioClips are currently playing or are still active for some reason.  Since
	 * the audioClips are run on individual threads that have been registered to the AudioMangers ThreadGroup, this 
	 * method asks the ThreadGroup if there are any active threads. If the response is yes, then the ThreadGroup attempts to 
	 * interrupt all active threads. If all the threads are or become inactive with-in 5 seconds, the ThreadGroup is destroyed
	 * (to release any hung resources of the audioClips) and is set to null. The Thread group is re-created the next time an
	 * audioClip is called on to play.
	 * @throws Exception
	 * */
	public void resetClips() throws Exception{
		synchronized(this){
			if(getThreadGroup().activeCount() != 0){
				try {
					Logger.getLogger().debug(this.getClass().getName()+"resetClips()::Interrupting "+threadGroup.activeCount()+" activeThreads");
					getThreadGroup().interrupt();
					
					for(int retryCount = 0; getThreadGroup().activeCount() != 0 || retryCount < 5; retryCount++){
						wait(100);
					}	
					
				} catch (InterruptedException e) {
					Logger.getLogger().error(this.getClass().getName()+"::resetClips():InterruptedException occured:"+e);
				}
			}
				
			if(getThreadGroup().activeCount() == 0){
				Logger.getLogger().debug(this.getClass().getName()+"resetClips()::Destorying ThreadGroup");
				try {
					threadGroup.destroy();
					threadGroup = null;
				} catch (IllegalThreadStateException e) {
					Logger.getLogger().error(this.getClass().getName()+"resetClips()::IllegalThreadStateException : "+e);
					throw new Exception("Cannot destroy ThreadGroup: Exception is :"+e);
				}
				return;
				
			}else{
				Logger.getLogger().warn(this.getClass().getName()+"::resetClips():Theads cannot be interrupted thus, Threadgroup cannot be destroyed!");
				throw new Exception("Cannot interrupt active Threads:Possible hung thread!");	
			}
		}

	}
	
	/**
	 * loadClip takes a String representation of a URL and creates a Runnable class AudioPlayer to hold the clip in memory.
	 * The created class is referenced by a member Array with the Array Location being returned to the caller.  The returned 
	 * int (Array Location) is used to play the clip later on.
	 * @param String
	 * @throws Exception
	 */	
	public int loadClip(String urlString) throws Exception{
		synchronized(this){
			if(clipCount >= 20){
				throw new Exception("Maximum number of clips have been loaded");
			}
			
			try {
				audioClips[clipCount] = new AudioPlayer(urlString);
			} catch (Exception e) {
				Logger.getLogger().warn(this.getClass().getName()+"::loadClip(String urlString):MalformedURLException:"+e);
				throw new Exception("Load clip failed: The String which was passed in does not map to a valid URL :Exception is "+e);
			}
			
			clipCount++;
			return clipCount-1;
		}
	}
	
	public int loadClip(String urlString, int repeat) throws Exception{
		synchronized(this){
			if(clipCount >= 20){
				throw new Exception("Maximum number of clips have been loaded");
			}
			
			try {
				audioClips[clipCount] = new RepeatableAudioPlayer(urlString, repeat);
			} catch (Exception e) {
				Logger.getLogger().warn(this.getClass().getName()+"::loadClip(String urlString):MalformedURLException:"+e);
				throw new Exception("Load clip failed: The String which was passed in does not map to a valid URL :Exception is "+e);
			}
			
			clipCount++;
			return clipCount-1;
		}
	}
	
	/**
	 * This method spawns a new thread to execute the previously loaded Runnable class AudioPlayer. The clipNumber
	 * represents the location in the member array which holds a collection of loaded AudioPlayers.
	 * @param int
	 * @throws Exception
	 */ 
	public void play(int clipNumber)throws Exception{
		synchronized(this){
			if(audioClips[clipNumber] == null){
				throw new Exception("Play clip failed; The clipNumber passed in to be played has not been Loaded!");			
			}
			
			try{
				new Thread(getThreadGroup(), audioClips[clipNumber]).start();
			} catch (Exception e){
				Logger.getLogger().error(this.getClass().getName()+"Exception occured attempting to play clip:"+e);
				
			}	
		}
	}
	
	/**
	 * This method deletes all previously loaded AudioPlayer audio clips.
	 */
	public void resetAll(){
		synchronized(this){
			clipCount = 0;
			for(int i=0; i<audioClips.length; i++){
				audioClips[i].finalizer();
				audioClips[i] = null;
			}
		}	
	}	
	
	/**
	 * Returns the threadGroup.
	 * @return ThreadGroup
	 */
	private ThreadGroup getThreadGroup() {
		if(threadGroup == null){
			threadGroup = new ThreadGroup("AudioManagerThreadGroup");	
		}
		return threadGroup;
	}

}
