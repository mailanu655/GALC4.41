package com.honda.galc.dao.conf;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.RegionalProcessPointGroup;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.service.IDaoService;

public interface ProcessPointDao extends IDaoService<ProcessPoint, String> {

	/**
     * Finds all terminals ordered by PROCESS_POINT_ID.
     */
	public List<ProcessPoint> findAllOrderByProcessPointId();

    List<ProcessPoint> findAllByDivision(Division division);

    List<ProcessPoint> findAllByLine(Line line);
    
    int countByLine(Line line);

    /**
     *  Find all process point that has lot control rule defined
     */
    List<ProcessPoint> findAllLotControlProcessPoint();
    
    public List<ProcessPoint> findAllByIds(List<String> processPointIds);
    
    /**
     * Find all process point which has lot control rule defined for the given division
     * @param division
     * @return
     */
    public List<ProcessPoint> findAllLotControlProcessPointByDivision(Division division);
    
    public List<ProcessPoint> getProcessPointLst(List<String> processPointListAsString);
    
	public ProcessPoint findPreviousProcessPointByProcessPointId(String processPointId);
	
	public ProcessPoint findById( String processPointId );
	
	public Map<String, String> findAllProcessPtProductTypeMapping();
	
    /**
     *  Find all process points by process point type. 
     */
	List<ProcessPoint> findAllByProcessPointType(ProcessPointType type);
	
	/**
	 * Select process points by application type.
	 * @param type
	 * @return
	 */
	public List<ProcessPoint> findAllByApplicationType(List<ApplicationType> types);

	/**
	 * Select process points by application type and division id.
	 * @param type
	 * @return
	 */
	public List<ProcessPoint> findAllByApplicationType(List<ApplicationType> types, String divisionId);

	/**
	 * Find all headless process points
	 * @return
	 */
	List<ProcessPoint> findDeviceDrivenHeadlessProcessPoints();
	
	/**
	 * Find all QICS station by division id 
	 * @return
	 */
	public List<ProcessPoint> findAllByApplicationComponentDivision(String divisionId);
	
	/**
	 * Find First process point where kickout flag is set
	 * @return
	 */
	public ProcessPoint findFirstKickoutProcessPointForProduct(String productId, String processPoint,int currentProcessSeq);
	
	public List<ProcessPoint> findAllByDivisionId(String divisionId);
	
	/**
	 * Find process points that have a particular property and property value
	 * @param propertyKey
	 * @param propertyValue
	 * @return
	 */
	public List<ProcessPoint> findAllByDbProperty(String propertyKey, String propertyValue);
	
	public List<String> findAllNAQProcessPointId();
	
	public List<ProcessPoint> findAllByGroup(RegionalProcessPointGroup group);
    public List<ProcessPoint> findAllByGroupAndMatchingText(RegionalProcessPointGroup group, String searchText);
    public List<ProcessPoint> findAllByMatchingTextAndNotInGroup(String searchText, RegionalProcessPointGroup group);
	
	public List<String> findAllProcessPoint();
	
	public String findLastTrackingStatus(String productId, String exceptionLineIds, String productType);
  
    public List<ProcessPoint> findAllByDivisionIdandByType(String divisionId, ProcessPointType type);
    
    public List<ProcessPoint> findAllKickoutPocessByLine(Line line);

	List<ProcessPoint> findAllTrackingPointsByLineAndProductType(String productType, String lineId);
	
	public List<ProcessPoint> findTrackingPointsByLine(String lineId);
}
