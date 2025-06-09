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
