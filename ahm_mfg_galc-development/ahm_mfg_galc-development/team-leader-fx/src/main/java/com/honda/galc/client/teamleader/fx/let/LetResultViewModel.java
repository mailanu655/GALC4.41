package com.honda.galc.client.teamleader.fx.let;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetConnectedProgramDao;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.dao.product.LetPassCriteriaProgramDao;
import com.honda.galc.dao.product.LetProgramCategoryDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.LetConnectedProgram;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.entity.product.LetProgramCategory;
import com.honda.galc.entity.product.LetProgramCategoryId;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.letxml.model.FaultCode;
import com.honda.galc.letxml.model.ReFaultCode;
import com.honda.galc.letxml.model.Process;
import com.honda.galc.letxml.model.ReTest;
import com.honda.galc.letxml.model.ReTestAttribute;
import com.honda.galc.letxml.model.ReTestParam;
import com.honda.galc.letxml.model.Test;
import com.honda.galc.letxml.model.TestAttrib;
import com.honda.galc.letxml.model.TestParam;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.service.ServiceFactory;
import com.thoughtworks.xstream.XStream;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultViewModel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Aug 7, 2019
 */
public class LetResultViewModel {

    // === client model === //
    private Map<LetProgramCategoryId, LetProgramCategory> programCategoryIx;
    private Map<String, LetPassCriteriaProgram> programIx;
    private Map<String, List<String>> connectedProgramIx;
    private List<LetConnectedProgram> connectedPrograms;
    private List<ColumnMapping> defaultColumnMappings;
    private ProductType productType;
    private ProductTypeData productTypeData;

    // === product model === //
    private Map<Long, LetMessage> messageIx;
    private Map<Long, UnitInTest> unitInTestIx;
    private List<LetPassCriteriaProgram> productPrograms;
    private List<ColumnMapping> programColumnMappings;

    private XStream unitInTestXmlConvertor;

    public LetResultViewModel() {
        init();
        initData();
    }

    protected void init() {

        this.programCategoryIx = new HashMap<LetProgramCategoryId, LetProgramCategory>();
        this.programIx = new LinkedHashMap<String, LetPassCriteriaProgram>();
        this.connectedProgramIx = new HashMap<String, List<String>>();
        this.connectedPrograms = new ArrayList<LetConnectedProgram>();
        this.defaultColumnMappings = new ArrayList<ColumnMapping>();

        this.messageIx = new LinkedHashMap<Long, LetMessage>();
        this.unitInTestIx = new LinkedHashMap<Long, UnitInTest>();
        this.productPrograms = new ArrayList<LetPassCriteriaProgram>();
        this.programColumnMappings = new ArrayList<ColumnMapping>();
        this.unitInTestXmlConvertor = createUnitInTestXmlConvertor();
    }

    protected void initData() {
        List<LetPassCriteriaProgram> programs = getLetPassCriteriaProgramDao().findAll();
        for (LetPassCriteriaProgram pgm : programs) {
            getProgramIx().put(pgm.getCriteriaPgmName(), pgm);
        }
        List<LetProgramCategory> programCategories = getLetProgramCategoryDao().findAll();
        for (LetProgramCategory lpc : programCategories) {
            getProgramCategoryIx().put(lpc.getId(), lpc);
        }

        List<LetConnectedProgram> connectedPrograms = getLetConnectedProgramDao().findAll();
        getConnectedPrograms().addAll(connectedPrograms);
        for (LetConnectedProgram lcp : getConnectedPrograms()) {
            List<String> list = getConnectedProgramIx().get(lcp.getProgram().getCriteriaPgmName());
            if (list == null) {
                list = new ArrayList<String>();
                getConnectedProgramIx().put(lcp.getProgram().getCriteriaPgmName(), list);
            }
            list.add(lcp.getTriggeringProgram().getInspectionPgmName());
        }
        setProductType(ProductType.FRAME);
        setProductTypeData(getProductTypeDao().findByKey(getProductType().name()));
    }

    protected List<LetMessage> selectMessages(String productId) {
        return getLetMessageDao().findAll(productId, LetTotalStatus.OK.name());
    }

    public UnitInTest convert(String xml) {
        UnitInTest unitInTest = null;
        try {
            unitInTest = (UnitInTest) getUnitInTestXmlConvertor().fromXML(xml);
        } catch (Exception e) {
            Logger.getLogger().error(e, "Error converting LET message to UnitInTest, xml:" + xml);
            return null;
        }
        return unitInTest;
    }

    // === dao === //
    protected LetPassCriteriaProgramDao getLetPassCriteriaProgramDao() {
        return getDao(LetPassCriteriaProgramDao.class);
    }

    protected LetProgramCategoryDao getLetProgramCategoryDao() {
        return getDao(LetProgramCategoryDao.class);
    }

    protected LetConnectedProgramDao getLetConnectedProgramDao() {
        return ServiceFactory.getDao(LetConnectedProgramDao.class);
    }

    protected LetMessageDao getLetMessageDao() {
        return ServiceFactory.getDao(LetMessageDao.class);
    }

    protected ProductTypeDao getProductTypeDao() {
        return ServiceFactory.getDao(ProductTypeDao.class);
    }

    // === get/set === //
    protected Map<String, LetPassCriteriaProgram> getProgramIx() {
        return programIx;
    }

    protected Map<LetProgramCategoryId, LetProgramCategory> getProgramCategoryIx() {
        return programCategoryIx;
    }

    protected List<LetConnectedProgram> getConnectedPrograms() {
        return connectedPrograms;
    }

    protected Map<String, List<String>> getConnectedProgramIx() {
        return connectedProgramIx;
    }

    protected List<ColumnMapping> getDefaultColumnMappings() {
        return defaultColumnMappings;
    }

    protected Map<Long, LetMessage> getMessageIx() {
        return messageIx;
    }

    protected Collection<LetMessage> getMessages() {
        return getMessageIx().values();
    }

    protected Map<Long, UnitInTest> getUnitInTestIx() {
        return unitInTestIx;
    }

    protected Collection<UnitInTest> getUnitInTests() {
        return getUnitInTestIx().values();
    }

    protected List<ColumnMapping> getProgramColumnMappings() {
        return programColumnMappings;
    }

    protected List<LetPassCriteriaProgram> getProductPrograms() {
        return productPrograms;
    }

    protected ProductTypeData getProductTypeData() {
        return productTypeData;
    }

    protected void setProductTypeData(ProductTypeData productTypeData) {
        this.productTypeData = productTypeData;
    }

    protected ProductType getProductType() {
        return productType;
    }

    protected void setProductType(ProductType productType) {
        this.productType = productType;
    }

    protected XStream createUnitInTestXmlConvertor() {
        XStream xs = new XStream();
        xs.processAnnotations(UnitInTest.class);
        xs.processAnnotations(Process.class);
        xs.processAnnotations(Test.class);
        xs.processAnnotations(TestParam.class);
        xs.processAnnotations(TestAttrib.class);
        xs.processAnnotations(FaultCode.class);
        xs.processAnnotations(ReTest.class);
        xs.processAnnotations(ReTestParam.class);
        xs.processAnnotations(ReTestAttribute.class);
        xs.processAnnotations(ReFaultCode.class);
        return xs;
    }

    public XStream getUnitInTestXmlConvertor() {
        return unitInTestXmlConvertor;
    }
}
