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
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dto.ShippingQuorumDetailDto;
/**
 * 
 * 
 * <h3>ShippingKdLotQueryPanel Class description</h3>
 * <p> ShippingKdLotQueryPanel description </p>
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
public class ShippingKdLotQueryPanel extends TabbedPanel{

	private static final long serialVersionUID = 1L;

	private LabeledTextField kdLotField = new LabeledTextField("KD Lot Number");
	private JButton searchButton = new JButton("Search");
	
	private ObjectTablePane<ShippingQuorumDetailDto> quorumDetailTablePane;
	
	ShippingKdLotQueryPanel(MainWindow mainWindow){
		super("",0,mainWindow);
		initComponents();
		kdLotField.getComponent().addActionListener(this);
		searchButton.addActionListener(this);
	}

	@Override
	public void onTabSelected() {
		kdLotField.getComponent().requestFocusInWindow();
		
	}
	
	private void initComponents() {
		setLayout(new MigLayout("insets 20 200 20 200", "[grow,fill]"));
		kdLotField.setFont(new Font("sansserif", 1, 20));
		searchButton.setFont(new Font("sansserif", 1, 20));
		kdLotField.getComponent().requestFocusInWindow();
		add(kdLotField);
		add(searchButton,"wrap");
		add(quorumDetailTablePane = createQuorumDetailTablePane(),"span");
	}
	
	private ObjectTablePane<ShippingQuorumDetailDto> createQuorumDetailTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer No","trailerNumber")
		.put("Trailer Row","trailerRow").put("Quorum Seq","quorumSeq").put("YMTO","ymto")
		.put("Engine Number","engineNumber");
	
		ObjectTablePane<ShippingQuorumDetailDto> pane = 
			new ObjectTablePane<ShippingQuorumDetailDto>("Shipping Detail",clumnMappings.get(),false,false);
		pane.getTable().setFont(new Font("sansserif", 1, 16));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		ViewUtil.setPreferredHeight(pane, 600);
		return pane;
	}
	
	public void actionPerformed(ActionEvent e) {
		String kdLot = StringUtils.trim(kdLotField.getComponent().getText());
		if(StringUtils.isEmpty(kdLot)) return;
		
		List<ShippingQuorumDetailDto> details = getDao(ShippingQuorumDetailDao.class).findAllDetailsByKdLot(kdLot);
		quorumDetailTablePane.reloadData(details);
	}
}
