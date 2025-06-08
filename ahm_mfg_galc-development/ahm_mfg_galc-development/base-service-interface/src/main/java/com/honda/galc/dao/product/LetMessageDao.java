package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Jan 21, 2016
 */
public interface LetMessageDao extends IDaoService<LetMessage, Long> {

	public LetMessage findDataByTimeStamp(Timestamp sqlTimestamp);

	public List<Long> findMsgIdsByStatusAndTs(Timestamp actualTimestamp, List<String> status);

	public List<Object[]> findAllMessages(Integer spoolId,String status, String productTxt, long startIndex, long endIndex);
	
	public long getTotalLetMsgCount(Integer spoolId, String status, String productTxt);

	public int updateStatusAndDurationByMessageId(long messageId, String status, double duration);

	public List<Long> findAllMsg(Integer spoolId, String status, String productTxt, int maxRecords);
	
    public List<Long> findAllMessageIds(String programName, String productId, FrameSpec spec, Timestamp startDate, Timestamp endDate, String lineNo, String production);

    public List<Map<String, Object>> findAllProgramResultData(String programName, List<Long> messageIds);
    
    public List<LetMessage> findAllByProductId(String productId);
    
    public List<LetMessage> findAll(String productId, String totalStatus);
}