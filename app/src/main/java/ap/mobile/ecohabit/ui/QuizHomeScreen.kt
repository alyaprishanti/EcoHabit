package ap.mobile.ecohabit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizHomeScreen(
    bestScore: Int,
    streak: Int,
    onStartQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Eco Quiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        // --- Card Info Quiz ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Kuis Harian tersedia : 5 Soal", fontSize = 16.sp)

                Spacer(Modifier.height(8.dp))
                Text("Best Score Hari Ini: $bestScore", fontSize = 14.sp)

                Spacer(Modifier.height(8.dp))
                Text("Streak: $streak", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mari menambah wawasan kamu agar dunia menjadi\nlebih bersih bebas carbon",
            fontSize = 14.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Mulai Quiz", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM NAV (dummy UI)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kalkulator")
            Text("Quiz", fontWeight = FontWeight.Bold)
            Text("Target")
        }
    }
}
