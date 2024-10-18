package main.resources
data class ScaleConfig(
	val broker_url: String = "tcp://localhost:8081",
	val WRP : Int = 100
	val timeout : Long = 10000
)