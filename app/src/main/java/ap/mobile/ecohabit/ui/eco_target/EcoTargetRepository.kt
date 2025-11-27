package ap.mobile.ecohabit.ui.eco_target

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object EcoTargetRepository {
    data class WeeklyHistory(
        val id: String,
        val weekLabel: String,
        val carbonTarget: Int,
        val quizTarget: Int,
        val carbonTotal: Float,
        val quizTotal: Float,
        val carbonAchieved: Boolean,
        val quizAchieved: Boolean,
        val improvement: Int,
        var motivation: String = ""
    )
    fun getHistoryById(id: String): WeeklyHistory? {
        return weeklyHistoryList.firstOrNull { it.id == id }
    }
    private val today = LocalDate.now()
    val endOfWeek = today.with(DayOfWeek.SUNDAY)

    val remainingDays = ChronoUnit.DAYS.between(today, endOfWeek)

    // Generate list 7 hari terakhir
    val weekDates: List<LocalDate> =
        (0..6).map { today.minusDays((6 - it).toLong()) }

    // Dummy daily carbon usage (kg)
    val carbonDailyData = listOf(5f, 3f, 4f, 3f, 2f, 2f, 4f)

    // Dummy daily quiz points
    val quizDailyData = listOf(10f, 20f, 15f, 25f, 30f, 40f, 35f)

    // Targets
    const val carbonWeeklyTarget = 25
    const val quizWeeklyTarget = 200

    val orderedWeekDates = weekDates.sortedBy { it.dayOfWeek.value }

    val carbonTotall = carbonDailyData.sum()
    val quizTotall = quizDailyData.sum()

    val weekDayLabels = orderedWeekDates.map {
        it.dayOfWeek.name.take(3)  // Mon, Tue, ...
    }
    fun generateWeekLabel(startDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        val endDate = startDate.plusDays(6)

        return "${startDate.format(formatter)} - ${endDate.format(formatter)}"
    }



    //dummyHistory
    val weeklyHistoryList = listOf(
        WeeklyHistory(
            id = "1",
            weekLabel = "Minggu 18–24 Nov 2025",
            carbonTarget = carbonWeeklyTarget,
            quizTarget = quizWeeklyTarget,
            carbonTotal = carbonTotall,
            quizTotal = quizTotall,
            carbonAchieved = carbonTotall <= carbonWeeklyTarget,
            quizAchieved = quizTotall >= quizWeeklyTarget,
            improvement = 8
        ),
        WeeklyHistory(
            id = "2",
            weekLabel = "Minggu 11–17 Nov 2025",
            carbonTarget = carbonWeeklyTarget,
            quizTarget = quizWeeklyTarget,
            carbonTotal = carbonTotall,
            quizTotal = quizTotall,
            carbonAchieved = carbonTotall <= carbonWeeklyTarget,
            quizAchieved = quizTotall >= quizWeeklyTarget,
            improvement = 8
        ),
        WeeklyHistory(
            id = "3",
            weekLabel = "Minggu 4–10 Nov 2025",
            carbonTarget = carbonWeeklyTarget,
            quizTarget = quizWeeklyTarget,
            carbonTotal = carbonTotall,
            quizTotal = quizTotall,
            carbonAchieved = carbonTotall <= carbonWeeklyTarget,
            quizAchieved = quizTotall >= quizWeeklyTarget,
            improvement = 8
        ),
        WeeklyHistory(
            id = "4",
            weekLabel = "Minggu 28 Okt–3 Nov 2025",
            carbonTarget = carbonWeeklyTarget,
            quizTarget = quizWeeklyTarget,
            carbonTotal = carbonTotall,
            quizTotal = quizTotall,
            carbonAchieved = carbonTotall <= carbonWeeklyTarget,
            quizAchieved = quizTotall >= quizWeeklyTarget,
            improvement = 8
        )
    )

}
