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

//User imports JAN2024

class Monitoringdevice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				var levelAshStorage = 0;
				var statoIncinerator = 0;
				val DLIMIT = 10;
				val DMIN = 100;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(1000) 
						CommUtils.outblack("$name STARTS")
						observeResource("192.168.1.110","8125","ctx_waste_incinerator_service","incinerator","statoIncinerator")
						subscribeToLocalActor("datacleaner") 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						CommUtils.outblack("Waiting data from sonar or updates from Incinerator...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="handleUpdateStatoIncinerator",cond=whenDispatch("statoIncinerator"))
					transition(edgeName="t03",targetState="handleAshStorageLevel",cond=whenEvent("ashStorageLevel"))
				}	 
				state("handleUpdateStatoIncinerator") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("statoIncinerator(N)"), Term.createTerm("statoIncinerator(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 statoIncinerator = payloadArg(0).toInt()  
								if( statoIncinerator==1 
								 ){forward("led_on", "led_on(1)" ,"led" ) 
								}
								else
								 {if( levelAshStorage < DMIN && levelAshStorage > DLIMIT 
								  ){forward("led_off", "led_off(1)" ,"led" ) 
								 }
								 else
								  {forward("led_blink", "led_blink(1)" ,"led" ) 
								  }
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleAshStorageLevel") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ashStorageLevel(D)"), Term.createTerm("ashStorageLevel(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												levelAshStorage = payloadArg(0).toInt()
								if( statoIncinerator==0 
								 ){if( levelAshStorage < DMIN && levelAshStorage > DLIMIT 
								 ){forward("led_off", "led_off(1)" ,"led" ) 
								}
								else
								 {forward("led_blink", "led_blink(1)" ,"led" ) 
								 }
								}
								if( levelAshStorage <= DLIMIT 
								 ){updateResourceRep( "statoAshStorage(1)"  
								)
								}
								else
								 {updateResourceRep( "statoAshStorage(0)"  
								 )
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
