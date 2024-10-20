package controller

import (
	"encoding/json"
	"log"
	"net/http"
	"pingProcessorGolang/response"
	"pingProcessorGolang/service"
	"strconv"
)

type SensorController struct {
	sensorService service.SensorService
}

func NewSensorController(sensorService service.SensorService) *SensorController {
	return &SensorController{sensorService: sensorService}
}

func (s *SensorController) SendPing(w http.ResponseWriter, r *http.Request) {
	// Get the sensorID from the query parameters
	sensorIDStr := r.URL.Query().Get("sensorID")
	sensorID, err := strconv.Atoi(sensorIDStr)
	if err != nil {
		log.Printf("Invalid sensor ID: %v", err)
		response := response.ApiResponse{
			Success: false,
			Message: "Invalid sensor ID",
			Data:    nil,
		}
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(response)
		return
	}

	// Process the ping
	s.sensorService.ProcessPing(sensorID)
	log.Printf("Ping successfully processed for sensor ID: %d", sensorID)

	// Return success response
	response := response.ApiResponse{
		Success: true,
		Message: "Ping processed successfully",
		Data:    sensorID,
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

func (s *SensorController) GetTopFrequencySensor(w http.ResponseWriter, r *http.Request) {
	// Get the 'k' parameter from the query parameters
	kStr := r.URL.Query().Get("k")
	k, err := strconv.Atoi(kStr)
	if err != nil || k <= 0 {
		log.Printf("Invalid range k: %v", err)
		response := response.ApiResponse{
			Success: false,
			Message: "Parameter 'k' must be greater than 0",
			Data:    nil,
		}
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(response)
		return
	}

	// Retrieve the sensor with the highest frequency
	topSensorID := s.sensorService.GetTopKMaxFreqSensorID(k)
	if topSensorID == -1 {
		log.Printf("No sensor data available for the given range: %d", k)
		response := response.ApiResponse{
			Success: false,
			Message: "No sensor data found for the given range",
			Data:    nil,
		}
		w.WriteHeader(http.StatusNotFound)
		json.NewEncoder(w).Encode(response)
		return
	}

	log.Printf("Top frequency sensor ID found: %d", topSensorID)

	// Return success response
	response := response.ApiResponse{
		Success: true,
		Message: "Top frequency sensor found",
		Data:    topSensorID,
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}
