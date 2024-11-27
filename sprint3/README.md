# WASTE INCINERATOR SERVICE - Sprint 3
---

## Table of Contents
1. [Introduction](#introduction)
2. [Requirements](#requirements)
3. [Previous Sprint](#previous-sprint)
4. [Goals](#goals)
5. [Problem Analysis](#problem-analysis)
6. [Project Architecture](#project)
8. [Test Plans](#test-plans)
9. [Deployment](#deployment)
10. [Future Work](#future-work)
11. [Authors](#authors)
---

## Introduction
A company intends to develop a **WasteIncineratorService** to process waste by burning it and requires a software system service (WIS) to control a robot (called OpRobot) for moving the waste.

## Requirements
The following link leads to the requirements document provided by the client:  
[Requirements Document](./userDocs/TemaFinale24.html)

## Previous Sprint
The following link leads to the document of the previous SPRINT, which focused on developing the MonitoringDevice and the Scale:  
[SPRINT 2](../sprint2/README.md)

### Initial Reference Architecture
Below is an image representing the final architecture of the previous sprint, which serves as the initial architecture for this sprint:  
![Initial Architecture](img/architetturaIniziale.png)

## Goals
In this SPRINT, the following requirements will be addressed:
- Definition of the ServiceStatusGUI
- Interaction between WIS and ServiceStatusGUI

## Problem Analysis

### Definition of the ServiceStatusGUI
The requirements specify that the ServiceStatusGUI should be a graphical interface allowing a user to monitor the system status, specifically:
- Display the status of the **WasteStorage**, such as the current number of RP present.
- Display the status of the **AshStorage**, indicating the current capacity level.
- Display the status of the **Incinerator**, showing whether it is burning or not.
- Display the status of the **OpRobot**, indicating its current position in the service area and its task.

### Interaction WIS - ServiceStatusGUI
The main challenge in WIS - ServiceStatusGUI interaction is how to transfer WIS system information to the GUI.  
The GUI is read-only and does not send updates to the system.  
Possible approaches for retrieving such information:
- Make the WIS observable by the GUI.
- Subscribe the GUI to the MQTT broker defined in the previous sprint to retrieve information directly.

#### Analysis of Solutions
- **MQTT publish/subscribe**: Reuse the broker for status updates (e.g., Incinerator state, AshStorage level, arrival of a new RP), with added publishers for OpRobot state.
- **Observability**: GUI observes WIS directly without additional broker subscriptions.

Each solution has pros and cons, leaving the choice to designers.

## Project

### Interface Technologies
Options:
- **Mobile application (Android/iOS)**: Interesting but labor-intensive for obtaining information from Qak actors across system nodes.
- **Web application**: Faster implementation, especially using container-based technologies like [**Spring**](https://spring.io).

#### Why Spring?
- Lightweight container: Load only required modules.
- Modular framework with layered architecture.
- **Dependency Injection**: Resources provided automatically by the container, avoiding manual binding.

### Structure of wisFacade
The Spring application consists of:
- **Backend**
- **Frontend** (communicates using WebSockets for real-time updates).

Components:
- **FacadeBuilder**: Creates application components.
- **FacadeController**: Entry point of the application.
- **ApplGuiCore**: Routes messages from the broker to WebSocket clients.
- **WSHandler**: Manages WebSocket interactions.
- **MqttFacadeClient**: Interfaces with the MQTT broker.

![GUI Illustration](img/wisfacadegui.png)

### Interaction Between WIS and WISFacade
Two communication protocols:
- **MQTT (publish/subscribe)**: Provides greater decoupling and reuses previous sprint's work.
- **COAP**: Real-time verification but requires significant refactoring of the Qak model.

#### Modifications for MQTT
- **Monitoring Device**: Added payload for AshStorage level in `ashStorageState` event. Updates on distance variations. [Updated model](./progetto/monitoringDevice/src/monitoringdevice.qak)
- **Scale**: Sends updates with the number of RP in WasteStorage. [Updated model](./progetto/scale/src/scale.qak)
- **WIS**: Introduced `opRobotState` and `opRobotJob` events. Added a `raspberryinfocontroller` auxiliary actor. [Updated model](./progetto/wis/src/WasteIncineratorService_progetto.qak)

#### Configuration Management
Configurations are separated into files, read using Kotlin objects (e.g., [WISConfigReader.kt](./progetto/wis/src/main/resources/WISConfigReader.kt)).

### Data Security
Measures for securing the web application include:
- Preventing message tampering using authentication and encryption.
- Addressing DoS attacks and "man-in-the-middle" risks.
The client has told us that this aspect can be assessed in future work.

## RaspberryMockFacade
An alternative mock application to simulate Raspberry Pi devices for testing. Built using Spring and QAK.  
![Mock GUI](./userDocs/img/raspberrymockgui.png)

#### RaspberryMock QAK Model
Added `raspberrymockproxy` to handle Spring interactions. MonitoringDevice sends approximate AshStorage levels. [QAK Model](./progetto/raspberrymock/src/raspberrymock.qak)

#### RaspberryMock Spring Application
Spring application components:
- **FacadeBuilder**: Creates components.
- **FacadeController**: Entry point.
- **ApplGuiCore**: Updates WebSocket clients and `raspberrymockproxy`.
- **WSHandler**: Manages WebSocket interactions.

## Testing Plan
SpringBoot allows automated testing with `@SpringBootTest`.  
Example: A GET request to retrieve the RobotFacade page.

- **TestRobotFacade**: Navigate to `/sprint3/progetto/wisFacade` and execute `./gradlew test`. View output [here](./progetto/wisFacade/build/reports/tests/test/classes/unibo.wisFacade.FacadeApplicationTests.html).  
[Automated Test](./progetto/wisFacade/src/test/java/unibo/wisFacade/FacadeApplicationTests.java)

## Deployment

### Deployment with Gradle
Steps:
1. **VirtualRobot**: In `it.unibo.virtualRobot2023`, run `docker compose -f ./virtualRobot23.yaml up`.
2. **BasicRobot**: In `unibo.basicrobot24`, run `gradlew run`.
3. **WasteIncineratorService**: In `sprint2`, run `gradlew run`.

Other steps for **Scale** and **MonitoringDevice**:
4. Create executables using `./gradlew distZip`.
5. Transfer zip files to Raspberry Pis.
6. Extract and execute files in `bin` directory.

### Deployment with Docker

Deployment using Docker was chosen to create a containerized solution, ready to use, which further decouples the various nodes of the system. This approach also introduces security features, such as authentication and TLS encryption of messages exchanged between different contexts.

#### Steps for Deployment
1. **Creating Dockerfiles**:  
   For each entity involved, a `Dockerfile` was created. These files can be found in the respective folders and contain detailed instructions on how to build the corresponding images.

2. **Securing MQTT Communication**:  
   For each image that connects to the MQTT broker, `stunnel` was configured to enable mutual authentication and encrypt traffic exchanged with the broker. This ensures protection against:
   - External users attempting to read the state of devices.
   - Intrusion attempts into the system.

3. **Launching Services**:  
   Navigate to the `sprint2/progetto/wis` directory and execute the following command to start the **virtual robot**, **basic robot**, **MQTT broker**, and **Waste Incinerator Service (WIS)** together:
   ```docker-compose -f wis.yaml up```

    Deploying on Raspberry Pi:
    To run the Monitoring Device and Scale on Raspberry Pi:
        Download the monitoringdevice and scale images.
        Transfer the raspberry.yaml and stunnel.conf files to the Raspberry Pi.
        Execute:
    ```docker-compose -f raspberry.yaml up```

#### Using Mock Version:
To run a version of WIS using mock components instead of Raspberry Pi, execute: ```docker-compose -f raspberry.yaml up```

#### Alternative Approach: Docker Swarm

For automated deployment directly onto system nodes, a tool like Docker Swarm could be used. This would also allow traffic encryption between containers without relying on external configurations, as done currently. This possibility will however be further investigated in future releases.

## Future work
In the next release we plan to release a version of the system that uses a physical robot

## Authors

Bryan Bertoni: bryan.bertoni@studio.unibo.it  
Gabriele Daga: gabriele.daga@studio.unibo.it  
Emanuele D'Arsié: emanuele.darsie@studio.unibo.it  