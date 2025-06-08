package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.util.Hashtable;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class KnPruningAbstractTask extends OifAbstractTask implements IEventTaskExecutable
{
	private String productionDate = new String();
    private int batchSize = 0;
    private int sleepMilliseconds = 0;
    private int max = 0;
    private int days = 0;
    private Hashtable<String, Integer> pruningCounts = new Hashtable<String, Integer>();

    public KnPruningAbstractTask(String name) 
    {
		super(name);
	}
    public void execute(Object[] args) 
    {
    }
    protected void getPruningParameters(String component)
    {
        PropertyService.refreshComponentProperties(component);
        setBatchSize        (PropertyService.getPropertyInt(component, "BATCH_SIZE", 50));
        setSleepMilliseconds(PropertyService.getPropertyInt(component, "SLEEP_TIME", 1000));
        setMax              (PropertyService.getPropertyInt(component, "MAX_QTY", 100));
        setDays             (PropertyService.getPropertyInt(component, "BUSINESS_DAYS_TO_KEEP", 100));
        if(getBatchSize()         == 0) setBatchSize(50);
        if(getSleepMilliseconds() < 10) setSleepMilliseconds(1000);
        if(getMax()               == 0) setMax(100);
        if(getDays()              == 0) setDays(100);
        logger.info("     Batch size:              " + getBatchSize());        
        logger.info("     Max number to prune:     " + getMax());
        logger.info("     Business days to keep:   " + getDays());     
        logger.info("     Sleep time:              " + getSleepMilliseconds());
    }
    protected String getPruningDate(int days)
    {
        String productionDate = null;
        productionDate = getDao(SubProductShippingDao.class).findMinPruningDate(days);
        logger.info("Prune data more than "  + Integer.toString(days) + " business days old");
        logger.info("Prune data older than " + productionDate);        
        return productionDate;
    }
    protected void processCounts(String tableName, int count) 
    {
        int total = 0;
        logger.info(" Removing " + count + " rows from " + tableName + " table");
        
        if (getPruningCounts().containsKey(tableName))
        {
            total = getPruningCounts().get(tableName) + count;
        }
        else
        {
            total = count;
        }
        getPruningCounts().put(tableName, total);
    }

    protected void displayCounts() 
    {
        logger.info("Pruning Totals ");
        logger.info("---------------");
        Object[] results = getPruningCounts().keySet().toArray();
        
        for (int i = 0; i < results.length; i++)
        {
            logger.info(results[i] + " rows deleted =  " + getPruningCounts().get(results[i]));
        }
    }    
    public int getBatchSize()
    {
        return batchSize;
    }

    public int getDays()
    {
        return days;
    }

    public int getMax()
    {
        return max;
    }

    public String getProductionDate()
    {
        return productionDate;
    }

    public int getSleepMilliseconds()
    {
        return sleepMilliseconds;
    }

    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    public void setProductionDate(String productionDate)
    {
        this.productionDate = productionDate;
    }

    public void setSleepMilliseconds(int sleepMilliseconds)
    {
        this.sleepMilliseconds = sleepMilliseconds;
    }

    public Hashtable<String, Integer> getPruningCounts()
    {
        return pruningCounts;
    }

    public void setPruningCounts(Hashtable<String, Integer> pruningCounts)
    {
        this.pruningCounts = pruningCounts;
    }
}
