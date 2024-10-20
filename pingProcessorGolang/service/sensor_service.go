package service

import (
	"log"
)

type SensorService struct {
	sensorFrequencyIndexedMap map[int]map[int]int
	eventSize                 int
}

func NewSensorService() *SensorService {
	return &SensorService{
		sensorFrequencyIndexedMap: make(map[int]map[int]int),
		eventSize:                 0,
	}
}

func (s *SensorService) ProcessPing(sensorID int) {
	s.eventSize++
	sensorFrequencyMap := s.sensorFrequencyIndexedMap[s.eventSize-1]
	if sensorFrequencyMap == nil {
		sensorFrequencyMap = make(map[int]int)
	}
	newFrequencyMap := make(map[int]int)
	for k, v := range sensorFrequencyMap {
		newFrequencyMap[k] = v
	}
	newFrequencyMap[sensorID] = newFrequencyMap[sensorID] + 1
	s.sensorFrequencyIndexedMap[s.eventSize] = newFrequencyMap
	log.Printf("Ping processed for sensor ID: %d", sensorID)
}

func (s *SensorService) GetTopKMaxFreqSensorID(k int) int {
	if s.eventSize == 0 {
		return -1
	}
	if s.eventSize <= k {
		return s.extractLargestFreqSensorID(s.eventSize)
	}
	return s.extractLargestFreqSensorIDFromPingDiff(s.eventSize, s.eventSize-k)
}

func (s *SensorService) extractLargestFreqSensorID(pingID int) int {
	sensorFrequencyMap := s.sensorFrequencyIndexedMap[pingID]
	maxSensorID, maxFrequency := -1, -1
	for sensorID, frequency := range sensorFrequencyMap {
		if frequency > maxFrequency {
			maxFrequency = frequency
			maxSensorID = sensorID
		}
	}
	return maxSensorID
}

func (s *SensorService) extractLargestFreqSensorIDFromPingDiff(pingID1, pingID2 int) int {
	sensorFrequencyMap1 := s.sensorFrequencyIndexedMap[pingID1]
	sensorFrequencyMap2 := s.sensorFrequencyIndexedMap[pingID2]
	maxSensorID, maxFrequencyDifference := -1, -1
	for sensorID, frequency1 := range sensorFrequencyMap1 {
		frequency2 := sensorFrequencyMap2[sensorID]
		frequencyDifference := frequency1 - frequency2
		if frequencyDifference > maxFrequencyDifference {
			maxFrequencyDifference = frequencyDifference
			maxSensorID = sensorID
		}
	}
	return maxSensorID
}
