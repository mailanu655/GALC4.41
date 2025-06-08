/**
 * 
 */
package com.honda.galc.client.device.plc;

/**
 * @author Subu Kathiresan
 * @date Nov 2, 2012
 */
public interface IPlcMemory {
	
    public int getBankCode();

    public int getRegister();
    
    public int getBitAddress();
    
    public ByteOrder getByteOrder();
 
    public int getNumberOfRegisters();

    public void setNumberOfRegisters(int numOfRegisters);
}
