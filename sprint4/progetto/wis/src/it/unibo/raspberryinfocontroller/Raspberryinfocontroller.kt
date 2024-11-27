/* Generated by AN DISI Unibo */ 
package it.unibo.raspberryinfocontroller

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

class Raspberryinfocontroller ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = WISConfigReader.loadWISConfig("wis_conf.json")
		
				var RPs = 0;
				var AshesLevel = -1;
				var previousAshesLevel = -1;
				var wisReady = 0;
				var broker_url = config.broker_url
				var monitoringDeviceRunningTimeThreshold = 10000
				var lastAshStorageStateUpdate = 0L
				var monitoringDeviceRunning = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(500) 
						connectToMqttBroker( "$broker_url", "wismonitoringdeviceproxynat" )
						CommUtils.outcyan("$name | CREATED  (and connected to mosquitto) ... ")
						delay(1000) 
						subscribe(  "wisinfo" ) //mqtt.subscribe(this,topic)
						observeResource("localhost","8125","ctx_waste_incinerator_service","wis","waitingForUpdates")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						monitoringDeviceRunning = if((System.currentTimeMillis() - lastAshStorageStateUpdate) < monitoringDeviceRunningTimeThreshold) 1 else 0 
						CommUtils.outblack("$name | mdrunning=$monitoringDeviceRunning, wisReady=$wisReady, RPs=$RPs, AshesLevel=$AshesLevel")
						if(  monitoringDeviceRunning==1 && wisReady == 1 && previousAshesLevel != AshesLevel 
						 ){forward("ashesLevel", "ashesLevel($AshesLevel)" ,"wis" ) 
						previousAshesLevel = AshesLevel 
						}
						if( wisReady == 1 && RPs>0 && AshesLevel==0 
						 ){forward("arrived_RP", "arrived_RP(1)" ,"wis" ) 
						CommUtils.outred("$name | sent wis a new RP")
						wisReady = 0 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleMonitoringDeviceOff",cond=whenDispatch("monitoringDeviceOff"))
					transition(edgeName="t01",targetState="handleNewStatoAshStorage",cond=whenDispatch("statoAshStorage"))
					transition(edgeName="t02",targetState="handleNewScaleState",cond=whenDispatch("num_RP"))
					transition(edgeName="t03",targetState="handleWISReady",cond=whenDispatch("waitingForUpdates"))
				}	 
				state("handleMonitoringDeviceOff") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name | Waiting for monitoringDevice to run again..")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleNewScaleState") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("num_RP(N)"), Term.createTerm("num_RP(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outmagenta("$name received new RP")
								RPs = payloadArg(0).toInt() 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleWISReady") { //this:State
					action { //it:State
						wisReady = 1 
						CommUtils.outblack("$name | WIS ready to take new RPs")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleNewStatoAshStorage") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoAshStorage(N,D)"), Term.createTerm("statoAshStorage(N,D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												try{
													AshesLevel = payloadArg(0).toInt()
													lastAshStorageStateUpdate = System.currentTimeMillis()
												} catch(e:Exception){
								CommUtils.outred("$name | received invalid payload for statoAshStorage:${payloadArg(0)}")
								
												}
											
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
