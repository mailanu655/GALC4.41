package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.BitLoc;
import com.honda.galc.client.device.plc.ByteOrder;
import com.honda.galc.client.device.plc.IMemoryLoc;
import com.honda.galc.client.device.plc.IPlcMemory;
import com.honda.galc.client.device.plc.RegisterLoc;
import com.honda.galc.entity.conf.PlcMemoryMapItem;


/**
 * @author Subu Kathiresan
 * @date Nov 2, 2012
 */
public class PlcMemory implements IPlcMemory {

    private IMemoryLoc _memoryLoc;					// could represent a Register or a Bit (FINS requires different memory bank codes)
    private int _register;		    				// address of the target register within the bank
    private int _bitAddress = -1;					// valid values are between 0 and 15
    private int _numberOfRegisters = 1; 			// must be between 0 and 256 (0h - 0100h)
    private ByteOrder _byteOrder = ByteOrder.none;	// H or L
    
    public PlcMemory () {}
    
    /**
     * memoryAddress should be in one of the following formats 
     * E0.100    --> Bank E0, Register 100
     * E0.100.15 --> Bank E0, Register 100, Bit 15 
     * E0.100.H  --> Bank E0, Register 100, higher order byte
     * E0.100.L  --> Bank E0, Register 100, lower order byte
     * 
     * @param memoryAddress
     */
    public PlcMemory(String memoryAddress) {
    	String[] parsedVal = memoryAddress.split("\\.");
    	_memoryLoc = Enum.valueOf(RegisterLoc.class, parsedVal[0]);
    	
    	if (parsedVal.length > 1) {
    		_register = Integer.parseInt(parsedVal[1]);
    		if (_register < 0 || _register > 65535) {
    			throw new IllegalArgumentException("Invalid memory address:  " + _register + " --> Needs to be between 0 and 65535");
    		}
        }
    	
        if (parsedVal.length > 2) {
        	if (parsedVal[2].trim().equals("H"))
        		_byteOrder = ByteOrder.high;
        	else if (parsedVal[2].trim().equals("L"))
        		_byteOrder = ByteOrder.low;
        	else {
        		_memoryLoc = Enum.valueOf(BitLoc.class, parsedVal[0]);
        		_bitAddress = Integer.parseInt(parsedVal[2]);
        	}
        }	
    }

    /**
     * 
     * @param memoryAddress
     * @param numberOfRegisters
     */
    public PlcMemory(String memoryAddress, int numberOfRegisters) {
    	this(memoryAddress);
        if (numberOfRegisters < 0 || numberOfRegisters > 256) {
            throw new IllegalArgumentException("Invalid memory size:  " + numberOfRegisters + " --> Needs to be between 0 and 256");
        }
    	_numberOfRegisters = numberOfRegisters;
    }
    
    /**
     * 
     * @param memoryLoc
     * @param register
     */
    public PlcMemory(IMemoryLoc memoryLoc, int register) {
        if (register < 0 || register > 65535) {
            throw new IllegalArgumentException("Invalid memory address:  " + register + " --> Needs to be between 0 and 65535");
        }
        _memoryLoc = memoryLoc;
        _register = register;
    }
    
    /**
     * 
     * @param memoryLoc
     * @param register
     * @param numberOfRegisters
     */
    public PlcMemory(IMemoryLoc memoryLoc, int register, int numberOfRegisters) {
    	this(memoryLoc, register);
        if (numberOfRegisters < 0 || numberOfRegisters > 256) {
            throw new IllegalArgumentException("Invalid memory size:  " + numberOfRegisters + " --> Needs to be between 0 and 256");
        }
        _numberOfRegisters = numberOfRegisters;
    }
    
	public static PlcMemory createPlcMemory(PlcMemoryMapItem item) {
		PlcMemory memory = new PlcMemory(item.getMemoryBank().trim() + "." + Integer.parseInt(item.getStartAddress().trim()));
		memory.setNumberOfRegisters((item.getLength() + 1)/2);
		
    	if (item.getByteOrder().trim().equals("H"))
    		memory.setByteOrder(ByteOrder.high);
    	else if (item.getByteOrder().trim().equals("L"))
    		memory.setByteOrder(ByteOrder.low);
		if (item.getBitIndex() != null)
			memory.setBitAddress(item.getBitIndex());
		return memory;
	}
	    
    public void setBitAddress(int bitAddress) {
    	// if bit address is set use BitLoc, else use RegisterLoc (they have different bank codes)
    	if (bitAddress != -1) {
    		_memoryLoc = Enum.valueOf(BitLoc.class, _memoryLoc.name());
    	} else {
    		_memoryLoc = Enum.valueOf(RegisterLoc.class, _memoryLoc.name());
    	}
        _bitAddress = bitAddress;
    }
    
    public ByteOrder getByteOrder() {
        return _byteOrder;
    }
    
    public void setByteOrder(ByteOrder byteOrder) {
    	_byteOrder = byteOrder;
    }
    
    public int getNumberOfRegisters() {
    	return _numberOfRegisters;
    }
    
    public void setNumberOfRegisters(int numOfRegisters) {
    	_numberOfRegisters = numOfRegisters;
    }

    public int getRegister() {
        return _register;
    }
    
    public void setRegister(int register) {
        _register = register;
    }
    
    public int getBitAddress() {
        return _bitAddress;
    }
    
    public IMemoryLoc getMemoryLoc() {
    	return _memoryLoc;
    }
    
    public int getBankCode() {
    	return getMemoryLoc().code();
    }

    public String toString() {
        return ("MEMORY:  " + _memoryLoc.name() + ":" + getRegister() + "[" + getNumberOfRegisters() + "]");
    }
}

