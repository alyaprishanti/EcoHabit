package ap.mobile.ecohabit.ui.eco_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.ui.theme.EcoHabitTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            EcoHabitTheme {
                val vm: MainViewModel = viewModel()
                NavGraph(viewModel = vm)
            }
        }
    }
}
