import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object DateUtils {

    // yyyy-MM-dd → untuk documentId dailyRecords
    fun todayDateId(): String =
        LocalDate.now().toString()

    // yyyy-MM-dd (Monday) → untuk weekId
    fun currentWeekId(): String {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return monday.toString()
    }

    // Label UI: "16 Dec 2025 – 22 Dec 2025"
    fun getCurrentWeekRange(): String {
        val now = LocalDate.now()
        val start = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")
        return "${start.format(formatter)} – ${end.format(formatter)}"
    }
}
