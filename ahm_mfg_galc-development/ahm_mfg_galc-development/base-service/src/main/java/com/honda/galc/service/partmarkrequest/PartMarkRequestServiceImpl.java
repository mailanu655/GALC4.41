package com.honda.galc.service.partmarkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.PartMarkRequestService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class PartMarkRequestServiceImpl implements PartMarkRequestService  {

	protected DataContainer retList = new DefaultDataContainer();
	
	private static final int SUCCESS = 0;
	private static final int ERROR = 1;
	private static final String DATA_ERROR = "ERROR";
	

	@Override
	public DataContainer getPartMarks(String modelYearCode, List<String> partNumbers) {
		try {
			HashMap<String,String> partNoPartMarkMap = new HashMap<String,String>();
			retList.clear();
			
			if(partNumbers.size()>getPartRequestLimit()) {
				retList.put("RETURN_CODE",ERROR);
				retList.put(TagNames.ERROR_MESSAGE, "Request Exceeds Part Number Limit");
			}else {
				retList.put("RETURN_CODE",SUCCESS);
				retList.put(TagNames.ERROR_MESSAGE, "");
				
				for(String partNo:partNumbers) {
					if(partNoPartMarkMap != null && partNoPartMarkMap.size()>0 && partNoPartMarkMap.containsKey(partNo)) {
						continue;
					}
					Logger.getLogger().info("Retrieving Part Marks for Part Number-"+partNo );
					List<Object[]> partMarks = getBuildAttributeByBomDao().findAllAttributeForPartNoAndModelYear(modelYearCode, StringUtil.padRight(partNo,18,' ',false), getSystemId());
					if(partMarks == null || partMarks.size() == 0) {
						partNoPartMarkMap.put(partNo," ");
					}else if(partMarks.size()>1) {
						partNoPartMarkMap.put(partNo,DATA_ERROR);
					}else {
						for(Object[] objArr:partMarks) {
								if(objArr.length>1) {
									String partMark = (String)objArr[1];
									if(!StringUtil.isNullOrEmpty(partMark) && partMark.length() > getPartMarkMaxLength()){
										partNoPartMarkMap.put(partNo, DATA_ERROR);
									}else
										partNoPartMarkMap.put(partNo,partMark);
								}
						}
					}
				}
				if(partNoPartMarkMap.size() > 0) {
					retList.put("PART_MARKS", getPartMarksList(partNoPartMarkMap));
				}else {
					retList.put("PART_MARKS", " ");
				}
			}
			
		}catch(Exception e){
			retList.put("RETURN_CODE",ERROR);
			retList.put(TagNames.ERROR_MESSAGE, e.getMessage());
			Logger.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return retList;
	}
	
	private BuildAttributeByBomDao getBuildAttributeByBomDao() {
		return ServiceFactory.getDao(BuildAttributeByBomDao.class);
	}
	
	private String getSystemId() {
		return (PropertyService.getPropertyBean(HeadLessPropertyBean.class)).getPartMarkRequestSystemId();
	}
	
	private int getPartRequestLimit() {
		return (PropertyService.getPropertyBean(HeadLessPropertyBean.class)).getPartMarkRequestLimit();
	}
	private int getPartMarkMaxLength() {
		return (PropertyService.getPropertyBean(HeadLessPropertyBean.class)).getPartMarkMaxLength();
	}
		
	private List<PartMarkRequest> getPartMarksList(HashMap<String,String> partNoPartMarkMap){
		List<PartMarkRequest> partMarks = new ArrayList<PartMarkRequest>();
		
		for(String partNo:partNoPartMarkMap.keySet()) {
			PartMarkRequest request = new PartMarkRequest();
			request.setPartNumber(partNo);
			request.setPartMark(partNoPartMarkMap.get(partNo));
			
			partMarks.add(request);
		}
		
		return partMarks;
	}
	
	private class PartMarkRequest{
		String part_Number;
		String part_Mark;
		public String getPartNumber() {
			return part_Number;
		}
		public void setPartNumber(String partNumber) {
			this.part_Number = partNumber;
		}
		public String getPartMark() {
			return part_Mark;
		}
		public void setPartMark(String partMark) {
			this.part_Mark = partMark;
		}
			
	}
}
