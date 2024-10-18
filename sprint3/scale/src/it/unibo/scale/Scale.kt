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
import main.resources.ScaleConfigReader
import main.resources.ScaleConfig

class Scale ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = ScaleConfigReader.loadScaleConfig("scale_conf.json")
		 
				var RPs = 0;
				var previous_RPs = 0; 
				val WRP = config.WRP;
				var timeLastUpdate = System.currentTimeMillis();
				var timeout = config.timeout;
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1000) 
						subscribeToLocalActor("scaledevice") 
						CommUtils.outblue("$name subscribed to scaledevice")
						connectToMqttBroker( "$broker_url", "scalenat" )
						CommUtils.outblue("$name | CREATED  (and connected to mosquitto) ... ")
						subscribe(  "wisinfo" ) //mqtt.subscribe(this,topic)
						//val m = MsgUtil.buildEvent(name, "num_RP", "num_RP(0)" ) 
						publish(MsgUtil.buildEvent(name,"num_RP","num_RP(0)").toString(), "wisinfo" )   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="filter",cond=whenEvent("scaledata"))
				}	 
				state("filter") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  
									      		var W = payloadArg(0).toInt() 
									      		RPs = (W/WRP).toInt()
								if( previous_RPs != RPs 
								 ){//val m = MsgUtil.buildEvent(name, "num_RP", "num_RP($RPs)" ) 
								publish(MsgUtil.buildEvent(name,"num_RP","num_RP($RPs)").toString(), "wisinfo" )   
								}
								 previous_RPs = RPs;  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t01",targetState="filter",cond=whenEvent("scaledata"))
				}	 
			}
		}
} 
