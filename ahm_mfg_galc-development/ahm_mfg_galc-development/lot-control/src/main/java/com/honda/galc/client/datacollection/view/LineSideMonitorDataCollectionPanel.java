package com.honda.galc.client.datacollection.view;

import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ColorUtil;

@SuppressWarnings("serial")
public class LineSideMonitorDataCollectionPanel extends DataCollectionPanel {
	protected SingleColumnTablePanel skipedProductPanel = null;
	protected final LineSideMonitorPropertyBean lsmProperty;

	public LineSideMonitorDataCollectionPanel(DefaultViewProperty property, LineSideMonitorPropertyBean lsmProperty, int winWidth, int winHeight) {
		super(property, winWidth, winHeight);
		this.lsmProperty = lsmProperty;
		if (this.lsmProperty.isUseExpectedAsScan()) {
			if (property.isProductIdButton()) {
				super.getJButtonProductId().setVisible(false);
			} else {
				super.getLabelProdId().setVisible(false);
			}
			super.getTextFieldProdId().setVisible(false);
			super.getTextFieldProdId().setEnabled(false);
			super.getLabelExpPIDOrProdSpec().setVisible(false);
			super.getTextFieldExpPidOrProdSpec().setVisible(false);
			super.getLabelLastPid().setVisible(false);
			super.getTextFieldLastPid().setVisible(false);
			super.getTestTorqueButton().setVisible(false);
			super.getWiderButton().setVisible(false);
		}
	}

	@Override
	public void setProductInputFocused() {
		if (this.lsmProperty == null || !this.lsmProperty.isUseExpectedAsScan()) {
			super.setProductInputFocused();
		}
	}

	@Override
	public void setProductSpecBackGroudColor(String colorName) {
		if (!StringUtils.isEmpty(colorName)) {
			getTextFieldExpPidOrProdSpec().setBackground(ColorUtil.getColor(colorName));
		} else {
			super.setProductSpecBackGroudColor(colorName);
		}
	}

	public JComponent getSkipedProductPanel() {
		if (this.skipedProductPanel == null) {
			try {
				this.skipedProductPanel = new SingleColumnTablePanel("Skipped Product");
				this.skipedProductPanel.initialize(this.viewProperty);
			} catch (Exception e) {
				handleException(e);
			}			
		}
		return this.skipedProductPanel;
	}

	@Override
	protected void initPanel() {
		super.initPanel();
		if (this.viewProperty.isMonitorSkippedProduct()) {
			add(getSkipedProductPanel());
		}
	}

	@Override
	public javax.swing.JButton getJButtonProductId() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getJButtonProductId().setVisible(false);
		}
		return super.getJButtonProductId();
	}

	@Override
	public javax.swing.JLabel getLabelProdId() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getLabelProdId().setVisible(false);
		}
		return super.getLabelProdId();
	}

	@Override
	public UpperCaseFieldBean getTextFieldProdId() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getTextFieldProdId().setVisible(false);
		}
		return super.getTextFieldProdId();
	}

	@Override
	public javax.swing.JLabel getLabelExpPIDOrProdSpec() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getLabelExpPIDOrProdSpec().setVisible(false);
		}
		return super.getLabelExpPIDOrProdSpec();
	}

	@Override
	public javax.swing.JTextField getTextFieldExpPidOrProdSpec() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getTextFieldExpPidOrProdSpec().setVisible(false);
		}
		return super.getTextFieldExpPidOrProdSpec();
	}

	@Override
	protected javax.swing.JLabel getLabelLastPid() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getLabelLastPid().setVisible(false);
		}
		return super.getLabelLastPid();
	}

	@Override
	public javax.swing.JTextField getTextFieldLastPid() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getTextFieldLastPid().setVisible(false);
		}
		return super.getTextFieldLastPid();
	}

	@Override
	public javax.swing.JButton getTestTorqueButton() {
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getTestTorqueButton().setVisible(false);
		}
		return super.getTestTorqueButton();
	}

	@Override
	protected void setTestTorqueButtonVisible(boolean visible) {
		if (this.lsmProperty != null && !this.lsmProperty.isUseExpectedAsScan()) {
			super.setTestTorqueButtonVisible(visible);
		}
	}

	@Override
	public javax.swing.JButton getWiderButton() {
		if (this.widerButton == null) {
			super.getWiderButton().setBounds(getButton(2).getX()-getButton(2).getWidth(), getButton(2).getY()+getButton(2).getHeight()+this.viewProperty.getButtonGap(), getButton(2).getWidth()*2, getButton(2).getHeight());
		}
		if (this.lsmProperty != null && this.lsmProperty.isUseExpectedAsScan()) {
			super.getWiderButton().setVisible(false);
		}
		return super.getWiderButton();
	}

}
