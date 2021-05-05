package com.dannark.myevents.ui.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentEventsBinding
import com.dannark.myevents.util.setupRefreshLayout
import com.google.android.material.transition.MaterialElevationScale

class EventsFragment : Fragment() {

    private lateinit var binding: FragmentEventsBinding
    private lateinit var viewModel: EventsViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentEventsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(EventsViewModel::class.java)
        binding.viewModel = viewModel

        setupAdapters()
        setupObservableFields()
        setupRefreshLayout(binding.refreshLayout, binding.eventList)

        return binding.root
    }

    private fun setupAdapters(){
        eventAdapter = EventAdapter(EventItemListener{ view, event ->
            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            val cardDetailTransitionName = getString(R.string.card_detail_transition_name)
            val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
            val directions = EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(event)
            findNavController().navigate(directions, extras)
        })
        binding.eventList.adapter = eventAdapter
    }

    private fun setupObservableFields(){
        viewModel.eventList.observe(viewLifecycleOwner) {
            it?.let {
                eventAdapter.addHeaderAndSubmitList(it)
            }
        }
    }

    private fun setupSnackbar() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Render Back transition animation for Places Details Frag to -> this fragment
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}