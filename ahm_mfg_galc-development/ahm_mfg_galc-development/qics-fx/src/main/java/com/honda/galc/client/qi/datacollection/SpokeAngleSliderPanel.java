package com.honda.galc.client.qi.datacollection;

import java.util.ArrayList;
import java.util.List;
import org.tbee.javafx.scene.layout.MigPane;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.ServiceFactory;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>SpokeAngleSliderPanel</code> is the view class for collecting Spoke Angle data
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Bernard Leong</TD>
 * <TD>July 15, 2019</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class SpokeAngleSliderPanel extends AbstractQiProcessView<QiProcessModel, SpokeAngleSliderController> {
	private MigPane migPane = null;
	private Label label1, label2, label3, label4;
	private Slider sliderData = null;
	private String partName = "";
	private String processPointId = "";
	private String partId = "";

	public SpokeAngleSliderPanel(MainWindow mainWindow) {
			super(ViewId.SPOKE_ANGLE_ENTRY,mainWindow);
			postInit();
		}

	@Override
	public void reload() {
		checkLotControlRuleExist();
		displaySpokeAngleInstruction();
	}

	private void displaySpokeAngleInstruction() {
		label2.setText("Using the mouse click on the bar to save a Spoke Angle result");
		if (sliderData!=null) {sliderData.setValue(0);};
	}

	@Override
	public void start() {
		checkLotControlRuleExist();
		displaySpokeAngleInstruction();
	}

	@Override
	public void initView() {}
	
	private void postInit() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		partName = getModel().getProperty().getLotControlPartName();
		processPointId = getModel().getProperty().getLotControlProcessPointId();
		if (processPointId.isEmpty()) 
			processPointId = getProcessPointId();
		this.setCenter(getSpokeDataPane());
		displaySpokeAngleInstruction();
	}

	private void checkLotControlRuleExist() {
		List<LotControlRule> rules = ServiceFactory.getDao(LotControlRuleDao.class).
				findAllByProcessPointIdAndPartName(processPointId, partName);
		if (rules == null || rules.isEmpty()) 
			disableSpokeAnglePanel();
		else
			partId = rules.get(0).getParts().get(0).getId().getPartId();
	}

	private void disableSpokeAnglePanel() {
		migPane.setDisable(true);
		getController().displayErrorMessage("No Spoke Angle Lot Control Rule setup!");
	}
	
	private MigPane getSpokeDataPane() {
		if (migPane == null) {
			migPane = new MigPane();
		    label1 = new Label("Select spoke angle value:");
		    label2 = new Label("Value: 0");
		    label1.setFont(Fonts.DIALOG_BOLD_26);
		    label2.setFont(Fonts.DIALOG_BOLD_26);
		    label3 = new Label("Left");
		    label4 = new Label("Right");
		    migPane.add(label1, "wrap 100");
		    migPane.add(label3 ,"gaptop 20, left ,split2");
			migPane.add(label4,"gapleft 1800, gaptop 20, wrap");
		    migPane.add(getSlider(),"wrap");
		    migPane.add(label2,"wrap 20, center");
		    label3.setFont(Fonts.DIALOG_BOLD_20);
		    label4.setFont(Fonts.DIALOG_BOLD_20);
		}
		return migPane;
	}
	
	public Slider getSlider() {
		if (sliderData==null) {
			Double minValue =getModel().getProperty().getMinSliderValue();
			Double maxValue =getModel().getProperty().getMaxSliderValue();
			Double incrValue =getModel().getProperty().getIncrementSliderValue();
			Double majorTickValue =getModel().getProperty().getMajorTickSliderUnitValue();
			int minorTickValue =getModel().getProperty().getMinorTickSliderUnitValue();
			sliderData = new Slider(minValue, maxValue, 0);
			sliderData.setShowTickMarks(true);
			sliderData.setShowTickLabels(true);
			sliderData.setMajorTickUnit(majorTickValue);
			sliderData.setMinorTickCount(minorTickValue);
			sliderData.setBlockIncrement(incrValue);
			sliderData.setSnapToTicks(true);
			sliderData.setPrefSize(25000, 100);
			sliderData.setScaleY(1.5);
			// Adding mouse listener to value property.
			sliderData.setOnMouseReleased(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent e) {
						String result = String.format("%.2f", sliderData.getValue());
						label2.setText("Value: " + result);
						save();
						getController().displaySuccessfulMessage("Spoke Angle data has been saved.");
					}
			});
		}
		return sliderData;
	}
	
	protected void save() {
		List<InstalledPart> parts = new ArrayList<InstalledPart>();
		parts.add(createInstalledPart());
		getModel().saveAlllInstalledPartResult(parts);
	}
	
	private InstalledPart createInstalledPart() {
		InstalledPart part = new InstalledPart(getModel().getProductId(), partName);
		part.setProcessPointId(processPointId);
		part.setAssociateNo(processPointId);
		part.setProductType(getApplicationProductTypeName());
		part.setPartId(partId);
		part.setInstalledPartStatusId(1);
		part.addMeasurement(""+sliderData.getValue(),partName);
		for (Measurement measurement: part.getMeasurements()) {
			measurement.setMeasurementStatus(MeasurementStatus.OK);
		} 
		return part;
	}
}
