package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SkippedProductDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.oif.task.common.PruningData;

public class KnPruningByLotTask extends KnPruningAbstractTask 
{
	private static String OIF_KN_PRUNING_BY_LOT = "OIF_KN_PRUNING_BY_LOT";
    private Hashtable<String, PruningData>   pruningProdLots= new Hashtable<String, PruningData>();    

    public KnPruningByLotTask(String name) 
    {
		super(name);
	}
	
	public void execute(Object[] args) 
	{
        logger.info("start knuckles pruning process - by Lot");
		getPruningParameters(OIF_KN_PRUNING_BY_LOT);        
        setProductionDate(getPruningDate(getDays()));
        setPruningProdLots(loadProdLotsForPruning(getMax(), getProductionDate()));        
        
		if (getPruningProdLots().size() > 0)
		{
            pruneLots(getPruningProdLots());
		}
        displayCounts();
		logger.info("end knuckles pruning process - by Lot");
	}


    private Hashtable<String, PruningData> loadProdLotsForPruning(int maxQty, String productionDate) 
    {
        int    count       = 0;
        String prevProdLot   = "";
        
        Hashtable<String, PruningData> lots = new Hashtable<String, PruningData>();
        PruningData lot     = new PruningData();
        logger.info("Loading data to be pruned");
        
        List<SubProduct> subProduct = null;
        subProduct = getDao(SubProductDao.class).findAllShipped(productionDate);

        if(!subProduct.isEmpty()) 
        {
            try
            {
                for(SubProduct shippedSubProduct : subProduct)
                {
                    lot     = new PruningData();

                    if (prevProdLot.equalsIgnoreCase(""))
                    {
                        prevProdLot = shippedSubProduct.getProductionLot();
                    }
                    if (lots.containsKey(shippedSubProduct.getProductionLot()))
                    {
                        lot = lots.get(shippedSubProduct.getProductionLot());                        
                        if (!lot.getKdLots().contains(shippedSubProduct.getKdLotNumber())) 
                        {
                            logger.info(" KD Lot  : " +  shippedSubProduct.getKdLotNumber() + " added");
                            lot.getKdLots().add(shippedSubProduct.getKdLotNumber());
                        }
                        if (!lot.getProdIds().contains(shippedSubProduct.getProductId())) 
                        {
                            logger.info(" Prod Id : " +  shippedSubProduct.getProductId() + " added");
                            lot.getProdIds().add(shippedSubProduct.getProductId());
                        }
                    }
                    else
                    {
                        if (!shippedSubProduct.getProductionLot().equalsIgnoreCase(prevProdLot))
                        {
                            count++;
                            prevProdLot = shippedSubProduct.getProductionLot();
                            if (count == maxQty)
                            {
                                return lots;
                            }
                        }
                        logger.info(" KD Lot  : " +  shippedSubProduct.getKdLotNumber()   + " added");
                        logger.info(" Prod Lot: " +  shippedSubProduct.getProductionLot() + " added");
                        logger.info(" Prod Id : " +  shippedSubProduct.getProductId()     + " added");
                        lot.setProdLotNumber(shippedSubProduct.getProductionLot());
                        lot.getKdLots().add(shippedSubProduct.getKdLotNumber());
                        lot.getProdIds().add(shippedSubProduct.getProductId());
                        lots.put(shippedSubProduct.getProductionLot(),lot);
                    }
                }
            }
            catch(Exception e) 
            {
                logger.error(e,"Loading Lots to be pruned. Exception occurs:");
                return lots;
            }
        }
        return lots;
    }
    
    private void pruneLots(Hashtable<String, PruningData> pruningLots)
    {
        int startBatchIndex       = 0;
        int endBatchIndex         = getBatchSize();
        PruningData pruningData   = new PruningData();
        
        Object[] results = getSortedPruningProdLots();
        if (endBatchIndex > results.length)             // handles when batch size is greater than number of lots
        {
            endBatchIndex = results.length;
        }

        while(startBatchIndex < results.length)
        {
            logger.info("Processing Lots from " + (startBatchIndex + 1) + " to " + endBatchIndex);            
            for (int i = startBatchIndex; i < endBatchIndex; i++)
            {
                pruningData = (PruningData) results[i];
                logger.info("Removing data for Prod Lot: " + pruningData.getProdLotNumber());            
                removeProdIds(pruningData.getProdIds());
                removeProdLots(pruningData.getProdLotNumber());
                removeKdLots(pruningData.getKdLots());
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
                logger.info("Pausing " + getSleepMilliseconds() + " milliseconds between batches of lots to be pruned");                
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

	private void removeKdLots(ArrayList<String> kdLotsArray) 
	{
        processCounts("SubProductShippingDetail",(int) getDao(SubProductShippingDetailDao.class).deleteKdLots("KUNCKLE",kdLotsArray));
        processCounts("SubProductShipping"      ,(int) getDao(SubProductShippingDao.class      ).deleteKdLots("KUNCKLE",kdLotsArray));
        processCounts("SubProduct"              ,(int) getDao(SubProductDao.class              ).deleteKdLots("KUNCKLE",kdLotsArray));
	}

    private void removeProdLots(String prodLot) 
    {
        processCounts("PreProductionLot",(int) getDao(PreProductionLotDao.class).delete(prodLot));
        processCounts("ProductionLot"   ,(int) getDao(ProductionLotDao.class   ).delete(prodLot));
    }

    private void removeProdIds(ArrayList<String> prodIdsArray) 
    {
        processCounts("InProcessProduct",(int) getDao(InProcessProductDao.class).deleteProdIds(prodIdsArray));
        processCounts("InstalledPart"   ,(int) getDao(InstalledPartDao.class   ).deleteProdIds(prodIdsArray));
        processCounts("Measurement"     ,(int) getDao(MeasurementDao.class     ).deleteProdIds(prodIdsArray));
        processCounts("SkippedProduct"  ,(int) getDao(SkippedProductDao.class  ).deleteProdIds(prodIdsArray));
        processCounts("ProductResult"   ,(int) getDao(ProductResultDao.class   ).deleteProdIds(prodIdsArray));        
        processCounts("ProductSequence" ,(int) getDao(ProductSequenceDao.class ).deleteProdIds(prodIdsArray));
    }

    private Object[] getSortedPruningProdLots()
    {
        Object[] results = getPruningProdLots().values().toArray();
        Arrays.sort(results);
        return results;
    }

    public Hashtable<String, PruningData> getPruningProdLots()
    {
        return pruningProdLots;
    }

    public void setPruningProdLots(Hashtable<String, PruningData> pruningProdLots)
    {
        this.pruningProdLots = pruningProdLots;
    }    



	
}
