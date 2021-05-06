package com.dannark.myevents.ui.checkin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentCheckinBinding
import com.dannark.myevents.domain.Event
import com.dannark.myevents.ui.eventdetails.EventDetailViewModel
import com.dannark.myevents.ui.eventdetails.EventDetailsFragmentArgs
import com.dannark.myevents.util.themeColor
import com.google.android.material.transition.MaterialContainerTransform

class CheckinFragment : Fragment() {
    private lateinit var binding: FragmentCheckinBinding
    private lateinit var viewModel: CheckinViewModel
    private lateinit var eventSelected: Event

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentCheckinBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(CheckinViewModel::class.java)
        binding.viewModel = viewModel

        eventSelected = EventDetailsFragmentArgs.fromBundle(requireArguments()).eventSelected

        setOnClickToSendHandler()
        setObservableFields()

        return binding.root
    }

    private fun setOnClickToSendHandler(){
        binding.sendButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()

            if(validateFields()){
                viewModel.postCheckIn(eventSelected.id, name, email)
            }
        }
    }

    private fun validateFields():Boolean{
        val isNameValid = viewModel.isNameFieldValid(binding.name.text.toString()).let {
            if (!it) binding.name.error = getString(R.string.error_invalid_name)
            it
        }
        val isEmailValid = viewModel.isValidEmailField(binding.email.text.toString()).let {
            if (!it) binding.email.error = getString(R.string.error_invalid_email)
            it
        }
        return isNameValid && isEmailValid
    }

    private fun setObservableFields(){
        viewModel.loadingCheckInState.observe(viewLifecycleOwner) {
            if(!it){
                val success = viewModel.isCheckinSuccessful.value?:false
                val res = if(success) R.string.is_checkin_successful else R.string.is_checkin_unsuccessful
                Toast.makeText(requireContext(), getString(res), Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run{
            enterTransition = MaterialContainerTransform().apply {
                startView = requireActivity().findViewById(R.id.checkin_button)
                endView = postCheckin
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                scrimColor = Color.TRANSPARENT
                containerColor = requireContext().themeColor(R.attr.colorSurface)
                startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
                endContainerColor = requireContext().themeColor(R.attr.colorSurface)
            }
            returnTransition = Slide().apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_medium).toLong()
                addTarget(R.id.postCheckin)
            }
        }
    }
}