# StampStorage Technical Documentation

Welcome to the StampStorage technical documentation. This documentation provides detailed information about the StampStorage application's architecture, components, and functionality.

## Overview

StampStorage is a manufacturing application that manages the storage and movement of stamp carriers in Honda's manufacturing facilities. It integrates with Manufacturing Execution Systems (MES) and Programmable Logic Controllers (PLCs) to track carrier locations, manage storage operations, and fulfill orders for the welding lines.

## Documentation Sections

### Architecture and Design
- [Overview](overview.md) - High-level overview of the StampStorage system
- [Project Structure](project_structure.md) - Organization of the codebase and modules
- [Components](components.md) - Key components and their responsibilities
- [Processes](processes.md) - Core business processes and workflows
- [Integration](integration.md) - Integration with external systems

### Technical Reference
- [Database](database.md) - Database schema and data model
- [Domain Entities](domain/index.md) - Domain entity classes and their functionality
- [Controllers](controllers.md) - Web controllers and request handling
- [Configuration](config/index.md) - Application configuration files
- [Operations](operations.md) - Deployment, monitoring, and maintenance

## Key Features

1. **Carrier Tracking**: Tracks the location and status of stamp carriers throughout the facility
2. **Storage Management**: Manages the storage of carriers in designated storage rows
3. **Order Fulfillment**: Processes orders from welding lines and delivers carriers as needed
4. **MES Integration**: Integrates with Manufacturing Execution Systems for real-time data exchange
5. **PLC Communication**: Communicates with Programmable Logic Controllers to control physical equipment
6. **User Interface**: Provides a web-based interface for monitoring and managing the system
7. **Reporting**: Generates reports on carrier inventory, movement, and status

## Technology Stack

- **Java**: Core programming language
- **Spring Framework**: Application framework for dependency injection and configuration
- **Spring MVC**: Web framework for controller and view management
- **Hibernate/JPA**: Object-relational mapping for database access
- **JSP/JSTL**: View templates for web interface
- **JavaScript/jQuery**: Client-side scripting for user interface
- **DB2**: Database for data persistence
- **LDAP**: Authentication and authorization
- **Maven**: Build and dependency management

## Getting Started

To get started with the StampStorage documentation:

1. Review the [Overview](overview.md) for a high-level understanding of the system
2. Explore the [Project Structure](project_structure.md) to understand the codebase organization
3. Dive into specific areas of interest using the documentation sections above
4. Refer to the [Technical Documentation](technical_documentation.md) for detailed technical information

## Additional Resources

- [Technical Glossary](../AHM_StampStorage_Technical_Glossary.md) - Definitions of technical terms and acronyms
- [Process Flows](../AHM_StampStorage_Process_Flows.md) - Detailed process flow diagrams
- [Database ERD](../AHM_StampStorage_Database_ERD.md) - Entity Relationship Diagrams for the database
- [Executive Summary](../AHM_StampStorage_Executive_Summary.md) - Business overview of the system
