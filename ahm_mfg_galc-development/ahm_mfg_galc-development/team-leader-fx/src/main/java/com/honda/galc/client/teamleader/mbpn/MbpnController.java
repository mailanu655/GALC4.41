package com.honda.galc.client.teamleader.mbpn;


import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.Mbpn;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MbpnController </code> is the Controller class for Mbpn.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @author L&T Infotech
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class MbpnController extends AbstractController<MbpnModel, MbpnMaintPanel> implements EventHandler<ActionEvent> {

	public MbpnController(MbpnModel model, MbpnMaintPanel view) {
		super(model, view);
		getModel().setApplicationContext(getView().getMainWindow().getApplicationContext());
	}

	public void handle(ActionEvent event) {
		if(event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			Mbpn mbpn= getView().getMbpnTablePane().getSelectedItem();
			if(QiConstant.CREATE.equals(menuItem.getText())) createMbpn(event);	
			else if(QiConstant.UPDATE.equals(menuItem.getText())) updateMbpn(event,mbpn);
		}
	}

	
	/**
	 *  this method is called when update contextmenu is clicked
	 * @param event
	 * @param mbpn
	 */
	private void updateMbpn(ActionEvent event, Mbpn mbpn) {
		MbpnDialog dialog = new MbpnDialog(QiConstant.UPDATE, mbpn, getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload();
	}

	/**
	 * this method is called when create contextmenu is clicked
	 * @param event
	 */
	private void createMbpn(ActionEvent event) {
		MbpnDialog dialog = new MbpnDialog(QiConstant.CREATE, new Mbpn(), getModel(), getApplicationId());
		dialog.showDialog();
		getView().reload();
	}

	public void addContextMenuItems() {
		if(getView().getMbpnTablePane().getSelectedItem()!=null){
			String[] menuItems = new String[] {
					QiConstant.CREATE, QiConstant.UPDATE
			};
			getView().getMbpnTablePane().createContextMenu(menuItems, this);
		}
		else{
			String[] menuItems = new String[] {
					QiConstant.CREATE
			};
			getView().getMbpnTablePane().createContextMenu(menuItems, this);
		}

	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addMbpnListener();
		}
	}

	
	/**
	 * This method is for Mbpn table listener
	 */
	private void addMbpnListener() {
		getView().getMbpnTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mbpn>() {
			public void changed(
					ObservableValue<? extends Mbpn> arg0,
					Mbpn arg1,
					Mbpn arg2) {
				addContextMenuItems();
				if(arg2!=null)
				   Logger.getLogger().check("Selected Row:"+arg2.toString());
			}
		});
		getView().getMbpnTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
}
