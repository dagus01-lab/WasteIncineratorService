/* Generated by AN DISI Unibo */ 
package it.unibo.wismonitoringdeviceproxy

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
import main.resources.WISConfigReader
import main.resources.WISConfig

class Wismonitoringdeviceproxy ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = WISConfigReader.loadWISConfig("wis_conf.json")
		
				var Status = -1;
				var previousStatus = -1;
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "$broker_url", "wismonitoringdeviceproxynat" )
						CommUtils.outcyan("$name | CREATED  (and connected to mosquitto) ... ")
						delay(1000) 
						subscribe(  "wisinfo" ) //mqtt.subscribe(this,topic)
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
				 	 					  scope, context!!, "local_tout_"+name+"_wait", 15000.toLong() )  //OCT2023
					}	 	 
					 transition(edgeName="t02",targetState="handleTimeoutExpired",cond=whenTimeout("local_tout_"+name+"_wait"))   
					transition(edgeName="t03",targetState="handleUpdateStatoAshStorage",cond=whenEvent("statoAshStorage"))
				}	 
				state("handleUpdateStatoAshStorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoAshStorage(N,D)"), Term.createTerm("statoAshStorage(N,D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												try{
													var Status = payloadArg(0).toInt()
								if( Status>=0 && Status != previousStatus 
								 ){forward("ashesLevel", "ashesLevel($Status)" ,"raspberryinfocontroller" ) 
								}
									
												previousStatus=Status
												}catch(e:Exception){
								CommUtils.outred("$name received invalid payload:${payloadArg(0)}")
								
												}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleTimeoutExpired") { //this:State
					action { //it:State
						previousStatus=-1 
						forward("monitoringDeviceOff", "monitoringDeviceOff(1)" ,"raspberryinfocontroller" ) 
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
