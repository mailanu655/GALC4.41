package com.honda.galc.client.product.mvc;

import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.mvc.IModel;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
public class ProcessPointSelectionPaneModel implements IModel{

	public List<Division> getDivisions() {
		return getDivisionDao().findAll();
	}
	
	public List<Division> getDivisions(List<String> ids) {
		return getDivisionDao().findAllByDivisionIds(ids);
	}
	
	public List<Line> getLines(Division division) {
		return getLineDao().findAllByDivisionId(division, false);
	}

	public List<Line> getLines(List<String> ids) {
		return getLineDao().findAllByLineIds(ids);
	}

	public List<ProcessPoint> getProcessPoints(Line line) {
		return getProcessPointDao().findAllByLine(line);
	}
	
	public List<ProcessPoint> getProcessPointsWithKickout(Line line) {
		return getProcessPointDao().findAllKickoutPocessByLine(line);
	}
	
	public List<String> getDeviceIdsByProcessPoint(String componentId) {
		String[] machineIds = PropertyService.getPropertyBean(QiPropertyBean.class, componentId).getMachineIds();
		return Arrays.asList(machineIds);
	}
	
	private DivisionDao getDivisionDao() {
		return ServiceFactory.getDao(DivisionDao.class);
	}
	
	private LineDao getLineDao() {
		return ServiceFactory.getDao(LineDao.class);
	}
	
	private ProcessPointDao getProcessPointDao() {
		return ServiceFactory.getDao(ProcessPointDao.class);
	}

	@Override
	public void reset() {
	}
}
