package com.project.sensor.pingProcessor.controller;

import com.project.sensor.pingProcessor.service.SensorService;
import com.project.sensor.pingProcessor.response.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendPing_Success() {
        int sensorID = 1;
        doNothing().when(sensorService).processPing(sensorID);

        ResponseEntity<ApiResponse> response = sensorController.sendPing(sensorID);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Ping processed successfully", responseBody.getMessage());
        verify(sensorService, times(1)).processPing(sensorID);
    }

    @Test
    void testSendPing_Failure() {
        int sensorID = 1;
        doThrow(new RuntimeException("Error processing ping")).when(sensorService).processPing(sensorID);

        ResponseEntity<ApiResponse> response = sensorController.sendPing(sensorID);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Failed to process ping for sensor ID", responseBody.getMessage());
        verify(sensorService, times(1)).processPing(sensorID);
    }

    @Test
    void testGetTopFrequencySensor_Success() {
        int k = 5;
        int topSensorID = 3;
        when(sensorService.getTopKMaxFreqSensorID(k)).thenReturn(topSensorID);

        ResponseEntity<ApiResponse> response = sensorController.getTopFrequencySensor(k);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(topSensorID, responseBody.getData()); // Assuming data is the property that holds the top sensor ID
        verify(sensorService, times(1)).getTopKMaxFreqSensorID(k);
    }

    @Test
    void testGetTopFrequencySensor_BadRequest() {
        int k = -1;

        ResponseEntity<ApiResponse> response = sensorController.getTopFrequencySensor(k);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Parameter 'k' must be greater than 0", responseBody.getMessage());
        verify(sensorService, never()).getTopKMaxFreqSensorID(anyInt());
    }

    @Test
    void testGetTopFrequencySensor_NoDataFound() {
        int k = 5;
        when(sensorService.getTopKMaxFreqSensorID(k)).thenReturn(-1);

        ResponseEntity<ApiResponse> response = sensorController.getTopFrequencySensor(k);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("No sensor data found for the given range", responseBody.getMessage());
        verify(sensorService, times(1)).getTopKMaxFreqSensorID(k);
    }

    @Test
    void testGetTopFrequencySensor_Failure() {
        int k = 5;
        doThrow(new RuntimeException("Error retrieving sensor data")).when(sensorService).getTopKMaxFreqSensorID(k);

        ResponseEntity<ApiResponse> response = sensorController.getTopFrequencySensor(k);
        ApiResponse responseBody = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Failed to retrieve top-frequency sensor", responseBody.getMessage());
        verify(sensorService, times(1)).getTopKMaxFreqSensorID(k);
    }
}
