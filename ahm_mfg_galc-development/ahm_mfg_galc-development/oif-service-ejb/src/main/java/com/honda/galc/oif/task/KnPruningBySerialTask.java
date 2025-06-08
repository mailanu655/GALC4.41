package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.PartLot;

public class KnPruningBySerialTask extends KnPruningAbstractTask 
{
	private static String OIF_KN_PRUNING_BY_SERIAL = "OIF_KN_PRUNING_BY_SERIAL";
    private ArrayList pruningSerialNumbers   = new ArrayList();

    public KnPruningBySerialTask(String name) 
    {
		super(name);
	}
	
	public void execute(Object[] args) 
	{
        logger.info("start knuckles pruning process - by Part Serial");
		getPruningParameters(OIF_KN_PRUNING_BY_SERIAL);        
        setProductionDate(getPruningDate(getDays()));
		setPruningSerialNumbers(loadSerialNumbersForPruning(getMax(), getProductionDate()));
        
		if (getPruningSerialNumbers().size() > 0)
		{
            pruneSerialNumbers(getPruningSerialNumbers());
		}
        displayCounts();
		logger.info("end knuckles pruning process - by Part Serial");
	}

    private ArrayList loadSerialNumbersForPruning(int maxQty, String productionDate) 
	{
        int   count  = 0;
		ArrayList<PartLot> serialNumbers = new ArrayList<PartLot>();
		logger.info("Loading data to be pruned");
		
		List<PartLot> partLot = null;
		partLot = getDao(PartLotDao.class).findAllByStatusAndDate(productionDate + " 00:00:00.000000", (short)PartLotStatus.CLOSED.getId());

		if(!partLot.isEmpty()) 
		{
			try
			{
				for(PartLot eligiblePartLot : partLot)
				{
					if (!serialNumbers.contains(eligiblePartLot))
					{
                        if (count == maxQty)
                        {
                            return serialNumbers;
                        }
                        logger.info(" Serial: " +  eligiblePartLot.getId().getPartSerialNumber() +
                                    " Number: " +  eligiblePartLot.getId().getPartNumber() +
                                    " Name: "   +  eligiblePartLot.getId().getPartName() + " added");
						serialNumbers.add(eligiblePartLot);
                        count++;
                    }
				}
			}
			catch(Exception e) 
			{
				logger.error(e,"Loading Serial Numbers to be pruned. Exception occurs:");
				return serialNumbers;
			}
		}
		return serialNumbers;
	}

    private void pruneSerialNumbers(ArrayList pruningSerialNumbers)
    {
        int startBatchIndex         = 0;
        int endBatchIndex           = getBatchSize();
        
        Object[] results = getPruningSerialNumbers().toArray();
        if (endBatchIndex > results.length)             // handles when batch size is greater than number of lots
        {
            endBatchIndex = results.length;
        }

        while(startBatchIndex < results.length)
        {
            logger.info("Processing Part Serial Numbers from " + (startBatchIndex + 1) + " to " + endBatchIndex);            
            for (int i = startBatchIndex; i < endBatchIndex; i++)
            {
                logger.info("Removing data for Part Serial: " + results[i]);            
                removeSerialNumbers((PartLot)results[i]);
            }
            startBatchIndex = endBatchIndex;
            endBatchIndex   = startBatchIndex + getBatchSize();
            if (endBatchIndex > results.length)
            {
                endBatchIndex = results.length;
            }
            try 
            {
                if (startBatchIndex < results.length)
                {
                logger.info("Pausing " + getSleepMilliseconds() + " milliseconds between batches of serial numbers to be pruned");                
                Thread.sleep(getSleepMilliseconds());
                }
            } 
            catch (InterruptedException e) 
            {
                logger.error(e, " Thread sleep error");
                return;
            }
        }
    }

	private void removeSerialNumbers(PartLot partLot) 
	{
        processCounts("PartLot",(int) 
        getDao(PartLotDao.class).delete(
                partLot.getId().getPartSerialNumber(),
                partLot.getId().getPartName(),
                partLot.getId().getPartNumber()));
	}
    
    public ArrayList getPruningSerialNumbers()
    {
        return pruningSerialNumbers;
    }

    public void setPruningSerialNumbers(ArrayList pruningSerialNumbers)
    {
        this.pruningSerialNumbers = pruningSerialNumbers;
    }
}
