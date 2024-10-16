package main.resources
data class WISConfig{
	val broker_url: String,
	val step_time: Int,
	val incineration_duration: Int,
	val HOMEx: Int,val HOMEy:Int,
	val WASTEINx: Int,val WASTEINy: Int,
	val BURNINx: Int, val BURNINy: Int,
	val BURNOUTx: Int,val BURNOUTy: Int,
	val ASHOUTx: Int, val ASHOUTy:Int
}