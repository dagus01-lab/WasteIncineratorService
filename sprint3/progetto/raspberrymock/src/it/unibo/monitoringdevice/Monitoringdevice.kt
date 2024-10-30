/* Generated by AN DISI Unibo */ 
package it.unibo.monitoringdevice

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
//Sept2024
import org.slf4j.Logger
import org.slf4j.LoggerFactory 
import org.json.simple.parser.JSONParser
import org.json.simple.JSONObject


//User imports JAN2024
import main.resources.MonitoringDeviceConfigReader
import main.resources.MonitoringDeviceConfig

class Monitoringdevice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = MonitoringDeviceConfigReader.loadMDConfig("monitoringdevice_conf.json")
		
				var State = 0
				var broker_url = config.broker_url
				var DLIMIT = config.DLIMIT;
				var DMIN = config.DMIN;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name | STARTS")
						connectToMqttBroker( "$broker_url", "monitoringdevicenat" )
						CommUtils.outcyan("$name | connected to MQTT broker $broker_url")
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
					 transition(edgeName="t02",targetState="keepConnectionAlive",cond=whenTimeout("local_tout_"+name+"_wait"))   
					transition(edgeName="t03",targetState="handleNewStatoAshStorage",cond=whenEvent("statoAshStorage"))
				}	 
				state("handleNewStatoAshStorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoAshStorage(N,L)"), Term.createTerm("statoAshStorage(N,L)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												State = payloadArg(0).toInt()
												when(State){
													0 ->  
								//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage(0,$DMIN)" ) 
								publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage(0,$DMIN)").toString(), "wisinfo" )   
								
													1 ->  
								//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage(1,$DLIMIT)" ) 
								publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage(1,$DLIMIT)").toString(), "wisinfo" )   
								
													else ->  
								CommUtils.outgreen("Invalid input!")
								
												}
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
						
									when(State){
											0 ->  
						//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage(0,$DMIN)" ) 
						publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage(0,$DMIN)").toString(), "wisinfo" )   
						
											1 ->  
						//val m = MsgUtil.buildEvent(name, "statoAshStorage", "statoAshStorage(1,$DLIMIT)" ) 
						publish(MsgUtil.buildEvent(name,"statoAshStorage","statoAshStorage(1,$DLIMIT)").toString(), "wisinfo" )   
						
										}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
} 