package com.honda.galc.client.teamleader.shipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dto.ShippingQuorumDetailDto;

/**
 * 
 * 
 * <h3>ShippingEngineQueryPanel Class description</h3>
 * <p> ShippingEngineQueryPanel description </p>
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
 * @author Jeffray Huang<br>
 * Apr 2, 2015
 *
 *
 */
public class ShippingEngineQueryPanel extends TabbedPanel{

	private static final long serialVersionUID = 1L;
	
	private LabeledTextField einField = new LabeledTextField("Engine Number");
	private JButton searchButton = new JButton("Search");
	
	private ObjectTablePane<ShippingQuorumDetailDto> quorumDetailTablePane;
	
	ShippingEngineQueryPanel(MainWindow mainWindow){
		super("",0,mainWindow);
		initComponents();
		einField.getComponent().addActionListener(this);
		searchButton.addActionListener(this);
	}

	@Override
	public void onTabSelected() {
		einField.getComponent().requestFocusInWindow();
	}
	
	private void initComponents() {
		setLayout(new MigLayout("insets 200", "[grow,fill]"));
		einField.setFont(new Font("sansserif", 1, 20));
		searchButton.setFont(new Font("sansserif", 1, 20));
		einField.getComponent().requestFocusInWindow();
		add(einField);
		add(searchButton,"wrap");
		add(quorumDetailTablePane = createQuorumDetailTablePane(),"span");
	}
	
	private ObjectTablePane<ShippingQuorumDetailDto> createQuorumDetailTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer No","trailerNumber")
		.put("KD LOT","kdLot").put("YMTO","ymto").put("Trailer Status","trailerInfoStatus");
	
		ObjectTablePane<ShippingQuorumDetailDto> pane = 
			new ObjectTablePane<ShippingQuorumDetailDto>("Shipping Detail",clumnMappings.get(),false,false);
		pane.getTable().setFont(new Font("sansserif", 1, 16));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	public void actionPerformed(ActionEvent e) {
		String ein = StringUtils.trim(einField.getComponent().getText());
		if(StringUtils.isEmpty(ein)) return;
		
		List<ShippingQuorumDetailDto> details = getDao(ShippingQuorumDetailDao.class).findAllDetailsByEngineNumber(ein);
		quorumDetailTablePane.reloadData(details);
	}
}
