import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object DateUtils {

    fun getCurrentWeekRange(): String {
        val now = LocalDate.now()

        val startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        return "${startOfWeek.format(formatter)} – ${endOfWeek.format(formatter)}"
    }
}