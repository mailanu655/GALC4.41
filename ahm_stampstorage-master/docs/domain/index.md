# Domain Entity Documentation

This section contains technical documentation for the domain entities in the Stamp Storage system. Each entity is documented with its purpose, logic/functionality, flow, key elements, usage examples, and debugging/production support information.

## Entity Documentation

| Entity | Description |
|--------|-------------|
| [AlarmContact](AlarmContact.md) | Represents the association between an alarm definition and a contact person who should be notified when the alarm is triggered. |
| [AlarmDefinition](AlarmDefinition.md) | Defines alarm configurations including severity, notification requirements, and auto-archive settings. |
| [AlarmDefinitionWrapper](AlarmDefinitionWrapper.md) | A wrapper class for AlarmDefinition that includes additional contact information for notifications. |
| [AlarmEvent](AlarmEvent.md) | Represents an occurrence of an alarm in the system, tracking when it was triggered and cleared. |
| [AlarmEventArchive](AlarmEventArchive.md) | Archives historical alarm events that have been cleared and processed. |
| [AuditErrorLog](AuditErrorLog.md) | Records system errors and audit information for troubleshooting and compliance. |
| [AuditErrorLogFinderCriteria](AuditErrorLogFinderCriteria.md) | Provides search criteria for querying audit error logs. |
| [BitInfo](BitInfo.md) | Manages bit-level information for maintenance and status flags. |
| [Carrier](Carrier.md) | Represents a physical carrier that transports parts through the system. |
| [CarrierFinderCriteria](CarrierFinderCriteria.md) | Provides search criteria for querying carriers. |
| [CarrierHistory](CarrierHistory.md) | Archives historical carrier information for tracking and auditing. |
| [CarrierHistoryFinderCriteria](CarrierHistoryFinderCriteria.md) | Provides search criteria for querying carrier history records. |
| [CarrierMes](CarrierMes.md) | Represents carrier information in the Manufacturing Execution System (MES). |
| [CarrierMesInStorageLanesComparator](CarrierMesInStorageLanesComparator.md) | Provides comparison logic for sorting carriers in storage lanes. |
| [CarrierRelease](CarrierRelease.md) | Manages the release of carriers from storage to their destinations. |
| [Contact](Contact.md) | Represents a person who can be contacted for notifications. |
| [Defect](Defect.md) | Records information about defects found in parts. |
| [Die](Die.md) | Represents a die used in the stamping process. |
| [DieInventory](DieInventory.md) | Tracks inventory information for dies. |
| [GroupHoldFinderCriteria](GroupHoldFinderCriteria.md) | Provides search criteria for querying group holds. |
| [Model](Model.md) | Represents a product model with associated left and right dies. |
| [OrderFulfillment](OrderFulfillment.md) | Manages the fulfillment of orders for parts. |
| [OrderFulfillmentPk](OrderFulfillmentPk.md) | Provides a composite primary key for OrderFulfillment. |
| [OrderMgr](OrderMgr.md) | Manages orders for a specific welding line. |
| [ParmSetting](ParmSetting.md) | Stores system parameters and settings. |
| [PressActivity](PressActivity.md) | Records activity information for presses. |
| [ServiceRole](ServiceRole.md) | Defines roles for services in the system. |
| [StampingImage](StampingImage.md) | Stores image data for stamping operations. |
| [Stop](Stop.md) | Represents a physical location in the system where carriers can stop. |
| [StorageRow](StorageRow.md) | Represents a row in storage where carriers can be stored. |
| [ValidDestination](ValidDestination.md) | Defines valid destinations for carriers from specific stops. |
| [WeldOrder](WeldOrder.md) | Represents an order for parts to be sent to a welding line. |
| [WeldSchedule](WeldSchedule.md) | Represents the production schedule for a welding line. |

## Common Patterns

The domain entities in the Stamp Storage system follow several common patterns:

1. **JPA Entity Pattern**: All domain entities are JPA entities with standard persistence methods (persist, merge, remove, flush, clear).

2. **Finder Methods**: Most entities provide static finder methods for retrieving entities by ID, finding all entities, and finding entities with pagination.

3. **Custom Queries**: Many entities provide custom query methods for specific business requirements.

4. **Validation**: Entities use Bean Validation annotations (@NotNull, etc.) to enforce data integrity.

5. **Relationships**: Entities define relationships using JPA annotations (@OneToOne, @ManyToOne, etc.).

6. **Enumerated Types**: Many entities use enumerated types for status, type, and category fields.

7. **Transactional Methods**: Persistence operations are annotated with @Transactional.

8. **Entity Manager**: Entities use a shared entity manager for persistence operations.

## Database Schema

The domain entities map to database tables with the following naming convention:

- Entity class names are in CamelCase (e.g., `AlarmContact`)
- Table names are in uppercase with `_TBX` suffix (e.g., `ALARM_CONTACT_TBX`)
- Column names are in uppercase (e.g., `ALARM_CONTACT_ID`)

## Debugging and Production Support

Each entity documentation includes specific debugging and production support information, including:

- Common issues related to the entity
- Debugging steps to identify and diagnose issues
- Resolution strategies for common issues
- Monitoring recommendations

For system-wide debugging and production support, refer to the [Technical Documentation](../technical_documentation.md).