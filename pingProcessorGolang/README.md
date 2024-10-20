# Sensor Ping Processor API (Go)

This Go-based API allows you to send sensor pings and retrieve the sensor with the highest frequency based on sliding windows of past events.

## Directory Structure
- `cmd/main.go`: Entry point for running the API.
- `controller/sensor_controller.go`: Handles HTTP requests and routes.
- `service/sensor_service.go`: Contains the business logic for processing pings.
- `response/api_response.go`: Defines the structure of the API response.

## Prerequisites

- Go version 1.16 or later.

## Installation

1. Clone this repository:
    ```
    git clone https://github.com/rohichau/generic-ping-aggregator.git
    cd pingProcessorGolang
    ```

2. Install dependencies:
    ```
    go mod tidy
    ```

## Running the API

To run the server:

```
go run cmd/main.go
```

By default, the API will be available on `http://localhost:8080`.

## API Endpoints

1. **Send Ping:**
    ```
    POST /api/sensors/ping?sensorID=<sensorID>
    ```

2. **Get Top Frequency Sensor:**
    ```
    GET /api/sensors/top-frequency-sensor?k=<k>
    ```

## Example Curl Commands

1. Send a ping to sensor:
    ```
    curl -X POST "http://localhost:8080/api/sensors/ping?sensorID=1"
    ```

2. Get top-frequency sensor from last `k` pings:
    ```
    curl -X GET "http://localhost:8080/api/sensors/top-frequency-sensor?k=5"
    ```
