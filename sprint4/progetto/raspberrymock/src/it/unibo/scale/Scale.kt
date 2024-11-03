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
//Sept2024
import org.slf4j.Logger
import org.slf4j.LoggerFactory 
import org.json.simple.parser.JSONParser
import org.json.simple.JSONObject


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
		
				var RPs = 0
				var broker_url = config.broker_url
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("$name | STARTS")
						connectToMqttBroker( "$broker_url", "scalenat" )
						CommUtils.outcyan("$name | connected to MQTT broker $broker_url")
						//val m = MsgUtil.buildEvent(name, "num_RP", "num_RP($RPs)" ) 
						publish(MsgUtil.buildEvent(name,"num_RP","num_RP($RPs)").toString(), "wisinfo" )   
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
					 transition(edgeName="t04",targetState="keepConnectionAlive",cond=whenTimeout("local_tout_"+name+"_wait"))   
					transition(edgeName="t05",targetState="notifyNewRP",cond=whenEvent("num_RP"))
				}	 
				state("notifyNewRP") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("num_RP(N)"), Term.createTerm("num_RP(N)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								RPs = payloadArg(0).toInt() 
								//val m = MsgUtil.buildEvent(name, "num_RP", "num_RP($RPs)" ) 
								publish(MsgUtil.buildEvent(name,"num_RP","num_RP($RPs)").toString(), "wisinfo" )   
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
						//val m = MsgUtil.buildEvent(name, "num_RP", "num_RP($RPs)" ) 
						publish(MsgUtil.buildEvent(name,"num_RP","num_RP($RPs)").toString(), "wisinfo" )   
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
