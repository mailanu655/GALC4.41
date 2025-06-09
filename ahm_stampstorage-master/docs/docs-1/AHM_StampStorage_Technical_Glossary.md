# AHM Stamp Storage System Technical Glossary

## Core Concepts

### Storage Management

- **Carrier**: Physical unit used to transport and store stamping dies
- **Storage Row**: Designated area in the storage system for carrier placement
- **Storage Area**: Logical grouping of storage rows (e.g., A-Area, B-Area)
- **Storage State**: Current status of the storage system including capacity and utilization
- **Water Mark**: Threshold levels for storage capacity management
  - High Water Mark: Upper limit for storage utilization
  - Low Water Mark: Lower limit triggering optimization

### Manufacturing Components

- **Die**: Tool used in the stamping process to form metal parts
- **Press**: Machine that uses dies to stamp metal parts
- **Conveyor**: Automated system for moving carriers between locations
- **Buffer**: Temporary storage area for carriers in transit

### System States

- **Active**: Component or system is operational and processing requests
- **Maintenance**: Component is undergoing scheduled maintenance
- **Error**: Component has encountered an issue requiring attention
- **Standby**: Component is ready but not actively processing
- **Recovery**: System is recovering from an error state

## Technical Terms

### Integration Systems

- **MES (Manufacturing Execution System)**
  - Production management system
  - Order processing
  - Quality control
  - Resource tracking

- **PLC (Programmable Logic Controller)**
  - Hardware control system
  - Conveyor management
  - Sensor integration
  - Real-time control

### Database Concepts

- **Transaction**: Atomic unit of database operations
- **Audit Trail**: Historical record of system actions
- **Index**: Database structure for optimizing queries
- **Partition**: Division of large tables for performance
- **Archive**: Long-term storage of historical data

### Communication Protocols

- **TCP/IP**: Network communication protocol
- **JSON**: Data format for system messages
- **REST**: API architecture style
- **WebSocket**: Real-time communication protocol

## Business Terms

### Production Management

- **Production Order**: Request for specific stamped parts
- **Cycle Time**: Time to complete one production cycle
- **Throughput**: Production rate measurement
- **Backorder**: Unfulfilled production requests
- **Lead Time**: Time from order to completion

### Quality Control

- **QA (Quality Assurance)**: Quality management processes
- **QC (Quality Control)**: Quality verification steps
- **Defect Rate**: Measure of production quality
- **Tolerance**: Acceptable variation in specifications
- **Inspection**: Quality verification process

## System Components

### Software Modules

- **StorageManager**: Core storage management logic
- **ConnectionDevice**: Communication interface module
- **MesParser**: MES message processing module
- **MesProxy**: MES integration interface
- **StampStorageWeb**: Web interface module

### Hardware Components

- **Storage System**: Physical storage infrastructure
- **Control System**: PLC and control hardware
- **Network Infrastructure**: Communication hardware
- **Sensor Array**: Position and status sensors
- **HMI (Human Machine Interface)**: Operator interface

## Operational Terms

### Maintenance

- **Preventive Maintenance**: Scheduled upkeep
- **Corrective Maintenance**: Issue resolution
- **System Backup**: Data protection process
- **Recovery Point**: Data restoration target
- **Recovery Time**: System restoration duration

### Performance Metrics

- **Uptime**: System availability measurement
- **Response Time**: System reaction speed
- **Throughput Rate**: Processing capacity
- **Error Rate**: System reliability measure
- **Utilization**: Resource usage measurement

## Security Terms

### Access Control

- **Authentication**: Identity verification
- **Authorization**: Permission management
- **Role**: User permission group
- **Privilege**: Specific access right
- **Session**: User access period

### Data Protection

- **Encryption**: Data security method
- **Audit Log**: Security event record
- **Access Control**: Permission system
- **Data Integrity**: Information accuracy
- **Backup**: Data protection copy

## Error Handling

### Error Types

- **System Error**: Core system issue
- **Communication Error**: Network issue
- **Data Error**: Information integrity issue
- **User Error**: Operator mistake
- **Hardware Error**: Physical component issue

### Resolution Terms

- **Root Cause**: Primary error source
- **Mitigation**: Error impact reduction
- **Resolution**: Error correction process
- **Prevention**: Future error avoidance
- **Documentation**: Error record keeping

## Integration Terms

### System Integration

- **API**: Application Programming Interface
- **Interface**: System connection point
- **Protocol**: Communication rules
- **Gateway**: System connection bridge
- **Middleware**: Integration software

### Data Integration

- **ETL**: Extract, Transform, Load
- **Mapping**: Data field correlation
- **Validation**: Data verification
- **Transformation**: Data format conversion
- **Synchronization**: Data consistency maintenance

## Monitoring Terms

### System Monitoring

- **Alert**: System notification
- **Threshold**: Trigger point value
- **Metric**: Measurement value
- **Dashboard**: Status display
- **Report**: Information summary

### Performance Monitoring

- **KPI**: Key Performance Indicator
- **Benchmark**: Performance standard
- **Baseline**: Normal performance level
- **Trend**: Performance pattern
- **Analysis**: Performance evaluation
