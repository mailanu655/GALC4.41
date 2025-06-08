package com.honda.galc.test.common;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.KeyPressInfo;
import org.testng.annotations.AfterClass;

import com.honda.galc.client.qics.view.fragments.ProductNumberPanel;


public class QicsGuiTestCase extends GuiTestCase{
    private String FRAME_NAME = "QicsFrame";
    
    protected void startApp(String batchRun,String clientName) {
        
        this.startApp(batchRun, clientName, FRAME_NAME);
    }
    
    protected void inputProductId(String productId) {
        
        mainFrame.panel(new GenericTypeMatcher<JPanel>(JPanel.class){
            @Override protected boolean isMatching(JPanel panel){
                return ProductNumberPanel.class.isInstance(panel);
            }
        }).textBox().enterText(productId).pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_ENTER));
        
    }
    
    @AfterClass
    public void tearDown(){
        
        closeRequestRouterView();
        delay(1000);
        closeApp();
        cleanUp();
    }
}
