package ap.mobile.ecohabit.ui.eco_calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton

fun formatTanggal(dateTime: String): String {
    val datePart = dateTime.split(" ")[0]
    val (year, month, day) = datePart.split("-").map { it.toInt() }

    // Nama bulan manual
    val namaBulan = listOf(
        "", "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )[month]

    var m = month
    var y = year
    if (m < 3) {
        m += 12
        y -= 1
    }

    val K = y % 100
    val J = y / 100

    val h = (day + (13 * (m + 1)) / 5 + K + (K / 4) + (J / 4) + (5 * J)) % 7

    val namaHari = listOf(
        "Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    )[h]

    return "$namaHari $day $namaBulan $year"
}

@Composable
fun HistoryScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val history = viewModel.history.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8ECEF))
            .padding(16.dp)
    ) {

        // HEADER
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Riwayat Eco Carbon",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (history.value.isEmpty()) {
            Text("Belum ada riwayat.")
        } else {

            LazyColumn {

                items(history.value) { item ->

                    // === Tanggal dalam box putih rounded ===
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = formatTanggal(item.date),
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // === Card Riwayat (persis seperti desain) ===
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = "Riwayat",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            // ELECTRICITY ITEMS
                            item.electricityMap.forEach { (name, grams) ->
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

                            // FOOD ITEMS
                            item.foodMapGrams.forEach { (name, grams) ->
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

                            // TRANSPORT - Motor/Mobil (dari transportEmiGrams)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.transportType)
                                Text(
                                    if (item.transportEmiGrams >= 1000)
                                        String.format("%.3f kg CO₂e", item.transportEmiGrams / 1000)
                                    else
                                        String.format("%.1f g CO₂e", item.transportEmiGrams)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}