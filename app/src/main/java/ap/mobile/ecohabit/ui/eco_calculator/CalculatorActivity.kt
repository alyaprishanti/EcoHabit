package ap.mobile.ecohabit.ui.eco_calculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.BottomTab
import ap.mobile.ecohabit.EcoTargetActivity
import ap.mobile.ecohabit.MainBottomBar
import ap.mobile.ecohabit.QuizActivity
import ap.mobile.ecohabit.ui.theme.EcoHabitTheme
import com.google.firebase.FirebaseApp

class CalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            EcoHabitTheme {
                val vm: MainViewModel = viewModel()
                Scaffold(
                    bottomBar = {
                        MainBottomBar(
                            current = BottomTab.CALCULATOR,
                            onSelect = { tab ->
                                when (tab) {
                                    BottomTab.QUIZ -> startActivity(
                                        Intent(this, QuizActivity::class.java)
                                    )

                                    BottomTab.ECOTARGET -> startActivity(
                                        Intent(this, EcoTargetActivity::class.java)
                                    )

                                    else -> {}
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavGraph(viewModel = vm)
                }
            }
        }
    }
}

