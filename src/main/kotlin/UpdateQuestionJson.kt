
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

class UpdateQuestionJson (private val filePath: String, private val questionTopic: String) {

    // Define data class for JSON objects
    data class Question(
        var questionNumber: Int? = null,
        val question:String,
        val options:Map<String, String>,
        val questionClass: String,
        val questionLevel: String,
        val questionTopic: String?,
        var answer:String? = null
    )

    fun updateFile() {
        // Load JSON data from file
         val objectMapper = ObjectMapper().registerKotlinModule()
         val jsonArray = objectMapper.readValue<Array<Question>>(File(filePath))

        // Add new property to each JSON object and update it
        val updatedJsonArray = jsonArray.map { it.copy(questionTopic = questionTopic) }

        // Write updated JSON data back to file
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        val writeName = filePath.subSequence(0, filePath.length-5)
        val file = File("${writeName}_new.json")
        file.writeText(objectMapper.writeValueAsString(updatedJsonArray))
    }

}


fun main (){
    val updateJson = UpdateQuestionJson("C:\\Users\\DELL\\Desktop\\QEnglish_Project\\questions.json\\questions 2.0\\grammar\\intermediate\\grammar.intermediate.tenses.json", "tenses")
    updateJson.updateFile()
}