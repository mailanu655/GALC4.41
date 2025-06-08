package com.honda.galc.device.bbscale;

import java.util.List;
import com.honda.galc.device.dataformat.InputData;

public class VehicleWeight extends InputData{
	
	String vin;
	List<ScaleWeight> scaleWeights;
	
	public VehicleWeight(String vin, List<ScaleWeight> newScaleWeights) {
		setScaleWeights(newScaleWeights);
		setVin(vin);
	}
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public List<ScaleWeight> getScaleWeights() {
		return scaleWeights;
	}
	
	public void setScaleWeights(List<ScaleWeight> newScaleWeights) {
		this.scaleWeights = newScaleWeights;
		if(scaleWeights == null)  return;
		int listSize = scaleWeights.size();
		if(listSize > 4)  {
			//just want to make sure we don't pick up extraneous elements
			scaleWeights.subList(4, listSize-1).clear();
		}
		if(listSize < 4)  return;
		
		double lfrt = scaleWeights.get(0).getWeight();
		double lrr = scaleWeights.get(1).getWeight();
		double rfrt = scaleWeights.get(2).getWeight();
		double rrr = scaleWeights.get(3).getWeight();
		
		ScaleWeight sLcr = new ScaleWeight();
		sLcr.setCorner(Corner.BBSCALES_LCR);
		sLcr.setWeight(rfrt + lrr);
		sLcr.setStable(true);
		scaleWeights.add(sLcr);
		
		ScaleWeight sRcr = new ScaleWeight();
		sRcr.setCorner(Corner.BBSCALES_RCR);
		sRcr.setWeight(lfrt + rrr);
		sRcr.setStable(true);
		scaleWeights.add(sRcr);
		
		ScaleWeight sTot = new ScaleWeight();
		sTot.setCorner(Corner.BBSCALES_TOTAL);
		sTot.setWeight(lfrt + rrr + rfrt + lrr);
		sTot.setStable(true);
		scaleWeights.add(sTot);
		
		
		ScaleWeight sCrDiff = new ScaleWeight();
		sCrDiff.setCorner(Corner.BBSCALES_CRDIFF);
		sCrDiff.setWeight((lfrt + rrr) - (rfrt + lrr));
		sCrDiff.setStable(true);
		scaleWeights.add(sCrDiff);
		
		
	}
	
}
