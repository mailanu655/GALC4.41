# Project Structure

## Overview

The Stamp Storage System is organized into several key modules, each responsible for specific functionality. This document outlines the structure and purpose of each component.

## Core Modules

### 1. StampStorageDomain
Location: `branches/STAMPSTORAGE-EXPANSION/StampStorageDomain/`

Contains the core domain model and business logic:
- Entity definitions
- Business rules
- Data access objects
- Domain services

Key Components:
```
src/main/java/com/honda/mfg/stamp/conveyor/domain/
├── Carrier.java           # Core carrier entity
├── Die.java              # Die information
├── StorageRow.java       # Storage location management
├── Stop.java             # Stop/station definitions
├── enums/                # Domain enumerations
│   ├── CarrierStatus.java
│   ├── Press.java
│   └── StorageArea.java
└── messages/             # Domain messages
```

### 2. StampStorageManager
Location: `branches/STAMPSTORAGE-EXPANSION/StampStorageManager/`

Handles storage and routing logic:
- Storage management
- Carrier routing
- State management
- Business rules implementation

Key Components:
```
src/main/java/com/honda/mfg/stamp/conveyor/manager/
├── Storage.java          # Main storage interface
├── StorageImpl.java      # Storage implementation
├── StorageState.java     # System state management
├── StoreInManager.java   # Storage intake logic
└── StoreOutManager.java  # Retrieval logic
```

### 3. Device Integration
Location: `branches/STAMPSTORAGE-EXPANSION/Device/`

Manages hardware communication:
- PLC integration
- RFID device management
- Protocol implementations

Key Components:
```
src/main/java/com/honda/mfg/device/
├── plc/                  # PLC communication
├── rfid/                 # RFID device management
└── mesmodule/           # MES integration
```

### 4. MES Integration
Location: `branches/STAMPSTORAGE-EXPANSION/MesProxy/`

Handles MES communication:
- Message parsing
- Protocol conversion
- Connection management

Key Components:
```
src/main/java/com/honda/mfg/mesproxy/
├── MesProxy.java         # Main proxy implementation
├── Connection.java       # Connection management
└── messages/            # Message definitions
```

## Support Modules

### 1. ConnectionDevice
Location: `branches/STAMPSTORAGE-EXPANSION/ConnectionDevice/`

Provides communication infrastructure:
- Socket management
- Protocol implementations
- Connection monitoring

### 2. MesParser
Location: `branches/STAMPSTORAGE-EXPANSION/MesParser/`

Handles MES message parsing:
- JSON conversion
- Message validation
- Data transformation

## Database Scripts
Location: `documentation/Database/`

Contains database management scripts:
- Schema definitions
- Table creation
- Stored procedures
- Triggers

## Configuration
Location: `provisioning/`

System configuration files:
- Environment settings
- Network configuration
- System parameters
- Deployment scripts

## Testing Resources
Location: `documentation/functional_testing/`

Contains test scenarios and resources:
- Functional test cases
- Test data
- Validation scripts
- Performance test scenarios

## Development Guidelines

1. **Module Independence**
   - Each module should have minimal dependencies
   - Use interfaces for cross-module communication
   - Maintain clear separation of concerns

2. **Code Organization**
   - Follow package naming conventions
   - Group related functionality
   - Maintain consistent file structure

3. **Testing**
   - Unit tests for each module
   - Integration tests for interfaces
   - Functional tests for workflows

4. **Documentation**
   - Maintain up-to-date documentation
   - Document public APIs
   - Include usage examples
