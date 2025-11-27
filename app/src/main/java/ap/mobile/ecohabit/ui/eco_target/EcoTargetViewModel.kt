package ap.mobile.ecohabit.ui.eco_target

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EcoTargetViewModel : ViewModel() {

    private val repository = EcoTargetRepository
    private val _currentWeekHistory =
        MutableStateFlow(repository.weeklyHistoryList.first())
    val currentWeekHistory: StateFlow<EcoTargetRepository.WeeklyHistory>
        get() = _currentWeekHistory.asStateFlow()
    val carbonTarget: Int
        get() = repository.carbonWeeklyTarget

    val quizTarget: Int
        get() = repository.quizWeeklyTarget
    val carbonDailyData: List<Float>
        get() = repository.carbonDailyData

    val quizDailyData: List<Float>
        get() = repository.quizDailyData
    val remainingDays: Long
        get() = repository.remainingDays
    val currentWeekRange: String
        get() = DateUtils.getCurrentWeekRange()



    // Live state untuk list history
    private val _historyList = MutableStateFlow(repository.weeklyHistoryList)
    val historyList: StateFlow<List<EcoTargetRepository.WeeklyHistory>> = _historyList.asStateFlow()

    // Current selected history (untuk WeeklyResultScreen)
    private val _selectedHistory = MutableStateFlow<EcoTargetRepository.WeeklyHistory?>(null)
    val selectedHistory: StateFlow<EcoTargetRepository.WeeklyHistory?> = _selectedHistory.asStateFlow()

    fun loadHistoryById(id: String) {
        _selectedHistory.value = repository.getHistoryById(id)
    }
    fun getHistoryById(id: String): EcoTargetRepository.WeeklyHistory? {
        return repository.weeklyHistoryList.firstOrNull { it.id == id }
    }
}
