package com.honda.galc.entity.enumtype;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;
/**
 * 
 * <h3>QiWriteUpDepartmentColor Class description</h3>
 * <p>
 * QiWriteUpDepartmentColor contains the color values for write up departments
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 */
public enum QiWriteUpDepartmentColor implements IdEnum<QiWriteUpDepartmentColor>  {
	RED(0,"RED"),
	GREEN(1,"GREEN"),
	BLUE(2,"BLUE"),
	BLACK(3,"BLACK"),
	BROWN(4,"BROWN"),
	DARKGREEN(5,"DARKGREEN"),
	DARKORANGE(6,"DARKORANGE"),
	DARKBLUE(7,"DARKBLUE"),
	DARKGREY(8,"DARKGREY"),
	GOLD(9,"GOLD"),
	GRAY(10,"GRAY"),
	LIGHTBLUE(11,"LIGHTBLUE"),
	LIGHTGREEN(12,"LIGHTGREEN"),
	ORANGE(13,"ORANGE"),
	PURPLE(14,"PURPLE"),
	TEAL(15,"TEAL"),
	YELLOW(16,"YELLOW"),
	PINK(17,"PINK");
	
	int id;
	String name;
	
	private QiWriteUpDepartmentColor(int id,String name) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return StringUtils.trimToEmpty(name);
	}
	public static QiWriteUpDepartmentColor getType(int id) {
		return EnumUtil.getType(QiWriteUpDepartmentColor.class, id);
	}
}
