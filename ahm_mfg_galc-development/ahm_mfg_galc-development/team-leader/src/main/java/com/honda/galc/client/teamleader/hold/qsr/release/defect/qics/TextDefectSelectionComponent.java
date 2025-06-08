package com.honda.galc.client.teamleader.hold.qsr.release.defect.qics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.qics.InspectionModelDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TextDefectSelectionComponent</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 26, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class TextDefectSelectionComponent extends JPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<PartGroup> partGroupPane;
	private ObjectTablePane<InspectionPart> partPane;
	private ObjectTablePane<InspectionPartLocation> locationPane;
	private ObjectTablePane<DefectType> defectPane;
	private ObjectTablePane<InspectionPart> otherPartPane;

	private ProcessPoint processPoint;
	private String modelCode;

	private List<InspectionModel> inspectionModels;
	private List<InspectionPartDescription> inspectionPartDescriptions;
	private List<DefectDescription> defectDescriptions;

	private Map<String, Object> selectionCache = new HashMap<String, Object>();;

	public TextDefectSelectionComponent(JPanel parentPanel, Map<String, Object> selectionCache) {
		this.inspectionModels = new ArrayList<InspectionModel>();
		this.inspectionPartDescriptions = new ArrayList<InspectionPartDescription>();
		this.defectDescriptions = new ArrayList<DefectDescription>();
		this.selectionCache = selectionCache == null ? new HashMap<String, Object>() : selectionCache;
		setLayout(null);
		initView();
		mapListeners();
	}

	protected void initView() {
		partGroupPane = createPartGroupPane();
		partPane = createPartPane();
		locationPane = createLocationPane();
		defectPane = createDefectPane();
		otherPartPane = createOtherPartPane();
		add(getPartGroupPane());
		add(getPartPane());
		add(getLocationPane());
		add(getDefectPane());
		add(getOtherPartPane());
	}

	// === get/set === //
	public ObjectTablePane<PartGroup> getPartGroupPane() {
		return partGroupPane;
	}

	public ObjectTablePane<InspectionPart> getPartPane() {
		return partPane;
	}

	public ObjectTablePane<InspectionPartLocation> getLocationPane() {
		return locationPane;
	}

	public ObjectTablePane<DefectType> getDefectPane() {
		return defectPane;
	}

	public ObjectTablePane<InspectionPart> getOtherPartPane() {
		return otherPartPane;
	}

	protected int getSpacing() {
		return 0;
	}

	// ============= factory methods for ui components
	public ObjectTablePane<PartGroup> createPartGroupPane() {
		ColumnMappings columnMappings = ColumnMappings.with("PartGroup", "partGroupName");
		ObjectTablePane<PartGroup> panel = new ObjectTablePane<PartGroup>(columnMappings.get(), true);
		panel.setSize(250, 150);
		panel.setLocation(0, 0);
		return panel;
	}

	public ObjectTablePane<InspectionPart> createPartPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Part", "inspectionPartName");
		ObjectTablePane<InspectionPart> panel = new ObjectTablePane<InspectionPart>(columnMappings.get(), true);
		panel.setSize(getPartGroupPane().getWidth(), 350);
		panel.setLocation(getPartGroupPane().getX(), getPartGroupPane().getY() + getPartGroupPane().getHeight() + getSpacing());
		return panel;
	}

	public ObjectTablePane<InspectionPartLocation> createLocationPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Location", "inspectionPartLocationName");
		ObjectTablePane<InspectionPartLocation> panel = new ObjectTablePane<InspectionPartLocation>(columnMappings.get(), true);
		panel.setSize(getPartGroupPane().getWidth(), 500);
		panel.setLocation(getPartGroupPane().getX() + getPartGroupPane().getWidth() + getSpacing(), getPartGroupPane().getY());
		return panel;
	}

	public ObjectTablePane<DefectType> createDefectPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Defect", "defectTypeName");
		ObjectTablePane<DefectType> panel = new ObjectTablePane<DefectType>(columnMappings.get(), true);
		panel.setSize(250, 500);
		panel.setLocation(getLocationPane().getX() + getLocationPane().getWidth() + getSpacing(), getLocationPane().getY());
		return panel;
	}

	public ObjectTablePane<InspectionPart> createOtherPartPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Other Part", "inspectionPartName");
		ObjectTablePane<InspectionPart> panel = new ObjectTablePane<InspectionPart>(columnMappings.get(), true);
		panel.setSize(230, 500);
		panel.setLocation(getDefectPane().getX() + getDefectPane().getWidth() + getSpacing(), getDefectPane().getY());
		return panel;
	}

	protected List<PartGroup> getPartGroups() {
		List<PartGroup> partGroups = new ArrayList<PartGroup>();
		for (InspectionModel model : getInspectionModels()) {
			PartGroup partGroup = new PartGroup();
			partGroup.setPartGroupName(model.getPartGroupName().trim());

			if (!partGroups.contains(partGroup)) {
				partGroups.add(partGroup);
			}
		}
		return partGroups;
	}

	public List<InspectionPart> getParts() {
		List<InspectionPart> items = new ArrayList<InspectionPart>();
		for (InspectionPartDescription description : getInspectionPartDescriptions()) {
			InspectionPart item = new InspectionPart();
			item.setInspectionPartName(description.getInspectionPartName().trim());
			if (!items.contains(item)) {
				items.add(item);
			}
		}
		return items;
	}

	public List<InspectionPartLocation> getLocations(InspectionPart part) {

		List<InspectionPartLocation> items = new ArrayList<InspectionPartLocation>();
		if (part == null) {
			return items;
		}
		for (InspectionPartDescription description : getInspectionPartDescriptions()) {
			if (HoldUtils.equals(part.getInspectionPartName(), description.getInspectionPartName())) {
				InspectionPartLocation item = new InspectionPartLocation();
				item.setInspectionPartLocationName(description.getInspectionPartLocationName().trim());
				if (!items.contains(item)) {
					items.add(item);
				}
			}
		}
		return items;
	}

	public List<DefectType> getDefects(PartGroup partGroup, InspectionPart part, InspectionPartLocation location) {

		List<DefectType> items = new ArrayList<DefectType>();
		if (partGroup == null || part == null || location == null) {
			return items;
		}
		for (DefectDescription description : getDefectDescriptions()) {
			if (HoldUtils.equals(partGroup.getPartGroupName(), description.getPartGroupName()) && HoldUtils.equals(part.getInspectionPartName(), description.getInspectionPartName())
					&& HoldUtils.equals(location.getInspectionPartLocationName(), description.getId().getInspectionPartLocationName())) {

				DefectType item = new DefectType();
				item.setDefectTypeName(description.getDefectTypeName().trim());
				if (!items.contains(item)) {
					items.add(item);
				}
			}
		}
		return items;
	}

	public List<InspectionPart> getSecondaryParts(PartGroup partGroup, InspectionPart part, InspectionPartLocation location, DefectType defect) {
		List<InspectionPart> items = new ArrayList<InspectionPart>();
		if (partGroup == null || part == null || location == null || defect == null) {
			return items;
		}
		for (DefectDescription description : getDefectDescriptions()) {
			if (HoldUtils.equals(partGroup.getPartGroupName(), description.getPartGroupName()) && HoldUtils.equals(part.getInspectionPartName(), description.getInspectionPartName())
					&& HoldUtils.equals(location.getInspectionPartLocationName(), description.getId().getInspectionPartLocationName()) && HoldUtils.equals(defect.getDefectTypeName(), description.getDefectTypeName())) {

				InspectionPart item = new InspectionPart();
				item.setInspectionPartName(description.getSecondaryPartName().trim());
				if (!items.contains(item)) {
					items.add(item);
				}
			}
		}
		return items;
	}

	public DefectDescription getDefectDescription(PartGroup partGroup, InspectionPart part, InspectionPartLocation location, DefectType defect, InspectionPart secondaryPart) {
		DefectDescription defectDescription = null;
		if (partGroup == null || part == null || location == null || defect == null || secondaryPart == null) {
			return defectDescription;
		}
		for (DefectDescription description : getDefectDescriptions()) {
			if (HoldUtils.equals(partGroup.getPartGroupName(), description.getPartGroupName()) && HoldUtils.equals(part.getInspectionPartName(), description.getInspectionPartName())
					&& HoldUtils.equals(location.getInspectionPartLocationName(), description.getId().getInspectionPartLocationName()) && HoldUtils.equals(defect.getDefectTypeName(), description.getDefectTypeName())
					&& HoldUtils.equals(secondaryPart.getInspectionPartName(), description.getSecondaryPartName())

			) {
				return description;
			}
		}
		return defectDescription;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
		onSetProcessPoint();
	}

	public List<InspectionModel> getInspectionModels() {
		return inspectionModels;
	}

	public List<InspectionPartDescription> getInspectionPartDescriptions() {
		return inspectionPartDescriptions;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public List<DefectDescription> getDefectDescriptions() {
		return defectDescriptions;
	}

	// ================= event handlers mappings ========================//
	protected void mapListeners() {
		getPartGroupPane().getTable().getSelectionModel().addListSelectionListener(new PartGroupSelectionListener(this));
		getPartPane().getTable().getSelectionModel().addListSelectionListener(new PartSelectionListener(this));
		getLocationPane().getTable().getSelectionModel().addListSelectionListener(new LocationSelectionListener(this));
		getDefectPane().getTable().getSelectionModel().addListSelectionListener(new DefectSelectionListener(this));
	}

	protected void onSetProcessPoint() {

		if (getProcessPoint() == null) {
			getInspectionModels().clear();
			getInspectionPartDescriptions().clear();
			getDefectDescriptions().clear();
			getPartGroupPane().removeData();
			return;
		}

		List<InspectionModel> list = null;
		InspectionModelDao inspectionModelDao = ServiceFactory.getDao(InspectionModelDao.class);

		if (StringUtils.isBlank(getModelCode())) {
			list = inspectionModelDao.findAllByApplicationId(getProcessPoint().getProcessPointId());
		} else {
			list = inspectionModelDao.findAllByApplicationIdAndModelCode(getProcessPoint().getProcessPointId(), getModelCode());
		}

		if (list == null) {
			list = new ArrayList<InspectionModel>();
		}

		getInspectionModels().clear();
		getInspectionModels().addAll(list);

		getPartGroupPane().reloadData(getPartGroups());

		if (getPartGroupPane().getTable().getRowCount() == 1) {
			getPartGroupPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	public Map<String, Object> getSelectionCache() {
		return selectionCache;
	}

	public void setSelectionFromCache() {
		PartGroup partGroup = (PartGroup) getSelectionCache().get("partGroup");
		InspectionPart part = (InspectionPart) getSelectionCache().get("part");
		InspectionPartLocation location = (InspectionPartLocation) getSelectionCache().get("location");
		DefectType defect = (DefectType) getSelectionCache().get("defect");
		InspectionPart otherPart = (InspectionPart) getSelectionCache().get("otherPart");
		if (partGroup != null) {
			getPartGroupPane().setSelectedObjectDataRow(partGroup.getPartGroupName());
			if (part != null) {
				getPartPane().setSelectedObjectDataRow(part.getInspectionPartName());
				if (location != null) {
					getLocationPane().setSelectedObjectDataRow(location.getInspectionPartLocationName());
					if (defect != null) {
						getDefectPane().setSelectedObjectDataRow(defect.getDefectTypeName());
						if (otherPart != null) {
							getOtherPartPane().setSelectedObjectDataRow(otherPart.getInspectionPartName());
						}
					}
				}
			}
		}
	}

	public void setSelectionToCache() {
		PartGroup partGroup = getPartGroupPane().getSelectedItem();
		InspectionPart part = getPartPane().getSelectedItem();
		InspectionPartLocation location = getLocationPane().getSelectedItem();
		DefectType defect = getDefectPane().getSelectedItem();
		InspectionPart otherPart = getOtherPartPane().getSelectedItem();
		if (partGroup != null) {
			getSelectionCache().put("partGroup", partGroup);
			if (part != null) {
				getSelectionCache().put("part", part);
				if (location != null) {
					getSelectionCache().put("location", location);
					if (defect != null) {
						getSelectionCache().put("defect", defect);
						if (otherPart != null) {
							getSelectionCache().put("otherPart", otherPart);
						}
					}
				}
			}
		}
	}
}
