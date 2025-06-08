package com.honda.galc.device.mitsubishi;
/**
 * 
 * <h3>QnASubCommand</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnASubCommand description </p>
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
 * @author Paul Chou
 * Nov 5, 2010
 *
 */

public enum QnASubCommand {
	bitUnit(0x0001),
	wordUnit(0x0000);
	private Integer code;
	
	QnASubCommand(int code){
		this.code = code;
	}

	//Getter & Setters
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	};
	
	public static String getCommandByCode(int code){
		for(QnACommand c : QnACommand.values())
		{
			if(c.getCode() == code)
				return c.toString();
		}
		return null;
	}

}
