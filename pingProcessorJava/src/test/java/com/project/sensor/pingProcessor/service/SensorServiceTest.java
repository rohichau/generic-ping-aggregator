package com.project.sensor.pingProcessor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class SensorServiceTest {

    private SensorService sensorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sensorService = new SensorService();
    }

    @Test
    void testProcessPing_NewSensor() {
        int sensorID = 1;
        sensorService.processPing(sensorID);

        // After processing, the event size should be 1 and the frequency for sensorID should be 1
        assertEquals(1, sensorService.getEventSize()); // Assuming you have a method to get the event size
        assertEquals(1, sensorService.getFrequency(sensorID)); // Assuming you have a method to get the frequency of a sensor
    }

    @Test
    void testProcessPing_ExistingSensor() {
        int sensorID = 1;
        sensorService.processPing(sensorID); // First ping
        sensorService.processPing(sensorID); // Second ping

        assertEquals(2, sensorService.getEventSize());
        assertEquals(2, sensorService.getFrequency(sensorID)); // Frequency should now be 2
    }

    @Test
    void testGetTopKMaxFreqSensorID_NoEvents() {
        int result = sensorService.getTopKMaxFreqSensorID(5);
        assertEquals(-1, result); // Should return -1 when no events have been processed
    }

    @Test
    void testGetTopKMaxFreqSensorID_SingleEvent() {
        sensorService.processPing(1); // Process a ping for sensorID 1
        int result = sensorService.getTopKMaxFreqSensorID(1);
        assertEquals(1, result); // Should return 1 since it's the only sensor pinged
    }

    @Test
    void testGetTopKMaxFreqSensorID_MultipleEvents() {
        sensorService.processPing(1); // 1
        sensorService.processPing(2); // 1
        sensorService.processPing(1); // 2       1 -> 2, 2 -> 1
        sensorService.processPing(2); // 2
        sensorService.processPing(3); // 1        1 -> 2, 2 -> 2, 3 -> 1
        sensorService.processPing(3);
        int result = sensorService.getTopKMaxFreqSensorID(3);
        assertEquals(3, result); // Sensor 2 has the highest frequency
    }

    @Test
    void testGetTopKMaxFreqSensorID_ValidK() {
        sensorService.processPing(1);
        sensorService.processPing(2);
        sensorService.processPing(1);
        sensorService.processPing(2);
        sensorService.processPing(1); // Frequency of sensor 1 is 3, sensor 2 is 2

        int result = sensorService.getTopKMaxFreqSensorID(3);
        assertEquals(1, result); // Sensor 1 should be the top sensor
    }

    @Test
    void testGetTopKMaxFreqSensorID_InvalidK() {
        sensorService.processPing(1);
        sensorService.processPing(2);
        sensorService.processPing(1); // Frequency of sensor 1 is 2, sensor 2 is 1

        int result = sensorService.getTopKMaxFreqSensorID(4); // k > eventSize
        assertEquals(1, result); // Should return sensor ID with the highest frequency (sensor 1)
    }
}
