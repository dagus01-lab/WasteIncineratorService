/* Generated by AN DISI Unibo */ 
package it.unibo.oprobot

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

class Oprobot ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
			
				var HOMEx = 0
				var HOMEy = 0
				var WASTEINx = 0
				var WASTEINy = 4
				var BURNINx = 3
				var BURNINy = 2
				var BURNOUTx = 4
				var BURNOUTy = 3
				var ASHOUTx = 6
				var ASHOUTy = 4
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name STARTS")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="engage", cond=doswitch() )
				}	 
				state("engage") { //this:State
					action { //it:State
						CommUtils.outyellow("$name | $MyName engaging ... ")
						request("engage", "engage($MyName,330)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_engage", 
				 	 					  scope, context!!, "local_tout_"+name+"_engage", 1000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t018",targetState="noResponse",cond=whenTimeout("local_tout_"+name+"_engage"))   
					transition(edgeName="t019",targetState="waitingWorking",cond=whenReply("engagedone"))
					transition(edgeName="t020",targetState="end",cond=whenReply("engagerefused"))
				}	 
				state("noResponse") { //this:State
					action { //it:State
						CommUtils.outyellow("BasicRobot did not answer!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="end", cond=doswitch() )
				}	 
				state("waitingWorking") { //this:State
					action { //it:State
						CommUtils.outyellow("$name is in HOME position waiting for working")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="takeRP",cond=whenDispatch("arrived_RP"))
				}	 
				state("takeRP") { //this:State
					action { //it:State
						CommUtils.outyellow("OpRobot is going to take an RP..")
						request("moverobot", "moverobot($WASTEINx,$WASTEINy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t022",targetState="bringRPtoBURNIN",cond=whenReply("moverobotdone"))
					transition(edgeName="t023",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("bringRPtoBURNIN") { //this:State
					action { //it:State
						delay(2000) 
						request("moverobot", "moverobot($BURNINx,$BURNINy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t024",targetState="returnHOME",cond=whenReply("moverobotdone"))
					transition(edgeName="t025",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("returnHOME") { //this:State
					action { //it:State
						delay(2000) 
						forward("rpInBurnin", "rpInBurnin(1)" ,"wis" ) 
						CommUtils.outyellow("An RP is in BURNIN port")
						request("moverobot", "moverobot($HOMEx,$HOMEy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t026",targetState="waitingForIncinerator",cond=whenReply("moverobotdone"))
					transition(edgeName="t027",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("waitingForIncinerator") { //this:State
					action { //it:State
						CommUtils.outyellow("Waiting for incinerator to finish its job")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t028",targetState="takeAshFromBURNOUT",cond=whenEvent("endBurning"))
				}	 
				state("takeAshFromBURNOUT") { //this:State
					action { //it:State
						request("moverobot", "moverobot($BURNOUTx,$BURNOUTy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t029",targetState="bringAshtoASHOUT",cond=whenReply("moverobotdone"))
					transition(edgeName="t030",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("bringAshtoASHOUT") { //this:State
					action { //it:State
						delay(2000) 
						request("moverobot", "moverobot($ASHOUTx,$ASHOUTy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t031",targetState="restartJob",cond=whenReply("moverobotdone"))
					transition(edgeName="t032",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("restartJob") { //this:State
					action { //it:State
						delay(2000) 
						CommUtils.outyellow("The ash has been taken out")
						forward("newAshes", "newAshes(1)" ,"wis" ) 
						request("moverobot", "moverobot($HOMEx,$HOMEy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t033",targetState="takeRpBeforeFinishPlan",cond=whenDispatch("arrived_RP"))
					transition(edgeName="t034",targetState="waitingWorking",cond=whenReply("moverobotdone"))
					transition(edgeName="t035",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("takeRpBeforeFinishPlan") { //this:State
					action { //it:State
						CommUtils.outyellow("A new RP has arrived before OpRobot returned HOME")
						delay(500) 
						emit("alarm", "alarm(1)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t036",targetState="testko",cond=whenReply("moverobotdone"))
					transition(edgeName="t037",targetState="takeRP",cond=whenReply("moverobotfailed"))
				}	 
				state("testko") { //this:State
					action { //it:State
						CommUtils.outyellow("BasicRobot ignored alarm!!")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="takeRP", cond=doswitch() )
				}	 
				state("end") { //this:State
					action { //it:State
						CommUtils.outyellow("$name | ENDS ")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("exit") { //this:State
					action { //it:State
						CommUtils.outyellow("$name has been terminated")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
