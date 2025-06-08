package com.honda.galc.dao.product;

import java.util.List;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameEngineModelMapId;
import com.honda.galc.service.IDaoService;


public interface FrameEngineModelMapDao extends IDaoService<FrameEngineModelMap, FrameEngineModelMapId>{
	
	public List<FrameEngineModelMap> findAll();
	
	public List<FrameEngineModelMap> findAllByFrameYmto(String frmYmto);
	
	public List<FrameEngineModelMap> findAllByFrameYmto(String frmYear, String frmModel, String frmType, String frmOption);
}
