package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.service.IDaoService;

public interface QiImageDao extends IDaoService<QiImage, String> {
	public List<QiImageSectionDto> findImageByFilter(String filterValue, String productKind,List<Short> statusList);
	public void updateImageStatus(String name, short active, String user);
	public boolean isDuplicateImageName(String imageName);
	public QiImage findImageByImageName(String imageName);
	public List<QiImageSectionDto> findAllByProductKind(String filterValue, String productKind);
	public void updateImage(String imageName, String imageDescription, String updateUser, String bitmapFileName);
	public void updateImage(byte[] imageData, String updateUser, String bitmapFileName);
	public List<QiImage> findAllByImageName(List<String> imageNames);
}
