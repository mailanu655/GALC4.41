package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.InRepairAreaDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.InRepairArea;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.ExceptionalOutId;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.EngineLinePropertyBean;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.MbpnLinePropertyBean;
import com.honda.galc.property.MissionLinePropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.QiClearParking;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 13, 2014
 */
@SuppressWarnings(value = { "all" })
public class ExceptionalOutgoingPanel extends TabbedPanel 
{
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JPanel jPanelLastProcPoint = null;
	private LabeledTextField lastProcPointID = null;
	private LabeledTextField lastProcPointName = null;
	private LabeledTextField lastProcPointDiv = null;
	private LabeledTextField lastProcPointLine = null;
	private LabeledTextField lastProcPointTS = null;
	private JLabel jLabelTitle = null;
	private JLabel jLabelProdLot = null;
	private JLabel jLabelTrackingStatus = null;
	private JLabel jLabelEngineType = null;
	private JLabel jLabelKDLot = null;
	private JLabel jLabelMtoc = null;
	private JLabel jLabelReason = null;
	private JButton jButtonSearch = null;
	private JButton jButtonDone = null;
	private JComboBox jComboBoxProdctionLot = null;
	private JComboBox jComboBoxProductId = null;
	private JTextField jTextFieldProductId = null;
	private JRadioButton jRadioButtonExceptional = null;
	private JRadioButton jRadioButtonScrap = null;
	private JTextField jTextFieldEngineType = null;
	private JTextField jTextFieldKDLot = null;
	private JTextField jTextFieldMTOC = null;
	private JTextField jTextFieldTrackingStatus = null;
	private LengthFieldBean reasonLengthFieldBean = null;
	private String shippingStatus = null;
	private List<PreProductionLot> productionLotsList = null;
	private Vector<Vector> productsVec = null;
	private IProperty propertyBean = null;
	private String scrapLineId;
	private String exceptionLineId;
	private String scrapPPId;
	private String exceptionPPId;
	private String productTypeName;
	private String applicationId = getApplicationId();
	private List<BroadcastDestination> broadcastDestinations;
	private static final String IS_BROADCAST_ENABLED = "BROADCAST_ENABLED";
	private static final String IS_RESTRICT_ACCESS = "RESTRICT_ACCESS";
	private static final String IS_NO_RESTRICTION = "NO_RESTRICTION";
	private static final String IS_SCRAP = "SCRAP";
	private static final String IS_EXCEPTIONAL_OUTGOING = "EXCEPTIONAL_OUTGOING";
	

	public ExceptionalOutgoingPanel(TabbedMainWindow mainWindow) {
		super("Exceptional Outgoing Panel", KeyEvent.VK_E,mainWindow);	
		initialize();
	}

	public ExceptionalOutgoingPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(getApplicationId()).info("Exceptional Outgoing Panel is selected");
		if(isInitialized) return;		
		initialize();
	}

	private String getShippingStatus() {
		if (shippingStatus != null)
			return shippingStatus;
		else
			return "";
	}

	private void setShippingStatus(Object status) {
		if (status != null)
			shippingStatus = ((Integer) status).toString();
		else
			shippingStatus = "";
	}

	private javax.swing.JButton getJButtonDone() {
		if (jButtonDone == null) {
			try {
				jButtonDone = new javax.swing.JButton();
				jButtonDone.setName("JButtonDone");
				jButtonDone.setFont(new java.awt.Font("dialog", 0, 14));
				jButtonDone.setText("Done");
				jButtonDone.setBounds(570, 410, 145, 36);
				jButtonDone.setEnabled(false);
				jButtonDone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonDoneActionPerformed(e, getJRadioButtonScrap().isSelected(), getReasonLengthFieldBean().getText());
					}
				});
			} catch (Exception e) {
				logException(e);
			}
		}
		return jButtonDone;
	}
	
	private javax.swing.JButton getJButtonSearch() {
		if (jButtonSearch == null) {
			try {
				jButtonSearch = new javax.swing.JButton();
				jButtonSearch.setName("JButtonSearch");
				jButtonSearch.setFont(new java.awt.Font("dialog", 0, 14));
				jButtonSearch.setHorizontalAlignment(SwingConstants.LEFT);
				jButtonSearch.setText("Product Id:");
				jButtonSearch.setMargin(new Insets(0,5,0,0));
				jButtonSearch.setBounds(305, 110, 130, 30);
				jButtonSearch.setEnabled(true);
				jButtonSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonSearchActionPerformed();
					}
				});
			} catch (Exception e) {
				logException(e);
			}
		}
		return jButtonSearch;
	}

	private javax.swing.JComboBox getJComboBoxProdLot() {
		if (jComboBoxProdctionLot == null) {
			try {
				jComboBoxProdctionLot = new javax.swing.JComboBox();
				jComboBoxProdctionLot.setName("JComboBoxProdctionLot");
				jComboBoxProdctionLot.setFont(new java.awt.Font("dialog", 0, 14));
				jComboBoxProdctionLot.setBackground(java.awt.Color.white);
				jComboBoxProdctionLot.setBounds(442, 70, 300, 30);
				jComboBoxProdctionLot.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jComboBoxProdLotActionPerformed(e, getJComboBoxProdLot().getSelectedIndex());
					}
				});
			} catch (Exception e) {
				logException(e);
			}
		}
		return jComboBoxProdctionLot;
	}
	
	private JPanel getLastProcPointPanel() {
		if (this.jPanelLastProcPoint == null) {
			this.jPanelLastProcPoint = new JPanel(new MigLayout());
			this.jPanelLastProcPoint.add(this.getLastProcPointIdField(), "wrap, al right, wrap");
			this.jPanelLastProcPoint.add(this.getLastProcPointNameField(), "wrap, al right, wrap");
			this.jPanelLastProcPoint.add(this.getLastProcPointDivField(), "wrap, al right, wrap");
			this.jPanelLastProcPoint.add(this.getLastProcPointTSField(), "wrap, al right");
			this.jPanelLastProcPoint.setBounds(750, 61, 200, 243);
			this.jPanelLastProcPoint.setAlignmentY(CENTER_ALIGNMENT);
			TitledBorder border = new TitledBorder("Last Process Point");
			border.setTitleFont(new java.awt.Font("dialog", Font.BOLD, 14));
			border.setTitleJustification(TitledBorder.CENTER);
			this.jPanelLastProcPoint.setBorder(border);
		}
		return this.jPanelLastProcPoint;
	}
	
	private LabeledTextField getLastProcPointIdField() {
		if (this.lastProcPointID == null) {
			this.lastProcPointID = this.getLabeledTextField("Process Point ID:");
		}
		return this.lastProcPointID;
	}
	
	private LabeledTextField getLastProcPointNameField() {
		if (this.lastProcPointName == null) {
			this.lastProcPointName = this.getLabeledTextField("Process Point Name:");
		}
		return this.lastProcPointName;
	}
	
	private LabeledTextField getLastProcPointDivField() {
		if (this.lastProcPointDiv == null) {
			this.lastProcPointDiv = this.getLabeledTextField("Division Name:");
		}
		return this.lastProcPointDiv;
	}
	
	private LabeledTextField getLastProcPointLineField() {
		if (this.lastProcPointLine == null) {
			this.lastProcPointLine = this.getLabeledTextField("Line:");
		}
		return this.lastProcPointLine;
	}
	
	private LabeledTextField getLastProcPointTSField() {
		if (this.lastProcPointTS == null) {
			this.lastProcPointTS = this.getLabeledTextField("Timestamp:");
		}
		return this.lastProcPointTS;
	}
	
	private LabeledTextField getLabeledTextField(String name) {
		LabeledTextField textField = new LabeledTextField(name, false);
		textField.getComponent().setEditable(false);
		textField.getComponent().setBackground(this.getBackground());
		textField.setFont(new java.awt.Font("dialog", 0, 14));
    	textField.getComponent().setHorizontalAlignment(JTextField.CENTER);
		textField.setBorder(null);
    	((BorderLayout)textField.getLayout()).setVgap(0);
		return textField;
	}

	private javax.swing.JTextField getJTextFieldProductID() {
		if (jTextFieldProductId == null) {
			try {
				jTextFieldProductId = new javax.swing.JTextField();
				jTextFieldProductId.setName("JTextFieldProductID");
				jTextFieldProductId.setFont(new java.awt.Font("dialog", 0, 14));
				jTextFieldProductId.setBackground(java.awt.Color.white);
				jTextFieldProductId.setBounds(442, 140, 300, 30);
				jTextFieldProductId.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jTextFieldProductIdActionPerformed(e, getJTextFieldProductID().getText());
					}
				});
			} catch (Exception e) {
				logException(e);
			}
		}
		return jTextFieldProductId;
	}
	
	private javax.swing.JComboBox getJComboBoxProductID() {
		if (jComboBoxProductId == null) {
			try {
				jComboBoxProductId = new javax.swing.JComboBox();
				jComboBoxProductId.setName("JComboBoxProductID");
				jComboBoxProductId.setFont(new java.awt.Font("dialog", 0, 14));
				jComboBoxProductId.setBackground(java.awt.Color.white);
				jComboBoxProductId.setBounds(442, 110, 300, 30);
				jComboBoxProductId.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jComboBoxProductIdActionPerformed(e, getJComboBoxProductID().getSelectedIndex());
					}
				});
			} catch (Exception e) {
				logException(e);
			}
		}
		return jComboBoxProductId;
	}

	private javax.swing.JLabel getJLabelTitle() {
		if (jLabelTitle == null) {
			try {
				jLabelTitle = new javax.swing.JLabel();
				jLabelTitle.setName("jLabelTitle");
				jLabelTitle.setText("Exceptional Outgoing Panel");
				jLabelTitle.setForeground(java.awt.Color.black);
				jLabelTitle.setPreferredSize(new java.awt.Dimension(250, 60));
				jLabelTitle.setBounds(270, 5, 755, 30);
				jLabelTitle.setFont(new java.awt.Font("Arial", 1, 24));
				jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelTitle;
	}

	private javax.swing.JLabel getJLabelProdLot() {
		if (jLabelProdLot == null) {
			try {
				jLabelProdLot = new javax.swing.JLabel();
				jLabelProdLot.setName("jLabelProdLot");
				jLabelProdLot.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelProdLot.setText("Production Lot :");
				jLabelProdLot.setBounds(312, 70, 130, 30);
				jLabelProdLot.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelProdLot;
	}

	private javax.swing.JLabel getJLabelTrackingStatus() {
		if (jLabelTrackingStatus == null) {
			try {
				jLabelTrackingStatus = new javax.swing.JLabel();
				jLabelTrackingStatus.setName("jLabelTrackingStatus");
				jLabelTrackingStatus.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelTrackingStatus.setText("Tracking Status :");
				jLabelTrackingStatus.setBounds(312, 270, 130, 30);
				jLabelTrackingStatus.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelTrackingStatus;
	}

	private javax.swing.JLabel getJLabelEngineType() {
		if (jLabelEngineType == null) {
			try {
				jLabelEngineType = new javax.swing.JLabel();
				jLabelEngineType.setName("jLabelEngineType");
				jLabelEngineType.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelEngineType.setText("Engine Type :");
				jLabelEngineType.setBounds(312, 230, 130, 30);
				jLabelEngineType.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelEngineType;
	}
	
	private javax.swing.JLabel getJLabelKDLot() {
		if (jLabelKDLot == null) {
			try {
				jLabelKDLot = new javax.swing.JLabel();
				jLabelKDLot.setName("jLabelKDLot");
				jLabelKDLot.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelKDLot.setText("KD Lot :");
				jLabelKDLot.setBounds(312, 150, 130, 30);
				jLabelKDLot.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelKDLot;
	}

	private javax.swing.JLabel getJLabelMtoc() {
		if (jLabelMtoc == null) {
			try {
				jLabelMtoc = new javax.swing.JLabel();
				jLabelMtoc.setName("jLabelMtoc");
				jLabelMtoc.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelMtoc.setText("MTOC :");
				jLabelMtoc.setBounds(312, 190, 130, 30);
				jLabelMtoc.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelMtoc;
	}
	
	private javax.swing.JLabel getJLabelReason() {
		if (jLabelReason == null) {
			try {
				jLabelReason = new javax.swing.JLabel();
				jLabelReason.setName("jLabelReason");
				jLabelReason.setFont(new java.awt.Font("dialog", 0, 14));
				jLabelReason.setText("Reason :");
				jLabelReason.setBounds(312, 350, 130, 30);
				jLabelReason.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jLabelReason;
	}

	private javax.swing.JPanel getJPanel() {
		if (jPanel == null) {
			try {
				jPanel = new javax.swing.JPanel();
				jPanel.setName("JPanel");
				jPanel.setLayout(null);
				getJPanel().add(this.getLastProcPointPanel());
				getJPanel().add(getJLabelTitle(), getJLabelTitle().getName());
				getJPanel().add(getJLabelProdLot(),getJLabelProdLot().getName());
				getJPanel().add(getJLabelTrackingStatus(),getJLabelTrackingStatus().getName());
				getJPanel().add(getJLabelEngineType(),getJLabelEngineType().getName());
				getJPanel().add(getJLabelKDLot(), getJLabelKDLot().getName());
				getJPanel().add(getJLabelMtoc(), getJLabelMtoc().getName());
				getJPanel().add(getJRadioButtonScrap(),getJRadioButtonScrap().getName());
				getJPanel().add(getJRadioButtonExceptional(),getJRadioButtonExceptional().getName());
				getJPanel().add(getJLabelReason(), getJLabelReason().getName());
				getJPanel().add(getJComboBoxProdLot(),getJComboBoxProdLot().getName());
				getJPanel().add(getJComboBoxProductID(),getJComboBoxProductID().getName());
				getJPanel().add(getJButtonSearch(), getJButtonSearch().getName());
				getJPanel().add(getJButtonDone(), getJButtonDone().getName());
				getJPanel().add(getJTextFieldKDLot(),getJTextFieldKDLot().getName());
				getJPanel().add(getJTextFieldMTOC(),getJTextFieldMTOC().getName());
				getJPanel().add(getJTextFieldEngineType(),getJTextFieldEngineType().getName());
				getJPanel().add(getJTextFieldTrackingStatus(),getJTextFieldTrackingStatus().getName());
				getJPanel().add(getReasonLengthFieldBean(),getReasonLengthFieldBean().getName());
			} catch (Exception e) {
				logException(e);
			}
		}
		return jPanel;
	}

	private javax.swing.JRadioButton getJRadioButtonExceptional() {
		if (jRadioButtonExceptional == null) {
			try {
				jRadioButtonExceptional = new javax.swing.JRadioButton();
				jRadioButtonExceptional.setName("JRadioButtonExceptional");
				jRadioButtonExceptional.setFont(new java.awt.Font("dialog", 0,14));
				jRadioButtonExceptional.setText("Exceptional");
				jRadioButtonExceptional.setBounds(682, 310, 130, 36);
				ButtonGroup group = new ButtonGroup();
				group.add(jRadioButtonExceptional);
				group.add(getJRadioButtonScrap());
			} catch (Exception e) {
				logException(e);
			}
		}
		return jRadioButtonExceptional;
	}

	private javax.swing.JRadioButton getJRadioButtonScrap() {
		if (jRadioButtonScrap == null) {
			try {
				jRadioButtonScrap = new javax.swing.JRadioButton();
				jRadioButtonScrap.setName("JRadioButtonScrap");
				jRadioButtonScrap.setSelected(true);
				jRadioButtonScrap.setFont(new java.awt.Font("dialog", 0, 14));
				jRadioButtonScrap.setText("Scrap");
				jRadioButtonScrap.setBounds(552, 310, 130, 36);
			} catch (Exception e) {
				logException(e);
			}
		}
		return jRadioButtonScrap;
	}

	private javax.swing.JTextField getJTextFieldEngineType() {
		if (jTextFieldEngineType == null) {
			try {
				jTextFieldEngineType = new javax.swing.JTextField();
				jTextFieldEngineType.setName("JTextFieldEngineType");
				jTextFieldEngineType.setFont(new java.awt.Font("dialog", 0, 14));
				jTextFieldEngineType.setBounds(442, 230, 300, 30);
				jTextFieldEngineType.setEditable(false);
				jTextFieldEngineType.setMargin(new java.awt.Insets(0, 2, 0, 0));
			} catch (Exception e) {
				logException(e);
			}
		}
		return jTextFieldEngineType;
	}

	private javax.swing.JTextField getJTextFieldKDLot() {
		if (jTextFieldKDLot == null) {
			jTextFieldKDLot = new javax.swing.JTextField();
			jTextFieldKDLot.setName("jTextFieldKDLot");
			jTextFieldKDLot.setText("");
			jTextFieldKDLot.setFont(new java.awt.Font("dialog", 0, 14));
			jTextFieldKDLot.setBounds(442, 150, 300, 30);
			jTextFieldKDLot.setEnabled(true);
			jTextFieldKDLot.setEditable(false);
			jTextFieldKDLot.setMargin(new java.awt.Insets(0, 2, 0, 0));
		}
		return jTextFieldKDLot;
	}
	
	private javax.swing.JTextField getJTextFieldMTOC() {
		if (jTextFieldMTOC == null) {
			try {
				jTextFieldMTOC = new javax.swing.JTextField();
				jTextFieldMTOC.setName("JTextFieldMTOC");
				jTextFieldMTOC.setText("");
				jTextFieldMTOC.setFont(new java.awt.Font("dialog", 0, 14));
				jTextFieldMTOC.setBounds(442, 190, 300, 30);
				jTextFieldMTOC.setEnabled(true);
				jTextFieldMTOC.setEditable(false);
				jTextFieldMTOC.setMargin(new java.awt.Insets(0, 2, 0, 0));
			} catch (Exception e) {
				logException(e);
			}
		}
		return jTextFieldMTOC;
	}
	
	private javax.swing.JTextField getJTextFieldTrackingStatus() {
		if (jTextFieldTrackingStatus == null) {
			try {
				jTextFieldTrackingStatus = new javax.swing.JTextField();
				jTextFieldTrackingStatus.setName("JTextFieldTrackingStatus");
				jTextFieldTrackingStatus.setFont(new java.awt.Font("dialog", 0,14));
				jTextFieldTrackingStatus.setBounds(442, 270, 300, 30);
				jTextFieldTrackingStatus.setEditable(false);
				jTextFieldTrackingStatus.setMargin(new java.awt.Insets(0, 2, 0,0));
			} catch (Exception e) {
				logException(e);
			}
		}
		return jTextFieldTrackingStatus;
	}

	private LengthFieldBean getReasonLengthFieldBean() {
		if (reasonLengthFieldBean == null) {
			try {
				reasonLengthFieldBean = new LengthFieldBean();
				reasonLengthFieldBean.setName("ReasonLengthFieldBean");
				reasonLengthFieldBean.setFont(new java.awt.Font("dialog", 0, 14));
				reasonLengthFieldBean.setBounds(442, 350, 508, 30);
				reasonLengthFieldBean.setFixedLength(false);
				reasonLengthFieldBean.setMargin(new java.awt.Insets(0, 2, 0, 0));
				reasonLengthFieldBean.setMaximumLength(240);
			} catch (Exception e) {
				logException(e);
			}
		}
		return reasonLengthFieldBean;
	}

	public void logException(Exception e) {
		Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the Exceptional Outgoing Panel screen");
		e.printStackTrace();
		setErrorMessage("An error Occurred while processing the Exceptional Outgoing Panel screen");
	}

	private void initConnections() {
		getJComboBoxProdLot().addActionListener(this);
		getJComboBoxProductID().addActionListener(this);
		getJButtonDone().addActionListener(this);
		productTypeName = PropertyService.getProperty(getProcessPointId(), "PRODUCT_TYPE");
		loadProductionLots();
		
	}
	public void loadProductionLots(){
		try {
			setErrorMessage("");
			initPanel();
			updateDisplay();
			if(!hasProductionLot()) {
				return;
			}
			
			String processLocation;
			switch(ProductType.getType(getSelectedProductType())) {
			case FRAME : 
				processLocation = "AF";
				break;
			case ENGINE :
				processLocation = "AE";
				break;
			case MISSION :
				processLocation = "AM";
				break;
			default:
				processLocation = "";
			}
			
			if (ProductType.MBPN.name().equals(getSelectedProductType())) {
				String[] procLoc = PropertyService.getPropertyBean(ApplicationPropertyBean.class, this.applicationId).getMbpnProcessLocations().split(",");
				productionLotsList = new ArrayList<PreProductionLot>();
				for(String aProcLoc : procLoc) 
					productionLotsList.addAll(getDao(PreProductionLotDao.class).findAllSortedLotsByProcessLocation(aProcLoc, false));
			}
			else 
				productionLotsList = getDao(PreProductionLotDao.class).findAllSortedLotsByProcessLocation(processLocation, false);
				
			Vector<String> productionLotsVector = new Vector<String>();
			for (PreProductionLot lot : productionLotsList) {
				productionLotsVector.add(lot.getProductionLot());
			}
			getJComboBoxProdLot().setModel(new DefaultComboBoxModel(productionLotsVector));
			getJComboBoxProdLot().setSelectedIndex(-1);
		} catch (Exception e) {
			logException(e);
		}
	
	}
	private void initialize() {
		try {
			setSize(1024, 768);
			setLayout(new BorderLayout());
			add(getJPanel(), BorderLayout.CENTER);
			initConnections();
			initializeProperties();
			handleAccessRestrictions();
		} catch (Exception e) {
			logException(e);
		}
	}
	
	private void handleAccessRestrictions() {
		String accessReq = getAccessRequested();
		if(StringUtils.isNotEmpty(accessReq)) {
			if(StringUtils.equalsIgnoreCase(accessReq.trim(), IS_SCRAP)) {
				getJRadioButtonExceptional().setEnabled(false);
				getJRadioButtonScrap().setEnabled(true);
				getJRadioButtonExceptional().setSelected(false);
				getJRadioButtonScrap().setSelected(true);
			}else if(StringUtils.equalsIgnoreCase(accessReq.trim(), IS_EXCEPTIONAL_OUTGOING )){
				getJRadioButtonExceptional().setEnabled(true);
				getJRadioButtonScrap().setEnabled(false);
				getJRadioButtonExceptional().setSelected(true);
				getJRadioButtonScrap().setSelected(false);
			}else {
				getJRadioButtonExceptional().setEnabled(true);
				getJRadioButtonScrap().setEnabled(true);
			}
		}
	}

	private void initializeProperties() {
		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByProcessPointType(ProcessPointType.Scrap);
		if(processPoints.size() > 0) {
			ProcessPoint pp = processPoints.get(0);
			setScrapLineId(pp.getLineId());
			setScrapPPId(pp.getProcessPointId());
		} else {
			MessageDialog.showError(this,"Cannot find scrap process point. Please setup process point type in process point configurator.","Error");
		}

		processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByProcessPointType(ProcessPointType.ExceptionalOut);
		if(processPoints.size() > 0) {
			ProcessPoint pp = processPoints.get(0);
			setExceptionLineId(pp.getLineId());
			setExceptionPPId(pp.getProcessPointId());
		} else {
			MessageDialog.showError(this,"Cannot find exceptional out process point. Please setup process point type in process point configurator.","Error");
		}
	}

	public void initPanel() {
		this.getJButtonDone().setEnabled(false);
		this.getJTextFieldEngineType().setText("");
		this.getJTextFieldMTOC().setText("");
		this.getJTextFieldKDLot().setText("");
		this.getJTextFieldTrackingStatus().setText("");
		this.getLastProcPointIdField().getComponent().setText("");
		this.getLastProcPointNameField().getComponent().setText("");
		this.getLastProcPointDivField().getComponent().setText("");
		this.getLastProcPointLineField().getComponent().setText("");
		this.getLastProcPointTSField().getComponent().setText("");
	}
	
	private void updateDisplay() {
		if(hasProductionLot()) {
			getJComboBoxProdLot().setVisible(true);
			getJLabelProdLot().setVisible(true);
			if(getJComboBoxProductID().getParent() == null) {
				getJPanel().remove(getJTextFieldProductID());
				getJPanel().add(getJComboBoxProductID());
			}
			getJComboBoxProdLot().removeAllItems();
			getJComboBoxProductID().removeAllItems();
			return;
		}

		getJComboBoxProdLot().setVisible(false);
		getJLabelProdLot().setVisible(false);
		if(getJTextFieldProductID().getParent() == null) {
			getJPanel().remove(getJComboBoxProductID());
			getJPanel().add(getJTextFieldProductID());
		}
		getJTextFieldProductID().setText("");
	}

	private boolean hasProductionLot() {
		String productType = getSelectedProductType();
		return ProductType.FRAME.name().equals(productType) || ProductType.ENGINE.name().equals(productType) 
				|| ProductType.MISSION.name().equals(productType) || ProductType.MBPN.name().equals(productType);
	}
	
	public void jComboBoxProdLotActionPerformed(ActionEvent actionEvent, int aSelectedIndex) {
		try {
			setErrorMessage("");
			initPanel();
			if (aSelectedIndex < 0)
				return;
			List<Object[]> list = null;
			productsVec = new Vector();

			switch(ProductType.getType(getSelectedProductType())) {
			case FRAME : 
				list = getDao(FrameDao.class).getFrameLineMtoData(productionLotsList.get(aSelectedIndex).getProductionLot(), getScrapLineId(), getExceptionLineId());
				for (Object[] product : list) {
					Vector rowData = new Vector();
					rowData.addElement(product[0]);
					rowData.addElement(product[1]);
					rowData.addElement(product[2]);
					rowData.addElement(product[3]);
					rowData.addElement(product[4]);
					rowData.addElement(product[5]);
					rowData.addElement(product[6]);
					productsVec.add(rowData);
				}
				break;
			case ENGINE :
				list = getDao(EngineDao.class).getEngineLineShipmentStatusData(productionLotsList.get(aSelectedIndex).getProductionLot(), getScrapLineId(), getExceptionLineId());
				for (Object[] product : list) {
					Vector rowData = new Vector();
					rowData.addElement(product[0]);
					rowData.addElement(product[1]);
					rowData.addElement(product[2]);
					rowData.addElement(product[3]);
					rowData.addElement(product[4]);
					rowData.addElement(product[5]);
					productsVec.add(rowData);
				}
				break;
			case MISSION :
				List<Mission> missions = getDao(MissionDao.class).findAllByProductionLot(productionLotsList.get(aSelectedIndex).getProductionLot());
				for(Mission aMission : missions) {
					if(!DefectStatus.SCRAP.equals(aMission.getDefectStatus())) {
						Vector rowData = new Vector();
						rowData.addElement(aMission.getProductId());
						rowData.addElement(aMission.getProductSpecCode());
						rowData.addElement(aMission.getTrackingStatus());
						rowData.addElement(aMission.getKdLotNumber());
						rowData.addElement(aMission.getLastPassingProcessPointId());
						productsVec.add(rowData);
					}
				}
				break;
			case MBPN :
				List<MbpnProduct> mbpns = getDao(MbpnProductDao.class).findAllByProductionLot(productionLotsList.get(aSelectedIndex).getProductionLot());
				for(MbpnProduct aMbpn : mbpns) {
					if(!DefectStatus.SCRAP.equals(aMbpn.getDefectStatus())) {
						Vector rowData = new Vector();
						rowData.addElement(aMbpn.getProductId());
						rowData.addElement(aMbpn.getProductSpecCode());
						rowData.addElement(aMbpn.getTrackingStatus());
						rowData.addElement(aMbpn.getCurrentOrderNo());
						rowData.addElement(aMbpn.getLastPassingProcessPointId());
						productsVec.add(rowData);
					}
				}
				break;
			default:
			}

			getJComboBoxProductID().removeAllItems();
			for (int i = 0; i < productsVec.size(); i++) {
				getJComboBoxProductID().addItem((String) productsVec.get(i).get(0));
			}
			getJComboBoxProductID().setSelectedIndex(-1);
			getReasonLengthFieldBean().setText("");
		} catch (Exception e) {
			logException(e);
		}
	}

	public void jComboBoxProductIdActionPerformed(ActionEvent actionEvent, int aSelectedIndex) {
		try {
			setErrorMessage("");
			initPanel();
			if (aSelectedIndex < 0)
				return;
			String kdLot = "";
			String mtoc = "";
			String engineType = "";
			String trackingStatus = "";
			String lastProcPointId = "";
			Object status = null;
			if (0 < productsVec.size()) {
				switch(ProductType.getType(getSelectedProductType())) {
				case FRAME : 
					mtoc = (String) productsVec.get(aSelectedIndex).get(1);
					engineType = (String) productsVec.get(aSelectedIndex).get(2);
					trackingStatus = (String) productsVec.get(aSelectedIndex).get(3);
					status = productsVec.get(aSelectedIndex).get(4);
					kdLot = (String) productsVec.get(aSelectedIndex).get(5);
					lastProcPointId = (String) productsVec.get(aSelectedIndex).get(6);
					setShippingStatus(status);
					break;
				case ENGINE :
					engineType = (String) productsVec.get(aSelectedIndex).get(1);
					trackingStatus = (String) productsVec.get(aSelectedIndex).get(2);
					kdLot = (String) productsVec.get(aSelectedIndex).get(4);
					lastProcPointId = (String) productsVec.get(aSelectedIndex).get(5);
					break;
				case MISSION :
				case MBPN :
					mtoc = (String) productsVec.get(aSelectedIndex).get(1);
					String ts = (String) productsVec.get(aSelectedIndex).get(2);
					if (ts==null) ts = "";
					trackingStatus = ts;
					kdLot = (String) productsVec.get(aSelectedIndex).get(3);
					String lastppid = (String) productsVec.get(aSelectedIndex).get(4);
					if (lastppid==null) lastppid = "";
					lastProcPointId = lastppid;
					break;
				default:
				}
				getJButtonDone().setEnabled(true);
			}
			getJTextFieldMTOC().setText(mtoc);
			getJTextFieldKDLot().setText(kdLot);
			getJTextFieldEngineType().setText(engineType);
			getJTextFieldTrackingStatus().setText(trackingStatus);
			this.populateLastProcPointPanel(this.getProductId(), lastProcPointId);
		} catch (Exception e) {
			logException(e);
		}
	}
	
	public void populateLastProcPointPanel(String productId, String lastProcPointId) {
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(lastProcPointId);
		if (processPoint == null) {
			this.setErrorMessage("Process point [" + lastProcPointId.trim() + "] does not exist.");
			return;
		}
		List <ProductResult> productResults;
		this.lastProcPointID.getComponent().setText(processPoint.getId());
		this.lastProcPointName.getComponent().setText(processPoint.getProcessPointName());
		this.lastProcPointDiv.getComponent().setText(processPoint.getDivisionName());
		productResults = ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(productId, processPoint.getId());
		if (!productResults.isEmpty()){
			Timestamp timestamp = null;
			for (ProductResult productResult : productResults){
				timestamp = productResult.getActualTimestamp();
			}
			if (timestamp != null) this.lastProcPointTS.getComponent().setText(timestamp.toString());
		}else {
			this.setMessage("Product result record for product " + productId.trim() + " at process point " + lastProcPointId.trim() + " is missing.");
		}
	}
	
	public void jTextFieldProductIdActionPerformed(ActionEvent actionEvent, String productId) {
		try {
			setErrorMessage("");
			initPanel();
			if (productId == null || productId.trim().length() == 0) {
				return;
			}
			
			BaseProduct product = ProductTypeUtil.findProduct(getSelectedProductType(), productId);
			getJTextFieldMTOC().setText(product.getProductSpecCode());
			getJTextFieldEngineType().setText("");
			getJTextFieldTrackingStatus().setText(product.getTrackingStatus());
			getJButtonDone().setEnabled(true);
		} catch (Exception e) {
			logException(e);
		}
	}
	
	public void jButtonSearchActionPerformed() {
		ProductType productType = ProductType.getType(getSelectedProductType());
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getMainWindow(), productType.toString(), ProductNumberDef.getProductNumberDef(productType).get(0).getName());
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		String productId = manualProductEntry.getResultProductId();
		if (StringUtils.isBlank(productId)) return;
		
		ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(productType.toString());
		
		switch(productType) {
		case FRAME :
			Frame frame = getDao(FrameDao.class).findBySn(productId);
			if (frame == null) {
				this.setErrorMessage("Frame [" + productId + "] does not exist.");
			} else {
				PreProductionLot prodLot = getDao(PreProductionLotDao.class).findByKey(frame.getProductionLot());
				if (prodLot == null) {
					this.setErrorMessage("Production lot [" + frame.getProductionLot() + "] does not exist");
					break;
				}
				this.jComboBoxProdctionLot.setSelectedItem(prodLot.getProductionLot());
				this.jComboBoxProductId.setSelectedItem(frame.getProductId());
			} break;
		case ENGINE :
			Engine engine = getDao(EngineDao.class).findBySn(productId);
			this.jComboBoxProdctionLot.setSelectedItem(engine.getProductionLot());
			break;
		case MISSION :
			Mission mission = getDao(MissionDao.class).findBySn(productId);
			this.jComboBoxProdctionLot.setSelectedItem(mission.getProductionLot());
			break;
		case MBPN :
			MbpnProduct mbpn = getDao(MbpnProductDao.class).findBySn(productId);
			this.jComboBoxProdctionLot.setSelectedItem(mbpn.getProductionLot());
			break;
		default:
		}
	}
	
	public void jButtonDoneActionPerformed(ActionEvent actionEvent, boolean isScrap, String aReason) {
		try {
			setErrorMessage("");
			if (aReason.length() < 1) {
				Logger.getLogger(this.getApplicationId()).error("Enter the Input Reason");
				MessageDialog.showError(this, "Enter the Input Reason","Error Exceptional Outgoing");				
				return;
			}
			
			// This code is added to check the valid tracking point for both scrap and exceptional out
			BaseProduct product = ProductTypeUtil.findProduct((String) productTypeName, getProductId());
			String trackingStatus= product.getTrackingStatus()==null?"":product.getTrackingStatus().trim();
			if (isScrap) {
				if (isScrapApplicable(trackingStatus)) {
					performAction(isScrap,  aReason);
				} else {
					String screenTitle = "Scrap Not Allowed ( " + getProductId() + " )";
					String scrapErrorMessage = "Scrap is not allowed for the type "+ getSelectedProductType() + " with tracking status "+ product.getTrackingStatus();
					Logger.getLogger(this.getApplicationId()).error(scrapErrorMessage);
					MessageDialog.showError(this, scrapErrorMessage,screenTitle);
					return;
				}
			} else {
				if(isExceptionalOutApplicable(trackingStatus)){
					performAction( isScrap,  aReason);
				} else {
					String screenTitle = "ExceptionalOut Not Allowed ( " + getProductId() + " )";
					String scrapErrorMessage = "ExceptionalOut is not allowed for the type "+ getSelectedProductType() + " with tracking status "+ product.getTrackingStatus();
					Logger.getLogger(this.getApplicationId()).error(scrapErrorMessage);
					MessageDialog.showError(this, scrapErrorMessage,screenTitle);
					return;
				}
			}
		} catch (Exception e) {
			logException(e);
		}
	}
	
	private boolean isScrapApplicable(String trackingStatus){
		String productType = getSelectedProductType();
		if((ProductType.FRAME.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(FrameLinePropertyBean.class).getValidLinesToScrapFrom())) || 
				(ProductType.ENGINE.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(EngineLinePropertyBean.class).getValidLinesToScrapFrom())) ||
					(ProductType.MISSION.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(MissionLinePropertyBean.class).getValidLinesToScrapFrom())) ||
						ProductType.MBPN.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(MbpnLinePropertyBean.class).getValidLinesToScrapFrom())){
			return true;
		}
		return false;
	}
	
	private boolean isExceptionalOutApplicable(String trackingStatus){
		String productType = getSelectedProductType();
		if((ProductType.FRAME.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(FrameLinePropertyBean.class).getValidLinesToExceptionalOutgoingFrom())) || 
				(ProductType.ENGINE.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(EngineLinePropertyBean.class).getValidLinesToExceptionalOutgoingFrom())) ||
					(ProductType.MISSION.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(MissionLinePropertyBean.class).getValidLinesToExceptionalOutgoingFrom())) ||
						ProductType.MBPN.name().equals(productType) && isValidTrackingStatus(trackingStatus, PropertyService.getPropertyBean(MbpnLinePropertyBean.class).getValidLinesToExceptionalOutgoingFrom())){
			return true;
		}
		return false;
	}
	
	private void performAction(boolean isScrap, String aReason){
		String message = null;
		BaseProduct product = ProductTypeUtil.findProduct((String) productTypeName, getProductId());
		if (isScrap) {
			message = "Really Scrap? ";
			aReason = "SCRAP : " + aReason;
		} else {
			message = "Really Exceptional? ";
			aReason = "EXCEPTIONAL : " + aReason;
		}
		
		if (!StringUtils.equalsIgnoreCase(ProductType.MBPN.name(), getSelectedProductType())) {
			if (isOffProcessMissing(getProductId())) {
				String errorMessage = "This product did not pass Off process!!!";
				setErrorMessage(errorMessage);
				message = errorMessage + "\n" + message;
			}
		}
		
		if (!MessageDialog.confirm(this, message + getProductId())) {
			setErrorMessage("");
			return;
		}

		setErrorMessage("");
		if ("4".equals(getShippingStatus())) {
			MessageDialog.showError(this,"Cannot update the shipping status table because the VIN has been shipment confirmed.  Please contact Product Control(PC) to resolve this issue.","Error Exceptional Outgoing");
			return;
		}
		
		if (isScrap) {
			if (!StringUtils.equalsIgnoreCase(ProductType.MISSION.name(), getSelectedProductType()) && isPartAssigned(product) && 
					!MessageDialog.confirm(this, "On continue, it will de-assign all the installed parts/engine assigned and delete all measurements for "+ ProductNumberDef.getProductNumberDef(getProduct(getProductId()).getProductType()).get(0).getName() +" "+getProductId()+
							"\nYes: De-assign all the installed parts/engine, then do scrap.\nNo: Go back to main menu without doing scrap.")) {
				return;
			}
			
			doScrap(getProductId(), aReason);
		} else {
			doExceptionalOutgoing(getProductId(), aReason);
		}
		
		if (PropertyService.getPropertyBoolean(applicationId, IS_BROADCAST_ENABLED, false)) {
			BaseProduct baseProduct = getProduct(getProductId());
			if (baseProduct != null){
				loadBroadcastDestinations();
				if (!this.broadcastDestinations.isEmpty()) {
					for (BroadcastDestination destination : this.broadcastDestinations) {
						int seqNo = destination.getSequenceNumber();
						Logger.getLogger(applicationId).info("Broadcast Services started for " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")");
						invokeBroadcastService(baseProduct,seqNo,aReason);
					}
				}
			}
		}
		
		getJButtonDone().setEnabled(false);
		jComboBoxProdLotActionPerformed(null, getJComboBoxProdLot().getSelectedIndex());
		getJTextFieldProductID().setText("");
		getReasonLengthFieldBean().setText("");
		initPanel();
	}
	
	public String getProductId() {
		String productId;
		if(hasProductionLot()) {
			productId = (String) getJComboBoxProductID().getSelectedItem();
		} else {
			productId = getJTextFieldProductID().getText();
		}
		return productId;
	}
	
	private boolean isOffProcessMissing(String productId) {
		boolean isMissing = true;
		BaseProduct product = ProductTypeUtil.findProduct((String) productTypeName, productId);
		Line line = ServiceFactory.getDao(LineDao.class).findByKey(product.getTrackingStatus());
		Division division = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(line.getDivisionId());

		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByDivision(division);
		List<String> offProcessPointIds = new ArrayList<String>();
		for(ProcessPoint pp : processPoints) {
			if(ProcessPointType.Off.equals(pp.getProcessPointType()) || ProcessPointType.ProductOff.equals(pp.getProcessPointType())
					|| ProcessPointType.OffQics.equals(pp.getProcessPointType())) {
				offProcessPointIds.add(pp.getProcessPointId());
			}
		}
		
		if(offProcessPointIds.isEmpty()) {
			isMissing = false;
		} else {
			List<ProductResult> results = ServiceFactory.getDao(ProductResultDao.class).findAllByProductId(productId); 
			for(ProductResult result : results) {
				for(String anId : offProcessPointIds) {
					if(anId.equals(result.getProcessPointId())) {
						isMissing = false;
					}
				}
			}
		}
		return isMissing;
	}
	
	private void doScrap(String productId, String reason) {
		BaseProduct product = ProductTypeUtil.findProduct((String) productTypeName, productId);
		if (StringUtils.equalsIgnoreCase(ProductType.MISSION.name(), getSelectedProductType()) && isPartAssigned(product)) {
			String screenTitle = "Scrap Not Allowed ( " + productId + " )";
			String scrapErrorMessage = "Scrap is not allowed for vin "+ productId + " until all parts/engine are de-assigned";
			Logger.getLogger(this.getApplicationId()).error(scrapErrorMessage);
			MessageDialog.showError(this, scrapErrorMessage,screenTitle);					
			return;
		}
		
		// This method is added to deassign Engine/Frame and remove installed parts
		removeAllParts(product, productId);
		performScrap(productId, reason);
	}
	
	private void doExceptionalOutgoing(String productId, String reason) {
		performException(productId, reason);
	}
	
	private void doException(String productId) {
		
	}

	private boolean isPartAssigned(BaseProduct product) {
		boolean partAssigned = false;
		List<InstalledPart> installedPartList = getDao(InstalledPartDao.class).findAllByProductId(product.getProductId());
		for (InstalledPart part : installedPartList) {
			if (part.getPartSerialNumber() != null&& !part.getPartSerialNumber().trim().equals("")) {
				partAssigned = true;
			}
		}
		return partAssigned;
	}

	private boolean checkPartAssigned(String vin) {
		boolean partAssigned = false;
		List<InstalledPart> installedPartList = getDao(InstalledPartDao.class).findAllByProductId(vin);
		for (InstalledPart part : installedPartList) {
			if (part.getPartSerialNumber() != null&& !part.getPartSerialNumber().trim().equals("")) {
				partAssigned = true;
			}
		}
		return partAssigned;
	}

	private boolean checkEngineMissionAssigned(String vin) {
		boolean engineOrMissionAssigned = false;
		Frame frame = getDao(FrameDao.class).findByKey(vin);
		if (frame != null && frame.getEngineStatus() != null && frame.getEngineStatus() == 1) {
			engineOrMissionAssigned = true;
		}
		return engineOrMissionAssigned;
	}
	
	private void performScrap(String productId, String reason) {
		performExceptionalOut(productId, getScrapPPId(), getScrapLineId(), reason);
	}

	private void performException(String productId, String reason) {
		performExceptionalOut(productId, getExceptionPPId(), getExceptionLineId(), reason);
	}

	private void performExceptionalOut(String productId, String processPointId, String lineId, String reason) {
		String productType = getSelectedProductType();
		Date date = null;

		try {
			BaseProduct product = getProduct(productId);
			if(ProductType.FRAME.name().equals(productType) || ProductType.ENGINE.name().equals(productType) 
					|| ProductType.MISSION.name().equals(getSelectedProductType())) {
				date = ((Product) product).getProductionDate();
			} else if (ProductType.MBPN.name().equals(productType)) {
				date = new Date();
			}
				
			updateExpectedProduct(productId, getNextProductId(productId));
			// Remove productid from QICS Parking Lot when scrapped
			if(ProductType.FRAME.name().equals(productType)) {
				QiClearParking clearParked = ServiceFactory.getService(QiClearParking.class);
				clearParked.removeVinFromQicsParking(productId); 
			}
			removeInRepairArea(productId);
			updateShippingStatus(productId, lineId);
 			
			createExceptionalOut(productId, reason, date, product.getLastPassingProcessPointId());
			
			product.setDefectStatus(DefectStatus.SCRAP);
			((ProductDao) ProductTypeUtil.getProductDao(productType)).save(product);
			logUserAction(SAVED, product);
			
			getTrackingService().track(product, processPointId);
		} catch (Exception e) {
			logException(e);
		}
	}

	private BaseProduct getProduct(String productId) {
		BaseProduct product = ProductTypeUtil.findProduct(getSelectedProductType(), productId);
		if (ProductType.FRAME.name().equals(getSelectedProductType())) {
			removeAfOnSequenceNumber((Frame) product);
		}
		return product;
	}

	private void removeAfOnSequenceNumber(Frame frame) {
		try {
			frame.setAfOnSequenceNumber(null);
			getDao(FrameDao.class).save(frame);
			logUserAction(SAVED, frame);
		} catch (Exception ex) {
			logException(ex);
		}
	}

	private String getNextProductId(String productId) {
		
		InProcessProduct inProcessProduct = getDao(InProcessProductDao.class).findByKey(productId);
		
		if (inProcessProduct == null) return null;
        
		if(inProcessProduct.getNextProductId() != null) {
			// this is to filter "MISSED_ON" etc product id 
			InProcessProduct next = getDao(InProcessProductDao.class).findByKey(inProcessProduct.getNextProductId());
			return next == null ? null : next.getProductId();
		}
		
        try {
        	do {
    	    	List<PreviousLine> lines = getDao(PreviousLineDao.class).findAllByLineId(inProcessProduct.getLineId());
				
    			if(lines.isEmpty() || lines.size() > 1) return null;
    					
    			List<InProcessProduct> products = getDao(InProcessProductDao.class).findHeadProducts(lines.get(0).getId().getPreviousLineId());
    					
    			if(!products.isEmpty()) return products.get(0).getProductId();
        
        	}while(true);
		} catch (Exception e) {
			logException(e);
		}
		
		return null;
	}

	private void updateExpectedProduct(String productID,String expectedProductID) {
		try {
			List<ExpectedProduct> expProductList = getDao(ExpectedProductDao.class).findAllByProductId(productID);
			for (ExpectedProduct expProduct : expProductList) {
				expProduct.setProductId(expectedProductID);
				getDao(ExpectedProductDao.class).save(expProduct);
				logUserAction(SAVED, expProduct);
			}
		} catch (Exception e) {
			logException(e);
		}
	}

	private void createExceptionalOut(String productID, String reason,Date productionDate, String lastPassingPPID) {
		try {
			String lastPassingProcessPoint = "";
			if (!(lastPassingPPID == null)) lastPassingProcessPoint = lastPassingPPID.trim();
			ExceptionalOutId id = new ExceptionalOutId(productID,new Timestamp(System.currentTimeMillis()));
			ExceptionalOut exceptionalOut = new ExceptionalOut(id,getMainWindow().getUserId(), reason, null, productionDate,lastPassingProcessPoint, null);
			getDao(ExceptionalOutDao.class).save(exceptionalOut);
			logUserAction(SAVED, exceptionalOut);
		} catch (Exception e) {
			logException(e);
		}
	}

	private void removeInRepairArea(String productID) {
		try {
			InRepairArea inRepairArea = getDao(InRepairAreaDao.class).findByKey(productID);
			if (inRepairArea != null) {
				getDao(InRepairAreaDao.class).remove(inRepairArea);
				logUserAction(REMOVED, inRepairArea);
			}
		} catch (Exception e) {
			logException(e);
		}
	}
	
	private void removeAllParts(BaseProduct product, String productId) {
		try {
			if (ProductType.FRAME.name().equals(getSelectedProductType())) {	
				FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
				Frame frame = frameDao.findByKey(productId);
				
				if(StringUtils.isNotBlank(frame.getEngineSerialNo())){
					EngineDao dao = ServiceFactory.getDao(EngineDao.class);
					Engine engine = dao.findByKey(frame.getEngineSerialNo());
					if(engine != null){
						engine.setVin(null);
						getEngineDao().save(engine);
						logUserAction(SAVED, engine);
					}					
					frame.setEngineSerialNo(null);
					frame.setEngineStatus(false);
					frame.setActualMissionType(null);
					frame.setMissionSerialNo(null);
					getFrameDao().save(frame);
					logUserAction(SAVED, frame);
				}
				removeInstalledPart(productId);
				removeMeasurements(productId); 
			} else if (ProductType.ENGINE.name().equals(getSelectedProductType()) ||
						ProductType.MBPN.name().equals(getSelectedProductType())) {
				removeInstalledPart(productId);
				removeMeasurements(productId);
			}
		} catch (Exception e) {
			logException(e);
		}
	}
	
	private void loadBroadcastDestinations(){
		 this.broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(applicationId);
	}
	
	private void invokeBroadcastService(BaseProduct product, int seqNo, String aReason){
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT, product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		dc.put(DataContainerTag.PROCESS_POINT_ID, product.getLastPassingProcessPointId());
		dc.put(DataContainerTag.ACTUAL_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
		dc.put(DataContainerTag.ASSOCIATE_NO, getMainWindow().getUserId());
		dc.put(DataContainerTag.EXCEPTIONAL_OUT_COMMENT, aReason);	
		
		getService(BroadcastService.class).broadcast(applicationId, seqNo, dc);
	}
	
	private void removeInstalledPart(String productId){
		InstalledPartDao dao = getDao(InstalledPartDao.class);
		List<InstalledPart> installedParts = dao.findAllByProductId(productId);
		if (installedParts == null || installedParts.isEmpty()) return;
		
		for (InstalledPart installedPart : installedParts) {
			logUserAction(REMOVED, installedPart);
			installedPart.setPartSerialNumber(null);
			installedPart.setInstalledPartStatus(InstalledPartStatus.REMOVED);
			installedPart.setActualTimestamp(null);
		}
		logUserAction(REMOVED, installedParts);
		dao.saveAll(installedParts);
	}
	
	private void removeMeasurements(String productId){  
		MeasurementDao dao = getDao(MeasurementDao.class);
		List<Measurement> measurements = dao.findAllByProductId(productId);
		if(measurements == null || measurements.isEmpty()) return; 
		
		for (Measurement measurement : measurements) {
			logUserAction(REMOVED, measurement);
			measurement.setMeasurementAngle(0.0);
			measurement.setMeasurementValue(0.0);
			measurement.setPartSerialNumber(null);
			measurement.setMeasurementName(null);
			measurement.setMeasurementStringValue(null);
			measurement.setActualTimestamp(null);
			measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
		}
		dao.saveAll(measurements);
	}
	
	private boolean isValidTrackingStatus(String trackingStatus, String trackingPoints){
		if(StringUtils.isBlank(trackingPoints)){
			return true;
		}
		StringTokenizer st = new StringTokenizer(trackingPoints,",");
		while(st.hasMoreElements()){
			if(StringUtils.equalsIgnoreCase(st.nextToken().trim(), trackingStatus))
			return true;
		}
		return false;
	}
	
	private void updateShippingStatus(String productID,String scrapORexceptionalLineID) {
		try {
			if (ProductType.FRAME.name().equals(getSelectedProductType())) {
				ShippingStatus shippingStatus = getDao(ShippingStatusDao.class).findByKey(productID);
				if (shippingStatus != null) {
					Integer shippedStatus = shippingStatus.getStatus();
					if (shippedStatus != 4) {
						if (scrapORexceptionalLineID.equals(getScrapLineId())) {
							shippingStatus.setStatus(-2);
						} else {
							shippingStatus.setStatus(-3);
						}
						getDao(ShippingStatusDao.class).save(shippingStatus);
						logUserAction(SAVED, shippingStatus);
					}
				}
			}
		} catch (Exception e) {
			logException(e);
		}
	}

	public String getSelectedProductType() {
		return productTypeName;
	}
	
	public void actionPerformed(ActionEvent e) {}
	
	protected TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}

	public String getScrapLineId() {
		return scrapLineId;
	}

	public void setScrapLineId(String scrapLineId) {
		this.scrapLineId = scrapLineId;
	}

	public String getExceptionLineId() {
		return exceptionLineId;
	}

	public void setExceptionLineId(String exceptionLineId) {
		this.exceptionLineId = exceptionLineId;
	}

	public String getScrapPPId() {
		return scrapPPId;
	}

	public void setScrapPPId(String scrapPPId) {
		this.scrapPPId = scrapPPId;
	}

	public String getExceptionPPId() {
		return exceptionPPId;
	}

	public void setExceptionPPId(String exceptionPPId) {
		this.exceptionPPId = exceptionPPId;
	}
	
	protected FrameDao getFrameDao() {
		return ServiceFactory.getDao(FrameDao.class);
	}

	protected EngineDao getEngineDao() {
		return ServiceFactory.getDao(EngineDao.class);
	}	

	protected String getAccessRequested() {
		return PropertyService.getProperty(this.applicationId, IS_RESTRICT_ACCESS, IS_NO_RESTRICTION);
	}
}