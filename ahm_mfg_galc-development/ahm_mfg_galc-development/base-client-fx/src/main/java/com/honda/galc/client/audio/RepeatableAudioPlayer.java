package com.honda.galc.client.audio;

import java.net.URL;

import com.honda.galc.client.audio.AudioPlayer;

/**
 * <h3>Class description</h3>
 * <h4>
 * RepeatableAudioPlayer 
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
 * @author Paul Chou
 */
public class RepeatableAudioPlayer extends AudioPlayer {

	int repeat = 1;
	public RepeatableAudioPlayer(){
		super();	
	}
	
	public RepeatableAudioPlayer(URL soundURL) throws Exception{
		super(soundURL);
		
	}
	public RepeatableAudioPlayer(String urlString) throws Exception {
		super(urlString);
	}
	
	
	public RepeatableAudioPlayer(String urlString, int repeat) throws Exception{
		super(urlString);
		this.repeat = repeat;
		
	}
	public RepeatableAudioPlayer(URL soundURL, int repeat) throws Exception {
		super(soundURL);
		this.repeat = repeat;
	}
	
	

	@Override
	public void run() {
		if(repeat == -1)
		{
			//super.loop();
			while(true)
			{
				super.run();
				
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					Thread.currentThread().interrupt();
					return;
				}				
			}
		} else {
			for(int i = 0; i < repeat; i++)
			{
				super.run();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					Thread.currentThread().interrupt();
					return;
				}		
			}
		}
			
	}
	

}
