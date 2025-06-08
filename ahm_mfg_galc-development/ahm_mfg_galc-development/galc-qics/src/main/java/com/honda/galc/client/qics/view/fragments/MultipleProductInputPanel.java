package com.honda.galc.client.qics.view.fragments;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.honda.galc.client.qics.view.screen.LpdcIdlePanel;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>MultipleProductInputPanel</code>
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
 * <TD>Pankaj Gopal</TD>
 * <TD>Apr 16, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Pankaj Gopal
 */

public class MultipleProductInputPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private LpdcIdlePanel lpdcPanel;
	private ArrayList<SingleProductInputPanel> inputPanelList;
	private int panelId;
	private int labelId;
	public enum Style{One_Row, Two_Row};
	private Style style;
	private boolean order;

	public MultipleProductInputPanel(LpdcIdlePanel lpdcPanel, int panelId, int labelId, Style style, boolean order) {
		super();
		this.lpdcPanel = lpdcPanel;
		this.panelId = panelId;
		this.labelId = labelId;
		this.style = style;
		this.order = order;
		initialize();
	}

	protected void initialize() {
		setLayout(style == Style.One_Row ? new GridLayout(2, 1) : new GridLayout(1, 2));
		addProductInputPanels();
	}


	protected void addProductInputPanels() {
		inputPanelList = new ArrayList<SingleProductInputPanel>();
		for (int i = 0; i < 2; i++) {
			inputPanelList.add(i, new SingleProductInputPanel(lpdcPanel, style.ordinal()));
			inputPanelList.get(i).setName("inputPanel"+i);
			inputPanelList.get(i).getInputLabel().setText(String.valueOf(getLabelId()));
			inputPanelList.get(i).getDirectPassButton().setName("DirectPass_" + panelId + String.valueOf(i));
			inputPanelList.get(i).getScrapButton().setName("Scrap_" + panelId + String.valueOf(i));
//			inputPanelList.get(i).getLpdcOnButton().setName("LpdcOn_" + panelId + String.valueOf(i));
			if(order) add(inputPanelList.get(i));
		}
		
		if(!order){
			for(int i = 1; i >= 0; i--)
			{
				add(inputPanelList.get(i));
			}
		}
	}
	
	protected int getLabelId() {
		labelId = labelId + panelId;
		int result = (labelId > 4) ? 4 : labelId;
		return (style == Style.Two_Row && result > 2) ? (result -2) : result;
	}
	
	public JTextField getSingleProductInputField1() {
		return inputPanelList.get(0).getInputField();
	}
	
	public JTextField getSingleProductInputField2() {
		return inputPanelList.get(1).getInputField();
	}
	
	public void directPassDataHandler1() {
		inputPanelList.get(0).directPassButtonHandler();
	}
	
	public void directPassDataHandler2() {
		inputPanelList.get(1).directPassButtonHandler();
	}

}
