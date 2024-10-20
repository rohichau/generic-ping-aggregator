package com.project.sensor.pingProcessor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.sensor.pingProcessor.service.SensorService;
import com.project.sensor.pingProcessor.response.ApiResponse;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/ping")
    public ResponseEntity<ApiResponse> sendPing(@RequestParam int sensorID) {
        logger.info("Received ping request for sensor ID: {}", sensorID);
        try {
            sensorService.processPing(sensorID);
            logger.info("Ping successfully processed for sensor ID: {}", sensorID);
            ApiResponse response = new ApiResponse(true, "Ping processed successfully", sensorID);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing ping for sensor ID: {}", sensorID, e);
            ApiResponse response = new ApiResponse(false, "Failed to process ping for sensor ID", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/top-frequency-sensor")
    public ResponseEntity<ApiResponse> getTopFrequencySensor(@RequestParam int k) {
        logger.info("Received request to get top-frequency sensor with range: {}", k);
        try {
            if (k <= 0) {
                logger.warn("Invalid range k: {}. Must be greater than 0.", k);
                ApiResponse response = new ApiResponse(false, "Parameter 'k' must be greater than 0", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            int topSensorID = sensorService.getTopKMaxFreqSensorID(k);
            if (topSensorID == -1) {
                logger.warn("No sensor data available for the given range: {}", k);
                ApiResponse response = new ApiResponse(false, "No sensor data found for the given range", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            logger.info("Top frequency sensor ID found: {}", topSensorID);
            ApiResponse response = new ApiResponse(true, "Top frequency sensor found", topSensorID);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving top-frequency sensor for range: {}", k, e);
            ApiResponse response = new ApiResponse(false, "Failed to retrieve top-frequency sensor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
