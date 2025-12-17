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
fun QuizFeedbackWrongScreen(
    correctAnswer: String,
    explanation: String,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Text(
            text = "Eco Quiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Text "Jawaban Kamu... SALAH"
        Text(
            text = "Jawaban Kamu...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "SALAH",
            fontSize = 28.sp,
            color = Color(0xFFE53935),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Card jawaban benar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Jawaban Benar : $correctAnswer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text(explanation, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(30.dp))

        // Tombol next
        Button(
            onClick = onNext,
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5271FF))
        ) {
            Text("Soal Selanjutnya", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.weight(1f))

        // Bottom nav
        BottomNavBar()
    }
}

@Composable
fun QuizFeedbackCorrectScreen(
    explanation: String,
    onSeeResult: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Text(
            text = "Eco Quiz",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Text "Jawaban Kamu... Benar"
        Text(
            text = "Jawaban Kamu...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Benar",
            fontSize = 28.sp,
            color = Color(0xFF43A047),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Explainer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    explanation,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        // Tombol lihat hasil
        Button(
            onClick = onSeeResult,
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5271FF))
        ) {
            Text("Soal Selanjutnya", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.weight(1f))

        BottomNavBar()
    }
}

@Composable
fun BottomNavBar() {
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


