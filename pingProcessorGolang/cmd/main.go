package main

import (
	"log"
	"net/http"

	"pingProcessorGolang/controller"
	"pingProcessorGolang/service"

	"github.com/gorilla/mux"
)

func main() {
    // Create a new router
    router := mux.NewRouter()

    sensorService := service.NewSensorService()

    // Initialize the controller
    sensorController := controller.NewSensorController(*sensorService)

    // Define the API routes
    router.HandleFunc("/api/sensors/ping", sensorController.SendPing).Methods("POST")  // Remove the '()'
    router.HandleFunc("/api/sensors/top-frequency-sensor", sensorController.GetTopFrequencySensor).Methods("GET")  // Remove the '()'

    // Start the HTTP server
    log.Println("Starting server on :8080")
    if err := http.ListenAndServe(":8080", router); err != nil {
        log.Fatal("Server failed to start:", err)
    }
}
