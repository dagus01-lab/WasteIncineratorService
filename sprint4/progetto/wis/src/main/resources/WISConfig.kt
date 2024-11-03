package main.resources
data class WISConfig(
	val broker_url: String = "tcp://localhost:8081",
	val incineration_duration: Long = 10000,
	val step_time: Int = 330,
	val HOMEx: Int = 0,val HOMEy:Int = 0,
	val WASTEINx: Int = 0,val WASTEINy: Int = 4,
	val BURNINx: Int = 3, val BURNINy: Int = 2,
	val BURNOUTx: Int = 4,val BURNOUTy: Int = 3,
	val ASHOUTx: Int = 6, val ASHOUTy:Int = 4
)