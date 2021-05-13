package com.dannark.myevents.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dannark.myevents.domain.Event
import com.dannark.myevents.repository.events.FakeEventRepository
import com.dannark.myevents.ui.events.EventsViewModel
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {

    private lateinit var eventRepository: FakeEventRepository
    private lateinit var eventViewModel: EventsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        eventRepository = FakeEventRepository()
        val event1 = Event(1534784400, "description1", "","-51.2146267", "-30.0392981",19.99.toLong(), "test1", "1")
        val event2 = Event(1534784400, "description2", "","-51.2146267", "-30.0392981",29.99.toLong(), "test2", "2")
        val event3 = Event(1534784400, "description3", "","-51.2146267", "-30.0392981",39.99.toLong(), "test3", "3")
        eventRepository.addEventsHelper(event1, event2, event3)

        eventViewModel = EventsViewModel(eventRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun loadEvents_onInitialization(){
        val value = eventViewModel.eventList.getOrAwaitValue()
        assertThat(value, not(nullValue()))
    }

}