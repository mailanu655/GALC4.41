package com.honda.galc.client.product.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;

/**
 * 
 * 
 * <h3>DefaultProcessInfoDialog Class description</h3>
 * <p> DefaultProcessInfoDialog description </p>
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
 * Mar 17, 2014
 *
 *
 */
public class DefaultProcessInfoDialog extends AbstractProcessInfoDialog{

	public DefaultProcessInfoDialog(ProductController controller) {
		super("Process Info", controller);
		initComponents();
	}

	private void initComponents() {
		setHeight(600);
		setWidth(600);
		Label label = UiFactory.createLabel("PCN/PPE", "PCN/PPE", Fonts.SS_DIALOG_BOLD(40));
		getRootBorderPane().setCenter(label);
	}
	
	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

}
