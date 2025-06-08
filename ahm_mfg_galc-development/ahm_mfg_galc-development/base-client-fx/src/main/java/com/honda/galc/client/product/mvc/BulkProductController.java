package com.honda.galc.client.product.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.ProductDeviceListener;
import com.honda.galc.client.product.pane.AbstractProductSearchPane;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.schedule.EntryDepartmentEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.schedule.UserResetEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProductCancelledEvent;
import com.honda.galc.client.ui.event.ProductFinishedEvent;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.ProductPropertyBean;

import javafx.application.Platform;

public class BulkProductController extends ProductController {
	
	public BulkProductController(AbstractProductClientPane view,ProductPropertyBean productPropertyBean) {
		super(view, productPropertyBean);

		this.model = new ProductModel(getView().getMainWindow().getApplicationContext());
		if(productPropertyBean != null) this.model.setProperty(productPropertyBean);
		this.state = State.NOT_INITIALIZED;
		this.deviceListener = new ProductDeviceListener(this, view.getLogger());
		this.productIdProcessor = createProductIdProcessor();
		this.audioManager = createAudioManager();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createKeyBoardPopup();
			}
		});
		EventBusUtil.register(this);
	}

	@Override
	public void finishProduct() {

		BaseProduct product = getModel().getProduct();
		List<AbstractProcessView<?, ?>> processes = getView().getProductProcessPane().getActiveProcessViews();
		if (!processes.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("The following process are not finished and are required.");
			sb.append("\nPlease complete them before finishing product.\n");
			for (int i = 0; i < processes.size(); i++) {
				sb.append(" - ").append(processes.get(i).getController().getProcessName());
				if (i < processes.size() - 1) {
					sb.append("\n");
				}
			}

			getView().getProductProcessPane().selectProcessView(0);
			return;
		}

		getLogger().info(String.format("Finish Processing product: %s ", product));

		EventBusUtil.publish(new ProductFinishedEvent(getModel()));
		if (!getModel().isTrainingMode()) {
			getModel().invokeBulkTracking();
			getModel().invokeBroadcastService(CheckPoints.AFTER_PRODUCT_PROCESSED);
		}
		ProductSearchResult.updateDefectResultCache();

		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionComplete();
		}
		getModel().clearProcessedProducts();
		toIdle();
		
	}

	public void startProduct(List<BaseProduct> products) {
		getLogger().info(String.format("Start Processing products: %s ", products.toString()));
		BaseProduct firstProduct = products.get(0);
		getModel().setProduct(firstProduct);
		getModel().setProcessedProducts(products);
		EventBusUtil.publish(new ProductStartedEvent(getModel()));

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("product", firstProduct);

		if (!getModel().isTrainingMode() && getModel().getProperty().isAutoTracking())
			for(BaseProduct product : products) {
				startTracking(product.getProductId());
			}

		//commonMainDefects is actually a list of ACTUAL defects - not QiCommonDefect
		List<QiRepairResultDto> commonMainDefects = ProductSearchResult.initCommonDefects(products);
		//Remove the common main defect if child defects are not common 
		if(commonMainDefects != null && commonMainDefects.size() > 0) {
			for(QiRepairResultDto defect : commonMainDefects) {
				List<QiRepairResultDto> childDefects = getModel().findAllRepairEntryDefectsByDefectId(defect.getDefectResultId());			
				if(getModel().isTrainingMode()) {
					childDefects = updateChildrenDefectResultsForTrainingMode(defect.getDefectResultId(), childDefects);
				}
				defect.setChildRepairResultList(childDefects);
			}
			ProductSearchResult.initCommonChildDefect(commonMainDefects, products);
		}

		setState(State.VALIDATING);
		getView().getInfoPane().setInfo(model);
		getView().showInfoPane();

		getView().getProductProcessPane().prepareProcessViews(firstProduct);
		getView().getProductProcessPane().setFirstEnabledProcessViewSelected();

		getView().showProcessPane();

		setState(State.PROCESSING);
	}
	
	/**
	 * This method is to ADD/UPDATE/DELETE the searched child defect list according to cached defect list
	 * @param defectResultId
	 * @param childrenDefectResults
	 * @return
	 */
	private List<QiRepairResultDto> updateChildrenDefectResultsForTrainingMode(
			long defectResultId, List<QiRepairResultDto> childrenDefectResults){
		Map<QiRepairResultDto, Integer> cachedDefectsForTM = getModel().getCachedDefectsForTraingMode();
		if(cachedDefectsForTM != null && cachedDefectsForTM.size() > 0) {	
			for(Map.Entry<QiRepairResultDto, Integer> entry : cachedDefectsForTM.entrySet()) {
				QiRepairResultDto cachedDefect = (QiRepairResultDto) entry.getKey();
				Integer type = entry.getValue();
				if((defectResultId == cachedDefect.getDefectResultId()) && 
						(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM 
						|| type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM 
						|| type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM)) {
					if(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM)
						childrenDefectResults.add(cachedDefect);
					else if(type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);	
									break;
								}
							}
						}
					}else if (type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);
									childrenDefectResults.add(cachedDefect);
									break;
								}
							}
						}
					}
				}
				
			}
		}
		
		return childrenDefectResults;
	}

	@Override
	public void cancelWithPrompt() {	
		getLogger().check("Cancel Processing");
		getModel().setSkipped(true);
		EventBusUtil.publish(new ProductCancelledEvent(getModel()));
		toIdle();
		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionInComplete();
		}
	}

	@Override
	public void processInputNumberInvoke(String productId) {
		getView().getInputPane().setProductId(productId);
		EventBusUtil.publish(new ProductEvent(productId, ProductEventType.PRODUCT_INPUT_RECIEVED));
	}

	@Override
	public void startProduct(BaseProduct product) {		
	}

    @Subscribe()
    public void onProductEvent(ProductEvent event) {
        if (null!=event && event.getEventType().equals(ProductEventType.PRODUCT_INPUT_PROCESS)) {
            productIdProcessor.processInputNumber(event);
        }
    }
    
	@Subscribe
	public void onAssociateSelectedEvent(EntryDepartmentEvent event) {
		if(null!=event &&  event.getEventType().equals(QiConstant.ASSOCIATE_ID_SELECTED)) {
			AbstractProductSearchPane selectedPane = (AbstractProductSearchPane)getView().getInputPane().getProductInputTabPane().getSelectionModel().getSelectedItem().getContent();
			selectedPane.associateSelected();
		}
	}
	
    @Subscribe()
    public void onAssociateResetEvent(UserResetEvent event) {
        if (null!=event && (event.getEventType().equals(QiConstant.NEW) || 
        		event.getEventType().equals(QiConstant.LOGOUT))) {
        	getView().getInputPane().getProductInputTabPane().getSelectionModel().select(0);
        }
    }
}