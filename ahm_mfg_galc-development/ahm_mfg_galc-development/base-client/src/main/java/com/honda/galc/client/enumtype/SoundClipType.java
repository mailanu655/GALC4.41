package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Jun 11, 2013
 */
public enum SoundClipType implements IdEnum<SoundClipType> {
	
	OK				(1, "OK"),
	NG				(2, "NG"),
	CONNECTED		(3, "CONNECTED"),
	DISCONNECTED	(4, "DISCONNECTED"),
	NO_ACTION		(5, "NO_ACTION"),
	CHANGED			(6, "CHANGED"),
	NG_REPEAT		(7, "NG_REPEAT"),
	WARN			(8, "WARN"),
	DESTINATION		(9, "DESTINATION"),
	OK_PRODUCT_ID	(10, "OK_PRODUCT_ID"),
	ALARM           (11, "ALARM"),
	SCRAP           (12, "SCRAP");
	
	private int id = 1;
	private String name = "";
	
	private SoundClipType(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
