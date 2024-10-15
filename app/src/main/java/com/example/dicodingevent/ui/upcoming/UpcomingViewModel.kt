package com.example.dicodingevent.ui.upcoming

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

class UpcomingViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    init {
        fetchUpcomingEvents()
    }

    private fun fetchUpcomingEvents() {
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getEvents(active = 1)
                if (response.isSuccessful && response.body() != null) {
                    _upcomingEvents.value = response.body()?.listEvents ?: listOf()
                } else {
                    Log.e("UpcomingViewModel", "Failed to fetch upcoming events: ${response.message()}")
                    _upcomingEvents.value = listOf()
                }
            } catch (e: Exception) {
                Log.e("UpcomingViewModel", "Exception during fetch: ${e.message}")
                _upcomingEvents.value = listOf() // Clear list on failure
            }
        }
    }
}
