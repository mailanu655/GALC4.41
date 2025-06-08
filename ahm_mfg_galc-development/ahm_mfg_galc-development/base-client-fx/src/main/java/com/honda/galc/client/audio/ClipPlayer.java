package com.honda.galc.client.audio;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.QueueProcessor;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ClipPlayer</code> is new audio player use AudioInputStream instead of
 * applet.AudioClip.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Paul Chou</TD>
 * <TD>Apr 16, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public class ClipPlayer extends QueueProcessor<SoundClip> {

    private static final int EXTERNAL_BUFFER_SIZE = 10000;    
	private static ClipPlayer instance;
	
    private volatile boolean stopPlayback;
    private volatile SoundClip playingNow = null;

    private ClipPlayer() {}
	
	public static ClipPlayer getInstance() {
		if (instance == null) {
			instance = new ClipPlayer();
			instance.start();
		}
		return instance;
	}
    
	@Override
	public void processItem(SoundClip soundClip) {
        stopPlayback = false;
        playingNow = soundClip;
        if(soundClip.getRepeat() == -1) {
            while(!stopPlayback) {
                if(!play(soundClip)) {
                	Logger.getLogger().error("Failed to play sound - Contact IT for more assistance.");
                }
            }
        } else {
            for(int i = 0; i < soundClip.getRepeat(); i++) {
            	if (!stopPlayback)
            		play(soundClip);
            }
        }
      	playingNow = null;
	}

    private boolean play(SoundClip soundClip) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundClip.getResource());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
            SourceDataLine sourceLine = (SourceDataLine)AudioSystem.getLine(info);
            sourceLine.open(audioInputStream.getFormat());
            sourceLine.start();

            int bytesRead = 0;
            byte[] databuffer = new byte[EXTERNAL_BUFFER_SIZE];
            
            try {
                while (bytesRead != -1 && !stopPlayback) {
                    bytesRead = audioInputStream.read(databuffer, 0, databuffer.length);
                    if (bytesRead >= 0)
                        sourceLine.write(databuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                Logger.getLogger().warn(e, "Failed to play sound - read audio input stream exception.");
                return false;
            } finally {
                sourceLine.drain();
                sourceLine.close();
            }           
          } catch (Exception e) {
            Logger.getLogger().warn(e, "Failed to play sound: " + soundClip.getSoundName());
            return false;
          } 
          
          Logger.getLogger().check("Successfully played " + soundClip.getSoundName() + " sound" );
          Logger.getLogger().info("Successfully played " + soundClip.getSoundName() + " sound (" + soundClip.getResource().getFile().substring(soundClip.getResource().getFile().lastIndexOf("/") + 1) + ")");
		return true;
    }
    
    public boolean isPlaying(SoundClip soundClip) {
    	if (playingNow == null)
    		return false;
    	
    	return playingNow.equals(soundClip);
    }
    
    public void requestStop() {
    	stopPlayback = true;
    }
    public void flushAll()
    {
    	try {
			ArrayList list=new ArrayList();
			if(queue!=null&&queue.size()>0)
			{
			  queue.drainTo(list);
			  Logger.getLogger().debug("Successfully removed " + list.size() + " sound clips from queue" );
			}
		} catch (Exception e) {
			 Logger.getLogger().warn("Failed to remove sound clips from queue");
			e.printStackTrace();
		}
    }
}
