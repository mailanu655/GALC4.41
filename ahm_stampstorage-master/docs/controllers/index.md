# StampStorage Controller Documentation

This directory contains technical documentation for the controller classes in the StampStorage system. Each controller is responsible for handling specific aspects of the system's functionality, from carrier management to image handling.

## Controller Overview

| Controller | Purpose |
|------------|---------|
| [CarrierHistoryController](CarrierHistoryController.md) | Manages the history of carrier movements and status changes in the system. |
| [CarrierMesController](CarrierMesController.md) | Manages the direct interaction with the MES (Manufacturing Execution System) for carrier data. |
| [CarrierReleaseController](CarrierReleaseController.md) | Handles the release of carriers from storage to their destinations. |
| [ContactController](ContactController.md) | Manages contact information for alarm notifications and system communications. |
| [DefectController](DefectController.md) | Handles the recording and management of defects associated with carriers. |
| [DieController](DieController.md) | Manages die information, which represents the tooling used to produce parts. |
| [GroupHoldController](GroupHoldController.md) | Provides functionality for applying holds to groups of carriers based on various criteria. |
| [ImageController](ImageController.md) | Manages image resources for visual representation of dies and other components. |
| [LanesController](LanesController.md) | Manages the visualization and manipulation of storage lanes in the system. |
| [ModelController](ModelController.md) | Manages vehicle models and their associated dies. |
| [OrderFulfillmentController](OrderFulfillmentController.md) | Handles the fulfillment of orders by managing carrier assignments to orders. |
| [OrderMgrController](OrderMgrController.md) | Manages order managers, which coordinate order processing for specific production lines. |
| [ParmSettingController](ParmSettingController.md) | Manages system parameter settings that control various aspects of system behavior. |
| [ResetConnectionController](ResetConnectionController.md) | Provides functionality to reset connections to external services when issues arise. |
| [StopController](StopController.md) | Manages stop locations, which represent physical locations in the conveyor system. |
| [StorageRowController](StorageRowController.md) | Manages storage rows, which are collections of carriers in specific storage areas. |
| [StorageStateController](StorageStateController.md) | Manages the overall state of the storage system, including carrier positions and availability. |
| [ValidDestinationController](ValidDestinationController.md) | Manages valid destination configurations for carrier routing. |
| [WeldOrderController](WeldOrderController.md) | Manages weld orders, which represent production orders for welding operations. |
| [WeldScheduleController](WeldScheduleController.md) | Manages weld schedules, which coordinate the timing of welding operations. |

## Common Patterns

The controllers in the StampStorage system follow several common patterns:

1. **RESTful Design**: Most controllers implement standard RESTful endpoints for CRUD operations (Create, Read, Update, Delete).
2. **Spring MVC**: All controllers use Spring MVC annotations for request mapping and parameter binding.
3. **Validation**: Controllers validate input data before processing to ensure data integrity.
4. **Exception Handling**: Controllers include exception handling to provide informative error messages.
5. **Logging**: Controllers log significant events for auditing and troubleshooting purposes.
6. **Security**: Controllers often check user permissions and log user actions for accountability.
7. **Service Layer Integration**: Controllers delegate business logic to service classes, particularly the CarrierManagementService and CarrierManagementServiceProxy.

## Key Relationships

The controllers in the StampStorage system have several key relationships:

1. **Carrier Management**: Multiple controllers (CarrierController, CarrierHistoryController, CarrierMesController, CarrierReleaseController) work together to manage carriers throughout their lifecycle.
2. **Order Processing**: Several controllers (OrderFulfillmentController, OrderMgrController, WeldOrderController, WeldScheduleController) coordinate to manage the order processing workflow.
3. **Storage Management**: Multiple controllers (LanesController, StorageRowController, StorageStateController) work together to manage the storage system.
4. **Quality Control**: Several controllers (DefectController, GroupHoldController) coordinate to manage quality control processes.
5. **Configuration Management**: Multiple controllers (ModelController, DieController, StopController, ValidDestinationController, ParmSettingController) work together to manage system configuration.

## Debugging and Support

Each controller documentation includes detailed information on common issues, debugging steps, resolution strategies, and monitoring recommendations. These sections provide valuable guidance for troubleshooting and maintaining the system.

For system-wide debugging and support information, refer to the [Operations Guide](../operations.md).