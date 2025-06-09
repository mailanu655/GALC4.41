# StampStorage Web Controllers Documentation

## CarrierHistoryController
**Purpose**: Manages the history of carrier movements and operations.

**Key Methods**:
- `list()`: Displays paginated carrier history records
- `findCarrierHistoryByCriteria()`: Searches history based on carrier number
- `findCarrierHistoryByCarrierNumber()`: Retrieves specific carrier history

**Database Tables**: 
- CarrierHistory
- CarrierHistoryAudit

**Usage**: Used for tracking and auditing carrier movements through the system.

## CarrierMesController
**Purpose**: Handles communication between the MES system and carrier operations.

**Key Methods**:
- `moving()`: Lists carriers currently in motion
- `list()`: Shows all carriers in the system
- `update()`: Updates carrier status and location

**Database Tables**:
- CarrierMes
- CarrierInfo

**Usage**: Core controller for carrier tracking and MES integration.

## CarrierReleaseController
**Purpose**: Manages the release of carriers from storage locations.

**Key Methods**:
- `create()`: Initiates new carrier release
- `delete()`: Cancels carrier release
- `update()`: Modifies release parameters

**Database Tables**:
- CarrierRelease
- CarrierInfo

**Usage**: Controls carrier movement out of storage areas.

## ContactController
**Purpose**: Manages contact information for notifications and alerts.

**Key Methods**:
- `create()`: Adds new contact
- `update()`: Modifies contact information
- `delete()`: Removes contact

**Database Tables**:
- Contact

**Usage**: Maintains contact information for system notifications.

## DefectController
**Purpose**: Handles defect reporting and tracking.

**Key Methods**:
- `create()`: Records new defect
- `createDefectForm()`: Displays defect entry form
- `update()`: Updates defect information

**Database Tables**:
- Defect
- CarrierMes

**Usage**: Manages quality control and defect tracking.

## DieController
**Purpose**: Manages stamping die information.

**Key Methods**:
- `create()`: Adds new die
- `list()`: Shows all dies
- `update()`: Modifies die information

**Database Tables**:
- Die

**Usage**: Controls die inventory and specifications.

## GroupHoldController
**Purpose**: Manages group hold operations for carriers.

**Key Methods**:
- `findCarrierByCriteria()`: Locates carriers for hold
- `submitGroupHold()`: Applies hold to group
- `getOrderStatusList()`: Retrieves status options

**Database Tables**:
- CarrierMes
- GroupHold

**Usage**: Implements group-level hold operations.

## ImageController
**Purpose**: Handles image storage and retrieval.

**Key Methods**:
- `getImage()`: Retrieves stored images
- `putTestImage()`: Stores test images
- `handleFileUpload()`: Processes image uploads

**Database Tables**:
- StampingImage

**Usage**: Manages system images and visual resources.

## LanesController
**Purpose**: Controls conveyor lane operations.

**Key Methods**:
- `listLanes()`: Shows all lanes
- `getInventory()`: Retrieves lane inventory
- `update()`: Updates lane status

**Database Tables**:
- StorageRow
- CarrierMes

**Usage**: Manages conveyor system lanes and routing.

## ModelController
**Purpose**: Handles vehicle model information.

**Key Methods**:
- `create()`: Adds new model
- `update()`: Modifies model details
- `delete()`: Removes model

**Database Tables**:
- Model

**Usage**: Maintains vehicle model specifications.

## OrderFulfillmentController
**Purpose**: Manages order fulfillment operations.

**Key Methods**:
- `create()`: Creates new fulfillment order
- `update()`: Updates order status
- `delete()`: Cancels order

**Database Tables**:
- OrderFulfillment
- OrderFulfillmentPk

**Usage**: Controls order processing and fulfillment.

## OrderMgrController
**Purpose**: Manages order management operations.

**Key Methods**:
- `create()`: Creates new order
- `list()`: Shows all orders
- `update()`: Updates order details

**Database Tables**:
- OrderMgr
- WeldOrder

**Usage**: Oversees order management system.

## ParmSettingController
**Purpose**: Manages system parameters.

**Key Methods**:
- `create()`: Adds new parameter
- `update()`: Modifies parameter
- `delete()`: Removes parameter

**Database Tables**:
- ParmSetting

**Usage**: Controls system configuration parameters.

## ResetConnectionController
**Purpose**: Handles system connection resets.

**Key Methods**:
- `ResetNow()`: Performs connection reset

**Usage**: Manages system connectivity issues.

## StopController
**Purpose**: Manages conveyor stop points.

**Key Methods**:
- `create()`: Adds new stop
- `update()`: Modifies stop details
- `delete()`: Removes stop

**Database Tables**:
- Stop

**Usage**: Controls conveyor system stop points.

## StorageRowController
**Purpose**: Manages storage row operations.

**Key Methods**:
- `create()`: Creates new storage row
- `update()`: Updates row configuration
- `delete()`: Removes row

**Database Tables**:
- StorageRow
- Stop

**Usage**: Controls storage area organization.

## StorageStateController
**Purpose**: Manages storage system state.

**Key Methods**:
- `refreshStorageState()`: Updates storage state

**Usage**: Maintains storage system status.

## ValidDestinationController
**Purpose**: Manages valid carrier destinations.

**Key Methods**:
- `create()`: Adds new destination
- `update()`: Modifies destination
- `delete()`: Removes destination

**Database Tables**:
- ValidDestination
- Stop

**Usage**: Controls carrier routing rules.

## WeldOrderController
**Purpose**: Manages welding operations.

**Key Methods**:
- `create()`: Creates new weld order
- `update()`: Updates order status
- `delete()`: Cancels order

**Database Tables**:
- WeldOrder
- OrderFulfillment

**Usage**: Controls welding process orders.

## WeldScheduleController
**Purpose**: Manages welding schedules.

**Key Methods**:
- `create()`: Creates new schedule
- `update()`: Updates schedule
- `delete()`: Removes schedule

**Database Tables**:
- WeldSchedule

**Usage**: Controls welding operation scheduling.
