package com.honda.galc.data;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.util.KeyValue;
/**
 * 
 * <h3>YMTOCI</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> YMTOCI description </p>
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
 * Jun.15, 2022
 *
 */
public enum YMTOCI {
	Y(0,1),
	M(1,4),
	T(4,7),
	O(7,10),
	C(10,20),
	I(20,22);
	
	private int start;
	private int end;
	private static final String SPEC_STRING="YMTOCI";
	YMTOCI(int start, int end){
		this.start = start;
		this.end = end;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	
	public static KeyValue<Integer, Integer> getSpecPositions(String spec){
		//check to make sure the input configuration is valid
		if(!SPEC_STRING.contains(spec))
			throw new TaskException("Invalid Check Spec from Configuration:" + spec);
		
		KeyValue<Integer, Integer> positions = null;
		try {
			if (spec.length() == 1) {
				YMTOCI oneTag = YMTOCI.valueOf(spec);
				positions = new KeyValue<Integer, Integer>(oneTag.getStart(), oneTag.getEnd());
			} else {
				YMTOCI first = YMTOCI.valueOf(spec.substring(0, 1));
				YMTOCI last = YMTOCI.valueOf(spec.substring(spec.length() -1));
				positions = new KeyValue<Integer, Integer>(first.getStart(), last.getEnd());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return positions;
	}
	
	public static String getString(String specCode,String spec) {
		KeyValue<Integer, Integer> specPositions = YMTOCI.getSpecPositions(spec);
		/**
		 * spec code is trimed from Frame getter. so adjust for trimmed.
		 */
		int endposition = specPositions.getValue();
		if(spec.endsWith("I") && specCode.length() < endposition)
			endposition = specCode.length();
		
		return specCode.substring(specPositions.getKey(), endposition);
	}
}
