package com.dannark.myevents.event

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.dannark.myevents.R
import com.dannark.myevents.ServiceLocator
import com.dannark.myevents.domain.Event
import com.dannark.myevents.repository.FakeAndroidTestEventRepository
import com.dannark.myevents.repository.event.EventRepository
import com.dannark.myevents.ui.events.EventsFragment
import com.dannark.myevents.ui.events.EventsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class EventFragmentTest {

    private lateinit var repository: FakeAndroidTestEventRepository

    @Before
    fun initRepository(){
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = FakeAndroidTestEventRepository()
        ServiceLocator.eventRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest{
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickEvent_navigateToDetailFragment() = runBlockingTest {
        val event1 = Event(1534784400, "description1", "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png","-51.2146267", "-30.0392981",19.99.toLong(), "test1", "1")
        val event2 = Event(1534784400, "description2", "http://www.fernaogaivota.com.br/documents/10179/1665610/feira-troca-de-livros.jpg","-51.2146267", "-30.0392981",29.99.toLong(), "test2", "2")

        repository.addEventsHelper(event1, event2)

        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer<EventsFragment>()

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.event_list)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("test1")), click())
        )

        if(Build.VERSION.SDK_INT < 21) {
            verify(navController).navigate(
                EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(event1)
            )
        }
        // In order to test on SDK >= 21 it's necessary to pass an extra argument (target view)
        // So it is would need a different approach in order to test it
    }
}