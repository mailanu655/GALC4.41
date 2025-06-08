package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductTypeData;
/**
 * 
 * <h3>ManualLtCtrResultEnterView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLtCtrResultEnterView description </p>
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
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
public class ManualLtCtrResultEnterView extends JFrame 
implements ActionListener, WindowListener{

    private static final long serialVersionUID = 1943762991205931891L;
    
    private JPanel contentPane = null;
    private JPanel resultEnterViewPane = null;
    private StatusMessagePanel statusMessagePane = null;
    private ProductPanel productPanel;
    private ManualLtCtrResultEnterDataPanel dataPanel;
    private JPanel resultEnterPane = null;
    private IManualLtCtrResultEnterViewManager manager;
	private ProductTypeData productTypeData;
	protected ManualLotControlRepairPropertyBean property;

	public ManualLtCtrResultEnterView(ProductTypeData productTypeData, IManualLtCtrResultEnterViewManager viewManager) {
        super();
        this.productTypeData = productTypeData;
        this.manager = viewManager;
        initialize();
    }
	
	public ManualLtCtrResultEnterView(ProductTypeData productTypeData, IManualLtCtrResultEnterViewManager viewManager, 
			ManualLotControlRepairPropertyBean property) {
        super();
        this.productTypeData = productTypeData;
        this.manager = viewManager;
        this.property = property; 
        initialize();
    }

	private void initialize() {
        try {
            setName(this.getClass().getSimpleName());
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(1024, 768);
            setTitle(this.getClass().getSimpleName());
            setContentPane(getViewContentPane());
            initConnections();
            
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    public void actionPerformed(ActionEvent e) {
    	
        if (isTorqueField(e.getSource())) 
        	manager.confirmMeasurement();
        
        if(isStringValueField(e.getSource()))
        	manager.confirmStringValue();
        
        if (e.getSource() == getDataPanel().getButtonExit()) 
        	manager.subScreenClose(e);
        if (e.getSource() == getDataPanel().getPartSnField()) 
        	manager.confirmPartSn();
        if (e.getSource() == getDataPanel().getButtonHlCompleted())
        	manager.setHlStatus();
        if (e.getSource() == getDataPanel().getButtonSave()) 
        	manager.saveUpdate();
        if (e.getSource() == getDataPanel().getButtonReset())
        	manager.resetScreen();
        if(e.getSource() == getDataPanel().getButtonTorqueValue())
        	manager.addDefaultTorqueValues();
    }

    private boolean isStringValueField(Object source) {
    	for(Object field : getDataPanel().getStringvalueFieldList())
			if(field == source) return true;
		
		return false;
	}

	private boolean isTorqueField(Object source) {
		for(Object field : getDataPanel().getTorqueFieldList())
			if(field == source) return true;
		
		return false;
	}

    protected void confirmMissionTye(){};

	public Object getCurrentProductId() {
		return getProductPanel().getProductIdField().getText();
	}

	private JPanel getViewContentPane() {
        if (contentPane == null) {
            try {
                contentPane = new javax.swing.JPanel();
                contentPane.setName("JFrameContentPane");
                contentPane.setLayout(new java.awt.BorderLayout());
                contentPane.add(getManualLtCtrResultEnterViewPane(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return contentPane;
    }
  
   
    protected javax.swing.JPanel getResultEnterPane() {
        if (resultEnterPane == null) {
            try {
                resultEnterPane = new JPanel();
                resultEnterPane.setPreferredSize(new Dimension(1024, 600));
                resultEnterPane.setName("ResultEnterPane");
                resultEnterPane.setLayout(new BoxLayout(resultEnterPane,BoxLayout.Y_AXIS));
                resultEnterPane.add(getProductPanel());
                resultEnterPane.add(getDataPanel());
                
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);			
            }
        }
        return resultEnterPane;
    }
 
    private JPanel getManualLtCtrResultEnterViewPane() {
        if (resultEnterViewPane == null) {
            try {
                resultEnterViewPane = new javax.swing.JPanel();
                resultEnterViewPane.setName("ManualLtCtrResultEnterViewPane");
                resultEnterViewPane.setLayout(new BorderLayout());
                getManualLtCtrResultEnterViewPane().add(getResultEnterPane(), BorderLayout.CENTER);
                getManualLtCtrResultEnterViewPane().add(getStatusMessagePane(), BorderLayout.SOUTH);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return resultEnterViewPane;
    }
    
   
    private StatusMessagePanel getStatusMessagePane() {
        if (statusMessagePane == null) {
        	statusMessagePane = new StatusMessagePanel();
        }
        return statusMessagePane;
    }
    
    protected void handleException(Throwable t) {

    	Logger.getLogger().error(t, "Exception " + this.getClass().getSimpleName());
    }
   
    protected void initConnections() throws java.lang.Exception {
        this.addWindowListener(this);
        
        for(LengthFieldBean bean : getDataPanel().getTorqueFieldList())
        	bean.addActionListener(this);
        
        for(LengthFieldBean bean : getDataPanel().getStringvalueFieldList())
        	bean.addActionListener(this);
        
       
        getDataPanel().getButtonExit().addActionListener(this);
        getDataPanel().getPartSnField().addActionListener(this);
        getDataPanel().getJCheckBoxHLCompleted().addActionListener(this);
        getDataPanel().getButtonHlCompleted().addActionListener(this);
        getDataPanel().getButtonSave().addActionListener(this);
        getDataPanel().getButtonReset().addActionListener(this);
        getDataPanel().getButtonTorqueValue().addActionListener(this);


    }
    
    public ProductPanel getProductPanel() {
    	if(productPanel == null){
    		if(property != null && property.isProductIdCheckDisabled()) {
    			productPanel = new ProductPanel(this, property.getMaxProductSnLength(), productTypeData);
    		} else {
    			productPanel = new ProductPanel(this, productTypeData);
    		}
    		productPanel.getProductLookupButton().setEnabled(false);
    	}
		return productPanel;
	}


	public void windowActivated(java.awt.event.WindowEvent e) {
        locateFocus();       
    }

	private void locateFocus() {
		manager.locateFocus();
	}

   
    public JTextField getCurrentTorque(int index) {
        return getDataPanel().getCurrentTorque(index);
	}

	public void windowClosed(WindowEvent e) {
        if (e.getSource() == this) 
        	subScreenClose();
    }

	public void windowClosing(java.awt.event.WindowEvent e) {
        
    }
    
    public void windowDeactivated(java.awt.event.WindowEvent e) {
    }
    
    public void windowDeiconified(java.awt.event.WindowEvent e) {
    }
    
    public void windowIconified(java.awt.event.WindowEvent e) {
        
    }
    
    public void windowOpened(java.awt.event.WindowEvent e) {
       locateFocus();
    }
    
  
    public void setErrorMessageArea(String msg) {
    	getStatusMessagePane().setErrorMessageArea(msg);
		
	}

	public ManualLtCtrResultEnterDataPanel getDataPanel() {
    	if(dataPanel == null)
    		dataPanel = new ManualLtCtrResultEnterDataPanel();
    	
		return dataPanel;
	}


    private void subScreenClose() {
		setErrorMessageArea(null);
		
	}

	public void showPartComment(String comment) {
		getDataPanel().showPartComment(comment);
		
	}

	public LengthFieldBean getCurrentStringValueField(int index) {
		 return getDataPanel().getCurrentStringValueField(index);
	}

}
