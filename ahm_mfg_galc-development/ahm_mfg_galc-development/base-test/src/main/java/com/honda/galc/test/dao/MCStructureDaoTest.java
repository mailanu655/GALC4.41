package com.honda.galc.test.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.MCStructureId;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Feb 20, 2014
 */
public class MCStructureDaoTest extends AbstractBaseTest {

	private MCStructureDao structureDao;
	private String productSpecCode;
	private long revision;
	private String processPointId;
	private String operationName;
	private int operationRevision;
	private String partId;
	private int partRevision;
	private int pddaProcessId;
	private int recordCount = 22;
	
	public MCStructureDaoTest() {
		try {
			productSpecCode = "14000NSX C000";
			revision = 1;
			processPointId = "PP3";
			operationName = "RF Assy";
			operationRevision = 4;
			partId = "A0001";
			partRevision = 4;
			pddaProcessId = 946767;
			
		} catch (Exception ex) {}
	}
	
	@Test
	public void getStructuresTest() {
		MCStructureId id = new MCStructureId("14000NSX C000", 1, "PP3", "RF Assy", 4, "A0001", 4, 946767,"YAF4");
		MCStructure structure = getDao().findByKey(id);
		assertEquals(productSpecCode, structure.getId().getProductSpecCode());
		assertEquals(revision, structure.getId().getRevision());
		assertEquals(processPointId, structure.getId().getProcessPointId());
		assertEquals(operationName, structure.getId().getOperationName());
		assertEquals(operationRevision, structure.getId().getOperationRevision());
		assertEquals(partId, structure.getId().getPartId());
		assertEquals(partRevision, structure.getId().getPartRevision());
		assertEquals(pddaProcessId, structure.getId().getPddaPlatformId());
	}
	
	@Test
	public void findAllByProductSpecCodeTest() {
		List<MCStructure> structures = getDao().findAllByProductSpecCode(productSpecCode);
		assertEquals(productSpecCode, structures.get(0).getId().getProductSpecCode().trim());
	}
	
	@Test
	public void findAllByStructureRevisionTest() {
		List<MCStructure> structures = getDao().findAllByStructureRevision(revision);
		assertEquals(revision, structures.get(0).getId().getRevision());
	}
	
	@Test
	public void findAllByProcessPointIdTest() {
		List<MCStructure> structures = getDao().findAllByProcessPointId(processPointId);
		assertEquals(processPointId, structures.get(0).getId().getProcessPointId().trim());
	}
	
	@Test
	public void findAllByOperationNameTest() {
		List<MCStructure> structures = getDao().findAllByOperationName(operationName);
		assertEquals(operationName, structures.get(0).getId().getOperationName().trim());
	}
	
	@Test
	public void findAllByOperationRevisionTest() {
		List<MCStructure> structures = getDao().findAllByOperationRevision(operationRevision);
		assertEquals(operationRevision, structures.get(0).getId().getOperationRevision());
	}
	
	@Test
	public void findAllByPartRevisionTest() {
		List<MCStructure> structures = getDao().findAllByPartRevision(partRevision);
		assertEquals(partRevision, structures.get(0).getId().getPartRevision());
	}
	
	@Test
	public void verifyPopulateStructureMethod(){
		MCStructureDao struDao = getDao();
		List<Object[]> mcStrLst = struDao.createNewStructuresForProdSpec("Frame","9SVKAD000 VIOSM     D",5,"PRODUCT");
		System.out.println("Result from DB : " + mcStrLst.size());
		assertEquals(recordCount, mcStrLst.size());
	}
	
	public MCStructureDao getDao() {
		if(structureDao == null)
			structureDao = ServiceFactory.getDao(MCStructureDao.class);
		return structureDao;
	}

}
