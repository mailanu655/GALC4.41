package com.honda.galc.test.common;

import com.honda.galc.test.db.DeviceManager;
import com.honda.galc.test.db.ProcessManager;
import com.honda.galc.test.db.ProductionManager;

public abstract class BaseTestCase {

    protected DeviceManager deviceManager = DeviceManager.getInstance();
    protected ProcessManager processManager = ProcessManager.getInstance();
    protected ProductionManager productionManager = ProductionManager.getInstance();
    protected java.util.logging.Logger logger = LoggerFactory.createLogger(getClass().getSimpleName());
 
    
    /**
    +    * Creates delay 
    +    * @param miliSeconds
    +    * @throws InterruptedException
    +    */
    public void delay (int miliSeconds)  {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
