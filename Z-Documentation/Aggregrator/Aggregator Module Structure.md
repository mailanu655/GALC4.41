# Aggregator Module Structure

```
aggregator/
│
├── pom.xml                           # Main build configuration file
│   │                                 # - Defines dependencies for all modules
│   │                                 # - Configures build plugins
│   │                                 # - Sets up build profiles
│   │                                 # - Manages version numbers
│
├── galc-app-was9/                    # WebSphere Application Server package
│   ├── pom.xml                       # EAR packaging configuration
│   │                                 # - Defines dependencies for server components
│   │                                 # - Configures EAR structure
│   │                                 # - Sets up security roles
│   │
│   └── src/                          # Source directory
│       └── main/                     # Main source files
│           ├── application.xml       # J2EE application descriptor
│           └── resources/            # Application resources
│               └── META-INF/         # Metadata directory
│                   └── ibmconfig/    # IBM WebSphere specific configuration
│
├── galc-client-swing/                # Swing client package
│   ├── pom.xml                       # Build configuration for Swing client
│   │                                 # - Defines dependencies for Swing components
│   │                                 # - Configures JAR packaging
│   │
│   ├── src/                          # Source directory
│   │   └── assembly/                 # Assembly configuration
│   │       └── swing-jar.xml         # JAR assembly descriptor
│   │                                 # - Defines how to package dependencies
│   │                                 # - Configures output format
│   │
│   └── target/                       # Build output directory
│       ├── classes/                  # Compiled classes
│       └── resource/                 # Processed resources
│
├── galc-client-fx/                   # JavaFX client package
│   ├── pom.xml                       # Build configuration for JavaFX client
│   │                                 # - Defines dependencies for JavaFX components
│   │                                 # - Configures JAR packaging
│   │
│   ├── src/                          # Source directory
│   │   └── assembly/                 # Assembly configuration
│   │       └── fx-jar.xml            # JAR assembly descriptor
│   │                                 # - Defines how to package dependencies
│   │                                 # - Configures output format
│   │
│   └── target/                       # Build output directory
│       ├── classes/                  # Compiled classes
│       └── resource/                 # Processed resources
│
└── galc-client-all/                  # Combined client package
    ├── pom.xml                       # Build configuration for combined client
    │                                 # - Includes both Swing and JavaFX components
    │                                 # - Configures comprehensive packaging
    │
    └── src/                          # Source directory
        └── assembly/                 # Assembly configuration
            └── all-jar.xml           # JAR assembly descriptor
                                      # - Combines Swing and JavaFX packages
                                      # - Includes all client dependencies
```

## Purpose and Logic of the Aggregator Module

### 1. Central Build Coordination

The aggregator module serves as the central hub for building all GALC components. It:

- **Defines Common Properties**: Standardizes version numbers, dependency versions, and build parameters across all modules
- **Manages Dependencies**: Maintains a centralized dependency management section to ensure consistent library versions
- **Configures Build Plugins**: Sets up Maven plugins with standardized configurations
- **Creates Build Profiles**: Defines different build configurations for various deployment scenarios

### 2. Build Profiles

The aggregator uses Maven profiles to organize the build process:

- **galc-app**: Builds server-side components and packages them as an EAR for WebSphere
- **galc-client**: Builds client applications (both Swing and JavaFX versions)
- **scheduler-app**: Builds the scheduler application for background task processing
- **log-server**: Builds the centralized logging server

### 3. Packaging Strategy

The aggregator creates several deployable packages:

- **galc-app-was9**: An EAR file containing all server components for WebSphere Application Server 9
  - Includes web applications (WAR files)
  - Includes EJB modules (JAR files)
  - Includes resource adapters (RAR files)
  - Configures security roles and application context roots
- **galc-client-swing**: A JAR file containing traditional Swing client applications
  - Packages all Swing-based client modules
  - Includes necessary dependencies
- **galc-client-fx**: A JAR file containing modern JavaFX client applications
  - Packages all JavaFX-based client modules
  - Includes necessary dependencies
- **galc-client-all**: A comprehensive JAR file containing all client applications
  - Combines both Swing and JavaFX clients
  - Includes all client dependencies

### 4. Dependency Management

The aggregator carefully manages dependencies to:

- **Prevent Duplication**: Uses "skinnyWars" configuration to avoid duplicate libraries
- **Ensure Compatibility**: Standardizes library versions across all modules
- **Optimize Deployment**: Organizes shared libraries efficiently

### 5. Build Naming and Versioning

The aggregator implements a consistent naming convention for build artifacts:

- Uses the pattern `${galc.build.prefix}_${galc.build.version}_[component]`
- Includes build timestamps for traceability
- Maintains version information in manifest files

This structured approach ensures that all GALC components are built consistently and can work together seamlessly when deployed.