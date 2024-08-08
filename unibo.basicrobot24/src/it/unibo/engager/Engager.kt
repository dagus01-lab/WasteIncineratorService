/* Generated by AN DISI Unibo */ 
package it.unibo.engager

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

class Engager ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		     
		  //var Owner           = "unkknown"
		  var EngageCaller    = ""
		  var EngageDone      = true //false  MAY24
		  var curConn : Interaction? = null 
		  var OwnerMngr       = supports.OwnerManager //Kotlin object
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						if(  ! currentMsg.isEvent()  
						 ){CommUtils.outblack("$name waiting ..")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleEngage",cond=whenRequest("engage"))
					transition(edgeName="t01",targetState="disengageRobot",cond=whenDispatch("disengage"))
					transition(edgeName="t02",targetState="checkTheOwner",cond=whenRequest("checkowner"))
				}	 
				state("handleEngage") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("engage(OWNER,STEPTIME)"), Term.createTerm("engage(OWNER,STEPTIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 if( currentMsg.conn != null ) curConn = currentMsg.conn					
												   EngageCaller  = currentMsg.msgSender() //payloadArg(0)
												   if( curConn != null )
												     CommUtils.outmagenta("engager | engaged by remote $EngageCaller on $curConn" )		
												   else CommUtils.outmagenta("engager | engaged by local $EngageCaller " )	
												   //MAY24
												   EngageDone = OwnerMngr.engage(EngageCaller)
												   if( EngageDone ) OwnerMngr.setStepTime(payloadArg(1))
												   //MAY24
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="engageAccept", cond=doswitchGuarded({ EngageDone  
					}) )
					transition( edgeName="goto",targetState="engageRefuse", cond=doswitchGuarded({! ( EngageDone  
					) }) )
				}	 
				state("disengageRobot") { //this:State
					action { //it:State
						 OwnerMngr.disengage()   
						CommUtils.outblack("$name has disengaged")
						emitLocalStreamEvent("alarm", "alarm(disengaged)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("engageRefuse") { //this:State
					action { //it:State
						 val Owner = OwnerMngr.owner   
						CommUtils.outblack("$name engage refused since already working for $Owner")
						answer("engage", "engagerefused", "engagerefused($Owner)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("engageAccept") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						 val Owner    = OwnerMngr.owner  
						updateResourceRep( "workingfor($Owner)"  
						)
						answer("engage", "engagedone", "engagedone($Owner)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("checkTheOwner") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
} 
