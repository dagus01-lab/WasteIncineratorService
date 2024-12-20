/* Generated by AN DISI Unibo */ 
package it.unibo.led

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

class Led ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
				lateinit var writer : java.io.BufferedWriter
		    	lateinit var p : Process
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name started")
						
									p       = Runtime.getRuntime().exec("python ledDevice.py")
									writer = java.io.BufferedWriter( java.io.OutputStreamWriter(p.getOutputStream()));
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						CommUtils.outblack("Waiting info from monitoringDevice...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="handle_led_on",cond=whenDispatch("led_on"))
					transition(edgeName="t07",targetState="handle_led_off",cond=whenDispatch("led_off"))
					transition(edgeName="t08",targetState="handle_led_blink",cond=whenDispatch("led_blink"))
				}	 
				state("handle_led_on") { //this:State
					action { //it:State
						CommUtils.outmagenta("LED on")
						 
									writer.write("on")
						        	writer.newLine()
						        	writer.flush()
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handle_led_off") { //this:State
					action { //it:State
						CommUtils.outmagenta("LED off")
						 
									writer.write("off")
							        writer.newLine()
							        writer.flush()
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handle_led_blink") { //this:State
					action { //it:State
						CommUtils.outmagenta("LED blinks")
						
									writer.write("blink")
							        writer.newLine()
							        writer.flush()
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
