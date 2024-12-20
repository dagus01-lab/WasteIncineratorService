/* Generated by AN DISI Unibo */ 
package it.unibo.datacleaner

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

class Datacleaner ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 val config = MonitoringDeviceConfigReader.loadMDConfig("monitoringdevice_conf.json")
		 
				var Level = -1;
				var previous_D = -1;
				var D = -1; 
				val DLIMIT = config.DLIMIT;
				val DMIN = config.DMIN; //rappresenta l'altezza del contenitore
				var timeLastUpdate = System.currentTimeMillis();
				var timeout = config.timeout;
				var range = 3;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1100) 
						subscribeToLocalActor("sonardevice") 
						CommUtils.outblue("$name subscribed to sonardevice")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="filter",cond=whenEvent("sonardata"))
				}	 
				state("filter") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								  
									      		D = payloadArg(0).toInt()
									      		Level = if (D > DMIN) {
													    0
													} else if (D < DLIMIT) {
													    2
													} else {
													    1
													}
								if( D - range > previous_D || D+range < previous_D 
								 ){CommUtils.outmagenta("$name emit newLevel of Ash")
								emitLocalStreamEvent("ashStorageLevel", "ashStorageLevel($Level,$D)" ) 
								previous_D = D 
								}
								if( System.currentTimeMillis()-timeLastUpdate>=timeout 
								 ){emitLocalStreamEvent("ashStorageLevel", "ashStorageLevel($Level,$D)" ) 
								timeLastUpdate=System.currentTimeMillis() 
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="filter",cond=whenEvent("sonardata"))
				}	 
			}
		}
} 
