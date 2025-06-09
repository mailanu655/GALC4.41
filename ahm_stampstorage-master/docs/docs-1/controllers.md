# StampStorageWeb Controllers Documentation

## Overview
This document provides detailed documentation for the controller classes in the StampStorageWeb application. Each controller handles specific functionality related to different aspects of the stamp storage system.

## Controller Documentation

### CarrierHistoryController
**Purpose**: Manages the history of carrier movements and operations.

**Key Methods**:
- `list()`: Displays paginated carrier history records
- `findCarrierHistoryByCriteria()`: Searches history based on specified criteria
- `create()`, `update()`, `delete()`: Standard CRUD operations

**Database Tables**: 
- CARRIER_HISTORY
- CARRIER_MES

**Flow**:
1. Receives carrier history requests
2. Applies filtering criteria if specified
3. Retrieves records from database
4. Returns formatted results to view

### CarrierMesController
**Purpose**: Handles carrier manufacturing execution system (MES) integration.

**Key Methods**:
- `moving()`: Lists carriers currently in transit
- `list()`: Shows all carriers in system
- `update()`: Updates carrier MES information

**Database Tables**:
- CARRIER_MES
- CARRIER
- DIE
- STOP

**Flow**:
1. Receives MES-related carrier requests
2. Processes carrier status updates
3. Syncs with MES system
4. Updates local database

### CarrierReleaseController
**Purpose**: Manages the release of carriers from storage.

**Key Methods**:
- `create()`: Initiates new carrier release
- `delete()`: Cancels release request
- `list()`: Shows pending releases

**Database Tables**:
- CARRIER_RELEASE
- CARRIER_MES
- STOP

**Flow**:
1. Receives release request
2. Validates release conditions
3. Updates carrier status
4. Triggers release workflow

### ContactController
**Purpose**: Manages contact information for notifications and alerts.

**Key Methods**:
- `create()`: Adds new contact
- `update()`: Modifies contact info
- `list()`: Displays all contacts

**Database Tables**:
- CONTACT

**Flow**:
1. Handles contact management requests
2. Validates contact information
3. Updates contact database
4. Returns confirmation

### DefectController
**Purpose**: Handles defect reporting and tracking.

**Key Methods**:
- `create()`: Records new defect
- `updateForm()`: Displays defect update form
- `delete()`: Removes defect record

**Database Tables**:
- DEFECT
- CARRIER_MES
- DIE

**Flow**:
1. Receives defect report
2. Associates with carrier/die
3. Records defect details
4. Updates tracking system

### DieController
**Purpose**: Manages stamping die information.

**Key Methods**:
- `create()`: Adds new die
- `update()`: Modifies die info
- `list()`: Shows all dies

**Database Tables**:
- DIE
- PART_PRODUCTION_VOLUME

**Flow**:
1. Handles die management requests
2. Validates die specifications
3. Updates die inventory
4. Maintains die status

### GroupHoldController
**Purpose**: Manages group hold operations for carriers.

**Key Methods**:
- `findCarrierByCriteria()`: Locates carriers for hold
- `submitGroupHold()`: Applies hold to group
- `getDefectList()`: Lists defects for held group

**Database Tables**:
- CARRIER_MES
- DEFECT
- STORAGE_ROW

**Flow**:
1. Receives hold request
2. Identifies affected carriers
3. Applies hold status
4. Updates tracking system

### ImageController
**Purpose**: Handles die and part images.

**Key Methods**:
- `getImage()`: Retrieves stored images
- `putTestImage()`: Adds test images
- `handleFileUpload()`: Processes image uploads

**Database Tables**:
- STAMPING_IMAGE

**Flow**:
1. Receives image request
2. Validates image data
3. Processes image
4. Returns/stores image

### LanesController
**Purpose**: Manages storage lane operations.

**Key Methods**:
- `listLanes()`: Shows storage lanes
- `listOverviewRows()`: Displays lane overview
- `update()`: Updates lane status

**Database Tables**:
- STORAGE_ROW
- CARRIER_MES
- CARRIER_RELEASE

**Flow**:
1. Handles lane operations
2. Updates lane status
3. Manages carrier positions
4. Maintains lane inventory

### ModelController
**Purpose**: Manages part models.

**Key Methods**:
- `create()`: Adds new model
- `update()`: Modifies model info
- `list()`: Shows all models

**Database Tables**:
- MODEL
- DIE

**Flow**:
1. Handles model management
2. Validates model data
3. Updates model database
4. Maintains relationships

### OrderFulfillmentController
**Purpose**: Manages order fulfillment process.

**Key Methods**:
- `create()`: Creates fulfillment order
- `update()`: Updates order status
- `delete()`: Cancels order

**Database Tables**:
- ORDER_FULFILLMENT
- ORDER_FULFILLMENT_PK
- WELD_ORDER

**Flow**:
1. Receives fulfillment request
2. Processes order details
3. Updates fulfillment status
4. Tracks completion

### OrderMgrController
**Purpose**: Manages order operations.

**Key Methods**:
- `create()`: Creates new order
- `update()`: Updates order info
- `list()`: Shows all orders

**Database Tables**:
- ORDER_MGR
- WELD_ORDER
- STOP

**Flow**:
1. Handles order management
2. Validates order data
3. Updates order status
4. Maintains order tracking

### ParmSettingController
**Purpose**: Manages system parameters.

**Key Methods**:
- `create()`: Adds parameter
- `update()`: Modifies parameter
- `list()`: Shows parameters

**Database Tables**:
- PARM_SETTING

**Flow**:
1. Handles parameter requests
2. Validates settings
3. Updates configuration
4. Maintains audit trail

### ResetConnectionController
**Purpose**: Manages system connections.

**Key Methods**:
- `ResetNow()`: Resets connections

**Flow**:
1. Receives reset request
2. Validates connection state
3. Performs reset
4. Confirms completion

### StopController
**Purpose**: Manages conveyor stops.

**Key Methods**:
- `create()`: Adds new stop
- `update()`: Modifies stop
- `list()`: Shows all stops

**Database Tables**:
- STOP
- STOP_AREA
- STOP_TYPE

**Flow**:
1. Handles stop management
2. Validates stop data
3. Updates stop status
4. Maintains stop configuration

### StorageRowController
**Purpose**: Manages storage rows.

**Key Methods**:
- `create()`: Adds storage row
- `update()`: Updates row info
- `list()`: Shows all rows

**Database Tables**:
- STORAGE_ROW
- STOP
- STORAGE_AREA

**Flow**:
1. Handles row management
2. Updates row status
3. Manages capacity
4. Maintains configuration

### StorageStateController
**Purpose**: Manages storage system state.

**Key Methods**:
- Legacy controller maintained for compatibility

**Flow**:
1. Historical state management
2. Superseded by newer implementations

### ValidDestinationController
**Purpose**: Manages valid carrier destinations.

**Key Methods**:
- `create()`: Adds destination
- `update()`: Modifies destination
- `list()`: Shows destinations

**Database Tables**:
- VALID_DESTINATION
- STOP

**Flow**:
1. Handles destination management
2. Validates routing rules
3. Updates valid paths
4. Maintains routing configuration

### WeldOrderController
**Purpose**: Manages welding operations.

**Key Methods**:
- `create()`: Creates weld order
- `update()`: Updates order status
- `list()`: Shows orders
- `manuallyCompleteWeldOrder()`: Manual completion

**Database Tables**:
- WELD_ORDER
- ORDER_MGR
- ORDER_FULFILLMENT

**Flow**:
1. Handles weld orders
2. Tracks order status
3. Manages fulfillment
4. Updates completion

### WeldScheduleController
**Purpose**: Manages welding schedules.

**Key Methods**:
- `create()`: Creates schedule
- `update()`: Updates schedule
- `list()`: Shows schedules

**Database Tables**:
- WELD_SCHEDULE

**Flow**:
1. Handles schedule management
2. Updates timelines
3. Maintains schedules
4. Tracks completion

## Common Patterns

### Security
- User authentication required for operations
- Role-based access control
- Audit logging of changes

### Data Validation
- Input validation on all operations
- Business rule enforcement
- Error handling and reporting

### Transaction Management
- ACID compliance
- Rollback on failures
- Consistent state maintenance

### UI Integration
- Model-View-Controller pattern
- Form handling
- Response formatting

### Error Handling
- Exception management
- User feedback
- System logging

## Best Practices

1. **Input Validation**
   - All user input validated
   - Type checking enforced
   - Security measures implemented

2. **Transaction Management**
   - Proper transaction boundaries
   - Consistent state maintenance
   - Rollback handling

3. **Error Handling**
   - Comprehensive error catching
   - User-friendly messages
   - Detailed logging

4. **Security**
   - Authentication required
   - Authorization checked
   - Audit trail maintained

5. **Performance**
   - Efficient queries
   - Proper indexing
   - Connection management
