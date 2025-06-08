package com.honda.galc.test.common;

import org.testng.annotations.Test;

public class TerminateBatchGuiTest {
    
    @Test
    public void allowSystemExit(){
    	
        
   }
    
    public void delay (int miliSeconds)  {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
