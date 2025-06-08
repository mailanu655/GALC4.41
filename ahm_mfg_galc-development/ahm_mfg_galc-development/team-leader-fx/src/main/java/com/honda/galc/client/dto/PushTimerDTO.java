package com.honda.galc.client.dto;

import org.apache.commons.lang.StringUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PushTimerDTO {
	private final StringProperty date = new SimpleStringProperty();
	private final StringProperty hh = new SimpleStringProperty();
	private final StringProperty mm = new SimpleStringProperty();
	private final StringProperty time = new SimpleStringProperty();
	private final StringProperty delay = new SimpleStringProperty();
	
	public PushTimerDTO() {
		super();
	}

	public PushTimerDTO(String date, String hh, String mm, String delay) {
		super();
		this.date.set(date);
		this.hh.set(hh);
		this.mm.set(mm);
		this.time.set(getTime());
		this.delay.set(delay);
	}

	public String getDate() {
		return date.get();
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public String getHh() {
		return hh.get();
	}

	public void setHh(String hh) {
		this.hh.set(hh);
	}

	public String getMm() {
		return mm.get();
	}

	public void setMm(String mm) {
		this.mm.set(mm);
	}
	
	public String getTime() {
		return hh.get()+":"+mm.get();
	}
	
	public void setTime(String time) {
		this.time.set(time);
	}

	public String getDelay() {
		return delay.get();
	}

	public void setDelay(String delay) {
		this.delay.set(delay);
	}
	
	public StringProperty dateProperty() {
		return date;
	}
	public StringProperty hhProperty() {
		return hh;
	}
	public StringProperty mmProperty() {
		return mm;
	}
	public StringProperty timeProperty() {
		return time;
	}
	public StringProperty delayProperty() {
		return delay;
	}

	@Override
	public String toString() {
		return "PushTimerDetail [date=" + date.get() + ", hh=" + hh.get() + ", mm=" + mm.get()
				+ ", delay=" + delay.get() + "]";
	}
	
	public String toPropertyValue() {
		if(this.isValid())
			return date.get() + " " + time.get() + " " +  delay.get();
		else
			return "";
	}
	
	public boolean isValid() {
		if(StringUtils.isBlank(date.get()) ||
				StringUtils.isBlank(time.get()) ||
				StringUtils.isBlank(delay.get())) {
			return false;
		}
		else {
			return true;
		}
	}

}