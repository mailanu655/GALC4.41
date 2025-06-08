package com.honda.galc.client.teamleader.qi.stationconfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Plant;

/**
 * 
 * <h3>EntryStationPanel Class description</h3>
 * <p>
 * EntryStationPanel is used to load data in dropdowns etc.
 * </p>
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
 * @author LnTInfotech<br>
 * 
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class EntryStationPanel {
	private static EntryStationPanel instance;
	private LoggedComboBox<ComboBoxDisplayDto> divisionComboBox;
	private LoggedComboBox<String> plantComboBox;
	private LoggedComboBox<ComboBoxDisplayDto> qicsStationComboBox; 
	private LoggedLabel siteValueLabel;
	private static EntryStationConfigModel model;
	private double width;
	private EntryStationPanel() {

	}

	public static EntryStationPanel getInstance(EntryStationConfigModel entryStationConfigModel) {
		if (instance == null) {
			instance = new EntryStationPanel();
		}
		model=entryStationConfigModel;
		return instance;
	}

	/**
	 * This method is used to create EntryStation Panel having Site,Plant,Division and QICS station
	 * @return
	 */
	public MigPane getEntryStationPanel(){
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		this.width = screenBounds.getWidth();
		HBox hBox = new HBox();
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		HBox siteContainer = createSiteContainer();
		plantComboBox=new LoggedComboBox<String>();
		plantComboBox.setId("plantComboBox");
		String siteName = siteValueLabel.getText();
		plantComboBox.setPromptText("Select");
		for (Plant plantObj : getModel().findAllBySiteName(siteName)) { plantComboBox.getItems().add(plantObj.getPlantName());}
		divisionComboBox =new LoggedComboBox<ComboBoxDisplayDto>();
		divisionComboBox.setId("divisionComboBox");
		qicsStationComboBox=new LoggedComboBox<ComboBoxDisplayDto>(); 
		qicsStationComboBox.setId("qicsStationComboBox");
		Insets insets = new Insets(5, 5, 0, 20);
		hBox.getChildren().addAll(siteContainer,getGenericComboBox("Plant",15,insets,plantComboBox),
				getGenericComboBox("Division",15,insets,divisionComboBox),getGenericComboBox("QICS Station",15,insets,qicsStationComboBox));
		hBox.setPadding(new Insets(0, 0, 0, 30));
		hBox.setSpacing(40);
		pane.add(EntryStationConfigPanel.createTitledPane("Entry Station",hBox,width,60),"span,wrap");
		return pane;
	}
	/**
	 * This method is used create site outer container which contains site label and site value.
	 */
	public HBox createSiteContainer() {
		HBox siteLabelContainer = new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("label", "Site", (int)(0.01 * width));
		siteValueLabel =UiFactory.createLabel("siteValueLabel", StringUtils.EMPTY, (int)(0.01 * width));
		siteValueLabel.setText(getModel().findSiteName());
		siteLabelContainer.getChildren().addAll(siteLabel,siteValueLabel);
		siteLabelContainer.setSpacing(10);
		siteLabelContainer.setAlignment(Pos.CENTER_RIGHT);
		return siteLabelContainer;
	}
	/**
	 * This method is used to create Drop down for Plant , Division and QICS station
	 * @param name
	 * @param css
	 * @param spacing
	 * @param padding
	 * @param alignment
	 * @param LoggedComboBox
	 * @return
	 */
	private <T> HBox getGenericComboBox(String name,int spacing,Insets padding,LoggedComboBox<T> loggedComboBox) {
		HBox container = new HBox();
		LoggedLabel productTypeLabel = UiFactory.createLabel(name, name, (int)(0.01 * width));
		productTypeLabel.getStyleClass().add("display-label-14");
		container.getChildren().addAll(productTypeLabel, loggedComboBox);
		container.setSpacing(spacing);
		container.setPadding(padding);
		container.setAlignment(Pos.CENTER_RIGHT);
		return container;
	}

	public LoggedLabel getSiteValueLabel() {
		return siteValueLabel;
	}

	public void setSiteValueLabel(LoggedLabel siteValueLabel) {
		this.siteValueLabel = siteValueLabel;
	}

	public static EntryStationConfigModel getModel() {
		return model;
	}

	public static void setModel(EntryStationConfigModel model) {
		EntryStationPanel.model = model;
	}

	public LoggedComboBox<ComboBoxDisplayDto> getDivisionComboBox() {
		return divisionComboBox;
	}

	public String getDivisionComboBoxSelectedId() {
		return ComboBoxDisplayDto.getComboBoxSelectedId(getDivisionComboBox());
	}

	public void clearDivisionComboBox() {
		getDivisionComboBox().getItems().clear();
	}

	public void setDivisionComboBox(LoggedComboBox<ComboBoxDisplayDto> divisionComboBox) {
		this.divisionComboBox = divisionComboBox;
	}

	public LoggedComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public void setPlantComboBox(LoggedComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	public LoggedComboBox<ComboBoxDisplayDto> getQicsStationComboBox() {
		return qicsStationComboBox;
	}

	public String getQicsStationComboBoxSelectedId() {
		return ComboBoxDisplayDto.getComboBoxSelectedId(getQicsStationComboBox());
	}
	
	public void clearQicsStationComboBox() {
		getQicsStationComboBox().getItems().clear();
	}

	public void setQicsStationComboBox(LoggedComboBox<ComboBoxDisplayDto> qicsStationComboBox) {
		this.qicsStationComboBox = qicsStationComboBox;
	}

}
