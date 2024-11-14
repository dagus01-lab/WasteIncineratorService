/* Generated by AN DISI Unibo */ 
package it.unibo.wisscaleproxy

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

class Wisscaleproxy ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = WISConfigReader.loadWISConfig("wis_conf.json")
		
				var RPs = 0;
				var previous_RPs = 0;
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						connectToMqttBroker( "$broker_url", "wisscaleproxynat" )
						CommUtils.outmagenta("$name | CREATED  (and connected to mosquitto) ... ")
						var basicrobot =  
						CommUtils.outred("${System.getenv("BASICROBOT")}")
						subscribe(  "wisinfo" ) //mqtt.subscribe(this,topic)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handle_new_RP",cond=whenEvent("num_RP"))
				}	 
				state("handle_new_RP") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("num_RP(N)"), Term.createTerm("num_RP(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												try{
													RPs = payloadArg(0).toInt()
								if( RPs >= 0 && RPs != previous_RPs 
								 ){forward("arrived_RP", "arrived_RP($RPs)" ,"raspberryinfocontroller" ) 
								previous_RPs = RPs 
								}
								
												} catch(e:Exception){
								CommUtils.outred("$name received invalid payload:${payloadArg(0)}")
								
												}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="handle_new_RP",cond=whenEvent("num_RP"))
				}	 
			}
		}
} 
