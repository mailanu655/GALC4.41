package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>KnuckleSide</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnuckleSide description </p>
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
 * Nov 26, 2010
 *
 */
public class KnuckleSide implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean leftSide;
	private boolean rightSide;
	
	public KnuckleSide() {
		super();
	}
	
	
	public boolean isLeftSide() {
		return leftSide;
	}
	
	public void setLeftSide(boolean leftSide) {
		this.leftSide = leftSide;
	}
	
	public boolean isRightSide() {
		return rightSide;
	}
	
	public void setRightSide(boolean rightSide) {
		this.rightSide = rightSide;
	}

	public String getSide() {
		if(leftSide && !rightSide) return "L";
		if(rightSide && !leftSide) return "R";
		if(!leftSide && !rightSide) return null;
		if(leftSide && rightSide) return "E";
		return null;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KnuckleSide other = (KnuckleSide) obj;
		if (leftSide != other.leftSide)
			return false;
		if (rightSide != other.rightSide)
			return false;
		return true;
	}

	public KnuckleSide clone()  {
		KnuckleSide newObj = new KnuckleSide();
		newObj.setLeftSide(this.leftSide);
		newObj.setRightSide(this.rightSide);
		
		return newObj;
	}


	
	
	
}
