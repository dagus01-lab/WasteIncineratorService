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
		 var AshStorageStatus = 0  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(500) 
						CommUtils.outgreen("$name STARTS")
						forward("activationCommand", "activationCommand(1)" ,"incinerator" ) 
						delay(8000) 
						observeResource("192.168.11.105","8100","ctxmonitoringdevice","monitoringdevice","statoAshStorage")
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
						CommUtils.outgreen("$name OpRobot has finished its route")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_endRoute", 
				 	 					  scope, context!!, "local_tout_"+name+"_endRoute", 2000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t05",targetState="waitingRP",cond=whenTimeout("local_tout_"+name+"_endRoute"))   
					transition(edgeName="t06",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
				}	 
				state("handleUpdateStatoAshStorage") { //this:State
					action { //it:State
						CommUtils.outgreen("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("statoAshStorage(SENDER,N)"), Term.createTerm("statoAshStorage(SENDER,N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  !payloadArg(1).contains("nonews")  
								 ){ AshStorageStatus = payloadArg(1).split("(")[1].split(")")[0].toInt()  
								CommUtils.outgreen("AshStorageStatus: $AshStorageStatus")
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handleUpdateStatoAshStorage", 
				 	 					  scope, context!!, "local_tout_"+name+"_handleUpdateStatoAshStorage", 2000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t07",targetState="ashCheckFinish",cond=whenTimeout("local_tout_"+name+"_handleUpdateStatoAshStorage"))   
					transition(edgeName="t08",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
				}	 
				state("ashCheckFinish") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitingRP", cond=doswitchGuarded({ AshStorageStatus == 0  
					}) )
					transition( edgeName="goto",targetState="waitingAshesToBeRemoved", cond=doswitchGuarded({! ( AshStorageStatus == 0  
					) }) )
				}	 
				state("waitingAshesToBeRemoved") { //this:State
					action { //it:State
						CommUtils.outgreen("WIS is waiting for an operator to remove ashes in AshStorage...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="handleUpdateStatoAshStorage",cond=whenDispatch("statoAshStorage"))
				}	 
			}
		}
} 
