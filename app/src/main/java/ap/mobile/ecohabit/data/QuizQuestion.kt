package ap.mobile.ecohabit.data

data class QuizQuestion(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)