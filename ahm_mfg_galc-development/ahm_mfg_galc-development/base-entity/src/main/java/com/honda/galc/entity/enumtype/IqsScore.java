package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.IdEnum;
/**
 * <h3>Class description</h3>
 * List of IQS scores
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Oct. 16, 2019</TD>
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public enum IqsScore implements IdEnum<IqsScore> {
	
	IQS70(0, 7.0),
	IQS65(1, 6.5),
	IQS60(2, 6.0),
	IQS55(3, 5.5),
	IQS50(4, 5.0),
	IQS40(5, 4.0),
	IQS80(6, 8.0),
	IQS90(7, 9.0),
	IQS100(8, 10.0);
	
	private final int id;
	private double score;
      
    
    private IqsScore(int intValue, double score) {
		this.id = intValue;
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public double getScore() {
	    	return score;
	}

	@Override
	public String toString() {
		return String.valueOf(score);
	}
}
