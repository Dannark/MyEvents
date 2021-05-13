package com.dannark.myevents.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dannark.myevents.domain.Event
import com.dannark.myevents.network.CheckInNetwork
import com.dannark.myevents.repository.events.FakeEventRepository
import com.dannark.myevents.ui.checkin.CheckinViewModel
import com.dannark.myevents.ui.events.EventsViewModel
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.apache.tools.ant.taskdefs.condition.IsTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckinViewModelTest {

    private lateinit var eventRepository: FakeEventRepository
    private lateinit var checkinViewModel: CheckinViewModel

    val event1 = Event(1534784400, "description1", "","-51.2146267", "-30.0392981",19.99.toLong(), "test1", "1")

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        eventRepository = FakeEventRepository()
        eventRepository.addEventsHelper(event1)

        checkinViewModel = CheckinViewModel(eventRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun postCheckIn_receiveGoodAnswer200(){
        checkinViewModel.postCheckIn(event1.id, "Tester Name", "test@mail.com")
        val isSuccessful = checkinViewModel.isCheckinSuccessful.getOrAwaitValue()
        val isLoading = checkinViewModel.loadingCheckInState.getOrAwaitValue()

        assertThat(isSuccessful, `is`(true))
        assertThat(isLoading, `is`(false))
    }

    @Test
    fun postCheckIn_receiveBadAnswer_edgeCase(){
        eventRepository.forceApiError = true
        checkinViewModel.postCheckIn(event1.id, "Tester Name", "test@mail.com")
        val isSuccessful = checkinViewModel.isCheckinSuccessful.getOrAwaitValue()

        eventRepository.forceApiError = false
        assertThat(isSuccessful, `is`(false))

        val isLoading = checkinViewModel.loadingCheckInState.getOrAwaitValue()
        assertThat(isLoading, `is`(false))
    }
}