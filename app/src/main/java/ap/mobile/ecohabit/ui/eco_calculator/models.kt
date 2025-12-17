package ap.mobile.ecohabit.ui.eco_calculator

data class EmissionResult(
    val date: String = "",
    val transportType: String = "",
    val transportEmiGrams: Double = 0.0,
    val transportDistanceKm: Double = 0.0,
    val electricityMap: Map<String, Double> = emptyMap(),
    val foodMapGrams: Map<String, Double> = emptyMap(),
    val totalKgCO2e: Double = 0.0,
    val category: String = "",
    val diff: Double = 0.0
)
