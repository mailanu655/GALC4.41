package com.honda.galc.client.teamleader.speccode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SpecCodeColorMaintController extends AbstractController<SpecCodeColorMaintModel, SpecCodeColorMaintPanel> implements EventHandler<ActionEvent>{

	private final static String ERROR_MESSAGE = "Data can not be updated as Lot is completed.";
	private final static String DIALOG_HEADER = "Manual Product Entry Dialog";
	private final static String VIN_STRING = "VIN";
	public SpecCodeColorMaintController(SpecCodeColorMaintModel model, SpecCodeColorMaintPanel view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent event) {
	}

	@Override
	public void initEventHandlers() {
		
	}

	public void productIdButtonClick() {
		ProductTypeData productTypeData = new ProductTypeData();
		productTypeData.setProductTypeName(ProductType.FRAME.name());
		productTypeData.setProductIdLabel(VIN_STRING);
		ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
				DIALOG_HEADER,productTypeData,getView().getMainWindow().getApplicationContext().getApplicationId());
		manualProductEntryDialog.showDialog();
		String productId = manualProductEntryDialog.getResultProductId();
		if (!StringUtils.isEmpty(productId)) {
			getView().getProductIdTextField().setText(productId);
			getView().reload();
		}
	}

	public void editButtonClicked() {
		FrameSpecDto selectedItem = getView().getFrameSpecDtoDataList().getTable().getItems().get(0);
		boolean isValidationRequired = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class,
				ApplicationContext.getInstance().getProcessPointId()).isValidationRequired();

		List<String> productionLots = new ArrayList<String>();
		boolean isCompletedLot = false;
		for (FrameSpecDto frameSpecDto : getView().getFrameSpecDtoList()) {
			if(!(frameSpecDto.getSendStatusString() == PreProductionLotSendStatus.WAITING)) {
				isCompletedLot = true;
			}
			productionLots.add(frameSpecDto.getProductionLot());
		}
		
		if(isCompletedLot && isValidationRequired) {
			MessageDialog.showError(ERROR_MESSAGE);
			return;
		}  else {
			SpecCodeColorMaintDialog dialog = new SpecCodeColorMaintDialog(getView().getMainWindow(),selectedItem, getView().getFrameSpecDtoDataList(), productionLots, isCompletedLot);
			dialog.showDialog();
		}
	}

	
}
