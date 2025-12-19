package ap.mobile.ecohabit.ui.eco_target

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ap.mobile.ecohabit.data.EcoTargetRestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EcoTargetViewModel : ViewModel() {

    private val repo = EcoTargetRestRepository

    private val _historyList = MutableStateFlow<List<EcoTargetRepository.WeeklyHistory>>(emptyList())
    val historyList: StateFlow<List<EcoTargetRepository.WeeklyHistory>> = _historyList.asStateFlow()

    private val _selectedHistory = MutableStateFlow<EcoTargetRepository.WeeklyHistory?>(null)
    val selectedHistory: StateFlow<EcoTargetRepository.WeeklyHistory?> = _selectedHistory.asStateFlow()
    private val _dailyCarbon =
        MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val dailyCarbon: StateFlow<List<Pair<String, Float>>> = _dailyCarbon

    private val _dailyQuiz =
        MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val dailyQuiz: StateFlow<List<Pair<String, Float>>> = _dailyQuiz

    private val _currentWeekHistory = MutableStateFlow<EcoTargetRepository.WeeklyHistory?>(null)
    val currentWeekHistory: StateFlow<EcoTargetRepository.WeeklyHistory?> = _currentWeekHistory.asStateFlow()

    val carbonTarget: Int get() = EcoTargetRepository.carbonWeeklyTarget
    val quizTarget: Int get() = EcoTargetRepository.quizWeeklyTarget
    val remainingDays: Long get() = EcoTargetRepository.remainingDays

    fun loadWeeklyHistory() {
        viewModelScope.launch {
            val data = repo.fetchWeeklyHistoryList()
            _historyList.value = data
            if (data.isNotEmpty()) _currentWeekHistory.value = data.first()
            loadDailyData()
        }
    }

    fun loadHistoryById(id: String) {
        viewModelScope.launch {
            val item = repo.fetchWeeklyHistoryById(id)
            _selectedHistory.value = item
        }
    }

    fun saveMotivation(id: String, note: String) {
        viewModelScope.launch {
            try {
                repo.updateMotivation(id, note)
                loadHistoryById(id)
                loadWeeklyHistory()
            } catch (e: Exception) {
                // handle error (log or expose state)
                e.printStackTrace()
            }
        }
    }

    fun syncTotals(weekId: String) {
        viewModelScope.launch {
            repo.updateWeeklyTotalsFromDaily(weekId)
            loadHistoryById(weekId)
            loadWeeklyHistory()
        }
    }


    // EcoTargetViewModel.kt
    fun loadDailyData() {
        viewModelScope.launch {
            val weekId = _currentWeekHistory.value?.id ?: return@launch
            val daily = repo.listDailyByWeek(weekId)

            val sorted = daily.sortedBy { it.dateId }

            _dailyCarbon.value = sorted.map {
                it.dateId to it.carbon
            }

            _dailyQuiz.value = sorted.map {
                it.dateId to it.quiz.toFloat()
            }

        }
    }
}
