
package com.honda.galc.client.teamleader.let;



import static com.honda.galc.service.ServiceFactory.getDao;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.teamleader.let.util.AsciiTrimDocument;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.enumtype.LetProductType;
import com.honda.galc.message.Message;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetProductPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private List listenerList = new ArrayList();
	private JLabel frameNoLabel = null;
	private JTextField frameNo = null;
	private JLabel productLabel = null;
	private JTextField productTextField = null;
	private JLabel mtoLabel = null;
	private JLabel mto = null;
	private JButton vinButton = null;
	private JTextField initialFocusText = null;
	private String modelYearCode=null;
	private String modelCode=null;
	private String modelTypeCode=null;
	private String modelOptionCode=null;
	private String extColorCode=null;
	private String intColorCode=null;
	private String mtoc=null;
	private java.sql.Date planOffDate=null;
	private String vinPanelCase=null;
	private String seriesFrameNo=null;
	private TabbedPanel parentPanel=null;
	private JComboBox comboProductType=null;
	private JLabel lblDropDown=null;
	
	public LetProductPanel( TabbedPanel parentPanel) {
		super();
		initialize();
		this.parentPanel=parentPanel;
	}

	public void addListener(LetProductPanelListener listener) {
		listenerList.add(listener);
	}

	public void setFrameNo(String frameNo) {
		getFrameNo().setText(frameNo);
	}

	public void setVin(String vin) {
		getProductId().setText(vin);
	}

	public void setMto(String mto) {
		getMto().setText(mto);
	}

	private void initialize() {

		this.setLayout(null);     
		String useFrameNo =Message.get("USE_FRAME");
		if (useFrameNo.equals("true")) {
			this.add(getFrameNoLabel(), null);
			this.add(getFrameNo(), null);
			this.setInitialFocus(getFrameNo());
		} else {
			getFrameNoLabel();
			getFrameNo();
			this.setInitialFocus(getProductId());
		}
		this.add(getMto(), null);
		this.add(getProductIdLabel(), null);
		this.add(getLblDropDown(), null);
		this.add(getDropDown(), null);
		this.add(getProductId(), null);
		this.add(getMtoLabel(), null);
		this.add(getProductIdButton(), null);
		this.setSize(1000, 87);
	}

	private JLabel getFrameNoLabel() {
		if (frameNoLabel == null) {
			frameNoLabel = new JLabel();
			frameNoLabel.setSize(135, 35);
			frameNoLabel.setText(Message.get("SeriesFrameNo"));
			frameNoLabel.setLocation(75, 9);
		}
		return frameNoLabel;
	}


	public JTextField getFrameNo() {
		if (frameNo == null) {
			frameNo = new JTextField();
			frameNo.setSize(250, 30);
			frameNo.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			frameNo.setLocation(215, 12);
			frameNo.setDocument(new AsciiTrimDocument(10, 17));
			frameNo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					myActionListenerForFrameNo();
				}
			});
		}
		return frameNo;
	}


	private JLabel getProductIdLabel() {
		if (productLabel == null) {
			productLabel = new JLabel();
			productLabel.setSize(100, 35);
			productLabel.setText(Message.get("VIN"));
			productLabel.setLocation(35, 46);
		}
		return productLabel;
	}


	private javax.swing.JButton getProductIdButton() {
		if (vinButton == null) {
			vinButton = new javax.swing.JButton();
			vinButton.setSize(75, 30);
			vinButton.setText("Search");
			vinButton.setLocation(380, 50);
			vinButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getProductIdButton(e);
				}
			});
		}
		return vinButton;
	}

	private JLabel getLblDropDown() {
		if (lblDropDown == null) {
			lblDropDown = new JLabel();
			lblDropDown.setSize(80, 31);
			lblDropDown.setText("Product Type");
			lblDropDown.setLocation(35, 10);
		}
		return lblDropDown;
	}

	@SuppressWarnings("unchecked")
	private JComboBox getDropDown() {
		if (comboProductType == null) {
			comboProductType = new JComboBox(LetProductType.values());
			comboProductType.setSize(250, 35);
			comboProductType.setLocation(140, 10);
			comboProductType.setSelectedItem(LetProductType.FRAME);
			comboProductType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					productTextField.setText("");
					mto.setText("");
					productLabel.setText(LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductLabel());
					int productIdLength = LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductIdLength();
					productTextField.setToolTipText("Expected length is " + productIdLength);
					productTextField.setDocument(new AsciiDocument(productIdLength));
					//clear table data
					Iterator it = listenerList.iterator();
					while (it.hasNext()) {
						((LetProductPanelListener) it.next()).actionPerformed();
					}
					parentPanel.getMainWindow().clearMessage();
				}
			});
		}
		return comboProductType;
	}

	public JTextField getProductId() {
		if (productTextField == null) {
			productTextField = new JTextField();
			productTextField.setSize(250, 30);
			productTextField.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 18));
			productTextField.setName("productIdTextField");
			productTextField.setLocation(140, 50);
			int productIdLength = LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductIdLength();
			productTextField.setDocument(new AsciiDocument(LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductIdLength()));
			productTextField.setToolTipText("Expected length is " + productIdLength);
			productTextField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					myActionListenerForVin();
				}
			});
		}
		return productTextField;
	}


	private JLabel getMtoLabel() {
		if (mtoLabel == null) {
			mtoLabel = new JLabel();
			mtoLabel.setSize(75, 35);
			mtoLabel.setText(Message.get("YMTO"));
			mtoLabel.setLocation(500, 45);
		}
		return mtoLabel;
	}


	private JLabel getMto() {
		if (mto == null) {
			mto = new JLabel();
			mto.setBounds(575, 50, 395, 25);
			mto.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
		}
		return mto;
	}

	public void setData() {


	}

	private void myActionListenerForFrameNo() {

		try {

			String strFrameNo = frameNo.getText();
			if (strFrameNo.equals("")) {
				Logger.getLogger(parentPanel.getApplicationId()).error("An error Occurred while processing the LET VIN Panel");
				parentPanel.getMainWindow().setErrorMessage("An error Occurred while processing the LET VIN Panel");
				return;
			}
			productTextField.setText("");
			mto.setText("");			
			setFrameNo(frameNo.getText());
			setModelYearCode("");
			setModelCode("");
			setModelOptionCode("");
			setModelTypeCode("");
			setVinPanelCase(LETDataTag.VIN_PANEL_CASE_FNO);
			Iterator it = listenerList.iterator();
			while (it.hasNext()) {
				((LetProductPanelListener) it.next()).actionPerformed();
			}
			getFrameNo().selectAll();
		} catch (Exception e) {
			Logger.getLogger(parentPanel.getApplicationId()).error(e,"An error Occurred while processing the LET VIN Panel");
			e.printStackTrace();
			parentPanel.getMainWindow().setErrorMessage("An error Occurred while processing the LET VIN Panel");
		}
	}

	private void myActionListenerForVin() {
		try {
			String strProductID = productTextField.getText();
			int productIdLength = LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductIdLength();
			String productLabel = LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductLabel();
			
			if(StringUtils.isBlank(strProductID) || strProductID.length() < productIdLength){
				Logger.getLogger(parentPanel.getApplicationId()).error("Please enter " + productLabel + " of expected length - " + productIdLength);
				parentPanel.getMainWindow().setErrorMessage("Please enter " + productLabel + " of expected length - " + productIdLength);
				return;
			}
			BaseProduct product = null;
			if (StringUtils.isNotBlank(getDropDown().getSelectedItem().toString()) && (StringUtils.isNotBlank(strProductID))) {
				LetProductType selectedProductType=LetProductType.valueOf(getDropDown().getSelectedItem().toString());
				product=selectedProductType.getProductDao().findBySn(strProductID);

				if(product==null){
					Logger.getLogger(parentPanel.getApplicationId()).error("Product does not exist");
					parentPanel.getMainWindow().setErrorMessage("Product does not exist");
					return;
				}
			}
			productTextField.setText(strProductID);
			frameNo.setText("");
			mto.setText("");		
			setFrameNo("");
			setModelYearCode("");
			setModelCode("");
			setModelOptionCode("");
			setModelTypeCode("");
			setVinPanelCase(LETDataTag.VIN_PANEL_CASE_VIN);
			Iterator it = listenerList.iterator();
			while (it.hasNext()) {
				((LetProductPanelListener) it.next()).actionPerformed();
			}
			getProductId().selectAll();

		} catch (Exception e) {
			Logger.getLogger(parentPanel.getApplicationId()).error(e,"An error Occurred while processing the LET VIN Panel");
			e.printStackTrace();
			parentPanel.getMainWindow().setErrorMessage("An error Occurred while processing the LET VIN Panel");
		}
	}

	private void setInitialFocus(JTextField text) {
		initialFocusText = text;
	}

	public void getInitialFocus() {

		if (initialFocusText == null) {

			initialFocusText = getProductId();
		}
		initialFocusText.requestFocusInWindow();
		initialFocusText.selectAll();
	}

	public void getProductIdButton(java.awt.event.ActionEvent actionEvent) {
		try {

			ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(parentPanel.getMainWindow(), LetProductType.getType(getDropDown().getSelectedItem().toString()).toString(),
					LetProductType.getType(getDropDown().getSelectedItem().toString()).getProductLabel());
			manualProductEntry.setModal(true);
			manualProductEntry.setVisible(true);
			getProductId().requestFocus();
			String productId =manualProductEntry.getResultProductId();

			if (!productId.equals("")) {
				getProductId().setText(productId);
				this.myActionListenerForVin();

			}
			return;
		}
		catch (Exception e) {
			Logger.getLogger(parentPanel.getApplicationId()).error(e,"An error Occurred while processing the LET VIN Panel");
			parentPanel.getMainWindow().setErrorMessage("An error Occurred while processing the LET VIN Panel");
			e.printStackTrace();
		}
	}


	public String getModelYearCode() {
		return modelYearCode;
	}


	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}


	public String getModelCode() {
		return modelCode;
	}


	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}


	public String getModelTypeCode() {
		return modelTypeCode;
	}


	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}


	public String getModelOptionCode() {
		return modelOptionCode;
	}


	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getVinPanelCase() {
		return vinPanelCase;
	}


	public void setVinPanelCase(String vinPanelCase) {
		this.vinPanelCase = vinPanelCase;
	}


	public String getSeriesFrameNo() {
		return seriesFrameNo;
	}


	public void setSeriesFrameNo(String seriesFrameNo) {
		this.seriesFrameNo = seriesFrameNo;
	}

	public String getExtColorCode() {
		return extColorCode;
	}


	public void setExtColorCode(String extColorCode) {
		this.extColorCode = extColorCode;
	}


	public String getIntColorCode() {
		return intColorCode;
	}


	public void setIntColorCode(String intColorCode) {
		this.intColorCode = intColorCode;
	}


	public java.sql.Date getPlanOffDate() {
		return planOffDate;
	}


	public void setPlanOffDate(java.sql.Date planOffDate) {
		this.planOffDate = planOffDate;
	}


	public String getMtoc() {
		return mtoc;
	}


	public void setMtoc(String mtoc) {
		this.mtoc = mtoc;
	}

	public boolean getData()
	{
		try {
			if (getVinPanelCase().equals(LETDataTag.VIN_PANEL_CASE_FNO)) 
			{
				Object[] letProduct = getDao(LetResultDao.class).getLetProduct(getSeriesFrameNo());
				if (letProduct !=null)
				{
					if (letProduct[0] != null&& !(((String) letProduct[0]).trim().equals(""))) 
					{
						setVin(((String) letProduct[0]).trim());
					} 				
				} 
			} else if (getVinPanelCase().equals(LETDataTag.VIN_PANEL_CASE_VIN)) {				
				Object[] letProduct = getDao(LetResultDao.class).getLetProductSeriesFrame(getProductId().getText());
				if (letProduct !=null)
				{
					if (letProduct[0] != null&& !(((String) letProduct[0]).trim().equals(""))) 
					{
						setSeriesFrameNo(((String) letProduct[0]).trim());
					}					
				} 
			}
			List<Object[]> vinMtoDataList = getDao(LetResultDao.class).getVinMtoData(getProductId().getText());
			if(vinMtoDataList.size()==0)
			{
				Logger.getLogger(parentPanel.getApplicationId()).error("Vehicle not found");
				parentPanel.getMainWindow().setErrorMessage("Vehicle not found");
				return false;
			}else
			{
				for (Object[] vinMtoData:vinMtoDataList) 
				{
					setMtoc((String) vinMtoData[0]);
					setModelCode((String) vinMtoData[1]);
					setModelYearCode((String) vinMtoData[2]);
					setModelTypeCode((String) vinMtoData[3]);
					setModelOptionCode((String) vinMtoData[4]);
					setExtColorCode((String) vinMtoData[5]);
					setIntColorCode((String) vinMtoData[6]);
					setPlanOffDate((java.sql.Date)vinMtoData[7]);
					setMto(getModelYearCode()+ getModelCode()+ "-"+ getModelTypeCode()+ "-"+ getModelOptionCode());
				}
			}

		} catch (Exception e) {
			Logger.getLogger(parentPanel.getApplicationId()).error(e,"An error occurred in LetVinPanelData");
			parentPanel.getMainWindow().setErrorMessage("An error occurred in LetVinPanelData");
			e.printStackTrace();
		}
		return true;
	}



} 