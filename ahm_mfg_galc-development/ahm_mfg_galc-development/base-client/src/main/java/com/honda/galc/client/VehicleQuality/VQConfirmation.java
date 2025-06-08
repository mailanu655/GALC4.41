package com.honda.galc.client.VehicleQuality;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 23, 2014
 */
public class VQConfirmation extends MainWindow
{

	private static final long serialVersionUID = 1L;
	class WindowIconifiedEventHandler extends WindowAdapter {
		public void windowIconified(WindowEvent e) {
			setState(java.awt.Frame.NORMAL);
			requestFocus();
		};
	}
	private JPanel mainPanel = null;
	protected WindowIconifiedEventHandler wieh = new WindowIconifiedEventHandler();
	private VQConfirmationVinPanel vinPanel = null;
	private JLabel lastPassingVinLabel = null;
	private UpperCaseFieldBean lastPassingUpperCaseTxtField = null;
	private final static int MAX_LENGTH_VIN = 17;
	private ProductCheckUtil productCheckUtil=null;
	private ProcessPoint vqConfProcessPoint=null;

	public VQConfirmation(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		initialize(appContext);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getVqConfVinPanel().getTextFieldVIN()) {
			try {
				scanVin(getVqConfVinPanel().getTextFieldVIN().getText());
			} catch (Exception ex) {
				logException(ex);
			}
		}
	}

	private JLabel getJLabelLastPassVIN() {
		if (lastPassingVinLabel == null) {
			try {
				lastPassingVinLabel = new JLabel();
				lastPassingVinLabel.setName("JLabelLastPassVIN");
				lastPassingVinLabel.setText("LAST PASSING VIN");
				lastPassingVinLabel.setForeground(Color.black);
				lastPassingVinLabel.setFont(new Font("dialog", 0, 30));
				lastPassingVinLabel.setBounds(14, 250, 500, 50);
				lastPassingVinLabel.setHorizontalAlignment(SwingConstants.LEFT);
			} catch (Exception e) {
				logException(e);
			}
		}
		return lastPassingVinLabel;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			try {
				mainPanel = new JPanel();
				mainPanel.setName("MainPanel");
				mainPanel.setLayout(null);
				mainPanel.add(getVqConfVinPanel(), getVqConfVinPanel().getName());
				mainPanel.add(getJLabelLastPassVIN(), getJLabelLastPassVIN().getName());
				mainPanel.add(getUpperCaseFieldLastPassVIN(), getUpperCaseFieldLastPassVIN().getName());
				mainPanel.add(getStatusMessagePanel(),getStatusMessagePanel().getName());
			} catch (Exception e) {
				logException(e);
			}
		}
		return mainPanel;
	}

	@Override
	protected JPanel initStatusMessagePanel() {
		if (statusMessagePanel == null) {
			statusMessagePanel = new StatusMessagePanel();
			statusMessagePanel.setSize(1024, 125);
			statusMessagePanel.setLocation(0, 585);
			statusMessagePanel.getStatusPanel().setUser(getUserId());
			statusMessagePanel.setMessageFont(new java.awt.Font("dialog", 0, 40));
		}
		return statusMessagePanel;
	}

	private UpperCaseFieldBean getUpperCaseFieldLastPassVIN() {
		if (lastPassingUpperCaseTxtField == null) {
			try {
				lastPassingUpperCaseTxtField = new UpperCaseFieldBean();
				lastPassingUpperCaseTxtField.setName("UpperCaseFieldLastPassVIN");
				lastPassingUpperCaseTxtField.setBackground(new Color(204,204,204));
				lastPassingUpperCaseTxtField.setDisabledTextColor(Color.black);
				lastPassingUpperCaseTxtField.setBounds(14, 290, 999, 230);
				lastPassingUpperCaseTxtField.setEnabled(false);
				lastPassingUpperCaseTxtField.setBackground(Color.white);
				lastPassingUpperCaseTxtField.setFont(new Font("dialog", 0, 90));
			} catch (Exception e) {
				logException(e);
			}
		}
		return lastPassingUpperCaseTxtField;
	}

	private VQConfirmationVinPanel getVqConfVinPanel() {
		if (vinPanel == null) {
			try {
				vinPanel = new VQConfirmationVinPanel(this);
				vinPanel.setName("vinPanel");
				vinPanel.setLocation(171, 125);
				vinPanel.setBounds(14,31,999,200);
				vinPanel.setBackground(Color.white);
				vinPanel.setFocusColor(Color.white);
				vinPanel.setForeground(Color.white);
				vinPanel.getTextFieldVIN().setFont(new Font("dialog", 0, 90));
			} catch (Exception e) {
				logException(e);
			}
		}
		return vinPanel;
	}

	private void logException(Exception e) {
		Logger.getLogger().error(e,"An error Occurred while processing the VQ Confirmation screen");
		e.printStackTrace();
		setErrorMessage("An error Occurred while processing the VQ Confirmation screen");
		getVqConfVinPanel().refreshObject(getVqConfVinPanel().getTextFieldVIN(), null, 4);
	}

	private void logErrorMsg(String errorMsg) {
		Logger.getLogger().error(errorMsg);
		setErrorMessage(errorMsg);
		getVqConfVinPanel().refreshObject(getVqConfVinPanel().getTextFieldVIN(), null, 4);
	}

	private void init() {
		try {
			getVqConfVinPanel().refreshObject(getVqConfVinPanel().getTextFieldVIN(), "", 1);
		} catch (Exception e) {
			logException(e);
		} 
	}

	private void initConnections() throws Exception {
		addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){				
				getVqConfVinPanel().getTextFieldVIN().requestFocus();						
			}
		});
		getVqConfVinPanel().getTextFieldVIN().addActionListener(this);				
	}

	private void initialize(ApplicationContext appContext) {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setSize(1024, 768);
			vinPanel=new VQConfirmationVinPanel(this);
			setLayout(new BorderLayout());
			add(getMainPanel(),BorderLayout.CENTER);
			initConnections();

		} catch (Exception e) {
			logException(e);
		}
		addWindowListener(wieh);
		getVqConfVinPanel().getTextFieldVIN().setBackground(Color.blue);
		init();
	}	



	public void scanVin(String vin) {
		try {	
			vqConfProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(getFrameLinePropertyBean().getVqConfirmationProcessPointId());
			setErrorMessage("");
			if (vin == null)
				vin = "";
			if (vin.trim().length() != MAX_LENGTH_VIN) {
				logErrorMsg("Digit error of the input VIN.");
				return ;
			}
			if(executeProductChecks(vin)) {
				ProductResult productResult = new ProductResult(vin, vqConfProcessPoint.getProcessPointId(), ServiceFactory.getDao(FrameDao.class).getDatabaseTimeStamp());
				productResult.setAssociateNo(applicationContext.getUserId());
				getTrackingService().track(ProductType.FRAME, productResult);
				
				getVqConfVinPanel().refreshObject(getVqConfVinPanel().getTextFieldVIN(), "", 2);
				lastPassingUpperCaseTxtField.setText(vin);
			}
		}		
		catch (Exception e) {
			logException(e);
		}
	}

	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}

	public boolean executeProductChecks(String vin)
	{
		try
		{          			
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			Frame frame = frameDao.findByKey(vin);
			if(frame==null)
			{
				logErrorMsg("The Frame with the specified VIN does not exist."); 
				return false;	
			}
			productCheckUtil=new ProductCheckUtil();
			productCheckUtil.setProduct(frame);
			productCheckUtil.setProcessPoint(vqConfProcessPoint);
			if (productCheckUtil.engineNumberEmptyCheck())
			{
				logErrorMsg("This VIN has no engine.");
				return false;
			}
			if (productCheckUtil.missionNumberEmptyCheck())
			{
				logErrorMsg("This VIN has no mission."); 
				return false;
			}
			List<String> vinHoldList=productCheckUtil.productOnHoldCheck();
			if(vinHoldList!=null&&vinHoldList.size()>0)
			{
				logErrorMsg("Specified VIN is on hold."); 
				return false;
			}		
			List<String> engineHoldList=productCheckUtil.engineOnHoldCheck();
			if(engineHoldList!=null&&engineHoldList.size()>0)
			{
				logErrorMsg("Engine is on hold."); 
				return false;
			}
			productCheckUtil.setProduct(frame);
			if(!productCheckUtil.engineDockingCheck())
			{
				logErrorMsg("The Engine type does not match."); 
				return false;
			}
			if(productCheckUtil.duplicateEngineAssignmentCheck())
			{
				logErrorMsg("Engine is assigned to multiple VINs"); 
				return false;
			}
			if(!productCheckUtil.missionDockingCheck())
			{
				logErrorMsg("The Mission type does not match."); 
				return false;
			}           
			if(productCheckUtil.installedPartsInspectionCheck().size()>0)
			{
				logErrorMsg("Required Parts has not been installed."); 
				return false;
			}
			if(productCheckUtil.letCheck().size()>0)
			{
				logErrorMsg("LET Test Failed."); 
				return false;
			}          
			if (!productCheckUtil.onOffProductHistoryCountCheck())
			{
				logErrorMsg("This VIN has not passed ON/OFF points(WE,PA,AF,VQ).");
				return false;
			} 
			ProcessPoint vqOffProcessPoint=ServiceFactory.getDao(ProcessPointDao.class).findByKey(getFrameLinePropertyBean().getVqOffProcessPointId());
			List<ProductResult> vqOffProductResultList=getDao(ProductResultDao.class).findByProductAndProcessPoint(new ProductResult(frame,vqOffProcessPoint));
			if(vqOffProductResultList.size()==0)
			{
				logErrorMsg("This VIN has not passed VQ Off point."); 
				return false;
			}
			if(productCheckUtil.outstandingDefectsCheck().size()>0){
				logErrorMsg("This VIN has Outstanding defects");
				return false;
			}
			removeInRepairArea(vin);            
		}
		catch (Exception e)
		{
			logException(e);
			return false;
		} 
		return true;
	}

	private void removeInRepairArea(String productID) {
		try {
			InRepairArea inRepairArea = getDao(InRepairAreaDao.class).findByKey(productID);
			if (inRepairArea != null)
				getDao(InRepairAreaDao.class).remove(inRepairArea);
		} catch (Exception e) {
			logException(e);
		}
	}
	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
}