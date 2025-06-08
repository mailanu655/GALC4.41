package com.honda.galc.service.datacollection.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;

public class EngineNumbering extends CollectorTask {

	private Map<Object, Object> context;
	
	public EngineNumbering(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
		this.context = context;
	}

	@Override
	public void execute() {
		marryBlockAndEngine();
	}

	private void marryBlockAndEngine() {
		String esn = ((Engine) ((HeadlessDataCollectionContext)context).getProduct()).getId();
		
		String blockId = (String) context.get(TagNames.BLOCK_ID.name());
		BlockDao blockDao = ServiceFactory.getDao(BlockDao.class);
		List<Block> blocks = blockDao.findAllByEngineSerialNumber(esn);

		if(blocks.size() > 0) {
			throw new TaskException("Engine already married: " + esn);
		}
		
		Block block = blockDao.findByKey(blockId);
		if(block == null) {
			throw new TaskException("Block does not exist: " + blockId);
		}
		
		if(!StringUtils.isEmpty(block.getEngineSerialNumber())) {
			throw new TaskException("Block already married: " + blockId);
		}
		
		block.setEngineSerialNumber(esn);
		blockDao.save(block);
	}
}
