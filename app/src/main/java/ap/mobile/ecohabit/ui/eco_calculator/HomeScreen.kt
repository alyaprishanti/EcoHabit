package ap.mobile.ecohabit.ui.eco_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun FoodCheckItem(
    name: String,
    checked: Boolean,
    value: String,
    onChecked: (Boolean) -> Unit,
    onValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = onChecked
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (!checked) {
            // Mode normal (nama makanan)
            Text(
                text = name,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Mode input gram (sesuai desain)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                UnderlineInput(
                    value = value,
                    onValueChange = onValueChange,
                    width = 70.dp
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text("gr", fontSize = 14.sp)
            }
        }
    }
}


fun calculateEmissions(
    transportType: String,
    distanceKm: Double,
    electricityMap: Map<String, Double>,
    foodMapGrams: Map<String, Double>
): Double {
    val transportFactors = mapOf(
        "Motor" to 0.08,
        "Mobil" to 0.21,
        "Motor Listrik" to 0.02,
        "Transport Publik" to 0.05,
        "Sepeda" to 0.0,
        "Jalan Kaki" to 0.0
    )
    val tFactor = transportFactors[transportType] ?: 0.0
    val transportEm = tFactor * distanceKm

    val gridFactor = 0.9
    val electricityEm = electricityMap.values.sum() * gridFactor

    val foodFactorsPerGram = mapOf(
        "Nasi" to 0.0027,
        "Ayam" to 0.0069,
        "Daging (Sapi)" to 0.027,
        "Sayur" to 0.002
    )
    var foodEm = 0.0
    for ((k, grams) in foodMapGrams) {
        val f = foodFactorsPerGram[k] ?: 0.0
        foodEm += f * grams
    }

    return transportEm + electricityEm + foodEm
}


@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onViewHistory: ()->Unit,
    onNavigateToResult: ()->Unit
) {
    val latest = viewModel.latest.collectAsStateWithLifecycle()

    val scroll = rememberScrollState()

    val transportOptions =
        listOf("Motor", "Mobil", "Motor Listrik", "Transport Publik", "Sepeda", "Jalan Kaki")
    var selectedTransport by remember { mutableStateOf(transportOptions[0]) }
    var distanceStr by remember { mutableStateOf("") }

    var lampStr by remember { mutableStateOf("") }
    var tvStr by remember { mutableStateOf("") }
    var chargerStr by remember { mutableStateOf("") }
    var acStr by remember { mutableStateOf("") }

    val foodItems = listOf(
        "Keju/susu",
        "Nasi",
        "Telur",
        "Daging Ayam",
        "Daging merah",
        "Makanan Nabati"
    )
    val foodChecked = remember { mutableStateListOf(false, false, false, false, false, false) }
    val foodGrams = remember { mutableStateListOf("0", "0", "0", "0", "0", "0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F3F5))
            .verticalScroll(scroll)
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
                .background(Color(0xFFF2F3F5))
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            // TOP card: Ringkasan Hari Ini (kecil)
            Card(
                elevation = 6.dp,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ringkasan Hari Ini",
                            style = MaterialTheme.typography.h6
                        )
                        TextButton(
                            onClick = onViewHistory,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "Lihat Semua",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (latest.value != null) {
                        val l = latest.value!!
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF2F3F5), RoundedCornerShape(50))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Total Emisi: ${String.format("%.2f kg CO₂e", l.totalKgCO2e)}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Bar 2 — Kategori
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF2F3F5), RoundedCornerShape(50))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Kategori: ${l.category}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Bar 3 — Perubahan Emisi
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF2F3F5), RoundedCornerShape(50))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Emisi naik  +${
                                    String.format(
                                        "%.2f",
                                        l.diff
                                    )
                                } kg CO₂e dari hari kemarin"
                            )
                        }

                    } else {
                        // Tidak ada data
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF2F3F5), RoundedCornerShape(50))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Belum ada perhitungan hari ini.")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // FORM input (desain mengikuti urutan gambar)
            Text(
                "Aktivitas Harian",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            DropdownInput(
                placeholder = "Jenis Transportasi",
                selected = selectedTransport,
                options = transportOptions,
                onSelect = { selectedTransport = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            DistanceInput(
                value = distanceStr,
                onValueChange = { distanceStr = it }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Penggunaan Listrik",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            ElectricityInputItem(label = "Lampu", value = lampStr, onValueChange = { lampStr = it })
            ElectricityInputItem(label = "TV", value = tvStr, onValueChange = { tvStr = it })
            ElectricityInputItem(
                label = "Charger",
                value = chargerStr,
                onValueChange = { chargerStr = it })
            ElectricityInputItem(label = "AC", value = acStr, onValueChange = { acStr = it })


            Spacer(modifier = Modifier.height(15.dp))

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Konsumsi Makanan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Dua kolom makanan
                Row(modifier = Modifier.fillMaxWidth()) {

                    // Kolom kiri (index 0–2)
                    Column(modifier = Modifier.weight(1f)) {
                        for (idx in foodItems.indices.filter { it <= 2 }) {
                            FoodCheckItem(
                                name = foodItems[idx],
                                checked = foodChecked[idx],
                                value = foodGrams[idx],
                                onChecked = {
                                    foodChecked[idx] = it
                                    if (!it) foodGrams[idx] = "0"
                                },
                                onValueChange = { foodGrams[idx] = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Kolom kanan (index 3–5)
                    Column(modifier = Modifier.weight(1f)) {
                        for (idx in foodItems.indices.filter { it >= 3 }) {
                            FoodCheckItem(
                                name = foodItems[idx],
                                checked = foodChecked[idx],
                                value = foodGrams[idx],
                                onChecked = {
                                    foodChecked[idx] = it
                                    if (!it) foodGrams[idx] = "0"
                                },
                                onValueChange = { foodGrams[idx] = it }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Hitung
            Button(
                onClick = {
                    val dist = distanceStr.toDoubleOrNull() ?: 0.0
                    val lamp = lampStr.toDoubleOrNull() ?: 0.0
                    val tv = tvStr.toDoubleOrNull() ?: 0.0
                    val charger = chargerStr.toDoubleOrNull() ?: 0.0
                    val ac = acStr.toDoubleOrNull() ?: 0.0
                    val foodMap = mutableMapOf<String, Double>()
                    for (i in foodItems.indices) {
                        if (foodChecked[i]) {
                            val g = foodGrams.getOrNull(i)?.toDoubleOrNull() ?: 0.0
                            foodMap[foodItems[i]] = g
                        }
                    }
                    val electricityMap = mapOf(
                        "Lampu" to lamp,
                        "TV" to tv,
                        "Charger" to charger,
                        "AC" to ac
                    )
                    val total = calculateEmissions(
                        transportType = selectedTransport,
                        distanceKm = dist,
                        electricityMap = electricityMap,
                        foodMapGrams = foodMap
                    )

                    val category = when {
                        total >= 20 -> "BESAR"
                        total >= 10 -> "SEDANG"
                        else -> "KECIL"
                    }


                    val emissionFactor = when (selectedTransport) {
                        "Motor" -> 30.0
                        "Mobil" -> 120.0
                        "Bus" -> 70.0
                        else -> 0.0
                    }

                    // hitung total emisi transport
                    val transportEmigrams = dist * emissionFactor
                    val previousTotal = latest.value?.totalKgCO2e ?: 0.0
                    val diff = total - previousTotal
                    val entry = EmissionResult(
                        date = viewModel.createDateNow(),
                        transportType = selectedTransport,
                        transportDistanceKm = dist,
                        electricityMap = electricityMap,
                        foodMapGrams = foodMap,
                        totalKgCO2e = total,
                        category = category,
                        diff = diff,
                        transportEmiGrams = transportEmigrams
                    )
                    viewModel.saveResult(entry)
                    onNavigateToResult()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 52.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF667ADD),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Hitung",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}