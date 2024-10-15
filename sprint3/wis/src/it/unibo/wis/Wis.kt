/* Generated by AN DISI Unibo */ 
package it.unibo.wis

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

class Wis ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 var statoAshStorage = 0  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(500) 
						CommUtils.outgreen("$name STARTS")
						observeResource("192.168.1.105","8100","ctxmonitoringdevice","monitoringdevice","statoAshStorage")
						forward("activationCommand", "activationCommand(1)" ,"incinerator" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitingRP", cond=doswitch() )
				}	 
				state("waitingRP") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
					transition(edgeName="t01",targetState="handleRP",cond=whenDispatch("arrived_RP"))
				}	 
				state("handleRP") { //this:State
					action { //it:State
						CommUtils.outgreen("New RP is arrived")
						forward("arrived_RP", "arrived_RP(1)" ,"oprobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleRPInBurnin",cond=whenDispatch("rpInBurnin"))
				}	 
				state("handleRPInBurnin") { //this:State
					action { //it:State
						CommUtils.outgreen("An RP is ready to be burnt")
						forward("startBurning", "startBurning(1)" ,"incinerator" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="handleEndBurning",cond=whenEvent("endBurning"))
				}	 
				state("handleEndBurning") { //this:State
					action { //it:State
						CommUtils.outgreen("Incinerator has finished to burn")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="endRoute",cond=whenDispatch("newAshes"))
				}	 
				state("endRoute") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_endRoute", 
				 	 					  scope, context!!, "local_tout_"+name+"_endRoute", 1000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t05",targetState="waitingRP",cond=whenTimeout("local_tout_"+name+"_endRoute"))   
					transition(edgeName="t06",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
				}	 
				state("handleUpdateStatoAshStorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoAshStorage(N)"), Term.createTerm("statoAshStorage(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 statoAshStorage = payloadArg(0).toInt()  
								CommUtils.outgreen("AshStorageStatus: $statoAshStorage")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitingRP", cond=doswitchGuarded({ statoAshStorage == 0  
					}) )
					transition( edgeName="goto",targetState="waitingAshesToBeRemoved", cond=doswitchGuarded({! ( statoAshStorage == 0  
					) }) )
				}	 
				state("waitingAshesToBeRemoved") { //this:State
					action { //it:State
						CommUtils.outgreen("WIS is waiting an operator to remove ashes in AshStorage...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
				}	 
			}
		}
} 
