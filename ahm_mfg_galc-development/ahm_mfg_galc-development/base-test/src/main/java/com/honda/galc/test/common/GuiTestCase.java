package com.honda.galc.test.common;


import static org.fest.swing.core.matcher.JButtonMatcher.withText;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;

import java.awt.Container;
import java.awt.Window;

import org.fest.swing.annotation.RunsInCurrentThread;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.BasicComponentPrinter;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.core.matcher.DialogMatcher;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.hierarchy.ComponentHierarchy;
import org.fest.swing.lock.ScreenLock;
import org.fest.swing.monitor.WindowMonitor;
import org.testng.annotations.Test;

import com.honda.galc.client.ClientMain;
import com.honda.galc.test.db.SystemPropertyManager;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>GuiTestCase</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 9, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
public abstract class GuiTestCase extends BaseTestCase{
    
    protected Robot robot ;

    protected FrameFixture loginFrame;
    protected FrameFixture mainFrame;
    protected FrameFixture requestRouterFrame = null;
    
    protected String frameName;
    protected boolean batchRunFlag = false;
    
    public void startApp(String batchRun,String clientName,String frameName) {
        
        batchRunFlag = !("not-found".equalsIgnoreCase(batchRun) || "false".equalsIgnoreCase(batchRun));
        
        this.frameName = frameName;
        
        this.processManager.setClientName(clientName);
        
        logger.info("launching GUI Client : " + clientName);
        forbidSystemExitCall(batchRunFlag);
        
        setupRobot();
        
        startApp(clientName);
        
    }
    
    protected void setupRobot() {
    	
    	 robot = BasicRobot.robotWithNewAwtHierarchy();
 // 	 robot = BasicRobot.robotWithCurrentAwtHierarchy();
         
    }
    
    /**
     * start GALC client
     * @param clientName - client name
     */
    
    public void startApp(String clientName) {
        
        String[] args = new String[]{SystemPropertyManager.getProperty("ClientDir"),
                                    SystemPropertyManager.getProperty("Dispatcher"),
                                     clientName,"2"};
        
        // start GALC client
        application(ClientMain.class).withArgs(args).start();
        
    }
  
    /**
     * Try to forbid the System Exit call
     * 
     * @param flag - true  --- forbid system exit call
     *               false --- allow system exit call
     */
    
    public void forbidSystemExitCall(boolean flag) {
        
        if(flag) SystemExitManager.forbidSystemExitCall();
        
    }
    
    @Test
    public void login() {
      
     // now the interesting part, we need to wait till the main window is shown.
      loginFrame = findFrame("LoginDialog2").withTimeout(10000).using(robot);

      loginFrame.textBox("LengthFieldBeanUserID").enterText(SystemPropertyManager.getProperty("UserName"));
      loginFrame.textBox("JPasswordField").enterText(SystemPropertyManager.getProperty("Password"));
      loginFrame.button("JButtonOK").click();
      
      //    now the interesting part, we need to wait till the main window is shown.
      mainFrame = findFrame(frameName).withTimeout(10000).using(robot);
       
    }
    
    public DialogFixture findDialog(String name) {
        
        return findDialog(name,5000);
        
    }
    
    public DialogFixture findDialog(String name, int delay) {
        
        return WindowFinder.findDialog(name).withTimeout(delay).using(robot);
        
    }
    
    public DialogFixture findDialogWithTitle(String name) {
        
        return findDialogWithTitle(name,5000);
        
    }
    
    public DialogFixture findDialogWithTitle(String name, int delay) {
        
        return WindowFinder.findDialog(DialogMatcher.withTitle(name)).withTimeout(delay).using(robot);
        
    }
    
    /**
     * print current Awt Hierarchy of components
     * 
     */
    
    public void printComponents() {

        BasicComponentPrinter.printerWithCurrentAwtHierarchy().printComponents(System.out);

    }
    
    
    public void closeRequestRouterView() {
        if (requestRouterFrame == null) return;
        requestRouterFrame.show();
        requestRouterFrame.button(withText("Stop")).click();
        requestRouterFrame.close();
        requestRouterFrame.component().dispose();
 //       printComponents();
    }
    
    @RunsInEDT
	protected void cleanUp() {
		execute(new GuiTask() {
		      protected void executeInEDT() {
		    	  WindowMonitor.instance().rootWindows();
		        for (Container c :WindowMonitor.instance().rootWindows()) 
//		        	if (c instanceof Window && !(c.getName().equals("Mock Frame"))) dispose(robot.hierarchy(), (Window)c);
		        	if (c instanceof Window 
		        		&& !(c.getName().equals("Mock Frame"))
		        		&& !(c.getClass().getSimpleName().equalsIgnoreCase("SharedOwnerFrame"))) 
		        		
		        		dispose(robot.hierarchy(), (Window)c);
		    	
		      }
		    });
		ScreenLock.instance().release(robot);
	}
    
    @RunsInCurrentThread
    private void dispose(final ComponentHierarchy hierarchy, Window w) {
      hierarchy.dispose(w);
      w.setVisible(false);
      System.out.println("Disposing window " + w.getName() + "  dddd " + w.getClass().getSimpleName());
      w.dispose();
    }
    
    
    protected void closeApp() {
        
        mainFrame.menuItemWithPath("System","Exit").click();
        
    }
}
