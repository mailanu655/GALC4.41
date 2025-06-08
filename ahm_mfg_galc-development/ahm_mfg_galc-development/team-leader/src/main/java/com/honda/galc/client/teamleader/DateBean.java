package com.honda.galc.client.teamleader;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.event.EventListenerList;

import com.honda.galc.client.ui.component.ItemChange;
import com.honda.galc.client.ui.component.ItemChangeListener;
import com.honda.galc.client.ui.component.LengthFieldBean;

public class DateBean extends JPanel implements ItemChange {
	private static final long serialVersionUID = 1L;
	static final int LOW_YEAR   = 2000;
	static final int HIGH_YEAR  = 2099;
	static final int LOW_MONTH  = 1;
	static final int HIGH_MONTH = 12;
	static final int LOW_DAY    = 1;
	static final int HIGH_DAY   = 31;
	static final int VISIBLEAMOUNT = 0;
	EventListenerList listenrList = new EventListenerList();

	Date currentDate = new Date();
	SimpleDateFormat yearFormatter = new SimpleDateFormat ("yyyy");
	SimpleDateFormat monthFormatter = new SimpleDateFormat ("M");
	SimpleDateFormat dayFormatter = new SimpleDateFormat ("d");

	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	public LengthFieldBean yearField = new LengthFieldBean();
	public LengthFieldBean monthField = new LengthFieldBean();
	public LengthFieldBean dayField = new LengthFieldBean();
	public JScrollBar scrollBarYear = new JScrollBar(JScrollBar.VERTICAL,
			Integer.parseInt(yearFormatter.format(currentDate)),
			VISIBLEAMOUNT,
			LOW_YEAR,
			HIGH_YEAR);
	public JScrollBar scrollBarMonth = new JScrollBar(JScrollBar.VERTICAL,
			Integer.parseInt(monthFormatter.format(currentDate)),
			VISIBLEAMOUNT,
			LOW_MONTH,
			HIGH_MONTH);
	public JScrollBar scrollBarDay = new JScrollBar(JScrollBar.VERTICAL,
			Integer.parseInt(dayFormatter.format(currentDate)),
			VISIBLEAMOUNT,
			LOW_DAY,
			HIGH_DAY);


	/**
	 * ?????????????????????? DateBean ???????
	 */
	 public DateBean() {
		super();
		try {
			initialize();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	 }
	 /**
	  * FlowLayout ????????????????????? DateBean ???????<br>
	  * isDoubleBuffered ? true ????JPanel ???????????????
	  * @param isDoubleBuffered boolean ??true ?????????????????????????????????????????????
	  */
	 public DateBean(boolean isDoubleBuffered) {
		 super(isDoubleBuffered);
		 try {
			 initialize();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
	 /**
	  * ????? ItemChangeListener ??????ActionEvent???FocusEvent ?<br>
	  * ?? DateBean ?????????
	  * @param l ItemChangeListener????
	  * @see ItemChangeListener
	  */
	 public void addItemChangeListener(ItemChangeListener l) {
		 listenerList.add(ItemChangeListener.class, l);
	 }
	 /**
	  * ?????????????????
	  * @return ????????false????????? true  
	  */
	 private boolean checkDate() {
		 try{
			 // ?????????
			 if (LOW_YEAR > Integer.parseInt(yearField.getText()) ||
					 HIGH_YEAR < Integer.parseInt(yearField.getText())) {
				 setFieldBackgroundred();
				 return false;
			 }
			 // ?????????
			 if (LOW_MONTH > Integer.parseInt(monthField.getText()) ||
					 HIGH_MONTH < Integer.parseInt(monthField.getText())) {
				 setFieldBackgroundred();
				 return false;
			 }
			 // ????????????????
			 Calendar calendar = new GregorianCalendar();
			 // ??????????1??????????????????
			 calendar.set(Integer.parseInt(yearField.getText()),
					 Integer.parseInt(monthField.getText()) - 1,
					 LOW_DAY);
			 // ?????????
			 if (LOW_DAY > Integer.parseInt(dayField.getText()) ||
					 calendar.getActualMaximum(Calendar.DATE) < Integer.parseInt(dayField.getText())) {
				 setFieldBackgroundred();
				 return false;
			 }
			 // ??????????????????????????(???)????
			 // ???????????????????
			 // Must save to prevent overwriting of text from "previous" value
			 String dayFieldText = dayField.getText();
			 scrollBarDay.setMaximum(calendar.getActualMaximum(Calendar.DATE));
			 dayField.setText(dayFieldText);
		 }
		 catch(NumberFormatException e){
			 setFieldBackgroundred();
			 return false;
		 }
		 setFieldBackgroundwhite();
		 return true;
	 }
	 /**
	  * ?????????????????????????????????????<br>
	  * ?????????????????????????????????????????<br>
	  * ????????????????????
	  * @param e ActionEvent???FocusEvent???
	  * @see EventListenerList
	  * @see ActionEvent
	  * @see FocusEvent
	  */
	 protected void fireItemChange(AWTEvent e) {
		 Object[] listeners = listenerList.getListenerList();
		 for (int i = listeners.length-2; i>=0; i-=2) {
			 if (listeners[i]==ItemChangeListener.class) {
				 ((ItemChangeListener)listeners[i+1]).itemChanged(e);
			 }
		 }
	 }
	 /**
	  * ?????????????????
	  * @return YYYYMMDD???<br>????????????null
	  */
	 public String getDate() {
		 DecimalFormat fourFormatter = new DecimalFormat ("###0");
		 DecimalFormat twoFormatter = new DecimalFormat ("00");
		 if (checkDate()){
			 return fourFormatter.format(Integer.parseInt(yearField.getText().trim())) + 
			 twoFormatter.format(Integer.parseInt(monthField.getText().trim())) + 
			 twoFormatter.format(Integer.parseInt(dayField.getText().trim()));
		 }else{
			 return null;
		 }
	 }

	 /**
	  * Return the date in the format of input param
	  * @return 
	  */
	 public String getDate(String format) {
		 SimpleDateFormat sdfOutput = new SimpleDateFormat(format);
		 SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMdd");
		 String textDate = getDate();
		 Date date = null;
		 try {
			 date = sdfInput.parse(textDate);
		 } catch (ParseException e) {
			 e.printStackTrace();
			 return null;
		 }
		 return sdfOutput.format(date);
	 }

	 private void initialize() throws Exception {
		 this.setLayout(null);
		 jLabel1.setText("Year");
		 jLabel1.setForeground(Color.black);
		 jLabel1.setBounds(new Rectangle(17, 3, 41, 21));
		 jLabel2.setText("Month");
		 jLabel2.setForeground(Color.black);
		 jLabel2.setBounds(new Rectangle(76, 3, 35, 21));
		 jLabel3.setText("Day");
		 jLabel3.setForeground(Color.black);
		 jLabel3.setBounds(new Rectangle(138, 3, 35, 21));

		 yearField.setBounds(new Rectangle(5, 24, 53, 26));
		 yearField.setMaximumLength(4);
		 yearField.setText(yearFormatter.format(currentDate));

		 monthField.setBounds(new Rectangle(75, 24, 37, 26));
		 monthField.setMaximumLength(2);
		 monthField.setText(monthFormatter.format(currentDate));

		 dayField.setBounds(new Rectangle(130, 24, 37, 26));
		 dayField.setMaximumLength(2);
		 dayField.setText(dayFormatter.format(currentDate));

		 // change 2001/04/02 T.Ohkawa add
		 //*	setUnitIncrement
		 //* setBlockIncrement
		 scrollBarYear.setBounds(new Rectangle(57, 24, 15, 26));
		 scrollBarYear.setUnitIncrement(-1);
		 scrollBarYear.setBlockIncrement(-1);
		 scrollBarMonth.setBounds(new Rectangle(111, 24, 15, 26));
		 scrollBarMonth.setUnitIncrement(-1);
		 scrollBarMonth.setBlockIncrement(-1);
		 scrollBarDay.setBounds(new Rectangle(166, 24, 15, 26));
		 scrollBarDay.setUnitIncrement(-1);
		 scrollBarDay.setBlockIncrement(-1);

		 // ?????????????????
		 //  change 2001/04/02 T.Ohkawa add
		 //*	fireItemChange
		 scrollBarYear.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
			 public void adjustmentValueChanged(AdjustmentEvent e) {
				 setValuetoYear();
				 fireItemChange(e);
			 }
		 });
		 // ?????????????????
		 scrollBarMonth.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
			 public void adjustmentValueChanged(AdjustmentEvent e) {
				 setValuetoMonth();
				 fireItemChange(e);
			 }
		 });
		 // ?????????????????
		 scrollBarDay.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
			 public void adjustmentValueChanged(AdjustmentEvent e) {
				 setValuetoDay();
				 fireItemChange(e);
			 }
		 });

		 // ???????ActionEvent???FocusEvent????
		 yearField.addItemChangeListener(new ItemChangeListener() {
			 public void itemChanged(AWTEvent e) {
				 // AWTEvent?ActionEvent?????????
				 if ((e instanceof ActionEvent)) {
					 setYeartoValue();
				 }else if((e instanceof FocusEvent)){
					 setYeartoValue(); 
				 }
				 // AWTEvent???????
				 fireItemChange(e);
			 }
		 });
		 // ???????ActionEvent???FocusEvent????
		 monthField.addItemChangeListener(new ItemChangeListener() {
			 public void itemChanged(AWTEvent e) {
				 // AWTEvent?ActionEvent?????????
				 if ((e instanceof ActionEvent)) {
					 setMonthtoValue();
				 }else if((e instanceof FocusEvent)){
					 setMonthtoValue(); 
				 }
				 // AWTEvent???????
				 fireItemChange(e);
			 }
		 });
		 // ???????ActionEvent???FocusEvent????
		 dayField.addItemChangeListener(new ItemChangeListener() {
			 public void itemChanged(AWTEvent e) {
				 // AWTEvent?ActionEvent?????????
				 if ((e instanceof ActionEvent)) {
					 setDaytoValue();
				 }else if((e instanceof FocusEvent)){
					 setDaytoValue(); 
				 }
				 // AWTEvent???????
				 fireItemChange(e);
			 }
		 });
		 this.add(jLabel3, null);
		 this.add(dayField);
		 this.add(scrollBarDay);
		 this.add(jLabel1, null);
		 this.add(yearField);
		 this.add(scrollBarYear);
		 this.add(jLabel2, null);
		 this.add(monthField);
		 this.add(scrollBarMonth);
	 }              
	 /**
	  * ????? ItemChangeListener ??????ActionEvent???FocusEvent ?<br>
	  * ?? DateBean ???????????????????
	  * @param l ItemChangeListener????
	  * @see ItemChangeListener
	  */
	 public void removeItemChangeListener(ItemChangeListener l) {
		 listenerList.remove(ItemChangeListener.class, l);
	 }
	 /**
	  * ??????????
	  * @param strDate ??????(??? yyyyMMdd ??)
	  * @exception IndexOutOfBoundsException - ???????????????????
	  * @exception NumberFormatException - ???????????????????? 
	  */
	 public void setDate(String strDate) {
		 try{

			 int year  = Integer.parseInt(strDate.substring(0,4));
			 int month = Integer.parseInt(strDate.substring(4,6)); 
			 int day   = Integer.parseInt(strDate.substring(6,8));

			 // ????????????????
			 Calendar calendar = new GregorianCalendar(year,month-1,day);
			 year  = calendar.get(Calendar.YEAR);
			 month = calendar.get(Calendar.MONTH)+1;
			 day   = calendar.get(Calendar.DATE);

			 //????????
			 if ((LOW_YEAR <= year) && (year <= HIGH_YEAR)){
				 yearField.setText(Integer.toString(year));	
				 monthField.setText(Integer.toString(month));	
				 dayField.setText(Integer.toString(day));
				 setYeartoValue();
				 setMonthtoValue();
				 setDaytoValue();
			 }
		 }
		 catch(NumberFormatException ne){
		 }			
		 catch(IndexOutOfBoundsException e){
		 }
	 }  /**
	  * ??????
	  */
	 /**
	  * dayField???scrollBarDay?????????
	  */
	 private void setDaytoValue() {
		 try{
			 // ????????????????????????
			 scrollBarDay.setValue(Integer.parseInt(dayField.getText()));
			 // ?????????????????????????
			 // (??????????2???Enter???????????????????????? Java Bug?)
			 dayField.setText(Integer.toString(scrollBarDay.getValue()));
			 // ???????????
			 checkDate();
		 }
		 catch(NumberFormatException e){
			 // ?????????????????????
			 setFieldBackgroundred();
		 }
	 }
	 /**
	  * ?????????????????????
	  */
	 public void setFieldBackgroundred(){
		 yearField.setBackground(Color.red);
		 monthField.setBackground(Color.red);
		 dayField.setBackground(Color.red);
	 }  
	 /**
	  * ?????????????????????
	  */
	 public void setFieldBackgroundwhite(){
		 yearField.setBackground(Color.white);
		 monthField.setBackground(Color.white);
		 dayField.setBackground(Color.white);
	 }  
	 /**
	  * monthField???scrollBarMonth?????????
	  */
	 private void setMonthtoValue() {
		 try{
			 // ????????????????????????
			 scrollBarMonth.setValue(Integer.parseInt(monthField.getText()));
			 // ?????????????????????????
			 // (??????????2???Enter???????????????????????? Java Bug?)
			 monthField.setText(Integer.toString(scrollBarMonth.getValue()));
			 // ???????????
			 checkDate();
		 }
		 catch(NumberFormatException e){
			 // ?????????????????????
			 setFieldBackgroundred();
		 }
	 }
	 /**
	  * scrollBarDay???dayField?????????
	  */
	 private void setValuetoDay() {
		 dayField.setText(Integer.toString(scrollBarDay.getValue()));
		 // ???????????
		 checkDate();
	 }
	 /**
	  * scrollBarMonth???monthField?????????
	  */
	 private void setValuetoMonth() {
		 monthField.setText(Integer.toString(scrollBarMonth.getValue()));
		 // ???????????
		 checkDate();
	 }
	 /**
	  * scrollBarYear???yearField?????????
	  */
	 private void setValuetoYear() {
		 yearField.setText(Integer.toString(scrollBarYear.getValue()));
		 // ???????????
		 checkDate();
	 }
	 /**
	  * yearField???scrollBarYear?????????
	  */
	 private void setYeartoValue() {
		 try{
			 // ????????????????????????
			 scrollBarYear.setValue(Integer.parseInt(yearField.getText()));
			 // ?????????????????????????
			 // (??????????2???Enter???????????????????????? Java Bug?)
			 yearField.setText(Integer.toString(scrollBarYear.getValue()));
			 // ???????????
			 checkDate();
		 }
		 catch(NumberFormatException e){
			 // ?????????????????????
			 setFieldBackgroundred();
		 }
	 }

	 public void setEnabled(boolean status) {
		 yearField.setEnabled(status);
		 monthField.setEnabled(status);
		 dayField.setEnabled(status);
		 scrollBarYear.setEnabled(status);
		 scrollBarMonth.setEnabled(status);
		 scrollBarDay.setEnabled(status);
	 }
}
