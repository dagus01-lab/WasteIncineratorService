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

class Incinerator ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val BTIME = 10000L; var stato = 0;  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outred("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="turnOn",cond=whenDispatch("activationCommand"))
				}	 
				state("turnOn") { //this:State
					action { //it:State
						CommUtils.outred("Incinerator is on. Waiting for RPs to burn...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("handleStartBurning") { //this:State
					action { //it:State
						CommUtils.outred("Incinerator is burning...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handleStartBurning", 
				 	 					  scope, context!!, "local_tout_"+name+"_handleStartBurning", BTIME )  //OCT2023
					}	 	 
					 transition(edgeName="t03",targetState="handleEndBurning",cond=whenTimeout("local_tout_"+name+"_handleStartBurning"))   
				}	 
				state("handleEndBurning") { //this:State
					action { //it:State
						CommUtils.outred("Incinerator has finished to burn")
						emit("endBurning", "endBurning(1)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
