/**
 * 
 */
package com.honda.galc.web.service;

import java.util.ArrayList;
import java.util.Random;


/**
 * @author Subu Kathiresan
 * @date June 04, 2012
 *
 */
public class FactoryNewsService implements IWebService {
	
	private static final long serialVersionUID = 2610765270038742135L;
	
	public FactoryNewsService() {}
	
	public ArrayList<FactoryNewsCurrent> getUpdate() {
		ArrayList<FactoryNewsCurrent> currInvList = new ArrayList<FactoryNewsCurrent>();
		Random random = new Random();
		FactoryNewsCurrent curInv1 = new FactoryNewsCurrent();
		curInv1.divisionName = "WE";
		curInv1.lineName = "WE TEST1";
		curInv1.nextLineName = "WE TEST2";
		curInv1.plan = random.nextInt() % 10;
		if (curInv1.plan < 0)
			curInv1.plan = curInv1.plan * -1;
		currInvList.add(curInv1);
		
		FactoryNewsCurrent curInv2 = new FactoryNewsCurrent();
		curInv2.divisionName = "WE";
		curInv2.lineName = "WE TEST2";
		curInv2.nextLineName = "PA TEST1";
		curInv2.plan = random.nextInt() % 10;
		if (curInv2.plan < 0)
			curInv2.plan = curInv2.plan * -1;
		currInvList.add(curInv2);
		
		FactoryNewsCurrent curInv3 = new FactoryNewsCurrent();
		curInv3.divisionName = "PA";
		curInv3.lineName = "PA TEST1";
		curInv3.nextLineName = "PA TEST2";
		curInv3.plan = random.nextInt() % 10;
		if (curInv3.plan < 0)
			curInv3.plan = curInv3.plan * -1;
		currInvList.add(curInv3);
		
		FactoryNewsCurrent curInv4 = new FactoryNewsCurrent();
		curInv4.divisionName = "PA";
		curInv4.lineName = "PA TEST2";
		curInv4.nextLineName = "AF TEST1";
		curInv4.plan = random.nextInt() % 10;
		if (curInv4.plan < 0)
			curInv4.plan = curInv4.plan * -1;
		currInvList.add(curInv4);
		
		FactoryNewsCurrent curInv5 = new FactoryNewsCurrent();
		curInv5.divisionName = "AF";
		curInv5.lineName = "AF TEST1";
		curInv5.nextLineName = "AF TEST2";
		curInv5.plan = random.nextInt() % 10;
		if (curInv5.plan < 0)
			curInv5.plan = curInv5.plan * -1;
		currInvList.add(curInv5);
		
		FactoryNewsCurrent curInv6 = new FactoryNewsCurrent();
		curInv6.divisionName = "AF";
		curInv6.lineName = "AF TEST2";
		curInv6.nextLineName = "VQ TEST1";
		curInv6.plan = random.nextInt() % 10;
		if (curInv6.plan < 0)
			curInv6.plan = curInv6.plan * -1;
		currInvList.add(curInv6);
		
		FactoryNewsCurrent curInv7 = new FactoryNewsCurrent();
		curInv7.divisionName = "VQ";
		curInv7.lineName = "VQ TEST1";
		curInv7.nextLineName = "VQ TEST2";
		curInv7.plan = random.nextInt() % 10;
		if (curInv7.plan < 0)
			curInv7.plan = curInv7.plan * -1;
		currInvList.add(curInv7);
		
		FactoryNewsCurrent curInv8 = new FactoryNewsCurrent();
		curInv8.divisionName = "VQ";
		curInv8.lineName = "VQ TEST2";
		curInv8.nextLineName = "VQ TEST3";
		curInv8.plan = random.nextInt() % 10;
		if (curInv8.plan < 0)
			curInv8.plan = curInv8.plan * -1;
		currInvList.add(curInv8);
		
		FactoryNewsCurrent curInv9 = new FactoryNewsCurrent();
		curInv9.divisionName = "VQ";
		curInv9.lineName = "VQ TEST3";
		curInv9.nextLineName = "VQ TEST4";
		curInv9.plan = random.nextInt() % 10;
		if (curInv9.plan < 0)
			curInv9.plan = curInv9.plan * -1;
		currInvList.add(curInv9);
		
		FactoryNewsCurrent curInv10 = new FactoryNewsCurrent();
		curInv10.divisionName = "VQ";
		curInv10.lineName = "VQ TEST4";
		curInv10.nextLineName = "VQ TEST5";
		curInv10.plan = random.nextInt() % 10;
		if (curInv10.plan < 0)
			curInv10.plan = curInv10.plan * -1;
		currInvList.add(curInv10);
		
		FactoryNewsCurrent curInv11 = new FactoryNewsCurrent();
		curInv11.divisionName = "VQ";
		curInv11.lineName = "VQ TEST5";
		curInv11.nextLineName = "VQ TEST6";
		curInv11.plan = random.nextInt() % 10;
		if (curInv11.plan < 0)
			curInv11.plan = curInv11.plan * -1;
		currInvList.add(curInv11);
		
		FactoryNewsCurrent curInv12 = new FactoryNewsCurrent();
		curInv12.divisionName = "VQ";
		curInv12.lineName = "VQ TEST6";
		curInv12.nextLineName = "VQ TEST7";
		curInv12.plan = random.nextInt() % 10;
		if (curInv12.plan < 0)
			curInv12.plan = curInv12.plan * -1;
		currInvList.add(curInv12);
		
		return currInvList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
