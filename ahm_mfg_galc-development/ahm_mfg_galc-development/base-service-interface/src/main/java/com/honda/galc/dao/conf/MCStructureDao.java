/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;
import java.util.Set;

import com.honda.galc.dto.PddaUnitOfOperation;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.MCStructureId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCStructureDao 
extends IDaoService<MCStructure, MCStructureId> {

	public List<MCStructure> getStructures(String productSpecCode, String processPointId, long structureRevision);

	public List<MCStructure> findAllByProductSpecCode(String productSpecCode);

	public List<MCStructure> findAllByStructureRevision(long structureRevision);

	public List<MCStructure> findAllByProcessPointId(String processPointId);

	public List<MCStructure> findAllByOperationName(String operationName);

	public List<MCStructure> findAllByOperationRevision(int operationRevision);

	public List<MCStructure> findAllByPartRevision(int partRevision);

	public List<Object[]> createNewStructuresForProdSpec(String plantName, String productSpecCode, int structureRev, String specCodeType);
	
	public List<PddaUnitOfOperation> getAllOperationsForProcessPointAndProduct(String productId, String processPoint);
	
	public List<PddaUnitOfOperation> getAllOperationsForProcessPointAndProductPPMode(String productId, String processPoint);
	
	public List<MCStructure> findAllByStructureForProdSpecCodeAndRevision(String productSpecCode, long structureRevision);
	
	public int getMaxRevision();
	
	public int getStructureRowCount(String prodSpecCodeMask);
	
	public List<MCStructure> getStructureForProdSpecCodeForMaxStruRev(String prodSpecCode);
	
	public List<String[]> findAllUnmappedProductIdsForEngine(String productSpecCode);
	
	public List<String[]> findAllUnmappedProductIdsForFrame(String productSpecCode);
	
	public List<String[]> findAllUnmappedProductIdsForMBPN(String productSpecCode);
	
	public List<MCStructure> getStructureForProdSpecCodeMask(List<String> prodSpecCodeMasks);
	
	public List<MCStructure> getStructureForProdSpecCodeMaskPerPage(String prodSpecCodeMask, long startIndex, long endIndex);
	
	public Set<MCStructure> createStructureForProdSpecAndDivisionId(String productSpecCode, String opSpecCode, String divisionId, int strucRevId, String specCodeType, PddaPlatformDto pddaPlatform);
	
	public Set<MCStructure> createStructureForProdSpecAndProcessPoint(String productSpecCode, String opSpecCode, int strucRevId, String specCodeType, String processPoint, PddaPlatformDto pddaPlatform);
	
	public Set<MCStructure> getMaxRevStrucForSpecCodeAndDivId(String prodSpecCode, String divisionId);
	
	public Set<MCStructure> getMaxRevStrucForSpecCodeAndProcessPointId(String prodSpecCode, String processPointId);
		
	public List<String> getMatchingFIFSpecCode(String prodSpecCode);
	
	public List<MCStructure> getAllStructureForPlatFormId(int platFormId);
	
	public List<String> findMadeFromPartMask(String productSpecCode, long StructureRev, String processPoint);
	
	public List<MCStructure> getStructuresByDivision(String productSpecCode, String divId, long structureRevision);
	
	public void deleteStructureBySpecCodeDivIdAndStructureRev(String productSpecCode, String divId, long structureRevision);
	
	public List<PddaPlatformDto> findAllPlatformDetailsByStructureRev(long structureRevision);
	
	public Boolean isFirstTimeRunningModelAtProcessPoint(String processPoint, String productSpecCode, int noOfDays);
	
	public MCStructure findByOpNameAndOpRev(String operationName, int operationRev);
	
	public MCStructure findByOpNameAndPart(String operationName, String partId, int partRev);
}
