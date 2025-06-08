# GALC Project Overview

## Project Purpose

GALC (Global Assembly Line Control) is a manufacturing control system developed by Honda for managing and monitoring assembly line operations. The system appears to be designed for tracking products (likely automotive components or engines) through various manufacturing processes, collecting data, and ensuring quality control.

## System Architecture

The GALC system follows a multi-tier architecture:

### 1. Server Components

- **Base Services**: Core business logic and data processing
- **Web Services**: REST and traditional web interfaces
- **Configuration Services**: System configuration management
- **Database Integration**: Extensive database tables for storing manufacturing data
- **Scheduler**: Task scheduling and automation

### 2. Client Components

- **Swing Clients**: Traditional Java desktop applications
- **JavaFX Clients**: Modern UI applications
- **Handheld/Mobile Clients**: Applications for mobile devices
- **Headless Clients**: Background processing applications

### 3. Device Integration

- **Device Control**: Management of manufacturing equipment
- **Device Simulation**: Testing and simulation tools
- **JCA Adapters**: Connection to enterprise systems

## Key Modules

### Core Modules

1. **Base Common (base-common)**
   - Purpose: Provides fundamental utilities and shared functionality
   - Key Components: Logging, exception handling, common data structures
   - Interactions: Used by all other modules as a foundation
2. **Base Entity (base-entity)**
   - Purpose: Defines data models and entity objects
   - Key Components: Java classes representing database tables and business objects
   - Interactions: Used by persistence and service layers
3. **Base Persistence (base-persistence)**
   - Purpose: Handles database operations and data access
   - Key Components: DAO (Data Access Object) implementations, JPA entities
   - Interactions: Connects to database, used by service layer
4. **Base Service (base-service)**
   - Purpose: Implements business logic and processing
   - Key Components: Service implementations, business rules
   - Interactions: Uses persistence layer, called by client applications
5. **Base Web (base-web)**
   - Purpose: Provides web interfaces and services
   - Key Components: Web controllers, REST endpoints
   - Interactions: Exposes services to web clients

### Client Applications

1. **Base Client (base-client)**
   - Purpose: Core client application framework
   - Key Components: Main application window, login handling, client controllers
   - Interactions: Connects to services, displays UI
2. **Team Leader (team-leader, team-leader-fx)**
   - Purpose: Management interface for team leaders
   - How It Works: Provides oversight and control capabilities
   - Key Components: Dashboard views, team management tools
3. **QICS (galc-qics, qics-fx)**
   - Purpose: Quality Inspection and Control System
   - How It Works: Manages quality control processes and inspections
   - Key Components: Defect tracking, repair management, quality reporting
4. **Lot Control (lot-control, lot-control-fx)**
   - Purpose: Manages manufacturing lots and batches
   - How It Works: Tracks groups of products through production
   - Key Components: Lot creation, tracking, and management

### Device Integration

1. **Device Control (device-control)**
   - Purpose: Manages communication with manufacturing equipment
   - How It Works: Provides interfaces to control and monitor devices
   - Key Components: Device drivers, communication protocols
2. **Device Simulator (device-simulator, device-simulator-fx)**
   - Purpose: Simulates device behavior for testing
   - How It Works: Mimics real device responses and data
   - Key Components: Simulation controls, test scenarios

### Support Systems

1. **Log Server (log-server)**
   - Purpose: Centralized logging system
   - How It Works: Collects and stores logs from all components
   - Key Components: Log collection, storage, and retrieval
2. **Scheduler App (scheduler-app)**
   - Purpose: Manages scheduled tasks and jobs
   - How It Works: Uses Quartz scheduler to run periodic tasks
   - Key Components: Job definitions, triggers, scheduling

## Database Structure

The system uses an extensive database with hundreds of tables (visible in the GALC-DB and GALC-DATABASE directories). Key table categories include:

1. **Product Tracking Tables**: Track products through manufacturing
2. **Quality Control Tables**: Store defect and repair information
3. **Configuration Tables**: System configuration and settings
4. **User Management Tables**: User accounts and permissions
5. **Device Data Tables**: Device configurations and status

## Development Tools and Technologies

The project uses:

1. **Java**: Primary programming language
2. **Maven**: Build and dependency management
3. **Spring Framework**: Application framework
4. **JavaFX/Swing**: UI frameworks
5. **JPA/OpenJPA**: Database persistence
6. **Quartz**: Job scheduling
7. **Log4j**: Logging framework
8. **Crystal Reports**: Reporting engine
9. **WebStart**: Application deployment

## Workflow and Interactions

1. **Product Flow**:
   - Products enter the system at specific process points
   - Data is collected at each stage of manufacturing
   - Quality inspections occur at designated points
   - Issues are tracked and managed through repair processes
   - Completed products are tracked for shipping
2. **Data Flow**:
   - Devices collect data from manufacturing processes
   - Client applications display and manage this data
   - Services process and store the information
   - Reports provide insights and analysis
3. **User Interactions**:
   - Operators use client applications to input data and control processes
   - Team leaders monitor operations and manage exceptions
   - Quality personnel track and address defects
   - Administrators configure the system and manage users



GALC Project Structure
│
├── README.md                           # Project overview
├── Jenkinsfile                         # CI/CD configuration
│
├── Core Modules
│   ├── base-common                     # Common utilities and shared functionality
│   ├── base-entity                     # Data models and entity objects
│   ├── base-persistence                # Database operations and data access
│   ├── base-service                    # Business logic implementation
│   ├── base-service-interface          # Service interfaces
│   ├── base-common-service             # Common service implementations
│   ├── base-web                        # Web interfaces
│   ├── base-web-start                  # Web start configuration
│   └── base-library                    # Shared libraries and resources
│
├── Client Applications
│   ├── base-client                     # Core client framework
│   ├── base-client-fx                  # JavaFX client framework
│   ├── client-resource                 # Client resources (images, etc.)
│   ├── galc-qics                       # Quality Inspection and Control System
│   ├── qics-fx                         # JavaFX version of QICS
│   ├── lot-control                     # Lot management system
│   ├── lot-control-fx                  # JavaFX version of lot control
│   ├── team-leader                     # Team leader application
│   ├── team-leader-fx                  # JavaFX version of team leader
│   └── gts-client                      # GTS client application
│
├── Web Applications
│   ├── config-service-web              # Configuration web service
│   ├── rest-web                        # REST API endpoints
│   ├── handheld-web                    # Mobile/handheld web interface
│   ├── oif-config-web                  # OIF configuration web interface
│   └── gwt                             # Google Web Toolkit applications
│       ├── FactoryNews                 # Factory news application
│       ├── LineOverview                # Production line overview
│       ├── LocationTracking            # Product location tracking
│       ├── QicsMobileWeb               # Mobile QICS web interface
│       ├── Vios                        # VIOS web application
│       └── VisualOverview              # Visual production overview
│
├── Device Integration
│   ├── device-control                  # Device communication and control
│   ├── device-simulator                # Device simulation (Swing)
│   ├── device-simulator-fx             # Device simulation (JavaFX)
│   ├── jca-adaptor                     # JCA adapter for enterprise integration
│   └── jca-socket-ejb                  # Socket-based EJB for device communication
│
├── Services
│   ├── log-server                      # Centralized logging service
│   ├── scheduler-app                   # Task scheduling service
│   ├── oif-service-ejb                 # OIF service implementation
│   └── galc-tools                      # Utility tools and applications
│
├── Mobile Applications
│   └── mobile                          # Mobile client applications
│       └── QicsBrowser                 # QICS mobile browser
│
├── Build and Deployment
│   └── aggregator                      # Build aggregation and packaging
│       ├── galc-app-was9               # WebSphere application package
│       ├── galc-client-swing           # Swing client package
│       ├── galc-client-fx              # JavaFX client package
│       └── galc-client-all             # Combined client package
│
├── Testing
│   ├── base-test                       # Test framework and utilities
│   └── service-test                    # Service testing framework
│
├── Database
│   ├── GALC-DB                         # Database scripts and definitions
│   └── GALC-DATABASE                   # Database schema and data
│
├── Utilities
│   ├── galc-utilities                  # Utility applications
│   │   ├── AutomatedClientRestart      # Client restart tools
│   │   ├── DevServerConfiguration      # Server configuration tools
│   │   └── HeadlessClient              # Headless client applications
│   └── MAP-DW-L1                       # Manufacturing process definitions
│
└── Documentation
    └── GALC DW                         # Data warehouse documentation