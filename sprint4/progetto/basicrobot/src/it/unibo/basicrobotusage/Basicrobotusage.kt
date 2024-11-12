/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobotusage

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

class Basicrobotusage ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "ss0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val Plan    = "\"[w, w, l, w, w, w, w]\"" //Come quello restituito da doplan
				val MyName = name 
		return { //this:ActionBasciFsm
				state("ss0") { //this:State
					action { //it:State
						delay(3000) 
						CommUtils.outblack("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						request("engage", "engage(MyName,350)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="dowork",cond=whenReply("engagedone"))
				}	 
				state("dowork") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						request("step", "step(350)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t026",targetState="handleStepDone",cond=whenReply("stepdone"))
					transition(edgeName="t027",targetState="handleStepFail",cond=whenReply("stepfailed"))
				}	 
				state("handleStepDone") { //this:State
					action { //it:State
						CommUtils.outblack("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="dowork", cond=doswitch() )
				}	 
				state("handleStepFail") { //this:State
					action { //it:State
						CommUtils.outblack("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("testdoplan") { //this:State
					action { //it:State
						request("doplan", "doplan($Plan,345)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t028",targetState="testdoplanEnd",cond=whenReply("doplandone"))
					transition(edgeName="t029",targetState="testdoplanEnd",cond=whenReply("doplanfailed"))
				}	 
				state("testdoplanEnd") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						forward("disengage", "disengage($MyName)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
