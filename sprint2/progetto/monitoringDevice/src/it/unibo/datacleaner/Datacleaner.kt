/* Generated by AN DISI Unibo */ 
package it.unibo.datacleaner

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

class Datacleaner ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
				var D = 0;
				var previous_D = 0; 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1000) 
						subscribeToLocalActor("sonardevice") 
						CommUtils.outblue("$name subscribed to sonardevice")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="filter",cond=whenEvent("sonardata"))
				}	 
				state("filter") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  D = payloadArg(0).toInt()  
								CommUtils.outblack("$name D=$D")
								if( D != previous_D 
								 ){CommUtils.outmagenta("$name emit newLevel of Ash")
								emitLocalStreamEvent("ashStorageLevel", "ashStorageLevel($D)" ) 
								}
								 previous_D = D;  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="filter",cond=whenEvent("sonardata"))
				}	 
			}
		}
} 
