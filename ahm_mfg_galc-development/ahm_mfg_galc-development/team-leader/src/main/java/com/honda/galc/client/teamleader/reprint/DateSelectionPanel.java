package com.honda.galc.client.teamleader.reprint;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.honda.galc.client.ui.component.LabeledNumberSpinner;

/**
 * 
 * <h3>DateSelectionPanel Class description</h3>
 * <p> DateSelectionPanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 20, 2011
 *
 *
 */
public class DateSelectionPanel extends JPanel implements ChangeListener {
	
	
	private static final long serialVersionUID = 1L;
	private LabeledNumberSpinner yearSpinner;
	private LabeledNumberSpinner monthSpinner;
	private LabeledNumberSpinner daySpinner;
	
	public DateSelectionPanel() {
		
		initComponents();
		addListeners();
	}
	
	private void initComponents() {
		
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		Calendar calendar = Calendar.getInstance();
		
		int year = calendar.get(Calendar.YEAR);
		yearSpinner = createNumberSpinner("Year",year, year - 5, year + 5);
		yearSpinner.setBounds(new Rectangle(5, 24, 53, 26));
		monthSpinner = createNumberSpinner("Month",calendar.get(Calendar.MONTH) + 1, 1, 12);
		monthSpinner.setBounds(new Rectangle(75, 24, 37, 26));
		daySpinner = createNumberSpinner("Day",calendar.get(Calendar.DATE), 1, calendar.getActualMaximum(Calendar.DATE));
		daySpinner.setBounds(new Rectangle(130, 24, 37, 26));
		add(yearSpinner);
		add(monthSpinner);
		add(daySpinner);
	}
	
	private void addListeners() {
		monthSpinner.getComponent().addChangeListener(this);
	}
	
	private LabeledNumberSpinner createNumberSpinner(String label,int value, int min, int max) {
		
		LabeledNumberSpinner comp = new LabeledNumberSpinner(label,false);
		comp.setRange(value, min, max);
		return comp;
		
	}

	public void stateChanged(ChangeEvent e) {
		
		Calendar calendar = new GregorianCalendar(yearSpinner.getValue(), monthSpinner.getValue()-1, 1);
		int day = daySpinner.getValue();
		int maxDay = calendar.getActualMaximum(Calendar.DATE);
		int actualDay = day > maxDay ? maxDay : day;
		
		daySpinner.setRange(actualDay, 1, maxDay);
	}
	
	public Date getDate() {
		
		return new Date(new GregorianCalendar(yearSpinner.getValue(), monthSpinner.getValue()-1, daySpinner.getValue()).getTimeInMillis());
		
	}
	
	public void setFont(Font font) {
		
		super.setFont(font);
		if(yearSpinner!=null) yearSpinner.setFont(font);
		if(monthSpinner!=null) monthSpinner.setFont(font);
		if(daySpinner!=null) daySpinner.setFont(font);
		
	}
	
}
