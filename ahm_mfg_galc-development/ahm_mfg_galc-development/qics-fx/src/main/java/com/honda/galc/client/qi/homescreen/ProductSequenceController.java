package com.honda.galc.client.qi.homescreen;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationController</code> is the controller class for Part Location Combination Panel.
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
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class ProductSequenceController implements EventHandler<ActionEvent>  {
	
	HomeScreenModel model;
	ProductSequencePanel myPanel;
	HomeScreenView parentView;
	int numRows = 30;
	String stationId = "";
	private boolean isAutoSequence = false;
	private static Timeline timer;
	private String autoInputFrequency = "5";
		
	public ProductSequenceController(ProductSequencePanel prodSeqPanel, HomeScreenModel hsModel, HomeScreenView hsView) {
		model = hsModel;
		myPanel = prodSeqPanel;
		parentView = hsView;
		numRows = 30;
		stationId = PropertyService.getProperty(model.getProcessPointId(), QiConstant.RFID_STATION_PROP_KEY);
		isAutoSequence = PropertyService.getPropertyBoolean(model.getProcessPointId(), QiConstant.IS_AUTO_SEQ_KEY,false);
		autoInputFrequency = PropertyService.getProperty(model.getProcessPointId(), QiConstant.AUTO_INPUT_FREQ, "");
	}
	
	
	public void initEventHandlers() {
		addProductSequenceTableListner();
	}


	/**
	 * This method used to load data on panel
	 */
	public void loadInitialData() {
		reload();
	}
	
	public boolean isAutoProductInput()  {
		QiStationConfiguration qicsSetting = model.findStationConfiguration(QiEntryStationConfigurationSettings.AUTO_PRODUCT_INPUT.getSettingsName());
		if(qicsSetting != null)  {
			if(qicsSetting.getPropertyValue().equalsIgnoreCase(QiConstant.YES))  {
				return true;
			}
			else  {
				return false;
			}
		}
		else  {
			return isAutoSequence();
		}
	}
	
	public int getPollFrequency()  {
		
		String pollFreqInSec = "";
		int pollFreq = 0;
		QiStationConfiguration qiStationConfigEntry = parentView.getModel().findPropertyKeyValueByProcessPoint(
				parentView.getProcessPointId(),QiEntryStationConfigurationSettings.AUTO_INPUT_POLL_FREQ.getSettingsName());
		//initialize to default value
		pollFreqInSec = QiEntryStationConfigurationSettings.AUTO_INPUT_POLL_FREQ.getDefaultPropertyValue();	
		//if its a QICS station, get the station config setting
		if(qiStationConfigEntry != null && !StringUtils.isBlank(qiStationConfigEntry.getPropertyValue()))  {
			pollFreqInSec = qiStationConfigEntry.getPropertyValue();
		}
		//if not QICS station or qics station setting not set explicitly, check process point configuration
		else if(!StringUtils.isBlank(autoInputFrequency))  {
			pollFreqInSec = autoInputFrequency;
		}
		try {
			if(!StringUtils.isBlank(pollFreqInSec))  {
				pollFreq = Integer.parseInt(pollFreqInSec);
			}
		} catch (NumberFormatException e) {
			pollFreq = 0;
		}

		return pollFreq;
	}
	
	private boolean isLastProductSkipped()  {
		ProductModel pModel = parentView.getProductModel();
		if(pModel != null && pModel.isLastProductSkipped())  {
			return true;
		}
		else  {
			return false;
		}
	}
	
	private void resetLastProductSkipped()  {
		ProductModel pModel = parentView.getProductModel();
		if(pModel != null)  {
			pModel.setLastProductSkipped(false);		
		}
	}

	/**
	 * @return the isAutoSequence
	 */
	public boolean isAutoSequence() {
		return isAutoSequence;
	}


	/**
	 * @param isAutoSequence the isAutoSequence to set
	 */
	public void setAutoSequence(boolean isAutoSequence) {
		this.isAutoSequence = isAutoSequence;
	}


	/**
	 * This method used to load data on panel
	 */
	public void reload() {
		List<ProductSequence> prodSeqList = ServiceFactory.getDao(ProductSequenceDao.class).findAll(stationId);
		myPanel.getMyTable().setData(prodSeqList);
		refreshSelectButton();
		if(isAutoProductInput() && !isLastProductSkipped())
		{
			if(prodSeqList != null && !prodSeqList.isEmpty()) {
				stopClock();
				EventBusUtil.publish(new ProductEvent(prodSeqList.get(0).getId().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
			}
			else {
				int pollFreq = getPollFrequency();
				if(pollFreq > 0)  {
					startClock(getPollFrequency() * 1000);
				}
			}
		}
		else  {
			stopClock();
			resetLastProductSkipped();
		}
	}

	public void stopClock() {
		if(timer != null){
			timer.stop();		
		}
	}
	
	public void startClock(long redirectTime) {
		stopClock();
		if(redirectTime <= 0)  return;
		if(timer == null)  {
		    timer =  new Timeline(new KeyFrame(Duration.millis(redirectTime),new EventHandler<ActionEvent>() {
			  public void handle(ActionEvent event) {
						List<ProductSequence> prodSeqList = ServiceFactory.getDao(ProductSequenceDao.class).findAll(stationId);
						if(prodSeqList != null && !prodSeqList.isEmpty())  {
							stopClock();
							myPanel.getMyTable().setData(prodSeqList);
							EventBusUtil.publish(new ProductEvent(prodSeqList.get(0).getId().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
						}
			  };
			}));
		    timer.setOnFinished(new EventHandler<ActionEvent>(){
		    	public void handle(ActionEvent event) {
		    	}	
		    });
			timer.setCycleCount(Timeline.INDEFINITE);
			timer.play();
		}
		else  {
			timer.playFromStart();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addProductSequenceTableListner() {

		ObservableList<TableColumn<ProductSequence, ?>> columns  = myPanel.getMyTable().getTable().getColumns();
		TableColumn productIdColumn  = columns.get(1);
		
		productIdColumn.setCellFactory(new Callback<TableColumn<ProductSequence, String>, TableCell<ProductSequence, String>>() {
		    @Override
		    public TableCell<ProductSequence, String> call(TableColumn<ProductSequence, String> col) {
		        final TableCell<ProductSequence, String> cell = new TableCell<ProductSequence, String>() {
		            @Override
		            public void updateItem(String firstName, boolean empty) {
		                super.updateItem(firstName, empty);
		                if (empty) {
		                    setText(null);
		                } else {
		                    setText(firstName);
		                }
		            }
		         };
		         cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		             @Override
		             public void handle(MouseEvent event) {
		                 if (event.getClickCount() > 1) {
		                	EventBusUtil.publish(new ProductEvent(myPanel.getMyTable().getSelectedItem().getId().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
		                 } 
		                 refreshSelectButton();
		             }
		         });
		         return cell ;
		    }
		});
		
	}


	private void addProductSequenceData() {
		
		List<ProductSequence> productQueue = model.getProductQueueForStationId(stationId);

		if(null!=productQueue)
		myPanel.getMyTable().setData(productQueue);
		
		myPanel.getMyTable().getTable().setRowFactory(new Callback<TableView<ProductSequence>, TableRow<ProductSequence>>() {
            @Override
            public TableRow<ProductSequence> call(TableView<ProductSequence> paramP) {
                return new TableRow<ProductSequence>() {
                    @Override
                    protected void updateItem(ProductSequence prodSeq, boolean paramBoolean) {
                    	super.updateItem(prodSeq, paramBoolean);
                    	
                    }
                };
            }
        });
	}
	

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			
			if (QiConstant.REFRESH.equalsIgnoreCase(loggedButton.getText()))  {
				reload();
			};
			
			if(loggedButton.equals(myPanel.getSelectButton())) {
				ProductSequence ps = myPanel.getMyTable().getSelectedItem();
				if(ps != null) {
	            	EventBusUtil.publish(new ProductEvent(ps.getId().getProductId(), ProductEventType.PRODUCT_INPUT_RECIEVED));
				} else {
					refreshSelectButton();
				}
			}
		}
		
	}


	/**
	 * This method is used to execute action for Product Buttons: DONE & DIRECT PASS
	 */
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		//Timestamp now = model.getDatabaseTimestamp();
		if (event == null)  return;
		else if(event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_DONE) ||
				event.getEventType().equals(ProductEventType.PRODUCT_DIRECT_PASSED))  {
			//if product id is in product_sequence list, delete it
		}
	}
			

	public void refreshSelectButton() {
 		myPanel.getSelectButton().setDisable(myPanel.getMyTable().getTable().getSelectionModel().getSelectedItems().isEmpty());
	}
	
	public ProductSequencePanel getMyPanel() {
		return myPanel;
	}

	public void setMyPanel(ProductSequencePanel myPanel) {
		this.myPanel = myPanel;
	}
}