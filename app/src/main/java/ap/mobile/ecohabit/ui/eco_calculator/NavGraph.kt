package ap.mobile.ecohabit.ui.eco_calculator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onViewHistory = { navController.navigate("history") },
                onNavigateToResult = { navController.navigate("result") }
            )
        }
        composable("history") {
            HistoryScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable("result") {
            ResultScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}