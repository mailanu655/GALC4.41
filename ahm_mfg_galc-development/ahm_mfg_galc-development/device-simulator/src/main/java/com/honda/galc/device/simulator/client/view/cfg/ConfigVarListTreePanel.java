package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * <h3>ConfigVarListTreePanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public class ConfigVarListTreePanel extends JPanel
implements TreeSelectionListener{
	private static final long serialVersionUID = -3780453383286609355L;
	private JTree tree;
    JScrollPane treeView;
    DeviceSimulatorConfigView detailView;

    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = true;

    public ConfigVarListTreePanel() {
        super(new GridLayout(1,0));
        /* Windows style look and feel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			;
		}  
		*/
		   
		//Create the nodes.
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Device Simulator Configuration");
        createNodes(top);

        //Create a tree that allows one selection at a time.
        tree = new ConfigVarListTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
        
       
        if (playWithLineStyle) {
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //Create the scroll pane and add the tree to it. 
        treeView = new JScrollPane(tree);
        
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        detailView.setVariablePanel(nodeInfo.toString());
    }

    private void createNodes(DefaultMutableTreeNode top) {
    	DefaultMutableTreeNode general = null;
        DefaultMutableTreeNode tmpVar = null;
        DefaultMutableTreeNode simServer = null;

        general = new DefaultMutableTreeNode("General");
        top.add(general);
        
        tmpVar = new DefaultMutableTreeNode("Simulator UI");
        general.add(tmpVar);

        tmpVar = new DefaultMutableTreeNode("Simulator Application Server Client");
        top.add(tmpVar);
        
        simServer = new DefaultMutableTreeNode("Simulator Server");
        top.add(simServer);

    }
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new ConfigVarListTreePanel());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	/**
	 * @return the treeView
	 */
	public JScrollPane getTreeView() {
		return treeView;
	}

	/**
	 * @param treeView the treeView to set
	 */
	public void setTreeView(JScrollPane treeView) {
		this.treeView = treeView;
	}

	/**
	 * @return the detailView
	 */
	public DeviceSimulatorConfigView getDetailView() {
		return detailView;
	}

	/**
	 * @param detailView the detailView to set
	 */
	public void setDetailView(DeviceSimulatorConfigView detailView) {
		this.detailView = detailView;
	}
}
