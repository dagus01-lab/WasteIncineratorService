/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_waste_incinerator_service
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
	QakContext.createContexts(
	        "localhost", this, "waste_incinerator_service.pl", "sysRules.pl", "ctx_waste_incinerator_service"
	)
	//JAN Facade
	//JAN24 Display
}

