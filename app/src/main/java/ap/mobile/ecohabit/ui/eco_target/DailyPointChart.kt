package ap.mobile.ecohabit.ui.eco_target

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry

@Composable
fun DailyPointChart(data: List<Pair<String, Float>>) {

    val producer = remember { ChartEntryModelProducer() }

    LaunchedEffect(data) {
        producer.setEntries(
            data.mapIndexed { index, pair ->
                FloatEntry(index.toFloat(), pair.second)
            }
        )
    }

    Chart(
        chart = columnChart(),
        chartModelProducer = producer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                data.getOrNull(value.toInt())
                    ?.first
                    ?.let { DateUtils.shortDateLabel(it) }
                    ?: ""
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}



