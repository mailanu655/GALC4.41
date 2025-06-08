package com.honda.galc.dao.conf;


import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.product.FactoryNewsCurrent;
import com.honda.galc.service.IDaoService;

/**
 * added new method findEligibleLines method for Repair In Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public interface LineDao extends IDaoService<Line, String> {

    public List<Line> findAllByDivisionId(Division division, boolean withChildren);
    
    public Line findByLineName(String lineName);
    
    public ArrayList<FactoryNewsCurrent> getFactoryNews(String plantName, String gpcsPlantCode);
    
    public Line getByEntryProcessPointId(String entryProcessPointId);
    
    public List<Line> findEligibleRepairLines(String divisionId);
    
    public int getAgedInventory(String lineId, int ageInMins);
    
    public int findNextLineSeq(String lineId);
    
    public List<Line> findAlLinesParents();
    
    public String findNextLineId(String lineId);

    public List<Line> findAllByLineIds(List<String> lineIds);
    
    List<Line> findAllTrackingLinesByProductType(String productType);
}
