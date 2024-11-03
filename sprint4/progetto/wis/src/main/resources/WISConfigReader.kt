package main.resources
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
object WISConfigReader {

    private val mapper = ObjectMapper().registerKotlinModule()
    private lateinit var config : WISConfig

    fun loadWISConfig(filePath: String): WISConfig {
        if(!::config.isInitialized)
            config = mapper.readValue(File(filePath))
        return config
    }
}