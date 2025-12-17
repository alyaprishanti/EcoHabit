package ap.mobile.ecohabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.ui.theme.MyApplicationTheme
import ap.mobile.ecohabit.ui.nav.QuizNavGraph
import ap.mobile.ecohabit.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val quizViewModel: QuizViewModel = viewModel()
                QuizNavGraph()
            }
        }
    }
}
