package com.honda.galc.service.broadcast;

import java.io.StringWriter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.qics.RepairResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.RepairResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.qwx.namespace.*;

/**
 * 
 * <h3>SciemetricXMLConvertor Class description</h3>
 * <p> SciemetricXMLConvertor description </p>
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
 *
 * </TABLE>
 *   
 * @author K Maharjan<br>
 * Jan 03 14
 *
 *
 */

public class QWXXMLConvertor {
	
	public  String convertToXML(DataContainer dc) throws JAXBException{
	     ObjectFactory factory = new ObjectFactory();
	     Root.SinglePart sss=new Root.SinglePart();
	     
	     PartType part=factory.createPartType();     
	     part=preparePart(dc);
	     sss.setPart(part);
	   	     
		 SectionsType sst=factory.createSectionsType();
		 sst=prepareSections(dc);
		 sss.setSections(sst);
 	     
	     Root root = factory.createRoot();
	     root.setSinglePart(sss);
	     
	     JAXBContext context = JAXBContextSingletonHelper.getSciemetricContextInstance("com.honda.galc.service.qwx.namespace");
	     Marshaller marshaller = context.createMarshaller();
	     StringWriter writer = new StringWriter();
	     marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
	     marshaller.marshal(root,writer);
	     return writer.toString();
	}
	
	public PartType preparePart(DataContainer dc) throws JAXBException{
		ObjectFactory factory = new ObjectFactory();
		ModelType modelType=factory.createModelType();
	    modelType.setLabel(dc.getString(DataContainerTag.PRODUCT_SPEC_CODE));
	     
	    PartType part=factory.createPartType();
	    part.setLabel(dc.getString(DataContainerTag.PRODUCT_TYPE));
	    part.setSerialNumber(dc.getString(DataContainerTag.PRODUCT_ID));
	    part.setModelNumber(modelType);
	    Root.SinglePart sss=new Root.SinglePart();
	    sss.setPart(part);
	    return part;			
	}
	
	public SectionsType prepareSections(DataContainer dc) throws JAXBException{
		ObjectFactory factory = new ObjectFactory();
		//sections
		SectionsType sst=factory.createSectionsType();
		String productId=dc.getString(DataContainerTag.PRODUCT_ID);
		List<DefectResult> defectresults=(List<DefectResult>) dc.get(DataContainerTag.DEFECT_REPAIRED);
		for(DefectResult dr:defectresults){
			if(dr.isRepairedStatus()){
				//section : responsible_line
				SectionType sectionType=factory.createSectionType();
				if(dr.getResponsibleLine()==null){
					sectionType.setLabel(dr.getResponsibleDept());}
				else{
					sectionType.setLabel(dr.getResponsibleLine());
				}
				//stations : Process Point ID
				StationsType stationsType=factory.createStationsType();
				StationType stationType=factory.createStationType();
				stationType.setLabel(dr.getId().getApplicationId());
					ResultType resultType=factory.createResultType();
					resultType.setLabel("FAIL");
				stationType.setResult(resultType);
				//Operations 
				OperationsType operationsType=factory.createOperationsType();
				//operation
				OperationType operationType=factory.createOperationType();
				String operation=dr.getInspectionPartLocationName();
				String ro=replaceCommaAndSpace(operation);
				if(ro.length()>12){
					ro=ro.substring(0,11);
				}
				operationType.setLabel(ro);
				//operationType.setDescription(operation);
				operationType.setResult(resultType);
				operationType.setCount(BigInteger.valueOf(-2));
				operationType.setDateStamp(getXMLDate(new Date()));
					
				////defects GAL125TBX : DefectResult
				DefectsType defectsType=factory.createDefectsType();
				//defect
				DefectType defectType=factory.createDefectType();
				defectType.setLabel(replaceCommaAndSpace(dr.getDefectTypeName()));
					
				defectType.setTimeIn(getXMLDate(dr.getCreateTimestamp()));
				defectType.setTimeRepaired(getXMLDate(dr.getRepairTimestamp()));

				//location
				LocationType defectLocationType=factory.createLocationType();
				defectLocationType.setLabel(replaceCommaAndSpace(dr.getInspectionPartLocationName()));
				defectLocationType.setId(BigInteger.valueOf(1));
				defectType.setLocation(defectLocationType);
				//component
				ComponentType componentType=factory.createComponentType();
				componentType.setLabel(replaceCommaAndSpace(dr.getInspectionPartName()));
				componentType.setId(BigInteger.valueOf(1));
				defectType.setComponent(componentType);
				//repair station
				RepairStationType repairStationType=factory.createRepairStationType();
				repairStationType.setLabel(replaceCommaAndSpace(dr.getId().getApplicationId()));
				repairStationType.setId(BigInteger.valueOf(1));
				defectType.setRepairStation(repairStationType);
				//operator
				OperatorType operatorType=factory.createOperatorType();
					User user=new User();
					user=getUserDetail(dr.getAssociateNo());
					String username=user.getUserName().trim();
					if(username.contains(" ")){
						String strArr[] = username.split(" ",2);
						operatorType.setFirstName(strArr[0]);
						operatorType.setLastName(strArr[1]);
					}
					else{
						operatorType.setFirstName(username);
						operatorType.setLastName("NONE");
					}
					operatorType.setLogid(user.getUserId());
				defectType.setOperator(operatorType);
				//repairs GAL222TBX
				RepairsType repairsType=factory.createRepairsType();
				RepairResultDao repairdao=ServiceFactory.getDao(RepairResultDao.class);
				int defectResultId=dr.getId().getDefectResultId();
				List<RepairResult> repairs=repairdao.findAllByProductIdandDefectId(productId,defectResultId);
				for(RepairResult repair:repairs){
					//repair
					RepairType repairType=factory.createRepairType();
					repairType.setLabel(replaceCommaAndSpace(repair.getRepairMethodName()));
					//location
					LocationType repairLocationType=factory.createLocationType();
					repairLocationType.setLabel(replaceCommaAndSpace(dr.getInspectionPartLocationName()));
					repairLocationType.setId(BigInteger.valueOf(1));
					repairType.setLocation(defectLocationType);
					//component
					ComponentType repaircomponentType=factory.createComponentType();
					repaircomponentType.setLabel(replaceCommaAndSpace(dr.getInspectionPartName()));
					repaircomponentType.setId(BigInteger.valueOf(1));
					repairType.setComponent(componentType);
					repairsType.getRepair().add(repairType);
				}
				defectType.setRepairs(repairsType);
				defectsType.getDefect().add(defectType);
				operationType.setDefects(defectsType);
				operationsType.getOperation().add(operationType);	
				stationType.setOperations(operationsType);				
				stationsType.getStation().add(stationType);	
				sectionType.getStationsOrOperations().add(stationsType);	
				sst.getSection().add(sectionType);	
			}
			
		}
		return sst;
	}
	
	public String replaceCommaAndSpace(String oString){
		String newdata=oString.replaceAll("[^a-zA-Z0-9]", "");
		return newdata;
	}
	
	public User getUserDetail(String userid){
		User user=new User();
		UserDao userdao=ServiceFactory.getDao(UserDao.class);
		user=userdao.findByKey(userid);
		return user;
	}
	
	public XMLGregorianCalendar getXMLDate(Date date){
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		if(date==null){
			date=new Date();
		}
        gc.setTime(date);
		XMLGregorianCalendar xmlDate=null;
		try {
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xmlDate;
	}

	public XMLGregorianCalendar getXMLDate(Timestamp stamp){
		Date date=new Date(stamp.getTime());
		return getXMLDate(date);
	}
	
}
