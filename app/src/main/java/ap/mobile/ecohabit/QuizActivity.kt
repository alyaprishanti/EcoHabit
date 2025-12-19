package ap.mobile.ecohabit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.ui.eco_calculator.CalculatorActivity
import ap.mobile.ecohabit.ui.nav.QuizNavGraph
import ap.mobile.ecohabit.ui.theme.EcoHabitTheme
import ap.mobile.ecohabit.viewmodel.QuizViewModel

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EcoHabitTheme {
                Scaffold(
                    bottomBar = {
                        MainBottomBar(
                            current = BottomTab.QUIZ,
                            onSelect = { tab ->
                                when (tab) {
                                    BottomTab.CALCULATOR ->
                                        startActivity(Intent(this, CalculatorActivity::class.java))
                                    BottomTab.ECOTARGET ->
                                        startActivity(Intent(this, EcoTargetActivity::class.java))
                                    else -> {}
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    QuizNavGraph()
                }
            }
        }
    }
}
