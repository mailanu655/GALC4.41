package com.honda.galc.dao.jpa.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.service.Parameters;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 03, 2013
 */
public class LetInspectionProgramDaoImpl extends BaseDaoImpl<LetInspectionProgram, Integer> implements LetInspectionProgramDao {

	private static String GET_VIN_FAULT_LIST_DATA = "SELECT  T701.PRODUCT_ID,  T703.PROCESS_STEP,  T714.INSPECTION_PGM_NAME,  T703.INSPECTION_PGM_STATUS,  T144.PRODUCT_SPEC_CODE FROM ( SELECT  TMP02.PRODUCT_ID,  TMP02.INSPECTION_PGM_ID,  MAX(TMP02.END_TIMESTAMP) END_TIMESTAMP FROM  GAL701TBX TMP01 INNER JOIN GAL703TBXV TMP02 ON TMP01.PRODUCT_ID = TMP02.PRODUCT_ID WHERE  TMP01.PRODUCT_ID = ?1  GROUP BY  TMP02.PRODUCT_ID,  TMP02.INSPECTION_PGM_ID  ) T701 INNER JOIN  GAL703TBXV T703 ON T701.PRODUCT_ID = T703.PRODUCT_ID  AND  T703.INSPECTION_PGM_ID = T701.INSPECTION_PGM_ID AND  T703.END_TIMESTAMP = T701.END_TIMESTAMP   INNER JOIN GAL143TBX T143 ON T703.PRODUCT_ID = T143.PRODUCT_ID  INNER JOIN GAL144TBX T144 ON T143.PRODUCT_SPEC_CODE = T144.PRODUCT_SPEC_CODE  INNER JOIN GAL714TBX T714 ON T703.INSPECTION_PGM_ID = T714.INSPECTION_PGM_ID  WHERE T703.INSPECTION_PGM_STATUS <> '1' ORDER BY SUBSTR(T703.PROCESS_STEP , 1,1 ) DESC, SUBSTR(T703.PROCESS_STEP , 2 )  FOR READ ONLY";

	public static final String INSERT_IF_NOT_EXISTS =  "insert into gal714tbx (inspection_pgm_id, inspection_pgm_name) \n"
	        + "select ?1, ?2 from sysibm.sysdummy1 where (select count(*) from gal714tbx where inspection_pgm_name = ?2) = 0";


	public Map<String, Integer> loadLetInspectionProgram() {
		Map<String, Integer> letInspectionProgramMap = new HashMap<String, Integer>();
		List<LetInspectionProgram> LetInspectionProgramList = findAll();
		for(LetInspectionProgram letInspectionProgram : LetInspectionProgramList){
			letInspectionProgramMap.put(letInspectionProgram.getInspectionPgmName().trim(), letInspectionProgram.getInspectionPgmId());
		}
		return letInspectionProgramMap;
	}

	public List<LetInspectionProgram> findAllLetInspPgmOrderByPgmName()
	{
		return findAll(null,new String[] {"inspectionPgmName"},true);
	}

	public List<Object[]> getLetVinFaultResultData(String productId)
	{
		Parameters param=Parameters.with("1",productId);
		return findAllByNativeQuery(GET_VIN_FAULT_LIST_DATA,param,Object[].class);
	}
	
	public LetInspectionProgram findPgmIdByName(String programName) {
		return findFirst(Parameters.with("inspectionPgmName", programName));
	}

    @Transactional
    public int insertIfNotExists(int id, String name) {
        Parameters params = Parameters.with("1", id);
        params.put("2", name);
        int count = executeNative(INSERT_IF_NOT_EXISTS, params);
        return count;
    }
}
