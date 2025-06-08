package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CodeDao;
import com.honda.galc.entity.product.Code;
import com.honda.galc.entity.product.CodeId;
import com.honda.galc.util.StringUtil;

public class CodeDaoImpl extends BaseDaoImpl<Code, CodeId> implements CodeDao {

	private String FIND_BY_CODE_TYPES = "SELECT * FROM GALADM.CODE_TBX WHERE CODE_TYPE IN (@CODE_TYPES@) ORDER BY CODE_TYPE, CODE";

	private String FIND_CODES_BY_CODE_TYPES = "SELECT CODE FROM GALADM.CODE_TBX WHERE CODE_TYPE IN (@CODE_TYPES@) ORDER BY CODE_TYPE, CODE";

	public List<Code> findByCodeTypes(List<String> codeTypes) {
		if (codeTypes == null || codeTypes.isEmpty()) {
			return null;
		}
		List<Code> codes = findAllByNativeQuery(FIND_BY_CODE_TYPES.replace("@CODE_TYPES@", StringUtil.toSqlInString(codeTypes)), null, Code.class);
		if (codes == null || codes.isEmpty()) {
			return null;
		}
		return codes;
	}

	public List<String> findCodesByCodeTypes(List<String> codeTypes) {
		if (codeTypes == null || codeTypes.isEmpty()) {
			return null;
		}
		List<String> codes = findAllByNativeQuery(FIND_CODES_BY_CODE_TYPES.replace("@CODE_TYPES@", StringUtil.toSqlInString(codeTypes)), null, String.class);
		if (codes == null || codes.isEmpty()) {
			return null;
		}
		return codes;
	}

}
