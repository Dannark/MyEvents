package com.dannark.myevents.ui.checkin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import com.dannark.myevents.R
import com.dannark.myevents.databinding.FragmentCheckinBinding
import com.dannark.myevents.ui.eventdetails.EventDetailViewModel
import com.dannark.myevents.util.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_checkin.*

class CheckinFragment : Fragment() {
    private lateinit var binding: FragmentCheckinBinding
    private lateinit var viewModel: CheckinViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentCheckinBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(CheckinViewModel::class.java)
        binding.viewModel = viewModel

        setOnClickToSendHandler()
        setObservableFields()

        return binding.root
    }

    fun setOnClickToSendHandler(){
        binding.sendButton.setOnClickListener {
            val eventId = "0"
            val name = binding.name
            val email = binding.email

            val isNameValid = viewModel.isNameFieldValid(name.text.toString()).let {
                if (!it) binding.name.error = getString(R.string.error_invalid_name)
                it
            }
            val isEmailValid = viewModel.isValidEmailField(email.text.toString()).let {
                if (!it) binding.email.error = getString(R.string.error_invalid_email)
                it
            }
            if(isNameValid && isEmailValid){
                viewModel.postCheckIn(eventId, name.toString(), email.toString())
            }
        }

    }

    fun setObservableFields(){
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