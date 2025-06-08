package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Code;
import com.honda.galc.entity.product.CodeId;
import com.honda.galc.service.IDaoService;

public interface CodeDao extends IDaoService<Code, CodeId> {

	/**
	 * Find codes with a CODE_TYPE in the given list of code types
	 */
	public List<Code> findByCodeTypes(List<String> codeTypes);
	/**
	 * Find CODE values for codes with a CODE_TYPE in the given list of code types
	 */
	public List<String> findCodesByCodeTypes(List<String> codeTypes);

}
