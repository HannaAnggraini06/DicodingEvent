package com.example.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    init {
        fetchFinishedEvents()
    }

    private fun fetchFinishedEvents() {
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(active = 0)
                if (response.isSuccessful && response.body() != null) {
                    _finishedEvents.value = response.body()?.listEvents ?: listOf()
                } else {
                    Log.e("FinishedViewModel", "Failed to fetch finished events: ${response.message()}")
                    _finishedEvents.value = listOf()
                }
            } catch (e: Exception) {
                Log.e("FinishedViewModel", "Exception during fetch: ${e.message}")
                _finishedEvents.value = listOf() // Clear list on failure
            }
        }
    }
}
