package com.honda.galc.client.qi.ipptagentry;

import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.IPPTag;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IPPTagEntryPanel</code> is the view class for IPP Tag Entry 
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
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 31, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author L&T Infotech
 */

public class IppTagEntryPanel extends AbstractQiProcessView<IppTagEntryModel, IppTagEntryPanelController> {
	
	private LoggedLabel ippTagNumberLabel;
	private LoggedTextField ippTagNumberTextField;
	private ObjectTablePane<IPPTag> ippTagTablePane;
	

	public IppTagEntryPanel(MainWindow mainWindow) {
		super(ViewId.IPP_TAG_ENTRY,mainWindow);
		initView();
	}

	@Override
	public void reload() {
		List<IPPTag> list = getModel().selectIppHistory();
        ippTagTablePane.setData(list);
        clearErrorMessage();
        Platform.runLater( new Runnable() {
			@Override
			public void run() {
				ippTagNumberTextField.requestFocus();
			}
		});
	}

	@Override
	public void start() {
	}

	@Override
	public void initView() {
	
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()*0.2;
		double height = (primaryScreenBounds.getHeight()/4)*0.3;
		MigPane migPane = new MigPane();
		ippTagNumberLabel = UiFactory.createLabelWithStyle("IPPTagNumber", "IPP Tag Number", "display-label-18");
		ippTagNumberTextField = createTextField("IPPTextField", width, height , getController());
		ippTagNumberTextField.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		migPane.add(ippTagNumberLabel,"span 3");
		migPane.add(ippTagNumberTextField,"wrap");
		ippTagTablePane = createDisplayPane();
		this.setTop(migPane);
		this.setCenter(ippTagTablePane);
	}

	private ObjectTablePane<IPPTag> createDisplayPane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product ID", "productId")
				  .put("IPP Tag Number", "ippTagNo").put("Division ID", "divisionId")
				  .put("Actual Timestamp", "actualTimestamp");

		Double[] columnWidth = new Double[] {0.25,0.25,0.23,0.26};
		ObjectTablePane<IPPTag> panel = new ObjectTablePane<IPPTag>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
			
	}

	public LoggedLabel getIppTagNumberLabel() {
		return ippTagNumberLabel;
	}

	public void setIppTagNumberLabel(LoggedLabel ippTagNumberLabel) {
		this.ippTagNumberLabel = ippTagNumberLabel;
	}

	public LoggedTextField getIppTagNumberTextField() {
		return ippTagNumberTextField;
	}

	public void setIppTagNumberTextField(LoggedTextField ippTagNumberTextField) {
		this.ippTagNumberTextField = ippTagNumberTextField;
	}

	public ObjectTablePane<IPPTag> getIppTagTablePane() {
		return ippTagTablePane;
	}

	public void setIppTagTablePane(ObjectTablePane<IPPTag> ippTagTablePane) {
		this.ippTagTablePane = ippTagTablePane;
	}
	
	public void onTabSelected(){
		reload();
	}
		
}
