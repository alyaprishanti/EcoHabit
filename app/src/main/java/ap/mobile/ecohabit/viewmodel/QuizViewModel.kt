package ap.mobile.ecohabit.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ap.mobile.ecohabit.data.QuizHistoryEntry
import ap.mobile.ecohabit.data.QuizQuestion
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private val db = FirebaseFirestore.getInstance()


class QuizViewModel : ViewModel() {

    // Daftar soal
    val questions = listOf(
        QuizQuestion(
            text = "Mana yang lebih ramah lingkungan digunakan setiap hari?",
            options = listOf("Kantong Plastik", "Kantong Kertas", "Kantong Kain", "Styrofoam"),
            correctIndex = 2,
            explanation = "Material kain bisa digunakan berkali-kali."
        ),
        QuizQuestion(
            text = "Transportasi paling rendah emisi?",
            options = listOf("Motor", "Mobil", "Transport Publik", "Sepeda"),
            correctIndex = 3,
            explanation = "Sepeda menghasilkan 0 emisi."
        ),
        QuizQuestion(
            text = "Sumber energi paling ramah lingkungan?",
            options = listOf("Batu bara", "Minyak bumi", "Surya", "Gas alam"),
            correctIndex = 2,
            explanation = "Energi surya adalah energi terbarukan."
        )
    )

    var currentIndex = mutableStateOf(0)
    var selectedAnswer = mutableStateOf(-1)
    var score = mutableStateOf(0)
    var streak = mutableStateOf(0)
    var bestScore = mutableStateOf(0)
    var maxStreak = mutableStateOf(0)
    var timer = mutableStateOf(12)
    var weeklyProgress = mutableStateOf(0)
    val quizHistory = mutableStateOf<List<QuizHistoryEntry>>(emptyList())



    fun startQuiz() {
        currentIndex.value = 0
        selectedAnswer.value = -1
        score.value = 0
        streak.value = 0
    }

    private fun recordQuizResult() {
        val currentDate = SimpleDateFormat(
            "dd MMMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date())

        val newEntry = QuizHistoryEntry(
            score = score.value,
            category = getCategory(),
            date = currentDate
        )

        // SIMPAN KE FIREBASE
        val data = hashMapOf(
            "score" to newEntry.score,
            "category" to newEntry.category,
            "date" to newEntry.date
        )

        db.collection("quiz_results")
            .add(data)
            .addOnSuccessListener {
                println("Berhasil simpan ke Firebase")
            }
            .addOnFailureListener {
                println("Gagal simpan: ${it.message}")
            }

        // SIMPAN KE UI (LOCAL)
        quizHistory.value = listOf(newEntry) + quizHistory.value
    }


    fun submitAnswer(): Boolean {
        val question = questions[currentIndex.value]
        val isCorrect = selectedAnswer.value == question.correctIndex

        if (isCorrect) {
            score.value += 10
            streak.value += 1
            if (streak.value > maxStreak.value) maxStreak.value = streak.value
        } else {
            score.value -= 3
            streak.value = 0
        }

        return isCorrect
    }

    fun getTodayTip(): String {
        return "Cobalah bawa botol minum sendiri untuk mengurangi penggunaan plastik."
    }

    fun getCategory(): String {
        return when {
            score.value >= 45 -> "EARTH HERO"
            score.value >= 30 -> "ECO FRIENDLY"
            score.value >= 15 -> "KEEP TRYING"
            else -> "LETS IMPROVE!"
        }
    }

    fun nextQuestion(): Boolean {
        return if (currentIndex.value < questions.size - 1) {
            currentIndex.value++
            selectedAnswer.value = -1
            true
        } else {
            recordQuizResult()
            false
        }
    }
}
