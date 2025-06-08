package com.honda.galc.client.datacollection.view.info;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.common.data.MeasurementSpecTableModel;
import com.honda.galc.client.common.data.PartSpecTableModel;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.LotControlRuleTableModel;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;

/**
 * 
 * <h3>LotControlRulePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRulePanel description </p>
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
 * May 28, 2010
 *
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlRulePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	TablePane lotControlRulesPanel;
	TablePane partSpecPanel;
	TablePane measurementSpecPanel;
	
	public LotControlRulePanel() {
		super();
		
		initialize();
	}

	private void initialize() {
		 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		 add(getLotControlRulesPanel());
		 add(Box.createVerticalGlue());
		 add(getPartSpecPanel());
		 add(getMeasurementSpecPanel());
		 
	}


	public TablePane getLotControlRulesPanel() {
		if(lotControlRulesPanel == null){
			lotControlRulesPanel = new TablePane("Lot Control Rules:");
			ListSelectionModel rowSm = lotControlRulesPanel.getTable().getSelectionModel();
            rowSm.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) { 
                	if (e.getValueIsAdjusting()) return;
                	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                	if (!lsm.isSelectionEmpty()) {
                        int selectedIndex = lsm.getMinSelectionIndex();
                        LotControlRuleTableModel model = (LotControlRuleTableModel)lotControlRulesPanel.getTable().getModel();
                        LotControlRule rule = model.getItem(selectedIndex);
                        showPartSpecs(rule);
                    }
                }
            });
		}
		return lotControlRulesPanel;
	}

	protected void showPartSpecs(LotControlRule  rule) {
		new PartSpecTableModel(rule.getParts(), getPartSpecPanel().getTable());
		new MeasurementSpecTableModel(null, getMeasurementSpecPanel().getTable());
		
	}

	public TablePane getMeasurementSpecPanel() {
		if(measurementSpecPanel == null){
			measurementSpecPanel = new TablePane("Measurement Spec:");
			new MeasurementSpecTableModel(null, measurementSpecPanel.getTable());
		}
		return measurementSpecPanel;
	}
	
	
	
	public TablePane getPartSpecPanel() {
		if(partSpecPanel == null){
			partSpecPanel = new TablePane("Part Spec:");
			ListSelectionModel rowSm = partSpecPanel.getTable().getSelectionModel();
            rowSm.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) { 
                	if (e.getValueIsAdjusting()) return;
                	ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                	if (!lsm.isSelectionEmpty()) {
                        int selectedIndex = lsm.getMinSelectionIndex();
                        PartSpecTableModel model = (PartSpecTableModel)partSpecPanel.getTable().getModel();
                        PartSpec partSpec = model.getItem(selectedIndex);
                        showMeasurementSpecs(partSpec);
                    }
                }
            });
		}
		return partSpecPanel;
	}

	protected void showMeasurementSpecs(PartSpec partSpec) {
		new MeasurementSpecTableModel(partSpec.getMeasurementSpecs(), getMeasurementSpecPanel().getTable());
		
	}

	public void initScreen(){
		new LotControlRuleTableModel(getLotControlRulesPanel().getTable(),DataCollectionController.getInstance().getState().getLotControlRules(), false);
		new PartSpecTableModel(null, getPartSpecPanel().getTable());
		new MeasurementSpecTableModel(null, getMeasurementSpecPanel().getTable());
		
		
	}
	
	public void refresh(){
		initScreen();
	}

}
