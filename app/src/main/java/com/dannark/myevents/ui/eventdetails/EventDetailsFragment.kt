package com.dannark.myevents.ui.eventdetails

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentEventDetailsBinding
import com.dannark.myevents.util.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

class EventDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private lateinit var viewModel: EventDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentEventDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val eventSelected = EventDetailsFragmentArgs.fromBundle(requireArguments()).eventSelected
        val viewModelFactory = EventDetailViewModelFactory(eventSelected, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EventDetailViewModel::class.java)
        binding.viewModel = viewModel

        setupObservableFields()

        return binding.root
    }

    fun setupObservableFields(){
        viewModel.onCheckinNavigationEvent.observe(viewLifecycleOwner){
            if(it){
                Log.e("EventDetailsFrag","emited")
                navigateToCheckin()
                viewModel.onCheckinNavigationCompleted()
            }
        }
    }

    fun navigateToCheckin(){
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        findNavController().navigate(
            EventDetailsFragmentDirections.actionEventDetailsFragmentToCheckinFragment())
    }
}