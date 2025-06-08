package com.honda.galc.client.datacollection.view;

import java.awt.Rectangle;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Manager to display MTOC and Expected VIN at the same time
 * Display expected EIN/Mission as well
 * <h4>Usage and Example</h4>
 *
 * <h4>Special Notes</h4>
 * Based on ClassicViewManager
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.12.12</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see
 * @ver 0.1
 * @author YX
 */

public class ClassicViewManagerWithExpSN extends ClassicViewManager{
	
	public ClassicViewManagerWithExpSN(ClientContext clientContext) {
		super(clientContext);
	}
	
	//Component for MTOC, which share the same components of 'Expected VIN' in ClassicViewManager
	private JLabel labelProdSpec;
	private JTextField textFieldProdSpec;

	/**
	 * Add expected Part values in the part labels
	 */
	@Override
	public void partVisibleControl(List<LotControlRule> lotControlRules) {
		try {
			// check LotControlRuleInfo
			if (lotControlRules == null || lotControlRules.size() == 0)
				return;

			view.getTextFieldExpPidOrProdSpec().setVisible(true);

			int scanPartsIndex = 0;
			// control Part with expected values
			for (int i = 0;((i < view.getMaxNumPart()) && (i < lotControlRules.size())); i++) {
				if (lotControlRules.get(i) != null && (lotControlRules.get(i).getSerialNumberScanFlag() == 1 || lotControlRules.get(i).isDateScan())) {
					PartName partName = lotControlRules.get(i).getPartName();
					String text = "<html>"+ "<table border=0><tr>" + 
						"<td align='right' nowrap='nowrap'>Expected " + partName.getWindowLabel() +":</td><td align='left' width='140' nowrap='nowrap'>&nbsp;&nbsp;" + 
						lotControlRules.get(i).getParts().get(0).getPartSerialNumberMask() + "</td></tr><tr><td align='right'>" + 
						partName.getWindowLabel() + ":" + "</td><td align='left'></td></tr></table></html>";
					view.getPartLabel(i).setText(text);
					view.setFont(new java.awt.Font("dialog", 0, 18));
					repositionPartLabel(view, view.getPartLabel(i), scanPartsIndex,isAfOnSeqNumExist());
					repositionSerialNumber(view, view.getPartSerialNumber(i), scanPartsIndex,isAfOnSeqNumExist());
					view.getPartLabel(i).setVisible(true);
					view.getPartSerialNumber(i).setVisible(true);

					scanPartsIndex++;
				}
			}
		}
		catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::partVisibleControl() exception.");
		}
	}
	
	/**
	 * adjust the position of part label position to contain expected part value
	 * @param view Lot Control Panel
	 * @param partLabel label with expected part value
	 * @param position the part index
	 * @param isAfOnSeqNoExist 
	 */
	public void repositionPartLabel(ClassicDataCollectionPanel view, JLabel partLabel, int position, boolean isAfOnSeqNoExist) {
		int x = -30;
		Rectangle bounds = view.getLabelExpPidOrProdSpecRefrenceBounds();
		if(isAfOnSeqNoExist)
			partLabel.setBounds(bounds.x + x, bounds.y + (position + 2)*(bounds.height)* 2 + view.getSeparatorGap()+ bounds.height + view.gap, 
					360, bounds.height*2+view.gap*2+10);
		else
			partLabel.setBounds(bounds.x + x, bounds.y + (position + 1)*(bounds.height)* 2 + view.getSeparatorGap()+ bounds.height + view.gap, 
					360, bounds.height*2+view.gap*2+10);
	}
	
	/**
	 * adjust the position of part text filed
	 * @param view Lot Control Panel
	 * @param partSN part text filed
	 * @param position the part index
	 * @param isAfOnSeqNoExist
	 */
	public void repositionSerialNumber(ClassicDataCollectionPanel view, UpperCaseFieldBean partSN, int position, boolean isAfOnSeqNoExist) {
		Rectangle bounds = view.getTextFieldProdId().getBounds();
		if(isAfOnSeqNoExist)
			partSN.setBounds(bounds.x, bounds.y + (position + 3) * (bounds.height + view.gap)*2 + view.getSeparatorGap()*2, 
					bounds.width -viewProperty.getStatusLabelWidth() - view.gap, bounds.height);
		else
			partSN.setBounds(bounds.x, bounds.y + (position + 2) * (bounds.height + view.gap)*2 + view.getSeparatorGap()*2, 
					bounds.width -viewProperty.getStatusLabelWidth() - view.gap, bounds.height);
	}
	
	/**
	 * same as ClassicViewManager, but to display MTOC and Mission at the same time
	 */
	@Override
	public void productIdOk(ProcessProduct state) {
		Logger.getLogger().debug(getClass().getName()+": entering productIdOk");

		//Show product Id status
		view.getTextFieldProdId().setText(state.getProductId());
		if(state.getProduct().isValidProductId()) {
			view.getTextFieldProdId().setColor(ViewControlUtil.VIEW_COLOR_OK);
			moveTextFieldHighlight(view.getTextFieldProdId(), null, ViewControlUtil.VIEW_COLOR_OK);
		} else {
			view.getTextFieldProdId().setColor(ViewControlUtil.VIEW_COLOR_NG);
		}

		//Display additional MTOC
		renderProdSpec(view.getProdSpecLabel(), state.getProductSpecCode());
		if(isAfOnSeqNumExist()){
			renderSeqNo(view.getAfOnSeqNumLabel(), getSequenceNumberFromState(state));
		}
		if(isProductLotCountExist()){
			view.renderProductCount(view.getProductCountLabel(), state.getProductCount(), state.getLotSize());
		}
		//Show data collection buttons
		buttonControl(view.getButton(0), true, viewProperty.isEnableCancel());
		buttonControl(view.getButton(1), true, viewProperty.isEnableSkipPart());
		buttonControl(view.getButton(2), true, viewProperty.isEnableSkipProduct());
		buttonControl(view.getButton(3), true, viewProperty.isEnableNextProduct());		
		
		//Disable ProductId Button
		view.getJButtonProductId().setEnabled(false);

		//init display
		partVisibleControl(state.getLotControlRules());
		torqueVisibleControl(state.getLotControlRules());
		
		clearMessageArea(state);
		if(!state.getProduct().isValidProductId()) {
			setErrorMessage("The product id entered is not the expected product id");
		}
		
		if(state.getProduct().isMissingRequiredPart())
			setErrorMessage(state.getProduct().getMissingRequiredPartMessage());
		notifyNewProduct(state.getProductId());
		Logger.getLogger().debug(getClass().getName()+": exiting productIdOk");
	}
	
	private String getSequenceNumberFromState(ProcessProduct state) {
		String sequenceNumber = state.getProduct().getAfOnSequenceNumber();
		if (StringUtil.isNullOrEmpty(sequenceNumber) && state.getProduct() != null && state.getProduct().getProductId() != null) {
			Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(state.getProduct().getProductId());
			if (frame !=  null) {
				sequenceNumber = frame.getLineRef(getProperty().getAfOnSeqNumDisplayLength()).toString();
			}
		}
		return sequenceNumber;
	}
	
	/**
	 * display MTOC
	 * @param label 
	 * @param text MTOC
	 */
	protected void renderProdSpec(String label, String text) {
		getLabelProdSpec().setText(label);
		getLabelProdSpec().setVisible(true);
		getTextFieldProdSpec().setText(text);
		getTextFieldProdSpec().setVisible(true);
	}
	
	/**
	 * add additional components for MTOC
	 */
	@Override
	public void initProductId(ProcessProduct state) {
		super.initProductId(state);
		
		getLabelProdSpec().setVisible(false);
		getTextFieldProdSpec().setVisible(false);
	}
	
	private JLabel getLabelProdSpec () {
		if(labelProdSpec == null) {
			labelProdSpec = new javax.swing.JLabel();
			Rectangle expPidBounds = view.getLabelExpPidOrProdSpecRefrenceBounds();
			labelProdSpec.setName("LabelProdSpec");
			labelProdSpec.setFont(new java.awt.Font("dialog", 0, 18));
			labelProdSpec.setText("");
			labelProdSpec.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelProdSpec.setBounds(expPidBounds.x, expPidBounds.y + 46 + expPidBounds.height, expPidBounds.width, expPidBounds.height);
			labelProdSpec.setForeground(java.awt.Color.black);
			view.add(labelProdSpec);
		}
		return labelProdSpec;
	}
	
	private JTextField getTextFieldProdSpec() {
		if(textFieldProdSpec == null) {
			textFieldProdSpec = new javax.swing.JTextField();
			Rectangle expPidFieldBounds = view.getTextFieldExpPidOrProdSpecRefrenceBounds();
			textFieldProdSpec.setName("TextFieldProdSpec");
			textFieldProdSpec.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldProdSpec.setText("XXXXXXXXXXXX");
			int fieldWith = viewProperty.isShowProductSubid() ? expPidFieldBounds.width *7/10 : expPidFieldBounds.width;
			textFieldProdSpec.setBounds(expPidFieldBounds.x, expPidFieldBounds.y + 46 + expPidFieldBounds.height, fieldWith, expPidFieldBounds.height);
			view.add(textFieldProdSpec);
		}
		return textFieldProdSpec;
	}
	
	
	
}
