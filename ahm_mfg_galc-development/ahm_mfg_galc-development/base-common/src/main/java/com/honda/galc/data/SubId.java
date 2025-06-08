package com.honda.galc.data;
/**
 * 
 * 
 * <h3>SubId Class description</h3>
 * <p> SubId description </p>
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
 * Aug 14, 2014
 *
 *
 */
public enum SubId {
	LEFT("L", "Left"),
	RIGHT("R","Right"),
	TOP("T","Top"),
	BOTTOM("B","Bottom"),
	FRONT("F","Front"),
	REAR("R","Rear"),
	FRONT_LEFT("FL","Front Left"),
	FRONT_RIGHT("FR","Front Right"),
	REAR_LEFT("RL","Rear Left"),
	REAR_RIGHT("RR","Rear Right");

	private String id;
	private String name;

	private SubId(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}
	
   public static SubId getType(String name) {
      for(SubId subId: SubId.values()) {
    	  if(subId.toString().equalsIgnoreCase(name)) return subId;
      }
      return null;
    }

}
