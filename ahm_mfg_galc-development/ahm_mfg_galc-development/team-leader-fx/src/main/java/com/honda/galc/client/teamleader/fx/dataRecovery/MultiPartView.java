package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;

import javafx.scene.Node;
import javafx.scene.control.Label;
import net.miginfocom.layout.CC;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartView</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 * @created July 18, 2017
 */

public class MultiPartView extends MigPane {

	private PartDefinition[] partDefinitions;
	private int rowCount;
	private boolean renderLabels;
	private List<PartView> partViews;

	public MultiPartView(int rowCount, boolean renderLabels, PartDefinition... pds) {
		this.partDefinitions = pds;
		this.rowCount = rowCount;
		this.renderLabels = renderLabels;
		this.partViews = new ArrayList<PartView>();
		initView();
	}


	protected void initView() {
		if (getPartDefinitions() == null) {
			return;
		}
		int colCount = getColumnCount();
		for (int i = 0; i < getPartDefinitions().length; i++) {
			PartDefinition pd = getPartDefinitions()[i];
			PartView view = new PartView(pd, pd.isStatus(), pd.isValue());
			getPartViews().add(view);
			CC cc = new CC();
			if (StringUtils.isNotBlank(pd.getLabel())) {
				Label label = new Label(pd.getLabel());
				Utils.setFontSize(label);
				add(label);
			} else {
				cc.spanX(2);
			}
			if ((i + 1) % colCount == 0) {
				cc.wrap();
			}
			add(view,cc.gap("0").minWidth((Utils.getScreenWidth() * 0.08)+"px"));
		}
	}

	// === controlling api === //
	public void setIdleMode() {
		for (PartView view : getPartViews()) {
			view.setIdleMode();
		}
	}

	public void setReadOnlyMode() {
		for (PartView view : getPartViews()) {
			view.setReadOnlyMode();
		}
	}

	public void setEditMode() {
		for (PartView view : getPartViews()) {
			if (view.getPartDefinition().isEditable()) {
				view.setEditMode();
			}
		}
	}

	protected Node getFirstFocusableComponent() {
		for (PartView view : getPartViews()) {
			Node comp = view.getFirstFocusableComponent();
			if (comp != null) {
				return comp;
			}
		}
		return null;
	}

	protected LoggedTextField getFirstTextFieldInState(TextFieldState state) {
		for (PartView view : getPartViews()) {
			LoggedTextField comp = view.getFirstTextFieldInState(state);
			if (comp != null) {
				return comp;
			}
		}
		return null;
	}

	// === get/set === //
	protected PartDefinition[] getPartDefinitions() {
		return partDefinitions;
	}

	protected int getRowCount() {
		return rowCount;
	}

	protected boolean isRenderLabels() {
		return renderLabels;
	}

	protected int getColumnCount() {
		int colCount = getPartDefinitions().length;
		if (getRowCount() > 1) {
			double d = Double.valueOf(colCount);
			d = d / getRowCount();
			colCount = (int) Math.ceil(d);
		}
		return colCount;
	}

	protected List<PartView> getPartViews() {
		return partViews;
	}
}
