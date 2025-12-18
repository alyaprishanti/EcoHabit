package ap.mobile.ecohabit.ui.eco_target

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EcoTargetScreen(
    onNavigateHistory: () -> Unit,
    viewModel: EcoTargetViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
// pertama kali aplikasi dibuka → load weekly dulu
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyHistory()
    }
// setelah weekly berhasil ter-load → load daily-nya
    val currentWeek = viewModel.currentWeekHistory.collectAsState().value
    LaunchedEffect(currentWeek?.id) {
        if (currentWeek != null) {
            viewModel.loadDailyData()
        }
    }

    val history = viewModel.currentWeekHistory.collectAsState().value
    val carbonTarget = viewModel.carbonTarget
    val quizTarget = viewModel.quizTarget
    val remainingDays = viewModel.remainingDays
    val carbonTotal = history?.carbonTotal ?: 0f
    val quizTotal = history?.quizTotal ?: 0f
    val carbonProgress = (carbonTotal / carbonTarget).coerceIn(0f, 1f)
    val quizProgress = (quizTotal / quizTarget).coerceIn(0f, 1f)
    val dailyCarbon by viewModel.dailyCarbon.collectAsState()
    val dailyQuiz by viewModel.dailyQuiz.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eco Target") },
                actions = {
                    TextButton(onClick = onNavigateHistory) {
                        Text("Riwayat")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO: Navigate calculator */ },
                    label = { Text("Calc") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Calc") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO: Navigate quiz */ },
                    label = { Text("Quiz") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Quiz") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    label = { Text("Target") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Target") }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ===============================
            // SECTION: TARGET MINGGUAN
            // ===============================
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val currentWeekRange = DateUtils.getCurrentWeekRange()
                Text(
                    text = "Target Mingguan: $currentWeekRange",
                    style = MaterialTheme.typography.titleMedium
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    val remainingText = when (remainingDays) {
                        0L -> "Hari terakhir!"
                        1L -> "Sisa 1 hari!"
                        else -> "Sisa $remainingDays hari"
                    }

                    Text(text = remainingText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        textAlign = TextAlign.Center)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)   // jarak antar card
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                            .height(90.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Target Karbon:")
                            Text("${carbonTarget} kg CO₂e")

                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                            .height(90.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Target Quiz:")
                            Text("${quizTarget} poin")
                        }
                    }
                }
            }

            // ===============================
            // SECTION: PROGRESS
            // ===============================
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Progress",
                    style = MaterialTheme.typography.titleMedium
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // ---- PROGRESS KARBON ----
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Karbon Mingguan")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = carbonProgress,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${(carbonProgress * 100).toInt()}%",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Text("Sisa kuota karbonmu ${(carbonTarget-carbonTotal)} kg CO₂e")
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Quiz Mingguan")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = quizProgress,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${(quizProgress * 100).toInt()}%",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Text("${quizTotal.toInt()} poin terkumpul dari target ${quizTarget.toInt()} poin")
                        }
                    }
                }
            }
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Detail Karbon Harian",
                    style = MaterialTheme.typography.titleMedium,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        WeeklyCarbonChart (data = dailyCarbon)
                    }
                }
            }
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Detail Poin Harian",
                    style = MaterialTheme.typography.titleMedium,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        DailyPointChart(data = dailyQuiz)
                    }
                }
            }
            Button(onClick = {
                viewModel.syncTotals(history!!.id)
            }) {
                Text("Sync Total")
            }
        }

    }
}