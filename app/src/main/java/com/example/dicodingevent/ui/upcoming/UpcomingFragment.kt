package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.EventAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private lateinit var upcomingViewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeEvents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoading()

        upcomingViewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)
        setupRecyclerView()
        observeEvents()
        upcomingViewModel.fetchUpcomingEvents()
    }

    private fun setupLoading() {
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.apply {
                if (isLoading) {
                    pbLoading.visibility = View.VISIBLE
                    rvUpcomingEvents.visibility = View.GONE
                } else {
                    pbLoading.visibility = View.GONE
                    rvUpcomingEvents.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        eventAdapter = EventAdapter()
        binding.rvUpcomingEvents.adapter = eventAdapter
    }

    private fun observeEvents() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            if (events.isNotEmpty()) {
                eventAdapter.submitList(events)
            } else {
                Toast.makeText(requireContext(), "No upcoming events available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}