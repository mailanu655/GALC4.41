package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.service.ServiceFactory;


public class SequenceDaoImpl extends BaseDaoImpl<Sequence,String> implements SequenceDao {

	public Sequence getNextSequence(String sequenceName) {
		Sequence currSeq = findByKey(sequenceName);
				
		if (currSeq.getCurrentSeq() < currSeq.getEndSeq() && currSeq.getCurrentSeq() >= currSeq.getStartSeq() ) {
			currSeq.setCurrentSeq(currSeq.getCurrentSeq() + currSeq.getIncrementValue());
		} else {
			currSeq.setCurrentSeq(currSeq.getStartSeq());
		}
		return currSeq;
	}
	
	public Sequence getNextSequence(String sequenceName, Boolean update) {
		Sequence currentSequence = getNextSequence(sequenceName);
		if (update) {
			ServiceFactory.getDao(SequenceDao.class).update(currentSequence);
		}
		return currentSequence;
	}
	
}
