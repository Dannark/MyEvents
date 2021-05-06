package com.dannark.myevents.ui.eventdetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentEventDetailsBinding
import com.dannark.myevents.domain.Event
import com.dannark.myevents.util.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

class EventDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private lateinit var viewModel: EventDetailViewModel
    private lateinit var eventSelected: Event

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
        eventSelected = EventDetailsFragmentArgs.fromBundle(requireArguments()).eventSelected
        val viewModelFactory = EventDetailViewModelFactory(eventSelected, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EventDetailViewModel::class.java)
        binding.viewModel = viewModel

        setupObservableFields()
        setupShareButton()

        return binding.root
    }

    private fun setupObservableFields(){
        viewModel.onCheckinNavigationEvent.observe(viewLifecycleOwner){
            if(it){
                navigateToCheckin()
                viewModel.onCheckinNavigationCompleted()
            }
        }
    }

    private fun navigateToCheckin(){
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        findNavController().navigate(
            EventDetailsFragmentDirections.actionEventDetailsFragmentToCheckinFragment(eventSelected))
    }

    private fun setupShareButton(){
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.event_detail_menu, menu)
        // Avoid crashes if there are no compatible activities available to be shared
        // by checking if activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            menu?.findItem(R.id.share)?.setVisible(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item?.let {
            when(it.itemId){
                R.id.share -> shareEvent()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareEvent(){
        startActivity(getShareIntent())
    }

    private fun getShareIntent() : Intent {
        val location = "https://www.google.com/maps/dir/?api=1&travelmode=driving&layer=traffic&destination=${eventSelected.latitude},${eventSelected.longitude}"
        return ShareCompat.IntentBuilder.from(requireActivity())
                .setText(getString(R.string.share_event,
                    eventSelected.title,
                    eventSelected.description,
                    eventSelected.price_formatted,
                    location,
                    "https://myevents.app"))
                .setType("text/plain")
                .intent
    }
}