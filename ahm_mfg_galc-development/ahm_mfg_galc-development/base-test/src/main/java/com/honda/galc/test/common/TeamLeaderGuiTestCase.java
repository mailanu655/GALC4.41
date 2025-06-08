package com.honda.galc.test.common;

import org.fest.swing.fixture.FrameFixture;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import static org.fest.swing.finder.WindowFinder.findFrame;

public abstract class TeamLeaderGuiTestCase extends GuiTestCase {

    
    // Application menu path
    private String [] menuPath;
    protected String clientFrameName = "ScreenSuperClass";
    protected FrameFixture clientFrame;
    
    
    
    public void startTeamLeaderApp(String batchRun, String... menuPath) {
        
        this.startApp(batchRun, "TeamLeader", "GALCMain");
        this.menuPath = menuPath;
        
    }
    
    @AfterClass
    public void tearDown(){
    	
    	closeClient();
        closeRequestRouterView();
        
        closeApp();
        
        cleanUp();
    }
    
    @Test (dependsOnMethods = {"login"})
    public void launchClient() {
        
        String [] menuPath1 = new String[menuPath.length + 1];
        menuPath1[0] = "ApplicationMenu";
        
        for (int i =0; i<menuPath.length; i++)
            menuPath1[i+1] = menuPath[i];
        
        mainFrame.menuItemWithPath(menuPath1).click();
 //       this.printComponents();
        clientFrame = findFrame(clientFrameName).withTimeout(5000).using(robot);
        
    }
    
    public void setClientFrameName(String clientFrameName) {
    	this.clientFrameName = clientFrameName;
    }
    
    public void closeClient() {
        
        clientFrame.menuItemWithPath("System","Close").click();
    }
    
}
