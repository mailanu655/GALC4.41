package com.honda.galc.device.simulator.client.ui;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * This class is responsible for displaying system clock in status bar
 * and triggers background color change in the message area.
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Jun 22, 2007</TD>
 * <TD>1.0</TD>
 * <TD>20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */
public class StatusBarClock extends Thread {
	private javax.swing.JLabel currentDate;
	private javax.swing.JLabel currentTime;
	private GalcStatusBar owner;
	private boolean running;

	public StatusBarClock() {
		super();
	}
	
	public StatusBarClock(GalcStatusBar aPanel, JLabel Date, JLabel Time) {
		currentTime = Time;
		currentDate = Date;
		owner = aPanel;
	}

	public void run() {
		String DateAndTimeStr = null, DayOfWeekStr = null;
		Date DateAndTime = new Date();
		Calendar Col = null;
		int DayOfWeekInt;
		running = true;

		try {
			while (running) {
				DateAndTime.setTime(System.currentTimeMillis());
		
				DateAndTimeStr = DateFormat.getTimeInstance().format(DateAndTime);
				currentTime.setText(DateAndTimeStr);

				Col = Calendar.getInstance();
				DayOfWeekInt = Col.get(Calendar.DAY_OF_WEEK);
				switch (DayOfWeekInt) {
					case Calendar.SUNDAY:
						DayOfWeekStr = "Sun  ";
						break;
					case Calendar.MONDAY:
						DayOfWeekStr = "Mon  ";
						break;
					case Calendar.TUESDAY:
						DayOfWeekStr = "Tue  ";
						break;
					case Calendar.WEDNESDAY:
						DayOfWeekStr = "Wed  ";
						break;
					case Calendar.THURSDAY:
						DayOfWeekStr = "Thu  ";
						break;
					case Calendar.FRIDAY:
						DayOfWeekStr = "Fri  ";
						break;
					case Calendar.SATURDAY:
						DayOfWeekStr = "Sat  ";
						break;
					default:
						DayOfWeekStr = "";
				}
				DateAndTimeStr = DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(DateAndTime);
				DateAndTimeStr = DayOfWeekStr + DateAndTimeStr;
				currentDate.setText(DateAndTimeStr);

				Thread.sleep(500);
				owner.changeMessageBackgroundColor();
				Thread.sleep(500);
				owner.changeMessageBackgroundColor();
			}
		} catch (Exception e) {
		}
	}
	
	public void stopRunning() {
		running = false;
	}
}
