package com.example.dicodingevent

import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.example.dicodingevent.ui.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup StrictMode untuk debugging
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Setup RecyclerView
        eventAdapter = EventAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }

        // Observe loading state
        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error message
        mainViewModel.errorMessage.observe(this) { errorMessage ->
            binding.tvError.visibility = if (errorMessage != null) View.VISIBLE else View.GONE
            binding.tvError.text = errorMessage
        }

        // Observe list of events
        mainViewModel.listEvents.observe(this) { events ->
            eventAdapter.submitList(events)
            binding.tvError.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        }

        // Setup SearchView for event search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mainViewModel.searchEvents(it) // Gunakan ViewModel untuk mencari event berdasarkan query
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}
