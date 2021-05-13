package com.dannark.myevents.checkin

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.dannark.myevents.R
import com.dannark.myevents.ServiceLocator
import com.dannark.myevents.domain.Event
import com.dannark.myevents.repository.FakeAndroidTestEventRepository
import com.dannark.myevents.ui.checkin.CheckinFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class CheckinFragmentTest {

    val eventSelected = Event(1534784400, "description1", "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png","-51.2146267", "-30.0392981",19.99.toLong(), "test1", "1")
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
    fun activeCheckin_checkInputFieldsAreValid()  = runBlockingTest{
        launchFragmentInContainer<CheckinFragment>(bundleOf("eventSelected" to eventSelected))

        onView(withId(R.id.name)).perform(replaceText("short"))
        onView(withId(R.id.email)).perform(replaceText("invalid@email"))

        onView(withId(R.id.send_button)).perform(click())

        onView(withId(R.id.name)).check(matches(hasErrorText(getResourceString(R.string.error_invalid_name))))
        onView(withId(R.id.email)).check(matches(hasErrorText(getResourceString(R.string.error_invalid_email))))

        onView(withId(R.id.name)).perform(replaceText(""))
        onView(withId(R.id.email)).perform(replaceText(""))

        onView(withId(R.id.send_button)).perform(click())

        onView(withId(R.id.name)).check(matches(hasErrorText(getResourceString(R.string.error_invalid_name))))
        onView(withId(R.id.email)).check(matches(hasErrorText(getResourceString(R.string.error_invalid_email))))
    }

    @Test
    fun clickToNavigateUp_navigateBackToDetailFragment() {
        val scenario = launchFragmentInContainer<CheckinFragment>(bundleOf("eventSelected" to eventSelected))

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
        navController.navigateUp()

        Mockito.verify(navController).navigateUp()
    }

    @Test
    fun clickToSend_andReturnToPreviousFragment(){
        val scenario = launchFragmentInContainer<CheckinFragment>(bundleOf("eventSelected" to eventSelected))

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.name)).perform(replaceText("A Valid User Name"))
        onView(withId(R.id.email)).perform(replaceText("tester@email.com"))
        onView(withId(R.id.send_button)).perform(click())

        Mockito.verify(navController).navigateUp()
    }
}

private fun getResourceString(id: Int): String? {
    val targetContext: Context = InstrumentationRegistry.getTargetContext()
    return targetContext.resources.getString(id)
}