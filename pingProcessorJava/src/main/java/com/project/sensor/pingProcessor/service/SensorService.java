package com.project.sensor.pingProcessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);

    private Map<Integer, Map<Integer, Integer>> sensorFrequencyIndexedMap;
    private int eventSize;

    public SensorService() {
        sensorFrequencyIndexedMap = new HashMap<>();
        eventSize = 0;
    }

    public int getEventSize() {
        return eventSize;
    }

    public int getFrequency(int sensorID) {
        Map<Integer, Integer> sensorFrequencyMap = sensorFrequencyIndexedMap.get(eventSize);
        return sensorFrequencyMap.getOrDefault(sensorID, 0);
    }

    public void processPing(int sensorID) {
        try {
            logger.info("Processing ping for sensor ID: {}", sensorID);

            // Increment the event size
            eventSize++;
            logger.debug("Event size incremented to: {}", eventSize);

            // Get the last frequency map or create a new one
            Map<Integer, Integer> sensorFrequencyMap = sensorFrequencyIndexedMap
                    .get(sensorFrequencyIndexedMap.size());

            if (sensorFrequencyMap == null) {
                sensorFrequencyMap = new HashMap<>();
                logger.debug("Created a new frequency map for event size: {}", eventSize);
            } else {
                // Create a copy of the map to avoid modifying the original map
                sensorFrequencyMap = new HashMap<>(sensorFrequencyMap);
                logger.debug("Copied the existing frequency map for event size: {}", eventSize);
            }

            // Update the frequency of the sensor
            sensorFrequencyMap.put(sensorID, sensorFrequencyMap.getOrDefault(sensorID, 0) + 1);
            logger.info("Updated frequency for sensor ID: {} to {}", sensorID, sensorFrequencyMap.get(sensorID));

            // Store the updated map in the indexed map with the current event size
            sensorFrequencyIndexedMap.put(eventSize, sensorFrequencyMap);
            logger.debug("Stored the frequency map for event size: {}", eventSize);

        } catch (Exception e) {
            logger.error("Error occurred while processing ping for sensor ID: {}", sensorID, e);
            throw new RuntimeException("Failed to process ping for sensor ID: " + sensorID, e);
        }
    }

    public int getTopKMaxFreqSensorID(int k) {
        try {
            if (eventSize == 0) {
                logger.warn("No sensor events have been processed yet.");
                return -1;
            }

            logger.info("Calculating top-frequency sensor ID for last {} pings.", k);
            if (eventSize <= k) {
                return extractLargestFreqSensorID(sensorFrequencyIndexedMap, eventSize);
            }

            int pingID1 = eventSize;
            int pingID2 = eventSize - k;
            return extractLargestFreqSensorIDFromPingDiff(sensorFrequencyIndexedMap, pingID1, pingID2);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving top-frequency sensor for k: {}", k, e);
            throw new RuntimeException("Failed to retrieve top-frequency sensor for k: " + k, e);
        }
    }

    private int extractLargestFreqSensorID(Map<Integer, Map<Integer, Integer>> sensorFrequencyIndexedMap, int pingID) {
        Map<Integer, Integer> sensorFrequencyMap = sensorFrequencyIndexedMap.get(pingID);
        if (sensorFrequencyMap == null) {
            logger.warn("No frequency data found for ping ID: {}", pingID);
            return -1;
        }

        int maxSensorID = -1;
        int maxFrequency = -1;

        for (Map.Entry<Integer, Integer> entry : sensorFrequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                maxSensorID = entry.getKey();
            }
        }
        logger.info("Sensor with max frequency found: Sensor ID = {}, Frequency = {}", maxSensorID, maxFrequency);
        return maxSensorID;
    }

    private int extractLargestFreqSensorIDFromPingDiff(Map<Integer, Map<Integer, Integer>> sensorFrequencyIndexedMap,
            int pingID1, int pingID2) {
        Map<Integer, Integer> sensorFrequencyMap1 = sensorFrequencyIndexedMap.get(pingID1);
        Map<Integer, Integer> sensorFrequencyMap2 = sensorFrequencyIndexedMap.get(pingID2);

        if (sensorFrequencyMap1 == null || sensorFrequencyMap2 == null) {
            logger.warn("Frequency data not available for pingID1: {} or pingID2: {}", pingID1, pingID2);
            return -1;
        }

        int maxSensorID = -1;
        int maxFrequencyDifference = -1;

        for (Map.Entry<Integer, Integer> entry : sensorFrequencyMap1.entrySet()) {
            int sensorID = entry.getKey();
            int frequency1 = entry.getValue();
            int frequency2 = sensorFrequencyMap2.getOrDefault(sensorID, 0);
            int frequencyDifference = frequency1 - frequency2;

            if (frequencyDifference > maxFrequencyDifference) {
                maxFrequencyDifference = frequencyDifference;
                maxSensorID = sensorID;
            }
        }

        logger.info("Sensor with max frequency difference found: Sensor ID = {}, Frequency Difference = {}",
                maxSensorID, maxFrequencyDifference);
        return maxSensorID;
    }
}
