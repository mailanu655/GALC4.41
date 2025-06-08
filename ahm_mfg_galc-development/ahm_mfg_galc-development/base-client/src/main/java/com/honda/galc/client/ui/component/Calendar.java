package com.honda.galc.client.ui.component;


import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.honda.galc.common.exception.TaskException;

/**
 * <h3></h3>
 * <h4>Description</h4>
 * 
 * <h4>Usage and Example</h4>
 * 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Santosh.S.Kulkarni</TD>
 * <TD>(2003/11/14 11:42:00)</TD>
 * <TD>WDHAC-2003/11/14</TD>
 * <TD>Ver_20031114</TD>
 * <TD>Changed protected access specifier to public.</TD>
 * </TR>
 * <TR>
 * <TD>Santosh.S.Kulkarni</TD>
 * <TD>(2003/11/13 18:13:15)</TD>
 * <TD>WDHAC-2003/11/13</TD>
 * <TD>Ver_20031113</TD>
 * <TD>Changed logic for getting the month in int format.</TD>
 * </TR>
 * <TR>
 * <TD>Santosh.S.Kulkarni</TD>
 * <TD>(2003/11/13 15:15:35)</TD>
 * <TD>WDHAC-2003/11/13</TD>
 * <TD>Ver_20031113</TD>
 * <TD>Output the stack trace to console.</TD>
 * </TR>
 * <TR>
 * <TD>Santosh.S.Kulkarni</TD>
 * <TD>(2003/10/29 15:33:35)</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD>New release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Santosh.S.Kulkarni
 */
public class Calendar extends JDialog implements Serializable {
	//_____________________________________________________________________________________________________________
	// Start of screen display variables.      
	//_____________________________________________________________________________________________________________

	/**
	 * 
	 */
	private static final long serialVersionUID = 621882398172606473L;
	private javax.swing.JPanel jContentPaneOfMainScreen = null;
	private javax.swing.JPanel jpnlMainPanel = null;
	private javax.swing.JPanel jpnlYear = null;
	private javax.swing.JButton jbtnPreviousYear = null;
	private javax.swing.JButton jbtnNextYear = null;
	private javax.swing.JTextField jtxtSelectedYear = null;
	private javax.swing.JPanel jpnlMonth = null;
	private javax.swing.JButton jbtnPreviousMonth = null;
	private javax.swing.JButton jbtnNextMonth = null;
	private javax.swing.JTextField jtxtSelectedMonth = null;
	private javax.swing.JPanel jpnlDate = null;
	private javax.swing.JButton jbtnClose = null;
	private javax.swing.JButton jbtnOk = null;
	private javax.swing.JTable jtblDate = null;
	private javax.swing.JTextField jtfldTime = null;
	private javax.swing.JLabel hourLabel = null;
	private javax.swing.JSpinner hourSpinner = null;
	private javax.swing.JLabel minuteLabel = null;
	private javax.swing.JSpinner minuteSpinner = null;
	private javax.swing.JLabel secondLabel = null;
	private javax.swing.JSpinner secondSpinner = null;

	//_____________________________________________________________________________________________________________
	// End of screen display variables.      
	//_____________________________________________________________________________________________________________

	//_____________________________________________________________________________________________________________
	// Start of business logic variables.      
	//_____________________________________________________________________________________________________________
	private final static int FIRST_DAY_OF_THE_MONTH = 1;
	private final static int MIN_YEAR = 1800;
	private final static int MAX_YEAR = 9999;
	private final static int DEFAULT_XLOC = 1024 / 2;
	private final static int DEFAULT_YLOC = 768 / 2;	
	

	// Variables containing available years, months and dates.
	private Vector m_vctYear;
	private Vector m_vctMonths;
	private Vector m_vctDays;

	// Contains available dates for a particular month in a year.
	private String m_arrDate[][];

	// Table cell selection
	private int m_intSelectedRow;
	private int m_intSelectedCol;

	// Calendar location
	private int m_intXloc;
	private int m_intYloc;

	// Current year, month day and date.
	private String m_strSelectedYear;
	private String m_strSelectedMonth;
	private String m_strSelectedDay;
	private String m_strSelectedDate;

	// Selected hour, minute and seconds.
	private String m_strSelectedHour;
	private String m_strSelectedMinute;
	private String m_strSelectedSeconds;
	
	private boolean isCalTimeStamp;
	private Pattern pattern;
	private Matcher matcher;
	private String finalTextDate;
	
	private static final String TIME24HOURS_PATTERN = 
        "([01]?[0-9]|2[0-3]).[0-5][0-9].[0-5][0-9]";

	

	// Custom date model class
	private DateModel m_dateModel;
	// Color Renderer class
	private ColorDisplay m_colorDisplay;
	// Event handler class
	private EventHandler m_eventHandler;
	// Object of the class which requires information about calendar events
	// such as click on a date or close button. This variable is set by the
	// class which initializes Calendar class.  
	private Object m_CallerClassEventHandler;

	//_____________________________________________________________________________________________________________
	// End of business logic variables.      
	//_____________________________________________________________________________________________________________

	//_____________________________________________________________________________________________________________
	// Start of initialization methods.      
	//_____________________________________________________________________________________________________________

	/**
	 * Initialize calendar
	 * @param Object Event handler of the caller class
	 */
	public Calendar(JFrame owner, Object aEventHandler, int aXlocation, int aYLocation) throws TaskException {
		super(owner);
		try {			
			setCallerClassEventHandler(aEventHandler);
			setXlocation(aXlocation);
			setYlocation(aYLocation);			
			initialize();			
			//this.setVisible(true);					
		
		} catch (Exception exc) {
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
			throw new TaskException();
		}
	}
	/**
	 * This method initializes 
	 * 
	 */
	public Calendar(JFrame owner, Object aEventHandler, int aXlocation, int aYLocation, boolean displayTime) throws TaskException {
		this(owner, aEventHandler, aXlocation, aYLocation);
		getJTextFldTime().setVisible(displayTime);
		isCalTimeStamp = displayTime;
	}
	
	/**
	 * This method initializes 
	 * 
	 */
	public Calendar() throws TaskException {
		this(null, null, DEFAULT_XLOC, DEFAULT_YLOC);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() throws TaskException {
		try {

			setTodaysDate();
			displayCalendar();

		} catch (TaskException sysexc) {
			/*
			 * Start Ver_20031113 
			 */
			sysexc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
			throw new TaskException();
		} catch (Exception exc) {
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
			throw new TaskException();
		}
		setContentPane(getJContentPaneOfMainScreen());
		setSize(416, 270);
		setLocation(getXlocation(), getYlocation());
		setTitle("Calendar");
		//setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	}

	//_____________________________________________________________________________________________________________
	// End of initialization methods.      
	//_____________________________________________________________________________________________________________

	//_____________________________________________________________________________________________________________
	// Start of screen display methods.      
	//_____________________________________________________________________________________________________________
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPaneOfMainScreen() {
		if (jContentPaneOfMainScreen == null) {
			jContentPaneOfMainScreen = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout1 =
				new java.awt.BorderLayout();
			jContentPaneOfMainScreen.setLayout(layBorderLayout1);
			jContentPaneOfMainScreen.add(
				getJpnlMainPanel(),
				java.awt.BorderLayout.CENTER);
		}
		return jContentPaneOfMainScreen;
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpnlMainPanel() {
		if (jpnlMainPanel == null) {
			jpnlMainPanel = new javax.swing.JPanel();
			jpnlMainPanel.setLayout(null);
			jpnlMainPanel.add(getJpnlYear(), null);
			jpnlMainPanel.add(getJpnlMonth(), null);
			jpnlMainPanel.add(getJpnlDate(), null);
			//jpnlMainPanel.add(getJTextFldTime(),null);
			jpnlMainPanel.add(getJbtnOk());
			jpnlMainPanel.add(getJbtnClose(), null);
			jpnlMainPanel.add(getHourLabel());
			jpnlMainPanel.add(getHourSpinner());
			jpnlMainPanel.add(getMinuteLabel());
			jpnlMainPanel.add(getMinuteSpinner());
			jpnlMainPanel.add(getSecondLabel());
			jpnlMainPanel.add(getSecondSpinner());
		}
		return jpnlMainPanel;
	}
	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpnlYear() {
		if (jpnlYear == null) {
			jpnlYear = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout8 =
				new java.awt.BorderLayout();
			jpnlYear.setLayout(layBorderLayout8);
			jpnlYear.add(getJbtnPreviousYear(), java.awt.BorderLayout.WEST);
			jpnlYear.add(getJbtnNextYear(), java.awt.BorderLayout.EAST);
			jpnlYear.add(getJtxtSelectedYear(), java.awt.BorderLayout.CENTER);
			jpnlYear.setSize(170, 15);
			jpnlYear.setLocation(5, 5);
		}
		return jpnlYear;
	}
	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbtnPreviousYear() {
		if (jbtnPreviousYear == null) {
			jbtnPreviousYear = new javax.swing.JButton();
			jbtnPreviousYear.setText("<");
			jbtnPreviousYear.addActionListener(getEventHandler());
		}
		return jbtnPreviousYear;
	}
	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbtnNextYear() {
		if (jbtnNextYear == null) {
			jbtnNextYear = new javax.swing.JButton();
			jbtnNextYear.setText(">");
			jbtnNextYear.addActionListener(getEventHandler());
		}
		return jbtnNextYear;
	}
	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtxtSelectedYear() {
		if (jtxtSelectedYear == null) {
			jtxtSelectedYear = new javax.swing.JTextField();
			jtxtSelectedYear.setHorizontalAlignment(
				javax.swing.JTextField.CENTER);
			jtxtSelectedYear.setEditable(false);
		}
		return jtxtSelectedYear;
	}
	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpnlMonth() {
		if (jpnlMonth == null) {
			jpnlMonth = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout9 =
				new java.awt.BorderLayout();
			jpnlMonth.setLayout(layBorderLayout9);
			jpnlMonth.add(getJbtnPreviousMonth(), java.awt.BorderLayout.WEST);
			jpnlMonth.add(getJbtnNextMonth(), java.awt.BorderLayout.EAST);
			jpnlMonth.add(getJtxtSelectedMonth(), java.awt.BorderLayout.CENTER);
			jpnlMonth.setSize(170, 15);
			jpnlMonth.setLocation(235, 5);
		}
		return jpnlMonth;
	}
	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbtnPreviousMonth() {
		if (jbtnPreviousMonth == null) {
			jbtnPreviousMonth = new javax.swing.JButton();
			jbtnPreviousMonth.setText("<");
			jbtnPreviousMonth.addActionListener(getEventHandler());
			
			// Enable or disable JbtnPreviousMonth button.
			int intMonthIndex = getMonths().indexOf(m_strSelectedMonth);
			if (intMonthIndex == 0) {
				// month is January
				jbtnPreviousMonth.setEnabled(false);
			} else {
				jbtnPreviousMonth.setEnabled(true);
			}
		}
		return jbtnPreviousMonth;
	}
	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbtnNextMonth() {
		if (jbtnNextMonth == null) {
			jbtnNextMonth = new javax.swing.JButton();
			jbtnNextMonth.setText(">");
			jbtnNextMonth.addActionListener(getEventHandler());
			
			// Enable or disable JbtnNextMonth button.
			int intMonthIndex = getMonths().indexOf(m_strSelectedMonth);
			if (intMonthIndex == 11) {
				// month is December
				jbtnNextMonth.setEnabled(false);
			} else {
				jbtnNextMonth.setEnabled(true);
			}
		}
		return jbtnNextMonth;
	}
	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtxtSelectedMonth() {
		if (jtxtSelectedMonth == null) {
			jtxtSelectedMonth = new javax.swing.JTextField();
			jtxtSelectedMonth.setHorizontalAlignment(
				javax.swing.JTextField.CENTER);
			jtxtSelectedMonth.setEditable(false);
		}
		return jtxtSelectedMonth;
	}
	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpnlDate() {
		if (jpnlDate == null) {
			jpnlDate = new javax.swing.JPanel();
			jpnlDate.add(getJtblDate(), null);
			jpnlDate.setBounds(19, 30, 374, 134);
		}
		return jpnlDate;
	}
	
	/*
	 * This method initializes ok button
	 * */
	
	private javax.swing.JButton getJbtnOk() {
		if (jbtnOk == null) {
			jbtnOk = new javax.swing.JButton();
			jbtnOk.setSize(60, 15);
			jbtnOk.setLocation(120, 220);
			jbtnOk.setText("Ok");
			// Register with this class listener.
			jbtnOk.addActionListener(getEventHandler());
			// Register with parent class,i.e ListPrintScreen, listener.
			jbtnOk.addActionListener(
				(java.awt.event.ActionListener) getCallerClassEventHandler());
		}
		return jbtnOk;
	}
	
	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbtnClose() {
		if (jbtnClose == null) {
			jbtnClose = new javax.swing.JButton();
			jbtnClose.setSize(80, 15);
			jbtnClose.setLocation(190, 220);
			jbtnClose.setText("Cancel");
			// Register with this class listener.
			jbtnClose.addActionListener(getEventHandler());
			// Register with parent class,i.e ListPrintScreen, listener.
			jbtnClose.addActionListener(
				(java.awt.event.ActionListener) getCallerClassEventHandler());
		}
		return jbtnClose;
	}
	
	/*Spinner codes
	 * */
	private JLabel getHourLabel(){
		if(hourLabel == null){
			hourLabel = new JLabel("HH");
			hourLabel.setBounds(132, 153, 40, 30);
		}
		return hourLabel;	
	}
	
	private JLabel getMinuteLabel(){
		if(minuteLabel == null){
			minuteLabel = new JLabel("MM");
			minuteLabel.setBounds(172, 153, 40, 30);
		}
		return minuteLabel;
	}
	
	private JLabel getSecondLabel(){
		if(secondLabel == null){
			secondLabel = new JLabel("SS");
			secondLabel.setBounds(212, 153, 40, 30);
		}
		return secondLabel;
	}
	
	private JSpinner getHourSpinner(){
		if (hourSpinner == null) {
			hourSpinner = new JSpinner( new SpinnerDateModel() );
			JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(hourSpinner, "HH");
			hourSpinner.setEditor(timeEditor);
			hourSpinner.setValue(new Date());
			hourSpinner.setBounds(130, 180, 40, 30);
			hourSpinner.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		        	SimpleDateFormat formatHour = new SimpleDateFormat("HH");
		        	String strHour = formatHour.format(getHourSpinner().getValue());
		        	setSelectedHour(strHour);
		        }
		     });
		}
		return hourSpinner;
	}
	
	private JSpinner getMinuteSpinner(){
		if(minuteSpinner == null){
			minuteSpinner= new JSpinner( new SpinnerDateModel() );
			JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(minuteSpinner, "mm");
			minuteSpinner.setEditor(timeEditor);
			minuteSpinner.setValue(new Date());
			minuteSpinner.setBounds(170, 180, 40, 30);
			minuteSpinner.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		        	SimpleDateFormat formatMinute = new SimpleDateFormat("mm");
		        	String strMinute = formatMinute.format(getMinuteSpinner().getValue());
		        	setSelectedMinute(strMinute);
		        	
		        }
		     });
		}
		return minuteSpinner;
	}
	
	private JSpinner getSecondSpinner(){
		if(secondSpinner == null){
			secondSpinner= new JSpinner( new SpinnerDateModel() );
			JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(secondSpinner, "ss");
			secondSpinner.setEditor(timeEditor);
			secondSpinner.setValue(new Date());
			secondSpinner.setBounds(210, 180, 40, 30);
			secondSpinner.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		        	SimpleDateFormat formatSeconds = new SimpleDateFormat("ss");
		        	String strSeconds = formatSeconds.format(getSecondSpinner().getValue());
		        	setSelectedSeconds(strSeconds);
		        }
		     });
		}
		return secondSpinner;
	}
	
	
	
	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private javax.swing.JTable getJtblDate() {
		if (jtblDate == null) {
			jtblDate = new javax.swing.JTable();
			jtblDate.setCellSelectionEnabled(true);
			jtblDate.setRowSelectionAllowed(true);
			jtblDate.setColumnSelectionAllowed(true);
			jtblDate.setAutoResizeMode(
				javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			jtblDate.setRowHeight(16);
			jtblDate.setSelectionMode(
				javax.swing.ListSelectionModel.SINGLE_SELECTION);
			// Register with this class listener.
			jtblDate.addMouseListener(getEventHandler());
			// Register with parent class,i.e ListPrintScreen, listener.
			jtblDate.addMouseListener(
				(java.awt.event.MouseListener) getCallerClassEventHandler());
		}
		return jtblDate;
	}
	//_____________________________________________________________________________________________________________
	// End of screen display methods.      
	//_____________________________________________________________________________________________________________
	//_____________________________________________________________________________________________________________
	// Start of data display methods.      
	//_____________________________________________________________________________________________________________
	/**
	 * Get the available years.
	 * @return Vector Available years.
	 */
	private Vector getYear() {

		if (m_vctYear == null) {
			m_vctYear = new Vector();
			for (int intYearCount = MIN_YEAR;
				intYearCount < MAX_YEAR;
				intYearCount++) {
				m_vctYear.add(String.valueOf(intYearCount));
			}
		}

		return m_vctYear;
	}
	/**
	 * Gets the available months
	 * @return Vector Available months
	 */
	private Vector getMonths() {

		if (m_vctMonths == null) {
			m_vctMonths = new Vector();
			m_vctMonths.add("January");
			m_vctMonths.add("February");
			m_vctMonths.add("March");
			m_vctMonths.add("April");
			m_vctMonths.add("May");
			m_vctMonths.add("June");
			m_vctMonths.add("July");
			m_vctMonths.add("August");
			m_vctMonths.add("September");
			m_vctMonths.add("October");
			m_vctMonths.add("November");
			m_vctMonths.add("December");
		}

		return m_vctMonths;
	}
	/**
	 * Gets he available days of the week.
	 * @return Vector Days of the week.
	 */
	private Vector getDays() {

		if (m_vctDays == null) {
			m_vctDays = new Vector();
			m_vctDays.add("Mon");
			m_vctDays.add("Tue");
			m_vctDays.add("Wed");
			m_vctDays.add("Thu");
			m_vctDays.add("Fri");
			m_vctDays.add("Sat");
			m_vctDays.add("Sun");
		}

		return m_vctDays;
	}
	/**
	 * Gets the dates based.
	 * @return String date.
	 */
	private String[][] getDate() {
		if (m_arrDate == null) {
			m_arrDate = new String[7][7];
		}
		return m_arrDate;
	}
	/**
	 * Sets today's Date
	 */
	private void setTodaysDate() throws TaskException {

		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		// Year format eg: 2003
		SimpleDateFormat formatIntMonth = new SimpleDateFormat("MM");
		// Month format eg: 10
		SimpleDateFormat formatDate = new SimpleDateFormat("dd");

		try {

			Date date = new Date(System.currentTimeMillis());

			// Set the year and month text fields
			String strYear = formatYear.format(date);
			String strIntMonth = formatIntMonth.format(date);
			String strDate = formatDate.format(date);

			/*
			 * Start Ver_20031113
			 * getMonths() return a vector which contains a list of available
			 * months in aplhabets. The vector index is from 0 to 11 (Jan to Feb). 
			 * strIntMonth containts month from 1 to 12 (Jan to Dec).   
			 */
			String strMonth =
				(String) getMonths().get(Integer.parseInt(strIntMonth) - 1);
			/*
			 * End Ver_20031113
			 */

			// If the date contains 0 as prefix , then remove it.
			// Eg 01 -> 1, 02 -> 2 .....
			strDate =
				(strDate.startsWith("0")) ? strDate.substring(1) : strDate;

			// Set the current date as the Selected date.
			setSelectedYear(strYear);
			setSelectedMonth(strMonth);
			setSelectedDate(strDate);

			/*
			 * Start Ver_20031113 
			 */
			// Get the index of the month by using getMonths().
			//			String strIntMonth = "";
			for (int intMonthCount = 0;
				intMonthCount < getMonths().size();
				intMonthCount++) {
				if (strMonth != null || strMonth != "") {

					if (strMonth
						.equalsIgnoreCase(
							(String) getMonths().get(intMonthCount))) {
						strIntMonth = String.valueOf(intMonthCount);
					}
				}
			}

			// Set the dates of the calendar based on the year and month.			
			setCalendarDates(getSelectedYear(), strIntMonth);
			//String.valueOf(getMonths().indexOf(getSelectedMonth())));

			/*
			 * End Ver_20031113 
			 */

			// Display the year and month.
			displayYear(strYear);
			displayMonth(strMonth);

		} catch (Exception exc) {
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
			throw new TaskException();
		}
	}

	/**
	 * Set the date jTable.
	 * @param String Selected year.
	 * @param String Selected month.
	 */
	private void setCalendarDates(String aYear, String aMonth)
		throws TaskException {

		int intYear = 0;
		int intMonth = 0;
		int intDate = 0;

		int intStartingCol = 0;

		// Refresh color display
		getColorDisplay().highlightDate(-1, -1);

		// Check the date components.
		try {
			intYear = Integer.parseInt(aYear);
			intMonth = Integer.parseInt(aMonth);
			// Reset month since it is zero based.
			// i.e January = 0
			intDate = FIRST_DAY_OF_THE_MONTH;
		} catch (NumberFormatException exc) {
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
			throw new TaskException();
		}

		java.util.Calendar cal = java.util.Calendar.getInstance();

		// Flush the previous values.
		cal.clear();
		cal.setLenient(false);
		cal.set(intYear, intMonth, intDate);
		
		switch (cal.get(java.util.Calendar.DAY_OF_WEEK)) {
			case java.util.Calendar.MONDAY :
				{
					intStartingCol = 0;
					break;
				}
			case java.util.Calendar.TUESDAY :
				{
					intStartingCol = 1;
					break;
				}
			case java.util.Calendar.WEDNESDAY :
				{
					intStartingCol = 2;
					break;
				}
			case java.util.Calendar.THURSDAY :
				{
					intStartingCol = 3;
					break;
				}
			case java.util.Calendar.FRIDAY :
				{
					intStartingCol = 4;
					break;
				}
			case java.util.Calendar.SATURDAY :
				{
					intStartingCol = 5;
					break;
				}
			case java.util.Calendar.SUNDAY :
				{
					intStartingCol = 6;
					break;
				}
		}

		// Get details of the first day of the month.
		for (int intRows = 0; intRows < 7; intRows++) {
			for (int intColumns = 0; intColumns < 7; intColumns++) {

				// Since the first row is taken up by days of the week.
				if (intRows == 0) {
					getDate()[intRows][intColumns] = "";
					continue;
				}

				if (intDate > cal.getActualMaximum(cal.DAY_OF_MONTH)) {
					getDate()[intRows][intColumns] = "";
				} else {
					if (intRows == 1 && intColumns < intStartingCol) {
						getDate()[intRows][intColumns] = "";
					} else {
						getDate()[intRows][intColumns] =
							String.valueOf(intDate);
						// Mark the date with some color. 
						// Do not mark the table cells for February 30 or 31
						if (getSelectedDate()
							.equals(String.valueOf(intDate))) {
							getColorDisplay().highlightDate(
									intRows,
									intColumns);
						}
						// Increase the date.
						intDate++;
					}
				}
			}
		}
	}

	/**
	 * Sets the calendar
	 */
	private void displayCalendar() {

		// Set the color renderer
		getJtblDate().repaint();
		getJtblDate().setDefaultRenderer(Object.class, getColorDisplay());
		getJtblDate().setModel(getDateModel());
		// Set column width
		getJtblDate().getColumnModel().getColumn(0).setPreferredWidth(40);		
		getJtblDate().getColumnModel().getColumn(1).setPreferredWidth(40);
		getJtblDate().getColumnModel().getColumn(2).setPreferredWidth(40);
		getJtblDate().getColumnModel().getColumn(3).setPreferredWidth(40);
		getJtblDate().getColumnModel().getColumn(4).setPreferredWidth(40);
		getJtblDate().getColumnModel().getColumn(5).setPreferredWidth(40);
		getJtblDate().getColumnModel().getColumn(6).setPreferredWidth(40);
	}

	/**
	 * Displays year in the JtxtSelectedYear text box.
	 * @param String Year
	 */
	private void displayYear(String aYear) {
		getJtxtSelectedYear().setText(aYear);
	}

	/**
	 * Display month in the 
	 * @param String month.
	 */
	private void displayMonth(String aMonth) {
		getJtxtSelectedMonth().setText(aMonth);
	}

	/**
	 * Returns ColorDisplay object
	 * @return ColorDisplay object
	 */
	private ColorDisplay getColorDisplay() {
		return (
			(m_colorDisplay == null)
				? (m_colorDisplay = new ColorDisplay())
				: m_colorDisplay);
	}

	/**
	 * Returs Date model
	 * @return DateModel object
	 */
	private DateModel getDateModel() {
		return (
			(m_dateModel == null) ? m_dateModel =
				new DateModel() : m_dateModel);
	}
	//_____________________________________________________________________________________________________________
	// End of data display methods.      
	//_____________________________________________________________________________________________________________

	//_____________________________________________________________________________________________________________
	// Start of event generation classes and methods.      
	//_____________________________________________________________________________________________________________
	/**
	 * Event handler for the various components of the present class.
	 * 
	 */
	private class EventHandler
		implements java.awt.event.ActionListener, java.awt.event.MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			//Incase of next year button.
			if (e.getSource() == Calendar.this.getJbtnNextYear()) {
				nextYearBtn_ActionPerformed(e);
			}
			//Incase of previous year button.
			if (e.getSource() == Calendar.this.getJbtnPreviousYear()) {
				previousYearBtn_ActionPerformed(e);
			}
			//Incase of next month button.
			if (e.getSource() == Calendar.this.getJbtnNextMonth()) {
				nextmonthBtn_ActionPerformed(e);
			}
			//Incase of previous month button.
			if (e.getSource() == Calendar.this.getJbtnPreviousMonth()) {
				previousMonthBtn_ActionPerformed(e);
			}
			//Incase of Ok Button
			if (e.getSource() == Calendar.this.getJbtnOk()) {
				okBtn_ActionPerformed(e);
			}
			//Incase of close button.
			if (e.getSource() == Calendar.this.getJbtnClose()) {
				closeBtn_ActionPerformed(e);
			}
		}

		public void mouseClicked(java.awt.event.MouseEvent e) {
			// Incase of cell selection in the table.
			dateCell_mouseClicked(e);
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}

	}

	/**
	 * Handles event of JbtnNextYear
	 * @param ActionEvent action
	 */
	private void nextYearBtn_ActionPerformed(
		java.awt.event.ActionEvent action) {

		// Get the Selected year from the JtxtSelectedYear
		String strSelectedYear = getJtxtSelectedYear().getText();

		// Find the index of the selected year in m_vctYear vector. 
		int intYearIndex = getYear().indexOf(strSelectedYear);

		// Display the calendar.		
		setSelectedYear((String) getYear().get(intYearIndex + 1));
		try {

			/*
			 * Start Ver_20031113 
			 */

			String strMonth = getSelectedMonth();
			// Get the index of the month by using getMonths().
			String strIntMonth = "";
			for (int intMonthCount = 0;
				intMonthCount < getMonths().size();
				intMonthCount++) {
				if (strMonth != null || strMonth != "") {
					if (strMonth
						.equalsIgnoreCase(
							(String) getMonths().get(intMonthCount))) {
						strIntMonth = String.valueOf(intMonthCount);
					}
				}
			}

			// Set the dates of the calendar based on the year and month.
			setCalendarDates(getSelectedYear(), strIntMonth);
			//String.valueOf(getMonths().indexOf(getSelectedMonth())));

			/*
			 * End Ver_20031113 
			 */

			displayCalendar();
		} catch (Exception exc) {
			// Do not display any log message.
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
		}

		// Display in the text box.
		getJtxtSelectedYear().setText(getSelectedYear());

		// Enable or disable JbtnNextYear button.
		if (intYearIndex + 2 >= getYear().size()) {
			getJbtnNextYear().setEnabled(false);
		} else {
			getJbtnNextYear().setEnabled(true);
		}

		// Always enable JbtnPreviousYear when JbtnNextYear is clicked.
		getJbtnPreviousYear().setEnabled(true);
	}
	/**
	 * Handles event of JbtnPreviousYear
	 * @param ActionEvent action
	 */
	private void previousYearBtn_ActionPerformed(
		java.awt.event.ActionEvent action) {

		// Get the Selected year from the JtxtSelectedYear
		String strSelectedYear = getJtxtSelectedYear().getText();

		// Find the index of the selected year in m_vctYear vector. 
		int intYearIndex = getYear().indexOf(strSelectedYear);

		// Display the calendar.		
		setSelectedYear((String) getYear().get(intYearIndex - 1));

		try {

			/*
			 * Start Ver_20031113 
			 */

			String strMonth = getSelectedMonth();
			// Get the index of the month by using getMonths().
			String strIntMonth = "";
			for (int intMonthCount = 0;
				intMonthCount < getMonths().size();
				intMonthCount++) {
				if (strMonth != null || strMonth != "") {

					if (strMonth
						.equalsIgnoreCase(
							(String) getMonths().get(intMonthCount))) {
						strIntMonth = String.valueOf(intMonthCount);
					}
				}
			}

			// Set the dates of the calendar based on the year and month.
			setCalendarDates(getSelectedYear(), strIntMonth);
			//String.valueOf(getMonths().indexOf(getSelectedMonth())));

			/*
			 * End Ver_20031113 
			 */

			displayCalendar();
		} catch (Exception exc) {
			exc.printStackTrace();
			// Do not display any log message.
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
		}

		// Display in the text box.
		getJtxtSelectedYear().setText(getSelectedYear());

		// Enable or disable JbtnNextYear button.
		if (intYearIndex - 1 <= 0) {
			getJbtnPreviousYear().setEnabled(false);
			getJbtnNextYear().setEnabled(true);
		} else {
			getJbtnPreviousYear().setEnabled(true);
		}

		// Always enable JbtnNextYear when JbtnPreviousYear is clicked.
		getJbtnNextYear().setEnabled(true);
	}
	/**
	 * Handles event of JbtnNextMonth
	 * @param ActionEvent action
	 */
	private void nextmonthBtn_ActionPerformed(
		java.awt.event.ActionEvent action) {

		// Get the Selected month from the JtxtSelectedMonth
		String strSelectedMonth = getJtxtSelectedMonth().getText();

		// Find the index of the selected month in m_vctYear vector. 
		int intMonthIndex = getMonths().indexOf(strSelectedMonth);

		// Display the calendar.		
		setSelectedMonth((String) getMonths().get(intMonthIndex + 1));
		try {

			/*
			 * Start Ver_20031113 
			 */

			String strMonth = getSelectedMonth();
			// Get the index of the month by using getMonths().
			String strIntMonth = "";
			for (int intMonthCount = 0;
				intMonthCount < getMonths().size();
				intMonthCount++) {
				if (strMonth != null || strMonth != "") {

					if (strMonth
						.equalsIgnoreCase(
							(String) getMonths().get(intMonthCount))) {
						strIntMonth = String.valueOf(intMonthCount);
					}
				}
			}

			// Set the dates of the calendar based on the year and month.
			setCalendarDates(getSelectedYear(), strIntMonth);
			//String.valueOf(getMonths().indexOf(getSelectedMonth())));

			/*
			 * End Ver_20031113 
			 */

			displayCalendar();
		} catch (Exception exc) {
			// Do not display any log message.
			/*
			 * Start Ver_20031113 
			 */
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
		}

		// Display in the text box.
		getJtxtSelectedMonth().setText(getSelectedMonth());

		// Enable or disable JbtnNextYear button.
		if (intMonthIndex + 2 >= getMonths().size()) {
			getJbtnNextMonth().setEnabled(false);
		} else {
			getJbtnNextMonth().setEnabled(true);
		}

		// Always enable JbtnPreviousMonth when getJbtnNextMonth is clicked.
		getJbtnPreviousMonth().setEnabled(true);

	}
	/**
	 * Handles event of JbtnPreviousMonth
	 * @param ActionEvent action
	 */
	private void previousMonthBtn_ActionPerformed(
		java.awt.event.ActionEvent action) {

		// Get the Selected month from the JtxtSelectedMonth
		String strSelectedMonth = getJtxtSelectedMonth().getText();

		// Find the index of the selected month in m_vctYear vector. 
		int intMonthIndex = getMonths().indexOf(strSelectedMonth);

		// Display the calendar.		
		setSelectedMonth((String) getMonths().get(intMonthIndex - 1));

		try {

			/*
			 * Start Ver_20031113 
			 */

			String strMonth = getSelectedMonth();
			// Get the index of the month by using getMonths().
			String strIntMonth = "";
			for (int intMonthCount = 0;
				intMonthCount < getMonths().size();
				intMonthCount++) {
				if (strMonth != null || strMonth != "") {

					if (strMonth
						.equalsIgnoreCase(
							(String) getMonths().get(intMonthCount))) {
						strIntMonth = String.valueOf(intMonthCount);
					}
				}
			}

			// Set the dates of the calendar based on the year and month.
			setCalendarDates(getSelectedYear(), strIntMonth);
			//String.valueOf(getMonths().indexOf(getSelectedMonth())));

			/*
			 * End Ver_20031113 
			 */

			displayCalendar();
		} catch (Exception exc) {
			// Do not display any log message.
			/*
			* Start Ver_20031113 
			*/
			exc.printStackTrace();
			/*
			 * End Ver_20031113 
			 */
		}

		// Display in the text box.
		getJtxtSelectedMonth().setText(getSelectedMonth());

		// Enable or disable JbtnNextYear button.
		if (intMonthIndex - 1 <= 0) {
			getJbtnPreviousMonth().setEnabled(false);

		} else {
			getJbtnPreviousMonth().setEnabled(true);
		}

		// Always enable getJbtnNextMonth when JbtnPreviousMonth is clicked.
		getJbtnNextMonth().setEnabled(true);

	}
	
	public String getFinalTextDate() {
		return finalTextDate;
	}
	
	/**
	 * Handles event of JbtnOk
	 * @param ActionEvent action
	 */
	private void okBtn_ActionPerformed(java.awt.event.ActionEvent action) {		
		finalTextDate = getCalendarDate();
		dispose();
	}
	
	/**
	 * Handles event of JbtnClose
	 * @param ActionEvent action
	 */
	private void closeBtn_ActionPerformed(java.awt.event.ActionEvent action) {		
		dispose();
	}
	
	/**
	 * Handles event of cell selection in the table
	 * @param SelectionEvent event
	 */
	private void dateCell_mouseClicked(java.awt.event.MouseEvent event) {
		// Get the cell selected by the user.
		if (event.getClickCount() == 1) {
			int row = getJtblDate().getSelectedRow();
			int col = getJtblDate().getSelectedColumn();
			setSelectedRow(row);
			setSelectedColumn(col);
			setSelectedDate(getDate()[getSelectedRow()][getSelectedColumn()]);
			try {
				/*
				 * Start Ver_20031113 
				 */

				String strMonth = getSelectedMonth();
				// Get the index of the month by using getMonths().
				String strIntMonth = "";
				for (int intMonthCount = 0;
					intMonthCount < getMonths().size();
					intMonthCount++) {
					if (strMonth != null || strMonth != "") {

						if (strMonth
							.equalsIgnoreCase(
								(String) getMonths().get(intMonthCount))) {
							strIntMonth = String.valueOf(intMonthCount);
						}
					}
				}

				// Set the dates of the calendar based on the year and month.
				setCalendarDates(getSelectedYear(), strIntMonth);				

				/*
				 * End Ver_20031113 
				 */

				displayCalendar();

			} catch (Exception exc) {
				// Do not display any log message.
				/*
				* Start Ver_20031113 
				*/
				exc.printStackTrace();
				/*
				 * End Ver_20031113 
				 */
			}
		}
	}

	//_____________________________________________________________________________________________________________
	// End of event generation classes and methods. 
	//_____________________________________________________________________________________________________________

	//_____________________________________________________________________________________________________________
	// Start of miscellaneous and helper methods. 
	//_____________________________________________________________________________________________________________

	/**
	 * Date model class
	 */
	public class DateModel extends AbstractTableModel {
		public int getColumnCount() {
			return 7;
		}
		public int getRowCount() {
			return 7;
		}
		public String getColumnName(int col) {
			return "";
		}
		public Object getValueAt(int row, int col) {
			if (row == 0)
				return getDays().get(col);
			else
				return getDate()[row][col];
		}
		public Class getColumnClass(int c) {
			return getValueAt(0,c).getClass();
		}
	}

	/**
	 * Color renderer for the Jtable cells.
	 */
	public class ColorDisplay
		extends DefaultTableCellRenderer
		implements TableCellRenderer {

		private int m_Row;
		private int m_Col;

		public ColorDisplay() {
			super();
		}

		/**
		 * Sets the item of the table whose color has to be changed.
		 * @param int Row
		 * @param int Column
		 */
		private void highlightDate(int aRow, int aCol) {
			m_Row = aRow;
			m_Col = aCol;
		}

		/**
		 * Overriden method of getTableCellRendererComponent
		 */
		public Component getTableCellRendererComponent(
			javax.swing.JTable aTable,
			Object aColor,
			boolean aSelected,
			boolean aHasFocus,
			int aRow,
			int aCol) {

			super.getTableCellRendererComponent(
				aTable,
				aColor,
				aSelected,
				aHasFocus,
				aRow,
				aCol);

			// Change the color of the selected item.
			if (aRow == m_Row && aCol == m_Col)
				this.setBackground(Color.yellow);
			else
				this.setBackground(Color.lightGray);
			return this;
		}
	}

	/**
	 * Set X location
	 * @param int X location
	 */
	private void setXlocation(int aXloc) {
		m_intXloc = aXloc;
	}

	/**
	 * Get X location
	 * @return int X location
	 */
	private int getXlocation() {
		return m_intXloc;
	}

	/**
	 * Set Y location
	 * @param int Y location
	 */
	private void setYlocation(int aYloc) {
		m_intYloc = aYloc;
	}

	/**
	 * Get Y location
	 * @return int Y location
	 */
	private int getYlocation() {
		return m_intYloc;
	}

	/**
	 * Set the selected row
	 * @param int Row
	 */
	private void setSelectedRow(int aRow) {
		m_intSelectedRow = aRow;
	}

	/**
	 * Gets the selcted row
	 * @return int Row
	 */
	private int getSelectedRow() {
		return m_intSelectedRow;
	}

	/**
	 * Set the selected column
	 * @param int Column
	 */
	private void setSelectedColumn(int aColumn) {
		m_intSelectedCol = aColumn;
	}

	/**
	 * Gets the selcted column
	 * @return int Column
	 */
	private int getSelectedColumn() {
		return m_intSelectedCol;
	}

	/**
	 * Sets the selected year
	 * @param String Selected year.
	 */
	private void setSelectedYear(String aYear) {
		m_strSelectedYear = aYear;
	}

	/**
	 * Gets the Selected year
	 * @return String Selected year.
	 */
	private String getSelectedYear() {
		return m_strSelectedYear;
	}

	/**
	 * Sets the selected month
	 * @param String Selected month.
	 */
	private void setSelectedMonth(String aMonth) {
		m_strSelectedMonth = aMonth;
	}

	/**
	 * Gets the Selected month
	 * @return String Selected month.
	 */
	private String getSelectedMonth() {
		return m_strSelectedMonth;
	}

	/**
	 * Gets The Selected hour
	 * @return String Selected Hour
	 */
	private String getSelectedHour() {
		return m_strSelectedHour;
	}
	
	
	public void setSelectedHour(String m_strSelectedHour) {
		this.m_strSelectedHour = m_strSelectedHour;
	}
	
	/**
	 * Gets the Selected minute
	 * @param String Selected Minute
	 */
	private String getSelectedMinute(){
		return m_strSelectedMinute;
	}
	
	public void setSelectedMinute(String m_strSelectedMinute) {
		this.m_strSelectedMinute = m_strSelectedMinute;
	}
	
	/**
	 * Gets the Selected seconds
	 * @return String Selected Seconds
	 */
	private String getSelectedSeconds(){
		return m_strSelectedSeconds;
	}
	
	public void setSelectedSeconds(String m_strSelectedSeconds) {
		this.m_strSelectedSeconds = m_strSelectedSeconds;
	}
	
	/**
	 * Sets the selected date
	 * @param String Selected date.
	 */
	private void setSelectedDate(String aDate) {
		m_strSelectedDate = aDate;
	}

	/**
	 * Gets the Selected date
	 * @return String Selected date.
	 */
	private String getSelectedDate() {
		return m_strSelectedDate;
	}

	/**
	 * Gets event handler object
	 * @return EventHandler object
	 */
	private EventHandler getEventHandler() {
		return (
			(m_eventHandler == null) ? m_eventHandler =
				new EventHandler() : m_eventHandler);
	}

	/**
	 * Set the caller class event handler object
	 * @param Object Event handler object
	 */
	private void setCallerClassEventHandler(Object aEventHandler) {
		if (aEventHandler == null)
			aEventHandler = getEventHandler();
		m_CallerClassEventHandler = aEventHandler;		
	}
	/**
	 * Gets the caller class event handler
	 * @return Object caller class event handler 
	 */
	private Object getCallerClassEventHandler() {
		return m_CallerClassEventHandler;
	}

	/**
	 * Returns the selected date.
	 * @return String Selected date
	 */
	/*
	 * Start Ver_20031114
	 */
	//protected String getCalendarDate() {
	public String getCalendarDate() {
		/*
		 * End Ver_20031114
		 */
		String strYear = getSelectedYear();
		String strMonth = getSelectedMonth();
		String strDate = getSelectedDate();
		String hour = getSelectedHour();
		String minute = getSelectedMinute();
		String seconds = getSelectedSeconds();
		
		String strReturnDate = "";
		String formatString= "yyyy-MM-dd HH:mm:ss";

		// if no date has been selected then return blank.

		if (strDate == null) {
			strReturnDate = "";
		} else if (strDate.equals("")) {
			strReturnDate = "";
		} else {
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.set(java.util.Calendar.YEAR, Integer.parseInt(strYear));
			cal.set(java.util.Calendar.MONTH, getMonths().indexOf(strMonth));
			cal.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(strDate));
			if(hour !=null)
				cal.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			if(minute != null)
				cal.set(java.util.Calendar.MINUTE, Integer.parseInt(minute));
			if(seconds != null)
				cal.set(java.util.Calendar.SECOND, Integer.parseInt(seconds));
			strReturnDate = new SimpleDateFormat(formatString).format(cal.getTime());
		}

		return strReturnDate;

	}

	/**
	 * Get the Close button component
	 * @return Component Close button
	 */
	/*
	 * Start Ver_20031114
	 */
	// protected Component getCalendarCloseBtn() {
	public Component getCalendarCloseBtn() {
		/*
		 * End Ver_20031114
		 */

		return getJbtnClose();
	}

	/**
	 * Get the Tabel component
	 * @return Component table
	 */
	/*
	 * Start Ver_20031114
	 */
	//protected Component getCalendarComponent() {
	public Component getCalendarComponent() {
		/*
		 * End Ver_20031114
		 */

		return getJtblDate();
	}
	
	public javax.swing.JTextField getCalendarTimeStampJText() {	
		return getJTextFldTime();
	}
		
	/**
	 * This method initializes jtextField for time entry
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JTextField getJTextFldTime() {
		if (jtfldTime == null) {
			jtfldTime = new JTextField(8);			
			jtfldTime.setSize(75, 25);			
			jtfldTime.setLocation(168, 165);			
			jtfldTime.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			java.util.Calendar cal = java.util.Calendar.getInstance();				
			SimpleDateFormat sdf = new SimpleDateFormat("H.mm.ss");
			String time = sdf.format(cal.getTime());			
			jtfldTime.setText(time);					
		}
		return jtfldTime;
	}
	//_____________________________________________________________________________________________________________
	// End of miscellaneous and helper  methods. 
	//_____________________________________________________________________________________________________________
	
	/*
	 * Method used to validate 24 hour time format
	 */
	public boolean isValidTime(){
		boolean isvalid = false;
		try{
		if(isCalTimeStamp){
			pattern = Pattern.compile(TIME24HOURS_PATTERN);
			matcher = pattern.matcher(getCalendarTimeStampJText().getText());
			isvalid= matcher.matches();	
			if(!isvalid){
				JOptionPane.showMessageDialog(this, "Please enter a valid time");
			}
		}
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return isvalid;
	}
	
} //  @jve:visual-info  decl-index=0 visual-constraint="0,0"
