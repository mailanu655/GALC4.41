package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.data.HoldByNonProductType;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.service.utils.ProductTypeUtil;

/** * * 
 * @version 0.1 
 * @author Gangadhararao Gadde 
 * @since Mar 27, 2013
 */
public class ReleaseByProductPanel extends HoldReleaseProductBasePanel {
	private static final long serialVersionUID = 1L;
	
	private List<String> productIDsDisallowedReleased = new ArrayList<String>();

	public ReleaseByProductPanel(TabbedMainWindow mainWindow) {
		super("Release Product", KeyEvent.VK_R,mainWindow);	
		initReleaseScreenComponents();
	}
   
	public void initReleaseScreenComponents() 
	{
    	getHoldReleaseInformationLabel().setText("Release Information");
    	getHoldReleaseButton().setText("Release");
    	getSnInput1Panel().setBounds(185, 72, 249, 25);
    	getSnInput2Panel().setBounds(500, 72, 249, 25);
    	getJLabelUnitsHold().setText("Units On Hold");
    	getArrowLabel().setBounds(445, 77, 45, 14);
    	getStartLabel().setBounds(185, 42, 59, 20);
    	getStopLabel().setBounds(500, 42, 59, 20);
    	getHoldReleaseByComboBox().getLabel().setText("Release By:");
    	getMainPanel().add(getHoldReasonSearchComboBox(), getHoldReasonSearchComboBox().getName());
    	initComponents();
		addListeners();
		getHoldReasonSearchComboBox().getComponent().addItemListener(this);
    	getReleaseReasons();
    	getHoldReasons();

	}

	public LabeledComboBox getHoldReleaseByComboBox() {
		if(holdReleaseByComboBox == null){
			holdReleaseByComboBox = new LabeledComboBox("Release By:", false);				
			holdReleaseByComboBox.setBackground(new java.awt.Color(192,192,192));
			holdReleaseByComboBox.getLabel().setForeground(java.awt.Color.black);
			holdReleaseByComboBox.getComponent().setBackground(java.awt.Color.white);
			holdReleaseByComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			holdReleaseByComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			holdReleaseByComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			holdReleaseByComboBox.setBounds(15, 33,165,75);
		}		
		return holdReleaseByComboBox;
	}
	
	public LabeledComboBox getHoldReasonSearchComboBox() {
		if(holdReasonSearchComboBox == null){
			holdReasonSearchComboBox = new LabeledComboBox("Hold Reason", false);				
			holdReasonSearchComboBox.setBackground(new java.awt.Color(192,192,192));
			holdReasonSearchComboBox.getLabel().setForeground(java.awt.Color.black);
			holdReasonSearchComboBox.getComponent().setBackground(java.awt.Color.white);
			holdReasonSearchComboBox.getLabel().setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
			holdReasonSearchComboBox.getLabel().setFont(new java.awt.Font("dialog", 0, 14));
			holdReasonSearchComboBox.getLabel().setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			holdReasonSearchComboBox.setBounds(755, 33, 185, 75);
		}		
		return holdReasonSearchComboBox;
	}
	
	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getHoldReleaseButton()) 
			releaseButtonActionPerformed(e);
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 
			releaseByComboBoxActionPerformed(e);
		if (e.getSource() == getAddButton()) 
			addButtonActionPerformed(e);
		if (e.getSource() == getExcludeButton()) 
			excludeButtonActionPerformed(e);
		if (e.getSource() == getCancelButton()) 
			cancelButtonActionPerformed(e);
		if (e.getSource() == getSnInput1Panel().getTextFieldSN()) 				
			confirmSN(getSnInput1Panel().getTextFieldSN().getText());							
		if (e.getSource() == getSnInput2Panel().getTextFieldSN())			
			confirmSN(getSnInput2Panel().getTextFieldSN().getText());						
	}

	
	@Override
	public void focusLost(java.awt.event.FocusEvent e) {
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 
			FocusLost(e);
		if (e.getSource() == getSnInput1Panel().getSNTextField()) 
		{
			if(getSnInput1Panel().getSNTextField().getText().length()>0 && getSnInput2Panel().getSNTextField().getText().length()>0)
			{
				getAddButton().setEnabled(true);
			}
		}

		if (e.getSource() == getSnInput2Panel().getSNTextField())
		{
			if(getSnInput1Panel().getSNTextField().getText().length()>0 && getSnInput2Panel().getSNTextField().getText().length()>0)
			{
				getAddButton().setEnabled(true);
			}
		}

	}
	
	@Override
	public void itemStateChanged(java.awt.event.ItemEvent e) {
		if (e.getSource() == getHoldReleaseByComboBox().getComponent()) 
			releaseByComboBoxItemStateChanged(e);
		if (e.getSource() == getHoldReleaseReasonComboBox().getComponent()) 
			releaseReasonComboBoxItemStateChanged(e);
		if (e.getSource() == getHoldReasonSearchComboBox().getComponent())
			holdReasonSearchChanged(e);
	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		if (e.getSource() == getPhoneExtTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getPagerTextField().getComponent()) 
			associateDataChanged(null);
		if (e.getSource() == getAssociateNameTextField().getComponent()) 
			associateDataChanged(null);

	}

	private void changeReleaseView(){

		setErrorMessage(null);
		getSnInput1Panel().getTextFieldSN().setBackground(Color.white);
		getSnInput2Panel().getTextFieldSN().setBackground(Color.white);
		getHoldReasonSearchComboBox().getComponent().setBackground(Color.white); 
		if(getHoldReleaseByComboBox().getComponent().getSelectedIndex()<=0)
		{

			setErrorMessage("Please select Release Product Type ");
			changeViewReleaseDisabled();
			return;

		}else if(getSnInput1Panel().getTextFieldSN().getText().equals("")||getSnInput1Panel().getTextFieldSN().getText().equals(""))
		{
			if(!getHoldReasonSearchComboBox().getComponent().getSelectedItem().toString().trim().equals(""))
			{
				searchByHoldReason();
				changeViewReleaseEnabled();
			}else
			{
				setErrorMessage("Please search by start and stop product id or enter the hold reason ");
			}
		} 
		else 
		{
			if(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
			{
				if(getSnInput1Panel().getTextFieldSN().getText().length()< 4 || getSnInput1Panel().getTextFieldSN().getText().length()< 4)
				{
					setErrorMessage("Please enter AfOn Sequence Number in 4 digit format");
					changeViewReleaseDisabled();
					return;
				}
			}

			boolean checkCountResult; 
			try{
				checkCountResult = checkCount();
			}catch(Exception ex){
				setErrorMessage("Please verify the format for the Start and Stop product.");
				changeViewReleaseDisabled();
				return;
			}
			
			if (checkCountResult) 
			{
				if (checkYear()) 
				{
					if (!checkReleaseModelTypes())
					{
						if(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName())||getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
						{						
							JOptionPane.showMessageDialog(getMainWindow(), "Based on selected criteria we found Products that span across multiple model types.Please use the Exclude button to exclude a product from the list", "Validation Error", JOptionPane.ERROR_MESSAGE);						      										
							changeViewReleaseEnabled();
						}     			
						else
						{   							
							changeViewReleaseDisabled();
							setErrorMessage("Error - Product ID Range Spans across Multiple Models.");
							getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
							getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
							getHoldReasonSearchComboBox().getComponent().setBackground(Color.red);
						}
					}
					else 
					{
						changeViewReleaseEnabled();
						if (!getHoldReleaseReasonComboBox().getComponent().isEnabled())
						{
							getHoldReleaseReasonComboBox().getComponent().setEnabled(true);
						}                
						getHoldReleaseReasonComboBox().getComponent().requestFocus();

					}
				} else 
				{
					changeViewReleaseDisabled();
					setErrorMessage("First Part of Start/Stop not the same");
					getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
					getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
				}
			} else 
			{
				changeViewReleaseDisabled();
				if(countCheck ==1 )
				{
					countCheck = 0;				
					JOptionPane.showMessageDialog(getMainWindow(), "ERROR: CANNOT RELEASE MORE THAN 100 VINS \n Click OK and Modify your Input", "Validation Error", JOptionPane.ERROR_MESSAGE);
				}else 
				{				
					setErrorMessage("Error - Difference between start and stop value is not greater than or equal to 1.");
					getSnInput1Panel().getTextFieldSN().setBackground(Color.red);
					getSnInput2Panel().getTextFieldSN().setBackground(Color.red);
				}			
				getHoldReasonSearchComboBox().getComponent().setBackground(Color.red);
			}
		}
		
		if (productIDsDisallowedReleased.size()>0){
			String msgError = "One or more products have not been held or have already been released. "
					+ "These products cannot be released: " + productIDsDisallowedReleased.toString();
			setErrorMessage(msgError);
			changeViewReleaseDisabled();
			return;
		}
		
		if ( getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName()) 
				&& !checkFrameProductsAllowed()){
			setErrorMessage("One or more products are Shipped and is not allowed to release them. "
				+ "List of products already shipped: " + productIDsDisallowedShipped.toString());
			changeViewReleaseDisabled();
		}
	}


	private void searchByHoldReason() {

		ProductType selectedProductType=ProductNumberDef.getProductNumberDefs(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString()).get(0).getProductType();
		int productIdLength=selectedProductType.getProductIdLength();
		List<HoldResult> productOnHoldList= getDao(HoldResultDao.class).findAllByHoldReason(getHoldReasonSearchComboBox().getComponent().getSelectedItem().toString().trim().toUpperCase());
		defaultTableModel =createReleaseModel();
		for(HoldResult holdResult:productOnHoldList)
		{
			if(holdResult.getId().getProductId().trim().length()==productIdLength)
			{
				List<Object[]> productList = ProductTypeUtil.getProductDao(selectedProductType).getProductsWithinRange(holdResult.getId().getProductId(), holdResult.getId().getProductId());
				for (Object[] productData : productList)
				{
					Vector<String> product = new Vector<String>();

					if(!productOnHoldList.isEmpty())
					{
						if(productData[0]!=null)
						{
							product.add((String)productData[0]);

						}
						if(productData[1]!=null)
						{
							product.add((String)productData[1]);	
						}
						if(productData[2]!=null)
						{
							product.add((String)productData[2]);	

						}
						if(productData[3]!=null)
						{
							product.add((String)productData[3]);	

						}
						if(holdResult.getId().getHoldType()==0)
						{
							product.add("Dept. Hold");
						}else
						{
							product.add("Hold At Shipping");
						}
						if(holdResult.getLotHoldStatus()==0)
						{
							product.add("No");
						}else
						{
							product.add("Yes");
						}
						product.add(holdResult.getHoldReason());
						defaultTableModel.addRow(product);
					}				
				}
			}
		}
		getProductsListTable().setModel(defaultTableModel);
		getProductsListTable().repaint();
		getProductsListScrollPane().repaint();

	}

	private DefaultTableModel createReleaseModel()
	{
		defaultTableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		defaultTableModel.addColumn("Product ID");
		defaultTableModel.addColumn("MTOC");
		defaultTableModel.addColumn("Production Date");
		defaultTableModel.addColumn("Production Lot");
		defaultTableModel.addColumn("Hold Type");
		defaultTableModel.addColumn("Lot on Hold");
		defaultTableModel.addColumn("Hold Reason");
		return defaultTableModel;

	}

	private boolean checkReleaseModelTypes() {
		try {

			productIDsDisallowedReleased = new ArrayList<String>();
			List<Object[]> productsList=null;
			if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))
			{				
				productsList = getDao(FrameDao.class).getProductsWithinAfOnSeqRange(Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText()), Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText()));

			}else
			{
				ProductType selectedProductType=ProductNumberDef.getProductNumberDefs(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString()).get(0).getProductType();				
				productsList = ProductTypeUtil.getProductDao(selectedProductType).getProductsWithinRange(getSnInput1Panel().getTextFieldSN().getText(), getSnInput2Panel().getTextFieldSN().getText());			
			}

			boolean uniqueModel=true;
			defaultTableModel =  createReleaseModel();


			for (Object[] productData : productsList)
			{
				Vector<String> product = new Vector<String>();
				String mtoc=null;
				if(productData[0]!=null)
				{
					List<HoldResult> productOnHoldList= getDao(HoldResultDao.class).findAllByProductAndReleaseFlag((String)productData[0], false,HoldResultType.GENERIC_HOLD);
					
					if (productOnHoldList.size()==0)
						productIDsDisallowedReleased.add((String)productData[0]);
					
					for(HoldResult holdResult:productOnHoldList)
					{

						if(!productOnHoldList.isEmpty())
						{
							if(productData[0]!=null)
							{
								product.add((String)productData[0]);

							}
							if(productData[1]!=null)
							{
								if(mtoc!=null&&!(uniqueModel==false))
								{
									if(!(mtoc.equals((String)productData[1])))
									{
										uniqueModel=false;
									}
								}
								mtoc=(String)productData[1];
								product.add((String)productData[1]);	
							}
							if(productData[2]!=null)
							{
								product.add((String)productData[2]);	

							}
							if(productData[3]!=null)
							{
								product.add((String)productData[3]);	

							}
							if(holdResult.getId().getHoldType()==0)
							{
								product.add("Dept. Hold");
							}else
							{
								product.add("Hold At Shipping");
							}
							if(holdResult.getLotHoldStatus()==0)
							{
								product.add("No");
							}else
							{
								product.add("Yes");
							}
							product.add(holdResult.getHoldReason());
							if(getHoldReasonSearchComboBox().getComponent().getSelectedItem().toString().trim().equals("")
									|| getHoldReasonSearchComboBox().getComponent().getSelectedItem().toString().trim().equalsIgnoreCase(holdResult.getHoldReason().trim()))
							{
								defaultTableModel.addRow(product);
							}

						}
					}
				}							
			}															

			getProductsListTable().setModel(defaultTableModel);
			getProductsListTable().repaint();
			getProductsListScrollPane().repaint();

			if(!uniqueModel)
			{
				return false;
			}	

		}catch (Exception e) {
			handleException(e);
			return false;
		} 
		return true;
	}

	private void changeViewReleaseDisabled() {

		changeViewHoldReleaseDisabled();	

	}

	private void changeViewReleaseEnabled() {

		changeViewHoldReleaseEnabled();
	}

	public boolean checkCount() {

		int intStarChar = 0;
		int intStopChar = 17;


		if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.ENGINE).get(0).getName())) {
			intStarChar = 8;
			intStopChar = 12;

		} else if (getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(ProductNumberDef.getProductNumberDef(ProductType.FRAME).get(0).getName())) {
			intStarChar = 11;
			intStopChar = 17;
		}if ((getHoldReleaseByComboBox().getComponent().getSelectedItem().toString().equals(HoldByNonProductType.AF_ON_SEQ_NUM.getProductName()))) {
			intStarChar = 1;
			intStopChar = 4;
		}

		if(Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				- Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				+ 1 > 100){
			countCheck = 1;
			return false;
		}
		else if(Integer.parseInt(getSnInput2Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				- Integer.parseInt(getSnInput1Panel().getTextFieldSN().getText().substring(intStarChar, intStopChar))
				+ 1	<= 0){
			return false;
		}
		else{
			return true;
		}

	}




	public void getReleaseReasons() {
		try{		
			List<Object[]> list = getDao(HoldResultDao.class).getReleaseReasons();

			if (list.size() > 0) {
				for (Object[] array : list) {
					if(array[0]!=null)
					{
						getHoldReleaseReasonComboBox().getComponent().addItem( ((String) array[0]).trim());
					}
				}
			}
		}
		catch(Exception e){
			handleException(e);
			return ;
		}
	}

	public void getHoldReasons() {
		try{
			getHoldReasonSearchComboBox().getComponent().addItem("");
			List<Object[]> list = getDao(HoldResultDao.class).getHoldReasons();

			if (list.size() > 0) {
				for (Object[] array : list) {
					if(array[0]!=null)
					{
						getHoldReasonSearchComboBox().getComponent().addItem( ((String) array[0]).trim() );
					}
				}
			}
		}
		catch(Exception e){
			handleException(e);
		}
	}
	
	public void initializeReleaseView() {
		
		initializeView();
		
		getHoldReasonSearchComboBox().getComponent().setSelectedItem("");
	}

	public void releaseButtonActionPerformed(java.awt.event.ActionEvent actionEvent) {

		if(checkText())
		{
			int ret = JOptionPane.showConfirmDialog(getMainWindow(),"Release the Products now?", "Are you sure ?",  JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) 
			{
				saveReleaseData();
				getHoldReleaseButton().setEnabled(false);
			}			
		}else
		{
			setErrorMessage("Please enter the Associate details");
		}		
	}

	public void releaseByComboBoxActionPerformed(java.awt.event.ActionEvent actionEvent) 
	{		
		holdReleaseByComboBoxActionPerformed();
		changeViewReleaseDisabled();		
	}


	public void releaseByComboBoxItemStateChanged(java.awt.event.ItemEvent itemEvent) {
		initializeReleaseView();

	}


	public void releaseReasonComboBoxItemStateChanged(java.awt.event.ItemEvent itemEvent) {
		getHoldReleaseButton().setEnabled(checkText());

	}

	public void addButtonActionPerformed(java.awt.event.ActionEvent actionEvent){	
		defaultTableModel=new DefaultTableModel();
		getProductsListTable().setModel(defaultTableModel);
		getProductsListTable().repaint();
		changeReleaseView();
		getHoldReleaseButton().setEnabled(checkText());

	}



	public void resetReleaseScreen()
	{		
		resetScreen();
		
		getHoldReasonSearchComboBox().getComponent().setSelectedItem("");
	}

	public void cancelButtonActionPerformed(java.awt.event.ActionEvent actionEvent){	

		resetReleaseScreen();
	}


	public void holdReasonSearchChanged(java.awt.AWTEvent e) {
		if((getSnInput1Panel().getTextFieldSN().getText().trim().length()>0) 
				&& (getSnInput2Panel().getTextFieldSN().getText().trim().length()>0))
			getAddButton().setEnabled(true);
		else
			getAddButton().setEnabled(false);
	}



	public boolean saveReleaseData() {
		try{		

			setErrorMessage(null);
			if(getHoldReleaseReasonComboBox().getComponent().getSelectedItem().toString().trim().length()==0)
			{
				setErrorMessage("Please enter or select the Release Reason");
				return false ;
			}
			Vector data = defaultTableModel.getDataVector();
			List<String> lotHoldList=new ArrayList<String>();
			List<String> scrapList=new ArrayList<String>();
			List<String> exceptionlList=new ArrayList<String>();
			List<String> noneStatusList=new ArrayList<String>();

			for (int i = 0; i < getProductsListTable().getRowCount(); i++)
			{
				Vector row = (Vector) data.elementAt(i);
				String productId =(String)row.get(0);
				List<HoldResult> productListOnHold=getDao(HoldResultDao.class).findAllByProductAndReleaseFlag(productId, false,HoldResultType.GENERIC_HOLD);
				for(HoldResult holdResult:productListOnHold)
				{
					boolean changeHoldStatus=true;
					ProductType selectedProductType=ProductNumberDef.getProductNumberDefs(getHoldReleaseByComboBox().getComponent().getSelectedItem().toString()).get(0).getProductType();					
					BaseProduct product=ProductTypeUtil.getProductDao(selectedProductType).findByKey(productId);
					String trackingStatus= product.getTrackingStatus()==null?"":product.getTrackingStatus().trim();
					if(trackingStatus.equals(getFrameLinePropertyBean().getScrapLineId()))
					{
						scrapList.add(productId);
						changeHoldStatus=false;
					}else if(trackingStatus.equals(getFrameLinePropertyBean().getExceptionLineId()))
					{
						exceptionlList.add(productId);
						changeHoldStatus=false;
					}else if(trackingStatus.equalsIgnoreCase(NONE_LINE_ID))
					{
						noneStatusList.add(productId);
						changeHoldStatus=false;
					}
					if(holdResult.getLotHoldStatus()==1)
					{
						lotHoldList.add(productId);
						changeHoldStatus=false;
					}
					if(changeHoldStatus)
					{
						holdResult.setReleaseAssociateName(getAssociateNameTextField().getComponent().getText());
						holdResult.setReleaseTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						holdResult.setReleaseAssociatePager(getPagerTextField().getComponent().getText());
						holdResult.setReleaseAssociatePhone(getPhoneExtTextField().getComponent().getText());
						holdResult.setReleaseAssociateNo(getAssociateNoTextField().getComponent().getText());
						holdResult.setReleaseReason(getHoldReleaseReasonComboBox().getComponent().getSelectedItem().toString().trim().toUpperCase());
						holdResult.setReleaseFlag((short)1);
						getDao(HoldResultDao.class).update(holdResult);
						logUserAction(UPDATED, holdResult);
					}					
				}				
			}
			String message="";
			if(lotHoldList.size()>0||scrapList.size()>0||exceptionlList.size()>0||lotHoldList.size()>0)
			{
				message=message.concat("Unable to change the hold for the following Products:");
				if(scrapList.size()>0)
				{
					message=message.concat("\n Products in Scrap Line:");				
					for(String scrapProductId:scrapList)
					{

						message=message.concat("\n "+scrapProductId);
					}
				}
				if(exceptionlList.size()>0)
				{
					message=message.concat("\n Products in Exceptional Line:");				
					for(String exceptionalProductId:exceptionlList)
					{
						message=message.concat("\n "+exceptionalProductId);
					}
				}
				if(noneStatusList.size()>0)
				{
					message=message.concat("\n Products in none Status Line:");				
					for(String noneStatusProductId:noneStatusList)
					{
						message=message.concat("\n "+noneStatusProductId);
					}
				}
				if(lotHoldList.size()>0)
				{
					message=message.concat("\n Products having a Lot Hold:");				
					for(String lotHoldProductId:lotHoldList)
					{
						message=message.concat("\n "+lotHoldProductId);
					}
				}
				JOptionPane.showMessageDialog(getMainWindow(), message, "Release Process Status", JOptionPane.INFORMATION_MESSAGE);			
			}
			resetReleaseScreen();

			return true;
		}
		catch(Exception e){
			handleException(e);
			return false;
		}

	}

}