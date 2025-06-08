package com.honda.galc.client.teamleader.qi.stationconfig.limitresponsibility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationResponsibilityId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;

/**
 * 
 * <h3>LimitResponsibilityController Class description</h3>
 * <p>
 * Controller class for LimitResponsibility
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
 *         June 13, 2017
 * 
 */
public class LimitResponsibilityController extends EntryStationConfigController {

	private static final Function<QiStationResponsibilityDto, QiStationResponsibilityDto> GET_SITE_LIST = new Function<QiStationResponsibilityDto, QiStationResponsibilityDto>() {
		@Override
		public QiStationResponsibilityDto apply(final QiStationResponsibilityDto entity) {
			return createFilteredDto(entity, true, false, false, false);
		}
	};

	private static final Function<QiStationResponsibilityDto, QiStationResponsibilityDto> GET_PLANT_LIST = new Function<QiStationResponsibilityDto, QiStationResponsibilityDto>() {
		@Override
		public QiStationResponsibilityDto apply(final QiStationResponsibilityDto entity) {
			return createFilteredDto(entity, true, true, false, false);
		}
	};

	private static final Function<QiStationResponsibilityDto, QiStationResponsibilityDto> GET_DEPT_LIST = new Function<QiStationResponsibilityDto, QiStationResponsibilityDto>() {
		@Override
		public QiStationResponsibilityDto apply(final QiStationResponsibilityDto entity) {
			return createFilteredDto(entity, true, true, true, false);
		}
	};

	private List<QiStationResponsibilityDto> assignedResponsibilities;

	public LimitResponsibilityController(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		super(model, view);
	}

	/**
	 * This method is used to initialize listeners
	 */
	public void initListeners() {
		addCurrentlyAssignedSiteListener();
		addCurrentlyAssignedPlantListener();
		addCurrentlyAssignedDeptListener();
		addCurrentlyAssignedRespLevel1Listener();
		addAvailableSiteListener();
		addAvailablePlantListener();
		addAvailableDeptListener();
		addAvailableRespLevelListener();
	}

	/**
	 * Overridden handle method for Event Handlers
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			switch (StationConfigurationOperations.getType(loggedButton.getId())) {

			case RIGHT_SHIFT_RESPONSIBILITY_SITE:
				assignSiteAction(actionEvent);
				break;

			case LEFT_SHIFT_RESPONSIBILITY_SITE:
				deassignSiteAction(actionEvent);
				break;

			case RIGHT_SHIFT_RESPONSIBILITY_PLANT:
				assignPlantAction(actionEvent);
				break;

			case LEFT_SHIFT_RESPONSIBILITY_PLANT:
				deassignPlantAction(actionEvent);
				break;

			case RIGHT_SHIFT_RESPONSIBILITY_DEPT:
				assignDepartmentAction(actionEvent);
				break;

			case LEFT_SHIFT_RESPONSIBILITY_DEPT:
				deassignDepartmentAction(actionEvent);
				break;

			case RIGHT_SHIFT_RESPONSIBILITY_LEVEL1:
				assignRespLevelAction(actionEvent);
				break;

			case LEFT_SHIFT_RESPONSIBILITY_LEVEL1:
				deassignRespLevelAction(actionEvent);
				break;

			case UPDATE_RESPONSIBILITY:
				updateResponsibility(actionEvent);
				break;

			case RESET_RESPONSIBILITY:
				resetResponsibility(actionEvent);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * This method is used to update responsibility
	 * 
	 * @param actionEvent
	 */
	private void updateResponsibility(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>(
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
				.getItems());
		String processPoint = getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString();
		List<QiStationResponsibility> stationResponsibilities = getQiStationResponsibilityListFromDtoList(dtoList,
				processPoint);
		
		Map<QiStationResponsibilityDto, String> keyValueMap=new HashMap<QiStationResponsibilityDto, String>();
		if( null != assignedResponsibilities && !assignedResponsibilities.isEmpty()){
			for (QiStationResponsibilityDto qiStationResponsibilityDto : assignedResponsibilities) {
				if (!dtoList.contains(qiStationResponsibilityDto)) {
					qiStationResponsibilityDto.setProcessPointId(processPoint);
					keyValueMap.put(qiStationResponsibilityDto,qiStationResponsibilityDto.getConcatenatedString());
				}
			}
		}
		getModel().deleteAllAssignedRespByProcessPoint(processPoint);
		getModel().saveAllStationResponsibilities(stationResponsibilities);
		
		for (QiStationResponsibilityDto qiStationResponsibilityDto : assignedResponsibilities){
			if (!dtoList.contains(qiStationResponsibilityDto)) {
				QiStationResponsibility station=new QiStationResponsibility();
				station.setId(new QiStationResponsibilityId(qiStationResponsibilityDto.getProcessPointId(), qiStationResponsibilityDto.getResponsibleLevelId()));
		AuditLoggerUtil.logAuditInfo(station, null, QiConstant.SAVE_REASON_FOR_STATION_RESPONSIBILITY_AUDIT, getView().getScreenName(),
				keyValueMap.get(qiStationResponsibilityDto),getUserId());
			}
		}
		loadInitialData();
	}

	/**
	 * This method is called on reset action
	 * 
	 * @param actionEvent
	 */
	private void resetResponsibility(ActionEvent actionEvent) {
		loadInitialData();
	}

	/**
	 * Available Site -> Currently Assigned Site
	 * 
	 * @param actionEvent
	 */
	private void assignSiteAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> availableSiteItems = getView().getLimitResponsibilityPanel()
				.getAvailableResponsibilitySiteTablePane().getSelectedItems();
		if (!availableSiteItems.isEmpty()) {
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable()
			.getItems().addAll(availableSiteItems);
			getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable().getItems()
			.removeAll(availableSiteItems);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
		}
		getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Currently Assigned Site -> Available Site
	 * 
	 * @param actionEvent
	 */
	private void deassignSiteAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> assignedSiteNames = new ArrayList<QiStationResponsibilityDto>(getView()
				.getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getSelectedItems());
		if (!assignedSiteNames.isEmpty()) {
			getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable().getItems()
			.addAll(assignedSiteNames);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable()
			.getItems().removeAll(assignedSiteNames);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			for (final QiStationResponsibilityDto dto : assignedSiteNames) {
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable()
				.getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite());
					}
				});
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane()
				.getTable().getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite());
					}
				});
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
				.getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite());
					}
				});
			}
		}
		getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Available Plant -> Currently Assigned Plant
	 * 
	 * @param actionEvent
	 */
	private void assignPlantAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> availablePlants = getView().getLimitResponsibilityPanel()
				.getAvailableResponsibilityPlantTablePane().getSelectedItems();
		if (!availablePlants.isEmpty()) {
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable()
			.getItems().addAll(availablePlants);
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems()
			.removeAll(availablePlants);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
		}
			getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Currently Assigned Plant -> Available Plant
	 * 
	 * @param actionEvent
	 */
	private void deassignPlantAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> assignedPlants = new ArrayList<QiStationResponsibilityDto>(getView()
				.getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getSelectedItems());
		if (!assignedPlants.isEmpty()) {
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems()
			.addAll(assignedPlants);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable()
			.getItems().removeAll(assignedPlants);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			for (final QiStationResponsibilityDto dto : assignedPlants) {
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane()
				.getTable().getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite())
								&& object.getPlant().equalsIgnoreCase(dto.getPlant());
					}
				});
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
				.getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite())
								&& object.getPlant().equalsIgnoreCase(dto.getPlant());
					}
				});
			}
		}
			getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Available Dept -> Currently Assigned Dept
	 * 
	 * @param actionEvent
	 */
	private void assignDepartmentAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> availableDept = getView().getLimitResponsibilityPanel()
				.getAvailableResponsibilityDepartmentTablePane().getSelectedItems();
		if (!availableDept.isEmpty()) {
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable()
			.getItems().addAll(availableDept);
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable()
			.getItems().removeAll(availableDept);
			Collections.sort(getView().getLimitResponsibilityPanel()
					.getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems(),
					new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
		}
			getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Currently Assigned Dept -> Available Dept
	 * 
	 * @param actionEvent
	 */
	private void deassignDepartmentAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> assignedDept = new ArrayList<QiStationResponsibilityDto>(
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane()
				.getSelectedItems());
		if (!assignedDept.isEmpty()) {
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable()
			.getItems().addAll(assignedDept);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable()
			.getItems().removeAll(assignedDept);
			Collections.sort(getView().getLimitResponsibilityPanel()
					.getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems(),
					new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			for (final QiStationResponsibilityDto dto : assignedDept) {
				getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
				.getItems().removeIf(new Predicate<QiStationResponsibilityDto>() {
							@Override
					public boolean test(QiStationResponsibilityDto object) {
						return object.getSite().equalsIgnoreCase(dto.getSite())
								&& object.getPlant().equalsIgnoreCase(dto.getPlant())
								&& object.getDept().equalsIgnoreCase(dto.getDept());
					}
				});
			}
		}
			getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
	}

	/**
	 * Available Resp Level -> Currently Assigned Resp Level
	 * 
	 * @param actionEvent
	 */
	private void assignRespLevelAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> availableRespLevel = getView().getLimitResponsibilityPanel()
				.getAvailableResponsibilityLevel1TablePane().getSelectedItems();
		if (!availableRespLevel.isEmpty()) {
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
			.getItems().addAll(availableRespLevel);
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems()
			.removeAll(availableRespLevel);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
		}
	}

	/**
	 * Currently Assigned Resp Level -> Available Resp Level
	 * 
	 * @param actionEvent
	 */
	private void deassignRespLevelAction(ActionEvent actionEvent) {
		List<QiStationResponsibilityDto> assignedRespLevel = getView().getLimitResponsibilityPanel()
				.getCurrentlyAssignedResponsibilityLevel1TablePane().getSelectedItems();
		if (!assignedRespLevel.isEmpty()) {
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems()
			.addAll(assignedRespLevel);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
			.getItems().removeAll(assignedRespLevel);
			Collections.sort(getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
			Collections.sort(getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane()
					.getTable().getItems(), new QiStationResponsibilityDto());
		}
	}

	/**
	 * This method is used to load initial data
	 */
	public void loadInitialData() {
		clearTablePanes();
		String processPoint = getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString();

		getView().getLimitResponsibilityPanel().getPlantTitledPane().setDisable(true);
		getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(true);
		getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(true);
		getView().getLimitResponsibilityPanel().getUpdateBtn().setDisable(true);
		getView().getLimitResponsibilityPanel().getResetBtn().setDisable(true);

		List<QiStationResponsibilityDto> availableSites = getDtoListFromSiteList(getModel().findAllActiveSite());
		assignedResponsibilities = new ArrayList<QiStationResponsibilityDto>(
				trimDtoList(getModel().findAllAssignedRespByProcessPoint(processPoint)));

		List<QiStationResponsibilityDto> assignedSites = new ArrayList<QiStationResponsibilityDto>(
				QiCommonUtil.getUniqueArrayList(Lists.transform(assignedResponsibilities, GET_SITE_LIST)));

		availableSites.removeAll(assignedSites);
		getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable()
		.setItems(FXCollections.observableArrayList(availableSites));
		if (!assignedResponsibilities.isEmpty()) {
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable()
			.getItems().addAll(assignedSites);

			List<QiStationResponsibilityDto> assignedPlants = new ArrayList<QiStationResponsibilityDto>(
					QiCommonUtil.getUniqueArrayList(Lists.transform(assignedResponsibilities, GET_PLANT_LIST)));
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems()
			.removeAll(assignedPlants);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable()
			.getItems().addAll(assignedPlants);

			List<QiStationResponsibilityDto> assignedDepts = new ArrayList<QiStationResponsibilityDto>(
					QiCommonUtil.getUniqueArrayList(Lists.transform(assignedResponsibilities, GET_DEPT_LIST)));
			getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable()
			.getItems().removeAll(assignedDepts);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable()
			.getItems().addAll(assignedDepts);

			getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems()
			.removeAll(assignedResponsibilities);
			getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
			.getItems().addAll(assignedResponsibilities);
		}
		if(isFullAccess())
			getView().getLimitResponsibilityPanel().enableButtons();
	}

	/**
	 * This method is used to add available site listener
	 */
	private void addAvailableSiteListener() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable().getItems()
		.addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> availableItems = getView().getLimitResponsibilityPanel()
						.getAvailableResponsibilitySiteTablePane().getTable().getItems();
				if (availableItems.isEmpty()) {
					getView().getLimitResponsibilityPanel().getSiteTitledPane().setDisable(true);
				} else{
					getView().getLimitResponsibilityPanel().getSiteTitledPane().setDisable(false);
				}
			}
		});
	}

	/**
	 * This method is used to add available plant listener
	 */
	private void addAvailablePlantListener() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems()
		.addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> availableItems = getView().getLimitResponsibilityPanel()
						.getAvailableResponsibilityPlantTablePane().getTable().getItems();
				if (availableItems.isEmpty()) {
					getView().getLimitResponsibilityPanel().getPlantTitledPane().setDisable(true);
				} else {
					getView().getLimitResponsibilityPanel().getPlantTitledPane().setDisable(false);
				}
			}
		});
	}

	/**
	 * This method is used to add available dept listener
	 */
	private void addAvailableDeptListener() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems()
		.addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> availableItems = getView().getLimitResponsibilityPanel()
						.getAvailableResponsibilityDepartmentTablePane().getTable().getItems();
				if (availableItems.isEmpty()) {
					getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(true);
				} else {
					getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(false);
				}
			}
		});
	}

	/**
	 * This method is used to add available level 1 listener
	 */
	private void addAvailableRespLevelListener() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems()
		.addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> availableItems = getView().getLimitResponsibilityPanel()
						.getAvailableResponsibilityLevel1TablePane().getTable().getItems();
				if (availableItems.isEmpty()) {
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(true);
				} else {
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(false);
				}
			}
		});
	}

	/**
	 * This method is used to add currently assigned site listener
	 */
	private void addCurrentlyAssignedSiteListener() {
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable().getItems()
		.addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> assignedSiteItems = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilitySiteTablePane().getTable().getItems();
				List<QiStationResponsibilityDto> assignedPlantItems = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems();
				if (!assignedSiteItems.isEmpty()) {
					List<String> assignedSiteNameList = getSiteNameListFromDtoList(assignedSiteItems);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane()
					.getTable().setItems(FXCollections.observableArrayList(getDtoListFromPlantList(getModel().findAllActivePlantBySiteList(assignedSiteNameList))));
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems().removeAll(assignedPlantItems);
					getView().getLimitResponsibilityPanel().getPlantTitledPane().setDisable(false);
				} else {
					getView().getLimitResponsibilityPanel().getPlantTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable().getItems().clear();
				}
			}
		});
	}

	/**
	 * This method is used to add currently assigned plant listener
	 */
	private void addCurrentlyAssignedPlantListener() {
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems().addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> assignedItems = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems();
				List<QiStationResponsibilityDto> assignedDeptItems = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems();
				if (!assignedItems.isEmpty()) {
					List<String> assignedConcatedList = getConcatedListFromDtoList(assignedItems, false);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane()
					.getTable().setItems(FXCollections.observableArrayList(getDtoListFromDeptList(
							getModel().findAllActiveDepartmentBySitePlantList(assignedConcatedList))));
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems().removeAll(assignedDeptItems);
					getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(false);
				} else {
					getView().getLimitResponsibilityPanel().getDeptTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable().getItems().clear();
				}
			}
		});
	}

	/**
	 * This method is used to add currently assigned dept listener
	 */
	private void addCurrentlyAssignedDeptListener() {
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable()
		.getItems().addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> assignedItems = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems();
				List<QiStationResponsibilityDto> assignedRespLevel1Items = getView()
						.getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane()
						.getTable().getItems();
				if (!assignedItems.isEmpty()) {
					List<String> assignedConcatedList = getConcatedListFromDtoList(assignedItems, true);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane()
					.getTable()
					.setItems(FXCollections.observableArrayList(
							getModel().findAllStationResponsibilityDtoByFilter(
									assignedConcatedList)));
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane()
					.getTable().getItems().removeAll(assignedRespLevel1Items);
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(false);
				} else {
					getView().getLimitResponsibilityPanel().getRespTitledPane().setDisable(true);
					getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane()
					.getTable().getItems().clear();
					getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane()
					.getTable().getItems().clear();
				}
			}
		});
	}

	/**
	 * This method is used to add currently assigned level 1 listener
	 */
	private void addCurrentlyAssignedRespLevel1Listener() {
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable()
		.getItems().addListener(new ListChangeListener<QiStationResponsibilityDto>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends QiStationResponsibilityDto> c) {
				List<QiStationResponsibilityDto> currentlyAssignedResponsibilities = getView().getLimitResponsibilityPanel()
						.getCurrentlyAssignedResponsibilityLevel1TablePane().getTable().getItems();
				final Set<QiStationResponsibilityDto> currentlyAssignedSet = new HashSet<QiStationResponsibilityDto>(currentlyAssignedResponsibilities);
				final Set<QiStationResponsibilityDto> assignedSet = new HashSet<QiStationResponsibilityDto>(assignedResponsibilities);

				if (!currentlyAssignedSet.equals(assignedSet)) {
					getView().getLimitResponsibilityPanel().getUpdateBtn().setDisable(false);
					getView().getLimitResponsibilityPanel().getResetBtn().setDisable(false);
				} else {
					getView().getLimitResponsibilityPanel().getUpdateBtn().setDisable(true);
					getView().getLimitResponsibilityPanel().getResetBtn().setDisable(true);
				}
			}
		});
	}

	/**
	 * This method is used to get DTO List for Site List
	 */
	private List<QiStationResponsibilityDto> getDtoListFromSiteList(List<QiSite> siteList) {
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>();
		for (QiSite site : siteList) {
			dtoList.add(new QiStationResponsibilityDto(site));
		}
		return dtoList;
	}

	/**
	 * This method is used to get DTO List for Plant List
	 */
	private List<QiStationResponsibilityDto> getDtoListFromPlantList(List<QiPlant> plantList) {
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>();
		for (QiPlant plant : plantList) {
			dtoList.add(new QiStationResponsibilityDto(plant));
		}
		return dtoList;
	}

	/**
	 * This method is used to get DTO List for Dept List
	 */
	private List<QiStationResponsibilityDto> getDtoListFromDeptList(List<QiDepartment> deptList) {
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>();
		for (QiDepartment dept : deptList) {
			dtoList.add(new QiStationResponsibilityDto(dept));
		}
		return dtoList;
	}

	/**
	 * This method is used to get DTO List for Resp Level List
	 */
	private List<QiStationResponsibilityDto> getDtoListFromRespList(List<QiResponsibleLevel> respList) {
		List<QiStationResponsibilityDto> dtoList = new ArrayList<QiStationResponsibilityDto>();
		for (QiResponsibleLevel resp : respList) {
			dtoList.add(new QiStationResponsibilityDto(resp));
		}
		return dtoList;
	}

	/**
	 * This method is used to get Site Name from DTO List
	 */
	private List<String> getSiteNameListFromDtoList(List<QiStationResponsibilityDto> dtoList) {
		List<String> siteNameList = new ArrayList<String>();
		for (QiStationResponsibilityDto dto : dtoList) {
			siteNameList.add(dto.getSite());
		}
		return siteNameList;
	}

	/**
	 * This method is used to clear Table Panes
	 */
	private void clearTablePanes() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable().getItems().clear();
	}

	/**
	 * This method is used to get QiStationResponsibility List From Dto List
	 * 
	 * @param dtoList
	 * @param processPoint
	 * @return
	 */
	private List<QiStationResponsibility> getQiStationResponsibilityListFromDtoList(
			List<QiStationResponsibilityDto> dtoList, String processPoint) {
		List<QiStationResponsibility> stationResponsibilities = new ArrayList<QiStationResponsibility>();
		for (QiStationResponsibilityDto dto : dtoList) {
			QiStationResponsibilityId id = new QiStationResponsibilityId(processPoint, dto.getResponsibleLevelId());
			QiStationResponsibility responsibility = new QiStationResponsibility();
			responsibility.setId(id);
			responsibility.setCreateUser(getModel().getUserId());
			stationResponsibilities.add(responsibility);
		}
		return stationResponsibilities;
	}

	/**
	 * This method is used to get list of concatenated attributes
	 * 
	 * @param dtoList
	 * @param isLevelConcated
	 * @return
	 */
	private List<String> getConcatedListFromDtoList(List<QiStationResponsibilityDto> dtoList, boolean isLevelConcated) {
		List<String> concatList = new ArrayList<String>();
		for (QiStationResponsibilityDto dto : dtoList) {
			if (isLevelConcated) {
				concatList.add("'" + dto.getSite() + "-" + dto.getPlant() + "-" + dto.getDept() + "'");
			} else {
				concatList.add("'" + dto.getSite() + "-" + dto.getPlant() + "'");
			}
		}
		return concatList;
	}

	/**
	 * This method is used to Trim attributes of DTO
	 * 
	 * @param dtoList
	 * @return
	 */
	private List<QiStationResponsibilityDto> trimDtoList(List<QiStationResponsibilityDto> dtoList) {
		List<QiStationResponsibilityDto> trimmedDtoList = new ArrayList<QiStationResponsibilityDto>();
		for (QiStationResponsibilityDto dto : dtoList) {
			QiStationResponsibilityDto trimmedDto = new QiStationResponsibilityDto();
			trimmedDto.setResponsibleLevelId(dto.getResponsibleLevelId());
			trimmedDto.setSite(dto.getSite());
			trimmedDto.setPlant(dto.getPlant());
			trimmedDto.setDept(dto.getDept());
			trimmedDto.setDepartmentName(dto.getDepartmentName());
			trimmedDto.setResponsibleLevelName(dto.getResponsibleLevelName());

			trimmedDtoList.add(trimmedDto);
		}
		return trimmedDtoList;
	}

	/**
	 * This method is used to get QiStationResponsibilityDto with options to set attributes.
	 * @param dto
	 * @param isSiteSet
	 * @param isPlantSet
	 * @param isDeptSet
	 * @param isLevelSet
	 * @return
	 */
	private static QiStationResponsibilityDto createFilteredDto(QiStationResponsibilityDto dto, boolean isSiteSet, boolean isPlantSet, boolean isDeptSet, boolean isLevelSet) {
		QiStationResponsibilityDto filteredDto = new QiStationResponsibilityDto();
		if(isSiteSet) {
			filteredDto.setSite(dto.getSite());
		}
		if(isPlantSet) {
			filteredDto.setPlant(dto.getPlant());
		}
		if(isDeptSet) {
			filteredDto.setDept(dto.getDept());
		}
		if(isLevelSet) {
			filteredDto.setResponsibleLevelId(dto.getResponsibleLevelId());
			filteredDto.setResponsibleLevelName(dto.getResponsibleLevelName());
		}
		return filteredDto;
	}

}
