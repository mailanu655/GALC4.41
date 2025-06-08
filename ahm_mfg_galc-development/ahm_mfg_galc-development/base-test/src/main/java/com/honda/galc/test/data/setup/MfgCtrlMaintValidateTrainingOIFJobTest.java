package com.honda.galc.test.data.setup;

import org.junit.Test;

import com.honda.galc.oif.task.MfgCtrlValidateTrainingOIFJob;
import com.honda.galc.test.dao.AbstractBaseTest;

public class MfgCtrlMaintValidateTrainingOIFJobTest extends AbstractBaseTest {
	
	public MfgCtrlMaintValidateTrainingOIFJobTest() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Test
	public void oifTest() {
		
		MfgCtrlValidateTrainingOIFJob job = new MfgCtrlValidateTrainingOIFJob("OIF_MFG_CTRL_VALIDATE_TRAINING_JOB");
		Object[] args = null;
		job.execute(args);
		System.out.println("Executed");
	}

}
