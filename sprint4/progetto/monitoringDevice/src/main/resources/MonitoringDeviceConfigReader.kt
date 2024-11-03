package main.resources
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
object MonitoringDeviceConfigReader {

    private val mapper = ObjectMapper().registerKotlinModule()
    private lateinit var config : MonitoringDeviceConfig

    fun loadMDConfig(filePath: String): MonitoringDeviceConfig {
        if(!::config.isInitialized)
            config = mapper.readValue(File(filePath))
        return config
    }
}