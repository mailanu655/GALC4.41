package com.honda.galc.client.dc.view.widget;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.vios.dto.PddaPlatformDto;

public class StructureDetailsWidget extends AbstractWidget {

	public StructureDetailsWidget(ProductController productController) {
		super(ViewId.STRUCTURE_DETAILS_WIDGET, productController);
	}

	@Override
	protected void initComponents() {
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setCenter(createTitiledPane(createDetailsPane(productModel)));
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		this.setCenter(null);
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		this.setCenter(null);
	}
	
	private TitledPane createTitiledPane(Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText("Structure Details");
		titledPane.setFont(Font.font("", FontWeight.BOLD, 14));
		titledPane.setContent(content);
		titledPane.setPrefSize(250, 150);
		titledPane.setCollapsible(false);
		return titledPane;
	}
	
	private VBox createDetailsPane(ProductModel productModel){
		VBox detailBox = new VBox();
		
		HBox struRevHbox = new HBox();
		Label struRevLbl = new Label("Revision ");
		struRevLbl.setStyle("-fx-font-weight: bold ;");
		LoggedText struRevText = new LoggedText();
		struRevHbox.setSpacing(25);
		struRevHbox.setPadding(new Insets(5));
		struRevHbox.getChildren().addAll(struRevLbl, struRevText);
		
		HBox dateTimeHbox = new HBox();
		Label dateTimeLbl = new Label("Time Stamp");
		dateTimeLbl.setStyle("-fx-font-weight: bold ;");
		LoggedText dateTimeText = new LoggedText();
		dateTimeHbox.setSpacing(6);
		dateTimeHbox.setPadding(new Insets(5));
		dateTimeHbox.getChildren().addAll(dateTimeLbl, dateTimeText);
		
		
		MCProductStructure productStructure = getDao(MCProductStructureDao.class).findByKey(productModel.getProductId(), productModel.getDivisionId(), productModel.getProduct().getProductSpecCode());
		Date createdDate = productStructure.getUpdateTimestamp() != null ? productStructure.getUpdateTimestamp()
				: productStructure.getCreateTimestamp();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		
		struRevText.setText(String.valueOf(productStructure.getStructureRevision()));
		dateTimeText.setText(format.format(createdDate));
		
		ProductPropertyBean property = PropertyService.getPropertyBean(ProductPropertyBean.class, productModel.getProcessPointId());
		Boolean runningTheModelFirstTime =  getDao(MCStructureDao.class).isFirstTimeRunningModelAtProcessPoint
			(productModel.getProcessPointId(), productModel.getProductSpec().getProductSpecCode(), property.getFirstTimeRunningModelInDays());
		if(runningTheModelFirstTime){
			HBox firstTimeRunningModel = new HBox();
			Label firstTimeRunningModelLabel = new Label("First Time Running model: "+StringUtils.substring(productModel.getProduct().getProductSpecCode(),0,7));
			firstTimeRunningModelLabel.setStyle("-fx-font-weight: bold ;-fx-background-color: yellow");
			firstTimeRunningModel.getChildren().add(firstTimeRunningModelLabel);
			detailBox.getChildren().addAll(struRevHbox, dateTimeHbox, getPlatformDetailsPane(productStructure.getStructureRevision()),firstTimeRunningModel);
		}else{
			detailBox.getChildren().addAll(struRevHbox, dateTimeHbox, getPlatformDetailsPane(productStructure.getStructureRevision()));
		}
		
		return detailBox;
	}
	
	private ScrollPane getPlatformDetailsPane(long structureRevision) {
		ScrollPane scrollPane = new ScrollPane();
		TilePane pane = new TilePane();
		pane.setVgap(1);
		List<PddaPlatformDto> platformList = getDao(MCStructureDao.class).findAllPlatformDetailsByStructureRev(structureRevision);
		for(PddaPlatformDto platform : platformList) {
			String labelText = "P: "+platform.getPlantLocCode()+", D: "+platform.getDeptCode()+", Y: "+String.valueOf(platform.getModelYearDate())+
					", Q: "+String.valueOf(platform.getProdSchQty())+", L: "+platform.getProdAsmLineNo()+", V: "+platform.getVehicleModelCode();
			Label label = new Label(labelText);
			pane.getChildren().add(label);
		}
		pane.minWidthProperty().bind(scrollPane.widthProperty().multiply(0.92));
		pane.maxWidthProperty().bind(scrollPane.widthProperty().multiply(0.92));
		scrollPane.setContent(pane);
		return scrollPane;
	}
	
}
