package com.honda.galc.client.datacollection.view.info;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.SoundClip;
import com.honda.galc.client.common.component.LabelJComboBoxButtonPanel;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.util.ResourceUtil;

/**
 * 
 * <h3>AudioConfigPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AudioConfigPanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Apr 8, 2010
 *
 */
public class AudioConfigPanel extends JPanel 
implements ActionListener, InformationPanel{
    private static final String RESOURCE_SOUNDS = "resource/com/honda/galc/client/sounds";
	private static final long serialVersionUID = 1L;
	private static final String SOUNDLIST_FILE = "soundlist.txt";
    private JRadioButton radioButtonEnableSound = null;
    private JPanel audioEnablePanel = null;
    private JPanel audioParampanel = null;
    private List<String> soundResourceList;
	private ClientContext context;
	private LabelJComboBoxButtonPanel okSoundPanel;
	private LabelJComboBoxButtonPanel ngSoundPanel;
	private LabelJComboBoxButtonPanel changedSoundPanel;
	private LabelJComboBoxButtonPanel connectedSoundPanel;
	private LabelJComboBoxButtonPanel disConnectedSoundPanel;
	private LabelJComboBoxButtonPanel soundNoActionPanel;
	private LabelJComboBoxButtonPanel destinationSoundPanel;
	private TerminalPropertyBean property;
 
    /**
     * This method initializes 
     * 
     */
    public AudioConfigPanel(ClientContext context) {
    	super();
    	this.context = context;
    	this.property = context.getProperty();
    	setName("Lot Control Client Audio Configuration");
		setLayout(new GridBagLayout());
    	initialize();
    }

    public AudioConfigPanel() {
    	setName("Lot Control Client Audio Configuration");
		setLayout(new GridBagLayout());
    	initialize();
	}

	/**
     * This method initializes this
     * 
     */
    private void initialize() {
        try {
			createComponents();
			initConnections();
			initScreen();
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to init Audio configuration panel");
		}
    }

	private void initConnections() {
		getRadioButtonEnableSound().addActionListener(this);
		getOkSoundPanel().getButton().addActionListener(this);
		getNgSoundPanel().getButton().addActionListener(this);
		getChangedSoundPanel().getButton().addActionListener(this);
		getConnectedSoundPanel().getButton().addActionListener(this);
		getDisConnectedSoundPanel().getButton().addActionListener(this);
		getSoundNoActionPanel().getButton().addActionListener(this);
		getDestinationSoundPanel().getButton().addActionListener(this);
	}

	private void createComponents() {
		soundResourceList = getSoundResouceList();
		GridBagConstraints c = getConstraint(0, 0, 1);
		c.anchor = GridBagConstraints.PAGE_START;
		add(getAudioEnablePanel(), c);
		add(getAudioParamPanel(), getConstraint(0, 1, 1));

	}

    private void initScreen() {
		getRadioButtonEnableSound().setSelected(isAudioEnabled());
		enableOptions(getRadioButtonEnableSound().isSelected());
	}

	private boolean isAudioEnabled() {
		return LotControlAudioManager.isExist();
	}

    protected void actionOnEnableSound() {
    	boolean b = getRadioButtonEnableSound().isSelected();

    	enableOptions(b);

    }

	private void enableOptions(boolean b) {
		getOkSoundPanel().getComboBox().setEnabled(b);
		getOkSoundPanel().getButton().setEnabled(b);
    	getNgSoundPanel().getComboBox().setEnabled(b);
    	getNgSoundPanel().getButton().setEnabled(b);
    	getChangedSoundPanel().getComboBox().setEnabled(b);
    	getChangedSoundPanel().getButton().setEnabled(b);
    	getDestinationSoundPanel().getComboBox().setEnabled(b);
    	getDestinationSoundPanel().getButton().setEnabled(b);
	}

	private List<String> getSoundResouceList() {
    	if(soundResourceList == null){
    		soundResourceList = new ArrayList<String>();
    		try {
    			String[] listPackedResources = ResourceUtil.listResourcesFromFile(RESOURCE_SOUNDS + "/" + SOUNDLIST_FILE);
				for(int i = 0; i < listPackedResources.length; i++){
					soundResourceList.add(RESOURCE_SOUNDS + "/" + listPackedResources[i].trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.getLogger().warn(e, "Failed to load resource " + RESOURCE_SOUNDS);
			}
    	}
    	return soundResourceList;
    }

	private JPanel getAudioParamPanel() {
    	if(audioParampanel == null){
    		audioParampanel = new JPanel();
       		TitledBorder border = new TitledBorder("Properties");
    		audioParampanel.setBorder(border);
    		
    		audioParampanel.setLayout(new GridBagLayout());
    		GridBagConstraints constraint = getConstraint(0,0,1);
    		constraint.anchor = GridBagConstraints.PAGE_START;;
    		audioParampanel.add(getOkSoundPanel(), constraint);
    		audioParampanel.add(getNgSoundPanel(), getConstraint(0, 1, 1));
    		audioParampanel.add(getChangedSoundPanel(), getConstraint(0, 2, 1));
    		audioParampanel.add(getSoundNoActionPanel(), getConstraint(0, 3, 1));
    		audioParampanel.add(getConnectedSoundPanel(), getConstraint(0,4,1));
    		audioParampanel.add(getDisConnectedSoundPanel(), getConstraint(0, 5, 1));
    		audioParampanel.add(getDestinationSoundPanel(), getConstraint(0, 6, 1));
    		
    	}
    	return audioParampanel;
    }


	private LabelJComboBoxButtonPanel getChangedSoundPanel() {
		if(changedSoundPanel == null){
			changedSoundPanel = new LabelJComboBoxButtonPanel("Changed Sound:",  getComboBoxModel(property.getSoundChanged()) , "Test");
		}
		return changedSoundPanel;
	}
	
	private LabelJComboBoxButtonPanel getDestinationSoundPanel() {
		if(destinationSoundPanel == null){
			destinationSoundPanel = new LabelJComboBoxButtonPanel("Destination Sound:",  getComboBoxModel(property.getSoundDestination()) , "Test");
		}
		return destinationSoundPanel;
	}

	private LabelJComboBoxButtonPanel getNgSoundPanel() {
		if(ngSoundPanel == null){
			ngSoundPanel = new LabelJComboBoxButtonPanel("NG Sound:",  getComboBoxModel(property.getSoundNg()) , "Test");
		}
		return ngSoundPanel;
	}

	private LabelJComboBoxButtonPanel getOkSoundPanel() {
		if(okSoundPanel == null){
			okSoundPanel = new LabelJComboBoxButtonPanel("OK Sound:", getComboBoxModel(property.getSoundOk()) , "Test");
		}
		return okSoundPanel;
	}
	
	private LabelJComboBoxButtonPanel getConnectedSoundPanel() {
		if(connectedSoundPanel == null){
			connectedSoundPanel = new LabelJComboBoxButtonPanel("Connected Sound:",  getComboBoxModel(property.getSoundConnected()) , "Test");
			connectedSoundPanel.getComboBox().setEnabled(false);
		}
		return connectedSoundPanel;
	}
	
	private LabelJComboBoxButtonPanel getDisConnectedSoundPanel() {
		if(disConnectedSoundPanel == null){
			disConnectedSoundPanel = new LabelJComboBoxButtonPanel("Disconnected Sound:",  getComboBoxModel(property.getSoundDisConnected()) , "Test");
			disConnectedSoundPanel.getComboBox().setEnabled(false);
		}
		return disConnectedSoundPanel;
	}

	private DefaultComboBoxModel getComboBoxModel(String currentItem) {
		return new DefaultComboBoxModel(getSoundResourceListForComboBox(currentItem));
	}

	private JPanel getAudioEnablePanel() {
		if(audioEnablePanel == null)
		{
			audioEnablePanel = new JPanel();
			audioEnablePanel.add(getRadioButtonEnableSound());
		}
		return audioEnablePanel;
	}



	private Object[] getSoundResourceListForComboBox(String currentSound) {
		
		currentSound = currentSound == null ? "" : currentSound.trim();
		List<String> list  = new ArrayList<String>();
		list.add(currentSound);
		for(String path : getSoundResouceList()){
			if(!path.equals(currentSound) && isValidPath(path))
				list.add(path);
		}
		
		return list.toArray();
	}

    private boolean isValidPath(String path) {
		return path.charAt(path.lastIndexOf("/") +1) != '.';
	}

    public LabelJComboBoxButtonPanel getSoundNoActionPanel() {
    	if(soundNoActionPanel == null){
    		soundNoActionPanel = new LabelJComboBoxButtonPanel("No Action Sound:",  getComboBoxModel(property.getSoundNoAction()), "Test");
		}
		return soundNoActionPanel;
	}

	public void setSoundNoActionPanel(LabelJComboBoxButtonPanel soundNoActionPanel) {
		this.soundNoActionPanel = soundNoActionPanel;
	}

	/**
     * This method initializes radioButtonEnableSound	
     * 	
     * @return javax.swing.JRadioButton	
     */
    public JRadioButton getRadioButtonEnableSound() {
        if (radioButtonEnableSound == null) {
            radioButtonEnableSound = new JRadioButton();
            radioButtonEnableSound.setText("Enable Audio");
        }
        return radioButtonEnableSound;
    }

    
    protected GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth) {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		c.weightx = 0.3;
		
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;		
		return c;
	}
    
    public static File[] loadPackageFiles(String dirName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(dirName);
        File f = new File(url.getFile());
        return f.listFiles();
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("CheckBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new AudioConfigPanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    
    public void saveAudioConfig() {

    	if(getRadioButtonEnableSound().isSelected()){

    		try {
    			context.getDbManager().deleteProperty(createProperty(LotControlAudioManager.findAudioManagerPropertyKey(),
    					LotControlAudioManager.class.getName()));
    		} catch (Exception e) {
    			//Ok, the key may not exist;
    		}
    		List<ComponentProperty> properties = getAudioProperties();

    		if (properties.size() > 0 ) {
    			context.getDbManager().saveProperties(properties);
    		}

    	} else {
    		context.getDbManager().saveProperty(createProperty(LotControlAudioManager.findAudioManagerPropertyKey(), ""));
    	}
    	
    	MessageDialog.showInfo(this, "Audio configurations were saved. Please restart client to pick up configuration changes.", "Information");
		
	}
    
    private List<ComponentProperty> getAudioProperties() {
		List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
		String okSound = (String)getOkSoundPanel().getComboBox().getSelectedItem();
		String ngSound = (String)getNgSoundPanel().getComboBox().getSelectedItem();
		String changedSound = (String)getChangedSoundPanel().getComboBox().getSelectedItem();
		String connected = (String) getConnectedSoundPanel().getComboBox().getSelectedItem();
		String disconnected = (String) getDisConnectedSoundPanel().getComboBox().getSelectedItem();
		String noActionSound = (String) getSoundNoActionPanel().getComboBox().getSelectedItem();
		String destinationSound = (String)getDestinationSoundPanel().getComboBox().getSelectedItem();
		
		if(!okSound.equals(context.getProperty().getSoundOk()))
			properties.add(createProperty("SOUND_OK", okSound));
		if(!ngSound.equals(context.getProperty().getSoundNg()))
			properties.add(createProperty("SOUND_NG", ngSound));
		if(!changedSound.equals(context.getProperty().getSoundChanged()))
			properties.add(createProperty("SOUND_CHANGED",changedSound));
		if(!connected.equals(context.getProperty().getSoundConnected()))
			properties.add(createProperty("CONNECTED",connected));
		if(!disconnected.equals(context.getProperty().getSoundDisConnected()))
			properties.add(createProperty("DIS_CONNECTED",disconnected));
		if(!noActionSound.equals(context.getProperty().getSoundNoAction()))
			properties.add(createProperty("SOUND_NO_ACTION",disconnected));
		if(!destinationSound.equals(context.getProperty().getSoundDestination()))
			properties.add(createProperty("DESTINATION_CHANGED",destinationSound));
		
		
		return properties;
	}
    
    private ComponentProperty createProperty(String key, String value) {
		ComponentProperty property = new ComponentProperty(context.getProcessPointId(), key, value);
		property.setDescription("updated by client");
		property.setChangeUserId(context.getProcessPointId());//use process point Id for now.
		property.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return property;
	}

    public void actionPerformed(ActionEvent e) {
    	if(getRadioButtonEnableSound() == e.getSource()) {
    		actionOnEnableSound();
    	} else if(getOkSoundPanel().getButton() == e.getSource()){
    		testSound((String)getOkSoundPanel().getComboBox().getSelectedItem());
    	} else if(getNgSoundPanel().getButton() == e.getSource()){
    		testSound((String)getNgSoundPanel().getComboBox().getSelectedItem());
    	} else if(getChangedSoundPanel().getButton() == e.getSource()){
    		testSound((String)getChangedSoundPanel().getComboBox().getSelectedItem());
    	} else if(getConnectedSoundPanel().getButton() == e.getSource()){
    		testSound((String)getConnectedSoundPanel().getComboBox().getSelectedItem());
    	} else if(getDisConnectedSoundPanel().getButton() == e.getSource()){
    		testSound((String)getDisConnectedSoundPanel().getComboBox().getSelectedItem());
    	} else if(getSoundNoActionPanel().getButton() == e.getSource()){
    		testSound((String)getSoundNoActionPanel().getComboBox().getSelectedItem());
    	} else if(getDestinationSoundPanel().getButton() == e.getSource()){
    		testSound((String)getDestinationSoundPanel().getComboBox().getSelectedItem());
    	}
    }

	private void testSound(String sound) {
		if(StringUtils.isEmpty(sound)) return;
		SoundClip clip = new SoundClip(Thread.currentThread().getContextClassLoader().getResource(sound));
        clip.playSound();
	}

	public void refresh() {
		initScreen();
		
	}


}  



