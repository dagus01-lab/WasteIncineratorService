/* Generated by AN DISI Unibo */ 
package it.unibo.ctxraspberrymock
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
	QakContext.createContexts(
	        "localhost", this, "raspberrymock.pl", "sysRules.pl", "ctxraspberrymock"
	)
	//JAN Facade
	unibo.raspberrymock.RaspberrymockApplication.main( arrayOf<String>() );
	//JAN24 Display
}

