package com.dannark.myevents.ui.events

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.dannark.myevents.MyEventApp
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentEventsBinding
import com.dannark.myevents.repository.event.DefaultEventRepository
import com.dannark.myevents.util.setupRefreshLayout
import com.google.android.material.transition.MaterialElevationScale
import timber.log.Timber

class EventsFragment : Fragment() {

    private val viewModel by viewModels<EventsViewModel> {
        EventsViewModelFactory((requireContext().applicationContext as MyEventApp).eventRepository)
    }
    private lateinit var binding: FragmentEventsBinding
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentEventsBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this
        setupAdapters()
        setupObservableFields()
        setupRefreshLayout(binding.refreshLayout, binding.eventList)
    }

    private fun setupAdapters(){
        eventAdapter = EventAdapter(EventItemListener{ view, event ->
            val directions = EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(event)

            if(Build.VERSION.SDK_INT < 21){
                setupNavigationWithoutAnimation(directions)
            }
            else{
                setupNavigationWithAnimation(directions, view)
            }
        })
        binding.eventList.adapter = eventAdapter
    }

    private fun setupNavigationWithAnimation(directions: NavDirections, view: View){
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val cardDetailTransitionName = getString(R.string.card_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to cardDetailTransitionName)
        findNavController().navigate(directions, extras)
    }

    private fun setupNavigationWithoutAnimation(directions: NavDirections){
        findNavController().navigate(directions)
    }

    private fun setupObservableFields(){
        viewModel.eventList.observe(viewLifecycleOwner) {
            it.let {
                eventAdapter.addHeaderAndSubmitList(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}