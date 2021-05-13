package com.dannark.myevents.eventdetails

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.dannark.myevents.R
import com.dannark.myevents.ServiceLocator
import com.dannark.myevents.domain.Event
import com.dannark.myevents.repository.FakeAndroidTestEventRepository
import com.dannark.myevents.repository.event.EventRepository
import com.dannark.myevents.ui.checkin.CheckinFragment
import com.dannark.myevents.ui.eventdetails.EventDetailsFragment
import com.dannark.myevents.ui.eventdetails.EventDetailsFragmentArgs
import com.dannark.myevents.ui.eventdetails.EventDetailsFragmentDirections
import com.dannark.myevents.ui.events.EventsFragment
import com.dannark.myevents.ui.events.EventsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class EventDetailsFragmentTest {

    val eventSelected = Event(1534784400, "description1", "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png","-51.2146267", "-30.0392981",19.99.toLong(), "test1", "1")

    @Test
    fun activeTaskDetails_DisplayedInUi()  = runBlockingTest{
        launchFragmentInContainer<EventDetailsFragment>(bundleOf("eventSelected" to eventSelected))

        onView(withId(R.id.eventName)).check(matches(withText(eventSelected.title)))
        onView(withId(R.id.description)).check(matches(withText(eventSelected.description)))
        onView(withId(R.id.event_detail_price)).check(matches(withText(eventSelected.price_formatted)))
    }

    @Test
    fun clickCheckin_navigateCheckinFragment(){
        //checkin_button
        val navController = Mockito.mock(NavController::class.java)
        val scenario = launchFragmentInContainer<EventDetailsFragment>(bundleOf("eventSelected" to eventSelected))

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.checkin_button)).perform(click())

        Mockito.verify(navController).navigate(
            EventDetailsFragmentDirections.actionEventDetailsFragmentToCheckinFragment(eventSelected)
        )
    }

    @Test
    fun clickToNavigateUp_navigateBackToEventFragment() {
        launchFragmentInContainer<EventDetailsFragment>(bundleOf("eventSelected" to eventSelected))

        val navController = Mockito.mock(NavController::class.java)
        navController.navigateUp()

        Mockito.verify(navController).navigateUp()
    }
}