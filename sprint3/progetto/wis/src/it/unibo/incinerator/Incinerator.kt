/* Generated by AN DISI Unibo */ 
package it.unibo.incinerator

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
import main.resources.WISConfigReader
import main.resources.WISConfig

class Incinerator ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = WISConfigReader.loadWISConfig("wis_conf.json")
		
				var BTIME = config.incineration_duration
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1000) 
						CommUtils.outred("$name STARTS")
						connectToMqttBroker( "$broker_url", "incineratornat" )
						CommUtils.outgreen("$name | CREATED  (and connected to mosquitto) ... ")
						//val m = MsgUtil.buildEvent(name, "statoIncinerator", "statoIncinerator(OFF)" ) 
						publish(MsgUtil.buildEvent(name,"statoIncinerator","statoIncinerator(OFF)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="turnOn",cond=whenDispatch("activationCommand"))
				}	 
				state("turnOn") { //this:State
					action { //it:State
						CommUtils.outred("Incinerator is on. Waiting for RPs to burn...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t035",targetState="handleStartBurning",cond=whenDispatch("startBurning"))
				}	 
				state("handleStartBurning") { //this:State
					action { //it:State
						CommUtils.outred("Incinerator is burning...")
						//val m = MsgUtil.buildEvent(name, "statoIncinerator", "statoIncinerator(BURNING)" ) 
						publish(MsgUtil.buildEvent(name,"statoIncinerator","statoIncinerator(BURNING)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handleStartBurning", 
				 	 					  scope, context!!, "local_tout_"+name+"_handleStartBurning", BTIME )  //OCT2023
					}	 	 
					 transition(edgeName="t036",targetState="handleEndBurning",cond=whenTimeout("local_tout_"+name+"_handleStartBurning"))   
				}	 
				state("handleEndBurning") { //this:State
					action { //it:State
						emit("endBurning", "endBurning(1)" ) 
						//val m = MsgUtil.buildEvent(name, "statoIncinerator", "statoIncinerator(OFF)" ) 
						publish(MsgUtil.buildEvent(name,"statoIncinerator","statoIncinerator(OFF)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t037",targetState="handleStartBurning",cond=whenDispatch("startBurning"))
				}	 
			}
		}
} 
