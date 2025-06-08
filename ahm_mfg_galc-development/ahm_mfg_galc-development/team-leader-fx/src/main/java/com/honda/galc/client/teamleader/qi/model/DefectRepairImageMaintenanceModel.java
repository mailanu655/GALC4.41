package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.utils.MediaUtils;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dao.qi.QiRepairResultImageDao;
import com.honda.galc.dto.qi.QiDefectResultImageDto;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiDefectResultImageId;
import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.entity.qi.QiRepairResultImageId;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class DefectRepairImageMaintenanceModel extends QiModel {
	
	private QiDefectResultImageDao defectResultImageDao = getDao(QiDefectResultImageDao.class);
	private QiRepairResultImageDao repairResultImageDao = getDao(QiRepairResultImageDao.class);
	
	public List<QiDefectResultImageDto> findAllByProductId(String productId) {
		List<QiDefectResultImageDto> list = defectResultImageDao.findAllByProductId(productId);
		updateUrl(list);
		return list; 
	}
	
	public List<QiDefectResultImageDto> findAllByApplicationId(String applicationId) {
		List<QiDefectResultImageDto> list = defectResultImageDao.findAllByApplicationId(applicationId);
		updateUrl(list);
		return list;
	}
	
	public List<QiDefectResultImageDto> findAllByPartDefectCombination(String searchString) {
		List<QiDefectResultImageDto> list = defectResultImageDao.findAllByPartDefectCombination(searchString);
		updateUrl(list);
		return list;
	}

	public void updateUrl(List<QiDefectResultImageDto> list) {
		for(QiDefectResultImageDto dto : list) {
			dto.setUrl(createHyperlink(dto.getImageUrl()));
		}
	}
	
	private Hyperlink createHyperlink(String url) {
		Hyperlink hyperlink = new Hyperlink(url);
		hyperlink.setFont(Font.font("Arial", 14));
		hyperlink.setTextFill(Color.INDIGO);
		hyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MediaUtils.showMedia(url);
			}
		});
		return hyperlink;
	}
	
	public QiDefectResultImage findDefectResultImage(QiDefectResultImageDto dto) {
		return defectResultImageDao.findByKey(new QiDefectResultImageId(dto.getDefectResultId(), dto.getImageUrl()));
	}
	
	public QiDefectResultImage createDefectResultImage(QiDefectResultImageDto dto, String url) {
		QiDefectResultImage entity = new QiDefectResultImage(dto.getDefectResultId(), url);
		entity.setApplicationId(getApplicationContext().getApplicationId());
		entity.setCreateUser(getApplicationContext().getUserId());
		defectResultImageDao.save(entity);
		return entity;
	}
	
	public void deleteDefectResultImage(QiDefectResultImage defectImage) {
		MicroserviceUtils.getInstance().deleteByImageUrl(defectImage.getId().getImageUrl());
		defectResultImageDao.removeByKey(defectImage.getId());
	}
	
	public QiRepairResultImage findRepairResultImage(QiDefectResultImageDto dto) {
		return repairResultImageDao.findByKey(new QiRepairResultImageId(dto.getRepairId(), dto.getImageUrl()));
	}
	
	public QiRepairResultImage createRepairResultImage(QiDefectResultImageDto dto, String url) {
		QiRepairResultImage entity = new QiRepairResultImage(dto.getRepairId(), url);
		entity.setApplicationId(getApplicationContext().getApplicationId());
		entity.setCreateUser(getApplicationContext().getUserId());
		entity = repairResultImageDao.save(entity);
		return entity;
	}
	
	public void deleteRepairResultImage(QiRepairResultImage repairImage) {
		MicroserviceUtils.getInstance().deleteByImageUrl(repairImage.getId().getImageUrl());
		repairResultImageDao.removeByKey(repairImage.getId());
	}
	
}
