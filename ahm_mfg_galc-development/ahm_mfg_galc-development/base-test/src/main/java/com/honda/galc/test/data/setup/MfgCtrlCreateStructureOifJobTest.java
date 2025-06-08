package com.honda.galc.test.data.setup;

import org.junit.Test;

import com.honda.galc.oif.task.MfgCtrlCreateStructureOIFJob;
import com.honda.galc.test.dao.AbstractBaseTest;

public class MfgCtrlCreateStructureOifJobTest extends AbstractBaseTest {
	
	/* to trigger and Test Manufacturing control OIF Job.
	 * */
	
	@Test
	public void testCreateStructure(){
		
		MfgCtrlCreateStructureOIFJob createStructure = new MfgCtrlCreateStructureOIFJob("OIF_MFG_CTRL_CREATE_STRUCTURE");
		createStructure.execute(null);
		System.out.println("Oif job to call structure create is finishes..");
		
	}
	
	public MfgCtrlCreateStructureOifJobTest() {
		// TODO Auto-generated constructor stub
	}

}
