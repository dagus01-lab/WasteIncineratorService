/* Generated by AN DISI Unibo */ 
package it.unibo.monitoring_device_mok

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

class Monitoring_device_mok ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 var ashLevel = 0; val MAX_LEVEL = 4  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t031",targetState="handleAshes",cond=whenDispatch("newAshes"))
				}	 
				state("handleAshes") { //this:State
					action { //it:State
						 ashLevel ++  
						if(  ashLevel == MAX_LEVEL  
						 ){CommUtils.outyellow("AshStorage is full. It is not possible to burn new RPs.")
						updateResourceRep( "statoAshStorage(1)"  
						)
						}
						else
						 {updateResourceRep( "statoAshStorage(0)"  
						 )
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handleAshes", 
				 	 					  scope, context!!, "local_tout_"+name+"_handleAshes", 40000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t032",targetState="simulateRemoveAshes",cond=whenTimeout("local_tout_"+name+"_handleAshes"))   
					transition(edgeName="t033",targetState="handleAshes",cond=whenDispatch("newAshes"))
				}	 
				state("simulateRemoveAshes") { //this:State
					action { //it:State
						CommUtils.outyellow("Removed ashes from AshStorage. Now the container is empty.")
						 ashLevel = 0  
						updateResourceRep( "statoAshStorage(0)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="handleAshes",cond=whenDispatch("newAshes"))
				}	 
			}
		}
} 
