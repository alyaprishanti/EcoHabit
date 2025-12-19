package ap.mobile.ecohabit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import ap.mobile.ecohabit.ui.eco_calculator.CalculatorActivity
import ap.mobile.ecohabit.ui.eco_target.EcoTargetViewModel
import ap.mobile.ecohabit.ui.theme.EcoHabitTheme
import ap.mobile.ecohabit.ui.eco_target.EcoTargetScreen
import ap.mobile.ecohabit.ui.eco_target.HistoryScreen
import ap.mobile.ecohabit.ui.eco_target.WeeklyResultScreen
import ap.mobile.ecohabit.ui.eco_target.WeeklyCarbonChart
import ap.mobile.ecohabit.ui.eco_target.DailyPointChart
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository

class EcoTargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoHabitTheme {
                val navController = rememberNavController()
                val viewModel: EcoTargetViewModel = viewModel()
                LaunchedEffect(Unit) { viewModel.loadWeeklyHistory() }

                Scaffold(
                    bottomBar = {
                        MainBottomBar(
                            current = BottomTab.ECOTARGET,
                            onSelect = { tab ->
                                when (tab) {
                                    BottomTab.CALCULATOR ->
                                        startActivity(Intent(this, CalculatorActivity::class.java))
                                    BottomTab.QUIZ ->
                                        startActivity(Intent(this, QuizActivity::class.java))
                                    else -> {}
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "eco_target"
                    ) {

                        // Eco Target
                        composable("eco_target") {
                            EcoTargetScreen(
                                onNavigateHistory = { navController.navigate("history") },
                                viewModel = viewModel
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                onBack = { navController.popBackStack() },
                                onClickItem = { id -> navController.navigate("weekly_result/$id") },
                                viewModel = viewModel
                            )
                        }

                        composable("weekly_result/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: return@composable
                            LaunchedEffect(id) { viewModel.loadHistoryById(id) }
                            val summary by viewModel.selectedHistory.collectAsState()
                            if (summary != null) {
                                WeeklyResultScreen(
                                    history = summary!!,
                                    onBack = { navController.popBackStack() },
                                    viewModel = viewModel
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

