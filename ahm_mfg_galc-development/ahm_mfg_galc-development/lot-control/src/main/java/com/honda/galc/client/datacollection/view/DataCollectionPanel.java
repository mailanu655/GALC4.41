package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
/**
 * 
 * <h3>DataCollectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionPanel is the base panel for default Data Collection Panel </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jan 23, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 23, 2012
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class DataCollectionPanel extends DataCollectionPanelBase {
	private static final long serialVersionUID = 1L;

	private DataCollectionImageManager imageManager;
	private DefaultViewProperty defaultProperty;
	protected ArrayList<JLabel> torqueResultLabelList = new ArrayList<JLabel>();

	public DataCollectionPanel(DefaultViewProperty property, int winWidth,
			int winHeight) {
		super(property, winWidth, winHeight);

		this.defaultProperty = property;

		init();
	}

	@Override
	protected void init() {

		AnnotationProcessor.process(this);
		
		initImageManager();
		super.init();

	}


	@Override
	protected void initPanel() {
		super.initPanel();

		for (int i = 0; i < maxNumOfPart; i++) {
			add(getTorqueResultLabel(i), null);
		}		
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		initTorqueResultLabelList();

	}

	protected void initTorqueResultLabelList() {

		JLabel torqueResultLabel;
		for (int i = 0; i < maxNumOfPart; i++) {
			Rectangle bounds = getPartSerialNumber(i).getBounds();
			torqueResultLabel = new JLabel();
			
			if(!isPartLotControl()){
				torqueResultLabel.setBounds(bounds.x + bounds.width, bounds.y, viewProperty.getStatusLabelWidth(), bounds.height);
				torqueResultLabel.setIcon(imageManager.getImageBlank());
				torqueResultLabel.setFont(new java.awt.Font("dialog", 0, 18));
			} else {
				torqueResultLabel.setBounds(bounds.x + bounds.width + gap, bounds.y, viewProperty.getStatusLabelWidth(), bounds.height);
				torqueResultLabel.setFont(new java.awt.Font("dialog", 0, 36));
				torqueResultLabel.setOpaque(true);
				torqueResultLabel.setBackground(Color.green);
			}
			
			torqueResultLabelList.add(torqueResultLabel);

		}	
		
	}

	public JLabel getTorqueResultLabel(int ic) {

		JLabel label = null;
		try {
			label = torqueResultLabelList.get(ic);

		} catch (Throwable e) {
			handleException(e);
		}
		return label;
	}

	private void initImageManager() {
		imageManager = new DataCollectionImageManager(defaultProperty);
	}

	public Icon getImgOk() {
		return imageManager.getImageOk();
	}
	
	public Icon getImgNg() {
		return imageManager.getImageNg();
	}

	@Override
	protected int getTorquePositionY(int row, int height, int gap) {
		int y = viewProperty.getTorqueStartPositionY();
		return y + row * (height + gap);

	}

	public void setProductSpecBackGroudColor(String colorName) {
		getTextFieldExpPidOrProdSpec().setBackground(Color.white);
	}

}
