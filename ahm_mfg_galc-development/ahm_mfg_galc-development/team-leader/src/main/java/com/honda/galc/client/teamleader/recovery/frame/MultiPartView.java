package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.component.FontSizeHandler;
import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MultiPartView</code> is ... .
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
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jun 17, 2013
 */
public class MultiPartView extends JPanel {

	private static final long serialVersionUID = 1L;
	private PartDefinition[] partDefinitions;
	private int rowCount;
	private boolean renderLabels;
	private List<PartView> partViews;

	public MultiPartView(int rowCount, boolean renderLabels, PartDefinition... pds) {
		this.partDefinitions = pds;
		this.rowCount = rowCount;
		this.renderLabels = renderLabels;
		this.partViews = new ArrayList<PartView>();
		setLayout(createLayout());
		initView();
	}

	protected LayoutManager createLayout() {
		StringBuilder sb = new StringBuilder();
		if (getPartDefinitions() != null) {
			for (int i = 0; i < getColumnCount(); i++) {
				sb.append(String.format("[][max,fill]"));
			}
		}
		MigLayout layout = new MigLayout("insets 0, gap 0", sb.toString());
		return layout;
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
			CC cc = new CC().height("max");
			if (StringUtils.isNotBlank(pd.getLabel())) {
				JLabel label = new JLabel(pd.getLabel());
				float factor = getRowCount() > 1 ? 0.75f : 0.6f;
				label.addComponentListener(new FontSizeHandler(factor));
				add(label, new CC().height("max").alignX("right").gapLeft("10").gapRight("5"));
			} else {
				cc.spanX(2);
			}
			if ((i + 1) % colCount == 0) {
				cc.wrap();
			}
			add(view, cc);
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

	protected JComponent getFirstFocusableComponent() {
		for (PartView view : getPartViews()) {
			JComponent comp = view.getFirstFocusableComponent();
			if (comp != null) {
				return comp;
			}
		}
		return null;
	}

	protected JTextField getFirstTextFieldInState(TextFieldState state) {
		for (PartView view : getPartViews()) {
			JTextField comp = view.getFirstTextFieldInState(state);
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
