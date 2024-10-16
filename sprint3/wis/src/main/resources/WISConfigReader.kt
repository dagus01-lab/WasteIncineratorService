package main.resources
import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
class WISConfigReader(private val filePath: String) {

    private val mapper = ObjectMapper().registerKotlinModule()

    // Method to load the person from the JSON file
    fun loadConfig(): WISConfig {
        return mapper.readValue(File(filePath), WISConfig::class.java)
    }
}