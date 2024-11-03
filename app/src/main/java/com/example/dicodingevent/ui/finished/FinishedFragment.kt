package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.EventAdapter
import com.example.dicodingevent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private lateinit var finishedViewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)

        setupRecyclerView()
        observeEvents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoading()

        finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)
        setupRecyclerView()
        observeEvents()
        finishedViewModel.fetchFinishedEvents()
    }

    private fun setupLoading() {
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.apply {
                if (isLoading) {
                    pbLoading.visibility = View.VISIBLE
                    rvFinishedEvents.visibility = View.GONE
                } else {
                    pbLoading.visibility = View.GONE
                    rvFinishedEvents.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        eventAdapter = EventAdapter()
        binding.rvFinishedEvents.adapter = eventAdapter
    }

    private fun observeEvents() {
        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            if (events.isNotEmpty()) {
                eventAdapter.submitList(events)
            } else {
                Toast.makeText(requireContext(), "No finished events available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
