# generic-ping-aggregator
Aggregate sensors ping metrics and query in real time

# Note: 
```
The separate README.md files are present in each language implementation folder of the project.
```

# API DOCUMENTATION:

## POST /api/sensors/ping
Sends a ping to the service for the given sensorID. This increments the frequency of the sensor in the system.

### Request:

* Method: POST
* URL: /api/sensors/ping
* Query Parameter: sensorID (ID of the sensor sending the ping)

### Response:
* Status: 200 OK
* Body:
```
{
  "message": "Ping processed for sensor ID: 1"
}
```

### Example curl Request:

```
curl -X POST "http://localhost:8080/api/sensors/ping" -H "Content-Type: application/json" -d '{"sensorID": 1}'
```

## GET /api/sensors/top-frequency-sensor
Retrieves the sensor ID that has the highest number of pings in the last k events.

### Request:
* Method: GET
* URL: /api/sensors/top-frequency-sensor
* Query Parameter: k (the number of recent pings to consider)

### Example curl Request:

```
curl -X GET "http://localhost:8080/api/sensors/top-frequency-sensor?k=5"
```

### Response:
* Status: 200 OK
* Body:

```
{
  "sensorID": 1
}
```
