package ap.mobile.ecohabit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun QuizQuestionScreen(
    question: String,
    options: List<String>,
    currentIndex: Int,
    timer: Int,
    selectedOption: Int,
    onSelect: (Int) -> Unit,
    onSubmit: () -> Unit
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

        // ROW: Soal + Timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "Soal: ${currentIndex + 1}/5",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 14.sp
                )
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Text(
                    text = "Timer: ${timer}s",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        // CARD PERTANYAAN
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = question,
                modifier = Modifier.padding(20.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(20.dp))

        // OPSI JAWABAN
        options.forEachIndexed { index, text ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onSelect(index) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    if (selectedOption == index) Color(0xFFDDE7FF)
                    else Color.White
                )
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        // TOMBOL SUBMIT
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5271FF))
        ) {
            Text(text = "SUBMIT", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.weight(1f))

        // BOTTOM NAV
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
