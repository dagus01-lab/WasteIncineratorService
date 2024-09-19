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
//Sept2024
import org.slf4j.Logger
import org.slf4j.LoggerFactory 
import org.json.simple.parser.JSONParser
import org.json.simple.JSONObject


//User imports JAN2024

class Sonardevice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		 
			lateinit var reader : java.io.BufferedReader
		    lateinit var p : Process	
		    var Distance = 0;
		    var Data = "";
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name | sonarstart")
						
									p       = Runtime.getRuntime().exec("python sonar.py")
									reader  = java.io.BufferedReader(  java.io.InputStreamReader(p.getInputStream() ))	
						delay(100) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="readSonarData", cond=doswitch() )
				}	 
				state("readSonarData") { //this:State
					action { //it:State
						 
								Data = reader.readLine()
								//CommUtils.outyellow("$name with python: data = $Data"   )
								if( Data != null ){
								try{ 
									val vd = Data.toFloat()
									val v  = vd.toInt()
									if( v <= 100 ){	//A first filter ...
										Distance = v				
									}else Distance = 0
								}catch(e: Exception){
										CommUtils.outred("$name readSonarDataERROR: $e "   )
								}
								}
								
						if(  Distance > 0  
						 ){emitLocalStreamEvent("sonardata", "distance($Distance)" ) 
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
