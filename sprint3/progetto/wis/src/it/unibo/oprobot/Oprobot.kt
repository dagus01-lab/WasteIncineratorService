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
import main.resources.WISConfigReader
import main.resources.WISConfig

class Oprobot ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = WISConfigReader.loadWISConfig("wis_conf.json")
			
				//TODO: evitare di creare variabili per poter usare direttamente gli attributi di config nei messaggi.
				//		Il problema al momento sta nel fatto che il payload dei messaggi nel modello qak non può contenere il carattere "."
				var HOMEx = config.HOMEx
				var HOMEy = config.HOMEy
				var WASTEINx = config.WASTEINx
				var WASTEINy = config.WASTEINy
				var BURNINx = config.BURNINx
				var BURNINy = config.BURNINy
				var BURNOUTx = config.BURNOUTx
				var BURNOUTy = config.BURNOUTy
				var ASHOUTx = config.ASHOUTx
				var ASHOUTy = config.ASHOUTy
				var StepTime = config.step_time
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name STARTS")
						connectToMqttBroker( "$broker_url", "oprobotnat" )
						CommUtils.outblack("$name connected to MQTT server")
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
						request("engage", "engage($MyName,$StepTime)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_engage", 
				 	 					  scope, context!!, "local_tout_"+name+"_engage", 1000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t014",targetState="noResponse",cond=whenTimeout("local_tout_"+name+"_engage"))   
					transition(edgeName="t015",targetState="waitingWorking",cond=whenReply("engagedone"))
					transition(edgeName="t016",targetState="end",cond=whenReply("engagerefused"))
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
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(HOME)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(HOME)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Waiting_for_an_RP)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Waiting_for_an_RP)").toString(), "wisinfo" )   
						CommUtils.outyellow("$name is in HOME position waiting for working")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="takeRP",cond=whenDispatch("arrived_RP"))
				}	 
				state("takeRP") { //this:State
					action { //it:State
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Going_to_WASTEIN)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Going_to_WASTEIN)").toString(), "wisinfo" )   
						CommUtils.outyellow("OpRobot is going to take an RP..")
						request("moverobot", "moverobot($WASTEINx,$WASTEINy)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t018",targetState="bringRPtoBURNIN",cond=whenReply("moverobotdone"))
					transition(edgeName="t019",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("bringRPtoBURNIN") { //this:State
					action { //it:State
						delay(2000) 
						request("moverobot", "moverobot($BURNINx,$BURNINy)" ,"basicrobot" )  
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(WASTEIN)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(WASTEIN)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Going_to_BURNIN)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Going_to_BURNIN)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t020",targetState="returnHOME",cond=whenReply("moverobotdone"))
					transition(edgeName="t021",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("returnHOME") { //this:State
					action { //it:State
						delay(2000) 
						forward("rpInBurnin", "rpInBurnin(1)" ,"wis" ) 
						CommUtils.outyellow("An RP is in BURNIN port")
						request("moverobot", "moverobot($HOMEx,$HOMEy)" ,"basicrobot" )  
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(BURNIN)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(BURNIN)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Returning_HOME)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Returning_HOME)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t022",targetState="waitingForIncinerator",cond=whenReply("moverobotdone"))
					transition(edgeName="t023",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("waitingForIncinerator") { //this:State
					action { //it:State
						CommUtils.outyellow("Waiting for incinerator to finish its job")
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(HOME)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(HOME)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Waiting_for_incinerator_to_finish_its_job)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Waiting_for_incinerator_to_finish_its_job)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t024",targetState="takeAshFromBURNOUT",cond=whenEvent("endBurning"))
				}	 
				state("takeAshFromBURNOUT") { //this:State
					action { //it:State
						request("moverobot", "moverobot($BURNOUTx,$BURNOUTy)" ,"basicrobot" )  
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Going_to_take_ashes_from_BURNOUT)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Going_to_take_ashes_from_BURNOUT)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t025",targetState="bringAshtoASHOUT",cond=whenReply("moverobotdone"))
					transition(edgeName="t026",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("bringAshtoASHOUT") { //this:State
					action { //it:State
						delay(2000) 
						request("moverobot", "moverobot($ASHOUTx,$ASHOUTy)" ,"basicrobot" )  
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(BURNOUT)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(BURNOUT)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Going_to_take_out_ashes_to_ASHOUT)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Going_to_take_out_ashes_to_ASHOUT)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t027",targetState="restartJob",cond=whenReply("moverobotdone"))
					transition(edgeName="t028",targetState="exit",cond=whenReply("moverobotfailed"))
				}	 
				state("restartJob") { //this:State
					action { //it:State
						delay(2000) 
						CommUtils.outyellow("The ash has been taken out")
						forward("newAshes", "newAshes(1)" ,"wis" ) 
						request("moverobot", "moverobot($HOMEx,$HOMEy)" ,"basicrobot" )  
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(ASHOUT)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(ASHOUT)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Returning_HOME)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Returning_HOME)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t029",targetState="takeRpBeforeFinishPlan",cond=whenDispatch("arrived_RP"))
					transition(edgeName="t030",targetState="waitingWorking",cond=whenReply("moverobotdone"))
					transition(edgeName="t031",targetState="exit",cond=whenReply("moverobotfailed"))
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
					 transition(edgeName="t032",targetState="testko",cond=whenReply("moverobotdone"))
					transition(edgeName="t033",targetState="takeRP",cond=whenReply("moverobotfailed"))
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
						//val m = MsgUtil.buildEvent(name, "opRobotState", "opRobotState(FAILURE)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotState","opRobotState(FAILURE)").toString(), "wisinfo" )   
						//val m = MsgUtil.buildEvent(name, "opRobotJob", "opRobotJob(Failed_to_complete_path)" ) 
						publish(MsgUtil.buildEvent(name,"opRobotJob","opRobotJob(Failed_to_complete_path)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
