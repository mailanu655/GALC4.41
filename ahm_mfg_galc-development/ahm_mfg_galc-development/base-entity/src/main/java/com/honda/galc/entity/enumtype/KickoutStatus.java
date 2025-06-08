/**
 * Enum used to denote the status for a kickout.
 *
 * @author Bradley Brown
 * @version 1.0
 * @since 2.43
 */
package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum KickoutStatus implements IdEnum<KickoutStatus> {
	
	OUTSTANDING(1, "Outstanding"),
	RELEASED(2, "Released");
	
	private final int id;
	private final String name;
	
	private KickoutStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public boolean isOutstanding() {
		return this.equals(OUTSTANDING);
	}
	
	public boolean isReleased() {
		return this.equals(RELEASED);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static KickoutStatus getType(int id) {
		return EnumUtil.getType(KickoutStatus.class, id);
	}
}
