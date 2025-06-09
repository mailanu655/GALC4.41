package com.honda.galc.device.simulator.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <h3>Class description</h3>
 * This class is used to create a menubar for GalcWindow. It reads 
 * menu definitions from XML files (for now).
 * <h4>Description</h4>
 * By default, the menu definition is in applicationmenu.xml file
 * in current directory. The file applicationmenu.dtd is used
 * to validate the XML file. Users have options to change the 
 * location of the files. <br><br>
 * All the elements defined in the XML file must meet the 
 * requirements defined in the dtd file. If the XML file cannot 
 * be parsed correctly based on the definitions, a default menu
 * will be created and the XML file will be ignored. <br><br>
 * During program execution, when a menu item is clicked, a call
 * to the corresponding method defined by the center panel (usually
 * a subclass of GalcMainPanel) of GalcWindow. The method must be
 * public and take no parameter. If such a method cannot be found, 
 * it will do nothing. 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Jun 22, 2007</TD>
 * <TD>1.0</TD>
 * <TD>20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcMenuBar extends JMenuBar implements ActionListener  {
	private static final long serialVersionUID = 1L;
	public final static String SUBMENU = "submenu";
	public final static String MENU_ITEM = "menuitem";
	public final static String MENU_NAME = "name";
	public final static String MENU_ACTION = "action";
	public final static String TOOLTIP = "tooltip";
	public final static String MNEMONIC = "mnemonic";
	public final static String MENU_FILE_URI = "Menu File URI";
	
	private GalcController controller;
	private JMenu currentMenu;
	private String uri;
	
	GalcMenuBar(GalcController aController) {
		super();
		setController(aController);
		initialize();
	}
	
	private void initialize() {
		setName("Menu Bar");
		String uri = getController().getProperties().getProperty(MENU_FILE_URI);
		setUri(uri);
		if(uri == null) {
			getController().showWarningDialog("Menu XML file cannot be found. System default menu will be used.");
			createDefaultMenu();
		} else {
			createApplicationMenu();
		}
		
	}

/**
 * Reset the menu bar for current application using menu definitions 
 * provided by the XML file.
 * @param uri The URI of the XML file which contains menu definitions.
 */	
	public void resetMenuBar(String uri) {
		setUri(uri);
		removeAll();
		createApplicationMenu();
	}

/**
 * Create a default menu if the menu definition XML file cannot be found or
 * the system cannot parse the XML file correctly.
 *
 */	
	private void createDefaultMenu() {
		JMenu aMenu = new JMenu();
		aMenu.setName("System");
		aMenu.setText("System");
		
		JMenuItem aMenuItem = null;
		aMenuItem = new JMenuItem();
		aMenuItem.setName("ExitMenuItem");
		aMenuItem.setText("Exit");
		aMenuItem.setActionCommand("exitSystem");
		aMenuItem.addActionListener(this);
		
		aMenu.add(aMenuItem);
		add(aMenu);
	}
	
/**
 * Create menu bar.
 *
 */
	public void createApplicationMenu() {
		Document doc;
		try {
			doc = controller.getXmlUtils().loadDocument(getUri());
			processNode(doc);
		} catch (ParserConfigurationException e) {
			getController().showErrorDialog(e.getMessage() + "\nSystem default menu will be used.");
		} catch (SAXException e) {
			getController().showErrorDialog(e.getMessage() + "\nSystem default menu will be used.");
		} catch (IOException e) {
			getController().showErrorDialog(e.getMessage() + "\nSystem default menu will be used.");
		} finally {
			if(this.getMenuCount() == 0) createDefaultMenu();
		}
	}

/**
 * Process aNode. 
 * If it contains a submenu, create a submenu and process its nodes..
 * If it is a menu item, create a menu item.
 * @param aNode The node to be processed.
 */	
	private void processNode(Node aNode) {
		String nodeName = aNode.getNodeName();

		switch (aNode.getNodeType()) {
			case Node.DOCUMENT_NODE:
				break;
			case Node.ELEMENT_NODE: 
				if(nodeName.equals(SUBMENU)) createSubMenu(aNode);
				else if(nodeName.equals(MENU_ITEM)) createMenuItem(aNode);
				break;
			case Node.TEXT_NODE:
				break;
			case Node.COMMENT_NODE:
				break;
			case Node.ATTRIBUTE_NODE:
				break;
			default:
				break;
		}
		
		NodeList aList = aNode.getChildNodes();
		for(int i=0; i<aList.getLength(); i++) {
			processNode(aList.item(i));
		}
	}

/**
 * Create submenu from aNode. 
 * @param aNode The node from which the submenu will be created.
 */	
	private void createSubMenu(Node aNode) {
		if(!aNode.hasAttributes()) return;
		NamedNodeMap aMap = aNode.getAttributes();
		Node attributeNode;
		Field aField=null;

		String menuName = aMap.getNamedItem(MENU_NAME).getNodeValue();
		JMenu aMenu = new JMenu();
		aMenu.setName(menuName);
		aMenu.setText(menuName);

		if((attributeNode = aMap.getNamedItem(TOOLTIP)) != null) 
			aMenu.setToolTipText(attributeNode.getNodeValue());

		if((attributeNode = aMap.getNamedItem(MNEMONIC)) != null) {
			aField = findField(attributeNode.getNodeValue());
			if(aField != null) {
				try {
					aMenu.setMnemonic(aField.getInt(KeyEvent.class));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		setCurrentMenu(aMenu);
		add(aMenu);
	}

/**
 * Create menu item from aNode.
 * @param aNode The node from which the menu item will be created.
 */	
	public void createMenuItem(Node aNode) {
		if(!aNode.hasAttributes() || getCurrentMenu() == null) return;
		NamedNodeMap aMap = aNode.getAttributes();
		Node attributeNode;
		Field aField=null;

		String menuItemName = aMap.getNamedItem(MENU_NAME).getNodeValue();
		JMenuItem aMenuItem = new JMenuItem();
		aMenuItem.setName(menuItemName);
		aMenuItem.setText(menuItemName);
		aMenuItem.setActionCommand(aMap.getNamedItem(MENU_ACTION).getNodeValue());
		aMenuItem.addActionListener(this);
		if((attributeNode = aMap.getNamedItem(TOOLTIP)) != null) 
			aMenuItem.setToolTipText(attributeNode.getNodeValue());

		if((attributeNode = aMap.getNamedItem(MNEMONIC)) != null) {
			aField = findField(attributeNode.getNodeValue());
			if(aField != null) {
				try {
					aMenuItem.setMnemonic(aField.getInt(KeyEvent.class));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}

		getCurrentMenu().add(aMenuItem);
	}

/**
 * Trigger an action when a menu item is clicked. 
 */
    public void actionPerformed(ActionEvent e) {
    	GalcPanel aPanel = getWindow().getCenterPanel();
    	Method aMethod = findMethod(aPanel.getClass(), e.getActionCommand());
    	if(aMethod == null) return;
    	
		try {
			aMethod.invoke(aPanel, (Object[]) null);
		} catch (IllegalArgumentException e1) {
			displayError("Illegal argument exception.");
		} catch (IllegalAccessException e1) {
			displayError("Illegal access exception.");
		} catch (InvocationTargetException e1) {
			displayError("Invocation target exception.");
		}
    }

/**
 * Find the method object named methodName defined by class aClass.
 * @param aClass The class object.
 * @param methodName The name of the method.
 * @return The method object if it was found or null if such a method does not exist.
 */
    private Method findMethod(Class aClass, String methodName) {
    	try {
			return aClass.getMethod(methodName, (Class[]) null);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}
    }
/**
 * Find the field named aName defined by class KeyEvent.
 * @param aName The name of the field.
 * @return The field object if it was found or null if nothing was found.
 */    
    private Field findField(String aName) {
		try {
			return KeyEvent.class.getField(aName);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
    }
    
    private GalcWindow getWindow() {
		return (GalcWindow) getTopLevelAncestor();
	}

	public void displayError(String message) {
		getWindow().displayError(message);
	}
	
	public JMenu getCurrentMenu() {
		return currentMenu;
	}

	public void setCurrentMenu(JMenu currentMenu) {
		this.currentMenu = currentMenu;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public GalcController getController() {
		return controller;
	}

	public void setController(GalcController controller) {
		this.controller = controller;
	}
	
}
