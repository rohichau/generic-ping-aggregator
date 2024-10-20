# Sensor Ping Processor API
This project implements a simple service that processes sensor pings and retrieves the sensor with the highest frequency of pings. The application is built using Spring Boot with a clear separation between the controller and service layers. The APIs provided allow clients to:

Send a ping from a sensor.
Retrieve the sensor with the highest frequency of pings over a specified number of recent pings.
# Features
* POST /api/sensors/ping: Send a ping to the service with the sensor ID.
* GET /api/sensors/top-frequency-sensor: Retrieve the sensor ID with the highest frequency in the last k pings.

## Requirements
* Java 8 or higher
* Maven 3.x
* Spring Boot 2.6.x or higher

## Setup and Installation

### 1. Clone the Repository:
```
git clone https://github.com/your-repo/sensor-ping-processor.git
cd sensor-ping-processor
```

### 2. Build the Project:

```
mvn clean install
```

### 3. Run the Application:

```
mvn spring-boot:run
```

The application will start on http://localhost:8080.

### Running Tests
The project includes a test suite for the controller and service logic. To run the tests, execute the following command:

```
mvn test
```