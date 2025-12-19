package ap.mobile.ecohabit.ui.eco_target

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object EcoTargetRepository {

    data class WeeklyHistory(
        val id: String = "",
        val weekLabel: String = "",
        val carbonTarget: Int = 0,
        val quizTarget: Int = 0,
        val carbonTotal: Float = 0f,
        val quizTotal: Float = 0f,
        val carbonAchieved: Boolean = false,
        val quizAchieved: Boolean = false,
        val improvement: Int = 0,
        val motivation: String = ""
    )

    data class DailyData(
        val dateId: String = "", // "2025-12-02"
        val carbon: Float = 0f,
        val quiz: Int = 0,
        val weekId: String = ""
    )


    private val today = LocalDate.now()
    val endOfWeek = today.with(DayOfWeek.SUNDAY)
    val remainingDays = ChronoUnit.DAYS.between(today, endOfWeek)

    val weekDates: List<LocalDate> =
        (0..6).map { today.minusDays((6 - it).toLong()) }


    const val carbonWeeklyTarget = 25
    const val quizWeeklyTarget = 200

    val orderedWeekDates = weekDates.sortedBy { it.dayOfWeek.value }


}