/**
 * 
 */
package com.honda.galc.dto.qi;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author VCC44349
 *
 */
public class QiDefectResultDtoTest {
	private QiDefectResultDto myDto = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		myDto = new QiDefectResultDto();
		myDto.setApplicationId("TVQ3QC1PQ0211");
		myDto.setDefectResultId(108082557);
		myDto.setEntryScreen("PAINT/DEFORMS");
		myDto.setProductId("5J8TC1H35KL007991");
		myDto.setInspectionPartName("DOOR JAMB R RR");
		myDto.setDefectTypeName("CLEAN UP");
		myDto.setDefectTypeName2("COMPOUND");
		myDto.setInspectionPartLocationName("LWR");
		myDto.setThemeName("EXTERIOR");
		myDto.setImageName("19M RDX EXTERIOR");
		myDto.setRepairAreaName("S-TUP");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.honda.galc.dto.qi.QiDefectResultDto#getCombinedDefectTypeName()}.
	 */
	@Test
	public void testGetCombinedDefectTypeName() {
		assert(myDto.getCombinedDefectTypeName().equals("CLEAN UP - COMPOUND"));
	}

	/**
	 * Test method for {@link com.honda.galc.dto.qi.QiDefectResultDto#combineTypeNames(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCombineTypeNames() {
		assert(QiDefectResultDto.combineTypeNames("APPEARANCE NO GOOD", "SEALER").equals("APPEARANCE NO GOOD - SEALER"));
	}

	/**
	 * Test method
	 */
	@Test
	public void testDefectName() {
		String s1 = QiDefectResultDto.combineTypeNames("CLEAN UP", "COMPOUND");
		String s2 = myDto.getCombinedDefectTypeName();
		assert(s1.equals(s2));
	}

}
