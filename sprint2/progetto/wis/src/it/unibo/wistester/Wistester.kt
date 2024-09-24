/* Generated by AN DISI Unibo */ 
package it.unibo.wistester

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023

//User imports JAN2024

class Wistester ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var MonitoringDeviceActivation = 0;
				var ScaleActivation = 0;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1000) 
						CommUtils.outred("$name STARTS")
						observeResource("169.254.12.90","8200","ctxscale","scale","arrived_RP")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="handleScaleInfo",cond=whenRequest("infoScale"))
					transition(edgeName="t035",targetState="handleMonitoringDeviceInfo",cond=whenRequest("infoMonitoringDevice"))
					transition(edgeName="t036",targetState="scaleActivationOk",cond=whenDispatch("arrived_RP"))
					transition(edgeName="t037",targetState="monitoringDeviceActivationOk",cond=whenDispatch("statoAshStorage"))
				}	 
				state("handleScaleInfo") { //this:State
					action { //it:State
						CommUtils.outred("$name Replying with Scale activation $ScaleActivation!")
						answer("infoScale", "infoScaleReply", "infoScaleReply($ScaleActivation)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleMonitoringDeviceInfo") { //this:State
					action { //it:State
						answer("infoMonitoringDevice", "infoMonitoringDeviceReply", "infoMonitoringDeviceReply($MonitoringDeviceActivation)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("scaleActivationOk") { //this:State
					action { //it:State
						ScaleActivation=1 
						CommUtils.outred("$name Scale activation ok!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("monitoringDeviceActivationOk") { //this:State
					action { //it:State
						MonitoringDeviceActivation=1 
						CommUtils.outred("$name MonitoringDevice activation ok")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
} 
