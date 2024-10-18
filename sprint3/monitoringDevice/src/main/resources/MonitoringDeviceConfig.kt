package main.resources
data class MonitoringDeviceConfig(
	val broker_url: String = "tcp://localhost:8081",
	val DLIMIT : Int = 10
	val DMIN : Int = 100
	val timeout : Long = 10000
)