package main.resources
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
object ScaleConfigReader {

    private val mapper = ObjectMapper().registerKotlinModule()
    private lateinit var config : ScaleConfig

    fun loadScaleConfig(filePath: String): ScaleConfig {
        if(!::config.isInitialized)
            config = mapper.readValue(File(filePath))
        return config
    }
}