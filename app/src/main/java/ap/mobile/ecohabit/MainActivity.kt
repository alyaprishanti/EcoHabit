package ap.mobile.ecohabit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.ui.eco_target.EcoTargetViewModel
import ap.mobile.ecohabit.ui.theme.EcoHabitTheme
import ap.mobile.ecohabit.ui.eco_target.EcoTargetScreen
import ap.mobile.ecohabit.ui.eco_target.HistoryScreen
import ap.mobile.ecohabit.ui.eco_target.WeeklyResultScreen
import ap.mobile.ecohabit.ui.eco_target.WeeklyCarbonChart
import ap.mobile.ecohabit.ui.eco_target.DailyPointChart
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoHabitTheme {
                val navController = rememberNavController()
                val viewModel: EcoTargetViewModel = viewModel()


                NavHost(
                    navController = navController,
                    startDestination = "eco_target"
                ){

                    // Eco Target
                    composable("eco_target") {
                        EcoTargetScreen(
                            onNavigateHistory = {
                                navController.navigate("history")
                            }
                        )
                    }

                    // History
                    composable("history") {
                        val viewModel: EcoTargetViewModel = viewModel()
                        val historyList by viewModel.historyList.collectAsState()
                        HistoryScreen(
                            onBack = { navController.popBackStack() },
                            historyList = historyList,
                            onClickItem = { id ->
                                navController.navigate("weekly_result/$id")
                            }
                        )
                    }

                    // Weekly Result
                    composable("weekly_result/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: "0"

                        // Load data untuk screen ini
                        viewModel.loadHistoryById(id)

                        val summary = viewModel.selectedHistory.collectAsState().value

                        if (summary != null) {
                            WeeklyResultScreen(
                                history = summary,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    composable("weeklyCarbonChart") {
                        WeeklyCarbonChart(data = EcoTargetRepository.carbonDailyData)
                    }
                    composable("dailyPointChart") {
                        DailyPointChart(data = EcoTargetRepository.quizDailyData)
                    }
                }
            }
        }
    }
}

