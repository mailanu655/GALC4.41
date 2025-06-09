# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

GALC (Global Assembly Line Control) is an enterprise-scale Manufacturing Execution System (MES) for vehicle assembly line control. The repository contains multiple interconnected projects:

- **ahm_mfg_galc-development**: Main GALC system (Java EE, WebSphere)
- **ahm_stampstorage-master**: Stamp storage application for document management
- **ahm-lc-sales-Interface-service-main**: Integration service between GALC and YMS
- **GALC-DB**: Database export files (400+ tables)
- **GALC-MAP-LOGS**: Manufacturing Automation Protocol log files

## Common Development Commands

### Building the Main GALC Project
```bash
cd ahm_mfg_galc-development/aggregator
mvn clean install -DskipTests
# To run tests:
mvn test
# To run a specific test:
mvn test -Dtest=ClassName#methodName
```

### Building Stamp Storage Application
```bash
cd ahm_stampstorage-master/ahm_stampstorage-master/trunk
mvn clean install
```

### Building LC Sales Interface Service
```bash
cd ahm-lc-sales-Interface-service-main/ahm-lc-sales-Interface-service-main
mvn clean install
# Build Docker image:
mvn compile jib:build
# Run locally:
mvn spring-boot:run
```

### Development Prerequisites
- Java 8 (required)
- Maven 3.6.3+
- WebSphere Application Server 9 (for GALC deployment)
- Access to Honda's internal Artifactory (artifactory.amerhonda.com)
- IBM DB2 client tools (for database access)

## High-Level Architecture

### Multi-Tier Architecture
1. **Client Tier**: JavaFX and Swing desktop applications for different user roles
2. **Web Tier**: RESTful services and web applications
3. **Business Tier**: EJB-based services on WebSphere
4. **Data Tier**: IBM DB2 database
5. **Integration Tier**: IBM MQ messaging, JCA adapters

### Key Architectural Patterns
- **Service-Oriented Architecture**: Business logic exposed as EJB and REST services
- **Event-Driven Processing**: IBM MQ for asynchronous shop floor events
- **Layered Architecture**: Clear separation between presentation, business, and data layers
- **Domain-Driven Design**: Rich domain model representing manufacturing concepts

### Module Structure (ahm_mfg_galc-development)
- **base-***: Core framework modules (entity, service, persistence, web)
- **galc-client-***: Desktop client applications (Swing/JavaFX)
- **device-control**: Shop floor equipment integration
- **oif-service-ejb**: Outbound interface for external systems
- **rest-web**: RESTful API endpoints
- **jca-adaptor**: External system connectors

### Database Schema Categories
- **Product Manufacturing**: BOM_TBX, PRODUCT_SEQUENCE_TBX, BUILD_ATTRIBUTE_*
- **Quality Control**: QI_* tables for inspection and defect tracking
- **Process Control**: MC_* tables, PROCESS_POINT_* for manufacturing control
- **Component Tracking**: BLOCK_TBX, CONROD_TBX, HEAD_TBX for engine parts

### Integration Points
1. **YMS Integration**: Through LC-Sales Interface Service for vehicle shipping
2. **Shop Floor Devices**: Via MAP protocol and device-control module
3. **Quality Systems**: QICS integration for quality management
4. **Enterprise Systems**: SAP, dealer management, supply chain

### Process Point System
Process points (e.g., AAH1RE1P00101) track products through manufacturing stages. Key patterns:
- Format: [Plant][Line][Station][Type][Number]
- Events trigger downstream actions via message queues
- Real-time validation and status updates

### Development Notes
- All modules use Honda's internal Artifactory for dependencies
- Crystal Reports integration for reporting functionality
- Quartz Scheduler for batch processes
- OpenJPA 2.4.1 for ORM
- JAX-RS (RESTEasy) for REST services