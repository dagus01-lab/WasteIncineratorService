/* Generated by AN DISI Unibo */ 
package it.unibo.sonardevice

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

class Sonardevice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
			lateinit var reader : java.io.BufferedReader
		    lateinit var p : Process	
		    var Distance = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name | sonarstart")
						
									p       = Runtime.getRuntime().exec("python sonar.py")
									reader  = java.io.BufferedReader(  java.io.InputStreamReader(p.getInputStream() ))	
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="readSonarData", cond=doswitch() )
				}	 
				state("readSonarData") { //this:State
					action { //it:State
						 
								var data = reader.readLine()
								CommUtils.outyellow("$name with python: data = $data"   ) 
								if( data != null ){
								try{ 
									val vd = data.toFloat()
									val v  = vd.toInt()
									if( v <= 100 ){	//A first filter ...
										Distance = v				
									}else Distance = 0
								}catch(e: Exception){
										CommUtils.outred("$name readSonarDataERROR: $e "   )
								}
								}//if
								
						if(  Distance > 0  
						 ){CommUtils.outyellow("$name with python: data = $data")
						emitLocalStreamEvent("sonardata", "distance($Distance)" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="readSonarData", cond=doswitch() )
				}	 
			}
		}
} 
