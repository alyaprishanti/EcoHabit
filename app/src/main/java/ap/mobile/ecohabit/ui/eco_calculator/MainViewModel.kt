package ap.mobile.ecohabit.ui.eco_calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _history = MutableStateFlow<List<EmissionResult>>(emptyList())
    val history: StateFlow<List<EmissionResult>> = _history

    private val _latest = MutableStateFlow<EmissionResult?>(null)
    val latest: StateFlow<EmissionResult?> = _latest

    // Listener registrations - to remove when ViewModel cleared
    private var latestListener: ListenerRegistration? = null
    private var historyListener: ListenerRegistration? = null

    init {
        startListening()
    }

    private fun startListening() {
        // listen latest
        latestListener = repo.listenLatest { latestResult ->
            viewModelScope.launch {
                _latest.value = latestResult
            }
        }

        // listen history
        historyListener = repo.listenHistory { list ->
            viewModelScope.launch {
                _history.value = list
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        latestListener?.remove()
        historyListener?.remove()
    }

    fun saveResult(result: EmissionResult) {
        // Save locally quickly (optional) then push to Firestore
        _latest.value = result
        _history.value = listOf(result) + _history.value

        // Push to Firestore
        repo.saveResult(result) { success, message ->
            if (!success) {
                // optional: log or handle failure
                // (we don't change UI here; you can expand later)
                println("Failed saving to Firestore: $message")
            }
        }
    }

    fun getLatest(): EmissionResult? = _latest.value

    fun createDateNow(): String {
        val now = LocalDateTime.now()
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return now.format(fmt)
    }
}
