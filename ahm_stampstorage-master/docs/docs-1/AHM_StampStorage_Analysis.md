# AHM Stamp Storage System Analysis

## Executive Summary

The AHM Stamp Storage system is an enterprise-level Manufacturing Execution System (MES) integration application designed to manage the automated storage and retrieval of stamp part carriers in Honda's automotive manufacturing facilities. The system interfaces with Programmable Logic Controllers (PLCs) that control physical conveyor systems, tracks carrier movements throughout the facility, and optimizes storage space utilization while ensuring timely delivery of stamped parts to welding lines.

Developed as a Java Enterprise Application with Spring Framework and Hibernate, the system uses a modular architecture to manage the complex workflow of carrier movement, storage, and retrieval. It provides real-time monitoring, alarm notifications, and a web-based user interface for operational oversight. The system's primary business value is in optimizing manufacturing logistics, reducing manual intervention, and ensuring just-in-time delivery of stamped parts to downstream production processes.

## 1. Project Structure and Component Analysis

### 1.1 Main Modules

The project follows a modular Maven-based structure with the following key components:

| Module | Purpose |
|--------|---------|
| ConnectionDevice | Handles communication with external hardware (PLC, RFID) |
| StampStorageDomain | Contains domain objects and data models |
| StampStorageManager | Business logic for storage management |
| StampStorageService | Service layer and API interfaces |
| StampStorageWeb | Web UI and user-facing interfaces |
| StampStorageEar | Enterprise Archive packaging |
| MesParser | Handles parsing MES messages |
| MesProxy | Communication proxy with MES systems |

### 1.2 Technology Stack

- **Programming Language**: Java
- **Application Framework**: Spring Framework
- **ORM**: Hibernate
- **Build System**: Maven
- **Database**: Relational database (schema prefix STAMPSTOR)
- **UI**: JSP, jQuery
- **Communication Protocols**: TCP/IP sockets, JSON messaging
- **Hardware Integration**: PLC communication, RFID readers

### 1.3 File Organization

The project follows a standard Maven project structure with main code in `src/main/java` and test code in `src/test/java`. Domain objects are organized by functionality, with clear separation between device communication, business logic, and user interface layers.

## 2. Project Purpose and Business Context

### 2.1 Core Purpose

The primary purpose of the AHM Stamp Storage system is to manage the automated storage and retrieval of stamped part carriers in Honda's manufacturing environment. It serves as a critical middleware that integrates with both Manufacturing Execution Systems (MES) and physical Programmable Logic Controllers (PLCs) to ensure efficient material flow throughout the manufacturing process.

### 2.2 Business Problems Solved

- **Storage Optimization**: Efficiently stores and manages carriers containing stamped parts in overhead conveyors with limited capacity
- **Just-in-Time Delivery**: Ensures stamped parts are delivered to welding lines precisely when needed
- **Inventory Tracking**: Maintains real-time tracking of all carriers, their contents, and locations
- **Automated Routing**: Determines optimal paths for carriers through the physical conveyor system
- **Empty Carrier Management**: Ensures empty carriers are routed to press lines when needed
- **Exception Handling**: Manages alarm conditions and system irregularities

### 2.3 Target Users and Stakeholders

- **Manufacturing Operations Personnel**: Monitor day-to-day operations
- **Production Supervisors**: Ensure smooth material flow
- **Maintenance Technicians**: Respond to system alarms and issues
- **Manufacturing Engineers**: Analyze and optimize system performance
- **IT Support Staff**: Maintain software and infrastructure

### 2.4 Key Features and Capabilities

- Automated carrier routing through overhead conveyor system
- Real-time tracking of carrier locations and contents
- Integration with Manufacturing Execution System (MES) via JSON messages
- Communication with PLCs to control physical conveyor movement
- Web-based monitoring interface for storage state visualization
- Alert and alarm notification system
- Configurable storage rules and policies
- Empty carrier management

### 2.5 Value Proposition

The system provides significant value through:
- Reduction in manual carrier handling and routing decisions
- Optimization of limited storage space in overhead conveyors
- Minimization of production delays due to parts availability
- Improved tracking and traceability of stamped parts
- Enhanced visibility into manufacturing material flow
- Reduced human error in carrier movement decisions

## 3. Technical Architecture

### 3.1 Architecture Pattern

The system follows a layered architecture with clear separation of concerns:

1. **Data Access Layer**: Hibernate-based persistence layer
2. **Domain Layer**: Business entities and logic
3. **Service Layer**: Business services and orchestration
4. **Integration Layer**: External systems integration (MES, PLC)
5. **Presentation Layer**: Web UI and user interfaces

The application uses Spring Framework for dependency injection and component management, following an MVC pattern for the web interface.

### 3.2 Component Hierarchy and Dependencies

```
+------------------+     +------------------+     +------------------+
| StampStorageWeb  |---->| StampStorageSvc  |---->| StampStorageDom  |
+------------------+     +------------------+     +------------------+
         |                       |                        |
         v                       v                        v
+------------------+     +------------------+     +------------------+
| Web Controllers  |     | Service Layer    |     | Domain Objects   |
+------------------+     +------------------+     +------------------+
                                 |
                                 v
                        +------------------+
                        | ConnectionDevice |
                        +------------------+
                                 |
        +---------------------+--+------------------+
        |                     |                     |
+---------------+    +----------------+    +----------------+
| MES Interface |    | PLC Interface  |    | RFID Interface |
+---------------+    +----------------+    +----------------+
```

### 3.3 Entry Points and Execution Flow

1. **Web Interface**: Entry point for human operators (`storage_state.jsp`)
2. **MES Messages**: JSON messages from MES trigger carrier movements
3. **PLC Signals**: Status updates from physical equipment
4. **Scheduled Jobs**: Background processes for optimizing storage

The main execution flow follows these patterns:
- Store-in process: Carriers with stamped parts enter storage system
- Store-out process: Carriers are retrieved based on welding line needs
- Empty carrier management: Ensures empty carriers are available where needed

### 3.4 System Boundaries and Integrations

The system integrates with:
- **Manufacturing Execution System (MES)**: Via JSON messaging for production data
- **Programmable Logic Controllers (PLCs)**: Using Omron FINS protocol
- **RFID Systems**: For carrier tracking and identification
- **Database**: For persistence of system state and historical data

### 3.5 Deployment Architecture

The application is deployed as a Java EAR (Enterprise Archive) on an application server, likely WebSphere or similar. The deployment includes all necessary modules and configurations for communication with external systems.

## 4. Logic and Process Flow

### 4.1 Main Business Processes

#### Store-In Process
1. Carrier with stamped parts arrives at entry point (ST5-13)
2. System evaluates storage state and determines optimal lane
3. Destination is set for carrier in database
4. PLC moves carrier to assigned lane
5. System updates storage state to reflect new inventory

#### Store-Out Process
1. Welding line requests parts (either via MES or low buffer detection)
2. System identifies appropriate carriers based on FIFO and part requirements
3. Carriers are marked for release from storage lanes
4. PLCs move carriers to welding line buffer
5. System updates inventory state

#### Empty Carrier Management
1. System monitors empty carrier buffer levels
2. When buffers fall below threshold, empty carriers are requested
3. Empty carriers are released from storage or retrieved from return conveyor
4. Empty carriers are routed to press lines as needed

### 4.2 Application Lifecycle

1. **Initialization**: System loads configuration and connects to external systems
2. **Normal Operation**: Continuous monitoring and management of carrier movements
3. **Exception Handling**: Managing alarm conditions and irregularities
4. **Shutdown**: Orderly shutdown preserving system state

### 4.3 State Management and Data Flow

The system maintains state information about:
- Current carrier locations and contents
- Storage lane utilization
- Welding line requirements and buffers
- System health and alarm conditions

Data flows from MES → Application → PLCs for control, and from PLCs → Application → UI for monitoring.

## 5. Database Structure and Data Model

### 5.1 Entity-Relationship Diagram (ERD)

```
+-------------+       +-------------+       +-------------+
|   CARRIER   |-------| CARRIER_MES |       |     DIE     |
+-------------+       +-------------+       +-------------+
      |  |                                       | |
      |  |                                       | |
      |  |            +-------------+            | |
      |  +------------| STOP/LANE   |------------+ |
      |               +-------------+              |
      |                     |                      |
      |               +-------------+              |
      +---------------| WELD_ORDER  |--------------+
                      +-------------+
                            |
                      +-------------+
                      | ORDER_MGR   |
                      +-------------+
```

### 5.2 Key Tables and Relationships

| Table | Purpose | Key Fields |
|-------|---------|------------|
| CARRIER_TBX | Tracks physical carriers | carrier_id, carrier_number, die, current_location |
| CARRIER_MES_TBX | MES view of carriers | carrier_id, carriernumber, currentlocation, dienumber |
| DIE_TBX | Information about dies | die_id, die_number, description |
| STOP_TBX | Locations in conveyor system | stop_id, conveyor_id, name, stop_type |
| WELD_ORDER_TBX | Production orders | order_id, model, left_qty, right_qty |
| ORDER_MGR_TBX | Order management | order_mgr_id, line_name, max_delivery_capacity |
| MODEL_TBX | Product models | model_id, name, left_die, right_die |
| ALARM_EVENT_TBX | System alarms | alarm_event_id, alarm_number, location |

### 5.3 Data Types and Constraints

The database uses standard relational data types with appropriate constraints:
- Primary keys: Hibernate-generated bigint IDs
- Foreign keys: Enforced relationships between entities
- Not null constraints: On critical fields
- Enumerations: For status and type fields

### 5.4 Data Access Patterns

The system primarily uses Hibernate for data access, with:
- JPA annotations on entity classes
- Named queries for complex data retrieval
- Finder methods for common access patterns
- Transactions for maintaining data integrity

## 6. Security and Access Control

### 6.1 Authentication and Authorization

The system appears to use standard Java EE security mechanisms, including:
- Role-based access control
- User authentication via application server
- Session management

### 6.2 Role-Based Access Control

Evidence suggests the following roles:
- Administrators: Full system access
- Operators: Monitoring and basic operations
- Maintenance: Access to troubleshooting tools

## 7. Integration Points

### 7.1 External API Dependencies

#### MES Integration
- JSON-based message exchange
- Carrier status updates
- Production order information
- Alarm notifications

#### PLC Communication
- Uses Omron FINS protocol
- TCP/IP socket communication
- Real-time status updates
- Command messages for conveyor control

### 7.2 Integration Patterns

- **Message-based integration**: JSON messages for MES communication
- **Direct socket communication**: For PLC interaction
- **Database integration**: For some system interfaces
- **Event-driven architecture**: For responding to system events

## 8. Development and Operations

### 8.1 Build and Deployment

- Maven-based build system
- EAR packaging for deployment
- Environment-specific configuration through filters

### 8.2 Monitoring and Logging

- Extensive logging throughout the application
- Error tracking and reporting
- Performance monitoring
- System health checks

## 9. Data Dictionary

### 9.1 Database Tables

| Table Name | Description |
|------------|-------------|
| ALARM_EVENT_TBX | Records system alarm events |
| ALARMS_DEFINITION_TBX | Defines possible system alarms |
| CARRIER_MES_TBX | MES view of carrier information |
| CARRIER_TBX | Tracks physical carriers in the system |
| DEFECT_TBL | Records defects in stamped parts |
| DIE_TBX | Information about stamping dies |
| MODEL_TBX | Product model definitions |
| ORDER_MGR_TBX | Manages weld orders for production |
| STOP_TBX | Defines physical locations in conveyor system |
| WELD_ORDER_TBX | Production orders for welding |

### 9.2 Key Fields

| Field | Table | Description |
|-------|-------|-------------|
| carrier_number | CARRIER_TBX | Unique identifier for a carrier |
| dienumber | CARRIER_MES_TBX | Die used to stamp parts in carrier |
| currentlocation | CARRIER_MES_TBX | Current physical location of carrier |
| destination | CARRIER_TBX | Target destination for carrier movement |
| buffer | CARRIER_TBX | Buffer status flag |
| stop_type | STOP_TBX | Type of stop (storage, transfer, etc.) |
| order_status | WELD_ORDER_TBX | Status of production order |
| production_run_no | CARRIER_TBX | Production run identifier |

## 10. Technical Glossary

| Term | Definition |
|------|------------|
| Carrier | Physical container that holds stamped parts |
| Die | Tool used for stamping parts in press |
| Lane | Storage area for carriers in overhead conveyor |
| Stop | Specific location in conveyor system |
| Store-In | Process of placing carriers into storage |
| Store-Out | Process of retrieving carriers from storage |
| Buffer | Temporary holding area for carriers |
| MES | Manufacturing Execution System |
| PLC | Programmable Logic Controller |
| FIFO | First In, First Out inventory management |
| OHCV | Overhead Conveyor (system that physically moves carriers) |