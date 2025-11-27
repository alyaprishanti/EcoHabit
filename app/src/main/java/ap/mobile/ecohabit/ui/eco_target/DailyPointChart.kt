package ap.mobile.ecohabit.ui.eco_target

import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry

@Composable
fun DailyPointChart(
    data: List<Float> = EcoTargetRepository.quizDailyData,
    labels: List<String> = EcoTargetRepository.weekDayLabels
) {

    val chartModelProducer = ChartEntryModelProducer(
        data.mapIndexed { index, value ->
            FloatEntry(index.toFloat(), value)
        }
    )

    Chart(
        chart = columnChart(),
        chartModelProducer = chartModelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                labels.getOrNull(value.toInt()) ?: ""
            }
        )
    )
}
