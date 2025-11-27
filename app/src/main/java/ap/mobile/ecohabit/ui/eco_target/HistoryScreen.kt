package ap.mobile.ecohabit.ui.eco_target

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import ap.mobile.ecohabit.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    historyList: List<EcoTargetRepository.WeeklyHistory>,
    onClickItem: (String) -> Unit
) {
    val listState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Eco Target") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(historyList) { history ->
                val context = LocalContext.current
                WeeklyHistoryCard(
                    data = history,
                    onClick = {
                        Toast.makeText(context, "Minggu: ${history.weekLabel}", Toast.LENGTH_SHORT).show()
                        onClickItem(history.id) })
            }
        }
    }
}

@Composable
fun WeeklyHistoryCard(
    data: EcoTargetRepository.WeeklyHistory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),

        ) {
            Image(
                painter = painterResource(id = R.drawable.headerhistory),
                contentDescription = "Ilustrasi Minggu",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = data.weekLabel,
                style = MaterialTheme.typography.titleMedium
            )

            // ============================ CARBON SECTION ============================
            Text("Jejak karbon", style = MaterialTheme.typography.bodyLarge)
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Target maks: ${data.carbonTarget} kgCO₂e",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Dipakai: ${data.carbonTotal} kgCO₂e",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val isCarbonAchieved = data.carbonAchieved
            val carbonStatusText = if (isCarbonAchieved) "Tercapai" else "Tidak tercapai"

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isCarbonAchieved) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCarbonAchieved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(carbonStatusText)
                }
            }

            Divider()

            // ============================ QUIZ SECTION ============================
            Text("Poin Quiz", style = MaterialTheme.typography.bodyLarge)
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Target: ${data.quizTarget} poin", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Terkumpul: ${data.quizTotal} poin",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val isQuizAchieved = data.quizAchieved
            val quizStatusText = if (isQuizAchieved) "Tercapai" else "Tidak tercapai"

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isQuizAchieved) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isQuizAchieved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(quizStatusText)
                }
            }
            if (data.motivation.isNotBlank()) {
                Divider(Modifier.padding(vertical = 8.dp))
                Text("Catatan:", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = data.motivation,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
