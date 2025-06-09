# BitInfo Technical Documentation

## Purpose
BitInfo.java provides a flexible bit-flag management system for the stamp storage application, enabling efficient storage and manipulation of multiple boolean flags in a single integer value. This class is primarily used for tracking maintenance-related flags and status indicators for carriers in the system.

## Logic/Functionality
- Manages up to 16 individual boolean flags (bits) in a compact representation
- Provides methods to set, get, and toggle individual bit values
- Supports conversion between bit flags and integer values
- Implements string representation for debugging and logging
- Offers parsing capabilities to convert string representations back to bit values
- Maintains labels for each bit to provide context for their meaning

## Flow
1. A BitInfo object is created, either with default values or specific bit settings
2. Individual bits are set or cleared based on system requirements
3. The BitInfo object is associated with a carrier or other entity
4. The system can query specific bits to determine status or required actions
5. The integer representation can be stored in the database efficiently
6. String representations can be used for logging and debugging

## Key Elements
- Bit flags (bit1 through bit15): Individual boolean flags for various statuses
- `maintRequired`: Special flag indicating maintenance is required
- Bit labels: Descriptive text for each bit to provide context
- `geIntValue()`: Converts all bits to a single integer value
- `showBitsToString()`: Creates a string representation of all bit values
- `parseShowBits()`: Parses a string representation back to bit values

## Usage
```java
// Create a BitInfo object with specific flags set
BitInfo bitInfo = new BitInfo();
bitInfo.setMaintRequired(true);
bitInfo.setShowBit1(true);
bitInfo.setShowBit3(true);

// Set labels for context
bitInfo.setBit1Label("Needs Cleaning");
bitInfo.setBit3Label("Safety Check Required");

// Get the integer representation for storage
int bitValue = bitInfo.geIntValue();
System.out.println("Bit value as integer: " + bitValue);

// Convert to string representation for logging
String bitString = bitInfo.showBitsToString();
System.out.println("Bit string: " + bitString);

// Parse a bit string back to bit values
BitInfo parsedBitInfo = new BitInfo();
parsedBitInfo.parseShowBits("1|1|0|1|0|0|0|0|0|0|0|0|0|0|0|0");

// Check if specific maintenance is required
if (parsedBitInfo.isShowBit1()) {
    System.out.println("Cleaning is required");
}
```

## Debugging and Production Support

### Common Issues
1. Bit values not persisting correctly in the database
2. Confusion about which bit represents which status
3. Inconsistent bit usage across different system components
4. String parsing errors with malformed input
5. Bit labels not matching actual bit usage
6. Integer overflow with improper bit manipulation

### Debugging Steps
1. Verify bit values are correctly set:
   ```java
   BitInfo bitInfo = new BitInfo();
   bitInfo.setShowBit1(true);
   bitInfo.setShowBit5(true);
   
   System.out.println("Bit1: " + bitInfo.isShowBit1());
   System.out.println("Bit5: " + bitInfo.isShowBit5());
   System.out.println("Integer value: " + bitInfo.geIntValue());
   System.out.println("Expected integer value: " + (2 + 32)); // 2^1 + 2^5
   ```

2. Check string representation and parsing:
   ```java
   BitInfo bitInfo = new BitInfo();
   bitInfo.setMaintRequired(true);
   bitInfo.setShowBit2(true);
   
   String bitString = bitInfo.showBitsToString();
   System.out.println("Original bit string: " + bitString);
   
   BitInfo parsedBitInfo = new BitInfo();
   parsedBitInfo.parseShowBits(bitString);
   
   System.out.println("Parsed maintRequired: " + parsedBitInfo.getMaintRequired());
   System.out.println("Parsed bit2: " + parsedBitInfo.isShowBit2());
   ```

3. Validate bit labels:
   ```java
   BitInfo bitInfo = new BitInfo();
   bitInfo.setBit1Label("Cleaning");
   bitInfo.setBit2Label("Lubrication");
   
   System.out.println("Bit1 label: " + bitInfo.getBit1Label());
   System.out.println("Bit2 label: " + bitInfo.getBit2Label());
   ```

### Resolution
- For persistence issues: Ensure proper conversion between BitInfo and database integer values
- For bit meaning confusion: Document bit usage and maintain consistent bit labels
- For inconsistent usage: Establish clear guidelines for bit assignments across the system
- For parsing errors: Add validation and error handling for string parsing
- For label mismatches: Implement a validation system to ensure labels match usage
- For integer overflow: Use proper data types and bounds checking

### Monitoring
- Log bit values before and after database operations
- Track changes to bit values over time for carriers
- Monitor frequency of specific maintenance flags
- Validate bit string formats in logs
- Check for unexpected bit patterns that might indicate system issues
- Periodically review bit usage to ensure alignment with business processes