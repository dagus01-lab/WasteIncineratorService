/* Generated by AN DISI Unibo */ 
package it.unibo.wastestoragemock

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

class Wastestoragemock ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_s0", 
				 	 					  scope, context!!, "local_tout_"+name+"_s0", 1000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t09",targetState="simulateRP",cond=whenTimeout("local_tout_"+name+"_s0"))   
				}	 
				state("simulateRP") { //this:State
					action { //it:State
						forward("arrived_RP", "arrived_RP(1)" ,"wis" ) 
						forward("arrived_RP", "arrived_RP(1)" ,"wis" ) 
						forward("arrived_RP", "arrived_RP(1)" ,"wis" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
