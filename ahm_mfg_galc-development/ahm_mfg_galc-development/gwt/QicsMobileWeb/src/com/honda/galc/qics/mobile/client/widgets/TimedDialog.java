package com.honda.galc.qics.mobile.client.widgets;

import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.client.util.SC;


/**
 * The Class EzDialog provides a dialog that displays a message on then automatically closes
 * after the specified delay.
 */
public class TimedDialog {

	/**
	 * Timed notification.
	 *
	 * @param content the content
	 * @param seconds the seconds
	 */
	public static void timedNotification( String content, int seconds ) {
        SC.showPrompt(content);
        new Timer() {

            @Override
            public void run() {
                SC.clearPrompt();
            }
        }.schedule(seconds * 1000);
	}
}
