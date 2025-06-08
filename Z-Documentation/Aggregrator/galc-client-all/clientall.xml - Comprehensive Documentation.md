# clientall.xml - Comprehensive Documentation

## Purpose

The `clientall.xml` file is a Maven Assembly descriptor that defines how to package the GALC (Global Assembly Line Control) client applications into a distributable ZIP archive. This file serves as a blueprint for creating a complete, self-contained client package that includes all necessary components for both Swing and JavaFX versions of the GALC client application.

## How It Works

The assembly descriptor orchestrates the packaging process by:

1. Defining the output format (ZIP)
2. Specifying which files to include in the package
3. Configuring how dependencies should be bundled
4. Creating launch scripts for different client versions

When the Maven build process runs, it uses this descriptor to gather all required components and package them according to the specified structure.

## Key Components

### 1. Assembly Configuration

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.0.0 http://maven.apache.org/xsd/assembly-2.0.0.xsd">
    <id>galc-client-all</id>
    <formats>
        <format>zip</format>
    </formats>
    
    <includeBaseDirectory>false</includeBaseDirectory>
```

- **Assembly ID**: `galc-client-all` - Identifies this specific assembly configuration
- **Output Format**: ZIP - The final output will be a compressed ZIP file
- **Base Directory**: Disabled - Files will be placed directly in the root of the ZIP without an extra directory level

### 2. File Inclusions

```xml
<files>
    <file>
        <source>../../base-client/src/main/resources/launch.bat</source>
        <outputDirectory></outputDirectory>
        <destName>launch-swing.bat</destName>
        <filtered>true</filtered>
    </file>
    <file>
        <source>../../base-client-fx/src/main/resources/launch.bat</source>
        <outputDirectory></outputDirectory>
        <destName>launch-fx.bat</destName>
        <filtered>true</filtered>
    </file>
    <file>
        <source>../galc-client-fx/target/galc-client-fx-${galc.build.version}.jar</source>
        <outputDirectory></outputDirectory>
    </file>
    <file>
        <source>../galc-client-swing/target/galc-client-swing-${galc.build.version}.jar</source>
        <outputDirectory></outputDirectory>
    </file>
    <file>
        <source>../../gts-client/target/gts-client-${galc.build.version}.jar</source>
        <outputDirectory></outputDirectory>
    </file>
</files>
```

This section specifies individual files to include in the package:

- **Launch Scripts**:
  - `launch-swing.bat` - For launching the Swing-based client
  - `launch-fx.bat` - For launching the JavaFX-based client
  - Both scripts are filtered, meaning Maven will replace variables like `@BuildVersion@` with actual values
- **Client Application JARs**:
  - `galc-client-fx-${galc.build.version}.jar` - JavaFX client application
  - `galc-client-swing-${galc.build.version}.jar` - Swing client application
  - `gts-client-${galc.build.version}.jar` - GTS client component

### 3. Dependency Management

```xml
<dependencySets>
    <dependencySet>
        <outputDirectory></outputDirectory>
        <unpack>false</unpack>
        <useProjectArtifact>false</useProjectArtifact>                                                                 
        <useTransitiveDependencies>false</useTransitiveDependencies>
        <includes>                                                                                                                       
            <include>com.honda.galc:vios-badge-reader</include>    
            <include>org.rxtx:rxtx</include>                
        </includes> 
    </dependencySet>
</dependencySets>
```

This section defines how project dependencies are handled:

- Dependencies Included

  :

  - `vios-badge-reader` - For badge reading functionality
  - `rxtx` - For serial communication with hardware devices

- Packaging Configuration

  :

  - `unpack: false` - Dependencies are included as JAR files, not unpacked
  - `useProjectArtifact: false` - The project's own artifact is not included in this dependency set
  - `useTransitiveDependencies: false` - Only direct dependencies are included, not their dependencies

## Workflow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Maven Build Process                      │
└───────────────────────────────┬─────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    clientall.xml Assembly                    │
└───────────────┬───────────────────────────┬─────────────────┘
                │                           │
                ▼                           ▼
┌───────────────────────────┐   ┌─────────────────────────────┐
│      File Collection      │   │    Dependency Collection     │
│                           │   │                             │
│ ┌─────────────────────┐   │   │ ┌─────────────────────────┐ │
│ │   Launch Scripts    │   │   │ │  vios-badge-reader.jar  │ │
│ │  - launch-swing.bat │   │   │ └─────────────────────────┘ │
│ │  - launch-fx.bat    │   │   │                             │
│ └─────────────────────┘   │   │ ┌─────────────────────────┐ │
│                           │   │ │        rxtx.jar         │ │
│ ┌─────────────────────┐   │   │ └─────────────────────────┘ │
│ │   Application JARs  │   │   │                             │
│ │ - galc-client-fx.jar│   │   └─────────────────────────────┘
│ │ - galc-client-swing.│   │
│ │ - gts-client.jar    │   │
│ └─────────────────────┘   │
│                           │
└───────────────┬───────────┘
                │
                ▼
┌─────────────────────────────────────────────────────────────┐
│                    ZIP Package Creation                      │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Final ZIP: ${galc.build.prefix}_${version}_client   │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Integration with Project

The `clientall.xml` file is referenced in the project's `pom.xml` file:

```xml
<build>
    <finalName>${galc.build.prefix}_${galc.build.version}_client</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <descriptors>
                    <descriptor>src/assembly/clientall.xml</descriptor>
                </descriptors>
            </configuration>
        </plugin>
    </plugins>
</build>
```

This configuration tells Maven to use the `clientall.xml` descriptor when running the assembly plugin, and to name the final output file according to the pattern `${galc.build.prefix}_${galc.build.version}_client.zip`.

## Data Flow

```
┌─────────────────────┐     ┌─────────────────────┐     ┌─────────────────────┐
│                     │     │                     │     │                     │
│  Source Components  │────▶│  Assembly Process   │────▶│  Packaged Client    │
│                     │     │                     │     │                     │
└─────────────────────┘     └─────────────────────┘     └─────────────────────┘
        │                            │                           │
        ▼                            ▼                           ▼
┌─────────────────────┐     ┌─────────────────────┐     ┌─────────────────────┐
│ - Launch scripts    │     │ - File collection   │     │ - launch-swing.bat  │
│ - Client JARs       │     │ - Filtering         │     │ - launch-fx.bat     │
│ - Dependencies      │     │ - Dependency        │     │ - Client JARs       │
│                     │     │   resolution        │     │ - Dependencies      │
└─────────────────────┘     └─────────────────────┘     └─────────────────────┘
```

## Launch Script Configuration

The launch scripts included in the package are configured to start the GALC client applications with specific parameters:

### Swing Client (launch-swing.bat)

```batch
%JAVA% -cp galc-client-swing-%VERSION%.jar; com.honda.galc.client.ClientMain %CACHEDIR% http://%SERVER%:%PORT%/BaseWeb/HttpServiceHandler %TERMINAL%
```

### JavaFX Client (launch-fx.bat)

```batch
%JAVA% -noverify -cp galc-client-fx-%VERSION%.jar; com.honda.galc.client.ClientMainFx %CACHEDIR% http://%SERVER%:%PORT%/BaseWeb/HttpServiceHandler %TERMINAL%
```

Both scripts accept the following parameters:

- `%CACHEDIR%`: Local cache directory (default: c:\ALC_CACHE)
- `%SERVER%`: Server hostname (default: qhma1was)
- `%PORT%`: Server port (default: 8005)
- `%TERMINAL%`: Terminal identifier (default varies by client type)

## Static Properties

The following static properties are used in the assembly process:

| Property | Description | Example Value | |----------|-------------|---------------| | `galc.build.version` | Version number of the GALC build | 4.31-SNAPSHOT | | `galc.build.prefix` | Prefix for the build name | GALC |

## Debugging Production Issues

When troubleshooting issues with the client package in production:

1. **Verify Package Contents**:
   - Ensure all required JARs are present in the ZIP file
   - Check that launch scripts have been properly filtered with correct version numbers
2. **Launch Script Issues**:
   - Verify JAVA_HOME is set correctly for the target environment
   - Check server and port settings match the deployment environment
   - Ensure the terminal identifier is valid for the environment
3. **Dependency Problems**:
   - Verify that all required dependencies are included in the package
   - Check for version compatibility issues between components
4. **Client Connection Issues**:
   - Use the URL in the launch script to verify server connectivity
   - Check firewall settings that might block client-server communication

## Debugging Flow Diagram

```
┌─────────────────────┐
│  Client Won't Start │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐     ┌─────────────────────┐
│ Check Launch Script │────▶│  - JAVA_HOME set?   │
└──────────┬──────────┘     │  - Correct version? │
           │                │  - File permissions? │
           ▼                └─────────────────────┘
┌─────────────────────┐     ┌─────────────────────┐
│  Check JAR Files    │────▶│  - All JARs present?│
└──────────┬──────────┘     │  - Correct versions?│
           │                └─────────────────────┘
           ▼
┌─────────────────────┐     ┌─────────────────────┐
│ Check Server Config │────▶│  - Correct server?  │
└──────────┬──────────┘     │  - Correct port?    │
           │                │  - Server running?   │
           ▼                └─────────────────────┘
┌─────────────────────┐
│   Check Network     │
└─────────────────────┘
```

## Example Usage

### Building the Client Package

To build the client package using Maven:

```bash
cd aggregator/galc-client-all
mvn clean package assembly:single
```

This will produce a ZIP file in the `target` directory with a name like `GALC_4.31-SNAPSHOT_client.zip`.

### Deploying the Client Package

1. Copy the ZIP file to the target environment
2. Extract the contents to a directory
3. Edit the launch script (launch-swing.bat or launch-fx.bat) to set the correct:
   - JAVA_HOME
   - SERVER
   - PORT
   - TERMINAL
4. Run the appropriate launch script to start the client

## Conclusion

The `clientall.xml` file is a critical component in the GALC project's build system, responsible for creating a complete, ready-to-deploy client package. It ensures that all necessary components are included and properly configured, making it easier to distribute and deploy the GALC client applications across different environments.

By understanding this file and its role in the build process, developers can more effectively manage the packaging and deployment of the GALC client applications, and troubleshoot issues that may arise during deployment or runtime.