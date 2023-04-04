data class Question(
    var questionNumber: Int? = null,
    val question:String,
    val options:Map<String, String>,
    val questionClass: String,
    val questionLevel: String,
    val questionTopic: String,
    var answer:String? = null
)