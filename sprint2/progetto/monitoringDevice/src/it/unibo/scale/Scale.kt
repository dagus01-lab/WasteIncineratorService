/* Generated by AN DISI Unibo */ 
package it.unibo.scale

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

class Scale ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		var Level=0 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name | STARTS")
						connectToMqttBroker( "tcp://localhost:8081", "scalenat" )
						//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage(0)" ) 
						publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage(0)").toString(), "wisinfo" )   
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
				 	 		stateTimer = TimerActor("timer_wait", 
				 	 					  scope, context!!, "local_tout_"+name+"_wait", 10000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t00",targetState="keepConnectionAlive",cond=whenTimeout("local_tout_"+name+"_wait"))   
					transition(edgeName="t01",targetState="handleNewStatoAshStorage",cond=whenEvent("statoAshStorage"))
				}	 
				state("handleNewStatoAshStorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoAshStorage(N)"), Term.createTerm("statoAshStorage(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								Level = payloadArg(0) 
								//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage($Level)" ) 
								publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage($Level)").toString(), "wisinfo" )   
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("keepConnectionAlive") { //this:State
					action { //it:State
						//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage($Level)" ) 
						publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage($Level)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 