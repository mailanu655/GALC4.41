package com.honda.mfg.stamp.conveyor.service;

public final class StorageServiceRuntimeStats {

    private StorageServiceRuntimeStats(){

    }
	
	public static String getStatsAsString()  {
		
		Runtime rt = Runtime.getRuntime();
		StringBuilder stats = new StringBuilder();
		stats.append("Total memory:").append(rt.totalMemory())
				.append("\nMax Memory").append(rt.totalMemory())
				.append("\nFree Memory").append(rt.freeMemory())
				.append("\nAvailable processors").append(rt.availableProcessors());
		return stats.toString();
				
	}

}
