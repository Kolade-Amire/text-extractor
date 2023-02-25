import com.google.gson.Gson
import java.io.FileWriter

fun main(){


        val filepath = "C:\\Users\\DELL\\Desktop\\use_of_english.pdf"
        val extractor = Extractor(filepath)
        val text = extractor.extractTextFromPDF()

        //remove unwanted texts from the pdf and create a list of individual questions
        val cleanedText = extractor.cleanText(text)
        val strippedText = extractor.stripText(cleanedText)

        val questionProperties = mutableListOf("Grammar", "Intermediate")

        val questions = extractor.convertQuestionsToDataClass(strippedText, questionProperties[0], questionProperties[1])
        val answers = extractor.getAnswers(text)
        val finalData = extractor.mergeAnswersWithQuestions(questions,answers)


        //convert Question to Json
        val gson = Gson()
        val json = gson.toJson(finalData)
        val writeName = filepath.subSequence(0, filepath.length-4)
        val writer = FileWriter("$writeName.json")
        writer.write(json)
        writer.close()
}
