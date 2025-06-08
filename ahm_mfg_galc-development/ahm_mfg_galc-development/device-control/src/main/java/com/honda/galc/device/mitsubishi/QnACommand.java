package com.honda.galc.device.mitsubishi;

/**
 * 
 * <h3>QnACommand</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnACommand description </p>
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
public enum QnACommand {
	BatchRead(0x0401), 
	BatchWrite(0x1401);
	private int code;

	QnACommand(int code){
		this.code = code;
	}

	//Getter & Setters
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
