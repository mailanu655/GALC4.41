package com.honda.galc.oif.task;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.EntityBuilderPlainText;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>OifFileScheduleHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OifFileScheduleHandler description </p>
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
 * @author Paul Chou
 * Jun. 9, 2014
 *
 */
public class OifFileScheduleHandler {
	
	protected BufferedReader breader;
	protected Logger logger;
	protected String fileName;
	private String currentLine;
	protected OifTaskPropertyBean oifPropertyBean;
	
	
	private int count;

	
	public OifFileScheduleHandler(OifTaskPropertyBean oifTaskPropertyBean, String filename, 
			BufferedReader breader,	Logger logger) {
		super();
		this.breader = breader;
		this.logger = logger;
		this.fileName = filename;
		this.oifPropertyBean = oifTaskPropertyBean;
		
		init();
	}
	
	

	private void init() {
		count = 0;
	}


	@SuppressWarnings("unchecked")
	public void process() throws Exception{
		
		EntityBuilderPlainText builder = new EntityBuilderPlainText(oifPropertyBean, logger);
		try {
			while ((currentLine = breader.readLine()) != null) {
				logger.debug("read line:" + currentLine);
				if (StringUtils.isEmpty(currentLine))
					continue;
				builder.readLine(currentLine);

				for (String entityName : getCurrentFileMapping()) {
					
					logger.debug("start to build Entity: ", entityName);
					
					Object entity = builder.buildEntity(entityName);

					logger.debug("Entity build results: ", buildLogMessage(entity));

					if (entity != null) {
						
						if (entity instanceof List) {
							List entityList = (List) entity;
							if(entityList.size() > 0){
								IDaoService dao = builder.getDao();
								dao.saveAll(entityList);
							}
						} else {
							IDaoService dao = builder.getDao();
							dao.save(entity);
						}
					}
				}
				count++;
			}
		} catch (Exception e) {
			logger.info("Exception on process schedule file:" + fileName + " total :" + count + " records processed."); 
			throw e;
		}
		logger.info("Finish process schedule file:" + fileName + " total :" + count + " records processed.");
	}



	@SuppressWarnings("unchecked")
	private String buildLogMessage(Object entity) {
		if (entity == null) return  "null";
		StringBuilder sb = new StringBuilder(entity.getClass().getSimpleName()).append("[");
		if(entity instanceof List)
			for(Object o : (List)entity){
				if (o != null) sb.append(o.toString()).append(" "); 
				else sb.append("null");
			}
		else
			sb.append(entity.toString());  
		
		sb.append("]");
		return sb.toString();
	}


	private List<String> getCurrentFileMapping() {
		List<String> list = new ArrayList<String>();
		for(Entry<String, String> entry: oifPropertyBean.getFileTableMapping().entrySet())
			if(entry.getKey().contains(fileName)){
				 list = Arrays.asList( entry.getValue().split(Delimiter.COMMA));
				 break;
			} 
		
		if(list.size() == 0)
			logger.warn("WARN: schedule:", fileName, " is not configured to create any GALC schedule." );
		return list;
	}

}
