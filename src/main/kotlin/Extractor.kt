import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File


class Extractor (private val filePath: String) {

    fun extractTextFromPDF(): String {
        val document = PDDocument.load(File(filePath))
        val stripper = PDFTextStripper()
        val text = stripper.getText(document)
        document.close()
        return text

    }

    fun stripText (pdfText: String): List<String> {
        val pattern = "(\\r\\n\\d+\\.\\s)".toRegex()
        val text = pdfText.split(pattern)
        val mutableText = text.toMutableList()
        mutableText.removeAt(0)
        return mutableText
    }

    fun getAnswers (pdfText: String): List<String> {
        val finalList: MutableList<String> = mutableListOf()
        val arr = pdfText.split("ANSWERS")
        val text = arr[1].replace("Test - \\d+".toRegex(), "")
        val clean1 = text.split("\\s+".toRegex(), 0)
        val mutableText = clean1.toMutableList()
        mutableText.removeAt(0)


        for (answers in mutableText){
            val temp = answers.replace("\\d+-".toRegex(), "")
            finalList.add(temp)
        }
        return finalList

    }

    fun mergeAnswersWithQuestions(questions: List<Question>, answers: List<String>) : List<Question>{
        for ((i, _) in questions.withIndex()){
            questions[i].questionNumber = i+1
            questions[i].answer = answers[i]
        }
        return questions

    }

    fun cleanText(pdfText: String): String {
        val workableText = pdfText.replace("\\d+-\\r\\n\\d+\\.".toRegex(), "1989-95")
        return workableText.replace("Test - \\d+ |Test-\\d+".toRegex(), "")
            .replaceAfterLast("ANSWERS", "")
            .replace("ANSWERS", "")
    }

    fun convertQuestionsToDataClass(text: List<String>, questionClass:String, questionLevel:String) : List<Question> {
        val pattern = "[ABCDE]\\)".toRegex()
        val questions: MutableList<Question> = mutableListOf()
        var tempQuestion:Question
        for(t in text){
            val temp = t.split(pattern)
            if (temp.size == 6) {
                val (question, optionA, optionB, optionC, optionD) = listOf(temp[0], temp[1], temp[2], temp[3], temp[4])
                val optionE = temp[5]

                tempQuestion = Question(
                    question = question,
                    options = mapOf("A" to optionA, "B" to optionB, "C" to optionC, "D" to optionD, "E" to optionE),
                    questionClass = questionClass,
                    questionLevel =
                    questionLevel
                )
            }else{
                val (question, optionA, optionB, optionC, optionD) = listOf(temp[0], temp[1], temp[2], temp[3], temp[4])
                tempQuestion = Question(
                    question = question,
                    options = mapOf("A" to optionA, "B" to optionB, "C" to optionC, "D" to optionD),
                    questionClass = questionClass,
                    questionLevel =
                    questionLevel
                )
            }

            questions.add(tempQuestion)
        }

        return questions
    }


}