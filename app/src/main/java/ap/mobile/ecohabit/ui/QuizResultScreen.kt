package ap.mobile.ecohabit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizResultScreen(
    score: Int,
    maxStreak: Int,
    category: String,
    weeklyProgress: Int,
    onHistoryClick: () -> Unit, // Parameter baru untuk navigasi ke History
    weeklyTarget: Int = 40,
    dailyTip: String,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Eco Quiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // TITLE HASIL QUIZ
        Text(
            text = "Hasil Quiz Hari ini!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(20.dp))

        // CARD SCORE 50/50 (warna hijau)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$score/50",
                    color = Color(0xFF43A047),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // STREAK
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Streak Tertinggi: $maxStreak", fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        // KATEGORI EARTH HERO
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Kategori: $category", fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        // ECO TARGET MINGGUAN
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Eco Target Mingguan", fontSize = 16.sp)
                Spacer(Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = weeklyProgress / weeklyTarget.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = Color(0xFF5271FF)
                )

                Spacer(Modifier.height(4.dp))
                Text("$weeklyProgress/$weeklyTarget poin", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // TIPS HARI INI
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Tips Hari Ini:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(dailyTip)
            }
        }

        Spacer(Modifier.weight(1f))

        // BUTTON HISTORY (BARU)
        Button(
            onClick = onHistoryClick,
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)) // Warna berbeda untuk membedakan
        ) {
            Text("History", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))

        // BUTTON KEMBALI KE HOME
        Button(
            onClick = onNext,
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5271FF))
        ) {
            Text("Kembali Ke Home", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(20.dp))
    }
}
