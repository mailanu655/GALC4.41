package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiProductKind;
import com.honda.galc.service.IDaoService;

public interface QiProductKindDao extends IDaoService<QiProductKind, String> {

	public List<String> findAllList();
	
}
