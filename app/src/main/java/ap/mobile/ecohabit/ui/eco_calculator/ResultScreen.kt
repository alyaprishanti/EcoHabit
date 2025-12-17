package ap.mobile.ecohabit.ui.eco_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color


@Composable
fun ResultScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val latest = viewModel.latest.collectAsStateWithLifecycle()
    val r = latest.value

    if (r == null) {
        Text("Tidak ada hasil.")
        return
    }

    val total = r.totalKgCO2e
    // Ubah warna teks dari total
    val colorEmisi = when {
        total > 10 -> Color(0xFFFF6B6B)
        total >= 5 -> Color(0xFFFFA500)
        else -> Color(0xFF4CAF50)
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Eco Carbon Footprint Estimator",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F3F5))
    ) {

        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Eco Carbon Footprint Estimator",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8ECEF))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            if (r == null) {
                Text("Tidak ada hasil.")
                return
            }

            // --- Total Emisi (tengah, besar, merah) ---
            Text(
                text = "Total Emisi",
                style = MaterialTheme.typography.body1
            )

            Text(
                text = String.format("%.2f kg CO₂e", r.totalKgCO2e),
                color = colorEmisi,
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
            )

            // Riwayat
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Riwayat",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Electricity Items
                    r.electricityMap.forEach { (name, grams) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name)
                            Text(
                                if (grams >= 1000)
                                    String.format("%.3f kg CO₂e", grams / 1000)
                                else
                                    String.format("%.1f g CO₂e", grams)
                            )
                        }
                    }

                    // Food Items
                    r.foodMapGrams.forEach { (name, grams) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name)
                            Text(
                                if (grams >= 1000)
                                    String.format("%.3f kg CO₂e", grams / 1000)
                                else
                                    String.format("%.1f g CO₂e", grams)
                            )
                        }
                    }

                    // --- Transport Item ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(r.transportType)
                        Text(
                            if (r.transportEmiGrams >= 1000)
                                String.format("%.3f kg CO₂e", r.transportEmiGrams / 1000)
                            else
                                String.format("%.1f g CO₂e", r.transportEmiGrams)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onBack, modifier = Modifier.width(200.dp)) {
                Text("Kembali")
            }
        }
    }
}